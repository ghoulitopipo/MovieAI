import ApiBackend
import math
import time
import numpy as np
from sklearn.metrics.pairwise import cosine_similarity

movies_id_dict = {}
movies_index_dict = {}

def get_movies_id_dict():
    """
    Crée et retourne deux dictionnaires stockant les ID des films et leurs indices de la table movies.
    
    ID MovieLens = de 1 à 193 000 environ
    indices (numéro de ligne) = de 0 à 9 000 environ

    movies_id_dict stocke les indices de ligne en clés et les ID MovieLens en valeur
    movies_index_dict stocke les ID MovieLens en clés et les indices de ligne en valeur

    Cela permet de créer une matrice utilisateur/film avec seulement les films qui ont été notés.
    """
    movies_id_dict = {}
    movies_index_dict = {}
    for i in range(movies_count):
        movie_id = int(movies_data[i, 0])
        movies_id_dict[i] = movie_id
        movies_index_dict[movie_id] = i
    return movies_id_dict, movies_index_dict

def create_user_item_matrix(null_values=True):
    """
    Crée une matrice utilisateur/film à partir de la table ratings.
    """
    user_rating_matrix = np.zeros((users_count, movies_count))

    for i in range(ratings_count):
        user_id = int(ratings_data[i, 0])
        movie_id = int(ratings_data[i, 1])
        rating = ratings_data[i, 2]
        user_rating_matrix[user_id - 1, movies_index_dict[movie_id]] = rating

    # Remplir par des zéros ou des valeurs nulles pour les films sans note
    if null_values:
        user_rating_matrix[user_rating_matrix == 0] = np.nan
    else:
        user_rating_matrix = np.nan_to_num(user_rating_matrix)
    return user_rating_matrix

def recommend_movies(ratings, user_index, k=20, n_recommendations=50):
    # Si l'utilisteur a noté tous les films, on ne peut pas faire de recommandation
    if np.all(~np.isnan(ratings[user_index])):
        print("L'utilisateur a noté tous les films.")
        return []

    # Remplace NaNs par 0 pour le calcul de similarité (cosine)
    filled_ratings = np.nan_to_num(ratings)
    
    similarity = cosine_similarity(filled_ratings)
    
    # Calcule la similarité entre l'utilisateur à prédire et les autres utilisateurs
    sim_scores = similarity[user_index]
    
    # Trouve les k utilisateurs les plus similaires de l'utilisateur (lui-même exclu)
    similar_users = np.argsort(sim_scores)[::-1]
    similar_users = similar_users[similar_users != user_index][:k]
    
    # Prédictions des notes
    user_ratings = ratings[user_index]
    unrated_indices = np.where(np.isnan(user_ratings))[0]
    
    predicted_ratings = {}
    
    for movie_idx in unrated_indices:
        sim_sum = 0
        weighted_sum = 0
        for other_user in similar_users:
            rating = ratings[other_user, movie_idx]
            if not np.isnan(rating):
                sim = sim_scores[other_user]
                weighted_sum += sim * rating
                sim_sum += sim
        if sim_sum > 0:
            predicted_ratings[movie_idx] = weighted_sum / sim_sum

    # Recommande les n films avec la plus haute prédiction (avec l'id du film)
    recommended = sorted(predicted_ratings.items(), key=lambda x: x[1], reverse=True)[:n_recommendations]

    # Remplace les indices de ligne par les ID MovieLens des films
    recommended = [(movies_id_dict[idx], rating) for idx, rating in recommended]

    # Arrondir les notes à 2 chiffres après la virgule
    recommended = [(movie_id, round(rating, 2)) for movie_id, rating in recommended]
    
    return recommended

if __name__ == "__main__":
    start = time.time()

    # Chargement des données
    movies_data, ratings_data, tags_data = ApiBackend.get_all_data()
    ratings_count = len(ratings_data)
    users_count = len(np.unique(ratings_data[:, 0]))
    movies_count = len(np.unique(movies_data[:, 0]))
    max_movies_id = int(ratings_data[:, 1].max())

    # Création des dictionnaires pour les ID et indices des films
    movies_id_dict, movies_index_dict = get_movies_id_dict()

    # Création de la matrice utilisateur/film
    R = create_user_item_matrix(True)

    # Test de la fonction de recommandation

    # On choisit un utilisateur test
    # Pour l'utilisateur test, stocker dans une liste toutes ses notes avec l'indice du film dans R
    test_user = 0
    user_test_ratings = []
    for i in range(movies_count):
        if not np.isnan(R[test_user][i]):
            # append [indice du film dans R, note]
            user_test_ratings.append((i, R[test_user][i]))
    
    # Puis on enlève une partie des notes pour simuler un utilisateur qui n'a pas noté tous les films
    # Et on garde dans la liste seulement les notes "cachées"

    # Nombre de notes à enlever
    n_ratings_to_remove = int(len(user_test_ratings) * 0.1)

    # Indices des notes à enlever choisis aléatoirement
    indices_to_remove = np.random.choice(len(user_test_ratings), n_ratings_to_remove, replace=False)

    # On enlève les notes de l'utilisateur test dans la matrice utilisateur/film
    hidden_rating = []
    for idx in indices_to_remove:
        movie_idx = user_test_ratings[idx][0]
        R[test_user][movie_idx] = np.nan
        hidden_rating.append((movie_idx, user_test_ratings[idx][1]))
    
    # On affiche les notes cachées
    # print("Notes cachées de l'utilisateur test :")
    # for idx in indices_to_remove:
    #     print(f"Film {user_test_ratings[idx][0]} : {user_test_ratings[idx][1]}")

    # Recommandations pour l'utilisateur test
    reco = recommend_movies(R, user_index=test_user)
    print("Recommandations pour l'utilisateur test :")
    print(len(reco), "films recommandés")

    # Afficher les films recommandés avec la note réelle et le score de recommandation
    hidden_rating = {movie_id: rating for movie_id, rating in hidden_rating}

    # Afficher les films recommandés qui ne sont pas dans les notes cachées
    for movie_id, rating in reco:
        if movie_id not in hidden_rating:
            print(f"Film {movie_id} :       Note recommandée : {rating}")
    # Puis afficher les films recommandés qui sont dans les notes cachées
    for movie_id, rating in reco:
        if movie_id in hidden_rating:
            print(f"Film {movie_id} :       Note recommandée : {rating} ----- Note réelle : {hidden_rating[movie_id]}")

    # Compter puis afficher le nombre de recommandations pour chaque tranche de notes
    ratings_count = [0] * 6
    for movie_id, rating in reco:
        int_rating = math.floor(rating)
        ratings_count[int_rating] += 1
    print("Nombre de notes pour chaque recommandation :")
    for i in range(6):
        print(f"Notes à {i} : {ratings_count[i]}")

    end = time.time()
    print(f"Temps d'exécution : {end - start:.4f} secondes")


def launch(id_user):
    if id_user >= 1:
        u = id_user
    else:
        u = 0
    movies_data, ratings_data, tags_data = ApiBackend.get_all_data()
    ratings_count = len(ratings_data)
    users_count = len(np.unique(ratings_data[:, 0]))
    movies_count = len(np.unique(movies_data[:, 0]))
    max_movies_id = int(ratings_data[:, 1].max())
    movies_id_dict, movies_index_dict = get_movies_id_dict()
    R = create_user_item_matrix(True)
    recommendations = recommend_movies(R,u,20,50)

    output = [movie_id for movie_id in recommendations]

    return json.dumps(output, ensure_ascii=False, indent=4)
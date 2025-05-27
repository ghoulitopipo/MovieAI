"""
Module de test des algorithmes Python.
"""

import time
from utils import *
from IA_for_other import *
from IA_for_you import *
from IA_for_no_data import *

def measure_time(func, *args, **kwargs):
    """
    Appelle une méthode et renvoie son résultat et son temps d'exécution.
    """
    start_time = time.time()
    result = func(*args, **kwargs)
    end_time = time.time()
    elapsed_time = end_time - start_time
    print(f"Méthode :'{func.__name__}', temps d'exécution : {elapsed_time:.4f} secondes")
    return result, elapsed_time

def test_cosine_user():
    """
    Test de la version user-based de l'algorithme cosine_sim.
    """
    # On choisit un utilisateur test
    # Pour l'utilisateur test, stocker dans une liste toutes ses notes avec l'indice du film dans R
    test_user = 5
    user_test_ratings = []
    for i in range(movies_count):
        if not np.isnan(R[test_user][i]):
            # append [indice du film dans R, note]
            user_test_ratings.append((i, R[test_user][i]))
    
    # Puis on enlève une partie des notes pour simuler un utilisateur qui n'a pas noté tous les films
    # Et on garde dans la liste seulement les notes "cachées"

    n_ratings_to_remove = int(len(user_test_ratings) * 0.2)
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
    reco = cosine_user_recommend(test_user)
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
        int_rating = int(np.floor(rating))
        ratings_count[int_rating] += 1
    print("Nombre de notes pour chaque recommandation :")
    for i in range(6):
        print(f"Notes à {i} : {ratings_count[i]}")
    return -1

def test_cosine_content():
    """
    Test de la version item-based de l'algorithme cosine_sim.
    """
    # Calcul de la matrice de similarité
    sim_matrix = similarite_genre_tag()

    max_users_to_test = 10
    tested_users = 0
    min_ratings_required = 10
    test_fraction = 0.3
    n_recommendations = 1000  # max affiché dans le rang

    for test_user in range(1, users_count + 1):
        user_ratings = ratings_data[ratings_data[:, 0] == test_user]
        if len(user_ratings) < min_ratings_required:
            continue

        # Séparation en données visibles (train) et test
        np.random.seed(50)
        indices = np.random.permutation(len(user_ratings))
        cutoff = int(len(user_ratings) * (1 - test_fraction))
        train_indices = indices[:cutoff]
        test_indices = indices[cutoff:]

        user_train_ratings = user_ratings[train_indices]
        user_test_ratings = user_ratings[test_indices]

        # Masquage des notes test
        ratings_data_masked = ratings_data[
            ~((ratings_data[:, 0] == test_user) & np.isin(ratings_data[:, 1], user_test_ratings[:, 1]))
        ]

        # Recommandation
        reco = cosine_content_recommend_user(
            user_id=test_user,
            sim_matrix=sim_matrix,
            n_reco=n_recommendations
        )

        recommended_ids = [movie_id for movie_id, _, _ in reco]
        test_movie_ids = user_test_ratings[:, 1].astype(int)

        note_count, avg_rating = get_user_rating_stats(ratings_data, test_user)
        print(f"\nRésultats pour l'utilisateur {test_user} — {note_count} films notés | moyenne des notes : {avg_rating} | {len(user_test_ratings)} notes cachées")

        found = False
        for i, movie_id in enumerate(test_movie_ids):
            if movie_id in recommended_ids:
                rating = user_test_ratings[i, 2]
                title = movies_data[movies_index_dict[movie_id], 1] if movie_id in movies_index_dict else "Inconnu"
                rank = recommended_ids.index(movie_id) + 1
                score = [r[2] for r in reco if r[0] == movie_id][0]
                print(f"{title} (ID {movie_id}) — recommandé en position {rank} sur {n_recommendations} avec score {score:.2f} | note réelle : {rating}")
                found = True

        if not found:
            print("Aucun des films cachés n'a été recommandé.")

        tested_users += 1
        if tested_users >= max_users_to_test:
            break

def get_user_rating_stats(ratings_data, user_id):
    """
    Retourne le nombre de notes et la moyenne des notes de l'utilisateur.
    """
    user_ratings = ratings_data[ratings_data[:, 0] == user_id]
    if len(user_ratings) == 0:
        return 0, 0.0
    mean_rating = np.mean(user_ratings[:, 2])
    return len(user_ratings), round(mean_rating, 2)

if __name__ == "__main__":
    measure_time(test_cosine_user)
    #measure_time(test_cosine_content)
"""
Algorithmes de filtrage collaboratif et basé sur le contenu.
A son éxécution, le script crée les matrices de similarité :
    - sim_user : similarité entre les utilisateurs (basée sur les notes)
    - sim_movies : similarité entre les films (basée sur les genres et les tags)
"""
from utils import *
from sklearn.preprocessing import MultiLabelBinarizer
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity

def cosine_sim_user():
    """
    Calcule et retourne la similarité entre tous les utilisateurs basée uniquement sur leurs notes.
    
    Retourne une matrice carrée de taille users_count
    avec pour la case (i, j) la similarité entre l'utilisateur i et l'utilisateur j.
    """
    # Normalisation : note - moyenne de l'utilisateur
    rating_means = np.nanmean(R, axis=1)
    normalized_ratings = R - rating_means[:, np.newaxis]
    
    # Calcul de la similarité utilisateur-utilisateur
    filled_normalized = np.nan_to_num(normalized_ratings)
    sim_user = cosine_similarity(filled_normalized)

    return sim_user

def cosine_user_recommend(user_index, n_reco=50, k=20):
    """
    Algorithme CF user based basé uniquement sur les notes des films.
    La méthode retourne une liste de n films recommandés avec leurs notes prédites.

    L'algorithme trouve les k utilisateurs les plus similaires de l'utilisateur choisi en fonction des notes,
    puis fais une prédiction des notes en fonction de ce groupe d'utilisateurs.
    """
    sim_scores = sim_user[user_index]
    
    # Trouve les k utilisateurs les plus similaires de l'utilisateur (lui-même exclu)
    similar_users = np.argsort(sim_scores)[::-1]
    similar_users = similar_users[similar_users != user_index][:k]
    
    # Prédictions des notes
    user_ratings = R[user_index]
    unrated_indices = np.where(np.isnan(user_ratings))[0]

    predicted_ratings = {}

    for movie_idx in unrated_indices:
        sim_sum = 0
        weighted_sum = 0
        for other_user in similar_users:
            rating = R[other_user, movie_idx]
            if not np.isnan(rating):
                sim = sim_scores[other_user]
                weighted_sum += sim * rating
                sim_sum += sim
        if sim_sum > 0:
            predicted_ratings[movie_idx] = weighted_sum / sim_sum

    # Recommande les n films avec la plus haute prédiction
    recommended = sorted(predicted_ratings.items(), key=lambda x: x[1], reverse=True)[:n_reco]

    # Arrondie les notes et remplace les indices de ligne par les ID MovieLens des films
    recommended = [(movies_id_dict[idx], round(rating, 2)) for idx, rating in recommended]

    return recommended

def cosine_sim_genre():
    """
    Calcule et retourne la similarité entre tous les films basée uniquement sur leurs genres.

    Retourne une matrice carrée de taille movies_count
    avec pour la case (i, j) la similarité entre le film i et le film j.
    """
    genres_raw = movies_data[:, 2]
    genre_lists = [g.split('|') if g else [] for g in genres_raw]

    mlb = MultiLabelBinarizer()
    genre_matrix = mlb.fit_transform(genre_lists)
    sim_movies = cosine_similarity(genre_matrix)

    return sim_movies

def cosine_sim_tag():
    """
    Calcule et retourne la similarité entre tous les films basée uniquement sur leurs tags.

    Retourne une matrice carrée de taille movies_count
    avec pour la case (i, j) la similarité entre le film i et le film j.
    """
    # Construire une liste de tags combinés pour chaque film dans l'ordre de movies_data
    tag_dict = {}

    for row in tags_data:
        movie_id = int(row[1])
        tag_text = str(row[2])
        if movie_id not in tag_dict:
            tag_dict[movie_id] = []
        tag_dict[movie_id].append(tag_text)

    movie_ids = movies_data[:, 0].astype(int)
    tag_strings = []

    for mid in movie_ids:
        if mid in tag_dict:
            tag_string = ' '.join(tag_dict[mid])
        else:
            tag_string = ''
        tag_strings.append(tag_string)

    # Calcul de similarité
    vectorizer = TfidfVectorizer()
    tag_matrix = vectorizer.fit_transform(tag_strings)
    sim_tag = cosine_similarity(tag_matrix)

    return sim_tag

def similarite_genre_tag(alpha=0.65):
    """
    Calcule la similarité entre les films en fonction des genres et des tags.
    Les notes ne sont pas prises en compte.

    Retourne une matrice carrée de taille movies_count
    avec pour la case (i, j) la similarité entre le film i et le film j.

    La méthode calcule la similarité pour les genres et pour les tags séparément,
    puis calcule une moyenne pondérée entre les deux similarités.

    alpha = poids des genres dans la moyenne
    """
    # Calcul de similarité
    sim_genre = cosine_sim_genre()
    sim_tag = cosine_sim_tag()

    return alpha * sim_genre + (1 - alpha) * sim_tag

def cosine_content_recommend_similar(movie_id, sim_matrix, n_reco=30, min_rating=4.0):
    """
    Algorithme CB basé uniquement sur les genres des films.
    Retourne les n films les plus similaires au film choisi.
    """
    if movie_id not in movies_index_dict:
        print(f"Film {movie_id} introuvable.")
        return []

    idx = movies_index_dict[movie_id]
    similarities = sim_matrix[idx]
    
    # Exclure le film lui-même
    similarities[idx] = -1
    similarities = np.nan_to_num(similarities, nan=-1)
    top_indices = np.argsort(similarities)[::-1][:n_reco]
    recommendations = []

    for i in top_indices:
        mid = int(movies_data[i, 0])
        score = similarities[i]
        recommendations.append((mid, score))

    return recommendations

def cosine_content_recommend_user(user_id, sim_matrix, n_reco=30, min_rating=3.0):
    """
    Recommande n films à un utilisateur en se basant sur la version content-based
    de l'algorithme cosine similarity.
    """
    # Récupère les films notés par l'utilisateur
    user_ratings = ratings_data[ratings_data[:, 0] == (user_id + 1)]
    liked_movies = user_ratings[user_ratings[:, 2] >= min_rating]

    # Calcul du score de recommandation
    scores = np.zeros(sim_matrix.shape[0])
    for row in liked_movies:
        movie_id = int(row[1])
        rating = float(row[2])
        if movie_id in movies_index_dict:
            idx = movies_index_dict[movie_id]
            scores += sim_matrix[idx] * rating

    # Éviter les films déjà vus par l'utilisateur
    seen_movie_ids = user_ratings[:, 1].astype(int)
    seen_indices = [movies_index_dict[mid] for mid in seen_movie_ids if mid in movies_index_dict]
    scores[seen_indices] = -1  # on les exclut des recommandations

    # Recommander les n premiers films
    top_indices = np.argsort(scores)[::-1][:n_reco]

    # Convertit les indices en movieIds et titres
    recommendations = []
    for idx in top_indices:
        movie_id = int(movies_data[idx, 0])
        score = round(scores[idx], 2)
        recommendations.append((movie_id, score))

    return recommendations

def launch_U(id_user):
    u_id = id_user if id_user >= 1 else 0
    
    recommendations = cosine_user_recommend(u_id)
    output = [{"movie_id": movie_id, "score": score} for movie_id, score in recommendations]

    return output

def launch_M(id_movie):    
    m_id = id_movie if id_movie >= 1 else 0

    recommendations = cosine_content_recommend_similar(m_id, sim_genre_tag, n_reco=4)
    output = [{"movie_id": movie_id, "score": score} for movie_id, score in recommendations]

    return output

# Création des matrices de similarité
sim_user = cosine_sim_user()
sim_genre_tag = similarite_genre_tag()

if __name__ == "__main__":
    test_user_id = 0
    test_movie_id = 1

    user_recommendations = launch_U(test_user_id)
    print("Recommandations pour l'utilisateur:", user_recommendations)

    movie_recommendations = launch_M(test_movie_id)
    print("Recommandations pour le film:", movie_recommendations)
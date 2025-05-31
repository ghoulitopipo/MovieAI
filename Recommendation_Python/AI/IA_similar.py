"""
Module de recommandation basée sur le contenu.
A son éxécution, le script crée la matrice de similarité sim_movies,
qui indique la similarité entre les films (basée sur les genres et les tags seulement).
"""
from utils import *
from sklearn.preprocessing import MultiLabelBinarizer
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity

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
    Algorithme CB basé uniquement sur les genres et les tags des films.
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

def launch_M(id_movie):    
    m_id = id_movie if id_movie >= 1 else 0

    recommendations = cosine_content_recommend_similar(m_id, sim_genre_tag, n_reco=4)
    output = [{"movie_id": movie_id, "score": score} for movie_id, score in recommendations]

    return output

# Création de la matrice de similarité
sim_genre_tag = similarite_genre_tag()

if __name__ == "__main__":
    test_movie_id = 1
    movie_recommendations = launch_M(test_movie_id)
    print("Recommandations pour le film:", movie_recommendations)
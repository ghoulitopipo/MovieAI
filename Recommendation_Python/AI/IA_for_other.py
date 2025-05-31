"""
Algorithmes de filtrage collaboratif.
A son éxécution, le script crée la matrice de similarité sim_user,
qui indique la similarité entre les utilisateurs (basée sur les notes seulement).
"""


import numpy as np
import ApiBackend
import utils
from sklearn.preprocessing import MultiLabelBinarizer
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity

def sgd_als(num_factors, learning_rate, regularization, iterations=10):
    """
    Factorisation matricielle avec ALS et SGD.

    num_factors: Number of latent factors to use.
    Higher values can capture more nuanced patterns but risk overfitting.
    regularization: Helps prevent overfitting by penalizing large parameter values.
    """
    rmse_list = []
    mae_list = []
    
    # Initialize user and item latent factor matrices with small random values
    user_factors = np.random.normal(scale=1./num_factors, size=(users_count, num_factors))
    item_factors = np.random.normal(scale=1./num_factors, size=(movies_count, num_factors))
    
    for iteration in range(iterations):
        total_rmse_error = 0
        total_mae_error = 0
        count = 0
        for u in range(users_count):
            for i in range(users_count):
                if R[u, i] > 0:
                    # Calcul de l'erreur entre la note réelle et la prédiction
                    error = R[u, i] - np.dot(user_factors[u, :], item_factors[i, :].T)
                    total_rmse_error += error**2
                    total_mae_error += np.abs(error)

                    # Mise à jour des features
                    user_factors[u, :] += learning_rate * (error * item_factors[i, :] - regularization * user_factors[u, :])
                    item_factors[i, :] += learning_rate * (error * user_factors[u, :] - regularization * item_factors[i, :])
        
                    count += 1

        # Calcul de la RMSE et de la MAE
        # rmse = np.sqrt(total_rmse_error / np.count_nonzero(R))
        # mae = total_mae_error / np.count_nonzero(R)

        rmse = np.sqrt(total_rmse_error / count)
        mae = total_mae_error / count

        rmse_list.append(rmse)
        mae_list.append(mae)

    return user_factors, item_factors, rmse_list, mae_list

def get_matching_movies(user_id, U, V):
    """
    Retourne les films avec les features recherchées par l'utilisateur.
    """
    # Pour chaque film, calculer le produit scalaire entre le vecteur utilisateur et le vecteur item du film
    similarities = np.zeros(V.shape[0])
    for i in range(V.shape[0]):
        similarities[i] = np.dot(U[user_id], V[i])

    # Pour chaque film, si le film a été noté par l'utilisateur, on le met à -1 pour ne pas le recommander
    for i in range(R.shape[1]):
        if R[user_id, i] > 0:
            similarities[i] = 0

    # Liste des indices des films triés par pertinence avec l'utilisateur
    # similarities = np.argsort(similarities)[::-1]

    # Ensuite on remplace les indices par les ID des films dans la table movies
    # for i in range(len(similarities)):
    #     similarities[i] = movies_id_dict[similarities[i]]

    return similarities

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

def cosine_user_recommend(user_index, k=20):
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
    predicted_ratings = np.zeros(movies_count)

    for id in unrated_indices:
        sim_sum = 0
        weighted_sum = 0
        for other_user in similar_users:
            rating = R[other_user, id]
            if not np.isnan(rating):
                sim = sim_scores[other_user]
                weighted_sum += sim * rating
                sim_sum += sim
        if sim_sum > 0:
            predicted_ratings[id] = round(weighted_sum / sim_sum, 2)

    # Recommande les n films avec la plus haute prédiction
    # recommended = sorted(predicted_ratings.items(), key=lambda x: x[1], reverse=True)[:n_reco]

    # Arrondie les notes et remplace les indices de ligne par les ID MovieLens des films
    recommended = [(utils.movies_id_dict[idx], round(rating, 2)) for idx, rating in recommended]


    return predicted_ratings


def launch_U(id_user, n_reco=50):
    u_id = id_user if id_user >= 1 else 0

    recommendations = cosine_user_recommend(u_id)
    matching_movies = get_matching_movies(id_user, U, V)

# Création de la matrice de similarité
sim_user = cosine_sim_user()

U, V, rmse_list, mae_list = sgd_als(num_factors=10, learning_rate=0.01, regularization=0.01, iterations=10)

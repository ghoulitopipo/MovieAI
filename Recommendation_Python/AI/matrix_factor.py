from read_data import *
import numpy as np
import time

movies_id_dict = {}
movies_index_dict = {}

def get_movies_id_dict():
    """
    Crée et retourne deux dictionnaires stockant les ID des films et leurs indices de la table movies.
    
    ID MovieLens = de 1 à 193 000 environ
    indices (numéro de ligne) = de 0 à 9 000 environ

    movies_id_dict stocke les indices de ligne en clés et les ID MovieLens en valeur
    movies_index_dict stocke les ID MovieLens en clés et les indices de ligne en valeur
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
    #user_rating_matrix = np.zeros((users_count, total_movies_count))
    user_rating_matrix = np.zeros((users_count, movies_count))

    for i in range(ratings_count):
        #print(f"i : {i} --- user_id : {ratings_data[i, 0]} --- movie_id : {ratings_data[i, 1]} --- rating : {ratings_data[i, 2]}, --- movies_index_dict : {movies_index_dict[ratings_data[i, 1]]}")

        user_id = int(ratings_data[i, 0])
        movie_id = int(ratings_data[i, 1])
        rating = ratings_data[i, 2]
        user_rating_matrix[user_id - 1, movies_index_dict[movie_id]] = rating

    # Exemple d'un utilisateur qui a noté tous les films pour tester l'algorithme
    for j in range(movies_count):
        if i % 3 == 0:
            user_rating_matrix[0, j] = 0.5
        if i % 3 == 1:
            user_rating_matrix[0, j] = 5.0
        if i % 3 == 2:
            user_rating_matrix[0, j] = 3.0

    # Remplir par des zéros ou des valeurs nulles pour les films sans note
    if null_values:
        user_rating_matrix[user_rating_matrix == 0] = np.nan
    else:
        user_rating_matrix.fillna(0, inplace=True)
        
    return user_rating_matrix

def sgd_als(user_item_matrix, num_factors, learning_rate, regularization, iterations):
    """
    Factorisation matricielle avec ALS et SGD.

    num_factors: Number of latent factors to use.
    Higher values can capture more nuanced patterns but risk overfitting.
    regularization: Helps prevent overfitting by penalizing large parameter values.
    """
    errors = []
    
    # Initialize user and item latent factor matrices with small random values
    print("init user and item latent factors")
    user_factors = np.random.normal(scale=1./num_factors, size=(users_count, num_factors))
    item_factors = np.random.normal(scale=1./num_factors, size=(movies_count, num_factors))
    
    # Iterate over the specified number of iterations
    for iteration in range(iterations):
        total_error = 0
        # Loop through all user-item pairs
        for u in range(users_count):
            # print(f'user = {u}')
            for i in range(users_count):
                #  print(f'item = {i}')
                # Only update factors for user-item pairs with interaction
                if user_item_matrix[u, i] > 0:
                    # Compute the prediction error
                    error = user_item_matrix[u, i] - np.dot(user_factors[u, :], item_factors[i, :].T)
                    total_error += error**2
                    # Update rules for user and item factors
                    user_factors[u, :] += learning_rate * (error * item_factors[i, :] - regularization * user_factors[u, :])
                    item_factors[i, :] += learning_rate * (error * user_factors[u, :] - regularization * item_factors[i, :])
        
        # Calculate RMSE for current iteration
        rmse = np.sqrt(total_error / np.count_nonzero(user_item_matrix))
        errors.append(rmse)

    return user_factors, item_factors, errors

def get_matching_movies(user_id, user_item_matrix, U, V):
    """
    Retourne les films avec les features recherchées par l'utilisateur.
    """
    # Récupérer le vecteur utilisateur
    user_features = U[user_id]

    # Pour chaque film, calculer le produit scalaire entre le vecteur utilisateur et le vecteur item du film
    similarities = np.zeros(V.shape[0])
    for i in range(V.shape[0]):
        similarities[i] = np.dot(user_features, V[i])

    # Pour chaque film, si le film a été noté par l'utilisateur, on le met à -1 pour ne pas le recommander
    for i in range(user_item_matrix.shape[1]):
        if user_item_matrix[user_id, i] > 0:
            print("i :", i, "note :", user_item_matrix[user_id, i], "similarities[i] : ", similarities[i])
            #similarities[i] = -1

    # Liste des indices des films triés par pertinence avec l'utilisateur
    sorted_matching_movies_indices = np.argsort(similarities)[::-1]
    
    print("sorted_matching_movies_indices : ", sorted_matching_movies_indices)

    # Ensuite on remplace les indices par les ID des films dans la table movies
    print("len(sorted_matching_movies_indices) : ", len(sorted_matching_movies_indices))
    for i in range(len(sorted_matching_movies_indices)):
        sorted_matching_movies_indices[i] = movies_id_dict[sorted_matching_movies_indices[i]]

    return sorted_matching_movies_indices

if __name__ == "__main__":
    start = time.time()

    movies_data, links_data, ratings_data, tags_data = get_all_data()
    
    ratings_count = len(ratings_data)
    users_count = len(np.unique(ratings_data[:, 0]))
    movies_count = len(np.unique(movies_data[:, 0]))
    max_movies_id = int(ratings_data[:, 1].max())

    # print("")
    # print("ratings_count : ", ratings_count)
    # print("users_count : ", users_count)
    # print("movies_count : ", movies_count)
    # print("max_movies_id : ", max_movies_id)
    # print("")

    movies_id_dict, movies_index_dict = get_movies_id_dict()
    #print(len(movies_id_dict), len(movies_index_dict))

    # for i in range(5000, 5200):
    #     print(f"i : {i} --- movie_id_dict : {movies_id_dict[i]}")
    #     if i in movies_index_dict:
    #         print(f"i : {i} --- movie_index_dict : {movies_index_dict[i]}")

    R = create_user_item_matrix(True)
    #R.to_csv('user_item.csv', index=False)

    U, V, error = sgd_als(R, num_factors=10, learning_rate=0.01, regularization=0.01, iterations=10)
    print("User Factors:")
    print(U)
    print("Item Factors:")
    print(V)
    print("\nShapes :", U.shape, V.shape)
    print("\nErrors:")
    print(error)

    print("\nMatching Movies for User 0:")
    matching_movies = get_matching_movies(0, R, U, V)
    print(matching_movies[:20])

    end = time.time()
    print(f"Elapsed time: {end - start:.4f} seconds")
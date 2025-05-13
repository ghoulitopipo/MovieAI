from read_data import *
import numpy as np

def zero_to_null(matrix):
    """
    Remplace les zéros par des valeurs nulles dans la matrice user-item
    """
    matrix[matrix == 0] = np.nan
    return matrix

def null_to_zero(matrix):
    """
    Remplace les valeurs nulles par des zéros dans la matrice user-item
    """
    matrix.fillna(0, inplace=True)
    return matrix

def create_user_item_matrix(null_values=True):
    """
    Crée une matrice utilisateur/film à partir des données de notation.
    """
    user_rating_matrix = ratings_data.pivot(index='userId', columns='movieId', values='rating')
    if null_values:
        user_rating_matrix = zero_to_null(user_rating_matrix)
    else:
        user_rating_matrix = null_to_zero(user_rating_matrix)
    return user_rating_matrix.to_numpy()

def sgd_als(user_item_matrix, num_factors, learning_rate, regularization, iterations):
    # num_factors: Number of latent factors to use. Higher values can capture more nuanced patterns but risk overfitting.
    # learning_rate: Controls the step size during optimization. Too high can cause overshooting, too low can lead to slow convergence.
    # regularization: Helps prevent overfitting by penalizing large parameter values.
    num_users, num_items = user_item_matrix.shape
    errors = []  # To store RMSE after each iteration
    
    # Initialize user and item latent factor matrices with small random values
    print("init user and item latent factors")
    user_factors = np.random.normal(scale=1./num_factors, size=(num_users, num_factors))
    item_factors = np.random.normal(scale=1./num_factors, size=(num_items, num_factors))
    
    # Iterate over the specified number of iterations
    for iteration in range(iterations):
        total_error = 0
        # Loop through all user-item pairs
        for u in range(num_users):
            # print(f'user = {u}')
            for i in range(num_items):
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
        if i == 2579:
            print("2579 :", user_item_matrix[user_id, i] > 0)
        if user_item_matrix[user_id, i] > 0:
            similarities[i] = -1

    # Liste des indices des films triés par pertinence avec l'utilisateur
    sorted_matching_movies_indices = np.argsort(similarities)[::-1]

    return sorted_matching_movies_indices

if __name__ == "__main__":
    movies_data, links_data, ratings_data, tags_data = get_all_data()

    R = create_user_item_matrix(True)
    #R.to_csv('user_item.csv', index=False)
    #print(R)

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
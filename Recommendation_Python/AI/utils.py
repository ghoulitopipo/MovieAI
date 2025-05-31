"""
Module qui contient des parties du code réutilisables par les différents algorithmes Python.
"""

from ApiBackend import *

# Charger les données dans des tableaux Numpy
users_count, movies_data, ratings_data, tags_data = get_all_data()

# Tailles des données
movies_count = len(movies_data)
ratings_count = len(ratings_data)
tags_count = len(tags_data)


#Une seule fois car pas de changement dans le nombre de films
def get_movies_id_dict():
    """
    Crée et retourne deux dictionnaires stockant les ID des films et leurs indices de la table movies.
    
    ID MovieLens = de 1 à 193 000 environ
    indices (numéro de ligne) = de 0 à 9 700 environ

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

#une fois par changement dans base (note ou tags)
def create_user_item_matrix(null_values=True):
    """
    Crée une matrice utilisateur/film à partir de la table ratings.
    """
    #user_rating_matrix = np.zeros((users_count, total_movies_count))
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

# Dictionnaires des ID MovieLens et des numéros de ligne dans la table movies
movies_id_dict, movies_index_dict = get_movies_id_dict()

# Création de la matrice utilisateur/film
R = create_user_item_matrix(True)

def reload_data():
    """
    Recharge les données depuis l'API backend pour garantir qu'elles sont à jour.
    """
    global users_count, movies_data, ratings_data, tags_data
    users_count, movies_data, ratings_data, tags_data = get_all_data()
    global movies_count, ratings_count, tags_count
    movies_count = len(movies_data)
    ratings_count = len(ratings_data)
    tags_count = len(tags_data)

def refresh_all():
    """
    Recharge toutes les données et recalcule les structures dépendantes.
    """
    reload_data()
    global movies_id_dict, movies_index_dict, R
    movies_id_dict, movies_index_dict = get_movies_id_dict()
    R = create_user_item_matrix(True)
    
"""
ApiBackend.py, this file is used to communicate with the backend Java server.
It contains functions to get data from the server.
"""
import requests
import numpy as np

BASE_URL = "http://localhost:8080"  # Backend Java

def get_most_count():
    """
    This function is used to get the most rated movies.
    """
    try:
        url = f"{BASE_URL}/movies/best/count"
        response = requests.get(url)
        return response.json()
    except:
        print("Error: Unable to fetch data from the server. (get_most_rated)")
        return None

def get_most_average():
    """
    This function is used to get the movies with the highest average average.
    """
    try:
        url = f"{BASE_URL}/movies/best/average"
        response = requests.get(url)
        return response.json()
    except:
        print("Error: Unable to fetch data from the server. (get_most_meaned)")
        return None

def get_not_rated(user_id, genre):
    """
    This function is used to get the movies that the user has not rated. (return id, average, genres, tags).
    """
    try:
        url = f"{BASE_URL}/movies/notrate/{user_id}/{genre}"
        response = requests.get(url)
        return response.json()
    except:
        print("Error: Unable to fetch data from the server. (get_not_rated)")
        return None

def get_rated(user_id):
    """
    This function is used to get the movies that the user has rated. (genres, rating, tags).
    """
    try:
        url = f"{BASE_URL}/movies/rated/{user_id}"
        response = requests.get(url)
        return response.json()
    except:
        print("Error: Unable to fetch data from the server. (get_rated)")
        return None

def get_genres():
    """
    This function is used to get all the genres in the database.
    """
    try:
        url = f"{BASE_URL}/movies/genres"
        response = requests.get(url)
        return response.json()
    except:
        print("Error: Unable to fetch data from the server. (get_genres)")
        return None
    
def get_all_movies():
    """
    This function is used to get all the movies in the database.
    """
    try:
        url = f"{BASE_URL}/movies"
        response = requests.get(url)
        return response.json()
    except:
        print("Error: Unable to fetch data from the server. (get_all_movies)")
        return None

def get_all_ratings():
    """
    This function is used to get all the ratings in the database.
    """
    try:
        url = f"{BASE_URL}/ratings"
        response = requests.get(url)
        return response.json()
    except:
        print("Error: Unable to fetch data from the server. (get_all_ratings)")
        return None
    
def get_all_tags():
    """
    This function is used to get all the tags in the database.
    """
    try:
        url = f"{BASE_URL}/tags"
        response = requests.get(url)
        return response.json()
    except:
        print("Error: Unable to fetch data from the server. (get_all_tags)")
        return None

def get_len_users():
    """
    This function is used to get the number of users in the database.
    """
    try:
        url = f"{BASE_URL}/users/count"
        response = requests.get(url)
        return response.json()
    except:
        print("Error: Unable to fetch data from the server. (get_all_tags)")
        return None

def get_all_data():
    """
    This function is used to get all the data in the database. (call the 4 previous methods).
    """
    data = []

    ud = get_len_users()
    data.append(ud)
    
    md = get_all_movies()
    md = np.array([[movie.get("id"), movie.get("title"), movie.get("genre")] for movie in md])
    data.append(md)

    rd = np.array(get_all_ratings())
    rd = np.array([[rating.get("user").get("id"), rating.get("movie").get("id"), rating.get("rating")] for rating in rd])
    data.append(rd)
    
    td = np.array(get_all_tags())
    td = np.array([[tag.get("user").get("id"), tag.get("movie").get("id"), tag.get("tag")] for tag in td])
    data.append(td)

    return data
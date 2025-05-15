import requests

BASE_URL = "http://localhost:8080"  # Backend Java

def get_not_rated(user_id, genre):
    url = f"{BASE_URL}/movies/notrate/{user_id}/{genre}"
    return requests.get(url).json()

def get_rated(user_id, genre):
    url = f"{BASE_URL}/movies/rated/{user_id}/{genre}"
    return requests.get(url).json()

def get_genres():
    url = f"{BASE_URL}/movies/genres"
    return requests.get(url).json()

def get_rating(movie_id, user_id):
    url = f"{BASE_URL}/{movie_id}/{user_id}"
    return requests.get(url).json()

def get_average(movie_id):
    url = f"{BASE_URL}/average/{movie_id}"
    return requests.get(url).json()


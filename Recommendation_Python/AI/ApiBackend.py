import requests
import pandas as pd
import numpy as np

BASE_URL = "http://localhost:8080"  # Backend Java

def get_not_rated(user_id, genre):
    try:
        url = f"{BASE_URL}/movies/notrate/{user_id}/{genre}"
        response = requests.get(url)
        data = response.json()
        LM =[]
        for el in data:
            LM.append(el.get('id'))
        return LM
    except:
        print("Error: Unable to fetch data from the server. (get_not_rated)")
        return None

def get_rated(user_id, genre):
    try:
        url = f"{BASE_URL}/movies/rated/{user_id}/{genre}"
        response = requests.get(url)
        data = response.json()
        LM =[]
        for el in data:
            LM.append(el.get('id'))
        return LM
    except:
        print("Error: Unable to fetch data from the server. (get_rated)")
        return None

def get_genres():
    try:
        url = f"{BASE_URL}/movies/genres"
        response = requests.get(url)
        return response.json()
    except:
        print("Error: Unable to fetch data from the server. (get_genres)")
        return None
    

def get_rating(movie_id, user_id):
    try:
        url = f"{BASE_URL}/ratings/get/{movie_id}/{user_id}"
        response = requests.get(url)
        data = response.json()
        return data.get('rating')
    except:
        print("Error: Unable to fetch data from the server. (get_rating)")
        return None

def get_average(movie_id):
    try:
        url = f"{BASE_URL}/ratings/average/{movie_id}"
        response = requests.get(url)
        return response.json()
    except:
        print("Error: Unable to fetch data from the server. (get_average)")
        return None
    
def get_all_movies():
    try:
        url = f"{BASE_URL}/movies"
        response = requests.get(url)
        return response.json()
    except:
        print("Error: Unable to fetch data from the server. (get_all_movies)")
        return None

def get_all_ratings():
    try:
        url = f"{BASE_URL}/ratings"
        response = requests.get(url)
        return response.json()
    except:
        print("Error: Unable to fetch data from the server. (get_all_ratings)")
        return None
    
def get_all_tags():
    try:
        url = f"{BASE_URL}/tags"
        response = requests.get(url)
        return response.json()
    except:
        print("Error: Unable to fetch data from the server. (get_all_tags)")
        return None

def get_len_users():
    try:
        url = f"{BASE_URL}/users/count"
        response = requests.get(url)
        return response.json()
    except:
        print("Error: Unable to fetch data from the server. (get_all_tags)")
        return None

def get_all_data():
    """
    Retourne toutes les données sous forme de tabeau numpy.
    """
    data = []
    ud = get_len_users()
    data.append(ud)
    md = np.array(get_all_movies())
    data.append(md)
    rd = np.array(get_all_ratings())
    data.append(rd)
    td = np.array(get_all_tags())
    data.append(td)
    return data

import requests

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
    


import os
import pandas as pd
import numpy as np

movies_file = "movies"
ratings_file = "ratings"
tags_file = "tags"
fileNames = [movies_file, ratings_file, tags_file]

movies_data = None
ratings_data = None
tags_data = None
datas = [movies_data, ratings_data, tags_data]

def read_file(fileName):
    """
    Lit un fichier CSV et retourne les données sous forme de DataFrame pandas.
    """
    filePath = os.path.join('./data', fileName + '.csv')
    if os.path.exists(filePath):
        df = pd.read_csv(filePath)
        return df
    else:
        print(f"Le fichier {filePath} n'existe pas.")
        return None

def get_all_data():
    """
    Retourne toutes les données sous forme de tabeau numpy.
    """
    global data, movies_data, ratings_data, tags_data
    data = []
    for fileName in fileNames:
        df = read_file(fileName).to_numpy()
        data.append(df)
    datas = data
    return data

def parse_genres(genre_str):
    """
    Parse une chaîne de genres au format 'genre1|genre2|genre3'
    et retourne une liste de genres [genre1, genre2, genre3].
    """
    return genre_str.split('|')

if __name__ == "__main__":
    movies_data, ratings_data, tags_data = get_all_data()
    print(movies_data)
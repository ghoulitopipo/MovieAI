import os
import pandas as pd

movies_file = "movies"
links_file = "links"
ratings_file = "ratings"
tags_file = "tags"
fileNames = [movies_file, links_file, ratings_file, tags_file]

def read_file(fileName):
    """
    Lit un fichier CSV et retourne les données sous forme de DataFrame pandas.
    """
    filePath = os.path.join('../data', fileName + '.csv')
    if os.path.exists(filePath):
        df = pd.read_csv(filePath)
        return df
    else:
        print(f"Le fichier {filePath} n'existe pas.")
        return None

def get_all_data():
    """
    Retourne toutes les données sous forme de liste de DataFrames.
    """
    data = []
    for fileName in fileNames:
        df = read_file(fileName)
        data.append(df)
    return data

def parse_genres(genre_str):
    """
    Parse une chaîne de genres au format 'genre1|genre2|genre3'
    et retourne une liste de genres [genre1, genre2, genre3].
    """
    return genre_str.split('|')

if __name__ == "__main__":
    movies_data, links_data, ratings_data, tags_data = get_all_data()
    print(parse_genres(movies_data['genres'][0]))
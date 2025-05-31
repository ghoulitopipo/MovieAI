import ApiBackend

def Notratedonegenre(genre, u=0):
    """
    Retrieves and sorts movies of a given genre not yet rated by the user, in descending order of average rating.
    """
    MM = ApiBackend.get_not_rated(u, genre)
    for movie in MM:
        movie[2] = movie[2].split("|")
    
    
    MM.sort(key=lambda x: x[1], reverse=True)
    return MM

def Matrixgenrerating(LG, u=0):
    """
    Computes the average rating and count of rated movies for each genre and tag by a given user.
    """
    MG = []
    MT = []
    LMG = ApiBackend.get_rated(u)
    
    T = {}
    G = {}
    for genre in LG:
        G[genre[0]] = [0 ,0]

    for movie in LMG:
        rating = movie[1]
        genres = movie[0].split("|")
        tags = movie[2]

        for tag in tags:
            if tag in T:
                T[tag][0] += rating
                T[tag][1] += 1
            else:
                T[tag] = [rating,1]

        for genre in genres:
            G[genre][0] += rating
            G[genre][1] += 1

    for tag in T.keys():
        MT.append([tag,T[tag][0]/T[tag][1],T[tag][1]])

    for genre in LG:
        if G[genre[0]][1] == 0:
            MG.append([genre[0], 0, 0])
        else:
            MG.append([genre[0], G[genre[0]][0]/G[genre[0]][1], G[genre[0]][1]])

    return MG, MT

def Allgenre():
    """
    Returns a list of all available genres formatted with zero placeholders
    """
    LG = ApiBackend.get_genres()
    G = []
    for genre in LG:
        G.append([genre, 0])
    return G

def LikedgenresTag(u=0):
    """
    Calculates Bayesian-weighted preference scores for genres and tags based on user's rating history.
    The genres more important than  the tags
    """
    M,MT = Matrixgenrerating(Allgenre(), u)

    total_notes = 0
    total_films = 0
    for genre, note_moy, nb_films in M:
        total_notes += note_moy * nb_films
        total_films += nb_films
    if total_films != 0 :
        C = total_notes / total_films 
    else:
        C = 0

    G = {}
    for genre in M:
        G[genre[0]] = 0

    T = {}
    for tag in MT:
        T[tag[0]] = 0

    s = 0 
    i = 0
    j = 0
    while i < len(M) and j < len(G):
        genre_M = M[i][0]
        if genre_M in G:
            R = M[i][1]
            v = M[i][2]
            if v == 0:
                score_bayesien = 0
            else:
                score_bayesien = (v / (v + 5)) * R + (5 / (v + 5)) * C
            G[genre_M] = score_bayesien
            s += score_bayesien
            i += 1
        j += 1
    for i in range(len(MT)):
        tag_M = MT[i][0]
        R = MT[i][1]
        v = MT[i][2]
        score_bayesien = (v / (v + 5)) * R + (5 / (v + 5)) * C
        T[tag_M] = score_bayesien

    return G,T

def generate_recommendations(u=0):
    """
    Generates personalized movie recommendations based on genre and tag preferences using weighted Bayesian scores.
    """
    G,Tnonfil = LikedgenresTag(u)
    T = {t: w for t, w in Tnonfil.items() if w > 3.0}
    
    DM = {}
    for genre, weight in G.items():
        LMG = Notratedonegenre(genre, u)
        for movie_id, avg, genres, tags in LMG:
            if movie_id not in DM:
                Wgenres = 0
                Wtags = 0
                for tag in tags:
                    if tag in T:
                        Wtags += T[tag]
                for genre in genres:
                    Wgenres += G[genre]
                Wgenres /= len(genres)
                if len(tags) == 0:
                    Wtags = 0
                else:
                    Wtags /= len(tags)
                DM[movie_id] = (avg * Wgenres)/5 + ((avg * Wtags)/5)/3
    LM = sorted(DM.items(), key=lambda item: item[1], reverse=True)
    return LM


def launch_Y(id_user):
    """
    Launches the recommendation engine for a given user and returns the list of recommended movies with scores.
    """
    if id_user >= 1:
        u = id_user
    else:
        u = 0

    recommendations = generate_recommendations(u)

    output = [{"movie_id": movie_id, "score": score} for movie_id, score in recommendations]
    return output
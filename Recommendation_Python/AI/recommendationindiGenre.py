import ApiBackend
import sys
import json

def Notratedonegenre(u=0, genre="Action"):
    LM = ApiBackend.get_not_rated(u, genre)
    MM = []
    for movie_id in LM:
        avg = ApiBackend.get_average(movie_id)
        if avg is not None:
            MM.append((movie_id, avg))
    MM.sort(key=lambda x: x[1], reverse=True)
    return MM

def Matrixgenrerating(u=0):
    LG = ApiBackend.get_genres()
    M = []
    for genre in LG:
        LMG = ApiBackend.get_rated(u, genre)
        c = len(LMG)
        if c == 0:
            avg = 0
        else:
            s = 0
            for movie_id in LMG:
                rating = ApiBackend.get_rating(movie_id, u)
                if rating is not None:
                    s += rating
                else:
                    print(f"Warning: rating is None for movie {movie_id} user {u}, skipping")
            avg = s / c
        M.append([genre, avg, c])
    return M

def Allgenre():
    LG = ApiBackend.get_genres()
    G = []
    for genre in LG:
        G.append([genre, 0])
    return G

def Likedgenres(u=0):
    CW = 1
    RW = 1
    N = 100
    M = Matrixgenrerating(u)

    G = Allgenre()

    sc = sum(m[2] for m in M)
    if sc == 0:
        sc = 1

    s = 0
    i = 0
    j = 0

    while i < len(M) and j < len(G):
        if G[j][0] == M[i][0]:
            G[j][1] = M[i][1] * RW + (M[i][2] * CW) / sc
            s += G[j][1]
            i += 1
        j += 1

    j = 0
    while j < len(G):
        if s != 0:
            G[j][1] = (G[j][1] * N) // s
        else:
            G[j][1] = 0
        j += 1

    G.sort(key=lambda x: x[1], reverse=True)
    return G

def generate_recommendations(u=0):
    G = Likedgenres(u)

    DM = {}
    for genre, weight in G:
        if weight != 0:
            LMG = Notratedonegenre(u, genre)
            for movie_id, avg in LMG:
                if movie_id in DM:
                    DM[movie_id] += avg * weight
                else:
                    DM[movie_id] = avg * weight

    LM = sorted(DM.items(), key=lambda item: item[1], reverse=True)
    return LM


def launch(id_user):
    if id_user >= 1:
        u = id_user
    else:
        u = 0

    recommendations = generate_recommendations(u)

    output = [movie_id for movie_id, score in recommendations]

    return json.dumps(output, ensure_ascii=False, indent=4)
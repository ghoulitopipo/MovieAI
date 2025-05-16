import recommendationflask
import sys
import json

def Notratedonegenre(u=0, genre="Action"):
    LM = recommendationflask.get_not_rated(u, genre)
    MM = []
    for movie_id in LM:
        avg = recommendationflask.get_average(movie_id)
        MM.append((movie_id, avg))
    MM.sort(key=lambda x: x[1], reverse=True)
    return MM

def Matrixgenrerating(u=0):
    LG = recommendationflask.get_genres()
    M = []
    for genre in LG:
        LMG = recommendationflask.get_rated(u, genre)
        c = len(LMG)
        if c == 0:
            avg = 0
        else:
            s = 0
            for movie_id in LMG:
                rating = recommendationflask.get_rating(movie_id, u)
                s += rating
            avg = s / c
        M.append([genre, avg, c])
    return M

def Allgenre():
    LG = recommendationflask.get_genres()
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


if __name__ == "__main__":
    if len(sys.argv) > 1:
        u = int(sys.argv[1])
    else:
        u = 0

    recommendations = generate_recommendations(u)

    output = [movie_id for movie_id, score in recommendations]

    print(json.dumps(output, ensure_ascii=False, indent=4))
import ApiBackend

def generate_recommendations():
    LN = ApiBackend.get_most_rated()
    LM = ApiBackend.get_most_meaned()

    Reco = {}

    for i in range(len(LM)):
        movie = LM[i]
        Reco[movie.get('id')]= (100 - i)

    for i in range(len(LN)):
        movie = LN[i]
        if movie.get('id') in Reco:
            Reco[movie.get('id')]+=  (100- i)
        else:
            Reco[movie.get('id')]= (100 - i)

    RM = sorted(Reco.items(), key=lambda item: item[1], reverse=True)
    return RM

def launch():

    recommendations = generate_recommendations()

    output = [{"movie_id": movie_id, "score": score} for movie_id, score in recommendations]

    return output
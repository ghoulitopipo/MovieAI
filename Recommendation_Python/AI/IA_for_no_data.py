
import ApiBackend

def generate_recommendations():
    """
    Generates a recommendation score for movies by combining ranking from most counted and highest average rated lists.
    """
    LN = ApiBackend.get_most_count()
    LM = ApiBackend.get_most_average()

    Reco = {}

    for i in range(len(LM)):
        movie = LM[i]
        Reco[movie]= (100 - i)

    for i in range(len(LN)):
        movie = LN[i]
        if movie in Reco:
            Reco[movie]+=  (100- i)
        else:
            Reco[movie]= (100 - i)

    RM = sorted(Reco.items(), key=lambda item: item[1], reverse=True)
    return RM

def launch_N():
    """
    Launches the recommendation generation and formats the result as a list of dictionaries with movie ID and score
    """
    recommendations = generate_recommendations()
    output = [{"movie_id": movie_id, "score": score} for movie_id, score in recommendations]
    return output
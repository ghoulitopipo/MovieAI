from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import linear_kernel

# Sample movie descriptions
movies = {
 'Movie A': 'futuristic sci-fi thriller',
 'Movie B': 'romantic comedy',
 'Movie C': 'sci-fi with robots',
 'Movie D': 'historical drama'
}

# Create a TF-IDF matrix
tfidf_vectorizer = TfidfVectorizer(stop_words='english')
tfidf_matrix = tfidf_vectorizer.fit_transform(movies.values())

# Compute the cosine similarity matrix
cosine_sim = linear_kernel(tfidf_matrix, tfidf_matrix)

# Get recommendations for a movie
def get_recommendations(title):
 idx = list(movies.keys()).index(title)
 sim_scores = list(enumerate(cosine_sim[idx]))
 sim_scores = sorted(sim_scores, key=lambda x: x[1], reverse=True)
 movie_indices = [i[0] for i in sim_scores[1:]]
 return [list(movies.keys())[i] for i in movie_indices]

print(get_recommendations('Movie A'))
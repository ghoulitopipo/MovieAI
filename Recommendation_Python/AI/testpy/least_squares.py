import numpy as np
import implicit
from scipy.sparse import coo_matrix

# Sample data: user-item interactions
# (user_id, item_id, interaction_strength)
data = [
 (0, 'Movie A', 1),
 (0, 'Movie B', 1),
 (0, 'Movie C', 1),
 (1, 'Movie A', 1),
 (1, 'Movie B', 1),
 (1, 'Movie D', 1)
]

user_ids, item_ids, data = zip(*data)
unique_users = list(set(user_ids))
unique_items = list(set(item_ids))

rows = [unique_users.index(uid) for uid in user_ids]
cols = [unique_items.index(iid) for iid in item_ids]

interaction_matrix = coo_matrix((data, (rows, cols)))

# Train the ALS model
model = implicit.als.AlternatingLeastSquares(factors=50, regularization=0.01, iterations=20)
model.fit(interaction_matrix.T) # Note: .T because it expects item-user matrix

# Recommend items for a user
user_idx = unique_users.index(0)
recommended = model.recommend(user_idx, interaction_matrix)
print([unique_items[i[0]] for i in recommended])
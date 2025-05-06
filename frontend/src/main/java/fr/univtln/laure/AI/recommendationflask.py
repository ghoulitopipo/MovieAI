from flask import Flask, jsonify
import psycopg2
from psycopg2.extras import RealDictCursor

app = Flask(__name__)

def get_db_connection():
    return psycopg2.connect(
        dbname='your_db_name',
        user='your_username',
        password='your_password',
        host='localhost',
        port='5432'
    )

@app.route('/ratings/by-genre/<int:user_id>')
def ratings_by_genre(user_id):
    conn = get_db_connection()
    cur = conn.cursor(cursor_factory=RealDictCursor)

    query = """
        SELECT M.genre, AVG(R.rating) AS avg_rating, COUNT(*) AS count
        FROM Ratings R
        JOIN Movies M ON R.movie_id = M.id
        WHERE R.user_id = %s AND R.rating != 0
        GROUP BY M.genre
    """
    cur.execute(query, (user_id,))
    rows = cur.fetchall()

    cur.close()
    conn.close()

    return jsonify(rows)

@app.route('/unrated-movies/<int:user_id>/<genre>')
def unrated_movies(user_id,genre):

    conn = get_db_connection()
    cur = conn.cursor(cursor_factory=RealDictCursor)

    query = """
        SELECT M.*
        FROM Ratings R
        JOIN Movies M ON R.movie_id = M.id
        WHERE R.user_id = %s AND R.rating = 0 AND M.genre = %s
    """
    cur.execute(query, (user_id, genre))
    rows = cur.fetchall()

    cur.close()
    conn.close()

    return jsonify(rows)



if __name__ == '__main__':
    app.run(debug=True)
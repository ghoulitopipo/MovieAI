"""
ApiJava.py, this file is used to create a Flask API that will be used by the Java application.

The API has four endpoints:

1. /api/RecoForYou/<int:id_user>: this endpoint is used to get recommendations for a user.

2. /api/RecoByOther/<int:id_user>: this endpoint is used to get recommendations for a user based on other users.

3. /api/RecoForMovie/<int:id_movie> : this endpoint is used to get recommandations based on similarities with a given movie.

4. /api/RecoNoData: this endpoint is used to get recommendations for a user that doesn't have enough ratings.

5. /api/update_values: this endpoint is used  to update the values after modifications.
"""
from flask import Flask, request, jsonify

import IA_for_other
import IA_for_you
import IA_for_no_data
import IA_similar
import utils

app = Flask(__name__)

@app.route("/api/RecoForYou/<int:id_user>", methods=["GET"])
def greet(id_user):
    return jsonify(IA_for_you.launch_Y(id_user))

@app.route("/api/RecoByOther/<int:id_user>", methods=["GET"])
def getrecobyother(id_user):
    return jsonify(IA_for_other.launch_U(id_user))

@app.route("/api/RecoForMovie/<int:id_movie>", methods=["GET"])
def getrecobymovie(id_movie):
    return jsonify(IA_similar.launch_M(id_movie))

@app.route("/api/RecoNoData", methods=["GET"])
def getnouser():
    return jsonify(IA_for_no_data.launch_N())
                   
     
@app.route("/api/update_values", methods=["POST"])
def update_values():
    """
    Endpoint to update the values in the database.
    This is a placeholder for the actual implementation.
    """
    utils.refresh_all()
    IA_for_other.refresh_all()
    return jsonify({"message": "Values updated successfully"}), 200

if __name__ == "__main__":
    app.run(port=5000)
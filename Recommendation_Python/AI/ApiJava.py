from flask import Flask, request, jsonify

import IA_for_other
import IA_for_you
import IA_for_no_data

"""
ApiJava.py, this file is used to create a Flask API that will be used by the Java application.
The API has three endpoints:

1. /api/RecoForYou/<int:id_user>: this endpoint is used to get recommendations for a user.

2. /api/RecoByOther/<int:id_user>: this endpoint is used to get recommendations for a user based on other users.

3. /api/RecoNoData: this endpoint is used to get recommendations for a user that doesn't have enough ratings.
"""

app = Flask(__name__)

@app.route("/api/RecoForYou/<int:id_user>", methods=["GET"])
def greet(id_user):
    data = jsonify(IA_for_you.launch(id_user))
    if len(data.json) <= 16:
        return jsonify(IA_for_no_data.launch())
    else:
        return data

@app.route("/api/RecoByOther/<int:id_user>", methods=["GET"])
def getrecobyother(id_user):
    return jsonify(IA_for_other.launch_U(id_user))

@app.route("/api/RecoForMovie/<int:id_movie>", methods=["GET"])
def getrecobymovie(id_movie):
    return jsonify(IA_for_other.launch_M(id_movie))

@app.route("/api/RecoNoData", methods=["GET"])
def getnouser():
    return jsonify(IA_for_no_data.launch())

if __name__ == "__main__":
    app.run(port=5000)
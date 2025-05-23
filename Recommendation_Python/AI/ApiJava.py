from flask import Flask, request, jsonify

import IA_for_other
import IA_for_you
import IA_for_no_user


app = Flask(__name__)

@app.route("/api/RecoForYou/<int:id_user>", methods=["GET"])
def greet(id_user):
    return jsonify(IA_for_you.launch(id_user))

@app.route("/api/RecoByOther/<int:id_user>", methods=["GET"])
def getrecobyother(id_user):
    return jsonify(IA_for_other.launch(id_user))

@app.route("/api/RecoNoUser", methods=["GET"])
def getnouser():
    return jsonify(IA_for_no_user.launch())

if __name__ == "__main__":
    app.run(port=5000)
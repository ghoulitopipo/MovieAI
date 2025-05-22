from flask import Flask, request, jsonify

import IA_for_you
import cosine_sim

app = Flask(__name__)

@app.route("/api/RecoForYou/<int:id_user>", methods=["GET"])
def greet(id_user):
    return jsonify(IA_for_you.launch(id_user))

@app.route("/api/RecoByOther/<int:id_user>", methods=["GET"])
def getrecobyother(id_user):
    return jsonify(cosine_sim.lauch(id_user))

if __name__ == "__main__":
    app.run(port=5000)
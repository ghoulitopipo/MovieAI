from flask import Flask, request, jsonify
import recommendationindiGenre
import IA_for_other

app = Flask(__name__)

@app.route("/api/RecoForYou/<int:id_user>", methods=["GET"])
def greet(id_user):
    return jsonify(recommendationindiGenre.launch(id_user))

@app.route("/api/RecoByOther/<int:id_user>", methods=["GET"])
def getrecobyother(id_user):
    return jsonify(IA_for_other.launch(id_user))

if __name__ == "__main__":
    app.run(port=5000)
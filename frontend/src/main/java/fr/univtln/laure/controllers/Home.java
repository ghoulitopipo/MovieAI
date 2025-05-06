package fr.univtln.laure.controllers;

public class Home {

    private static int IdConnexion;

    public void setIdConnexion(int id) {
        this.IdConnexion = id;
    }

    public static int getIdConnexion() {return IdConnexion;}
}
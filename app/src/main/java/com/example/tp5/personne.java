package com.example.tp5;

public class personne {
    private int age;
    private float taille;
    private float poids;

    public personne(int age, float poids, float taille) {
        this.age = age;
        this.poids = poids;
        this.taille = taille;
    }

    public int getAge() {
        return age;
    }

    public float getPoids() {
        return poids;
    }

    public float getTaille() {
        return taille;
    }

    @Override
    public String toString() {
        return "personne{" +
                "age=" + age +
                ", taille=" + taille +
                ", poids=" + poids +
                '}';
    }

    public float getIMC() {
        return this.poids / (float) Math.pow(this.taille, 2);
    }

    public float getPoidsIdeal(boolean estHomme) {
        float tailleCm = this.taille * 100;
        if (estHomme) {
            return tailleCm-100-(tailleCm-150)/4;
        } else {
            return tailleCm-100-(tailleCm-150)/2.5f;
        }
    }

}

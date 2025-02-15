package com.yettensyvus.sms;

import java.util.Objects;

class Profesor {
    private String nume;
    private String materie;
    private int experientaAni;

    public Profesor() {}

    public Profesor(String nume, String materie, int experientaAni) {
        this.nume = nume;
        this.materie = materie;
        this.experientaAni = experientaAni;
    }

    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }
    public String getMaterie() { return materie; }
    public void setMaterie(String materie) { this.materie = materie; }
    public int getExperientaAni() { return experientaAni; }
    public void setExperientaAni(int experientaAni) { this.experientaAni = experientaAni; }

    @Override
    public String toString() {
        return "Profesor{" + "nume='" + nume + '\'' + ", materie='" + materie + '\'' + ", experientaAni=" + experientaAni + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Profesor profesor = (Profesor) o;
        return experientaAni == profesor.experientaAni && Objects.equals(nume, profesor.nume) && Objects.equals(materie, profesor.materie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nume, materie, experientaAni);
    }
}
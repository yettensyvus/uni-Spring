package com.yettensyvus.sms;

import java.util.Objects;

class Curs {
    private String denumire;
    private int credite;
    private Profesor profesor;

    public Curs() {}

    public Curs(String denumire, int credite, Profesor profesor) {
        this.denumire = denumire;
        this.credite = credite;
        this.profesor = profesor;
    }

    public String getDenumire() { return denumire; }
    public void setDenumire(String denumire) { this.denumire = denumire; }
    public int getCredite() { return credite; }
    public void setCredite(int credite) { this.credite = credite; }
    public Profesor getProfesor() { return profesor; }
    public void setProfesor(Profesor profesor) { this.profesor = profesor; }

    @Override
    public String toString() {
        return "Curs{" + "denumire='" + denumire + '\'' + ", credite=" + credite + ", profesor=" + profesor.getNume() + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Curs curs = (Curs) o;
        return credite == curs.credite && Objects.equals(denumire, curs.denumire) && Objects.equals(profesor, curs.profesor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(denumire, credite, profesor);
    }
}

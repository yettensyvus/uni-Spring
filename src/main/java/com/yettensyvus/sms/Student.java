package com.yettensyvus.sms;

import java.time.LocalDate;
import java.util.Objects;

class Student {
    private String nume;
    private int varsta;
    private String email;
    private String specialitate;
    private int anStudiu;
    private double medie;
    private boolean bursier;
    private LocalDate dataInscrierii;

    public Student() {}

    public Student(String nume, int varsta, String email, String specialitate, int anStudiu,
                   double medie, boolean bursier, LocalDate dataInscrierii) {
        this.nume = nume;
        this.varsta = varsta;
        this.email = email;
        this.specialitate = specialitate;
        this.anStudiu = anStudiu;
        this.medie = medie;
        this.bursier = bursier;
        this.dataInscrierii = dataInscrierii;
    }

    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }

    public int getVarsta() { return varsta; }
    public void setVarsta(int varsta) { this.varsta = varsta; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getspecialitate() { return specialitate; }
    public void setspecialitate(String specialitate) { this.specialitate = specialitate; }

    public int getAnStudiu() { return anStudiu; }
    public void setAnStudiu(int anStudiu) { this.anStudiu = anStudiu; }

    public double getMedie() { return medie; }
    public void setMedie(double medie) { this.medie = medie; }

    public boolean isBursier() { return bursier; }
    public void setBursier(boolean bursier) { this.bursier = bursier; }

    public LocalDate getDataInscrierii() { return dataInscrierii; }
    public void setDataInscrierii(LocalDate dataInscrierii) { this.dataInscrierii = dataInscrierii; }

    @Override
    public String toString() {
        return "Student{" +
                "nume='" + nume + '\'' +
                ", varsta=" + varsta +
                ", email='" + email + '\'' +
                ", specialitate='" + specialitate + '\'' +
                ", anStudiu=" + anStudiu +
                ", medie=" + medie +
                ", bursier=" + bursier +
                ", dataInscrierii=" + dataInscrierii +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return varsta == student.varsta &&
                anStudiu == student.anStudiu &&
                Double.compare(student.medie, medie) == 0 &&
                bursier == student.bursier &&
                Objects.equals(nume, student.nume) &&
                Objects.equals(email, student.email) &&
                Objects.equals(specialitate, student.specialitate) &&
                Objects.equals(dataInscrierii, student.dataInscrierii);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nume, varsta, email, specialitate, anStudiu, medie, bursier, dataInscrierii);
    }
}

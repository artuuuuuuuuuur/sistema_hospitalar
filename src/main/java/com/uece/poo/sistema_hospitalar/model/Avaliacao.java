package com.uece.poo.sistema_hospitalar.model;

public class Avaliacao {
    private int estrelas;
    private String texto;

    public Avaliacao(int estrelas, String texto) {
        if (estrelas < 1) estrelas = 1;
        if (estrelas > 5) estrelas = 5;
        this.estrelas = estrelas;
        this.texto = texto;
    }

    public int getEstrelas() {
        return estrelas;
    }

    public void setEstrelas(int estrelas) {
        if (estrelas < 1) estrelas = 1;
        if (estrelas > 5) estrelas = 5;
        this.estrelas = estrelas;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}

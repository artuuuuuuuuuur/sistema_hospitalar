package com.uece.poo.sistema_hospitalar.model;

import java.util.ArrayList;

public class Avaliacao {
    private Medico medico;
    private int estrelas;
    private String texto;

    public Avaliacao(Medico medico, int estrelas, String texto) {
        this.medico = medico;
        if (estrelas < 1) estrelas = 1;
        if (estrelas > 5) estrelas = 5;
        this.estrelas = estrelas;
        this.texto = texto;
    }

    public static Avaliacao fromCSV(String[] l){
        Medico medico = new Medico(l[0], "", "", "", new ArrayList<>());
        int estrelas = Integer.parseInt(l[1]);
        return new Avaliacao(medico, estrelas, l[2]);
    }

    public Medico getMedico(){
        return medico;
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

    public String toCSV(){
        String estrelasString = Integer.toString(estrelas);
        return medico.getNome()+";"+estrelasString+";"+texto;
    }
}

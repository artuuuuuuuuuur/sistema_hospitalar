package com.uece.poo.sistema_hospitalar.model;

public class Paciente extends Usuario {
    private int idade;
    private String planoSaude;

    public Paciente(int id, String nome, String login, String senha, int idade, String planoSaude) {
        super(id, nome, login, senha);
        this.idade = idade;
        this.planoSaude = planoSaude;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getPlanoSaude() {
        return planoSaude;
    }

    public void setPlanoSaude(String planoSaude) {
        this.planoSaude = planoSaude;
    }

    public void alterarDados(String nome, int idade, String planoSaude) {
        setNome(nome);
        setIdade(idade);
        setPlanoSaude(planoSaude);
    }
}

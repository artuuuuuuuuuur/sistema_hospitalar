package com.uece.poo.sistema_hospitalar.model.usuario;

public class Paciente extends Usuario {
    private int idade;
    private String planoSaude;

    public Paciente(String nome, String id, String senha, int idade, String planoSaude) {
        super(nome, id, senha);
        this.idade = idade;
        this.planoSaude = planoSaude;
    }

    public static Paciente fromCSV(String[] l){
        String nome = l[0];
        String login = l[1];
        String senha = l[2];
        String planoSaude = l[4];

        int idade = Integer.parseInt(l[3]);
        return new Paciente(nome, login, senha, idade, planoSaude);
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

    public boolean temPlano(){
        return !planoSaude.equalsIgnoreCase("Não tenho");
    }

    public void alterarDados(String nome, int idade, String planoSaude) {
        setNome(nome);
        setIdade(idade);
        setPlanoSaude(planoSaude);
    }

    public String toCSV(){
        String idadeString = Integer.toString(idade);
        return nome+","+ id +","+senha+","+idadeString+","+planoSaude;
    }
}

package com.uece.poo.sistema_hospitalar.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Medico extends Usuario {
    private String especialidade;
    private List<String> planosAtendidos;
    private List<Avaliacao> avaliacoes;
    
    public Medico(String nome, String login, String senha, String especialidade, List<String> planosAtendidos) {
        super(nome, login, senha);
        this.especialidade = especialidade;
        this.planosAtendidos = planosAtendidos;
        this.avaliacoes = new ArrayList<>();
    }

    public static Medico fromCSV(String[] l){
        String nome = l[0];
        String login = l[1];
        String senha = l[2];
        String especialidade = l[3];

        List<String> planos = new ArrayList<>();
        if(!l[4].isBlank()){
            planos = Arrays.asList(l[4].split(","));
        }
        return new Medico(nome, login, senha, especialidade, planos);
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public List<String> getPlanosAtendidos() {
        return planosAtendidos;
    }

    public void adicionarPlano(String plano){
        planosAtendidos.add(plano);
    }

    public void removerPlano(String plano){
        planosAtendidos.remove(plano);
    }
    
    public boolean atendePlano(String plano){
        return planosAtendidos.contains(plano);
    }
    
    public List<Avaliacao> getAvaliacoes() {
        return avaliacoes;
    }

    public double calcularAvaliacaoMedia() {
        if(avaliacoes.isEmpty()){
            return 0;
        }
        int soma =0;
        for(Avaliacao a : avaliacoes){
            soma += a.getEstrelas();
        }
        return (double) soma/avaliacoes.size();
    }

    public void adicionarAvaliacao(Avaliacao novaAvaliacao) {
        avaliacoes.add(novaAvaliacao);
        System.out.println("Avaliação média: " + calcularAvaliacaoMedia());
    }
    
    public void alterarDados(String nome, String especialidade){
        setNome(nome);
        setEspecialidade(especialidade);
    }

    public String toCSV(){
        String planoString = String.join(",", planosAtendidos);
        return nome+";"+login+";"+senha+";"+especialidade+";"+planoString;
    }
}

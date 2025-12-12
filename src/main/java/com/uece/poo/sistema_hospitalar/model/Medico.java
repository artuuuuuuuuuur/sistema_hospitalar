package com.uece.poo.sistema_hospitalar.model;

import java.util.ArrayList;
import java.util.List;

public class Medico extends Usuario {
    private String especialidade;
    private String planoSaudeAtendimento;
    private double avaliacaoMedia;
    private List<Avaliacao> avaliacoes;

    public Medico(int id, String nome, String login, String senha, String especialidade, String planoSaudeAtendimento) {
        super(id, nome, login, senha);
        this.especialidade = especialidade;
        this.planoSaudeAtendimento = planoSaudeAtendimento;
        this.avaliacoes = new ArrayList<>();
        this.avaliacaoMedia = 0.0;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getPlanoSaudeAtendimento() {
        return planoSaudeAtendimento;
    }

    public void setPlanoSaudeAtendimento(String planoSaudeAtendimento) {
        this.planoSaudeAtendimento = planoSaudeAtendimento;
    }

    public double getAvaliacaoMedia() {
        return avaliacaoMedia;
    }

    public List<Avaliacao> getAvaliacoes() {
        return avaliacoes;
    }

    public void adicionarAvaliacao(int estrelas, String texto) {
        Avaliacao novaAvaliacao = new Avaliacao(estrelas, texto);
        avaliacoes.add(novaAvaliacao);
        calcularAvaliacaoMedia();
    }

    private void calcularAvaliacaoMedia() {
        if (!avaliacoes.isEmpty()) {
            int totalEstrelas = 0;
            for (Avaliacao a : avaliacoes) {
                totalEstrelas += a.getEstrelas();
            }
            this.avaliacaoMedia = (double) totalEstrelas / avaliacoes.size();
        } else {
            this.avaliacaoMedia = 0.0;
        }
    }

    public static class Avaliacao {
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
}

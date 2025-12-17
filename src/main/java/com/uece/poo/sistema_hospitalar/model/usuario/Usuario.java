package com.uece.poo.sistema_hospitalar.model.usuario;

public abstract class Usuario {
    protected String nome;
    protected String id;
    protected String senha;
    
    protected Usuario(String nome, String id, String senha) {
        this.nome = nome;
        this.id = id;
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }


    public boolean autenticar(String login, String senha){
        return this.id.equals(login) && this.senha.equals(senha);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                ", nome='" + nome + '\'' +
                ", login='" + id + '\'' +
                '}';
    }
}
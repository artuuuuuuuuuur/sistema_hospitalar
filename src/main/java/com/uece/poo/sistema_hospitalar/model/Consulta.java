package com.uece.poo.sistema_hospitalar.model;

import java.time.LocalDate;

public class Consulta {
    private Medico medico;
    private Paciente paciente;
    private LocalDate data;
    private String descricao;
    private boolean realizada;
    private double valor;

    public Consulta(Medico medico, Paciente paciente, LocalDate data){
        this.medico=medico;
        this.paciente=paciente;
        this.data=data;
        this.realizada = false;
    }

    public void realizarConsulta(String descricao, double valor){
        if(paciente.temPlano() == true){
            valor = 0;
        }
        else{
            this.valor=valor;
        }
        this.descricao = descricao;
        this.realizada = true;
    }

    public boolean isRealizada(){
        return realizada;
    }
}

package com.uece.poo.sistema_hospitalar.model;

import com.uece.poo.sistema_hospitalar.model.usuario.Medico;
import com.uece.poo.sistema_hospitalar.model.usuario.Paciente;

import java.time.LocalDate;
import java.util.ArrayList;

public class Consulta {
    private Medico medico;
    private Paciente paciente;
    private LocalDate data;

    private StatusConsulta status;

    private String descricao;
    private double valor;

    public Consulta(Medico medico, Paciente paciente, LocalDate data, StatusConsulta status){
        this.medico=medico;
        this.paciente=paciente;
        this.data=data;
        this.status = status;
    }

    public static Consulta fromCSV(String[] l){
        Medico medico = new Medico(l[0], "", "", "", new ArrayList<>());
        Paciente paciente = new Paciente(l[1], "", "", 0, "");

        return new Consulta(medico, paciente, LocalDate.parse(l[2]), StatusConsulta.valueOf(l[3]));
    }

    public void realizar(String descricao, double valorBase){
        if(status != StatusConsulta.AGENDADA){
            throw new IllegalStateException("Consulta não está agendada e não pode ser realizada");
        }
        this.descricao=descricao;

        if(paciente.temPlano()){
            this.valor = 0;
        }
        else{
            this.valor=valorBase;
        }

        this.status = StatusConsulta.REALIZADA;
    }

    public void cancelar(){
        if(status == StatusConsulta.REALIZADA){
            throw new IllegalStateException("Consulta realizada não pode ser cancelada");
        }
        this.status = StatusConsulta.CANCELADA;
    }
// funções de checagem de status
    public boolean estaAgendada(){
        return status == StatusConsulta.AGENDADA;
    }

    public boolean estaEmEspera(){
        return status == StatusConsulta.EM_ESPERA;
    }

    public boolean isRealizada(){
        return status == StatusConsulta.REALIZADA;
    }

    public boolean estaCancelada(){
        return status == StatusConsulta.CANCELADA;
    }
// getters de atributos da Consulta
    public Medico getMedico(){
        return medico;
    }

    public Paciente getPaciente(){
        return paciente;
    }

    public LocalDate getData(){
        return data;
    }

    public StatusConsulta getStatus(){
        return status;
    }

    public String getDescricao(){
        return descricao;
    }

    public double getValor(){
        return valor;
    }

    // Método para passar a Consulta pro CSV
    public String toCSV(){
        return medico.getNome()+","+paciente.getNome()+","+data.toString()+","+status.name();
    }
}

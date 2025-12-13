package com.uece.poo.sistema_hospitalar.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Agendamento{
    private Medico medico;
    private LocalDate data;
    private List<Paciente> pacientes;
    private List<Paciente> listaEspera;
    private static final int limite_diario = 3;

    public Agendamento(Medico medico, LocalDate data){
        this.medico=medico;
        this.data=data;
        this.pacientes = new ArrayList<>();
        this.listaEspera = new ArrayList<>();
    }

    public void agendar(Paciente p){
        if(pacientes.size() < limite_diario){
            pacientes.add(p);
        }
        else{
            listaEspera.add(p);
        }
    }

    public void cancelar(Paciente p){
        if(pacientes.remove(p)){
            if(!listaEspera.isEmpty()){
                pacientes.add(listaEspera.remove(0));
            }
        }
    }
}

package com.uece.poo.sistema_hospitalar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.uece.poo.sistema_hospitalar.model.Medico;
import com.uece.poo.sistema_hospitalar.model.Paciente;
import com.uece.poo.sistema_hospitalar.service.AvaliacaoService;
import com.uece.poo.sistema_hospitalar.service.ConsultaService;
import com.uece.poo.sistema_hospitalar.service.MedicoService;
import com.uece.poo.sistema_hospitalar.service.PacienteService;

public class Main {
    public static void main(String[] args){
        MedicoService medicoService = new MedicoService();
        PacienteService pacienteService = new PacienteService();
        ConsultaService consultaService = new ConsultaService();

        List<String> planos = new ArrayList<>();
        planos.add("Unimed");

        Medico m1 = new Medico("João Marcos", "123", "000", "Psicologo", planos);
        Paciente p1 = new Paciente("Roberta", "username", "falcao", 35, "Unimed");

        medicoService.cadastrar(m1);
        pacienteService.cadastrar(p1);

        consultaService.agendar(m1, p1, LocalDate.now());

        AvaliacaoService avaliacaoService = new AvaliacaoService();
        avaliacaoService.avaliar(m1, 6, "Muito bom!");
        System.out.println(m1.getAvaliacoes()); // O único problema aqui é que ele ainda não está formatando no get
    }
}

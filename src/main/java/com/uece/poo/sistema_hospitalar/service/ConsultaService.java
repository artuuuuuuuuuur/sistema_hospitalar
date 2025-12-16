package com.uece.poo.sistema_hospitalar.service;
import com.uece.poo.sistema_hospitalar.model.*;
import com.uece.poo.sistema_hospitalar.util.CSVUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class ConsultaService {
    private static final String CONSULTAS = "src/java/com/uece/poo/sistema_hospitalar/dados/consultas.csv";
    private static final int LIMITE_DIARIO = 3;

    public Consulta agendar(Medico m, Paciente p, LocalDate data){
        List<String[]> dados = CSVUtil.ler(CONSULTAS);
        int qtd = 0;

        for(String[] l : dados){
            if(l[0].equals(m.getNome()) && l[2].equals(data.toString()) && l[3].equals("AGENDADA")){
                qtd++;
            }
        }
        // Operador ternário p/ decidir os status da próxima consulta:
        StatusConsulta status = qtd >= LIMITE_DIARIO ? StatusConsulta.EM_ESPERA : StatusConsulta.AGENDADA;
        
        Consulta c = new Consulta(m, p, data, status);
        List<String> linhas = new ArrayList<>();
        for(String[] l : dados){
            linhas.add(String.join(";", l));
        }
        linhas.add(c.toCSV());
        CSVUtil.escrever(CONSULTAS, linhas);
        return c;
    }

    public void cancelar(Consulta consulta){
        List<String[]> dados = CSVUtil.ler(CONSULTAS);
        List<String> linhas = new ArrayList<>();
        Consulta espera = null;
        for(String[] l : dados){
            if(l[0].equals(consulta.getMedico().getNome()) && l[1].equals(consulta.getPaciente().getNome()) && l[2].equals(consulta.getData().toString())){
                continue;
            }
            if(espera == null && l[0].equals(consulta.getMedico().getNome()) && l[2].equals(consulta.getData().toString()) && l[3].equals("EM_ESPERA")){
                l[3] = "AGENDADA";
                espera = Consulta.fromCSV(l);
            }
            linhas.add(String.join(";", l));
        }
        CSVUtil.escrever(CONSULTAS, linhas);
    }
}

package com.uece.poo.sistema_hospitalar.service;

import com.uece.poo.sistema_hospitalar.model.usuario.Medico;
import com.uece.poo.sistema_hospitalar.model.usuario.Paciente;
import com.uece.poo.sistema_hospitalar.util.CSVUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PacienteService {
    private static final String MEDICOS = "src/main/resources/com/uece/poo/sistema_hospitalar/dados/medicos.csv";
    private static final String PACIENTES = "src/main/resources/com/uece/poo/sistema_hospitalar/dados/pacientes.csv";

    public static List<Medico> listarMedicos(Paciente paciente){
        List<String[]> dados = CSVUtil.ler(MEDICOS);
        List<Medico> resultado = new ArrayList<>();

        for(String[] l : dados){
            List<String> planos = new ArrayList<>(Arrays.asList(l[4].split("\\|")));
            Medico m = new Medico(l[0], l[1], l[2], l[3], planos);
            if(paciente.getPlanoSaude().equalsIgnoreCase("Não tenho") || m.atendePlano(paciente.getPlanoSaude())){
                resultado.add(m);
            }
        }
        return resultado;
    }

    public static void atualizar(Paciente paciente){
        List<String[]> dados = CSVUtil.ler(PACIENTES);
        List<String> linhas = new ArrayList<>();
        linhas.add("NOME,ID,SENHA,IDADE,PLANO");
        for(String[] l : dados){
            if(l[1].equals(paciente.getId())){
                linhas.add(paciente.toCSV());
            }
            else{
                linhas.add(String.join(",", l));
            }
        }
        CSVUtil.sobrescrever(PACIENTES, linhas);
    }

    public void cadastrar(Paciente paciente){
        List<String[]> dados = CSVUtil.ler(PACIENTES);
        List<String> linhas = new ArrayList<>();
        boolean existe = false;

        for(String[] l : dados){
            if(l[1].equals(paciente.getId())){
                existe = true;
            }
            linhas.add(String.join(",", l));
        }
        if(!existe){
            linhas.add(paciente.toCSV());
        }

        CSVUtil.escrever(PACIENTES, linhas);
    }
}

package com.uece.poo.sistema_hospitalar.service;

import com.uece.poo.sistema_hospitalar.model.usuario.Medico;
import com.uece.poo.sistema_hospitalar.model.usuario.Paciente;
import com.uece.poo.sistema_hospitalar.util.CSVUtil;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

public class LoginService {
    private static final String MEDICOS = "src/main/resources/com/uece/poo/sistema_hospitalar/dados/medicos.csv";
    private static final String PACIENTES = "src/main/resources/com/uece/poo/sistema_hospitalar/dados/pacientes.csv";

    public Medico loginMedico(String login, String senha){
        List<String[]> dados = CSVUtil.ler(MEDICOS);

        for(String[] l : dados){
            if(l[1].equalsIgnoreCase(login) && l[2].equalsIgnoreCase(senha)){
                List<String> planos = new ArrayList<>(Arrays.asList(l[4].split(",")));
                return new Medico(l[0], l[1], l[2], l[3], planos);
            }
        }
        return null;
    }

    public Paciente loginPaciente(String login, String senha){
        List<String[]> dados = CSVUtil.ler(PACIENTES);

        for(String[] l : dados){
            if(l[1].equalsIgnoreCase(login) && l[2].equalsIgnoreCase(senha)){
                return new Paciente(l[0], l[1], l[2], Integer.parseInt(l[3]), l[4]);
            }
        }
        return null;
    }
}

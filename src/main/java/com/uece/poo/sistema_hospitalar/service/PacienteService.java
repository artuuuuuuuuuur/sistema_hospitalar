package com.uece.poo.sistema_hospitalar.service;

import com.uece.poo.sistema_hospitalar.model.Medico;
import com.uece.poo.sistema_hospitalar.model.Paciente;
import com.uece.poo.sistema_hospitalar.util.CSVUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PacienteService {
    private static final String MEDICOS = "src/java/com/uece/poo/sistema_hospitalar/dados/medicos.csv";

    public List<Medico> listarMedicos(Paciente paciente){
        List<String[]> dados = CSVUtil.ler(MEDICOS);
        List<Medico> resultado = new ArrayList<>();

        for(String[] l : dados){
            List<String> planos = new ArrayList<>(Arrays.asList(l[4].split(",")));
            Medico m = new Medico(l[0], l[1], l[2], l[3], planos);
            if(paciente.getPlanoSaude().equalsIgnoreCase("NAO_TENHO") || m.atendePlano(paciente.getPlanoSaude())){
                resultado.add(m);
            }
        }
        return resultado;
    }
}

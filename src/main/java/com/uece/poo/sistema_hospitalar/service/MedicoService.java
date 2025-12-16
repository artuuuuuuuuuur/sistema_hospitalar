package com.uece.poo.sistema_hospitalar.service;

import com.uece.poo.sistema_hospitalar.model.Medico;
import com.uece.poo.sistema_hospitalar.util.CSVUtil;

import java.util.List;
import java.util.ArrayList;

public class MedicoService {
    private static final String MEDICOS = "src/java/com/uece/poo/sistema_hospitalar/dados/medicos.csv";

    public void atualizarMedico(Medico medico){
        List<String[]> dados = CSVUtil.ler(MEDICOS);
        List<String> linhas = new ArrayList<>();

        for(String[] l : dados){
            if(l[1].equals(medico.getLogin())){
                linhas.add(medico.toCSV());
            }
            else{
                linhas.add(String.join(";", l));
            }
        }
        CSVUtil.escrever(MEDICOS, linhas);
    }
}

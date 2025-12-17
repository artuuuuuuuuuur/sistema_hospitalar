package com.uece.poo.sistema_hospitalar.service;

import com.uece.poo.sistema_hospitalar.model.usuario.Medico;
import com.uece.poo.sistema_hospitalar.util.CSVUtil;

import java.util.List;
import java.util.ArrayList;

public class MedicoService {
    private static final String MEDICOS = "src/main/resources/com/uece/poo/sistema_hospitalar/dados/medicos.csv";

    public static void atualizar(Medico medico){
        List<String[]> dados = CSVUtil.ler(MEDICOS);
        List<String> linhas = new ArrayList<>();

        linhas.add("NOME,ID,SENHA,ESPECIALIDADE,PLANOS");
        for(String[] l : dados){
            if(l[1].equals(medico.getId())){
                linhas.add(medico.toCSV());
            }
            else{
                linhas.add(String.join(",", l));
            }
        }
        CSVUtil.sobrescrever(MEDICOS, linhas);
    }

    public void cadastrar(Medico medico){
        List<String[]> dados = CSVUtil.ler(MEDICOS);
        List<String> linhas = new ArrayList<>();
        boolean existe = false;

        for(String[] l : dados){
            if(l[1].equals(medico.getId())){
                existe = true;
            }
            linhas.add(String.join(",", l));
        }
        if(!existe){
            linhas.add(medico.toCSV());
        }

        CSVUtil.escrever(MEDICOS, linhas);
    }
}

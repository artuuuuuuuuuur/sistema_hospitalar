package com.uece.poo.sistema_hospitalar.service;
import com.uece.poo.sistema_hospitalar.model.*;
import com.uece.poo.sistema_hospitalar.util.CSVUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class ConsultaService {
    private static final String CONSULTAS = "src/java//com/uece/poo/sistema_hospitalar/dados/consultas.csv";
    private static final int LIMITE_DIARIO = 3;

    public Consulta agendar(Medico m, Paciente p, LocalDate data){
        List<String[]> dados = CSVUtil.ler(CONSULTAS);
        int qtd = 0;

        for(String[] l : dados){
            //continuar depois...
        }
    }
}

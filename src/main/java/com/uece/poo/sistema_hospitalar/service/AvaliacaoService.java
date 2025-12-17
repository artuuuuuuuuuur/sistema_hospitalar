package com.uece.poo.sistema_hospitalar.service;
import com.uece.poo.sistema_hospitalar.model.*;
import com.uece.poo.sistema_hospitalar.model.usuario.Medico;
import com.uece.poo.sistema_hospitalar.util.CSVUtil;

import java.util.List;
import java.util.ArrayList;

public class AvaliacaoService {
    private static final String AVALIACOES = "src/main/resources/com/uece/poo/sistema_hospitalar/dados/avaliacoes.csv";

    public void avaliar(Medico m, int estrelas, String texto){
        Avaliacao a = new Avaliacao(m, estrelas, texto);
        m.adicionarAvaliacao(a);
        List<String> linhas = new ArrayList<>();
        linhas.add("MÉDICO,ESTRELAS,TEXTO");

        CSVUtil.ler(AVALIACOES).forEach(l -> linhas.add(String.join(",", l)));
        linhas.add(a.toCSV());
        CSVUtil.sobrescrever(AVALIACOES, linhas);
    }
}

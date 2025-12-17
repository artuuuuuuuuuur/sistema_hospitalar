package com.uece.poo.sistema_hospitalar.service;

import com.uece.poo.sistema_hospitalar.model.Avaliacao;
import com.uece.poo.sistema_hospitalar.model.usuario.Medico;
import com.uece.poo.sistema_hospitalar.util.CSVUtil;

import java.util.ArrayList;
import java.util.List;

public class AvaliacaoService {

    private static final String AVALIACOES =
            "src/main/resources/com/uece/poo/sistema_hospitalar/dados/avaliacoes.csv";

    public static void avaliar(Medico medico, int estrelas, String comentario) {

        Avaliacao avaliacao = new Avaliacao(medico, estrelas, comentario);

        List<String> linhas = new ArrayList<>();

        List<String[]> existentes = CSVUtil.ler(AVALIACOES);
        linhas.add("MEDICO,ESTRELAS,COMENTARIO");

        existentes.forEach(l -> linhas.add(String.join(",", l)));



        linhas.add(avaliacao.toCSV());

        CSVUtil.sobrescrever(AVALIACOES, linhas);
    }
}

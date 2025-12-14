package com.uece.poo.sistema_hospitalar.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ArquivoMedico {

    private static final String CAMINHO = "dados/medico.txt";

    //  SALVAR
    public static void salvar(Medico medico) {

        try (FileWriter fw = new FileWriter(CAMINHO)) {

            String planos = String.join(",", medico.getPlanosAtendidos());

            fw.write(
                medico.getId() + ";" +
                medico.getNome() + ";" +
                medico.getLogin() + ";" +
                medico.getSenha() + ";" +
                medico.getEspecialidade() + ";" +
                planos
            );

        } catch (IOException e) {
            System.out.println("Erro ao salvar médico.");
        }
    }

    // CARREGAR
    public static Medico carregar() {

        File arquivo = new File(CAMINHO);

        if (!arquivo.exists()) return null;

        try (Scanner sc = new Scanner(arquivo)) {

            if (!sc.hasNextLine()) return null;

            String linha = sc.nextLine();
            String[] dados = linha.split(";");

            String[] planosArray = dados[5].split(",");
            List<String> planos = new ArrayList<>();

            for (String p : planosArray) {
                planos.add(p);
            }

            return new Medico(
                    Integer.parseInt(dados[0]),
                    dados[1],
                    dados[2],
                    dados[3],
                    dados[4],
                    planos
            );

        } catch (Exception e) {
            System.out.println("Erro ao carregar médico.");
            return null;
        }
    }
}


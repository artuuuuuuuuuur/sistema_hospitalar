package com.uece.poo.sistema_hospitalar.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Arquivo_Paciente {

    private static final String CAMINHO = "dados/pacientes.txt";

    //  SALVAR 
    public static void salvar(List<Paciente> pacientes) {

        try (FileWriter fw = new FileWriter(CAMINHO)) {

            for (Paciente p : pacientes) {
                fw.write(
                    p.getId() + ";" +
                    p.getNome() + ";" +
                    p.getLogin() + ";" +
                    p.getSenha() + ";" +
                    p.getIdade() + ";" +
                    p.getPlanoSaude() + "\n"
                );
            }

        } catch (IOException e) {
            System.out.println("Erro ao salvar pacientes.");
        }
    }

    // LER 
    public static List<Paciente> carregar() {

        List<Paciente> pacientes = new ArrayList<>();
        File arquivo = new File(CAMINHO);

        if (!arquivo.exists()) return pacientes;

        try (Scanner sc = new Scanner(arquivo)) {

            while (sc.hasNextLine()) {
                String linha = sc.nextLine();
                String[] dados = linha.split(";");

                Paciente p = new Paciente(
                        Integer.parseInt(dados[0]),
                        dados[1],
                        dados[2],
                        dados[3],
                        Integer.parseInt(dados[4]),
                        dados[5]
                );

                pacientes.add(p);
            }

        } catch (Exception e) {
            System.out.println("Erro ao carregar pacientes.");
        }

        return pacientes;
    }
}

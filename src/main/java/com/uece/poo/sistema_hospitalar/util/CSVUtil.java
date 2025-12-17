package com.uece.poo.sistema_hospitalar.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class CSVUtil {


    public static void escrever(String caminho, List<String> linhas) {
        try {
            Path path = Paths.get(caminho);
            Path parent = path.getParent();
            if (parent != null) Files.createDirectories(parent);

            try (BufferedWriter bw = Files.newBufferedWriter(
                    path,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            )) {
                for (String l : linhas) {
                    bw.write(l);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao escrever CSV: " + e.getMessage());
        }
    }

    public static void sobrescrever(String caminho, List<String> linhas) {
        try {
            Path path = Paths.get(caminho);
            Files.write(path, linhas, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static List<String[]> ler(String caminho) {
        List<String[]> linhas = new ArrayList<>();

        File f = new File(caminho);

        if (f.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {

                br.readLine();

                String linha;
                while ((linha = br.readLine()) != null) {
                    if (!linha.isBlank()) {
                        linhas.add(linha.split(","));
                    }
                }

            } catch (IOException e) {
                System.out.println("Erro ao ler CSV: " + e.getMessage());
            }
            return linhas;
        }

        // ===== Resource (dentro do JAR) =====
        try (InputStream in = CSVUtil.class.getResourceAsStream("/" + caminho)) {
            if (in == null) {
                System.out.println("Arquivo NÃO encontrado: " + caminho);
                return linhas;
            }

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(in, StandardCharsets.UTF_8))) {

                br.readLine();

                String linha;
                while ((linha = br.readLine()) != null) {
                    if (!linha.isBlank()) {
                        linhas.add(linha.split(","));
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Erro ao ler recurso CSV: " + e.getMessage());
        }

        return linhas;
    }
}

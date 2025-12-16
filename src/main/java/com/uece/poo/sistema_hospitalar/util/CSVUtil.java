package com.uece.poo.sistema_hospitalar.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CSVUtil {
    public static void escrever(String caminho, List<String> linhas){
        try {
            java.nio.file.Path path = java.nio.file.Paths.get(caminho);
            java.nio.file.Path parent = path.getParent();
            if (parent != null) java.nio.file.Files.createDirectories(parent);
            try (BufferedWriter bw = java.nio.file.Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                for(String l : linhas){
                    bw.write(l);
                    bw.newLine();
                }
            }
        } catch(IOException e){
            e.printStackTrace();
            System.out.println("Erro ao escrever CSV: " + e.getMessage());
        }
    }

    public static List<String[]> ler(String caminho){
        List<String[]> linhas = new ArrayList<>();

        File f = new File(caminho);
        if(f.exists()){
            try(BufferedReader br = new BufferedReader(new FileReader(f))){
                String linha;
                while((linha = br.readLine()) != null){
                    linhas.add(linha.split(";"));
                }
            } catch(IOException e){
                System.out.println("Erro ao ler CSV: "+ e.getMessage());
            }
            return linhas;
        }

        try (InputStream in = CSVUtil.class.getResourceAsStream("/com/uece/poo/sistema_hospitalar/dados/medicos.csv")) {
            if (in == null) {
                System.out.println("Arquivo não encontrado: " + caminho);
                return linhas;
            }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                String linha;
                while((linha = br.readLine()) != null){
                    linhas.add(linha.split(";"));
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler recurso CSV: " + e.getMessage());
        }

        return linhas;
    }
}

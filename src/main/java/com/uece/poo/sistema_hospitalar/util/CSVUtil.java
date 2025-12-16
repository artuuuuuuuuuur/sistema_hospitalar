package com.uece.poo.sistema_hospitalar.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVUtil {
    public static List<String[]> ler(String caminho){
        List<String[]> linhas = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new FileReader(caminho))){
            String linha;
            while((linha = br.readLine()) != null){
                linhas.add(linha.split(";"));
            }
        } catch(IOException e){
            System.out.println("Erro ao ler CSV: "+ e.getMessage());
        }
        return linhas;
    }

    public static void escrever(String caminho, List<String> linhas){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(caminho))){
            for(String l : linhas){
                bw.write(l);
                bw.newLine();
            }
        } catch(IOException e){
            System.out.println("Erro ao escrever CSV: " + e.getMessage());
        }
    }
}

package com.uece.poo.sistema_hospitalar.main_controlavel;

import java.util.Scanner;
public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Sistema_Hospitalar sistema = new Sistema_Hospitalar();

        int opcao;

        do {
            System.out.println("\n====== SISTEMA HOSPITALAR ======");
            System.out.println("1 - Cadastrar Médico");
            System.out.println("2 - Cadastrar Paciente");
            System.out.println("3 - Agendar Paciente");
            System.out.println("4 - Cancelar Agendamento");
            System.out.println("5 - Realizar Consulta");
            System.out.println("6 - Avaliar Médico");
            System.out.println("0 - Sair");
            System.out.print("Opção: ");

            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1 -> sistema.cadastrarMedico(sc);
                case 2 -> sistema.cadastrarPaciente(sc);
                case 3 -> sistema.agendarPaciente(sc);
                case 4 -> sistema.cancelarAgendamento(sc);
                case 5 -> sistema.realizarConsulta(sc);
                case 6 -> sistema.avaliarMedico(sc);
                case 0 -> System.out.println("Encerrando sistema...");
                default -> System.out.println("Opção inválida!");
            }

        } while (opcao != 0);

        sc.close();
    }
}

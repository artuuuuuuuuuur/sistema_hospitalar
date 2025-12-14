package com.uece.poo.sistema_hospitalar.main_controlavel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.uece.poo.sistema_hospitalar.model.Agendamento;
import com.uece.poo.sistema_hospitalar.model.ArquivoMedico;
import com.uece.poo.sistema_hospitalar.model.Arquivo_Paciente;
import com.uece.poo.sistema_hospitalar.model.Avaliacao;
import com.uece.poo.sistema_hospitalar.model.Consulta;
import com.uece.poo.sistema_hospitalar.model.Medico;
import com.uece.poo.sistema_hospitalar.model.Paciente;

public class Sistema_Hospitalar {

    private Medico medico;
    private Agendamento agendamento;
    private List<Paciente> pacientes = new ArrayList<>();
    
    
    
    public void cadastrarMedico(Scanner sc) {
        
        System.out.println("\n=== Cadastro de Médico ===");
        
        System.out.print("ID: ");
        int id = sc.nextInt();
        sc.nextLine();
        
        System.out.print("Nome: ");
        String nome = sc.nextLine();
        
        System.out.print("Login: ");
        String login = sc.nextLine();

        System.out.print("Senha: ");
        String senha = sc.nextLine();
        
        System.out.print("Especialidade: ");
        String especialidade = sc.nextLine();
        
        System.out.print("Quantidade de planos: ");
        int qtd = sc.nextInt();
        sc.nextLine();
        
        List<String> planos = new ArrayList<>();
        for (int i = 0; i < qtd; i++) {
            System.out.print("Plano " + (i + 1) + ": ");
            planos.add(sc.nextLine());
        }
        
        medico = new Medico(id, nome, login, senha, especialidade, planos);
        agendamento = new Agendamento(medico, LocalDate.now());
        
        System.out.println("Médico cadastrado com sucesso!");
          medico = new Medico(id, nome, login, senha, especialidade, planos);
          agendamento = new Agendamento(medico, LocalDate.now());

          ArquivoMedico.salvar(medico);
         System.out.println("Médico cadastrado e salvo em arquivo!");
    }
    
    
    public void cadastrarPaciente(Scanner sc) {
        pacientes = Arquivo_Paciente.carregar();
        
        System.out.println("\n=== Cadastro de Paciente ===");

        System.out.print("ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Nome: ");
        String nome = sc.nextLine();

        System.out.print("Login: ");
        String login = sc.nextLine();

        System.out.print("Senha: ");
        String senha = sc.nextLine();

        System.out.print("Idade: ");
        int idade = sc.nextInt();
        sc.nextLine();

        System.out.print("Plano de saúde (ou NAO_TENHO): ");
        String plano = sc.nextLine();

        pacientes.add(new Paciente(id, nome, login, senha, idade, plano));

        System.out.println("Paciente cadastrado com sucesso!");

        
    pacientes.add(new Paciente(id, nome, login, senha, idade, plano));

    Arquivo_Paciente.salvar(pacientes);

    System.out.println("Paciente cadastrado e salvo em arquivo!");
    }

  
    public void agendarPaciente(Scanner sc) {

        if (agendamento == null) {
            System.out.println("Cadastre um médico primeiro.");
            return;
        }

        System.out.print("ID do paciente: ");
        int id = sc.nextInt();
        sc.nextLine();

        Paciente p = buscarPaciente(id);

        if (p == null) {
            System.out.println("Paciente não encontrado.");
            return;
        }

        agendamento.agendar(p);
        System.out.println("Paciente agendado.");
    }

    public void cancelarAgendamento(Scanner sc) {

        System.out.print("ID do paciente: ");
        int id = sc.nextInt();
        sc.nextLine();

        Paciente p = buscarPaciente(id);

        if (p == null) {
            System.out.println("Paciente não encontrado.");
            return;
        }

        agendamento.cancelar(p);
        System.out.println("Agendamento cancelado.");
    }

    
    public void realizarConsulta(Scanner sc) {

        System.out.print("ID do paciente: ");
        int id = sc.nextInt();
        sc.nextLine();

        Paciente p = buscarPaciente(id);

        if (p == null) {
            System.out.println("Paciente não encontrado.");
            return;
        }

        System.out.print("Descrição: ");
        String descricao = sc.nextLine();

        System.out.print("Valor: ");
        double valor = sc.nextDouble();
        sc.nextLine();

        Consulta consulta = new Consulta(medico, p, LocalDate.now());
        consulta.realizarConsulta(descricao, valor);

        System.out.println("Consulta realizada com sucesso.");
    }

  
    public void avaliarMedico(Scanner sc) {

        System.out.print("Estrelas (1 a 5): ");
        int estrelas = sc.nextInt();
        sc.nextLine();

        System.out.print("Comentário: ");
        String texto = sc.nextLine();

        medico.adicionarAvaliacao(new Avaliacao(estrelas, texto));
    }


    private Paciente buscarPaciente(int id) {
        for (Paciente p : pacientes) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }
}

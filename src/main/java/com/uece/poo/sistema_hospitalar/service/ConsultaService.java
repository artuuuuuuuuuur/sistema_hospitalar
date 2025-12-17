    package com.uece.poo.sistema_hospitalar.service;
    import com.uece.poo.sistema_hospitalar.model.*;
    import com.uece.poo.sistema_hospitalar.model.usuario.Medico;
    import com.uece.poo.sistema_hospitalar.model.usuario.Paciente;
    import com.uece.poo.sistema_hospitalar.util.CSVUtil;
    import com.uece.poo.sistema_hospitalar.util.ExceptionModal;

    import java.io.IOException;
    import java.nio.charset.StandardCharsets;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.Paths;
    import java.nio.file.StandardOpenOption;
    import java.time.LocalDate;
    import java.util.List;
    import java.util.ArrayList;

    public class ConsultaService {
        private static final String CONSULTAS = "src/main/resources/com/uece/poo/sistema_hospitalar/dados/consultas.csv";
        private static final int LIMITE_DIARIO = 3;

        public static Consulta agendar(Medico m, Paciente p, LocalDate data){
            List<String[]> dados = CSVUtil.ler(CONSULTAS);
            int qtd = 0;

            for(String[] l : dados){
                if(l[0].equals(m.getNome()) && l[2].equals(data.toString()) && l[3].equals("AGENDADA")){
                    qtd++;
                }
            }
            // Operador ternário p/ decidir os status da próxima consulta:
            StatusConsulta status = qtd >= LIMITE_DIARIO ? StatusConsulta.EM_ESPERA : StatusConsulta.AGENDADA;

            Consulta c = new Consulta(m, p, data, status);
            List<String> linhas = new ArrayList<>();
            linhas.add("MÉDICO,PACIENTE,DATA,STATUS");
            for(String[] l : dados){
                linhas.add(String.join(",", l));
            }
            linhas.add(c.toCSV());
            CSVUtil.sobrescrever(CONSULTAS, linhas);
            return c;
        }

        public static void cancelar(Consulta consulta){
            List<String[]> dados = CSVUtil.ler(CONSULTAS);
            List<String> linhas = new ArrayList<>();
            Consulta espera = null;
            for(String[] l : dados){
                if(l[0].equals(consulta.getMedico().getNome()) && l[1].equals(consulta.getPaciente().getNome()) && l[2].equals(consulta.getData().toString())){
                    continue;
                }
                if(espera == null && l[0].equals(consulta.getMedico().getNome()) && l[2].equals(consulta.getData().toString()) && l[3].equals("EM_ESPERA")){
                    l[3] = "AGENDADA";
                    espera = Consulta.fromCSV(l);
                }
                linhas.add(String.join(",", l));
            }
            CSVUtil.sobrescrever(CONSULTAS, linhas);
        }

        public static List<Consulta> listarPorPaciente(Paciente paciente) {
            List<Consulta> resultado = new ArrayList<>();

            for (String[] l : CSVUtil.ler(CONSULTAS)) {
                Consulta c = Consulta.fromCSV(l);
                if (c.getPaciente().getNome().equals(paciente.getNome())) {
                    resultado.add(c);
                }
            }
            return resultado;
        }

        public static List<Consulta> listarPorMedico(Medico medico) {
            List<Consulta> resultado = new ArrayList<>();

            for (String[] l : CSVUtil.ler(CONSULTAS)) {
                Consulta c = Consulta.fromCSV(l);
                if (c.getMedico().getNome().equals(medico.getNome())) {
                    resultado.add(c);
                }
            }
            return resultado;
        }

        public static void realizarConsulta(
                Consulta consulta,
                String descricao,
                double valorBase
        ) {

            List<String[]> dados = CSVUtil.ler(CONSULTAS);
            List<String> linhas = new ArrayList<>();

            // Header
            linhas.add("MEDICO,PACIENTE,DATA,STATUS");

            // Realiza a consulta (define descrição, valor e status)
            consulta.realizar(descricao, valorBase);

            // Atualiza o CSV
            for (String[] l : dados) {

                boolean mesmaConsulta =
                        l[0].equals(consulta.getMedico().getNome()) &&
                                l[1].equals(consulta.getPaciente().getNome()) &&
                                l[2].equals(consulta.getData().toString());

                if (mesmaConsulta) {
                    linhas.add(consulta.toCSV());
                } else {
                    linhas.add(String.join(",", l));
                }
            }

            CSVUtil.sobrescrever(CONSULTAS, linhas);

            // ===== CRIAR ARQUIVO TXT COM DETALHES DA CONSULTA =====
            criarArquivoConsultaTxt(consulta);
        }
        private static void criarArquivoConsultaTxt(Consulta consulta) {

            try {
                String diretorio =
                        "src/main/resources/com/uece/poo/sistema_hospitalar/consultas_realizadas/";

                Files.createDirectories(Paths.get(diretorio));

                String nomeArquivo =
                        consulta.getMedico().getNome().replace(" ", "_") + "_" +
                                consulta.getPaciente().getNome().replace(" ", "_") + "_" +
                                consulta.getData() + ".txt";

                Path path = Paths.get(diretorio + nomeArquivo);

                List<String> conteudo = new ArrayList<>();

                conteudo.add("===== CONSULTA REALIZADA =====");
                conteudo.add("");
                conteudo.add("Médico: " + consulta.getMedico().getNome());
                conteudo.add("Paciente: " + consulta.getPaciente().getNome());
                conteudo.add("Data: " + consulta.getData());
                conteudo.add("Status: " + consulta.getStatus());
                conteudo.add("");
                conteudo.add("Descrição da Consulta:");
                conteudo.add(consulta.getDescricao());
                conteudo.add("");
                conteudo.add("Valor da Consulta: R$ " +
                        String.format("%.2f", consulta.getValor()));

                Files.write(
                        path,
                        conteudo,
                        StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING
                );

            } catch (IOException e) {
                ExceptionModal.popUp("Erro ao gerar arquivo da consulta.");
            }
        }



        public static void atualizarConsulta(Consulta consulta) {

            List<String[]> dados = CSVUtil.ler(CONSULTAS);
            List<String> linhasAtualizadas = new ArrayList<>();

            // ===== Header =====
            linhasAtualizadas.add("MEDICO,PACIENTE,DATA,STATUS");

            boolean atualizada = false;

            for (String[] l : dados) {

                boolean mesmaConsulta =
                        l[0].equals(consulta.getMedico().getNome()) &&
                                l[1].equals(consulta.getPaciente().getNome()) &&
                                l[2].equals(consulta.getData().toString());

                if (mesmaConsulta && !atualizada) {
                    linhasAtualizadas.add(consulta.toCSV());
                    atualizada = true;
                } else {
                    linhasAtualizadas.add(String.join(",", l));
                }
            }

            CSVUtil.sobrescrever(CONSULTAS, linhasAtualizadas);
        }



    }

package com.uece.poo.sistema_hospitalar.controller;

import com.uece.poo.sistema_hospitalar.model.Avaliacao;
import com.uece.poo.sistema_hospitalar.model.Consulta;
import com.uece.poo.sistema_hospitalar.model.usuario.Medico;
import com.uece.poo.sistema_hospitalar.model.usuario.Paciente;
import com.uece.poo.sistema_hospitalar.model.usuario.Usuario;
import com.uece.poo.sistema_hospitalar.service.AvaliacaoService;
import com.uece.poo.sistema_hospitalar.service.ConsultaService;
import com.uece.poo.sistema_hospitalar.service.MedicoService;
import com.uece.poo.sistema_hospitalar.service.PacienteService;
import com.uece.poo.sistema_hospitalar.util.CSVUtil;
import com.uece.poo.sistema_hospitalar.util.ExceptionModal;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController {

    @FXML private VBox opcoesVBox;
    @FXML private Button consultasButton;
    @FXML private Button alterarDadosButton;
    @FXML private Label helloLabel;
    @FXML private Label planoLabel;
    @FXML private VBox consultasContainer;

    private boolean isMedico;
    private Usuario user;


    public void carregarDados(boolean isMedico, Usuario user) {
        this.isMedico = isMedico;
        this.user = user;
        renderizarOpcoes();
    }

    private void renderizarOpcoes() {
        if (user == null) {
            return;
        }

        if (user instanceof Paciente) {
            renderizarOpcoesDePaciente((Paciente) user);
        } else {
            renderizarOpcoesDeMedico();
        }

        consultasButton.setOnAction(e -> {

            Stage stage = new Stage();
            VBox root = new VBox(10);
            root.setPadding(new Insets(15));

            ScrollPane scrollPane = new ScrollPane(root);
            scrollPane.setFitToWidth(true);

            atualizarTelaConsultas(root);

            stage.setScene(new Scene(scrollPane, 600, 400));
            stage.setTitle("Minhas Consultas");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        });


        alterarDadosButton.setOnAction(e -> {

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Alterar Dados");

            VBox root = new VBox(12);
            root.setStyle("-fx-padding: 20; -fx-background-color: #2b2b2b;");
            root.setAlignment(Pos.CENTER_LEFT);

            Label titulo = new Label("Alterar Dados");
            titulo.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

            root.getChildren().add(titulo);

            // ===== Campos comuns =====
            PasswordField senhaField = new PasswordField();
            senhaField.setPromptText("Nova senha (opcional)");
            Label senhaLabel = new Label("Senha: ");
            senhaLabel.setTextFill(Paint.valueOf("white"));

            root.getChildren().addAll(senhaLabel, senhaField);

            // =========================
// MÉDICO
// =========================
            if (user instanceof Medico medico) {

                // ===== ESPECIALIDADE =====
                Label espLabel = new Label("Especialidade:");
                espLabel.setTextFill(Paint.valueOf("white"));

                TextField especialidadeField = new TextField(medico.getEspecialidade());
                especialidadeField.setPromptText("Especialidade");

                // ===== PLANOS =====
                Label planosLabel = new Label("Planos atendidos (separados por |):");
                planosLabel.setTextFill(Paint.valueOf("white"));

                TextField planosField = new TextField(
                        String.join("|", medico.getPlanosAtendidos())
                );
                planosField.setPromptText("Ex: UNIMED|AMIL|HAPVIDA");

                root.getChildren().addAll(
                        espLabel,
                        especialidadeField,
                        planosLabel,
                        planosField
                );

                // ===== SALVAR =====
                Button salvarButton = new Button("Salvar");
                salvarButton.setOnAction(ev -> {
                    try {

                        // Especialidade
                        if (!especialidadeField.getText().isBlank()) {
                            medico.setEspecialidade(especialidadeField.getText());
                        }

                        // Senha
                        if (!senhaField.getText().isBlank()) {
                            medico.setSenha(senhaField.getText());
                        }

                        // Planos
                        if (!planosField.getText().isBlank()) {

                            List<String> novosPlanos = Arrays.stream(
                                            planosField.getText().split("\\|")
                                    )
                                    .map(String::trim)
                                    .filter(p -> !p.isBlank())
                                    .toList();

                            if (novosPlanos.isEmpty()) {
                                ExceptionModal.popUp("Informe ao menos um plano válido.");
                                return;
                            }

                            medico.setPlanosAtendidos(new ArrayList<>(novosPlanos));
                        }

                        MedicoService.atualizar(medico);

                        planoLabel.setText(
                                "Plano(s): " + String.join(", ", medico.getPlanosAtendidos())
                        );

                        mostrarCadastroSucesso();
                        stage.close();

                    } catch (Exception ex) {
                        ExceptionModal.popUp("Erro ao alterar dados.");
                    }
                });

                root.getChildren().add(salvarButton);
            }


            // =========================
            // PACIENTE
            // =========================
            if (user instanceof Paciente paciente) {

                TextField idadeField = new TextField(String.valueOf(paciente.getIdade()));
                idadeField.setPromptText("Idade");

                ComboBox<String> planoComboBox = new ComboBox<>();
                planoComboBox.getItems().addAll(
                        "Premium", "Plus", "Normal", "Ortodôntico", "Deluxe"
                );
                planoComboBox.setValue(paciente.getPlanoSaude());

                Label idadeLabel = new Label("Idade:");
                Label planoLabel = new Label("Plano:");
                idadeLabel.setTextFill(Paint.valueOf("white"));
                planoLabel.setTextFill(Paint.valueOf("white"));

                root.getChildren().addAll(
                        idadeLabel, idadeField,
                        planoLabel, planoComboBox
                );

                Button salvarButton = new Button("Salvar");
                salvarButton.setOnAction(ev -> {
                    try {
                        if (!idadeField.getText().matches("\\d+")) {
                            ExceptionModal.popUp("Idade inválida.");
                            return;
                        }

                        paciente.setIdade(Integer.parseInt(idadeField.getText()));

                        if (!senhaField.getText().isBlank()) {
                            paciente.setSenha(senhaField.getText());
                        }

                        paciente.setPlanoSaude(planoComboBox.getValue());

                        PacienteService.atualizar(paciente);

                        mostrarCadastroSucesso();
                        planoLabel.setText("Plano(s): " + paciente.getPlanoSaude());
                        stage.close();

                    } catch (Exception ex) {
                        ExceptionModal.popUp("Erro ao alterar dados.");
                    }
                });

                root.getChildren().add(salvarButton);
            }

            Scene scene = new Scene(root, 320, 350);
            stage.setScene(scene);

            Stage parentStage = (Stage) alterarDadosButton.getScene().getWindow();
            stage.initOwner(parentStage);

            stage.setResizable(false);
            stage.showAndWait();
        });

    }

    private void mostrarCadastroSucesso() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Sucesso");

        Label mensagem = new Label("Ação realizada com sucesso");
        mensagem.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;"
        );

        Button okButton = new Button("OK");
        okButton.setOnAction(e -> stage.close());

        VBox layout = new VBox(15);
        layout.getChildren().addAll(mensagem, okButton);
        layout.setStyle(
                "-fx-padding: 20;" +
                        "-fx-alignment: center;" +
                        "-fx-background-color: #2e7d32;" // verde sucesso
        );

        stage.setScene(new Scene(layout, 300, 120));
        stage.setResizable(false);
        stage.showAndWait();
    }

    private void renderizarOpcoesDeMedico() {
        helloLabel.setText(helloLabel.getText() + "Dr. " + user.getNome() + ".");
        planoLabel.setText(
                "Plano(s): " + String.join(", ", ((Medico) user).getPlanosAtendidos())
        );
    }

    private void renderizarOpcoesDePaciente(Paciente paciente) {
        helloLabel.setText(helloLabel.getText() + "Paciente " + paciente.getNome() + ".");
        planoLabel.setText(planoLabel.getText() + paciente.getPlanoSaude());
        Button agendarConsultaButton = new Button("AGENDAR CONSULTA");
        agendarConsultaButton.setOnAction(e -> {

            ConsultaService consultaService = new ConsultaService();

            Stage modal = new Stage();
            modal.setTitle("Agendar Consulta");
            modal.initModality(Modality.APPLICATION_MODAL);

            VBox root = new VBox(10);
            root.setPadding(new Insets(15));

            TextField pesquisaField = new TextField();
            pesquisaField.setPromptText("Pesquisar por nome ou especialidade");

            DatePicker dataPicker = new DatePicker();

            VBox listaMedicosVBox = new VBox(10);
            ScrollPane scrollPane = new ScrollPane(listaMedicosVBox);
            scrollPane.setFitToWidth(true);

            // ===== CARREGA MÉDICOS =====
            List<Medico> medicos = PacienteService.listarMedicos((Paciente) user);

            // ===== CARREGA AVALIAÇÕES =====
            List<Avaliacao> avaliacoes = new ArrayList<>();
            CSVUtil.ler("src/main/resources/com/uece/poo/sistema_hospitalar/dados/avaliacoes.csv")
                    .forEach(l -> avaliacoes.add(Avaliacao.fromCSV(l)));

            Runnable atualizarLista = () -> {

                listaMedicosVBox.getChildren().clear();
                String filtro = pesquisaField.getText().toLowerCase();
                String planoPaciente = ((Paciente) user).getPlanoSaude();

                for (Medico m : medicos) {
                    boolean atendePlano = !paciente.temPlano()
                            || m.getPlanosAtendidos().contains(paciente.getPlanoSaude());

                    if (!atendePlano) continue;

                    // ===== FILTRO POR TEXTO =====
                    if (!filtro.isBlank()
                            && !m.getNome().toLowerCase().contains(filtro)
                            && !m.getEspecialidade().toLowerCase().contains(filtro)) {
                        continue;
                    }

                    // ===== AVALIAÇÕES DO MÉDICO =====
                    List<Avaliacao> avaliacoesMedico = avaliacoes.stream()
                            .filter(a -> a.getMedico().getNome().equals(m.getNome()))
                            .toList();

                    int mediaEstrelas = avaliacoesMedico.isEmpty()
                            ? 0
                            : (int) avaliacoesMedico.stream()
                            .mapToInt(Avaliacao::getEstrelas)
                            .average()
                            .orElse(0);

                    String ultimasAvaliacoes = avaliacoesMedico.stream()
                            .limit(3)
                            .map(Avaliacao::getTexto)
                            .reduce("", (a, b) -> a + " | " + b);

                    // ===== CARD DO MÉDICO =====
                    VBox card = new VBox(5);
                    card.setStyle("""
                -fx-border-color: #ccc;
                -fx-padding: 10;
                -fx-border-radius: 5;
            """);

                    Label nome = new Label("Dr(a). " + m.getNome());
                    nome.setFont(Font.font(null, FontWeight.BOLD, 14));

                    Label especialidade = new Label("Especialidade: " + m.getEspecialidade());
                    Label estrelas = new Label("⭐".repeat(mediaEstrelas));

                    Label avaliacoesLabel = new Label(
                            ultimasAvaliacoes.isBlank()
                                    ? "Sem avaliações"
                                    : "Avaliações: " + ultimasAvaliacoes
                    );

                    Button agendarBtn = new Button("Agendar Consulta");

                    agendarBtn.setOnAction(ev -> {

                        if (dataPicker.getValue() == null) {
                            ExceptionModal.popUp("Selecione uma data.");
                            return;
                        }

                        Consulta consulta = consultaService.agendar(
                                m,
                                (Paciente) user,
                                dataPicker.getValue()
                        );

                        ExceptionModal.popUp(
                                consulta.estaAgendada()
                                        ? "Consulta agendada com sucesso!"
                                        : "Consulta adicionada à lista de espera."
                        );

                        modal.close();
                    });

                    card.getChildren().addAll(
                            nome,
                            especialidade,
                            estrelas,
                            avaliacoesLabel,
                            agendarBtn
                    );

                    listaMedicosVBox.getChildren().add(card);
                }

                if (listaMedicosVBox.getChildren().isEmpty()) {
                    listaMedicosVBox.getChildren().add(
                            new Label("Nenhum médico encontrado.")
                    );
                }
            };

            pesquisaField.textProperty().addListener((obs, o, n) -> atualizarLista.run());
            atualizarLista.run();

            root.getChildren().addAll(
                    pesquisaField,
                    dataPicker,
                    scrollPane
            );

            modal.setScene(new Scene(root, 650, 550));
            modal.showAndWait();
        });

        opcoesVBox.getChildren().add(agendarConsultaButton);

    }

    private void montarTelaMedico(VBox root, List<Consulta> consultas) {

        for (Consulta c : consultas) {

            VBox card = new VBox(5);
            card.setStyle("-fx-border-color: gray; -fx-padding: 10;");

            Label info = new Label(
                    "Paciente: " + c.getPaciente().getNome() +
                            "\nData: " + c.getData() +
                            "\nStatus: " + c.getStatus()
            );

            Button cancelar = new Button("Cancelar");
            cancelar.setDisable(!c.estaAgendada());

            cancelar.setOnAction(e -> {
                try {
                    ConsultaService.cancelar(c);
                    ExceptionModal.popUp("Consulta cancelada.");
                    root.getChildren().clear();
                    montarTelaMedico(root, consultas);
                } catch (Exception ex) {
                    ExceptionModal.popUp(ex.getMessage());
                }
            });

            Button realizar = new Button("Realizar Consulta");
            realizar.setDisable(!c.estaAgendada());

                realizar.setOnAction(e -> abrirModalRealizarConsulta(c));

            card.getChildren().addAll(info, cancelar, realizar);
            root.getChildren().add(card);
        }
    }

    private void abrirModalRealizarConsulta(Consulta c) {

        Stage stage = new Stage();
        stage.setTitle("Realizar Consulta");
        stage.initModality(Modality.APPLICATION_MODAL);

        VBox root = new VBox(10);
        root.setPadding(new Insets(15));

        Label medicoLabel = new Label("Médico: " + c.getMedico().getNome());
        Label pacienteLabel = new Label("Paciente: " + c.getPaciente().getNome());

        TextArea descricaoArea = new TextArea();
        descricaoArea.setPromptText(
                "Descreva a consulta:\n" +
                        "- Sintomas\n" +
                        "- Diagnóstico\n" +
                        "- Tratamento\n" +
                        "- Medicamentos\n" +
                        "- Exames"
        );
        descricaoArea.setWrapText(true);

        Label valorLabel = new Label();

        // ===== Definição do valor =====
        double valorBase = calcularValorConsulta(c.getMedico());

        if (c.getPaciente().temPlano()) {
            valorLabel.setText("Valor da consulta: R$ 0,00 (Plano de saúde)");
        } else {
            valorLabel.setText(String.format(
                    "Valor da consulta: R$ %.2f", valorBase
            ));
        }

        Button confirmarButton = new Button("Confirmar Consulta");
        Button cancelarButton = new Button("Cancelar");

        confirmarButton.setOnAction(e -> {
            try {
                if (descricaoArea.getText().isBlank()) {
                    ExceptionModal.popUp("Informe a descrição da consulta.");
                    return;
                }

                c.realizar(descricaoArea.getText(), valorBase);

                ConsultaService.atualizarConsulta(c);
                ConsultaService.realizarConsulta(c, c.getDescricao(), c.getValor());

                mostrarCadastroSucesso();
                stage.close();

            } catch (Exception ex) {
                ExceptionModal.popUp(ex.getMessage());
            }
        });

        cancelarButton.setOnAction(e -> stage.close());

        root.getChildren().addAll(
                medicoLabel,
                pacienteLabel,
                planoLabel,
                descricaoArea,
                valorLabel,
                new HBox(10, confirmarButton, cancelarButton)
        );

        stage.setScene(new Scene(root, 450, 400));
        stage.showAndWait();
    }

    private double calcularValorConsulta(Medico medico) {

        return switch (medico.getEspecialidade().toLowerCase()) {
            case "cardiologia" -> 250.0;
            case "ortopedia" -> 200.0;
            case "dermatologia" -> 180.0;
            case "pediatria" -> 150.0;
            default -> 120.0;
        };
    }

    private void atualizarConsultas() {
        consultasContainer.getChildren().clear();

        List<Consulta> consultas;

        if (user instanceof Paciente paciente) {
            consultas = ConsultaService.listarPorPaciente(paciente);
        } else if (user instanceof Medico medico) {
            consultas = ConsultaService.listarPorMedico(medico);
        } else {
            consultas = List.of();
        }

        if (consultas.isEmpty()) {
            Label vazio = new Label("Não há consultas");
            vazio.setStyle("-fx-text-fill: gray; -fx-font-size: 14px;");
            consultasContainer.getChildren().add(vazio);
            return;
        }

        for (Consulta c : consultas) {
            consultasContainer.getChildren().add(criarCardConsulta(c));
        }
    }

    private HBox criarCardConsulta(Consulta consulta) {

        Label info = new Label(
                "Médico: " + consulta.getMedico().getNome() +
                        " | Data: " + consulta.getData() +
                        " | Status: " + consulta.getStatus()
        );

        Button cancelarButton = new Button("Cancelar");
        cancelarButton.setDisable(!consulta.estaAgendada());

        cancelarButton.setOnAction(e -> {
            try {
                ConsultaService.cancelar(consulta);
                atualizarConsultas();
            } catch (Exception ex) {
                ExceptionModal.popUp(ex.getMessage());
            }
        });

        HBox card = new HBox(10);
        card.getChildren().addAll(info, cancelarButton);

        // ===============================
        // BOTÃO DE AVALIAÇÃO (PACIENTE)
        // ===============================
        if (user instanceof Paciente && consulta.isRealizada()) {

            Button avaliarButton = new Button("Avaliar Médico");

            avaliarButton.setOnAction(e -> abrirModalAvaliacao(consulta));

            card.getChildren().add(avaliarButton);
        }

        // ===============================
        // BOTÃO REALIZAR (MÉDICO)
        // ===============================
        if (user instanceof Medico && consulta.estaAgendada()) {

            Button realizarButton = new Button("Realizar");

            realizarButton.setOnAction(e -> {
                abrirModalRealizarConsulta(consulta);
                atualizarConsultas();
            });

            card.getChildren().add(realizarButton);
        }

        card.setStyle("""
        -fx-padding: 10;
        -fx-border-color: lightgray;
        -fx-border-radius: 5;
    """);

        return card;
    }

    private void abrirModalAvaliacao(Consulta consulta) {

        Stage stage = new Stage();
        stage.setTitle("Avaliar Médico");
        stage.initModality(Modality.APPLICATION_MODAL);

        VBox root = new VBox(10);
        root.setPadding(new Insets(15));
        root.setAlignment(Pos.CENTER_LEFT);

        Label medicoLabel = new Label(
                "Médico: Dr. " + consulta.getMedico().getNome()
        );
        medicoLabel.setFont(Font.font(null, FontWeight.BOLD, 14));

        // ===============================
        // SELETOR DE ESTRELAS
        // ===============================
        ComboBox<Integer> estrelasCombo = new ComboBox<>();
        estrelasCombo.getItems().addAll(1, 2, 3, 4, 5);
        estrelasCombo.setValue(5);

        Label estrelasLabel = new Label("Avaliação (1 a 5 estrelas):");

        // ===============================
        // COMENTÁRIO
        // ===============================
        TextArea comentarioArea = new TextArea();
        comentarioArea.setPromptText("Escreva um comentário sobre a consulta...");
        comentarioArea.setWrapText(true);

        Button confirmarButton = new Button("Confirmar Avaliação");
        Button cancelarButton = new Button("Cancelar");

        confirmarButton.setOnAction(e -> {
            try {
                if (comentarioArea.getText().isBlank()) {
                    ExceptionModal.popUp("O comentário não pode estar vazio.");
                    return;
                }

                AvaliacaoService.avaliar(
                        consulta.getMedico(),
                        estrelasCombo.getValue(),
                        comentarioArea.getText()
                );

                ExceptionModal.popUp("Avaliação registrada com sucesso!");
                stage.close();

            } catch (Exception ex) {
                ExceptionModal.popUp("Erro ao salvar avaliação.");
            }
        });

        cancelarButton.setOnAction(e -> stage.close());

        root.getChildren().addAll(
                medicoLabel,
                estrelasLabel,
                estrelasCombo,
                comentarioArea,
                new HBox(10, confirmarButton, cancelarButton)
        );

        stage.setScene(new Scene(root, 400, 350));
        stage.setResizable(false);
        stage.showAndWait();
    }


    private void montarTelaPaciente(VBox root, List<Consulta> consultas) {

        for (Consulta c : consultas) {

            VBox card = new VBox(5);
            card.setStyle("-fx-border-color: gray; -fx-padding: 10;");

            Label info = new Label(
                    "Médico: " + c.getMedico().getNome() +
                            "\nData: " + c.getData() +
                            "\nStatus: " + c.getStatus() +
                            "\nValor: R$ " + c.getValor()
            );

            card.getChildren().add(info);

            if (c.estaAgendada()) {
                Button cancelar = new Button("Desmarcar");
                cancelar.setOnAction(e -> {
                    try {
                        ConsultaService.cancelar(c);
                        atualizarTelaConsultas(root);
                    } catch (Exception ex) {
                        ExceptionModal.popUp(ex.getMessage());
                    }
                });
                card.getChildren().add(cancelar);
            }

            // AVALIAÇÃO
            if (c.isRealizada()) {
                Button avaliar = new Button("Avaliar Médico");
                avaliar.setOnAction(e -> abrirModalAvaliacao(c));
                card.getChildren().add(avaliar);
            }

            root.getChildren().add(card);
        }
    }


    private void atualizarTelaConsultas(VBox root) {
        root.getChildren().clear();

        if (user instanceof Paciente paciente) {
            List<Consulta> consultas = ConsultaService.listarPorPaciente(paciente);

            if (consultas.isEmpty()) {
                root.getChildren().add(criarLabelVazio());
            } else {
                montarTelaPaciente(root, consultas);
            }
        }

        if (user instanceof Medico medico) {
            List<Consulta> consultas = ConsultaService.listarPorMedico(medico);

            if (consultas.isEmpty()) {
                root.getChildren().add(criarLabelVazio());
            } else {
                montarTelaMedico(root, consultas);
            }
        }
    }

    private Label criarLabelVazio() {
        Label label = new Label("Não há consultas no momento");
        label.setStyle("-fx-text-fill: gray; -fx-font-size: 14px;");
        return label;
    }

    @FXML private void sair() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uece/poo/sistema_hospitalar/tela_de_inicio.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) opcoesVBox.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

}

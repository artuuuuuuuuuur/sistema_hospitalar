package com.uece.poo.sistema_hospitalar.controller;

import com.uece.poo.sistema_hospitalar.model.usuario.Medico;
import com.uece.poo.sistema_hospitalar.model.usuario.Paciente;
import com.uece.poo.sistema_hospitalar.util.CSVUtil;
import com.uece.poo.sistema_hospitalar.util.ExceptionModal;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TelaDeCadastroController {

    @FXML private Label tituloLabel;
    @FXML private TextField nomeField;
    @FXML private TextField idField;
    @FXML private PasswordField senhaField;
    @FXML private Button cadastrarButton;
    @FXML private AnchorPane cadastroAnchorPane;
    @FXML private VBox planosVBox;

    private VBox listaDePlanosVBox;
    private boolean isMedico;
    private List<String> planos;

    public void carregarDados(boolean isMedico) {
        this.isMedico = isMedico;
        this.planos = new CopyOnWriteArrayList<>();
        renderizarTela();
        cadastrarButton.setOnAction(e -> {
            cadstrarUsuario();
        });
    }

    private void renderizarTela() {
        tituloLabel.setText(tituloLabel.getText() + (isMedico ? "MÉDICO" : "PACIENTE"));
        VBox camposVBox = (VBox) cadastroAnchorPane.lookup("#camposVBox");
        if (isMedico) {
            adicionarCamposDeMedico(camposVBox);
        } else  {
            adicionarCamposDePaciente(camposVBox);
        }
    }

    private void cadstrarUsuario() {

        String nome = nomeField.getText();
        String id = idField.getText();
        String senha = senhaField.getText();

        // ===== Validações comuns =====
        if (nome == null || nome.isBlank()
                || id == null || id.isBlank()
                || senha == null || senha.isBlank()) {

            ExceptionModal.popUp("Preencha todos os campos obrigatórios.");
            return;
        }

        // ===== Validação do ID (CPF ou CRM) + duplicidade =====
        if (isMedico) {

            // CRM → 5 números
            if (!id.matches("\\d{5}")) {
                ExceptionModal.popUp("CRM inválido. Informe exatamente 5 números.");
                return;
            }

            if (idJaExiste(
                    "src/main/resources/com/uece/poo/sistema_hospitalar/dados/medicos.csv",
                    id)) {
                ExceptionModal.popUp("Já existe um médico cadastrado com este CRM.");
                return;
            }

        } else {

            // CPF → 11 números
            if (!id.matches("\\d{11}")) {
                ExceptionModal.popUp("CPF inválido. Informe exatamente 11 números.");
                return;
            }

            if (idJaExiste(
                    "src/main/resources/com/uece/poo/sistema_hospitalar/dados/pacientes.csv",
                    id)) {
                ExceptionModal.popUp("Já existe um paciente cadastrado com este CPF.");
                return;
            }
        }

        // ===== MÉDICO =====
        if (isMedico) {

            TextField especialidadeField =
                    (TextField) cadastroAnchorPane.lookup("#especialidadeField");

            if (especialidadeField == null || especialidadeField.getText().isBlank()) {
                ExceptionModal.popUp("Informe a especialidade do médico.");
                return;
            }

            if (planos == null || planos.isEmpty()) {
                ExceptionModal.popUp("Adicione pelo menos um plano de saúde.");
                return;
            }

            String especialidade = especialidadeField.getText();

            Medico medico = new Medico(
                    nome,
                    id,
                    senha,
                    especialidade,
                    planos
            );

            String linhaCSV =
                    medico.getNome() + "," +
                            medico.getId() + "," +
                            medico.getSenha() + "," +
                            medico.getEspecialidade() + "," +
                            String.join("|", medico.getPlanosAtendidos());

            CSVUtil.escrever(
                    "src/main/resources/com/uece/poo/sistema_hospitalar/dados/medicos.csv",
                    List.of(linhaCSV)
            );

        }
        // ===== PACIENTE =====
        else {

            TextField idadeField =
                    (TextField) cadastroAnchorPane.lookup("#idadeField");

            ComboBox<String> planoComboBox =
                    (ComboBox<String>) cadastroAnchorPane.lookup("#planoComboBox");

            if (idadeField == null || idadeField.getText().isBlank()) {
                ExceptionModal.popUp("Informe a idade.");
                return;
            }

            if (!idadeField.getText().matches("\\d+")) {
                ExceptionModal.popUp("A idade deve conter apenas números.");
                return;
            }

            if (planoComboBox == null || planoComboBox.getValue() == null) {
                ExceptionModal.popUp("Selecione um plano de saúde.");
                return;
            }

            int idade = Integer.parseInt(idadeField.getText());
            String planoSaude = planoComboBox.getValue();

            Paciente paciente = new Paciente(
                    nome,
                    id,
                    senha,
                    idade,
                    planoSaude
            );

            String linhaCSV =
                    paciente.getNome() + "," +
                            paciente.getId() + "," +
                            paciente.getSenha() + "," +
                            paciente.getIdade() + "," +
                            paciente.getPlanoSaude();

            CSVUtil.escrever(
                    "src/main/resources/com/uece/poo/sistema_hospitalar/dados/pacientes.csv",
                    List.of(linhaCSV)
            );
        }
        mostrarCadastroSucesso();
        Stage stage = (Stage) planosVBox.getScene().getWindow();
        stage.close();
        try {
            goToTelaInicial();
        } catch (IOException e) {
            ExceptionModal.popUp(e.getMessage());
        }
    }

    private boolean idJaExiste(String caminhoCSV, String id) {
        List<String[]> dados = CSVUtil.ler(caminhoCSV);

        for (String[] linha : dados) {
            if (linha.length > 1 && linha[1].equals(id)) {
                return true;
            }
        }
        return false;
    }

    private void mostrarCadastroSucesso() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Sucesso");

        Label mensagem = new Label("Cadastro efetuado com sucesso");
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


    private void adicionarCamposDeMedico(VBox camposVBox) {
        HBox especialidadeHBox = new HBox();
        Label especialidadeLabel = createStyledLabel("ESPECIALIDADE:");
        TextField textField = new TextField();
        textField.setId("especialidadeField");

        especialidadeHBox.getChildren().add(especialidadeLabel);
        especialidadeHBox.getChildren().add(textField);

        Button addPlano = new Button("Adicionar Plano de Saúde");
        addPlano.setOnAction(e -> {
            try {
                adicionarPlanoDoMedico();
            } catch (IOException ex) {
                ExceptionModal.popUp(ex.getMessage());
            }
        });
        Button limparPlanos = new Button("Limpar planos");
        limparPlanos.setOnAction(e -> {
            planos.clear();
            mostrarPlanosNaTela();
        });

        HBox buttonsHBox = new HBox();
        buttonsHBox.getChildren().add(addPlano);
        buttonsHBox.getChildren().add(limparPlanos);
        listaDePlanosVBox = new VBox();



        camposVBox.getChildren().add(especialidadeHBox);
        planosVBox.getChildren().add(buttonsHBox);
        planosVBox.getChildren().add(listaDePlanosVBox);
    }

    private void adicionarPlanoDoMedico() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/uece/poo/sistema_hospitalar/add_planos.fxml")
        );

        Parent root = loader.load();
        AddPlanosController controller = loader.getController();
        controller.carregarDados(this);

        Stage modalStage = new Stage();
        modalStage.setTitle("Adicionar Plano");
        modalStage.setScene(new Scene(root));

        modalStage.initModality(Modality.APPLICATION_MODAL);

        Stage parentStage = (Stage) cadastroAnchorPane.getScene().getWindow();
        modalStage.initOwner(parentStage);

        modalStage.showAndWait();
    }


    public void addPlanoNaLista(String plano) {

        if (plano == null || plano.isBlank()) {
            ExceptionModal.popUp("Selecione um plano válido.");
            return;
        }

        if (planoJaAdicionado(plano)) {
            ExceptionModal.popUp("O plano \"" + plano + "\" já foi adicionado.");
            return;
        }

        planos.add(plano);
        mostrarPlanosNaTela();
    }


    private void mostrarPlanosNaTela() {
        listaDePlanosVBox.getChildren().clear();
        for (String plano : planos) {
            Label label = new Label(plano);
            label.setTextFill(Paint.valueOf("white"));
            listaDePlanosVBox.getChildren().add(label);
        }
    }

    private Label createStyledLabel(String text) {
        Label label = new Label(text);
        label.setTextFill(Paint.valueOf("white"));
        label.setFont(Font.font("SansSerif", FontWeight.BOLD,15));
        return label;
    }

    private boolean planoJaAdicionado(String plano) {
        return planos.contains(plano);
    }

    private void adicionarCamposDePaciente(VBox camposVBox) {

        // ===== HBox IDADE =====
        HBox idadeHBox = new HBox();
        Label idadeLabel = createStyledLabel("IDADE: ");
        TextField idadeField = new TextField();
        idadeField.setId("idadeField");

        idadeHBox.getChildren().addAll(idadeLabel, idadeField);

        // ===== HBox PLANO =====
        HBox planoHBox = new HBox();
        Label planoLabel = createStyledLabel("PLANO: ");
        ComboBox<String> planoComboBox = new ComboBox<>();
        planoComboBox.setId("planoComboBox");

        // Caminho do CSV
        String caminho = "src/main/resources/com/uece/poo/sistema_hospitalar/dados/planos.csv";

        // Ler planos do CSV
        List<String[]> linhas = CSVUtil.ler(caminho);
        planoComboBox.getItems().addFirst("Não tenho");

        for (String[] linha : linhas) {
            if (linha.length > 0) {
                planoComboBox.getItems().add(linha[0]);
            }
        }

        planoHBox.getChildren().addAll(planoLabel, planoComboBox);

        // Adicionar à tela
        camposVBox.getChildren().add(idadeHBox);
        camposVBox.getChildren().add(planoHBox);
    }

    private void goToTelaInicial() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uece/poo/sistema_hospitalar/tela_de_inicio.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) tituloLabel.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }


}

package com.uece.poo.sistema_hospitalar.controller;

import com.uece.poo.sistema_hospitalar.model.usuario.Medico;
import com.uece.poo.sistema_hospitalar.model.usuario.Paciente;
import com.uece.poo.sistema_hospitalar.model.usuario.Usuario;
import com.uece.poo.sistema_hospitalar.util.CSVUtil;
import com.uece.poo.sistema_hospitalar.util.ExceptionModal;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class TelaDeLoginController {
    @FXML private Label tituloLabel;
    @FXML private TextField idTextField;
    @FXML private PasswordField senhaPasswordField;
    @FXML private Button loginButton;
    @FXML private Button cadastroButton;

    private boolean isMedico;

    public void carregarDados(boolean isMedico) {
        this.isMedico = isMedico;
        renderizarTela();

        cadastroButton.setOnAction(event -> {
            try {
                goToCadastro();
            } catch (IOException e) {
                ExceptionModal.popUp(e.getMessage());
            }
        });

        loginButton.setOnAction(event -> {
            Usuario user;
            if((user = logarUsuario(idTextField.getText(), senhaPasswordField.getText())) != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uece/poo/sistema_hospitalar/home.fxml"));
                    Parent root = loader.load();
                    HomeController controller = loader.getController();
                    controller.carregarDados(isMedico, user);
                    Stage stage = (Stage) tituloLabel.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (RuntimeException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void renderizarTela() {
        this.tituloLabel.setText(this.tituloLabel.getText() + (isMedico ? "MÉDICO" : "PACIENTE"));
    }

    private void goToCadastro() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uece/poo/sistema_hospitalar/cadastro.fxml"));
        Parent root = loader.load();
        TelaDeCadastroController controller = loader.getController();
        controller.carregarDados(isMedico);
        Stage stage = (Stage) tituloLabel.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private Usuario logarUsuario(String id, String senha) {

        try {
            // ===== Validações básicas =====
            if (id == null || id.isBlank() || senha == null || senha.isBlank()) {
                ExceptionModal.popUp("Informe ID e senha.");
                return null;
            }

            // ===== LOGIN MÉDICO =====
            if (isMedico) {

                List<String[]> dados = CSVUtil.ler(
                        "src/main/resources/com/uece/poo/sistema_hospitalar/dados/medicos.csv"
                );

                for (String[] linha : dados) {
                    // NOME,ID,SENHA,ESPECIALIDADE,PLANOS
                    if (linha.length < 5) continue;

                    String idCSV = linha[1];
                    String senhaCSV = linha[2];

                    if (idCSV.equals(id) && senhaCSV.equals(senha)) {

                        String nome = linha[0];
                        String especialidade = linha[3];
                        List<String> planos =
                                List.of(linha[4].split("\\|"));

                        return new Medico(
                                nome,
                                idCSV,
                                senhaCSV,
                                especialidade,
                                planos
                        );
                    }
                }

                ExceptionModal.popUp("CRM ou senha inválidos.");
                return null;
            }

            // ===== LOGIN PACIENTE =====
            else {

                List<String[]> dados = CSVUtil.ler(
                        "src/main/resources/com/uece/poo/sistema_hospitalar/dados/pacientes.csv"
                );

                for (String[] linha : dados) {
                    // NOME,ID,SENHA,IDADE,PLANO
                    if (linha.length < 5) continue;

                    String idCSV = linha[1];
                    String senhaCSV = linha[2];

                    if (idCSV.equals(id) && senhaCSV.equals(senha)) {

                        String nome = linha[0];
                        int idade = Integer.parseInt(linha[3]);
                        String plano = linha[4];

                        return new Paciente(
                                nome,
                                idCSV,
                                senhaCSV,
                                idade,
                                plano
                        );
                    }
                }

                ExceptionModal.popUp("CPF ou senha inválidos.");
                return null;
            }

        } catch (Exception e) {
            ExceptionModal.popUp("Erro ao realizar login: " + e.getMessage());
            return null;
        }
    }

    @FXML private void voltar() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uece/poo/sistema_hospitalar/tela_de_inicio.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) tituloLabel.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

}

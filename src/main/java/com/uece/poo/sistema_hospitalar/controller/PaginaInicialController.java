package com.uece.poo.sistema_hospitalar.controller;

import com.uece.poo.sistema_hospitalar.util.ExceptionModal;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PaginaInicialController implements Initializable {
    @FXML private Button medicoButton;
    @FXML private Button pacienteButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        medicoButton.setOnAction(ev -> {
            try {
                goToScreen(true);
            } catch (IOException e) {
                ExceptionModal.popUp(e.getMessage());
            }
        });

        pacienteButton.setOnAction(ev -> {
            try{
                goToScreen(false);
            } catch (IOException e) {
                ExceptionModal.popUp(e.getMessage());
            }
        });

    }

    private void goToScreen(boolean isMedico) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uece/poo/sistema_hospitalar/tela_de_login.fxml"));
        Parent root = loader.load();
        TelaDeLoginController controller = loader.getController();
        controller.carregarDados(isMedico);
        Stage stage = (Stage) medicoButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }


}

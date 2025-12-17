package com.uece.poo.sistema_hospitalar.controller;

import com.uece.poo.sistema_hospitalar.util.CSVUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AddPlanosController implements Initializable {

    @FXML private ComboBox<String> planosComboBox;
    @FXML private Button addPlanoButton;

    private TelaDeCadastroController controller;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        carregarPlanos();

        addPlanoButton.setOnAction(e -> {
            controller.addPlanoNaLista(planosComboBox.getValue());
            Stage stage = (Stage) addPlanoButton.getScene().getWindow();
            stage.close();
        });
    }

    public void carregarDados(TelaDeCadastroController controller) {
        this.controller = controller;
    }

    private void carregarPlanos() {
        List<String[]> dados =
                CSVUtil.ler("com/uece/poo/sistema_hospitalar/dados/planos.csv");

        ObservableList<String> planos = FXCollections.observableArrayList();

        for (String[] linha : dados) {
            if (linha.length > 0 && !linha[0].isBlank()) {
                planos.add(linha[0].trim());
            }
        }

        planosComboBox.setItems(planos);

        if (!planos.isEmpty()) {
            planosComboBox.getSelectionModel().selectFirst();
        }
    }
}

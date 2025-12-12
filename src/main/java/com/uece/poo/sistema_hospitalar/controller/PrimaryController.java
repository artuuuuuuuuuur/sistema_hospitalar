package com.uece.poo.sistema_hospitalar.controller;

import java.io.IOException;

import com.uece.poo.sistema_hospitalar.App;
import javafx.fxml.FXML;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}

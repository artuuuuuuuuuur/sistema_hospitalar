package com.uece.poo.sistema_hospitalar.util;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ExceptionModal {

    private ExceptionModal() {}

    public static void popUp(String msg) {
        Platform.runLater(() -> {
            Stage alerta = new Stage();
            alerta.setTitle("Erro");

            Label message = new Label(msg);
            message.setStyle("-fx-font-size: 16px; -fx-text-fill: red;");

            Button ok = new Button("OK");
            ok.setOnAction(e -> alerta.close());

            VBox layout = new VBox(15, message, ok);
            layout.setAlignment(Pos.CENTER);
            layout.setPadding(new Insets(20));

            alerta.setScene(new Scene(layout, 550, 150));
            alerta.initModality(Modality.APPLICATION_MODAL);
            alerta.showAndWait();
        });
    }
}

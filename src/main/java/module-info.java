module com.uece.poo.sistema_hospitalar {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.uece.poo.sistema_hospitalar to javafx.fxml;
    exports com.uece.poo.sistema_hospitalar;
    opens com.uece.poo.sistema_hospitalar.controller to javafx.fxml;
    exports com.uece.poo.sistema_hospitalar.controller;
}

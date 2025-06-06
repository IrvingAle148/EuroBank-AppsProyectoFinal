module controlador {
    requires javafx.controls;
    requires javafx.fxml;

    opens controlador to javafx.fxml;
    exports controlador ;
    exports vista;
    opens vista to javafx.fxml;
}
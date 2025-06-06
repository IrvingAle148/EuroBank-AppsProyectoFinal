module controlador {
    requires javafx.controls;
    requires javafx.fxml;

    opens controlador to javafx.fxml;
    exports controlador ;
    opens vista to javafx.fxml;
    exports vista.cliente;
    opens vista.cliente to javafx.fxml;
}
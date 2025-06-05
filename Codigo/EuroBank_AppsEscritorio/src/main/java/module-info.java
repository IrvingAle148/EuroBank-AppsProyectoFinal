module controlador {
    requires javafx.controls;
    requires javafx.fxml;

    opens controlador to javafx.fxml;
    exports controlador ;
}
module controlador {
    requires javafx.controls;
    requires javafx.fxml;


    exports vista;
    exports vista.empleados;
    exports vista.cliente;
    exports vista.formularioAgregarEditar;
    exports main;

    opens main;
    opens vista;
    opens vista.empleados;
    opens vista.cliente;
    opens vista.formularioAgregarEditar;
}

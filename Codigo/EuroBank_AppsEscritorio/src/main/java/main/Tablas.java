package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Tablas extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/TransaccionesMainView.fxml"));

        //Ingresar Directamente a otras tablas
         //"/FXML/ClientesMainView.fxml"
         //"/FXML/CuentasMainView.fxml"
         //"/FXML/SucursalesMainView.fxml"
         //"/FXML/EmpleadosMainView.fxml"
        // "/FXML/TransaccionesMainView.fxml"

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("EuroBank - Sistema de Gestión Bancaria");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(200);
        primaryStage.setMinHeight(250);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

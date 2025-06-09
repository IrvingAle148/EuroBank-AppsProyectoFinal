package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Login extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/LoginMainView.fxml"));

        Scene scene = new Scene(root, 400, 450);
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

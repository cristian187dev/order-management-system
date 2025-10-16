package edu.oms;

import edu.oms.vista.VentanaPrincipal;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        VentanaPrincipal ventanaPrincipal = new VentanaPrincipal(primaryStage);
        ventanaPrincipal.mostrar();
    }

    public static void main(String[] args) {
        launch(args); // Lanzo JavaFX
    }
}

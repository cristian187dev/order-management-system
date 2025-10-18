package edu.oms.vista;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VentanaPrincipal {

    private final Stage stage;
        //Constructor
    public VentanaPrincipal(Stage stage) {
        this.stage = stage;
    }

    public void mostrar() {
        // Botones
        Button btnClientes = new Button("Clientes");
        Button btnSalir = new Button("Salir");

        // Acciones
        btnClientes.setOnAction(e -> {
            VentanaClientes ventanaClientes = new VentanaClientes(stage);
            ventanaClientes.mostrar();
        });

        btnSalir.setOnAction(e -> stage.close());

        // Diseño
        VBox layout = new VBox(20);
        layout.setStyle("-fx-padding: 30; -fx-alignment: center; -fx-background-color: #f0f0f0;");
        layout.getChildren().addAll(btnClientes, btnSalir);

        // Escena
        Scene scene = new Scene(layout, 1200, 800);
        stage.setTitle("Sistema de Gestión - Menú Principal");
        stage.setScene(scene);
        stage.show();
    }
}

package edu.oms.vista;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VentanaClientes {

    private final Stage stage;

    public VentanaClientes(Stage stage) {
        this.stage = stage;
    }

    public void mostrar() {
        // Botones
        Button btnAgregar = new Button("Agregar Cliente");
        Button btnListar = new Button("Listar Clientes");
        Button btnVolver = new Button("Volver al Menú Principal");

        // Acciones
        btnAgregar.setOnAction(e -> {
            VentanaAgregarCliente ventanaAgregar = new VentanaAgregarCliente(stage);
            ventanaAgregar.mostrar();
        });

        btnListar.setOnAction(e -> {
            //mensaje de prueba
            System.out.println("Listar clientes...");
        });

        btnVolver.setOnAction(e -> {
            VentanaPrincipal ventanaPrincipal = new VentanaPrincipal(stage);
            ventanaPrincipal.mostrar();
        });

        // Diseño
        VBox layout = new VBox(20);
        layout.setStyle("-fx-padding: 30; -fx-alignment: center; -fx-background-color: #e9f5ff;");
        layout.getChildren().addAll(btnAgregar, btnListar, btnVolver);

        // Escena
        Scene scene = new Scene(layout, 400, 300);
        stage.setTitle("Gestión de Clientes");
        stage.setScene(scene);
        stage.show();
    }
}

package edu.oms.vista;

import edu.oms.controlador.ClienteControlador;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VentanaClientes {

    private final Stage stage;
    private final ClienteControlador controlador = new ClienteControlador();

    public VentanaClientes(Stage stage) {
        this.stage = stage;
    }

    public void mostrar() {
        // Botones
        Button btnAgregar = new Button("Agregar Cliente");
        Button btnListar = new Button("Listar Clientes (Activos / Inactivos)");
        Button btnVolver = new Button("Volver al Menú Principal");

        // Acciones
        btnAgregar.setOnAction(e -> {
            VentanaAgregarCliente ventanaAgregar = new VentanaAgregarCliente(stage);
            ventanaAgregar.mostrar();
        });

        btnListar.setOnAction(e -> {
            VentanaListarClientes ventanaListar = new VentanaListarClientes(stage);
            ventanaListar.mostrar();
        });

        btnVolver.setOnAction(e -> {
            VentanaPrincipal ventanaPrincipal = new VentanaPrincipal(stage);
            ventanaPrincipal.mostrar();
        });

        // Diseño
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-background-color: #e9f5ff; -fx-alignment: center;");
        layout.getChildren().addAll(btnAgregar, btnListar, btnVolver);

        // Escena
        Scene scene = new Scene(layout, 1200, 800);
        stage.setTitle("Gestión de Clientes");
        stage.setScene(scene);
        stage.show();
    }
}

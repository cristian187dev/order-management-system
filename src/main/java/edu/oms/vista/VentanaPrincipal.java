package edu.oms.vista;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VentanaPrincipal {

    private final Stage stage;

    public VentanaPrincipal(Stage stage) {
        this.stage = stage;
    }

    public void mostrar() {
        // Botones principales
        Button btnClientes = new Button("Clientes");
        Button btnProductos = new Button("Productos");
        Button btnPedidos = new Button("Pedidos");
        Button btnSalir = new Button("Salir");

        // Acciones
        btnClientes.setOnAction(e -> new VentanaClientes(stage).mostrar());
        btnProductos.setOnAction(e -> new VentanaProductos(stage).mostrar());
        btnPedidos.setOnAction(e -> new VentanaPedidos(stage).mostrar());
        btnSalir.setOnAction(e -> stage.close());

        // Diseño
        VBox layout = new VBox(25);
        layout.setStyle("""
            -fx-padding: 40;
            -fx-alignment: center;
            -fx-background-color: #f0f0f0;
        """);
        layout.getChildren().addAll(btnClientes, btnProductos, btnPedidos, btnSalir);

        // Escena
        Scene scene = new Scene(layout, 1200, 800);
        stage.setTitle("Sistema de Gestión - Menú Principal");
        stage.setScene(scene);
        stage.show();
    }
}

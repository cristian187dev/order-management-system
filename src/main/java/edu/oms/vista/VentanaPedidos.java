package edu.oms.vista;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VentanaPedidos {

    private final Stage stage;

    public VentanaPedidos(Stage stage) {
        this.stage = stage;
    }

    public void mostrar() {
        // Botones
        Button btnAgregar = new Button("Agregar Pedido");
        Button btnListar = new Button("Listar Pedidos");
        Button btnVolver = new Button("Volver al Menú Principal");

        // Acciones
        btnAgregar.setOnAction(e -> new VentanaAgregarPedido(stage).mostrar());
        btnListar.setOnAction(e -> new VentanaListarPedidos(stage).mostrar());
        btnVolver.setOnAction(e -> new VentanaPrincipal(stage).mostrar());

        // Diseño
        VBox layout = new VBox(25);
        layout.setStyle("""
            -fx-padding: 40;
            -fx-alignment: center;
            -fx-background-color: #e9f5ff;
        """);
        layout.getChildren().addAll(btnAgregar, btnListar, btnVolver);

        // Escena
        Scene scene = new Scene(layout, 1200, 800);
        stage.setTitle("Gestión de Pedidos");
        stage.setScene(scene);
        stage.show();
    }
}


package edu.oms.vista;

import javafx.application.Platform;
import javafx.geometry.Insets;
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

        Button btnClientes = new Button("Clientes");
        btnClientes.setOnAction(e -> new VentanaClientes(stage).mostrar());

        Button btnProductos = new Button("Productos");
        btnProductos.setOnAction(e -> new VentanaProductos(stage).mostrar());

        Button btnPedidos = new Button("Pedidos");
        btnPedidos.setOnAction(e -> new VentanaPedidos(stage).mostrar());

        Button btnFacturacion = new Button("Facturación");
        btnFacturacion.setOnAction(e -> new VentanaFacturacion(stage).mostrar());

        Button btnSalir = new Button("Salir");
        btnSalir.setOnAction(e -> Platform.exit());

        VBox root = new VBox(30, btnClientes, btnProductos, btnPedidos, btnFacturacion, btnSalir);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #e9f5ff; -fx-alignment: center;");

        Scene scene = new Scene(root, 1200, 800);

        stage.setScene(scene);
        stage.setTitle("Menú Principal");
        stage.show();
    }
}
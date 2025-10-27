package edu.oms.vista;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VentanaPreciosBase {

    private final Stage stage;

    public VentanaPreciosBase(Stage stage) {
        this.stage = stage;
    }

    public void mostrar() {
        Button btnAgregar = new Button("Agregar Precio Base");
        Button btnListar = new Button("Listar Precios Base");
        Button btnVolver = new Button("Volver");

        btnAgregar.setOnAction(e -> new VentanaAgregarPrecioBase(stage).mostrar());
        btnListar.setOnAction(e -> new VentanaListarPreciosBase(stage).mostrar());
        btnVolver.setOnAction(e -> new VentanaProductos(stage).mostrar());

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-alignment: center; -fx-background-color: #f8f9fa;");
        layout.getChildren().addAll(btnAgregar, btnListar, btnVolver);

        stage.setScene(new Scene(layout, 800, 600));
        stage.setTitle("Gestión de Precios Base");
        stage.show();
    }
}

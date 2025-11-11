package edu.oms.vista;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VentanaPreciosEspeciales {

    private final Stage stage;

    public VentanaPreciosEspeciales(Stage stage) {
        this.stage = stage;
    }

    public void mostrar() {
        Button btnAgregar = new Button("Agregar Precio Especial");
        Button btnListar = new Button("Listar Precios Especiales");
        Button btnVolver = new Button("Volver");

        btnAgregar.setOnAction(e -> new VentanaAgregarPrecioEspecial(stage).mostrar());
        btnListar.setOnAction(e -> new VentanaListarPreciosEspeciales(stage).mostrar());
        btnVolver.setOnAction(e -> new VentanaProductos(stage).mostrar());

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-alignment: center; -fx-background-color: #f8f9fa;");
        layout.getChildren().addAll(btnAgregar, btnListar, btnVolver);

        stage.setScene(new Scene(layout, 1200, 800));
        stage.setTitle("Gestión de Precios Especiales");
        stage.show();
    }
}

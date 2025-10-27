package edu.oms.vista;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VentanaProductos {

    private final Stage stage;

    public VentanaProductos(Stage stage) {
        this.stage = stage;
    }

    public void mostrar() {
        Button btnAgregar = new Button("Agregar Producto");
        Button btnListar = new Button("Listar Productos");
        Button btnPreciosBase = new Button("Precios Base Productos");
        Button btnPreciosEspeciales = new Button("Precios Especiales por Cliente");
        Button btnVolver = new Button("Volver al Menú Principal");

        btnAgregar.setOnAction(e -> new VentanaAgregarProducto(stage).mostrar());
        btnListar.setOnAction(e -> new VentanaListarProductos(stage).mostrar());
        btnPreciosBase.setOnAction(e -> new VentanaPreciosBase(stage).mostrar());
        btnPreciosEspeciales.setOnAction(e -> new VentanaPreciosEspeciales(stage).mostrar());
        btnVolver.setOnAction(e -> new VentanaPrincipal(stage).mostrar());

        VBox layout = new VBox(20);
        layout.setStyle("-fx-padding: 30; -fx-alignment: center; -fx-background-color: #f0f0f0;");
        layout.getChildren().addAll(btnAgregar, btnListar, btnPreciosBase, btnPreciosEspeciales, btnVolver);

        stage.setScene(new Scene(layout, 1200, 800));
        stage.setTitle("Gestión de Productos");
        stage.show();
    }
}

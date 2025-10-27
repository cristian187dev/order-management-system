package edu.oms.vista;

import edu.oms.controlador.ProductoControlador;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class VentanaAgregarProducto {
    private final Stage stage;
    private final ProductoControlador controlador = new ProductoControlador();

    public VentanaAgregarProducto(Stage stage) {
        this.stage = stage;
    }

    public void mostrar() {
        Label lblTitulo = new Label("Agregar Producto");
        Label lblNombre = new Label("Nombre del producto:");
        Label lblUnidad = new Label("Unidad de medida:");
        Label lblStock = new Label("Stock actual:");

        TextField txtNombre = new TextField();
        TextField txtUnidad = new TextField();
        TextField txtStock = new TextField();

        Button btnGuardar = new Button("Guardar");
        Button btnVolver = new Button("Volver");

        btnGuardar.setOnAction(e -> {
            try {
                double stock = txtStock.getText().isBlank() ? 0.0 : Double.parseDouble(txtStock.getText());
                controlador.agregarProducto(txtNombre.getText(), txtUnidad.getText(), stock);

                new Alert(Alert.AlertType.INFORMATION, "Producto guardado correctamente.").showAndWait();
                txtNombre.clear();
                txtUnidad.clear();
                txtStock.clear();

            } catch (NumberFormatException ex) {
                new Alert(Alert.AlertType.WARNING, "El stock debe ser un número.").showAndWait();
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Error: " + ex.getMessage()).showAndWait();
            }
        });

        btnVolver.setOnAction(e -> new VentanaProductos(stage).mostrar());

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);
        grid.add(lblTitulo, 0, 0, 2, 1);
        grid.add(lblNombre, 0, 1); grid.add(txtNombre, 1, 1);
        grid.add(lblUnidad, 0, 2); grid.add(txtUnidad, 1, 2);
        grid.add(lblStock, 0, 3); grid.add(txtStock, 1, 3);
        grid.add(btnGuardar, 0, 4); grid.add(btnVolver, 1, 4);

        stage.setScene(new Scene(grid, 520, 300));
        stage.setTitle("Agregar Producto");
        stage.show();
    }
}

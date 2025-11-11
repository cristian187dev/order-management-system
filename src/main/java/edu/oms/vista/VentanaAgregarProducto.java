package edu.oms.vista;

import edu.oms.controlador.ProductoControlador;
import edu.oms.modelo.UnidadMedida;
import edu.oms.servicio.UnidadMedidaServicio;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class VentanaAgregarProducto {
    private final Stage stage;
    private final ProductoControlador controlador = new ProductoControlador();
    private final UnidadMedidaServicio unidadServicio = new UnidadMedidaServicio();

    public VentanaAgregarProducto(Stage stage) {
        this.stage = stage;
    }

    public void mostrar() {
        Label lblNombre = new Label("Nombre del Producto:");
        Label lblUnidad = new Label("Unidad de Medida:");
        Label lblStock = new Label("Stock Inicial:");

        TextField txtNombre = new TextField();
        TextField txtStock = new TextField();

        ComboBox<UnidadMedida> cmbUnidad = new ComboBox<>();
        try {
            cmbUnidad.setItems(FXCollections.observableArrayList(unidadServicio.listarActivas()));
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error al cargar unidades: " + e.getMessage()).showAndWait();
        }

        Button btnGuardar = new Button("Guardar");
        Button btnVolver = new Button("Volver");

        btnGuardar.setOnAction(e -> {
            try {
                UnidadMedida seleccionada = cmbUnidad.getValue();
                if (seleccionada == null) {
                    new Alert(Alert.AlertType.WARNING, "Debe seleccionar una unidad.").showAndWait();
                    return;
                }

                controlador.agregarProducto(
                        txtNombre.getText(),
                        seleccionada.getIdUnidad(),
                        Double.parseDouble(txtStock.getText())
                );

                new Alert(Alert.AlertType.INFORMATION, "Producto agregado correctamente.").showAndWait();
                txtNombre.clear();
                txtStock.clear();
                cmbUnidad.getSelectionModel().clearSelection();

            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Error al guardar: " + ex.getMessage()).showAndWait();
            }
        });

        btnVolver.setOnAction(e -> new VentanaProductos(stage).mostrar());

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.add(lblNombre, 0, 0);
        grid.add(txtNombre, 1, 0);
        grid.add(lblUnidad, 0, 1);
        grid.add(cmbUnidad, 1, 1);
        grid.add(lblStock, 0, 2);
        grid.add(txtStock, 1, 2);
        grid.add(btnGuardar, 0, 3);
        grid.add(btnVolver, 1, 3);

        stage.setScene(new Scene(grid, 500, 300));
        stage.setTitle("Agregar Producto");
        stage.show();
    }
}

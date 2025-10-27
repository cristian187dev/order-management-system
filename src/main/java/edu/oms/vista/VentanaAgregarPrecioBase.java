package edu.oms.vista;

import edu.oms.controlador.PrecioProductoBaseControlador;
import edu.oms.controlador.ProductoControlador;
import edu.oms.modelo.Producto;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class VentanaAgregarPrecioBase {

    private final Stage stage;
    private final PrecioProductoBaseControlador controlador = new PrecioProductoBaseControlador();
    private final ProductoControlador productoControlador = new ProductoControlador();

    public VentanaAgregarPrecioBase(Stage stage) {
        this.stage = stage;
    }

    public void mostrar() {
        Label lblTitulo = new Label("Agregar Precio Base de Producto");
        Label lblProducto = new Label("Producto:");
        Label lblPrecio = new Label("Precio Base:");
        Label lblInicio = new Label("Fecha Inicio:");

        ComboBox<Producto> cmbProducto = new ComboBox<>();
        TextField txtPrecio = new TextField();
        DatePicker dpInicio = new DatePicker(LocalDate.now());

        Button btnGuardar = new Button("Guardar");
        Button btnVolver = new Button("Volver");

        // Cargar productos desde BD
        try {
            List<Producto> productos = productoControlador.listarProductos();
            cmbProducto.setItems(FXCollections.observableArrayList(productos));
            cmbProducto.setPromptText("Seleccione un producto");
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error al cargar productos: " + e.getMessage()).showAndWait();
        }

        cmbProducto.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Producto item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombreProducto());
            }
        });
        cmbProducto.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Producto item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombreProducto());
            }
        });

        btnGuardar.setOnAction(e -> {
            try {
                Producto seleccionado = cmbProducto.getValue();
                if (seleccionado == null) throw new IllegalArgumentException("Seleccione un producto.");
                double precio = Double.parseDouble(txtPrecio.getText());

                controlador.agregarPrecioBase(seleccionado.getIdProducto(), precio, dpInicio.getValue(), null);

                new Alert(Alert.AlertType.INFORMATION, "Precio base guardado correctamente.").showAndWait();
                txtPrecio.clear();
                dpInicio.setValue(LocalDate.now());
                cmbProducto.getSelectionModel().clearSelection();

            } catch (NumberFormatException ex) {
                new Alert(Alert.AlertType.WARNING, "El precio debe ser numérico.").showAndWait();
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Error: " + ex.getMessage()).showAndWait();
            }
        });

        btnVolver.setOnAction(e -> new VentanaPreciosBase(stage).mostrar());

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);
        grid.add(lblTitulo, 0, 0, 2, 1);
        grid.add(lblProducto, 0, 1); grid.add(cmbProducto, 1, 1);
        grid.add(lblPrecio, 0, 2); grid.add(txtPrecio, 1, 2);
        grid.add(lblInicio, 0, 3); grid.add(dpInicio, 1, 3);
        grid.add(btnGuardar, 0, 4); grid.add(btnVolver, 1, 4);

        stage.setScene(new Scene(grid, 500, 350));
        stage.setTitle("Agregar Precio Base");
        stage.show();
    }
}

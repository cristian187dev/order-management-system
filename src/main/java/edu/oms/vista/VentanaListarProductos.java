package edu.oms.vista;

import edu.oms.controlador.ProductoControlador;
import edu.oms.modelo.Producto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.sql.SQLException;

public class VentanaListarProductos {
    private final Stage stage;
    private final ProductoControlador controlador = new ProductoControlador();

    public VentanaListarProductos(Stage stage) {
        this.stage = stage;
    }

    public void mostrar() {
        TableView<Producto> tabla = new TableView<>();

        TableColumn<Producto, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getIdProducto()).asObject());

        TableColumn<Producto, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNombreProducto()));

        TableColumn<Producto, String> colUnidad = new TableColumn<>("Unidad");
        colUnidad.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getNombreUnidad() + " (" + c.getValue().getAbreviaturaUnidad() + ")"
        ));

        TableColumn<Producto, Double> colStock = new TableColumn<>("Stock");
        colStock.setCellValueFactory(c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getStockActual()).asObject());

        TableColumn<Producto, Void> colAcciones = new TableColumn<>("Acciones");
        colAcciones.setCellFactory(param -> new TableCell<>() {
            private final Button btnBorrar = new Button("Borrar");

            {
                btnBorrar.setOnAction(e -> {
                    Producto producto = getTableView().getItems().get(getIndex());
                    try {
                        controlador.eliminarProducto(producto.getIdProducto());
                        getTableView().getItems().remove(producto);
                    } catch (Exception ex) {
                        new Alert(Alert.AlertType.ERROR, "Error al eliminar: " + ex.getMessage()).showAndWait();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnBorrar);
            }
        });

        tabla.getColumns().addAll(colId, colNombre, colUnidad, colStock, colAcciones);

        try {
            ObservableList<Producto> datos = FXCollections.observableArrayList(controlador.listarProductos());
            tabla.setItems(datos);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error al cargar productos: " + e.getMessage()).showAndWait();
        }

        Button btnVolver = new Button("Volver");
        btnVolver.setOnAction(e -> new VentanaProductos(stage).mostrar());

        BorderPane root = new BorderPane();
        root.setCenter(tabla);
        root.setBottom(btnVolver);

        stage.setScene(new Scene(root, 800, 400));
        stage.setTitle("Listado de Productos");
        stage.show();
    }
}

package edu.oms.vista;

import edu.oms.controlador.ProductoControlador;
import edu.oms.modelo.Producto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
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
        colId.setPrefWidth(50);

        TableColumn<Producto, String> colNombre = new TableColumn<>("Producto");
        colNombre.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNombreProducto()));
        colNombre.setPrefWidth(200);

        TableColumn<Producto, String> colUnidad = new TableColumn<>("Unidad");
        colUnidad.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getUnidadMedida()));
        colUnidad.setPrefWidth(100);

        TableColumn<Producto, Double> colStock = new TableColumn<>("Stock");
        colStock.setCellValueFactory(c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getStockActual()).asObject());
        colStock.setPrefWidth(100);

        TableColumn<Producto, Void> colAcciones = new TableColumn<>("Acción");
        colAcciones.setCellFactory(param -> new TableCell<>() {
            private final Button btnBorrar = new Button("Borrar");
            {
                btnBorrar.setOnAction(e -> {
                    Producto p = getTableView().getItems().get(getIndex());
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                            "¿Eliminar el producto \"" + p.getNombreProducto() + "\"?",
                            ButtonType.YES, ButtonType.NO);
                    confirm.setHeaderText(null);
                    confirm.showAndWait().ifPresent(bt -> {
                        if (bt == ButtonType.YES) {
                            try {
                                controlador.eliminarProducto(p.getIdProducto());
                                getTableView().getItems().remove(p);
                                new Alert(Alert.AlertType.INFORMATION, "Producto eliminado correctamente").showAndWait();
                            } catch (SQLException ex) {
                                new Alert(Alert.AlertType.ERROR, "Error al eliminar: " + ex.getMessage()).showAndWait();
                            }
                        }
                    });
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnBorrar);
            }
        });
        colAcciones.setPrefWidth(100);

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
        root.setPadding(new Insets(15));
        root.setCenter(tabla);
        root.setBottom(btnVolver);
        BorderPane.setMargin(btnVolver, new Insets(10));

        stage.setScene(new Scene(root, 700, 450));
        stage.setTitle("Listado de Productos");
        stage.show();
    }
}

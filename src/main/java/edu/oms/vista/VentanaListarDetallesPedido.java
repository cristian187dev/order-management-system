package edu.oms.vista;

import edu.oms.controlador.DetallePedidoControlador;
import edu.oms.modelo.DetallePedido;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.sql.SQLException;

public class VentanaListarDetallesPedido {

    private final Stage stage;
    private final int idPedido;
    private final DetallePedidoControlador controlador = new DetallePedidoControlador();

    public VentanaListarDetallesPedido(Stage stage, int idPedido) {
        this.stage = stage;
        this.idPedido = idPedido;
    }

    public void mostrar() {
        TableView<DetallePedido> tabla = new TableView<>();

        TableColumn<DetallePedido, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getIdDetalle()).asObject());
        colId.setPrefWidth(60);

        TableColumn<DetallePedido, String> colProducto = new TableColumn<>("Producto");
        colProducto.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombreProducto()));
        colProducto.setPrefWidth(200);

        TableColumn<DetallePedido, Double> colCantidad = new TableColumn<>("Cantidad");
        colCantidad.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getCantidad()).asObject());
        colCantidad.setPrefWidth(100);

        TableColumn<DetallePedido, Double> colPrecio = new TableColumn<>("Precio Unitario");
        colPrecio.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getPrecioUnitario()).asObject());
        colPrecio.setPrefWidth(120);

        TableColumn<DetallePedido, Double> colSubtotal = new TableColumn<>("Subtotal");
        colSubtotal.setCellValueFactory(c ->
                new SimpleDoubleProperty(c.getValue().getCantidad() * c.getValue().getPrecioUnitario()).asObject());
        colSubtotal.setPrefWidth(120);

        TableColumn<DetallePedido, Void> colAcciones = new TableColumn<>("Acciones");
        colAcciones.setCellFactory(col -> new TableCell<>() {
            private final Button btnEliminar = new Button("Eliminar");
            private final HBox box = new HBox(5, btnEliminar);

            {
                btnEliminar.setOnAction(e -> {
                    DetallePedido detalle = getTableView().getItems().get(getIndex());
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                            "¿Eliminar el producto '" + detalle.getNombreProducto() + "' del pedido?",
                            ButtonType.YES, ButtonType.NO);
                    confirm.showAndWait().ifPresent(resp -> {
                        if (resp == ButtonType.YES) {
                            try {
                                controlador.eliminarDetalle(detalle.getIdDetalle());
                                getTableView().getItems().remove(detalle);
                            } catch (SQLException ex) {
                                new Alert(Alert.AlertType.ERROR,
                                        "Error al eliminar detalle: " + ex.getMessage()).showAndWait();
                            }
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else setGraphic(box);
            }
        });
        colAcciones.setPrefWidth(120);

        tabla.getColumns().addAll(colId, colProducto, colCantidad, colPrecio, colSubtotal, colAcciones);

        try {
            ObservableList<DetallePedido> datos =
                    FXCollections.observableArrayList(controlador.listarDetalles(idPedido));
            tabla.setItems(datos);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error al cargar detalles: " + e.getMessage()).showAndWait();
        }

        Button btnAgregar = new Button("Agregar Producto");
        Button btnVolver = new Button("Volver");

        btnAgregar.setOnAction(e -> new VentanaAgregarDetallePedido(stage, idPedido).mostrar());
        btnVolver.setOnAction(e -> new VentanaListarPedidos(stage).mostrar());

        HBox acciones = new HBox(10, btnAgregar, btnVolver);
        acciones.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));
        root.setCenter(tabla);
        root.setBottom(acciones);

        stage.setScene(new Scene(root, 800, 450));
        stage.setTitle("Detalles del Pedido #" + idPedido);
        stage.show();
    }
}

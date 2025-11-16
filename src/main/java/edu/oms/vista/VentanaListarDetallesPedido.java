package edu.oms.vista;

import edu.oms.controlador.DetallePedidoControlador;
import edu.oms.controlador.ProductoBolsaControlador;
import edu.oms.modelo.DetallePedido;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VentanaListarDetallesPedido {

    private final Stage stage;
    private final int idPedido;
    private final int idCliente;
    private final DetallePedidoControlador controlador = new DetallePedidoControlador();
    private final ProductoBolsaControlador productoBolsaControlador = new ProductoBolsaControlador();

    private Set<Integer> productosQueUsanBolsas = new HashSet<>();

    public VentanaListarDetallesPedido(Stage stage, int idPedido, int idCliente) {
        this.stage = stage;
        this.idPedido = idPedido;
        this.idCliente = idCliente;
    }

    public void mostrar() {
        TableView<DetallePedido> tabla = new TableView<>();

        // Cargo configuración de productos que usan bolsas
        recargarConfigBolsas();

        TableColumn<DetallePedido, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(c ->
                new SimpleIntegerProperty(c.getValue().getIdDetalle()).asObject());
        colId.setPrefWidth(60);

        TableColumn<DetallePedido, String> colProducto = new TableColumn<>("Producto");
        colProducto.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getNombreProducto()));
        colProducto.setPrefWidth(220);

        TableColumn<DetallePedido, Double> colCantidad = new TableColumn<>("Cantidad");
        colCantidad.setCellValueFactory(c ->
                new SimpleDoubleProperty(c.getValue().getCantidad()).asObject());
        colCantidad.setPrefWidth(100);

        TableColumn<DetallePedido, Double> colPrecio = new TableColumn<>("Precio Unitario");
        colPrecio.setCellValueFactory(c ->
                new SimpleDoubleProperty(c.getValue().getPrecioUnitario()).asObject());
        colPrecio.setPrefWidth(120);

        TableColumn<DetallePedido, Double> colSubtotal = new TableColumn<>("Subtotal");
        colSubtotal.setCellValueFactory(c ->
                new SimpleDoubleProperty(
                        c.getValue().getCantidad() * c.getValue().getPrecioUnitario()
                ).asObject());
        colSubtotal.setPrefWidth(120);

        //Cantidad de bolsas
        TableColumn<DetallePedido, Integer> colBolsas = new TableColumn<>("Bolsas");
        colBolsas.setCellValueFactory(c ->
                new SimpleIntegerProperty(calcularBolsas(c.getValue())).asObject());
        colBolsas.setPrefWidth(100);

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

        tabla.getColumns().addAll(
                colId, colProducto, colCantidad, colPrecio, colSubtotal, colBolsas, colAcciones
        );

        try {
            ObservableList<DetallePedido> datos =
                    FXCollections.observableArrayList(controlador.listarDetalles(idPedido));
            tabla.setItems(datos);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,
                    "Error al cargar detalles: " + e.getMessage()).showAndWait();
        }

        Button btnAgregar = new Button("Agregar Producto");
        Button btnVolver = new Button("Volver");
        Button btnConfig = new Button("⚙ Configuración bolsas");

        btnAgregar.setOnAction(e ->
                new VentanaAgregarDetallePedido(stage, idPedido, idCliente).mostrar());

        btnVolver.setOnAction(e -> new VentanaListarPedidos(stage).mostrar());

        btnConfig.setOnAction(e ->
                new VentanaConfiguracionBolsas(stage, idPedido, idCliente).mostrar()
        );

        HBox acciones = new HBox(10, btnAgregar, btnVolver);
        acciones.setPadding(new Insets(10));

        HBox barraSuperior = new HBox(btnConfig);
        barraSuperior.setPadding(new Insets(10));
        barraSuperior.setAlignment(Pos.TOP_RIGHT);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));
        root.setTop(barraSuperior);
        root.setCenter(tabla);
        root.setBottom(acciones);

        stage.setScene(new Scene(root, 1200, 800));
        stage.setTitle("Detalles del Pedido #" + idPedido);
        stage.show();
    }

    private void recargarConfigBolsas() {
        try {
            productosQueUsanBolsas = new HashSet<>(productoBolsaControlador.obtenerProductosQueUsanBolsas());
        } catch (SQLException e) {
            productosQueUsanBolsas = new HashSet<>();
        }
    }

    private int calcularBolsas(DetallePedido d) {
        if (!productosQueUsanBolsas.contains(d.getIdProducto())) {
            return 0;
        }

        double cantidad = d.getCantidad();
        if (cantidad <= 0) return 0;
        if (cantidad <= 5) return 1;

        double base = Math.floor(cantidad / 5.0);
        double resto = cantidad - base * 5.0;
        int bolsasBase = (int) base;

        if (resto == 0) {
            return bolsasBase;
        } else if (resto <= 1.0) {
            // Se reparte hasta 1 kg extra en las bolsas (quedan algunas de hasta 6 kg)
            return bolsasBase;
        } else {
            // Hace falta una bolsa extra
            return bolsasBase + 1;
        }
    }
}
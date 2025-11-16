package edu.oms.vista;

import edu.oms.controlador.DetallePedidoControlador;
import edu.oms.controlador.ProductoControlador;
import edu.oms.controlador.PrecioClienteControlador;
import edu.oms.controlador.PrecioProductoBaseControlador;
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

public class VentanaAgregarDetallePedido {

    private final Stage stage;
    private final int idPedido;
    private final int idCliente;
    private final DetallePedidoControlador detalleControlador = new DetallePedidoControlador();
    private final ProductoControlador productoControlador = new ProductoControlador();
    private final PrecioClienteControlador precioClienteControlador = new PrecioClienteControlador();
    private final PrecioProductoBaseControlador precioBaseControlador = new PrecioProductoBaseControlador();

    public VentanaAgregarDetallePedido(Stage stage, int idPedido, int idCliente) {
        this.stage = stage;
        this.idPedido = idPedido;
        this.idCliente = idCliente;
    }

    public void mostrar() {
        Label lblTitulo = new Label("Agregar Producto al Pedido");
        Label lblProducto = new Label("Producto:");
        Label lblCantidad = new Label("Cantidad:");
        Label lblPrecio = new Label("Precio Unitario:");

        ComboBox<Producto> comboProducto = new ComboBox<>();
        try {
            List<Producto> productos = productoControlador.listarProductos();
            comboProducto.setItems(FXCollections.observableArrayList(productos));
            comboProducto.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Producto item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? "" : item.getNombreProducto());
                }
            });
            comboProducto.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(Producto item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? "" : item.getNombreProducto());
                }
            });
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,
                    "Error al cargar productos: " + e.getMessage()).showAndWait();
        }

        TextField txtCantidad = new TextField();
        TextField txtPrecio = new TextField();

        Runnable completarPrecio = () -> {
            Producto p = comboProducto.getValue();
            if (p == null) return;
            try {
                LocalDate hoy = LocalDate.now();
                Double precio = precioClienteControlador
                        .obtenerPrecioVigente(idCliente, p.getIdProducto(), hoy);
                if (precio == null) {
                    precio = precioBaseControlador
                            .obtenerPrecioBaseVigente(p.getIdProducto(), hoy);
                }
                if (precio != null && precio > 0) {
                    txtPrecio.setText(String.valueOf(precio));
                }
            } catch (SQLException ex) {
                new Alert(Alert.AlertType.ERROR,
                        "Error al consultar precio: " + ex.getMessage()).showAndWait();
            }
        };

        comboProducto.valueProperty().addListener((obs, o, n) -> completarPrecio.run());

        Button btnGuardar = new Button("Agregar");
        Button btnVolver = new Button("Volver");

        btnGuardar.setOnAction(e -> {
            try {
                Producto p = comboProducto.getValue();
                if (p == null) {
                    new Alert(Alert.AlertType.WARNING,
                            "Seleccioná un producto.").showAndWait();
                    return;
                }

                if (txtPrecio.getText().isBlank()) {
                    completarPrecio.run();
                }

                double cantidad = Double.parseDouble(txtCantidad.getText());
                double precio = Double.parseDouble(txtPrecio.getText());

                detalleControlador.agregarDetalle(idPedido, p.getIdProducto(), cantidad, precio);

                new Alert(Alert.AlertType.INFORMATION,
                        "Producto agregado correctamente.").showAndWait();
                txtCantidad.clear();
                txtPrecio.clear();
                comboProducto.getSelectionModel().clearSelection();

            } catch (NumberFormatException ex) {
                new Alert(Alert.AlertType.WARNING,
                        "Cantidad y precio deben ser números válidos.").showAndWait();
            } catch (SQLException ex) {
                new Alert(Alert.AlertType.ERROR,
                        "Error al guardar detalle: " + ex.getMessage()).showAndWait();
            }
        });

        btnVolver.setOnAction(e ->
                new VentanaListarDetallesPedido(stage, idPedido, idCliente).mostrar());

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setStyle("-fx-background-color: #f9f9f9;");

        grid.add(lblTitulo, 0, 0, 2, 1);
        grid.add(lblProducto, 0, 1);
        grid.add(comboProducto, 1, 1);
        grid.add(lblCantidad, 0, 2);
        grid.add(txtCantidad, 1, 2);
        grid.add(lblPrecio, 0, 3);
        grid.add(txtPrecio, 1, 3);
        grid.add(btnGuardar, 0, 4);
        grid.add(btnVolver, 1, 4);

        stage.setScene(new Scene(grid, 520, 350));
        stage.setTitle("Agregar Detalle Pedido");
        stage.show();
    }
}
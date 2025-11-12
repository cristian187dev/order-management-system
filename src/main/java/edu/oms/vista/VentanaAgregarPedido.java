package edu.oms.vista;

import edu.oms.controlador.ClienteControlador;
import edu.oms.controlador.DetallePedidoControlador;
import edu.oms.controlador.PedidoControlador;
import edu.oms.controlador.PrecioClienteControlador;
import edu.oms.controlador.PrecioProductoBaseControlador;
import edu.oms.controlador.ProductoControlador;
import edu.oms.modelo.Cliente;
import edu.oms.modelo.DetallePedido;
import edu.oms.modelo.Producto;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class VentanaAgregarPedido {

    private final Stage stage;
    private final PedidoControlador pedidoControlador = new PedidoControlador();
    private final ClienteControlador clienteControlador = new ClienteControlador();
    private final ProductoControlador productoControlador = new ProductoControlador();
    private final DetallePedidoControlador detalleControlador = new DetallePedidoControlador();
    private final PrecioClienteControlador precioClienteControlador = new PrecioClienteControlador();
    private final PrecioProductoBaseControlador precioBaseControlador = new PrecioProductoBaseControlador();

    private final ObservableList<DetallePedido> items = FXCollections.observableArrayList();

    public VentanaAgregarPedido(Stage stage) {
        this.stage = stage;
    }

    public void mostrar() {
        Label lblTitulo = new Label("Nuevo Pedido");
        Label lblCliente = new Label("Cliente:");
        Label lblEstadoPago = new Label("Estado de Pago:");
        Label lblObservaciones = new Label("Observaciones:");

        ComboBox<Cliente> comboClientes = new ComboBox<>();
        comboClientes.setPrefWidth(280);
        try {

            List<Cliente> clientes = clienteControlador.listarClientesActivos();
            comboClientes.setItems(FXCollections.observableArrayList(clientes));
            comboClientes.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Cliente item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? "" : item.getNombre() + " " + item.getApellido());
                }
            });
            comboClientes.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(Cliente item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? "" : item.getNombre() + " " + item.getApellido());
                }
            });
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error al cargar clientes: " + e.getMessage()).showAndWait();
        }

        ComboBox<String> comboEstado = new ComboBox<>();
        comboEstado.setItems(FXCollections.observableArrayList("PENDIENTE", "PAGADO"));
        comboEstado.setValue("PENDIENTE");

        TextArea txtObs = new TextArea();
        txtObs.setPrefRowCount(3);

        Label lblProducto = new Label("Producto:");
        Label lblCantidad = new Label("Cantidad:");
        Label lblPrecio = new Label("Precio Unitario:");

        ComboBox<Producto> comboProducto = new ComboBox<>();
        comboProducto.setPrefWidth(280);
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
            new Alert(Alert.AlertType.ERROR, "Error al cargar productos: " + e.getMessage()).showAndWait();
        }

        TextField txtCantidad = new TextField();
        TextField txtPrecio = new TextField();

        Runnable completarPrecio = () -> {
            Cliente c = comboClientes.getValue();
            Producto p = comboProducto.getValue();
            if (c == null || p == null) return;
            try {
                LocalDate hoy = LocalDate.now();
                Double precio = precioClienteControlador.obtenerPrecioVigente(c.getIdCliente(), p.getIdProducto(), hoy);
                if (precio == null) {
                    precio = precioBaseControlador.obtenerPrecioBaseVigente(p.getIdProducto(), hoy);
                }
                if (precio != null && precio > 0) {
                    txtPrecio.setText(String.valueOf(precio));
                }
            } catch (SQLException ex) {
                new Alert(Alert.AlertType.ERROR, "Error al consultar precio: " + ex.getMessage()).showAndWait();
            }
        };

        comboClientes.valueProperty().addListener((obs, o, n) -> completarPrecio.run());
        comboProducto.valueProperty().addListener((obs, o, n) -> completarPrecio.run());

        Button btnAgregarItem = new Button("Agregar");
        btnAgregarItem.setOnAction(e -> {
            try {
                Producto p = comboProducto.getValue();
                if (p == null) {
                    new Alert(Alert.AlertType.WARNING, "Seleccioná un producto.").showAndWait();
                    return;
                }
                if (txtPrecio.getText().isBlank()) {
                    completarPrecio.run();
                }
                double cantidad = Double.parseDouble(txtCantidad.getText());
                if (cantidad <= 0) throw new NumberFormatException();
                double precio = Double.parseDouble(txtPrecio.getText());
                if (precio <= 0) throw new NumberFormatException();

                DetallePedido d = new DetallePedido();
                d.setIdProducto(p.getIdProducto());
                d.setNombreProducto(p.getNombreProducto());
                d.setCantidad(cantidad);
                d.setPrecioUnitario(precio);
                items.add(d);

                comboProducto.getSelectionModel().clearSelection();
                txtCantidad.clear();
                txtPrecio.clear();
            } catch (NumberFormatException ex) {
                new Alert(Alert.AlertType.WARNING, "Cantidad y precio deben ser números válidos (> 0).").showAndWait();
            }
        });

        TableView<DetallePedido> tabla = new TableView<>(items);
        TableColumn<DetallePedido, String> colProd = new TableColumn<>("Producto");
        colProd.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombreProducto()));
        colProd.setPrefWidth(220);

        TableColumn<DetallePedido, Double> colCant = new TableColumn<>("Cantidad");
        colCant.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getCantidad()).asObject());
        colCant.setPrefWidth(100);

        TableColumn<DetallePedido, Double> colPrecio = new TableColumn<>("Precio Unit.");
        colPrecio.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getPrecioUnitario()).asObject());
        colPrecio.setPrefWidth(110);

        TableColumn<DetallePedido, Double> colSub = new TableColumn<>("Subtotal");
        colSub.setCellValueFactory(c -> new SimpleDoubleProperty(
                c.getValue().getCantidad() * c.getValue().getPrecioUnitario()).asObject());
        colSub.setPrefWidth(110);

        TableColumn<DetallePedido, Void> colAcc = new TableColumn<>("Acción");
        colAcc.setCellFactory(col -> new TableCell<>() {
            private final Button btnQuitar = new Button("Quitar");
            {
                btnQuitar.setOnAction(e -> {
                    DetallePedido d = getTableView().getItems().get(getIndex());
                    getTableView().getItems().remove(d);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnQuitar);
            }
        });
        colAcc.setPrefWidth(90);

        tabla.getColumns().addAll(colProd, colCant, colPrecio, colSub, colAcc);

        Button btnListo = new Button("Listo");
        Button btnVolver = new Button("Volver");

        btnListo.setOnAction(e -> {
            Cliente cliente = comboClientes.getValue();
            if (cliente == null) {
                new Alert(Alert.AlertType.WARNING, "Seleccioná un cliente.").showAndWait();
                return;
            }
            if (items.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Agregá al menos un producto.").showAndWait();
                return;
            }
            try {
                int idPedido = pedidoControlador.agregarPedido(
                        cliente.getIdCliente(),
                        comboEstado.getValue(),
                        txtObs.getText()
                );
                for (DetallePedido d : items) {
                    detalleControlador.agregarDetalle(idPedido, d.getIdProducto(), d.getCantidad(), d.getPrecioUnitario());
                }
                new Alert(Alert.AlertType.INFORMATION, "Pedido guardado correctamente.").showAndWait();
                new VentanaListarPedidos(stage).mostrar();
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Error al guardar el pedido: " + ex.getMessage()).showAndWait();
            }
        });

        btnVolver.setOnAction(e -> new VentanaPedidos(stage).mostrar());

        GridPane encabezado = new GridPane();
        encabezado.setPadding(new Insets(15));
        encabezado.setVgap(10);
        encabezado.setHgap(10);
        encabezado.add(lblTitulo, 0, 0, 2, 1);
        encabezado.add(lblCliente, 0, 1); encabezado.add(comboClientes, 1, 1);
        encabezado.add(lblEstadoPago, 0, 2); encabezado.add(comboEstado, 1, 2);
        encabezado.add(lblObservaciones, 0, 3); encabezado.add(txtObs, 1, 3);

        GridPane agregar = new GridPane();
        agregar.setPadding(new Insets(10));
        agregar.setVgap(8);
        agregar.setHgap(8);
        agregar.add(new Label("Producto:"), 0, 0); agregar.add(comboProducto, 1, 0);
        agregar.add(new Label("Cantidad:"), 0, 1); agregar.add(txtCantidad, 1, 1);
        agregar.add(new Label("Precio Unitario:"), 0, 2); agregar.add(txtPrecio, 1, 2);
        agregar.add(btnAgregarItem, 1, 3);

        HBox acciones = new HBox(10, btnListo, btnVolver);
        acciones.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setTop(encabezado);
        root.setCenter(tabla);
        root.setLeft(agregar);
        root.setBottom(acciones);
        BorderPane.setMargin(encabezado, new Insets(5));
        BorderPane.setMargin(tabla, new Insets(10));

        stage.setScene(new Scene(root, 1100, 600));
        stage.setTitle("Agregar Pedido");
        stage.show();
    }
}

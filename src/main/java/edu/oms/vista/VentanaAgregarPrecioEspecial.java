package edu.oms.vista;

import edu.oms.controlador.PrecioClienteControlador;
import edu.oms.controlador.ClienteControlador;
import edu.oms.controlador.ProductoControlador;
import edu.oms.modelo.Cliente;
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

public class VentanaAgregarPrecioEspecial {

    private final Stage stage;
    private final PrecioClienteControlador controlador = new PrecioClienteControlador();
    private final ClienteControlador clienteControlador = new ClienteControlador();
    private final ProductoControlador productoControlador = new ProductoControlador();

    public VentanaAgregarPrecioEspecial(Stage stage) {
        this.stage = stage;
    }

    public void mostrar() {
        Label lblTitulo = new Label("Agregar Precio Especial por Cliente");
        Label lblCliente = new Label("Cliente:");
        Label lblProducto = new Label("Producto:");
        Label lblPrecio = new Label("Precio Especial:");
        Label lblInicio = new Label("Fecha Inicio:");

        ComboBox<Cliente> cmbCliente = new ComboBox<>();
        ComboBox<Producto> cmbProducto = new ComboBox<>();
        TextField txtPrecio = new TextField();
        DatePicker dpInicio = new DatePicker(LocalDate.now());

        Button btnGuardar = new Button("Guardar");
        Button btnVolver = new Button("Volver");

        // Cargar clientes
        try {
            List<Cliente> clientes = clienteControlador.listarClientes();
            cmbCliente.setItems(FXCollections.observableArrayList(clientes));
            cmbCliente.setPromptText("Seleccione un cliente");
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error al cargar clientes: " + e.getMessage()).showAndWait();
        }

        cmbCliente.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Cliente item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombre() + " " + item.getApellido());
            }
        });
        cmbCliente.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Cliente item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombre() + " " + item.getApellido());
            }
        });

        // Cargar productos
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
                Cliente cliente = cmbCliente.getValue();
                Producto producto = cmbProducto.getValue();
                if (cliente == null || producto == null)
                    throw new IllegalArgumentException("Debe seleccionar cliente y producto.");

                double precio = Double.parseDouble(txtPrecio.getText());
                controlador.agregarPrecioCliente(cliente.getIdCliente(), producto.getIdProducto(), precio, dpInicio.getValue(), null);

                new Alert(Alert.AlertType.INFORMATION, "Precio especial guardado correctamente.").showAndWait();
                cmbCliente.getSelectionModel().clearSelection();
                cmbProducto.getSelectionModel().clearSelection();
                txtPrecio.clear();
                dpInicio.setValue(LocalDate.now());

            } catch (NumberFormatException ex) {
                new Alert(Alert.AlertType.WARNING, "El precio debe ser numérico.").showAndWait();
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Error: " + ex.getMessage()).showAndWait();
            }
        });

        btnVolver.setOnAction(e -> new VentanaPreciosEspeciales(stage).mostrar());

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);
        grid.add(lblTitulo, 0, 0, 2, 1);
        grid.add(lblCliente, 0, 1); grid.add(cmbCliente, 1, 1);
        grid.add(lblProducto, 0, 2); grid.add(cmbProducto, 1, 2);
        grid.add(lblPrecio, 0, 3); grid.add(txtPrecio, 1, 3);
        grid.add(lblInicio, 0, 4); grid.add(dpInicio, 1, 4);
        grid.add(btnGuardar, 0, 5); grid.add(btnVolver, 1, 5);

        stage.setScene(new Scene(grid, 600, 400));
        stage.setTitle("Agregar Precio Especial");
        stage.show();
    }
}

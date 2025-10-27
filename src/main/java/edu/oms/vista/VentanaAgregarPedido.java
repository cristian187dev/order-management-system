package edu.oms.vista;

import edu.oms.controlador.ClienteControlador;
import edu.oms.controlador.PedidoControlador;
import edu.oms.modelo.Cliente;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class VentanaAgregarPedido {

    private final Stage stage;
    private final PedidoControlador pedidoControlador = new PedidoControlador();
    private final ClienteControlador clienteControlador = new ClienteControlador();

    public VentanaAgregarPedido(Stage stage) {
        this.stage = stage;
    }

    public void mostrar() {
        Label lblTitulo = new Label("Agregar Pedido");
        Label lblCliente = new Label("Cliente:");
        Label lblEstadoPago = new Label("Estado de Pago:");
        Label lblObservaciones = new Label("Observaciones:");

        ComboBox<Cliente> comboClientes = new ComboBox<>();
        comboClientes.setPrefWidth(250);
        try {
            List<Cliente> clientes = clienteControlador.listarClientes();
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
        txtObs.setPrefRowCount(4);

        Button btnGuardar = new Button("Guardar Pedido");
        Button btnVolver = new Button("Volver");

        btnGuardar.setOnAction(e -> {
            Cliente cliente = comboClientes.getValue();
            if (cliente == null) {
                new Alert(Alert.AlertType.WARNING, "Seleccioná un cliente.").showAndWait();
                return;
            }

            try {
                pedidoControlador.agregarPedido(
                        cliente.getIdCliente(),
                        comboEstado.getValue(),
                        txtObs.getText()
                );

                new Alert(Alert.AlertType.INFORMATION, "Pedido guardado correctamente.").showAndWait();

                comboClientes.getSelectionModel().clearSelection();
                comboEstado.setValue("PENDIENTE");
                txtObs.clear();
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Error al guardar el pedido: " + ex.getMessage()).showAndWait();
            }
        });

        btnVolver.setOnAction(e -> new VentanaPedidos(stage).mostrar());

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setStyle("-fx-background-color: #f8f9fa;");

        grid.add(lblTitulo, 0, 0, 2, 1);
        grid.add(lblCliente, 0, 1);
        grid.add(comboClientes, 1, 1);
        grid.add(lblEstadoPago, 0, 2);
        grid.add(comboEstado, 1, 2);
        grid.add(lblObservaciones, 0, 3);
        grid.add(txtObs, 1, 3);
        grid.add(btnGuardar, 0, 4);
        grid.add(btnVolver, 1, 4);

        stage.setScene(new Scene(grid, 520, 350));
        stage.setTitle("Agregar Pedido");
        stage.show();
    }
}

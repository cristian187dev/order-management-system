package edu.oms.vista;

import edu.oms.controlador.PedidoControlador;
import edu.oms.modelo.Pedido;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class VentanaModificarPedido {

    private final Stage stage;
    private final Pedido pedido;
    private final PedidoControlador controlador = new PedidoControlador();

    public VentanaModificarPedido(Stage stage, Pedido pedido) {
        this.stage = stage;
        this.pedido = pedido;
    }

    public void mostrar() {
        Label lblTitulo = new Label("Modificar Pedido");
        Label lblCliente = new Label("Cliente:");
        Label lblEstado = new Label("Estado de Pago:");
        Label lblObservaciones = new Label("Observaciones:");

        TextField txtCliente = new TextField(pedido.getNombreCliente());
        txtCliente.setEditable(false);

        ComboBox<String> comboEstado = new ComboBox<>();
        comboEstado.setItems(FXCollections.observableArrayList("PENDIENTE", "PAGADO"));
        comboEstado.setValue(pedido.getEstadoPago());

        TextArea txtObservaciones = new TextArea(pedido.getObservaciones());
        txtObservaciones.setPrefRowCount(4);

        Button btnGuardar = new Button("Guardar Cambios");
        Button btnCancelar = new Button("Cancelar");

        btnGuardar.setOnAction(e -> {
            try {
                pedido.setEstadoPago(comboEstado.getValue());
                pedido.setObservaciones(txtObservaciones.getText());

                controlador.modificarPedido(pedido);

                new Alert(Alert.AlertType.INFORMATION, "Pedido actualizado correctamente.").showAndWait();
                new VentanaListarPedidos(stage).mostrar();
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR,
                        "Error al actualizar pedido: " + ex.getMessage()).showAndWait();
            }
        });

        btnCancelar.setOnAction(e -> new VentanaListarPedidos(stage).mostrar());

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        grid.add(lblTitulo, 0, 0, 2, 1);
        grid.add(lblCliente, 0, 1);
        grid.add(txtCliente, 1, 1);
        grid.add(lblEstado, 0, 2);
        grid.add(comboEstado, 1, 2);
        grid.add(lblObservaciones, 0, 3);
        grid.add(txtObservaciones, 1, 3);
        grid.add(btnGuardar, 0, 4);
        grid.add(btnCancelar, 1, 4);

        stage.setScene(new Scene(grid, 520, 350));
        stage.setTitle("Modificar Pedido");
        stage.show();
    }
}
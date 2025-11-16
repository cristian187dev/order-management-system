package edu.oms.vista;

import edu.oms.controlador.ClienteControlador;
import edu.oms.controlador.PagoControlador;
import edu.oms.controlador.PedidoControlador;
import edu.oms.modelo.Cliente;
import edu.oms.modelo.PedidoResumen;
import edu.oms.modelo.TotalPorDia;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class VentanaFacturacion {

    private final Stage stage;
    private final ClienteControlador clienteControlador = new ClienteControlador();
    private final PedidoControlador pedidoControlador = new PedidoControlador();
    private final PagoControlador pagoControlador = new PagoControlador();

    public VentanaFacturacion(Stage stage) {
        this.stage = stage;
    }

    public void mostrar() {

        Label lblTitulo = new Label("Facturación de Clientes");
        lblTitulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label lblCliente = new Label("Cliente:");
        ComboBox<Cliente> comboClientes = new ComboBox<>();

        try {
            comboClientes.setItems(FXCollections.observableArrayList(
                    clienteControlador.listarActivos()
            ));
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error al cargar clientes: " + e.getMessage()).showAndWait();
        }

        Button btnCargar = new Button("Cargar Deuda");

        HBox boxCliente = new HBox(10, lblCliente, comboClientes, btnCargar);
        boxCliente.setPadding(new Insets(10));

        TableView<TotalPorDia> tablaDias = new TableView<>();
        TableColumn<TotalPorDia, LocalDate> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getFecha()));

        TableColumn<TotalPorDia, Double> colTotalDia = new TableColumn<>("Total Día");
        colTotalDia.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getTotalDia()).asObject());

        tablaDias.getColumns().addAll(colFecha, colTotalDia);

        TableView<PedidoResumen> tablaPedidos = new TableView<>();

        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");

        TableColumn<PedidoResumen, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getIdPedido()));

        TableColumn<PedidoResumen, String> colF = new TableColumn<>("Fecha");
        colF.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFecha().format(formatoFecha)));

        TableColumn<PedidoResumen, String> colH = new TableColumn<>("Hora");
        colH.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getHora().format(formatoHora)));

        TableColumn<PedidoResumen, Double> colTotalPedido = new TableColumn<>("Total");
        colTotalPedido.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getTotalPedido()).asObject());

        TableColumn<PedidoResumen, Void> colAcc = new TableColumn<>("Acciones");
        colAcc.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Ver Detalle");

            {
                btn.setOnAction(e -> {
                    PedidoResumen resumen = getTableView().getItems().get(getIndex());
                    Cliente c = comboClientes.getValue();
                    new VentanaListarDetallesPedido(stage, resumen.getIdPedido(), c.getIdCliente()).mostrar();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        tablaPedidos.getColumns().addAll(colId, colF, colH, colTotalPedido, colAcc);

        Label lblTotalDeuda = new Label("Total Deuda: $0.00");
        Label lblTotalPagos = new Label("Total Pagos: $0.00");
        Label lblSaldo = new Label("Saldo: $0.00");
        lblSaldo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        VBox boxTotales = new VBox(5, lblTotalDeuda, lblTotalPagos, lblSaldo);
        boxTotales.setPadding(new Insets(10));

        Button btnRegistrarPago = new Button("Registrar Pago");
        Button btnVolver = new Button("Volver al Menú Principal");

        btnRegistrarPago.setOnAction(e -> {
            Cliente c = comboClientes.getValue();
            if (c != null) new VentanaPagos(stage, c).mostrar();
        });

        btnVolver.setOnAction(e -> new VentanaPrincipal(stage).mostrar());

        HBox boxInferior = new HBox(10, btnRegistrarPago, btnVolver);
        boxInferior.setPadding(new Insets(10));

        btnCargar.setOnAction(e -> {
            Cliente c = comboClientes.getValue();
            if (c == null) return;

            try {
                List<TotalPorDia> dias = pedidoControlador.obtenerTotalesPorDia(c.getIdCliente());
                tablaDias.setItems(FXCollections.observableArrayList(dias));

                double deuda = pedidoControlador.obtenerTotalDeuda(c.getIdCliente());
                double pagos = pagoControlador.obtenerTotalPagos(c.getIdCliente());
                double saldo = deuda - pagos;

                lblTotalDeuda.setText("Total Deuda: $" + deuda);
                lblTotalPagos.setText("Total Pagos: $" + pagos);
                lblSaldo.setText("Saldo: $" + saldo);

            } catch (SQLException ex) {
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
            }
        });

        tablaDias.setOnMouseClicked(e -> {
            TotalPorDia dia = tablaDias.getSelectionModel().getSelectedItem();
            Cliente c = comboClientes.getValue();

            if (dia == null || c == null) return;

            try {
                List<PedidoResumen> lista =
                        pedidoControlador.obtenerPedidosPorFecha(c.getIdCliente(), dia.getFecha());
                tablaPedidos.setItems(FXCollections.observableArrayList(lista));

            } catch (SQLException ex) {
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
            }
        });

        VBox root = new VBox(15, lblTitulo, boxCliente, tablaDias, tablaPedidos, boxTotales, boxInferior);
        root.setPadding(new Insets(15));

        stage.setScene(new Scene(root, 1200, 800));
        stage.setTitle("Facturación");
        stage.show();
    }
}
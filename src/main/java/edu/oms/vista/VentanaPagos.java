package edu.oms.vista;

import edu.oms.controlador.PagoControlador;
import edu.oms.modelo.Cliente;
import edu.oms.modelo.Pago;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

public class VentanaPagos {

    private final Stage stage;
    private final Cliente cliente;
    private final PagoControlador pagoControlador = new PagoControlador();

    public VentanaPagos(Stage stage, Cliente cliente) {
        this.stage = stage;
        this.cliente = cliente;
    }

    public void mostrar() {

        Label lblTitulo = new Label("Pagos del Cliente");
        lblTitulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label lblCliente = new Label(
                "Cliente: " + cliente.getNombre() + " " + cliente.getApellido()
        );

        Label lblMonto = new Label("Monto:");
        TextField txtMonto = new TextField();

        Label lblObs = new Label("Observaciones:");
        TextArea txtObs = new TextArea();
        txtObs.setPrefRowCount(2);

        Button btnGuardar = new Button("Registrar Pago");
        Button btnVolver = new Button("Volver a Facturación");

        HBox boxBotones = new HBox(10, btnGuardar, btnVolver);

        GridPane form = new GridPane();
        form.setPadding(new Insets(10));
        form.setHgap(10);
        form.setVgap(8);

        form.add(lblMonto, 0, 0);
        form.add(txtMonto, 1, 0);
        form.add(lblObs, 0, 1);
        form.add(txtObs, 1, 1);
        form.add(boxBotones, 1, 2);

        //TABLA DE PAGOS

        TableView<Pago> tabla = new TableView<>();

        TableColumn<Pago, String> colFecha = new TableColumn<>("Fecha");
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        colFecha.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getFechaPago().format(formatoFecha)
                )
        );
        colFecha.setPrefWidth(110);

        TableColumn<Pago, Double> colMonto = new TableColumn<>("Monto");
        colMonto.setCellValueFactory(c ->
                new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getMonto())
        );
        colMonto.setPrefWidth(100);

        TableColumn<Pago, String> colObs = new TableColumn<>("Observaciones");
        colObs.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getObservaciones())
        );
        colObs.setPrefWidth(250);

        tabla.getColumns().addAll(colFecha, colMonto, colObs);

        cargarPagos(tabla);

        //EVENTOS

        btnGuardar.setOnAction(e -> {
            try {
                double monto = Double.parseDouble(txtMonto.getText());
                if (monto <= 0) throw new NumberFormatException();

                String observaciones = txtObs.getText();

                pagoControlador.registrarPago(cliente.getIdCliente(), monto, observaciones);

                txtMonto.clear();
                txtObs.clear();
                cargarPagos(tabla);

                new Alert(Alert.AlertType.INFORMATION,
                        "Pago registrado correctamente.").showAndWait();

            } catch (NumberFormatException ex) {
                new Alert(Alert.AlertType.ERROR,
                        "Monto inválido. Debe ser un número mayor a 0.").showAndWait();
            } catch (SQLException ex) {
                new Alert(Alert.AlertType.ERROR,
                        "Error al registrar pago: " + ex.getMessage()).showAndWait();
            }
        });

        btnVolver.setOnAction(e -> new VentanaFacturacion(stage).mostrar());

        //LAYOUT GENERAL

        VBox topBox = new VBox(5, lblTitulo, lblCliente);
        topBox.setPadding(new Insets(10));

        VBox root = new VBox(10, topBox, form, tabla);
        root.setPadding(new Insets(15));

        stage.setScene(new Scene(root, 700, 500));
        stage.setTitle("Pagos - " + cliente.getNombre() + " " + cliente.getApellido());
        stage.show();
    }

    private void cargarPagos(TableView<Pago> tabla) {
        try {
            tabla.setItems(FXCollections.observableArrayList(
                    pagoControlador.listarPagos(cliente.getIdCliente())
            ));
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,
                    "Error al cargar pagos: " + e.getMessage()).showAndWait();
        }
    }
}
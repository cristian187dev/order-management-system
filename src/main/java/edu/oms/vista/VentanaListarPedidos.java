package edu.oms.vista;

import edu.oms.controlador.PedidoControlador;
import edu.oms.modelo.Pedido;
import javafx.beans.property.SimpleObjectProperty;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class VentanaListarPedidos {

    private final Stage stage;
    private final PedidoControlador controlador = new PedidoControlador();

    public VentanaListarPedidos(Stage stage) {
        this.stage = stage;
    }

    public void mostrar() {
        TableView<Pedido> tabla = new TableView<>();
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");

        TableColumn<Pedido, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getIdPedido()));
        colId.setPrefWidth(60);

        TableColumn<Pedido, String> colCliente = new TableColumn<>("Cliente");
        colCliente.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombreCliente()));
        colCliente.setPrefWidth(150);

        TableColumn<Pedido, String> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getFechaPedido().format(formatoFecha)));
        colFecha.setPrefWidth(100);

        TableColumn<Pedido, String> colHora = new TableColumn<>("Hora");
        colHora.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getHoraPedido().format(formatoHora)));
        colHora.setPrefWidth(90);

        TableColumn<Pedido, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEstadoPago()));
        colEstado.setPrefWidth(100);

        TableColumn<Pedido, String> colObs = new TableColumn<>("Observaciones");
        colObs.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getObservaciones()));
        colObs.setPrefWidth(200);

        TableColumn<Pedido, Void> colAcciones = new TableColumn<>("Acciones");
        colAcciones.setCellFactory(col -> new TableCell<>() {
            private final Button btnEliminar = new Button("Eliminar");
            private final Button btnVer = new Button("Ver Detalles");
            private final HBox box = new HBox(5, btnVer, btnEliminar);

            {
                btnVer.setOnAction(e -> {
                    Pedido pedido = getTableView().getItems().get(getIndex());
                    new VentanaListarDetallesPedido(
                            stage,
                            pedido.getIdPedido(),
                            pedido.getIdCliente()
                    ).mostrar();
                });

                btnEliminar.setOnAction(e -> {
                    Pedido pedido = getTableView().getItems().get(getIndex());
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                            "¿Eliminar pedido de " + pedido.getNombreCliente() + "?",
                            ButtonType.YES, ButtonType.NO);
                    confirm.showAndWait().ifPresent(resp -> {
                        if (resp == ButtonType.YES) {
                            try {
                                controlador.eliminarPedido(pedido.getIdPedido());
                                getTableView().getItems().remove(pedido);
                            } catch (SQLException ex) {
                                new Alert(Alert.AlertType.ERROR,
                                        "Error al eliminar pedido: " + ex.getMessage()).showAndWait();
                            }
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });
        colAcciones.setPrefWidth(200);

        tabla.getColumns().addAll(colId, colCliente, colFecha, colHora, colEstado, colObs, colAcciones);

        DatePicker dpFecha = new DatePicker(LocalDate.now());
        Button btnFiltrar = new Button("Filtrar por fecha");
        Button btnTodos = new Button("Todos");

        btnFiltrar.setOnAction(e -> {
            LocalDate fecha = dpFecha.getValue();
            if (fecha == null) return;
            try {
                ObservableList<Pedido> datos =
                        FXCollections.observableArrayList(controlador.listarPedidosPorFecha(fecha));
                tabla.setItems(datos);
            } catch (SQLException ex) {
                new Alert(Alert.AlertType.ERROR,
                        "Error al filtrar pedidos: " + ex.getMessage()).showAndWait();
            }
        });

        btnTodos.setOnAction(e -> cargarTodos(tabla));

        HBox barraFiltro = new HBox(10, new Label("Fecha:"), dpFecha, btnFiltrar, btnTodos);
        barraFiltro.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));
        root.setTop(barraFiltro);
        root.setCenter(tabla);

        Button btnVolver = new Button("Volver");
        btnVolver.setOnAction(e -> new VentanaPedidos(stage).mostrar());
        BorderPane bottom = new BorderPane();
        bottom.setPadding(new Insets(10));
        bottom.setRight(btnVolver);
        root.setBottom(bottom);

        stage.setScene(new Scene(root, 1050, 550));
        stage.setTitle("Listado de Pedidos");
        stage.show();

        cargarTodos(tabla);
    }

    private void cargarTodos(TableView<Pedido> tabla) {
        try {
            ObservableList<Pedido> datos =
                    FXCollections.observableArrayList(controlador.listarPedidos());
            tabla.setItems(datos);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,
                    "Error al cargar pedidos: " + e.getMessage()).showAndWait();
        }
    }
}
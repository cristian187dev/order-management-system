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
            private final Button btnEditar = new Button("Modificar");
            private final Button btnEliminar = new Button("Eliminar");
            private final HBox box = new HBox(5, btnEditar, btnEliminar);

            {
                btnEditar.setOnAction(e -> {
                    Pedido pedido = getTableView().getItems().get(getIndex());
                    new VentanaModificarPedido(stage, pedido).mostrar();
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
                                new Alert(Alert.AlertType.ERROR, "Error al eliminar pedido: " + ex.getMessage()).showAndWait();
                            }
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(box);
                }
            }
        });
        colAcciones.setPrefWidth(200);

        tabla.getColumns().addAll(colId, colCliente, colFecha, colHora, colEstado, colObs, colAcciones);

        try {
            ObservableList<Pedido> datos = FXCollections.observableArrayList(controlador.listarPedidos());
            tabla.setItems(datos);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error al cargar pedidos: " + e.getMessage()).showAndWait();
        }

        Button btnVolver = new Button("Volver");
        btnVolver.setOnAction(e -> new VentanaPedidos(stage).mostrar());

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));
        root.setCenter(tabla);
        root.setBottom(btnVolver);
        BorderPane.setMargin(btnVolver, new Insets(10));

        stage.setScene(new Scene(root, 1000, 500));
        stage.setTitle("Listado de Pedidos");
        stage.show();
    }
}

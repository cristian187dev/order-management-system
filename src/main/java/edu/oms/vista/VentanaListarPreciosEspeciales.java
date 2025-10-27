package edu.oms.vista;

import edu.oms.controlador.PrecioClienteControlador;
import edu.oms.modelo.PrecioCliente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

public class VentanaListarPreciosEspeciales {

    private final Stage stage;
    private final PrecioClienteControlador controlador = new PrecioClienteControlador();

    public VentanaListarPreciosEspeciales(Stage stage) {
        this.stage = stage;
    }

    public void mostrar() {
        TableView<PrecioCliente> tabla = new TableView<>();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        TableColumn<PrecioCliente, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getIdPrecioCliente()).asObject());
        colId.setPrefWidth(50);

        TableColumn<PrecioCliente, String> colCliente = new TableColumn<>("Cliente");
        colCliente.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNombreCliente()));
        colCliente.setPrefWidth(200);

        TableColumn<PrecioCliente, String> colProducto = new TableColumn<>("Producto");
        colProducto.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNombreProducto()));
        colProducto.setPrefWidth(200);

        TableColumn<PrecioCliente, Double> colPrecio = new TableColumn<>("Precio");
        colPrecio.setCellValueFactory(c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getPrecioUnitario()).asObject());
        colPrecio.setPrefWidth(100);

        TableColumn<PrecioCliente, String> colInicio = new TableColumn<>("Inicio");
        colInicio.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getFechaInicioPrecio() != null
                                ? c.getValue().getFechaInicioPrecio().format(formato)
                                : "-"
                ));
        colInicio.setPrefWidth(100);

        TableColumn<PrecioCliente, String> colFin = new TableColumn<>("Fin");
        colFin.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getFechaFinPrecio() != null
                                ? c.getValue().getFechaFinPrecio().format(formato)
                                : "-"
                ));
        colFin.setPrefWidth(100);

        TableColumn<PrecioCliente, Void> colAcciones = new TableColumn<>("Acción");
        colAcciones.setCellFactory(param -> new TableCell<>() {
            private final Button btnBorrar = new Button("Borrar");
            {
                btnBorrar.setOnAction(e -> {
                    PrecioCliente pc = getTableView().getItems().get(getIndex());
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                            "¿Eliminar precio especial del cliente \"" + pc.getNombreCliente() + "\"?",
                            ButtonType.YES, ButtonType.NO);
                    confirm.setHeaderText(null);
                    confirm.showAndWait().ifPresent(bt -> {
                        if (bt == ButtonType.YES) {
                            try {
                                controlador.eliminarPrecioCliente(pc.getIdPrecioCliente());
                                getTableView().getItems().remove(pc);
                                new Alert(Alert.AlertType.INFORMATION, "Registro eliminado correctamente").showAndWait();
                            } catch (SQLException ex) {
                                new Alert(Alert.AlertType.ERROR, "Error al eliminar: " + ex.getMessage()).showAndWait();
                            }
                        }
                    });
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnBorrar);
            }
        });
        colAcciones.setPrefWidth(100);

        tabla.getColumns().addAll(colId, colCliente, colProducto, colPrecio, colInicio, colFin, colAcciones);

        try {
            ObservableList<PrecioCliente> datos = FXCollections.observableArrayList(controlador.listarPreciosClientes());
            tabla.setItems(datos);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error al cargar precios especiales: " + e.getMessage()).showAndWait();
        }

        Button btnVolver = new Button("Volver");
        btnVolver.setOnAction(e -> new VentanaPreciosEspeciales(stage).mostrar());

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));
        root.setCenter(tabla);
        root.setBottom(btnVolver);
        BorderPane.setMargin(btnVolver, new Insets(10));

        stage.setScene(new Scene(root, 900, 450));
        stage.setTitle("Listado de Precios Especiales por Cliente");
        stage.show();
    }
}

package edu.oms.vista;

import edu.oms.controlador.PrecioProductoBaseControlador;
import edu.oms.modelo.PrecioProductoBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

public class VentanaListarPreciosBase {

    private final Stage stage;
    private final PrecioProductoBaseControlador controlador = new PrecioProductoBaseControlador();

    public VentanaListarPreciosBase(Stage stage) {
        this.stage = stage;
    }

    public void mostrar() {
        TableView<PrecioProductoBase> tabla = new TableView<>();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        TableColumn<PrecioProductoBase, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getIdPrecioBase()).asObject());
        colId.setPrefWidth(50);

        TableColumn<PrecioProductoBase, String> colProducto = new TableColumn<>("Producto");
        colProducto.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNombreProducto()));
        colProducto.setPrefWidth(200);

        TableColumn<PrecioProductoBase, Double> colPrecio = new TableColumn<>("Precio Base");
        colPrecio.setCellValueFactory(c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getPrecioBase()).asObject());
        colPrecio.setPrefWidth(120);

        TableColumn<PrecioProductoBase, String> colInicio = new TableColumn<>("Inicio");
        colInicio.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getFechaInicioPrecio() != null
                                ? c.getValue().getFechaInicioPrecio().format(formato)
                                : "-"
                ));
        colInicio.setPrefWidth(100);

        TableColumn<PrecioProductoBase, String> colFin = new TableColumn<>("Fin");
        colFin.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getFechaFinPrecio() != null
                                ? c.getValue().getFechaFinPrecio().format(formato)
                                : "-"
                ));
        colFin.setPrefWidth(100);

        TableColumn<PrecioProductoBase, Void> colAcciones = new TableColumn<>("Acción");
        colAcciones.setCellFactory(param -> new TableCell<>() {
            private final Button btnBorrar = new Button("Borrar");
            {
                btnBorrar.setOnAction(e -> {
                    PrecioProductoBase pb = getTableView().getItems().get(getIndex());
                    Alert confirm = new Alert(
                            Alert.AlertType.CONFIRMATION,
                            "¿Seguro que querés eliminar el precio base de \"" + pb.getNombreProducto() + "\"?",
                            ButtonType.YES, ButtonType.NO
                    );
                    confirm.setHeaderText(null);
                    confirm.showAndWait().ifPresent(bt -> {
                        if (bt == ButtonType.YES) {
                            try {
                                controlador.eliminarPrecioBase(pb.getIdPrecioBase());
                                getTableView().getItems().remove(pb);
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

        tabla.getColumns().addAll(colId, colProducto, colPrecio, colInicio, colFin, colAcciones);

        try {
            ObservableList<PrecioProductoBase> datos = FXCollections.observableArrayList(controlador.listarPreciosBase());
            tabla.setItems(datos);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error al cargar precios base: " + e.getMessage()).showAndWait();
        }

        Button btnVolver = new Button("Volver");
        btnVolver.setOnAction(e -> new VentanaPreciosBase(stage).mostrar());

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));
        root.setCenter(tabla);
        root.setBottom(btnVolver);
        BorderPane.setMargin(btnVolver, new Insets(10));

        stage.setScene(new Scene(root, 1200, 800));
        stage.setTitle("Listado de Precios Base");
        stage.show();
    }
}
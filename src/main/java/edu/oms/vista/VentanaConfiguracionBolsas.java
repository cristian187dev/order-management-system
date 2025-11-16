package edu.oms.vista;

import edu.oms.controlador.ProductoBolsaControlador;
import edu.oms.controlador.ProductoControlador;
import edu.oms.modelo.Producto;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.List;

public class VentanaConfiguracionBolsas {

    private final Stage stage;
    private final int idPedido;
    private final int idCliente;

    private final ProductoControlador productoControlador = new ProductoControlador();
    private final ProductoBolsaControlador productoBolsaControlador = new ProductoBolsaControlador();

    public VentanaConfiguracionBolsas(Stage stage, int idPedido, int idCliente) {
        this.stage = stage;
        this.idPedido = idPedido;
        this.idCliente = idCliente;
    }

    public void mostrar() {
        Label lblTitulo = new Label("Configuración de productos que usan bolsas");
        lblTitulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TableView<Producto> tabla = new TableView<>();
        tabla.setEditable(true);

        TableColumn<Producto, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(
                c.getValue().getIdProducto()
        ).asObject());
        colId.setPrefWidth(60);

        TableColumn<Producto, String> colNombre = new TableColumn<>("Producto");
        colNombre.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getNombreProducto()
        ));
        colNombre.setPrefWidth(260);

        TableColumn<Producto, String> colUnidad = new TableColumn<>("Unidad");
        colUnidad.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getNombreUnidad() + " (" + c.getValue().getAbreviaturaUnidad() + ")"
        ));
        colUnidad.setPrefWidth(160);

        TableColumn<Producto, Boolean> colUsaBolsas = new TableColumn<>("Usa bolsas");
        colUsaBolsas.setEditable(true);
        colUsaBolsas.setCellValueFactory(c -> {
            boolean usa = false;
            try {
                usa = productoBolsaControlador.usaBolsas(c.getValue().getIdProducto());
            } catch (SQLException e) {
                // Si hay error, dejo false
            }
            return new javafx.beans.property.SimpleBooleanProperty(usa);
        });

        colUsaBolsas.setCellFactory(col -> new CheckBoxTableCell<>(index -> {
            Producto p = tabla.getItems().get(index);
            javafx.beans.property.BooleanProperty prop =
                    new javafx.beans.property.SimpleBooleanProperty();
            try {
                prop.set(productoBolsaControlador.usaBolsas(p.getIdProducto()));
            } catch (SQLException e) {
                prop.set(false);
            }

            prop.addListener((obs, oldVal, newVal) -> {
                try {
                    productoBolsaControlador.setUsaBolsas(p.getIdProducto(), newVal);
                } catch (SQLException e) {
                    prop.set(oldVal);
                    new Alert(Alert.AlertType.ERROR,
                            "Error al actualizar configuración de bolsas: " + e.getMessage()
                    ).showAndWait();
                }
            });

            return prop;
        }));
        colUsaBolsas.setPrefWidth(100);

        tabla.getColumns().addAll(colId, colNombre, colUnidad, colUsaBolsas);

        try {
            List<Producto> productos = productoControlador.listarProductos();
            tabla.setItems(FXCollections.observableArrayList(productos));
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,
                    "Error al cargar productos: " + e.getMessage()).showAndWait();
        }

        Button btnVolver = new Button("Volver a detalles");
        btnVolver.setOnAction(e ->
                new VentanaListarDetallesPedido(stage, idPedido, idCliente).mostrar()
        );

        HBox barraInferior = new HBox(btnVolver);
        barraInferior.setPadding(new Insets(10));
        barraInferior.setAlignment(Pos.CENTER_RIGHT);

        VBox contCentro = new VBox(10, lblTitulo, tabla);
        contCentro.setPadding(new Insets(15));

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));
        root.setCenter(contCentro);
        root.setBottom(barraInferior);

        Scene scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.setTitle("Configuración de Bolsas");
        stage.show();
    }
}
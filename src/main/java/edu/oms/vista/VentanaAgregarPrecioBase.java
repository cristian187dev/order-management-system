package edu.oms.vista;

import edu.oms.controlador.PrecioProductoBaseControlador;
import edu.oms.controlador.ProductoControlador;
import edu.oms.modelo.Producto;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class VentanaAgregarPrecioBase {

    private final Stage stage;
    private final PrecioProductoBaseControlador controlador = new PrecioProductoBaseControlador();
    private final ProductoControlador productoControlador = new ProductoControlador();

    public VentanaAgregarPrecioBase(Stage stage) {
        this.stage = stage;
    }

    public void mostrar() {

        //TITULO
        Label lblTitulo = new Label("Agregar Precio Base de Producto");
        lblTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        //ETIQUETAS Y CONTROLES
        Label lblProducto = new Label("Producto:");
        Label lblPrecio = new Label("Precio Base:");
        Label lblInicio = new Label("Fecha Inicio:");

        ComboBox<Producto> cmbProducto = new ComboBox<>();
        TextField txtPrecio = new TextField();
        DatePicker dpInicio = new DatePicker(LocalDate.now());

        Button btnGuardar = new Button("Guardar");
        Button btnVolver = new Button("Volver");

        // Cargar productos desde BD
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

        //ACCIONES BOTONES
        btnGuardar.setOnAction(e -> {
            try {
                Producto seleccionado = cmbProducto.getValue();
                if (seleccionado == null) {
                    throw new IllegalArgumentException("Seleccione un producto.");
                }
                double precio = Double.parseDouble(txtPrecio.getText());

                controlador.agregarPrecioBase(seleccionado.getIdProducto(), precio, dpInicio.getValue(), null);

                new Alert(Alert.AlertType.INFORMATION, "Precio base guardado correctamente.").showAndWait();
                txtPrecio.clear();
                dpInicio.setValue(LocalDate.now());
                cmbProducto.getSelectionModel().clearSelection();

            } catch (NumberFormatException ex) {
                new Alert(Alert.AlertType.WARNING, "El precio debe ser numérico.").showAndWait();
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Error: " + ex.getMessage()).showAndWait();
            }
        });

        btnVolver.setOnAction(e -> new VentanaPreciosBase(stage).mostrar());

        //GRID DEL FORMULARIO
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(12);
        grid.setHgap(15);
        grid.setAlignment(Pos.CENTER);

        grid.add(lblProducto, 0, 0); grid.add(cmbProducto, 1, 0);
        grid.add(lblPrecio, 0, 1);   grid.add(txtPrecio, 1, 1);
        grid.add(lblInicio, 0, 2);   grid.add(dpInicio, 1, 2);

        //BOTONES
        HBox botones = new HBox(15, btnGuardar, btnVolver);
        botones.setAlignment(Pos.CENTER);

        //PANEL
        VBox panelConMarco = new VBox(20, lblTitulo, grid, botones);
        panelConMarco.setAlignment(Pos.CENTER);
        panelConMarco.setPadding(new Insets(25));
        panelConMarco.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #b0b0b0;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;"
        );
        panelConMarco.setMaxWidth(500);

        //CONTENEDOR PRINCIPAL
        VBox root = new VBox(panelConMarco);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #e9f5ff;");

        Scene scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.setTitle("Agregar Precio Base");
        stage.show();
    }
}
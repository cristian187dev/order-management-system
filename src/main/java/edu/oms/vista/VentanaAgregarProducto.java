package edu.oms.vista;

import edu.oms.controlador.ProductoControlador;
import edu.oms.modelo.UnidadMedida;
import edu.oms.servicio.UnidadMedidaServicio;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VentanaAgregarProducto {
    private final Stage stage;
    private final ProductoControlador controlador = new ProductoControlador();
    private final UnidadMedidaServicio unidadServicio = new UnidadMedidaServicio();

    public VentanaAgregarProducto(Stage stage) {
        this.stage = stage;
    }

    public void mostrar() {

        Label lblTitulo = new Label("Agregar Producto");
        lblTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label lblNombre = new Label("Nombre del Producto:");
        Label lblUnidad = new Label("Unidad de Medida:");

        TextField txtNombre = new TextField();
        ComboBox<UnidadMedida> cmbUnidad = new ComboBox<>();

        try {
            cmbUnidad.setItems(FXCollections.observableArrayList(unidadServicio.listarActivas()));
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error al cargar unidades: " + e.getMessage()).showAndWait();
        }

        Button btnGuardar = new Button("Guardar");
        Button btnVolver = new Button("Volver");

        btnGuardar.setOnAction(e -> {
            try {
                UnidadMedida seleccionada = cmbUnidad.getValue();
                if (seleccionada == null) {
                    new Alert(Alert.AlertType.WARNING, "Debe seleccionar una unidad.").showAndWait();
                    return;
                }

                controlador.agregarProducto(
                        txtNombre.getText(),
                        seleccionada.getIdUnidad(),
                        0
                );

                new Alert(Alert.AlertType.INFORMATION, "Producto agregado correctamente.").showAndWait();
                txtNombre.clear();
                cmbUnidad.getSelectionModel().clearSelection();

            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Error al guardar: " + ex.getMessage()).showAndWait();
            }
        });

        btnVolver.setOnAction(e -> new VentanaProductos(stage).mostrar());

        GridPane grid = new GridPane();
        grid.setVgap(12);
        grid.setHgap(15);
        grid.setAlignment(Pos.CENTER);

        grid.add(lblNombre, 0, 0);
        grid.add(txtNombre, 1, 0);

        grid.add(lblUnidad, 0, 1);
        grid.add(cmbUnidad, 1, 1);

        HBox botones = new HBox(15, btnGuardar, btnVolver);
        botones.setAlignment(Pos.CENTER);

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
        panelConMarco.setMaxWidth(450);

        VBox root = new VBox(panelConMarco);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #e9f5ff;");

        Scene scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.setTitle("Agregar Producto");
        stage.show();
    }
}
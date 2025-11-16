package edu.oms.vista;

import edu.oms.controlador.ClienteControlador;
import edu.oms.modelo.Cliente;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;

public class VentanaAgregarCliente {

    private final Stage stage;
    private final ClienteControlador controlador = new ClienteControlador();

    public VentanaAgregarCliente(Stage stage) {
        this.stage = stage;
    }

    public void mostrar() {

        // TITULO
        Label lblTitulo = new Label("Agregar Cliente");
        lblTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // CAMPOS
        Label lblNombre = new Label("Nombre:");
        TextField txtNombre = new TextField();

        Label lblApellido = new Label("Apellido:");
        TextField txtApellido = new TextField();

        Label lblTelefono = new Label("Teléfono:");
        TextField txtTelefono = new TextField();

        Label lblDireccion = new Label("Dirección:");
        TextField txtDireccion = new TextField();

        Label lblCuil = new Label("CUIL:");
        TextField txtCuil = new TextField();

        // BOTONES
        Button btnGuardar = new Button("Guardar");
        Button btnVolver = new Button("Volver");

        btnGuardar.setOnAction(e -> {
            try {
                Cliente cliente = new Cliente();
                cliente.setNombre(txtNombre.getText());
                cliente.setApellido(txtApellido.getText());
                cliente.setTelefono(txtTelefono.getText());
                cliente.setDireccion(txtDireccion.getText());
                cliente.setCuil(txtCuil.getText());
                cliente.setEstadoUsuario("ACTIVO");
                cliente.setFechaInicio(LocalDate.now());
                cliente.setFechaFin(null);

                controlador.agregarCliente(cliente);

                new Alert(Alert.AlertType.INFORMATION, "Cliente agregado correctamente").showAndWait();
                new VentanaClientes(stage).mostrar();

            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Error: " + ex.getMessage()).showAndWait();
            }
        });

        btnVolver.setOnAction(e -> new VentanaClientes(stage).mostrar());

        // FORMULARIO GRID
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(15);
        grid.setVgap(12);
        grid.setAlignment(Pos.CENTER);

        grid.add(lblNombre, 0, 0);
        grid.add(txtNombre, 1, 0);

        grid.add(lblApellido, 0, 1);
        grid.add(txtApellido, 1, 1);

        grid.add(lblTelefono, 0, 2);
        grid.add(txtTelefono, 1, 2);

        grid.add(lblDireccion, 0, 3);
        grid.add(txtDireccion, 1, 3);

        grid.add(lblCuil, 0, 4);
        grid.add(txtCuil, 1, 4);

        // BOTONES
        HBox botones = new HBox(15, btnGuardar, btnVolver);
        botones.setAlignment(Pos.CENTER);

        //MARCO
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

        //CONTENEDOR PRINCIPAL
        VBox root = new VBox(panelConMarco);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #e9f5ff;");

        Scene scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.setTitle("Agregar Cliente");
        stage.show();
    }
}
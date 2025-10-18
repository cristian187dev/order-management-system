package edu.oms.vista;

import edu.oms.controlador.ClienteControlador;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class VentanaAgregarCliente {

    private final Stage stage;
    private final ClienteControlador controlador = new ClienteControlador();

    public VentanaAgregarCliente(Stage stage) {
        this.stage = stage;
    }

    public void mostrar() {
        Label lblTitulo     = new Label("Agregar Cliente");
        Label lblNombre     = new Label("Nombre:");
        Label lblApellido   = new Label("Apellido:");
        Label lblTelefono   = new Label("Teléfono:");
        Label lblDireccion  = new Label("Dirección:");
        Label lblCuil       = new Label("CUIL:");

        TextField txtNombre    = new TextField();
        TextField txtApellido  = new TextField();
        TextField txtTelefono  = new TextField();
        TextField txtDireccion = new TextField();
        TextField txtCuil      = new TextField();

        Button btnGuardar = new Button("Guardar");
        Button btnVolver  = new Button("Volver");

        btnGuardar.setOnAction(e -> {
            try {
                if (txtNombre.getText().isBlank() || txtApellido.getText().isBlank() || txtTelefono.getText().isBlank()) {
                    Alert a = new Alert(Alert.AlertType.WARNING);
                    a.setTitle("Campos obligatorios");
                    a.setHeaderText(null);
                    a.setContentText("Completá Nombre, Apellido y Teléfono!.");
                    a.showAndWait();
                    return;
                }

                controlador.agregarCliente(
                        txtNombre.getText(),
                        txtApellido.getText(),
                        txtTelefono.getText(),
                        txtDireccion.getText(),
                        txtCuil.getText()
                );

                Alert ok = new Alert(Alert.AlertType.INFORMATION);
                ok.setTitle("Éxito");
                ok.setHeaderText(null);
                ok.setContentText("Cliente agregado correctamente.\n");
                ok.showAndWait();

                txtNombre.clear();
                txtApellido.clear();
                txtTelefono.clear();
                txtDireccion.clear();
                txtCuil.clear();

            } catch (Exception ex) {
                Alert err = new Alert(Alert.AlertType.ERROR);
                err.setTitle("Error");
                err.setHeaderText("No se pudo guardar el cliente");
                err.setContentText(ex.getMessage());
                err.showAndWait();
            }
        });

        btnVolver.setOnAction(e -> {
            VentanaClientes vc = new VentanaClientes(stage);
            vc.mostrar();
        });

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setStyle("-fx-background-color: #f8f9fa;");

        grid.add(lblTitulo,    0, 0, 2, 1);
        grid.add(lblNombre,    0, 1); grid.add(txtNombre,    1, 1);
        grid.add(lblApellido,  0, 2); grid.add(txtApellido,  1, 2);
        grid.add(lblTelefono,  0, 3); grid.add(txtTelefono,  1, 3);
        grid.add(lblDireccion, 0, 4); grid.add(txtDireccion, 1, 4);
        grid.add(lblCuil,      0, 5); grid.add(txtCuil,      1, 5);
        grid.add(btnGuardar,   0, 6); grid.add(btnVolver,    1, 6);

        stage.setScene(new Scene(grid, 520, 350));
        stage.setTitle("Agregar Cliente");
        stage.show();
    }
}

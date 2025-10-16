package edu.oms.vista;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class VentanaAgregarCliente {

    private final Stage stage;

    public VentanaAgregarCliente(Stage stage) {
        this.stage = stage;
    }

    public void mostrar() {
        Label lblTitulo = new Label("Agregar Cliente");
        Label lblNombre = new Label("Nombre:");
        Label lblDireccion = new Label("Dirección:");
        Label lblTelefono = new Label("Teléfono:");

        TextField txtNombre = new TextField();
        TextField txtDireccion = new TextField();
        TextField txtTelefono = new TextField();

        Button btnGuardar = new Button("Guardar");
        Button btnVolver = new Button("Volver");

        // Acción Guardar
        btnGuardar.setOnAction(e -> {
            String nombre = txtNombre.getText();
            String direccion = txtDireccion.getText();
            String telefono = txtTelefono.getText();

            System.out.println("Cliente guardado:");
            System.out.println("Nombre: " + nombre);
            System.out.println("Dirección: " + direccion);
            System.out.println("Teléfono: " + telefono);
        });

        // Vuelvo al menú Clientes
        btnVolver.setOnAction(e -> {
            VentanaClientes ventanaClientes = new VentanaClientes(stage);
            ventanaClientes.mostrar();
        });

        // Diseño con GridPane
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setStyle("-fx-background-color: #f8f9fa;");

        grid.add(lblTitulo, 0, 0, 2, 1);
        grid.add(lblNombre, 0, 1);
        grid.add(txtNombre, 1, 1);
        grid.add(lblDireccion, 0, 2);
        grid.add(txtDireccion, 1, 2);
        grid.add(lblTelefono, 0, 3);
        grid.add(txtTelefono, 1, 3);
        grid.add(btnGuardar, 0, 4);
        grid.add(btnVolver, 1, 4);

        Scene scene = new Scene(grid, 400, 300);
        stage.setTitle("Agregar Cliente");
        stage.setScene(scene);
        stage.show();
    }
}

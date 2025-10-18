package edu.oms.vista;

import edu.oms.controlador.ClienteControlador;
import edu.oms.modelo.Cliente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

public class VentanaListarClientes {

    private final Stage stage;
    private final ClienteControlador controlador = new ClienteControlador();

    public VentanaListarClientes(Stage stage) {
        this.stage = stage;
    }

    public void mostrar() {
        TableView<Cliente> tabla = new TableView<>();
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        TableColumn<Cliente, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getIdCliente()).asObject());
        colId.setPrefWidth(50);

        TableColumn<Cliente, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNombre()));
        colNombre.setPrefWidth(100);

        TableColumn<Cliente, String> colApellido = new TableColumn<>("Apellido");
        colApellido.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getApellido()));
        colApellido.setPrefWidth(100);

        TableColumn<Cliente, String> colTelefono = new TableColumn<>("Teléfono");
        colTelefono.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getTelefono()));
        colTelefono.setPrefWidth(110);

        TableColumn<Cliente, String> colDireccion = new TableColumn<>("Dirección");
        colDireccion.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDireccion()));
        colDireccion.setPrefWidth(150);

        TableColumn<Cliente, String> colCuil = new TableColumn<>("CUIL");
        colCuil.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCuil()));
        colCuil.setPrefWidth(120);

        TableColumn<Cliente, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getEstadoUsuario()));
        colEstado.setPrefWidth(80);

        TableColumn<Cliente, String> colFecha = new TableColumn<>("Alta");
        colFecha.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getFechaInicio() != null ? c.getValue().getFechaInicio().format(formatoFecha) : "-"
                ));
        colFecha.setPrefWidth(90);

        tabla.getColumns().addAll(colId, colNombre, colApellido, colTelefono, colDireccion, colCuil, colEstado, colFecha);

        try {
            ObservableList<Cliente> datos = FXCollections.observableArrayList(controlador.listarClientes());
            tabla.setItems(datos);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error al cargar clientes: " + e.getMessage()).showAndWait();
        }

        Button btnVolver = new Button("Volver");
        btnVolver.setOnAction(e -> new VentanaClientes(stage).mostrar());

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));
        root.setCenter(tabla);
        root.setBottom(btnVolver);
        BorderPane.setMargin(btnVolver, new Insets(10));

        stage.setScene(new Scene(root, 900, 450));
        stage.setTitle("Listado de Clientes");
        stage.show();
    }
}

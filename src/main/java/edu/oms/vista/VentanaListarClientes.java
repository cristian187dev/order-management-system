package edu.oms.vista;

import edu.oms.controlador.ClienteControlador;
import edu.oms.modelo.Cliente;
import javafx.beans.property.SimpleIntegerProperty;
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

public class VentanaListarClientes {

    private final Stage stage;
    private final ClienteControlador controlador = new ClienteControlador();

    private final TableView<Cliente> tablaActivos = new TableView<>();
    private final TableView<Cliente> tablaInactivos = new TableView<>();
    private final DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public VentanaListarClientes(Stage stage) {
        this.stage = stage;
    }

    public void mostrar() {

        TabPane tabs = new TabPane();
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab tabActivos = new Tab("Clientes Activos", construirContenidoActivos());
        Tab tabInactivos = new Tab("Clientes Inactivos", construirContenidoInactivos());
        tabs.getTabs().addAll(tabActivos, tabInactivos);

        Button btnVolver = new Button("Volver");
        btnVolver.setOnAction(e -> new VentanaClientes(stage).mostrar());
        HBox pie = new HBox(btnVolver);
        pie.setPadding(new Insets(10));
        pie.setSpacing(10);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));
        root.setCenter(tabs);
        root.setBottom(pie);

        stage.setScene(new Scene(root, 1200, 800));
        stage.setTitle("Listado de Clientes");
        stage.show();

        // Cargar datos
        refrescarActivos();
        refrescarInactivos();
    }

    private BorderPane construirContenidoActivos() {
        configurarColumnasBase(tablaActivos);

        TableColumn<Cliente, Void> colAcciones = new TableColumn<>("Acción");
        colAcciones.setCellFactory(col -> new TableCell<>() {
            private final Button btnDesactivar = new Button("Desactivar");

            {
                btnDesactivar.setOnAction(e -> {
                    Cliente c = getTableView().getItems().get(getIndex());
                    Alert confirm = new Alert(
                            Alert.AlertType.CONFIRMATION,
                            "¿Desactivar al cliente \"" + c.getNombre() + " " + c.getApellido() + "\"?",
                            ButtonType.YES, ButtonType.NO
                    );
                    confirm.setHeaderText(null);
                    confirm.showAndWait().ifPresent(bt -> {
                        if (bt == ButtonType.YES) {
                            try {
                                controlador.desactivarCliente(c.getIdCliente());
                                tablaActivos.getItems().remove(c);
                                // Lo paso al otro listado
                                refrescarInactivos();
                                new Alert(Alert.AlertType.INFORMATION, "Cliente desactivado.").showAndWait();
                            } catch (SQLException ex) {
                                new Alert(Alert.AlertType.ERROR, "Error al desactivar: " + ex.getMessage()).showAndWait();
                            }
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnDesactivar);
            }
        });
        colAcciones.setPrefWidth(110);

        tablaActivos.getColumns().add(colAcciones);

        BorderPane cont = new BorderPane();
        cont.setCenter(tablaActivos);
        return cont;
    }

    private BorderPane construirContenidoInactivos() {
        configurarColumnasBase(tablaInactivos);

        TableColumn<Cliente, Void> colAcciones = new TableColumn<>("Acción");
        colAcciones.setCellFactory(col -> new TableCell<>() {
            private final Button btnActivar = new Button("Activar");

            {
                btnActivar.setOnAction(e -> {
                    Cliente c = getTableView().getItems().get(getIndex());
                    Alert confirm = new Alert(
                            Alert.AlertType.CONFIRMATION,
                            "¿Activar nuevamente a \"" + c.getNombre() + " " + c.getApellido() + "\"?",
                            ButtonType.YES, ButtonType.NO
                    );
                    confirm.setHeaderText(null);
                    confirm.showAndWait().ifPresent(bt -> {
                        if (bt == ButtonType.YES) {
                            try {
                                controlador.activarCliente(c.getIdCliente());
                                tablaInactivos.getItems().remove(c);
                                // Lo paso al otro listado
                                refrescarActivos();
                                new Alert(Alert.AlertType.INFORMATION, "Cliente activado.").showAndWait();
                            } catch (SQLException ex) {
                                new Alert(Alert.AlertType.ERROR, "Error al activar: " + ex.getMessage()).showAndWait();
                            }
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnActivar);
            }
        });
        colAcciones.setPrefWidth(100);

        tablaInactivos.getColumns().add(colAcciones);

        BorderPane cont = new BorderPane();
        cont.setCenter(tablaInactivos);
        return cont;
    }

    private void configurarColumnasBase(TableView<Cliente> tabla) {
        tabla.getColumns().clear();

        TableColumn<Cliente, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getIdCliente()).asObject());
        colId.setPrefWidth(60);

        TableColumn<Cliente, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombre()));
        colNombre.setPrefWidth(120);

        TableColumn<Cliente, String> colApellido = new TableColumn<>("Apellido");
        colApellido.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getApellido()));
        colApellido.setPrefWidth(120);

        TableColumn<Cliente, String> colTelefono = new TableColumn<>("Teléfono");
        colTelefono.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTelefono()));
        colTelefono.setPrefWidth(120);

        TableColumn<Cliente, String> colDireccion = new TableColumn<>("Dirección");
        colDireccion.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDireccion()));
        colDireccion.setPrefWidth(180);

        TableColumn<Cliente, String> colCuil = new TableColumn<>("CUIL");
        colCuil.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCuil()));
        colCuil.setPrefWidth(120);

        TableColumn<Cliente, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEstadoUsuario()));
        colEstado.setPrefWidth(90);

        TableColumn<Cliente, String> colFecha = new TableColumn<>("Alta");
        colFecha.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getFechaInicio() != null ? c.getValue().getFechaInicio().format(formatoFecha) : "-"
                ));
        colFecha.setPrefWidth(90);

        tabla.getColumns().addAll(colId, colNombre, colApellido, colTelefono, colDireccion, colCuil, colEstado, colFecha);
    }

    private void refrescarActivos() {
        try {
            ObservableList<Cliente> datos = FXCollections.observableArrayList(controlador.listarClientesActivos());
            tablaActivos.setItems(datos);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error al cargar clientes activos: " + e.getMessage()).showAndWait();
        }
    }

    private void refrescarInactivos() {
        try {
            ObservableList<Cliente> datos = FXCollections.observableArrayList(controlador.listarClientesInactivos());
            tablaInactivos.setItems(datos);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error al cargar clientes inactivos: " + e.getMessage()).showAndWait();
        }
    }
}
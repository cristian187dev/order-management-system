package edu.oms.vista;

import edu.oms.controlador.ProductoControlador;
import edu.oms.controlador.StockControlador;
import edu.oms.modelo.Producto;
import edu.oms.modelo.StockProductoDia;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class VentanaControlStock {

    private final Stage stage;
    private final ProductoControlador productoControlador = new ProductoControlador();
    private final StockControlador stockControlador = new StockControlador();
    private final TableView<Producto> tablaStockActual = new TableView<>();

    public VentanaControlStock(Stage stage) {
        this.stage = stage;
    }

    public void mostrar() {

        TabPane tabs = new TabPane();
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab tabStockActual = new Tab("Stock Actual", construirTabStockActual());
        Tab tabDemanda = new Tab("Demanda vs Stock", construirTabDemanda());
        Tab tabAgregarStock = new Tab("Agregar Stock", construirTabAgregarStock());

        tabs.getTabs().addAll(tabStockActual, tabDemanda, tabAgregarStock);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));
        root.setCenter(tabs);

        Button btnVolver = new Button("Volver al Menú Principal");
        btnVolver.setOnAction(e -> new VentanaPrincipal(stage).mostrar());

        HBox pie = new HBox(btnVolver);
        pie.setPadding(new Insets(10));
        pie.setAlignment(Pos.CENTER_RIGHT);

        root.setBottom(pie);

        Scene scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.setTitle("Control de Stock");
        stage.show();
    }

    //STOCK ACTUAL
    private VBox construirTabStockActual() {

        TableColumn<Producto, String> colNombre = new TableColumn<>("Producto");
        colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombreProducto()));
        colNombre.setPrefWidth(250);

        TableColumn<Producto, String> colUnidad = new TableColumn<>("Unidad");
        colUnidad.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getNombreUnidad() + " (" + c.getValue().getAbreviaturaUnidad() + ")"
        ));
        colUnidad.setPrefWidth(180);

        TableColumn<Producto, Double> colStock = new TableColumn<>("Stock Actual");
        colStock.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getStockActual()).asObject());
        colStock.setPrefWidth(150);

        tablaStockActual.getColumns().clear();
        tablaStockActual.getColumns().addAll(colNombre, colUnidad, colStock);

        recargarTablaStockActual();

        VBox cont = new VBox(10, tablaStockActual);
        cont.setPadding(new Insets(10));
        return cont;
    }

    private void recargarTablaStockActual() {
        try {
            List<Producto> productos = productoControlador.listarProductos();
            tablaStockActual.setItems(FXCollections.observableArrayList(productos));
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error al cargar productos: " + e.getMessage()).showAndWait();
        }
    }

    //DEMANDA VS STOCK

    private VBox construirTabDemanda() {

        DatePicker dpFecha = new DatePicker(LocalDate.now());
        Button btnCargar = new Button("Cargar");
        HBox barra = new HBox(10, new Label("Fecha:"), dpFecha, btnCargar);
        barra.setAlignment(Pos.CENTER_LEFT);
        barra.setPadding(new Insets(10));

        TableView<StockProductoDia> tabla = new TableView<>();

        TableColumn<StockProductoDia, String> colProd = new TableColumn<>("Producto");
        colProd.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombreProducto()));
        colProd.setPrefWidth(250);

        TableColumn<StockProductoDia, Double> colStock = new TableColumn<>("Stock Actual");
        colStock.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getStockActual()).asObject());
        colStock.setPrefWidth(120);

        TableColumn<StockProductoDia, Double> colPedida = new TableColumn<>("Cantidad Pedida");
        colPedida.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getCantidadPedida()).asObject());
        colPedida.setPrefWidth(140);

        TableColumn<StockProductoDia, Double> colFalta = new TableColumn<>("Faltante");
        colFalta.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getFaltante()).asObject());
        colFalta.setPrefWidth(120);

        tabla.getColumns().addAll(colProd, colStock, colPedida, colFalta);

        // Filas rojas si faltante > 0
        tabla.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(StockProductoDia item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle("");
                } else if (item.getFaltante() > 0) {
                    setStyle("-fx-background-color: #ffb3b3;"); // rojo clarito
                } else {
                    setStyle("");
                }
            }
        });

        btnCargar.setOnAction(e -> {
            LocalDate fecha = dpFecha.getValue();
            if (fecha == null) return;
            try {
                List<StockProductoDia> lista = stockControlador.obtenerDemandaPorFecha(fecha);
                tabla.setItems(FXCollections.observableArrayList(lista));
            } catch (SQLException ex) {
                new Alert(Alert.AlertType.ERROR,
                        "Error al cargar demanda por día: " + ex.getMessage()).showAndWait();
            }
        });

        // Cargar automáticamente para la fecha de hoy al abrir
        btnCargar.fire();

        VBox cont = new VBox(10, barra, tabla);
        cont.setPadding(new Insets(10));
        return cont;
    }

    //AGREGAR STOCK

    private VBox construirTabAgregarStock() {

        Label lblTitulo = new Label("Agregar Stock a un Producto");
        lblTitulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label lblProducto = new Label("Producto:");
        ComboBox<Producto> cmbProducto = new ComboBox<>();

        try {
            List<Producto> productos = productoControlador.listarProductos();
            cmbProducto.setItems(FXCollections.observableArrayList(productos));
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

        Label lblCantidad = new Label("Cantidad a agregar:");
        TextField txtCantidad = new TextField();

        Button btnAgregar = new Button("Agregar al Stock");

        btnAgregar.setOnAction(e -> {
            try {
                Producto p = cmbProducto.getValue();
                if (p == null) {
                    new Alert(Alert.AlertType.WARNING, "Seleccioná un producto.").showAndWait();
                    return;
                }

                if (txtCantidad.getText().isBlank()) {
                    new Alert(Alert.AlertType.WARNING, "Ingresá una cantidad.").showAndWait();
                    return;
                }

                double cantidad = Double.parseDouble(txtCantidad.getText());
                if (cantidad <= 0) {
                    throw new NumberFormatException();
                }

                stockControlador.agregarStock(p.getIdProducto(), cantidad);

                new Alert(Alert.AlertType.INFORMATION,
                        "Stock actualizado correctamente.").showAndWait();

                // Refrescar tabla de stock actual
                recargarTablaStockActual();

                // Limpiar campos
                txtCantidad.clear();
                cmbProducto.getSelectionModel().clearSelection();

            } catch (NumberFormatException ex) {
                new Alert(Alert.AlertType.WARNING,
                        "La cantidad debe ser un número mayor a 0.").showAndWait();
            } catch (SQLException ex) {
                new Alert(Alert.AlertType.ERROR,
                        "Error al actualizar el stock: " + ex.getMessage()).showAndWait();
            }
        });

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(15);
        grid.setVgap(12);

        grid.add(lblProducto, 0, 0);
        grid.add(cmbProducto, 1, 0);
        grid.add(lblCantidad, 0, 1);
        grid.add(txtCantidad, 1, 1);
        grid.add(btnAgregar, 1, 2);

        VBox cont = new VBox(15, lblTitulo, grid);
        cont.setAlignment(Pos.TOP_CENTER);
        cont.setPadding(new Insets(20));

        return cont;
    }
}
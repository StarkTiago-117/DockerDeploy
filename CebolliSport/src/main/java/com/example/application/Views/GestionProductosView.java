package com.example.application.Views;

import com.example.application.Services.Producto_Clase;
import com.example.application.Services.GestionMenu_Back;

import jakarta.validation.ValidationException;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.List;
import java.util.ArrayList;


@PageTitle("CebolliSport")//Controla el título de la pestaña
@Route("Gestion")//Controla la ruta de la vista
@Menu(order = 1, icon = LineAwesomeIconUrl.TRUCK_SOLID)//Controla el icono de la vista
public class GestionProductosView extends VerticalLayout {

    // --- Listas y Grids principales ---
    public List<Producto_Clase> PRODUCTOS = new ArrayList<>();
    public Grid<Producto_Clase> GRID = new Grid<>(Producto_Clase.class);

    // --- Campos de texto y selección ---
    public TextField IMAGEURL_FIELD = new TextField("URL de Imagen");
    public TextField STOCK_FIELD = new TextField("Stock");
    public TextField PRICE_FIELD = new TextField("Precio");

    public ComboBox<String> MODEL_FIELD = new ComboBox<>("Modelo");
    public ComboBox<String> BRAND_FIELD = new ComboBox<String>("Marca");
    public ComboBox<String> MODEL_FILTRO = new ComboBox<>("Filtrar Modelo");
    public ComboBox<String> COLOR_FIELD = new ComboBox<>("Colores");
    
    public Select<String> TYPE_FIELD = new Select<>();
    public Select<String> SIZE_FIELD = new Select<>();
    public Select<String> GENERE_FIELD = new Select<>();

    // --- Notificación actual ---
    private Notification NOTIFICACION_ACTUAL = null;

    public GestionProductosView() {

        try {
            // --- Botones principales ---
            Button addButton = new Button("Añadir Producto");
            Button removeButton = new Button("Eliminar seleccionado");
            Button modifyButton = new Button("Modificar seleccionado");

            // --- Layouts principales ---
            HorizontalLayout firstRow = new HorizontalLayout();
            HorizontalLayout secondRow = new HorizontalLayout();
            HorizontalLayout buttonRow = new HorizontalLayout();
            VerticalLayout formLayout = new VerticalLayout();

            // --- Inicialización de campos y valores por defecto ---
            String regexURL = "^https.*";
            String[] tallasRopa = {"XS", "S", "M", "L", "XL", "XXL", "Unica"};
            String[] tallasZapatos = {
                "33.5", "34", "35", "36", "36.5", "37", "37.5",
                "38", "39", "39.5", "40", "40.5", "41", "41.5",
                "42", "42.5", "43"
            };
            String[] generos = {"Hombre", "Mujer", "Unisex", "Niño", "Niña", "-"};
            String[] tipoProduct = {"Calzado", "Ropa", "Accesorio"};

            // --- Título ---
            H4 title = new H4("Gestión de Inventario || Productos");
            Hr lineaDiv = new Hr();
            title.getStyle().set("text-align", "center");
            title.getStyle().set("font-size", "24");
            title.getStyle().set("margin", "0 auto");

            // --- Configuración de campos ---
            IMAGEURL_FIELD.setClearButtonVisible(true);
            IMAGEURL_FIELD.setWidthFull();
            IMAGEURL_FIELD.setMaxWidth("250px");
            IMAGEURL_FIELD.setMinWidth("50px");
            
            MODEL_FIELD.setWidthFull();
            MODEL_FIELD.setMaxWidth("250px");
            MODEL_FIELD.setMinWidth("50px");

            BRAND_FIELD.setWidthFull();
            BRAND_FIELD.setMaxWidth("250px");
            BRAND_FIELD.setMinWidth("50px");

            SIZE_FIELD.setLabel("Talla");
            SIZE_FIELD.setWidthFull();
            SIZE_FIELD.setMaxWidth("250px");
            SIZE_FIELD.setMinWidth("50px");

            GENERE_FIELD.setLabel("Genero");
            GENERE_FIELD.setWidthFull();
            GENERE_FIELD.setMaxWidth("250px");
            GENERE_FIELD.setMinWidth("50px");
            GENERE_FIELD.setItems(generos);

            COLOR_FIELD.setWidthFull();
            COLOR_FIELD.setMaxWidth("250px");
            COLOR_FIELD.setMinWidth("50px");

            TYPE_FIELD.setLabel("Tipo De Producto");
            TYPE_FIELD.setItems(tipoProduct);
            TYPE_FIELD.setWidthFull();
            TYPE_FIELD.setMaxWidth("250px");
            TYPE_FIELD.setMinWidth("50px");

            STOCK_FIELD.setWidthFull();
            STOCK_FIELD.setMaxWidth("250px");
            STOCK_FIELD.setMinWidth("50px");

            PRICE_FIELD.setWidthFull();
            PRICE_FIELD.setMaxWidth("250px");
            PRICE_FIELD.setMinWidth("50px");

            MODEL_FILTRO.setWidthFull();
            MODEL_FILTRO.setMaxWidth("250px");
            MODEL_FILTRO.setMinWidth("50px");


            // --- Configuración de ComboBoxes ---
            establecerComboBox();

            // Listener para actualizar las tallas según el tipo y género
            TYPE_FIELD.addValueChangeListener(event -> {
                actualizarTallas(SIZE_FIELD, TYPE_FIELD, GENERE_FIELD, tallasRopa, tallasZapatos);
            });

            // Asignación de placeholders
            PRICE_FIELD.setPlaceholder("$0.00");
            STOCK_FIELD.setPlaceholder("0 Unidades");
            IMAGEURL_FIELD.setPlaceholder("https://...");

            // --- Configuración del Grid ---
            GRID.setColumns("model", "brand", "size", "color", "type", "genero", "stock", "price");

            // Columna de imagen
            GRID.addColumn(new ComponentRenderer<>(producto -> {
                Image image = new Image(producto.getImage(), "Imagen del producto");
                image.setWidth("100px");
                image.setHeight("100px");
                return image;
            })).setHeader("Imagen");

            // Encabezados personalizados
            GRID.getColumnByKey("model").setHeader("Modelo");
            GRID.getColumnByKey("brand").setHeader("Marca");
            GRID.getColumnByKey("size").setHeader("Talla");
            GRID.getColumnByKey("color").setHeader("Colores");
            GRID.getColumnByKey("type").setHeader("Tipo");
            GRID.getColumnByKey("genero").setHeader("Genero");
            GRID.getColumnByKey("stock").setHeader("Stock");
            GRID.getColumnByKey("price").setHeader("Precio");

            // --- Cargar productos existentes ---
            llenarLista(PRODUCTOS, false);
            GRID.setItems(PRODUCTOS);

            // --- Liteneer para actualizar los datos del campo al seleccionar un modelo que ya existe---
            MODEL_FIELD.addValueChangeListener(event -> {
                try{
                    // Encontrar el producto correspondiente al modelo seleccionado
                    String selectedModel = event.getValue();
                    // Si el modelo es nulo, no hacer nada
                    if (selectedModel != null) {
                        // Buscar el producto en la lista de productos
                        for (Producto_Clase producto : PRODUCTOS) {
                            // Si el modelo del producto coincide con el modelo seleccionado, actualizar los campos con sus datos
                            // Pero limpiamos los de talla stock y precio ya que estos son los que varian 
                            if (producto.getModel().equals(selectedModel)) {
                                llenarCamposModificar(producto);
                                SIZE_FIELD.clear();
                                STOCK_FIELD.clear();
                                PRICE_FIELD.clear();
                                break;
                            }
                        }
                    }

                }catch(Exception e){
                    mostrarNotificacionUnica("Error al seleccionar el modelo: " + e.getMessage());
                }
            });
          
            // --- Botón Añadir Producto ---
            addButton.addClickListener(event -> {
                // Creamos los campos necesarios para la validación
                int stock = 0;
                double price = 0;
                Producto_Clase product;

                // Establecemos los limites y ecepciones
                int maxLongitud = 40;

                int minStock = 0;
                int maxStock = 1000;

                double minPrice = 0;
                double maxPrice = 9999999;

                try {
                    // Limpiamos el campo de filtro
                    MODEL_FILTRO.clear();

                    // Validación de campos vacíos
                    if (
                        IMAGEURL_FIELD.getValue() == null || IMAGEURL_FIELD.getValue().trim().isEmpty() ||
                        MODEL_FIELD.getValue() == null || MODEL_FIELD.getValue().trim().isEmpty() ||
                        BRAND_FIELD.getValue() == null || BRAND_FIELD.getValue().trim().isEmpty() ||
                        SIZE_FIELD.getValue() == null || SIZE_FIELD.getValue().trim().isEmpty() ||
                        COLOR_FIELD.getValue() == null || COLOR_FIELD.getValue().trim().isEmpty() ||
                        STOCK_FIELD.getValue() == null || STOCK_FIELD.getValue().trim().isEmpty() ||
                        GENERE_FIELD.getValue() == null || GENERE_FIELD.getValue().trim().isEmpty() ||
                        PRICE_FIELD.getValue() == null || PRICE_FIELD.getValue().trim().isEmpty()
                    ) {
                        throw new NullPointerException("Por favor, completa todos los campos.");
                    }

                    // Validación de longitud de campos
                    if (
                        MODEL_FIELD.getValue().trim().length() > maxLongitud ||
                        BRAND_FIELD.getValue().trim().length() > maxLongitud ||
                        COLOR_FIELD.getValue().trim().length() > maxLongitud ||
                        TYPE_FIELD.getValue().trim().length() > maxLongitud
                    ) {
                        throw new ValidationException("Ningún campo puede tener más de 50 caracteres.");
                    }

                    // Validación de URL de imagen
                    if (!IMAGEURL_FIELD.getValue().matches(regexURL)) {
                        throw new ValidationException("La URL de la imagen no es válida.");
                    }

                    // Validación de stock
                    if (!STOCK_FIELD.getValue().trim().matches("\\d+")) {
                        STOCK_FIELD.clear();
                        throw new NumberFormatException("El Stock debe ser un numero valido");
                    }
                    stock = Integer.parseInt(STOCK_FIELD.getValue().trim());
                    if (stock < minStock || stock > maxStock) {
                        STOCK_FIELD.clear();
                        throw new NumberFormatException("El stock no puede ser negativo. O superior a 1000 Unidades.");
                    }

                    // Validación de precio
                    try {
                        price = Double.parseDouble(PRICE_FIELD.getValue().trim());
                    } catch (NumberFormatException e) {
                        PRICE_FIELD.clear();
                        throw new NumberFormatException("El campo Precio debe ser un número válido.");
                    }
                    if (price < minPrice || price > maxPrice) {
                        PRICE_FIELD.clear();
                        throw new NumberFormatException("El precio no puede ser negativo. O superior a 10000000$.");
                    }

                    // Verificar que el producto no exista
                    for (Producto_Clase producto : PRODUCTOS) {
                        if (producto.getModel().equals(MODEL_FIELD.getValue()) &&
                            producto.getSize().equals(SIZE_FIELD.getValue())) {
                            throw new ValidationException("El producto ya existe.");
                        }
                    }

                    // Agregar el producto a la base de datos y a la lista
                    product = agregarABaseDatos();
                    // Agregamos el producto a la lista y actualizamos el Grid
                    PRODUCTOS.add(product);
                    GRID.setItems(PRODUCTOS);
                    // Limpiamos los campos de texto
                    limpiarCampos();
                    // Actualizamos los ComboBoxes
                    establecerComboBox();
                    // Mostramos una notificación de éxito
                    mostrarNotificacionUnica("Producto añadido correctamente.");

                } catch (NullPointerException ne) {
                    mostrarNotificacionUnica(ne.getMessage());
                } catch (ValidationException ve) {
                    mostrarNotificacionUnica(ve.getMessage());
                } catch (NumberFormatException nf) {
                    mostrarNotificacionUnica(nf.getMessage());
                } catch (Exception e) {
                    mostrarNotificacionUnica(e.getMessage());
                }
            });

            // --- Botón Eliminar Producto ---
            removeButton.addClickListener(event -> {

                try {
                    
                    // Creamos los varibales necesarias 
                    Producto_Clase selected = GRID.asSingleSelect().getValue();
                    
                    // Verificamos si hay un producto seleccionado
                    if (selected != null) {
                        // Eliminamos el producto de la base de datos y de la lista
                        GestionMenu_Back.eliminarProducto(selected.getModel(), selected.getSize());
                        // Eliminamos el producto de la lista y actualizamos el Grid
                        PRODUCTOS.remove(selected);
                        GRID.setItems(PRODUCTOS);
                        // mostramos una notificación de éxito
                        mostrarNotificacionUnica("Tabla Actulizada.");
                        
                    } else {
                        throw new NullPointerException("Selecciona un producto para eliminar.");
                    }

                

                } catch (NullPointerException np) {
                    mostrarNotificacionUnica(np.getMessage());
                } catch (Exception e) {
                    mostrarNotificacionUnica("Error al eliminar el producto: " + e.getMessage());
                }
            });

            // --- Botón Modificar Producto ---
            modifyButton.addClickListener(event -> {
                
                try {
                    
                    // Creamos las variables necesarias
                    Producto_Clase selected = GRID.asSingleSelect().getValue();

                    // Verificamos si hay un producto seleccionado
                    if (selected != null) {
                        // Limpiamos los campos antes de llenarlos con el producto a modificar
                        limpiarCampos();
                        // llenamos los campos de texto con los datos del producto seleccionado
                        llenarCamposModificar(selected);
                        // eliminamos el producto de la lista y del Grid
                        removeButton.click();
                        // deseleccionamos el producto en el Grid
                        GRID.deselectAll();

                    } else {
                        throw new NullPointerException("Selecciona un producto para modificar.");
                    }

                } catch (NullPointerException e) {
                    mostrarNotificacionUnica(e.getMessage());
                } catch (Exception e) {
                    mostrarNotificacionUnica("Error al modificar el producto: " + e.getMessage());
                }
            });

            // --- Filtro de modelo ---
            MODEL_FILTRO.setPlaceholder("Filtrar por modelo");
            MODEL_FILTRO.setClearButtonVisible(true);
            MODEL_FILTRO.setWidth("200px");

            MODEL_FILTRO.addValueChangeListener(event -> {
                // Creamos las variables necesarias
                String filtro = event.getValue();

                // Verificamos si el filtro es nulo o vacío
                if (filtro == null || filtro.trim().isEmpty()) {
                    // Si el filtro está vacío, mostramos todos los productos
                    GRID.setItems(PRODUCTOS);

                } else {
                    // Si hay un filtro, filtramos los productos por el modelo
                    GRID.setItems(PRODUCTOS.stream()
                        .filter(p -> p.getModel().toLowerCase().contains(filtro.trim().toLowerCase()))
                        .toList());
                }
            });

            // --- Layouts de la interfaz ---
            firstRow.add(IMAGEURL_FIELD, MODEL_FIELD, BRAND_FIELD, SIZE_FIELD, GENERE_FIELD);
            firstRow.setWidthFull();
            firstRow.setAlignItems(Alignment.CENTER);
            firstRow.setSpacing(true);

            addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            secondRow.add(COLOR_FIELD, TYPE_FIELD, STOCK_FIELD, PRICE_FIELD, addButton); // ! BAJO ESTO MODIFIQUE LA ALINEACION DEL BOTON
            secondRow.setAlignSelf(FlexComponent.Alignment.END, addButton);
            secondRow.setWidthFull();
            secondRow.setAlignItems(Alignment.CENTER);
            secondRow.setSpacing(true);

            buttonRow.add(MODEL_FILTRO, modifyButton, removeButton); // ! BAJO ESTO MODIFIQUE LA ALINEACION DEL BOTON
            buttonRow.setAlignSelf(FlexComponent.Alignment.END, modifyButton);
            buttonRow.setAlignSelf(FlexComponent.Alignment.END, removeButton);
            buttonRow.setWidthFull();
            buttonRow.setAlignItems(Alignment.CENTER);
            buttonRow.setSpacing(true);

            formLayout.add(firstRow, secondRow, buttonRow);
            formLayout.setWidthFull();

            // --- Agregar todo al layout principal ---
            add(title, lineaDiv, formLayout, GRID);

        } catch (Exception e) {
            Notification.show("Error al cargar la gestión de productos: " + e.getMessage());
        }
    }


    // --- Funciones Publicas ---

    // --- Notificación única (no se acumulan) ---
    // Este método asegura que solo una notificación esté abierta a la vez.
    public void mostrarNotificacionUnica(String mensaje) {
        try {

            // Verificar si la notificación actual está abierta y cerrarla
            if (NOTIFICACION_ACTUAL != null && NOTIFICACION_ACTUAL.isOpened()) {
                NOTIFICACION_ACTUAL.close();
            }
            // Crear una nueva notificación
            NOTIFICACION_ACTUAL = new Notification(mensaje, 3000, Notification.Position.TOP_CENTER); // ! MODIFICADO POR EL PANA CEBOLLAS
            NOTIFICACION_ACTUAL.open();

        } catch (Exception e) {
            Notification.show("Error al mostrar la notificación: " + e.getMessage());
        }
    }


    // --- Llenar combobox con valores dinámicos ---
    // Este método llena un ComboBox con los productos de un índice específico.
    public void llenarComboBox(ComboBox<String> comboBox, int index) throws Exception {
     
        try {
            // Configuración del ComboBox
            comboBox.setClearButtonVisible(true);
            comboBox.setAllowCustomValue(true);

            // Llenar el ComboBox con los productos del índice especificado
            comboBox.setItems(GestionMenu_Back.leerIndexProducto(index));
            
            // Creamos un listener para manejar valores personalizados
            comboBox.addCustomValueSetListener(event -> {
                // Creamos las variables necesarias
                String customValue = event.getDetail();
                // Añadimos el valor personalizado al ComboBox
                comboBox.setValue(customValue);
            });

        } catch (Exception e) {
            Notification.show("Error al cargar los productos: " + e.getMessage());
        }
    }

    // --- Metodo para llenar las SelectBox de tallas segun el tipo de producto y genero
    public void actualizarTallas(Select<String> sizeField, Select<String> typeField, Select<String> genereField, String[] tallasRopa, String[] tallasZapatos) {
        
        String tipo = typeField.getValue();

        if ("Calzado".equals(tipo)) {
            sizeField.setItems(tallasZapatos);
        } else if ("Ropa".equals(tipo)) {
            sizeField.setItems(tallasRopa);
        } else if ("Accesorio".equals(tipo)) {
            sizeField.setItems("Sin Talla");
        }
        
    }

    // --- Establecer ComboBox ---
    // Este método establece los ComboBox con los productos de la base de datos.
    public void establecerComboBox() throws Exception {
        try {
            // Definimos los indices
            int modelIndex = 1;
            int marcaIndex = 2;
            int colorIndex = 4;
            
            llenarComboBox(MODEL_FIELD, modelIndex);
            llenarComboBox(MODEL_FILTRO, modelIndex);
            llenarComboBox(BRAND_FIELD, marcaIndex);
            llenarComboBox(COLOR_FIELD, colorIndex);

           } catch (Exception e) {
            Notification.show("Error al establecer los ComboBox: " + e.getMessage());
        }
    }


    // --- Llenar lista de productos --- 
    // Este método llena una lista de productos a partir de un archivo TXT (Base de Datos del Productos).
    // Si variantes es true, no se añaden productos repetidos (Mismo modelo).
    public void llenarLista(List<Producto_Clase> productos, boolean variantes) throws Exception {
      
        try {
            // Creamos las varibles necesarias
            List<String> apariciones = new ArrayList<>();
            
            // Iteramos sobre las filas del archivo de productos
            for (String[] fila : GestionMenu_Back.leerProductos()) {

                // Verificamos si el modelo ya ha sido añadido y si variantes es true, continuamos
                if (apariciones.contains(fila[1]) && variantes) continue;

                // Añadimos el modelo a la lista de apariciones y al producto
                apariciones.add(fila[1]);

                // Añadimos el producto a la lista de productos
                productos.add(new Producto_Clase(
                    fila[0], fila[1], fila[2], fila[3], fila[4], fila[5],
                    Integer.parseInt(fila[6]), fila[7], Double.parseDouble(fila[8])
                ));
            }

        } catch (Exception e) {
            Notification.show("Error al cargar los productos: " + e.getMessage());
        }
    }


    // --- Limpiar campos de texto ---
    // Este método limpia todos los campos de texto del formulario.
    public void limpiarCampos() throws Exception {

        try {
            // Limpiamos todos los campos de texto
            IMAGEURL_FIELD.clear();
            MODEL_FIELD.clear();
            BRAND_FIELD.clear();
            SIZE_FIELD.clear();
            COLOR_FIELD.clear();
            TYPE_FIELD.clear();
            STOCK_FIELD.clear();
            GENERE_FIELD.clear();
            PRICE_FIELD.clear();

        } catch (Exception e) {
            Notification.show("Error al limpiar los campos: " + e.getMessage());
        }
    }


    // --- Agregar producto a la base de datos --- 
    // Este método crea un nuevo producto y lo agrega a la base de datos.
    // Retorna el producto creado.
    public Producto_Clase agregarABaseDatos(){
        try {
            // Creamos un nuevo producto con los valores de los campos de texto
            Producto_Clase product = new Producto_Clase(
                IMAGEURL_FIELD.getValue().trim(),
                GestionMenu_Back.capitalizar(MODEL_FIELD.getValue().trim()),
                GestionMenu_Back.capitalizar(BRAND_FIELD.getValue().trim()),
                SIZE_FIELD.getValue().trim().toUpperCase(),
                GestionMenu_Back.capitalizar(COLOR_FIELD.getValue().trim()),
                GestionMenu_Back.capitalizar(TYPE_FIELD.getValue().trim()),
                Integer.parseInt(STOCK_FIELD.getValue().trim()),
                GestionMenu_Back.capitalizar(GENERE_FIELD.getValue().trim()),
                Double.parseDouble(PRICE_FIELD.getValue().trim())
            );
            // Agregamos el producto a la base de datos
            GestionMenu_Back.agregarProducto(
                product.getImage(), product.getModel(), product.getBrand(), product.getSize(),
                product.getColor(), product.getType(), product.getStock(), product.getGenero(),
                String.valueOf(product.getPrice())
            );

            // Retornamos el producto creado
            return product;

        } catch (Exception e) {
            Notification.show("Error al agregar el producto: " + e.getMessage());
            return null;
        }
    }


    // --- Llenar campos para modificar producto ---
    // Este método llena los campos de texto con los datos del producto seleccionado para su modificación.
    public void llenarCamposModificar(Producto_Clase selected) throws Exception {
        
        try {

            // LLenamos los campos de texto con los datos del producto seleccionado
            IMAGEURL_FIELD.setValue(selected.getImage());
            MODEL_FIELD.setValue(selected.getModel());
            BRAND_FIELD.setValue(selected.getBrand());
            SIZE_FIELD.setValue(selected.getSize());
            COLOR_FIELD.setValue(selected.getColor());
            TYPE_FIELD.setValue(selected.getType());
            STOCK_FIELD.setValue(String.valueOf(selected.getStock()));
            GENERE_FIELD.setValue(selected.getGenero());
            PRICE_FIELD.setValue(String.valueOf(selected.getPrice()));

        } catch (Exception e) {
            Notification.show("Error al llenar los campos para modificar: " + e.getMessage());
        }
    }
}
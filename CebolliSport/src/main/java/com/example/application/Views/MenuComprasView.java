package com.example.application.Views;

import com.vaadin.flow.component.UI;
import com.example.application.Services.GestionMenu_Back;
import com.example.application.Services.Producto_Clase;
import com.example.application.Services.ProductoCarrito_Clase;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.stream.Collectors;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Hr;

import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import org.vaadin.lineawesome.LineAwesomeIconUrl;


import com.vaadin.flow.router.BeforeLeaveEvent;    // ! Se importa lo necesario para evitar que salga de la vista
import com.vaadin.flow.router.BeforeLeaveObserver;


@PageTitle("CebolliSport")//Controla el título de la pestaña
@Route("")//Controla la ruta de la vista
@Menu(order = 0, icon = LineAwesomeIconUrl.BOX_OPEN_SOLID)//Controla el icono de la vista
public class MenuComprasView extends Composite<VerticalLayout> implements BeforeLeaveObserver {

    // Advertencia al salir de la vista
    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        // Este método se ejecuta antes de que el usuario abandone la vista (por ejemplo, al navegar a otra página).

        if (!CARRITO_PRODUCTOS.isEmpty()) {
            // Si el carrito NO está vacío, se detiene la navegación para mostrar un diálogo de confirmación.
            event.postpone();

            // Se crea un diálogo personalizado para advertir al usuario.
            Dialog dialogo = new Dialog();
            Div texto = new Div();
            Button botonConfirmar = new Button();
            Button botonCancelar = new Button();
            HorizontalLayout contenedorBotones = new HorizontalLayout(botonConfirmar, botonCancelar);
            VerticalLayout layout = new VerticalLayout(texto, contenedorBotones);

            // Configuración de los botones y el texto del diálogo
            botonConfirmar.setText("Confirmar");
            botonCancelar.setText("Cancelar");
            botonConfirmar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            texto.getStyle().set("white-space", "pre-line");
            texto.setText("¡Atención! Esta acción es irreversible.\nAl salir de esta pestaña se perderán los productos que haya registrado.\n¿Desea Continuar?");

            // Configuración del diálogo para que no se cierre accidentalmente
            dialogo.setVisible(true);
            dialogo.setCloseOnEsc(false);
            dialogo.setCloseOnOutsideClick(false);
            dialogo.add(layout);

            // Si el usuario confirma, se cierra el diálogo y se continúa la navegación
            botonConfirmar.addClickListener(e -> {
                dialogo.close();
                event.getContinueNavigationAction().proceed();
            });

            // Si el usuario cancela, se cierra el diálogo y se cancela la navegación
            botonCancelar.addClickListener(e -> {
                dialogo.close();
                event.getContinueNavigationAction().cancel();
            });

            // Se muestra el diálogo
            dialogo.open();
        }
    }
    Dialog dialogoInteractivo = new Dialog();

    // --- Listas y Grids principales ---
    private final Grid<Producto_Clase> PRODUCTO_GRID = new Grid<>();
    private final List<Producto_Clase> PRODUCTOS = new ArrayList<>();
    private final List<Producto_Clase> PRODUCTOS_FILTRADOS = new ArrayList<>();
    private final List<ProductoCarrito_Clase> CARRITO_PRODUCTOS = new ArrayList<>();
    private final Grid<ProductoCarrito_Clase> CARRITO_GRID = new Grid<>(ProductoCarrito_Clase.class);

    public MenuComprasView() {

        try {
            
            // --- Filtros y campos de selección ---
            ComboBox<String> modelo = new ComboBox<>("Modelo");
            Select<String> marca = new Select<>();
            Select<String> color = new Select<>();
            Select<String> genero = new Select<>();
            Select<String> tipo = new Select<>();
            Button limpiarFiltrosButton = new Button("Limpiar Filtros");

            // --- Layouts principales ---
            VerticalLayout filtros = new VerticalLayout();
            HorizontalLayout layoutFiltros = new HorizontalLayout();
            HorizontalLayout layoutFiltros2 = new HorizontalLayout();

            VerticalLayout productLayout = new VerticalLayout();

            HorizontalLayout tituloCarrito = new HorizontalLayout();
            VerticalLayout carritoLayout = new VerticalLayout();
            HorizontalLayout productoSelecMain = new HorizontalLayout();
            VerticalLayout productoSelecDer = new VerticalLayout();
            VerticalLayout productoSelecIzq = new VerticalLayout();
            VerticalLayout estadisticas = new VerticalLayout();

            HorizontalLayout precioFinalLayout = new HorizontalLayout();

            // --- Panel producto seleccionado ---
            Select<String> selectTalla = new Select<>();
            Span precioLabelTotal = new Span();
            Span modeloLabel = new Span();
            Span precioUnidad = new Span();
            Span cantLabel = new Span();
            Span maxStock = new Span();
            Span totalLabel = new Span();
            Span avisoAgotado = new Span();

            // --- Botones principales ---
            Button irAlCarritoButton = new Button("Finalizar Registro");  // ! MODIFICADO
            Button agregarUnidad = new Button("Agregar Producto");
            Button eliminarUnidad = new Button("Eliminar Producto");

            // --- Título principal ---
            H4 titulo = new H4("Menú de Compra Productos || Registradora");
            Hr lineaDiv = new Hr();

            // --- Imagen del carrito ---
            Image imagenCarrito = new Image("line-awesome/svg/shopping-cart-solid.svg", "Carrito");
            // -- Index Pocision de elmentos en Base de datos --- 
            int marcaIndex = 2;
            int colorIndex = 4;
            int generoIndex = 7;
            int tipoIndex = 5;

            // --- Instancia de gestión de productos ---
            GestionProductosView gestionView = new GestionProductosView();
            gestionView.llenarLista(PRODUCTOS, true);

            // --- Cargar productos en la lista filtrada ---
            PRODUCTOS_FILTRADOS.addAll(PRODUCTOS);

            // --- Configuración de filtros ---
            modelo.setItems(GestionMenu_Back.leerIndexProducto(1));
            modelo.setPlaceholder("Seleccione");
            llenarCampos(marcaIndex, marca, "Marca");
            llenarCampos(colorIndex, color, "Color");
            llenarCampos(generoIndex, genero, "Genero");
            llenarCampos(tipoIndex, tipo, "Tipo");

            // --- Listener para limpiar filtros ---
            limpiarFiltrosButton.addClickListener(event -> {
                modelo.clear();
                marca.clear();
                color.clear();
                genero.clear();
                tipo.clear();
                filtrarProductos(null, null, null, null, null);
            });

            // --- Listener para cambios en los filtros ---
            modelo.addValueChangeListener(e -> filtrarProductos(modelo.getValue(), marca.getValue(), color.getValue(), genero.getValue(), tipo.getValue()));
            marca.addValueChangeListener(e -> filtrarProductos(modelo.getValue(), marca.getValue(), color.getValue(), genero.getValue(), tipo.getValue()));
            color.addValueChangeListener(e -> filtrarProductos(modelo.getValue(), marca.getValue(), color.getValue(), genero.getValue(), tipo.getValue()));
            genero.addValueChangeListener(e -> filtrarProductos(modelo.getValue(), marca.getValue(), color.getValue(), genero.getValue(), tipo.getValue()));
            tipo.addValueChangeListener(e -> filtrarProductos(modelo.getValue(), marca.getValue(), color.getValue(), genero.getValue(), tipo.getValue()));

            // --- Configuración de layouts de filtros ---
            modelo.setWidthFull();
            modelo.setMaxWidth("250px");
            modelo.setMinWidth("50px");
            marca.setWidthFull();
            marca.setMaxWidth("250px");
            marca.setMinWidth("50px");
            color.setWidthFull();
            color.setMaxWidth("250px");
            color.setMinWidth("50px");
            layoutFiltros.setWidthFull();
            layoutFiltros.add(modelo, marca, color);
            layoutFiltros.setAlignItems(FlexComponent.Alignment.CENTER);
            layoutFiltros.addClassName(Gap.MEDIUM);
            
            genero.setWidthFull();
            genero.setMaxWidth("250px");
            genero.setMinWidth("50px");
            tipo.setWidthFull();
            tipo.setMaxWidth("250px");
            tipo.setMinWidth("50px");
            layoutFiltros2.setWidthFull();
            layoutFiltros2.add(genero, tipo, limpiarFiltrosButton); // ! BAJO ESTO MODIFIQUE LA ALINEACION DEL BOTON
            layoutFiltros2.setAlignSelf(FlexComponent.Alignment.END, limpiarFiltrosButton);
            layoutFiltros2.setAlignItems(FlexComponent.Alignment.CENTER);
            layoutFiltros2.addClassName(Gap.MEDIUM);

            filtros.add(layoutFiltros, layoutFiltros2);

            // --- Configuración del grid de productos ---
            configurarGridDeProductos();
            PRODUCTO_GRID.setItems(PRODUCTOS_FILTRADOS);

            // --- Configuración del grid del carrito ---
            configurarCarritoGrid();

            // --- Configuración de layouts principales ---

            // --- Configuración del layout de productos ---
            productLayout.add(filtros);
            productLayout.setWidth("60%");
            productLayout.add(PRODUCTO_GRID);
            productLayout.setPadding(false);
            productLayout.setSpacing(false);

            // --- Configuración del layout del carrito ---
            carritoLayout.setWidth("40%");
            tituloCarrito.add(new H3("Carrito"));
            imagenCarrito.setWidth("50px");
            imagenCarrito.setHeight("50px");
            tituloCarrito.add(imagenCarrito);
            tituloCarrito.setAlignItems(FlexComponent.Alignment.CENTER);

            // --- Configuración del layout del producto seleccionado ---
            productoSelecMain.getStyle().set("background-color", "#e8ba9e7e");
            productoSelecMain.setSpacing(true);
            productoSelecMain.setWidth("557px");
            productoSelecDer.setWidth("auto");
            productoSelecDer.setVisible(false);
            productoSelecDer.setSpacing(false);
            productoSelecIzq.setWidth("auto");
            productoSelecIzq.setVisible(false);
            productoSelecIzq.setSpacing(false);

            selectTalla.setLabel("Talla disponible");

            // --- Configuración de estradisticas en el producto selecionado ---
            estadisticas.setSpacing(false);
            estadisticas.setPadding(false);
            avisoAgotado.setVisible(false);
            avisoAgotado.setText("A G O T A D O");
            avisoAgotado.getStyle().set("color", "red");
            avisoAgotado.getStyle().set("background-color", "#ffcccc");
            avisoAgotado.getStyle().set("border-radius", "12px");
            avisoAgotado.getStyle().set("padding", "4px 12px");
            estadisticas.add(maxStock, cantLabel, precioUnidad, precioLabelTotal, avisoAgotado);

            // --- Configuracion del layoot derecho del producto seleccionado ---
            modeloLabel.getStyle().set("font-weight", "bold");
            productoSelecDer.add(modeloLabel, selectTalla, estadisticas);

            // --- Configuracion del layot del botones de finalizar compra y total ---
            precioFinalLayout.setWidthFull();
            precioFinalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
            totalLabel.setText("Precio Total: " + "0 $");
            irAlCarritoButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            precioFinalLayout.add(irAlCarritoButton, totalLabel);

            // --- Botón finalizar compra ---
            irAlCarritoButton.addClickListener(event -> {

                // Verifica si el carrito está vacío antes de finalizar la compra
                if (CARRITO_PRODUCTOS.isEmpty()) {
                    gestionView.mostrarNotificacionUnica("El carrito está vacío. Agrega productos antes de finalizar el registro.");
                } else {
                    
                    // Mostramos la advertencia al finalizar compra
                    dialogoInteractivo = crearDialogo("¡Atención!\nSi continúa, no podrá agregar otro producto más a esta compra.\n¿Desea continuar?", ()->{
                        
                        try {
                            // Registra la compra de cada producto en el carrito
                            for (ProductoCarrito_Clase productoCarrito : CARRITO_PRODUCTOS) {
                                GestionMenu_Back.registrarCompra(productoCarrito.getModelo(), productoCarrito.getTalla() ,productoCarrito.getUnidades(), productoCarrito.getPrecioTotal());
                            }
                            gestionView.mostrarNotificacionUnica("Registro efectuado exitosamente.");
                            // Limpia el carrito y actualiza el grid
                            CARRITO_PRODUCTOS.clear();
                            CARRITO_GRID.setItems(CARRITO_PRODUCTOS);
                            // Actualiza el total del carrito
                            actualizarPrecioTotal(totalLabel);

                            UI.getCurrent().navigate("Compras"); // Esto redirige a la vista a la ruta Compras.
                        
                        } catch (Exception e) {
                            Notification.show("Error al finalizar la compra: " + e.getMessage());
                        }
                        
                    });
                    dialogoInteractivo.setHeaderTitle("Panel de Confirmación");
                    dialogoInteractivo.open();
                }
            });

            // --- Listener para selección en el grid de productos ---
            PRODUCTO_GRID.asSingleSelect().addValueChangeListener(event -> {
                try {
                    
                    // Creamos las variables necesarias
                    Producto_Clase producto = event.getValue();
                    Image imagenProducto = new Image();
                    HashMap<String, List<Double>> tallasPrecios;

                    // Limpia los campos de estadísticas y el layout del producto seleccionado
                    productoSelecMain.removeAll();
                    productoSelecIzq.removeAll();

                    if (producto != null) {

                        // Muestra el layout del producto seleccionado  
                        productoSelecMain.setVisible(true);

                        // Imagen del producto seleccionado
                        imagenProducto = new Image(producto.getImage(), "Imagen producto");
                        imagenProducto.setMaxWidth("250px");    // Límite máximo de ancho
                        imagenProducto.setMaxHeight("200px");   // Límite máximo de alto
                        imagenProducto.getStyle().set("object-fit", "contain"); // Ajusta la imagen sin deformarla

                        // Diccionario de tallas: talla -> [unidades, precio]
                        tallasPrecios = GestionMenu_Back.extraerTallarPrecio(producto.getModel());
                        selectTalla.setItems(tallasPrecios.keySet());

                        // Listener para seleccionar talla
                        selectTalla.addValueChangeListener(e -> {

                            // Creamos las variables necesarias
                            String tallaSeleccionada = e.getValue();
                            List<Double> datos;
                            int unidades = 0;
                            int cantUnidades = 0;
                            double precioUni = 0;
                            double precio = 0;

                            // Ocultamos el aviso de Agotado
                            avisoAgotado.setVisible(false);

                            // Mostrar estadísticas del producto seleccionado
                            estadisticas.setVisible(true);
                            
                            if (tallaSeleccionada != null && tallasPrecios.containsKey(tallaSeleccionada)) {

                                // Obtiene los datos de la talla seleccionada
                                datos = tallasPrecios.get(tallaSeleccionada);
                                unidades = datos.get(0).intValue();
                                precioUni = datos.get(1);
                                precio = precioUni;

                                // Buscamos el producto en el carrito de productos si es que ya existe y asignamos el precio y unidades
                                // correspondientes
                                ProductoCarrito_Clase encontrado = CARRITO_PRODUCTOS.stream()
                                    .filter(p -> p.getModelo().equals(producto.getModel()) && p.getTalla().equals(tallaSeleccionada))
                                    .findFirst()
                                    .orElse(null);

                                if (encontrado != null) {
                                    precio = encontrado.getPrecioTotal();
                                    cantUnidades = encontrado.getUnidades();
                                }

                                // Actualiza el objeto producto y los labels dependiendo de la talla seleccionada
                                producto.setSize(tallaSeleccionada);
                                producto.setStock(unidades);
                                producto.setPrice(precio);

                                // Actualiza los labels de estadísticas
                                maxStock.setText("Unidades Disponibles: " + unidades);
                                cantLabel.setText("Unidades Agregadas: " + cantUnidades);
                                precioUnidad.setText("Precio Unidad: " + precioUni + " $");
                                precioLabelTotal.setText("Precio: " + precio + " $");

                            } else {
                                // Si no hay talla seleccionada o no existe, ocultamos las estadísticas
                                estadisticas.setVisible(false);
                            }
                        });

                        // Actualiza el modelo del producto seleccionado y mostramos el layout del producto seleccionado
                        modeloLabel.setText("Modelo " + producto.getModel());
                        productoSelecDer.setVisible(true);
                        productoSelecIzq.setVisible(true);
                        
                        // Añadimos los componentes al layout del producto seleccionado
                        productoSelecIzq.add(imagenProducto, agregarUnidad, eliminarUnidad);
                        productoSelecMain.add(productoSelecIzq, productoSelecDer);

                    } else {

                        // Si no hay producto seleccionado, ocultamos el layout del producto seleccionado
                        productoSelecMain.setVisible(false);
                        productoSelecDer.setVisible(false);
                    }

                } catch (Exception e) {
                    Notification.show("Error al seleccionar talla: " + e.getMessage());
                }
            });

            // --- Lógica para agregar unidades ---
            agregarUnidad.addClickListener(e -> {

                // Creamos las variables necesarias
                Producto_Clase producto = PRODUCTO_GRID.asSingleSelect().getValue();
                double precioTotal = 0;


                try {
                    if (producto != null && selectTalla.getValue() != null) {

                        // Agrega el producto al carrito y obtiene el precio total
                        precioTotal = agregarAlCarrito(producto);

                        // Verifica si el precio total es negativo, lo que indica que se ha alcanzado el límite de stock
                        if (precioTotal < 0 || precioTotal == 0){
                            // Si esta agotado mostramos la etiqueta de agotado
                            if (precioTotal == 0) avisoAgotado.setVisible(true);
                            throw new Exception("Ya llego al limite de Stock");
                        }

                        // Actualiza los labels de estadísticas y el precio total
                        cantLabel.setText("Unidades Agregadas: " + String.valueOf((int)(precioTotal / producto.getPrice())));
                        precioLabelTotal.setText("Precio: " + precioTotal + " $");
                        actualizarPrecioTotal(totalLabel);
                    }
                    // Si no hay producto o talla seleccionada, lanza una excepción
                    else throw new Exception("Seleccione un producto y una talla.");

                } catch (Exception ex) {
                    gestionView.mostrarNotificacionUnica(ex.getMessage());
                }
            });

            // --- Lógica para eliminar unidades ---
            eliminarUnidad.addClickListener(e -> {

                // Creamos las variables necesarias
                Producto_Clase producto = PRODUCTO_GRID.asSingleSelect().getValue();
                double precioTotal = 0;

                try {
                    if (producto != null && selectTalla.getValue() != null) {
                        // Elimina una unidad del producto en el carrito y obtiene el precio total
                        precioTotal = eliminarDelCarrito(producto);

                        // Verifica si el precio total es negativo, lo que indica que el producto no estaba en el carrito
                        if (precioTotal < 0){
                            throw new Exception("El producto no esta agregado");
                        }

                        // Actualiza los labels de estadísticas y el precio total
                        cantLabel.setText("Unidades Agregadas: " + String.valueOf((int)(precioTotal / producto.getPrice())));
                        precioLabelTotal.setText("Precio Total: " + precioTotal + " $");
                        actualizarPrecioTotal(totalLabel);

                    }
                    // Si no hay producto o talla seleccionada, lanza una excepción
                    else throw new Exception("Seleccione un producto y una talla.");

                } catch (Exception ex) {
                    gestionView.mostrarNotificacionUnica(ex.getMessage());
                }
            });

            // --- Listener para selección en el grid del carrito ---
            CARRITO_GRID.addSelectionListener(event -> {
                // Creamos las variables necesarias
                ProductoCarrito_Clase productoSeleccionado = event.getFirstSelectedItem().orElse(null);

                if (productoSeleccionado != null) {
                    // Limpiamos los filtros 
                    limpiarFiltrosButton.click();
                    // Filtramos los productos para mostrar solo el seleccionado
                    modelo.setValue(productoSeleccionado.getModelo());
                    
                    if (PRODUCTOS_FILTRADOS.size() == 1) {
                        // Si solo hay un producto filtrado, lo seleccionamos automáticamente
                        PRODUCTO_GRID.asSingleSelect().setValue(PRODUCTOS_FILTRADOS.get(0));
                        // Actualizamos la talla seleccionada para mostrarla como producto seleccionado
                        selectTalla.setValue(productoSeleccionado.getTalla());
                    }
                }
            });

            // --- Añadir componentes al layout del carrito ---
            carritoLayout.add(tituloCarrito, productoSelecMain, CARRITO_GRID, precioFinalLayout);

            // --- Layout principal horizontal ---
            HorizontalLayout gridsProductosCompras = new HorizontalLayout();
            gridsProductosCompras.setWidthFull();
            gridsProductosCompras.setSpacing(true);
            gridsProductosCompras.add(productLayout, carritoLayout);

            // --- Añadir todo al layout principal ---
            titulo.getStyle().set("text-align", "center");
            titulo.getStyle().set("font-size", "24");
            titulo.getStyle().set("margin", "0 auto");
            getContent().add(titulo, lineaDiv, gridsProductosCompras);

        } catch (Exception e) {
            Notification.show("Error al cargar la vista de compras: " + e.getMessage());
        }
    }


    /// F U N C I O N E S   P R I V A D A S /// 

    // --- Configuración del Grid de productos ---
    // Este método configura el grid de productos para mostrar la imagen y la información del producto.
    private void configurarGridDeProductos() throws Exception {
        
        try {
            PRODUCTO_GRID.setWidthFull();

            // Columna imagen
            PRODUCTO_GRID.addColumn(new ComponentRenderer<>(producto -> {

                // Crea una imagen del producto 
                Image imagen = new Image(producto.getImage(), "Producto");
                imagen.setMaxWidth("200px");    // Límite máximo de ancho
                imagen.setMaxHeight("200px");   // Límite máximo de alto
                imagen.getStyle().set("object-fit", "contain"); // Ajusta la imagen sin deformarla
                imagen.setWidth("150px");
                imagen.setHeight("150px");
                return imagen;

            })).setHeader("Imagen").setAutoWidth(true);

            // Columna información
            PRODUCTO_GRID.addColumn(new ComponentRenderer<>(producto -> {

                // Crea un layout vertical para mostrar la información del producto
                VerticalLayout infoLayout = new VerticalLayout();
                String[] labels = {
                    "Modelo: " + producto.getModel(),
                    "Marca: " + producto.getBrand(),
                    "Genero: " + producto.getGenero(),
                    "Tipo: " + producto.getType(),
                    "Color: " + producto.getColor()
                };

                // Añade cada etiqueta de información al layout
                for (String text : labels) {
                    infoLayout.add(new Span(text));
                }

                // Configura el layout de información
                infoLayout.setSpacing(false);
                infoLayout.setPadding(false);
                return infoLayout;

            })).setHeader("Información").setAutoWidth(true);

        } catch (Exception e) {
            Notification.show("Error al cargar los productos: " + e.getMessage());
        }
    }


    // --- Configuración del Grid del carrito ---
    // Este método configura el grid del carrito para mostrar los productos agregados al carrito.
    private void configurarCarritoGrid() throws Exception {
        try {
            // Configuración del grid del carrito
            CARRITO_GRID.setColumns();
            // Añadir columnas al grid del carrito
            CARRITO_GRID.addColumn(ProductoCarrito_Clase::getModelo).setHeader("Modelo");
            CARRITO_GRID.addColumn(ProductoCarrito_Clase::getTalla).setHeader("Talla");
            CARRITO_GRID.addColumn(ProductoCarrito_Clase::getUnidades).setHeader("Unidades");
            CARRITO_GRID.addColumn(ProductoCarrito_Clase::getPrecioTotal).setHeader("Precio");
            // Configuramos el tamaño del grid del carrito
            CARRITO_GRID.setWidth("557px");
            CARRITO_GRID.setHeight((CARRITO_PRODUCTOS.size() * 50) + "px");

        } catch (Exception e) {
            Notification.show("Error al configurar el grid del carrito: " + e.getMessage());
        }
    }


    // --- Método para agregar un producto al carrito ---
    // Este método agrega un producto al carrito y actualiza el precio total.
    // Retorna el precio total del producto agregado o -1 si no se puede agregar (por ejemplo, si se alcanza el límite de stock).
    // Retorno 0 si el producto ya esta totalmente agotado
    private double agregarAlCarrito(Producto_Clase producto) throws Exception {

        try {
            // Tomamos el precio del producto y buscamos si ya existe en el carrito
            double totalPrice = producto.getPrice();

            ProductoCarrito_Clase productoEnCarrito = CARRITO_PRODUCTOS.stream()
                .filter(p -> p.getModelo().equals(producto.getModel()) && p.getTalla().equals(producto.getSize()))
                .findFirst()
                .orElse(null);

            // Si el producto ya está en el carrito, actualizamos las unidades y el precio total
            if (productoEnCarrito != null) {
                // Verificamos si el producto tiene stock suficiente
                if (productoEnCarrito.getUnidades() >= producto.getStock()) {
                    return -1;
                }
                // Si hay stock, incrementamos las unidades y actualizamos el precio total
                productoEnCarrito.setUnidades(productoEnCarrito.getUnidades() + 1);
                productoEnCarrito.setPrecioTotal(productoEnCarrito.getUnidades() * producto.getPrice());
                // Actualizamos el totalPrice con el nuevo precio total del producto en el carrito
                totalPrice = productoEnCarrito.getPrecioTotal();

            // Si el producto no está en el carrito, lo agregamos como nuevo producto
            } else {

                // Si el producto directamente esta agotado retormamos -1
                if (producto.getStock() == 0) return 0;

                // Creamos un nuevo objeto ProductoCarrito y lo agregamos al carrito
                CARRITO_PRODUCTOS.add(new ProductoCarrito_Clase(
                    producto.getModel(),
                    producto.getSize(),
                    1,
                    producto.getPrice()
                ));
            }
            // Actualizamos el grid del carrito con los productos actuales
            CARRITO_GRID.setItems(CARRITO_PRODUCTOS);
            // Retornamos el precio total del producto agregado
            return totalPrice;

        }catch (Exception e) {
            Notification.show("Error al agregar el producto al carrito: " + e.getMessage());
            return -1;
        }
    }
    

    // --- Método para eliminar una unidad de un producto del carrito ---
    // Este método elimina una unidad de un producto del carrito y actualiza el precio total.
    // Retorna el precio total del producto eliminado o -1 si el producto no estaba en el carrito.
    private double eliminarDelCarrito(Producto_Clase producto) throws Exception {

        try {
            // Creamos una variable para almacenar el precio total de la eliminación
            double totalPriceEli = 0;

            // Buscamos el producto en el carrito de productos
            ProductoCarrito_Clase productoEnCarrito = CARRITO_PRODUCTOS.stream()
                .filter(p -> p.getModelo().equals(producto.getModel()) && p.getTalla().equals(producto.getSize()))
                .findFirst()
                .orElse(null);

            // Si el producto está en el carrito, actualizamos las unidades o lo eliminamos si es necesario
            if (productoEnCarrito != null) {
                // Si el producto tiene más de una unidad, decrementamos las unidades y actualizamos el precio total
                if (productoEnCarrito.getUnidades() > 1) {
                    // Decrementamos las unidades y actualizamos el precio total
                    productoEnCarrito.setUnidades(productoEnCarrito.getUnidades() - 1);
                    productoEnCarrito.setPrecioTotal(productoEnCarrito.getUnidades() * producto.getPrice());
                    totalPriceEli = productoEnCarrito.getPrecioTotal();

                // Si el producto tiene una sola unidad, lo eliminamos del carrito
                } else {
                    CARRITO_PRODUCTOS.remove(productoEnCarrito);
                }
                
            }else{
                // Si no encontramos el producto en el carrito, mostramos que no está agregado
                return -1;
            }
            // Actualizamos el grid del carrito con los productos actuales
            CARRITO_GRID.setItems(CARRITO_PRODUCTOS);
            // Retornamos el precio total del producto eliminado
            return totalPriceEli;

        } catch (Exception e) {
            Notification.show("Error al eliminar el producto del carrito: " + e.getMessage());
            return -1; 
        }
    }


    // --- Método para filtrar productos ---
    // Este método filtra los productos según los criterios seleccionados y actualiza el grid de productos.
    private void filtrarProductos(String modelo, String marca, String color, String genero, String tipo){
        try {
            // Limpiamos la lista de productos filtrados y aplicamos los filtros
            PRODUCTOS_FILTRADOS.clear();
            // Filtramos los productos según los criterios seleccionados
            PRODUCTOS_FILTRADOS.addAll(PRODUCTOS.stream()
                .filter(producto -> (modelo == null || modelo.isEmpty() || producto.getModel().toLowerCase().contains(modelo.toLowerCase())))
                .filter(producto -> (marca == null || marca.isEmpty() || producto.getBrand().equalsIgnoreCase(marca)))
                .filter(producto -> (color == null || color.isEmpty() || producto.getColor().equalsIgnoreCase(color)))
                .filter(producto -> (genero == null || genero.isEmpty() || producto.getGenero().equalsIgnoreCase(genero)))
                .filter(producto -> (tipo == null || tipo.isEmpty() || producto.getType().equalsIgnoreCase(tipo)))
                .collect(Collectors.toList()));
            // Actualizamos el grid de productos con los productos filtrados
            PRODUCTO_GRID.getDataProvider().refreshAll();

        } catch (Exception e) {
            Notification.show("Error al filtrar los productos: " + e.getMessage());
        }
    }


    // --- Método para llenar campos de filtro ---
    // Este método llena los campos de filtro con las opciones disponibles según el índice proporcionado.
    private void llenarCampos(int index, Select<String> clasificacion, String nombre) throws Exception {
        try {
            // Llenamos el campo de clasificación con las opciones disponibles
            List<String> classExits = GestionMenu_Back.leerIndexProducto(index);
            clasificacion.setItems(classExits);

            // Configuramos el placeholder y la etiqueta del campo de clasificación
            clasificacion.setPlaceholder("Seleccione");
            clasificacion.setLabel(nombre);

        } catch (Exception e) {
            Notification.show("Error al llenar los campos: " + e.getMessage());
        }
    }


    // --- Método para actualizar el precio total del carrito ---
    // Este método actualiza el label que muestra el precio total del carrito.
    private void actualizarPrecioTotal(Span totalLabel) throws Exception {
        try {
            // Calcula el precio total sumando los precios de todos los productos en el carrito
            double total = CARRITO_PRODUCTOS.stream().mapToDouble(ProductoCarrito_Clase::getPrecioTotal).sum();
            // Actualiza el label del precio total
            totalLabel.setText("Precio Total: " + total + " $");
        } catch (Exception e) {
            Notification.show("Error al actualizar el precio total: " + e.getMessage());
        }
    }

   
    public static Dialog crearDialogo(String mensaje, Runnable esConfirmado) { 
        
        Dialog dialogo = new Dialog();
        Div texto = new Div();
        Button botonConfirmar = new Button();
        Button botonCancelar = new Button("Cancelar", event -> dialogo.close());
        HorizontalLayout contenedorBotones = new HorizontalLayout(botonConfirmar, botonCancelar);
        VerticalLayout layout = new VerticalLayout(texto, contenedorBotones);
        

        botonConfirmar.setText("Confirmar");
        botonConfirmar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        texto.getStyle().set("white-space", "pre-line");
        texto.setText(mensaje);
        dialogo.setVisible(true);
        dialogo.setCloseOnEsc(false);
        dialogo.setCloseOnOutsideClick(false);
        dialogo.add(layout);

        
        botonConfirmar.addClickListener(event ->{
            esConfirmado.run();
            dialogo.close();
            
        });
        

        return dialogo;
    }


}
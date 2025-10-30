package com.example.application.Views;

import com.example.application.Services.CarritoDeComprasService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import com.vaadin.flow.router.BeforeLeaveEvent; // ! Se importa lo necesario para evitar que salga de la vista
import com.vaadin.flow.router.BeforeLeaveObserver;

@PageTitle("CebolliSport")
@Route("Compras")
@Menu(order = 2, icon = LineAwesomeIconUrl.SHOPPING_CART_SOLID)
public class CarritoDeComprasView extends Composite<VerticalLayout> implements BeforeLeaveObserver {
    // ! Se implementa el BeforeLeaveObserver.

    // ? Se implementa el dialogo y el evento BeforeLeave.
    @Override
    public void beforeLeave(BeforeLeaveEvent event) {

        if (generarFacturaBtn.isEnabled()) {

            event.postpone();

            Dialog dialogo = new Dialog();
            Div texto = new Div();
            Button botonConfirmar = new Button();
            Button botonCancelar = new Button();
            HorizontalLayout contenedorBotones = new HorizontalLayout(botonConfirmar, botonCancelar);
            VerticalLayout layout = new VerticalLayout(texto, contenedorBotones);

            botonConfirmar.setText("Confirmar");
            botonCancelar.setText("Cancelar");
            botonConfirmar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            texto.getStyle().set("white-space", "pre-line");
            texto.setText(
                    "¡Atención! Esta acción es irreversible.\nAl salir de esta pestaña se perderán los productos que haya registrado.\n¿Desea Continuar?");

            dialogo.setVisible(true);
            dialogo.setCloseOnEsc(false);
            dialogo.setCloseOnOutsideClick(false);
            dialogo.add(layout);
            botonConfirmar.addClickListener(e -> {

                dialogo.close();

                try (
                        FileWriter escritor = new FileWriter("Datos/Compras.txt", false)) {
                    escritor.write("MODELO | TALLA | CANTIDAD | PRECIO UNIDAD | TOTAL" + System.lineSeparator());
                    Notification.show("La Compra ha sido descartada.", 3000, Notification.Position.TOP_CENTER);

                } catch (IOException x) {
                    System.out.println("Error al limpiar el archivo Compras.txt: " + x.getMessage());
                }

                event.getContinueNavigationAction().proceed();
            });

            botonCancelar.addClickListener(e -> {
                dialogo.close();
                event.getContinueNavigationAction().cancel();

            });
            dialogo.open();

        } else {

            try (
                    FileWriter escritor = new FileWriter("Datos/Compras.txt", false)) {
                escritor.write("MODELO | TALLA | CANTIDAD | PRECIO UNIDAD | TOTAL" + System.lineSeparator());

            } catch (IOException x) {
                System.out.println("Error al limpiar el archivo Compras.txt: " + x.getMessage());
            }

        }

    }

    // * Se crea la clase que representa cada línea de la vista previa de la factura

    public class ProductoFactura {

        String descripcion;
        NumberField cantidadField;
        HorizontalLayout filaResumen;
        double precioUnitario;

        public ProductoFactura(String descripcion, NumberField cantidadField, HorizontalLayout filaResumen,
                double precioUnitario) {
            this.descripcion = descripcion;
            this.cantidadField = cantidadField;
            this.filaResumen = filaResumen;
            this.precioUnitario = precioUnitario;
        }

    }

    // * Se inicializa la lista de productos

    private final List<ProductoFactura> productos = new ArrayList<>();
    private Button confirmarPagoBtn = new Button();
    private Button generarFacturaBtn = new Button();

    // * Se crea la variable global para las notificaciones:
    Notification notificacionActual = new Notification();

    public CarritoDeComprasView() {

        // * Se inicializan las variables con las que se va a interactuar con la lógica
        // * de la interfaz

        CarritoDeComprasService carrito = new CarritoDeComprasService();
        int lineasCompra = carrito.contadorLineasCompra();
        String numeroActualFactura = carrito.numeroActualFactura();
        String[] modeloCantidad = { "" };
        String[] cedulaListener = { "" };
        String[] cedula = { "" };
        String[] fechaHoy = { carrito.retornaFechaActual() };
        String[] formaDePago = { "" };
        String[] descuento = { "" };
        double[] vlTotalPorElemento = { 0.0 };
        double[] subtotal = { 0.0 };
        double[] iva = { 0.0 };
        double[] total = { 0.0 };
        double[] monto = { 0.0 };
        int[] cantidadUnitaria = { 0 };
        boolean[] cedulaExiste = { false };
        List<String> descripcionesString = new ArrayList<>();
        List<String> cantidadesString = new ArrayList<>();
        List<String> totalesPorFila = new ArrayList<>();

        // * Se inicializan las variables de la interfaz gráfica y se construye dicha
        // * interfaz (todo generado por Vaadin)

        Hr hr = new Hr();
        Hr hr2 = new Hr();
        Hr hr3 = new Hr();
        Hr hr4 = new Hr();
        Hr hr5 = new Hr();
        Hr hr6 = new Hr();
        Hr hr7 = new Hr();
        Hr hr8 = new Hr();
        H4 tituloPrevisualizacion = new H4();
        H4 tituloInformacionDePago = new H4();
        H4 tituloResumenProductos = new H4();
        H4 tituloFactura = new H4();
        H5 textoDescripcion = new H5();
        H5 textoModelo = new H5();
        H5 textoCantidad = new H5();
        H5 textoNIT = new H5();
        H5 textoDireccion = new H5();
        H5 textoCorreoCorporativo = new H5();
        H5 textoNumeroActualFactura = new H5();
        H5 textoAgradecimiento = new H5();

        HorizontalLayout layoutRow = new HorizontalLayout();
        HorizontalLayout layoutRow2 = new HorizontalLayout();
        HorizontalLayout layoutRow3 = new HorizontalLayout();
        HorizontalLayout layoutRow4 = new HorizontalLayout();
        HorizontalLayout layoutRow5 = new HorizontalLayout();
        HorizontalLayout layoutRow6 = new HorizontalLayout();
        HorizontalLayout layoutRow7 = new HorizontalLayout();
        HorizontalLayout layoutRow8 = new HorizontalLayout();
        HorizontalLayout layoutRow9 = new HorizontalLayout();
        HorizontalLayout layoutRow10 = new HorizontalLayout();
        HorizontalLayout layoutRow11 = new HorizontalLayout();
        HorizontalLayout layoutRow12 = new HorizontalLayout();
        HorizontalLayout layoutRow13 = new HorizontalLayout();
        VerticalLayout layoutColumn2 = new VerticalLayout();
        VerticalLayout layoutColumn3 = new VerticalLayout();
        VerticalLayout layoutColumn4 = new VerticalLayout();
        VerticalLayout resumenProductosLayout = new VerticalLayout();

        ComboBox<String> metodoPagoDropdown = new ComboBox<>();
        TextField campoTextoCedula = new TextField();
        Paragraph textoCedula = new Paragraph();
        Paragraph textoFechaHoy = new Paragraph();
        Paragraph tituloDescripcion = new Paragraph();
        Paragraph tituloCantidad = new Paragraph();
        Paragraph tituloVlTotal = new Paragraph();
        Paragraph tituloSubtotal = new Paragraph();
        Paragraph textoSubtotal = new Paragraph();
        Paragraph tituloIVA = new Paragraph();
        Paragraph textoIVA = new Paragraph();
        Paragraph tituloTotal = new Paragraph();
        Paragraph textoTotal = new Paragraph();
        Paragraph tituloFormaDePago = new Paragraph();
        Paragraph tituloDescuento = new Paragraph();
        Paragraph tituloMonto = new Paragraph();
        Paragraph textoFormaDePago = new Paragraph();
        Paragraph textoDescuento = new Paragraph();
        Paragraph textoMonto = new Paragraph();

        getContent().setWidth("100%");
        getContent().setHeight("min-content");
        getContent().setJustifyContentMode(JustifyContentMode.CENTER);
        getContent().setAlignItems(Alignment.CENTER);
        tituloPrevisualizacion.setText("Previsualización || Factura Electrónica de la Venta");
        getContent().setAlignSelf(FlexComponent.Alignment.CENTER, tituloPrevisualizacion);
        tituloPrevisualizacion.setWidth("max-content");
        layoutRow.setWidthFull();
        getContent().setFlexGrow(1.0, layoutRow);
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        layoutRow.setAlignItems(Alignment.START);
        layoutRow.setJustifyContentMode(JustifyContentMode.START);
        layoutColumn2.setHeightFull();
        layoutRow.setFlexGrow(1.0, layoutColumn2);
        layoutColumn2.addClassName(Gap.SMALL);
        layoutColumn2.getStyle().set("flex-grow", "1");
        layoutColumn2.setHeight("min-content");
        layoutColumn2.setJustifyContentMode(JustifyContentMode.START);
        layoutColumn2.setAlignItems(Alignment.CENTER);
        layoutColumn3.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutColumn3);
        layoutColumn3.setPadding(false);
        layoutColumn3.setWidth("100%");
        layoutColumn3.setHeight("min-content");
        layoutColumn3.setJustifyContentMode(JustifyContentMode.START);
        layoutColumn3.setAlignItems(Alignment.START);
        tituloInformacionDePago.setText("Información de Pago");
        tituloInformacionDePago.setWidth("max-content");
        layoutRow2.setWidthFull();
        layoutColumn3.setFlexGrow(1.0, layoutRow2);
        layoutRow2.addClassName(Gap.SMALL);
        layoutRow2.setWidth("100%");
        layoutRow2.setHeight("min-content");
        layoutRow2.setAlignItems(Alignment.CENTER);
        layoutRow2.setJustifyContentMode(JustifyContentMode.START);
        layoutRow2.getStyle().set("margin-bottom", "20px");
        campoTextoCedula.setLabel("Cédula del Cliente");
        campoTextoCedula.setWidthFull();
        campoTextoCedula.setMaxWidth("200px");
        campoTextoCedula.setMinWidth("50px");
        campoTextoCedula.setHeight("min-content");
        campoTextoCedula.setValue("0");
        campoTextoCedula.setValueChangeMode(ValueChangeMode.EAGER);
        campoTextoCedula.setMaxLength(11);

        // * Se crea un listener que previene numeros negativos en el campo cédula,
        // * texto que no sea numerico,
        // * y coloca un 0 predeterminadamente en el campo para que se muestre el
        // * cliente no registrado por defecto.

        campoTextoCedula.addValueChangeListener(event -> {

            cedulaListener[0] = event.getValue();

            if (!cedulaListener[0].matches("\\d+")) {
                campoTextoCedula.setValue("0");

            }

            if (cedulaListener[0] == null || cedulaListener[0].trim().isEmpty()
                    || Long.parseLong(cedulaListener[0]) < 0) {
                campoTextoCedula.setValue("0");

            }

            if ((cedulaListener[0].startsWith("0")) && campoTextoCedula.getValue().length() > 1) {
                campoTextoCedula.setValue(cedulaListener[0].substring(1));

            }

        });

        // * Se crea un listener que hace que cuando se detecte un cambio al desenfocar
        // el campo cedula, solicite la generación de la factura nuevamente
        campoTextoCedula.addBlurListener(event -> {
            if (!productos.isEmpty()) {
                gestorDeNotificaciones("¡Se han detectado cambios! Por favor genere la factura nuevamente.", 3000,
                        Notification.Position.TOP_CENTER);
                confirmarPagoBtn.setEnabled(false);
            }
        });

        // * Se crea un listener que hace que cuando se detecte un cambio en el campo
        // método de pago, solicite la generación de la factura nuevamente
        metodoPagoDropdown.addValueChangeListener(event -> {
            if (!productos.isEmpty()) {
                gestorDeNotificaciones("¡Se han detectado cambios! Por favor genere la factura nuevamente.", 3000,
                        Notification.Position.TOP_CENTER);
                confirmarPagoBtn.setEnabled(false);
            }

        });

        layoutRow2.setAlignSelf(FlexComponent.Alignment.END, campoTextoCedula);

        metodoPagoDropdown.setLabel("Método de Pago");
        metodoPagoDropdown.setWidthFull();
        metodoPagoDropdown.setMaxWidth("200px");
        metodoPagoDropdown.setMinWidth("50px");
        metodoPagoDropdown.setHeight("min-content");
        metodoPagoDropdown.setAllowCustomValue(false);
        metodoPagoDropdown.getElement().executeJs("this.inputElement.setAttribute('readonly', 'readonly');");
        metodoPagoDropdown.getElement().executeJs("this.inputElement.style.cursor = 'pointer'");
        layoutRow2.setAlignSelf(FlexComponent.Alignment.END, metodoPagoDropdown);
        setMetodoPagoDropdownSampleData(metodoPagoDropdown);

        generarFacturaBtn.setText("Generar factura");
        generarFacturaBtn.setWidth("min-content");

        hr2.setHeight("2px");

        tituloResumenProductos.setText("Resumen Productos");
        layoutColumn2.setAlignSelf(FlexComponent.Alignment.START, tituloResumenProductos);
        tituloResumenProductos.setWidth("max-content");
        tituloResumenProductos.setHeight("40px");
        generarFacturaBtn.setHeight("35px");
        layoutRow2.setAlignSelf(FlexComponent.Alignment.END, generarFacturaBtn);
        generarFacturaBtn.getStyle().set("cursor", "pointer");

        confirmarPagoBtn.setText("Confirmar Pago");
        confirmarPagoBtn.setWidth("min-content");
        confirmarPagoBtn.setHeight("35px");
        layoutRow2.setAlignSelf(FlexComponent.Alignment.END, confirmarPagoBtn);
        confirmarPagoBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        confirmarPagoBtn.getStyle().set("cursor", "pointer");
        confirmarPagoBtn.setEnabled(false);

        layoutRow3.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutRow3);
        layoutRow3.addClassName(Gap.MEDIUM);
        layoutRow3.setWidth("100%");
        layoutRow3.setHeight("min-content");
        layoutRow3.setAlignItems(Alignment.CENTER);
        layoutRow3.setJustifyContentMode(JustifyContentMode.START);

        textoDescripcion.setText("Descripción");
        textoDescripcion.setWidth("68%");
        layoutRow3.setAlignSelf(FlexComponent.Alignment.CENTER, textoDescripcion);

        textoModelo.setText("Modelo");
        textoModelo.setWidth("15%");
        layoutRow3.setAlignSelf(FlexComponent.Alignment.CENTER, textoModelo);

        textoCantidad.setText("Cantidad");
        layoutRow3.setAlignSelf(FlexComponent.Alignment.CENTER, textoCantidad);
        textoCantidad.getStyle().set("flex-grow", "1");

        hr3.setWidth("100%");
        hr3.setHeight("5px");

        layoutRow4.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutRow4);
        layoutRow4.addClassName(Gap.MEDIUM);
        layoutRow4.setWidth("50%");
        layoutRow4.setHeight("min-content");
        layoutRow4.setAlignItems(Alignment.CENTER);
        layoutRow4.setJustifyContentMode(JustifyContentMode.CENTER);

        layoutRow5.setHeightFull();
        layoutRow4.setFlexGrow(1.0, layoutRow5);
        layoutRow5.addClassName(Gap.MEDIUM);
        layoutRow5.setWidth("100%");
        layoutRow5.getStyle().set("flex-grow", "1");

        layoutColumn4.setAlignSelf(FlexComponent.Alignment.CENTER, textoAgradecimiento);
        textoAgradecimiento.setWidth("max-content");
        getContent().add(tituloPrevisualizacion);
        getContent().add(hr);
        getContent().add(layoutRow);
        layoutRow.add(layoutColumn2);
        layoutColumn2.add(layoutColumn3);
        layoutColumn3.add(tituloInformacionDePago);
        layoutColumn3.add(layoutRow2);
        layoutRow2.add(campoTextoCedula);
        layoutRow2.add(metodoPagoDropdown);
        layoutRow2.add(generarFacturaBtn);
        layoutRow2.add(confirmarPagoBtn);
        layoutColumn2.add(hr2);
        layoutColumn2.add(tituloResumenProductos);
        layoutColumn2.add(layoutRow3);
        layoutRow3.add(textoDescripcion);
        layoutRow3.add(textoModelo);
        layoutRow3.add(textoCantidad);
        layoutColumn2.add(hr3);
        layoutColumn2.add(resumenProductosLayout);

        // * Se crea un ciclo que trae la información del archivo de compra actual para
        // * ser mostrado y editado en el carrito

        for (int i = 0; i < lineasCompra; i++) {

            modeloCantidad = carrito.retornaModeloTallaCantidadVlunitario(i);

            agregarFilaProductoOQuitarFactura(resumenProductosLayout, modeloCantidad, layoutColumn4);

        }

        // * Se activa el botón generarFactura solo cuando hayan productos en el listado
        // de Resumen Productos
        generarFacturaBtn.setEnabled(!productos.isEmpty());

        layoutRow.add(layoutColumn4);
        layoutColumn4.setVisible(false);

        // * Se crea un listener para el botón de generar factura que usa todos los
        // * valores modificados de las variables usadas en la vista

        generarFacturaBtn.addClickListener(event -> {

            HorizontalLayout filaFactura;
            Paragraph descripcion;
            Paragraph cantidad;
            Paragraph valorTotal;

            // * Activamos el botón de confirmar pago
            confirmarPagoBtn.setEnabled(true);

            cedula[0] = campoTextoCedula.getValue();
            subtotal[0] = 0;

            // * Limpiamos estas listas para que no haya inconsistencias por si no es la
            // primera vez que se genera la factura
            descripcionesString.clear();
            cantidadesString.clear();
            totalesPorFila.clear();

            // * Reiniciamos el layout que contiene la vista previa de la factura
            layoutColumn4.removeAll();
            layoutColumn4.setVisible(true);

            // * Si la cedula no existe, se establece por defecto la cédula 0 (cliente no
            // registrado)
            cedulaExiste[0] = carrito.cedulaEstaRegistrada(cedula[0]);

            if (!cedulaExiste[0]) {
                campoTextoCedula.setValue("0");
                cedula[0] = "0";
            }

            // * Si la cedula existe (comprobado en el condicional anterior), se establece
            // el metodo de pago junto con el descuento,
            // * si no, solo el metodo de pago
            if (!cedula[0].equalsIgnoreCase("0")) {
                switch (metodoPagoDropdown.getValue()) {
                    case "credito":
                        formaDePago[0] = "Crédito";
                        descuento[0] = "3";
                        break;

                    case "debito":
                        formaDePago[0] = "Débito";
                        descuento[0] = "3";
                        break;

                    case "transferencia":
                        formaDePago[0] = "Transferencia";
                        descuento[0] = "3";
                        break;

                    case "efectivo":
                        formaDePago[0] = "Efectivo";
                        descuento[0] = "5";
                        break;

                    default:
                        formaDePago[0] = "";
                        descuento[0] = "0";
                        break;
                }

            } else {
                switch (metodoPagoDropdown.getValue()) {
                    case "credito":
                        formaDePago[0] = "Crédito";
                        break;

                    case "debito":
                        formaDePago[0] = "Débito";
                        break;

                    case "transferencia":
                        formaDePago[0] = "Transferencia";
                        break;

                    case "efectivo":
                        formaDePago[0] = "Efectivo";
                        break;

                    default:
                        formaDePago[0] = "";
                        break;
                }

                descuento[0] = "0";
            }

            monto[0] = total[0] - (total[0] * (Double.parseDouble(descuento[0]) * 0.01));

            layoutColumn4.setHeightFull();
            layoutRow.setFlexGrow(1.0, layoutColumn4);
            layoutColumn4.addClassName(Gap.XSMALL);
            layoutColumn4.addClassName(Padding.LARGE);
            layoutColumn4.setWidth("406px");
            layoutColumn4.setHeight("100%");
            layoutColumn4.setJustifyContentMode(JustifyContentMode.START);
            layoutColumn4.setAlignItems(Alignment.CENTER);
            tituloFactura.setText("CebolliSport®");
            layoutColumn4.setAlignSelf(FlexComponent.Alignment.CENTER, tituloFactura);
            tituloFactura.setWidth("117px");
            textoNIT.setText("NIT: 000.000.000-0");
            layoutColumn4.setAlignSelf(FlexComponent.Alignment.CENTER, textoNIT);
            textoNIT.setWidth("137px");
            textoDireccion.setText("Cl. 1000 # 2000 - 3000 Medellín");
            layoutColumn4.setAlignSelf(FlexComponent.Alignment.CENTER, textoDireccion);
            textoDireccion.setWidth("228px");
            textoCorreoCorporativo.setText("contabilidad.cs@cebollis.com");
            layoutColumn4.setAlignSelf(FlexComponent.Alignment.CENTER, textoCorreoCorporativo);
            textoCorreoCorporativo.setWidth("214px");
            hr4.setWidth("100%");
            hr4.setHeight("2px");
            textoNumeroActualFactura.setText("No. " + String.format("%010d", Long.parseLong(numeroActualFactura)));
            layoutColumn4.setAlignSelf(FlexComponent.Alignment.CENTER, textoNumeroActualFactura);
            textoNumeroActualFactura.setWidth("max-content");
            layoutRow6.setWidthFull();
            layoutColumn4.setFlexGrow(1.0, layoutRow6);
            layoutRow6.addClassName(Gap.MEDIUM);
            layoutRow6.setWidth("358px");
            layoutRow6.setHeight("min-content");
            layoutRow6.setAlignItems(Alignment.CENTER);
            layoutRow6.setJustifyContentMode(JustifyContentMode.CENTER);
            textoCedula.setText("CC: " + cedula[0]);
            layoutRow6.setAlignSelf(FlexComponent.Alignment.CENTER, textoCedula);
            textoCedula.setWidth("153px");
            textoCedula.getStyle().set("font-size", "var(--lumo-font-size-xs)");
            textoFechaHoy.setText("Fecha: " + fechaHoy[0]);
            layoutRow6.setAlignSelf(FlexComponent.Alignment.CENTER, textoFechaHoy);
            textoFechaHoy.setWidth("189px");
            textoFechaHoy.getStyle().set("font-size", "var(--lumo-font-size-xs)");
            hr5.setWidth("100%");
            hr5.setHeight("2px");
            layoutRow7.setWidthFull();
            layoutColumn4.setFlexGrow(1.0, layoutRow7);
            layoutRow7.addClassName(Gap.MEDIUM);
            layoutRow7.setWidth("358px");
            layoutRow7.setHeight("min-content");
            layoutRow7.setAlignItems(Alignment.CENTER);
            layoutRow7.setJustifyContentMode(JustifyContentMode.CENTER);
            tituloDescripcion.setText("Descripción");
            layoutRow7.setAlignSelf(FlexComponent.Alignment.CENTER, tituloDescripcion);
            tituloDescripcion.setWidth("210px");
            tituloDescripcion.getStyle().set("font-size", "var(--lumo-font-size-xs)");
            tituloCantidad.setText("Cant.");
            layoutRow7.setAlignSelf(FlexComponent.Alignment.CENTER, tituloCantidad);
            tituloCantidad.setWidth("50px");
            tituloCantidad.getStyle().set("font-size", "var(--lumo-font-size-xs)");
            tituloVlTotal.setText("V. Total");
            layoutRow7.setAlignSelf(FlexComponent.Alignment.CENTER, tituloVlTotal);

            tituloVlTotal.setWidth("66px");
            tituloVlTotal.getStyle().set("font-size", "var(--lumo-font-size-xs)");
            layoutRow8.setWidthFull();
            layoutColumn4.setFlexGrow(1.0, layoutRow8);
            layoutRow8.addClassName(Gap.MEDIUM);
            layoutRow8.setWidth("358px");
            layoutRow8.setHeight("min-content");
            layoutRow8.setAlignItems(Alignment.CENTER);
            layoutRow8.setJustifyContentMode(JustifyContentMode.CENTER);

            hr6.setWidth("100%");
            hr6.setHeight("2px");
            layoutRow9.setWidthFull();
            layoutColumn4.setFlexGrow(1.0, layoutRow9);
            layoutRow9.addClassName(Gap.MEDIUM);
            layoutRow9.setWidth("358px");
            layoutRow9.setHeight("min-content");
            layoutRow9.setAlignItems(Alignment.CENTER);

            layoutColumn4.add(tituloFactura);
            layoutColumn4.add(textoNIT);
            layoutColumn4.add(textoDireccion);
            layoutColumn4.add(textoCorreoCorporativo);
            layoutColumn4.add(hr4);
            layoutColumn4.add(textoNumeroActualFactura);
            layoutColumn4.add(layoutRow6);
            layoutRow6.add(textoCedula);
            layoutRow6.add(textoFechaHoy);
            layoutColumn4.add(hr5);
            layoutColumn4.add(layoutRow7);
            layoutRow7.add(tituloDescripcion);
            layoutRow7.add(tituloCantidad);
            layoutRow7.add(tituloVlTotal);

            // * Este ciclo for each genera cada fila de cada producto en la factura según
            // lo que estaba en la última generación de factura

            for (ProductoFactura p : productos) {
                filaFactura = new HorizontalLayout();

                descripcion = new Paragraph(p.descripcion);
                descripcion.setWidth("230px");
                descripcion.getStyle().set("font-size", "var(--lumo-font-size-xs)");

                cantidadUnitaria[0] = p.cantidadField.getValue().intValue();
                cantidad = new Paragraph(String.valueOf(cantidadUnitaria[0]));
                cantidad.setWidth("50px");
                cantidad.getStyle().set("font-size", "var(--lumo-font-size-xs)");

                vlTotalPorElemento[0] = cantidadUnitaria[0] * p.precioUnitario;
                subtotal[0] += vlTotalPorElemento[0];

                valorTotal = new Paragraph("$" + String.format("%,.2f", vlTotalPorElemento[0]));
                valorTotal.setWidth("59px");
                valorTotal.getStyle().set("font-size", "var(--lumo-font-size-xs)");

                filaFactura.setWidth("358px");
                filaFactura.setHeight("min-content");
                filaFactura.setAlignItems(Alignment.CENTER);
                filaFactura.setJustifyContentMode(JustifyContentMode.CENTER);
                filaFactura.addClassName(Gap.MEDIUM);
                filaFactura.add(descripcion, cantidad, valorTotal);

                layoutColumn4.add(filaFactura);

                descripcionesString.add(p.descripcion);
                cantidadesString.add(String.valueOf(cantidadUnitaria[0]));
                totalesPorFila.add(String.valueOf(vlTotalPorElemento[0]));

            }

            iva[0] = subtotal[0] * 0.19;
            total[0] = iva[0] + subtotal[0];
            monto[0] = total[0] - (total[0] * (Double.parseDouble(descuento[0]) * 0.01));

            layoutRow9.setJustifyContentMode(JustifyContentMode.CENTER);
            tituloSubtotal.setText("Subtotal:");
            layoutRow9.setAlignSelf(FlexComponent.Alignment.CENTER, tituloSubtotal);
            tituloSubtotal.setWidth("210px");
            tituloSubtotal.getStyle().set("font-size", "var(--lumo-font-size-xs)");
            textoSubtotal.setText("$" + String.format("%,.2f", subtotal[0]));
            layoutRow9.setAlignSelf(FlexComponent.Alignment.CENTER, textoSubtotal);
            textoSubtotal.setWidth("136px");
            textoSubtotal.getStyle().set("font-size", "var(--lumo-font-size-xs)");
            layoutRow10.setWidthFull();
            layoutColumn4.setFlexGrow(1.0, layoutRow10);
            layoutRow10.addClassName(Gap.MEDIUM);
            layoutRow10.setWidth("358px");
            layoutRow10.setHeight("min-content");
            layoutRow10.setAlignItems(Alignment.CENTER);
            layoutRow10.setJustifyContentMode(JustifyContentMode.CENTER);
            tituloIVA.setText("IVA aplicado (19%):");
            layoutRow10.setAlignSelf(FlexComponent.Alignment.CENTER, tituloIVA);
            tituloIVA.setWidth("210px");
            tituloIVA.getStyle().set("font-size", "var(--lumo-font-size-xs)");
            textoIVA.setText("$" + String.format("%,.2f", iva[0]));
            layoutRow10.setAlignSelf(FlexComponent.Alignment.CENTER, textoIVA);
            textoIVA.setWidth("136px");
            textoIVA.getStyle().set("font-size", "var(--lumo-font-size-xs)");
            layoutRow11.setWidthFull();
            layoutColumn4.setFlexGrow(1.0, layoutRow11);
            layoutRow11.addClassName(Gap.MEDIUM);
            layoutRow11.setWidth("358px");
            layoutRow11.setHeight("min-content");
            layoutRow11.setAlignItems(Alignment.CENTER);
            layoutRow11.setJustifyContentMode(JustifyContentMode.CENTER);
            tituloTotal.setText("Total:");
            layoutRow11.setAlignSelf(FlexComponent.Alignment.CENTER, tituloTotal);
            tituloTotal.setWidth("210px");
            tituloTotal.getStyle().set("font-size", "var(--lumo-font-size-xs)");
            textoTotal.setText("$" + String.format("%,.2f", total[0]));
            layoutRow11.setAlignSelf(FlexComponent.Alignment.CENTER, textoTotal);
            textoTotal.setWidth("136px");
            textoTotal.getStyle().set("font-size", "var(--lumo-font-size-xs)");
            hr7.setWidth("100%");
            hr7.setHeight("2px");
            layoutRow12.setWidthFull();
            layoutColumn4.setFlexGrow(1.0, layoutRow12);
            layoutRow12.addClassName(Gap.MEDIUM);
            layoutRow12.setWidth("358px");
            layoutRow12.setHeight("min-content");
            layoutRow12.setAlignItems(Alignment.CENTER);
            layoutRow12.setJustifyContentMode(JustifyContentMode.CENTER);
            tituloFormaDePago.setText("Forma de Pago");
            layoutRow12.setAlignSelf(FlexComponent.Alignment.CENTER, tituloFormaDePago);
            tituloFormaDePago.setWidth("140px");
            tituloFormaDePago.getStyle().set("font-size", "var(--lumo-font-size-xs)");
            tituloDescuento.setText("Desc.");
            layoutRow12.setAlignSelf(FlexComponent.Alignment.CENTER, tituloDescuento);
            tituloDescuento.setWidth("50px");
            tituloDescuento.getStyle().set("font-size", "var(--lumo-font-size-xs)");
            tituloMonto.setText("Monto");
            layoutRow12.setAlignSelf(FlexComponent.Alignment.CENTER, tituloMonto);
            tituloMonto.setWidth("136px");
            tituloMonto.getStyle().set("font-size", "var(--lumo-font-size-xs)");
            layoutRow13.setWidthFull();
            layoutColumn4.setFlexGrow(1.0, layoutRow13);
            layoutRow13.addClassName(Gap.MEDIUM);
            layoutRow13.setWidth("358px");
            layoutRow13.setHeight("min-content");
            layoutRow13.setAlignItems(Alignment.CENTER);
            layoutRow13.setJustifyContentMode(JustifyContentMode.CENTER);
            textoFormaDePago.setText(formaDePago[0]);
            layoutRow13.setAlignSelf(FlexComponent.Alignment.CENTER, textoFormaDePago);
            textoFormaDePago.setWidth("154px");
            textoFormaDePago.getStyle().set("font-size", "var(--lumo-font-size-xs)");
            textoDescuento.setText(descuento[0] + "%");
            layoutRow13.setAlignSelf(FlexComponent.Alignment.CENTER, textoDescuento);
            textoDescuento.setWidth("36px");
            textoDescuento.getStyle().set("font-size", "var(--lumo-font-size-xs)");
            textoMonto.setText("$" + String.format("%,.2f", monto[0]));
            layoutRow13.setAlignSelf(FlexComponent.Alignment.CENTER, textoMonto);
            textoMonto.setWidth("136px");
            textoMonto.getStyle().set("font-size", "var(--lumo-font-size-xs)");
            hr8.setWidth("100%");
            hr8.setHeight("2px");
            textoAgradecimiento.setText("¡Gracias por confiar en nosotros :D!");

            layoutColumn4.add(layoutRow8);
            layoutColumn4.add(hr6);
            layoutColumn4.add(layoutRow9);
            layoutRow9.add(tituloSubtotal);
            layoutRow9.add(textoSubtotal);
            layoutColumn4.add(layoutRow10);
            layoutRow10.add(tituloIVA);
            layoutRow10.add(textoIVA);
            layoutColumn4.add(layoutRow11);
            layoutRow11.add(tituloTotal);
            layoutRow11.add(textoTotal);
            layoutColumn4.add(hr7);
            layoutColumn4.add(layoutRow12);
            layoutRow12.add(tituloFormaDePago);
            layoutRow12.add(tituloDescuento);
            layoutRow12.add(tituloMonto);
            layoutColumn4.add(layoutRow13);
            layoutRow13.add(textoFormaDePago);
            layoutRow13.add(textoDescuento);
            layoutRow13.add(textoMonto);
            layoutColumn4.add(hr8);
            layoutColumn4.add(textoAgradecimiento);

        });

        // * Este listener genera una nueva linea en el archivo Facturas.txt para
        // * almacenar la información de esta compra nueva como factura

        confirmarPagoBtn.addClickListener(event -> {

            try {

                File archivoCompras = new File("Datos/Compras.txt");
                File archivoFacturas = new File("Datos/Facturas.txt");
                File archivoProductosTemp = new File("Datos/Productos_temp.txt");
                File archivoProductosViejo = new File("Datos/Productos.txt");
                String lineaFactura = "";

                String linea;
                String[] campos = { "" };
                String modeloArchivo = "";
                String tallaArchivo = "";
                String modeloFactura = "";
                String tallaFactura = "";
                String[] partesDescripcion = { "" };
                int stockActual = 0;
                int cantidadComprada = 0;
                int nuevoStock = 0;

                FileWriter writerFacturas = null;
                FileReader lectorProductos = null;
                BufferedReader lectorIntProductos = null;
                FileWriter escritorIntProductos = null;
                BufferedReader lectorCompras = null;
                FileWriter escritorCompras = null;

                // * Generamos el texto de las variables que se mostrarán en la factura
                lineaFactura = String.format("%010d", Integer.parseInt(numeroActualFactura)) + "|" +
                        cedula[0] + "|" +
                        fechaHoy[0] + "|" +
                        String.join("_", descripcionesString) + "|" +
                        String.join("_", cantidadesString) + "|" +
                        String.join("_", totalesPorFila) + "|" +
                        String.format("%,.2f", subtotal[0]) + "|" +
                        String.format("%,.2f", iva[0]) + "|" +
                        String.format("%,.2f", total[0]) + "|" +
                        formaDePago[0] + "|" +
                        descuento[0] + "|" +
                        String.format("%,.2f", monto[0]);

                // * Este try valida que la modificación del archivo Facturas.txt funcione
                try {
                    writerFacturas = new FileWriter(archivoFacturas, true);
                    writerFacturas.write(System.lineSeparator() + lineaFactura);
                } catch (IOException e) {
                    System.out.println("Error en la modificación del archivo Facturas.txt: " + e.getMessage());
                } finally {
                    if (writerFacturas != null)
                        writerFacturas.close();
                }

                // * Este try valida que la resta del stock de los productos se realice
                try {
                    lectorProductos = new FileReader("Datos/Productos.txt");
                    lectorIntProductos = new BufferedReader(lectorProductos);
                    escritorIntProductos = new FileWriter(archivoProductosTemp);

                    lectorIntProductos.readLine();

                    escritorIntProductos
                            .write("URL | MODELO | MARCA | TALLA | COLOR | TIPO | CANTIDAD | GENERO | PRECIO UNIDAD |"
                                    + System.lineSeparator());

                    while ((linea = lectorIntProductos.readLine()) != null) {

                        if (linea.trim().isEmpty())
                            continue;

                        campos = linea.split("\\|");
                        modeloArchivo = campos[1].trim();
                        tallaArchivo = campos[3].trim();

                        for (ProductoFactura producto : productos) {

                            partesDescripcion = producto.descripcion.split("-");
                            modeloFactura = partesDescripcion[0].trim();
                            tallaFactura = partesDescripcion[1].trim();

                            if (modeloArchivo.equalsIgnoreCase(modeloFactura)
                                    && tallaArchivo.equalsIgnoreCase(tallaFactura)) {

                                stockActual = Integer.parseInt(campos[6].trim());
                                cantidadComprada = producto.cantidadField.getValue().intValue();
                                nuevoStock = Math.max(0, stockActual - cantidadComprada);
                                campos[6] = String.valueOf(nuevoStock);
                                break;

                            }
                        }

                        escritorIntProductos.write(String.join("|", campos) + System.lineSeparator());
                    }

                    lectorIntProductos.close();
                    escritorIntProductos.close();

                    archivoProductosViejo.delete();
                    archivoProductosTemp.renameTo(archivoProductosViejo);

                } catch (IOException e) {
                    System.out.println("Error actualizando el stock en Productos.txt: " + e.getMessage());
                } finally {
                    if (lectorIntProductos != null)
                        lectorIntProductos.close();
                    if (escritorIntProductos != null)
                        escritorIntProductos.close();
                }

                // * Este try valida que se borre el contenido del archivo de compras
                // correctamente
                try {

                    lectorCompras = new BufferedReader(new FileReader(archivoCompras));
                    escritorCompras = new FileWriter(archivoCompras, false);

                    escritorCompras.write("MODELO | TALLA | CANTIDAD | PRECIO UNIDAD | TOTAL" + System.lineSeparator());

                    confirmarPagoBtn.setEnabled(false);
                    generarFacturaBtn.setEnabled(false);
                    resumenProductosLayout.removeAll();
                    productos.clear();
                    Notification.show("Compra realizada con éxito", 3000, Notification.Position.TOP_CENTER);

                } catch (IOException e) {
                    System.out.println("Error al limpiar el archivo Compras.txt: " + e.getMessage());
                } finally {
                    if (lectorCompras != null)
                        lectorCompras.close();
                    if (escritorCompras != null)
                        escritorCompras.close();
                }

            } catch (Exception e) {
                System.out.println("Error en la interacción de un archivo: " + e.getMessage());
            }
        });

    }

    // * Esta función agrega una fila nueva a la tabla de "Resumen Productos"
    public void agregarFilaProductoOQuitarFactura(VerticalLayout contenedor, String[] compraActual,
            VerticalLayout layoutColumn4) {

        CarritoDeComprasService carrito = new CarritoDeComprasService();
        String[] descripcionModelo = { "", "" };
        descripcionModelo = carrito.retornaDescripcion(compraActual[0], compraActual[1]);
        HorizontalLayout fila = new HorizontalLayout();
        Paragraph descripcion = new Paragraph(descripcionModelo[0]);
        Paragraph referencia = new Paragraph(compraActual[0]);
        NumberField cantidad = new NumberField();
        Button remover = new Button("Remover");
        double stock = carrito.retornaStockProducto(compraActual[0], compraActual[1]);
        ProductoFactura[] producto = { null };

        fila.setWidthFull();
        fila.setAlignItems(Alignment.CENTER);
        fila.addClassName(Gap.MEDIUM);

        descripcion.getStyle().set("flex-grow", "1");
        descripcion.getStyle().set("font-size", "var(--lumo-font-size-m)");
        descripcion.setWidth("68%");

        referencia.getStyle().set("flex-grow", "1");
        referencia.setWidth("15%");

        cantidad.setPlaceholder("Cantidad");
        cantidad.setValue(Double.parseDouble(compraActual[2]));
        cantidad.setWidth("100px");

        cantidad.setMin(1);
        cantidad.setStep(1);

        cantidad.addValueChangeListener(e -> {

            Double val = e.getValue();

            if (val == null || val < 1) {
                cantidad.setValue(1.0);
                Notification.show(
                        "La cantidad no puede ser negativa ni 0, si desea remover el producto haga click en \"Remover\"",
                        3000, Notification.Position.TOP_CENTER);
            } else if (stock < val) {
                cantidad.setValue(stock);
                Notification.show(
                        "La cantidad elegida supera el stock disponible, se colocó el stock total automáticamente",
                        3000, Notification.Position.TOP_CENTER);
            } else {
                cantidad.setValue(Math.floor(val));

                gestorDeNotificaciones("¡Se han detectado cambios! Por favor genere la factura nuevamente.", 3000,
                        Notification.Position.TOP_CENTER);
                confirmarPagoBtn.setEnabled(false);
            }

        });

        producto[0] = new ProductoFactura(descripcionModelo[1], cantidad, fila, Double.parseDouble(compraActual[3]));
        productos.add(producto[0]);

        remover.setWidth("min-content");
        remover.getStyle().set("cursor", "pointer");

        remover.addClickListener(e -> {
            contenedor.remove(fila);
            productos.remove(producto[0]);

            if (!productos.isEmpty()) {

                generarFacturaBtn.setEnabled(true);
                gestorDeNotificaciones("¡Se han detectado cambios! Por favor genere la factura nuevamente.", 3000,
                        Notification.Position.TOP_CENTER);
                confirmarPagoBtn.setEnabled(false);

            } else {
                generarFacturaBtn.setEnabled(false);
                confirmarPagoBtn.setEnabled(false);
                layoutColumn4.removeAll();
                layoutColumn4.setVisible(false);
                gestorDeNotificaciones(
                        "Para generar una factura, debe haber al menos 1 producto.\n Por favor regrese a \"Menú de Compras\"",
                        5000,
                        Notification.Position.TOP_CENTER);
            }

        });

        fila.add(descripcion, referencia, cantidad, remover);
        contenedor.add(fila);
    }

    // ! FUNCION AGREGADA PARA EVITAR LA ACUMULACION DE NOTIFICACIONES, Y GENERARLAS
    // CONTROLADAMENTE
    private void gestorDeNotificaciones(String message, int duration, Notification.Position position) {
        if (notificacionActual != null && notificacionActual.isOpened()) {
            notificacionActual.close();
        }
        notificacionActual = Notification.show(message, duration, position);
    }

    record SampleItem(String value, String label, Boolean disabled) {
    }

    private void setMetodoPagoDropdownSampleData(ComboBox<String> metodoPagoDropdown) {
        List<SampleItem> sampleItems = new ArrayList<>();
        List<String> values = new ArrayList<>();

        sampleItems.add(new SampleItem("credito", "Crédito", null));
        sampleItems.add(new SampleItem("debito", "Débito", null));
        sampleItems.add(new SampleItem("efectivo", "Efectivo", null));
        sampleItems.add(new SampleItem("transferencia", "Transferencia", null));

        for (SampleItem item : sampleItems) {
            values.add(item.value());
        }
        metodoPagoDropdown.setItems(values);
        metodoPagoDropdown.setItemLabelGenerator(value -> {
            for (SampleItem item : sampleItems) {
                if (item.value().equals(value)) {
                    return item.label();
                }
            }
            return value;
        });

        metodoPagoDropdown.setValue("efectivo");

    }

}
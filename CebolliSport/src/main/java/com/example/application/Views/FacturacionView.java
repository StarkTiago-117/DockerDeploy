package com.example.application.Views;


// ? Se realizan las importaciones de todos los elementos necesarios para la vista.
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vaadin.flow.component.grid.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.vaadin.lineawesome.LineAwesomeIconUrl;






@PageTitle("CebolliSport") // ? Se controla el título de la pestaña.
@Route("Facturas") // ? Se controla la ruta de la vista.
@Menu(order = 3, icon = LineAwesomeIconUrl.MONEY_BILL_WAVE_SOLID) // ? Se controla el icono de la vista.
public class FacturacionView extends Composite<VerticalLayout> { // ? Se controla la vista de la pestaña.



    // ? Se crean las variables globales necesarias para la vista.
    DatosFactura factura = new DatosFactura("", "", "", "", "");

    List<DatosFactura> vistaBase = new ArrayList<>();
    List<DatosFactura> vistaDinamica = new ArrayList<>();
    
    String esUsuarioDefault = ""; // ? Variable para determinar si el cliente tiene cédula por defecto (0).
    String esRegistrado = ""; // ? Variable para determinar si el cliente está registrado o no.
    
    

    // ? Se crean los componentes de la vista.
    Hr lineaDivisoraSuperior = new Hr();
    Hr lineaDivisoraGrid = new Hr();
    Hr lineaDivisora1Fact = new Hr();
    Hr lineaDivisora2Fact = new Hr();
    Hr lineaDivisora3Fact = new Hr();
    Hr lineaDivisora4Fact = new Hr();
    Hr lineaDivisora5Fact = new Hr();
    
    H4 headerPrincipal = new H4();
    H4 headerBusqueda = new H4();
    H4 headerSeccionGrid = new H4();
    H4 tituloFactura = new H4();
    
    H5 nitCebolliSport = new H5();
    H5 direccionTienda = new H5();
    H5 correoCorporativo = new H5();
    H5 numeroFactura = new H5();
    H5 mensajeFinal = new H5();
    
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

    VerticalLayout layoutColumn2 = new VerticalLayout();
    VerticalLayout layoutColumn3 = new VerticalLayout();
    VerticalLayout layoutColumn4 = new VerticalLayout();

    TextField numFacturaField = new TextField();
    TextField cedulaField = new TextField();
    
    DatePicker datePicker = new DatePicker();
    
    Select<String> metodoDePago = new Select<>();
    
    Button aplicarFiltros = new Button();
    Button botonResetear = new Button();
    
    Checkbox checkNoRegistrado = new Checkbox();
    Checkbox checkRegistrado = new Checkbox();
    
    
    
    Paragraph cedulaCliente = new Paragraph();
    Paragraph fechaGenFact = new Paragraph();
    Paragraph descripcionText = new Paragraph();
    Paragraph cantidadText = new Paragraph();
    Paragraph valorTotalText = new Paragraph();
    
    Paragraph descripcionProducto = new Paragraph();
    Paragraph cantidadProducto = new Paragraph();
    Paragraph valorTotal = new Paragraph();
    
    Paragraph subtotalText = new Paragraph();
    Paragraph subtotalSinIva = new Paragraph();
    Paragraph ivaText = new Paragraph();
    Paragraph subtotalConIva = new Paragraph();
    Paragraph totalText = new Paragraph();
    Paragraph totalSinDesc = new Paragraph();
    Paragraph formaPagoText = new Paragraph();
    Paragraph descText = new Paragraph();
    Paragraph montoText = new Paragraph();
    Paragraph metodoPagoElegido = new Paragraph();
    Paragraph porcDescuento = new Paragraph();
    Paragraph montoPago = new Paragraph();



    
    // ? Se define el constructor general de la vista.
    public FacturacionView() {
        
       
        
    // ? Se sincroniza el grid con el archivo de facturas, cargando los datos en la lista de facturas.
    
    
    
        // ? Se recorre la lista de facturas leídas y se crea un objeto DatosFactura por cada línea.
        List<String> facturasLeidas = DatosFactura.lecturaFacturas(-1);
        
        for (String linea : facturasLeidas) {
            String[] partes = linea.split("\\|"); // ? Separa la línea en partes usando el delimitador '|'
            

            esRegistrado = partes[1].trim().equals("0") ? "No Registrado" : "Registrado"; // ? Determina si el cliente está registrado o no.
            esUsuarioDefault = esRegistrado.equals("No Registrado")? "0000000000" : partes[1].trim(); 

            //* numeroFactura|cedula|fecha|descripcion1_descripcion2_...|cantidad1_cantidad2_...|totalPorFila1_totalPorFila2_...|subtotal|iva|total|formaDePago|descuento|monto
            // ? Se crea un nuevo objeto DatosFactura con las partes correspondientes.
            vistaBase.add(new DatosFactura( 
                partes[0].trim(), // ? Número de factura.
                partes[2].trim(), // ? Fecha de generación.
                esUsuarioDefault, // ? Cédula del cliente.
                partes[9].trim(), // ? Forma de pago.
                esRegistrado      // ? Estado del cliente (registrado o no registrado).
            ));
            vistaDinamica.clear();
            vistaDinamica.addAll(vistaBase); // ? Se asigna la lista de facturas a la lista dinámica para el grid.

        }
        



    // ? Apardado de configuraciones de los componentes de la vista [la configuración del lado derecho está en una función].

        
        getContent().setWidth("100%");
        getContent().setHeight("min-content");

        
        headerPrincipal.setText("Lista de Facturas Electrónicas || Ventas Almacén");
        headerPrincipal.setWidth("max-content");
        getContent().setAlignSelf(FlexComponent.Alignment.CENTER, headerPrincipal);
        lineaDivisoraSuperior.setHeight("1px");
        

        layoutRow.setWidthFull();
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.setHeight("min-content");
        getContent().setFlexGrow(1.0, layoutRow);


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
        layoutColumn3.setHeight("190px");


        headerBusqueda.setText("Filtros de Búsqueda");
        headerBusqueda.setWidth("max-content");
        

        layoutRow2.setWidthFull();
        layoutColumn3.setFlexGrow(1.0, layoutRow2);
        layoutRow2.setHeight("fit-content");
        layoutRow2.setAlignItems(Alignment.CENTER);
        layoutRow2.setJustifyContentMode(JustifyContentMode.START);


        numFacturaField.setLabel("Número de Factura");
        numFacturaField.setWidthFull();
        numFacturaField.setMaxWidth("200px");
        numFacturaField.setMinWidth("50px");
        numFacturaField.getElement().setAttribute("autocomplete", "off");
        layoutRow2.setAlignSelf(FlexComponent.Alignment.END, numFacturaField);


        numFacturaField.getElement().executeJs(
            "this.addEventListener('keydown', function(e) {" +
            "  const allowed = (e.key >= '0' && e.key <= '9') || " +
            "    ['Backspace','Tab','ArrowLeft','ArrowRight','Delete'].includes(e.key) || " +
            "    e.ctrlKey || e.metaKey;" +
            "  if (!allowed) {" +
            "    e.preventDefault();" +
            "    return;" +
            "  }" +
            "  if ((e.key >= '0' && e.key <= '9') && this.value.length >= 10 && this.selectionStart === this.selectionEnd) {" +
            "    e.preventDefault();" +
            "  }" +
            "});"
        );


        

        cedulaField.setLabel("Cédula del Cliente");
        cedulaField.setWidthFull();
        cedulaField.setMaxWidth("200px");
        cedulaField.setMinWidth("50px");
        cedulaField.getElement().setAttribute("autocomplete", "off"); 
        layoutRow2.setAlignSelf(FlexComponent.Alignment.END, cedulaField);


        cedulaField.getElement().executeJs(
            "this.addEventListener('keydown', function(e) {" +
            "  const allowed = (e.key >= '0' && e.key <= '9') || " +
            "    ['Backspace','Tab','ArrowLeft','ArrowRight','Delete'].includes(e.key) || " +
            "    e.ctrlKey || e.metaKey;" +
            "  if (!allowed) {" +
            "    e.preventDefault();" +
            "    return;" +
            "  }" +
            "  if ((e.key >= '0' && e.key <= '9') && this.value.length >= 10 && this.selectionStart === this.selectionEnd) {" +
            "    e.preventDefault();" +
            "  }" +
            "});"
        );
        


        datePicker.getElement().executeJs(
            "this.inputElement.setAttribute('readonly', true);"
        );

        datePicker.setMin(LocalDate.parse("2000-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        datePicker.setMax(LocalDate.now());

        datePicker.setLabel("Fecha de Generación");
        datePicker.setWidthFull();
        datePicker.setMaxWidth("200px");
        datePicker.setMinWidth("50px");
        layoutRow2.setAlignSelf(FlexComponent.Alignment.END, datePicker);
        

        metodoDePago.setLabel("Método de Pago");
        metodoDePago.setWidthFull();
        metodoDePago.setMaxWidth("200px");
        metodoDePago.setMinWidth("50px");
        metodoDePago.setItems("", "Crédito", "Débito", "Efectivo", "Transferencia");
        metodoDePago.setValue("");
        layoutRow2.setAlignSelf(FlexComponent.Alignment.END, metodoDePago);



        layoutRow3.setWidthFull();
        layoutColumn3.setFlexGrow(1.0, layoutRow3);
        layoutRow3.addClassName(Gap.SMALL);
        layoutRow3.setWidth("100%");
        layoutRow3.setHeight("min-content");
        layoutRow3.setAlignItems(Alignment.CENTER);
        layoutRow3.setJustifyContentMode(JustifyContentMode.CENTER);
        
        
        checkNoRegistrado.setLabel("Cliente No Registrado");
        checkNoRegistrado.setWidth("195px");
        layoutRow3.setAlignSelf(FlexComponent.Alignment.CENTER, checkNoRegistrado);
        

        checkRegistrado.setLabel("Cliente Registrado");
        checkRegistrado.getStyle().set("flex-grow", "1");
        layoutRow3.setAlignSelf(FlexComponent.Alignment.CENTER, checkRegistrado);


        aplicarFiltros.setText("Aplicar Filtros");
        aplicarFiltros.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        layoutRow3.setAlignSelf(FlexComponent.Alignment.CENTER, aplicarFiltros);


        botonResetear.setText("Resetear");
        layoutRow3.setAlignSelf(FlexComponent.Alignment.CENTER, botonResetear);
        

        lineaDivisoraGrid.setHeight("2px");
        

        headerSeccionGrid.setText("Historial de Facturaciones");
        headerSeccionGrid.setWidth("max-content");
        headerSeccionGrid.setHeight("40px");
        layoutColumn2.setAlignSelf(FlexComponent.Alignment.START, headerSeccionGrid);


        // ? Se crea el grid para mostrar las facturas.
        // ? Se configura el grid para mostrar los datos de las facturas.
        Grid<DatosFactura> grid = new Grid<>();
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.addColumn(DatosFactura::getNumero).setHeader("Número Factura").setSortable(true);
        grid.addColumn(DatosFactura::getFecha).setHeader("Fecha Generación").setSortable(true);
        grid.addColumn(DatosFactura::getCedula).setHeader("Cédula").setSortable(true);
        grid.addColumn(DatosFactura::getFormaPago).setHeader("Método Pago").setSortable(true);
        grid.addColumn(DatosFactura::getRegistrado).setHeader("Cliente").setSortable(true);
        grid.setItems(vistaDinamica); // ? Se asigna la lista de facturas al grid.



        // ? Este es el layout principal de la factura, donde se mostrarán los datos de la factura seleccionada.
        layoutColumn4.setHeightFull();
        layoutRow.setFlexGrow(1.0, layoutColumn4);
        layoutColumn4.addClassName(Gap.XSMALL);
        layoutColumn4.addClassName(Padding.LARGE);
        layoutColumn4.setWidth("min-content");
        layoutColumn4.setHeight("100%");
        layoutColumn4.setJustifyContentMode(JustifyContentMode.START);
        layoutColumn4.setAlignItems(Alignment.CENTER);



        // ? Se añaden todos los componentes al layout izquierdo de la vista.
        getContent().add(headerPrincipal);
        getContent().add(lineaDivisoraSuperior);
        getContent().add(layoutRow);
        layoutRow.add(layoutColumn2);
        layoutColumn2.add(layoutColumn3);
        layoutColumn3.add(headerBusqueda);
        layoutColumn3.add(layoutRow2);
        layoutRow2.add(numFacturaField);
        layoutRow2.add(datePicker);
        layoutRow2.add(cedulaField);
        layoutRow2.add(metodoDePago);
        layoutColumn3.add(layoutRow3);
        layoutRow3.add(checkNoRegistrado);
        layoutRow3.add(checkRegistrado);
        layoutRow3.add(aplicarFiltros);
        layoutRow3.add(botonResetear);
        layoutColumn2.add(lineaDivisoraGrid);
        layoutColumn2.add(headerSeccionGrid);
        layoutColumn2.add(grid);


        // ? Se añade el contenedor del lado derecho a la vista (lado de la factura).
        layoutRow.add(layoutColumn4);

        // ? Se genera la factura por defecto al abrir la vista.
        generarFactura(new DatosFactura("", "", "", "", ""));

        



        numFacturaField.addBlurListener(event -> {

            String numFactura = numFacturaField.getValue().trim();
            numFactura = numFactura.replaceAll("[^0-9]", ""); // ? Se eliminan los caracteres no numéricos del número de factura.
            if (numFactura.length() > 10) {
                numFactura = numFactura.substring(0, 10); // ? Se limita el número de factura a 10 caracteres.
            }
            numFacturaField.setValue(numFactura); // ? Se actualiza el campo de número de factura con el valor formateado.

        });




        cedulaField.addBlurListener(event -> {

            String cedula = cedulaField.getValue().trim();
            cedula = cedula.replaceAll("[^0-9]", ""); // ? Se eliminan los caracteres no numéricos de la cédula.
            if (cedula.length() > 10) {
                cedula = cedula.substring(0, 10); // ? Se limita la cédula a 10 caracteres.
            }
            cedulaField.setValue(cedula); // ? Se actualiza el campo de cédula con el valor formateado.

        });



        grid.addSelectionListener(event -> {

            // ! Probablemente deberían ser variables definidas en la parte superior de la clase.
            DatosFactura numFactSeleccionado = event.getFirstSelectedItem()
                    .orElse(new DatosFactura("", "", "", "", ""));

            // ? Se llama al método para generar la factura con los datos de la factura seleccionada.
            generarFactura(numFactSeleccionado);

        });
    
    

        aplicarFiltros.addClickListener(event -> {

            // ? Se obtienen los valores de los filtros.
            String numFactura = numFacturaField.getValue().trim();
            String cedula = cedulaField.getValue().trim();
            String fecha = datePicker.getValue() != null ? datePicker.getValue().toString() : "";
            String metodoPago = metodoDePago.getValue();
            boolean noRegistrado = checkNoRegistrado.getValue();
            boolean registrado = checkRegistrado.getValue();

                
            // ? Se formatea la fecha para que sea compatible con el formato de la factura.
            if (fecha.isEmpty()) {
                fecha = "";
            
            } else {
                fecha = fecha.replaceAll("-", ""); 
                fecha = fecha.substring(6)+ "/" + fecha.substring(4, 6) +  "/" + fecha.substring(0, 4);
                // AAAA/MM/DD -> DD/MM/AAAA
                // AAAAMMDD -> DD/MM/AAAA
            }
            
            
            // ? Se filtra la lista de facturas según los filtros aplicados.
            vistaDinamica.clear(); // ? Se limpia la lista dinámica antes de aplicar los filtros.
            
            for (DatosFactura factura : vistaBase) {
                boolean matches = true;
                
                if (!numFactura.isEmpty() && !factura.getNumero().contains(numFactura)) {
                    matches = false;
                }
                if (!cedula.isEmpty() && !factura.getCedula().contains(cedula)) {
                    matches = false;
                }
                if (!fecha.isEmpty() && !factura.getFecha().contains(fecha)) {
                    matches = false;
                }
                if (!metodoPago.isEmpty() && !factura.getFormaPago().equals(metodoPago)) {
                    matches = false;
                }
                if (noRegistrado && factura.getRegistrado().equals("Registrado")) {
                    matches = false;
                }
                if (registrado && factura.getRegistrado().equals("No Registrado")) {
                    matches = false;
                }
                
                if (matches) {
                    vistaDinamica.add(factura);
                }
                
            }
            
            grid.setItems(vistaDinamica); // ? Se actualiza el grid con la lista filtrada.
            grid.getDataProvider().refreshAll();


        });


        
        botonResetear.addClickListener(event -> {
            
            // ? Se reinician los filtros y se actualiza el grid con la lista base de facturas.
            numFacturaField.clear();
            cedulaField.clear();
            datePicker.clear();
            metodoDePago.setValue("");
            checkNoRegistrado.setValue(false);
            checkRegistrado.setValue(false);
            vistaDinamica.clear();
            vistaDinamica.addAll(vistaBase);
            grid.setItems(vistaDinamica); // ? Se actualiza el grid con la lista base de facturas.
            grid.getDataProvider().refreshAll();
            
            // ? Se genera la factura por defecto al reiniciar los filtros.
            generarFactura(new DatosFactura("", "", "", "", ""));

        });


    

    }



    // * Aquí comienza el espacio para las funciones y métodos de la vista.


    public void generarFactura(DatosFactura numFactSeleccionado) {
        // ? Método para generar una factura, se puede implementar la lógica de generación aquí.
        // ? Generará la factura por defecto al abrir la vista, y también genera la factura seleccionada en el grid.

            String facturaActual = "";
            String [] partesFactura = new String[0]; // ? Se inicializa la variable para almacenar los datos de la factura seleccionada.
            String [] descripciones = new String[0]; // ? Se inicializa la variable para almacenar las descripciones de los productos de la factura.
            String [] cantidades = new String[0]; // ? Se inicializa la variable para almacenar las cantidades de los productos de la factura.
            String [] totalesPorFila = new String[0]; // ? Se inicializa la variable para almacenar los totales por fila de los productos de la factura.
            int indiceFactura = vistaBase.indexOf(numFactSeleccionado); // ? Se obtiene el índice de la factura seleccionada.

            
            // ? Se llama la función para configurar la factura.
            configurarFactura();

            // ? Se limpia el layout de la factura para evitar duplicados al generar una nueva factura.
            layoutColumn4.removeAll();


            // ? Sección constante Header de la factura.
            layoutColumn4.add(tituloFactura);
            layoutColumn4.add(nitCebolliSport);
            layoutColumn4.add(direccionTienda);
            layoutColumn4.add(correoCorporativo);
            layoutColumn4.add(lineaDivisora1Fact);


            if ( !numFactSeleccionado.getNumero().isEmpty() ) {

                facturaActual = DatosFactura.lecturaFacturas( indiceFactura ).get(0); // ? Se obtiene la factura seleccionada.
                partesFactura = facturaActual.split("\\|"); // ? Se separa la factura en partes usando el delimitador '|'.

                // * numeroFactura|cedula|fecha|descripcion1_descripcion2_...|cantidad1_cantidad2_...|totalPorFila1_totalPorFila2_...|subtotal|iva|total|formaDePago|descuento|monto

                // ? Sección dinámica de la factura.
                numeroFactura.setText("No. " + partesFactura[0].trim());
                esUsuarioDefault = (partesFactura[1].trim().equals("0"))? "0000000000" : partesFactura[1].trim() ;
                cedulaCliente.setText("CC: " + esUsuarioDefault);

                fechaGenFact.setText("Fecha: " + partesFactura[2].trim());

            } else {

                // ? Si no se selecciona una factura, se muestra la primer sección de la factura por defecto.
                numeroFactura.setText("No. 0000000000");
                cedulaCliente.setText("CC: 0000000000");
                fechaGenFact.setText("Fecha: DD/MM/AAAA");

            }


            // ? Se añade la primera sección de la factura con los casos ya separados. {Defecto, Seleccionado}
            layoutColumn4.add(numeroFactura);
            layoutColumn4.add(layoutRow4);
            layoutRow4.add(cedulaCliente);
            layoutRow4.add(fechaGenFact);
            layoutColumn4.add(lineaDivisora2Fact);
            layoutColumn4.add(layoutRow5);

            layoutRow5.add(descripcionText);
            layoutRow5.add(cantidadText);
            layoutRow5.add(valorTotalText);  
            


            if ( !numFactSeleccionado.getNumero().isEmpty() ) {


                // ? Sección de productos de la factura.
                descripciones = partesFactura[3].trim().split("_"); // ? Separa las descripciones de los productos.
                cantidades = partesFactura[4].trim().split("_"); // ? Separa las cantidades de los productos.
                totalesPorFila = partesFactura[5].trim().split("_"); // ? Separa los totales por fila de los productos.
                

                for (int i = 0; i < descripciones.length; i++) {
                    
                    layoutRow6.setWidth("358px");
                    descripcionProducto.setText(descripciones[i].trim());
                    descripcionProducto.setWidth("230px");
                    descripcionProducto.getStyle().set("font-size", "var(--lumo-font-size-xs)");
                    layoutRow6.setAlignSelf(FlexComponent.Alignment.CENTER, descripcionProducto);
                    
                    cantidadProducto.setText(cantidades[i].trim());
                    cantidadProducto.setWidth("50px");
                    cantidadProducto.getStyle().set("font-size", "var(--lumo-font-size-xs)");
                    layoutRow6.setAlignSelf(FlexComponent.Alignment.CENTER, cantidadProducto);
                    
                    valorTotal.setText("$" + String.format("%,.2f", Double.parseDouble(totalesPorFila[i].trim())));
                    valorTotal.setWidth("59px");
                    valorTotal.getStyle().set("font-size", "var(--lumo-font-size-xs)");
                    layoutRow6.setAlignSelf(FlexComponent.Alignment.CENTER, valorTotal);


                    // ? Se añade la segunda sección de la factura para el caso seleccionado.
                    layoutRow6.add(descripcionProducto);
                    layoutRow6.add(cantidadProducto);
                    layoutRow6.add(valorTotal);
                    layoutColumn4.add(layoutRow6);
                    
                    // ? Se reinician los valores de las variables para la siguiente iteración.
                    descripcionProducto = new Paragraph();
                    cantidadProducto = new Paragraph();
                    valorTotal = new Paragraph();
                    layoutRow6 = new HorizontalLayout();
                }



            } else {

                descripcionProducto.setText("Aquí va la descripción del producto");
                cantidadProducto.setText("0");
                valorTotal.setText("$0");

                
                // ? Se añade la segunda sección de la factura para el caso por defecto.
                layoutRow6.add(descripcionProducto);
                layoutRow6.add(cantidadProducto);
                layoutRow6.add(valorTotal);
                layoutColumn4.add(layoutRow6);

            }



            if ( !numFactSeleccionado.getNumero().isEmpty() ) {

                subtotalSinIva.setText("$" + partesFactura[6].trim());
                subtotalConIva.setText("$" + partesFactura[7].trim());
                totalSinDesc.setText("$" + partesFactura[8].trim());
                metodoPagoElegido.setText(partesFactura[9].trim());
                porcDescuento.setText(partesFactura[10].trim() + "%");
                montoPago.setText("$" + partesFactura[11].trim());


            } else {

                subtotalSinIva.setText("$0");
                subtotalConIva.setText("$0");
                totalSinDesc.setText("$0");
                metodoPagoElegido.setText("?");
                porcDescuento.setText("0%");
                montoPago.setText("$0");

            }


                // ? Se añade la tercera sección de la factura con los casos ya separados. {Defecto, Seleccionado}
                layoutColumn4.add(lineaDivisora3Fact);
                layoutColumn4.add(layoutRow7);
                layoutRow7.add(subtotalText);
                layoutRow7.add(subtotalSinIva);
                layoutColumn4.add(layoutRow8);
                layoutRow8.add(ivaText);
                layoutRow8.add(subtotalConIva);
                layoutColumn4.add(layoutRow9);
                layoutRow9.add(totalText);
                layoutRow9.add(totalSinDesc);
                layoutColumn4.add(lineaDivisora4Fact);
                layoutColumn4.add(layoutRow10);
                layoutRow10.add(formaPagoText);
                layoutRow10.add(descText);
                layoutRow10.add(montoText);
                layoutColumn4.add(layoutRow11);
                layoutRow11.add(metodoPagoElegido);
                layoutRow11.add(porcDescuento);
                layoutRow11.add(montoPago);
                layoutColumn4.add(lineaDivisora5Fact);
                layoutColumn4.add(mensajeFinal);


    }

    // ? Función para generar la configuración de la factura.
    public void configurarFactura() {


        // ? Configuración de la sección constante de la factura (Encabezado).
        tituloFactura.setText("CebolliSport®");
        tituloFactura.setWidth("117px");
        layoutColumn4.setAlignSelf(FlexComponent.Alignment.CENTER, tituloFactura);
        
        nitCebolliSport.setText("NIT: 000.000.000-0");
        nitCebolliSport.setWidth("137px");
        layoutColumn4.setAlignSelf(FlexComponent.Alignment.CENTER, nitCebolliSport);
        
        direccionTienda.setText("Cl. 1000 # 2000 - 3000 Medellín");
        direccionTienda.setWidth("228px");
        layoutColumn4.setAlignSelf(FlexComponent.Alignment.CENTER, direccionTienda);
        
        correoCorporativo.setText("contabilidad.cs@cebollis.com");
        correoCorporativo.setWidth("214px");
        layoutColumn4.setAlignSelf(FlexComponent.Alignment.CENTER, correoCorporativo);
        
        lineaDivisora1Fact.setHeight("2px");
        

        numeroFactura.setWidth("max-content");
        layoutColumn4.setAlignSelf(FlexComponent.Alignment.CENTER, numeroFactura);
        
        layoutColumn4.setFlexGrow(1.0, layoutRow4);
        layoutRow4.addClassName(Gap.MEDIUM);
        layoutRow4.setWidth("358px");
        layoutRow4.setHeight("min-content");
        layoutRow4.setAlignItems(Alignment.CENTER);
        layoutRow4.setJustifyContentMode(JustifyContentMode.CENTER);
        
        cedulaCliente.setWidth("153px");
        cedulaCliente.getStyle().set("font-size", "var(--lumo-font-size-xs)");
        layoutRow4.setAlignSelf(FlexComponent.Alignment.CENTER, cedulaCliente);
        
        fechaGenFact.setWidth("189px");
        fechaGenFact.getStyle().set("font-size", "var(--lumo-font-size-xs)");
        layoutRow4.setAlignSelf(FlexComponent.Alignment.CENTER, fechaGenFact);
        
        lineaDivisora2Fact.setWidth("100%");
        lineaDivisora2Fact.setHeight("2px");
        
        layoutColumn4.setFlexGrow(1.0, layoutRow5);
        layoutRow5.addClassName(Gap.MEDIUM);
        layoutRow5.setWidth("358px");
        layoutRow5.setHeight("min-content");
        layoutRow5.setAlignItems(Alignment.CENTER);
        layoutRow5.setJustifyContentMode(JustifyContentMode.CENTER);
        
        descripcionText.setText("Descripción");
        descripcionText.setWidth("210px");
        descripcionText.getStyle().set("font-size", "var(--lumo-font-size-xs)");
        layoutRow5.setAlignSelf(FlexComponent.Alignment.CENTER, descripcionText);
        
        cantidadText.setText("Cant.");
        cantidadText.setWidth("50px");
        cantidadText.getStyle().set("font-size", "var(--lumo-font-size-xs)");
        layoutRow5.setAlignSelf(FlexComponent.Alignment.CENTER, cantidadText);
        
        valorTotalText.setText("Vl. Total");
        valorTotalText.setWidth("66px");
        valorTotalText.getStyle().set("font-size", "var(--lumo-font-size-xs)");
        layoutRow5.setAlignSelf(FlexComponent.Alignment.CENTER, valorTotalText);
        
        layoutColumn4.setFlexGrow(1.0, layoutRow6);
        layoutRow6.addClassName(Gap.MEDIUM);
        layoutRow6.setWidth("358px");
        layoutRow6.setHeight("min-content");
        layoutRow6.setAlignItems(Alignment.CENTER);
        layoutRow6.setJustifyContentMode(JustifyContentMode.CENTER);
        


        descripcionProducto.setWidth("230px");
        descripcionProducto.getStyle().set("font-size", "var(--lumo-font-size-xs)");
        layoutRow6.setAlignSelf(FlexComponent.Alignment.CENTER, descripcionProducto);
        
        cantidadProducto.setWidth("50px");
        cantidadProducto.getStyle().set("font-size", "var(--lumo-font-size-xs)");
        layoutRow6.setAlignSelf(FlexComponent.Alignment.CENTER, cantidadProducto);
        
        valorTotal.setWidth("59px");
        valorTotal.getStyle().set("font-size", "var(--lumo-font-size-xs)");
        layoutRow6.setAlignSelf(FlexComponent.Alignment.CENTER, valorTotal);
        


        lineaDivisora3Fact.setWidth("100%");
        lineaDivisora3Fact.setHeight("2px");
        
        layoutColumn4.setFlexGrow(1.0, layoutRow7);
        layoutRow7.addClassName(Gap.MEDIUM);
        layoutRow7.setWidth("358px");
        layoutRow7.setHeight("min-content");
        layoutRow7.setAlignItems(Alignment.CENTER);
        layoutRow7.setJustifyContentMode(JustifyContentMode.CENTER);
        
        subtotalText.setText("Subtotal:");
        subtotalText.setWidth("210px");
        subtotalText.getStyle().set("font-size", "var(--lumo-font-size-xs)");
        layoutRow7.setAlignSelf(FlexComponent.Alignment.CENTER, subtotalText);
        
        subtotalSinIva.setText("$0");
        subtotalSinIva.setWidth("136px");
        subtotalSinIva.getStyle().set("font-size", "var(--lumo-font-size-xs)");
        layoutRow7.setAlignSelf(FlexComponent.Alignment.CENTER, subtotalSinIva);
        
        layoutColumn4.setFlexGrow(1.0, layoutRow8);
        layoutRow8.addClassName(Gap.MEDIUM);
        layoutRow8.setWidth("358px");
        layoutRow8.setHeight("min-content");
        layoutRow8.setAlignItems(Alignment.CENTER);
        layoutRow8.setJustifyContentMode(JustifyContentMode.CENTER);
        
        ivaText.setText("IVA aplicado (19%):");
        ivaText.setWidth("210px");
        ivaText.getStyle().set("font-size", "var(--lumo-font-size-xs)");
        layoutRow8.setAlignSelf(FlexComponent.Alignment.CENTER, ivaText);
        
        subtotalConIva.setText("$0");
        subtotalConIva.setWidth("136px");
        subtotalConIva.getStyle().set("font-size", "var(--lumo-font-size-xs)");
        layoutRow8.setAlignSelf(FlexComponent.Alignment.CENTER, subtotalConIva);
        
        layoutColumn4.setFlexGrow(1.0, layoutRow9);
        layoutRow9.addClassName(Gap.MEDIUM);
        layoutRow9.setWidth("358px");
        layoutRow9.setHeight("min-content");
        layoutRow9.setAlignItems(Alignment.CENTER);
        layoutRow9.setJustifyContentMode(JustifyContentMode.CENTER);
        
        totalText.setText("Total:");
        totalText.setWidth("210px");
        totalText.getStyle().set("font-size", "var(--lumo-font-size-xs)");
        layoutRow9.setAlignSelf(FlexComponent.Alignment.CENTER, totalText);
        
        totalSinDesc.setWidth("136px");
        totalSinDesc.getStyle().set("font-size", "var(--lumo-font-size-xs)");
        layoutRow9.setAlignSelf(FlexComponent.Alignment.CENTER, totalSinDesc);
        
        lineaDivisora4Fact.setWidth("100%");
        lineaDivisora4Fact.setHeight("2px");
        
        layoutColumn4.setFlexGrow(1.0, layoutRow10);
        layoutRow10.addClassName(Gap.MEDIUM);
        layoutRow10.setWidth("358px");
        layoutRow10.setHeight("min-content");
        layoutRow10.setAlignItems(Alignment.CENTER);
        
        layoutRow10.setJustifyContentMode(JustifyContentMode.CENTER);
        formaPagoText.setText("Forma de Pago");
        formaPagoText.setWidth("140px");
        formaPagoText.getStyle().set("font-size", "var(--lumo-font-size-xs)");
        layoutRow10.setAlignSelf(FlexComponent.Alignment.CENTER, formaPagoText);
        
        descText.setText("Desc.");
        descText.setWidth("50px");
        descText.getStyle().set("font-size", "var(--lumo-font-size-xs)");
        layoutRow10.setAlignSelf(FlexComponent.Alignment.CENTER, descText);
        
        montoText.setText("Monto");
        montoText.setWidth("136px");
        montoText.getStyle().set("font-size", "var(--lumo-font-size-xs)");
        layoutRow10.setAlignSelf(FlexComponent.Alignment.CENTER, montoText);
        
        layoutColumn4.setFlexGrow(1.0, layoutRow11);
        layoutRow11.addClassName(Gap.MEDIUM);
        layoutRow11.setWidth("358px");
        layoutRow11.setHeight("min-content");
        layoutRow11.setAlignItems(Alignment.CENTER);
        layoutRow11.setJustifyContentMode(JustifyContentMode.CENTER);
        
        metodoPagoElegido.setWidth("154px");
        metodoPagoElegido.getStyle().set("font-size", "var(--lumo-font-size-xs)");
        layoutRow11.setAlignSelf(FlexComponent.Alignment.CENTER, metodoPagoElegido);
        
        porcDescuento.setWidth("36px");
        porcDescuento.getStyle().set("font-size", "var(--lumo-font-size-xs)");
        layoutRow11.setAlignSelf(FlexComponent.Alignment.CENTER, porcDescuento);
        
        montoPago.setWidth("136px");
        montoPago.getStyle().set("font-size", "var(--lumo-font-size-xs)");
        layoutRow11.setAlignSelf(FlexComponent.Alignment.CENTER, montoPago);
        
        lineaDivisora5Fact.setWidth("100%");
        lineaDivisora5Fact.setHeight("2px");
        
        mensajeFinal.setText("¡Gracias por confiar en nosotros :D!");
        mensajeFinal.setWidth("max-content");
        layoutColumn4.setAlignSelf(FlexComponent.Alignment.CENTER, mensajeFinal);

    }




    // ? Clase interna para manejar los datos de las facturas.
    public static class DatosFactura {
        private String numero;
        private String fecha;
        private String cedula;
        private String formaPago;
        private String registrado;



        public DatosFactura(String numero, String fecha, String cedula, String formaPago, String registrado) {
            this.numero = numero;
            this.fecha = fecha;
            this.cedula = cedula;
            this.formaPago = formaPago;
            this.registrado = registrado;
        }


        public static List<String> lecturaFacturas(int indice) {

            List<String> facturas = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader("Datos/Facturas.txt"))) {
                
                String linea;
                int contador = 0;

                br.readLine(); // ? Se descarta el header.
                while ((linea = br.readLine()) != null) {
                    
                    if ( indice == -1 ) {
                        facturas.add(linea);
                    } else if ( contador == indice ) {
                        facturas.add(linea);
                        break; // ? Si se encuentra la línea deseada, se sale del bucle.
                    }
                    contador++;

                }

            } catch (FileNotFoundException e) {
                System.out.println("Archivo no encontrado: " + e.getMessage());
            } catch (IOException io) {
                System.out.println("Error al leer el archivo: " + io.getMessage());
            } catch (Exception e) {
                System.out.println("Error inesperado: " + e.getMessage());
            }

            return facturas;

        }



        public String getNumero() { 
            return numero; 
        }

        public String getFecha() { 
            return fecha; 
        }

        public String getCedula() {
            return cedula; 
        }

        public String getFormaPago() { 
            return formaPago; 
        }

        public String getRegistrado() { 
            return registrado; 
        }

    }

}

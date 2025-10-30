package com.example.application.Views;


// ? Se realizan las importaciones de todos los elementos necesarios para la vista.
import com.example.application.Services.UserService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.vaadin.lineawesome.LineAwesomeIconUrl;
import com.vaadin.flow.component.grid.Grid;





@PageTitle("CebolliSport") // ? Se controla el título de la pestaña.
@Route("Cuentas") // ? Se controla la ruta de la vista.
@Menu(order = 4, icon = LineAwesomeIconUrl.ADDRESS_BOOK_SOLID) // ? Se controla el icono de la vista.
public class CuentasUsuariosView extends Composite<VerticalLayout> { // ? Se controla la vista de la pestaña.



// * Constructores de la clase UserService
    // ? Constructor creacion cuentas, orientada a conseguir los valores de los campos diligenciados correctamente.
    UserService extraccionValores = new UserService("Cédula", "Nombre", "Correo", "Teléfono", "RolUsuario");
    

    // ? Constructores edicion cuentas, orientados a dejar una copia (finder) y a definir una fila vacia en el grid (emtpy).
    // El finder aloja la cuenta previa al momento de edición para facilitar la cancelación del proceso.
    // El empty sirve para resetear el valor del finder y para mostrar la fila en edición como vacia en el grid.
    UserService finder = new UserService("", "", "", "","");
    UserService empty = new UserService("", "", "", "", "");


    // ? Se crea la lista de usuarios para almacenar los datos del archivo de cuentas.
    // usuariosBase es la lista de objetos tipo UserService que siempre estará sincronizada con el archivo cuentas.txt
    // usuariosDinamicos es la lista del mismo estilo que permitirá la aplicacion de filtros sin afectar el sync con el archivo
    final List<UserService> usuariosBase = new ArrayList<>();
    final List<UserService> usuariosDinamicos = new ArrayList<>();


    // ? Se crea la lista de indices para almacenar los indices de las cuentas a eliminar.
    // Los indices de esta lista se obtienen con indexOf de cada objeto que se haya seleccionado en el grid aplicado en usuariosBase
    // Al estar las cuentas de usuariosBase en el mismo orden que las cuentas en el archivo, los indices nos permiten saber que lineas eliminar de este
    List<Integer> indicesPorEliminar = new ArrayList<>();


    // ? Se inicializa la variable a usar para su posterior uso en la funcion gestorDeNotificaciones.
    private Notification notificacionActual = null;
    

// ? Se crean los componentes de la vista (de manera global para poder ser recursivos).
    Notification notificaciones = new Notification();
    Dialog dialogoInteractivo = new Dialog();
    Hr lineaDivisora = new Hr();
    H4 headerPrincipal = new H4();
    H5 headerSecundario = new H5();
    HorizontalLayout layoutRow = new HorizontalLayout();
    HorizontalLayout layoutRow2 = new HorizontalLayout();
    HorizontalLayout layoutRow3 = new HorizontalLayout();
    HorizontalLayout layoutRow4 = new HorizontalLayout();
    TextField cedulaField = new TextField();
    TextField nombreField = new TextField();
    TextField correoField = new TextField();
    TextField telefonoField = new TextField();
    TextField busquedaField = new TextField();
    Button botonVariable = new Button();
    Button botonCancelar = new Button();
    Button aplicarFiltros = new Button();
    Button botonResetear = new Button();
    Button botonEliminar = new Button();
    Select<String> select = new Select<>();



// ? Se crean las variables globales necesarias.
    boolean busquedaValida = false;
    boolean filtrosActivos = false;
    String mensajePanel = "";
    String textoNotificacion = "";
    String valorBusqueda = "";
    String seleccionOptionBox = "";
    String casoValidacion = "";
    String regexValidacion = ""; 
    String replaceFilter = ""; 
    String seccionLocal = "";
    String seccionDominio = "";
    String seccionTLD = "";
    
    String nombre = "";
    int minLength = 0; 
    int maxLength = 0;
    int firstValidCharIndex = 0;
    int lastValidCharIndex = 0;

    int indiceEdicionD = 0;
    int indiceEdicionB = 0;
    



// TODO: WAYPOINT (VIEW MAIN)

    public CuentasUsuariosView() {
        
        
        // ? Se crea el grid que maneja objetos del tipo UserService
        // El componente grid es la tabla en la que se muestran las cuentas creadas con todos sus datos.
        Grid<UserService> grid = new Grid<>();
        

        // ? Se sincroniza el grid con el archivo de cuentas, cargando los datos en la lista de usuarios.
        List<String> cuentasLeidas = UserService.lecturaCuentas(indicesPorEliminar);
        String[] partes = new String[0];
    
        try {

            for (String linea : cuentasLeidas) {
                partes = linea.split("\\|");
                if (partes.length == 5) {
                    usuariosBase.add(new UserService(partes[0], partes[1], partes[2], partes[3], partes[4]));
                    usuariosDinamicos.clear();
                    usuariosDinamicos.addAll(usuariosBase);
                }
            }
            
        } catch (IndexOutOfBoundsException iob) {
            System.out.println("¡Alguna línea del archivo cuentas.txt no contiene la estructura adecuada!");
            gestorDeNotificaciones("¡Alguna línea del archivo cuentas.txt no contiene la estructura adecuada!", 3000, Notification.Position.TOP_CENTER);
        } catch (Exception e) {
            System.out.println("Error: " + "\n" + e.getMessage());
        }



    // TODO: APARTADO DE CONFIGURACIONES DE LOS COMPONENTES DE LA VISTA.

        // ? Configuracion del contenedor padre (contiene todos los elementos de la vista).
        getContent().setWidth("100%");
        getContent().setHeight("min-content");
        getContent().setJustifyContentMode(JustifyContentMode.CENTER);
        getContent().setAlignItems(Alignment.CENTER);

        // ? Configuracion del header "Lista de Usuarios Registrados".
        headerPrincipal.setText("Lista de Usuarios Registrados");
        getContent().setAlignSelf(FlexComponent.Alignment.CENTER, headerPrincipal);
        headerPrincipal.setWidth("max-content");

        // ? Configuracion del header "Administración de Cuentas".
        headerSecundario.setText("Administración de Cuentas");
        headerSecundario.setWidth("max-content");
        getContent().setAlignSelf(FlexComponent.Alignment.START, headerSecundario);

        // ? Configuracion del contenedor horizontal1(que contiene los campos a llenar y 2 botones).
        layoutRow.setWidthFull();
        getContent().setFlexGrow(1.0, layoutRow);
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.setFlexGrow(1.0, cedulaField, nombreField, correoField, telefonoField);
        layoutRow.setAlignItems(Alignment.END);
        layoutRow.setJustifyContentMode(JustifyContentMode.CENTER);

        // ? Configuracion de los campos de texto.
        cedulaField.setLabel("Cédula de Ciudadanía");
        cedulaField.setPlaceholder("Ingrese su cédula");
        cedulaField.getElement().setAttribute("autocomplete", "off");
        cedulaField.setPattern("[0-9]{7,10}");
        cedulaField.setRequired(true);
        cedulaField.setRequiredIndicatorVisible(true);
        cedulaField.setWidthFull();
        cedulaField.setMaxWidth("150px");
        cedulaField.setMinWidth("50px");

        nombreField.setLabel("Nombre Completo");
        nombreField.setPlaceholder("Ingrese su nombre");
        nombreField.getElement().setAttribute("autocomplete", "off");
        nombreField.setPattern("[a-zA-Z]{3,13}(\\s[a-zA-Z]{3,13}){0,3}");
        nombreField.setRequired(true);
        nombreField.setRequiredIndicatorVisible(true);
        nombreField.setWidthFull();
        nombreField.setMaxWidth("350px");
        nombreField.setMinWidth("50px");

        correoField.setLabel("Correo Electrónico");
        correoField.setPlaceholder("Ingrese su correo");
        correoField.getElement().setAttribute("autocomplete", "off");
        correoField.setPattern("^[a-z0-9](?:[a-z0-9]|[._%+-](?=[a-z0-9]))*@(?:[a-z0-9-]+\\\\.)+[a-z]{2,}$");
        correoField.setRequired(true);
        correoField.setRequiredIndicatorVisible(true);
        correoField.setWidthFull();
        correoField.setMaxWidth("400px");
        correoField.setMinWidth("50px");

        telefonoField.setLabel("Número de Teléfono");
        telefonoField.setPlaceholder("Num. de Teléfono");
        telefonoField.getElement().setAttribute("autocomplete", "off");
        telefonoField.setPattern("[0-9]{10}");
        telefonoField.setRequired(true);
        telefonoField.setRequiredIndicatorVisible(true);
        telefonoField.setWidthFull();
        telefonoField.setMaxWidth("150px");
        telefonoField.setMinWidth("50px");

        // ? Configuracion del contenedor horizontal2 (Hijo de layoutRow). Contiene los botones
        layoutRow2.setHeightFull();
        layoutRow2.setWidthFull();
        layoutRow.setFlexGrow(0.0, layoutRow2);
        layoutRow2.addClassName(Gap.MEDIUM);
        layoutRow2.setAlignItems(Alignment.CENTER);
        layoutRow2.setJustifyContentMode(JustifyContentMode.END);

        // ? Configuracion del boton de crear/editar (Contenido en layoutRow2).
        botonVariable.setText("Crear");
        botonVariable.setWidth("min-content");
        botonVariable.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        layoutRow2.setAlignSelf(FlexComponent.Alignment.END, botonVariable);

        // ? Configuración del botón de cancelar edición.
        botonCancelar.setText("Cancelar");
        botonCancelar.setWidth("min-content");
        layoutRow2.setAlignSelf(FlexComponent.Alignment.END, botonCancelar);
        botonCancelar.setVisible(false);

        // ? Configuracion del contenedor horizontal3. Contiene los campos de búsqueda y los botones controladores de filtros.
        layoutRow3.setWidthFull();
        getContent().setFlexGrow(1.0, layoutRow3);
        layoutRow3.addClassName(Gap.MEDIUM);
        layoutRow3.setWidth("100%");
        layoutRow3.getStyle().set("flex-grow", "1");
        layoutRow3.setAlignItems(Alignment.END);
        layoutRow3.setJustifyContentMode(JustifyContentMode.CENTER);

        // ? Configuracion del campo de busqueda dinamica.
        busquedaField.setLabel("Búsqueda Dinámica");
        busquedaField.setPlaceholder("Ingrese el dato a buscar");
        busquedaField.getElement().setAttribute("autocomplete", "off");
        busquedaField.setPattern("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\\\.[a-zA-Z]{2,}");
        busquedaField.setWidthFull();
        busquedaField.setMaxWidth("400px");
        busquedaField.setMinWidth("50px");

        // ? Configuracion del selector de tipo de dato.
        select.setLabel("Tipo de dato");
        select.setRequiredIndicatorVisible(true);
        select.setWidthFull();
        select.setMaxWidth("160px");
        select.setMinWidth("50px");
        select.setItems("", "Cédula", "Nombre", "Correo", "Teléfono");
        select.setValue("");

        // ? Configuracion del boton de aplicar filtro.
        aplicarFiltros.setText("Aplicar Filtros");
        aplicarFiltros.setWidth("min-content");
        aplicarFiltros.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        layoutRow3.setAlignSelf(FlexComponent.Alignment.END, aplicarFiltros);

        // ? Configuracion del boton de resetear.
        botonResetear.setText("Resetear");
        botonResetear.setWidth("min-content");
        layoutRow3.setAlignSelf(FlexComponent.Alignment.END, botonResetear);

        // ? Configuracion del contenedor horizontal4 (Hijo de layoutRow3). Contiene el boton de eliminar.
        layoutRow4.setHeightFull();
        layoutRow3.setFlexGrow(1.0, layoutRow4);
        layoutRow4.addClassName(Gap.MEDIUM);
        layoutRow4.setWidth("100%");
        layoutRow4.getStyle().set("flex-grow", "1");
        layoutRow4.setAlignItems(Alignment.CENTER);
        layoutRow4.setJustifyContentMode(JustifyContentMode.END);

        // ? Configuracion del boton de eliminar (Contenido en layoutRow4).
        botonEliminar.setText("Eliminar (0)");
        botonEliminar.setWidth("min-content");
        botonEliminar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        layoutRow4.setAlignSelf(FlexComponent.Alignment.END, botonEliminar);



    // TODO: ADICION DE LOS COMPONENTES A SUS RESPECTIVOS CONTENEDORES.

        // ? Añade el header "Lista de Usuarios Registrados".
        // ? Añade el header "Administración de Cuentas".
        // ? Y añade el separador al contenedor padre.
        getContent().add(headerPrincipal);
        getContent().add(lineaDivisora);
        getContent().add(headerSecundario);
        
        // ? Añade la fila de campos al contenedor padre.
        getContent().add(layoutRow);

        // ? Añade los campos de texto a la fila de campos.
        layoutRow.add(cedulaField);
        layoutRow.add(nombreField);
        layoutRow.add(correoField);
        layoutRow.add(telefonoField);

        // ? Añade el contenedor de los botones al contenedor de los campos, y el boton de crear/editar y el cancelar al contenedor de los botones.
        layoutRow.add(layoutRow2);
        layoutRow2.add(botonVariable);
        layoutRow2.add(botonCancelar);

        // ? Añade la fila de busqueda al contenedor padre.
        getContent().add(layoutRow3);

        // ? Añade el campo de busqueda dinamica. Añade el selector de tipo de dato.Y añade los botones de aplicar filtro y resetear a la fila de busqueda.
        layoutRow3.add(busquedaField);
        layoutRow3.add(select);
        layoutRow3.add(aplicarFiltros);
        layoutRow3.add(botonResetear);

        // ? Añade la fila de botones 2 a la fila de busqueda, y el boton de eliminar a la fila de botones.
        layoutRow3.add(layoutRow4);
        layoutRow4.add(botonEliminar);

        



    // * Configuracion del grid (Tabla de datos).
    // ? El grid se encarga de mostrar los datos de los usuarios en una tabla.

        // ? Se le asigna el modo de seleccion multiple al grid para poder seleccionar varios usuarios a la vez.
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        // ? Se crean las columnas del grid.
        grid.addColumn(user -> usuariosBase.indexOf(user) + 1)
            .setHeader("Índice")
            .setSortable(false)
            .setVisible(true);
        grid.addColumn(UserService::getCedula).setHeader("Cédula de Ciudadanía").setSortable(true);
        grid.addColumn(UserService::getNombre).setHeader("Nombre Completo").setSortable(true);
        grid.addColumn(UserService::getCorreo).setHeader("Correo Electrónico").setSortable(true);
        grid.addColumn(UserService::getTelefono).setHeader("Número de Teléfono").setSortable(true);
        grid.addColumn(UserService::getRol).setHeader("Rol de Usuario").setSortable(true);
        
        // ? Se añade el grid al contenedor padre para que se muestre en la vista.
        getContent().add(grid);
        
        // ? Se define la cantidad de filas del grid.
        grid.setItems(usuariosDinamicos);




// TODO: FIN DE LA CONFIGURACION DE LA VISTA... SIGUE PARTE LOGICA DEL PROGRAMA.




// * Se crea el grupo de listeners numero 1(Encargado de restringir el contenido de los campos de texto).

// ? Validaciones para el campo de Cedula.
        cedulaField.addBlurListener(event -> {
            casoValidacion = "Cédula";
            asignarFiltros(casoValidacion);
            validacionesGenerales(casoValidacion);

            if ( cedulaField.getValue().isEmpty() ) {
                cedulaField.setInvalid(false);
            }
        });

        // ?  Limitación del input a solo dígitos y teclas de control en cedulaField.
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




// ? Validaciones para el campo de Nombre.
        nombreField.addBlurListener(event -> {
            casoValidacion = "Nombre";

            asignarFiltros(casoValidacion);
            validacionesGenerales(casoValidacion);
            nombre = formateadorNombre(nombreField.getValue());
            nombre = eliminarDuplicados(nombre, 2);
            nombreField.setValue(nombre);

            if ( !nombre.matches("^[a-zA-ZñÑ]{3,13}(\\s[a-zA-ZñÑ]{2,13}){1,4}$")  &&  !nombre.isEmpty()) {
                nombreField.setInvalid(true);
                gestorDeNotificaciones("Jmm, ¡eso no parece un nombre completo válido!", 3000, Notification.Position.TOP_CENTER);
            } else {
                nombreField.setInvalid(false);
            }

        });

        // ?  Limitación del input a solo letras y teclas de control en nombreField.
        nombreField.getElement().executeJs(
            "this.addEventListener('keydown', function(e) {" +
            "  if (e.ctrlKey || e.metaKey) return;" +
            "  if (!/^([a-zA-ZáéíóúÁÉÍÓÚñÑ ]|Backspace|Tab|ArrowLeft|ArrowRight|Delete)$/.test(e.key)) {" +
            "    e.preventDefault();" +
            "    return;" +
            "  }" +
            "  if ((e.key.length === 1) && this.value.length >= 55 && this.selectionStart === this.selectionEnd) {" +
            "    e.preventDefault();" +
            "  }" +
            "});"
        );




// ? Validaciones para el campo de Correo (Funcion propia, caso especial).
        correoField.addBlurListener(event -> {
            
            casoValidacion = "Correo";
            validacionesCorreo(casoValidacion);

        });

        // ?  Limitación del input a máximo 70 caracteres en nombreField.
        correoField.getElement().executeJs(
            "this.addEventListener('keydown', function(e) {" +
            "  if (e.ctrlKey || e.metaKey) return;" +
            "  if (this.value.length >= 70 && e.key.length === 1 && this.selectionStart === this.selectionEnd) {" +
            "    e.preventDefault();" +
            "    return;" +
            "  }" +
            "});"
        );




// ? Validaciones para el campo de Telefono.
        telefonoField.addBlurListener(event -> {
            casoValidacion = "Teléfono";
            asignarFiltros(casoValidacion);
            validacionesGenerales(casoValidacion);
            
            if ( telefonoField.getValue().isEmpty() ) {
                telefonoField.setInvalid(false);
            }

        });

        // ?  Limitación del input a solo dígitos y teclas de control en nombreField.
        telefonoField.getElement().executeJs(
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




        // ? Maneja las validaciones del campo de busqueda dinamica.
        busquedaField.addBlurListener(event -> {
            
            valorBusqueda = busquedaField.getValue();
            seleccionOptionBox = select.getValue();

            if ( !seleccionOptionBox.equals("") ) {

                if ( seleccionOptionBox.equals("Correo") ) {
                    casoValidacion = "Busqueda";
                    validacionesCorreo(casoValidacion);
                    busquedaField.setInvalid(false);
                
                } else if ( seleccionOptionBox.equals("Nombre") ){
                    casoValidacion = seleccionOptionBox;
                    asignarFiltros(seleccionOptionBox);
                    casoValidacion = "Busqueda" + casoValidacion;
                    validacionesGenerales(casoValidacion);
                    valorBusqueda = eliminarDuplicados(valorBusqueda, 2);
                    busquedaField.setValue(formateadorNombre(valorBusqueda));
                    busquedaField.setInvalid(false);

                } else {
                    casoValidacion = seleccionOptionBox;
                    asignarFiltros(seleccionOptionBox);
                    casoValidacion = "Busqueda" + casoValidacion;
                    validacionesGenerales(casoValidacion);
                    busquedaField.setInvalid(false);

                }

            }

        });

        // ? Limita el input del campo de busqueda a la maxima longitud de un correo (campo con capacidad de caracteres mas larga).
        busquedaField.getElement().executeJs(
            "this.addEventListener('keydown', function(e) {" +
            "  if (e.ctrlKey || e.metaKey) return;" +
            "  if (this.value.length >= 70 && e.key.length === 1 && this.selectionStart === this.selectionEnd) {" +
            "    e.preventDefault();" +
            "    return;" +
            "  }" +
            "});"
        );


        // ? Valida que el valor del campo de busqueda, sea compatible en todo momento con el tipo de dato seleccionado.
        select.addValueChangeListener(event -> {
            busquedaField.focus();

        });


        // ? Listener para la aplicacion de filtros de busqueda.        
        aplicarFiltros.addClickListener(event -> {

            String valorBusquedaLower = "";
            boolean match = false;
            
            filtrosActivos = true;
            seleccionOptionBox = select.getValue();

            if (!busquedaField.isEmpty()   &&   !seleccionOptionBox.equals("")) {

                valorBusqueda = busquedaField.getValue();

                if ( casoValidacion.contains("Nombre") ) {
                    busquedaValida = (valorBusqueda.length() > 55)? false : true;
                    
                } else if ( casoValidacion.equals("Busqueda") ) {
                    busquedaValida = (valorBusqueda.length() > 70)? false : true;

                } else {
                    busquedaValida = (valorBusqueda.length() > 10)? false : true;
                }

                if (busquedaValida) {

                    // Se vacía el contenido de la lista dinámica.
                    usuariosDinamicos.clear();

                    valorBusquedaLower = valorBusqueda.toLowerCase();

                    // Se filtran las cuentas de usuariosBase y se añaden las ocurrencias a la lista usuariosDinamicos.
                    for (UserService user : usuariosBase) {
                        switch (seleccionOptionBox) {
                            case "Cédula":
                                match = user.getCedula().contains(valorBusqueda);
                                break;
                            case "Nombre":
                                match = user.getNombre().toLowerCase().contains(valorBusquedaLower);
                                break;
                            case "Correo":
                                match = user.getCorreo().toLowerCase().contains(valorBusquedaLower);
                                break;
                            case "Teléfono":
                                match = user.getTelefono().contains(valorBusqueda);
                                break;
                        }
                        if (match) {
                            usuariosDinamicos.add(user);
                        }
                    }

                    // Se actualiza la cuadrícula con los datos filtrados.
                    grid.setItems(usuariosDinamicos);
                    grid.getDataProvider().refreshAll();
                }
            
            } else if ( seleccionOptionBox.equals("")  &&  busquedaField.isEmpty() ){
                textoNotificacion = "Ningún criterio de búsqueda ha sido ingresado";
                gestorDeNotificaciones(textoNotificacion, 3000, Notification.Position.TOP_CENTER);

            } else if ( seleccionOptionBox.equals("") ){
                busquedaField.clear();
                textoNotificacion = "Información de búsqueda insuficiente. Por favor seleccione el tipo de dato que desee filtrar";
                gestorDeNotificaciones(textoNotificacion, 3000, Notification.Position.TOP_CENTER);

            
            } else {
                textoNotificacion = "El campo de búsqueda está vacío o no contenía ningún caracter válido para el tipo de dato: ''" + seleccionOptionBox + "''";
                gestorDeNotificaciones(textoNotificacion, 3000, Notification.Position.TOP_CENTER);
            }        
        });


        botonResetear.addClickListener(event -> {
            filtrosActivos = false;
            usuariosDinamicos.clear();
            usuariosDinamicos.addAll(usuariosBase);
            grid.setItems(usuariosDinamicos);
            busquedaField.clear();
            select.setValue("");
        
        });





        // * Se crea el listener encargado de determinar la cantidad de filas seleccionadas.
        // ? Se asigna este numero al texto del boton de eliminar para que el usuario tenga feedback visual de lo que esta por eliminar.
        grid.addSelectionListener(event -> {
            int seleccionados = event.getAllSelectedItems().size();
            botonEliminar.setText("Eliminar (" + seleccionados + ")");
            if (seleccionados == 1   &&   !botonCancelar.isVisible()) {
                botonVariable.setText("Editar");
                
            } else if (seleccionados != 1   &&   !botonCancelar.isVisible()) {
                botonVariable.setText("Crear");
            }

        });




        // * Se crea el listener encargado de validar y extraer los valores de los campos de texto.
        // ? Luego de la extraccion, se procede dependiendo del texto que tenga el botonVariable.
        // ? Si el texto es "Crear", se crea una nueva cuenta.
        // ? Si el texto es "Editar", se edita la cuenta seleccionada.
        // ? Si el texto es "Actualizar", se actualiza la cuenta seleccionada, la cual es la misma que se edito.
        botonVariable.addClickListener(event -> {

            Set<UserService> seleccionados = new HashSet<>();
            UserService usuario;


            if (!botonVariable.getText().equals("Editar")){
                

                // Verifica que no exista una cuenta ya creada con alguno de los datos. Esto lo evalúa tanto para Crear como para Editar.
                usuario = getValues(cedulaField, nombreField, correoField, telefonoField);
                if (usuario != null) {
                    boolean coincidencia = usuariosBase.stream().anyMatch(u ->
                        u.getCedula().equals(usuario.getCedula()) ||
                        u.getNombre().equalsIgnoreCase(usuario.getNombre()) ||
                        u.getCorreo().equals(usuario.getCorreo()) ||
                        u.getTelefono().equals(usuario.getTelefono())
                    );

                    if (!botonCancelar.isVisible()) {

                        if (!coincidencia) {
                            usuariosBase.add(usuario);
                            extraccionValores.creacionCuenta(
                                usuario.getCedula(),
                                usuario.getNombre(),
                                usuario.getCorreo(),
                                usuario.getTelefono(),
                                "Cliente"
                            );
    
                            usuariosDinamicos.clear();
                            
                            if (filtrosActivos) {
                                aplicarFiltros.click();
                            
                            } else {
                                usuariosDinamicos.addAll(usuariosBase);
                                grid.setItems(usuariosDinamicos);
                                grid.getDataProvider().refreshAll();
                            }
                        
    
                        } else {
                            gestorDeNotificaciones("Ya existe una cuenta con alguno de los datos ingresados", 3000, Notification.Position.TOP_CENTER);
                        }


                    } else {

                        // Procedimiento para cuando se presione el botón en modo actualizar.
                        
                        if (!coincidencia) {
                            
                            botonCancelar.setVisible(false);
                            usuariosBase.set(indiceEdicionB, usuario);
                            extraccionValores.edicionCuentas(indiceEdicionB, usuario);
                            usuariosDinamicos.clear();

                            indiceEdicionB = 0;
                            indiceEdicionD = 0;
                            finder = empty;

                            botonEliminar.setEnabled(true);
                            botonVariable.setText("Crear");

                            
                            if (filtrosActivos) {
                                aplicarFiltros.click();
                            
                            } else {
                                usuariosDinamicos.addAll(usuariosBase);
                                grid.setItems(usuariosDinamicos);
                                grid.getDataProvider().refreshAll();
                            }
                        
    
                        } else {
                            gestorDeNotificaciones("Ya existe una cuenta con alguno de los datos ingresados", 3000, Notification.Position.TOP_CENTER);
                        }


                    }


                }



            } else { // Procedimiento cuando se presiona el boton de editar.

                // Obtenemos el valor de la fila seleccionada, se reemplaza el valor de cada campo respectivamente con su equivalente de la cuenta.
                seleccionados = grid.getSelectedItems();

                for (UserService cuenta : seleccionados) {
                    finder = cuenta;
                    indiceEdicionB = usuariosBase.indexOf(cuenta); 
                    indiceEdicionD = usuariosDinamicos.indexOf(cuenta);
                }
                    
                cedulaField.setValue(finder.getCedula());
                cedulaField.setInvalid(false);
                nombreField.setValue(finder.getNombre());
                nombreField.setInvalid(false);
                correoField.setValue(finder.getCorreo());
                correoField.setInvalid(false);
                telefonoField.setValue(finder.getTelefono());
                telefonoField.setInvalid(false);

                usuariosDinamicos.set(indiceEdicionD, empty);
                usuariosBase.set(indiceEdicionB, empty);
                grid.setItems(usuariosDinamicos);
                
                botonCancelar.setVisible(true);
                botonVariable.setText("Actualizar");

                botonEliminar.setEnabled(false);

            }

        });




        botonCancelar.addClickListener(event -> {

            botonCancelar.setVisible(false);
            usuariosBase.set(indiceEdicionB, finder);
            usuariosDinamicos.set(indiceEdicionD, finder);
            grid.setItems(usuariosDinamicos);

            cedulaField.clear();
            nombreField.clear();
            correoField.clear();
            telefonoField.clear();
            
            indiceEdicionB = 0;
            indiceEdicionD = 0;
            finder = empty;

            botonVariable.setText("Crear");
            botonEliminar.setEnabled(true);

        });





        // * Se crea el listener encargado de eliminar las cuentas seleccionadas.
        // ? Tras un mensaje de confirmacion, de resultar confirmado, se eliminan las cuentas seleccionadas de todas partes.
        // ? Del archivo de cuentas, de la lista de usuarios que genera el contenido del grid.
        // ? Y se limpian los indicesPorEliminar.
        botonEliminar.addClickListener(event -> {
            
            // ! Toca crearlas dentro del listener. Las operaciones lambda requieren variables final, y eso no conviene para listas de longitud variable.
            Set<UserService> seleccionados = grid.getSelectedItems(); 
            List<Integer> indicesPorEliminar = seleccionados.stream()
            .map(user -> usuariosBase.indexOf(user) + 1)
            .collect(Collectors.toList());

                

            if (indicesPorEliminar.isEmpty()) {
        
                gestorDeNotificaciones("No se han seleccionado cuentas para eliminar", 3000, Notification.Position.TOP_CENTER);
            
            } else {
                    
                    if ( indicesPorEliminar.size() == 1 ) {
                        mensajePanel = "¡Atención! Esta acción es irreversible.\n¿Está seguro de querer eliminar la cuenta seleccionada?";
                    } else {
                        mensajePanel = "¡Atención! Esta acción es irreversible.\n¿Está seguro de querer eliminar las " + indicesPorEliminar.size() + " cuentas selecionadas?";
                    }

                    // ? Se crea un diálogo interactivo que en caso de ser confirmado (Click en el botón confirmar de este), ejecutará el evento:
                    dialogoInteractivo  = crearDialogo( mensajePanel, () -> {
                        
                        // ? ...
                        UserService.lecturaCuentas(indicesPorEliminar);
                        usuariosBase.removeAll(seleccionados);
                        usuariosDinamicos.removeAll(seleccionados);
                        grid.getDataProvider().refreshAll();
                        grid.deselectAll();
                        botonEliminar.setText("Eliminar (0)");

                    });
                    dialogoInteractivo.setHeaderTitle("Panel de Confirmación");
                    dialogoInteractivo.open();
            }
    
        });




        



    }


// TODO: COMIENZA EL ESPACIO PARA LAS FUNCIONES.


    // ? Maneja las notificaciones emergentes, garantizando que solo haya una a la vez.
    private void gestorDeNotificaciones(String message, int duration, Notification.Position position) {
        if (notificacionActual != null   &&   notificacionActual.isOpened()) {
            notificacionActual.close();
        }
        notificacionActual = Notification.show(message, duration, position);
    }




    // ? Maneja la creación de dialogos organizados, que además permiten la segmentación de decisiones en función de click listeners.
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




    
    // ? Se recorre el correo desde el inicio y el final, buscando caracteres especiales.
    // ? Se guarda la posición del primer y último caracter que no sea especial.
    public String validadorSeccionCorreo(String seccion, String tipo) {
        
        String filtro = "";

        try {
            
            if ( tipo.equals("Busqueda") ) {
                filtro = "[a-z0-9@]";
            } else {
                filtro = "[a-z0-9]";
            }
    
            firstValidCharIndex = -1;
            lastValidCharIndex = -1;
    
            for (int i = 0; i < seccion.length(); i++) {
                        
                        if ( firstValidCharIndex == -1  &&  Character.toString(seccion.charAt(i)).matches(filtro) ){
                            firstValidCharIndex = i;
                        }
    
                        if ( lastValidCharIndex == -1   &&   Character.toString(seccion.charAt(seccion.length()-1-i)).matches(filtro)) {
                            lastValidCharIndex = seccion.length()-1-i;
                        }
    
                        if ( firstValidCharIndex != -1  &&  lastValidCharIndex != -1) {
                            return seccion.substring(firstValidCharIndex, lastValidCharIndex + 1);
    
                        }
                    
            }
            
            return "";
            
        } catch (IndexOutOfBoundsException iob) {
            System.out.println("¡Hubo un error en el substring de sección o en el chatAt!");
            return "";
        } catch (Exception e){
            System.out.println("Error: " + "\n" + e.getMessage());
            return "";
        }

    }



    // ? Funcion encargada de establecer los filtros para cada tipo de dato.
    public void asignarFiltros(String tipoDato) {

        switch (tipoDato) {
            case "Cédula":
                regexValidacion = "^[0-9]{7,10}$";
                replaceFilter = "[^0-9]";
                minLength = 7;
                maxLength = 10;    
                break;

            case "Nombre":
                regexValidacion = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]{3,55}$";
                replaceFilter = "[^a-zA-ZáéíóúÁÉÍÓÚñÑ ]";
                minLength = 3;
                maxLength = 55;    
                break;

            case "Teléfono":
                regexValidacion = "^[0-9]{10}$";
                replaceFilter = "[^0-9]";
                minLength = 10;
                maxLength = 10;                
                break;
        
            default:
                break;
        }

    }




    // ? Funcion encargada de eliminar letras duplicadas excesivamente [Para el caso de nombre; 2+ letras es inválido].
    // ? Para el caso de correo, +5 letras es invalido.
    public String eliminarDuplicados(String campo, int limite) {

    
        char letraAnalisis = '\0';
        int contador = 0;
        String limitada = "";
        boolean esPrimero = true;
        boolean charEspecialRep = false;


        try {

            for (int i = 0; i < campo.length(); i++) {
    
                // Determina si ambos la letra analizada y la letra actual son caracteres especiales.
                // Si lo son, se evita que se añada el segundo y se continua con la siguiente letra.
                if (Character.toString(campo.charAt(i)).matches("[+%_.-]")   &&  Character.toString(letraAnalisis).matches("[+%_.-]")) {
                    charEspecialRep = true;
                } else {
                    charEspecialRep = false;
                }
    
    
                // Si la letra analizada es igual a la letra actual y no es un caracter especial repetido.
                // Se aumenta el contador y se añade la letra al string limitado.
                if ((letraAnalisis == campo.charAt(i))  &&  !charEspecialRep   &&  !esPrimero) {
                    contador++;
                
                } else {
                    letraAnalisis = campo.charAt(i);
                    esPrimero = false;
                    contador = 0;
                } 
                
                if (contador < limite  &&  !charEspecialRep) {
                   limitada += campo.charAt(i);
                }
    
            }
    
            return limitada;
        
        } catch (IndexOutOfBoundsException iob) {
            System.out.println("¡Hubo un error en algún ChatAt o substring!");
            return "";
        } catch (Exception e) {
            System.out.println("Error: " + "\n" + e.getMessage());
            return "";
        }

    }




    // ? Esta funcion se encarga de darle formato al nombre, poniendo mayuscula inicial a cada seccion, y reemplazando acentos.
    public String formateadorNombre(String nombreCompleto){

        String seccionNombre = "";
        String [] splitEspacios = {};
        String letraCambiar = "";
        String formateado = "";


        try {
        
            seccionNombre = nombreCompleto.toLowerCase().trim();
    
            if ( seccionNombre.contains("á") ){
                seccionNombre = seccionNombre.replaceAll("á", "a");
            }
            
            if ( seccionNombre.contains("é") ){
                seccionNombre = seccionNombre.replaceAll("é", "e");
            }
            
            if ( seccionNombre.contains("í") ){
                seccionNombre = seccionNombre.replaceAll("í", "i");
            }
            
            if ( seccionNombre.contains("ó") ){
                seccionNombre = seccionNombre.replaceAll("ó", "o");
            }
            
            if ( seccionNombre.contains("ú") ){
                seccionNombre = seccionNombre.replaceAll("ú", "u");
            }
    
    
            splitEspacios = seccionNombre.split(" ");
            seccionNombre = "";
    
    
            for (int i = 0; i < splitEspacios.length; i++) {
    
                seccionNombre = splitEspacios[i];
                seccionNombre.trim().replaceAll("[^a-z]", "");
    
                if (!seccionNombre.equals("")) {
                    letraCambiar = Character.toString(seccionNombre.charAt(0));
                    formateado += letraCambiar.toUpperCase() + seccionNombre.substring(1) + " ";
                } else {
                    break;
                }
    
            }
            formateado = formateado.trim();
            return formateado;
            
        } catch (IndexOutOfBoundsException iob) {
            System.out.println("¡Hubo un error en algún ChatAt o substring!");
            return "";
        } catch (Exception e) {
            System.out.println("Error: " + "\n" + e.getMessage());
            return "";
        }


    }



    // ? funcion encargada de validar que los campos no esten vacios y que su contenido sea valido.
    // ? Entonces, de ser asi; extraer los valores de los campos de texto.
    public UserService getValues(TextField id, TextField name, TextField email, TextField phone) {
        if (!(id.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty())) {
                
            
            // ? Ultimate Verification and Debugging Monstruosity:
            //* |@°¬!"#$%&/()='\?¿¡/*-+.QqqqqSantiagoqqqqqqqqqqqqweeeéééá´qííóóúr@tyuiop @asdfgh+++J22222222222345890231548963.KLÑ@ ZxCvBnm <>,;.:-%%%_{[}]+*´¨~^
            //* !"#$%/()==?¡¿?´___-----<-<-<>>><<>´++}*~~}}{}{[[][]-{-{.,.,:;:;``]|°°¬¬¬!!""^^^#// ? \\__1234567890"&$/(")--__.-.-%%-_-SANTIAGO677777777777777-..45CEBAAAAAAAAAAAAAALLOS..7HENAO9.-+@-*-GM5AIL...C4O468M..-,.
            if (!id.isInvalid() && !name.isInvalid() && !email.isInvalid() && !phone.isInvalid()) {
                
                String cedula = id.getValue();
                String nombre = name.getValue();
                String correo = email.getValue();
                String telefono = phone.getValue();
    
                id.clear();
                id.setInvalid(false);
                name.clear();
                name.setInvalid(false);
                email.clear();
                email.setInvalid(false);
                email.setPlaceholder("Ingrese su correo");
                phone.clear();
                phone.setInvalid(false);
                return new UserService(cedula, nombre, correo, telefono, "Cliente");
                
            } else {
                gestorDeNotificaciones("Algunos de los campos no son válidos!", 3000, Notification.Position.TOP_CENTER);
                return null;
            }


        } else {
            
            if (id.isEmpty()) {
                id.setInvalid(true);
            }
            if (name.isEmpty()) {
                name.setInvalid(true);
            }
            if (email.isEmpty()) {
                email.setInvalid(true);
            }
            if (phone.isEmpty()) {
                phone.setInvalid(true);
            }
            gestorDeNotificaciones("Todos los campos son obligatorios!", 3000, Notification.Position.TOP_CENTER);
            return null;
        }
    }

    




    // ? Funcion encargada de validar el contenido de los campos de texto.
    public void validacionesGenerales(String clave) {
        
        Map<String, TextField> fieldMap = new HashMap<>();
        fieldMap.put("Cédula", cedulaField);
        fieldMap.put("Nombre", nombreField);
        fieldMap.put("Teléfono", telefonoField);
        fieldMap.put("BusquedaCédula", busquedaField);
        fieldMap.put("BusquedaNombre", busquedaField);
        fieldMap.put("BusquedaTeléfono", busquedaField);
        
        String value = fieldMap.get(clave).getValue().trim().replaceAll("\\s+", " ");
        try {
            
            fieldMap.get(clave).setValue(value);
    
                
                if (!value.matches(regexValidacion)) {
                    value = value.replaceAll(replaceFilter, "");
                    value = value.substring(0, Math.min(value.length(), maxLength));
                    fieldMap.get(clave).setValue(value);
    
                } else if (value.length() < minLength   ||   value.length() > maxLength) {
                    fieldMap.get(clave).setInvalid(true);
                    
                } else {
                    fieldMap.get(clave).setInvalid(false);
                }


        } catch (IndexOutOfBoundsException iob) {
            System.out.println("¡Hubo un error en el substring!");
        } catch (Exception e) {
            System.out.println("Error: " + "\n" + e.getMessage());
        }

        

    }




    // ? Funcion encargada de verificar la validez del correo ingresado por el usuario, y de reemplazar cualquier estructura no permitida.
    public void validacionesCorreo(String clave) {

        String correo = "";
        Map<String, TextField> fieldMap = new HashMap<>();
        fieldMap.put("Correo", correoField);
        fieldMap.put("Busqueda", busquedaField);

        minLength = 6;
        maxLength = 70;


        try {
            
            // Remover todos los espacios en blanco y reemplazarlos por un caracter vacío.
            // Esto se hace para evitar que el regex falle al validar el correo.
            correo = fieldMap.get(clave).getValue().trim().toLowerCase().replaceAll("\\s+", ""); //AMBOS CASOS
    
            correo = correo.replaceAll("ñ", "n");
            correo = correo.replaceAll("á", "a");
            correo = correo.replaceAll("é", "e");
            correo = correo.replaceAll("í", "i");
            correo = correo.replaceAll("ó", "o");
            correo = correo.replaceAll("ú", "u");
            fieldMap.get(clave).setValue(correo);
    
        
    
            // Paso1: Remover todos los caracteres inválidos.
            // Aquellos diferentes de: letras, números, puntos, guiones, guiones bajos, arrobas y signos de más.
            correo = correo.replaceAll("[^a-z0-9._%+@-]", ""); //AMBOS CASOS
    
            // Paso2: Remover caracteres especiales repetidos.
            //AMBOS CASOS
            correo = correo.replaceAll("[.]+", ".");
            correo = correo.replaceAll("[+]+", "+");
            correo = correo.replaceAll("[%]+", "%");
            correo = correo.replaceAll("[-]+", "-");
            correo = correo.replaceAll("[_]+", "_");
            correo = correo.replaceAll("[@]+", "@");
            
    
            // Paso3: Remover caracteres especiales al inicio y al final.
            correo = validadorSeccionCorreo(correo, clave); //SE PUDO ESCALAR
            
    
    
            if ( clave.equals("Correo") ){
    
                // Seccionar la direccion de correo en partes; Local, Dominio y TLD.
                // Primero se debe validar si al perder focus, el campo de texto tiene un arroba.
                if (correo.contains("@")){
                
                    // Si tiene arroba, hay que garantizar que solo sea uno, se tomará el primero.
                    seccionLocal = correo.substring(0, correo.indexOf("@"));
                    seccionLocal = validadorSeccionCorreo(seccionLocal, "Correo");
                
    
                    // Primero se intenta reducir la longitud del correo evitando que cualquier caracter se repita de seguido.
                    // Más de 5 veces... ej: "aaaaaa", "111111" esto porque probablemente haya sido un error de tipeo, o una cuenta falsa.
                    seccionLocal = eliminarDuplicados(seccionLocal, 5);
    
                    seccionDominio = correo.substring(correo.indexOf("@")+1);
                    seccionDominio = seccionDominio.replaceAll("[^a-z0-9.-]", "");
                    
                    if (seccionDominio.contains(".")) {
                        seccionTLD = seccionDominio.substring(seccionDominio.lastIndexOf(".")+1);
                        seccionTLD = seccionTLD.replaceAll("[^a-z]","");
    
                        seccionDominio = seccionDominio.substring(0, seccionDominio.lastIndexOf("."));
                        seccionDominio = validadorSeccionCorreo(seccionDominio, "Correo");
                        seccionDominio = eliminarDuplicados(seccionDominio, 5);
    
                        correo = seccionLocal + "@" + seccionDominio + "." + seccionTLD;
    
                    } else {
                        ////seccionDominio += ".tld";
                        ////correo = seccionLocal + "@" + seccionDominio;
                        gestorDeNotificaciones("¡Por favor ingrese una dirección de correo completa!", 3000, Notification.Position.TOP_CENTER);
                    }
    
                } else {
    
                    if (correo.isEmpty()){
                        fieldMap.get(clave).setPlaceholder("direccion_correo@ejemplo.com.co");
    
                    } else if (correo.length() >= 58){
                        gestorDeNotificaciones("El correo es muy largo, máximo " + maxLength + " caracteres", 4000, Notification.Position.TOP_CENTER);
                        fieldMap.get(clave).setInvalid(true);
                    } else {
                        //// correo += "@dominio.tld";
                        correo += "@";
                        gestorDeNotificaciones("¡Por favor ingrese una dirección de correo completa!", 3000, Notification.Position.TOP_CENTER);
                    }
    
                }
            
                // Paso4: Limitar la longitud del correo.
                // Funcion para limitar caracteres duplicados.
                correo = eliminarDuplicados(correo, 5);
                
                // Luego se limita la longitud del correo a un máximo de 70 caracteres.
                correo = correo.substring(0, Math.min(correo.length(), maxLength));
                fieldMap.get(clave).setValue(correo);
    
                if ( !correo.matches("^[a-z0-9](?:[a-z0-9]|[._%+-](?=[a-z0-9]))*@(?:[a-z0-9-]+\\.)+[a-z]{2,}$")   &&  !correo.isEmpty() )  {
                    fieldMap.get(clave).setInvalid(true);
    
                } else {
                    fieldMap.get(clave).setInvalid(false);
                }
            
            }

        } catch (IndexOutOfBoundsException iob) {
            System.out.println("¡Hubo un error en algún ChatAt o substring!");
        } catch (Exception e) {
            System.out.println("Error: " + "\n" + e.getMessage());
        }


    }
}

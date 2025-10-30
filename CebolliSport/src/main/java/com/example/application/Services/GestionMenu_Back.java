package com.example.application.Services;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class GestionMenu_Back {

    // RUTAS DE LOS ARCHIVOS DE DATOS
    public static String RUTA_DATA_PRODUCTO_ESC = "Datos/Productos.txt";
    
    public static String RUTA_DATA_PRODUCTO_LEER = "Datos/Productos.txt";
    
    public static String RUTA_DATA_PRODUCTO_TEMPORAL = "Datos/tempProductos.txt";
    
    public static String RUTA_DATA_COMPRAS = "Datos/Compras.txt";

    // Método para capitalizar la primera letra de cada palabra en un texto
    // Recibe un texto y retorna el mismo texto con la primera letra de cada palabra en mayúscula
    public static String capitalizar(String texto) throws Exception {
        try {

            // Separa el texto en palabras usando el espacio como delimitador
            String[] palabras = texto.split(" ");
            String resultadoCap = "";

            // Si el texto es nulo o está vacío, retorna el texto original
            if (texto == null || texto.isEmpty()) {
                return texto;
            }
            // Recorre cada palabra y capitaliza la primera letra
            for (String palabra : palabras) {
                resultadoCap += " " + palabra.substring(0, 1).toUpperCase() + palabra.substring(1).toLowerCase();
            }
            // Retorna el texto capitalizado, eliminando el espacio inicial
            return resultadoCap.trim();

        } catch (Exception e) {
            throw new Exception("Error al capitalizar el texto: " + e.getMessage());
        }
    }

    // Método para agregar un producto al archivo Productos.txt
    // Recibe los detalles del producto como parámetros y los escribe en el archivo
    public static void agregarProducto(String url, String modelo, String marca, String talla, String color, String type, int stock,String genero, String precio) throws IOException {
        try {
            // Ruta del archivo Productos.txt
            File ubicProd = new File(RUTA_DATA_PRODUCTO_ESC);
            FileWriter escribir = new FileWriter(ubicProd, true);
            PrintWriter memoria = new PrintWriter(escribir);

            memoria.println(url + "|" + modelo + "|" + marca + "|" + talla + "|" + color + "|" + type + "|" +  stock + "|" + genero + "|" +  precio);
            memoria.close();

        } catch (IOException e) {
            throw new IOException("Error al agregar el producto: " + e.getMessage());
        }
    }

    // Método para eliminar un producto del archivo Productos.txt
    // Recibe el modelo y la talla del producto a eliminar
    public static void eliminarProducto(String modelo, String talla) throws IOException {
        try {

            File origFile = new File(RUTA_DATA_PRODUCTO_LEER);
            File tempFile = new File(RUTA_DATA_PRODUCTO_TEMPORAL);
            PrintWriter writer = new PrintWriter(new FileWriter(tempFile));

            List<String[]> productosExist = leerProductos();

            // Ponemos el Header
            writer.println("URL | MODELO | MARCA | TALLA | COLOR | TIPO | CANTIDAD | GENERO | PRECIO UNIDAD |");

            for (String[] producto : productosExist) {
                if (!(producto[1].equals(modelo) && producto[3].equals(talla))){
                    writer.println(String.join("|", producto));
                }
            }

            writer.close();
            
            // Reemplazar el archivo original con el temporal
            origFile.delete();
            tempFile.renameTo(origFile);

        } catch (IOException e) {
            throw new IOException("Error al eliminar el producto: " + e.getMessage());
        }
    }

    // Método para leer los productos del archivo Productos.txt
    // Retorna una lista de arreglos de cadenas, donde cada arreglo representa una fila del archivo
    public static List<String[]> leerProductos() throws IOException{
        List<String[]> filasProducto = new ArrayList<>();

        try {
            File ubicProd = new File(RUTA_DATA_PRODUCTO_LEER);
            FileReader fileReader = new FileReader(ubicProd);
            BufferedReader reader = new BufferedReader(fileReader);    

            String lineText;
            
            // Descartamos la primera linea porque es de documentacion
            reader.readLine();

            while ((lineText = reader.readLine()) != null) {
                filasProducto.add(lineText.split("\\|"));
            }

            reader.close();
            return filasProducto;

        } catch (IOException e) {
            throw new IOException("Error al leer los productos: " + e.getMessage());
        }
    }

    // Método para leer un índice específico de los productos
    // Recibe un índice y retorna una lista de cadenas con los valores de ese índice de cada producto
    // sin repetir valores
    public static List<String> leerIndexProducto(int index) throws Exception {

        try {
            
            List<String[]> filasProudcto = leerProductos();
            List<String> filaSolicitada = new ArrayList<>();

            for (int i = 0; i < filasProudcto.size(); i++) {
                if (!filaSolicitada.contains(filasProudcto.get(i)[index])) filaSolicitada.add(filasProudcto.get(i)[index]);
            }

            return filaSolicitada;

        } catch (Exception e) {
            throw new Exception("Error al leer el índice del producto: " + e.getMessage());
        }
    }

    // Método para extraer las tallas y precios de un modelo específico
    // Recibe el modelo y retorna un mapa donde la clave es la talla y el valor es una lista {unidades disponibles, precio}
    public static HashMap<String, List<Double>> extraerTallarPrecio(String modelo) throws Exception {
        
        try {
            List<String[]> filasProudcto = leerProductos();
            HashMap<String, List<Double>> tallaPrecio = new HashMap<String, List<Double>>();

            for (String[] producto : filasProudcto) {
                if (producto[1].equals(modelo)){
                    tallaPrecio.putIfAbsent(producto[3], new ArrayList<>());
                    tallaPrecio.get(producto[3]).add(Double.parseDouble(producto[6]));
                    tallaPrecio.get(producto[3]).add(Double.parseDouble(producto[8]));

                }
            }
        
            return tallaPrecio;

        } catch (Exception e) {
            throw new Exception("Error al extraer tallas y precios: " + e.getMessage());
        }
    } 

    // Método para registrar una compra en el archivo Compras.txt
    // Recibe el modelo, cantidad y precio total de la compra
    public static void registrarCompra(String modelo, String talla, int cantidad, double precio) throws IOException {

        try {
            File ubicProd = new File(RUTA_DATA_COMPRAS);
            FileWriter escribir = new FileWriter(ubicProd, true);
            PrintWriter memoria = new PrintWriter(escribir);

            memoria.println(modelo + "|" + talla  + "|" +  cantidad + "|" + (precio/cantidad) + "|" + precio);
            memoria.close();

        } catch (IOException e) {
            throw new IOException("Error al registrar la compra: " + e.getMessage());
        }
    }
}

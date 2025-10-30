package com.example.application.Services;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CarritoDeComprasService {

    // ? Esta función es omnipresente
    public void main(String[] args) {

    }

    // ? Esta función retorna 2 descripciones, la que se mostrará en la tabla "Resumen productos" y la que se mostrará en la factura
    public String[] retornaDescripcion(String modelo, String talla) {

        try {

            // * Declaramos las variables
            String linea = "";
            String[] datosSeparadosProductoActual = {};
            String[] descripciones = {"", ""};

            // * Leemos el archivo
            BufferedReader datosPorLinea = new BufferedReader(new FileReader("Datos/Productos.txt"));

            // * Ignoramos la linea del header
            datosPorLinea.readLine();

            // * Recorremos el archivo
            while ((linea = datosPorLinea.readLine()) != null) {

                // * Validamos que la linea no esté vacía
                if (linea.trim().isEmpty()) {
                    continue;
                }

                // * Separamos cada dato de una linea en un arreglo
                datosSeparadosProductoActual = linea.trim().split("\\|");

                // * Si el modelo coincide con el de la linea, sale del ciclo
                if (modelo.equalsIgnoreCase(datosSeparadosProductoActual[1])
                        && talla.equalsIgnoreCase(datosSeparadosProductoActual[3])) {

                    // * Por un lado, retornamos la descripción que se mostrará en la tabla "Resumen Productos"
                    descripciones[0] = " - " + datosSeparadosProductoActual[5] + " "
                            + datosSeparadosProductoActual[2] + ", talla " + datosSeparadosProductoActual[3]
                            + ", color " + datosSeparadosProductoActual[4].toLowerCase();

                    // * Por otro lado, retornamos la descripción que se mostrará en la factura y que se usará para manejar el stock restante
                    descripciones[1] = datosSeparadosProductoActual[1] + " - " + datosSeparadosProductoActual[3] + " - " + datosSeparadosProductoActual[4];

                    break;
                }

            }

            // * Cerramos el buffered
            datosPorLinea.close();

            return descripciones;

        } catch (Exception e) {
            return new String[] {"Error en la función retornaDescripción " + e.getMessage()};
        }

    }

    // ? Esta función retorna un entero con la cantidad de lineas que contiene el archivo "Compras.txt", para generar la cantidad de filas correspondientes a cada compra
    public int contadorLineasCompra() {

        try {

            // * Declaramos las variables
            String linea = "";
            int contador = 0;

            // * Leemos el archivo
            BufferedReader datosPorLinea = new BufferedReader(new FileReader("Datos/Compras.txt"));

            // * Ignoramos la linea del header
            datosPorLinea.readLine();

            // * Recorremos el archivo
            while ((linea = datosPorLinea.readLine()) != null) {

                // * Validamos que la linea no esté vacía
                if (linea.trim().isEmpty()) {
                    continue;
                }

                // * Usamos un contador para conocer la cantidad de filas del archivo Compras.txt
                contador++;
            }

            // * Cerramos el buffered
            datosPorLinea.close();

            return contador;

        } catch (Exception e) {
            // * Se retorna un 0 en caso de error para que no se generen filas con errores
            return 0;
        }

    }

    // ? Esta función retorna el modelo, la talla, la cantidad y el valor unitario de cada producto según la iteración actual del ciclo en que se llama esta función
    public String[] retornaModeloTallaCantidadVlunitario(int numeroIteracion) {

        try {
            // * Declaramos las variables
            String linea = "";
            String[] datosSeparadosCompraActual = {};
            String[] modelo = {};
            int contador = 0;

            // * Leemos el archivo
            BufferedReader datosPorLinea = new BufferedReader(new FileReader("Datos/Compras.txt"));

            // * Ignoramos la primera linea
            datosPorLinea.readLine();

            // * Recorremos el archivo
            while ((linea = datosPorLinea.readLine()) != null) {

                // * Validamos que la linea no esté vacía
                if (linea.trim().isEmpty()) {
                    continue;
                }

                // * Separamos cada dato de una linea en un arreglo
                datosSeparadosCompraActual = linea.trim().split("\\|");

                // * Si el contador coincide con la iteración, significa que estamos en el producto que le sigue consecutivamente al anterior
                if (contador == numeroIteracion) {
                    modelo = new String[] { datosSeparadosCompraActual[0], datosSeparadosCompraActual[1],
                            datosSeparadosCompraActual[2], datosSeparadosCompraActual[3] };

                    break;
                }

                // * Agregamos 1 al contador
                contador++;

            }

            // * Cerramos el buffered
            datosPorLinea.close();

            return modelo;

        } catch (Exception e) {
            return new String[] { "Error en la función retornaModeloCantidad: " + e.getMessage() };
        }

    }

    // ? Esta función retorna el número de la factura más reciente + 1 para así asignar correctamente el nuevo número de la factura a generar
    public String numeroActualFactura() {

        try {

            // * Ubicamos el archivo Facturas.txt
            String rutaArchivo = "Datos/Facturas.txt"; 
            File archivo = new File(rutaArchivo);
            FileReader archivoALeer = new FileReader(archivo);
            BufferedReader lector = new BufferedReader(archivoALeer);
            int contador = 0;

            // * Verificamos si existe el archivo
            if (!archivo.exists()) {
                lector.close();
                throw new Exception("El archivo no existe");
            }

            // * Contamos la cantidad de lineas que hay en el archivo para conocer cual seria el número de factura próximo a generar
            while (lector.readLine() != null) {
                contador++;
            }
            
            // * Cerramos el buffered
            lector.close();

            return String.valueOf(contador);

        } catch (Exception e) {
            return "Error en la función numeroActualFactura: " + e.getMessage();
        }
    }

    // ? Esta función retorna la fecha en el momento que se genera la factura de manera automática
    public String retornaFechaActual() {

        try {
            String fechaFormateada = "";

            // * Obtenemos la fecha actual
            LocalDate fechaActual = LocalDate.now();

            // * La formateamos en DD/MM/AAAA
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            fechaFormateada = fechaActual.format(formato);

            return fechaFormateada;

        } catch (Exception e) {
            return "Error en la función retornaFechaActual: " + e.getMessage();
        }

    }

    // ? Esta función retorna un valor booleano que me sirve para validar si la cédula digitada por el usuario existe en la base de datos
    public boolean cedulaEstaRegistrada(String cedula) {

        try {

            // * Declaramos las variables
            String linea = "";
            boolean existe = false;
            String[] datosSeparadosCuentaActual = {};

            // * Leemos el archivo 
            BufferedReader datosPorLinea = new BufferedReader(new FileReader("Datos/Cuentas.txt"));

            // * Ignoramos la linea del header
            datosPorLinea.readLine();

            // * Recorremos el archivo
            while ((linea = datosPorLinea.readLine()) != null) {

                // * Validamos que la linea no esté vacía
                if (linea.trim().isEmpty()) {
                    continue;
                }

                // * Separamos cada dato de una linea en un arreglo
                datosSeparadosCuentaActual = linea.trim().split("\\|");

                // * Verificamos si hay al menos un campo y comparamos con la cédula
                if (datosSeparadosCuentaActual.length > 0 && datosSeparadosCuentaActual[0].equals(cedula)) {
                    existe = true;
                    break;
                }

            }

            // * Cerramos el buffered
            datosPorLinea.close();

            return existe;

        } catch (Exception e) {
            return false;
        }

    }

    // ? Esta función retorna un double con el stock del producto actual, con el fin de poder hacerse validaciones y realizar su modificación correctamente
    public double retornaStockProducto(String modeloActual, String tallaActual) {

        try {

            // * Declaramos las variables
            String linea = "";
            String[] datosSeparadosProductoActual = {};
            Double stock = 0.0;

            // * Leemos el archivo
            BufferedReader datosPorLinea = new BufferedReader(new FileReader("Datos/Productos.txt"));

            // * Ignoramos la linea del header
            datosPorLinea.readLine();

            // * Recorremos el archivo

            while ((linea = datosPorLinea.readLine()) != null) {

                // * Validamos que la linea no esté vacía
                if (linea.trim().isEmpty()) {
                    continue;
                }

                // * Separamos cada dato de una linea en un arreglo
                datosSeparadosProductoActual = linea.trim().split("\\|");

                // * Si el modelo coincide con el de la linea, sale del ciclo
                if (modeloActual.equalsIgnoreCase(datosSeparadosProductoActual[1])
                        && tallaActual.equalsIgnoreCase(datosSeparadosProductoActual[3])) {

                    stock = Double.parseDouble(datosSeparadosProductoActual[6]);

                    break;
                }

            }

            // * Cerramos el buffered
            datosPorLinea.close();

            return stock;

        } catch (Exception e) {
            return -999;
        }

    }

}

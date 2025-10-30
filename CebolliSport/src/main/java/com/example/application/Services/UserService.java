package com.example.application.Services;

import java.io.*;
import java.util.List;
import java.util.ArrayList;


//Definicion de la clase UserService
//Esta clase se encarga de la gestion de cuentas de usuario
public class UserService {
    
    List<Integer> indices = new ArrayList<>();
    String cedula = "";
    String nombre = "";
    String correo = "";
    String telefono = "";
    String rolUsuario = "";


    //Constructor1 de la clase UserService
    //Este constructor se utiliza para crear el objeto de la clase UserService previa a la indexacion de las filas
    public UserService(String cedula, String nombre, String correo, String telefono, String rol) {

        this.cedula = cedula;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.rolUsuario = rol;
    }


    //Funcion encargada de crear cuentas, guardando los datos en un archivo de texto
    //con el formato: "cedula","nombre","correo","telefono","rol"    Notese que el indice no se escribe en el archivo
    public void creacionCuenta(String cedula, String nombre, String correo, String telefono, String rol) {

        FileWriter escritor = null;
        PrintWriter linea = null;
        
        try {
            
            String rutaRelativa = "Datos/Cuentas.txt"; 
            File archivoCuentas = new File(rutaRelativa);
            escritor = new FileWriter(archivoCuentas,true);
            linea = new PrintWriter(escritor);
            
            linea.println(cedula + "|" + nombre + "|" + correo + "|" + telefono + "|" + rol);
            
        } catch (IOException io) {
            System.out.println("Error al escribir en el archivo: " + io.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
        } finally {
            if (linea != null) {
                linea.close();
                
            }
        }
    }
    

    //Funcion encargada de leer el archivo de cuentas y devolver una lista con los datos de cada cuenta
    //con el formato: "cedula"|"nombre"|"correo"|"telefono"|"rol"   Aquí tampoco se escribe el indice
    public static List<String> lecturaCuentas(List<Integer> indices) {


        List<String> cuentas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("Datos/Cuentas.txt"))) {
            String linea;
            int contador = 1;

            br.readLine(); // ? Se descarta el header.
            while ((linea = br.readLine()) != null) {
                if (!indices.contains(contador)) {
                    cuentas.add(linea);
                }
                contador++;
            }
            
        } catch (FileNotFoundException fnf) {
            System.out.println("Archivo no encontrado: " + fnf.getMessage());
        } catch (IOException io) {
            System.out.println("Error al leer el archivo: " + io.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }


        // Sobrescribir el archivo con las cuentas a conservar
        try (PrintWriter writer = new PrintWriter(new FileWriter("Datos/Cuentas.txt", false))) {

            // ? Se imprime el header.
            writer.println("CEDULA | NOMBRE | CORREO | CELULAR | CLIENTE");

            for (String cuenta : cuentas) {
                writer.println(cuenta);
            }
        } catch (FileNotFoundException fnf) {
            System.out.println("Archivo no encontrado: " + fnf.getMessage());
        } catch (IOException io) {
            System.out.println("Error al escribir el archivo: " + io.getMessage());
        }

        return cuentas;
    }





    public void edicionCuentas (int indice, UserService modificacion) {


        List<String> cuentas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("Datos/Cuentas.txt"))) {
            String linea;
            int contador = 1;

            br.readLine(); // ? Se descarta el header.
            while ((linea = br.readLine()) != null) {
                if (indice+1 != contador) {
                    cuentas.add(linea);
                
                } else {
                    cuentas.add(modificacion.getCedula() + "|" + 
                    modificacion.getNombre() + "|" + 
                    modificacion.getCorreo() + "|" + 
                    modificacion.getTelefono() + "|" + 
                    modificacion.getRol());
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


        // Sobrescribir el archivo con las cuentas a conservar
        try (PrintWriter writer = new PrintWriter(new FileWriter("Datos/Cuentas.txt", false))) {

            // ? Se imprime el header.
            writer.println("CEDULA | NOMBRE | CORREO | CELULAR | CLIENTE");

            for (String cuenta : cuentas) {
                writer.println(cuenta);
            }

        } catch (FileNotFoundException fnf) {
            System.out.println("Archivo no encontrado: " + fnf.getMessage());
        } catch (IOException io) {
            System.out.println("Error al escribir el archivo: " + io.getMessage());
        }

    }

    


    //Getters y Setters de la clase UserService

    public String getCedula() {
        return cedula; 
    }

    public String getNombre() {
        return nombre; 
    }

    public String getCorreo() {
        return correo; 
    }

    public String getTelefono() {
        return telefono; 
    }

    public String getRol() {
        return rolUsuario; 
    }


}
package com.example.application.Services;

// Definimos la clase ProductoCarrito que representa un producto en el carrito de compras
public class ProductoCarrito_Clase {
    private String modelo;
    private String talla;
    private int unidades;
    private double precioTotal;

    // Constructor de la clase ProductoCarrito
    public ProductoCarrito_Clase(String modelo,String talla,  int unidades, double precioTotal) {
        this.modelo = modelo;
        this.talla = talla;
        this.unidades = unidades;
        this.precioTotal = precioTotal;
    }

    // Getters
    public String getModelo() {
        return modelo;
    }

    public String getTalla() {
        return talla;
    }

    public int getUnidades() {
        return unidades;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    // Setters
    public void setUnidades(int unidades) {
        this.unidades = unidades;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }
}
package com.example.application.Services;

// Definimos la clase Producto que representa un producto en el sistema
public class Producto_Clase {
    private String image;
    private String model;
    private String brand;
    private String size;
    private String color;
    private String type;
    private int stock;
    private String genero;
    private double price;
    
    // Constructor de la clase Producto
    public Producto_Clase(String image, String model, String brand, String size, String color, String type, int stock,String genero, double price) {
        this.image = image;
        this.model = model;
        this.brand = brand;
        this.size = size;
        this.color = color;
        this.type = type;
        this.stock = stock;
        this.genero = genero;
        this.price = price;
    }

    // Getters
    public String getImage() {
        return image;
    }

    public String getModel() {
        return model;
    }

    public String getBrand() {
        return brand;
    }

    public String getSize() {
        return size;
    }

    public String getColor() {
        return color;
    }

    public String getType() {
        return type;
    }
    public int getStock() {
        return stock;
    }
    public String getGenero() {
        return genero;
    }
    public double getPrice() {
        return price;
    }

    // Setters
    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}


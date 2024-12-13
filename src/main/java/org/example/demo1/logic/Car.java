package org.example.demo1.logic;

public class Car {
    private String plate;
    private String brand;
    private int year;
    private String color;
    private boolean running;

    public Car(String plate, String brand, Integer year, String color) {
        this.plate = plate;
        this.brand = brand;
        this.year = year;
        this.color = color;
        this.running = false;
    }

    public void start() {
        running = true;
    }

    public void stop() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
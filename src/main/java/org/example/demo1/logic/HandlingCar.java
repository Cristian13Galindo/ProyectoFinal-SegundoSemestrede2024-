package org.example.demo1.logic;

import java.util.ArrayList;
import java.util.List;

public class HandlingCar {
    private List<Car> cars;

    public HandlingCar() {
        cars = new ArrayList<>();
    }

    public boolean save(Car car) {
        if (findByPlate(car.getPlate()) == null) {
            cars.add(car);
            return true;
        }
        return false;
    }

    public Car findByPlate(String plate) {
        for (Car car : cars) {
            if (car.getPlate().equals(plate)) {
                return car;
            }
        }
        return null;
    }

    public List<Car> getAll() {
        return new ArrayList<>(cars);
    }
}
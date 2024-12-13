package org.example.demo1.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HandlingCarTest {
    private HandlingCar handlingCar;

    @BeforeEach
    void setUp() {
        handlingCar = new HandlingCar();
    }

    @Test
    void findByPlate() {
        assertNull(handlingCar.findByPlate("HPV990"));
        handlingCar.save(new Car("JIA764", "BMW", (int) 1987, "Verde"));
        handlingCar.save(new Car("NOT560", "Nissan", (int) 2000, "Azul"));
        assertNull(handlingCar.findByPlate("HPV990"));
        assertNotNull(handlingCar.findByPlate("NOT560"));
        assertEquals("NOT560", handlingCar.findByPlate("NOT560").getPlate());
    }

    @Test
    void save() {
        assertTrue(handlingCar.save(new Car("NOT560", "Nissan", (int) 1987, "Verde")));
        assertTrue(handlingCar.save(new Car("JIA764", "Nissan", (int) 1987, "Verde")));
        assertTrue(handlingCar.save(new Car("PQR534", "Nissan", (int) 1987, "Verde")));
        assertFalse(handlingCar.save(new Car("NOT560", "BMW", (int) 1987, "Verde")));
        assertEquals(3, handlingCar.getAll().size());
    }

    @Test
    void getAll() {
        handlingCar.save(new Car("ABC123", "Toyota", (int) 2020, "Red"));
        handlingCar.save(new Car("XYZ789", "Honda", (int) 2019, "Blue"));
        List<Car> cars = handlingCar.getAll();
        assertEquals(2, cars.size(), "There should be two cars in the list");
        assertEquals("ABC123", cars.get(0).getPlate());
        assertEquals("XYZ789", cars.get(1).getPlate());
    }
}
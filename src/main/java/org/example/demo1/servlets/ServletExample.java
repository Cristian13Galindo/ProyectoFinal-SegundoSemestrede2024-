package org.example.demo1.servlets;

import java.io.*;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.demo1.logic.Car;
import org.example.demo1.logic.HandlingCar;

@WebServlet(name = "ServletExample", value = "/ServletExample")
public class ServletExample extends HttpServlet {
    private HandlingCar handlingCar;
    private Gson gson;

    public void init() throws ServletException {
        super.init();
        handlingCar = new HandlingCar();
        gson = new Gson();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String opt = request.getParameter("option");
        try (PrintWriter out = response.getWriter()) {
            switch (opt) {
                case "1":
                    out.println(gson.toJson(handlingCar.getAll()));
                    break;
                case "2":
                    String plate = request.getParameter("plate");
                    out.println(gson.toJson(handlingCar.findByPlate(plate)));
                    break;
                case "3":
                    String plate3 = request.getParameter("plate");
                    String brand = request.getParameter("brand");
                    Integer year = Integer.valueOf(request.getParameter("year"));
                    String color = request.getParameter("color");
                    Car car = new Car(plate3, brand, year, color);
                    handlingCar.save(car);
                    out.println("Carro agregado exitosamente");
                    break;
                default:
                    out.println("Opción no válida");
                    break;
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader reader = request.getReader();
        Car car = gson.fromJson(reader, Car.class);
        try (PrintWriter out = response.getWriter()) {
            if(handlingCar.save(car)){
            out.println(gson.toJson(handlingCar.findByPlate(car.getPlate())));
        }else{
                out.println("Ya existe un carro con esa placa");
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader reader = request.getReader();
        Car car = gson.fromJson(reader, Car.class);
        try (PrintWriter out = response.getWriter()) {
            Car carFound = handlingCar.findByPlate(car.getPlate());
            if (carFound != null) {
                carFound.setBrand(car.getBrand());
                carFound.setYear(car.getYear());
                carFound.setColor(car.getColor());
                out.println(gson.toJson(carFound));
            } else {
                out.println("No se encontró el carro");
            }
        }
    }

    public void destroy() {
    }
}
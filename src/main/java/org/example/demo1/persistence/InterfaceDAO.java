package org.example.demo1.persistence;

import java.util.List;

public interface InterfaceDAO<T> {
    void save(T object); // Crear o guardar un objeto
    T findById(String id); // Buscar un objeto por ID
    List<T> findAll(); // Obtener todos los objetos
    void update(T object); // Actualizar un objeto existente
    void delete(String id); // Eliminar un objeto por ID
}

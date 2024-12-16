package co.uptc.edu.servlets;

import co.uptc.edu.model.Category;
import co.uptc.edu.util.MongoDBUtil;
import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "CategoryServlet", urlPatterns = {"/categories"})
public class CategoryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private MongoCollection<Document> categoryCollection;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        categoryCollection = MongoDBUtil.getDatabase().getCollection("categories");
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Listar todas las categorías
        List<Category> categories = new ArrayList<>();
        for (Document doc : categoryCollection.find()) {
            Category category = new Category();
            category.setCategoryId(doc.getString("categoryId"));
            category.setName(doc.getString("name"));
            category.setDescription(doc.getString("description"));
            category.setCreatedAt(doc.getDate("createdAt"));
            category.setUpdatedAt(doc.getDate("updatedAt"));
            categories.add(category);
        }

        // Responder al cliente
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(gson.toJson(categories));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Obtener parámetros del formulario
        String name = request.getParameter("name");
        String description = request.getParameter("description");

        // Validar parámetros
        if (name == null || name.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "El nombre es obligatorio.");
            return;
        }

        // Verificar si la categoría ya existe
        Document existingCategory = categoryCollection.find(Filters.eq("name", name)).first();
        if (existingCategory != null) {
            response.sendError(HttpServletResponse.SC_CONFLICT, "La categoría ya existe.");
            return;
        }

        // Generar un ID único para la categoría
        String categoryId = UUID.randomUUID().toString();

        // Crear objeto Category
        Category category = new Category(categoryId, name, description, new Date(), new Date());

        // Convertir a Document
        Document doc = new Document("categoryId", category.getCategoryId())
                .append("name", category.getName())
                .append("description", category.getDescription())
                .append("createdAt", category.getCreatedAt())
                .append("updatedAt", category.getUpdatedAt());

        // Insertar en la colección
        try {
            InsertOneResult result = categoryCollection.insertOne(doc);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            // Responder con el ID de la categoría creada
            String jsonResponse = "{\"status\":\"success\", \"categoryId\":\"" + categoryId + "\"}";
            response.getWriter().write(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al crear la categoría.");
        }
    }
}

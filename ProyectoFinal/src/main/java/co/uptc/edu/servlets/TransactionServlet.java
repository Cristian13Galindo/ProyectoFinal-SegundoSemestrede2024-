// src/main/java/co/uptc/edu/servlets/TransactionServlet.java
package co.uptc.edu.servlets;

import co.uptc.edu.util.MongoDBUtil;
import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

@WebServlet(name = "TransactionServlet", urlPatterns = {"/transactions/*"})
public class TransactionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private MongoCollection<Document> transactionCollection;
    private MongoCollection<Document> budgetCollection;
    private MongoCollection<Document> categoryCollection;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        transactionCollection = MongoDBUtil.getDatabase().getCollection("transactions");
        budgetCollection = MongoDBUtil.getDatabase().getCollection("budgets");
        categoryCollection = MongoDBUtil.getDatabase().getCollection("categories");
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String budgetId = request.getParameter("budgetId");
            if (budgetId == null || budgetId.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"status\":\"error\", \"message\":\"Debe especificar el budgetId como parte de la URL.\"}");
                return;
            }

            // Filtrar transacciones por budgetId
            List<Document> transactions = transactionCollection.find(Filters.eq("budgetId", budgetId)).into(new ArrayList<>());

            // Convertir las transacciones a un formato JSON amigable
            List<Map<String, Object>> transactionList = new ArrayList<>();
            for (Document doc : transactions) {
                Map<String, Object> transactionMap = new HashMap<>();
                transactionMap.put("transactionId", doc.getString("transactionId"));
                transactionMap.put("budgetId", doc.getString("budgetId"));
                transactionMap.put("categoryId", doc.getString("categoryId"));
                transactionMap.put("amount", doc.getDouble("amount"));
                transactionMap.put("description", doc.getString("description"));
                transactionMap.put("date", doc.getDate("date"));

                // Obtener el nombre de la categoría
                Document category = categoryCollection.find(Filters.eq("categoryId", doc.getString("categoryId"))).first();
                if (category != null) {
                    transactionMap.put("categoryName", category.getString("name"));
                }

                transactionList.add(transactionMap);
            }

            response.getWriter().write(new Gson().toJson(transactionList));
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"status\":\"error\", \"message\":\"Error interno del servidor.\"}");
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // Leer el cuerpo de la solicitud
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String jsonData = sb.toString();

            // Validar que el cuerpo no esté vacío
            if (jsonData == null || jsonData.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"status\":\"error\", \"message\":\"El cuerpo de la solicitud está vacío.\"}");
                return;
            }

            // Convertir el JSON a un mapa
            Map<String, Object> requestData = gson.fromJson(jsonData, Map.class);

            // Validar campos obligatorios
            if (!requestData.containsKey("budgetId") || !requestData.containsKey("categoryId")
                    || !requestData.containsKey("amount") || !requestData.containsKey("description")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"status\":\"error\", \"message\":\"Datos obligatorios faltantes.\"}");
                return;
            }

            String budgetId = requestData.get("budgetId").toString();
            String categoryId = requestData.get("categoryId").toString();
            double amount = ((Number) requestData.get("amount")).doubleValue();
            String description = requestData.get("description").toString();

            // Verificar que el presupuesto existe
            Document budget = budgetCollection.find(Filters.eq("budgetId", budgetId)).first();
            if (budget == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"status\":\"error\", \"message\":\"Presupuesto no encontrado.\"}");
                return;
            }

            // Verificar que la categoría existe dentro del presupuesto
            List<Document> categories = (List<Document>) budget.get("categories");
            Document category = categories.stream()
                    .filter(cat -> categoryId.equals(cat.getString("categoryId")))
                    .findFirst()
                    .orElse(null);

            if (category == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"status\":\"error\", \"message\":\"Categoría no encontrada en el presupuesto.\"}");
                return;
            }

            // Crear la transacción
            String transactionId = UUID.randomUUID().toString();
            Document transactionDoc = new Document()
                    .append("transactionId", transactionId)
                    .append("budgetId", budgetId)
                    .append("categoryId", categoryId)
                    .append("amount", amount)
                    .append("description", description)
                    .append("date", new Date());

            // Insertar la transacción en la base de datos
            InsertOneResult result = transactionCollection.insertOne(transactionDoc);

            if (result.getInsertedId() != null) {
                // Actualizar el gasto en la categoría del presupuesto
                double spentAmount = ((Number) category.get("spentAmount")).doubleValue();
                spentAmount += amount;

                category.put("spentAmount", spentAmount);
                budgetCollection.updateOne(
                        Filters.eq("budgetId", budgetId),
                        new Document("$set", new Document("categories", categories))
                );

                response.getWriter().write("{\"status\":\"success\", \"transactionId\":\"" + transactionId + "\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"status\":\"error\", \"message\":\"Error al registrar la transacción.\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"status\":\"error\", \"message\":\"Error interno del servidor.\", \"details\":\"" + e.getMessage() + "\"}");
        }
    }
}

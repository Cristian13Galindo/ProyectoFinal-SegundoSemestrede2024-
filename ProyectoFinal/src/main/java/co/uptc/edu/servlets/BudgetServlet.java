// src/main/java/co/uptc/edu/servlets/BudgetServlet.java
package co.uptc.edu.servlets;

import co.uptc.edu.util.MongoDBUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

@WebServlet(name = "BudgetServlet", urlPatterns = {"/budgets/*"})
public class BudgetServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private MongoCollection<Document> budgetCollection;
    private MongoCollection<Document> categoryCollection;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        budgetCollection = MongoDBUtil.getDatabase().getCollection("budgets");
        categoryCollection = MongoDBUtil.getDatabase().getCollection("categories");
        gson = new Gson();
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
            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> requestData = gson.fromJson(jsonData, type);

            // Validar campos obligatorios
            if (!requestData.containsKey("periodType") || !requestData.containsKey("periodStart")
                    || !requestData.containsKey("periodEnd") || !requestData.containsKey("totalAmount")
                    || !requestData.containsKey("categories")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"status\":\"error\", \"message\":\"Datos obligatorios faltantes.\"}");
                return;
            }

            // Validar categorías
            List<Map<String, Object>> categories = (List<Map<String, Object>>) requestData.get("categories");
            if (categories == null || categories.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"status\":\"error\", \"message\":\"Se requiere al menos una categoría.\"}");
                return;
            }

            for (Map<String, Object> category : categories) {
                if (!category.containsKey("categoryId") || !category.containsKey("name") ||
                        !category.containsKey("allocatedAmount") || !category.containsKey("threshold")) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"status\":\"error\", \"message\":\"Datos de categoría incompletos.\"}");
                    return;
                }
            }

            // Obtener datos del presupuesto
            String periodType = requestData.get("periodType").toString();
            String periodStartStr = requestData.get("periodStart").toString();
            String periodEndStr = requestData.get("periodEnd").toString();

            // Manejo seguro de totalAmount
            Number totalAmountNumber = (Number) requestData.get("totalAmount");
            double totalAmount = totalAmountNumber.doubleValue();

            // Convertir fechas
            Date periodStart;
            Date periodEnd;
            try {
                periodStart = java.sql.Date.valueOf(periodStartStr);
                periodEnd = java.sql.Date.valueOf(periodEndStr);
            } catch (IllegalArgumentException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"status\":\"error\", \"message\":\"Formato de fecha inválido. Use 'yyyy-mm-dd'.\"}");
                return;
            }

            // Obtener userId (esto debería obtenerse de la sesión o autenticación; aquí se usa un ID fijo para ejemplo)
            String userId = "user-unique-id"; // Reemplaza con el ID real del usuario

            // Generar un budgetId único
            String budgetId = UUID.randomUUID().toString();

            // Construir lista de categorías Document
            List<Document> categoryDocs = new ArrayList<>();
            for (Map<String, Object> category : categories) {
                String categoryIdStr = category.get("categoryId").toString();
                if (categoryIdStr == null || categoryIdStr.equals("undefined")) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"status\":\"error\", \"message\":\"categoryId es requerido en cada categoría.\"}");
                    return;
                }

                // Verificar que la categoría existe
                Document existingCategory = categoryCollection.find(Filters.eq("categoryId", categoryIdStr)).first();
                if (existingCategory == null) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"status\":\"error\", \"message\":\"categoryId inválido: " + categoryIdStr + "\"}");
                    return;
                }

                String name = category.get("name").toString();

                // Manejo seguro de allocatedAmount y threshold
                Number allocatedAmountNumber = (Number) category.get("allocatedAmount");
                double allocatedAmount = allocatedAmountNumber.doubleValue();

                Number thresholdNumber = (Number) category.get("threshold");
                double threshold = thresholdNumber.doubleValue();

                Document catDoc = new Document()
                        .append("categoryId", categoryIdStr)
                        .append("name", name)
                        .append("allocatedAmount", allocatedAmount)
                        .append("spentAmount", 0.0) // Inicializar gastado en 0
                        .append("threshold", threshold);
                categoryDocs.add(catDoc);
            }

            // Crear documento de presupuesto
            Document budgetDoc = new Document()
                    .append("budgetId", budgetId)
                    .append("userId", userId)
                    .append("periodType", periodType)
                    .append("periodStart", periodStart)
                    .append("periodEnd", periodEnd)
                    .append("totalAmount", totalAmount)
                    .append("categories", categoryDocs)
                    .append("createdAt", new Date())
                    .append("updatedAt", new Date());

            // Insertar en la colección
            InsertOneResult result = budgetCollection.insertOne(budgetDoc);

            if (result.getInsertedId() != null) {
                Map<String, Object> responseMap = new HashMap<>();
                responseMap.put("status", "success");
                responseMap.put("budgetId", budgetId);
                response.getWriter().write(gson.toJson(responseMap));
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"status\":\"error\", \"message\":\"Error al guardar el presupuesto.\"}");
            }

        } catch (Exception e) {
            e.printStackTrace(); // Asegura que la traza se imprime en los logs del servidor
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            // Incluir detalles del error para diagnóstico (solo durante desarrollo)
            response.getWriter().write("{\"status\":\"error\", \"message\":\"Error interno del servidor.\", \"details\":\"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Obtener el path info para determinar si se solicita un presupuesto específico
        String pathInfo = request.getPathInfo(); // Puede ser null o /{budgetId}

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                // GET /budgets - Obtener todos los presupuestos
                List<Document> budgets = budgetCollection.find().into(new ArrayList<>());

                // Convertir documentos a objetos JSON con estructura adecuada
                List<Map<String, Object>> budgetList = new ArrayList<>();
                for (Document doc : budgets) {
                    Map<String, Object> budgetMap = new HashMap<>();
                    budgetMap.put("budgetId", doc.getString("budgetId"));
                    budgetMap.put("userId", doc.getString("userId"));
                    budgetMap.put("periodType", doc.getString("periodType"));

                    // Manejo seguro de fechas
                    Date periodStartDate = doc.getDate("periodStart");
                    Date periodEndDate = doc.getDate("periodEnd");
                    Date createdAtDate = doc.getDate("createdAt");
                    Date updatedAtDate = doc.getDate("updatedAt");

                    budgetMap.put("periodStart", periodStartDate != null ? periodStartDate.toString() : null);
                    budgetMap.put("periodEnd", periodEndDate != null ? periodEndDate.toString() : null);
                    budgetMap.put("totalAmount", getDoubleFromDocument(doc, "totalAmount"));
                    budgetMap.put("createdAt", createdAtDate != null ? createdAtDate.toString() : null);
                    budgetMap.put("updatedAt", updatedAtDate != null ? updatedAtDate.toString() : null);

                    // Procesar categorías
                    List<Document> categories = (List<Document>) doc.get("categories");
                    List<Map<String, Object>> categoryList = new ArrayList<>();
                    for (Document cat : categories) {
                        Map<String, Object> catMap = new HashMap<>();
                        catMap.put("categoryId", cat.getString("categoryId"));
                        catMap.put("name", cat.getString("name"));
                        catMap.put("allocatedAmount", getDoubleFromDocument(cat, "allocatedAmount"));
                        catMap.put("spentAmount", getDoubleFromDocument(cat, "spentAmount"));
                        catMap.put("threshold", getDoubleFromDocument(cat, "threshold"));
                        categoryList.add(catMap);
                    }
                    budgetMap.put("categories", categoryList);

                    budgetList.add(budgetMap);
                }

                // Enviar respuesta
                response.getWriter().write(gson.toJson(budgetList));
            } else {
                // GET /budgets/{budgetId} - Obtener un presupuesto específico
                String[] splits = pathInfo.split("/");
                if (splits.length != 2) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"status\":\"error\", \"message\":\"URL inválida.\"}");
                    return;
                }

                String budgetId = splits[1];
                Document budgetDoc = budgetCollection.find(Filters.eq("budgetId", budgetId)).first();

                if (budgetDoc == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{\"status\":\"error\", \"message\":\"Presupuesto no encontrado.\"}");
                    return;
                }

                // Convertir el documento a JSON con estructura adecuada
                Map<String, Object> budgetMap = new HashMap<>();
                budgetMap.put("budgetId", budgetDoc.getString("budgetId"));
                budgetMap.put("userId", budgetDoc.getString("userId"));
                budgetMap.put("periodType", budgetDoc.getString("periodType"));

                // Manejo seguro de fechas
                Date periodStartDate = budgetDoc.getDate("periodStart");
                Date periodEndDate = budgetDoc.getDate("periodEnd");
                Date createdAtDate = budgetDoc.getDate("createdAt");
                Date updatedAtDate = budgetDoc.getDate("updatedAt");

                budgetMap.put("periodStart", periodStartDate != null ? periodStartDate.toString() : null);
                budgetMap.put("periodEnd", periodEndDate != null ? periodEndDate.toString() : null);
                budgetMap.put("totalAmount", getDoubleFromDocument(budgetDoc, "totalAmount"));
                budgetMap.put("createdAt", createdAtDate != null ? createdAtDate.toString() : null);
                budgetMap.put("updatedAt", updatedAtDate != null ? updatedAtDate.toString() : null);

                // Procesar categorías
                List<Document> categories = (List<Document>) budgetDoc.get("categories");
                List<Map<String, Object>> categoryList = new ArrayList<>();
                for (Document cat : categories) {
                    Map<String, Object> catMap = new HashMap<>();
                    catMap.put("categoryId", cat.getString("categoryId"));
                    catMap.put("name", cat.getString("name"));
                    catMap.put("allocatedAmount", getDoubleFromDocument(cat, "allocatedAmount"));
                    catMap.put("spentAmount", getDoubleFromDocument(cat, "spentAmount"));
                    catMap.put("threshold", getDoubleFromDocument(cat, "threshold"));
                    categoryList.add(catMap);
                }
                budgetMap.put("categories", categoryList);

                // Enviar respuesta
                response.getWriter().write(gson.toJson(budgetMap));
            }
        } catch (Exception e) {
            e.printStackTrace(); // Asegura que la traza se imprime en los logs del servidor
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            // Incluir detalles del error para diagnóstico (solo durante desarrollo)
            response.getWriter().write("{\"status\":\"error\", \"message\":\"Error interno del servidor.\", \"details\":\"" + e.getMessage() + "\"}");
        }
    }

    /**
     * Método auxiliar para obtener un valor double de un documento,
     * manejando posibles tipos Integer y Double.
     */
    private double getDoubleFromDocument(Document doc, String key) {
        Object value = doc.get(key);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return 0.0; // Valor por defecto o podrías lanzar una excepción
    }

    // Otros métodos (doPut, doDelete, etc.) si existen...
}

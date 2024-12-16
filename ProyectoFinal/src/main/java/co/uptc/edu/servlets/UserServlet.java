
package co.uptc.edu.servlets;

import co.uptc.edu.model.User;
import co.uptc.edu.util.MongoDBUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.mindrot.jbcrypt.BCrypt;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(name = "UserServlet", urlPatterns = {"/users"})
public class UserServlet extends HttpServlet {
    private MongoCollection<Document> userCollection;
    private Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        userCollection = MongoDBUtil.getUserCollection();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Leer el cuerpo de la petición
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null){
            sb.append(line);
        }
        String requestBody = sb.toString();

        // Parsear JSON
        JsonObject jsonObject;
        try {
            jsonObject = JsonParser.parseString(requestBody).getAsJsonObject();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JsonObject res = new JsonObject();
            res.addProperty("status", "error");
            res.addProperty("message", "Formato JSON inválido.");
            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(res));
            return;
        }

        if (!jsonObject.has("action")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JsonObject res = new JsonObject();
            res.addProperty("status", "error");
            res.addProperty("message", "Acción no especificada.");
            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(res));
            return;
        }

        String action = jsonObject.get("action").getAsString();

        switch (action) {
            case "register":
                handleRegister(jsonObject, response);
                break;
            case "login":
                handleLogin(jsonObject, response);
                break;
            default:
                // Acción no reconocida
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                JsonObject res = new JsonObject();
                res.addProperty("status", "error");
                res.addProperty("message", "Acción no reconocida.");
                response.setContentType("application/json");
                response.getWriter().write(gson.toJson(res));
                break;
        }
    }

    private void handleRegister(JsonObject jsonObject, HttpServletResponse response) throws IOException {
        // Extraer datos del JSON
        String username = jsonObject.has("username") ? jsonObject.get("username").getAsString().trim() : "";
        String email = jsonObject.has("email") ? jsonObject.get("email").getAsString().trim() : "";
        String password = jsonObject.has("password") ? jsonObject.get("password").getAsString() : "";

        // Validaciones básicas
        if(username.isEmpty() || email.isEmpty() || password.isEmpty()){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JsonObject res = new JsonObject();
            res.addProperty("status", "error");
            res.addProperty("message", "Todos los campos son obligatorios.");
            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(res));
            return;
        }

        // Verificar si el usuario ya existe
        Document existingUser = userCollection.find(new Document("username", username)).first();
        if(existingUser != null){
            // Usuario ya existe
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            JsonObject res = new JsonObject();
            res.addProperty("status", "error");
            res.addProperty("message", "El nombre de usuario ya está en uso.");
            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(res));
            return;
        }

        // Hashear la contraseña
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // Crear el usuario
        User newUser = new User(username, email, hashedPassword);
        Document userDoc = new Document("username", newUser.getUsername())
                .append("email", newUser.getEmail())
                .append("password", newUser.getPasswordHash()) // Uso correcto de getPasswordHash()
                .append("createdAt", new java.util.Date());

        // Insertar en la colección
        userCollection.insertOne(userDoc);

        // Responder con éxito
        response.setStatus(HttpServletResponse.SC_OK);
        JsonObject res = new JsonObject();
        res.addProperty("status", "success");
        res.addProperty("message", "Usuario registrado exitosamente.");
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(res));
    }

    private void handleLogin(JsonObject jsonObject, HttpServletResponse response) throws IOException {
        // Extraer datos del JSON
        String username = jsonObject.has("username") ? jsonObject.get("username").getAsString().trim() : "";
        String password = jsonObject.has("password") ? jsonObject.get("password").getAsString() : "";

        // Validaciones básicas
        if(username.isEmpty() || password.isEmpty()){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JsonObject res = new JsonObject();
            res.addProperty("status", "error");
            res.addProperty("message", "Usuario y contraseña son obligatorios.");
            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(res));
            return;
        }

        // Buscar el usuario
        Document userDoc = userCollection.find(new Document("username", username)).first();
        if(userDoc == null){
            // Usuario no encontrado
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            JsonObject res = new JsonObject();
            res.addProperty("status", "error");
            res.addProperty("message", "Usuario o contraseña incorrectos.");
            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(res));
            return;
        }

        // Verificar la contraseña
        String hashedPassword = userDoc.getString("password");
        if(!BCrypt.checkpw(password, hashedPassword)){
            // Contraseña incorrecta
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            JsonObject res = new JsonObject();
            res.addProperty("status", "error");
            res.addProperty("message", "Usuario o contraseña incorrectos.");
            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(res));
            return;
        }

        // Responder con éxito y nombre de usuario
        response.setStatus(HttpServletResponse.SC_OK);
        JsonObject res = new JsonObject();
        res.addProperty("status", "success");
        res.addProperty("message", "Inicio de sesión exitoso.");
        res.addProperty("username", username);
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(res));
    }

    // Implementación de doGet para manejar peticiones GET
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Responder con error 405 Method Not Allowed
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        JsonObject res = new JsonObject();
        res.addProperty("status", "error");
        res.addProperty("message", "El método GET no está permitido para esta URL.");
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(res));
    }
}

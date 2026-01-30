package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Cliente Socket para operaciones con usuarios
 * 
 * Conecta con el servidor TCP para obtener datos de usuarios
 * desde la base de datos MySQL
 */
public class GetAllUsers {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 6000;
    private static final int TIMEOUT = 5000;
    
    private Gson gson;
    
    public GetAllUsers() {
        this.gson = new Gson();
    }
    
    /**
     * Obtiene todos los usuarios del sistema
     * 
     * @return Lista de usuarios o lista vacía si hay error
     */
    public List<UserData> obtenerTodosUsuarios() {
        return ejecutarComando("LORTU_USERS");
    }
    
    /**
     * Obtiene usuarios por tipo
     * 
     * @param tipoId ID del tipo (1=God, 2=Admin, 3=Profesor, 4=Alumno)
     * @return Lista de usuarios de ese tipo
     */
    public List<UserData> obtenerUsuariosPorTipo(int tipoId) {
        return ejecutarComando("LORTU_USERS_TIPO:" + tipoId);
    }
    
    /**
     * Obtiene un usuario específico por ID
     * 
     * @param userId ID del usuario
     * @return UserData o null si no se encuentra
     */
    public UserData obtenerUsuarioPorId(long userId) {
        Socket socket = null;
        
        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            socket.setSoTimeout(TIMEOUT);
            
            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            String comando = "LORTU_USER:" + userId;
            salida.println(comando);
            
            String respuesta = entrada.readLine();
            
            if (respuesta == null || respuesta.trim().isEmpty()) {
                return null;
            }
            
            JsonObject jsonRespuesta = gson.fromJson(respuesta, JsonObject.class);
            
            if (jsonRespuesta.has("aurkituta") && jsonRespuesta.get("aurkituta").getAsBoolean()) {
                JsonObject userJson = jsonRespuesta.getAsJsonObject("user");
                return jsonToUserData(userJson);
            }
            
            return null;
            
        } catch (Exception e) {
            System.err.println("Error obteniendo usuario por ID: " + e.getMessage());
            return null;
            
        } finally {
            cerrarSocket(socket);
        }
    }
    
    /**
     * Ejecuta un comando que devuelve lista de usuarios
     */
    private List<UserData> ejecutarComando(String comando) {
        List<UserData> usuarios = new ArrayList<>();
        Socket socket = null;
        
        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            socket.setSoTimeout(TIMEOUT);
            
            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            salida.println(comando);
            
            String respuesta = entrada.readLine();
            
            if (respuesta == null || respuesta.trim().isEmpty()) {
                System.err.println("No se recibió respuesta del servidor");
                return usuarios;
            }
            
            JsonObject jsonRespuesta = gson.fromJson(respuesta, JsonObject.class);
            
            if (jsonRespuesta.has("errorea")) {
                System.err.println("Error del servidor: " + jsonRespuesta.get("errorea").getAsString());
                return usuarios;
            }
            
            if (jsonRespuesta.has("users")) {
                JsonArray usersArray = jsonRespuesta.getAsJsonArray("users");
                
                for (int i = 0; i < usersArray.size(); i++) {
                    JsonObject userJson = usersArray.get(i).getAsJsonObject();
                    UserData user = jsonToUserData(userJson);
                    usuarios.add(user);
                }
                
                System.out.println("✓ Cargados " + usuarios.size() + " usuarios desde el servidor");
            }
            
        } catch (SocketTimeoutException e) {
            System.err.println("Timeout conectando al servidor: " + e.getMessage());
            
        } catch (IOException e) {
            System.err.println("Error de conexión: " + e.getMessage());
            
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
            
        } finally {
            cerrarSocket(socket);
        }
        
        return usuarios;
    }
    
    /**
     * Convierte un JsonObject a UserData
     */
    private UserData jsonToUserData(JsonObject userJson) {
        Long id = userJson.get("id").getAsLong();
        String email = userJson.get("email").getAsString();
        String username = userJson.get("username").getAsString();
        String nombre = userJson.has("nombre") && !userJson.get("nombre").isJsonNull() 
                        ? userJson.get("nombre").getAsString() : "";
        String apellidos = userJson.has("apellidos") && !userJson.get("apellidos").isJsonNull() 
                          ? userJson.get("apellidos").getAsString() : "";
        String dni = userJson.has("dni") && !userJson.get("dni").isJsonNull() 
                    ? userJson.get("dni").getAsString() : "";
        Integer tipoId = userJson.get("tipoId").getAsInt();
        String telefono1 = userJson.has("telefono1") && !userJson.get("telefono1").isJsonNull() 
                          ? userJson.get("telefono1").getAsString() : "";
        String argazkiaUrl = userJson.has("argazkiaUrl") && !userJson.get("argazkiaUrl").isJsonNull() 
                            ? userJson.get("argazkiaUrl").getAsString() : null;
        String direccion = userJson.has("direccion") && !userJson.get("direccion").isJsonNull() 
                          ? userJson.get("direccion").getAsString() : "";
        String telefono2 = userJson.has("telefono2") && !userJson.get("telefono2").isJsonNull() 
                          ? userJson.get("telefono2").getAsString() : "";
        
        return new UserData(id, email, username, nombre, apellidos, dni, tipoId, 
                           telefono1, argazkiaUrl, direccion, telefono2);
    }
    
    /**
     * Cierra el socket de forma segura
     */
    private void cerrarSocket(Socket socket) {
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Error al cerrar socket: " + e.getMessage());
            }
        }
    }
    
    /**
     * Clase para almacenar datos de usuario
     */
    public static class UserData {
        private Long id;
        private String email;
        private String username;
        private String nombre;
        private String apellidos;
        private String dni;
        private Integer tipoId;
        private String telefono1;
        private String argazkiaUrl;
        private String direccion;
        private String telefono2;
        
        public UserData(Long id, String email, String username, String nombre, 
                       String apellidos, String dni, Integer tipoId, 
                       String telefono1, String argazkiaUrl, String direccion, String telefono2) {
            this.id = id;
            this.email = email;
            this.username = username;
            this.nombre = nombre;
            this.apellidos = apellidos;
            this.dni = dni;
            this.tipoId = tipoId;
            this.telefono1 = telefono1;
            this.argazkiaUrl = argazkiaUrl;
            this.direccion = direccion;
            this.telefono2 = telefono2;
        }
        
        // Getters
        public Long getId() { return id; }
        public String getEmail() { return email; }
        public String getUsername() { return username; }
        public String getNombre() { return nombre; }
        public String getApellidos() { return apellidos; }
        public String getDni() { return dni; }
        public Integer getTipoId() { return tipoId; }
        public String getTelefono1() { return telefono1; }
        public String getArgazkiaUrl() { return argazkiaUrl; }
        public String getDireccion() { return direccion; }
        public String getTelefono2() { return telefono2; }
        
        public String getNombreCompleto() {
            return nombre + " " + apellidos;
        }
        
        public String getTipoNombre() {
            switch (tipoId) {
                case 1: return "God";
                case 2: return "Admin";
                case 3: return "Profesor";
                case 4: return "Alumno";
                default: return "Desconocido";
            }
        }
        
        @Override
        public String toString() {
            return "UserData{" +
                    "id=" + id +
                    ", email='" + email + '\'' +
                    ", nombre='" + nombre + '\'' +
                    ", apellidos='" + apellidos + '\'' +
                    ", tipoId=" + tipoId +
                    '}';
        }
    }
}
package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import connect.Connect;

/**
 * Cliente Socket para operaciones de perfil de usuario
 * 
 * Conecta con el servidor TCP para obtener y actualizar información del perfil
 * del usuario
 */
public class Profile {

	private static Connect connect = new Connect();

	private static final String SERVER_HOST = connect.getServerHost();
	private static final int SERVER_PORT = connect.getServerPort();
	private static final int TIMEOUT = connect.getTimeout();

	private Gson gson;

	public Profile() {
		this.gson = new Gson();
	}

	/**
	 * Obtiene los datos completos de un usuario por ID
	 * 
	 * @param userId ID del usuario
	 * @return UserProfileData con toda la información o null si hay error
	 */
	public UserProfileData obtenerPerfilUsuario(long userId) {
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
				System.err.println("No se recibió respuesta del servidor");
				return null;
			}

			JsonObject jsonRespuesta = gson.fromJson(respuesta, JsonObject.class);

			if (jsonRespuesta.has("errorea")) {
				System.err.println("Error del servidor: " + jsonRespuesta.get("errorea").getAsString());
				return null;
			}

			if (jsonRespuesta.has("aurkituta") && jsonRespuesta.get("aurkituta").getAsBoolean()) {
				JsonObject userJson = jsonRespuesta.getAsJsonObject("user");
				return jsonToUserProfileData(userJson);
			} else {
				System.err.println("Usuario no encontrado con ID: " + userId);
				return null;
			}

		} catch (SocketTimeoutException e) {
			System.err.println("Timeout conectando al servidor: " + e.getMessage());
			return null;

		} catch (IOException e) {
			System.err.println("Error de conexión: " + e.getMessage());
			return null;

		} catch (Exception e) {
			System.err.println("Error inesperado: " + e.getMessage());
			e.printStackTrace();
			return null;

		} finally {
			cerrarSocket(socket);
		}
	}

	/**
	 * Obtiene el tipo de usuario (nombre descriptivo)
	 * 
	 * @param tipoId ID del tipo
	 * @return Nombre del tipo o null si hay error
	 */
	public String obtenerNombreTipo(long tipoId) {
		Socket socket = null;

		try {
			socket = new Socket(SERVER_HOST, SERVER_PORT);
			socket.setSoTimeout(TIMEOUT);

			PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			String comando = "LORTU_TIPO:" + tipoId;
			salida.println(comando);

			String respuesta = entrada.readLine();

			if (respuesta == null || respuesta.trim().isEmpty()) {
				return getTipoNombrePorDefecto(tipoId);
			}

			JsonObject jsonRespuesta = gson.fromJson(respuesta, JsonObject.class);

			if (jsonRespuesta.has("aurkituta") && jsonRespuesta.get("aurkituta").getAsBoolean()) {
				JsonObject tipoJson = jsonRespuesta.getAsJsonObject("tipo");
				return tipoJson.has("name") && !tipoJson.get("name").isJsonNull() ? tipoJson.get("name").getAsString()
						: getTipoNombrePorDefecto(tipoId);
			}

			return getTipoNombrePorDefecto(tipoId);

		} catch (Exception e) {
			System.err.println("Error obteniendo tipo: " + e.getMessage());
			return getTipoNombrePorDefecto(tipoId);

		} finally {
			cerrarSocket(socket);
		}
	}

	/**
	 * Obtiene nombre de tipo por defecto según ID
	 */
	private String getTipoNombrePorDefecto(long tipoId) {
		switch ((int) tipoId) {
		case 1:
			return "God";
		case 2:
			return "Administrador";
		case 3:
			return "Profesor";
		case 4:
			return "Alumno";
		default:
			return "Usuario";
		}
	}

	/**
	 * Convierte un JsonObject a UserProfileData
	 */
	private UserProfileData jsonToUserProfileData(JsonObject userJson) {
		Long id = userJson.get("id").getAsLong();
		String email = userJson.get("email").getAsString();
		String username = userJson.get("username").getAsString();

		String nombre = userJson.has("nombre") && !userJson.get("nombre").isJsonNull()
				? userJson.get("nombre").getAsString()
				: "";

		String apellidos = userJson.has("apellidos") && !userJson.get("apellidos").isJsonNull()
				? userJson.get("apellidos").getAsString()
				: "";

		String dni = userJson.has("dni") && !userJson.get("dni").isJsonNull() ? userJson.get("dni").getAsString() : "";

		String direccion = userJson.has("direccion") && !userJson.get("direccion").isJsonNull()
				? userJson.get("direccion").getAsString()
				: "";

		String telefono1 = userJson.has("telefono1") && !userJson.get("telefono1").isJsonNull()
				? userJson.get("telefono1").getAsString()
				: "";

		String telefono2 = userJson.has("telefono2") && !userJson.get("telefono2").isJsonNull()
				? userJson.get("telefono2").getAsString()
				: "";

		Integer tipoId = userJson.get("tipoId").getAsInt();

		String argazkiaUrl = userJson.has("argazkiaUrl") && !userJson.get("argazkiaUrl").isJsonNull()
				? userJson.get("argazkiaUrl").getAsString()
				: null;

		return new UserProfileData(id, email, username, nombre, apellidos, dni, direccion, telefono1, telefono2, tipoId,
				argazkiaUrl);
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
	 * Clase para almacenar datos completos del perfil de usuario
	 */
	public static class UserProfileData {
		private Long id;
		private String email;
		private String username;
		private String nombre;
		private String apellidos;
		private String dni;
		private String direccion;
		private String telefono1;
		private String telefono2;
		private Integer tipoId;
		private String argazkiaUrl;

		public UserProfileData(Long id, String email, String username, String nombre, String apellidos, String dni,
				String direccion, String telefono1, String telefono2, Integer tipoId, String argazkiaUrl) {
			this.id = id;
			this.email = email;
			this.username = username;
			this.nombre = nombre;
			this.apellidos = apellidos;
			this.dni = dni;
			this.direccion = direccion;
			this.telefono1 = telefono1;
			this.telefono2 = telefono2;
			this.tipoId = tipoId;
			this.argazkiaUrl = argazkiaUrl;
		}

		// Getters
		public Long getId() {
			return id;
		}

		public String getEmail() {
			return email;
		}

		public String getUsername() {
			return username;
		}

		public String getNombre() {
			return nombre;
		}

		public String getApellidos() {
			return apellidos;
		}

		public String getDni() {
			return dni;
		}

		public String getDireccion() {
			return direccion;
		}

		public String getTelefono1() {
			return telefono1;
		}

		public String getTelefono2() {
			return telefono2;
		}

		public Integer getTipoId() {
			return tipoId;
		}

		public String getArgazkiaUrl() {
			return argazkiaUrl;
		}

		public String getNombreCompleto() {
			if (nombre.isEmpty() && apellidos.isEmpty()) {
				return username;
			}
			return (nombre + " " + apellidos).trim();
		}

		public String getTipoNombre() {
			switch (tipoId) {
			case 1:
				return "God";
			case 2:
				return "Administrador";
			case 3:
				return "Profesor";
			case 4:
				return "Alumno";
			default:
				return "Usuario";
			}
		}

		@Override
		public String toString() {
			return "UserProfileData{" + "id=" + id + ", email='" + email + '\'' + ", nombre='" + getNombreCompleto()
					+ '\'' + ", tipoId=" + tipoId + '}';
		}
	}
}
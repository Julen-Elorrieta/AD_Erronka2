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

public class CheckLogin {
	
	private static Connect connect = new Connect();

	private static final String SERVER_HOST = connect.getServerHost();
	private static final int SERVER_PORT = connect.getServerPort();
	private static final int TIMEOUT = connect.getTimeout();

	private Gson gson;

	public CheckLogin() {
		this.gson = new Gson();
	}

	public LoginResponse validarLogin(String email, String password) {

		if (email == null || email.trim().isEmpty()) {
			return new LoginResponse(false, "El email no puede estar vacío", null);
		}

		if (password == null || password.trim().isEmpty()) {
			return new LoginResponse(false, "La contraseña no puede estar vacía", null);
		}

		Socket socket = null;

		try {
			socket = new Socket(SERVER_HOST, SERVER_PORT);
			socket.setSoTimeout(TIMEOUT);

			PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			String comando = String.format("LOGIN:%s:%s", email, password);
			salida.println(comando);

			String respuesta = entrada.readLine();

			if (respuesta == null || respuesta.trim().isEmpty()) {
				return new LoginResponse(false, "No se recibió respuesta del servidor", null);
			}

			JsonObject jsonRespuesta = gson.fromJson(respuesta, JsonObject.class);

			boolean arrakasta = jsonRespuesta.get("arrakasta").getAsBoolean();
			String mezua = jsonRespuesta.get("mezua").getAsString();

			if (arrakasta) {
				JsonObject userData = jsonRespuesta.getAsJsonObject("erabiltzailea");
				UserData user = new UserData(userData.get("id").getAsLong(), userData.get("email").getAsString(),
						userData.get("username").getAsString(), userData.get("nombre").getAsString(),
						userData.get("apellidos").getAsString(), userData.get("dni").getAsString(),
						userData.get("tipoId").getAsInt(), userData.get("telefono1").getAsString(),
						userData.has("argazkiaUrl") && !userData.get("argazkiaUrl").isJsonNull()
								? userData.get("argazkiaUrl").getAsString()
								: null);

				return new LoginResponse(true, mezua, user);
			} else {
				return new LoginResponse(false, mezua, null);
			}

		} catch (SocketTimeoutException e) {
			return new LoginResponse(false, "Tiempo de espera agotado. El servidor no responde.", null);

		} catch (IOException e) {
			return new LoginResponse(false, "Error de conexión: " + e.getMessage(), null);

		} catch (Exception e) {
			return new LoginResponse(false, "Error inesperado: " + e.getMessage(), null);

		} finally {
			if (socket != null && !socket.isClosed()) {
				try {
					socket.close();
				} catch (IOException e) {
					System.err.println("Error al cerrar socket: " + e.getMessage());
				}
			}
		}
	}

	public static class LoginResponse {
		private boolean exitoso;
		private String mensaje;
		private UserData usuario;

		public LoginResponse(boolean exitoso, String mensaje, UserData usuario) {
			this.exitoso = exitoso;
			this.mensaje = mensaje;
			this.usuario = usuario;
		}

		public boolean isExitoso() {
			return exitoso;
		}

		public String getMensaje() {
			return mensaje;
		}

		public UserData getUsuario() {
			return usuario;
		}
	}

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

		public UserData(Long id, String email, String username, String nombre, String apellidos, String dni,
				Integer tipoId, String telefono1, String argazkiaUrl) {
			this.id = id;
			this.email = email;
			this.username = username;
			this.nombre = nombre;
			this.apellidos = apellidos;
			this.dni = dni;
			this.tipoId = tipoId;
			this.telefono1 = telefono1;
			this.argazkiaUrl = argazkiaUrl;
		}

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

		public Integer getTipoId() {
			return tipoId;
		}

		public String getTelefono1() {
			return telefono1;
		}

		public String getArgazkiaUrl() {
			return argazkiaUrl;
		}

		public String getNombreCompleto() {
			return nombre + " " + apellidos;
		}
	}
}
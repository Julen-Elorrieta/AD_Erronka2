package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Cliente Socket para operaciones con reuniones
 * 
 * Conecta con el servidor TCP para obtener y gestionar reuniones entre
 * profesores y alumnos
 */
public class Meetings {

	private static final String SERVER_HOST = "localhost";
	private static final int SERVER_PORT = 6000;
	private static final int TIMEOUT = 5000;

	private Gson gson;

	public Meetings() {
		this.gson = new Gson();
	}

	/**
	 * Obtiene todas las reuniones del sistema
	 * 
	 * @return Lista de reuniones o lista vacía si hay error
	 */
	public List<MeetingData> obtenerTodasReuniones() {
		return ejecutarComando("LORTU_REUNIONES");
	}

	/**
	 * Obtiene reuniones de un profesor específico
	 * 
	 * @param profesorId ID del profesor
	 * @return Lista de reuniones del profesor
	 */
	public List<MeetingData> obtenerReunionesPorProfesor(long profesorId) {
		return ejecutarComando("LORTU_REUNIONES_PROFE:" + profesorId);
	}

	/**
	 * Obtiene reuniones de un alumno específico
	 * 
	 * @param alumnoId ID del alumno
	 * @return Lista de reuniones del alumno
	 */
	public List<MeetingData> obtenerReunionesPorAlumno(long alumnoId) {
		return ejecutarComando("LORTU_REUNIONES_ALUMNO:" + alumnoId);
	}

	/**
	 * Ejecuta un comando que devuelve lista de reuniones
	 */
	private List<MeetingData> ejecutarComando(String comando) {
		List<MeetingData> reuniones = new ArrayList<>();
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
				return reuniones;
			}

			JsonObject jsonRespuesta = gson.fromJson(respuesta, JsonObject.class);

			if (jsonRespuesta.has("errorea")) {
				System.err.println("Error del servidor: " + jsonRespuesta.get("errorea").getAsString());
				return reuniones;
			}

			if (jsonRespuesta.has("reuniones")) {
				JsonArray reunionesArray = jsonRespuesta.getAsJsonArray("reuniones");

				for (int i = 0; i < reunionesArray.size(); i++) {
					JsonObject reunionJson = reunionesArray.get(i).getAsJsonObject();
					MeetingData reunion = jsonToMeetingData(reunionJson);
					reuniones.add(reunion);
				}

				System.out.println("✓ Cargadas " + reuniones.size() + " reuniones desde el servidor");
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

		return reuniones;
	}

	/**
	 * Convierte un JsonObject a MeetingData
	 */
	private MeetingData jsonToMeetingData(JsonObject meetingJson) {
		Long idReunion = meetingJson.get("idReunion").getAsLong();

		String estado = meetingJson.has("estado") && !meetingJson.get("estado").isJsonNull()
				? meetingJson.get("estado").getAsString()
				: "pendiente";

		String estadoEus = meetingJson.has("estadoEus") && !meetingJson.get("estadoEus").isJsonNull()
				? meetingJson.get("estadoEus").getAsString()
				: "onartzeke";

		Long profesorId = meetingJson.has("profesorId") && !meetingJson.get("profesorId").isJsonNull()
				? meetingJson.get("profesorId").getAsLong()
				: null;

		Long alumnoId = meetingJson.has("alumnoId") && !meetingJson.get("alumnoId").isJsonNull()
				? meetingJson.get("alumnoId").getAsLong()
				: null;

		String idCentro = meetingJson.has("idCentro") && !meetingJson.get("idCentro").isJsonNull()
				? meetingJson.get("idCentro").getAsString()
				: "";

		String titulo = meetingJson.has("titulo") && !meetingJson.get("titulo").isJsonNull()
				? meetingJson.get("titulo").getAsString()
				: "";

		String asunto = meetingJson.has("asunto") && !meetingJson.get("asunto").isJsonNull()
				? meetingJson.get("asunto").getAsString()
				: "";

		String aula = meetingJson.has("aula") && !meetingJson.get("aula").isJsonNull()
				? meetingJson.get("aula").getAsString()
				: "";

		LocalDateTime fecha = null;
		if (meetingJson.has("fecha") && !meetingJson.get("fecha").isJsonNull()) {
			String fechaStr = meetingJson.get("fecha").getAsString();
			try {
				fecha = LocalDateTime.parse(fechaStr);
			} catch (Exception e) {
				System.err.println("Error parseando fecha: " + fechaStr);
			}
		}

		return new MeetingData(idReunion, estado, estadoEus, profesorId, alumnoId, idCentro, titulo, asunto, aula,
				fecha);
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
	 * Clase para almacenar datos de reunión
	 */
	public static class MeetingData {
		private Long idReunion;
		private String estado;
		private String estadoEus;
		private Long profesorId;
		private Long alumnoId;
		private String idCentro;
		private String titulo;
		private String asunto;
		private String aula;
		private LocalDateTime fecha;

		public MeetingData(Long idReunion, String estado, String estadoEus, Long profesorId, Long alumnoId,
				String idCentro, String titulo, String asunto, String aula, LocalDateTime fecha) {
			this.idReunion = idReunion;
			this.estado = estado;
			this.estadoEus = estadoEus;
			this.profesorId = profesorId;
			this.alumnoId = alumnoId;
			this.idCentro = idCentro;
			this.titulo = titulo;
			this.asunto = asunto;
			this.aula = aula;
			this.fecha = fecha;
		}

		// Getters
		public Long getIdReunion() {
			return idReunion;
		}

		public String getEstado() {
			return estado;
		}

		public String getEstadoEus() {
			return estadoEus;
		}

		public Long getProfesorId() {
			return profesorId;
		}

		public Long getAlumnoId() {
			return alumnoId;
		}

		public String getIdCentro() {
			return idCentro;
		}

		public String getTitulo() {
			return titulo;
		}

		public String getAsunto() {
			return asunto;
		}

		public String getAula() {
			return aula;
		}

		public LocalDateTime getFecha() {
			return fecha;
		}

		/**
		 * Obtiene el estado en español
		 */
		public String getEstadoEspañol() {
			switch (estado.toLowerCase()) {
			case "pendiente":
				return "Pendiente";
			case "aceptada":
				return "Aceptada";
			case "denegada":
				return "Denegada";
			case "conflicto":
				return "Conflicto";
			default:
				return estado;
			}
		}

		/**
		 * Obtiene la fecha formateada
		 */
		public String getFechaFormateada() {
			if (fecha == null)
				return "Sin fecha";
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
			return fecha.format(formatter);
		}

		/**
		 * Obtiene solo la fecha (sin hora)
		 */
		public String getFechaSoloFecha() {
			if (fecha == null)
				return "Sin fecha";
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			return fecha.format(formatter);
		}

		/**
		 * Obtiene solo la hora
		 */
		public String getFechaSoloHora() {
			if (fecha == null)
				return "--:--";
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
			return fecha.format(formatter);
		}

		@Override
		public String toString() {
			return "MeetingData{" + "idReunion=" + idReunion + ", titulo='" + titulo + '\'' + ", estado=" + estado
					+ ", profesorId=" + profesorId + ", alumnoId=" + alumnoId + ", fecha=" + getFechaFormateada() + '}';
		}
	}
}
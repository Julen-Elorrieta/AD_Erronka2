package com.elorserv.model;

public class UserDTO {
	private Integer id;
	private String email;
	private String username;
	private String password;
	private String nombre;
	private String apellidos;
	private Integer tipoId;

	public UserDTO(Integer id, String email, String username, String password, String nombre, String apellidos,
			Integer tipoId) {
		this.id = id;
		this.email = email;
		this.username = username;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.tipoId = tipoId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public Integer getTipoId() {
		return tipoId;
	}

	public void setTipoId(Integer tipoId) {
		this.tipoId = tipoId;
	}

	
}

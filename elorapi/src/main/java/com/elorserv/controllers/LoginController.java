package com.elorserv.controllers;

import java.util.List;

import jakarta.persistence.*;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elorserv.model.UserDTO;
import com.elorserv.requests.LoginRequest;


@RestController
@RequestMapping("/login")
public class LoginController {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@PersistenceContext
    private EntityManager entityManager;
	
	@PostMapping
	public ResponseEntity<?> login(LoginRequest loginRequest) {
		
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "SELECT NEW com.elorserv.model(u.id, u.email, u.username, u.password, u.nombre, u.apellidos, u.tipos.id) FROM Users u WHERE u.email = :param";
			TypedQuery<UserDTO> q = entityManager.createQuery(hql, UserDTO.class);
			q.setParameter("param", loginRequest.getEmail());
			UserDTO user = q.getSingleResult();
			
			if (user != null) {
				if (user.getPassword().equals(loginRequest.getPassword())) {
					return ResponseEntity.ok(user);
				} else {
					return ResponseEntity.status(401).body("Contraseña incorrecta");
				}
			} else {
				return ResponseEntity.status(404).body("Usuario no encontrado");
			}
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error en el servidor: " + e.getMessage());
		}
	}
}

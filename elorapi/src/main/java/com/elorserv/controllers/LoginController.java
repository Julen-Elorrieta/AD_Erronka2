package com.elorserv.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elorserv.interfaces.UsersRepository;
import com.elorserv.requests.LoginRequest;

@RestController
@RequestMapping("/api")
public class LoginController {
	@Autowired
	private UsersRepository usersRepository;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
		return usersRepository.findByEmail(loginRequest.getEmail())
				.map(user -> {
					if(user.getPassword().equals(loginRequest.getPassword())) {
						return ResponseEntity.ok(user);
					} else {
						return ResponseEntity.status(401).body("Contraseña incorrecta");
					}
				})
				.orElse(ResponseEntity.status(404).body("Usuario no encontrado"));
	}
}

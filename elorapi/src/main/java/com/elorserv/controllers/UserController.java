package com.elorserv.controllers;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elorserv.model.Users;

@RestController
@RequestMapping("/users")
public class UserController {

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/getAlumnos")
    public ResponseEntity<?> getAllStudents() {
        try {
            TypedQuery<Users> q = entityManager.createQuery("SELECT u FROM Users u WHERE u.tipos.id = :tipoid", Users.class);
            q.setParameter("tipoid", 4);
            List<Users> list = q.getResultList();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Server error: " + e.getMessage());
        }
    }
    
    @GetMapping("/getUsers")
    public ResponseEntity<?> getAllUsers() {
        try {
            TypedQuery<Users> q = entityManager.createQuery("SELECT u FROM Users u", Users.class);
            List<Users> list = q.getResultList();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Server error: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        try {
            Users u = entityManager.find(Users.class, id);
            if (u == null) return ResponseEntity.status(404).body("User not found: " + id);
            return ResponseEntity.ok(u);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Server error: " + e.getMessage());
        }
    }
    
    

}
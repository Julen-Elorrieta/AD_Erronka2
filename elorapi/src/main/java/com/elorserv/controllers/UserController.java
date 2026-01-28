package com.elorserv.controllers;

import java.util.List;
import java.util.Collections;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<?> getAllUsers(@RequestParam Integer id) {
        try {
            TypedQuery<Users> q = entityManager.createQuery("SELECT u FROM Users u WHERE u.id != :id", Users.class);
            q.setParameter("id", id);
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
    
    @GetMapping("/profesor/{profId}/alumnos")
    public ResponseEntity<?> getAlumnosFromProfesor(@PathVariable Integer profId) {
        try {
            System.out.println("=== ENDPOINT LLAMADO: /users/profesor/" + profId + "/alumnos ===");
            
            // Paso 1: Sacar los ciclo_id de los módulos del horario del profesor
            TypedQuery<Integer> qCiclos = entityManager.createQuery(
                "SELECT DISTINCT m.ciclos.id " +
                "FROM Horarios h " +
                "JOIN h.modulos m " +
                "WHERE h.users.id = :pid",
                Integer.class
            );
            qCiclos.setParameter("pid", profId);
            List<Integer> cicloIds = qCiclos.getResultList();
            
            System.out.println("Ciclos encontrados: " + cicloIds);
            
            if (cicloIds == null || cicloIds.isEmpty()) {
                System.out.println("No se encontraron ciclos para el profesor " + profId);
                return ResponseEntity.ok(Collections.emptyList());
            }
            
            // Paso 2: Sacar los alumnos matriculados en esos ciclos
            TypedQuery<Users> qAlumnos = entityManager.createQuery(
                "SELECT DISTINCT mat.users " +
                "FROM Matriculaciones mat " +
                "WHERE mat.ciclos.id IN :cicloIds " +
                "AND mat.users.tipos.id = 4",
                Users.class
            );
            qAlumnos.setParameter("cicloIds", cicloIds);
            List<Users> alumnos = qAlumnos.getResultList();
            
            System.out.println("Alumnos encontrados: " + alumnos.size());
            
            return ResponseEntity.ok(alumnos);
        } catch (Exception e) {
            System.err.println("ERROR en getAlumnosFromProfesor: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Server error: " + e.getMessage());
        }
    }


}
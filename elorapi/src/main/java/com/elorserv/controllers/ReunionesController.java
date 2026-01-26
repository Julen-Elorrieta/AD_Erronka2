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

import com.elorserv.model.Reuniones;

@RestController
@RequestMapping("/reuniones")
public class ReunionesController {

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/getReuniones/profesor/{id}")
    public ResponseEntity<?> getReunionesByProfesorId(@PathVariable Integer id) {
        try {
            List<Reuniones> result = findByProfesorId(id);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Server error: " + e.getMessage());
        }
    }

    @GetMapping("/getReuniones/alumno/{id}")
    public ResponseEntity<?> getReunionesByAlumnoId(@PathVariable Integer id) {
        try {
            List<Reuniones> result = findByAlumnoId(id);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Server error: " + e.getMessage());
        }
    }

    private List<Reuniones> findByProfesorId(Integer id) throws Exception {
        String[] candidates = {
            "SELECT r FROM Reuniones r WHERE r.usersByProfesorId.id = :id",
            "SELECT r FROM Reuniones r WHERE r.profesor.id = :id",
            "SELECT r FROM Reuniones r WHERE r.usersByProfesorId = :id",
            "SELECT r FROM Reuniones r WHERE r.profesor = :id"
        };

        for (String jpql : candidates) {
            try {
                TypedQuery<Reuniones> q = entityManager.createQuery(jpql, Reuniones.class);
                q.setParameter("id", id);
                return q.getResultList();
            } catch (IllegalArgumentException ex) {
                // try next
            }
        }

        throw new Exception("Unable to build JPQL for Reuniones by profesor id. Check Reuniones entity mapping.");
    }

    private List<Reuniones> findByAlumnoId(Integer id) throws Exception {
        String[] candidates = {
            "SELECT r FROM Reuniones r WHERE r.usersByAlumnoId.id = :id",
            "SELECT r FROM Reuniones r WHERE r.alumno.id = :id",
            "SELECT r FROM Reuniones r WHERE r.usersByAlumnoId = :id",
            "SELECT r FROM Reuniones r WHERE r.alumno = :id"
        };

        for (String jpql : candidates) {
            try {
                TypedQuery<Reuniones> q = entityManager.createQuery(jpql, Reuniones.class);
                q.setParameter("id", id);
                return q.getResultList();
            } catch (IllegalArgumentException ex) {
                // try next
            }
        }

        throw new Exception("Unable to build JPQL for Reuniones by alumno id. Check Reuniones entity mapping.");
    }
}
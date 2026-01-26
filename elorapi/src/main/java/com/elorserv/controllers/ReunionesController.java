package com.elorserv.controllers;

import java.util.List;
import java.util.Map;
import java.sql.Timestamp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.elorserv.model.Reuniones;
import com.elorserv.model.ReunionesDTO;
import com.elorserv.model.Users;
import com.elorserv.services.ReunionesService;

@RestController
@RequestMapping("/reuniones")
public class ReunionesController {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ReunionesService reunionesService;

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

    @PostMapping("/create")
    public ResponseEntity<?> createReunion(@RequestBody ReunionesDTO dto) {
        if (dto == null) return ResponseEntity.badRequest().body("Request body is required");

        // basic validation before transaction: check referenced users exist
        if (dto.getAlumnoId() != null) {
            Users alumno = entityManager.find(Users.class, dto.getAlumnoId());
            if (alumno == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Alumno not found: " + dto.getAlumnoId());
        }
        if (dto.getProfesorId() != null) {
            Users profesor = entityManager.find(Users.class, dto.getProfesorId());
            if (profesor == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Profesor not found: " + dto.getProfesorId());
        }

        try {
            ReunionesDTO saved = reunionesService.saveReunion(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(iae.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteReunion(@PathVariable Integer id) {
        try {
            reunionesService.deleteReunion(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(iae.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error: " + e.getMessage());
        }
    }

    @PutMapping("/updateEstado/{id}")
    public ResponseEntity<?> updateEstado(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        try {
            if (body == null || !body.containsKey("estado") ) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing 'estado' in request body");
            }
            String estado = body.get("estado");
            ReunionesDTO updated = reunionesService.updateEstado(id, estado);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(iae.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error: " + e.getMessage());
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
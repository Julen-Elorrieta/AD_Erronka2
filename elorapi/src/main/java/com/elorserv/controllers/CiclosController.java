package com.elorserv.controllers;

import com.elorserv.model.Ciclos;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@RestController
@RequestMapping("/ciclos")
public class CiclosController {

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/getCiclos")
    public ResponseEntity<?> getAllCiclos() {
        try {
            TypedQuery<Ciclos> q = entityManager.createQuery("SELECT c FROM Ciclos c", Ciclos.class);
            List<Ciclos> ciclos = q.getResultList();
            return ResponseEntity.ok(ciclos);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Server error: " + e.getMessage());
        }
    }
}

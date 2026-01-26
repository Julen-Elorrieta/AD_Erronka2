package com.elorserv.controllers;

import com.elorserv.model.Modulos;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@RestController
@RequestMapping("/modulos")
public class ModulosController {

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/getModulos")
    public ResponseEntity<?> getAllModulos() {
        try {
            TypedQuery<Modulos> q = entityManager.createQuery("SELECT m FROM Modulos m", Modulos.class);
            List<Modulos> modulos = q.getResultList();
            return ResponseEntity.ok(modulos);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Server error: " + e.getMessage());
        }
    }
}
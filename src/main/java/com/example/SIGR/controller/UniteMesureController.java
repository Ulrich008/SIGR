package com.example.SIGR.controller;

import com.example.SIGR.dto.request.UniteMesureRequest;
import com.example.SIGR.dto.response.UniteMesureResponse;
import com.example.SIGR.services.UniteMesureService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/unites-mesure")
public class UniteMesureController {

    private final UniteMesureService service;

    public UniteMesureController(UniteMesureService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UniteMesureResponse> create(@RequestBody UniteMesureRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @GetMapping("/{code}")
    public ResponseEntity<UniteMesureResponse> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(service.getByCode(code));
    }

    @GetMapping
    public ResponseEntity<List<UniteMesureResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{code}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UniteMesureResponse> update(
            @PathVariable String code,
            @RequestBody UniteMesureRequest request
    ) {
        return ResponseEntity.ok(service.update(code, request));
    }

    @DeleteMapping("/{code}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String code) {
        service.delete(code);
        return ResponseEntity.noContent().build();
    }
}

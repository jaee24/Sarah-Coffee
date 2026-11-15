package com.senja.api;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ApiErrorHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> rse(ResponseStatusException e) {
        return body(e.getStatusCode().value(), e.getReason());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> any(Exception e) {
        Throwable root = e;
        while (root.getCause() != null && root.getCause() != root) root = root.getCause();
        return body(500, root.getClass().getSimpleName() + ": " + root.getMessage());
    }

    private ResponseEntity<Map<String, Object>> body(int status, String pesan) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("status", status);
        m.put("pesan", pesan == null ? "Kesalahan internal" : pesan);
        return ResponseEntity.status(status).body(m);
    }
}
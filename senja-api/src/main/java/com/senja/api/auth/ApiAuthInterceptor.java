package com.senja.api.auth;

import java.util.Map;
import java.util.Set;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ApiAuthInterceptor implements HandlerInterceptor {

    private static final Set<String> PUBLIC = Set.of("/api/health", "/api/auth/login", "/api/devices/register");
    private final TokenService tokens;

    public ApiAuthInterceptor(TokenService tokens) { this.tokens = tokens; }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        String path = req.getRequestURI();
        if (PUBLIC.contains(path)) return true;
        if ("/api/sync".equals(path)) return true; // auth perangkat (X-Device-Id/Key) dicek di SyncController

        String auth = req.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            resp.sendError(401, "Header Authorization: Bearer <token> wajib diisi");
            return false;
        }
        try {
            Map<String, Object> sess = tokens.verify(auth.substring(7).trim());
            req.setAttribute("user_id", sess.get("user_id"));
            req.setAttribute("user_level", sess.get("level"));
            return true;
        } catch (Exception e) {
            resp.sendError(401, "Token tidak valid atau kedaluwarsa");
            return false;
        }
    }
}
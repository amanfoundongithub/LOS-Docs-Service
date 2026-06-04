package com.loan_org.document_service.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class SimpleSecurityInterceptor implements HandlerInterceptor {

    // IMPORTANT: For modern HMAC-SHA algorithms, this string MUST be at least 32 bytes (256 bits) long.
    private final String SIGNING_KEY = "zZFvO6Mb9unL62IIhtfzJfoP6FSoLZ1FtmaPqFblGzr";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        try {
            String token = authHeader.substring(7);

            // 1. Convert the plain text string key safely into a SecretKey object
            SecretKey key = Keys.hmacShaKeyFor(SIGNING_KEY.getBytes(StandardCharsets.UTF_8));

            // 2. Parse using the new immutable builder pipeline
            Claims claims = Jwts.parser()
                    .verifyWith(key)             // Replaces setSigningKey(String)
                    .build()                     // Compiles the parser instance
                    .parseSignedClaims(token)    // Replaces parseClaimsJws(String)
                    .getPayload();               // Replaces getBody()

            // Attach claims to request attributes for the controller layer
            request.setAttribute("userId", claims.getSubject());

            // Get attributes
            Map attributes = claims.get("attributes", Map.class);
            request.setAttribute("userRole", attributes.get("user_role"));
            request.setAttribute("canUpload", attributes.get("document:upload"));

            return true;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
    }
}
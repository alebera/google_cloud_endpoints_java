package com.example.echo.auth;

import com.google.api.server.spi.response.UnauthorizedException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;

import javax.servlet.http.HttpServletRequest;

public class FirebaseAuthService {

    public String getEmailFromToken(HttpServletRequest req) throws UnauthorizedException {
        String bearerToken= req.getHeader("Authorization");
        System.out.println("bearerToken ---> ");
        System.out.println(bearerToken);
        if (bearerToken == null) {
            throw new UnauthorizedException("Missing Authorization header");
        }

        try {
            if (!bearerToken.startsWith("Bearer ")) {
                throw new RuntimeException("Invalid Authorization header");
            }
            String idToken = bearerToken.substring(7);
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            return decodedToken.getEmail();
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid or expired token");
        }
    }
}
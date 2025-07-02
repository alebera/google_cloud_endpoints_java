package com.crystalloids.alessandro.berardinelli.auth;

import com.google.api.server.spi.response.UnauthorizedException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;

import javax.servlet.http.HttpServletRequest;

public class FirebaseAuthService implements AuthService {

    public static final int BEGIN_INDEX = 7;
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER_ = "Bearer ";

    /**
     * Check if an user exists in Firebase
     * @param req
     * @return the email of the user registered in Firebase
     * @throws UnauthorizedException
     */
    @Override
    public String verifyUser(HttpServletRequest req) throws UnauthorizedException {
        String bearerToken = req.getHeader(AUTHORIZATION);
        if (bearerToken == null || !bearerToken.startsWith(BEARER_)) {
            throw new UnauthorizedException("Missing Authorization header");
        }
        try {
            String idToken = bearerToken.substring(BEGIN_INDEX);
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            return decodedToken.getEmail();
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid or expired token");
        }
    }
}
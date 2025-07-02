package com.crystalloids.alessandro.berardinelli.auth;

import com.google.api.server.spi.response.UnauthorizedException;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {
    String verifyUser(HttpServletRequest req) throws UnauthorizedException;
    }

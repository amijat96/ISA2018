package com.example.backend.security;

import com.example.backend.config.ConfigProperties;
import com.example.backend.exception.RoleNotFoundException;
import com.example.backend.model.User;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    private final ConfigProperties configProperties;

    @Autowired
    public JwtTokenProvider(ConfigProperties configProperties) {
        this.configProperties = configProperties;
    }

    public String generateAuthToken(Authentication authentication) {

        User userPrincipal = (User) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + configProperties.getJwtExpirationInMs());
        Claims claims = Jwts.claims().setSubject(Long.toString(userPrincipal.getUserId()));
        claims.put("role", userPrincipal.getAuthorities()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RoleNotFoundException("Role not found!"))
                .getAuthority());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, configProperties.getJwtSecret())
                .compact();
    }

    public String generateConfirmationToken(Integer userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + configProperties.getJwtExpirationInMs());

        return Jwts.builder()
                .setSubject(Integer.toString(userId))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, configProperties.getJwtSecret())
                .compact();
    }

    public String generateExaminationConfirmationToken(Integer examinationId) {
        return Jwts.builder()
                .setSubject(Integer.toString(examinationId))
                .signWith(SignatureAlgorithm.HS512, configProperties.getJwtSecret())
                .compact();
    }

    public Integer getIdFromJwt(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(configProperties.getJwtSecret())
                .parseClaimsJws(token)
                .getBody();

        return Integer.parseInt(claims.getSubject());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(configProperties.getJwtSecret()).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }
}

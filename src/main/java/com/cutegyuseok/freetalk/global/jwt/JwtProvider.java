package com.cutegyuseok.freetalk.global.jwt;

import com.cutegyuseok.freetalk.dto.TokenDTO;

import com.cutegyuseok.freetalk.dto.UserDTO;
import com.cutegyuseok.freetalk.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtProperties jwtProperties;

    public TokenDTO makeJwtToken(User user) {
        Date now = new Date();

        String accessToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + Duration.ofMinutes(30).toMillis()))
                .claim("email", user.getEmail())
                .claim("role", user.getRole())
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();

        String refreshToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + Duration.ofDays(15).toMillis()))
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();

        return TokenDTO.builder().accessToken(accessToken).refreshToken(refreshToken).role(user.getRole().name()).build();
    }


    public UserDTO.UserAccessDTO tokenToUser(String accessToken) {

        try {
            accessToken = extractToken(accessToken);
            Claims claims = null;
            claims = tokenToClaims(accessToken);
            return new UserDTO.UserAccessDTO(claims);
        } catch (Exception e) {
            return null;
        }
    }

    public Claims tokenToClaims(String accessToken) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(accessToken)
                .getBody();

    }

    public String extractToken(String authorizationHeader) {
        return authorizationHeader.substring(jwtProperties.getTokenPrefix().length());
    }

    public Long getExpiration(String accessToken) {
        Date expiration =
                Jwts.parser()
                        .setSigningKey(jwtProperties.getSecretKey())
                        .parseClaimsJws(accessToken)
                        .getBody().getExpiration();
        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }

    public String recreationAccessToken(String userEmail, String userRole) {

        Date now = new Date();
        //Access Token
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + Duration.ofMinutes(30).toMillis()))
                .claim("email", userEmail)
                .claim("role", userRole)
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }


}

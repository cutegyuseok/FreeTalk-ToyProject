package com.cutegyuseok.freetalk.auth.service;


import com.cutegyuseok.freetalk.auth.dto.TokenDTO;
import org.springframework.http.ResponseEntity;

public interface TokenService {

    ResponseEntity<?> logout(String header, TokenDTO.RefreshTokenReqDTO refreshTokenReqDTO);

    ResponseEntity<?> validateRefreshToken(String refreshToken);

    boolean checkToken(String token);
}

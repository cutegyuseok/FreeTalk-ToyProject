package com.cutegyuseok.freetalk.service;


import com.cutegyuseok.freetalk.dto.TokenDTO;
import org.springframework.http.ResponseEntity;

public interface TokenService {

    ResponseEntity<?> logout(String header, TokenDTO.RefreshTokenReqDTO refreshTokenReqDTO);

    ResponseEntity<?> validateRefreshToken(String refreshToken);

    boolean checkToken(String token);
}

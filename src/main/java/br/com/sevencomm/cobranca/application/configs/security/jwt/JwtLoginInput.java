package br.com.sevencomm.cobranca.application.configs.security.jwt;

import lombok.Data;

@Data
class JwtLoginInput {
    private String username;
    private String password;
}
package com.example.loginauthapi.dto;

public class RedefinirSenhaRequest {

    private String codigo;
    private String novaSenha;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }
}

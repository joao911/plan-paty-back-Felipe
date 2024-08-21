package com.example.loginauthapi.domain.user;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class CodigoRecuperacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    @Column(nullable = false)
    private String codigo;

    @Column(nullable = false)
    private Date dataCriacao;

    public CodigoRecuperacao() {
        // Construtor padrão
    }

    public CodigoRecuperacao(User usuario, String codigo, Date dataCriacao) {
        this.usuario = usuario;
        this.codigo = codigo;
        this.dataCriacao = dataCriacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

}

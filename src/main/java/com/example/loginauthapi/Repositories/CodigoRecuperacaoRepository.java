package com.example.loginauthapi.Repositories;

import com.example.loginauthapi.domain.user.CodigoRecuperacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodigoRecuperacaoRepository extends JpaRepository<CodigoRecuperacao, Long> {

    Optional<CodigoRecuperacao> findByCodigo(String codigo);
}

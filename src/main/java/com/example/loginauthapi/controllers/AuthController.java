package com.example.loginauthapi.controllers;

import com.example.loginauthapi.Services.AuthService;
import com.example.loginauthapi.Repositories.UserRepository;
import com.example.loginauthapi.domain.user.User;
import com.example.loginauthapi.dto.*;
import com.example.loginauthapi.infra.security.TokenService;
import com.example.loginauthapi.exceptions.CodigoRecuperacaoInvalidoException;
import com.example.loginauthapi.exceptions.SenhaIgualAnteriorException;
import com.example.loginauthapi.exceptions.UsuarioNotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO body){
        User user = this.repository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("User not found"));
        if(passwordEncoder.matches(body.password(), user.getPassword())) {
            String token = this.tokenService.generateToken(user);
            return ResponseEntity.ok(new ResponseDTO(user.getName(), token));
        }
        return ResponseEntity.badRequest().body("Senha incorreta corno!!");
    }


    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequestDTO body) {
        if (!authService.isPasswordValid(body.password())) {
            return ResponseEntity.badRequest().body("Senha deve ter no mínimo 6 dígitos, conter letras, números e um caractere especial.");
        }

        if (!authService.isEmailValid(body.email())) {
            return ResponseEntity.badRequest().body("E-mail inválido.");
        }

        Optional<User> user = this.repository.findByEmail(body.email());

        if (user.isEmpty()) {
            User newUser = new User();
            newUser.setPassword(passwordEncoder.encode(body.password()));
            newUser.setEmail(body.email());
            newUser.setName(body.name());
            this.repository.save(newUser);

            String token = this.tokenService.generateToken(newUser);
            return ResponseEntity.ok(new ResponseDTO(newUser.getName(), token));
        }
        return ResponseEntity.badRequest().build();
    }


    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = this.repository.findAll();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/recuperar-senha")
    public ResponseEntity<String> recuperarSenha(@RequestBody RecuperarSenhaRequest request) {
        try {
            authService.enviarCodigoRecuperacao(request.getEmail());
            return ResponseEntity.ok("Código de recuperação enviado para o e-mail.");
        } catch (UsuarioNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @PostMapping("/redefinir-senha")
    public ResponseEntity<String> redefinirSenha(@RequestBody RedefinirSenhaRequest request) {
        try {
            authService.redefinirSenha(request.getCodigo(), request.getNovaSenha());
            return ResponseEntity.ok("Senha redefinida com sucesso.");
        } catch (CodigoRecuperacaoInvalidoException | SenhaIgualAnteriorException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


}
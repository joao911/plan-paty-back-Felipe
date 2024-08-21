package com.example.loginauthapi.Services;

import com.example.loginauthapi.Repositories.CodigoRecuperacaoRepository;
import com.example.loginauthapi.Repositories.UserRepository;
import com.example.loginauthapi.domain.user.CodigoRecuperacao;
import com.example.loginauthapi.domain.user.User;
import com.example.loginauthapi.exceptions.CodigoRecuperacaoInvalidoException;
import com.example.loginauthapi.exceptions.SenhaIgualAnteriorException;
import com.example.loginauthapi.exceptions.UsuarioNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CodigoRecuperacaoRepository codigoRecuperacaoRepository;

    @Autowired
    private JavaMailSender mailSender;

    public void enviarCodigoRecuperacao(String email) {
        Optional<User> usuarioOptional = userRepository.findByEmail(email);
        if (!usuarioOptional.isPresent()) {
            throw new UsuarioNotFoundException("Usuário não encontrado");
        }
        User usuario = usuarioOptional.get();
        String codigo = gerarCodigoRecuperacao();
        CodigoRecuperacao codigoRecuperacao = new CodigoRecuperacao(usuario, codigo, new Date());
        codigoRecuperacaoRepository.save(codigoRecuperacao);

        enviarEmailComCodigo(usuario.getEmail(), codigo);
    }

    private String gerarCodigoRecuperacao() {
        int length = 6;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder codigo = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            codigo.append(characters.charAt(random.nextInt(characters.length())));
        }

        return codigo.toString();
    }

    private void enviarEmailComCodigo(String email, String codigo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Código de Recuperação de Senha");
        message.setText("Seu código de recuperação é: " + codigo);
        mailSender.send(message);
    }

    public void redefinirSenha(String codigo, String novaSenha) {
        CodigoRecuperacao codigoRecuperacao = codigoRecuperacaoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new CodigoRecuperacaoInvalidoException("Código de recuperação inválido"));

        User usuario = codigoRecuperacao.getUsuario();

        if (!isPasswordValid(novaSenha)) {
            throw new IllegalArgumentException("A senha deve ter no mínimo 6 dígitos, contendo letras, números e um caractere especial.");
        }

        if (usuario.getPassword().equals(novaSenha)) {
            throw new SenhaIgualAnteriorException("A nova senha não pode ser igual à anterior.");
        }

        usuario.setPassword(novaSenha);
        userRepository.save(usuario);
        codigoRecuperacaoRepository.delete(codigoRecuperacao);  // Remove o código usado
    }

    public boolean isPasswordValid(String password) {
        String regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{6,}$";
        return password.matches(regex);
    }

    public boolean isEmailValid(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(regex);
    }



}

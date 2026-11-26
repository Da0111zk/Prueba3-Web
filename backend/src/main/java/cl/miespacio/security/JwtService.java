package cl.miespacio.security;

import cl.miespacio.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Date;
import javax.crypto.SecretKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private static final Logger LOG = LoggerFactory.getLogger(JwtService.class);

    private final SecretKey clave;
    private final long expiracionMs;

    public JwtService(@Value("${app.jwt.secret:}") String secreto,
                      @Value("${app.jwt.expiracion-minutos:60}") long expiracionMinutos) {
        byte[] bytes;
        if (secreto == null || secreto.isBlank()) {
            bytes = new byte[48];
            new SecureRandom().nextBytes(bytes);
            LOG.warn("JWT_SECRET no está definido: se generó una clave temporal. Los tokens dejarán de ser válidos al reiniciar.");
        } else {
            bytes = secreto.getBytes(StandardCharsets.UTF_8);
            if (bytes.length < 32) {
                throw new IllegalStateException("JWT_SECRET debe tener al menos 32 caracteres.");
            }
        }
        this.clave = Keys.hmacShaKeyFor(bytes);
        this.expiracionMs = expiracionMinutos * 60_000L;
    }

    public String generar(Usuario usuario) {
        Date ahora = new Date();
        return Jwts.builder()
                .subject(usuario.getEmail())
                .claim("rol", usuario.getRol().name())
                .claim("uid", usuario.getId())
                .issuedAt(ahora)
                .expiration(new Date(ahora.getTime() + expiracionMs))
                .signWith(clave)
                .compact();
    }

    public Claims leer(String token) {
        return Jwts.parser().verifyWith(clave).build().parseSignedClaims(token).getPayload();
    }

    public long getExpiracionMs() {
        return expiracionMs;
    }
}

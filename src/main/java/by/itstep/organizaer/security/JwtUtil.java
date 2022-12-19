package by.itstep.organizaer.security;

import by.itstep.organizaer.config.ProjectConfiguration;
import by.itstep.organizaer.model.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class JwtUtil {

    ProjectConfiguration config;

    /**
     * Содержание токена:
     * 1. subject: organizer
     * 2. userId: userId
     * 3. expiration time: дата-время (для теста можно установить в 2 минуты)
     * 4. секретный ключ
     * 5. алгоритм шифрования
     * Ex.:{
     *     header: {
     *         alg: ES256,
     *         typ: JWT
     *     },
     *     payload: {
     *         sub: organizer,
     *         userId: ${user.id},
     *         expiration: ${екущее время + 2 минуты}
     *         issued: expiration - 2 minutes
     *     },
     *     signature: {
     *         binaryData
     *     }
     * }
     * @param user - пользователь
     * @return строку - JSON-токен
     */
    public String generateToken(final User user) {
        final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.ES256);
        final Date expirationDate = Date.from(LocalDateTime.now().plusMinutes(2).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setSubject("organizer")
                .addClaims(Map.of("userId", user.getId()))
                .setExpiration(expirationDate)
                .setIssuedAt(new Date())
                .signWith(key, SignatureAlgorithm.ES256)
                .compact();
    }

    public Claims validateAndGet(final String token) {

    }
}

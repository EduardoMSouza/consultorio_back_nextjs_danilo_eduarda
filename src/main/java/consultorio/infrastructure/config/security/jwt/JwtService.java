package consultorio.infrastructure.config.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Slf4j
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration:21600000}") // 6 horas padrão
    private long jwtExpiration;

    @Value("${jwt.refresh-expiration:604800000}") // 7 dias padrão
    private long refreshExpiration;

    // Em produção, usar Redis ou banco de dados
    private final Map<String, Set<String>> validRefreshTokens = new ConcurrentHashMap<>();
    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();

    public String extractUsername(String token) {
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (Exception e) {
            log.debug("Falha ao extrair username do token: {}", e.getMessage());
            return null;
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        Map<String, Object> claims = new HashMap<>(extraClaims);
        claims.put("type", expiration == refreshExpiration ? "refresh" : "access");
        claims.put("created", System.currentTimeMillis());

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        if (isTokenBlacklisted(token)) {
            log.debug("Token está na blacklist");
            return false;
        }

        final String username = extractUsername(token);
        return (username != null && username.equals(userDetails.getUsername()))
                && !isTokenExpired(token);
    }

    public boolean isRefreshTokenValid(String username, String refreshToken) {
        if (isTokenBlacklisted(refreshToken)) {
            return false;
        }

        Set<String> userRefreshTokens = validRefreshTokens.get(username);
        return userRefreshTokens != null
                && userRefreshTokens.contains(refreshToken)
                && !isTokenExpired(refreshToken);
    }

    @Transactional
    public void storeRefreshToken(String username, String refreshToken) {
        validRefreshTokens
                .computeIfAbsent(username, k -> new HashSet<>())
                .add(refreshToken);
        log.debug("Refresh token armazenado para usuário: {}", username);
    }

    @Transactional
    public void invalidateRefreshToken(String username, String refreshToken) {
        Set<String> userTokens = validRefreshTokens.get(username);
        if (userTokens != null) {
            userTokens.remove(refreshToken);
            if (userTokens.isEmpty()) {
                validRefreshTokens.remove(username);
            }
        }
        blacklistedTokens.add(refreshToken);
        log.debug("Refresh token invalidado para usuário: {}", username);
    }

    @Transactional
    public void invalidateToken(String token) {
        blacklistedTokens.add(token);
        log.debug("Token adicionado à blacklist");
    }

    @Transactional
    public void invalidateAllUserTokens(String username) {
        Set<String> userTokens = validRefreshTokens.remove(username);
        if (userTokens != null) {
            blacklistedTokens.addAll(userTokens);
        }
        log.info("Todos os tokens invalidados para usuário: {}", username);
    }

    @Transactional
    public void cleanupExpiredTokens() {
        // Limpa tokens expirados da memória periodicamente
        blacklistedTokens.removeIf(this::isTokenExpired);

        validRefreshTokens.forEach((username, tokens) -> {
            tokens.removeIf(this::isTokenExpired);
            if (tokens.isEmpty()) {
                validRefreshTokens.remove(username);
            }
        });
        log.debug("Cleanup de tokens expirados realizado");
    }

    private boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }

    private boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (Exception e) {
            log.debug("Erro ao verificar expiração do token: {}", e.getMessage());
            return true; // Se não consegue verificar, considera expirado
        }
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            log.warn("Token JWT inválido: {}", e.getMessage());
            throw e;
        }
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public long getJwtExpiration() {
        return jwtExpiration;
    }

    public long getRefreshExpiration() {
        return refreshExpiration;
    }
}
package consultorio.infrastructure.config.security.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Componente para gerenciar tokens JWT
 * Inclui limpeza automática de tokens expirados
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtService jwtService;

    /**
     * Limpa tokens expirados a cada hora
     */
    @Scheduled(fixedRate = 3600000) // 1 hora
    public void cleanupExpiredTokens() {
        log.debug("Iniciando limpeza de tokens expirados...");
        jwtService.cleanupExpiredTokens();
        log.debug("Limpeza de tokens expirados concluída - {}", LocalDateTime.now());
    }

    /**
     * Valida estrutura do token
     */
    public boolean validateTokenStructure(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }

        try {
            String username = jwtService.extractUsername(token);
            return username != null && !username.isEmpty();
        } catch (Exception e) {
            log.debug("Token com estrutura inválida: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Extrai informações do token para logs/debug
     */
    public String getTokenInfo(String token) {
        try {
            String username = jwtService.extractUsername(token);
            return String.format("Token[user=%s]", username);
        } catch (Exception e) {
            return "Token[invalid]";
        }
    }
}
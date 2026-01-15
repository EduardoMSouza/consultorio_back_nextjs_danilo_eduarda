package consultorio.infrastructure.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    @Value("${cors.allowed-origins:http://localhost:4200,http://localhost:3000}")
    private String[] allowedOrigins;

    @Value("${cors.allowed-methods:GET,POST,PUT,PATCH,DELETE,OPTIONS}")
    private String[] allowedMethods;

    @Value("${cors.allowed-headers:*}")
    private String[] allowedHeaders;

    @Value("${cors.exposed-headers:Authorization}")
    private String[] exposedHeaders;

    @Value("${cors.allow-credentials:true}")
    private boolean allowCredentials;

    @Value("${cors.max-age:3600}")
    private long maxAge;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Origens permitidas
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins));

        // Métodos permitidos
        configuration.setAllowedMethods(Arrays.asList(allowedMethods));

        // Headers permitidos
        configuration.setAllowedHeaders(Arrays.asList(allowedHeaders));

        // Headers expostos
        configuration.setExposedHeaders(Arrays.asList(exposedHeaders));

        // Credenciais
        configuration.setAllowCredentials(allowCredentials);

        // Tempo máximo de cache
        configuration.setMaxAge(maxAge);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
package consultorio.infrastructure.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.LocalTime;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "fila-espera")
public class FilaEsperaProperties {

    private TentativasContato tentativasContato = new TentativasContato();
    private int diasExpiracao = 30;
    private Notificacao notificacao = new Notificacao();

    @Getter
    @Setter
    public static class TentativasContato {
        private int maxima = 3;
    }

    @Getter
    @Setter
    public static class Notificacao {
        private LocalTime horario = LocalTime.of(10, 0);
    }
}
package consultorio.infrastructure.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.LocalTime;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "agendamento")
public class AgendamentoProperties {

    private Horario horario = new Horario();
    private Duracao duracao = new Duracao();
    private Capacidade capacidade = new Capacidade();
    private Lembrete lembrete = new Lembrete();
    private Cron cron = new Cron();

    @Getter
    @Setter
    public static class Horario {
        private LocalTime inicioManha = LocalTime.of(9, 0);
        private LocalTime fimManha = LocalTime.of(12, 0);
        private LocalTime inicioTarde = LocalTime.of(14, 0);
        private LocalTime fimTarde = LocalTime.of(19, 0);
    }

    @Getter
    @Setter
    public static class Duracao {
        private int minima = 20; // minutos
        private int maxima = 240; // minutos
        private int padrao = 60; // minutos
    }

    @Getter
    @Setter
    public static class Capacidade {
        private int maximaPorDia = 20;
    }

    @Getter
    @Setter
    public static class Lembrete {
        private int antecedenciaDias = 1;
        private LocalTime horarioEnvio = LocalTime.of(9, 0);
    }

    @Getter
    @Setter
    public static class Cron {
        private String enviarLembretes = "0 0 9 * * *";
        private String processarFila = "0 0 */2 * * *";
        private String expirarFilas = "0 0 3 * * *";
        private String marcarFaltas = "0 0 23 * * *";
    }
}
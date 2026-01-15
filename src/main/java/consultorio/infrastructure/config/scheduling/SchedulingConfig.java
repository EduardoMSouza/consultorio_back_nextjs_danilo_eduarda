package consultorio.infrastructure.config.scheduling;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Configuração para habilitar agendamento de tarefas (Scheduled Tasks)
 *
 * As tarefas agendadas incluem:
 * - Envio de lembretes diários
 * - Processamento automático da fila de espera
 * - Expiração de filas antigas
 * - Marcação automática de faltas
 */
@Configuration
@EnableScheduling
public class SchedulingConfig {
    // A anotação @EnableScheduling já habilita o suporte a @Scheduled
}
package consultorio.infrastructure.scheduler;

import consultorio.config.FilaEsperaProperties;
import consultorio.domain.service.FilaEsperaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Jobs agendados para gerenciamento automático da fila de espera
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FilaEsperaScheduler {

    private final FilaEsperaService filaEsperaService;
    private final FilaEsperaProperties properties;

    /**
     * Processa a fila de espera automaticamente
     * Busca por vagas disponíveis e notifica pacientes
     * Execução: A cada 2 horas (configurável via properties)
     */
    @Scheduled(cron = "${agendamento.cron.processar-fila}")
    public void processarFilaAutomaticamente() {
        log.info("Iniciando processamento automático da fila de espera");

        try {
            filaEsperaService.processarFilaAutomaticamente();
            log.info("Processamento da fila concluído com sucesso");
        } catch (Exception e) {
            log.error("Erro ao processar fila de espera automaticamente", e);
        }
    }

    /**
     * Expira filas de espera antigas
     * Remove da fila registros com data preferencial vencida
     * Execução: Diariamente às 03:00 (configurável via properties)
     */
    @Scheduled(cron = "${agendamento.cron.expirar-filas}")
    public void expirarFilasAnteriores() {
        log.info("Iniciando expiração de filas antigas");

        try {
            filaEsperaService.expirarFilasAnteriores();
            log.info("Expiração de filas concluída com sucesso");
        } catch (Exception e) {
            log.error("Erro ao expirar filas antigas", e);
        }
    }

    /**
     * Envia notificações pendentes
     * Tenta reenviar notificações que falharam anteriormente
     * Execução: A cada 4 horas
     */
    @Scheduled(cron = "0 0 */4 * * *")
    public void enviarNotificacoesPendentes() {
        log.info("Iniciando envio de notificações pendentes");

        try {
            filaEsperaService.enviarNotificacoesPendentes();
            log.info("Notificações pendentes enviadas com sucesso");
        } catch (Exception e) {
            log.error("Erro ao enviar notificações pendentes", e);
        }
    }

    /**
     * Relatório de status da fila
     * Gera logs sobre o estado atual da fila de espera
     * Execução: Diariamente às 08:00
     */
    @Scheduled(cron = "0 0 8 * * *")
    public void gerarRelatorioStatusFila() {
        log.info("Gerando relatório de status da fila de espera");

        try {
            Long totalAtivas = filaEsperaService.contarTotalAtivas();
            log.info("Total de filas ativas: {}", totalAtivas);

            // Aqui você pode adicionar mais métricas ou enviar relatório por email

        } catch (Exception e) {
            log.error("Erro ao gerar relatório de status da fila", e);
        }
    }
}
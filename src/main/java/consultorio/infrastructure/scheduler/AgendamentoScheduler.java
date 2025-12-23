package consultorio.infrastructure.scheduler;

import consultorio.config.AgendamentoProperties;
import consultorio.domain.service.AgendamentoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Jobs agendados para gerenciamento automático de agendamentos
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AgendamentoScheduler {

    private final AgendamentoService agendamentoService;
    private final AgendamentoProperties properties;

    /**
     * Envia lembretes para consultas do próximo dia
     * Execução: Diariamente às 09:00 (configurável via properties)
     */
    @Scheduled(cron = "${agendamento.cron.enviar-lembretes}")
    public void enviarLembretesDiarios() {
        log.info("Iniciando envio de lembretes diários");

        try {
            LocalDate dataLembrete = LocalDate.now().plusDays(properties.getLembrete().getAntecedenciaDias());
            agendamentoService.enviarLembretes(dataLembrete);

            log.info("Lembretes enviados com sucesso para data: {}", dataLembrete);
        } catch (Exception e) {
            log.error("Erro ao enviar lembretes diários", e);
        }
    }

    /**
     * Marca como falta consultas que não foram finalizadas
     * Execução: Diariamente às 23:00 (configurável via properties)
     */
    @Scheduled(cron = "${agendamento.cron.marcar-faltas}")
    public void marcarFaltasAutomaticamente() {
        log.info("Iniciando marcação automática de faltas");

        try {
            var consultasPassadas = agendamentoService.buscarConsultasPassadasNaoFinalizadas();

            int totalFaltas = 0;
            for (var consulta : consultasPassadas) {
                try {
                    agendamentoService.marcarFalta(consulta.getId());
                    totalFaltas++;
                } catch (Exception e) {
                    log.error("Erro ao marcar falta para agendamento {}", consulta.getId(), e);
                }
            }

            log.info("Total de {} faltas marcadas automaticamente", totalFaltas);
        } catch (Exception e) {
            log.error("Erro ao processar marcação automática de faltas", e);
        }
    }

    /**
     * Limpeza de dados antigos (opcional)
     * Pode ser usado para arquivar ou limpar agendamentos muito antigos
     */
    @Scheduled(cron = "0 0 2 1 * *") // Todo dia 1 do mês às 02:00
    public void limpezaDeDadosAntigos() {
        log.info("Iniciando limpeza de dados antigos");

        // Implementar lógica de limpeza/arquivamento se necessário

        log.info("Limpeza de dados concluída");
    }
}
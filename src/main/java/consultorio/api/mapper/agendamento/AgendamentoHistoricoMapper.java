package consultorio.api.mapper.agendamento;

import consultorio.api.dto.response.agendamento.historico.AgendamentoHistoricoResponse;
import consultorio.domain.entity.agendamento.AgendamentoHistorico;
import org.springframework.stereotype.Component;

@Component
public class AgendamentoHistoricoMapper {

    public AgendamentoHistoricoResponse toResponse(AgendamentoHistorico historico) {
        AgendamentoHistoricoResponse response = new AgendamentoHistoricoResponse();
        response.setId(historico.getId());
        response.setAgendamentoId(historico.getAgendamentoId());
        response.setAcao(historico.getAcao());
        response.setStatusAnterior(historico.getStatusAnterior());
        response.setStatusNovo(historico.getStatusNovo());
        response.setUsuarioResponsavel(historico.getUsuarioResponsavel());
        response.setDescricao(historico.getDescricao());
        response.setDetalhes(historico.getDetalhes());
        response.setDataHora(historico.getDataHora());
        response.setIpOrigem(historico.getIpOrigem());
        return response;
    }
}
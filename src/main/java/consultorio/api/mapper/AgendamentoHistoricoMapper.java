package consultorio.api.mapper;

import consultorio.api.dto.response.AgendamentoHistoricoResponse;
import consultorio.domain.entity.AgendamentoHistorico;
import org.springframework.stereotype.Component;

@Component
public class AgendamentoHistoricoMapper {

    public AgendamentoHistoricoResponse toResponse(AgendamentoHistorico entity) {
        return new AgendamentoHistoricoResponse(
                entity.getId(),
                entity.getAgendamentoId(),
                entity.getAcao(),
                entity.getAcao() != null ? entity.getAcao().getDescricao() : null,
                entity.getStatusAnterior(),
                entity.getStatusNovo(),
                entity.getUsuarioResponsavel(),
                entity.getDescricao(),
                entity.getDetalhes(),
                entity.getDataHora(),
                entity.getIpOrigem()
        );
    }
}
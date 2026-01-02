package consultorio.api.mapper;

import consultorio.api.dto.response.AgendamentoHistoricoResponse;
import consultorio.domain.entity.AgendamentoHistorico;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AgendamentoHistoricoMapper {

    public AgendamentoHistoricoResponse toResponse(AgendamentoHistorico h) {
        AgendamentoHistoricoResponse r = new AgendamentoHistoricoResponse();
        r.setId(h.getId());
        r.setAgendamentoId(h.getAgendamentoId());
        r.setAcao(h.getAcao());
        r.setAcaoDescricao(h.getAcao() != null ? h.getAcao().getDescricao() : null);
        r.setStatusAnterior(h.getStatusAnterior());
        r.setStatusAnteriorDescricao(h.getStatusAnterior() != null ? h.getStatusAnterior().getDescricao() : null);
        r.setStatusNovo(h.getStatusNovo());
        r.setStatusNovoDescricao(h.getStatusNovo() != null ? h.getStatusNovo().getDescricao() : null);
        r.setUsuarioResponsavel(h.getUsuarioResponsavel());
        r.setDescricao(h.getDescricao());
        r.setDetalhes(h.getDetalhes());
        r.setDataHora(h.getDataHora());
        r.setIpOrigem(h.getIpOrigem());
        return r;
    }

    public List<AgendamentoHistoricoResponse> toResponseList(List<AgendamentoHistorico> historicos) {
        return historicos.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
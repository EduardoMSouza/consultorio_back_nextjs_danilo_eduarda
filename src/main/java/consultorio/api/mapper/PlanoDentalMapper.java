package consultorio.api.mapper;

import consultorio.api.dto.request.PlanoDentalRequest;
import consultorio.api.dto.response.PlanoDentalResponse;
import consultorio.domain.entity.PlanoDental;
import org.springframework.stereotype.Component;

@Component
public class PlanoDentalMapper {

    public PlanoDental toEntity(PlanoDentalRequest request) {
        return PlanoDental.builder()
                .pacienteId(request.pacienteId())
                .dentistaId(request.dentistaId())
                .dente(request.dente())
                .procedimento(request.procedimento())
                .valor(request.valor())
                .status(request.status() != null ? request.status() : PlanoDental.StatusPlano.PENDENTE)
                .observacoes(request.observacoes())
                .build();
    }

    public PlanoDentalResponse toResponse(PlanoDental entity) {
        return new PlanoDentalResponse(
                entity.getId(),
                entity.getPacienteId(),
                entity.getDentistaId(),
                entity.getDente(),
                entity.getProcedimento(),
                entity.getValor(),
                entity.getStatus(),
                entity.getObservacoes(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm()
        );
    }

    public void updateEntityFromRequest(PlanoDentalRequest request, PlanoDental entity) {
        entity.setPacienteId(request.pacienteId());
        entity.setDentistaId(request.dentistaId());
        entity.setDente(request.dente());
        entity.setProcedimento(request.procedimento());
        entity.setValor(request.valor());
        if (request.status() != null) {
            entity.setStatus(request.status());
        }
        entity.setObservacoes(request.observacoes());
    }
}

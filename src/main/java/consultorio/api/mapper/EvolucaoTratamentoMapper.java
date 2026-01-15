package consultorio.api.mapper;

import consultorio.api.dto.request.EvolucaoTratamentoRequest;
import consultorio.api.dto.response.EvolucaoTratamentoResponse;
import consultorio.domain.entity.EvolucaoTratamento;
import org.springframework.stereotype.Component;

@Component
public class EvolucaoTratamentoMapper {

    public EvolucaoTratamento toEntity(EvolucaoTratamentoRequest request) {
        return EvolucaoTratamento.builder()
                .pacienteId(request.pacienteId())
                .dentistaId(request.dentistaId())
                .dataEvolucao(request.dataEvolucao())
                .descricao(request.descricao())
                .procedimentosRealizados(request.procedimentosRealizados())
                .observacoes(request.observacoes())
                .proximaConsulta(request.proximaConsulta())
                .build();
    }

    public EvolucaoTratamentoResponse toResponse(EvolucaoTratamento entity) {
        return new EvolucaoTratamentoResponse(
                entity.getId(),
                entity.getPacienteId(),
                entity.getDentistaId(),
                entity.getDataEvolucao(),
                entity.getDescricao(),
                entity.getProcedimentosRealizados(),
                entity.getObservacoes(),
                entity.getProximaConsulta(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm()
        );
    }

    public void updateEntityFromRequest(EvolucaoTratamentoRequest request, EvolucaoTratamento entity) {
        entity.setPacienteId(request.pacienteId());
        entity.setDentistaId(request.dentistaId());
        entity.setDataEvolucao(request.dataEvolucao());
        entity.setDescricao(request.descricao());
        entity.setProcedimentosRealizados(request.procedimentosRealizados());
        entity.setObservacoes(request.observacoes());
        entity.setProximaConsulta(request.proximaConsulta());
    }
}

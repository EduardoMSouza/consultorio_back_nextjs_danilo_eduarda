package consultorio.api.mapper;

import consultorio.api.dto.request.FilaEsperaRequest;
import consultorio.api.dto.response.FilaEsperaResponse;
import consultorio.domain.entity.Dentista;
import consultorio.domain.entity.FilaEspera;
import consultorio.domain.entity.Paciente;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FilaEsperaMapper {

    public FilaEspera toEntity(FilaEsperaRequest request, Paciente paciente, Dentista dentista) {
        FilaEspera filaEspera = new FilaEspera();

        filaEspera.setPaciente(paciente);
        filaEspera.setDentista(dentista); // Pode ser null
        filaEspera.setTipoProcedimento(request.getTipoProcedimento());
        filaEspera.setDataPreferencial(request.getDataPreferencial());
        filaEspera.setHoraInicioPreferencial(request.getHoraInicioPreferencial());
        filaEspera.setHoraFimPreferencial(request.getHoraFimPreferencial());
        filaEspera.setPeriodoPreferencial(request.getPeriodoPreferencial());
        filaEspera.setObservacoes(request.getObservacoes());
        filaEspera.setPrioridade(request.getPrioridade() != null ? request.getPrioridade() : 0);
        filaEspera.setAceitaQualquerHorario(request.getAceitaQualquerHorario());
        filaEspera.setAceitaQualquerDentista(request.getAceitaQualquerDentista());

        return filaEspera;
    }

    public FilaEsperaResponse toResponse(FilaEspera entity) {
        return toResponse(entity, null);
    }

    public FilaEsperaResponse toResponse(FilaEspera entity, Integer posicaoFila) {
        FilaEsperaResponse response = new FilaEsperaResponse();

        response.setId(entity.getId());
        response.setTipoProcedimento(entity.getTipoProcedimento());
        response.setTipoProcedimentoDescricao(entity.getTipoProcedimento() != null ?
                entity.getTipoProcedimento().getDescricao() : null);
        response.setDataPreferencial(entity.getDataPreferencial());
        response.setHoraInicioPreferencial(entity.getHoraInicioPreferencial());
        response.setHoraFimPreferencial(entity.getHoraFimPreferencial());
        response.setPeriodoPreferencial(entity.getPeriodoPreferencial());
        response.setPeriodoPreferencialDescricao(entity.getPeriodoPreferencial() != null ?
                entity.getPeriodoPreferencial().getDescricao() : null);
        response.setStatus(entity.getStatus());
        response.setStatusDescricao(entity.getStatus() != null ? entity.getStatus().getDescricao() : null);
        response.setObservacoes(entity.getObservacoes());
        response.setPrioridade(entity.getPrioridade());
        response.setAceitaQualquerHorario(entity.getAceitaQualquerHorario());
        response.setAceitaQualquerDentista(entity.getAceitaQualquerDentista());

        // Auditoria
        response.setCriadoEm(entity.getCriadoEm());
        response.setAtualizadoEm(entity.getAtualizadoEm());
        response.setCriadoPor(entity.getCriadoPor());

        // Conversão
        response.setAgendamentoId(entity.getAgendamento() != null ? entity.getAgendamento().getId() : null);
        response.setConvertidoEm(entity.getConvertidoEm());

        // Notificação
        response.setNotificado(entity.getNotificado());
        response.setNotificadoEm(entity.getNotificadoEm());
        response.setTentativasContato(entity.getTentativasContato());
        response.setUltimaTentativaContato(entity.getUltimaTentativaContato());

        // Flags
        response.setIsAtiva(entity.isAtiva());
        response.setIsExpirada(entity.isExpirado());
        response.setPosicaoFila(posicaoFila);

        // Dados do Paciente
        if (entity.getPaciente() != null) {
            response.setPacienteId(entity.getPaciente().getId());
            response.setPacienteNome(entity.getPaciente().getDadosBasicos().getNome());
            response.setPacienteTelefone(entity.getPaciente().getDadosBasicos().getTelefone());
            response.setPacienteEmail(entity.getPaciente().getDadosBasicos().getEmail());
        }

        // Dados do Dentista
        if (entity.getDentista() != null) {
            response.setDentistaId(entity.getDentista().getId());
            response.setDentistaNome(entity.getDentista().getNome());
        }

        return response;
    }

    public List<FilaEsperaResponse> toResponseList(List<FilaEspera> entities) {
        return entities.stream()
                .map(entity -> toResponse(entity, entities.indexOf(entity) + 1))
                .collect(Collectors.toList());
    }

    public void updateEntityFromRequest(FilaEsperaRequest request, FilaEspera entity,
                                        Paciente paciente, Dentista dentista) {
        entity.setPaciente(paciente);
        entity.setDentista(dentista);
        entity.setTipoProcedimento(request.getTipoProcedimento());
        entity.setDataPreferencial(request.getDataPreferencial());
        entity.setHoraInicioPreferencial(request.getHoraInicioPreferencial());
        entity.setHoraFimPreferencial(request.getHoraFimPreferencial());
        entity.setPeriodoPreferencial(request.getPeriodoPreferencial());
        entity.setObservacoes(request.getObservacoes());
        entity.setPrioridade(request.getPrioridade() != null ? request.getPrioridade() : entity.getPrioridade());
        entity.setAceitaQualquerHorario(request.getAceitaQualquerHorario());
        entity.setAceitaQualquerDentista(request.getAceitaQualquerDentista());
    }
}
package consultorio.api.mapper.agendamento;

import consultorio.api.dto.request.agendamento.fila_espera.FilaEsperaRequest;
import consultorio.api.dto.response.agendamento.fila_espera.FilaEsperaResponse;
import consultorio.domain.entity.agendamento.FilaEspera;
import consultorio.domain.entity.pessoa.Dentista;
import consultorio.domain.entity.pessoa.Paciente;
import org.springframework.stereotype.Component;

@Component
public class FilaEsperaMapper {

    public FilaEspera toEntity(FilaEsperaRequest request, Paciente paciente, Dentista dentista) {
        FilaEspera filaEspera = new FilaEspera();
        filaEspera.setPaciente(paciente);
        filaEspera.setDentista(dentista);
        filaEspera.setTipoProcedimento(request.getTipoProcedimento());
        filaEspera.setDataPreferencial(request.getDataPreferencial());
        filaEspera.setHoraInicioPreferencial(request.getHoraInicioPreferencial());
        filaEspera.setHoraFimPreferencial(request.getHoraFimPreferencial());
        filaEspera.setPeriodoPreferencial(request.getPeriodoPreferencial());
        filaEspera.setObservacoes(request.getObservacoes());
        filaEspera.setPrioridade(request.getPrioridade() != null ? request.getPrioridade() : 0);
        filaEspera.setAceitaQualquerHorario(request.getAceitaQualquerHorario() != null ? request.getAceitaQualquerHorario() : false);
        filaEspera.setAceitaQualquerDentista(request.getAceitaQualquerDentista() != null ? request.getAceitaQualquerDentista() : false);
        filaEspera.setCriadoPor(request.getCriadoPor());
        return filaEspera;
    }

    public FilaEsperaResponse toResponse(FilaEspera filaEspera) {
        FilaEsperaResponse response = new FilaEsperaResponse();
        response.setId(filaEspera.getId());
        response.setPacienteId(filaEspera.getPaciente() != null ? filaEspera.getPaciente().getId() : null);
        response.setNomePaciente(filaEspera.getNomePaciente());
        response.setDentistaId(filaEspera.getDentista() != null ? filaEspera.getDentista().getId() : null);
        response.setNomeDentista(filaEspera.getNomeDentista());
        response.setTipoProcedimento(filaEspera.getTipoProcedimento());
        response.setDataPreferencial(filaEspera.getDataPreferencial());
        response.setHoraInicioPreferencial(filaEspera.getHoraInicioPreferencial());
        response.setHoraFimPreferencial(filaEspera.getHoraFimPreferencial());
        response.setPeriodoPreferencial(filaEspera.getPeriodoPreferencial());
        response.setStatus(filaEspera.getStatus());
        response.setObservacoes(filaEspera.getObservacoes());
        response.setPrioridade(filaEspera.getPrioridade());
        response.setAceitaQualquerHorario(filaEspera.getAceitaQualquerHorario());
        response.setAceitaQualquerDentista(filaEspera.getAceitaQualquerDentista());
        response.setCriadoEm(filaEspera.getCriadoEm());
        response.setAtualizadoEm(filaEspera.getAtualizadoEm());
        response.setCriadoPor(filaEspera.getCriadoPor());
        response.setAgendamentoId(filaEspera.getAgendamento() != null ? filaEspera.getAgendamento().getId() : null);
        response.setConvertidoEm(filaEspera.getConvertidoEm());
        response.setNotificado(filaEspera.getNotificado());
        response.setNotificadoEm(filaEspera.getNotificadoEm());
        response.setTentativasContato(filaEspera.getTentativasContato());
        response.setUltimaTentativaContato(filaEspera.getUltimaTentativaContato());
        response.setAtiva(filaEspera.isAtiva());
        response.setExpirado(filaEspera.isExpirado());
        return response;
    }

    public void updateEntityFromRequest(FilaEsperaRequest request, FilaEspera filaEspera, Paciente paciente, Dentista dentista) {
        filaEspera.setPaciente(paciente);
        filaEspera.setDentista(dentista);
        filaEspera.setTipoProcedimento(request.getTipoProcedimento());
        filaEspera.setDataPreferencial(request.getDataPreferencial());
        filaEspera.setHoraInicioPreferencial(request.getHoraInicioPreferencial());
        filaEspera.setHoraFimPreferencial(request.getHoraFimPreferencial());
        filaEspera.setPeriodoPreferencial(request.getPeriodoPreferencial());
        filaEspera.setObservacoes(request.getObservacoes());
        if (request.getPrioridade() != null) {
            filaEspera.setPrioridade(request.getPrioridade());
        }
        if (request.getAceitaQualquerHorario() != null) {
            filaEspera.setAceitaQualquerHorario(request.getAceitaQualquerHorario());
        }
        if (request.getAceitaQualquerDentista() != null) {
            filaEspera.setAceitaQualquerDentista(request.getAceitaQualquerDentista());
        }
    }
}
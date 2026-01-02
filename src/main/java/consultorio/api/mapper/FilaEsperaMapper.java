package consultorio.api.mapper;

import consultorio.api.dto.request.FilaEsperaRequest;
import consultorio.api.dto.response.FilaEsperaResponse;
import consultorio.domain.entity.Dentista;
import consultorio.domain.entity.FilaEspera;
import consultorio.domain.entity.Paciente;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FilaEsperaMapper {

    public FilaEspera toEntity(FilaEsperaRequest request, Paciente paciente, Dentista dentista) {
        FilaEspera fila = new FilaEspera();
        fila.setPaciente(paciente);
        fila.setDentista(dentista);
        fila.setTipoProcedimento(request.getTipoProcedimento());
        fila.setDataPreferencial(request.getDataPreferencial());
        fila.setHoraInicioPreferencial(request.getHoraInicioPreferencial());
        fila.setHoraFimPreferencial(request.getHoraFimPreferencial());
        fila.setPeriodoPreferencial(request.getPeriodoPreferencial());
        fila.setObservacoes(request.getObservacoes());
        fila.setPrioridade(request.getPrioridade() != null ? request.getPrioridade() : 0);
        fila.setAceitaQualquerHorario(request.getAceitaQualquerHorario() != null ? request.getAceitaQualquerHorario() : false);
        fila.setAceitaQualquerDentista(request.getAceitaQualquerDentista() != null ? request.getAceitaQualquerDentista() : false);
        return fila;
    }

    public void updateEntityFromRequest(FilaEsperaRequest request, FilaEspera fila, Paciente paciente, Dentista dentista) {
        fila.setPaciente(paciente);
        fila.setDentista(dentista);
        fila.setTipoProcedimento(request.getTipoProcedimento());
        fila.setDataPreferencial(request.getDataPreferencial());
        fila.setHoraInicioPreferencial(request.getHoraInicioPreferencial());
        fila.setHoraFimPreferencial(request.getHoraFimPreferencial());
        fila.setPeriodoPreferencial(request.getPeriodoPreferencial());
        fila.setObservacoes(request.getObservacoes());
        fila.setPrioridade(request.getPrioridade() != null ? request.getPrioridade() : fila.getPrioridade());
        fila.setAceitaQualquerHorario(request.getAceitaQualquerHorario() != null ? request.getAceitaQualquerHorario() : fila.getAceitaQualquerHorario());
        fila.setAceitaQualquerDentista(request.getAceitaQualquerDentista() != null ? request.getAceitaQualquerDentista() : fila.getAceitaQualquerDentista());
    }

    public FilaEsperaResponse toResponse(FilaEspera f) {
        FilaEsperaResponse r = new FilaEsperaResponse();
        r.setId(f.getId());

        // Paciente
        if (f.getPaciente() != null) {
            r.setPacienteId(f.getPaciente().getId());
            r.setPacienteNome(f.getPaciente().getDadosBasicos().getNome());
            r.setPacienteTelefone(f.getPaciente().getDadosBasicos().getTelefone());
            r.setPacienteEmail(f.getPaciente().getDadosBasicos().getEmail());
        }

        // Dentista
        if (f.getDentista() != null) {
            r.setDentistaId(f.getDentista().getId());
            r.setDentistaNome(f.getDentista().getNome());
        }

        // Preferências
        r.setTipoProcedimento(f.getTipoProcedimento());
        r.setTipoProcedimentoDescricao(f.getTipoProcedimento() != null ? f.getTipoProcedimento().getDescricao() : null);
        r.setDataPreferencial(f.getDataPreferencial());
        r.setHoraInicioPreferencial(f.getHoraInicioPreferencial());
        r.setHoraFimPreferencial(f.getHoraFimPreferencial());
        r.setPeriodoPreferencial(f.getPeriodoPreferencial());
        r.setPeriodoPreferencialDescricao(f.getPeriodoPreferencial() != null ? f.getPeriodoPreferencial().getDescricao() : null);

        // Status
        r.setStatus(f.getStatus());
        r.setStatusDescricao(f.getStatus().getDescricao());
        r.setObservacoes(f.getObservacoes());
        r.setPrioridade(f.getPrioridade());

        // Flags
        r.setAceitaQualquerHorario(f.getAceitaQualquerHorario());
        r.setAceitaQualquerDentista(f.getAceitaQualquerDentista());
        r.setAtiva(f.isAtiva());
        r.setExpirada(f.isExpirado());

        // Notificação
        r.setNotificado(f.getNotificado());
        r.setNotificadoEm(f.getNotificadoEm());
        r.setTentativasContato(f.getTentativasContato());
        r.setUltimaTentativaContato(f.getUltimaTentativaContato());

        // Conversão
        if (f.getAgendamento() != null) {
            r.setAgendamentoId(f.getAgendamento().getId());
        }
        r.setConvertidoEm(f.getConvertidoEm());

        // Auditoria
        r.setCriadoEm(f.getCriadoEm());
        r.setAtualizadoEm(f.getAtualizadoEm());
        r.setCriadoPor(f.getCriadoPor());

        return r;
    }

    public List<FilaEsperaResponse> toResponseList(List<FilaEspera> filas) {
        return filas.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
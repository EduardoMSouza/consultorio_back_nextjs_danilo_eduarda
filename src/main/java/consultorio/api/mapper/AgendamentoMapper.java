package consultorio.api.mapper;

import consultorio.api.dto.request.AgendamentoRequest;
import consultorio.api.dto.response.AgendamentoResponse;
import consultorio.api.dto.response.AgendamentoResumoResponse;
import consultorio.domain.entity.Agendamento;
import consultorio.domain.entity.Dentista;
import consultorio.domain.entity.Paciente;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AgendamentoMapper {

    public Agendamento toEntity(AgendamentoRequest request, Dentista dentista, Paciente paciente) {
        Agendamento agendamento = new Agendamento();

        agendamento.setDentista(dentista);
        agendamento.setPaciente(paciente);
        agendamento.setDataConsulta(request.getDataConsulta());
        agendamento.setHoraInicio(request.getHoraInicio());
        agendamento.setHoraFim(request.getHoraFim());
        agendamento.setTipoProcedimento(request.getTipoProcedimento());
        agendamento.setObservacoes(request.getObservacoes());
        agendamento.setValorConsulta(request.getValorConsulta());
        agendamento.setAtivo(true);

        return agendamento;
    }

    public AgendamentoResponse toResponse(Agendamento entity) {
        AgendamentoResponse response = new AgendamentoResponse();

        response.setId(entity.getId());
        response.setDataConsulta(entity.getDataConsulta());
        response.setHoraInicio(entity.getHoraInicio());
        response.setHoraFim(entity.getHoraFim());
        response.setDuracaoMinutos(entity.getDuracaoEmMinutos());
        response.setStatus(entity.getStatus());
        response.setStatusDescricao(entity.getStatus() != null ? entity.getStatus().getDescricao() : null);
        response.setTipoProcedimento(entity.getTipoProcedimento());
        response.setTipoProcedimentoDescricao(entity.getTipoProcedimento() != null ?
                entity.getTipoProcedimento().getDescricao() : null);
        response.setObservacoes(entity.getObservacoes());
        response.setValorConsulta(entity.getValorConsulta());

        // Flags
        response.setAtivo(entity.getAtivo());
        response.setPodeSerEditado(entity.isPodeSerEditado());
        response.setPodeSerCancelado(entity.isPodeSerCancelado());
        response.setIsFinalizado(entity.isFinalizado());
        response.setIsHoje(entity.isHoje());
        response.setIsPassado(entity.isConsultaPassada());
        response.setLembreteEnviado(entity.getLembreteEnviado());

        // Auditoria
        response.setCriadoEm(entity.getCriadoEm());
        response.setAtualizadoEm(entity.getAtualizadoEm());
        response.setCriadoPor(entity.getCriadoPor());
        response.setAtualizadoPor(entity.getAtualizadoPor());

        // Confirmação
        response.setConfirmadoEm(entity.getConfirmadoEm());

        // Cancelamento
        response.setCanceladoPor(entity.getCanceladoPor());
        response.setCanceladoEm(entity.getCanceladoEm());
        response.setMotivoCancelamento(entity.getMotivoCancelamento());

        // Dados do Dentista
        if (entity.getDentista() != null) {
            response.setDentistaId(entity.getDentista().getId());
            response.setDentistaNome(entity.getDentista().getNome());
            response.setDentistaCro(entity.getDentista().getCro());
            response.setDentistaEspecialidade(entity.getDentista().getEspecialidade());
        }

        // Dados do Paciente
        if (entity.getPaciente() != null) {
            response.setPacienteId(entity.getPaciente().getId());
            response.setPacienteNome(entity.getPaciente().getDadosBasicos().getNome());
            response.setPacienteTelefone(entity.getPaciente().getDadosBasicos().getTelefone());
            response.setPacienteEmail(entity.getPaciente().getDadosBasicos().getEmail());
            response.setPacienteProntuario(entity.getPaciente().getDadosBasicos().getProntuarioNumero());
        }

        return response;
    }

    public AgendamentoResumoResponse toResumoResponse(Agendamento entity) {
        AgendamentoResumoResponse response = new AgendamentoResumoResponse();

        response.setId(entity.getId());
        response.setDataConsulta(entity.getDataConsulta());
        response.setHoraInicio(entity.getHoraInicio());
        response.setHoraFim(entity.getHoraFim());
        response.setDuracaoMinutos(entity.getDuracaoEmMinutos());
        response.setStatus(entity.getStatus());
        response.setStatusDescricao(entity.getStatus() != null ? entity.getStatus().getDescricao() : null);
        response.setTipoProcedimento(entity.getTipoProcedimento());
        response.setTipoProcedimentoDescricao(entity.getTipoProcedimento() != null ?
                entity.getTipoProcedimento().getDescricao() : null);

        // Flags
        response.setIsHoje(entity.isHoje());
        response.setIsPassado(entity.isConsultaPassada());
        response.setLembreteEnviado(entity.getLembreteEnviado());

        // Dados do Dentista
        if (entity.getDentista() != null) {
            response.setDentistaId(entity.getDentista().getId());
            response.setDentistaNome(entity.getDentista().getNome());
        }

        // Dados do Paciente
        if (entity.getPaciente() != null) {
            response.setPacienteId(entity.getPaciente().getId());
            response.setPacienteNome(entity.getPaciente().getDadosBasicos().getNome());
            response.setPacienteTelefone(entity.getPaciente().getDadosBasicos().getTelefone());
        }

        return response;
    }

    public List<AgendamentoResponse> toResponseList(List<Agendamento> entities) {
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<AgendamentoResumoResponse> toResumoResponseList(List<Agendamento> entities) {
        return entities.stream()
                .map(this::toResumoResponse)
                .collect(Collectors.toList());
    }

    public void updateEntityFromRequest(AgendamentoRequest request, Agendamento entity,
                                        Dentista dentista, Paciente paciente) {
        entity.setDentista(dentista);
        entity.setPaciente(paciente);
        entity.setDataConsulta(request.getDataConsulta());
        entity.setHoraInicio(request.getHoraInicio());
        entity.setHoraFim(request.getHoraFim());
        entity.setTipoProcedimento(request.getTipoProcedimento());
        entity.setObservacoes(request.getObservacoes());
        entity.setValorConsulta(request.getValorConsulta());
    }
}
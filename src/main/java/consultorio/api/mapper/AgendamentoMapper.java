package consultorio.api.mapper;

import consultorio.api.dto.request.AgendamentoRequest;
import consultorio.api.dto.response.AgendamentoResponse;
import consultorio.api.dto.response.AgendamentoResumoResponse;
import consultorio.domain.entity.Agendamento;
import consultorio.domain.entity.Agendamento.StatusAgendamento;
import consultorio.domain.entity.Dentista;
import consultorio.domain.entity.Paciente;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
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
        agendamento.setStatus(StatusAgendamento.AGENDADO);
        agendamento.setAtivo(true);
        return agendamento;
    }

    public void updateEntityFromRequest(AgendamentoRequest request, Agendamento agendamento, Dentista dentista, Paciente paciente) {
        agendamento.setDentista(dentista);
        agendamento.setPaciente(paciente);
        agendamento.setDataConsulta(request.getDataConsulta());
        agendamento.setHoraInicio(request.getHoraInicio());
        agendamento.setHoraFim(request.getHoraFim());
        agendamento.setTipoProcedimento(request.getTipoProcedimento());
        agendamento.setObservacoes(request.getObservacoes());
        agendamento.setValorConsulta(request.getValorConsulta());
    }

    public AgendamentoResponse toResponse(Agendamento a) {
        AgendamentoResponse r = new AgendamentoResponse();
        r.setId(a.getId());

        // Dentista
        if (a.getDentista() != null) {
            r.setDentistaId(a.getDentista().getId());
            r.setDentistaNome(a.getDentista().getNome());
            r.setDentistaCro(a.getDentista().getCro());
            r.setDentistaEspecialidade(a.getDentista().getEspecialidade());
        }

        // Paciente
        if (a.getPaciente() != null) {
            r.setPacienteId(a.getPaciente().getId());
            r.setPacienteNome(a.getPaciente().getDadosBasicos().getNome());
            r.setPacienteTelefone(a.getPaciente().getDadosBasicos().getTelefone());
            r.setPacienteCpf(a.getPaciente().getDadosBasicos().getCpf());
        }

        // Agendamento
        r.setDataConsulta(a.getDataConsulta());
        r.setHoraInicio(a.getHoraInicio());
        r.setHoraFim(a.getHoraFim());
        r.setDuracaoMinutos(Duration.between(a.getHoraInicio(), a.getHoraFim()).toMinutes());
        r.setStatus(a.getStatus());
        r.setStatusDescricao(a.getStatus().getDescricao());
        r.setTipoProcedimento(a.getTipoProcedimento());
        r.setTipoProcedimentoDescricao(a.getTipoProcedimento() != null ? a.getTipoProcedimento().getDescricao() : null);
        r.setObservacoes(a.getObservacoes());
        r.setValorConsulta(a.getValorConsulta());

        // Flags
        r.setPodeEditar(a.isPodeSerEditado());
        r.setPodeCancelar(a.isPodeSerCancelado());
        r.setConsultaPassada(a.isConsultaPassada());
        r.setHoje(a.isHoje());

        // Confirmação/Lembrete
        r.setConfirmadoEm(a.getConfirmadoEm());
        r.setLembreteEnviado(a.getLembreteEnviado());
        r.setLembreteEnviadoEm(a.getLembreteEnviadoEm());

        // Cancelamento
        r.setMotivoCancelamento(a.getMotivoCancelamento());
        r.setCanceladoPor(a.getCanceladoPor());
        r.setCanceladoEm(a.getCanceladoEm());

        // Auditoria
        r.setCriadoEm(a.getCriadoEm());
        r.setAtualizadoEm(a.getAtualizadoEm());
        r.setCriadoPor(a.getCriadoPor());
        r.setAtualizadoPor(a.getAtualizadoPor());

        return r;
    }

    public AgendamentoResumoResponse toResumoResponse(Agendamento a) {
        AgendamentoResumoResponse r = new AgendamentoResumoResponse();
        r.setId(a.getId());
        r.setDataConsulta(a.getDataConsulta());
        r.setHoraInicio(a.getHoraInicio());
        r.setHoraFim(a.getHoraFim());
        r.setDuracaoMinutos(Duration.between(a.getHoraInicio(), a.getHoraFim()).toMinutes());

        if (a.getDentista() != null) {
            r.setDentistaId(a.getDentista().getId());
            r.setDentistaNome(a.getDentista().getNome());
        }

        if (a.getPaciente() != null) {
            r.setPacienteId(a.getPaciente().getId());
            r.setPacienteNome(a.getPaciente().getDadosBasicos().getNome());
            r.setPacienteTelefone(a.getPaciente().getDadosBasicos().getTelefone());
        }

        r.setStatus(a.getStatus());
        r.setStatusDescricao(a.getStatus().getDescricao());
        r.setTipoProcedimento(a.getTipoProcedimento());
        r.setTipoProcedimentoDescricao(a.getTipoProcedimento() != null ? a.getTipoProcedimento().getDescricao() : null);
        r.setConfirmado(a.getStatus() == StatusAgendamento.CONFIRMADO || a.getConfirmadoEm() != null);
        r.setHoje(a.getDataConsulta().equals(LocalDate.now()));

        return r;
    }

    public List<AgendamentoResumoResponse> toResumoResponseList(List<Agendamento> agendamentos) {
        return agendamentos.stream().map(this::toResumoResponse).collect(Collectors.toList());
    }
}
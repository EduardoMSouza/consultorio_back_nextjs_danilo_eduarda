package consultorio.api.mapper.agendamento;

import consultorio.api.dto.request.agendamento.AgendamentoRequest;
import consultorio.api.dto.response.agendamento.AgendamentoResponse;
import consultorio.domain.entity.agendamento.Agendamento;
import consultorio.domain.entity.agendamento.enums.StatusAgendamento;
import consultorio.domain.entity.pessoa.Dentista;
import consultorio.domain.entity.pessoa.Paciente;
import org.springframework.stereotype.Component;

@Component
public class AgendamentoMapper {

    public Agendamento toEntity(AgendamentoRequest request, Dentista dentista, Paciente paciente) {
        Agendamento agendamento = new Agendamento();
        agendamento.setDentista(dentista);
        agendamento.setPaciente(paciente);
        agendamento.setDataConsulta(request.getDataConsulta());
        agendamento.setHoraInicio(request.getHoraInicio());
        agendamento.setHoraFim(request.getHoraFim());
        agendamento.setStatus(request.getStatus() != null ? request.getStatus() : StatusAgendamento.AGENDADO);
        agendamento.setTipoProcedimento(request.getTipoProcedimento());
        agendamento.setObservacoes(request.getObservacoes());
        agendamento.setValorConsulta(request.getValorConsulta());
        agendamento.setCriadoPor(request.getCriadoPor());
        agendamento.setAtualizadoPor(request.getCriadoPor());
        return agendamento;
    }

    public AgendamentoResponse toResponse(Agendamento agendamento) {
        AgendamentoResponse response = new AgendamentoResponse();
        response.setId(agendamento.getId());
        response.setDentistaId(agendamento.getDentista() != null ? agendamento.getDentista().getId() : null);
        response.setNomeDentista(agendamento.getNomeDentista());
        response.setPacienteId(agendamento.getPaciente() != null ? agendamento.getPaciente().getId() : null);
        response.setNomePaciente(agendamento.getNomePaciente());
        response.setDataConsulta(agendamento.getDataConsulta());
        response.setHoraInicio(agendamento.getHoraInicio());
        response.setHoraFim(agendamento.getHoraFim());
        response.setStatus(agendamento.getStatus());
        response.setTipoProcedimento(agendamento.getTipoProcedimento());
        response.setObservacoes(agendamento.getObservacoes());
        response.setValorConsulta(agendamento.getValorConsulta());
        response.setAtivo(agendamento.getAtivo());
        response.setCriadoEm(agendamento.getCriadoEm());
        response.setAtualizadoEm(agendamento.getAtualizadoEm());
        response.setCriadoPor(agendamento.getCriadoPor());
        response.setAtualizadoPor(agendamento.getAtualizadoPor());
        response.setCanceladoPor(agendamento.getCanceladoPor());
        response.setCanceladoEm(agendamento.getCanceladoEm());
        response.setMotivoCancelamento(agendamento.getMotivoCancelamento());
        response.setConfirmadoEm(agendamento.getConfirmadoEm());
        response.setLembreteEnviado(agendamento.getLembreteEnviado());
        response.setLembreteEnviadoEm(agendamento.getLembreteEnviadoEm());
        response.setDuracaoEmMinutos(agendamento.getDuracaoEmMinutos());
        response.setPodeSerEditado(agendamento.isPodeSerEditado());
        response.setPodeSerCancelado(agendamento.isPodeSerCancelado());
        response.setFinalizado(agendamento.isFinalizado());
        response.setConsultaPassada(agendamento.isConsultaPassada());
        response.setHoje(agendamento.isHoje());
        return response;
    }

    public void updateEntityFromRequest(AgendamentoRequest request, Agendamento agendamento, Dentista dentista, Paciente paciente) {
        agendamento.setDentista(dentista);
        agendamento.setPaciente(paciente);
        agendamento.setDataConsulta(request.getDataConsulta());
        agendamento.setHoraInicio(request.getHoraInicio());
        agendamento.setHoraFim(request.getHoraFim());
        if (request.getStatus() != null) {
            agendamento.setStatus(request.getStatus());
        }
        agendamento.setTipoProcedimento(request.getTipoProcedimento());
        agendamento.setObservacoes(request.getObservacoes());
        agendamento.setValorConsulta(request.getValorConsulta());
    }
}
package consultorio.api.mapper;

import consultorio.api.dto.request.AgendamentoRequest;
import consultorio.api.dto.response.AgendamentoResponse;
import consultorio.api.dto.response.AgendamentoResumoResponse;
import consultorio.domain.entity.Agendamento;
import consultorio.domain.entity.Dentista;
import consultorio.domain.entity.Paciente;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AgendamentoMapper {

    private final ModelMapper modelMapper;

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

        return agendamento;
    }

    public AgendamentoResponse toResponse(Agendamento entity) {
        AgendamentoResponse response = new AgendamentoResponse();

        response.setId(entity.getId());
        response.setDataConsulta(entity.getDataConsulta());
        response.setHoraInicio(entity.getHoraInicio());
        response.setHoraFim(entity.getHoraFim());
        response.setStatus(entity.getStatus());
        response.setTipoProcedimento(entity.getTipoProcedimento());
        response.setObservacoes(entity.getObservacoes());
        response.setValorConsulta(entity.getValorConsulta());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());

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
            response.setPacienteNome(entity.getPaciente().getNome());
            response.setPacienteTelefone(entity.getPaciente().getTelefone());
            response.setPacienteProntuario(entity.getPaciente().getProntuarioNumero());
        }

        return response;
    }

    public AgendamentoResumoResponse toResumoResponse(Agendamento entity) {
        AgendamentoResumoResponse response = new AgendamentoResumoResponse();

        response.setId(entity.getId());
        response.setDataConsulta(entity.getDataConsulta());
        response.setHoraInicio(entity.getHoraInicio());
        response.setHoraFim(entity.getHoraFim());
        response.setStatus(entity.getStatus());
        response.setTipoProcedimento(entity.getTipoProcedimento());

        if (entity.getDentista() != null) {
            response.setDentistaNome(entity.getDentista().getNome());
        }

        if (entity.getPaciente() != null) {
            response.setPacienteNome(entity.getPaciente().getNome());
        }

        return response;
    }

    public List<AgendamentoResponse> toResponseList(List<Agendamento> entities) {
        return entities.stream()
                .map(this::toResponse)
                .toList();
    }

    public List<AgendamentoResumoResponse> toResumoResponseList(List<Agendamento> entities) {
        return entities.stream()
                .map(this::toResumoResponse)
                .toList();
    }

    public void updateEntityFromRequest(AgendamentoRequest request, Agendamento entity, Dentista dentista, Paciente paciente) {
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
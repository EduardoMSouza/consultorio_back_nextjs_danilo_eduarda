package consultorio.api.dto.response;

import consultorio.domain.entity.Agendamento.StatusAgendamento;
import consultorio.domain.entity.Agendamento.TipoProcedimento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoResponse {

    private Long id;

    // Dados do Dentista
    private Long dentistaId;
    private String dentistaNome;
    private String dentistaCro;
    private String dentistaEspecialidade;

    // Dados do Paciente
    private Long pacienteId;
    private String pacienteNome;
    private String pacienteTelefone;
    private String pacienteProntuario;

    // Dados do Agendamento
    private LocalDate dataConsulta;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private StatusAgendamento status;
    private TipoProcedimento tipoProcedimento;
    private String observacoes;
    private Double valorConsulta;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
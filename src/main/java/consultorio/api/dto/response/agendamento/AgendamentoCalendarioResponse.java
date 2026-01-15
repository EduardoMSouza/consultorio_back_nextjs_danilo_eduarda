package consultorio.api.dto.response.agendamento;// AgendamentoCalendarioResponse.java

import consultorio.domain.entity.agendamento.enums.StatusAgendamento;
import consultorio.domain.entity.agendamento.enums.TipoProcedimento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoCalendarioResponse {

    private Long id;
    private String title; // Nome do paciente
    private LocalDateTime start; // dataConsulta + horaInicio
    private LocalDateTime end; // dataConsulta + horaFim
    private StatusAgendamento status;
    private TipoProcedimento tipoProcedimento;
    private String backgroundColor; // Cor baseada no status
    private String borderColor;
    private String textColor;
    private Boolean editable;
    private String dentistaNome;
    private String pacienteNome;
    private String observacoes;
}
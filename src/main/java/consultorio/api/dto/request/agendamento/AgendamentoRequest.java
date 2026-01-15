package consultorio.api.dto.request.agendamento;

import consultorio.domain.entity.agendamento.enums.StatusAgendamento;
import consultorio.domain.entity.agendamento.enums.TipoProcedimento;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoRequest {

    @NotNull(message = "ID do dentista é obrigatório")
    private Long dentistaId;

    @NotNull(message = "ID do paciente é obrigatório")
    private Long pacienteId;

    @NotNull(message = "Data da consulta é obrigatória")
    @Future(message = "Data da consulta deve ser futura")
    private LocalDate dataConsulta;

    @NotNull(message = "Hora de início é obrigatória")
    private LocalTime horaInicio;

    @NotNull(message = "Hora de fim é obrigatória")
    private LocalTime horaFim;

    private StatusAgendamento status;

    private TipoProcedimento tipoProcedimento;

    @Size(max = 5000, message = "Observações não podem exceder 5000 caracteres")
    private String observacoes;

    @DecimalMin(value = "0.0", inclusive = false, message = "Valor da consulta deve ser positivo")
    private Double valorConsulta;

    private String criadoPor;
}
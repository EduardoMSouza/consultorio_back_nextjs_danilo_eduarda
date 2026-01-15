package consultorio.api.dto.request;

import consultorio.domain.entity.Agendamento.StatusAgendamento;
import consultorio.domain.entity.Agendamento.TipoProcedimento;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Dentista é obrigatório")
    private Long dentistaId;

    @NotNull(message = "Paciente é obrigatório")
    private Long pacienteId;

    @NotNull(message = "Data da consulta é obrigatória")
    private LocalDate dataConsulta;

    @NotNull(message = "Hora de início é obrigatória")
    private LocalTime horaInicio;

    @NotNull(message = "Hora de fim é obrigatória")
    private LocalTime horaFim;

    private TipoProcedimento tipoProcedimento;

    private String observacoes;

    private Double valorConsulta;
}
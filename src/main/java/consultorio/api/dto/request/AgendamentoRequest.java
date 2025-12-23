package consultorio.api.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import consultorio.domain.entity.Agendamento.TipoProcedimento;
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

    @NotNull(message = "Dentista é obrigatório")
    @Positive(message = "ID do dentista deve ser positivo")
    private Long dentistaId;

    @NotNull(message = "Paciente é obrigatório")
    @Positive(message = "ID do paciente deve ser positivo")
    private Long pacienteId;

    @NotNull(message = "Data da consulta é obrigatória")
    @FutureOrPresent(message = "Data da consulta deve ser hoje ou no futuro")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataConsulta;

    @NotNull(message = "Hora de início é obrigatória")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaInicio;

    @NotNull(message = "Hora de fim é obrigatória")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaFim;

    private TipoProcedimento tipoProcedimento;

    @Size(max = 1000, message = "Observações não podem ter mais de 1000 caracteres")
    private String observacoes;

    @DecimalMin(value = "0.0", inclusive = false, message = "Valor da consulta deve ser maior que zero")
    @DecimalMax(value = "999999.99", message = "Valor da consulta muito alto")
    private Double valorConsulta;
}
// AgendamentoUpdateRequest.java
package consultorio.api.dto.request.agendamento;


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
public class AgendamentoUpdateRequest {

    @NotNull(message = "Data da consulta é obrigatória")
    @FutureOrPresent(message = "Data da consulta deve ser hoje ou no futuro")
    private LocalDate dataConsulta;

    @NotNull(message = "Hora de início é obrigatória")
    private LocalTime horaInicio;

    @NotNull(message = "Hora de fim é obrigatória")
    private LocalTime horaFim;

    @NotNull(message = "Tipo de procedimento é obrigatório")
    private TipoProcedimento tipoProcedimento;

    @Size(max = 1000, message = "Observações deve ter no máximo 1000 caracteres")
    private String observacoes;

    @PositiveOrZero(message = "Valor da consulta deve ser positivo ou zero")
    private Double valorConsulta;

    private String atualizadoPor;
}
// FilaEsperaConversaoRequest.java
package consultorio.api.dto.request.agendamento;

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
public class FilaEsperaConversaoRequest {

    @NotNull(message = "Data da consulta é obrigatória")
    private LocalDate dataConsulta;

    @NotNull(message = "Hora de início é obrigatória")
    private LocalTime horaInicio;

    @NotNull(message = "Hora de fim é obrigatória")
    private LocalTime horaFim;

    private String observacoes;
    private Double valorConsulta;
    private String usuario;

    // Opcional: sobrescrever dentista
    private Long dentistaId;
}
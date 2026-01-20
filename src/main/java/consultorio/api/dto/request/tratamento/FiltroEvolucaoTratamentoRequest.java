package consultorio.api.dto.request.tratamento;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FiltroEvolucaoTratamentoRequest {

    private Long pacienteId;
    private Long dentistaId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataInicio;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataFim;

    private String nomePaciente;
    private String nomeDentista;
}
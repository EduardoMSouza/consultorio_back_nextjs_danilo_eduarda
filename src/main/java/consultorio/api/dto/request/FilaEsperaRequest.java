package consultorio.api.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import consultorio.domain.entity.Agendamento.TipoProcedimento;
import consultorio.domain.entity.FilaEspera.PeriodoPreferencial;
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
public class FilaEsperaRequest {

    @NotNull(message = "Paciente é obrigatório")
    @Positive(message = "ID do paciente deve ser positivo")
    private Long pacienteId;

    @Positive(message = "ID do dentista deve ser positivo")
    private Long dentistaId; // Opcional - se não informado, aceita qualquer dentista

    private TipoProcedimento tipoProcedimento;

    @FutureOrPresent(message = "Data preferencial deve ser hoje ou no futuro")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataPreferencial;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaInicioPreferencial;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaFimPreferencial;

    private PeriodoPreferencial periodoPreferencial;

    @Size(max = 1000, message = "Observações não podem ter mais de 1000 caracteres")
    private String observacoes;

    @Min(value = 0, message = "Prioridade deve ser maior ou igual a 0")
    @Max(value = 10, message = "Prioridade deve ser menor ou igual a 10")
    private Integer prioridade = 0;

    @NotNull(message = "Campo obrigatório")
    private Boolean aceitaQualquerHorario = false;

    @NotNull(message = "Campo obrigatório")
    private Boolean aceitaQualquerDentista = false;
}
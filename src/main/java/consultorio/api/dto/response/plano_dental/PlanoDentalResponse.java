package consultorio.api.dto.response.plano_dental;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanoDentalResponse {

    private Long id;

    // Dados do Paciente
    private Long pacienteId;
    private String pacienteNome;
    private String pacienteTelefone;

    // Dados do Dentista
    private Long dentistaId;
    private String dentistaNome;
    private String dentistaCro;

    // Dados do Plano
    private String dente;
    private String procedimento;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal valor;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal valorFinal;

    private BigDecimal valorDesconto;
    private String observacoes;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime criadoEm;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime atualizadoEm;

    // Campos calculados
    private boolean temDesconto;
    private String percentualDesconto;
}
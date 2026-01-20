package consultorio.api.dto.response.plano_dental;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanoDentalDetalheResponse {

    private Long id;

    // Dados completos do Paciente
    private Long pacienteId;
    private String pacienteNome;
    private String pacienteTelefone;
    private String pacienteEmail;
    private String pacienteCpf;

    // Dados completos do Dentista
    private Long dentistaId;
    private String dentistaNome;
    private String dentistaCro;
    private String dentistaEspecialidade;

    // Dados do Plano
    private String dente;
    private String procedimento;
    private String descricaoProcedimento;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "#,##0.00")
    private BigDecimal valor;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "#,##0.00")
    private BigDecimal valorFinal;

    private BigDecimal valorDesconto;
    private Double percentualDesconto;
    private String observacoes;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime criadoEm;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime atualizadoEm;

    // Controles de permissão
    private boolean podeEditar;
    private boolean podeExcluir;
    private boolean podeAplicarDesconto;

    // Campos calculados
    private String valorFormatado;
    private String valorFinalFormatado;
    private String valorDescontoFormatado;
}
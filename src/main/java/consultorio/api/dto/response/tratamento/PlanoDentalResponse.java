package consultorio.api.dto.response.tratamento;

import com.fasterxml.jackson.annotation.JsonFormat;
import consultorio.domain.entity.tratamento.enums.StatusPlano;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanoDentalResponse {
    private Long id;
    private Long pacienteId;
    private String pacienteNome;
    private Long dentistaId;
    private String dentistaNome;
    private String dente;
    private String faceDente;
    private String procedimento;
    private String codigoProcedimento;
    private BigDecimal valor;
    private BigDecimal valorDesconto;
    private BigDecimal valorFinal;
    private StatusPlano status;
    private String prioridade;
    private Boolean urgente;
    private String observacoes;
    private String motivoCancelamento;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataPrevista;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataRealizacao;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataCancelamento;

    private Boolean ativo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime criadoEm;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime atualizadoEm;

    // Método para formatar dente
    public String getDenteFormatado() {
        if (dente == null) return "";
        if (faceDente != null && !faceDente.trim().isEmpty()) {
            return dente + " (" + faceDente + ")";
        }
        return dente;
    }
}
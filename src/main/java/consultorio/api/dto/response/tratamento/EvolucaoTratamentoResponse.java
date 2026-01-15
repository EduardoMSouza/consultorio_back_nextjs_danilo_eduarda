package consultorio.api.dto.response.tratamento;

import com.fasterxml.jackson.annotation.JsonFormat;
import consultorio.domain.entity.tratamento.enums.TipoEvolucao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EvolucaoTratamentoResponse {

    private Long id;
    private Long planoDentalId;
    private String planoDentalNome; // Se quiser incluir o nome
    private Long pacienteId;
    private String pacienteNome;
    private Long dentistaId;
    private String dentistaNome;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataEvolucao;

    private TipoEvolucao tipoEvolucao;
    private String tipoEvolucaoDescricao;
    private String titulo;
    private String descricao;
    private String procedimentosRealizados;
    private String materiaisUtilizados;
    private String medicamentosPrescritos;
    private String observacoes;
    private String recomendacoes;
    private String doresQueixas;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate proximaConsulta;

    private Integer tempoConsultaMinutos;
    private Boolean urgente;
    private Boolean retornoNecessario;
    private String assinaturaDentista;
    private Boolean ativo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime criadoEm;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime atualizadoEm;

    // Métodos auxiliares
    public String getResumoDescricao() {
        if (descricao == null) return "";
        return descricao.length() > 100 ? descricao.substring(0, 100) + "..." : descricao;
    }

    public String getResumoTitulo() {
        if (titulo == null) return "";
        return titulo.length() > 50 ? titulo.substring(0, 50) + "..." : titulo;
    }
}
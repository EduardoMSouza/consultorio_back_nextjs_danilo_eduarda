package consultorio.api.dto.request.tratamento;

import com.fasterxml.jackson.annotation.JsonFormat;
import consultorio.domain.entity.tratamento.enums.TipoEvolucao;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EvolucaoTratamentoRequest {

    @NotNull(message = "ID do paciente é obrigatório")
    @Positive(message = "ID do paciente deve ser positivo")
    private Long pacienteId;

    @NotNull(message = "ID do dentista é obrigatório")
    @Positive(message = "ID do dentista deve ser positivo")
    private Long dentistaId;

    @NotNull(message = "ID do plano dental é obrigatório")
    @Positive(message = "ID do plano dental deve ser positivo")
    private Long planoDentalId;

    @NotNull(message = "Data da evolução é obrigatória")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataEvolucao;

    @NotNull(message = "Tipo de evolução é obrigatório")
    private TipoEvolucao tipoEvolucao;

    @NotBlank(message = "Título é obrigatório")
    @Size(min = 3, max = 200, message = "Título deve ter entre 3 e 200 caracteres")
    private String titulo;

    @NotBlank(message = "Descrição é obrigatória")
    @Size(min = 5, max = 5000, message = "Descrição deve ter entre 5 e 5000 caracteres")
    private String descricao;

    @Size(max = 5000, message = "Procedimentos realizados deve ter no máximo 5000 caracteres")
    private String procedimentosRealizados;

    @Size(max = 5000, message = "Materiais utilizados deve ter no máximo 5000 caracteres")
    private String materiaisUtilizados;

    @Size(max = 5000, message = "Medicamentos prescritos deve ter no máximo 5000 caracteres")
    private String medicamentosPrescritos;

    @Size(max = 5000, message = "Observações deve ter no máximo 5000 caracteres")
    private String observacoes;

    @Size(max = 5000, message = "Recomendações deve ter no máximo 5000 caracteres")
    private String recomendacoes;

    @Size(max = 2000, message = "Dores e queixas deve ter no máximo 2000 caracteres")
    private String doresQueixas;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate proximaConsulta;

    @Min(value = 1, message = "Tempo de consulta deve ser no mínimo 1 minuto")
    @Max(value = 480, message = "Tempo de consulta deve ser no máximo 480 minutos")
    private Integer tempoConsultaMinutos;

    private Boolean urgente = false;

    private Boolean retornoNecessario = false;

    @Size(max = 100, message = "Assinatura deve ter no máximo 100 caracteres")
    private String assinaturaDentista;

    public void formatarCampos() {
        if (titulo != null) titulo = titulo.trim();
        if (descricao != null) descricao = descricao.trim();
        if (procedimentosRealizados != null) procedimentosRealizados = procedimentosRealizados.trim();
        if (materiaisUtilizados != null) materiaisUtilizados = materiaisUtilizados.trim();
        if (medicamentosPrescritos != null) medicamentosPrescritos = medicamentosPrescritos.trim();
        if (observacoes != null) observacoes = observacoes.trim();
        if (recomendacoes != null) recomendacoes = recomendacoes.trim();
        if (doresQueixas != null) doresQueixas = doresQueixas.trim();
        if (assinaturaDentista != null) assinaturaDentista = assinaturaDentista.trim();
    }
}
package consultorio.api.dto.response.tratamento;

import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstatisticaEvolucaoResponse {

    private Long totalEvolucoes;
    private Long totalPacientes;
    private Long totalDentistas;
    private LocalDate dataPrimeiraEvolucao;
    private LocalDate dataUltimaEvolucao;
    private Long evolucoesMesAtual;
    private Long evolucoesMesAnterior;
    private Double crescimentoPercentual;
}
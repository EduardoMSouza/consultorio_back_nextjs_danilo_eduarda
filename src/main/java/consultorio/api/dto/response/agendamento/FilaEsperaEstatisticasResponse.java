// FilaEsperaEstatisticasResponse.java
package consultorio.api.dto.response.agendamento;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilaEsperaEstatisticasResponse {

    private Long totalNaFila;
    private Long ativas;
    private Long convertidas;
    private Long canceladas;
    private Long expiradas;
    private Long aguardandoContato;
    private Long altaPrioridade;
    private Long mediaTempoEsperaDias;
    private Long taxaConversao;

    // Por dentista
    private Long dentistaId;
    private String dentistaNome;
    private Long filasPorDentista;

    // Por procedimento
    private String procedimentoMaisComum;
    private Long quantidadeProcedimentoMaisComum;
}
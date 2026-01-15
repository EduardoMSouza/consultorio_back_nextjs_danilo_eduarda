// AgendamentoEstatisticasResponse.java
package consultorio.api.dto.response.agendamento;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoEstatisticasResponse {

    private Long totalAgendamentos;
    private Long agendamentosHoje;
    private Long agendamentosSemana;
    private Long agendamentosMes;
    private Long confirmados;
    private Long cancelados;
    private Long faltas;
    private Long concluidos;
    private Double taxaComparecimento;
    private Double faturamentoMes;
    private Double faturamentoAno;
}
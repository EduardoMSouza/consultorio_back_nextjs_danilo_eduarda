// FilaEsperaFiltroRequest.java
package consultorio.api.dto.request.agendamento;


import consultorio.domain.entity.agendamento.FilaEspera.StatusFila;
import consultorio.domain.entity.agendamento.enums.TipoProcedimento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilaEsperaFiltroRequest {

    private Long pacienteId;
    private Long dentistaId;
    private TipoProcedimento tipoProcedimento;
    private StatusFila status;
    private Boolean ativa;
    private Boolean notificado;
    private LocalDate dataInicio;
    private LocalDate dataFim;

    // Paginação
    private Integer pagina = 0;
    private Integer tamanho = 20;
    private String ordenarPor = "prioridade";
    private String direcao = "DESC";
}
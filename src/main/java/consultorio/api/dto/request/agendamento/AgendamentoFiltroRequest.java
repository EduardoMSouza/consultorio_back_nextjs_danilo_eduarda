package consultorio.api.dto.request.agendamento;

import consultorio.domain.entity.agendamento.enums.StatusAgendamento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoFiltroRequest {

    private Long dentistaId;
    private Long pacienteId;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private StatusAgendamento status;
    private Boolean ativo;
    private Boolean confirmado;
    private Boolean lembreteEnviado;

    // Paginação
    private Integer pagina = 0;
    private Integer tamanho = 20;
    private String ordenarPor = "dataConsulta";
    private String direcao = "ASC";
}
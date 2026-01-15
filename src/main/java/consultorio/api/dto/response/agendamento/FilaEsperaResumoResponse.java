package consultorio.api.dto.response.agendamento;// FilaEsperaResumoResponse.java


import consultorio.domain.entity.agendamento.FilaEspera.StatusFila;
import consultorio.domain.entity.agendamento.enums.TipoProcedimento;
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
public class FilaEsperaResumoResponse {

    private Long id;
    private LocalDate dataPreferencial;
    private LocalDateTime criadoEm;

    // Paciente
    private Long pacienteId;
    private String pacienteNome;
    private String pacienteTelefone;

    // Dentista
    private Long dentistaId;
    private String dentistaNome;

    // Informações básicas
    private TipoProcedimento tipoProcedimento;
    private String tipoProcedimentoDescricao;
    private StatusFila status;
    private String statusDescricao;
    private Integer prioridade;
    private Integer posicaoFila;

    // Flags
    private Boolean ativa;
    private Boolean notificado;
    private Boolean podeSerConvertido;
}
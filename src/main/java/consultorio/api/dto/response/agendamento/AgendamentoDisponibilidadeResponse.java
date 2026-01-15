// AgendamentoDisponibilidadeResponse.java
package consultorio.api.dto.response.agendamento;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoDisponibilidadeResponse {

    private Long dentistaId;
    private String dentistaNome;
    private LocalDate data;
    private List<HorarioDisponivel> horariosDisponiveis;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HorarioDisponivel {
        private LocalTime inicio;
        private LocalTime fim;
        private Boolean disponivel;
        private String motivoIndisponibilidade;
    }
}
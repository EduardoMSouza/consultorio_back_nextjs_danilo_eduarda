// AgendamentoConflitoResponse.java
package consultorio.api.dto.response.agendamento;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoConflitoResponse {

    private Boolean conflito;
    private String mensagem;
    private List<AgendamentoConflito> conflitos;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AgendamentoConflito {
        private Long agendamentoId;
        private String pacienteNome;
        private String dentistaNome;
        private LocalDateTime inicio;
        private LocalDateTime fim;
        private String status;
    }
}
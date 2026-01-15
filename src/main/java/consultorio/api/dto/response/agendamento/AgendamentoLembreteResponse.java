package consultorio.api.dto.response.agendamento;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoLembreteResponse {

    private Long id;
    private String pacienteNome;
    private String pacienteTelefone;
    private String pacienteEmail;
    private LocalDateTime dataHoraConsulta;
    private String dentistaNome;
    private String localConsulta;
    private String tipoProcedimento;
    private Boolean lembreteEnviado;
    private LocalDateTime lembreteEnviadoEm;
    private Boolean confirmado;
    private LocalDateTime confirmadoEm;
}

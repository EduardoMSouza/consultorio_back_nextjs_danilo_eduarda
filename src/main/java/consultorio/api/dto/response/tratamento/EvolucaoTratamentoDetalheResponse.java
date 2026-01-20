package consultorio.api.dto.response.tratamento;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvolucaoTratamentoDetalheResponse {

    private Long id;

    // Dados do Paciente
    private Long pacienteId;
    private String pacienteNome;
    private String pacienteTelefone;
    private String pacienteEmail;

    // Dados do Dentista
    private Long dentistaId;
    private String dentistaNome;
    private String dentistaCro;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate data;

    @JsonFormat(pattern = "EEEE, dd 'de' MMMM 'de' yyyy", locale = "pt_BR")
    private LocalDate dataFormatada;

    private String evolucaoEIntercorrencias;
    private Integer quantidadeCaracteres;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime criadoEm;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime atualizadoEm;

    private Boolean podeEditar;
    private Boolean podeExcluir;
}
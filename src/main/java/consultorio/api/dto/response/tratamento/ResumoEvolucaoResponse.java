package consultorio.api.dto.response.tratamento;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumoEvolucaoResponse {

    private Long id;
    private String pacienteNome;
    private String dentistaNome;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate data;

    private String evolucaoResumida; // Primeiros 100 caracteres
    private Integer tamanhoTexto;
}
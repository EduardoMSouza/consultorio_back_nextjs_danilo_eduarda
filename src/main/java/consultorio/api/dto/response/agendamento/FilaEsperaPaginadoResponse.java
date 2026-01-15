package consultorio.api.dto.response.agendamento;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilaEsperaPaginadoResponse {

    private List<FilaEsperaResponse> conteudo;
    private Integer pagina;
    private Integer tamanho;
    private Long totalElementos;
    private Integer totalPaginas;
    private Boolean ultima;
    private Boolean primeira;
}
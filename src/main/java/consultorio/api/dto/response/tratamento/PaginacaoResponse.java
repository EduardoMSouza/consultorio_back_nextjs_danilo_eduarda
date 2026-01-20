package consultorio.api.dto.response.tratamento;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginacaoResponse<T> {

    private List<T> conteudo;
    private Integer paginaAtual;
    private Integer totalPaginas;
    private Long totalElementos;
    private Integer tamanhoPagina;
    private Boolean primeiraPagina;
    private Boolean ultimaPagina;
}
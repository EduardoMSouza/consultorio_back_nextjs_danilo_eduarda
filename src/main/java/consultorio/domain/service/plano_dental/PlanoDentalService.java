package consultorio.domain.service.plano_dental;

import consultorio.api.dto.request.plano_dental.AtualizarPlanoDentalRequest;
import consultorio.api.dto.request.plano_dental.CriarPlanoDentalRequest;
import consultorio.api.dto.response.plano_dental.PlanoDentalDetalheResponse;
import consultorio.api.dto.response.plano_dental.PlanoDentalResponse;
import consultorio.api.dto.response.plano_dental.ResumoPlanoDentalResponse;
import consultorio.api.dto.response.tratamento.PaginacaoResponse;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface PlanoDentalService {

    // CRUD Básico
    List<PlanoDentalResponse> listarTodos();
    PaginacaoResponse<PlanoDentalResponse> listarPaginado(Pageable pageable);
    PlanoDentalDetalheResponse buscarPorId(Long id);
    PlanoDentalResponse criar(CriarPlanoDentalRequest request);
    PlanoDentalResponse atualizar(Long id, AtualizarPlanoDentalRequest request);
    void excluir(Long id);

    // Consultas específicas
    List<PlanoDentalResponse> buscarPorPaciente(Long pacienteId);
    PaginacaoResponse<PlanoDentalResponse> buscarPorPacientePaginado(Long pacienteId, Pageable pageable);
    List<PlanoDentalResponse> buscarPorDentista(Long dentistaId);
    List<PlanoDentalResponse> buscarPorDente(String dente);
    List<PlanoDentalResponse> buscarPorProcedimento(String procedimento);
    List<PlanoDentalResponse> buscarPorPeriodo(String dataInicio, String dataFim);
    List<PlanoDentalResponse> buscarPorPacienteEPeriodo(Long pacienteId, String dataInicio, String dataFim);
    List<PlanoDentalResponse> buscarPlanosComDesconto();
    List<ResumoPlanoDentalResponse> buscarPlanosRecentes(int quantidade);

    // Estatísticas e cálculos
    BigDecimal calcularValorTotalPorPaciente(Long pacienteId);
    BigDecimal calcularValorTotalPorDentista(Long dentistaId);
    Long contarPlanosPorPaciente(Long pacienteId);
    boolean existePlanoParaDente(Long pacienteId, String dente);

    // Operações especiais
    PlanoDentalResponse aplicarDesconto(Long id, BigDecimal valorDesconto);
    List<Object[]> buscarTopProcedimentos();
}
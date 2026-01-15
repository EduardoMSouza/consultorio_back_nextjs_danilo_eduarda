package consultorio.domain.service.plano_dental;

import consultorio.api.dto.request.tratamento.PlanoDentalRequest;
import consultorio.api.dto.response.tratamento.PlanoDentalResponse;
import consultorio.domain.entity.tratamento.enums.StatusPlano;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface PlanoDentalService {

    // ========== CRUD BÁSICO ==========

    /**
     * Cria um novo plano dental
     */
    PlanoDentalResponse criar(PlanoDentalRequest request);

    /**
     * Busca um plano dental por ID
     */
    PlanoDentalResponse buscarPorId(Long id);

    /**
     * Lista todos os planos dentais (com paginação)
     */
    Page<PlanoDentalResponse> listarTodos(Pageable pageable);

    /**
     * Lista todos os planos dentais (sem paginação)
     */
    List<PlanoDentalResponse> listarTodos();

    /**
     * Atualiza um plano dental existente
     */
    PlanoDentalResponse atualizar(Long id, PlanoDentalRequest request);

    /**
     * Atualização parcial de um plano dental
     */
    PlanoDentalResponse atualizarParcial(Long id, PlanoDentalRequest request);

    /**
     * Desativa um plano dental (soft delete)
     */
    void desativar(Long id);

    /**
     * Ativa um plano dental previamente desativado
     */
    void ativar(Long id);

    /**
     * Exclui permanentemente um plano dental
     */
    void excluir(Long id);

    // ========== OPERAÇÕES DE NEGÓCIO ==========

    /**
     * Conclui um plano dental
     */
    PlanoDentalResponse concluir(Long id);

    /**
     * Cancela um plano dental com motivo
     */
    PlanoDentalResponse cancelar(Long id, String motivo);

    /**
     * Inicia um plano dental pendente
     */
    PlanoDentalResponse iniciar(Long id);

    /**
     * Aplica desconto a um plano dental
     */
    PlanoDentalResponse aplicarDesconto(Long id, BigDecimal desconto);

    /**
     * Atualiza o valor de um plano dental
     */
    PlanoDentalResponse atualizarValor(Long id, BigDecimal valor);

    /**
     * Marca plano como urgente
     */
    PlanoDentalResponse marcarComoUrgente(Long id);

    /**
     * Remove marcação de urgência
     */
    PlanoDentalResponse removerUrgencia(Long id);

    // ========== BUSCAS POR RELACIONAMENTOS ==========

    /**
     * Lista planos dentais por paciente
     */
    List<PlanoDentalResponse> listarPorPaciente(Long pacienteId);

    /**
     * Lista planos dentais por paciente (paginado)
     */
    Page<PlanoDentalResponse> listarPorPaciente(Long pacienteId, Pageable pageable);

    /**
     * Lista planos dentais por dentista
     */
    List<PlanoDentalResponse> listarPorDentista(Long dentistaId);

    /**
     * Lista planos dentais por dentista (paginado)
     */
    Page<PlanoDentalResponse> listarPorDentista(Long dentistaId, Pageable pageable);

    /**
     * Lista planos dentais por paciente e dentista
     */
    List<PlanoDentalResponse> listarPorPacienteEDentista(Long pacienteId, Long dentistaId);

    // ========== BUSCAS POR STATUS ==========

    /**
     * Lista planos dentais por status
     */
    List<PlanoDentalResponse> listarPorStatus(StatusPlano status);

    /**
     * Lista planos dentais por status (paginado)
     */
    Page<PlanoDentalResponse> listarPorStatus(StatusPlano status, Pageable pageable);

    /**
     * Lista planos dentais por múltiplos status
     */
    List<PlanoDentalResponse> listarPorStatusEm(List<StatusPlano> statuses);

    // ========== BUSCAS ESPECÍFICAS ==========

    /**
     * Lista planos ativos por paciente ordenados por data prevista
     */
    List<PlanoDentalResponse> listarAtivosPorPacienteOrdenadosPorDataPrevista(Long pacienteId);

    /**
     * Lista planos dentais por data prevista
     */
    List<PlanoDentalResponse> listarPorDataPrevistaEntre(LocalDateTime inicio, LocalDateTime fim);

    /**
     * Lista planos urgentes
     */
    List<PlanoDentalResponse> listarUrgentes();

    /**
     * Lista planos urgentes por status
     */
    List<PlanoDentalResponse> listarUrgentesPorStatus(StatusPlano status);

    /**
     * Lista planos por paciente e status
     */
    List<PlanoDentalResponse> listarPorPacienteEStatus(Long pacienteId, StatusPlano status);

    /**
     * Lista planos por dentista e status
     */
    List<PlanoDentalResponse> listarPorDentistaEStatus(Long dentistaId, StatusPlano status);

    /**
     * Lista planos por paciente, dentista e status
     */
    List<PlanoDentalResponse> listarPorPacienteDentistaEStatus(Long pacienteId, Long dentistaId, StatusPlano status);

    // ========== BUSCAS COM FILTROS AVANÇADOS ==========

    /**
     * Busca planos com filtros múltiplos
     */
    List<PlanoDentalResponse> buscarComFiltros(
            Long pacienteId,
            Long dentistaId,
            StatusPlano status,
            String dente,
            String procedimento,
            Boolean urgente,
            Boolean ativo,
            LocalDateTime dataInicio,
            LocalDateTime dataFim);

    /**
     * Busca planos ativos com filtros múltiplos
     */
    List<PlanoDentalResponse> buscarAtivosComFiltros(
            Long pacienteId,
            Long dentistaId,
            StatusPlano status,
            String dente,
            String procedimento,
            Boolean urgente);

    // ========== RELATÓRIOS E ESTATÍSTICAS ==========

    /**
     * Calcula valor total por paciente e status
     */
    BigDecimal calcularTotalPorPacienteEStatus(Long pacienteId, StatusPlano status);

    /**
     * Calcula valor total por dentista e status
     */
    BigDecimal calcularTotalPorDentistaEStatus(Long dentistaId, StatusPlano status);

    /**
     * Calcula valor total de todos os planos ativos
     */
    BigDecimal calcularValorTotalAtivos();

    /**
     * Conta planos ativos por paciente
     */
    Long contarAtivosPorPaciente(Long pacienteId);

    /**
     * Conta planos por paciente e status
     */
    Long contarPorPacienteEStatus(Long pacienteId, StatusPlano status);

    /**
     * Conta planos por dentista e status
     */
    Long contarPorDentistaEStatus(Long dentistaId, StatusPlano status);

    /**
     * Obtém estatísticas agrupadas por status
     */
    List<Object[]> obterEstatisticasPorStatus();

    // ========== VALIDAÇÕES E VERIFICAÇÕES ==========

    /**
     * Verifica se plano existe por ID
     */
    boolean existePorId(Long id);

    /**
     * Verifica se plano está ativo
     */
    boolean estaAtivo(Long id);

    /**
     * Verifica se existe plano ativo para paciente, dente e procedimento
     */
    boolean existePlanoAtivoParaPacienteDenteProcedimento(Long pacienteId, String dente, String procedimento);

    /**
     * Busca planos com data prevista vencida
     */
    List<PlanoDentalResponse> buscarPlanosComDataPrevistaVencida();

    /**
     * Busca planos recentes (últimos 10)
     */
    List<PlanoDentalResponse> buscarPlanosRecentes();

    /**
     * Busca planos urgentes recentes (últimos 10)
     */
    List<PlanoDentalResponse> buscarUrgentesRecentes();
}
package consultorio.domain.service.plano_dental.impl;

import consultorio.api.dto.request.tratamento.PlanoDentalRequest;
import consultorio.api.dto.response.tratamento.PlanoDentalResponse;
import consultorio.api.exception.BusinessException;
import consultorio.api.exception.ResourceNotFoundException;
import consultorio.api.mapper.tratamento.PlanoDentalMapper;
import consultorio.domain.entity.pessoa.Dentista;
import consultorio.domain.entity.pessoa.Paciente;
import consultorio.domain.entity.tratamento.PlanoDental;
import consultorio.domain.entity.tratamento.enums.StatusPlano;
import consultorio.domain.repository.pessoa.DentistaRepository;
import consultorio.domain.repository.pessoa.PacienteRepository;
import consultorio.domain.repository.tratamento.PlanoDentalRepository;
import consultorio.domain.service.plano_dental.PlanoDentalService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PlanoDentalServiceImpl implements PlanoDentalService {

    private final PlanoDentalRepository planoDentalRepository;
    private final PacienteRepository pacienteRepository;
    private final DentistaRepository dentistaRepository;
    private final PlanoDentalMapper planoDentalMapper;

    @Override
    public PlanoDentalResponse criar(PlanoDentalRequest request) {
        log.info("Criando plano dental para paciente ID: {}", request.getPacienteId());

        // Formatar campos
        planoDentalMapper.formatarCamposRequest(request);

        // Validar dados únicos
        if (planoDentalRepository.existsByPacienteIdAndDenteAndProcedimentoAndAtivoTrue(
                request.getPacienteId(), request.getDente(), request.getProcedimento())) {
            throw new BusinessException("Já existe um plano ativo para este paciente com mesmo dente e procedimento");
        }

        // Buscar entidades relacionadas
        Paciente paciente = buscarPaciente(request.getPacienteId());
        Dentista dentista = buscarDentista(request.getDentistaId());

        // Criar plano
        PlanoDental plano = planoDentalMapper.toEntity(request, paciente, dentista);
        PlanoDental planoSalvo = planoDentalRepository.save(plano);

        log.info("Plano dental criado com ID: {}", planoSalvo.getId());
        return planoDentalMapper.toResponse(planoSalvo);
    }

    @Override
    public PlanoDentalResponse buscarPorId(Long id) {
        log.info("Buscando plano dental por ID: {}", id);

        PlanoDental plano = planoDentalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plano dental não encontrado com ID: " + id));

        return planoDentalMapper.toResponse(plano);
    }

    @Override
    public Page<PlanoDentalResponse> listarTodos(Pageable pageable) {
        log.info("Listando todos os planos dentais (paginado)");

        Page<PlanoDental> planos = planoDentalRepository.findAll(pageable);
        return planos.map(planoDentalMapper::toResponse);
    }

    @Override
    public List<PlanoDentalResponse> listarTodos() {
        log.info("Listando todos os planos dentais");

        List<PlanoDental> planos = planoDentalRepository.findAll();
        return planoDentalMapper.toResponseList(planos);
    }

    @Override
    public PlanoDentalResponse atualizar(Long id, PlanoDentalRequest request) {
        log.info("Atualizando plano dental ID: {}", id);

        // Formatar campos
        planoDentalMapper.formatarCamposRequest(request);

        // Buscar plano existente
        PlanoDental plano = planoDentalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plano dental não encontrado com ID: " + id));

        // Verificar se pode ser atualizado
        validarPlanoParaAtualizacao(plano);

        // Verificar se dados únicos foram alterados
        if ((request.getDente() != null && !request.getDente().equals(plano.getDente())) ||
                (request.getProcedimento() != null && !request.getProcedimento().equals(plano.getProcedimento()))) {

            if (planoDentalRepository.existsByPacienteIdAndDenteAndProcedimentoAndAtivoTrue(
                    plano.getPaciente().getId(), request.getDente(), request.getProcedimento())) {
                throw new BusinessException("Já existe um plano ativo para este paciente com mesmo dente e procedimento");
            }
        }

        // Buscar entidades relacionadas se fornecidas
        Paciente paciente = request.getPacienteId() != null ? buscarPaciente(request.getPacienteId()) : null;
        Dentista dentista = request.getDentistaId() != null ? buscarDentista(request.getDentistaId()) : null;

        // Atualizar plano
        planoDentalMapper.updateEntityFromRequest(request, plano, paciente, dentista);
        PlanoDental planoAtualizado = planoDentalRepository.save(plano);

        log.info("Plano dental ID: {} atualizado", id);
        return planoDentalMapper.toResponse(planoAtualizado);
    }

    @Override
    public PlanoDentalResponse atualizarParcial(Long id, PlanoDentalRequest request) {
        log.info("Atualização parcial do plano dental ID: {}", id);

        // Buscar plano existente
        PlanoDental plano = planoDentalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plano dental não encontrado com ID: " + id));

        // Verificar se pode ser atualizado
        validarPlanoParaAtualizacao(plano);

        // Formatar campos fornecidos
        if (request.getDente() != null) request.setDente(request.getDente().trim().toUpperCase());
        if (request.getFaceDente() != null) request.setFaceDente(request.getFaceDente().trim());
        if (request.getProcedimento() != null) request.setProcedimento(request.getProcedimento().trim());
        if (request.getCodigoProcedimento() != null) request.setCodigoProcedimento(request.getCodigoProcedimento().trim());
        if (request.getPrioridade() != null) request.setPrioridade(request.getPrioridade().trim());
        if (request.getObservacoes() != null) request.setObservacoes(request.getObservacoes().trim());

        // Aplicar atualização parcial
        planoDentalMapper.updateEntityPartial(request, plano);
        PlanoDental planoAtualizado = planoDentalRepository.save(plano);

        log.info("Plano dental ID: {} atualizado parcialmente", id);
        return planoDentalMapper.toResponse(planoAtualizado);
    }

    @Override
    public void desativar(Long id) {
        log.info("Desativando plano dental ID: {}", id);

        PlanoDental plano = planoDentalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plano dental não encontrado com ID: " + id));

        plano.desativar();
        planoDentalRepository.save(plano);

        log.info("Plano dental ID: {} desativado", id);
    }

    @Override
    public void ativar(Long id) {
        log.info("Ativando plano dental ID: {}", id);

        PlanoDental plano = planoDentalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plano dental não encontrado com ID: " + id));

        plano.ativar();
        planoDentalRepository.save(plano);

        log.info("Plano dental ID: {} ativado", id);
    }

    @Override
    public void excluir(Long id) {
        log.warn("Excluindo permanentemente plano dental ID: {}", id);

        if (!planoDentalRepository.existsById(id)) {
            throw new ResourceNotFoundException("Plano dental não encontrado com ID: " + id);
        }

        planoDentalRepository.deleteById(id);
        log.info("Plano dental ID: {} excluído permanentemente", id);
    }

    @Override
    public PlanoDentalResponse concluir(Long id) {
        log.info("Concluindo plano dental ID: {}", id);

        PlanoDental plano = planoDentalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plano dental não encontrado com ID: " + id));

        // Validações
        validarPlanoParaOperacao(plano);

        if (plano.getStatus() == StatusPlano.CANCELADO) {
            throw new BusinessException("Não é possível concluir um plano cancelado");
        }

        if (plano.getStatus() == StatusPlano.CONCLUIDO) {
            throw new BusinessException("Plano já está concluído");
        }

        // Concluir plano
        plano.concluir();
        PlanoDental planoAtualizado = planoDentalRepository.save(plano);

        log.info("Plano dental ID: {} concluído", id);
        return planoDentalMapper.toResponse(planoAtualizado);
    }

    @Override
    public PlanoDentalResponse cancelar(Long id, String motivo) {
        log.info("Cancelando plano dental ID: {}", id);

        PlanoDental plano = planoDentalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plano dental não encontrado com ID: " + id));

        // Validações
        validarPlanoParaOperacao(plano);

        if (plano.getStatus() == StatusPlano.CONCLUIDO) {
            throw new BusinessException("Não é possível cancelar um plano já concluído");
        }

        if (plano.getStatus() == StatusPlano.CANCELADO) {
            throw new BusinessException("Plano já está cancelado");
        }

        // Cancelar plano
        plano.cancelar(motivo);
        PlanoDental planoAtualizado = planoDentalRepository.save(plano);

        log.info("Plano dental ID: {} cancelado. Motivo: {}", id, motivo);
        return planoDentalMapper.toResponse(planoAtualizado);
    }

    @Override
    public PlanoDentalResponse iniciar(Long id) {
        log.info("Iniciando plano dental ID: {}", id);

        PlanoDental plano = planoDentalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plano dental não encontrado com ID: " + id));

        // Validações
        validarPlanoParaOperacao(plano);

        if (plano.getStatus() != StatusPlano.PENDENTE) {
            throw new BusinessException("Só é possível iniciar um plano pendente");
        }

        // Iniciar plano
        plano.iniciar();
        PlanoDental planoAtualizado = planoDentalRepository.save(plano);

        log.info("Plano dental ID: {} iniciado", id);
        return planoDentalMapper.toResponse(planoAtualizado);
    }

    @Override
    public PlanoDentalResponse aplicarDesconto(Long id, BigDecimal desconto) {
        log.info("Aplicando desconto ao plano dental ID: {}", id);

        PlanoDental plano = planoDentalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plano dental não encontrado com ID: " + id));

        // Validações
        validarPlanoParaOperacao(plano);

        if (plano.getStatus() == StatusPlano.CANCELADO || plano.getStatus() == StatusPlano.CONCLUIDO) {
            throw new BusinessException("Não é possível aplicar desconto em um plano concluído ou cancelado");
        }

        if (desconto == null || desconto.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Desconto inválido");
        }

        if (desconto.compareTo(plano.getValor()) > 0) {
            throw new BusinessException("O desconto não pode ser maior que o valor do plano");
        }

        // Aplicar desconto
        plano.aplicarDesconto(desconto);
        PlanoDental planoAtualizado = planoDentalRepository.save(plano);

        log.info("Desconto de {} aplicado ao plano dental ID: {}", desconto, id);
        return planoDentalMapper.toResponse(planoAtualizado);
    }

    @Override
    public PlanoDentalResponse atualizarValor(Long id, BigDecimal valor) {
        log.info("Atualizando valor do plano dental ID: {}", id);

        PlanoDental plano = planoDentalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plano dental não encontrado com ID: " + id));

        // Validações
        validarPlanoParaOperacao(plano);

        if (plano.getStatus() == StatusPlano.CONCLUIDO || plano.getStatus() == StatusPlano.CANCELADO) {
            throw new BusinessException("Não é possível atualizar valor de um plano concluído ou cancelado");
        }

        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Valor inválido");
        }

        // Atualizar valor
        plano.setValor(valor);
        PlanoDental planoAtualizado = planoDentalRepository.save(plano);

        log.info("Valor do plano dental ID: {} atualizado para {}", id, valor);
        return planoDentalMapper.toResponse(planoAtualizado);
    }

    @Override
    public PlanoDentalResponse marcarComoUrgente(Long id) {
        log.info("Marcando plano dental ID: {} como urgente", id);

        PlanoDental plano = planoDentalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plano dental não encontrado com ID: " + id));

        validarPlanoParaOperacao(plano);

        plano.setUrgente(true);
        PlanoDental planoAtualizado = planoDentalRepository.save(plano);

        log.info("Plano dental ID: {} marcado como urgente", id);
        return planoDentalMapper.toResponse(planoAtualizado);
    }

    @Override
    public PlanoDentalResponse removerUrgencia(Long id) {
        log.info("Removendo urgência do plano dental ID: {}", id);

        PlanoDental plano = planoDentalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plano dental não encontrado com ID: " + id));

        validarPlanoParaOperacao(plano);

        plano.setUrgente(false);
        PlanoDental planoAtualizado = planoDentalRepository.save(plano);

        log.info("Urgência removida do plano dental ID: {}", id);
        return planoDentalMapper.toResponse(planoAtualizado);
    }

    @Override
    public List<PlanoDentalResponse> listarPorPaciente(Long pacienteId) {
        log.info("Listando planos dentais do paciente ID: {}", pacienteId);

        List<PlanoDental> planos = planoDentalRepository.findByPacienteIdAndAtivoTrue(pacienteId);
        return planoDentalMapper.toResponseList(planos);
    }

    @Override
    public Page<PlanoDentalResponse> listarPorPaciente(Long pacienteId, Pageable pageable) {
        log.info("Listando planos dentais do paciente ID: {} (paginado)", pacienteId);

        Page<PlanoDental> planos = planoDentalRepository.findByPacienteId(pacienteId, pageable);
        return planos.map(planoDentalMapper::toResponse);
    }

    @Override
    public List<PlanoDentalResponse> listarPorDentista(Long dentistaId) {
        log.info("Listando planos dentais do dentista ID: {}", dentistaId);

        List<PlanoDental> planos = planoDentalRepository.findByDentistaIdAndAtivoTrue(dentistaId);
        return planoDentalMapper.toResponseList(planos);
    }

    @Override
    public Page<PlanoDentalResponse> listarPorDentista(Long dentistaId, Pageable pageable) {
        log.info("Listando planos dentais do dentista ID: {} (paginado)", dentistaId);

        Page<PlanoDental> planos = planoDentalRepository.findByDentistaId(dentistaId, pageable);
        return planos.map(planoDentalMapper::toResponse);
    }

    @Override
    public List<PlanoDentalResponse> listarPorPacienteEDentista(Long pacienteId, Long dentistaId) {
        log.info("Listando planos dentais do paciente ID: {} e dentista ID: {}", pacienteId, dentistaId);

        List<PlanoDental> planos = planoDentalRepository.findByPacienteIdAndDentistaIdAndAtivoTrue(pacienteId, dentistaId);
        return planoDentalMapper.toResponseList(planos);
    }

    @Override
    public List<PlanoDentalResponse> listarPorStatus(StatusPlano status) {
        log.info("Listando planos dentais com status: {}", status);

        List<PlanoDental> planos = planoDentalRepository.findByStatusAndAtivoTrue(status);
        return planoDentalMapper.toResponseList(planos);
    }

    @Override
    public Page<PlanoDentalResponse> listarPorStatus(StatusPlano status, Pageable pageable) {
        log.info("Listando planos dentais com status: {} (paginado)", status);

        Page<PlanoDental> planos = planoDentalRepository.findByStatus(status, pageable);
        return planos.map(planoDentalMapper::toResponse);
    }

    @Override
    public List<PlanoDentalResponse> listarPorStatusEm(List<StatusPlano> statuses) {
        log.info("Listando planos dentais com status em: {}", statuses);

        List<PlanoDental> planos = planoDentalRepository.findByStatusInAndAtivoTrueOrderByDataPrevista(statuses);
        return planoDentalMapper.toResponseList(planos);
    }

    @Override
    public List<PlanoDentalResponse> listarAtivosPorPacienteOrdenadosPorDataPrevista(Long pacienteId) {
        log.info("Listando planos ativos do paciente ID: {} ordenados por data prevista", pacienteId);

        List<PlanoDental> planos = planoDentalRepository.findAtivosByPacienteIdOrderByDataPrevista(pacienteId);
        return planoDentalMapper.toResponseList(planos);
    }

    @Override
    public List<PlanoDentalResponse> listarPorDataPrevistaEntre(LocalDateTime inicio, LocalDateTime fim) {
        log.info("Listando planos dentais com data prevista entre {} e {}", inicio, fim);

        List<PlanoDental> planos = planoDentalRepository.findByDataPrevistaBetween(inicio, fim);
        return planoDentalMapper.toResponseList(planos);
    }

    @Override
    public List<PlanoDentalResponse> listarUrgentes() {
        log.info("Listando planos dentais urgentes");

        List<PlanoDental> planos = planoDentalRepository.findByUrgenteTrueAndAtivoTrue();
        return planoDentalMapper.toResponseList(planos);
    }

    @Override
    public List<PlanoDentalResponse> listarUrgentesPorStatus(StatusPlano status) {
        log.info("Listando planos dentais urgentes com status: {}", status);

        List<PlanoDental> planos = planoDentalRepository.findUrgentesByStatus(status);
        return planoDentalMapper.toResponseList(planos);
    }

    @Override
    public List<PlanoDentalResponse> listarPorPacienteEStatus(Long pacienteId, StatusPlano status) {
        log.info("Listando planos do paciente ID: {} com status: {}", pacienteId, status);

        List<PlanoDental> planos = planoDentalRepository.findByPacienteIdAndStatus(pacienteId, status);
        return planoDentalMapper.toResponseList(planos);
    }

    @Override
    public List<PlanoDentalResponse> listarPorDentistaEStatus(Long dentistaId, StatusPlano status) {
        log.info("Listando planos do dentista ID: {} com status: {}", dentistaId, status);

        List<PlanoDental> planos = planoDentalRepository.findByDentistaIdAndStatus(dentistaId, status);
        return planoDentalMapper.toResponseList(planos);
    }

    @Override
    public List<PlanoDentalResponse> listarPorPacienteDentistaEStatus(Long pacienteId, Long dentistaId, StatusPlano status) {
        log.info("Listando planos do paciente ID: {}, dentista ID: {} e status: {}", pacienteId, dentistaId, status);

        List<PlanoDental> planos = planoDentalRepository.findByPacienteIdAndDentistaIdAndStatus(pacienteId, dentistaId, status);
        return planoDentalMapper.toResponseList(planos);
    }

    @Override
    public List<PlanoDentalResponse> buscarComFiltros(Long pacienteId, Long dentistaId, StatusPlano status,
                                                      String dente, String procedimento, Boolean urgente,
                                                      Boolean ativo, LocalDateTime dataInicio, LocalDateTime dataFim) {
        log.info("Buscando planos com filtros: paciente={}, dentista={}, status={}, dente={}, procedimento={}, urgente={}, ativo={}",
                pacienteId, dentistaId, status, dente, procedimento, urgente, ativo);

        List<PlanoDental> planos = planoDentalRepository.findByFiltros(
                pacienteId, dentistaId, status, dente, procedimento, urgente, ativo, dataInicio, dataFim);

        return planoDentalMapper.toResponseList(planos);
    }

    @Override
    public List<PlanoDentalResponse> buscarAtivosComFiltros(Long pacienteId, Long dentistaId, StatusPlano status,
                                                            String dente, String procedimento, Boolean urgente) {
        log.info("Buscando planos ativos com filtros: paciente={}, dentista={}, status={}, dente={}, procedimento={}, urgente={}",
                pacienteId, dentistaId, status, dente, procedimento, urgente);

        List<PlanoDental> planos = planoDentalRepository.findAtivosByFiltros(
                pacienteId, dentistaId, status, dente, procedimento, urgente);

        return planoDentalMapper.toResponseList(planos);
    }

    @Override
    public BigDecimal calcularTotalPorPacienteEStatus(Long pacienteId, StatusPlano status) {
        log.info("Calculando total de valores por paciente ID: {} e status: {}", pacienteId, status);

        BigDecimal total = planoDentalRepository.sumValorFinalByPacienteIdAndStatus(pacienteId, status);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal calcularTotalPorDentistaEStatus(Long dentistaId, StatusPlano status) {
        log.info("Calculando total de valores por dentista ID: {} e status: {}", dentistaId, status);

        BigDecimal total = planoDentalRepository.sumValorFinalByDentistaIdAndStatus(dentistaId, status);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal calcularValorTotalAtivos() {
        log.info("Calculando valor total de todos os planos ativos");

        BigDecimal total = planoDentalRepository.sumValorFinalTotal();
        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    public Long contarAtivosPorPaciente(Long pacienteId) {
        log.info("Contando planos ativos do paciente ID: {}", pacienteId);

        return planoDentalRepository.countAtivosByPacienteId(pacienteId);
    }

    @Override
    public Long contarPorPacienteEStatus(Long pacienteId, StatusPlano status) {
        log.info("Contando planos do paciente ID: {} com status: {}", pacienteId, status);

        return planoDentalRepository.countByPacienteIdAndStatus(pacienteId, status);
    }

    @Override
    public Long contarPorDentistaEStatus(Long dentistaId, StatusPlano status) {
        log.info("Contando planos do dentista ID: {} com status: {}", dentistaId, status);

        return planoDentalRepository.countByDentistaIdAndStatus(dentistaId, status);
    }

    @Override
    public List<Object[]> obterEstatisticasPorStatus() {
        log.info("Obtendo estatísticas de planos por status");

        return planoDentalRepository.countByStatusGroup();
    }

    @Override
    public boolean existePorId(Long id) {
        return planoDentalRepository.existsById(id);
    }

    @Override
    public boolean estaAtivo(Long id) {
        return planoDentalRepository.findById(id)
                .map(PlanoDental::getAtivo)
                .orElse(false);
    }

    @Override
    public boolean existePlanoAtivoParaPacienteDenteProcedimento(Long pacienteId, String dente, String procedimento) {
        if (dente != null) dente = dente.trim().toUpperCase();
        if (procedimento != null) procedimento = procedimento.trim();

        return planoDentalRepository.existsByPacienteIdAndDenteAndProcedimentoAndAtivoTrue(
                pacienteId, dente, procedimento);
    }

    @Override
    public List<PlanoDentalResponse> buscarPlanosComDataPrevistaVencida() {
        log.info("Buscando planos com data prevista vencida");

        LocalDateTime agora = LocalDateTime.now();
        List<StatusPlano> statusExcluidos = Arrays.asList(StatusPlano.CONCLUIDO, StatusPlano.CANCELADO);

        List<PlanoDental> planos = planoDentalRepository.findComDataPrevistaVencida(agora, statusExcluidos);
        return planoDentalMapper.toResponseList(planos);
    }

    @Override
    public List<PlanoDentalResponse> buscarPlanosRecentes() {
        log.info("Buscando planos recentes");

        List<PlanoDental> planos = planoDentalRepository.findTop10ByAtivoTrueOrderByCriadoEmDesc();
        return planoDentalMapper.toResponseList(planos);
    }

    @Override
    public List<PlanoDentalResponse> buscarUrgentesRecentes() {
        log.info("Buscando planos urgentes recentes");

        List<PlanoDental> planos = planoDentalRepository.findTop10ByUrgenteTrueAndAtivoTrueOrderByCriadoEmDesc();
        return planoDentalMapper.toResponseList(planos);
    }

    // ========== MÉTODOS PRIVADOS AUXILIARES ==========

    private Paciente buscarPaciente(Long pacienteId) {
        return pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado com ID: " + pacienteId));
    }

    private Dentista buscarDentista(Long dentistaId) {
        return dentistaRepository.findById(dentistaId)
                .orElseThrow(() -> new ResourceNotFoundException("Dentista não encontrado com ID: " + dentistaId));
    }

    private void validarPlanoParaOperacao(PlanoDental plano) {
        if (!plano.getAtivo()) {
            throw new BusinessException("Não é possível realizar operação em um plano inativo");
        }
    }

    private void validarPlanoParaAtualizacao(PlanoDental plano) {
        if (!plano.getAtivo()) {
            throw new BusinessException("Não é possível atualizar um plano inativo");
        }

        if (plano.getStatus() == StatusPlano.CONCLUIDO) {
            throw new BusinessException("Não é possível atualizar um plano já concluído");
        }
    }
}
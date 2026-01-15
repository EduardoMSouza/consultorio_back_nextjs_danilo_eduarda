package consultorio.domain.service.evolucao_tratamento.impl;

import consultorio.api.dto.request.tratamento.EvolucaoTratamentoRequest;
import consultorio.api.dto.response.tratamento.EvolucaoTratamentoResponse;
import consultorio.api.mapper.tratamento.EvolucaoTratamentoMapper;
import consultorio.domain.entity.pessoa.Dentista;
import consultorio.domain.entity.pessoa.Paciente;
import consultorio.domain.entity.tratamento.EvolucaoTratamento;
import consultorio.domain.entity.tratamento.PlanoDental;
import consultorio.domain.repository.pessoa.DentistaRepository;
import consultorio.domain.repository.pessoa.PacienteRepository;
import consultorio.domain.repository.tratamento.EvolucaoTratamentoRepository;
import consultorio.domain.repository.tratamento.PlanoDentalRepository;
import consultorio.domain.service.evolucao_tratamento.EvolucaoTratamentoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EvolucaoTratamentoServiceImpl implements EvolucaoTratamentoService {

    private final EvolucaoTratamentoRepository evolucaoRepository;
    private final PacienteRepository pacienteRepository;
    private final DentistaRepository dentistaRepository;
    private final PlanoDentalRepository planoDentalRepository;
    private final EvolucaoTratamentoMapper evolucaoMapper;

    @Override
    @Transactional
    public EvolucaoTratamentoResponse criar(EvolucaoTratamentoRequest request) {
        log.info("Criando nova evolução de tratamento para paciente ID: {}", request.getPacienteId());

        // Formatar campos antes de processar
        request.formatarCampos();

        // Validar e buscar entidades relacionadas
        Paciente paciente = buscarPaciente(request.getPacienteId());
        Dentista dentista = buscarDentista(request.getDentistaId());
        PlanoDental planoDental = buscarPlanoDental(request.getPlanoDentalId());

        // Criar entidade
        EvolucaoTratamento evolucao = evolucaoMapper.toEntity(request, paciente, dentista, planoDental);
        EvolucaoTratamento evolucaoSalva = evolucaoRepository.save(evolucao);

        log.info("Evolução de tratamento criada com ID: {}", evolucaoSalva.getId());
        return evolucaoMapper.toResponse(evolucaoSalva);
    }

    @Override
    public EvolucaoTratamentoResponse buscarPorId(Long id) {
        log.info("Buscando evolução de tratamento por ID: {}", id);

        EvolucaoTratamento evolucao = evolucaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evolução de tratamento não encontrada com ID: " + id));

        return evolucaoMapper.toResponse(evolucao);
    }

    @Override
    public List<EvolucaoTratamentoResponse> listarTodos() {
        log.info("Listando todas as evoluções de tratamento ativas");

        List<EvolucaoTratamento> evolucoes = evolucaoRepository.findByAtivoTrue();
        return evolucaoMapper.toResponseList(evolucoes);
    }

    @Override
    @Transactional
    public EvolucaoTratamentoResponse atualizar(Long id, EvolucaoTratamentoRequest request) {
        log.info("Atualizando evolução de tratamento ID: {}", id);

        // Formatar campos antes de processar
        request.formatarCampos();

        // Buscar evolução existente
        EvolucaoTratamento evolucao = evolucaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evolução de tratamento não encontrada com ID: " + id));

        // Validar e buscar entidades relacionadas
        Paciente paciente = buscarPaciente(request.getPacienteId());
        Dentista dentista = buscarDentista(request.getDentistaId());
        PlanoDental planoDental = buscarPlanoDental(request.getPlanoDentalId());

        // Atualizar entidade
        evolucaoMapper.updateEntityFromRequest(request, evolucao, paciente, dentista, planoDental);
        EvolucaoTratamento evolucaoAtualizada = evolucaoRepository.save(evolucao);

        log.info("Evolução de tratamento ID: {} atualizada", id);
        return evolucaoMapper.toResponse(evolucaoAtualizada);
    }

    @Override
    @Transactional
    public EvolucaoTratamentoResponse atualizarParcial(Long id, EvolucaoTratamentoRequest request) {
        log.info("Atualização parcial da evolução de tratamento ID: {}", id);

        // Buscar evolução existente
        EvolucaoTratamento evolucao = evolucaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evolução de tratamento não encontrada com ID: " + id));

        // Aplicar atualização parcial
        evolucaoMapper.updateEntityPartial(request, evolucao);
        EvolucaoTratamento evolucaoAtualizada = evolucaoRepository.save(evolucao);

        log.info("Evolução de tratamento ID: {} atualizada parcialmente", id);
        return evolucaoMapper.toResponse(evolucaoAtualizada);
    }

    @Override
    @Transactional
    public void desativar(Long id) {
        log.info("Desativando evolução de tratamento ID: {}", id);

        EvolucaoTratamento evolucao = evolucaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evolução de tratamento não encontrada com ID: " + id));

        evolucao.desativar();
        evolucaoRepository.save(evolucao);

        log.info("Evolução de tratamento ID: {} desativada", id);
    }

    @Override
    @Transactional
    public void ativar(Long id) {
        log.info("Ativando evolução de tratamento ID: {}", id);

        EvolucaoTratamento evolucao = evolucaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evolução de tratamento não encontrada com ID: " + id));

        evolucao.ativar();
        evolucaoRepository.save(evolucao);

        log.info("Evolução de tratamento ID: {} ativada", id);
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        log.warn("Excluindo permanentemente evolução de tratamento ID: {}", id);

        if (!evolucaoRepository.existsById(id)) {
            throw new EntityNotFoundException("Evolução de tratamento não encontrada com ID: " + id);
        }

        evolucaoRepository.deleteById(id);
        log.info("Evolução de tratamento ID: {} excluída permanentemente", id);
    }

    @Override
    public List<EvolucaoTratamentoResponse> buscarPorPaciente(Long pacienteId) {
        log.info("Buscando evoluções por paciente ID: {}", pacienteId);

        List<EvolucaoTratamento> evolucoes = evolucaoRepository.findByPacienteId(pacienteId);
        return evolucaoMapper.toResponseList(evolucoes);
    }

    @Override
    public List<EvolucaoTratamentoResponse> buscarPorDentista(Long dentistaId) {
        log.info("Buscando evoluções por dentista ID: {}", dentistaId);

        List<EvolucaoTratamento> evolucoes = evolucaoRepository.findByDentistaId(dentistaId);
        return evolucaoMapper.toResponseList(evolucoes);
    }

    @Override
    public List<EvolucaoTratamentoResponse> buscarPorPlanoDental(Long planoDentalId) {
        log.info("Buscando evoluções por plano dental ID: {}", planoDentalId);

        List<EvolucaoTratamento> evolucoes = evolucaoRepository.findByPlanoDentalId(planoDentalId);
        return evolucaoMapper.toResponseList(evolucoes);
    }

    @Override
    public List<EvolucaoTratamentoResponse> buscarPorData(LocalDate data) {
        log.info("Buscando evoluções por data: {}", data);

        List<EvolucaoTratamento> evolucoes = evolucaoRepository.findByDataEvolucao(data);
        return evolucaoMapper.toResponseList(evolucoes);
    }

    @Override
    public List<EvolucaoTratamentoResponse> buscarPorPeriodo(LocalDate inicio, LocalDate fim) {
        log.info("Buscando evoluções no período de {} até {}", inicio, fim);

        List<EvolucaoTratamento> evolucoes = evolucaoRepository.findByDataEvolucaoBetween(inicio, fim);
        return evolucaoMapper.toResponseList(evolucoes);
    }

    @Override
    public List<EvolucaoTratamentoResponse> buscarUrgentes() {
        log.info("Buscando evoluções urgentes");

        List<EvolucaoTratamento> evolucoes = evolucaoRepository.findByUrgenteTrue();
        return evolucaoMapper.toResponseList(evolucoes);
    }

    @Override
    public List<EvolucaoTratamentoResponse> buscarComRetornoNecessario() {
        log.info("Buscando evoluções com retorno necessário");

        List<EvolucaoTratamento> evolucoes = evolucaoRepository.findByRetornoNecessarioTrue();
        return evolucaoMapper.toResponseList(evolucoes);
    }

    @Override
    public List<EvolucaoTratamentoResponse> buscarRetornosAtrasados() {
        log.info("Buscando evoluções com retorno atrasado");

        List<EvolucaoTratamento> evolucoes = evolucaoRepository.findRetornosAtrasados(LocalDate.now());
        return evolucaoMapper.toResponseList(evolucoes);
    }

    @Override
    @Transactional
    public EvolucaoTratamentoResponse marcarComoUrgente(Long id) {
        log.info("Marcando evolução ID: {} como urgente", id);

        EvolucaoTratamento evolucao = evolucaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evolução de tratamento não encontrada com ID: " + id));

        evolucao.marcarComoUrgente();
        EvolucaoTratamento evolucaoAtualizada = evolucaoRepository.save(evolucao);

        return evolucaoMapper.toResponse(evolucaoAtualizada);
    }

    @Override
    @Transactional
    public EvolucaoTratamentoResponse agendarRetorno(Long id, LocalDate dataRetorno, String motivo) {
        log.info("Agendando retorno para evolução ID: {} na data {}", id, dataRetorno);

        EvolucaoTratamento evolucao = evolucaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evolução de tratamento não encontrada com ID: " + id));

        evolucao.agendarRetorno(dataRetorno, motivo);
        EvolucaoTratamento evolucaoAtualizada = evolucaoRepository.save(evolucao);

        return evolucaoMapper.toResponse(evolucaoAtualizada);
    }

    @Override
    @Transactional
    public EvolucaoTratamentoResponse adicionarProcedimento(Long id, String procedimento) {
        log.info("Adicionando procedimento à evolução ID: {}", id);

        EvolucaoTratamento evolucao = evolucaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evolução de tratamento não encontrada com ID: " + id));

        evolucao.adicionarProcedimento(procedimento);
        EvolucaoTratamento evolucaoAtualizada = evolucaoRepository.save(evolucao);

        return evolucaoMapper.toResponse(evolucaoAtualizada);
    }

    @Override
    @Transactional
    public EvolucaoTratamentoResponse adicionarMedicamento(Long id, String medicamento) {
        log.info("Adicionando medicamento à evolução ID: {}", id);

        EvolucaoTratamento evolucao = evolucaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evolução de tratamento não encontrada com ID: " + id));

        evolucao.adicionarMedicamento(medicamento);
        EvolucaoTratamento evolucaoAtualizada = evolucaoRepository.save(evolucao);

        return evolucaoMapper.toResponse(evolucaoAtualizada);
    }

    @Override
    @Transactional
    public EvolucaoTratamentoResponse adicionarMaterial(Long id, String material) {
        log.info("Adicionando material à evolução ID: {}", id);

        EvolucaoTratamento evolucao = evolucaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evolução de tratamento não encontrada com ID: " + id));

        evolucao.adicionarMaterial(material);
        EvolucaoTratamento evolucaoAtualizada = evolucaoRepository.save(evolucao);

        return evolucaoMapper.toResponse(evolucaoAtualizada);
    }

    @Override
    public List<EvolucaoTratamentoResponse> buscarComFiltros(Long pacienteId, Long dentistaId, Long planoDentalId,
                                                             LocalDate dataInicio, LocalDate dataFim,
                                                             String tipoEvolucao, Boolean urgente) {
        log.info("Buscando evoluções com filtros: paciente={}, dentista={}, plano={}, inicio={}, fim={}, tipo={}, urgente={}",
                pacienteId, dentistaId, planoDentalId, dataInicio, dataFim, tipoEvolucao, urgente);

        List<EvolucaoTratamento> evolucoes = evolucaoRepository.findByFiltros(
                pacienteId, dentistaId, planoDentalId, dataInicio, dataFim, tipoEvolucao, urgente);

        return evolucaoMapper.toResponseList(evolucoes);
    }

    @Override
    public Long contarPorPaciente(Long pacienteId) {
        log.info("Contando evoluções do paciente ID: {}", pacienteId);

        return evolucaoRepository.countByPacienteId(pacienteId);
    }

    @Override
    public EvolucaoTratamentoResponse buscarUltimaEvolucaoPorPaciente(Long pacienteId) {
        log.info("Buscando última evolução do paciente ID: {}", pacienteId);

        EvolucaoTratamento evolucao = evolucaoRepository.findUltimaEvolucaoByPacienteId(pacienteId)
                .orElseThrow(() -> new EntityNotFoundException("Nenhuma evolução encontrada para o paciente ID: " + pacienteId));

        return evolucaoMapper.toResponse(evolucao);
    }

    @Override
    public boolean existePorId(Long id) {
        return evolucaoRepository.existsById(id);
    }

    // Métodos auxiliares privados
    private Paciente buscarPaciente(Long pacienteId) {
        return pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado com ID: " + pacienteId));
    }

    private Dentista buscarDentista(Long dentistaId) {
        return dentistaRepository.findById(dentistaId)
                .orElseThrow(() -> new EntityNotFoundException("Dentista não encontrado com ID: " + dentistaId));
    }

    private PlanoDental buscarPlanoDental(Long planoDentalId) {
        return planoDentalRepository.findById(planoDentalId)
                .orElseThrow(() -> new EntityNotFoundException("Plano dental não encontrado com ID: " + planoDentalId));
    }
}
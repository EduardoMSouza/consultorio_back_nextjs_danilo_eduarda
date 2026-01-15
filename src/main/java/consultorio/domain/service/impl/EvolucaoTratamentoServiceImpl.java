package consultorio.domain.service.impl;

<<<<<<< HEAD
import consultorio.api.dto.request.tratamento.EvolucaoTratamentoRequest;
import consultorio.api.dto.response.tratamento.EvolucaoTratamentoResponse;
import consultorio.api.mapper.tratamento.EvolucaoTratamentoMapper;
import consultorio.domain.entity.pessoa.Dentista;
import consultorio.domain.entity.pessoa.Paciente;
import consultorio.domain.entity.tratamento.EvolucaoTratamento;
import consultorio.domain.entity.tratamento.PlanoDental;
import consultorio.domain.repository.EvolucaoTratamentoRepository;
import consultorio.domain.service.DentistaService;
import consultorio.domain.service.EvolucaoTratamentoService;
import consultorio.domain.service.PacienteService;
import consultorio.domain.service.PlanoDentalService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
=======
import consultorio.api.dto.request.EvolucaoTratamentoRequest;
import consultorio.api.dto.response.EvolucaoTratamentoResponse;
import consultorio.api.exception.ResourceNotFoundException;
import consultorio.api.mapper.EvolucaoTratamentoMapper;
import consultorio.domain.entity.EvolucaoTratamento;
import consultorio.domain.repository.EvolucaoTratamentoRepository;
import consultorio.domain.service.EvolucaoTratamentoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
<<<<<<< HEAD
@Transactional
=======
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
public class EvolucaoTratamentoServiceImpl implements EvolucaoTratamentoService {

    private final EvolucaoTratamentoRepository repository;
    private final EvolucaoTratamentoMapper mapper;
<<<<<<< HEAD
    private final PacienteService pacienteService;
    private final DentistaService dentistaService;
    private final PlanoDentalService planoDentalService;

    private static final String EVOLUCAO_NAO_ENCONTRADA = "Evolução de tratamento não encontrada com ID: ";

    @Override
    public EvolucaoTratamentoResponse criar(EvolucaoTratamentoRequest request) {
        log.info("Criando nova evolução de tratamento para paciente ID: {}", request.getPacienteId());

        // Formatar campos do request
        request.formatarCampos();

        // Buscar entidades relacionadas
        Paciente paciente = pacienteService.buscarEntidadePorId(request.getPacienteId());
        Dentista dentista = dentistaService.buscarEntidadePorId(request.getDentistaId());
        PlanoDental planoDental = planoDentalService.buscarPorId(request.getPlanoDentalId());

        // Converter para entidade
        EvolucaoTratamento evolucao = mapper.toEntity(request, paciente, dentista, planoDental);

        // Salvar
        evolucao = repository.save(evolucao);
        log.info("Evolução criada com sucesso. ID: {}", evolucao.getId());

=======

    @Override
    @Transactional
    public EvolucaoTratamentoResponse criar(EvolucaoTratamentoRequest request) {
        log.info("Criando evolução de tratamento");

        EvolucaoTratamento evolucao = mapper.toEntity(request);
        evolucao = repository.save(evolucao);

        log.info("Evolução de tratamento {} criada", evolucao.getId());
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
        return mapper.toResponse(evolucao);
    }

    @Override
<<<<<<< HEAD
    public EvolucaoTratamentoResponse buscarPorId(Long id) {
        log.debug("Buscando evolução por ID: {}", id);

        EvolucaoTratamento evolucao = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EVOLUCAO_NAO_ENCONTRADA + id));

        return mapper.toResponse(evolucao);
    }

    private EvolucaoTratamento buscarEntidadePorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EVOLUCAO_NAO_ENCONTRADA + id));
    }

    @Override
    public List<EvolucaoTratamentoResponse> listarTodos() {
        log.debug("Listando todas as evoluções ativas");
        List<EvolucaoTratamento> evolucoes = repository.findAllByAtivoTrue();
        return mapper.toResponseList(evolucoes);
    }

    @Override
    public List<EvolucaoTratamentoResponse> listarPorPaciente(Long pacienteId) {
        log.debug("Listando evoluções do paciente ID: {}", pacienteId);

        // Verificar se paciente existe
        pacienteService.verificarExistencia(pacienteId);

        List<EvolucaoTratamento> evolucoes = repository.findByPacienteIdAndAtivoTrue(pacienteId);
        return mapper.toResponseList(evolucoes);
    }

    @Override
    public List<EvolucaoTratamentoResponse> listarPorDentista(Long dentistaId) {
        log.debug("Listando evoluções do dentista ID: {}", dentistaId);

        // Verificar se dentista existe
        dentistaService.verificarExistencia(dentistaId);

        List<EvolucaoTratamento> evolucoes = repository.findByDentistaIdAndAtivoTrue(dentistaId);
        return mapper.toResponseList(evolucoes);
    }

    @Override
    public List<EvolucaoTratamentoResponse> listarPorPlanoDental(Long planoDentalId) {
        log.debug("Listando evoluções do plano dental ID: {}", planoDentalId);

        List<EvolucaoTratamento> evolucoes = repository.findByPlanoDentalIdAndAtivoTrue(planoDentalId);
        return mapper.toResponseList(evolucoes);
    }

    @Override
    public List<EvolucaoTratamentoResponse> listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        log.debug("Listando evoluções no período de {} a {}", dataInicio, dataFim);

        if (dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("Data início não pode ser maior que data fim");
        }

        List<EvolucaoTratamento> evolucoes = repository.findByDataEvolucaoBetweenAndAtivoTrue(dataInicio, dataFim);
        return mapper.toResponseList(evolucoes);
    }

    @Override
    public List<EvolucaoTratamentoResponse> listarUrgentes() {
        log.debug("Listando evoluções urgentes");
        List<EvolucaoTratamento> evolucoes = repository.findByUrgenteTrueAndAtivoTrue();
        return mapper.toResponseList(evolucoes);
    }

    @Override
    public List<EvolucaoTratamentoResponse> listarComRetornoAgendado() {
        log.debug("Listando evoluções com retorno agendado");
        List<EvolucaoTratamento> evolucoes = repository.findByRetornoNecessarioTrueAndAtivoTrue();
        return mapper.toResponseList(evolucoes);
    }

    @Override
    public EvolucaoTratamentoResponse atualizar(Long id, EvolucaoTratamentoRequest request) {
        log.info("Atualizando evolução ID: {}", id);

        // Buscar entidade existente
        EvolucaoTratamento evolucao = buscarEntidadePorId(id);

        // Formatar campos do request
        request.formatarCampos();

        // Buscar entidades relacionadas se IDs foram fornecidos
        Paciente paciente = null;
        Dentista dentista = null;
        PlanoDental planoDental = null;

        if (request.getPacienteId() != null) {
            paciente = pacienteService.buscarEntidadePorId(request.getPacienteId());
        }
        if (request.getDentistaId() != null) {
            dentista = dentistaService.buscarEntidadePorId(request.getDentistaId());
        }
        if (request.getPlanoDentalId() != null) {
            planoDental = planoDentalService.buscarPorId(request.getPlanoDentalId());
        }

        // Atualizar entidade
        mapper.updateEntityFromRequest(request, evolucao, paciente, dentista, planoDental);

        // Salvar
        evolucao = repository.save(evolucao);
        log.info("Evolução atualizada com sucesso. ID: {}", id);

=======
    @Transactional(readOnly = true)
    public EvolucaoTratamentoResponse buscarPorId(Long id) {
        EvolucaoTratamento evolucao = findById(id);
        return mapper.toResponse(evolucao);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvolucaoTratamentoResponse> listarTodos() {
        return repository.findAll().stream()
                .filter(e -> !e.getDeletado())
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvolucaoTratamentoResponse> listarPorPaciente(Long pacienteId) {
        return repository.findByPacienteIdAndDeletadoFalseOrderByDataEvolucaoDesc(pacienteId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvolucaoTratamentoResponse> listarPorDentista(Long dentistaId) {
        return repository.findByDentistaIdAndDeletadoFalseOrderByDataEvolucaoDesc(dentistaId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvolucaoTratamentoResponse> listarPorPacienteEDentista(Long pacienteId, Long dentistaId) {
        return repository.findByPacienteIdAndDentistaIdAndDeletadoFalseOrderByDataEvolucaoDesc(pacienteId, dentistaId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvolucaoTratamentoResponse> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return repository.findByDataEvolucaoBetweenAndDeletadoFalse(inicio, fim).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public EvolucaoTratamentoResponse atualizar(Long id, EvolucaoTratamentoRequest request) {
        log.info("Atualizando evolução de tratamento {}", id);

        EvolucaoTratamento evolucao = findById(id);
        mapper.updateEntityFromRequest(request, evolucao);
        evolucao = repository.save(evolucao);

        log.info("Evolução de tratamento {} atualizada", id);
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
        return mapper.toResponse(evolucao);
    }

    @Override
<<<<<<< HEAD
    public EvolucaoTratamentoResponse atualizarParcial(Long id, EvolucaoTratamentoRequest request) {
        log.info("Atualização parcial da evolução ID: {}", id);

        EvolucaoTratamento evolucao = buscarEntidadePorId(id);

        // Formatar campos não nulos
        if (request.getTitulo() != null) request.setTitulo(request.getTitulo().trim());
        if (request.getDescricao() != null) request.setDescricao(request.getDescricao().trim());

        // Atualização parcial (ignora campos nulos)
        mapper.updateEntityPartial(request, evolucao);

        evolucao = repository.save(evolucao);
        log.info("Evolução atualizada parcialmente. ID: {}", id);

        return mapper.toResponse(evolucao);
    }

    @Override
    public void desativar(Long id) {
        log.info("Desativando evolução ID: {}", id);

        EvolucaoTratamento evolucao = buscarEntidadePorId(id);
        evolucao.desativar();

        repository.save(evolucao);
        log.info("Evolução desativada. ID: {}", id);
    }

    @Override
    public void ativar(Long id) {
        log.info("Ativando evolução ID: {}", id);

        EvolucaoTratamento evolucao = buscarEntidadePorId(id);
        evolucao.ativar();

        repository.save(evolucao);
        log.info("Evolução ativada. ID: {}", id);
    }

    @Override
    public void excluir(Long id) {
        log.warn("Excluindo permanentemente evolução ID: {}", id);

        if (!repository.existsById(id)) {
            throw new EntityNotFoundException(EVOLUCAO_NAO_ENCONTRADA + id);
        }

        repository.deleteById(id);
        log.warn("Evolução excluída permanentemente. ID: {}", id);
    }

    @Override
    public void marcarComoUrgente(Long id) {
        log.info("Marcando evolução como urgente. ID: {}", id);

        EvolucaoTratamento evolucao = buscarEntidadePorId(id);
        evolucao.marcarComoUrgente();

        repository.save(evolucao);
        log.info("Evolução marcada como urgente. ID: {}", id);
    }

    @Override
    public void agendarRetorno(Long id, LocalDate dataRetorno, String motivo) {
        log.info("Agendando retorno para evolução ID: {}", id);

        if (dataRetorno.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Data de retorno não pode ser no passado");
        }

        EvolucaoTratamento evolucao = buscarEntidadePorId(id);
        evolucao.agendarRetorno(dataRetorno, motivo);

        repository.save(evolucao);
        log.info("Retorno agendado para {}. ID evolução: {}", dataRetorno, id);
    }

    @Override
    public void adicionarProcedimento(Long id, String procedimento) {
        log.info("Adicionando procedimento à evolução ID: {}", id);

        if (procedimento == null || procedimento.trim().isEmpty()) {
            throw new IllegalArgumentException("Procedimento não pode ser vazio");
        }

        EvolucaoTratamento evolucao = buscarEntidadePorId(id);
        evolucao.adicionarProcedimento(procedimento.trim());

        repository.save(evolucao);
        log.info("Procedimento adicionado. ID evolução: {}", id);
    }

    @Override
    public void adicionarMedicamento(Long id, String medicamento) {
        log.info("Adicionando medicamento à evolução ID: {}", id);

        if (medicamento == null || medicamento.trim().isEmpty()) {
            throw new IllegalArgumentException("Medicamento não pode ser vazio");
        }

        EvolucaoTratamento evolucao = buscarEntidadePorId(id);
        evolucao.adicionarMedicamento(medicamento.trim());

        repository.save(evolucao);
        log.info("Medicamento adicionado. ID evolução: {}", id);
    }

    @Override
    public void adicionarMaterial(Long id, String material) {
        log.info("Adicionando material à evolução ID: {}", id);

        if (material == null || material.trim().isEmpty()) {
            throw new IllegalArgumentException("Material não pode ser vazio");
        }

        EvolucaoTratamento evolucao = buscarEntidadePorId(id);
        evolucao.adicionarMaterial(material.trim());

        repository.save(evolucao);
        log.info("Material adicionado. ID evolução: {}", id);
    }

    @Override
    public boolean verificarRetornoAtrasado(Long id) {
        log.debug("Verificando se retorno está atrasado para evolução ID: {}", id);

        EvolucaoTratamento evolucao = buscarEntidadePorId(id);
        return evolucao.retornoAtrasado();
    }

    @Override
    public boolean verificarSeUrgente(Long id) {
        log.debug("Verificando se evolução é urgente. ID: {}", id);

        EvolucaoTratamento evolucao = buscarEntidadePorId(id);
        return evolucao.eUrgente();
    }

    @Override
    public boolean verificarSeAtiva(Long id) {
        log.debug("Verificando se evolução está ativa. ID: {}", id);

        EvolucaoTratamento evolucao = buscarEntidadePorId(id);
        return evolucao.estaAtiva();
    }
}
=======
    @Transactional
    public void deletar(Long id) {
        log.info("Deletando evolução de tratamento {}", id);

        EvolucaoTratamento evolucao = findById(id);
        evolucao.setDeletado(true);
        repository.save(evolucao);

        log.info("Evolução de tratamento {} deletada", id);
    }

    private EvolucaoTratamento findById(Long id) {
        return repository.findById(id)
                .filter(e -> !e.getDeletado())
                .orElseThrow(() -> new ResourceNotFoundException("Evolução de tratamento não encontrada com id: " + id));
    }
}
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb

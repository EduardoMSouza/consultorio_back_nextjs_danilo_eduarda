package consultorio.domain.service.evolucao_tratamento.impl;


import consultorio.api.dto.request.tratamento.AtualizarEvolucaoTratamentoRequest;
import consultorio.api.dto.request.tratamento.CriarEvolucaoTratamentoRequest;
import consultorio.api.dto.response.tratamento.EvolucaoTratamentoDetalheResponse;
import consultorio.api.dto.response.tratamento.EvolucaoTratamentoResponse;
import consultorio.api.dto.response.tratamento.PaginacaoResponse;
import consultorio.api.dto.response.tratamento.ResumoEvolucaoResponse;
import consultorio.api.mapper.tratamento.EvolucaoTratamentoMapper;
import consultorio.domain.entity.pessoa.Dentista;
import consultorio.domain.entity.pessoa.Paciente;
import consultorio.domain.entity.tratamento.EvolucaoTratamento;
import consultorio.domain.repository.pessoa.DentistaRepository;
import consultorio.domain.repository.pessoa.PacienteRepository;
import consultorio.domain.repository.tratamento.EvolucaoTratamentoRepository;
import consultorio.domain.service.evolucao_tratamento.EvolucaoTratamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EvolucaoTratamentoServiceImpl implements EvolucaoTratamentoService {

    private final EvolucaoTratamentoRepository evolucaoRepository;
    private final PacienteRepository pacienteRepository;
    private final DentistaRepository dentistaRepository;
    private final EvolucaoTratamentoMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<EvolucaoTratamentoResponse> listarTodos() {
        return evolucaoRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PaginacaoResponse<EvolucaoTratamentoResponse> listarPaginado(Pageable pageable) {
        Page<EvolucaoTratamento> page = evolucaoRepository.findAllComPaginacao(pageable);

        List<EvolucaoTratamentoResponse> conteudo = page.getContent().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());

        return PaginacaoResponse.<EvolucaoTratamentoResponse>builder()
                .conteudo(conteudo)
                .paginaAtual(page.getNumber())
                .totalPaginas(page.getTotalPages())
                .totalElementos(page.getTotalElements())
                .tamanhoPagina(page.getSize())
                .primeiraPagina(page.isFirst())
                .ultimaPagina(page.isLast())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public EvolucaoTratamentoDetalheResponse buscarPorId(Long id) {
        return evolucaoRepository.findById(id)
                .map(mapper::toDetalheResponse)
                .orElseThrow(() -> new RuntimeException("Evolução não encontrada com ID: " + id));
    }

    @Override
    public EvolucaoTratamentoResponse criar(CriarEvolucaoTratamentoRequest request) {
        // Validar paciente
        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado com ID: " + request.getPacienteId()));

        // Validar dentista
        Dentista dentista = dentistaRepository.findById(request.getDentistaId())
                .orElseThrow(() -> new RuntimeException("Dentista não encontrado com ID: " + request.getDentistaId()));

        // Verificar se já existe evolução para o mesmo paciente na mesma data
        if (evolucaoRepository.findByPacienteIdAndData(paciente.getId(), request.getData()).isPresent()) {
            throw new RuntimeException("Já existe uma evolução registrada para este paciente na data informada");
        }

        // Criar entidade
        EvolucaoTratamento evolucao = mapper.toEntity(request, paciente, dentista);

        // Salvar
        EvolucaoTratamento saved = evolucaoRepository.save(evolucao);
        return mapper.toResponse(saved);
    }

    @Override
    public EvolucaoTratamentoResponse atualizar(Long id, AtualizarEvolucaoTratamentoRequest request) {
        EvolucaoTratamento evolucao = evolucaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evolução não encontrada com ID: " + id));

        // Se estiver alterando a data, verificar conflitos
        if (request.getData() != null && !request.getData().equals(evolucao.getData())) {
            if (evolucaoRepository.findByPacienteIdAndData(evolucao.getPaciente().getId(), request.getData()).isPresent()) {
                throw new RuntimeException("Já existe uma evolução registrada para este paciente na nova data informada");
            }
        }

        // Atualizar campos
        mapper.updateEntityFromDTO(request, evolucao);

        EvolucaoTratamento updated = evolucaoRepository.save(evolucao);
        return mapper.toResponse(updated);
    }

    @Override
    public void excluir(Long id) {
        EvolucaoTratamento evolucao = evolucaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evolução não encontrada com ID: " + id));

        // Verificar se é evolução recente (permitir exclusão apenas de evoluções recentes)
        LocalDate dataLimite = LocalDate.now().minusDays(7); // Permitir exclusão de evoluções dos últimos 7 dias
        if (evolucao.getData().isBefore(dataLimite)) {
            throw new RuntimeException("Não é possível excluir evoluções com mais de 7 dias");
        }

        evolucaoRepository.delete(evolucao);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvolucaoTratamentoResponse> buscarPorPaciente(Long pacienteId) {
        return evolucaoRepository.findByPacienteIdOrderByDataDesc(pacienteId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PaginacaoResponse<EvolucaoTratamentoResponse> buscarPorPacientePaginado(Long pacienteId, Pageable pageable) {
        Page<EvolucaoTratamento> page = evolucaoRepository.findByPacienteIdOrderByDataDesc(pacienteId, pageable);

        List<EvolucaoTratamentoResponse> conteudo = page.getContent().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());

        return PaginacaoResponse.<EvolucaoTratamentoResponse>builder()
                .conteudo(conteudo)
                .paginaAtual(page.getNumber())
                .totalPaginas(page.getTotalPages())
                .totalElementos(page.getTotalElements())
                .tamanhoPagina(page.getSize())
                .primeiraPagina(page.isFirst())
                .ultimaPagina(page.isLast())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvolucaoTratamentoResponse> buscarPorDentista(Long dentistaId) {
        return evolucaoRepository.findByDentistaIdOrderByDataDesc(dentistaId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvolucaoTratamentoResponse> buscarPorData(LocalDate data) {
        return evolucaoRepository.findByDataOrderByPacienteNomeAsc(data).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvolucaoTratamentoResponse> buscarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        return evolucaoRepository.findByPeriodo(dataInicio, dataFim).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvolucaoTratamentoResponse> buscarPorPacienteEPeriodo(Long pacienteId, LocalDate dataInicio, LocalDate dataFim) {
        return evolucaoRepository.findByPacienteAndPeriodo(pacienteId, dataInicio, dataFim).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EvolucaoTratamentoDetalheResponse buscarUltimaEvolucaoPaciente(Long pacienteId) {
        return evolucaoRepository.findUltimaEvolucaoPaciente(pacienteId)
                .map(mapper::toDetalheResponse)
                .orElseThrow(() -> new RuntimeException("Nenhuma evolução encontrada para o paciente com ID: " + pacienteId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResumoEvolucaoResponse> buscarEvolucoesDoDia() {
        return evolucaoRepository.findEvolucoesDoDia().stream()
                .map(mapper::toResumoResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResumoEvolucaoResponse> buscarPorTextoEvolucao(String texto) {
        return evolucaoRepository.findByTextoEvolucao(texto).stream()
                .map(mapper::toResumoResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Long contarEvolucoesPorPaciente(Long pacienteId) {
        return evolucaoRepository.countByPacienteId(pacienteId);
    }

    @Override
    public Long contarTotalEvolucoes() {
        return evolucaoRepository.count();
    }

    @Override
    public boolean existeEvolucaoNaData(Long pacienteId, LocalDate data) {
        return evolucaoRepository.findByPacienteIdAndData(pacienteId, data).isPresent();
    }
}
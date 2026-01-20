package consultorio.domain.service.plano_dental.impl;

import consultorio.api.dto.request.plano_dental.AtualizarPlanoDentalRequest;
import consultorio.api.dto.request.plano_dental.CriarPlanoDentalRequest;
import consultorio.api.dto.response.plano_dental.PlanoDentalDetalheResponse;
import consultorio.api.dto.response.plano_dental.PlanoDentalResponse;
import consultorio.api.dto.response.plano_dental.ResumoPlanoDentalResponse;
import consultorio.api.dto.response.tratamento.PaginacaoResponse;
import consultorio.api.mapper.tratamento.PlanoDentalMapper;
import consultorio.domain.entity.pessoa.Dentista;
import consultorio.domain.entity.pessoa.Paciente;
import consultorio.domain.entity.tratamento.PlanoDental;
import consultorio.domain.repository.pessoa.DentistaRepository;
import consultorio.domain.repository.pessoa.PacienteRepository;
import consultorio.domain.repository.tratamento.PlanoDentalRepository;
import consultorio.domain.service.plano_dental.PlanoDentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PlanoDentalServiceImpl implements PlanoDentalService {

    private final PlanoDentalRepository planoDentalRepository;
    private final PacienteRepository pacienteRepository;
    private final DentistaRepository dentistaRepository;
    private final PlanoDentalMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<PlanoDentalResponse> listarTodos() {
        return planoDentalRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PaginacaoResponse<PlanoDentalResponse> listarPaginado(Pageable pageable) {
        Page<PlanoDental> page = planoDentalRepository.findAll(pageable);

        List<PlanoDentalResponse> conteudo = page.getContent().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());

        return PaginacaoResponse.<PlanoDentalResponse>builder()
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
    public PlanoDentalDetalheResponse buscarPorId(Long id) {
        return planoDentalRepository.findById(id)
                .map(mapper::toDetalheResponse)
                .orElseThrow(() -> new RuntimeException("Plano dental não encontrado com ID: " + id));
    }

    @Override
    public PlanoDentalResponse criar(CriarPlanoDentalRequest request) {
        // Validar paciente
        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado com ID: " + request.getPacienteId()));

        // Validar dentista
        Dentista dentista = dentistaRepository.findById(request.getDentistaId())
                .orElseThrow(() -> new RuntimeException("Dentista não encontrado com ID: " + request.getDentistaId()));

        // Verificar se já existe plano para o mesmo dente do paciente
        if (planoDentalRepository.existsByPacienteAndDente(paciente.getId(), request.getDente())) {
            throw new RuntimeException("Já existe um plano dental registrado para este dente do paciente");
        }

        // Criar entidade
        PlanoDental planoDental = mapper.toEntity(request, paciente, dentista);

        // Salvar
        PlanoDental saved = planoDentalRepository.save(planoDental);
        return mapper.toResponse(saved);
    }

    @Override
    public PlanoDentalResponse atualizar(Long id, AtualizarPlanoDentalRequest request) {
        PlanoDental planoDental = planoDentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plano dental não encontrado com ID: " + id));

        // Se estiver alterando o dente, verificar conflitos
        if (request.getDente() != null && !request.getDente().equals(planoDental.getDente())) {
            if (planoDentalRepository.existsByPacienteAndDente(planoDental.getPaciente().getId(), request.getDente())) {
                throw new RuntimeException("Já existe um plano dental registrado para este dente do paciente");
            }
        }

        // Atualizar campos
        mapper.updateEntityFromDTO(request, planoDental);

        PlanoDental updated = planoDentalRepository.save(planoDental);
        return mapper.toResponse(updated);
    }

    @Override
    public void excluir(Long id) {
        PlanoDental planoDental = planoDentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plano dental não encontrado com ID: " + id));

        planoDentalRepository.delete(planoDental);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanoDentalResponse> buscarPorPaciente(Long pacienteId) {
        return planoDentalRepository.findByPacienteIdOrderByCriadoEmDesc(pacienteId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PaginacaoResponse<PlanoDentalResponse> buscarPorPacientePaginado(Long pacienteId, Pageable pageable) {
        Page<PlanoDental> page = planoDentalRepository.findByPacienteIdOrderByCriadoEmDesc(pacienteId, pageable);

        List<PlanoDentalResponse> conteudo = page.getContent().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());

        return PaginacaoResponse.<PlanoDentalResponse>builder()
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
    public List<PlanoDentalResponse> buscarPorDentista(Long dentistaId) {
        return planoDentalRepository.findByDentistaIdOrderByCriadoEmDesc(dentistaId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanoDentalResponse> buscarPorDente(String dente) {
        return planoDentalRepository.findByDenteOrderByCriadoEmDesc(dente).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanoDentalResponse> buscarPorProcedimento(String procedimento) {
        return planoDentalRepository.findByProcedimentoContainingIgnoreCaseOrderByCriadoEmDesc(procedimento).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanoDentalResponse> buscarPorPeriodo(String dataInicioStr, String dataFimStr) {
        try {
            LocalDate dataInicio = LocalDate.parse(dataInicioStr);
            LocalDate dataFim = LocalDate.parse(dataFimStr);

            LocalDateTime inicio = dataInicio.atStartOfDay();
            LocalDateTime fim = dataFim.atTime(23, 59, 59);

            return planoDentalRepository.findByPeriodo(inicio, fim).stream()
                    .map(mapper::toResponse)
                    .collect(Collectors.toList());
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Formato de data inválido. Use yyyy-MM-dd");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanoDentalResponse> buscarPorPacienteEPeriodo(Long pacienteId, String dataInicioStr, String dataFimStr) {
        try {
            LocalDate dataInicio = LocalDate.parse(dataInicioStr);
            LocalDate dataFim = LocalDate.parse(dataFimStr);

            LocalDateTime inicio = dataInicio.atStartOfDay();
            LocalDateTime fim = dataFim.atTime(23, 59, 59);

            return planoDentalRepository.findByPacienteAndPeriodo(pacienteId, inicio, fim).stream()
                    .map(mapper::toResponse)
                    .collect(Collectors.toList());
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Formato de data inválido. Use yyyy-MM-dd");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanoDentalResponse> buscarPlanosComDesconto() {
        return planoDentalRepository.findPlanosComDesconto().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResumoPlanoDentalResponse> buscarPlanosRecentes(int quantidade) {
        Pageable pageable = PageRequest.of(0, quantidade);
        return planoDentalRepository.findPlanosRecentes(pageable).stream()
                .map(mapper::toResumoResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calcularValorTotalPorPaciente(Long pacienteId) {
        return planoDentalRepository.somarValorTotalPorPaciente(pacienteId)
                .orElse(BigDecimal.ZERO);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calcularValorTotalPorDentista(Long dentistaId) {
        return planoDentalRepository.somarValorTotalPorDentista(dentistaId)
                .orElse(BigDecimal.ZERO);
    }

    @Override
    public Long contarPlanosPorPaciente(Long pacienteId) {
        return planoDentalRepository.countByPacienteId(pacienteId);
    }

    @Override
    public boolean existePlanoParaDente(Long pacienteId, String dente) {
        return planoDentalRepository.existsByPacienteAndDente(pacienteId, dente);
    }

    @Override
    public PlanoDentalResponse aplicarDesconto(Long id, BigDecimal valorDesconto) {
        PlanoDental planoDental = planoDentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plano dental não encontrado com ID: " + id));

        BigDecimal valorAtual = planoDental.getValor();
        if (valorAtual == null || valorAtual.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Valor original inválido para aplicar desconto");
        }

        if (valorDesconto.compareTo(valorAtual) >= 0) {
            throw new RuntimeException("Valor do desconto não pode ser maior ou igual ao valor original");
        }

        BigDecimal valorFinal = valorAtual.subtract(valorDesconto);
        planoDental.setValorFinal(valorFinal);

        PlanoDental updated = planoDentalRepository.save(planoDental);
        return mapper.toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> buscarTopProcedimentos() {
        return planoDentalRepository.findTopProcedimentos();
    }
}
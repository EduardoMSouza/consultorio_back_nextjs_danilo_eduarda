package consultorio.domain.service.paciente.impl;

import consultorio.api.dto.request.pessoa.PacienteRequest;
import consultorio.api.dto.response.pessoa.PacienteResponse;
import consultorio.api.dto.response.pessoa.PacienteResumoResponse;
import consultorio.api.mapper.pessoa.PacienteMapper;
import consultorio.domain.entity.pessoa.Paciente;
import consultorio.domain.repository.pessoa.PacienteRepository;
import consultorio.domain.service.pessoa.PacienteService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;
    private final PacienteMapper pacienteMapper;

    private static final String PACIENTE_NAO_ENCONTRADO = "Paciente não encontrado com ID: ";
    private static final String PACIENTE_INATIVO = "Paciente está inativo. ID: ";

    // ==================== CRUD BÁSICO ====================

    @Override
    @Transactional
    public PacienteResponse criar(PacienteRequest request) {
        log.info("Criando novo paciente: {}", request.getDadosBasicos().getNome());

        // Validar unicidade do prontuário
        if (existePorProntuario(request.getDadosBasicos().getProntuarioNumero())) {
            throw new IllegalArgumentException("Já existe um paciente com este número de prontuário");
        }

        // Validar unicidade do CPF (se fornecido)
        if (request.getDadosBasicos().getCpf() != null && !request.getDadosBasicos().getCpf().isEmpty()) {
            if (existePorCpf(request.getDadosBasicos().getCpf())) {
                throw new IllegalArgumentException("Já existe um paciente com este CPF");
            }
        }

        // Validar unicidade do email (se fornecido)
        if (request.getDadosBasicos().getEmail() != null && !request.getDadosBasicos().getEmail().isEmpty()) {
            if (existePorEmail(request.getDadosBasicos().getEmail())) {
                throw new IllegalArgumentException("Já existe um paciente com este email");
            }
        }

        Paciente paciente = pacienteMapper.toEntity(request);
        paciente.setAtivo(true);
        paciente.setCriadoEm(LocalDateTime.now());
        paciente.setAtualizadoEm(LocalDateTime.now());

        Paciente pacienteSalvo = pacienteRepository.save(paciente);
        log.info("Paciente criado com ID: {}", pacienteSalvo.getId());

        return pacienteMapper.toResponse(pacienteSalvo);
    }

    @Override
    @Transactional(readOnly = true)
    public PacienteResponse buscarPorId(Long id) {
        log.debug("Buscando paciente por ID: {}", id);

        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(PACIENTE_NAO_ENCONTRADO + id));

        if (!paciente.getAtivo()) {
            log.warn("Tentativa de acesso a paciente inativo. ID: {}", id);
            throw new IllegalStateException(PACIENTE_INATIVO + id);
        }

        return pacienteMapper.toResponse(paciente);
    }

    @Override
    @Transactional
    public PacienteResponse atualizar(Long id, PacienteRequest request) {
        log.info("Atualizando paciente ID: {}", id);

        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(PACIENTE_NAO_ENCONTRADO + id));

        if (!paciente.getAtivo()) {
            throw new IllegalStateException(PACIENTE_INATIVO + id);
        }

        // Validar unicidade do CPF (se alterado)
        if (request.getDadosBasicos().getCpf() != null && !request.getDadosBasicos().getCpf().isEmpty()) {
            if (existePorCpfExcluindoId(request.getDadosBasicos().getCpf(), id)) {
                throw new IllegalArgumentException("Já existe outro paciente com este CPF");
            }
        }

        // Validar unicidade do email (se alterado)
        if (request.getDadosBasicos().getEmail() != null && !request.getDadosBasicos().getEmail().isEmpty()) {
            if (existePorEmail(request.getDadosBasicos().getEmail())) {
                // Verificar se não é o mesmo paciente
                Paciente pacientePorEmail = pacienteRepository.findByEmailIgnoreCase(request.getDadosBasicos().getEmail())
                        .orElse(null);
                if (pacientePorEmail != null && !pacientePorEmail.getId().equals(id)) {
                    throw new IllegalArgumentException("Já existe outro paciente com este email");
                }
            }
        }

        pacienteMapper.updateEntityFromRequest(request, paciente);
        paciente.setAtualizadoEm(LocalDateTime.now());

        Paciente pacienteAtualizado = pacienteRepository.save(paciente);
        log.info("Paciente atualizado com sucesso. ID: {}", id);

        return pacienteMapper.toResponse(pacienteAtualizado);
    }

    @Override
    @Transactional
    public void inativar(Long id) {
        log.info("Inativando paciente ID: {}", id);

        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(PACIENTE_NAO_ENCONTRADO + id));

        if (!paciente.getAtivo()) {
            log.warn("Paciente já está inativo. ID: {}", id);
            return;
        }

        paciente.setAtivo(false);
        paciente.setAtualizadoEm(LocalDateTime.now());
        pacienteRepository.save(paciente);

        log.info("Paciente inativado com sucesso. ID: {}", id);
    }

    @Override
    @Transactional
    public void ativar(Long id) {
        log.info("Ativando paciente ID: {}", id);

        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(PACIENTE_NAO_ENCONTRADO + id));

        if (paciente.getAtivo()) {
            log.warn("Paciente já está ativo. ID: {}", id);
            return;
        }

        paciente.setAtivo(true);
        paciente.setAtualizadoEm(LocalDateTime.now());
        pacienteRepository.save(paciente);

        log.info("Paciente ativado com sucesso. ID: {}", id);
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        log.warn("Excluindo paciente ID: {}", id);

        if (!pacienteRepository.existsById(id)) {
            throw new EntityNotFoundException(PACIENTE_NAO_ENCONTRADO + id);
        }

        pacienteRepository.deleteById(id);
        log.info("Paciente excluído permanentemente. ID: {}", id);
    }

    // ==================== BUSCAS ====================

    @Override
    @Transactional(readOnly = true)
    public PacienteResponse buscarPorProntuario(String prontuarioNumero) {
        log.debug("Buscando paciente por prontuário: {}", prontuarioNumero);

        Paciente paciente = pacienteRepository.findByProntuarioNumero(prontuarioNumero)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado com prontuário: " + prontuarioNumero));

        if (!paciente.getAtivo()) {
            throw new IllegalStateException(PACIENTE_INATIVO + paciente.getId());
        }

        return pacienteMapper.toResponse(paciente);
    }

    @Override
    @Transactional(readOnly = true)
    public PacienteResponse buscarPorCpf(String cpf) {
        log.debug("Buscando paciente por CPF: {}", cpf);

        Paciente paciente = pacienteRepository.findByCpf(cpf)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado com CPF: " + cpf));

        if (!paciente.getAtivo()) {
            throw new IllegalStateException(PACIENTE_INATIVO + paciente.getId());
        }

        return pacienteMapper.toResponse(paciente);
    }

    @Override
    @Transactional(readOnly = true)
    public PacienteResponse buscarPorEmail(String email) {
        log.debug("Buscando paciente por email: {}", email);

        Paciente paciente = pacienteRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado com email: " + email));

        if (!paciente.getAtivo()) {
            throw new IllegalStateException(PACIENTE_INATIVO + paciente.getId());
        }

        return pacienteMapper.toResponse(paciente);
    }

    // ==================== LISTAGENS ====================

    @Override
    @Transactional(readOnly = true)
    public List<PacienteResponse> listarTodos() {
        log.debug("Listando todos os pacientes");

        List<Paciente> pacientes = pacienteRepository.findAll();
        return pacienteMapper.toResponseList(pacientes);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PacienteResponse> listarTodos(Pageable pageable) {
        log.debug("Listando todos os pacientes paginados");

        Page<Paciente> pacientePage = pacienteRepository.findAll(pageable);
        return pacientePage.map(pacienteMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PacienteResponse> listarAtivos() {
        log.debug("Listando pacientes ativos");

        List<Paciente> pacientes = pacienteRepository.findByAtivo(true);
        return pacienteMapper.toResponseList(pacientes);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PacienteResponse> listarAtivos(Pageable pageable) {
        log.debug("Listando pacientes ativos paginados");

        Page<Paciente> pacientePage = pacienteRepository.findByAtivo(true, pageable);
        return pacientePage.map(pacienteMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PacienteResponse> listarInativos() {
        log.debug("Listando pacientes inativos");

        List<Paciente> pacientes = pacienteRepository.findByAtivo(false);
        return pacienteMapper.toResponseList(pacientes);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PacienteResumoResponse> listarResumo() {
        log.debug("Listando resumo de pacientes");

        List<Paciente> pacientes = pacienteRepository.findByAtivo(true);
        return pacienteMapper.toResumoResponseList(pacientes);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PacienteResumoResponse> listarResumo(Pageable pageable) {
        log.debug("Listando resumo de pacientes paginados");

        Page<Paciente> pacientePage = pacienteRepository.findByAtivo(true, pageable);
        return pacientePage.map(pacienteMapper::toResumoResponse);
    }

    // ==================== BUSCAS POR NOME ====================

    @Override
    @Transactional(readOnly = true)
    public List<PacienteResponse> buscarPorNome(String nome) {
        log.debug("Buscando pacientes por nome: {}", nome);

        if (nome == null || nome.trim().isEmpty()) {
            return listarAtivos();
        }

        List<Paciente> pacientes = pacienteRepository.findByNomeContaining(nome.trim());
        return pacienteMapper.toResponseList(pacientes);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PacienteResponse> buscarPorNome(String nome, Pageable pageable) {
        log.debug("Buscando pacientes por nome paginado: {}", nome);

        if (nome == null || nome.trim().isEmpty()) {
            return listarAtivos(pageable);
        }

        Page<Paciente> pacientePage = pacienteRepository.findByNomeContaining(nome.trim(), pageable);
        return pacientePage.map(pacienteMapper::toResponse);
    }

    // ==================== BUSCAS POR CONVÊNIO ====================

    @Override
    @Transactional(readOnly = true)
    public List<PacienteResponse> buscarPorConvenio(String convenio) {
        log.debug("Buscando pacientes por convênio: {}", convenio);

        List<Paciente> pacientes = pacienteRepository.findByConvenio(convenio);
        return pacienteMapper.toResponseList(pacientes);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PacienteResponse> buscarPorConvenio(String convenio, Pageable pageable) {
        log.debug("Buscando pacientes por convênio paginado: {}", convenio);

        Page<Paciente> pacientePage = pacienteRepository.findByConvenio(convenio, pageable);
        return pacientePage.map(pacienteMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> listarConveniosDistintos() {
        log.debug("Listando convênios distintos");

        return pacienteRepository.findDistinctConvenios();
    }

    // ==================== BUSCAS POR DATA ====================

    @Override
    @Transactional(readOnly = true)
    public List<PacienteResponse> buscarPorDataNascimento(LocalDate dataNascimento) {
        log.debug("Buscando pacientes por data de nascimento: {}", dataNascimento);

        List<Paciente> pacientes = pacienteRepository.findByDataNascimento(dataNascimento);
        return pacienteMapper.toResponseList(pacientes);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PacienteResponse> buscarPorDataNascimentoEntre(LocalDate inicio, LocalDate fim) {
        log.debug("Buscando pacientes por data de nascimento entre: {} e {}", inicio, fim);

        List<Paciente> pacientes = pacienteRepository.findByDataNascimentoBetween(inicio, fim);
        return pacienteMapper.toResponseList(pacientes);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PacienteResponse> buscarAniversariantesDoMes(Integer mes) {
        log.debug("Buscando aniversariantes do mês: {}", mes);

        if (mes < 1 || mes > 12) {
            throw new IllegalArgumentException("Mês inválido. Deve ser entre 1 e 12");
        }

        List<Paciente> pacientes = pacienteRepository.findByMesAniversario(mes);
        return pacienteMapper.toResponseList(pacientes);
    }

    // ==================== BUSCAS POR TELEFONE ====================

    @Override
    @Transactional(readOnly = true)
    public List<PacienteResponse> buscarPorTelefone(String telefone) {
        log.debug("Buscando pacientes por telefone: {}", telefone);

        if (telefone == null || telefone.trim().isEmpty()) {
            return List.of();
        }

        // Remover caracteres não numéricos para busca
        String telefoneBusca = telefone.replaceAll("[^0-9]", "").trim();

        List<Paciente> pacientes = pacienteRepository.findByTelefoneContaining(telefoneBusca);
        return pacienteMapper.toResponseList(pacientes);
    }

    // ==================== BUSCAS COM FILTROS ====================

    @Override
    @Transactional(readOnly = true)
    public Page<PacienteResponse> buscarComFiltros(String nome, String cpf, String convenio,
                                                   Boolean ativo, Boolean status, Pageable pageable) {
        log.debug("Buscando pacientes com filtros - nome: {}, cpf: {}, convênio: {}, ativo: {}, status: {}",
                nome, cpf, convenio, ativo, status);

        Page<Paciente> pacientePage = pacienteRepository.findWithFilters(
                nome, cpf, convenio, ativo, status, pageable);

        return pacientePage.map(pacienteMapper::toResponse);
    }

    // ==================== BUSCAS POR IDADE ====================

    @Override
    @Transactional(readOnly = true)
    public List<PacienteResponse> buscarPorFaixaEtaria(Integer idadeMin, Integer idadeMax) {
        log.debug("Buscando pacientes por faixa etária: {} - {}", idadeMin, idadeMax);

        if (idadeMin < 0 || idadeMax < 0 || idadeMin > idadeMax) {
            throw new IllegalArgumentException("Faixa etária inválida");
        }

        List<Paciente> pacientes = pacienteRepository.findByFaixaEtaria(idadeMin, idadeMax);
        return pacienteMapper.toResponseList(pacientes);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PacienteResponse> buscarMenoresDeIdade() {
        log.debug("Buscando pacientes menores de idade");

        List<Paciente> pacientes = pacienteRepository.findMenoresDeIdade();
        return pacienteMapper.toResponseList(pacientes);
    }

    // ==================== VERIFICAÇÕES ====================

    @Override
    @Transactional(readOnly = true)
    public boolean existePorProntuario(String prontuarioNumero) {
        return pacienteRepository.existsByProntuarioNumero(prontuarioNumero);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorCpf(String cpf) {
        return pacienteRepository.existsByCpf(cpf);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorCpfExcluindoId(String cpf, Long id) {
        return pacienteRepository.existsByCpfAndIdNot(cpf, id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorEmail(String email) {
        return pacienteRepository.existsByEmailIgnoreCase(email);
    }

    // ==================== ESTATÍSTICAS ====================

    @Override
    @Transactional(readOnly = true)
    public Long contarAtivos() {
        log.debug("Contando pacientes ativos");
        return pacienteRepository.countAtivos();
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarInativos() {
        log.debug("Contando pacientes inativos");
        return pacienteRepository.countInativos();
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarComConvenio() {
        log.debug("Contando pacientes com convênio");
        return pacienteRepository.countComConvenio();
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarSemConvenio() {
        log.debug("Contando pacientes sem convênio");
        return pacienteRepository.countSemConvenio();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> contarPorConvenio() {
        log.debug("Contando pacientes por convênio");
        return pacienteRepository.countByConvenio();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> contarPorFaixaEtaria() {
        log.debug("Contando pacientes por faixa etária");
        return pacienteRepository.countByFaixaEtaria();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> contarPorMesCadastro() {
        log.debug("Contando pacientes por mês de cadastro");
        return pacienteRepository.countByMesCadastro();
    }

    // ==================== BUSCAS POR CONDIÇÕES DE SAÚDE ====================

    @Override
    @Transactional(readOnly = true)
    public List<PacienteResponse> buscarComDiabetes() {
        log.debug("Buscando pacientes com diabetes");

        List<Paciente> pacientes = pacienteRepository.findComDiabetes();
        return pacienteMapper.toResponseList(pacientes);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PacienteResponse> buscarComHipertensao() {
        log.debug("Buscando pacientes com hipertensão");

        List<Paciente> pacientes = pacienteRepository.findComHipertensao();
        return pacienteMapper.toResponseList(pacientes);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PacienteResponse> buscarFumantes() {
        log.debug("Buscando pacientes fumantes");

        List<Paciente> pacientes = pacienteRepository.findFumantes();
        return pacienteMapper.toResponseList(pacientes);
    }

    // ==================== BUSCAS POR RESPONSÁVEL ====================

    @Override
    @Transactional(readOnly = true)
    public List<PacienteResponse> buscarComResponsavel() {
        log.debug("Buscando pacientes com responsável");

        List<Paciente> pacientes = pacienteRepository.findComResponsavel();
        return pacienteMapper.toResponseList(pacientes);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PacienteResponse> buscarPorNomeResponsavel(String nomeResponsavel) {
        log.debug("Buscando pacientes por nome do responsável: {}", nomeResponsavel);

        if (nomeResponsavel == null || nomeResponsavel.trim().isEmpty()) {
            return List.of();
        }

        List<Paciente> pacientes = pacienteRepository.findByNomeResponsavel(nomeResponsavel.trim());
        return pacienteMapper.toResponseList(pacientes);
    }

    // ==================== RELATÓRIOS ====================

    @Override
    @Transactional(readOnly = true)
    public List<PacienteResponse> gerarRelatorioCadastro(LocalDate inicio, LocalDate fim) {
        log.debug("Gerando relatório de cadastro entre {} e {}", inicio, fim);

        if (inicio.isAfter(fim)) {
            throw new IllegalArgumentException("Data início não pode ser após data fim");
        }

        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime fimDateTime = fim.atTime(LocalTime.MAX);

        List<Paciente> pacientes = pacienteRepository.findParaRelatorioCadastro(inicioDateTime, fimDateTime);
        return pacienteMapper.toResponseList(pacientes);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> gerarRelatorioNaturalidades() {
        log.debug("Gerando relatório de naturalidades");
        return pacienteRepository.findTopNaturalidades();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> gerarRelatorioNovosPacientesUltimos12Meses() {
        log.debug("Gerando relatório de novos pacientes últimos 12 meses");
        return pacienteRepository.findNovosPacientesUltimos12Meses();
    }
}
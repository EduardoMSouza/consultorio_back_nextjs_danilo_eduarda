package consultorio.domain.repository;

<<<<<<< HEAD

import consultorio.domain.entity.agendamento.Agendamento;
import consultorio.domain.entity.agendamento.enums.StatusAgendamento;
import consultorio.domain.entity.pessoa.Dentista;
import consultorio.domain.entity.pessoa.Paciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
=======
import consultorio.domain.entity.Agendamento;
import consultorio.domain.entity.Agendamento.StatusAgendamento;
import consultorio.domain.entity.Agendamento.TipoProcedimento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

<<<<<<< HEAD
    // Buscas por relacionamentos
    List<Agendamento> findByDentista(Dentista dentista);
    List<Agendamento> findByPaciente(Paciente paciente);
    List<Agendamento> findByDentistaAndPaciente(Dentista dentista, Paciente paciente);

    // Buscas por datas
    List<Agendamento> findByDataConsulta(LocalDate dataConsulta);
    List<Agendamento> findByDataConsultaBetween(LocalDate startDate, LocalDate endDate);
    List<Agendamento> findByDataConsultaAndDentista(LocalDate dataConsulta, Dentista dentista);
    List<Agendamento> findByDataConsultaAndPaciente(LocalDate dataConsulta, Paciente paciente);

    // Buscas por status
    List<Agendamento> findByStatus(StatusAgendamento status);
    List<Agendamento> findByStatusAndAtivoTrue(StatusAgendamento status);
    List<Agendamento> findByStatusAndAtivoFalse(StatusAgendamento status);

    // Buscas por ativo/inativo
    List<Agendamento> findByAtivoTrue();
    List<Agendamento> findByAtivoFalse();

    // Buscas por dentista e data
    List<Agendamento> findByDentistaAndDataConsultaAndAtivoTrue(Dentista dentista, LocalDate dataConsulta);
    List<Agendamento> findByDentistaAndDataConsultaBetween(Dentista dentista, LocalDate startDate, LocalDate endDate);

    // Buscas por paciente e data
    List<Agendamento> findByPacienteAndDataConsultaBetween(Paciente paciente, LocalDate startDate, LocalDate endDate);

    // Buscas com datas futuras/passadas
    List<Agendamento> findByDataConsultaBeforeAndAtivoTrue(LocalDate data);
    List<Agendamento> findByDataConsultaAfterAndAtivoTrue(LocalDate data);
    List<Agendamento> findByDataConsultaGreaterThanEqualAndAtivoTrue(LocalDate data);

    // Buscas de hoje
    List<Agendamento> findByDataConsultaAndAtivoTrue(LocalDate hoje);

    // Buscas por confirmação
    List<Agendamento> findByConfirmadoEmIsNotNull();
    List<Agendamento> findByConfirmadoEmIsNull();
    List<Agendamento> findByConfirmadoEmIsNotNullAndDataConsultaAfter(LocalDate data);

    // Buscas por lembrete
    List<Agendamento> findByLembreteEnviadoTrue();
    List<Agendamento> findByLembreteEnviadoFalse();
    List<Agendamento> findByLembreteEnviadoFalseAndDataConsultaAfter(LocalDate data);

    // Buscas por cancelamento
    List<Agendamento> findByCanceladoEmIsNotNull();
    List<Agendamento> findByCanceladoEmIsNull();

    // Buscas paginadas
    Page<Agendamento> findByAtivoTrue(Pageable pageable);
    Page<Agendamento> findByDentistaAndAtivoTrue(Dentista dentista, Pageable pageable);
    Page<Agendamento> findByPacienteAndAtivoTrue(Paciente paciente, Pageable pageable);
    Page<Agendamento> findByStatusAndAtivoTrue(StatusAgendamento status, Pageable pageable);
    Page<Agendamento> findByDataConsultaBetweenAndAtivoTrue(LocalDate startDate, LocalDate endDate, Pageable pageable);

    // Verificação de conflitos de horário
    @Query("SELECT a FROM Agendamento a WHERE " +
            "a.dentista = :dentista AND " +
            "a.dataConsulta = :dataConsulta AND " +
            "a.ativo = true AND " +
            "a.status <> 'CANCELADO' AND " +
            "((a.horaInicio < :horaFim AND a.horaFim > :horaInicio) OR " +
            "(a.horaInicio = :horaInicio AND a.horaFim = :horaFim))")
    List<Agendamento> findConflitosHorarios(
            @Param("dentista") Dentista dentista,
            @Param("dataConsulta") LocalDate dataConsulta,
            @Param("horaInicio") LocalTime horaInicio,
            @Param("horaFim") LocalTime horaFim);

    // Verificação de conflitos excluindo um agendamento específico (para updates)
    @Query("SELECT a FROM Agendamento a WHERE " +
            "a.id <> :excludeId AND " +
            "a.dentista = :dentista AND " +
            "a.dataConsulta = :dataConsulta AND " +
            "a.ativo = true AND " +
            "a.status <> 'CANCELADO' AND " +
            "((a.horaInicio < :horaFim AND a.horaFim > :horaInicio) OR " +
            "(a.horaInicio = :horaInicio AND a.horaFim = :horaFim))")
    List<Agendamento> findConflitosHorariosExcluding(
            @Param("dentista") Dentista dentista,
            @Param("dataConsulta") LocalDate dataConsulta,
            @Param("horaInicio") LocalTime horaInicio,
            @Param("horaFim") LocalTime horaFim,
            @Param("excludeId") Long excludeId);

    // Busca agendamentos futuros não confirmados para lembretes
    @Query("SELECT a FROM Agendamento a WHERE " +
            "a.ativo = true AND " +
            "a.status = 'AGENDADO' AND " +
            "a.confirmadoEm IS NULL AND " +
            "a.lembreteEnviado = false AND " +
            "a.dataConsulta >= :hoje AND " +
            "a.dataConsulta <= :dataLimite")
    List<Agendamento> findAgendamentosParaLembrete(
            @Param("hoje") LocalDate hoje,
            @Param("dataLimite") LocalDate dataLimite);

    // Busca agendamentos de hoje por status
    @Query("SELECT a FROM Agendamento a WHERE " +
            "a.ativo = true AND " +
            "a.dataConsulta = :hoje AND " +
            "a.status IN :statuses " +
            "ORDER BY a.horaInicio")
    List<Agendamento> findAgendamentosHojePorStatus(
            @Param("hoje") LocalDate hoje,
            @Param("statuses") List<StatusAgendamento> statuses);

    // Busca agendamentos com filtros múltiplos
    @Query("SELECT a FROM Agendamento a WHERE " +
            "(:dentistaId IS NULL OR a.dentista.id = :dentistaId) AND " +
            "(:pacienteId IS NULL OR a.paciente.id = :pacienteId) AND " +
            "(:dataInicio IS NULL OR a.dataConsulta >= :dataInicio) AND " +
            "(:dataFim IS NULL OR a.dataConsulta <= :dataFim) AND " +
            "(:status IS NULL OR a.status = :status) AND " +
            "(:ativo IS NULL OR a.ativo = :ativo) AND " +
            "(:lembreteEnviado IS NULL OR a.lembreteEnviado = :lembreteEnviado)")
    Page<Agendamento> findByFilters(
            @Param("dentistaId") Long dentistaId,
            @Param("pacienteId") Long pacienteId,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            @Param("status") StatusAgendamento status,
            @Param("ativo") Boolean ativo,
            @Param("lembreteEnviado") Boolean lembreteEnviado,
            Pageable pageable);

    // Busca estatísticas por período
    @Query("SELECT COUNT(a), " +
            "SUM(CASE WHEN a.status = 'CONFIRMADO' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN a.status = 'CONCLUIDO' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN a.status = 'CANCELADO' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN a.status = 'FALTOU' THEN 1 ELSE 0 END), " +
            "COALESCE(SUM(a.valorConsulta), 0) " +
            "FROM Agendamento a WHERE " +
            "a.dataConsulta BETWEEN :inicio AND :fim AND " +
            "a.ativo = true")
    Object[] findEstatisticasPeriodo(
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim);

    // Busca agendamentos próximos (próximos 7 dias)
    @Query("SELECT a FROM Agendamento a WHERE " +
            "a.ativo = true AND " +
            "a.dataConsulta BETWEEN :hoje AND :seteDias AND " +
            "a.status IN ('AGENDADO', 'CONFIRMADO') " +
            "ORDER BY a.dataConsulta, a.horaInicio")
    List<Agendamento> findProximosAgendamentos(
            @Param("hoje") LocalDate hoje,
            @Param("seteDias") LocalDate seteDias);

    // Busca histórico de agendamentos por paciente
    @Query("SELECT a FROM Agendamento a WHERE " +
            "a.paciente = :paciente AND " +
            "a.ativo = true AND " +
            "a.dataConsulta < :hoje " +
            "ORDER BY a.dataConsulta DESC, a.horaInicio DESC")
    List<Agendamento> findHistoricoPaciente(
            @Param("paciente") Paciente paciente,
            @Param("hoje") LocalDate hoje);

    // Busca agendamentos por dentista com filtro de data
    @Query("SELECT a FROM Agendamento a WHERE " +
            "a.dentista = :dentista AND " +
            "a.ativo = true AND " +
            "(:dataInicio IS NULL OR a.dataConsulta >= :dataInicio) AND " +
            "(:dataFim IS NULL OR a.dataConsulta <= :dataFim) " +
            "ORDER BY a.dataConsulta, a.horaInicio")
    List<Agendamento> findAgendamentosDentistaPorPeriodo(
            @Param("dentista") Dentista dentista,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim);

    // Verifica se paciente tem agendamento futuro
    @Query("SELECT COUNT(a) > 0 FROM Agendamento a WHERE " +
            "a.paciente = :paciente AND " +
            "a.ativo = true AND " +
            "a.dataConsulta >= :hoje AND " +
            "a.status <> 'CANCELADO'")
    boolean hasAgendamentoFuturo(
            @Param("paciente") Paciente paciente,
            @Param("hoje") LocalDate hoje);

    // Busca agendamentos não finalizados (em andamento)
    @Query("SELECT a FROM Agendamento a WHERE " +
            "a.ativo = true AND " +
            "a.dataConsulta = :hoje AND " +
            "a.status IN ('AGENDADO', 'CONFIRMADO', 'EM_ATENDIMENTO') " +
            "ORDER BY a.horaInicio")
    List<Agendamento> findAgendamentosEmAndamento(@Param("hoje") LocalDate hoje);

    // Busca para dashboard (agendamentos do dia)
    @Query("SELECT a.status, COUNT(a) FROM Agendamento a WHERE " +
            "a.ativo = true AND " +
            "a.dataConsulta = :hoje " +
            "GROUP BY a.status")
    List<Object[]> findDashboardHoje(@Param("hoje") LocalDate hoje);

    // Busca faturamento por período
    @Query("SELECT DATE(a.dataConsulta) as data, " +
            "COALESCE(SUM(CASE WHEN a.status = 'CONCLUIDO' THEN a.valorConsulta ELSE 0 END), 0) as faturamento " +
            "FROM Agendamento a WHERE " +
            "a.ativo = true AND " +
            "a.dataConsulta BETWEEN :inicio AND :fim " +
            "GROUP BY DATE(a.dataConsulta) " +
            "ORDER BY DATE(a.dataConsulta)")
    List<Object[]> findFaturamentoDiario(
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim);

    // Busca para calendário (período específico)
    @Query("SELECT a FROM Agendamento a WHERE " +
            "a.ativo = true AND " +
            "a.dataConsulta BETWEEN :inicio AND :fim " +
            "ORDER BY a.dataConsulta, a.horaInicio")
    List<Agendamento> findParaCalendario(
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim);
=======
    // ==================== BUSCAS BÁSICAS ====================

    @Query("SELECT a FROM Agendamento a WHERE a.id = :id AND a.ativo = true")
    Optional<Agendamento> findByIdAtivo(@Param("id") Long id);

    @Query("SELECT a FROM Agendamento a WHERE a.ativo = true")
    Page<Agendamento> findAllAtivos(Pageable pageable);

    // ==================== FILTROS PRINCIPAIS ====================

    @Query("SELECT a FROM Agendamento a WHERE a.dentista.id = :dentistaId AND a.ativo = true")
    Page<Agendamento> findByDentistaId(@Param("dentistaId") Long dentistaId, Pageable pageable);

    @Query("SELECT a FROM Agendamento a WHERE a.paciente.id = :pacienteId AND a.ativo = true")
    Page<Agendamento> findByPacienteId(@Param("pacienteId") Long pacienteId, Pageable pageable);

    @Query("SELECT a FROM Agendamento a WHERE a.status = :status AND a.ativo = true")
    Page<Agendamento> findByStatus(@Param("status") StatusAgendamento status, Pageable pageable);

    @Query("SELECT a FROM Agendamento a WHERE a.dataConsulta BETWEEN :inicio AND :fim AND a.ativo = true ORDER BY a.dataConsulta, a.horaInicio")
    Page<Agendamento> findByPeriodo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim, Pageable pageable);

    // ==================== AGENDA DO DIA ====================

    @Query("SELECT a FROM Agendamento a WHERE a.dentista.id = :dentistaId AND a.dataConsulta = :data AND a.ativo = true ORDER BY a.horaInicio")
    List<Agendamento> findAgendaDoDia(@Param("dentistaId") Long dentistaId, @Param("data") LocalDate data);

    @Query("SELECT a FROM Agendamento a WHERE a.dataConsulta = :data AND a.ativo = true ORDER BY a.horaInicio")
    List<Agendamento> findAllAgendaDoDia(@Param("data") LocalDate data);

    @Query("SELECT a FROM Agendamento a WHERE a.dataConsulta = :data AND a.status = 'CONFIRMADO' AND a.ativo = true ORDER BY a.horaInicio")
    List<Agendamento> findConfirmadosDoDia(@Param("data") LocalDate data);

    // ==================== CONFLITOS ====================

    @Query("""
        SELECT a FROM Agendamento a WHERE a.dentista.id = :dentistaId
        AND a.dataConsulta = :data AND a.ativo = true
        AND a.status NOT IN ('CANCELADO', 'FALTOU')
        AND ((a.horaInicio < :horaFim AND a.horaFim > :horaInicio))
        """)
    List<Agendamento> findConflitos(@Param("dentistaId") Long dentistaId, @Param("data") LocalDate data,
                                    @Param("horaInicio") LocalTime horaInicio, @Param("horaFim") LocalTime horaFim);

    @Query("""
        SELECT a FROM Agendamento a WHERE a.dentista.id = :dentistaId
        AND a.dataConsulta = :data AND a.id <> :idExcluir AND a.ativo = true
        AND a.status NOT IN ('CANCELADO', 'FALTOU')
        AND ((a.horaInicio < :horaFim AND a.horaFim > :horaInicio))
        """)
    List<Agendamento> findConflitosExcluindo(@Param("dentistaId") Long dentistaId, @Param("data") LocalDate data,
                                             @Param("horaInicio") LocalTime horaInicio, @Param("horaFim") LocalTime horaFim,
                                             @Param("idExcluir") Long idExcluir);

    @Query("""
        SELECT a FROM Agendamento a WHERE a.paciente.id = :pacienteId
        AND a.dataConsulta = :data AND a.ativo = true
        AND a.status NOT IN ('CANCELADO', 'FALTOU')
        AND ((a.horaInicio < :horaFim AND a.horaFim > :horaInicio))
        """)
    List<Agendamento> findConflitoPaciente(@Param("pacienteId") Long pacienteId, @Param("data") LocalDate data,
                                           @Param("horaInicio") LocalTime horaInicio, @Param("horaFim") LocalTime horaFim);

    // ==================== PRÓXIMOS AGENDAMENTOS ====================

    @Query("""
        SELECT a FROM Agendamento a WHERE a.paciente.id = :pacienteId
        AND a.dataConsulta >= :hoje AND a.ativo = true
        AND a.status IN ('AGENDADO', 'CONFIRMADO')
        ORDER BY a.dataConsulta, a.horaInicio
        """)
    List<Agendamento> findProximosPaciente(@Param("pacienteId") Long pacienteId, @Param("hoje") LocalDate hoje);

    @Query("""
        SELECT a FROM Agendamento a WHERE a.dentista.id = :dentistaId
        AND a.dataConsulta >= :hoje AND a.ativo = true
        AND a.status IN ('AGENDADO', 'CONFIRMADO')
        ORDER BY a.dataConsulta, a.horaInicio
        """)
    List<Agendamento> findProximosDentista(@Param("dentistaId") Long dentistaId, @Param("hoje") LocalDate hoje);

    // ==================== HORÁRIOS OCUPADOS ====================

    @Query("""
        SELECT a FROM Agendamento a WHERE a.dentista.id = :dentistaId
        AND a.dataConsulta = :data AND a.ativo = true
        AND a.status NOT IN ('CANCELADO', 'FALTOU')
        ORDER BY a.horaInicio
        """)
    List<Agendamento> findOcupadosDoDia(@Param("dentistaId") Long dentistaId, @Param("data") LocalDate data);

    // ==================== LEMBRETES ====================

    @Query("""
        SELECT a FROM Agendamento a WHERE a.dataConsulta = :data
        AND a.lembreteEnviado = false AND a.ativo = true
        AND a.status IN ('AGENDADO', 'CONFIRMADO')
        """)
    List<Agendamento> findParaLembrete(@Param("data") LocalDate data);

    @Modifying
    @Query("UPDATE Agendamento a SET a.lembreteEnviado = true, a.lembreteEnviadoEm = :agora WHERE a.id IN :ids")
    int marcarLembretesEnviados(@Param("ids") List<Long> ids, @Param("agora") LocalDateTime agora);

    // ==================== CONSULTAS ESPECIAIS ====================

    @Query("SELECT a FROM Agendamento a WHERE a.dataConsulta < :hoje AND a.status = 'AGENDADO' AND a.ativo = true")
    List<Agendamento> findPassadosNaoFinalizados(@Param("hoje") LocalDate hoje);

    @Query("SELECT a FROM Agendamento a WHERE a.status = 'EM_ATENDIMENTO' AND a.ativo = true ORDER BY a.dataConsulta, a.horaInicio")
    List<Agendamento> findEmAtendimento();

    // ==================== HISTÓRICO ====================

    @Query("SELECT a FROM Agendamento a WHERE a.paciente.id = :pacienteId AND a.status = 'CONCLUIDO' AND a.ativo = true ORDER BY a.dataConsulta DESC")
    Page<Agendamento> findHistoricoPaciente(@Param("pacienteId") Long pacienteId, Pageable pageable);

    // ==================== ESTATÍSTICAS ====================

    @Query("SELECT COUNT(a) FROM Agendamento a WHERE a.dentista.id = :dentistaId AND a.dataConsulta = :data AND a.ativo = true AND a.status NOT IN ('CANCELADO', 'FALTOU')")
    long countConsultasDoDia(@Param("dentistaId") Long dentistaId, @Param("data") LocalDate data);

    @Query("SELECT COUNT(a) FROM Agendamento a WHERE a.paciente.id = :pacienteId AND a.status = 'FALTOU' AND a.ativo = true")
    long countFaltasPaciente(@Param("pacienteId") Long pacienteId);

    @Query("SELECT COUNT(a) FROM Agendamento a WHERE a.dataConsulta = :data AND a.ativo = true AND a.status NOT IN ('CANCELADO', 'FALTOU')")
    long countTotalDoDia(@Param("data") LocalDate data);

    @Query("SELECT a.status, COUNT(a) FROM Agendamento a WHERE a.dataConsulta BETWEEN :inicio AND :fim AND a.ativo = true GROUP BY a.status")
    List<Object[]> countPorStatusNoPeriodo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

    @Query("SELECT a.tipoProcedimento, COUNT(a) FROM Agendamento a WHERE a.dataConsulta BETWEEN :inicio AND :fim AND a.ativo = true AND a.tipoProcedimento IS NOT NULL GROUP BY a.tipoProcedimento")
    List<Object[]> countPorProcedimentoNoPeriodo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

    @Query("SELECT a.dentista.id, a.dentista.nome, COUNT(a) FROM Agendamento a WHERE a.dataConsulta BETWEEN :inicio AND :fim AND a.ativo = true GROUP BY a.dentista.id, a.dentista.nome ORDER BY COUNT(a) DESC")
    List<Object[]> countPorDentistaNoPeriodo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

    // ==================== UPDATES EM LOTE ====================

    @Modifying
    @Query("UPDATE Agendamento a SET a.status = :status, a.atualizadoEm = :agora, a.atualizadoPor = :usuario WHERE a.id IN :ids")
    int updateStatusEmLote(@Param("ids") List<Long> ids, @Param("status") StatusAgendamento status,
                           @Param("agora") LocalDateTime agora, @Param("usuario") String usuario);

    @Modifying
    @Query("UPDATE Agendamento a SET a.ativo = false, a.atualizadoEm = :agora WHERE a.id = :id")
    int desativar(@Param("id") Long id, @Param("agora") LocalDateTime agora);

    // ==================== VERIFICAÇÕES ====================

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Agendamento a WHERE a.id = :id AND a.ativo = true")
    boolean existsAtivo(@Param("id") Long id);
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
}
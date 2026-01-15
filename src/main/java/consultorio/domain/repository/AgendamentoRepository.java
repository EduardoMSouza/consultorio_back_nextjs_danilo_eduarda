package consultorio.domain.repository;


import consultorio.domain.entity.agendamento.Agendamento;
import consultorio.domain.entity.agendamento.enums.StatusAgendamento;
import consultorio.domain.entity.pessoa.Dentista;
import consultorio.domain.entity.pessoa.Paciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
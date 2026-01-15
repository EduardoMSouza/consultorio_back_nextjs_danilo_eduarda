package consultorio.api.mapper.tratamento;

import consultorio.api.dto.request.tratamento.EvolucaoTratamentoRequest;
import consultorio.api.dto.response.tratamento.EvolucaoTratamentoResponse;
import consultorio.domain.entity.pessoa.Dentista;
import consultorio.domain.entity.pessoa.Paciente;
import consultorio.domain.entity.tratamento.EvolucaoTratamento;
import consultorio.domain.entity.tratamento.PlanoDental;
import org.springframework.stereotype.Component;

@Component
public class EvolucaoTratamentoMapper {

    // ========== REQUEST TO ENTITY (CREATE) ==========

    public EvolucaoTratamento toEntity(EvolucaoTratamentoRequest request) {
        if (request == null) {
            return null;
        }

        return EvolucaoTratamento.builder()
                .dataEvolucao(request.getDataEvolucao())
                .tipoEvolucao(request.getTipoEvolucao())
                .titulo(request.getTitulo())
                .descricao(request.getDescricao())
                .procedimentosRealizados(request.getProcedimentosRealizados())
                .materiaisUtilizados(request.getMateriaisUtilizados())
                .medicamentosPrescritos(request.getMedicamentosPrescritos())
                .observacoes(request.getObservacoes())
                .recomendacoes(request.getRecomendacoes())
                .doresQueixas(request.getDoresQueixas())
                .proximaConsulta(request.getProximaConsulta())
                .tempoConsultaMinutos(request.getTempoConsultaMinutos())
                .urgente(request.getUrgente() != null ? request.getUrgente() : false)
                .retornoNecessario(request.getRetornoNecessario() != null ? request.getRetornoNecessario() : false)
                .assinaturaDentista(request.getAssinaturaDentista())
                .ativo(true) // Por padrão, nova evolução é ativa
                .build();
    }

    // ========== REQUEST TO ENTITY WITH RELATIONSHIPS ==========

    public EvolucaoTratamento toEntity(EvolucaoTratamentoRequest request,
                                       Paciente paciente,
                                       Dentista dentista,
                                       PlanoDental planoDental) {
        if (request == null) {
            return null;
        }

        EvolucaoTratamento evolucao = toEntity(request);
        evolucao.setPaciente(paciente);
        evolucao.setDentista(dentista);
        evolucao.setPlanoDental(planoDental);

        return evolucao;
    }

    // ========== ENTITY TO RESPONSE ==========

    public EvolucaoTratamentoResponse toResponse(EvolucaoTratamento entity) {
        if (entity == null) {
            return null;
        }

        EvolucaoTratamentoResponse response = new EvolucaoTratamentoResponse();

        // IDs e informações básicas
        response.setId(entity.getId());
        response.setDataEvolucao(entity.getDataEvolucao());
        response.setTipoEvolucao(entity.getTipoEvolucao());
        response.setTipoEvolucaoDescricao(entity.getTipoEvolucaoDescricao());
        response.setTitulo(entity.getTitulo());
        response.setDescricao(entity.getDescricao());
        response.setProcedimentosRealizados(entity.getProcedimentosRealizados());
        response.setMateriaisUtilizados(entity.getMateriaisUtilizados());
        response.setMedicamentosPrescritos(entity.getMedicamentosPrescritos());
        response.setObservacoes(entity.getObservacoes());
        response.setRecomendacoes(entity.getRecomendacoes());
        response.setDoresQueixas(entity.getDoresQueixas());
        response.setProximaConsulta(entity.getProximaConsulta());
        response.setTempoConsultaMinutos(entity.getTempoConsultaMinutos());
        response.setUrgente(entity.getUrgente());
        response.setRetornoNecessario(entity.getRetornoNecessario());
        response.setAssinaturaDentista(entity.getAssinaturaDentista());
        response.setAtivo(entity.getAtivo());
        response.setCriadoEm(entity.getCriadoEm());
        response.setAtualizadoEm(entity.getAtualizadoEm());

        // Relacionamentos - Paciente
        if (entity.getPaciente() != null) {
            response.setPacienteId(entity.getPaciente().getId());
            response.setPacienteNome(entity.getPaciente().getNome());
        }

        // Relacionamentos - Dentista
        if (entity.getDentista() != null) {
            response.setDentistaId(entity.getDentista().getId());
            response.setDentistaNome(entity.getDentista().getNome());
        }

        // Relacionamentos - Plano Dental
        if (entity.getPlanoDental() != null) {
            response.setPlanoDentalId(entity.getPlanoDental().getId());
            // Supondo que PlanoDental tenha um campo 'nome' - ajuste conforme sua entidade
            // response.setPlanoDentalNome(entity.getPlanoDental().getNome());
        }

        return response;
    }

    // ========== UPDATE ENTITY FROM REQUEST (FULL UPDATE) ==========

    public void updateEntityFromRequest(EvolucaoTratamentoRequest request, EvolucaoTratamento entity) {
        if (request == null || entity == null) {
            return;
        }

        // Campos básicos
        if (request.getDataEvolucao() != null) {
            entity.setDataEvolucao(request.getDataEvolucao());
        }
        if (request.getTipoEvolucao() != null) {
            entity.setTipoEvolucao(request.getTipoEvolucao());
        }
        if (request.getTitulo() != null) {
            entity.setTitulo(request.getTitulo());
        }
        if (request.getDescricao() != null) {
            entity.setDescricao(request.getDescricao());
        }
        if (request.getProcedimentosRealizados() != null) {
            entity.setProcedimentosRealizados(request.getProcedimentosRealizados());
        }
        if (request.getMateriaisUtilizados() != null) {
            entity.setMateriaisUtilizados(request.getMateriaisUtilizados());
        }
        if (request.getMedicamentosPrescritos() != null) {
            entity.setMedicamentosPrescritos(request.getMedicamentosPrescritos());
        }
        if (request.getObservacoes() != null) {
            entity.setObservacoes(request.getObservacoes());
        }
        if (request.getRecomendacoes() != null) {
            entity.setRecomendacoes(request.getRecomendacoes());
        }
        if (request.getDoresQueixas() != null) {
            entity.setDoresQueixas(request.getDoresQueixas());
        }
        if (request.getProximaConsulta() != null) {
            entity.setProximaConsulta(request.getProximaConsulta());
        }
        if (request.getTempoConsultaMinutos() != null) {
            entity.setTempoConsultaMinutos(request.getTempoConsultaMinutos());
        }
        if (request.getUrgente() != null) {
            entity.setUrgente(request.getUrgente());
        }
        if (request.getRetornoNecessario() != null) {
            entity.setRetornoNecessario(request.getRetornoNecessario());
        }
        if (request.getAssinaturaDentista() != null) {
            entity.setAssinaturaDentista(request.getAssinaturaDentista());
        }

        // Campos calculados/automáticos
        entity.setAtualizadoEm(java.time.LocalDateTime.now());
    }

    // ========== UPDATE ENTITY FROM REQUEST WITH RELATIONSHIPS ==========

    public void updateEntityFromRequest(EvolucaoTratamentoRequest request,
                                        EvolucaoTratamento entity,
                                        Paciente paciente,
                                        Dentista dentista,
                                        PlanoDental planoDental) {

        updateEntityFromRequest(request, entity);

        // Atualizar relacionamentos se fornecidos
        if (paciente != null) {
            entity.setPaciente(paciente);
        }
        if (dentista != null) {
            entity.setDentista(dentista);
        }
        if (planoDental != null) {
            entity.setPlanoDental(planoDental);
        }
    }

    // ========== UPDATE PARTIAL (PATCH) ==========

    public void updateEntityPartial(EvolucaoTratamentoRequest request, EvolucaoTratamento entity) {
        if (request == null || entity == null) {
            return;
        }

        // Apenas atualiza campos que não são nulos no request
        if (request.getDataEvolucao() != null) {
            entity.setDataEvolucao(request.getDataEvolucao());
        }
        if (request.getTipoEvolucao() != null) {
            entity.setTipoEvolucao(request.getTipoEvolucao());
        }
        if (request.getTitulo() != null && !request.getTitulo().trim().isEmpty()) {
            entity.setTitulo(request.getTitulo());
        }
        if (request.getDescricao() != null && !request.getDescricao().trim().isEmpty()) {
            entity.setDescricao(request.getDescricao());
        }
        if (request.getProcedimentosRealizados() != null) {
            entity.setProcedimentosRealizados(request.getProcedimentosRealizados());
        }
        if (request.getMateriaisUtilizados() != null) {
            entity.setMateriaisUtilizados(request.getMateriaisUtilizados());
        }
        if (request.getMedicamentosPrescritos() != null) {
            entity.setMedicamentosPrescritos(request.getMedicamentosPrescritos());
        }
        if (request.getObservacoes() != null) {
            entity.setObservacoes(request.getObservacoes());
        }
        if (request.getRecomendacoes() != null) {
            entity.setRecomendacoes(request.getRecomendacoes());
        }
        if (request.getDoresQueixas() != null) {
            entity.setDoresQueixas(request.getDoresQueixas());
        }
        if (request.getProximaConsulta() != null) {
            entity.setProximaConsulta(request.getProximaConsulta());
        }
        if (request.getTempoConsultaMinutos() != null) {
            entity.setTempoConsultaMinutos(request.getTempoConsultaMinutos());
        }
        if (request.getUrgente() != null) {
            entity.setUrgente(request.getUrgente());
        }
        if (request.getRetornoNecessario() != null) {
            entity.setRetornoNecessario(request.getRetornoNecessario());
        }
        if (request.getAssinaturaDentista() != null) {
            entity.setAssinaturaDentista(request.getAssinaturaDentista());
        }

        entity.setAtualizadoEm(java.time.LocalDateTime.now());
    }

    // ========== LIST TO RESPONSE LIST ==========

    public java.util.List<EvolucaoTratamentoResponse> toResponseList(java.util.List<EvolucaoTratamento> entities) {
        if (entities == null) {
            return java.util.Collections.emptyList();
        }

        return entities.stream()
                .map(this::toResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    // ========== RESPONSE TO ENTITY (RARAMENTE USADO) ==========

    public EvolucaoTratamento responseToEntity(EvolucaoTratamentoResponse response) {
        if (response == null) {
            return null;
        }

        return EvolucaoTratamento.builder()
                .id(response.getId())
                .dataEvolucao(response.getDataEvolucao())
                .tipoEvolucao(response.getTipoEvolucao())
                .titulo(response.getTitulo())
                .descricao(response.getDescricao())
                .procedimentosRealizados(response.getProcedimentosRealizados())
                .materiaisUtilizados(response.getMateriaisUtilizados())
                .medicamentosPrescritos(response.getMedicamentosPrescritos())
                .observacoes(response.getObservacoes())
                .recomendacoes(response.getRecomendacoes())
                .doresQueixas(response.getDoresQueixas())
                .proximaConsulta(response.getProximaConsulta())
                .tempoConsultaMinutos(response.getTempoConsultaMinutos())
                .urgente(response.getUrgente())
                .retornoNecessario(response.getRetornoNecessario())
                .assinaturaDentista(response.getAssinaturaDentista())
                .ativo(response.getAtivo())
                .criadoEm(response.getCriadoEm())
                .atualizadoEm(response.getAtualizadoEm())
                .build();
    }

    // ========== MÉTODOS AUXILIARES PARA IDs ==========

    public Paciente mapPacienteIdToEntity(Long pacienteId) {
        if (pacienteId == null) {
            return null;
        }
        Paciente paciente = new Paciente();
        paciente.setId(pacienteId);
        return paciente;
    }

    public Dentista mapDentistaIdToEntity(Long dentistaId) {
        if (dentistaId == null) {
            return null;
        }
        Dentista dentista = new Dentista();
        dentista.setId(dentistaId);
        return dentista;
    }

    public PlanoDental mapPlanoDentalIdToEntity(Long planoDentalId) {
        if (planoDentalId == null) {
            return null;
        }
        PlanoDental plano = new PlanoDental();
        plano.setId(planoDentalId);
        return plano;
    }

    // ========== MÉTODO PARA DTO SIMPLIFICADO (PARA LISTAGENS) ==========

    public EvolucaoTratamentoResponse toSummaryResponse(EvolucaoTratamento entity) {
        if (entity == null) {
            return null;
        }

        EvolucaoTratamentoResponse summary = new EvolucaoTratamentoResponse();

        summary.setId(entity.getId());
        summary.setDataEvolucao(entity.getDataEvolucao());
        summary.setTipoEvolucao(entity.getTipoEvolucao());
        summary.setTipoEvolucaoDescricao(entity.getTipoEvolucaoDescricao());
        summary.setTitulo(entity.getTitulo());
        summary.setDescricao(entity.getDescricao().length() > 150 ?
                entity.getDescricao().substring(0, 150) + "..." : entity.getDescricao());
        summary.setProximaConsulta(entity.getProximaConsulta());
        summary.setUrgente(entity.getUrgente());
        summary.setRetornoNecessario(entity.getRetornoNecessario());
        summary.setAtivo(entity.getAtivo());
        summary.setCriadoEm(entity.getCriadoEm());

        if (entity.getPaciente() != null) {
            summary.setPacienteId(entity.getPaciente().getId());
            summary.setPacienteNome(entity.getPaciente().getNome());
        }
        if (entity.getDentista() != null) {
            summary.setDentistaId(entity.getDentista().getId());
            summary.setDentistaNome(entity.getDentista().getNome());
        }

        return summary;
    }
}
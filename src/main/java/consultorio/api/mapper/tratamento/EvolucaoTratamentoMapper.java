package consultorio.api.mapper.tratamento;

import consultorio.api.dto.request.tratamento.AtualizarEvolucaoTratamentoRequest;
import consultorio.api.dto.request.tratamento.CriarEvolucaoTratamentoRequest;
import consultorio.api.dto.response.tratamento.EvolucaoTratamentoDetalheResponse;
import consultorio.api.dto.response.tratamento.EvolucaoTratamentoResponse;
import consultorio.api.dto.response.tratamento.ResumoEvolucaoResponse;
import consultorio.domain.entity.pessoa.Dentista;
import consultorio.domain.entity.pessoa.Paciente;
import consultorio.domain.entity.tratamento.EvolucaoTratamento;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class EvolucaoTratamentoMapper {

    public EvolucaoTratamento toEntity(CriarEvolucaoTratamentoRequest request, Paciente paciente, Dentista dentista) {
        if (request == null) return null;

        return EvolucaoTratamento.builder()
                .paciente(paciente)
                .dentista(dentista)
                .data(request.getData())
                .evolucaoEIntercorrencias(request.getEvolucaoEIntercorrencias())
                .build();
    }

    public void updateEntityFromDTO(AtualizarEvolucaoTratamentoRequest request, EvolucaoTratamento entity) {
        if (request == null || entity == null) return;

        if (request.getData() != null) {
            entity.setData(request.getData());
        }
        if (request.getEvolucaoEIntercorrencias() != null) {
            entity.setEvolucaoEIntercorrencias(request.getEvolucaoEIntercorrencias());
        }
    }

    public EvolucaoTratamentoResponse toResponse(EvolucaoTratamento entity) {
        if (entity == null) return null;

        return EvolucaoTratamentoResponse.builder()
                .id(entity.getId())
                .pacienteId(entity.getPaciente() != null ? entity.getPaciente().getId() : null)
                .pacienteNome(entity.getPaciente() != null ? entity.getPaciente().getNome() : null)
                .dentistaId(entity.getDentista() != null ? entity.getDentista().getId() : null)
                .dentistaNome(entity.getDentista() != null ? entity.getDentista().getNome() : null)
                .data(entity.getData())
                .evolucaoEIntercorrencias(entity.getEvolucaoEIntercorrencias())
                .criadoEm(LocalDateTime.now()) // Adicionar timestamps reais se existirem
                .atualizadoEm(LocalDateTime.now())
                .build();
    }

    public EvolucaoTratamentoDetalheResponse toDetalheResponse(EvolucaoTratamento entity) {
        if (entity == null) return null;

        String evolucao = entity.getEvolucaoEIntercorrencias();
        boolean podeEditar = entity.getData().isAfter(LocalDate.now().minusDays(7)); // Exemplo de regra
        boolean podeExcluir = entity.getData().isAfter(LocalDate.now().minusDays(7));

        return EvolucaoTratamentoDetalheResponse.builder()
                .id(entity.getId())
                .pacienteId(entity.getPaciente() != null ? entity.getPaciente().getId() : null)
                .pacienteNome(entity.getPaciente() != null ? entity.getPaciente().getNome() : null)
                .pacienteTelefone(entity.getPaciente() != null ? entity.getPaciente().getTelefone() : null)
                .dentistaId(entity.getDentista() != null ? entity.getDentista().getId() : null)
                .dentistaNome(entity.getDentista() != null ? entity.getDentista().getNome() : null)
                .dentistaCro(entity.getDentista() != null ? entity.getDentista().getCro() : null)
                .data(entity.getData())
                .dataFormatada(entity.getData())
                .evolucaoEIntercorrencias(evolucao)
                .quantidadeCaracteres(evolucao != null ? evolucao.length() : 0)
                .criadoEm(LocalDateTime.now())
                .atualizadoEm(LocalDateTime.now())
                .podeEditar(podeEditar)
                .podeExcluir(podeExcluir)
                .build();
    }

    public ResumoEvolucaoResponse toResumoResponse(EvolucaoTratamento entity) {
        if (entity == null) return null;

        String evolucaoCompleta = entity.getEvolucaoEIntercorrencias();
        String evolucaoResumida = evolucaoCompleta != null && evolucaoCompleta.length() > 100
                ? evolucaoCompleta.substring(0, 100) + "..."
                : evolucaoCompleta;

        return ResumoEvolucaoResponse.builder()
                .id(entity.getId())
                .pacienteNome(entity.getPaciente() != null ? entity.getPaciente().getNome() : null)
                .dentistaNome(entity.getDentista() != null ? entity.getDentista().getNome() : null)
                .data(entity.getData())
                .evolucaoResumida(evolucaoResumida)
                .tamanhoTexto(evolucaoCompleta != null ? evolucaoCompleta.length() : 0)
                .build();
    }
}
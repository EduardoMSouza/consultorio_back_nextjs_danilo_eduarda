package consultorio.api.mapper.pessoa;

import consultorio.api.dto.request.pessoa.DentistaRequest;
import consultorio.api.dto.response.pessoa.DentistaResumoResponse;
import consultorio.api.dto.response.pessoa.DentistaResponse;
import consultorio.domain.entity.pessoa.Dentista;
import org.springframework.stereotype.Component;

@Component
public class DentistaMapper {

    /**
     * Converte DentistaRequest para entidade Dentista
     */
    public Dentista toEntity(DentistaRequest request) {
        if (request == null) {
            return null;
        }

        Dentista dentista = new Dentista();
        updateEntity(request, dentista);
        dentista.setAtivo(true); // Por padrão, novo dentista é ativo

        return dentista;
    }

    /**
     * Converte entidade Dentista para DentistaResponse (completo)
     */
    public DentistaResponse toResponse(Dentista dentista) {
        if (dentista == null) {
            return null;
        }

        DentistaResponse response = new DentistaResponse();
        response.setId(dentista.getId());
        response.setNome(dentista.getNome());
        response.setCro(dentista.getCro());
        response.setEspecialidade(dentista.getEspecialidade());
        response.setTelefone(dentista.getTelefone());
        response.setEmail(dentista.getEmail());
        response.setAtivo(dentista.getAtivo());
        response.setCriadoEm(dentista.getCriadoEm());
        response.setAtualizadoEm(dentista.getAtualizadoEm());

        return response;
    }

    /**
     * Converte entidade Dentista para DentistaResumoResponse (resumido)
     */
    public DentistaResumoResponse toResumoResponse(Dentista dentista) {
        if (dentista == null) {
            return null;
        }

        DentistaResumoResponse resumo = new DentistaResumoResponse();
        resumo.setId(dentista.getId());
        resumo.setNome(dentista.getNome());
        resumo.setCro(dentista.getCro());
        resumo.setEspecialidade(dentista.getEspecialidade());
        resumo.setTelefone(dentista.getTelefone());
        resumo.setEmail(dentista.getEmail());
        resumo.setAtivo(dentista.getAtivo());

        return resumo;
    }

    /**
     * Atualiza a entidade Dentista com os dados do DentistaRequest
     */
    public void updateEntity(DentistaRequest request, Dentista dentista) {
        if (request == null || dentista == null) {
            return;
        }

        // Atualiza apenas os campos que não estão em branco
        if (request.getNome() != null && !request.getNome().isBlank()) {
            dentista.setNome(request.getNome());
        }

        if (request.getCro() != null && !request.getCro().isBlank()) {
            dentista.setCro(request.getCro());
        }

        if (request.getEspecialidade() != null && !request.getEspecialidade().isBlank()) {
            dentista.setEspecialidade(request.getEspecialidade());
        }

        if (request.getTelefone() != null && !request.getTelefone().isBlank()) {
            dentista.setTelefone(request.getTelefone());
        }

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            dentista.setEmail(request.getEmail());
        }
    }

    /**
     * Converte DentistaResumoResponse para DentistaResponse
     */
    public DentistaResponse toResponseFromResumo(DentistaResumoResponse resumo, Dentista dentista) {
        if (resumo == null || dentista == null) {
            return null;
        }

        DentistaResponse response = new DentistaResponse();
        response.setId(resumo.getId());
        response.setNome(resumo.getNome());
        response.setCro(resumo.getCro());
        response.setEspecialidade(resumo.getEspecialidade());
        response.setTelefone(resumo.getTelefone());
        response.setEmail(resumo.getEmail());
        response.setAtivo(resumo.getAtivo());
        response.setCriadoEm(dentista.getCriadoEm());
        response.setAtualizadoEm(dentista.getAtualizadoEm());

        return response;
    }

    /**
     * Método para copiar dados entre duas entidades Dentista
     */
    public void copyEntity(Dentista source, Dentista target) {
        if (source == null || target == null) {
            return;
        }

        target.setNome(source.getNome());
        target.setCro(source.getCro());
        target.setEspecialidade(source.getEspecialidade());
        target.setTelefone(source.getTelefone());
        target.setEmail(source.getEmail());
        target.setAtivo(source.getAtivo());
        // Não copiamos id, timestamps e coleções
    }
}
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
        dentista.setNome(request.getNome());
        dentista.setCro(request.getCro());
        dentista.setEspecialidade(request.getEspecialidade());
        dentista.setTelefone(request.getTelefone());
        dentista.setEmail(request.getEmail());
        // O campo 'ativo' é definido como true por padrão na entidade Dentista

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

        // Atualiza apenas os campos que podem ser modificados
        if (request.getNome() != null) {
            dentista.setNome(request.getNome());
        }

        if (request.getCro() != null) {
            dentista.setCro(request.getCro());
        }

        if (request.getEspecialidade() != null) {
            dentista.setEspecialidade(request.getEspecialidade());
        }

        if (request.getTelefone() != null) {
            dentista.setTelefone(request.getTelefone());
        }

        if (request.getEmail() != null) {
            dentista.setEmail(request.getEmail());
        }
    }

    /**
     * Converte DentistaResumoResponse para DentistaResponse
     * Útil quando se tem um resumo e precisa completar com dados adicionais
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
     * Útil para operações de clone ou backup
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
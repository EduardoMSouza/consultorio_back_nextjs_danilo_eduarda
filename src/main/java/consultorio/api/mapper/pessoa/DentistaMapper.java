package consultorio.api.mapper.pessoa;

import consultorio.api.dto.request.pessoa.DentistaRequest;
import consultorio.api.dto.response.pessoa.DentistaResponse;
import consultorio.domain.entity.pessoa.Dentista;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class DentistaMapper {

    private final ModelMapper modelMapper;

    public DentistaMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;

        // Configurações personalizadas se necessário
        this.modelMapper.createTypeMap(DentistaRequest.class, Dentista.class);
        this.modelMapper.createTypeMap(Dentista.class, DentistaResponse.class);
    }

    public Dentista toEntity(DentistaRequest request) {
        return modelMapper.map(request, Dentista.class);
    }

    public DentistaResponse toResponse(Dentista dentista) {
        return modelMapper.map(dentista, DentistaResponse.class);
    }

    public void updateEntity(DentistaRequest request, Dentista dentista) {
        modelMapper.map(request, dentista);
    }
}
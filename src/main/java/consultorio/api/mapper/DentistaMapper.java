package consultorio.api.mapper;


import consultorio.api.dto.request.DentistaRequest;
import consultorio.api.dto.response.DentistaResponse;
import consultorio.domain.entity.Dentista;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DentistaMapper {

    private final ModelMapper modelMapper;

    public Dentista toEntity(DentistaRequest request) {
        return modelMapper.map(request, Dentista.class);
    }

    public DentistaResponse toResponse(Dentista entity) {
        return modelMapper.map(entity, DentistaResponse.class);
    }

    public List<DentistaResponse> toResponseList(List<Dentista> entities) {
        return entities.stream()
                .map(this::toResponse)
                .toList();
    }

    public void updateEntityFromRequest(DentistaRequest request, Dentista entity) {
        modelMapper.map(request, entity);
    }
}
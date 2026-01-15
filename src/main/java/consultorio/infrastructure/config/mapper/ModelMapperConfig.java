package consultorio.infrastructure.config.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        // Configuração geral
        mapper.getConfiguration()
                .setSkipNullEnabled(true) // Ignora campos nulos durante o mapping
                .setMatchingStrategy(MatchingStrategies.STRICT) // Mapeamento rigoroso
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        // Mapeamentos customizados específicos
//        // Evita sobrescrever ID ao mapear Request → Entity
//        mapper.typeMap(DentistaRequest.class, Dentista.class)
//                .addMappings(m -> m.skip(Dentista::setId));

        return mapper;
    }
}
package primeholding.configuration;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import primeholding.models.ToDoMapper;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public ToDoMapper getModelMapper(){
        return Mappers.getMapper(ToDoMapper.class);
    }

}

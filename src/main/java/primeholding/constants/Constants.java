package primeholding.constants;

import org.mapstruct.factory.Mappers;
import primeholding.mapper.ToDoMapper;

public final class Constants {

   private Constants() {
   }

   public static final ToDoMapper INSTANCE = Mappers.getMapper(ToDoMapper.class);
}

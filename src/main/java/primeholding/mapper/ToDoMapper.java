package primeholding.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import primeholding.entities.Item;

@Mapper
public interface ToDoMapper {
    ToDoMapper INSTANCE = Mappers.getMapper(ToDoMapper.class);

    Item itemDtoToItem (ItemDto item);
}

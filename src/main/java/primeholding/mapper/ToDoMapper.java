package primeholding.mapper;

import org.mapstruct.Mapper;
import primeholding.entities.Item;

@Mapper
public interface ToDoMapper {

    Item itemDtoToItem (ItemDto item);
}

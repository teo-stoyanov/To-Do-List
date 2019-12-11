package primeholding.mapper;

import org.mapstruct.Mapper;
import primeholding.entities.Item;

@Mapper
public interface ToDoMapper {

    Item postModelToItem(ItemPostModel itemPostModel);
    Item putModelToItem(ItemPutModel itemPutModel);
    Item patchModelToItem(ItemPatchModel itemPatchModel);

    ItemGetModel itemToItemGetModel(Item item);
    ItemPostModel itemToItemPostModel(Item item);
    ItemPutModel itemToItemPutModel(Item item);
    ItemPatchModel itemToItemPatchModel(Item item);
}

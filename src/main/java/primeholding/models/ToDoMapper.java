package primeholding.models;

import org.mapstruct.Mapper;
import primeholding.entities.Item;
import primeholding.models.items.ItemGetModel;
import primeholding.models.items.ItemPatchModel;
import primeholding.models.items.ItemPostModel;
import primeholding.models.items.ItemPutModel;

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

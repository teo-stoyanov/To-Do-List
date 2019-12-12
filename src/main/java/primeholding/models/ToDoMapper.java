package primeholding.models;

import org.mapstruct.Mapper;
import primeholding.entities.Item;
import primeholding.entities.ToDoList;
import primeholding.models.items.ItemGetModel;
import primeholding.models.items.ItemPostModel;
import primeholding.models.items.ItemPutModel;
import primeholding.models.todolists.ListChildModel;
import primeholding.models.todolists.ListGetModel;
import primeholding.models.todolists.ListPostModel;
import primeholding.models.todolists.ListPutModel;

import java.util.List;

@Mapper
public interface ToDoMapper {

    Item postModelToItem(ItemPostModel itemPostModel);
    Item putModelToItem(ItemPutModel itemPutModel);
    ItemGetModel itemToItemGetModel(Item item);

    ToDoList postListToList(ListPostModel listPostModel);
    ToDoList putListToList(ListPutModel listPostModel);
    ListGetModel listToListGetModel(ToDoList toDoList);
    List<ListChildModel> listToListGetResourcesModel(List<Item> toDoList);
}

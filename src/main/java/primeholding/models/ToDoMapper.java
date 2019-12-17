package primeholding.models;

import org.mapstruct.Mapper;
import primeholding.entities.Category;
import primeholding.entities.Item;
import primeholding.entities.ToDoList;
import primeholding.models.category.CategoryGetListModels;
import primeholding.models.category.CategoryGetModel;
import primeholding.models.category.CategoryPostModel;
import primeholding.models.category.CategoryPutModel;
import primeholding.models.items.ItemGetModel;
import primeholding.models.items.ItemPostModel;
import primeholding.models.items.ItemPutModel;
import primeholding.models.todolists.ListGetCategoriesModel;
import primeholding.models.todolists.ListGetItemsModel;
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
    List<ListGetItemsModel> listToListGetItemsModel(List<Item> items);
    List<ListGetCategoriesModel> listToListGetCategoriesModel(List<Category> categories);

    Category getModelToCategory(CategoryGetModel categoryGetModel);
    CategoryGetModel categoryToGetModel(Category category);
    Category postModelToCategory(CategoryPostModel categoryPostModel);
    Category putModelToCategory(CategoryPutModel categoryPostModel);
    List<CategoryGetListModels> categoryToListGetModels(List<ToDoList> toDoLists);
 }

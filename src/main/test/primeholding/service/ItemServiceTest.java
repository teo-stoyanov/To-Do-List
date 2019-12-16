package primeholding.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import primeholding.entities.Item;
import primeholding.entities.ToDoList;

import java.util.NoSuchElementException;
import java.util.Optional;

@SpringBootTest
class ItemServiceTest {

    private ItemService itemService;
    private ListService listService;
    private Item item;

    @Autowired
    ItemServiceTest(ItemService itemService, ListService listService) {
        this.itemService = itemService;
        this.listService = listService;
        this.item = new Item();
    }

    @BeforeEach
    public void setItem(){
        ToDoList toDoList = new ToDoList();
        toDoList.setName("Test List");
        this.listService.register(toDoList);

        this.item.setToDoList(toDoList);
        this.item.setTitle("Test Item");
        this.item.setDescription("Lorem ipsum...");
        this.item.setIsCompleted(false);
    }

    @Test
    public void serviceShouldPersistItemAndReturnCorrect(){
        Item registered = this.itemService.register(this.item);
        Optional<Item> optionalItem = this.itemService.findByProp(registered.getTitle());
        if(!optionalItem.isPresent()){
            throw new NoSuchElementException();
        }

        Assertions.assertEquals(this.item.getTitle(),optionalItem.get().getTitle());
    }

    @Test
    public void serviceGetByIdShouldReturnCorrect(){
        Item registered = this.itemService.register(this.item);
        Optional<Item> optionalToDoList = this.itemService.getById(registered.getId());
        if(!optionalToDoList.isPresent()){
            throw new NoSuchElementException();
        }

        Assertions.assertEquals(this.item.getTitle(),optionalToDoList.get().getTitle());
    }

    @Test
    public void serviceDeleteByIdShouldRemoveEntity(){
        this.itemService.deleteById(1);
        Assertions.assertFalse(this.itemService.getById(1).isPresent());
    }
}
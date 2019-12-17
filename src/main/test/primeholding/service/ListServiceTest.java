package primeholding.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import primeholding.entities.ToDoList;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.Optional;

@SpringBootTest
class ListServiceTest {
    private ListService listService;
    private ToDoList toDoList;

    @Autowired
    ListServiceTest(ListService listService) {
        this.listService = listService;
        this.toDoList = new ToDoList();
    }

    @BeforeEach
    void setToDoList() {
        this.toDoList.setName("Work List");
    }

    @Test
    @Transactional
    public void servicePersistShouldReturnCorrectList() {
        ToDoList registered = this.listService.register(this.toDoList);
        Optional<ToDoList> returned = this.listService.findByProp(registered.getName());
        if(!returned.isPresent()){
            throw new NoSuchElementException();
        }
        Assertions.assertEquals(this.toDoList.getName(), returned.get().getName());
    }

    @Test
    public void serviceGetByIdShouldReturnCorrectList(){
        ToDoList registered = this.listService.register(this.toDoList);
        Optional<ToDoList> optionalToDoList = this.listService.getById(registered.getId());
        if(!optionalToDoList.isPresent()){
            throw new NoSuchElementException();
        }

        Assertions.assertEquals(this.toDoList.getName(),optionalToDoList.get().getName());
    }

    @Test
    public void serviceDeleteByIdShouldRemoveEntity(){
        ToDoList register = this.listService.register(this.toDoList);

        this.listService.deleteById(register.getId());

        Assertions.assertFalse(this.listService.getById(register.getId()).isPresent());
    }
}
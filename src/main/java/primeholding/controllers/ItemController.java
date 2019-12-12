package primeholding.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import primeholding.entities.Item;
import primeholding.entities.ToDoList;
import primeholding.models.items.ItemGetModel;
import primeholding.models.items.ItemPostModel;
import primeholding.models.items.ItemPutModel;
import primeholding.models.ToDoMapper;
import primeholding.service.ItemService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController(value = "itemController")
@RequestMapping("/item")
public class ItemController {
    private static final String LIST_ID = "listId";
    private static final String TITLE = "title";

    private final ItemService service;
    private ToDoMapper mapper;

    @Autowired
    public ItemController(ItemService itemService, ToDoMapper mapper) {
        this.service = itemService;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<ItemGetModel>> get() {
        List<Item> items = this.service.getAll();
        List<ItemGetModel> itemGetModels = new ArrayList<>();
        items.forEach(item -> {
            ItemGetModel itemGetModel = this.mapper.itemToItemGetModel(item);
            itemGetModel.setListId(item.getToDoList().getId());
            itemGetModels.add(itemGetModel);
        });

        return new ResponseEntity<>(itemGetModels, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemGetModel> get(@PathVariable Integer id) {
        Optional<Item> entity = this.service.getById(id);
        if (!entity.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ItemGetModel itemGetModel = this.mapper.itemToItemGetModel(entity.get());
        itemGetModel.setListId(entity.get().getToDoList().getId());
        return new ResponseEntity<>(itemGetModel, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ItemGetModel> post(@RequestBody ItemPostModel postModel) {
        if (this.service.getUniqueValues().stream().anyMatch(s -> s.equals(postModel.getTitle()))) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else if (postModel.getTitle() == null || postModel.getListId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<ToDoList> list = this.service.getListById(postModel.getListId());
        if (!list.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Item item = this.mapper.postModelToItem(postModel);
        item.setToDoList(list.get());
        Item result = this.service.register(item);
        ItemGetModel itemGetModel = this.mapper.itemToItemGetModel(result);
        itemGetModel.setListId(result.getToDoList().getId());
        return new ResponseEntity<>(itemGetModel, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemGetModel> put(@PathVariable Integer id, @RequestBody ItemPutModel putModel) {
        Optional<Item> entity = this.service.getById(id);
        if (!entity.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<ToDoList> list = this.service.getListById(putModel.getListId());
        if (!list.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<Item> optional = this.service.findByProp(putModel.getTitle());
        if (optional.isPresent() && !optional.get().getId().equals(id)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else if (putModel.getTitle() == null || putModel.getListId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Item toDoItem = this.mapper.putModelToItem(putModel);
        toDoItem.setCreatedDate(entity.get().getCreatedDate());
        toDoItem.setToDoList(list.get());
        toDoItem.setId(id);

        Item result = this.service.register(toDoItem);
        ItemGetModel itemGetModel = this.mapper.itemToItemGetModel(result);
        itemGetModel.setListId(result.getToDoList().getId());
        return new ResponseEntity<>(itemGetModel, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ItemGetModel> patch(@PathVariable Integer id, @RequestBody Map<String, Object> fields) {
        Optional<Item> entity = this.service.getById(id);
        if (!entity.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (fields.get(TITLE) == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<Item> optional = this.service.findByProp(fields.get(TITLE).toString());
        if (optional.isPresent() && !optional.get().getId().equals(id)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Item updateEntity = this.service.update(entity.get(), fields);
        if (fields.containsKey(LIST_ID) && fields.get(LIST_ID) != null) {
            Optional<ToDoList> list = this.service.getListById(Integer.parseInt(fields.get(LIST_ID).toString()));
            if (!list.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                updateEntity.setToDoList(list.get());
            }
        }
        updateEntity.setId(id);

        Item result = this.service.register(updateEntity);
        ItemGetModel itemGetModel = this.mapper.itemToItemGetModel(result);
        if (result.getToDoList() != null) {
            itemGetModel.setListId(result.getToDoList().getId());
        }
        return new ResponseEntity<>(itemGetModel, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Integer id) {
        if (!this.service.getById(id).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        this.service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

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
import primeholding.constants.Constants;
import primeholding.entities.Item;
import primeholding.mapper.ItemDto;
import primeholding.service.BaseService;
import primeholding.service.ItemService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/item")
public class ItemController {

    private final BaseService service;

    @Autowired
    public ItemController(ItemService itemService) {
        this.service = itemService;
    }

    @GetMapping
    public ResponseEntity<List<Item>> get() {
        return new ResponseEntity<>(this.service.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> get(@PathVariable Integer id) {
        Optional<Item> entity = this.service.getById(id);
        return entity.map(toDoEntity -> new ResponseEntity<>(toDoEntity, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Item> post(@RequestBody ItemDto dto) {
        if (this.service.getUniqueValues().stream().anyMatch(s -> s.equals(dto.getTitle()))) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else if (dto.getTitle() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        Item item = Constants.INSTANCE.itemDtoToItem(dto);
        this.service.register(item);

        return new ResponseEntity<>(item, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> put(@PathVariable Integer id, @RequestBody ItemDto dto) {
        Optional<Item> entity = this.service.getById(id);
        if (!entity.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<Item> optional = this.service.findByProp(dto.getTitle());
        if (optional.isPresent() && !optional.get().getId().equals(id)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else if (dto.getTitle() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        Item toDoItem = Constants.INSTANCE.itemDtoToItem(dto);
        toDoItem.setCreatedDate(entity.get().getCreatedDate());
        toDoItem.setId(id);

        Item result = (Item) this.service.register(toDoItem);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Item> patch(@PathVariable Integer id, @RequestBody Map<String, Object> fields) {
        Optional<Item> entity = this.service.getById(id);
        if (!entity.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (fields.get("title") == null) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        Optional<Item> optional = this.service.findByProp(fields.get("title").toString());
        if (optional.isPresent() && !optional.get().getId().equals(id)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Item updateEntity = (Item) this.service.update(entity.get(), fields);
        updateEntity.setId(id);

        Item result = (Item) this.service.register(updateEntity);
        return new ResponseEntity<>(result, HttpStatus.OK);
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

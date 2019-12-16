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
import primeholding.entities.Category;
import primeholding.entities.ToDoList;
import primeholding.models.ToDoMapper;
import primeholding.models.category.CategoryGetListModels;
import primeholding.models.category.CategoryGetModel;
import primeholding.models.category.CategoryPostModel;
import primeholding.models.category.CategoryPutModel;
import primeholding.service.CategoryService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/category")
public class CategoryController {
    private static final String LIST_ID = "listIds";
    private static final String NAME = "name";

    private CategoryService service;
    private ToDoMapper mapper;

    @Autowired
    public CategoryController(CategoryService service, ToDoMapper toDoMapper) {
        this.service = service;
        this.mapper = toDoMapper;
    }

    @GetMapping
    public ResponseEntity<List<CategoryGetModel>> get() {
        List<Category> lists = this.service.getAll();
        List<CategoryGetModel> getModels = new ArrayList<>();

        for (Category list : lists) {
            CategoryGetModel categoryGetModel = this.mapper.categoryToGetModel(list);
            getModels.add(categoryGetModel);
        }

        return new ResponseEntity<>(getModels, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryGetModel> get(@PathVariable Integer id) {
        Optional<Category> optional = this.service.getById(id);
        if (!optional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        CategoryGetModel categoryGetModel = this.mapper.categoryToGetModel(optional.get());
        return new ResponseEntity<>(categoryGetModel, HttpStatus.OK);
    }

    @GetMapping("/{id}/lists")
    public ResponseEntity<List<CategoryGetListModels>> getLists(@PathVariable Integer id) {
        Optional<Category> optional = this.service.getById(id);
        if (!optional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<CategoryGetListModels> resultSet = this.mapper.categoryToListGetModels(optional.get().getLists());
        return new ResponseEntity<>(resultSet, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CategoryGetModel> post(@RequestBody CategoryPostModel categoryPostModel) {
        if (this.service.getUniqueValues().stream().anyMatch(s -> s.equals(categoryPostModel.getName()))) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else if (categoryPostModel.getName() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Category category = this.mapper.postModelToCategory(categoryPostModel);
        Category registered = this.service.register(category);
        CategoryGetModel categoryGetModel = this.mapper.categoryToGetModel(registered);
        return new ResponseEntity<>(categoryGetModel, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryGetModel> put(@PathVariable Integer id, @RequestBody CategoryPutModel categoryPutModel) {
        Optional<Category> entity = this.service.getById(id);
        if (!entity.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<Category> optional = this.service.findByProp(categoryPutModel.getName());
        if (optional.isPresent() && !optional.get().getId().equals(id)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else if (categoryPutModel.getName() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (categoryPutModel.getListIds() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        for (Integer listId : categoryPutModel.getListIds()) {
            Optional<ToDoList> toDoList = this.service.getOptionalToDoListById(listId);
            if (!toDoList.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }

        Category category = this.mapper.putModelToCategory(categoryPutModel);
        category.setId(id);
        category.setCreatedDate(entity.get().getCreatedDate());
        for (Integer listId : categoryPutModel.getListIds()) {
            category.getLists().add(this.service.getToDoListById(listId));
        }
        Category register = this.service.register(category);
        CategoryGetModel getModel = this.mapper.categoryToGetModel(register);

        return new ResponseEntity<>(getModel, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CategoryGetModel> patch(@PathVariable Integer id,  @RequestBody Map<String, Object> fields){
        Optional<Category> entity = this.service.getById(id);
        if (!entity.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (fields.get(NAME) == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (fields.containsKey(LIST_ID) && fields.get(LIST_ID) == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<Category> optional = this.service.findByProp(fields.get(NAME).toString());
        if (optional.isPresent() && !optional.get().getId().equals(id)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else if (fields.containsKey(LIST_ID)) {
            for (Integer categoryId : (List<Integer>) fields.get(LIST_ID)) {
                Optional<ToDoList> categoryById = this.service.getOptionalToDoListById(categoryId);
                if (!categoryById.isPresent()) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            }
        }

        Category category = this.service.update(entity.get(), fields);
        category.setId(id);
        if(fields.containsKey(LIST_ID)){
            category.setLists(new ArrayList<>());
            for (Integer listId : (List<Integer>) fields.get(LIST_ID)) {
                category.getLists().add(this.service.getToDoListById(listId));
            }
        }
        Category result = this.service.register(category);
        CategoryGetModel categoryGetModel = this.mapper.categoryToGetModel(result);

        return new ResponseEntity<>(categoryGetModel, HttpStatus.OK);
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

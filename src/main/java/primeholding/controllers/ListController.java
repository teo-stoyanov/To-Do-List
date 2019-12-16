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
import primeholding.models.todolists.ListGetCategoriesModel;
import primeholding.models.todolists.ListGetItemsModel;
import primeholding.models.todolists.ListGetModel;
import primeholding.models.todolists.ListPostModel;
import primeholding.models.todolists.ListPutModel;
import primeholding.service.ListService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController("listController")
@RequestMapping("/list")
public class ListController {
    private static final String CATEGORY_ID = "categoryIds";
    private static final String NAME = "name";

    private ListService service;
    private ToDoMapper mapper;

    @Autowired
    public ListController(ListService service, ToDoMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<ListGetModel>> get() {
        List<ToDoList> lists = this.service.getAll();
        List<ListGetModel> getModels = new ArrayList<>();

        lists.forEach(list -> {
            ListGetModel model = this.mapper.listToListGetModel(list);
            getModels.add(model);
        });

        return new ResponseEntity<>(getModels, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListGetModel> get(@PathVariable Integer id) {
        Optional<ToDoList> optional = this.service.getById(id);
        if (!optional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ListGetModel model = this.mapper.listToListGetModel(optional.get());

        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @GetMapping("/{id}/items")
    public ResponseEntity<List<ListGetItemsModel>> getItems(@PathVariable Integer id) {
        Optional<ToDoList> optional = this.service.getById(id);
        if (!optional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<ListGetItemsModel> getItemsModels = this.mapper.listToListGetItemsModel(optional.get().getItems());
        return new ResponseEntity<>(getItemsModels, HttpStatus.OK);
    }

    @GetMapping("/{id}/categories")
    public ResponseEntity<List<ListGetCategoriesModel>> getCategories(@PathVariable Integer id) {
        Optional<ToDoList> optional = this.service.getById(id);
        if (!optional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<ListGetCategoriesModel> getCategoriesModels = this.mapper.listToListGetCategoriesModel(optional.get().getCategories());
        return new ResponseEntity<>(getCategoriesModels, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<ListGetModel> post(@RequestBody ListPostModel listPostModel) {
        if (this.service.getUniqueValues().stream().anyMatch(s -> s.equals(listPostModel.getName()))) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else if (listPostModel.getName() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        ToDoList toDoList = this.mapper.postListToList(listPostModel);
        ToDoList register = this.service.register(toDoList);
        ListGetModel getModel = this.mapper.listToListGetModel(register);

        return new ResponseEntity<>(getModel, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListGetModel> put(@PathVariable Integer id, @RequestBody ListPutModel listPutModel) {
        Optional<ToDoList> entity = this.service.getById(id);
        if (!entity.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<ToDoList> optional = this.service.findByProp(listPutModel.getName());
        if (optional.isPresent() && !optional.get().getId().equals(id)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else if (listPutModel.getName() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (listPutModel.getCategoryIds() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        for (Integer categoryId : listPutModel.getCategoryIds()) {
            Optional<Category> categoryById = this.service.getOptionalCategoryById(categoryId);
            if (!categoryById.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }

        ToDoList toDoList = this.mapper.putListToList(listPutModel);
        toDoList.setId(id);
        toDoList.setCreatedDate(entity.get().getCreatedDate());

        for (Integer categoryId : listPutModel.getCategoryIds()) {
            toDoList.getCategories().add(this.service.getCategoryById(categoryId));
        }
        ToDoList result = this.service.register(toDoList);
        ListGetModel listGetModel = this.mapper.listToListGetModel(result);

        return new ResponseEntity<>(listGetModel, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ListGetModel> patch(@PathVariable Integer id, @RequestBody Map<String, Object> fields) {
        Optional<ToDoList> entity = this.service.getById(id);
        if (!entity.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (fields.get(NAME) == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (fields.containsKey(CATEGORY_ID) && fields.get(CATEGORY_ID) == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<ToDoList> optional = this.service.findByProp(fields.get(NAME).toString());
        if (optional.isPresent() && !optional.get().getId().equals(id)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else if (fields.containsKey(CATEGORY_ID)) {
            for (Integer categoryId : (List<Integer>) fields.get(CATEGORY_ID)) {
                Optional<Category> categoryById = this.service.getOptionalCategoryById(categoryId);
                if (!categoryById.isPresent()) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            }
        }

        ToDoList toDoList = this.service.update(entity.get(), fields);
        toDoList.setId(id);
        if(fields.containsKey(CATEGORY_ID)){
            toDoList.setCategories(new ArrayList<>());
            for (Integer categoryId : (List<Integer>) fields.get(CATEGORY_ID)) {
                toDoList.getCategories().add(this.service.getCategoryById(categoryId));
            }
        }

        ToDoList result = this.service.register(toDoList);
        ListGetModel listGetModel = this.mapper.listToListGetModel(result);

        return new ResponseEntity<>(listGetModel, HttpStatus.OK);
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

package primeholding.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import primeholding.entities.Category;
import primeholding.entities.ToDoList;
import primeholding.repositories.CategoryRepository;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class CategoryService implements BaseService<Category> {
    private static final Logger LOGGER = Logger.getLogger(CategoryService.class.getName());

    private CategoryRepository repository;
    private ListService listService;

    @Autowired
    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setListService(ListService listService) {
        this.listService = listService;
    }

    @Override
    public Category register(Category entity) {
        return this.repository.save(entity);
    }

    @Override
    public List<Category> getAll() {
        return this.repository.findAll();
    }

    @Override
    public Optional<Category> getById(Integer id) {
        return this.repository.findById(id);
    }

    @Override
    public void deleteById(Integer id) {
        this.repository.deleteById(id);
    }

    @Override
    public Category update(Category item, Map<String, Object> fields) {
        for (Map.Entry<String, Object> stringObjectEntry : fields.entrySet()) {
            Field entityFiled;
            try {
                entityFiled = item.getClass().getDeclaredField(stringObjectEntry.getKey());
                entityFiled.setAccessible(true);
                entityFiled.set(item, stringObjectEntry.getValue());
            } catch (NoSuchFieldException | IllegalAccessException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        }
        return item;
    }

    @Override
    public List<String> getUniqueValues() {
        return this.repository.getUniqueValues();
    }

    @Override
    public Optional<Category> findByProp(String name) {
        return this.repository.findByName(name);
    }

    public Category getCategoryById(Integer id) {
        return this.repository.getOne(id);
    }

    public Optional<ToDoList> getOptionalToDoListById(Integer id) {
        return this.listService.getById(id);
    }

    public ToDoList getToDoListById(Integer id) {
        return this.listService.getToDoList(id);
    }
}

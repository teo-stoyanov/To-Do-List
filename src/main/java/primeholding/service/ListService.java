package primeholding.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import primeholding.entities.ToDoList;
import primeholding.repositories.ListRepository;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ListService implements BaseService<ToDoList> {
    private static final Logger LOGGER = Logger.getLogger(ListService.class.getName());

    private ListRepository repository;

    @Autowired
    public ListService(ListRepository repository) {
        this.repository = repository;
    }

    @Override
    public ToDoList register(ToDoList entity) {
        return this.repository.save(entity);
    }

    @Override
    public java.util.List<ToDoList> getAll() {
        return this.repository.findAll();
    }

    @Override
    public Optional<ToDoList> getById(Integer id) {
        return this.repository.findById(id);
    }

    @Override
    public void deleteById(Integer id) {
        this.repository.deleteById(id);
    }

    @Override
    public ToDoList update(ToDoList list, Map<String, Object> fields) {
        for (Map.Entry<String, Object> stringObjectEntry : fields.entrySet()) {
            Field entityFiled;
            try {
                entityFiled = list.getClass().getDeclaredField(stringObjectEntry.getKey());
                entityFiled.setAccessible(true);
                entityFiled.set(list, stringObjectEntry.getValue());
            } catch (NoSuchFieldException | IllegalAccessException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        }
        return list;
    }

    @Override
    public java.util.List<String> getUniqueValues() {
        return this.repository.getUniqueValues();
    }

    @Override
    public Optional<ToDoList> findByProp(String name) {
        return this.repository.findByName(name);
    }
}

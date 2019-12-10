package primeholding.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import primeholding.entities.Item;
import primeholding.repositories.ItemRepository;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ItemService implements BaseService<Item> {
    private static final Logger LOGGER = Logger.getLogger(ItemService.class.getName());

    private ItemRepository repository;

    @Autowired
    public ItemService(ItemRepository repository) {
        this.repository = repository;
    }

    @Override
    public void register(Item entity) {
        this.repository.save(entity);
    }

    @Override
    public List<Item> getAll() {
        return this.repository.findAll();
    }

    @Override
    public Optional<Item> getById(Integer id) {
        return this.repository.findById(id);
    }

    @Override
    public void deleteById(Integer id) {
        this.repository.deleteById(id);
    }

    @Override
    public Item update(Item item, Map<String, Object> fields) {
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
}

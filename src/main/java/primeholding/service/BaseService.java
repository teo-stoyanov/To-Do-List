package primeholding.service;

import primeholding.entities.Item;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BaseService<T> {
    void register(T entity);

    List<Item> getAll();

    Optional<Item> getById(Integer id);

    void deleteById(Integer id);

    T update(T item, Map<String, Object> fields);

    List<String> getUniqueValues();
}

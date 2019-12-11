package primeholding.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BaseService<T> {
    T register(T entity);

    List<T> getAll();

    Optional<T> getById(Integer id);

    void deleteById(Integer id);

    T update(T item, Map<String, Object> fields);

    List<String> getUniqueValues();

    Optional<T> findByProp (String prop);
}

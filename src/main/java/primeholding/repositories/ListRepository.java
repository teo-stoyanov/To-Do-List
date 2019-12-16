package primeholding.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import primeholding.entities.Category;
import primeholding.entities.ToDoList;

import java.util.List;
import java.util.Optional;

@Repository
public interface ListRepository extends JpaRepository<ToDoList,Integer> {

    @Query(value = "SELECT name FROM to_do_list", nativeQuery = true)
    List<String> getUniqueValues();

    Optional<ToDoList> findByName(String name);
}

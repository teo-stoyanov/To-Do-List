package primeholding.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import primeholding.entities.Category;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer> {

    @Query(value = "SELECT name FROM category", nativeQuery = true)
    List<String> getUniqueValues();

    Optional<Category> findByName(String name);
}

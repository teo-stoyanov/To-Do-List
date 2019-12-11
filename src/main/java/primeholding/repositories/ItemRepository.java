package primeholding.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import primeholding.entities.Item;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

    @Query(value = "SELECT title FROM items", nativeQuery = true)
    List<String> getUniqueValues();

    Optional<Item> findByTitle(String title);
}

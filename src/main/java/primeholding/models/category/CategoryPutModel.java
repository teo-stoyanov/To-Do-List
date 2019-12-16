package primeholding.models.category;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class CategoryPutModel {
    private String name;
    private Set<Integer> listIds = new HashSet<>();
}

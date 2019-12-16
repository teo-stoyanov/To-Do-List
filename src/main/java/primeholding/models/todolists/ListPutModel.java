package primeholding.models.todolists;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class ListPutModel {
    private String name;
    private Set<Integer> categoryIds = new HashSet<>();
}

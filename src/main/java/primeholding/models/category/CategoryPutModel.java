package primeholding.models.category;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryPutModel {
    private String name;
    private List<Integer> listIds = new ArrayList<>();
}

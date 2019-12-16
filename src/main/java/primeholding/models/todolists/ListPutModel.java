package primeholding.models.todolists;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ListPutModel {
    private String name;
    private List<Integer> categoryIds = new ArrayList<>();
}

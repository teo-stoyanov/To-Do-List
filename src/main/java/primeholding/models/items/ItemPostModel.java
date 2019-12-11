package primeholding.models.items;

import lombok.Data;

@Data
public class ItemPostModel {
    private String title;
    private String description;
    private Boolean isCompleted = false;
}

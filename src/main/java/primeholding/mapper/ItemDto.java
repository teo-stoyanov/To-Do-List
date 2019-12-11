package primeholding.mapper;

import lombok.Data;

@Data
public class ItemDto {

    private Integer id;
    private String title;
    private String description;
    private Boolean isCompleted = false;
}

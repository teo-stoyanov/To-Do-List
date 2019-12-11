package primeholding.mapper;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ItemDto {

    private Integer id;
    @NotNull(message = "Title can not be null")
    private String title;
    private String description;
    private Boolean isCompleted = false;
}

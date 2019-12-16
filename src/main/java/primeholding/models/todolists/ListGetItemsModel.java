package primeholding.models.todolists;

import lombok.Data;

import java.util.Date;

@Data
public class ListGetItemsModel {
    private Integer id;
    private String title;
    private String description;
    private Boolean isCompleted;
    private Date createdDate;
    private Date lastModifiedDate;
}

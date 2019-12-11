package primeholding.mapper;

import lombok.Data;

import java.util.Date;

@Data
public class ItemGetModel {
    private Integer id;
    private String title;
    private String description;
    private Boolean isCompleted;
    private Date createdDate;
    private Date lastModifiedDate;
}

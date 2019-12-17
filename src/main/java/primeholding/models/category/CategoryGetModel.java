package primeholding.models.category;

import lombok.Data;

import java.util.Date;

@Data
public class CategoryGetModel {

    private Integer id;
    private String name;
    private Date createdDate;
    private Date lastModifiedDate;
}

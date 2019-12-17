package primeholding.models.todolists;

import lombok.Data;

import java.util.Date;

@Data
public class ListGetCategoriesModel {

    private Integer id;
    private String name;
    private Date createdDate;
    private Date lastModifiedDate;
}

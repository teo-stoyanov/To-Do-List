package primeholding.models.todolists;

import lombok.Data;

import java.util.Date;

@Data
public class ListGetModel {

    private Integer id;
    private String name;
    private Date createdDate;
    private Date lastModifiedDate;
}

package primeholding.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "items")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Item extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String title;

    @Column
    private String description;

    @Column(name = "is_completed")
    private Boolean isCompleted;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "list_id", nullable = false)
    private ToDoList toDoList;
}


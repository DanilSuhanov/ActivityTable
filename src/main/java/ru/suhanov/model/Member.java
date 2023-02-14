package ru.suhanov.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import ru.suhanov.model.enam.TaskRole;

import javax.persistence.*;

@Data
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private TaskRole taskRole;

    @JsonIgnore
    @ManyToOne
    @JoinTable(name = "user_id")
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="task_id")
    private Task task;
}

package ru.suhanov.model;

import lombok.Data;
import ru.suhanov.model.enam.TaskRoleAuthority;

import javax.persistence.*;

@Data
@Entity
public class TaskRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private TaskRoleAuthority roleAuthority;

    @ManyToOne
    @JoinColumn(name="task_id")
    private Task task;
}

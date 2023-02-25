package ru.suhanov.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import ru.suhanov.model.enam.TaskRole;
import ru.suhanov.model.task.Task;
import ru.suhanov.model.task.TaskMessage;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private TaskRole taskRole;


    @ManyToOne
    @JoinTable(name = "user_id")
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="task_id")
    private Task task;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<TaskMessage> taskMessages;
}

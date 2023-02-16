package ru.suhanov.model.task;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import ru.suhanov.model.Member;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class TaskMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonIgnore
    @ManyToOne
    @JoinTable(name = "task_id")
    private Task task;

    @JsonIgnore
    @ManyToOne
    @JoinTable(name = "member_id")
    private Member member;

    private String content;

    private Date date;

}

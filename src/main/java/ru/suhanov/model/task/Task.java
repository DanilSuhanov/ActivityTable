package ru.suhanov.model.task;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import ru.suhanov.model.Member;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private String description;

    private int completeness;

    private boolean verification;

    @OneToMany(mappedBy="task", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Member> members = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TaskMessage> taskMessages = new ArrayList<>();

    public void addMember(Member member) {
        members.add(member);
    }

    public void removeMember(Member member) {
        members.remove(member);
    }
}
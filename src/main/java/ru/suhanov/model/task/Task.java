package ru.suhanov.model.task;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.suhanov.model.Member;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private String description;

    //Completeness
    private int completeness;
    private boolean verification;

    //Deadlines
    private LocalDateTime deadline;
    private boolean expired;

    @OneToMany(mappedBy="task", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Member> members = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TaskMessage> taskMessages = new ArrayList<>();

    public Task(String title, String description, int completeness, LocalDateTime deadline) {
        this.title = title;
        this.description = description;
        this.completeness = completeness;
        this.deadline = deadline;
    }

    public void addMember(Member member) {
        members.add(member);
    }

    public void removeMember(Member member) {
        members.remove(member);
    }
}
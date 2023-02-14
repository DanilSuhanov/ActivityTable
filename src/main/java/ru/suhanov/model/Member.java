package ru.suhanov.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "member_roles",
            joinColumns = @JoinColumn(name = "memberId"),
            inverseJoinColumns = @JoinColumn(name = "roleId"))
    private List<TaskRole> roles = new ArrayList<>();

    @ManyToOne
    @JoinTable(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="task_id")
    private Task task;

    public void addTaskRole(TaskRole taskRole) {
        roles.add(taskRole);
    }
}

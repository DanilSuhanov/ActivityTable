package ru.suhanov.model;

import lombok.Data;

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

    @OneToMany(mappedBy="task", fetch = FetchType.LAZY)
    private List<Member> members;

    public void addMember(Member member) {
        members.add(member);
    }
}
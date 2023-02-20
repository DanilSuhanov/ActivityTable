package ru.suhanov.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import ru.suhanov.model.User;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class ImpRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonIgnore
    @ManyToOne
    @JoinTable(name = "imp_request_implementers")
    private User imp;

    @ManyToOne
    @JoinTable(name = "sender_request")
    private User sender;

    private Date date;
}
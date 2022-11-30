package ru.kata.spring.boot_security.demo.model;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "authority")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String authority;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    @Override
    public String getAuthority() {
        return authority;
    }

    @Override
    public String toString() {
        return this.authority;
    }

    public Role() {
    }

    public Role(Long id, String authority, Set<User> users) {
        this.id = id;
        this.authority = authority;
        this.users = users;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(id, role.id) && Objects.equals(authority, role.authority) && Objects.equals(users, role.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, authority, users);
    }
}

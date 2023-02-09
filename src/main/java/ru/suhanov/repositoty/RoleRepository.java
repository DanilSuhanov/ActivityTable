package ru.suhanov.repositoty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.suhanov.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByAuthority(@Param("authority") String authority);
}

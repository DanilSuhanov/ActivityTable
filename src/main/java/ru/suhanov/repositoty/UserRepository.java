package ru.suhanov.repositoty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.suhanov.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "from User u left join fetch u.roles where u.username = :username")
    User findUserByUsername(@Param("username") String username);
}

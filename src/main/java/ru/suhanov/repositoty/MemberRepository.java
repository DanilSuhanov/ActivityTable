package ru.suhanov.repositoty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.suhanov.model.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findMemberById(long id);
}

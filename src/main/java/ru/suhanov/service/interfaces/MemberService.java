package ru.suhanov.service.interfaces;

import ru.suhanov.model.Member;

public interface MemberService {
    void addNewMember(Member member);
    Member findMemberById(long id);
}

package ru.suhanov.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.suhanov.model.Member;
import ru.suhanov.model.User;
import ru.suhanov.repositoty.MemberRepository;
import ru.suhanov.service.interfaces.MemberService;
import ru.suhanov.service.interfaces.UserService;

import javax.transaction.Transactional;

@Service
public class MemberServiceImp implements MemberService {

    private final MemberRepository memberRepository;
    private final UserService userService;

    @Autowired
    public MemberServiceImp(MemberRepository memberRepository, UserService userService) {
        this.memberRepository = memberRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    public void addNewMember(Member member) {
        memberRepository.save(member);
    }

    @Override
    @Transactional
    public Member findMemberById(long id) {
        return memberRepository.findMemberById(id);
    }

    @Override
    @Transactional
    public void deleteMember(Member member) {
        User user = member.getUser();
        user.removeMember(member);
        userService.editUser(user);
        memberRepository.delete(member);
    }
}

package ru.suhanov.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.suhanov.model.Member;
import ru.suhanov.repositoty.MemberRepository;
import ru.suhanov.service.interfaces.MemberService;

import javax.transaction.Transactional;

@Service
public class MemberServiceImp implements MemberService {

    private final MemberRepository memberRepository;

    public MemberServiceImp(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    @Transactional
    public void addNewMember(Member member) {
        memberRepository.save(member);
    }
}

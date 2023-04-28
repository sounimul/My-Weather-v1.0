package weather.weatherspring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import weather.weatherspring.domain.Member;
import weather.weatherspring.entity.MemberForm;
import weather.weatherspring.repository.MemberRepository;

import java.util.Optional;

@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /* 회원가입 */
    public Long join(Member member) {
        validateDuplicateMember(member);    // 멤버 중복 체크
        memberRepository.save(member);      // 멤버 저장(회원가입)
        return member.getUid();              // 멤버의 uid를 가져와야함 (프로젝트 내 처리는 uid로)
    }

    /* 중복회원 체크 */
    private void validateDuplicateMember(Member member) {
        memberRepository.findById(member.getId())       // 아이디가 겹치는지 확인
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    /* id -> 회원 조회 (로그인 시) */
    public Optional<Member> findOne(Member member) {
        memberRepository.findById(member.getId())
                .ifPresentOrElse(m -> {
                    if(!m.getPw().equals(member.getPw())) {
                        throw new IllegalStateException("비밀번호가 일치하지 않습니다");
                    }else if(m.getAvail().equals('N')){
                        throw new IllegalStateException("탈퇴한 사용자입니다.");
                    }
                },() -> {
                    throw new IllegalStateException("존재하지 않는 회원입니다.");
                });
        return memberRepository.findById(member.getId());
    }

    /* 비밀번호 변경 */
    public Boolean updatePw(Long uid, MemberForm pwForm){
        // 현재 uid의 객체 가져오기
        Member member=memberRepository.findByUid(uid).get();
        // 현재 비밀번호가 일치할 때
        if(pwForm.getCurPw().equals(member.getPw())){
            member.setPw(pwForm.getPw());
            memberRepository.save(member);
            return true;
        }else return false;
    }

}

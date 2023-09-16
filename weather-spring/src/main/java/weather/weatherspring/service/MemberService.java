package weather.weatherspring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import weather.weatherspring.entity.Member;
import weather.weatherspring.domain.MemberForm;
import weather.weatherspring.repository.MemberRepository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

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

    public Optional<Member> validateDuplicateId(String id){
        return memberRepository.findById(id);
    }

    /* id -> 회원 조회 (로그인 시) */
    public Optional<Member> findOne(String id,String pw) {
        AtomicReference<Optional<Member>> found= new AtomicReference<>(memberRepository.findById(id));
        memberRepository.findById(id)
                .ifPresent(m -> {
                    if(!m.getPw().equals(pw)) {
                        found.set(Optional.empty());
                    }else if(m.getAvail().equals("N")){
                        found.set(Optional.empty());
                    }
                });
        return found.get();
    }

    public Optional<Member> findMember(Long uid){
        return memberRepository.findByUid(uid);
    }

    public List<Member> findMembers(String avail){
        return memberRepository.findAll(avail);
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

    /* 프로필 변경 */
    public Optional<Member> updateProfile(Long uid, MemberForm profileForm){
        // 현재 uid의 객체 가지고 오기
        Member member=memberRepository.findByUid(uid).get();
        member.setNickname(profileForm.getNickname());
        member.setFvweather(profileForm.getFvweather());
        memberRepository.save(member);
        return Optional.ofNullable(member);
    }

    /* 사용자 권한 변경 */
    public Long updateUserAuth(Long uid){
        Member member = memberRepository.findByUid(uid).get();
        member.setAvail("N");
        member = memberRepository.save(member);
        return member.getUid();
    }

}

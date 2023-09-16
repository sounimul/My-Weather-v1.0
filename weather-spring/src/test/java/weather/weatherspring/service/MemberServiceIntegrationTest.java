package weather.weatherspring.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import weather.weatherspring.entity.Member;
import weather.weatherspring.repository.MemberRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class MemberServiceIntegrationTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception{
        // Given
        Member member = new Member();
        member.setId("Hello");
        member.setPw("Hello12345!");
        member.setNickname("hi");
        member.setAvail("Y");

        // When
        Long saveUid = memberService.join(member);

        // Then
        Member findMember = memberRepository.findByUid(saveUid).get();
        assertThat(member.getId()).isEqualTo(findMember.getId());

    }

    @Test
    public void 중복회원_예외() throws Exception {
        // Given
        Member member1 = new Member();
        member1.setId("Hello");
        member1.setPw("Hello12345!");
        member1.setNickname("hi");
        member1.setAvail("Y");

        Member member2 = new Member();
        member2.setId("Hello");
        member2.setPw("Hello12345!");
        member2.setNickname("hi");
        member2.setAvail("Y");

        // When
        memberService.join(member1);
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> memberService.join(member2));

        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");

    }

//    public void 비밀번호변경() throws Exception{
//        // Given
//        Member member = new Member();
//        MemberForm pwForm = new MemberForm();
//        member.setId("Hello");
//        member.setPw("Hello12345!");
//        member.setNickname("hi");
//        member.setFvweather("맑음");
//        member.setAvail("Y");
//        pwForm.setCurPw("Hello1234!");
//        pwForm.setPw("Hi12345!");
//
//        // When
//        memberService.join(member);
//        memberService.updatePw(member.getUid(),pwForm);
//
//        // Then
//        Member findMember = memberService.findOne(member).get();
//        assertThat(member.getId()).isEqualTo(findMember.getId());
//    }
//
//    public void 기존비밀번호일치_예외(){
//        // Given
//        Member member = new Member();
//        MemberForm pwForm = new MemberForm();
//        member.setId("Hello");
//        member.setPw("Hello12345!");
//        member.setNickname("hi");
//        member.setFvweather("맑음");
//        member.setAvail("Y");
//        pwForm.setCurPw("Hello1234!");
//
//    }

    @Test
    public void 회원탈퇴_권한변경(){
        // Givne
        Member member = new Member();
        member.setId("Hello@coho.com");
        member.setPw("Hello12345!");
        member.setNickname("hi");
        member.setAvail("Y");
        Long uid = memberService.join(member);

        // When
        Long saveUid = memberService.updateUserAuth(uid);

        // Then
        Member saveMember = memberRepository.findByUid(saveUid).get();
        assertThat(saveMember.getAvail()).isEqualTo("N");


    }
}

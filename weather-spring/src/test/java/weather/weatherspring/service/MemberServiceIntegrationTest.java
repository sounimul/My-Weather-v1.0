package weather.weatherspring.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import weather.weatherspring.domain.Member;
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
        member.setId("hello");
        member.setPw("Hello12345!");
        member.setNickname("hi");
        member.setAvail("Y");

        // When
        Long saveUid = memberService.join(member);

        // Then
        Member findMember = memberService.findOne(member).get();
        assertThat(member.getId()).isEqualTo(findMember.getId());

    }

    @Test
    public void 중복회원_예외() throws Exception {
        // Given
        Member member1 = new Member();
        member1.setId("hello");
        member1.setPw("Hello12345!");
        member1.setNickname("hi");
        member1.setAvail("Y");

        Member member2 = new Member();
        member2.setId("hello");
        member2.setPw("Hello12345!");
        member2.setNickname("hi");
        member2.setAvail("Y");

        // When
        memberService.join(member1);
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> memberService.join(member2));

        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");

    }
}

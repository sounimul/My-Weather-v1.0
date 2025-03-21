package weather.weatherspring.domain.dto;

import lombok.Getter;
import lombok.Setter;

/* createMemberForm.html의 input 데이터를 전달 받을 폼 객체 */
@Getter
@Setter
public class MemberForm {

    // createMemberForm.html의 input name과 매칭되어 들어옴
    private String emailLocal;
    private String emailDomain;
    private String userid;
    private String pw;
    private String nickname;
    private String fvweather;
    private String curPw;
    private String checkPw;

}

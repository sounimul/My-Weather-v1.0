package weather.weatherspring.controller;

/* createMemberForm.html의 input 데이터를 전달 받을 폼 객체 */
public class JoinForm {

    // createMemberForm.html의 input name과 매칭되어 들어옴
    private String userid;
    private String pw;
    private String nickname;
    private String intro;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }
}

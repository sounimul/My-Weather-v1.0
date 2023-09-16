package weather.weatherspring.domain;

/* createMemberForm.html의 input 데이터를 전달 받을 폼 객체 */
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
    public String getEmailLocal() {
        return emailLocal;
    }

    public void setEmailLocal(String emailLocal) {
        this.emailLocal = emailLocal;
    }

    public String getEmailDomain() {
        return emailDomain;
    }

    public void setEmailDomain(String emailDomain) {
        this.emailDomain = emailDomain;
    }

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

    public String getFvweather() {
        return fvweather;
    }

    public void setFvweather(String fvweather) {
        this.fvweather = fvweather;
    }

    public String getCurPw() {
        return curPw;
    }

    public void setCurPw(String curPw) {
        this.curPw = curPw;
    }

    public String getCheckPw() {
        return checkPw;
    }

    public void setCheckPw(String checkPw) {
        this.checkPw = checkPw;
    }
}

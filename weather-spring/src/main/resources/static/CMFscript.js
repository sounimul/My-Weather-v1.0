const id = document.getElementById("userid");
const mail = document.getElementById("useridEmail");
let pw = document.getElementById("pw");
let pwc = document.getElementById("pwCheck");

function checkID(){
    //console.log('사용자가 원하는 아이디', id.value);
    // const user = id.value+ '@' +mail.value;
    // console.log(user);
    if(id.value.length===0 || mail.value.length===0) {
        alert('아이디를 입력해 주세요');
    }
    else{
        // Ajax 요청 생성하여 서버로 user 전송
        $.ajax({
            url:"/checkId",
            type:"POST",
            data:{
                id:id.value,
                mail:mail.value
            },
            success:function (data){
                console.log("서버로 회원 정보를 전송했습니다."+data);
                if(data===""){
                    alert("이미 존재하는 아이디입니다.");
                    id.value = '';
                }else{
                    alert('사용 가능한 아이디입니다.');
                    /*자바스크립트 내 확인용 저장*/
                    //id.value += '   ✔️'; //표시 기능, 값이 변경되어 불가, 체크 공간 구현하면 가능
                    id.style.borderBottom = 'none';
                    id.style.color = "green";
                    id.setAttribute('readonly', true); //수정 불가
                }

            },
            error:function(xhr, status, error){
                console.error("서버로 회원 정보를 전송하지 못하였습니다.");
            }
        });
    }

}

function checkPW(){
    //console.log('사용자가 원하는 비밀번호', pw.value);
    //8-20자리
    //영문 대소문자
    //숫자
    //아래의 특수문자

    let regexPw =  /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,20}$/;

    if(!regexPw.test(pw.value)) {
        pw.style.color = "red";
        pw.style.borderBottom = "1px solid red"
        return false;
    }

    else {
        pw.style.color = "green";
        pw.style.borderBottom = "1px solid green"
        return true;
    }
}

function doublecheckPW(){
    //console.log('비밀번호 중복 체크', pwc.value);
    //8-20자리
    //영문 대소문자
    //숫자
    //아래의 특수문자

    if(pw.value!==pwc.value) {
        pwc.style.color = "red";
        pwc.style.borderBottom = "1px solid red"
        return false;
    }

    else {
        pwc.style.color = "green";
        pwc.style.borderBottom = "1px solid green"
        return true;
    }
}

function confirmPW(){
    let regexPw =  /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,20}$/;

    if(!regexPw.test(pw.value)) {
        pw.value = '';
        pw.style.borderBottom = "1px solid gray"
        return false;
    }

    else {
        pw.style.borderBottom = 'none';
        return true;
    }
}

function confirmPWC(){
    let regexPw =  /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,20}$/;

    if(!regexPw.test(pw.value) && pw.value!==pwc.value) {
        pwc.value = '';
        pwc.style.borderBottom = "1px solid gray"
        return false;
    }

    else {
        pwc.style.borderBottom = 'none';
        return true;
    }
}

function changeImg(weather){
    document.querySelector('#wicon').setAttribute('src',`${weather}.png`);
}

// function changeImg(weather){
//     document.querySelector('#wicon').setAttribute('src',`../../static/${weather}.png`);
// }

/* pwCondition */
document.querySelector('#pwCondition').addEventListener('mouseover',()=>{
    document.querySelector('#pwConditionText').style.display = "flex";
})

document.querySelector('#pwCondition').addEventListener('mouseout',()=>{
    document.querySelector('#pwConditionText').style.display = "none";
})

function emailChange(){
    const select = document.getElementById('idEmail');
    //select.options[select.selectedIndex].value
    if(select.options[select.selectedIndex].value===''){
        document.getElementById('useridEmail').style.borderBottom = '1px solid gray';
        document.getElementById('useridEmail').disabled = false;
        document.getElementById('useridEmail').focus();
        document.getElementById('useridEmail').style.backgroundColor = 'transparent';
    }
    else {
        document.getElementById('useridEmail').value = select.options[select.selectedIndex].value;
        document.getElementById('useridEmail').style.backgroundColor = 'transparent';
        document.getElementById('useridEmail').style.border = 'none';
    }
}

function check(){
    if(id.style.color==='green'){
        alert('회원가입이 완료되었습니다.');
        return true;
    }
    else {
        alert("id 중복 확인이 필요합니다.");
        return false;
    }
}
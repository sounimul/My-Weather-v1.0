const regexPW =  /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,20}$/;
const currentPW = document.querySelector('#currentPW');
const newPW = document.querySelector('#newPW');
const confirmPW = document.querySelector('#confirmPW');

/* 현재 비밀번호 */
function checkCurrentPW(){
    //console.log('사용자가 원하는 비밀번호', pw.value);
    //8-20자리
    //영문 대소문자
    //숫자
    //아래의 특수문자
    
    if(!regexPW.test(currentPW.value)) {
        currentPW.style.color = "red";
        currentPW.style.borderBottom = "1px solid red"
        return false;
    }
    
    else {
        currentPW.style.color = "green";
        currentPW.style.borderBottom = "1px solid green"
        return true;
    }
}

function confirmCurrentPW(){

    if(!regexPW.test(currentPW.value)) {
        currentPW.style.color = "red";
        // currentPW.value = '';
        currentPW.style.borderBottom = "1px solid red"
        return false;
    }
    
    else {
        currentPW.style.borderBottom = 'none';
        return true;
    }
}

// 맞는 비밀번호인지 확인 추가


/* 새로운 비밀번호 */
function checkNewPW(){
    //console.log('사용자가 원하는 비밀번호', pw.value);
    //8-20자리
    //영문 대소문자
    //숫자
    //아래의 특수문자
    if(!regexPW.test(newPW.value) || newPW.value===currentPW.value) {
        newPW.style.color = "red";
        newPW.style.borderBottom = "1px solid red"
        return false;
    }
    
    else {
        newPW.style.color = "green";
        newPW.style.borderBottom = "1px solid green"
        return true;
    }
}

function confirmNewPW(){

    if(!regexPW.test(newPW.value) || newPW.value===currentPW.value) {
        // newPW.value = '';
        newPW.style.color = "red";
        newPW.style.borderBottom = "1px solid red"
        return false;
    }
    
    else {
        newPW.style.borderBottom = 'none';
        return true;
    }
}


/* 새로운 비밀번호 재확인 */
function checkConfirmPW(){
    //console.log('사용자가 원하는 비밀번호', pw.value);
    //8-20자리
    //영문 대소문자
    //숫자
    //아래의 특수문자
    
    if(!regexPW.test(confirmPW.value) || newPW.value!==confirmPW.value || newPW.value===currentPW.value) {
        confirmPW.style.color = "red";
        confirmPW.style.borderBottom = "1px solid red"
        return false;
    }
    
    else {
        confirmPW.style.color = "green";
        confirmPW.style.borderBottom = "1px solid green"
        return true;
    }
}

function completePWC(){
    let regexPw =  /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,20}$/;
    
    if(!regexPW.test(confirmPW.value) || newPW.value!==confirmPW.value || newPW.value===currentPW.value) {
        // confirmPW.value = '';
        confirmPW.style.color = "red";
        confirmPW.style.borderBottom = "1px solid red"
        return false;
    }
    
    else {
        confirmPW.style.borderBottom = 'none';
        return true;
    }
}
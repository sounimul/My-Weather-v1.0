let weather = document.querySelector('#currentExp').innerText;
let fileName = '';

const wicon = document.getElementById("wicon");
const clock = document.getElementById("clock");
const pastHour = document.getElementById('pastHour');
const futureHour = document.getElementById('futureHour');
const loc = document.getElementById("location");

const futureDay1 = document.getElementsByClassName('futureDay')[0];
const futureDay2 = document.getElementsByClassName('futureDay')[1];
const futureDay3 = document.getElementsByClassName('futureDay')[2];
const futureDay4 = document.getElementsByClassName('futureDay')[3];
const futureDay5 = document.getElementsByClassName('futureDay')[4];

const weather_arr = ['맑음','구름 많음','흐림','비','비 또는 눈','눈','소나기','빗방울','빗방울눈날림','눈날림'];

function Icon(){

    switch (weather){
        case '맑음':
            fileName="sun";
            break;
        case '구름 많음':
            fileName="cloudAndSun";
            break;
        case '흐림':
            fileName="cloud";
            break;
        case '비':
            fileName="shower";
            break;
        case '비 또는 눈':
            fileName="snow";
            break;
        case '눈':
            fileName="snow";
            break;
        case '빗방울':
            fileName="rain";
            break;
        case '빗방울눈날림':
            fileName="rain";
            break;
        case '눈날림':
            fileName="snow";
            break;
        default:
            fileName="cloud";
    }

    if(weather === "비 또는 눈" || weather === "구름 많음" ){
        document.querySelector("#currentExp").style.width = "120px";
    } else if(weather === "비" || weather === "눈"){
        document.querySelector("#currentExp").style["margin-right"] = "4px";
    }
    wicon.setAttribute('src',`../${fileName}.svg`); // 백엔드
    // wicon.setAttribute('src',`../static/${fileName}.svg`);   // 프론트엔드

    // 사용자가 좋아하는 날씨 표시
    const fvWeather = document.querySelector('#fvweather').innerHTML;
    
    // let fvWeather = 'shower';

    if(fvWeather === weather) {
        if(weather==="cloud"){
            document.querySelector("#love").style.transform = "translate(-70px, -16px)"
            document.querySelector(".myFvIcon").style.transform = "translate(40px, 30px)"
        }
        else if(weather === "rain"){
            document.querySelector("#love").style.transform = "translate(-100px, -16px)"
            document.querySelector(".myFvIcon").style.transform = "translate(10px, 30px)"
        }
        else if(weather === "shower"){
            document.querySelector("#love").style.transform = "translate(-80px, -16px)"
            document.querySelector(".myFvIcon").style.transform = "translate(30px, 30px)"
        } else {
            document.querySelector("#love").style.transform = "translate(164px, -16px)"
            document.querySelector(".myFvIcon").style.transform = "translate(274px, 30px)"
        }

        document.querySelector('#love').style.display = "block";
    }
}

const day_arr = ['일','월','화','수','목','금','토'];
let now = new Date(); //날짜, 시간 객체
let year = 0;
let month = 0;
let date = 0;
let day = 0;
let hour = 0; //24시간제
let min = 0;
let sec = 0;

function getClock(){

    now = new Date();
    day = now.getDay();
    hour = now.getHours();

    /* 1시간 전후 시간 */
    const past = hour-1;
    if(past<0) pastHour.innerText = '11시';
    else pastHour.innerText = `${past}시`;

    const future = hour+1;
    if(future==24) futureHour.innerText = `0시`
    else futureHour.innerText = `${future}시`
    
    /* 주간예보 요일 */
    if(day===2) { //화
        futureDay1.innerText = day_arr[3];
        futureDay2.innerText = day_arr[4];
        futureDay3.innerText = day_arr[5];
        futureDay4.innerText = day_arr[6];
        futureDay5.innerText = day_arr[1];
    }
    else if(day===3) { //수
        futureDay1.innerText = day_arr[4];
        futureDay2.innerText = day_arr[5];
        futureDay3.innerText = day_arr[6];
        futureDay4.innerText = day_arr[0];
        futureDay5.innerText = day_arr[1];
    }
    else if(day===4) { //목
        futureDay1.innerText = day_arr[5];
        futureDay2.innerText = day_arr[6];
        futureDay3.innerText = day_arr[0];
        futureDay4.innerText = day_arr[1];
        futureDay5.innerText = day_arr[2];
    }
    else if(day===5) { //금
        futureDay1.innerText = day_arr[6];
        futureDay2.innerText = day_arr[0];
        futureDay3.innerText = day_arr[1];
        futureDay4.innerText = day_arr[2];
        futureDay5.innerText = day_arr[3];
    }
    else if(day===6) { //토
        futureDay1.innerText = day_arr[0];
        futureDay2.innerText = day_arr[1];
        futureDay3.innerText = day_arr[2];
        futureDay4.innerText = day_arr[3];
        futureDay5.innerText = day_arr[4];
    }
    else { //나머지
        futureDay1.innerText = day_arr[day+1];
        futureDay2.innerText = day_arr[day+2];
        futureDay3.innerText = day_arr[day+3];
        futureDay4.innerText = day_arr[day+4];
        futureDay5.innerText = day_arr[day+5];
    }
}

/* 위치 새로고침 */
function getLoc(){

    document.querySelector('.loadBack').style.display = "flex";

    now = new Date();
    navigator.geolocation.getCurrentPosition(Sucess, Error);
    year = now.getFullYear();
    month = now.getMonth();
    date = now.getDate();
    day = now.getDay();
    hour = now.getHours(); //24시간제
    min = now.getMinutes();
    sec = now.getSeconds();

    function Sucess(position){

        const latitude = position.coords.latitude;
        const longitude = position.coords.longitude;

        // 1개의 $ajax로 날짜,시간,위도,경도를 서버로 전송하기 위해
        // // Ajax 요청 생성하여 서버로 위치 정보를 전송
        $.ajax({
            url:"/weather",
            type:"POST",
            data:JSON.stringify({
                latitude:latitude,
                longitude:longitude,
                year:year,
                month:month+1,
                date:date,
                hour:hour,
                min:min,
                sec:sec
            }),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function(data){
                location.href="/weather";
                console.log("서버로 위치 정보를 전송했습니다.");
            },
            error: function(xhr, status, error){
                console.error("서버로 위치 정보 전송하지 못하였습니다.");
            }
        });
    }

    function Error(position){
        console.log('위치 실패');
        //기본 위치 설정
    }
}

/* 함수 호출 */
Icon();
getClock();
// document.querySelector('#refresh').addEventListener('click',()=>{
//     // console.log('위치 새로고침');
//     getLoc();
// })

/* 버튼 기능 */
/* 메뉴 */
document.querySelector('#openMenu').addEventListener('click',()=>{

    if(document.querySelector('.menuBtn').style.display==='none' || document.querySelector('.menuBtn').style.display===''){
        // console.log('열기');
        document.querySelector('#openMenuIcon').innerText = 'remove';
        document.querySelector('.menuBtn').style.display = 'flex';
        document.querySelector('#openMenu').style.color = "var(--element-color)"
        document.querySelector('#openMenu').style.backgroundColor = "var(--element-background-color)"
    }
    
    else {
        // console.log('닫기');
        document.querySelector('#openMenu').style.color = "var(--element-background-color)"
        document.querySelector('#openMenu').style.backgroundColor = "var(--element-color)"
        document.querySelector('#openMenuIcon').innerText = 'menu';
        document.querySelector('.menuBtn').style.display = 'none';
    }

})

/* 예보 기준 */
document.querySelector('#infoStdTitle').addEventListener('mouseover',()=>{
    document.querySelector('#infoStdWrapper').style.display = 'flex';
})

document.querySelector('#infoStdTitle').addEventListener('mouseout',()=>{
    document.querySelector('#infoStdWrapper').style.display = 'none';
})

/* 화면 크기에 따른 메뉴 출력 형태 변경 */
// window.addEventListener('resize',()=>{
//     if(window.innerWidth >= 540){
//         document.querySelector('.menuWrapper').style.display = 'flex';
//         document.querySelector('.menuBtn').style.display = 'flex';
//     }
//     else if(window.innerWidth < 540) {
//         document.querySelector('.menuBtn').style.display = 'none';
//         document.querySelector('#openMenuIcon').innerText = 'menu';
//     }
// })

/* 날씨 기록 */
document.querySelector('#saveWeather').addEventListener('click',()=>{
    document.querySelector('.fvSaveWrapper').style.display = 'flex';
})

document.querySelector('#saveWeather').addEventListener('click',()=>{
    document.querySelector('.fvSaveWrapper').style.display = 'flex';
})

document.querySelector('#saveWeatherClose').addEventListener('click',()=>{
    document.querySelector('.fvSaveWrapper').style.display = 'none';
    
    document.querySelectorAll('select')[0].value = '';
    document.querySelectorAll('select')[1].value = '';
    document.querySelectorAll('select')[2].value = '';

    // document.querySelector('.tutorialInfo').style.display = 'none'; //추가
    document.querySelector('#scloseTuto ').style.display = 'none';
})

/* 피드백 */
document.querySelector('#feedback').addEventListener('click',()=>{
    document.querySelector('.feedbackWrapper').style.display = 'flex';
})

document.querySelector('#feedback').addEventListener('click',()=>{
    document.querySelector('.feedbackWrapper').style.display = 'flex';
})

document.querySelector('#feedbackClose').addEventListener('click',()=>{
    document.querySelector('.feedbackWrapper').style.display = 'none';

    const fill = document.defaultView.getComputedStyle(document.querySelector('#grade1')).getPropertyValue('font-variation-settings').split(' ')[0];
    document.querySelector('#grade1').style['font-variation-settings'] = `${fill} ${0}`;
    document.querySelector('#grade2').style['font-variation-settings'] = `${fill} ${0}`;
    document.querySelector('#grade3').style['font-variation-settings'] = `${fill} ${0}`;
    document.querySelector('#grade4').style['font-variation-settings'] = `${fill} ${0}`;
    document.querySelector('#grade5').style['font-variation-settings'] = `${fill} ${0}`;

    document.querySelector('#comment').value = '';
})

/* 별점 */
document.querySelector('#grade1').addEventListener('click',()=>{
    //font-variation-settings: 'FILL' 0;
    const fill = document.defaultView.getComputedStyle(document.querySelector('#grade1')).getPropertyValue('font-variation-settings').split(' ')[0];
    document.querySelector('#grade1').style['font-variation-settings'] = `${fill} ${1}`;
    document.querySelector('#grade2').style['font-variation-settings'] = `${fill} ${0}`;
    document.querySelector('#grade3').style['font-variation-settings'] = `${fill} ${0}`;
    document.querySelector('#grade4').style['font-variation-settings'] = `${fill} ${0}`;
    document.querySelector('#grade5').style['font-variation-settings'] = `${fill} ${0}`;
})

document.querySelector('#grade2').addEventListener('click',()=>{
    //font-variation-settings: 'FILL' 0;
    const fill = document.defaultView.getComputedStyle(document.querySelector('#grade1')).getPropertyValue('font-variation-settings').split(' ')[0];
    document.querySelector('#grade1').style['font-variation-settings'] = `${fill} ${1}`;
    document.querySelector('#grade2').style['font-variation-settings'] = `${fill} ${1}`;
    document.querySelector('#grade3').style['font-variation-settings'] = `${fill} ${0}`;
    document.querySelector('#grade4').style['font-variation-settings'] = `${fill} ${0}`;
    document.querySelector('#grade5').style['font-variation-settings'] = `${fill} ${0}`;
})

document.querySelector('#grade3').addEventListener('click',()=>{
    //font-variation-settings: 'FILL' 0;
    const fill = document.defaultView.getComputedStyle(document.querySelector('#grade1')).getPropertyValue('font-variation-settings').split(' ')[0];
    document.querySelector('#grade1').style['font-variation-settings'] = `${fill} ${1}`;
    document.querySelector('#grade2').style['font-variation-settings'] = `${fill} ${1}`;
    document.querySelector('#grade3').style['font-variation-settings'] = `${fill} ${1}`;
    document.querySelector('#grade4').style['font-variation-settings'] = `${fill} ${0}`;
    document.querySelector('#grade5').style['font-variation-settings'] = `${fill} ${0}`;
})

document.querySelector('#grade4').addEventListener('click',()=>{
    //font-variation-settings: 'FILL' 0;
    const fill = document.defaultView.getComputedStyle(document.querySelector('#grade1')).getPropertyValue('font-variation-settings').split(' ')[0];
    document.querySelector('#grade1').style['font-variation-settings'] = `${fill} ${1}`;
    document.querySelector('#grade2').style['font-variation-settings'] = `${fill} ${1}`;
    document.querySelector('#grade3').style['font-variation-settings'] = `${fill} ${1}`;
    document.querySelector('#grade4').style['font-variation-settings'] = `${fill} ${1}`;
    document.querySelector('#grade5').style['font-variation-settings'] = `${fill} ${0}`;
})

document.querySelector('#grade5').addEventListener('click',()=>{
    //font-variation-settings: 'FILL' 0;
    const fill = document.defaultView.getComputedStyle(document.querySelector('#grade1')).getPropertyValue('font-variation-settings').split(' ')[0];
    document.querySelector('#grade1').style['font-variation-settings'] = `${fill} ${1}`;
    document.querySelector('#grade2').style['font-variation-settings'] = `${fill} ${1}`;
    document.querySelector('#grade3').style['font-variation-settings'] = `${fill} ${1}`;
    document.querySelector('#grade4').style['font-variation-settings'] = `${fill} ${1}`;
    document.querySelector('#grade5').style['font-variation-settings'] = `${fill} ${1}`;
})

window.onload = function reset(){
    document.querySelector('.loadBack').style.display = "none";
};

/* 튜토리얼 */

/* 원래 요소 좌표 구하기 */
let num = 0;

let target = {};
let item = {};

let clientRect = 0;

const imgs = ["cloud.svg","cloudAndSun.svg","rain.svg","shower.svg","snow.svg","sun.svg",]
const text = ["흐림",
"구름 많음",
"비",
"소나기",
"눈",
"맑음",]

document.querySelector('#tutorial').addEventListener('click',()=>{
    const answer = confirm('튜토리얼을 보시겠습니까?');
    console.log('tuto 시작');

    if(answer){
        num = 1;
        document.querySelector('.tutorialWrapper').style.display = 'block';
        document.querySelector('.aboutTime').style.display = 'block';
        document.querySelector('#aboutitem1').style.display = 'flex';
        document.querySelector('.next').style.display = 'block';


        target = document.querySelector('#about1'); // 요소의 id 값이 target이라 가정
        console.log('about1', target);
        clientRect = target.getBoundingClientRect(); // DomRect 구하기 (각종 좌표값이 들어있는 객체)
    
        document.querySelector('#aboutitem1').style.top = `${clientRect.top}px`;
        document.querySelector('#aboutitem1').style.left = `${clientRect.left}px`;

        document.querySelector('.next').style.top = `${clientRect.top}px`;
        document.querySelector('.next').style.left = `${clientRect.left}px`;
    }
})

document.querySelector('.next').addEventListener('click',()=>{
    
    if(num===1){
        console.log('2번째');

        num = 2;
        document.querySelector('.aboutTime').style.display = 'none';
        document.querySelector('.aboutCharacter').style.display = 'block';

        
        target = document.querySelector('#about2'); // 요소의 id 값이 target이라 가정
        clientRect = target.getBoundingClientRect(); // DomRect 구하기 (각종 좌표값이 들어있는 객체)
        
        // console.log(target);

        document.querySelector('#aboutitem2').style.top = `${clientRect.top}px`;
        document.querySelector('#aboutitem2').style.left = `${clientRect.left}px`;
    
        document.querySelector('.next').style.top = `${clientRect.top - 40}px`;
        document.querySelector('.next').style.left = `${clientRect.left + 120}px`;

        document.querySelector('#aboutitem2').style.display = 'flex';

        let ind = text.indexOf(weather);
        document.querySelector('#iconText').innerText = text[ind];
        document.querySelector("#tutoicon").setAttribute('src',`${imgs[ind]}`);

    } else if(num===2){
        document.querySelector('.next').style.display = 'none';
        console.log('3번째');

        num = 3;
        document.querySelector('.aboutCharacter').style.display = 'none';
        document.querySelector('.aboutSave').style.display = 'block';
        
        target = document.querySelector('#saveWeather'); // 요소의 id 값이 target이라 가정
        clientRect = target.getBoundingClientRect(); // DomRect 구하기 (각종 좌표값이 들어있는 객체)
        
        // console.log(target);
        
        document.querySelector('#aboutitem3').style.top = `${clientRect.top}px`;
        document.querySelector('#aboutitem3').style.left = `${clientRect.left}px`;

        document.querySelector('#aboutSaveText').style.top = `${clientRect.top +16}px`;
        document.querySelector('#aboutSaveText').style.left = `${clientRect.left-170}px`;
        // document.querySelector('.next').style.top = `${clientRect.top}px`;
        // document.querySelector('.next').style.left = `${clientRect.left}px`;

        // document.querySelector('#aboutitem2').style.display = 'flex';
        // document.querySelector('#scloseTuto ').style.display = 'block';

        // 튜토리얼 마이 페이지 버튼 보이게 하기

    }
})

document.querySelector('#aboutitem3').addEventListener('click',()=>{
    // 기록지 닫기 할 때 num = 0 처리해야 함, 또한 튜토리얼 종료할 때
    // num=0;
    num = 4;

    // 단계 종료
    document.querySelector('.aboutSave').style.display = 'none';
    document.querySelector('#aboutitem3').style.display = 'none';
    document.querySelector('#aboutSaveText').style.display = 'none';


    // 새 단계 시작
    document.querySelector('.fvSaveWrapper').style.display = 'flex';
    document.querySelector('#tutoSave').style.display = 'block';


    // 튜토리얼 시 저장 할 때 마이페이지 보이게 하기
    // target = document.querySelector('#myPage'); // 요소의 id 값이 target이라 가정
    // clientRect = target.getBoundingClientRect("#about4"); // DomRect 구하기 (각종 좌표값이 들어있는 객체)

    // document.querySelector('#smyPage').style.top = `${clientRect.top}px`;
    // document.querySelector('#smyPage').style.left = `${clientRect.left}px`;


    // document.querySelector('#moveMyPage').style.top = `${clientRect.top + 68}px`;
    // document.querySelector('#moveMyPage').style.left = `${clientRect.left - 36}px`;

    // 기록 저장 가상 버튼
    target = document.querySelector('#about4'); // 요소의 id 값이 target이라 가정
    item = document.querySelector('#tutoSave');
    clientRect = target.getBoundingClientRect(); // DomRect 구하기 (각종 좌표값이 들어있는 객체)

    item.style.top = `${clientRect.top}px`;
    item.style.left = `${clientRect.left-8}px`;
})

document.querySelector('#smyPage').addEventListener('click',()=>{

    console.log('튜토리얼 마이 페이지 이동 버튼 누름');

    num = 0;

    document.querySelector('.aboutSave').style.display = 'none';
    document.querySelector('#aboutitem3').style.display = 'none';
    // document.querySelector('.tutorialWrapper').style.display = 'none';
    document.querySelector('.fvSaveWrapper').style.display = 'none';
    // document.querySelector('.tutorialInfo').style.display = 'none';
    document.querySelector('#smyPage').style.display = "none";

    // document.querySelector('#tutoSave').style.display = "none";

    // document.querySelector('#tutoSave').style.top = "px";
    // document.querySelector('#tutoSave').style.left = "px";

})

window.addEventListener('resize',(e)=>{

    if(document.querySelector(".tutorialWrapper").style.display === 'block'){
        

        console.log(num);
        if(num===1){
            target = document.querySelector('#about1'); // 요소의 id 값이 target이라 가정
            item = document.querySelector('#aboutitem1');
        } else if(num===2){
            target = document.querySelector('#about2'); // 요소의 id 값이 target이라 가정
            item = document.querySelector('#aboutitem2');
        }else if(num===3) {
            target = document.querySelector('#saveWeather');
            item = document.querySelector('#aboutitem3');
        }else if(num===4){
            target = document.querySelector('#about4');
            item = document.querySelector('#tutoSave');
        }
    
        clientRect = target.getBoundingClientRect(); // DomRect 구하기 (각종 좌표값이 들어있는 객체)
    
        // document.querySelector('#aboutitem1').style.top = `${clientRect.top}px`;
        // document.querySelector('#aboutitem1').style.left = `${clientRect.left}px`;
        item.style.top = `${clientRect.top}px`;
        item.style.left = `${clientRect.left}px`;
    
        if(num===2){
            document.querySelector('.next').style.top = `${clientRect.top-40}px`;
            document.querySelector('.next').style.left = `${clientRect.left+120}px`;
        }if(num===4){
            item.style.top = `${clientRect.top}px`;
            item.style.left = `${clientRect.left-8}px`;
        }
        else {
            document.querySelector('.next').style.top = `${clientRect.top}px`;
            document.querySelector('.next').style.left = `${clientRect.left}px`;
        }

        document.querySelector('#aboutSaveText').style.top = `${clientRect.top +16}px`;
        document.querySelector('#aboutSaveText').style.left = `${clientRect.left-170}px`;

    } else if(num===4) {
        console.log('4번 움직임');
        target = document.querySelector('#myPage');
        item = document.querySelector('#smyPage');
    

    clientRect = target.getBoundingClientRect(); // DomRect 구하기 (각종 좌표값이 들어있는 객체)

    // document.querySelector('#aboutitem1').style.top = `${clientRect.top}px`;
    // document.querySelector('#aboutitem1').style.left = `${clientRect.left}px`;
    item.style.top = `${clientRect.top}px`;
    item.style.left = `${clientRect.left}px`;

    document.querySelector('.next').style.top = `${clientRect.top}px`;
    document.querySelector('.next').style.left = `${clientRect.left}px`;

    document.querySelector('#aboutSaveText').style.top = `${clientRect.top +16}px`;
    document.querySelector('#aboutSaveText').style.left = `${clientRect.left-170}px`;

    document.querySelector('#moveMyPage').style.top = `${clientRect.top + 68}px`;
    document.querySelector('#moveMyPage').style.left = `${clientRect.left - 46}px`;
    }
    
        else {
        console.log('변화 없음');
    }
})

function move(e){

    let index = text.indexOf(document.querySelector("#iconText").innerHTML); //index

    const button = e.childNodes[1].innerText;
    if(button==='arrow_forward_ios'){
        if(index<5){
            index++;
        }else {
            index = 0;
        }
    }else{
        if(index>0){
            index--;
        }else {
            index = 5;
        }
    }

    // e.parentElement.childNodes[3].childNodes[1].setAttribute('src',`../static/${imgs[index]}`);
    document.querySelector("#tutoicon").setAttribute('src',`${imgs[index]}`);
    document.querySelector("#iconText").innerText = text[index];

    if(index===0){
        document.querySelector("#tutolove").style.transform = "translate(-70px, -16px)"
        document.querySelector(".tutoMyFvIcon").style.transform = "translate(31px, 30px)"
    }
    else if(index===2){
        document.querySelector("#tutolove").style.transform = "translate(-100px, -16px)"
        document.querySelector(".tutoMyFvIcon").style.transform = "translate(1px, 30px)"
    }
    else if(index===3){
        document.querySelector("#tutolove").style.transform = "translate(-80px, -16px)"
        document.querySelector(".tutoMyFvIcon").style.transform = "translate(21px, 30px)"
    } else {
        document.querySelector("#tutolove").style.transform = "translate(164px, -16px)"
        document.querySelector(".tutoMyFvIcon").style.transform = "translate(265px, 30px)"
    }
}

document.querySelector("#closeTuto").addEventListener("click",()=>{

    console.log('tuto close');
    num=0;
    document.querySelector('.aboutTime').style.display = 'none';
    document.querySelector('.aboutCharacter').style.display = 'none';
    document.querySelector('.aboutSave').style.display = 'none';

    document.querySelector('.tutorialWrapper').style.display = 'none';
    document.querySelector('#tempMyPage').style.display = "none";

    if(num===3){
        //저장 팝업 뜨면 저장 팝업 닫기
        //근데 저장 3단계는 버튼이랑 저장 팝업으로 나뉘는데
        //사실 저장 버튼 나오는게 마지막 페이지가 사실상이니깐
    }
})

// document.querySelector("#scloseTuto").addEventListener("click",()=>{

//     console.log('tuto close');
//     num=0;
//     // document.querySelector('.aboutTime').style.display = 'none';
//     // document.querySelector('.aboutCharacter').style.display = 'none';
//     // document.querySelector('.aboutSave').style.display = 'none';
//     // document.querySelector('.tutorialInfo').style.display = 'none';
//     // document.querySelector('#smyPage').style.display = 'none';
//     // document.querySelector('#moveMyPage').style.display = 'none';
//     document.querySelector('.fvSaveWrapper').style.display = 'none';
    
//     document.querySelectorAll('select')[0].value = '';
//     document.querySelectorAll('select')[1].value = '';
//     document.querySelectorAll('select')[2].value = '';

//     // document.querySelector('.tutorialInfo').style.display = 'none'; //추가
//     document.querySelector('#scloseTuto ').style.display = 'none';
    
//     document.querySelector('.tutorialWrapper').style.display = 'none';

// })

document.querySelector("#love").addEventListener('mouseover',()=>{
    document.querySelector(".myFvIcon").style.display = 'flex';
})


document.querySelector("#love").addEventListener('mouseout',()=>{
    document.querySelector(".myFvIcon").style.display = 'none';
})


document.querySelector("#tutolove").addEventListener('mouseover',()=>{
    document.querySelector(".tutoMyFvIcon").style.display = 'flex';
})


document.querySelector("#tutolove").addEventListener('mouseout',()=>{
    document.querySelector(".tutoMyFvIcon").style.display = 'none';
})

// document.querySelector("#smoveMypage").addEventListener("click",()=>{

//     document.querySelector('#smyPage').style.display = "flex";
//     document.querySelector('#moveMyPage').style.display = "inline";

// })

document.querySelector("#smyPage").addEventListener("click",()=>{

    console.log('tuto close');
    num=0;
    // document.querySelector('.tutorialInfo').style.display = 'none';
    document.querySelector('#smyPage').style.display = 'none';
    document.querySelector('#moveMyPage').style.display = 'none';
    document.querySelector('.fvSaveWrapper').style.display = 'none';
    
    document.querySelectorAll('select')[0].value = '';
    document.querySelectorAll('select')[1].value = '';
    document.querySelectorAll('select')[2].value = '';

    // document.querySelector('.tutorialInfo').style.display = 'none'; //추가
    document.querySelector('#scloseTuto ').style.display = 'none';
    document.querySelector('#tutoSave').style.display = 'none';
    
    document.querySelector('.tutorialWrapper').style.display = 'none';
})

// document.querySelector("#moveMyPage").addEventListener('click',()=>{
    
//     document.querySelector("#smyPageBtn").style.display = "none";
//     document.querySelector("#moveMyPage").style.display = "none";

// })

document.querySelector("#tutoSave").addEventListener('click',()=>{
    alert('가상 저장 버튼 누름');

    document.querySelector('.fvSaveWrapper').style.display = 'none';
    document.querySelector('#tutoSave').style.display = 'none';

    document.querySelector('#smyPage').style.display = "flex";
    document.querySelector('#moveMyPage').style.display = "inline";

    /* 위치 지정 */
    // 튜토리얼 내 저장버튼 누르면 마이페이지 버튼이랑 텍스트 보이게 하기
    target = document.querySelector('#myPage'); // 요소의 id 값이 target이라 가정
    clientRect = target.getBoundingClientRect(); // DomRect 구하기 (각종 좌표값이 들어있는 객체)
    item = document.querySelector('#smyPage');
    console.log(item);

    item.style.top = `${clientRect.top}px`;
    item.style.left = `${clientRect.left}px`;

    document.querySelector('#moveMyPage').style.top = `${clientRect.top + 68}px`;
    document.querySelector('#moveMyPage').style.left = `${clientRect.left - 36}px`;

})

document.querySelector('#smyPage').addEventListener('click',()=>{
    alert("임시 저장 버튼");
    // document.querySelector('#tempMyPage').style.display = "block";
    document.querySelector('.aboutAfterSave').style.display = "block";

})

const calendar = document.getElementById("calendar");
const clock = document.getElementById("clock");
const loc = document.getElementById("loc");
const icon1 = document.getElementById("icon1");
const icons = document.getElementsByClassName("icon"); //id는 유일하므로 인덱스 필요없지만, class는 여러개 일 수 있기 때문에 인덱스와 함께 사용해야함

const today = document.getElementById("today");
const tomorrow = document.getElementById("tomorrow");
const tomorrow2 = document.getElementById("tomorrow2");
const tomorrow3 = document.getElementById("tomorrow3");
const tomorrow4 = document.getElementById("tomorrow4");
const tomorrow5 = document.getElementById("tomorrow5");
const tomorrow6 = document.getElementById("tomorrow6");

const today_weather = prompt("오늘의 날씨는? 맑음 OR 흐림");

function getClock(){
   
    const day_arr = ['일','월','화','수','목','금','토'];
    const day_en_arr = ['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday'];
    const now = new Date(); //날짜, 시간 객체
    const year = now.getFullYear();
    const month = now.getMonth();
    const date = now.getDate();
    const day = now.getDay();
    let hour = now.getHours(); //24시간제

    /*12시간제*/
    if(hour==0) hour=12;
    else hour = (hour>12) ? hour%12 : hour ;
    /* */

    const min = now.getMinutes();
    const second = now.getSeconds();
    
    //console.log(day, day+1, day+2, day+3, day+4, day+5, day+6);
    

    /*clock.innerText = `${year}년 ${month+1}월 ${date}일 ${day_arr[day]}요일 
    ${hour}시 ${min}분 ${second}초`; 
    */
    calendar.innerText = `${year}년 ${month+1}월 ${date}일 ${day_arr[day]}요일`; 
    clock.innerText = `${hour}시 ${min}분 ${second}초`;
    today.innerText = `${day_en_arr[day]}`;
    tomorrow.innerText = `${day_en_arr[(day+1)>=7 ? (day+1)-7 : day+1]}`;
    tomorrow2.innerText = `${day_en_arr[(day+2)>=7 ? (day+2)-7 : day+2]}`;
    tomorrow3.innerText = `${day_en_arr[(day+3)>=7 ? (day+3)-7 : day+3]}`;
    tomorrow4.innerText = `${day_en_arr[(day+4)>=7 ? (day+4)-7 : day+4]}`;
    tomorrow5.innerText = `${day_en_arr[(day+5)>=7 ? (day+5)-7 : day+5]}`;
    tomorrow6.innerText = `${day_en_arr[(day+6)>=7 ? (day+6)-7 : day+6]}`;

    //innerText class로 할 땐 안되더니, id로 하니 됨

}

function getLoc(){
    navigator.geolocation.getCurrentPosition(function(pos) {
        const latitude = pos.coords.latitude;
        const longitude =pos.coords.longitude;
        loc.innerText = `위도 ${latitude} 경도 ${longitude}`; //주소 정보는 API 사용해야함 구글이나 카카오

        // Ajax 요청 생성하여 서버로 위치 정보를 전송
        $.ajax({
            url:"/weather",
            type:"POST",
            data:{
                latitude:latitude,
                longitude:longitude
            },
            success: function(response){
                console.log("서버로 위치 정보를 전송했습니다.");
            },
            error: function(xhr, status, error){
                console.error("서버로 위치 정보 전송하지 못하였습니다.");
            }
        });
    });
}

/* 위도,경도 -> X,Y 좌표로 바꾸기 */
function getXY(){

}

/* 위도, 경도를 이용하여 주소 받아오기 */
function getAddress(){

}

/* X,Y좌표를 이용하여 날씨정보 받아오기 */
function getWeather(){ //날씨 역시 API

}

function putIcon(){
    //const icon_arr = ['sun', 'cloud']; //변수일 때만 따옴표 없이 집어넣고, 문자열이라면 "" '' 꼭 집어넣기
    //icon1.innerHTML = `<img class="icon" src="${icon_arr[0]}.png" title="${icon_arr[0]}">`;
    /*
    Array.from(icons).forEach(item => item.innerHTML = `<img class="icon" src="${icon_arr[0]}.png" title="${icon_arr[0]}">`);
    //getElementsByClassName을 통해 가져온 값은 prototype이 HTMLCollection이므로
    //forEach와 같은 배열 메소드를 사용하려면 Array.from(배열명)으로 배열로 변환해주거나
    //querySelectorAll을 통해 요소를 가져와야 함
    */

    const w = (today_weather=='맑음') ? 'sun' : 'cloudy';
    Array.from(icons).forEach(item => item.innerHTML = `<img class="icon" src="${w}.png" title="${w}">`);

    
}

/*CSS 요소 조작하기*/
//1. jQuery
//2. html요소.style.속성 = "값";
//-하이픈 요소는 카멜 표기법으로 바꾸어서 적용
const background = document.querySelector("body");
function changeBackground(){
    //const today_weather = 'cloud'; //배열 안의 값과 마찬가지로 변수일 때만 따옴표 없이 작성되므로 꼭 문자열인지 판단해서 따옴표 집어넣기!
    if(today_weather=='맑음') {
        background.style.backgroundColor = "rgb(205, 236, 248)";
    }
    else if(today_weather=='흐림') {
        background.style.backgroundColor = 'gray';
    }
}

/*함수를 꼭 호출하기!! 그래야 실행됨*/

getClock();
getLoc();
putIcon();
changeBackground();

setInterval(getClock,1000); //초마다 시간 새로고침

 
//새로고침은 동기적이니깐, 비동기적으로 데이터 업데이트 하는 ajax 사용해볼것!           
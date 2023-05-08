// let weather = prompt('날씨를 입력하세요','sunny');
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
    wicon.setAttribute('src',`${fileName}.png`);
    // wicon.setAttribute('src',`../static/${fileName}.png`);
    // console.log(wicon);
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

function getLoc(){
    console.log('위치 시작');
    navigator.geolocation.getCurrentPosition(Sucess, Error);
    year = now.getFullYear();
    month = now.getMonth();
    date = now.getDate();
    day = now.getDay();
    hour = now.getHours(); //24시간제
    min = now.getMinutes();
    sec = now.getSeconds();

    /* 5일 요일 */
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

        function Sucess(position){
            const latitude = position.coords.latitude;
            const longitude = position.coords.longitude;

            // console.log(latitude,longitude);
            // loc.innerText = `${latitude} ${longitude}`

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
                    console.log("서버로 위치 정보를 전송했습니다.");
                },
                error: function(xhr, status, error){
                    console.error("서버로 위치 정보 전송하지 못하였습니다.");
                }
            });
        }

        function Error(position){
            console.log('위치 실패');
            document.getElementById('location').innerText = `위치 비동의`
        }
}

getLoc();
//새로고침은 동기적이니깐, 비동기적으로 데이터 업데이트 하는 ajax 사용해볼것!
Icon();

function getClock(){

    month = now.getMonth();
    date = now.getDate();
    day = now.getDay();
    hour = now.getHours(); //24시간제
    min = now.getMinutes();

    // console.log(hour, min);

    if(min<10) clock.innerText = `${month+1}월 ${date}일 ${day_arr[day]}요일 ${hour}시 0${min}분`;
    else clock.innerText = `${month+1}월 ${date}일 ${day_arr[day]}요일 ${hour}시 ${min}분`;
    
    const past = hour-1;
    if(past<0) pastHour.innerText = '11시';
    else pastHour.innerText = `${past}시`;

    const future = hour+1;
    if(future==24) futureHour.innerText = `0시`
    else futureHour.innerText = `${future}시`
}

getClock();
setInterval(getClock,1000); // 1분마다 시간 새로고침

/* 메뉴 */
document.querySelector('#openMenu').addEventListener('click',()=>{
    if(document.querySelector('.menu').style.display==='none'){
    document.querySelector('.menu').style.display = 'flex';
    document.querySelector('#openMenu').style.border = 'none';
    document.querySelector('#openMenuIcon').innerText = 'minimize';
    }
    else {
        document.querySelector('.menu').style.display = 'none';
        document.querySelector('#openMenu').style.border = 'var(--element-color) 1.5px solid';
        document.querySelector('#openMenuIcon').innerText = 'menu';
    }
})

/* 날씨 기록 */
document.querySelector('#saveWeather').addEventListener('click',()=>{
    document.querySelector('.fvSaveWrapper').style.display = 'flex';
})

document.querySelector('#saveWeather').addEventListener('click',()=>{
    document.querySelector('.fvSaveWrapper').style.display = 'flex';
})

document.querySelector('#close').addEventListener('click',()=>{
    document.querySelector('.fvSaveWrapper').style.display = 'none';
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
let weather = prompt('날씨를 입력하세요','sunny')
const wicon = document.getElementById("wicon");
const clock = document.getElementById("clock");
// const loc = document.getElementById("location");

const today = document.getElementById("today");
const tomorrow = document.getElementById("tomorrow");
const tomorrow2 = document.getElementById("tomorrow2");

function Icon(){
    wicon.setAttribute('src',`${weather}.png`);
    console.log(wicon);
}

Icon();
// setInterval(getClock,60000); //초마다 시간 새로고침

function getLoc(){
    console.log('위치 시작');
    navigator.geolocation.getCurrentPosition(Sucess, Error);

        function Sucess(position){
            const latitude = position.coords.latitude;
            const longitude = position.coords.longitude;

            console.log(latitude,longitude);
            // loc.innerText = `${latitude} ${longitude}`

            const day_arr = ['일','월','화','수','목','금','토'];
            const now = new Date(); //날짜, 시간 객체
            const year = now.getFullYear();
            const month = now.getMonth()+1;
            const date = now.getDate();
            const day = now.getDay();
            let hour = now.getHours(); //24시간제
            const min = now.getMinutes();

            clock.innerText = `${month}월 ${date}일 ${day_arr[day]}요일 \n ${hour}시 ${min}분`

            // 1개의 $ajax로 날짜,시간,위도,경도를 서버로 전송하기 위해
            // // Ajax 요청 생성하여 서버로 위치 정보를 전송
        $.ajax({
            url:"/weather",
            type:"POST",
            data:JSON.stringify({
                latitude:latitude,
                longitude:longitude,
                year:year,
                month:month,
                date:date,
                hour:hour,
                min:min
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
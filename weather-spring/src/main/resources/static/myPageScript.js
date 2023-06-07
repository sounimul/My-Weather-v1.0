/* 사용자가 좋아하는 날씨 */
let weather = document.querySelector('#userFavorite').innerText;
const wicon = document.getElementById("wicon");

function Icon(){
    switch (weather){
        case '맑음':
            fileName="fvSun";
            break;
        case '구름 많음':
            fileName="fvCloudAndSun";
            break;
        case '흐림':
            fileName="fvCloud";
            break;
        case '비':
            fileName="fvRain";
            break;
        case '눈':
            fileName="fvSnow";
            break;
        default:
            fileName="fvSun";
    }
    wicon.setAttribute('src',`../static/${fileName}.svg`);
}
Icon();

/* 날짜, 위치 받아오기 (날씨페이지 이동) */
let now = new Date(); //날짜, 시간 객체
let year = 0;
let month = 0;
let date = 0;
let hour = 0; //24시간제
let min = 0;
let sec = 0;

function getLoc(){
    now = new Date();
    console.log('위치 시작');
    navigator.geolocation.getCurrentPosition(Sucess, Error);
    year = now.getFullYear();
    month = now.getMonth();
    date = now.getDate();
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
        document.getElementById('location').innerText = `위치 비동의`
    }
}

/* 저장한 날씨 */
document.querySelector('#search').addEventListener('click',()=>{
    // alert('검색');
    location.reload();
})

/* 저장, 삭제 */
const fwList = document.querySelector('.fwList');

function createArticleItem(month, date, weather, temp, humidity, rain){
    /* article */
    const article = document.createElement("article");
    article.setAttribute('class', 'fwItem');

    /* header */
    const header = document.createElement("header");
    header.setAttribute('class','fwHeader');
    article.appendChild(header);

        /* header h4 */
        const h4= document.createElement("h4");
        const fwDay = document.createTextNode(`${month}월 ${date}일`);
        h4.appendChild(fwDay);
        header.appendChild(h4);

        /* header span */
        const span= document.createElement("span");
        const fWeather = document.createTextNode(weather);
        span.setAttribute('class','material-symbols-outlined');
        span.appendChild(fWeather);
        header.appendChild(span);

    /* div .fwDetails */
    const fwDetails = document.createElement("div");
    fwDetails.setAttribute('class','fwDetails');
    article.appendChild(fwDetails);

        /* p .fwTemp */
        const fwTemp = document.createElement("p");
        fwTemp.setAttribute('class','fwTemp');
        fwDetails.appendChild(fwTemp);
            /* span icon */
            const tempIcon= document.createElement("span");
            tempIcon.setAttribute('class','material-symbols-outlined');
            const tempIconText = document.createTextNode("device_thermostat");
            tempIcon.appendChild(tempIconText);
            fwTemp.appendChild(tempIcon);
            /* span value */
            const tempValue= document.createElement("span");
            const tempValueText = document.createTextNode(`${temp}°C`);
            tempValue.appendChild(tempValueText);
            fwTemp.appendChild(tempValue);

        /* p .fwHumidity */
        const fwHumidity = document.createElement("p");
        fwHumidity.setAttribute('class','fwHumidity');
        fwDetails.appendChild(fwHumidity);
        
            /* span icon */
            const humidityIcon= document.createElement("span");
            humidityIcon.setAttribute('class','material-symbols-outlined');
            const humidityIconText = document.createTextNode("water_drop");
            humidityIcon.appendChild(humidityIconText);
            fwHumidity.appendChild(humidityIcon);
            /* span value */
            const humidityValue= document.createElement("span");
            const humidityValueText = document.createTextNode(`${humidity}%`);
            humidityValue.appendChild(humidityValueText);
            fwHumidity.appendChild(humidityValue);

        /* p .fwRain */
        const fwRain = document.createElement("p");
        fwRain.setAttribute('class','fwRain');
        fwDetails.appendChild(fwRain);

            /* span icon */
            const rainIcon= document.createElement("span");
            rainIcon.setAttribute('class','material-symbols-outlined');
            const rainIconText = document.createTextNode("rainy");
            rainIcon.appendChild(rainIconText);
            fwRain.appendChild(rainIcon);
            /* span value */
            const rainValue= document.createElement("span");
            const rainValueText = document.createTextNode(`${rain}mm`);
            rainValue.appendChild(rainValueText);
            fwRain.appendChild(rainValue);

    return article;
}

/* 프로필 수정 */
document.querySelector('#changeProfile').addEventListener('click',()=>{
    document.querySelector('#changePFbackground').style.display = 'flex'
})

document.querySelector('#close').addEventListener('click',()=>{
    document.querySelector('#changePFbackground').style.display = 'none'
})

/* 좋아하는 날씨 변경 */
document.querySelector('#fw').addEventListener('change',()=>{
    document.querySelector('#currentFw').innerText = document.querySelector('#fw').value;
})

/* 조건 새로고침 */
document.querySelector('#refresh').addEventListener('click',()=>{
    document.querySelector('#sTemp').value = -1;
    document.querySelector('#sHumid').value = -1;
    document.querySelector('#sRain').value = -1;
})

function confirmDelete(c){
    const answer = confirm('정말 삭제하시겠습니까?'); //true, false
    
    if(answer){ //삭제 O

    }else { //삭제 X

    }

    // c.parentElement.parentElement.parentElement.style.display = 'none'; //팝업 닫기
}

// item expand
function expand(e){

    let expand = e.childNodes[3].style.display;

    if(expand === 'none' || expand === ''){
        e.childNodes[3].style.display = 'flex';
        e.childNodes[1].childNodes[11].childNodes[1].innerText = 'expand_less';
        e.style.height = '70px';
    }else {
        e.childNodes[3].style.display = 'none';
        e.childNodes[1].childNodes[11].childNodes[1].innerText = 'expand_more';
        e.style.height = '40px';
    }

}

/* delete confirm */
function confirmDelete(e){
    
    const uuid = e.parentElement.parentElement.childNodes[3].childNodes[11].innerText;
    const rdate = e.parentElement.parentElement.childNodes[3].childNodes[3].innerText;

    console.log("uuid와 rdate");
    console.log(uuid);
    console.log(rdate);

    const answer = confirm('정말 삭제하시겠습니까?'); //true, false
    
    if(answer){ //삭제 O
        $.ajax({
            url:"/deleteRecord",
            type:"DELETE",
            data:{
                uuid:uuid,
                rdate:rdate
            },
            success:function (data){
                location.replace("/myPage");
                console.log("서버로 날씨 기록 정보를 전송했습니다.");
            },
            error:function(xhr, status, error){
                console.error("서버로 날씨 기록 정보를 전송하지 못하였습니다.");
            }
        });
    }else { //삭제 X

    }

    // e.parentElement.parentElement.parentElement.style.display = 'none'; //팝업 닫기
}
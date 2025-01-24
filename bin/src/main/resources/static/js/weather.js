var RE = 6371.00877; // 지구 반경(km)
var GRID = 5.0; // 격자 간격(km)
var SLAT1 = 30.0; // 투영 위도1(degree)
var SLAT2 = 60.0; // 투영 위도2(degree)
var OLON = 126.0; // 기준점 경도(degree)
var OLAT = 38.0; // 기준점 위도(degree)
var XO = 43; // 기준점 X좌표(GRID)
var YO = 136; // 기1준점 Y좌표(GRID)

function dfs_xy_conv(v1, v2) {
    var DEGRAD = Math.PI / 180.0;
    var RADDEG = 180.0 / Math.PI;

    var re = RE / GRID;
    var slat1 = SLAT1 * DEGRAD;
    var slat2 = SLAT2 * DEGRAD;
    var olon = OLON * DEGRAD;
    var olat = OLAT * DEGRAD;

    var sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
    sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
    var sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
    sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
    var ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
    ro = re * sf / Math.pow(ro, sn);
    var rs = {};
        rs['lat'] = v1;
        rs['lng'] = v2;
        var ra = Math.tan(Math.PI * 0.25 + (v1) * DEGRAD * 0.5);
        ra = re * sf / Math.pow(ra, sn);
        var theta = v2 * DEGRAD - olon;
        if (theta > Math.PI) theta -= 2.0 * Math.PI;
        if (theta < -Math.PI) theta += 2.0 * Math.PI;
        theta *= sn;
        rs['x'] = Math.floor(ra * Math.sin(theta) + XO + 0.5);
        rs['y'] = Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);
    return rs;
}

    const searchButton = document.getElementById("searchButton")
	if(searchButton){
		searchButton.addEventListener("click", setXY);
	}

export function setXY() {
    return new Promise((resolve)=>{
        const select = document.getElementById("localSelect");
		const success = document.getElementById("s");
        let x = 0;
        let y = 0;
        let area = null;
        if(select !== null){
            area = select.value;

            switch(area){
                case "서울" :
                    x = 37.5665851;
                    y = 126.9782038;
                    break;
                case "인천" :
                    x = 37.4562557;
                    y = 126.7052062;
                    break;
                case "광주" :
                    x = 35.1599785;
                    y = 126.8513072;
                    break;
                case "세종" :
                    x = 36.4799919;
                    y = 127.2890511;
                    break;
                case "대전" :
                    x = 36.3504567;
                    y = 127.3848187;
                    break;
                case "대구" :
                    x = 35.8715411;
                    y = 128.601505;
                    break;
                case "부산" :
                    x = 35.179665;
                    y = 129.0747635;
                    break;
                case "울산" :
                    x = 35.5396224;
                    y = 129.3115276;
                    break;
                case "제주" :
                    x = 33.4998066;
                    y = 126.531359;
                    break;                
            }
        }		
        else {
            let area = "현재 위치";
            x = localStorage.getItem('lat');
            y = localStorage.getItem('lon');
        }

    const local = "";
    const result = dfs_xy_conv(x, y)
    const nx = result.x;
    const ny = result.y;

    const serviceKey = "0DhhJECJFBVCM3fY8j%2F0VqhcCvyjt%2FCtI%2F2lerNvBUEsB192uQPRIDzvpfJc%2B5SntP52r8kPxPem58kgRitkRw%3D%3D";

    let now = new Date();

    let year = String(now.getFullYear());
    let month = String(now.getMonth() + 1).padStart(2, "0");
    let day = String(now.getDate()).padStart(2, "0");
    const date = year + month + day;

    const nowHour = now.getHours();

    const url = `https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=${serviceKey}&pageNo=1&numOfRows=1000&dataType=JSON&base_date=${date}&base_time=0500&nx=${nx}&ny=${ny}`;
    fetch(url)
        .then(response => response.json())
        .then(data => {
            const loc = data.response.body.items.item;

            const container = document.getElementById("container");
            if(container !== null){
                container.innerHTML = "";
            }

            let cnt = 0;
            for(let num = 0; num < 43; num++){
                for(let i = 0; i < 12; i++){
                    if(loc[((num)*12)+i].category == 'TMX' || loc[((num)*12)+i].category == 'TMN'){
                        cnt++;
                    }
                }
                const wthTime = loc[0 + (num * 12) + cnt].fcstTime; // 현재 시간
                const tem = loc[0 + (num * 12) + cnt].fcstValue; // 현재 기온
                const wind = loc[4 + (num * 12) + cnt].fcstValue; // 현재 풍속
                const cloud = loc[5 + (num * 12) + cnt].fcstValue; // 현재 하늘 상태
                const pty = loc[6 + (num * 12) + cnt].fcstValue; // 현재 날씨
                const pop = loc[7 + (num * 12) + cnt].fcstValue; // 현재 강수 확률
                const reh = loc[10 + (num * 12) + cnt].fcstValue; // 현재 습도

                // 현재 시간
                let hour = wthTime.slice(0, 2);
                const nowTime = hour + "시";

                // 현재 날씨
                let w0 = false; // 없음
                let w1 = false; // 비
                let w2 = false; // 비/눈
                let w3 = false; // 눈
                let w4 = false; // 소나기
                switch(pty){
                    case '0' : w0 = true; break;
                    case '1' : w1 = true; break;
                    case '2' : w2 = true; break;
                    case '3' : w3 = true; break;
                    case '4' : w4 = true; break;
                }

                // 현재 하늘 상태
                let s1 = false; // 맑음
                let s3 = false; // 구름 많음
                let s4 = false; // 흐림
                switch(cloud){
                    case '1' : s1 = true; break;
                    case '3' : s3 = true; break;
                    case '4' : s4 = true; break;
                }

                // 종합 날씨
                let wth = "";
                let imagePath = "";
                if(w0 === true && s1 === true){
                    wth = "맑음";
                    if(Number(hour) < 18 && Number(hour) >= 6) {
                        imagePath = "/image/weather1.png";
                    }
                    else {
                        imagePath = "/image/weather14.png";
                    }
                }
                else if(w0 === true && s3 === true){
                    wth = "구름 많음";
                    imagePath = "/image/weather4.png";
                }
                else if(w0 === true && s4 === true){
                    wth = "흐림";
                    imagePath = "/image/weather3.png";
                }
                else if(w1 === true && s1 === true){
                    wth = "맑고 비";
                    imagePath = "/image/weather10.png";
                }
                else if(w1 === true && s3 === true){
                    wth = "구름 많고 비";
                    imagePath = "/image/weather9.png";
                }
                else if(w1 === true && s4 === true){
                    wth = "흐리고 비";
                    imagePath = "/image/weather6.png";
                }
                else if(w2 === true && s1 === true){
                    wth = "맑고 비/눈";
                    imagePath = "/image/weather16.png";
                }
                else if(w2 === true && s3 === true){
                    wth = "구름 많고 비/눈";
                    imagePath = "/image/weather16.png";
                }
                else if(w2 === true && s4 === true){
                    wth = "흐리고 비/눈";
                    imagePath = "/image/weather16.png";
                }
                else if(w3 === true && s1 === true){
                    wth = "맑고 눈";
                    imagePath = "/image/weather7.png";
                }
                else if(w3 === true && s3 === true){
                    wth = "구름 많고 눈";
                    imagePath = "/image/weather7.png";
                }
                else if(w3 === true && s4 === true){
                    wth = "흐리고 눈";
                    imagePath = "/image/weather7.png";
                }
                else if(w4 === true && s1 === true){
                    wth = "맑고 소나기";
                    imagePath = "/image/weather12.png";
                }
                else if(w4 === true && s3 === true){
                    wth = "구름 많고 소나기";
                    imagePath = "/image/weather12.png";
                }
                else if(w4 === true && s4 === true){
                    wth = "흐리고 소나기";
                    imagePath = "/image/weather12.png";
                }

                // 현재 날씨
                if(nowHour == Number(hour) && num <= 18 && area !== null){
                    const nowWeather = document.getElementById("nowWeather");

                    // 날씨 이미지
                    nowWeather.style.backgroundImage = `url(${imagePath})`;
                    nowWeather.style.backgroundPosition = "10px -20px";
                    nowWeather.style.backgroundSize = "80% 80%";
                    nowWeather.style.backgroundRepeat = "no-repeat";

                    nowWeather.innerHTML = `
                        <div class="nowLocal">${area}</div>
                        <div class="nowTem">${tem}℃</div>
                        <div class="nowSky">${wth}</div>
                        <div class="nowInfo">습도 : ${reh}%</div>
                        <div class="nowInfo">강수확률 : ${pop}%</div>
                    `;
                }
                else if(nowHour == Number(hour) && num <= 18 && area === null){
                    localStorage.setItem('tem', tem);
                    localStorage.setItem('wth', wth);
                }
                // 전체 날씨
                
                if(num >= nowHour - 6 && area !== null) {
                    const div = document.createElement("div");
                    div.id = "timeWeather";

                    // 날씨 이미지
                    div.style.backgroundImage = `url(${imagePath})`;
                    div.style.backgroundPosition = "left -10px";
                    div.style.backgroundSize = "80% 80%";
                    div.style.backgroundRepeat = "no-repeat";

                    div.innerHTML = `
                            <div class="tem">${tem}℃</div>
                            <div class="info">${nowTime}<br>습도 : ${reh}%</div>
                        `;
                    container.appendChild(div);

                    if(num == 18){
                        const tomorrow = document.createElement("div");
                        tomorrow.innerHTML = `
                            <div class="tomorrow">내일</div>
                        `;
                        container.appendChild(tomorrow);
                    }
                }
            }
            resolve();
        })
    });
}
setXY();

const container = document.getElementById("container");
const leftB = document.querySelector(".left");
const rightB = document.querySelector(".right");
if(leftB !== null){
    leftB.addEventListener("click", ()=>{
        container.scrollBy({
            left: -400,
            behavior: 'smooth'
        })
    });
    rightB.addEventListener("click", ()=>{
        container.scrollBy({
            left: 400,
            behavior: 'smooth'
        })
    });
}
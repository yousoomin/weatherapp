import { setXY } from './weather.js';
var mapContainer = document.getElementById('map'),
    mapOption = { 
        center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도 중앙 위치
        level: 10
    };

var map = new kakao.maps.Map(mapContainer, mapOption);

// 마커와 오버레이들을 저장할 배열
const markers = [];

// HTML5의 geolocation으로 현재 위치 가져오기
if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(async function(position) {
        var lat = position.coords.latitude,
            lon = position.coords.longitude;

            localStorage.setItem('lat', lat);
            localStorage.setItem('lon', lon);
            
            await setXY();

        var locPosition = new kakao.maps.LatLng(lat, lon), // 현재위치 출력값
            message = `<div style="padding:5px;">현재 위치<br>기온: ${localStorage.getItem("tem")}℃ ${localStorage.getItem("wth")}</div>`;

        document.getElementById("XY").textContent = `${lat}, ${lon}`;
        displayMarker(locPosition, message);
    });
} else {
    var locPosition = new kakao.maps.LatLng(33.450701, 126.570667),
        message = 'geolocation을 사용할 수 없어요.';

    document.getElementById("XY").textContent = '위치 정보를 가져올 수 없습니다.';
    displayMarker(locPosition, message);
}

// 지도에 마커와 인포윈도우를 표시하는 함수
function displayMarker(locPosition, message) {
    var marker = new kakao.maps.Marker({  
        map: map, 
        position: locPosition
    });

    // 인포윈도우 표시
    var infowindow = new kakao.maps.InfoWindow({
        content : message,
        removable : true
    });
    infowindow.open(map, marker);
    map.setCenter(locPosition);

    // 마커와 인포윈도우를 배열에 저장
    markers.push({ marker, infowindow });
}

// 클릭 이벤트로 마커 추가 및 위경도 표시
let i = 0;
kakao.maps.event.addListener(map, 'click', async function(mouseEvent) {
    
    const latlng = mouseEvent.latLng;
    const lat = latlng.getLat();
    const lng = latlng.getLng();

    localStorage.setItem('lat', lat);
    localStorage.setItem('lon', lng);
    await setXY();

    let tem = localStorage.getItem("tem");
    let wth = localStorage.getItem("wth");
    
    document.getElementById('latitude').textContent = lat;
    document.getElementById('longitude').textContent = lng;

    const markerText = document.getElementById('markerText').value || `<a href="/map/localWeathe?s=s">기온: ${tem}℃ ${wth}</a>`;

    const marker = new kakao.maps.Marker({
        position: latlng,
        map: map
    });

    // 커스텀 오버레이 생성
    const overlayContent = `
        <div style="
            padding:5px; 
            background:white; 
            border:1px solid #ddd; 
            border-radius:3px; 
            font-size:12px; 
            box-shadow: 0px 1px 2px rgba(0, 0, 0, 0.1);
            transform: translateY(-100%);
            white-space: nowrap;">
            ${markerText}
        </div>
    `;
    
    const customOverlay = new kakao.maps.CustomOverlay({
        position: latlng,
        content: overlayContent,
        yAnchor: 1.5
    });

    customOverlay.setMap(map);

    // 마커와 오버레이를 배열에 저장
    markers.push({ marker, customOverlay });

    i++;
    if(i > 1){
        markers[i-1].marker.setMap(null); // 마커 제거
        markers[i-1].customOverlay && markers[i-1].customOverlay.setMap(null); // 오버레이 제거
        markers[i-1].infowindow && markers[i-1].infowindow.close(); // 인포윈도우 닫기
    }
});
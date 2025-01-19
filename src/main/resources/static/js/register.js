
// 사용자 생년월일 입력시 자동 포맷  ex) 19960928 => 1996-09-26
document.querySelector('#birth').addEventListener('input', function(event) {
    let value = event.target.value;
    console.log(value)
    if (value.length === 8) {
        // 8자리 입력을 받으면 "yyyyMMdd"에서 "yyyy.MM.dd" 형식으로 변환
        let formatted = value.replace(/(\d{4})(\d{2})(\d{2})/, '$1-$2-$3');
        event.target.value = formatted;
    }
});



document.querySelector("#valiDate").addEventListener("click", (e) =>{
    const userId = document.querySelector("#userId").value;
    if(!userId){
        return;
    }
    e.preventDefault();
    fetch("http://localhost:8080/user/validateUserId", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'  // 요청 헤더에 JSON 형식 명시
        },
        body: JSON.stringify({userId : userId})  // 사용자 아이디를 JSON 형식으로 전달
    })
        .then(res => res.json())
        .then(result => {
            console.log(result)
            console.log(result.status)
            const validateButton = document.querySelector("#valiDate");
            console.log(result)
            if(result.status === "available"){
                validateButton.innerText = '검사 완료'
                validateButton.style.backgroundColor = 'green'
            }else{
                validateButton.innerText = '검사 실패'
                validateButton.style.backgroundColor = 'red';
            }
        }).catch(error =>{
            console.log("error" , error)
    })

})

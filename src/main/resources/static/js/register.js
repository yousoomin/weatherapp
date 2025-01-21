
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



// document.querySelector("#valiDate").addEventListener("click", (e) =>{
//     const userId = document.querySelector("#userId").value;
//     if(!userId){
//         return;
//     }
//     e.preventDefault();
//     fetch("http://localhost:8080/user/validateUserId", {
//         method: 'POST',
//         headers: {
//             'Content-Type': 'application/json'  // 요청 헤더에 JSON 형식 명시
//         },
//         body: JSON.stringify({userId : userId})  // 사용자 아이디를 JSON 형식으로 전달
//     })
//         .then(res => res.json())
//         .then(result => {
//             const validateButton = document.querySelector("#valiDate");
//             if(result.status === "available"){
//                 validateButton.innerText = '검사 완료'
//                 validateButton.style.backgroundColor = 'green'
//             }else{
//                 validateButton.innerText = '검사 실패'
//                 validateButton.style.backgroundColor = 'red';
//             }
//         }).catch(error =>{
//             console.log("error" , error)
//     })
//
// })

const validationRules = {
    userId: {
        regex: /^[a-zA-Z][a-zA-Z0-9]{5,20}$/, // 아이디: 영문, 숫자 4~12자
        errorMessage: "아이디 : 영문, 숫자로 5~20자여야 합니다.",
    },
    password: {
        regex: /^(?=.*[a-z])(?=.*\d)(?=.*[!@#$%^&*()_+])[A-Za-z\d!@#$%^&*()_+]{8,20}$/, // 비밀번호
        errorMessage: "비밀번호 : 8~16자의 영문 대/소문자, 숫자, 특수문자를 사용해 주세요.",
    },
    confirmPassword: {
        match: "password", // 비밀번호 확인은 password와 값이 같아야 함
        errorMessage: "비밀번호가 일치하지 않습니다.",
    },
    name: {
        regex: /^[가-힣]{2,5}$/, // 이름: 한글 2~5자
        errorMessage: "이름은 한글 2~5자여야 합니다.",
    },
    phone: {
        regex: /^01[016789]-\d{3,4}-\d{4}$/, // 전화번호
        errorMessage: "유효한 전화번호 형식이 아닙니다. (예: 010-1234-5678)",
    },
};


const errorState = {};

function updateErrorMessages() {
    const errorList = document.getElementById('errorList');
    errorList.innerHTML = ''; // 기존 에러 메시지 초기화

    // 모든 에러 메시지를 순회하면서 표시
    Object.values(errorState).forEach(error => {
        if (error) {
            const li = document.createElement('li');
            li.textContent = error;
            errorList.appendChild(li);
        }
    });
}

const allInput = document.querySelectorAll("input");

allInput.forEach(input => input.addEventListener("blur" , (e) =>{
    e.preventDefault();
    const fieldName = e.target.name
    const fieldValue = e.target.value;
    const fieldRule = validationRules[fieldName]

    if(fieldRule){
        errorState[fieldName] = null;
        if(fieldRule.regex && !fieldRule.regex.test(fieldValue)){
            errorState[fieldName] = fieldRule.errorMessage;
        }else if(fieldRule.match){
            const originValue = document.querySelector(`input[name=${fieldRule.match}]`).value;
            if(fieldValue !== originValue){
                errorState[fieldName] = fieldRule.errorMessage;
            }
        }
        updateErrorMessages()
        if (errorState[fieldName]) {
            input.style.border = '1px solid red';
        } else {
            input.style.border = '1px solid #ccc';
        }
    }
}))



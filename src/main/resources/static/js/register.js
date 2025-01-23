const validationRules = {
    userId: {
        regex: /^[a-zA-Z][a-zA-Z0-9]{5,20}$/, // 아이디: 영문, 숫자 4~12자
        errorMessage: "아이디 : 영문, 숫자로 5~20자여야 합니다.",
        validateErrorMessage : "아이디 : 중복된 아이디가 존재합니다."
    },
    password: {
        regex: /^(?=.*[a-z])(?=.*\d)(?=.*[!@#$%^&*()_+])[A-Za-z\d!@#$%^&*()_+]{8,20}$/, // 비밀번호
        errorMessage: "비밀번호 : 8~16자의 영문 대/소문자, 숫자, 특수문자를 사용해 주세요.",
    },
    confirmPassword: {
        match: "password", // 비밀번호 확인은 password와 값이 같아야 함
        errorMessage: "비밀번호 : 비밀번호가 일치하지 않습니다.",
    },
    email : {
        regex : /^([a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,})?$/,
        errorMessage : "이메일 형식을 올바르게 입력해주세요."
    },
    name: {
        regex: /^[가-힣]{2,5}$/, // 이름: 한글 2~5자
        errorMessage: "이름 : 이름은 한글 2~5자여야 합니다.",
    },
    birth : {
        regex : /^\d{4}-\d{2}-\d{2}$/,
        errorMessage : "생년월일 : 생년월일을 올바르게 입력해주세요"
    },
    gender : {
        required: true,
        errorMessage: "성별을 선택해주세요."
    },
    phone: {
        regex: /^01[016789]-\d{3,4}-\d{4}$/, // 전화번호
        errorMessage: "전화번호는 필수 입력 항목입니다. 010-1234-5678 형식으로 입력해주세요."
    },

};


// 사용자 생년월일 입력시 자동 포맷  ex) 19960928 => 1996-09-26
document.querySelector('#birth').addEventListener('input', function(event) {
    let value = event.target.value;
    if (value.length === 8) {
        // 8자리 입력을 받으면 "yyyyMMdd"에서 "yyyy.MM.dd" 형식으로 변환
        let formatted = value.replace(/(\d{4})(\d{2})(\d{2})/, '$1-$2-$3');
        event.target.value = formatted;
    }
});


const errorState = {};

function updateErrorMessages(errorState) {
    const errorList = document.getElementById('errorList');
    const errorList2 = document.getElementById('errorList2');
    errorList.innerHTML = ''; // 기존 에러 메시지 초기화
    errorList2.innerHTML = '';
    // 모든 에러 메시지를 순회하면서 표시
    Object.keys(errorState).forEach(errorField => {
        if (errorField) {
            const li = document.createElement('li');
            li.textContent = errorState[errorField]
            if (errorField === 'userId' || errorField === 'password' || errorField === 'confirmPassword' || errorField === 'email' ){
                errorList.appendChild(li);
            }else{
                errorList2.appendChild(li)
            }
        }
    });
}

const allInput = document.querySelectorAll("input");

allInput.forEach(input => input.addEventListener("blur" , async (e) =>{
    console.log("Blur event triggered"); // 추가
    e.preventDefault();
    const fieldName = e.target.name
    const fieldValue = e.target.value;
    const fieldRule = validationRules[fieldName]

    // errorState 객체에 에러가 발생한 필드이름과 에러메시지를 저장.
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
        // 필드 이름이 userId 이고 에러 객체에 저장된 에러가 없다면 아이디 중복 검사 진행
        if(fieldName === "userId" && !errorState[fieldName]){
            try{
                const response = await fetch('/user/validateUserId' , {
                    method : "POST",
                    headers : {
                        "Content-Type" : "application/json",
                    },
                    body : JSON.stringify({userId : fieldValue })
                });

                const result = await response.json();
                if(result.status === 'unavailable'){
                    errorState[fieldName] = fieldRule.validateErrorMessage
                }
            }catch (error){
                console.log(error)
            }
        }
        updateErrorMessages(errorState)

        if (errorState[fieldName]) {
            input.style.border = '1px solid red';
        } else {
            input.style.border = '1px solid #ccc';
        }
    }
}))


document.querySelector("#registerForm").addEventListener("submit" , (e)=>{
    e.preventDefault();
    const form = e.target
    const formData = new FormData(form)


    fetch("/user/register" , {
        method : "POST",
        body : formData
    })
        .then(res =>{
            if(!res.ok){
                return res.json()
                    .then(errorDetails =>{
                        Object.keys(errorDetails).forEach(field =>{
                            errorState[field] = errorDetails[field]
                        })
                        updateErrorMessages(errorState);

                        Object.keys(errorDetails).forEach(field => {
                            const input = document.querySelector(`input[name="${field}"]`);
                            if (input) {
                                input.style.border = '1px solid red';
                            }
                        });
                    });
            }
            return res.json();
        })
        .then(result => {
            if(result && result.status === 'success'){
                console.log('회원가입 성공')
            }
        }).catch(error =>{
            console.error("Error:" , error)
    })
})


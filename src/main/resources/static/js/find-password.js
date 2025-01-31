const findPassBtn = document.querySelector(".findPassBtn")
const resetPassBtn = document.querySelector(".resetPassBtn")

findPassBtn.addEventListener("click" , async (e) =>{
    e.preventDefault();
    const emailValue = document.querySelector("#email").value.trim();
    const response = await fetch("/api/password/reset" , {
        method : 'POST',
        headers : {
            'Content-Type' : 'application/json'
        },
        body : JSON.stringify({ email : emailValue })
    })

    const message =  document.querySelector(".reset-message")
    if(response.ok){
        const data = await response.text();
        message.textContent = data;
    }else{
        const data = await response.text();
        message.textContent = data;
    }
})


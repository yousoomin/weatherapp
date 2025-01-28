const findPassBtn = document.querySelector(".findPassBtn")

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


    if(response.ok){
        const message =  document.querySelector(".reset-message")
        const data = await response.text();
        message.textContent = data;
    }
})
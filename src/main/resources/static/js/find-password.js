const findPassBtn = document.querySelector(".findPassBtn")

findPassBtn.addEventListener("click" , async (e) =>{
    e.preventDefault();
    const emailValue = document.querySelector("#email").value.trim();
    const response = await fetch("/api/password/request" , {
        method : 'POST',
        headers : {
            'Content-Type' : 'application/json'
        },
        body : JSON.stringify({ email : emailValue })
    })

    console.log(response)
})
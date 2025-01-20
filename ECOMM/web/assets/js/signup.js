async function signUp() {

    const user_dto = {

        first_name: document.getElementById("first_name").value,
        last_name: document.getElementById("last_name").value,
        email: document.getElementById("email").value,
        password: document.getElementById("password").value 

    };
    
    //console.log(user_dto);
    const response = await fetch("SignUp",
            {
                method: "POST",
                body: JSON.stringify(user_dto),
                headers: {
                    "Content-Type": "application/json"
                }
            }
    );

    if (response.ok) {
        const json = await response.json();
        console.log(json);

        if (json.success) {
            window.location = "verifyAccount.html";
        } else {
            document.getElementById("message").innerHTML = json.content;
        }
    } else {
        document.getElementById("message").innerHTML = "Please try again later !";

    }
}




async function check() {


    const response = await fetch("CheckSignIn"); 

    if (response.ok) {

        const json = await response.json();

        const response_dto = json.response_dto;
         
        if (response_dto.success) { 

            const user = response_dto.content; 

            let st_button_1 = document.getElementById("st-button-1");
            let st_a_1 = document.getElementById("st-a-1");

            st_button_1.remove();
            st_a_1.remove(); 

            let sign_out = document.getElementById("sign-out"); 
            sign_out.innerHTML = '<i class="bi bi-box-arrow-right text-end ms-5 text-danger fs-3 fw-bold"></i>';
 

        } else {
            //not signed in 


        }
       
    }
}
 

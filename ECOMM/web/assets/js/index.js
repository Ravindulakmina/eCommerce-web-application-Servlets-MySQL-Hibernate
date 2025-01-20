async function checkSignIn() {


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
        //display products
        let productHtml = document.getElementById("al-product");
        document.getElementById("al-product-main").innerHTML = "";
 
        json.productList.forEach(product => {
            
            let productCloneHtml = productHtml.cloneNode(true);
 
            
            productCloneHtml.querySelector("#product-image").src = "product-images/" + product.id + "/image1.png";
            productCloneHtml.querySelector("#st-product-link").href = "singleProductView.html?id=" + product.id;
            productCloneHtml.querySelector("#product-title").innerHTML = product.tital;
            productCloneHtml.querySelector("#product-size").innerHTML = product.size.size;
            productCloneHtml.querySelector("#product-price").innerHTML = product.price;

            productCloneHtml.querySelector("#product-color").innerHTML = product.color.name;

            productCloneHtml.querySelector("#add-to-cart-sub").addEventListener("click",
                    (e) => {
                addToCart(product.id, 1);
                e.preventDefault();
            });

            document.getElementById("al-product-main").appendChild(productCloneHtml);

        });
    }
}

async function viewCart() {

    const response = await fetch("cart.html");

    if (response.ok) {

        const cartHtmlText = await response.text();

        const parser = new DOMParser();
        const cartHtml = parser.parseFromString(cartHtmlText, "text/html");

        const cart_main = cartHtml.querySelector("#cart-content");

        document.getElementById("index-main").innerHTML = cart_main.innerHTML;

        loadCartItems();

    }
}


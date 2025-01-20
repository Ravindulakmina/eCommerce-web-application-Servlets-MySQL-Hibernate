async function loadProducts() {

    const paramters = new URLSearchParams(window.location.search);

    if (paramters.has("id")) {

        const productId = paramters.get("id");

        const response = await fetch("LoadSingleProducts?id=" + productId);

        if (response.ok) {

            const json = await response.json(); 

            const id = json.product.id;
            
            document.getElementById("image1").src = "product-images/" + id + "/image1.png";  

            document.getElementById("product-title").innerHTML = json.product.tital;
            document.getElementById("product-price").innerHTML = "Rs. " + json.product.price;
            document.getElementById("product-published-date").innerHTML = json.product.datetime; 
            document.getElementById("product-brand").innerHTML = json.product.brand.name; 
            document.getElementById("product-quantity").innerHTML = json.product.qty;

            document.getElementById("product-color").innerHTML = json.product.color.name; 

            document.getElementById("product-size").innerHTML = json.product.size.size;
            document.getElementById("product-description").innerHTML = json.product.description;

            document.getElementById("add-to-cart-main").addEventListener("click",
                    (e) => {
                addToCart(id, document.getElementById("add-to-cart-qty").value);
                e.preventDefault();
            });

            let productHtml = document.getElementById("similar-product");
            document.getElementById("similar-product-main").innerHTML = "";

            json.productList.forEach(item => {

                let productCloneHtml = productHtml.cloneNode(true);

                productCloneHtml.querySelector("#similar-product-image").src = "product-images/" + item.id + "/image1.png";
                productCloneHtml.querySelector("#similar-product-a1").href = "singleProductView.html?id=" + item.id; 
                productCloneHtml.querySelector("#similar-product-title").innerHTML = item.tital;
                productCloneHtml.querySelector("#similar-product-size").innerHTML = item.size.size;
                productCloneHtml.querySelector("#similar-product-price").innerHTML = "Rs. " + item.price;
 
                productCloneHtml.querySelector("#similar-product-color").innerHTML = item.color.name;

                productCloneHtml.querySelector("#add-to-cart-sub").addEventListener("click",
                        (e) => {
                    addToCart(item.id, 1);
                    e.preventDefault();
                });

                document.getElementById("similar-product-main").appendChild(productCloneHtml);

            }); 
             

        } else {
            window.location = "index.html";
        }
    } else {
        window.location = "index.html";
    }
}

async function addToCart(id, qty) {

    const response = await fetch(
            "AddToCart?id=" + id + "&qty=" + qty
            );

    if (response.ok) {


        const json = await response.json();
        const popup = new Notification();

        if (json.success) {

            popup.success({
                message: json.content
            });

        } else {
            popup.error({
                message: json.content
            });
        }
        
        console.log(response);

    } else {

        popup.error({
            message: "Unable to processs your request"
        });
    }
}


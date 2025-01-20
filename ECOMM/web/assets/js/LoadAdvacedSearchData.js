async function LoadAdvacedSearchData() {


    const response = await fetch("LoadAdvacedSearchData"); 

    if (response.ok) {

        const json = await response.json();

        const response_dto = json.response_dto; 
        
        //display products
        let productHtml = document.getElementById("as-product");
        document.getElementById("as-product-main").innerHTML = "";
 
        json.productList.forEach(product => {
            
            let productCloneHtml = productHtml.cloneNode(true);
 
            
            productCloneHtml.querySelector("#as-product-image").src = "product-images/" + product.id + "/image1.png";
            productCloneHtml.querySelector("#as-product-link").href = "singleProductView.html?id=" + product.id;
            productCloneHtml.querySelector("#as-product-title").innerHTML = product.tital;
            productCloneHtml.querySelector("#as-product-size").innerHTML = product.size.size;
            productCloneHtml.querySelector("#as-product-price").innerHTML = product.price;

            productCloneHtml.querySelector("#as-product-color").innerHTML = product.color.name;

            productCloneHtml.querySelector("#add-to-cart-btn").addEventListener("click",
                    (e) => {
                addToCart(product.id, 1);
                e.preventDefault();
            });

            document.getElementById("as-product-main").appendChild(productCloneHtml);

        });
        
         
    }
}



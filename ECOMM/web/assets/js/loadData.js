async function productView() {
    
    const response = await fetch("LoadProduct");
    
    if (response.ok) {

        const json = await response.json(); 

        document.getElementById("product-title").innerHTML = json.product.tital;
        document.getElementById("product-price").innerHTML = "Rs. " + json.product.price;   
        document.getElementById("product-color").innerHTML = json.product.color.name; 
        document.getElementById("product-size").innerHTML = json.product.size.size; 

         

    } else {
        document.getElementById("message").innerHTML = "Please try again later !";
    }
    
    //load product list
    let st_product_container = document.getElementById("st-product-container");

    st_product_container.innerHTML = "";

    json.productList.forEach(product => {
        let st_product_clone = st_product_container.cloneNode(true);

        //update details
        st_product_clone.querySelector("#image1").href = "singleProductView.html?id=" + product.id;
        st_product_clone.querySelector("#product-title").innerHTML = product.title;
        st_product_clone.querySelector("#product-price").innerHTML = product.price;
        st_product_clone.querySelector("#product-color").innerHTML = product.color;
        st_product_clone.querySelector("#product-size").innerHTML = product.size.size;

        st_product_container.appendChild(st_product_clone);
        
        

    });

}







async function loadCartItems() {

    const response = await fetch(
            "LoadCartItems",
            );

    const popup = new Notification();

    if (response.ok) {
        const json = await response.json();

        if (json.length == 0) {

            popup.error({
                message: "Your Cart is Empty"
            });

        } else {

            let cartItemContainer = document.getElementById("cart-item-container");
            let cartItemRow = document.getElementById("cart-item-row");
            cartItemContainer.innerHTML = "";
            
            let total = 0;
            let totalQty = 0;

            json.forEach(item => {

                let itemTotal = item.product.price * item.qty;

                totalQty += item.qty;
                total += itemTotal;

                let cartItemRowClone = cartItemRow.cloneNode(true);
                cartItemRowClone.querySelector("#cart-item-a").href = "singleProductView.html?id=" + item.product.id;
                cartItemRowClone.querySelector("#cart-item-image").src = "product-images/" + item.product.id + "/image1.png";
                cartItemRowClone.querySelector("#cart-item-title").innerHTML = item.product.tital;
                cartItemRowClone.querySelector("#cart-item-size").innerHTML = item.product.size.size;
                cartItemRowClone.querySelector("#cart-item-price").innerHTML = item.product.price;
                cartItemRowClone.querySelector("#cart-item-qty").value = item.qty;
                cartItemRowClone.querySelector("#cart-item-subtotal").innerHTML = itemTotal;
                
                cartItemContainer.appendChild(cartItemRowClone);
                
            }); 
            
          
            document.getElementById("cart-total-qty").innerHTML = totalQty; 
            document.getElementById("cart-total").innerHTML = total; 
             
        }  
        
        

    } else {

        popup.error({
            message: "Unable to processs your request"
        });
    }
    
}


//function removeCart(){
//    
//    session.delete(loadCartItems());
//}



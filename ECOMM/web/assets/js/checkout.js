// Payment completed. It can be a successful failure.
payhere.onCompleted = function onCompleted(orderId) {

    const popup = new Notification();
    popup.success({
        message: "Order Placed Successfully"
    });

    setTimeout("location.href = 'index.html';", 2500);

};

// Payment window closed
payhere.onDismissed = function onDismissed() {
    // Note: Prompt user to pay again or show an error page
    console.log("Payment dismissed");
};

// Error occurred
payhere.onError = function onError(error) {
    // Note: show an error page
    console.log("Error:" + error);
};

async function loadData() {

    const response = await fetch("LoadCheckout");

    if (response.ok) {

        const json = await response.json();

        if (json.success) {

            const address = json.address;
            const cityList = json.cityList;
            const cartList = json.cartList;

            let cityTag = document.getElementById("city");

            cityTag.length = 1;

            cityList.forEach(city => {
                let optionTag = document.createElement("option");
                optionTag.innerHTML = city.name;
                optionTag.value = city.id;

                cityTag.appendChild(optionTag);
            });

            
            let cureentAddressCheckBox = document.getElementById("checkbox1");

            cureentAddressCheckBox.addEventListener("change", e => {

                let first_name = document.getElementById("first_name");
                let last_name = document.getElementById("last_name");
                let line1 = document.getElementById("address1");
                let line2 = document.getElementById("address2");
                let postal_code = document.getElementById("postal-code");
                let mobile = document.getElementById("mobile");

                if (cureentAddressCheckBox.checked) {
                    first_name.value = address.first_name ;
                    last_name.value = address.last_name;
                    line1.value = address.line1;
                    line2.value = address.line2;
                    postal_code.value = address.postal_code;
                    mobile.value = address.mobile;
                    cityTag.value = address.city.id;
                    cityTag.disabled = true;
                    cityTag.dispatchEvent(new Event("change"));

                } else {

                    first_name.value = "";
                    last_name.value = "";
                    line1.value = "";
                    line2.value = "";
                    postal_code.value = "";
                    mobile.value = "";
                    cityTag.value = 0;
                    cityTag.disabled = false;
                    cityTag.dispatchEvent(new Event("change"));
                }

            });
            
            let st_checkout_body = document.getElementById("st-checkout-body");
            let st_checkout_item = document.getElementById("st-checkout-item");
            let st_order_subtotal = document.getElementById("st-order-subtotal");
            let st_order_shipping = document.getElementById("st-order-shipping");
            let st_order_total = document.getElementById("st-order-total");
            

            st_checkout_body.innerHTML = "";

            let sub_total = 0;

            cartList.forEach(item => {

                let st_item_clone = st_checkout_item.cloneNode(true);
                st_item_clone.querySelector("#st-checkout-title").innerHTML = item.product.tital;
                st_item_clone.querySelector("#st-checkout-qty").innerHTML = item.qty;

                let item_sub_total = item.product.price * item.qty;
                sub_total += item_sub_total;

                st_item_clone.querySelector("#st-checkout-item-subtotal").innerHTML = item_sub_total;

                st_checkout_body.appendChild(st_item_clone);

            });

            st_order_subtotal.querySelector("#st-checkout-subtotal").innerHTML = sub_total;
            st_checkout_body.appendChild(st_order_subtotal);

            let shipping_amount = 0;

            
            cityTag.addEventListener("change", e => {
               
                let item_count = cartList.length;
 
                if (cityTag.value == 1) {
                     
                    shipping_amount = item_count * 350;
                    
                } else {
                    
                    shipping_amount = item_count * 500;
                }

                st_order_shipping.querySelector("#st-shipping-amount").innerHTML = shipping_amount;
                st_checkout_body.appendChild(st_order_shipping);
                
                st_order_total.querySelector("#st-total").innerHTML = sub_total + shipping_amount;
                st_checkout_body.appendChild(st_order_total);


            });

            cityTag.dispatchEvent(new Event("change")); 

        } else {
            window.location = "signin.html";
        }
       
    }
}

async function checkOut() {

    const popup = new Notification();
 
    let isCurrentAddress = document.getElementById("checkbox1").checked;
 
    let first_name = document.getElementById("first_name");
    let last_name = document.getElementById("last_name");
    let line1 = document.getElementById("address1");
    let line2 = document.getElementById("address2");
    let postal_code = document.getElementById("postal-code");
    let mobile = document.getElementById("mobile");
    let cityTag = document.getElementById("city");

    const data = {
        isCurrentAddress: isCurrentAddress,
        first_name: first_name.value,
        last_name: last_name.value,
        city_id: cityTag.value,
        address1: line1.value,
        address2: line2.value,
        postal_code: postal_code.value,
        mobile: mobile.value

    };

    const response = await fetch("CheckOut",
            {
                method: "POST",
                body: JSON.stringify(data),
                headers: {
                    "Content-Type": "application/json"
                }
            }
    );

    if (response.ok) {

        const json = await response.json();
        
        console.log(json);

        if (json.success) {
 
            console.log(json.payhereJson);
         
            payhere.startPayment(json.payhereJson);
                
//                window.location = "index.html";
            //};
            
//            popup.success({
//                message: "Checkout Complete"
//            });

           

        } else {
            
            popup.error({
                message: json.message
            });
        }

    } else {

        popup.error({
            message: "Try again later!"
        });
    }
     console.log(response);
}



var brandList;

async function loadShopData() {
 
    const response = await fetch("LoadProduct");

    if (response.ok) {

        const json = await response.json(); 
        
        const categoryList = json.categoryList; 
        brandList = json.brandList;
        const colorList = json.colorList; 
        const sizeList = json.sizeList;
        
        loadSelect("categorySelect", categoryList, ["id", "name"]);
        loadSelect("brandSelect", brandList, ["id", "name"]);
        loadSelect("sizeSelect", sizeList, ["id", "size"]);
        loadSelect("colorSelect", colorList, ["id", "name"]); 
         

    } else {
        document.getElementById("message").innerHTML = "Please try again later !";
    }
}

function loadSelect(selectTagId, list, propertyArray) {

    const selectTag = document.getElementById(selectTagId);

    list.forEach(item => {

        let optionTag = document.createElement("option");
        optionTag.value = item[propertyArray[0]];
        optionTag.innerHTML = item[propertyArray[1]];
        selectTag.appendChild(optionTag);

    });
}

function updateBrands() {

    let brandSelectTag = document.getElementById("brandSelect");
    let selectedCategoryId = document.getElementById("categorySelect").value;

    brandSelectTag.length = 1;

    brandList.forEach(brand => {
        if (brand.category.id == selectedCategoryId) {
            let optionTag = document.createElement("option");
            optionTag.value = brand.id;
            optionTag.innerHTML = brand.name;
            brandSelectTag.appendChild(optionTag);
        }
    });
}

async function searchProducts(firstResult) {

    const popup = Notification();

    const category = document.getElementById("categorySelect").value; 
    const brand = document.getElementById("brandSelect").value; 
    const size = document.getElementById("sizeSelect").value;
    const color = document.getElementById("colorSelect").value;

    let price_range_start = $('#slider-range').slider('values', 0);
    let price_range_end = $('#slider-range').slider('values', 1);

    

    //Prepare request data
    const data = {
        category: category, 
        brand: brand,
        size: size,
        color: color,
        price_range_start: price_range_start,
        price_range_end: price_range_end,
        firstResult: firstResult
    };
    const response = await fetch(
            "searchAllProducts",
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

        if (json.success) {

            console.log(json.productList);
            updateProductView(json);

            popup.success({
                message: "Search Complete"
            });


        } else {
            popup.error({
                message: "Try Again later"
            });
        }


    } else {
        popup.error({
            message: "Try Again later"
        });
    }
//
//    
//
//    if (response.ok) {
//        
//        const json = await response.json();
//        updateProductView(json);
//        
//    } else {
//    }
       

     
}

function clearsort(){
    window.location.href = "advancedSearch.html";
}

const st_product = document.getElementById("as-product"); 

var st_pagination_btn = document.getElementById("st-pagination-btn");
var current_page = 0;
  
function updateProductView(json) {
 
    let st_product_container = document.getElementById("as-product-main");

    st_product_container.innerHTML = "";
    
    json.productList.forEach(product => {
        let st_product_clone = st_product.cloneNode(true);
 
        st_product_clone.querySelector("#as-product-link").href = "singleProductView.html?id=" + product.id;
        st_product_clone.querySelector("#as-product-image").src = "product-images/" + product.id + "/image1.png";
        st_product_clone.querySelector("#as-product-title").innerHTML = product.tital;
        st_product_clone.querySelector("#as-product-size").innerHTML = product.size.size;
        st_product_clone.querySelector("#as-product-price").innerHTML = product.price;

        st_product_clone.querySelector("#as-product-color").innerHTML = product.color.name;
       
        st_product_clone.querySelector("#add-to-cart-btn").addEventListener("click",
                (e) => {
            addToCart(product.id,1);
            e.preventDefault();
        });
            
        st_product_container.appendChild(st_product_clone);
        
        console.log(product);



    }); 
    
    let st_pagination_container = document.getElementById("st-pagination-container");

    st_pagination_container.innerHTML = "";

    let product_count = json.allProductCount; 
    const product_per_page =6;

    let pages = Math.ceil(product_count / product_per_page);


    if (current_page != 0) {
        //add Prev Button
        let st_pagination_btn_clone_prev = st_pagination_btn.cloneNode(true);

        st_pagination_btn_clone_prev.addEventListener("click", e => {

            current_page--;
            searchProducts(current_page * 6);

        });

        st_pagination_container.appendChild(st_pagination_btn_clone_prev);
    }


    //add Page Buttons
    for (let i = 0; i < pages; i++) {

        let st_pagination_btn_clone = st_pagination_btn.cloneNode(true);
        st_pagination_btn_clone.innerHTML = i + 1;
        st_pagination_btn_clone.addEventListener("click", e => {

            current_page = i;
            searchProducts(i * 6);
//            e.preventDefault();

        });

        if (i == current_page) {
            st_pagination_btn_clone.className = "btn btn-primary ms-3";
        } else {
            st_pagination_btn_clone.className = "abtn btn-primary ms-3";
        }

        st_pagination_container.appendChild(st_pagination_btn_clone);

    }

    if (current_page != (pages - 1)) {

        //add Next Button
        let st_pagination_btn_clone_next = st_pagination_btn.cloneNode(true);

        st_pagination_btn_clone_next.addEventListener("click", e => {

            current_page++;
            searchProducts(current_page * 6);

        });

        st_pagination_container.appendChild(st_pagination_btn_clone_next);

    }


}

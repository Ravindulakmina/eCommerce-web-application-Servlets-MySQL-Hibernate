var brandList;

async function loadFeatures() {

    const response = await fetch("LoadDetails");

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
 

async function productListing() {

    const categorySelectTag = document.getElementById("categorySelect");
    const brandSelectTag = document.getElementById("brandSelect");
    const titleTag = document.getElementById("title");
    const descriptionTag = document.getElementById("description");
    const sizeSelectTag = document.getElementById("sizeSelect");
    const colorSelectTag = document.getElementById("colorSelect"); 
    const priceTag = document.getElementById("price");
    const quantityTag = document.getElementById("quantity");
    const image1Tag = document.getElementById("image1"); 

    const data = new FormData;
    data.append("categoryId", categorySelectTag.value);
    data.append("brandId", brandSelectTag.value);
    data.append("title", titleTag.value);
    data.append("description", descriptionTag.value);
    data.append("sizeId", sizeSelectTag.value);
    data.append("colorId", colorSelectTag.value) 
    data.append("price", priceTag.value);
    data.append("quantity", quantityTag.value);
    data.append("image1", image1Tag.files[0]); 

    const response = await fetch(
            "ProductList",
            {
                method: "POST",
                body: data

            });

    if (response.ok) {

        const json = await response.json();
        const popup = new Notification();

        if (json.success) {
            categorySelectTag.value = 0;
            brandSelectTag.length = 1;
            titleTag.value = "";
            descriptionTag.value = "";
            sizeSelectTag.value = 0;
            colorSelectTag.value = 0; 
            priceTag.value = "";
            quantityTag.value = 0;
            image1Tag.value = null; 

           popup.success({
               message:json.content
           });


        } else {
             popup.error({
               message:json.content
           });
        }

    } else {
         
    }
}











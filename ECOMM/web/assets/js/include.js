async function include() {
   
    let header = document.getElementById('header');
    
    let headerResponse = await fetch('header.html');
    
    if (headerResponse.ok) {
        header.innerHTML = await headerResponse.text();
    }
 
    let footer = document.getElementById('footer');
    
    let footerResponse = await fetch('footer.html');
    
    if (footerResponse.ok) {
        footer.innerHTML = await footerResponse.text();
    }
}
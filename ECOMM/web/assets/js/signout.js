async function signOut() {
    
    const response = await fetch("SignOut");

    if (response.redirected) {
        
        window.location.href = response.url;
    } else {
        console.error('Failed to log out.');
    } 
}

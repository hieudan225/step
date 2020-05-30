/* Using promise but without arrow function */
function getRandomImage() {
    let responsePromise = fetch("/data");
    responsePromise.then(handleResponse);
}
function handleResponse(response) {
    console.log("Got respone");
    let textPromise = response.text();
    textPromise.then(handleText);
}
function handleText(imageURL) {
    console.log(imageURL);
    document.getElementById("bg-image").style.background = "linear-gradient(rgba(0,0,0,0.5),rgba(0,0,0,0.5)), url(" + imageURL +") no-repeat fixed center";
}
/* Using promise and arrow function */
function getRandomImageArrow() {
    fetch("/data").then(response => response.text()).then(imageURL => document.getElementById("bg-image").style.background = "linear-gradient(rgba(0,0,0,0.5),rgba(0,0,0,0.5)), url(" + imageURL +") no-repeat fixed center");
}
/* Using asyn and await */
async function getRandomImageAsync() {
    let response = await fetch("/data");
    console.log("Got respone");
    let imageURL = await response.text();
    console.log(imageURL);
    document.getElementById("bg-image").style.background = "linear-gradient(rgba(0,0,0,0.5),rgba(0,0,0,0.5)), url(" + imageURL +") no-repeat fixed center";
};
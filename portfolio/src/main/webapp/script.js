/*Handle background-change request */
async function getRandomImageAsync() {
    let response = await fetch("/background-change");
    console.log("Got respone");
    let imageURL = await response.text();
    console.log(imageURL);
    document.getElementById("bg-image").style.background = "linear-gradient(rgba(0,0,0,0.5),rgba(0,0,0,0.5)), url(" + imageURL +") no-repeat fixed center";
};

/*Handle onload for comment section in intro.html*/
async function getExistingComments() {
    let response = await fetch("/comment");
    console.log("Got respone");
    let json = await response.json();
    console.log(json);
    let container = document.getElementById("comment-container");
    container.innerHTML = "";
    json.forEach(element => {
        let node = document.createElement("LI"); 
        let name = document.createElement("P");
        name.innerText = "Name: " + element.name;
        let time = document.createElement("P");
        time.innerText = "Time: " + element.time;
        let comment = document.createElement("P");
        comment.innerText = "Comment: " + element.comment;

        node.appendChild(name);
        node.appendChild(time);
        node.appendChild(comment);
        
        container.appendChild(node);
    });
}
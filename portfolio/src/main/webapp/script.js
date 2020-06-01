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
    let response = await fetch("/list-comments");
    console.log("Got respone");
    let json = await response.json();
    console.log(json);
    let container = document.getElementById("comment-container");
    container.innerHTML = "";
    json.forEach(comment => {
        let node = document.createElement("LI"); 
        
        let name = document.createElement("span");
        name.innerText = "Name: " + comment.name;
        let content = document.createElement("span");
        content.innerText = "Content: " + comment.content;
        
        let deleteElement = document.createElement("button");
        deleteElement.innerText = "delete";
        deleteElement.addEventListener('click', () => {
            node.remove();
            deleteEntity(comment.id);
        });
        node.appendChild(name);
        node.appendChild(content);
        node.appendChild(deleteElement);
        
        container.appendChild(node);
    });
}

function deleteEntity(id) {
    const params = new URLSearchParams();
    params.append('id', id);
    fetch('/delete-comment', {method: 'POST', body: params});
}
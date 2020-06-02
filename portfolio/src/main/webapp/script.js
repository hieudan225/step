/*Handle background-change request */
async function getRandomImageAsync() {
    let response = await fetch("/background-change");
    console.log("Got respone");
    let imageURL = await response.text();
    console.log(imageURL);
    document.getElementById("bg-image").style.background = "linear-gradient(rgba(0,0,0,0.5),rgba(0,0,0,0.5)), url(" + imageURL +") no-repeat fixed center";
};

/*Handle onload for comment section in intro.html*/
async function setMaxComments() {
    let maxComments = document.getElementById("maxComments").value;

    const params = new URLSearchParams();
    params.append('maxComments', maxComments);
    await fetch('/max-comments', {method: 'POST', body: params});
    location.reload();
}

async function getExistingComments() {

    let maxCommentsRep = await fetch("/max-comments");
    let maxCommentsText = await maxCommentsRep.text();
    let maxComments = parseInt(maxCommentsText, 10);

    console.log(maxComments);
    const params = new URLSearchParams();
    params.append('maxComments', maxComments);
    
    let response = await fetch('/list-comments', {method: 'POST', body: params});
    console.log("Got respone");
    let json = await response.json();
    console.log(json);
    let container = document.getElementById("comment-container");
    container.innerHTML = "";
    for (let i = 0; i < json.length; ++i) {
        let comment = json[i];
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
        };
}


async function deleteEntity(id) {
    const params = new URLSearchParams();
    params.append('id', id);
    await fetch('/delete-comment', {method: 'POST', body: params});
}

async function deleteAllComments() {
    await fetch('/delete-all-comments');

    let deleteConatiner = document.getElementById("delete");
    deleteConatiner.innerText = "Finish deleting.";
    setTimeout(location.reload(), 1000);
}
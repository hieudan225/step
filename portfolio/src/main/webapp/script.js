/*Handle background-change request */
async function getRandomImageAsync() {
    let response = await fetch("/background-change");
    console.log("Got respone");
    let imageURL = await response.text();
    console.log(imageURL);
    document.getElementById("bg-image").style.background = "linear-gradient(rgba(0,0,0,0.5),rgba(0,0,0,0.5)), url(" + imageURL +") no-repeat fixed center";
};

function isLetter(c) {
  return c.match("/^[A-Za-z]+$/");
} 

async function validateForm() {
  let name = document.getElementById("myComment")["name"].value;
  let content = document.forms["myComment"]["content"].value;
  if (!isLetter(name[i])) {
    alert("JS: Name must alphabet letter only.");
  } else {
    const params = new URLSearchParams();
    params.append('name', name);
    params.append('content', content);
    //await fetch('/new-comment', {method: 'POST', body: params});
    //location.reload();

  }
}

/*Handle onload for comment section in intro.html*/
async function setMaxComments() {
    let maxComments = document.getElementById("maxComments").value;

    const params = new URLSearchParams();
    params.append('maxComments', maxComments);
    await fetch('/max-comments', {method: 'POST', body: params});
    location.reload();
}
/*Load comments history for comment section in intro.html */
async function getExistingComments() {

    let maxCommentsRep = await fetch("/max-comments");
    let maxCommentsText = await maxCommentsRep.text();
    let maxComments = parseInt(maxCommentsText, 10);

    
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
    let response = await fetch('/delete-all-comments');
    let repText = await response.text();
    while (repText === "false") {
        response = await fetch('/delete-all-comments');
        repText = await response.text();
    }
    location.reload();
}
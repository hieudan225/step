/*Handle background-change request */
async function getRandomImageAsync() {
    let response = await fetch("/background-change");
    console.log("Got respone");
    let imageURL = await response.text();
    console.log(imageURL);
    document.getElementById("bg-image").style.background = "linear-gradient(rgba(0,0,0,0.5),rgba(0,0,0,0.5)), url(" + imageURL +") no-repeat fixed center";
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
        node.style.border = "2px solid #fff";
        node.style.margin = "10px";
        node.style.padding = "10px";
        
        let name = document.createElement("P");
        name.innerText = "Email: " + comment.email;
        let time = document.createElement("P");
        time.innerText = "Time: " + comment.timestamp;
        let content = document.createElement("P");
        content.innerText = "Content: " + comment.content;
        let sentiment = document.createElement("P");
        sentiment.innerText = "Sentiment: " + comment.sentiment;
        
        let deleteElement = document.createElement("button");
        deleteElement.innerText = "delete";
        deleteElement.addEventListener('click', () => {
            node.remove();
            deleteEntity(comment.id);
        });
        node.appendChild(name);
        node.appendChild(timestamp);
        node.appendChild(content);
        node.appendChild(sentiment);
        node.appendChild(deleteElement);
        
        container.appendChild(node);
        };
}

function deleteEntity(id) {
    const params = new URLSearchParams();
    params.append('id', id);
    fetch('/delete-comment', {method: 'POST', body: params});
}

function deleteAllComments() {
    let response = fetch('/delete-all-comments', {method: 'POST'});
    location.reload();
}

async function onLoad() {
    
    let loginStatusPromise  = await fetch("/login");
    let loginStatus = await loginStatusPromise.json();
    console.log(loginStatus);
    if (!loginStatus.login) {
        document.getElementById("login").style.display = "none";

        const loginURL = loginStatus.url;
        let loginURLContainer = document.getElementById("not-login");
        let loginURLElement = document.createElement("button");
        loginURLElement.innerText = "Click here to Login";
        let num = 1;
        loginURLElement.addEventListener('click', () => {
            window.location = loginURL;
        });
        loginURLContainer.appendChild(loginURLElement);
    }
    else {
        document.getElementById("not-login").style.display = "none";
        await getExistingComments();

        const helloUserContainer = document.getElementById("hello-user");
        let helloUserString = "Hello " + loginStatus.email +"!";
        let helloUserElement = document.createElement("P");
        helloUserElement.style.color = "#fff";
        helloUserElement.innerHTML = helloUserString;
        helloUserContainer.appendChild(helloUserElement);

        const logoffURL = loginStatus.url;
        let logoffURLContainer = document.getElementById("logoff");
        let logoffURLElement = document.createElement("button");
        logoffURLElement.innerText = "Click here to Logoff";
        logoffURLElement.addEventListener('click', () => {
            window.location = logoffURL;
        });
        logoffURLContainer.appendChild(logoffURLElement);
    }
    
}

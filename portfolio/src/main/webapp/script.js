/*Handle background-change request */
async function getRandomImageAsync() {
    let response = await fetch("/background-change");
    console.log("Got respone from /background-change");
    let imageURL = await response.text();
    document.getElementById("bg-image").style.background = "linear-gradient(rgba(0,0,0,0.5),rgba(0,0,0,0.5)), url(" + imageURL +") no-repeat fixed center";
}

async function setMaxComments() {
    let maxComments = document.getElementById("maxComments").value;
    const params = new URLSearchParams();
    params.append('maxComments', maxComments);
    await fetch('/max-comments', {method: 'POST', body: params});
    location.reload();
    console.log("Has set maxComments to " + maxComments);
}

/*Load comments history for comment section in intro.html */
async function getExistingComments() {

    let maxCommentsRep = await fetch("/max-comments");
    let maxCommentsText = await maxCommentsRep.text();
    let maxComments = parseInt(maxCommentsText, 10);
    
    
    const params = new URLSearchParams();
    params.append('maxComments', maxComments);
    let response = await fetch('/list-comments' +"?"+ params);
    console.log("Got response from /list-comments");

    let json = await response.json();

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
        time.innerText = "Time: " + parseTimeStamp(comment.timestamp);
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
        node.appendChild(time);
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

async function deleteAllComments() {
    let response = await fetch('/delete-all-comments', {method: 'POST'});
    console.log("Finished deleting all comments");
    setTimeout(location.reload(), 1000);
}

async function onLoad() {
    document.getElementById("login").style.display = "none";
    document.getElementById("not-login").style.display = "none";
    
    let loginStatusPromise  = await fetch("/login");
    let loginStatus = await loginStatusPromise.json();
    console.log("This is the login status: ");
    console.log(loginStatus);
    if (!loginStatus.login) {
        document.getElementById("not-login").style.display = "block";

        const loginURL = loginStatus.loginUrl;
        let loginURLContainer = document.getElementById("not-login");
        let loginURLElement = document.createElement("button");
        loginURLElement.innerText = "Click here to Login";
        loginURLElement.addEventListener('click', () => {
            window.location = loginURL;
        });
        loginURLContainer.appendChild(loginURLElement);
    }
    else {
        document.getElementById("login").style.display = "block";
        await getExistingComments();

        const helloUserContainer = document.getElementById("hello-user");
        let helloUserString = "Hello " + loginStatus.email +"!";
        let helloUserElement = document.createElement("P");
        helloUserElement.style.color = "#fff";
        helloUserElement.innerHTML = helloUserString;
        helloUserContainer.appendChild(helloUserElement);

        const logoffURL = loginStatus.logoutUrl;
        let logoffURLContainer = document.getElementById("logoff");
        let logoffURLElement = document.createElement("button");
        logoffURLElement.innerText = "Click here to Logoff";
        logoffURLElement.addEventListener('click', () => {
            window.location = logoffURL;
        });
        logoffURLContainer.appendChild(logoffURLElement);
    }
    
}

function parseTimeStamp(timestamp) {
    let string = "";
    string += timestamp.date.year + "-" + timestamp.date.month + "-"+ timestamp.date.day + " ";
    string += timestamp.time.hour + ":" + timestamp.time.minute;
    return string;
}
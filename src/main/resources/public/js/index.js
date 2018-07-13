console.log("Si funciona");

document.getElementById("lebutton").addEventListener("click",loadText);

function loadText(){
    var xhr = new XMLHttpRequest();
    xhr.open("GET","/text",true);
    
    xhr.onload = function () {
        if (xhr.status === 200){
            document.getElementsByTagName("h1")[0].textContent = xhr.response;
        }
    }

    xhr.send();
}
var editorHeight = 200;


let taggedUsers = [];

function deleteTag(idFriend){
    taggedUsers = taggedUsers.filter(function(resp){return resp != idFriend});
    $(`#friend${idFriend}`).remove();
}

function verify(idFriend){
    for(let i in taggedUsers){
        if (taggedUsers[i] == idFriend) return false;
    }
    return true;
}

function tagFriend(idFriend){
    if (idFriend !== ""){
        if (verify(idFriend)){
            $.get(`/getFriend/${idFriend}`).then(resp=>{
                let friend = JSON.parse(resp);
                $("#tagArea").append(`<span class='badge' style="padding: 7px" onclick="deleteTag(${idFriend})" id="friend${idFriend}">${friend.fullName}</span>`)
                taggedUsers.push(idFriend);
            });
        }
        $("#tagSelector").val("");
    }
}

$(document).ready(function () {
    let idPublication = sessionStorage.getItem("idPublication")


    var addButton = function (context) {
        var ui = $.summernote.ui;

        // create button
        var button = ui.button({
            contents: '<i class="fa fa-child"/> Actualizar Estado',
            tooltip: 'Actualizar Estado',
            click: function () {
                // invoke insertText method with 'hello' on editor module.
                let status = $('#summernote').summernote('code');
                let publication = {};
                publication.description = status;
                publication.id = idPublication;
                console.log(publication);
                $.post(`/updatePublication/${JSON.stringify(taggedUsers)}`,JSON.stringify(publication),function(){
                    location.href = "/";
                });
            }
        });

        return button.render();   // return button as jquery object
    }

    $('#summernote').summernote({
        height: editorHeight,
        minHeight:editorHeight,
        maxHeight: editorHeight,
        focus:true,
        disableResizeEditor: true,
        toolbar: [
            // [groupName, [list of button]]
            ['style', ['bold', 'clear']],
            'picture',
            'addComment'
        ],

        buttons:{
            addComment: addButton
        }
    });



    $.get(`/getPublication/${idPublication}`).then(resp=>{
        let publication = JSON.parse(resp);
        $('#summernote').summernote('code',publication.description);
        if (publication.hasOwnProperty('taggedUsers')){
            for (let i in publication.taggedUsers){
                taggedUsers.push(publication.taggedUsers[i].id);
                $("#tagArea").append(`<span class='badge' style="padding: 7px" onclick="deleteTag(${publication.taggedUsers[i].id})" id="friend${publication.taggedUsers[i].id}">${publication.taggedUsers[i].fullName}</span>`)
            }
        }
    })

});

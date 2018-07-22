
var editorHeight = 200;



$(document).ready(function() {

    //    Custom buttons

    var addButton = function (context) {
        var ui = $.summernote.ui;

        // create button
        var button = ui.button({
            contents: '<i class="fa fa-child"/> Actualizar Estado',
            tooltip: 'Actualizar Estado',
            click: function () {
                // invoke insertText method with 'hello' on editor module.
                let status = $($("#summernote").summernote("code")).text();
                let publication = {};
                publication.date = new Date();
                publication.description = status; //TODO Falta la lógica de tagging
                $.post(`/insertPublication/${JSON.stringify(taggedUsers)}`,JSON.stringify(publication),function(){
                    loadPage();
                    clean();
                });
            }
        });

        return button.render();   // return button as jquery object
    }

    //Add buttons



    $("#profilePic").click(function () {
        $("#imgupload").trigger('click');
    });

    $("#albumupload").click(function () {
        $("#albumcreate").trigger('click');
    });

    $("#albumcreate").on("change",function () {
        var input = this;
        var pictures = [];
        if (input.files && input.files[0]) {
            for (let i=0; i<input.files.length; i++){
                (function(file){
                    console.log("ENTRANDO")
                    var reader = new FileReader();
                    reader.onload = function(e){
                        let base64Image = {
                            image: e.target.result
                        };
                        pictures.push(base64Image);
                    }
                })(input.files[i]);
            }

            // $.post("/updateProfilePic",JSON.stringify(base64Image),function(){
            //     $('#profilePic').attr('src', e.target.result); //La imagen como tal se encuntra en result
            //     window.location.href = "/profile";
            // });
        }

        console.log(pictures);
    });


    //Sumernote
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






//    Edit Profile.

    var panels = $('.user-infos');
    var panelsButton = $('.dropdown-user');
    panels.hide();

    //Click dropdown
    panelsButton.click(function() {
        //get data-for attribute
        var dataFor = $(this).attr('data-for');
        var idFor = $(dataFor);

        //current button
        var currentButton = $(this);
        idFor.slideToggle(400, function() {
            //Completed slidetoggle
            if(idFor.is(':visible'))
            {
                currentButton.html('<i class="glyphicon glyphicon-chevron-up text-muted"></i>');
            }
            else
            {
                currentButton.html('<i class="glyphicon glyphicon-chevron-down text-muted"></i>');
            }
        })
    });


    $('[data-toggle="tooltip"]').tooltip();

    // $('button').click(function(e) {
    //     e.preventDefault();
    //     alert("This is a demo.\n :-)");
    // });

});

function goToGallery(){
    $("#wall_normal").hide();
    $("#wall_editP").hide();
    $("#wall_gallery").show();
    $("#wall_friends").hide();
}

function goToFriends(){
    $("#wall_gallery").hide();
    $("#wall_editP").hide();

    $("#wall_normal").hide();
    $("#wall_friends").show();
}

function backToWall(){
    $("#wall_gallery").hide();
    $("#wall_editP").hide();

    $("#wall_normal").show();
    $("#wall_friends").hide();
}

function goToEditProfile(){
    $("#wall_gallery").hide();
    $("#wall_normal").hide();
    $("#wall_editP").show();
    $("#wall_friends").hide();
}

function enableEdit(){
    $("table > tbody > tr >td > textarea").each(function(){
       $(this).removeAttr('disabled');
    });
    $("table > tbody > tr >td > input").each(function(){
       $(this).removeAttr('disabled');
    });
}

function disableEdit(){
    $("table > tbody > tr >td > textarea").each(function(){
        $(this).attr('disabled', true);
    });
    $("table > tbody > tr >td > input").each(function(){
        $(this).attr('disabled', true);
    });
}

function cleanEdit(){
    $("table > tbody > tr >td > textarea ").each(function(){
        $(this).val("");
    });
    $("table > tbody > tr >td > input").each(function(){
        $(this).val("");
    });
}

function compare(element){
    return typeof element !== "undefined" && element !== null && element !== '';
}

function saveEdit(idUser){

    let user = {};

    if (compare($("#email-signup").val()) && $("#email-signup").val().indexOf("@") !== -1){
        user.email = $("#email-signup").val();
    }
    if (compare($("#birthdate-edit").val())){
        let dat = new Date($("#birthdate-edit").val());
        user.bornDate = new Date(dat.getTime()+14400000);
    }
    if (compare($("#birthplace-edit").val())){
        user.bornPlace = $("#birthplace-edit").val();
    }
    if (compare($("#livingplace-edit").val())){
        user.location = $("#livingplace-edit").val();
    }
    if (compare($("#laststudyplace-edit").val())){
        user.studyPlace = $("#laststudyplace-edit").val();
    }
    if (compare($("#lastWorkPlace-edit").val())){
        user.jobs = $("#lastWorkPlace-edit").val();
    }
    if ($("#sex-male").is(":checked") || $("#sex-woman").is(":checked") || $("#sex-na").is(":checked")){
        user.sex = $("#sex-male").is(":checked") ? "m" : ($("#sex-woman").is(":checked") ? 'f' : 'n/a')
    }

    if (Object.keys(user).length > 0){
        $.post(`/editUser/${idUser}`,JSON.stringify(user)).then(resp=>{
            disableEdit();
            cleanEdit();
        });
    }
    else{
        $("#email-signup").effect("shake");
        $("#birthdate-edit").effect("shake");
        $("#birthplace-edit").effect("shake");
        $("#livingplace-edit").effect("shake");
        $("#laststudyplace-edit").effect("shake");
        $("#lastWorkPlace-edit").effect("shake");
        $("#sex-male").effect("shake");
        $("#sex-woman").effect("shake");
        $("#sex-na").effect("shake");
    }
}

// portfolio
$('.gallery ul li a').click(function() {
    var itemID = $(this).attr('href');
    $('.gallery ul').addClass('item_open');
    $(itemID).addClass('item_open');
    return false;
});
$('.close').click(function() {
    $('.port, .gallery ul').removeClass('item_open');
    return false;
});

$(".gallery ul li a").click(function() {
    $('html, body').animate({
        scrollTop: parseInt($("#top").offset().top)
    }, 400);
});

function addComment(idPublication, idUser){
    let comment = {
        description: $(`#text${idPublication}`).val()
    };
    $.post(`/saveComment/${idUser}/${idPublication}`, JSON.stringify(comment), function(resp){
        $.get(`/getComments/${idPublication}`, function(resp){
            console.log(resp);
            $(`#com${idPublication}`).html(resp);
        });
    })
}

function deleteComment(idPublication, idComment){
    $.post(`/deleteComment/${idPublication}/${idComment}`, function(resp){
        $.get(`/getComments/${idPublication}`, function(resp){
            $(`#com${idPublication}`).html(resp);
        });
    })
}

function addLike(idPublication, idUser){
    let like = {
        action: 1
    };
    $.post(`/saveLike/${idUser}/${idPublication}`, JSON.stringify(like), function(resp){
        $(`#likes${idPublication}`).html(resp);
    });
}

function quitLike(idPublication, idUser){
    $.post(`/quitLike/${idUser}/${idPublication}`, JSON.stringify({}), function(resp){
        $(`#likes${idPublication}`).html(resp);
    });
}

function refreshPubs(){
    $(function () {
        $('.panel-google-plus > .panel-footer > .input-placeholder, .panel-google-plus > .panel-google-plus-comment > .panel-google-plus-textarea > button[type="reset"]').on('click', function(event) {
            var $panel = $(this).closest('.panel-google-plus');
            $comment = $panel.find('.panel-google-plus-comment');

            $comment.find('.btn:first-child').addClass('disabled');
            $comment.find('textarea').val('');

            $panel.toggleClass('panel-google-plus-show-comment');

            if ($panel.hasClass('panel-google-plus-show-comment')) {
                $comment.find('textarea').focus();
            }
        });
        $('.panel-google-plus-comment > .panel-google-plus-textarea > textarea').on('keyup', function(event) {
            var $comment = $(this).closest('.panel-google-plus-comment');

            $comment.find('button[type="submit"]').addClass('disabled');
            if ($(this).val().length >= 1) {
                $comment.find('button[type="submit"]').removeClass('disabled');
            }
        });
    });
}

let taggedUsers = [];

function clean(){
    taggedUsers = [];
    $("#tagArea").html("");
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

function deleteTag(idFriend){
    $(`#friend${idFriend}`).remove();
}

function toggleMenu(idUser){
    $(`#menu${idUser}`).toggle(200);
}

function toggleMenuu(idNotification){
    console.log(idNotification);
    $(`#menuu${idNotification}`).toggle(200);
}

function sendRequest(idFriend, idUser){
    $.post(`/sendRequest/${idUser}/${idFriend}`).then(resp=>{
        $(`#uss${idFriend}`).remove();
    })
}

function acceptRequest(idNotification, idSender, idReceiver){
    $.post(`/acceptRequest/${idNotification}/${idSender}/${idReceiver}`).then(resp=>{
        $(`#req${idNotification}`).remove();
    });
}

function deleteRequest(idNotification){
    $.post(`/deleteRequest/${idNotification}`).then(resp=>{
        $(`#req${idNotification}`).remove();
    });
}

function deletePublication(idPublication){
    $.post(`/deletePublication/${idPublication}`).then(resp=>{
        let loc = location.href.split("/");
        if (loc[loc.length-1] === 'profile') {
            loadPage(function () {
                refreshPubs();
            });
        }
        else {
            loadHistory(function () {
                refreshPubs();
            });
        }
    });
}

function loadPage(callback){
    let id = sessionStorage.getItem("idUser");
    if (id !== null){
        $.get(`/loadPosts/${id}`).then(resp=>{
            $("#posts").html(resp);
            callback();
        })
    }
}

function loadHistory(callback){
    $.get(`/loadHistory`).then(resp=>{
        $("#posts").html(resp);
        callback();
    })
}

function editPublication(idPublication){
    sessionStorage.setItem("idPublication",idPublication);
    location.href = `/editPublication`;
}

let loc = location.href.split("/");
if (loc[loc.length-1] === 'profile'){
    $(document).ready(function(){
        loadPage(function(){
            refreshPubs();
        });
    });
}
else{
    $(document).ready(function(){
        loadHistory(function(){
            refreshPubs();
        });
    });
}








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
                $.post("/insertPublication",JSON.stringify(publication),function(){
                    window.location.href = "/profile";
                });
            }
        });

        return button.render();   // return button as jquery object
    }

    //Add buttons



    $("#profilePic").click(function () {
        console.log("TA PASANDO ALGO")
        $("#imgupload").trigger('click');
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

function goToGallery(){
    $("#wall_normal").hide();
    $("#wall_editP").hide();
    $("#wall_gallery").show();
}

function backToWall(){
    $("#wall_gallery").hide();
    $("#wall_editP").hide();

    $("#wall_normal").show();
}

function goToEditProfile(){
    $("#wall_gallery").hide();
    $("#wall_normal").hide();
    $("#wall_editP").show();
}

function enableEdit(){
    $("table > tbody > tr >td > input").each(function(){
       $(this).removeAttr('disabled');
    });
}

function saveEdit(){
//    Implement Code to save edits.
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

function addComment(idPublication){

}


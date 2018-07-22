$(document).ready({

});


function notificationHandler(){

    if ($(".friends-header-text").is(":visible")){
        $(".friends-header-text").hide();
        $("#activity-friends").hide();
    }


    if($(".notif-header-text").is(":hidden")){

       $(".notif-header-text").show();
       $("#activity-notifications").show();

   }else{
      $(".notif-header-text").hide();
      $(".activity-feed").hide();
   }
}

function friendshipHandler(){

    if ($(".notif-header-text").is(":visible")){
        $(".notif-header-text").hide();
        $("#activity-notifications").hide();
    }


    if($(".friends-header-text").is(":hidden")){

        $(".friends-header-text").show();
        $("#activity-friends").show();

    }else{
        $(".friends-header-text").hide();
        $("#activity-friends").hide();
    }
}

function goToPublication(idPublication, idNotification){
    $("#myModal").show(500);
    sessionStorage.setItem("ids",JSON.stringify({idPublication: idPublication, idNotification: idNotification}));
    loadPub();
}

var modal = document.getElementById('myModal');

var span = document.getElementsByClassName("close")[0];

function loadPub(){
    let ids = JSON.parse(sessionStorage.getItem('ids'));
    $.get(`/loadPublicationOnModal/${ids.idPublication}`).then(resp=>{
        $("#pub").html(resp);
        refreshPubs();
    });
}

span.onclick = function() {
    let ids = JSON.parse(sessionStorage.getItem('ids'));
    $.post(`/updateNotification/${ids.idNotification}`).then(resp=>{
        $(`#not${ids.idNotification}`).remove();
        $("#myModal").hide(500);
    });
};

window.onclick = function(event) {
    if (event.target == modal) {
        let ids = JSON.parse(sessionStorage.getItem('ids'));
        $.post(`/updateNotification/${ids.idNotification}`).then(resp=>{
            $(`#not${ids.idNotification}`).remove();
            $("#myModal").hide(500);
        });
    }
};

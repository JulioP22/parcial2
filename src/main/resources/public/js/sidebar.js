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
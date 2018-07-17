$(document).ready({

});

function notificationHandler(){
   if($(".notif-header-text").is(":hidden")){
       $(".notif-header-text").show();
       $(".activity-feed").show();
       $(this).addClass("fh5co-active");
       $(this).toggleClass("fh5co-active"); //TODO Make this work
   }else{
      $(".notif-header-text").hide();
      $(".activity-feed").hide();
      $(this).removeClass("fh5co-active"); //TODO make this work
   }
}

function friendShipHandler(){
//    Do logic to act over messages!
}
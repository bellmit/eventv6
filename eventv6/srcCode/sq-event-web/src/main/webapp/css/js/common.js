
(function ($){
    $(function () {
        var $NavDiv = $(".nav ul li");
           $NavDiv.click(function(){
                  $(this).addClass("current").siblings().removeClass("current");
          var NavIndex = $NavDiv.index(this);
          $(".TreeShow .con div").eq(NavIndex).show().siblings().hide();
           });
   
           $(".TreeHide").click(function(){
           $(".TreeShow").show();
	        });
           $(".TreeClose").click(function(){
           $(".TreeShow").hide();
        });
    });
})(jQuery);
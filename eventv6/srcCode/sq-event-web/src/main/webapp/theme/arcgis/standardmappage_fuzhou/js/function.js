window.onload = function(){
/*-------------------------------MainLeftRight自适应高度宽度-------------------------------------*/
$(function(){						
	var width=$(window).width();
	$(".MainRight").css("width",width-330);
	$(".matter").css("width",width-400);
	$("#WebSiteBox").css("width",width-62);
});
/*--------------------------------------------------------------------*/

/*----------------------------MapSwitch----------------------------------------*/
	$(function(){
		
		$(".MapSwitchBtn").click(function() {
				var div = $(".MapBox");
				if(div.hasClass("dest")) {
						div.removeClass("dest").slideUp(200, function(){
							$(".MapSwitchBtn").addClass("MapOpen");
						});
				} else {
						div.addClass("dest").slideDown(200, function(){
							$(".MapSwitchBtn").removeClass("MapOpen");
						});
				}
		});
		$(".SwitchBtn").click(function() {
			var div = $("#MainLeft");
			var w1 = $(".MainRight").width();
			if(div.hasClass("dest")) {
				div.removeClass("dest").hide(200, function(){
					$(".SwitchBtn").addClass("open");
					$("#MainRight").css("width",w1+310);
					openflag = true;
				});
			} else {
				div.addClass("dest").show(200, function(){
					$(".SwitchBtn").removeClass("open");
					$("#MainRight").css("width",w1-310);
					openflag = false;
				});
			}
		});
	});
}

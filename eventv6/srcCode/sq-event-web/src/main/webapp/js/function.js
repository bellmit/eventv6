
window.onload = function(){

/*-------------------------------MainLeftRight自适应高度宽度-------------------------------------*/
			setInterval(function(){
						var height=$(window).height();
						$(".RightContent").css("height",height);
						$(".LeftContent").css("height",height);
						$(".ColumnView .con").css("height",height-154);
						$(".MetterList .line").css("height",height-86);
						$(".MetterList .box").css("height",height-86);
						$(".MetterList .box2").css("height",height);
						$(".MC_con").css("height",height-110);
						$(".MC_con2").css("height",height-63);
						
						var width=$(window).width();
						$(".RightContent").css("width",width-197);
						$(".MetterList .box").css("width",width-266);
						$(".MetterList .MetterContent").css("width",width-310);
				},3);
				
			var $NavDiv = $(".LeftContent .menu ul li");
			$NavDiv.click(function(){
				  $(this).addClass("current").siblings().removeClass("current");
				  var NavIndex = $NavDiv.index(this);
				  $(".RightContent .abc").eq(NavIndex).show().siblings().hide();
		   });
		   
	       var $NavDiv2 = $("#tab ul li");
	       $NavDiv2.click(function(){
	              $(this).addClass("current").siblings().removeClass("current");
	              var NavIndex2 = $NavDiv2.index(this);
	              $(".ListShow div.tabss").eq(NavIndex2).show().siblings().hide();
	       });
}	
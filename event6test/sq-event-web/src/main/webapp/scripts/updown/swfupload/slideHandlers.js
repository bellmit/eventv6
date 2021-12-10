


function slideShow(_config){

	

	var ajaxUrl = _config.ajaxUrl;
	var ajaxData = _config.ajaxData;
	var positionId = _config.positionId;
	var slideWidth = _config.slideWidth || 920;
	var slideHeight =  _config.slideHeight || 400;
	$.ajax({
			type: "POST",
		   	url: ajaxUrl,
		   	data: ajaxData||{},
		   	dataType:'json',
		   	success: function(result){
		   		if(result.success==1){
		   			$("#"+positionId).html('<div id="main" style="width:'+slideWidth+';"><div id="gallery" style="width:'+slideWidth+';"><div id="slides" style="width:'+slideWidth+';height:'+slideHeight+';"></div><div id="menu"><ul id="ul"><li class="fbar">&nbsp;</li></ul></div></div></div>');
		   			$(result.list).each(function(object){
		   				var imgUrl = result.filedomain+this.filePath;
		   				if(this.imgWidth>slideWidth){
		   					var scale = slideWidth/this.imgWidth;
		   					this.imgWidth = slideWidth;
		   					this.imgHeight = this.imgHeight*scale;
		   				}
		   				if(this.imgHeight>slideHeight){
		   					var scale = slideHeight/this.imgHeight;
		   					this.imgHeight = slideHeight;
		   					this.imgWidth = this.imgWidth*scale;
		   				}
						$("#slides").append('<div class="slide"><table  border=0 width="'+slideWidth+'" height="'+slideHeight+'"><tr><td ><img src="'+imgUrl+'" width="'+this.imgWidth+'" height="'+this.imgHeight+'"  alt="'+this.fileName+'" /></td></tr></table></div>');
						$("#ul").append('<li class="menuItem"><a href=""><img src="'+imgUrl+'" alt="'+this.fileName+'" width="60" height="34"  /></a></li>');
					});
					slideStart();
		   		
  
		   		
		   		}else{
		   			$("#"+positionId).html("暂无图片");
		   		}
		   		
		   		if(($.browser.msie)){
		   			$('#ul li').css("display","inline");
		   		}else if(window.external&&window.external.twGetRunPath&&window.external.twGetRunPath().toLowerCase().indexOf("360se")>-1){
		   			$('#ul li').css("display","inline");
		   		}
				
				
		   	}
		});
}
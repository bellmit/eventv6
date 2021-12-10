<!-- 
#图片查看
说明：
#1、
#2、
 -->
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/fbsource/jquery.fancybox.css?v=2.1.5" media="screen" />
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/fbsource/jquery.fancybox-buttons.css"/>
<script type="text/javascript" src="${rc.getContextPath()}/js/fbsource/jquery.fancybox.js?v=2.1.5"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/fbsource/jquery.fancybox-buttons.js"></script>

<script type="text/javascript">
if (typeof (AnoleApi) == "undefined") {
	var ImageViewApi = {};
}
ImageViewApi.initImageView = function(fieldId,imgs) {
	var sourceId = fieldId + "_Div";
	$(document.body).append("<div id='"+sourceId+"' style='display: none;'></div>");
	var imgDiv = $("#"+sourceId+"");
	if(Object.prototype.toString.call(imgs) === '[object Array]'){
		if (imgs.length>0) {
			for(var i= 0; i<imgs.length;i++) {
				console.log(imgs[i]);
				var filePath = imgs[i];
				var img = '<a class="fancybox-button" href="'+filePath+'" data-fancybox-group="gallery" ><img class="pic" src="'+filePath+'" /></a>';
				imgDiv.append(img);
			}
		}
	}else{
		var filePath = imgs;
		var img = '<a class="fancybox-button" href="'+filePath+'"  data-fancybox-group="gallery"><img class="pic" src="'+filePath+'" /></a>';
		imgDiv.append(img);
// 		console.log(imgs);
	}

	imgDiv.find('.fancybox-button').fancybox({
		closeBtn		: false,
		helpers		: {
			buttons	: {}
		}
	});
	$("#"+fieldId+"").click(function(){ 
		var imgDiv = $("#"+sourceId+"");
		imgDiv.find('.fancybox-button').eq(0).click();
	});
};

function ffcs_viewImg(fieldId,index){
	var sourceId = fieldId + "_Div";
	$("#"+fieldId+"").click(function(){ 
		var imgDiv = $("#"+sourceId+"");
		imgDiv.find('.fancybox-button').eq(index).click();
	});
}
</script>
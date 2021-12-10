<!DOCTYPE HTML>
<html style="overflow: hidden;">
<head>
    <title>社区视频预览(live)</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
    <meta content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no" name="viewport">
    <script type="text/javascript" src="${uiDomain!''}/js/liveplayer/element/liveplayer-element.min.js"></script>
	<script type="text/javascript" src="${uiDomain!''}/js/security/waterMark.js"></script>
</head>
<body style="padding: 0px;margin: 0px;width:100%;height:100%;">
    <script type="text/javascript">
        var watermarkContent = "${watermarkContent!''}";
    </script>
    <div id="content_video">
        <live-player id="player01" video-url="${red5!''}" poster="${SKY_URL}/assets/resources/js/video/image/loading.gif" aspect="16:9" autoplay=true live="${liveStream!false}"></live-player>
    </div>
    <script type="text/javascript">
        // JavaScript 交互示例
        var height = window.parent.$('#${iframeId}').css('height');
		$("body").css('height',height);
        var player = document.getElementById('player01');
        player.getVueInstance().play();
        var clicknum=0;
        setTimeout(function () {
            var player = document.getElementById('player01');
            player.getVueInstance().play();
            $(".vjs-tech").click();
        },2000)
	var num_ = 0;
		function saveFileBackFn(e){
		if(num_++ <1){
			$.ajax({
				type: "POST",
				url: '${rc.getContextPath()}/zhsq/map/DDEarthController/saveImg.json',
				data:{
					'base64':e
				},
				success: function(data){
					num_ = 0;
					if(data){
						window.parent.eventReport(data);
					}
				}
				
			});
}
		}
		
		document.ready(function(){
            document.body.style.backgroundColor='#fff';   			
watermark({watermark_txt:"${watermarkContent!'未登录'}",watermark_cols:10,watermark_rows:10,watermark_x:-20,watermark_angle:5,hasParent:true,
watermark_x_space:150,watermark_y_space:80,watermark_height:$(window.parent.document).height(),watermark_width:$(window.parent.document).width()});

            var x = document.getElementsByClassName('panel-body');
            for (var i = 0; i < x.length; i++) {
                x[i].style.backgroundColor ='transparent';
            }
            console.log('${iframeId}'+""+(Math.random()));
        });
		var iframeId='${iframeId}';
		var monitorId=${monitorId};
		$("#content_video").click(function () {
			//直接在这边给样式，清除其他样式
			if(clicknum>0){//自动播放为第一次点击，不执行
				//layerPointObj[moniterId];
				var iframeIdObj = window.parent.$("#"+iframeId).parent();
				var index =parseInt(iframeId.substr(iframeId.length-1,1));
				window.parent.selected=index;
				window.parent.currentIndex=index;
				window.parent.videoSelect = iframeIdObj;
				window.parent.selectByHand=index;
				window.parent.currentIndex=index;
				window.parent.videoSelectByHand=iframeIdObj;
				iframeIdObj.css('border','3px solid rgba(255, 130, 62, 1)').parent().siblings().find('div').css('border','none');
				var pointObj = window.parent.layerPointObj[monitorId];
				window.parent.$("#video_name").html(pointObj.attributes.data.text);
				window.parent.$("#video_code").html(pointObj.attributes.data.indexCode);
            }
            clicknum++;
			
			
		});
    </script>
</body>
</html>
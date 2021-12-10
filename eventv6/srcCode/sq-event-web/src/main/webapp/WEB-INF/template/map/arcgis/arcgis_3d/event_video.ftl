<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>南昌-视频可视化中间弹窗</title>
    <link rel="stylesheet" href="${uiDomain!''}/web-assets/common/css/reset.css">
    <link rel="stylesheet" href="${uiDomain!''}/web-assets/_big-screen/nanchang-cc/css/index-mask.css">
    <script src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="${uiDomain!''}/js/security/waterMark.js" type="text/javascript" charset="utf-8"></script>
    
    <script type="text/javascript">
        var winW, scale;
        $(window).on('load resize', function () {
            fullPage();
            setTimeout(fullPage, 10); //二次执行页面缩放，解决全屏浏览时滚动条问题
            function fullPage() { //将页面等比缩放

                winW = $(window.parent).width();

                //winW = 1366;

                if (winW < 1000) {
                    winW = 1000;
                }
                var whdef = 100 / 1920;
                var rem = winW * whdef; // 以默认比例值乘以当前窗口宽度,得到该宽度下的相应FONT-SIZE值
                $('html').css('font-size', rem + "px");
            }
        });
    </script>
</head>

<body style="background: transparent;">
	<input type="hidden" name="indexCode" id="indexCode" value="${indexCode}"/>
	
    <div class="mae-container mae-container-on dianwei-center1">
        <div class="dianwei-shipin xqi-bs clearfix">
            <div class="clearfix  dianwei-shipin-content">
                <div class="dianwei-shipin-content1 fl dianwei-shipin-content1-on">
                    <div class="video" style="width:100%;height:4.5rem;">
                    	<iframe src="" width="100%" height="100%" id='videoUrl' allow="autoplay" allowfullscreen></iframe>
                    </div>
                    <div class="shangbao">
                        <p class="p1">一键督办</p>
                    </div>
                </div>
                
            </div>
        </div>
    </div>
    
    
    <div class="mae-container shijian-center1" style="margin-left:-5rem">
    <div class="shangbao-content xqi-bs clearfix">

            <div class="clearfix  shangbao-content-right" style="width: 9.6rem">
       	 		<iframe id="event_frame" width="100%" height="100%" frameborder="no" scrolling="no" overflow="hidden" ></iframe>
    	 	</Div>
     </div>
    </div>
    </div>
</body>

</html>

<script>
    $(function () {
        //点位视频弹窗切换
        var ind;
        $('.dianwei-shipin-title').on('click', 'a', function () {
            $(this).addClass('active').siblings('a').removeClass('active');
            ind = $(this).index();
            $('.dianwei-shipin-content1').eq(ind).addClass('dianwei-shipin-content1-on').siblings()
                .removeClass('dianwei-shipin-content1-on');
                
            init();
        });
        //点位视频弹窗切换

        // 事件上报
        $('.shangbao .p1').click(function () {
        	var event = {
    			    "isReport":false
    		} 
        	var event = JSON.stringify(event);  
        	$("#event_frame").attr("src","${rc.getContextPath()}/zhsq/event/eventDisposalController/toAddEventByVideo.jhtml?from=_nc3d&picName=&monitorById=${monitorId}&eventJson="+encodeURIComponent(event));
            $('.shijian-center1').css('display', 'block');
            $('.dianwei-center1').css('display', 'none');
        });
        // 事件上报


        //提交取消按钮
        $('.submit1').click(function () {
            $(this).addClass('active').siblings().removeClass('active');
        });
        //提交取消按钮

    });
</script>
<script>
	$(function(){
		setTimeout(function () {
            getVideoDetail('${indexCode}');
        }, 1000); 
			
	});

		//视频详情
		function getVideoDetail(indexCode){
			
				$.ajax({
					type: "POST",
					url: '${rc.getContextPath()}/zhsq/map/DDEarthController/getVideoUrl.json',
					//dataType:"json",
					data:{
						"indexCode":indexCode
					},
					success: function(data){
						console.log(data);
						var json = eval('(' + data + ')'); 
						if(json.code=="0"){
							var previewUrl = json.data.url;
							var url ="http://10.20.142.3:10050/shequ/video/index.jhtml?streamAddr="+previewUrl+"&live=1";
							$("#videoUrl").attr("src",url);
							
						}else{
							alert("获取预览url失败");
						}
					},
					error: function(XMLHttpRequest, textStatus, errorThrown) {
                        alert("获取预览url出错");
                    },
					
				});
			
		}
</script>

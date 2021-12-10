<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>中型-事件弹窗</title>
    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/_big-screen/JiangYinPlatform/css/mask.css"/>
    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/common/css/reset.css">
    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/plugins/swiper-5.4.1/package/css/swiper.min.css">
    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/_big-screen/tianshui/css/public.css"/>
    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/_big-screen/tianshui/css/mask.css"/>
    <script>
        // 页面缩放
        var winW, winH, whdef, rem;
        function fullPage() { //将页面等比缩放
            winW = <#if pwidth??>${pwidth}<#else>$(window.parent).width()</#if>,
            whdef = 100 / 1920,
            rem = winW * whdef, // 以默认比例值乘以当前窗口宽度,得到该宽度下的相应FONT-SIZE值
            document.querySelector('html').style.fontSize = rem + 'px';
        }
        fullPage();
        window.onresize = fullPage;
    </script>
    <style>
	    .datagrid-mask {
			z-index:51;
			position: absolute;
			left: 0;
			top: 0;
			opacity: 0.3;
			background: #ccc;
		}
		
		.swiper-slide>img {
    		height: auto; 
		}
		
		.maed-c-left {
    		height: 4.5rem;
		}
		
		.maed-c-right {
    		height: 4.5rem;
		}
		
		.legal-mcccr-center {
    		height: 3.21rem;
		}
		.lmcc-mask-box {
		    height: 90%;
		    padding-top:30px;
		}
    </style>
</head>
<body style="background-color: transparent;">
	<div class="event-mask" id="eventAttr" style="width: 10rem;height: 6rem;margin-top:-3rem;left:53%;z-index:55;display:none">
        <div class="mask-main-top" style="height:0rem;">
            <i class="close" id="eventAddClose"></i>
        </div>
        <div class="mask-main-bottom" style="height:100%;">
            <iframe id="mask-eventAttr" frameborder="0" width="100%" height="100%" style="overflow:hidden"></iframe>
        </div>
    </div>
    <!-- 有返回按钮时添加 ts-mask-title1-->
    <!-- 表格时查看 -->
    <div class="ts-mask-title clearfix">
       <p>详情</p>
    </div>
    <div class="sj-contnet bs">
        <div class="sj-c-table bs hide">
            <div class="sj-ct-top">
                <div class="ts-rbi3p-thead bs">
                    <table class="ts-rbip-table">
                        <colgroup>
                            <col style="width: .88rem;">
                            <col style="width: auto;">
                            <col style="width: 1.6rem;">
                            <col style="width: 1.6rem;">
                            <col style="width: 2rem;">
                            <col style="width: 1.4rem;">
                        </colgroup>
                        <thead>
                            <tr>
                                <th>序号</th>
                                <th>事件名称</th>
                                <th>来源</th>
                                <th>类型</th>
                                <th>时间</th>
                                <th>状态</th>
                            </tr>
                        </thead>
                    </table>
                </div>
                <div class="ts-rbi3p-tbody niceitem bs">
                    <table class="ts-rbip-table">
                        <colgroup>
                            <col style="width: .88rem;">
                            <col style="width: auto;">
                            <col style="width: 1.6rem;">
                            <col style="width: 1.6rem;">
                            <col style="width: 2rem;">
                            <col style="width: 1.4rem;">
                        </colgroup>
                        <tbody>
                            <tr>
                                <td>1</td>
                                <td><a href="javascript:void(0);">XXX事件名称事件名称</a></td>
                                <td>群众上报</td>
                                <td>群众上报</td>
                                <td>2020-06-18 09:30:29</td>
                                <td class="processing">处理中</td>
                            </tr>
                            <tr>
                                <td>2</td>
                                <td><a href="javascript:void(0);">XXX事件名称事件名称</a></td>
                                <td>人员预警</td>
                                <td>人员预警</td>
                                <td>2020-06-18 09:30:29</td>
                                <td class="processing">处理中</td>
                            </tr>
                            <tr>
                                <td>3</td>
                                <td><a href="javascript:void(0);">XXX事件名称事件名称</a></td>
                                <td>综治事件</td>
                                <td>综治事件</td>
                                <td>2020-06-18 09:30:29</td>
                                <td class="pending">待处理</td>
                            </tr>
                            <tr>
                                <td>4</td>
                                <td><a href="javascript:void(0);">XXX事件名称事件名称</a></td>
                                <td>人员预警</td>
                                <td>人员预警</td>
                                <td>2020-06-18 09:30:29</td>
                                <td class="processed">已处理</td>
                            </tr>
                            <tr>
                                <td>5</td>
                                <td><a href="javascript:void(0);">XXX事件名称事件名称</a></td>
                                <td>人员预警</td>
                                <td>人员预警</td>
                                <td>2020-06-18 09:30:29</td>
                                <td class="processed">已处理</td>
                            </tr>
                            <tr>
                                <td>6</td>
                                <td><a href="javascript:void(0);">XXX事件名称事件名称</a></td>
                                <td>人员预警</td>
                                <td>人员预警</td>
                                <td>2020-06-18 09:30:29</td>
                                <td class="processed">已处理</td>
                            </tr>
                            <tr>
                                <td>7</td>
                                <td><a href="javascript:void(0);">XXX事件名称事件名称</a></td>
                                <td>人员预警</td>
                                <td>人员预警</td>
                                <td>2020-06-18 09:30:29</td>
                                <td class="processed">已处理</td>
                            </tr>
                            <tr>
                                <td>8</td>
                                <td><a href="javascript:void(0);">XXX事件名称事件名称</a></td>
                                <td>人员预警</td>
                                <td>人员预警</td>
                                <td>2020-06-18 09:30:29</td>
                                <td class="processed">已处理</td>
                            </tr>
                            <tr>
                                <td>9</td>
                                <td><a href="javascript:void(0);">XXX事件名称事件名称</a></td>
                                <td>人员预警</td>
                                <td>人员预警</td>
                                <td>2020-06-18 09:30:29</td>
                                <td class="processed">已处理</td>
                            </tr>
                            <tr>
                                <td>10</td>
                                <td><a href="javascript:void(0);">XXX事件名称事件名称</a></td>
                                <td>人员预警</td>
                                <td>人员预警</td>
                                <td>2020-06-18 09:30:29</td>
                                <td class="processed">已处理</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="sj-ct-pagination">
                <a href="javascript:void(0);" class="sj-ctp-pre"></a>
                <div class="sj-ctp-page clearfix mlr10">
                    <a href="javascript:void(0);" class="active">1</a>
                    <a href="javascript:void(0);">2</a>
                    <a href="javascript:void(0);">3</a>
                    <a href="javascript:void(0);">4</a>
                    <a href="javascript:void(0);">5</a>
                    <a href="javascript:void(0);">6</a>
                    <a href="javascript:void(0);">7</a>
                    <a href="javascript:void(0);">8</a>
                    <a href="javascript:void(0);">9</a>
                    <a href="javascript:void(0);">10</a>
                </div>
                <a href="javascript:void(0);" class="sj-ctp-next mlr10"></a>
                <div class="ml25 sj-ctp-jump clearfix">
                    <p>跳至</p>
                    <input type="text" value="10">
                    <p>页</p>
                </div>
            </div>
        </div>
        <div class="sj-c-details bs">
            <div class="maed-content" id="eventInfoDiv">
                <div class="clearfix">
                    <div class="maed-c-left niceitem fl">
                        <p class="me-cl-title" id="eventName" style="white-space: nowrap;text-overflow: ellipsis;"></p>
                        <p class="me-cl-small-title" id="eventType"></p>
                        <div class="maed-cl-top bs niceitem">
                            <p class="maed-clt-title">于<span id="happenTimeStr"></span>在<span id="occurred"></span>发生：
                            </p>
                            <p class="maed-clt-text" id="content"></p>
                        </div>
                        <div class="maed-cl-bottom mtr15 bs">
                            <ul class="maed-clb-list bs">
                                <li class="clearfix">
                                    <div class="maed-w50 fl clearfix">
                                        <p>所属网格：</p>
                                        <p id="gridPath"></p>
                                    </div>
                                    <div class="maed-w50 fr clearfix">
                                        <p>紧急程度：</p>
                                        <p id="urgencyDegreeName"></p>
                                    </div>
                                </li>
                                <li class="clearfix">
                                   <div class="maed-w50 fl clearfix">
                                        <p>事件编号：</p>
                                        <p id="eventCode"></p>
                                   </div>
                                   <div class="maed-w50 fr clearfix">
                                        <p>联系人员：</p>
                                        <p id="contactor"></p>
                                   </div>
                                </li>
                                <li class="clearfix">
                                    <div class="maed-w50 fl clearfix">
                                        <p>影响范围：</p>
                                        <p id="influenceDegreeName"></p>
                                    </div>
                                    <div class="maed-w50 fr clearfix">
                                        <p>信息来源：</p>
                                        <p id="sourceName"></p>
                                    </div>
                                </li>
                                <li class="clearfix">
                                    <div class="maed-w50 fl clearfix">
                                        <p>涉及人员：</p>
                                        <p id="involvedNumName"></p>
                                    </div>
                                    <div class="maed-w50 fr clearfix">
                                        <p>当前状态：</p>
                                        <p id="statusName"></p>
                                    </div>
                                </li>
                                <li class="clearfix">
                                    <div class="maed-w50 fl clearfix">
                                        <p>采集渠道：</p>
                                        <p id="collectWayName"></p>
                                    </div>
                                </li>
                            </ul>
                        </div>
                    </div>
                    <div class="maed-c-right fr bs">
                        <div class="legal-mcccr-top bs">
                            <ul class="lmcccrt-list clearfix">
                                <li class="attrLabel" labelType="img" total="0" isLoad="0">
                                    <a href="javascript:void(0);">
                                        <p id="imgNum">图片</p>
                                    </a>
                                </li>
                                <li class="attrLabel" labelType="sound" total="0" isLoad="0">
                                    <a href="javascript:void(0);">
                                        <p id="soundNum">音频</p>
                                    </a>
                                </li>
                                <li class="attrLabel" labelType="video" total="0" isLoad="0">
                                    <a href="javascript:void(0);">
                                        <p id="videoNum">视频</p>
                                    </a>
                                </li>
                            </ul>
                        </div>
                        <div class="attrSwiper legal-mcccr-center bs" id="img_swiper_div">
                            <div class="swiper-container" id="img_swiper">
                                <div class="swiper-wrapper" id="img_swiper_content">
                                    <div class="swiper-slide">
                                        <img src="${uiDomain!''}/web-assets/_big-screen/tianshui/images/mask/thing-pic.png">
                                    </div>
                                </div>
                                <div class="swiper-button-next1" id="img_swiper_next"></div>
                                <div class="swiper-button-prev1" id="img_swiper_prev"></div>
                            </div>
                        </div>
                        <div class="attrSwiper legal-mcccr-center bs" id="sound_swiper_div" style="display:none">
                            <div class="swiper-container" id="sound_swiper">
                                <div class="swiper-wrapper" id="sound_swiper_content">
                                    <div class="swiper-slide">
                                        <img src="${uiDomain!''}/web-assets/_big-screen/tianshui/images/mask/thing-pic.png">
                                    </div>
                                </div>
                                <div class="swiper-button-next1" id="sound_swiper_next"></div>
                                <div class="swiper-button-prev1" id="sound_swiper_prev"></div>
                            </div>
                        </div>
                        <div class="attrSwiper legal-mcccr-center bs" id="video_swiper_div" style="display:none">
                            <div class="swiper-container" id="video_swiper">
                                <div class="swiper-wrapper" id="video_swiper_content">
                                    <div class="swiper-slide">
                                        <img src="${uiDomain!''}/web-assets/_big-screen/tianshui/images/mask/thing-pic.png">
                                    </div>
                                </div>
                                <div class="swiper-button-next1" id="video_swiper_next"></div>
                                <div class="swiper-button-prev1" id="video_swiper_prev"></div>
                            </div>
                        </div>
                        <div class="attrSwiper legal-mcccr-bottom" id="img_release">
                            <div class="lmcccrb-top clearfix">
                                <div class="lmcccrb-top-left fl clearfix">
                                    <i></i>
                                    <p id="img_title">处理前</p>
                                </div>
                                <div class="lmcccrb-top-right fr clearfix">
                                    <p id="img_time">1978-01-01</p>
                                </div>
                            </div>
                            <div class="lmcccrb-page">
                                <p>（ <span>1</span> / <span style="color:white" id="img_num">0</span> ）</p>
                            </div>
                        </div>
                        <div class="attrSwiper legal-mcccr-bottom" id="sound_release" style="display:none">
                            <div class="lmcccrb-top clearfix">
                                <div class="lmcccrb-top-left fl clearfix">
                                    <i></i>
                                    <p id="sound_title">处理前</p>
                                </div>
                                <div class="lmcccrb-top-right fr clearfix">
                                    <p id="sound_time">1978-01-01</p>
                                </div>
                            </div>
                            <div class="lmcccrb-page">
                                <p>（ <span>1</span> / <span style="color:white" id="sound_num">0</span> ）</p>
                            </div>
                        </div>
                        <div class="attrSwiper legal-mcccr-bottom" id="video_release" style="display:none">
                            <div class="lmcccrb-top clearfix">
                                <div class="lmcccrb-top-left fl clearfix">
                                    <i></i>
                                    <p id="video_title">处理前</p>
                                </div>
                                <div class="lmcccrb-top-right fr clearfix">
                                    <p id="video_time">1978-01-01</p>
                                </div>
                            </div>
                            <div class="lmcccrb-page">
                                <p>（ <span>1</span> / <span style="color:white" id="video_num">0</span> ）</p>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="event-details-btn" <#if showButton?? && showButton=='1'><#else>style="display:none"</#if>>
                    <a style="cursor: pointer;" onclick="showZhoubianResource()">查看周边资源</a>
                </div>
            </div>
            <a href="javascript:void(0);" class="lmcc-mask-open">
                <i class="lmccm-open"></i>
                <p>处</p>
                <p>理</p>
                <p>记</p>
                <p>录</p>
            </a>
            <div class="lmcc-mask-box">
                <a href="javascript:void(0);" class="lmcc-mask-open">
                    <i class="lmccm-close"></i>
                    <p>处</p>
                    <p>理</p>
                    <p>记</p>
                    <p>录</p>
                </a>
                <div class="lmcc-mask bs">
                    <div class="lmcc-mask-content">
                        <div class="det-links-des clearfix">
                            <ul class="flex layer_aj_bt_n fr">
                                <li class="clearfix fr">
                                    <div class="aj_ks aj_ks_blue fl">
                                        <div class="aj_ks1"></div>
                                        <div class="aj_ks2"></div>
                                    </div>
                                    <p>当前环节</p>
                                </li>
                                <li class="flex flex-ac fl" style="margin-right: .41rem;">
                                    <div class="aj_ks aj_ks_gray">
                                        <div class="aj_ks1"></div>
                                        <div class="aj_ks2"></div>
                                    </div>
                                    <p>历史环节</p>
                                </li>
                                <li class="flex flex-ac fl">
                                    <div class="aj_ks aj_ks_gray">
                                        <div class="aj_ks1" style="background-color: #f0bd56"></div>
                                        <div class="aj_ks2"></div>
                                    </div>
                                    <p>驳回环节</p>
                                </li>
                            </ul>
                        </div>
                        <div class="layer_aj_bt_b bs clearfix">
                            <p class="layer_aj_bt_b1">办理环节</p>
                            <p class="layer_aj_bt_b2">办理人/办理时间</p>
                            <p class="layer_aj_bt_b3">办理意见</p>
                        </div>
                        <div class="layer_aj_bt_s">
                            <div class="layer_aj_bt_line"></div>
                            <ul class="layer_aj_bt_items" id="flowDetail">
                                
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
    <script src="${uiDomain!''}/web-assets/plugins/swiper-5.4.1/package/js/swiper.min.js"></script>
    <script src="${uiDomain!''}/web-assets/plugins/jquery-nicescroll/jquery.nicescroll-1.js"></script>
    <script src="${uiDomain!''}/web-assets/_big-screen/tianshui/js/tainshui.js"></script>
    <script>
    
    	$(function(){
    		initEventInfo(${eventId});
    	});
    	
    	function modleEventLabelopen(dom) {
			$("<div class='datagrid-mask'></div>").css({
				display : "block",
				width : "100%",
				height : $(window).height()
			}).appendTo($('#'+dom));
	
			document.body.scroll = "no";//除去滚动条
		}

		function modleEventLabelclose() {
			$(".datagrid-mask").css({
				display : "none"
			});
			$(".datagrid-mask-msg").css({
				display : "none"
			});
			$(".datagrid-mask").remove();
			$(".datagrid-mask-msg").remove();
			document.body.scroll = "auto";
		}
    	
    	function formatStr(str){
			if(str == null ||str == 'null' || str == undefined ){
				return '';
			}else{
				return str;				
			}
		}
		
		function showImg(i){
			$('#eventAttr').show();
			$('#mask-eventAttr').attr('src','${rc.getContextPath()}/zhsq/showImage/indexOfPath.jhtml?drag=true&background=none&overflow=none&fieldId=playImg&index='+i+'&titles='+encodeURI(imgTitleList.toString())+'&paths='+imgList.toString());
			//window.open('${rc.getContextPath()}/zhsq/showImage/indexOfPath.jhtml?fieldId=playImg&index='+i+'&paths='+imgList.toString());
		}
	
		function showVideo(id){//eventSeq
			$('#eventAttr').show();
			$('#mask-eventAttr').attr('src','${rc.getContextPath()}/zhsq/att/toSeeVideo.jhtml?isBigScreen=1&videoType=2&attachmentId='+id);
			//window.open('${rc.getContextPath()}/zhsq/att/toSeeVideo.jhtml?videoType=2&attachmentId='+id);
		}
		
		function showZhoubianResource(){
			window.parent.postMessage('showEventZhouBian()','${messageUrl}');
		}
		
		var attrMap={
			'img':'图片',
			'sound':'音频',
			'video':'视频'
		}
		
		var imgTitleArr={
			"1":"处理前",
			"2":"处理中",
			"3":"处理后"
		}
    	
    	function initEventInfo(eventId){
    		modleEventLabelopen('eventInfoDiv');
    		$.ajax({
				type: "POST",
				url: '${rc.getContextPath()}/zhsq/event/centralControlCabinController/getEventInfo.json',
				data: {"eventId":eventId},
				dataType:"json",
				success: function(data){
				
					//设置事件基本信息
					var event=data.event;
					
					$('#urgencyDegreeName').html(formatStr(event.urgencyDegreeName));
					$('#influenceDegreeName').html(formatStr(event.influenceDegreeName));
					$('#involvedNumName').html(formatStr(event.involvedNumName));
					if(event.tel){
						$('#contactor').html(formatStr(event.contactUser)+'('+formatStr(event.tel)+')');
					}else{
						$('#contactor').html(formatStr(event.contactUser));
					}
					$('#sourceName').html(formatStr(event.sourceName));
					$('#collectWayName').html(formatStr(event.collectWayName));
					$('#gridPath').html(formatStr(event.gridPath));
					$('#eventCode').html(formatStr(event.code));
					$('#happenTimeStr').html(formatStr(event.happenTimeStr));
					$('#content').html(formatStr(event.content));
					$('#occurred').html(formatStr(event.occurred));
					$('#statusName').html(formatStr(event.statusName));
					$('#eventType').html('['+formatStr(event.eventClass)+']');
					$('#eventName').html(formatStr(event.eventName));
					$('#eventName').attr('title',formatStr(event.eventName));
					
					$("#content").niceScroll({
			            cursorcolor: "#185ab2",
			            cursoropacitymax: 1,
			            cursorwidth: "4px",
			            autohidemode: false,
			            cursorborder: "none",
			            cursorborderradius: "0",
			        })
					
					//填充环节信息
					var flow=data.process;
					var flowStr="";
					for(var i=0,j=flow.length;i<j;i++){
						var d=flow[i];
						
						//如果是当前环节#a9ff00
						if(d.IS_CURRENT_TASK){
							flowStr+='<li class="flex flex-ac">'+
							'<h5 class="aj_items_h aj_items_h_green" style="color:#a9ff00">'+d.TASK_NAME+'</h5>'+
							'<div class="aj_ks aj_ks_blue">'+
							'<div class="aj_ks1"></div>'+
							'<div class="aj_ks2"></div>'+
					        '</div>'+
					        '<div>'+
					        '<div class="flex flex-ac">'+
					        '<p class="aj_items_t aj_items_t_yellow">'+d.HANDLE_PERSON+'</p>'+
					        '</div>';
					        
					        if(d.subAndReceivedTaskList!=null && d.subAndReceivedTaskList.length>0){
					        
					        	for(var m=0,n=d.subAndReceivedTaskList.length;m<n;m++){
					        		var t=d.subAndReceivedTaskList[m];
					        		
					        		flowStr+='<div class="flex flex-ac mtr10">'+
					        		'<div class="aj_items_t2 flex">'+
					        		'<p class="aj_items_t aj_items_t_yellow" style="margin-left:0rem;text-align: left">'+t.TRANSACTOR_NAME+'('+t.ORG_NAME+')'+'<br>';
					        		if(m==0){
					        			flowStr+='<span>接受时间</span>:'+t.RECEIVE_TIME;
					        		}else{
					        			flowStr+='<span>处理时间</span>:'+t.END_TIME;
					        		}
					        		
					        		if(t.REMARKS){
					        			flowStr+='<p class="aj_items_t aj_items_t_result">'+t.REMARKS+'</p>'
					        		}
					        	
					        	}
					        
					        }
						
					        flowStr+='</div>'+
					        '</li>';
						}else{
						
							flowStr+='<li class="flex flex-ac">';
							
							if(d.OPERATE_TYPE==2){//驳回环节
								flowStr+='<h5 class="aj_items_h aj_items_h_green"  style="color:#f0bd56">'+formatStr(d.TASK_NAME)+'</h5>';
								flowStr+='<div class="aj_ks aj_ks_gray">'+
									'<div class="aj_ks1" style="background-color: #FFB90F"></div>'+
									'<div class="aj_ks2"></div>'+
									'</div>';
							}else{
							
								flowStr+='<h5 class="aj_items_h aj_items_h_green">'+formatStr(d.TASK_NAME)+'</h5>';
								flowStr+='<div class="aj_ks aj_ks_gray">'+
								'<div class="aj_ks1"></div>'+
								'<div class="aj_ks2"></div>'+
								'</div>';
							
							}
							
							flowStr+='<div class="aj_items_t2 flex">'+
							'<p class="aj_items_t aj_items_t_green bs">'+formatStr(d.TRANSACTOR_NAME)+'('+d.ORG_NAME+')'+'<span> 耗时 '+d.INTER_TIME+'</span><br><span>办理时间</span>:'+d.START_TIME+'</p>'+
							'</div>';
							if(d.REMARKS){
								flowStr+='<p class="aj_items_t aj_items_t_result">'+d.REMARKS+'</p>';
							}
							flowStr+='</li>';
						
						}
					
					}
					
					$('#flowDetail').html(flowStr);
					
					//设置附件
					var attrStr="";
					imgList=[];
					imgTitleList=[];
					
					attrTitleList={};
					attrTimeList={};
					
					var imgs=data.imgs;
					var sounds=data.sounds;
					var videos=data.videos;
					var attrflag=0;
					
					if(imgs!=null&&imgs.length>0){
						$('#imgNum').html('图片('+imgs.length+')');
						$('#img_num').html(imgs.length);
						$('.attrLabel[labelType=img]').attr('total',imgs.length);
						var imgstr="";
						for(var i=0,j=imgs.length;i<j;i++){
							var d=imgs[i];
							imgList.push("${IMG_URL}"+d.filePath);
							imgTitleList.push(imgTitleArr[d.eventSeq]?imgTitleArr[d.eventSeq]:"处理前");
							
							attrTitleList['img_'+i]=imgTitleArr[d.eventSeq]?imgTitleArr[d.eventSeq]:"处理前";
							attrTimeList['img_'+i]=d.createTimeStr;
							imgstr+='<div class="swiper-slide" >';
							imgstr+='<img style="cursor: pointer;" onclick="showImg('+i+')" src="${IMG_URL}'+d.filePath+'"/>';
							imgstr+='</div>';
						}
						$('#img_swiper_content').html(imgstr);
						
					}
					
					if(sounds!=null&&sounds.length>0){
						$('#soundNum').html('音频('+sounds.length+')');
						$('#sound_num').html(sounds.length);
						$('.attrLabel[labelType=sound]').attr('total',sounds.length);
						var soundstr="";
						for(var i=0,j=sounds.length;i<j;i++){
							var d=sounds[i];
							attrTitleList['sound_'+i]=imgTitleArr[d.eventSeq]?imgTitleArr[d.eventSeq]:"处理前";
							attrTimeList['sound_'+i]=d.createTimeStr;
							soundstr+='<div class="swiper-slide" >';
							soundstr+='<img style="cursor: pointer;" onclick="showVideo('+d.attachmentId+')" src="${rc.getContextPath()}/scripts/updown/swfupload/images/thumbnail/audio.jpg"/>';
							soundstr+='</div>';
						}
						$('#sound_swiper_content').html(soundstr);
					}
					
					if(videos!=null&&videos.length>0){
						$('#videoNum').html('视频('+videos.length+')');
						$('#video_num').html(videos.length);
						$('.attrLabel[labelType=video]').attr('total',videos.length);
						var videoStr="";
						for(var i=0,j=videos.length;i<j;i++){
							var d=videos[i];
							attrTitleList['video_'+i]=imgTitleArr[d.eventSeq]?imgTitleArr[d.eventSeq]:"处理前";
							attrTimeList['video_'+i]=d.createTimeStr;
							videoStr+='<div class="swiper-slide" >';
							videoStr+='<img style="cursor: pointer;" onclick="showVideo('+d.attachmentId+')" src="${rc.getContextPath()}/scripts/updown/swfupload/images/thumbnail/audio.jpg"/>';
							videoStr+='</div>';
						}
						$('#video_swiper_content').html(videoStr);
					}
					
					$('.attrLabel').eq(0).click();
					
					
				},
				complete:function(){
					modleEventLabelclose();
				}
			});	
    	}
    	
        //事件详情图片轮番
        
        //事件处理记录
        $(".layer_aj_bt_s").niceScroll({
            cursorcolor: "#185ab2",
            cursoropacitymax: 1,
            cursorwidth: "4px",
            autohidemode: false,
            cursorborder: "none",
            cursorborderradius: "0",
        })
        $('.sj-c-details>.lmcc-mask-open').click(function () {
            $(this).hide();
            $(".maed-cl-top").getNiceScroll().hide();
            $(".maed-clb-list").getNiceScroll().hide();
            $('.lmcc-mask-box').show();
            $('.lmcc-mask').animate({ "right": "0" }, 100, function () {
                $('.lmcc-mask-box').css({ "overflow": "unset" });
                $('.layer_aj_bt_line').height($('.layer_aj_bt_items').height() + 38);
                $(".layer_aj_bt_s").getNiceScroll().resize();
            });
        });
        $('.lmcc-mask-box>.lmcc-mask-open').click(function () {
            $('.lmcc-mask').animate({ "right": "-100%" }, 100, function () {
                $('.lmcc-mask-box').hide();
                $(".layer_aj_bt_s").getNiceScroll().resize();
                $(".maed-cl-top").getNiceScroll().show();
                $(".maed-clb-list").getNiceScroll().show();
                $('.sj-c-details>.lmcc-mask-open').show();
            });
        });
        
        $('.attrLabel').click(function(){
        	$(this).addClass('active').siblings().removeClass('active');
        	$('.attrSwiper').hide();
        	var labelType=$(this).attr('labelType');
        	$('#'+labelType+'_swiper_div').show();
        	$('#'+labelType+'_release').show();
        	if($(this).attr('isLoad')=='0'){
        		var swiper;
	            swiper = new Swiper('#'+labelType+'_swiper', {
	                allowTouchMove: false,
	                navigation: {
	                    nextEl: '#'+labelType+'_swiper_next',
	                    prevEl: '#'+labelType+'_swiper_prev',
	                },
	            });
	            //图片轮番的页数
	            $('#'+labelType+'_swiper_next, #'+labelType+'_swiper_prev').on('click', function () {
	                var swiI = swiper.activeIndex;
	                $('#'+labelType+'_title').html(attrTitleList[labelType+'_'+swiI]);
	                $('#'+labelType+'_time').html(attrTimeList[labelType+'_'+swiI]);
	                swiI += 1;
	                $('.lmcccrb-page p span:visible').eq(0).text(swiI);
	            });
		        if(parseInt($(this).attr('total'))<2){
		        	$('#'+labelType+'_swiper_next').hide();
		        	$('#'+labelType+'_swiper_prev').hide();
		        }
		        if(parseInt($(this).attr('total'))==0){
		        	if (labelType=='img'){
                        $('#'+labelType+'_release').html('<div class="lmcccrb-page" style="margin-top: 0.30rem;"><p>暂无图片</p></div>');
                    }else if (labelType=='sound'){
                        $('#'+labelType+'_release').html('<div class="lmcccrb-page" style="margin-top: 0.30rem;"><p>暂无音频</p></div>');
                    }else if (labelType=='video'){
                        $('#'+labelType+'_release').html('<div class="lmcccrb-page" style="margin-top: 0.30rem;"><p>暂无视频</p></div>');
                    }
		        }else{
		        	$('#'+labelType+'_title').html(attrTitleList[labelType+'_0']);
		        	$('#'+labelType+'_time').html(attrTimeList[labelType+'_0']);
		        }
		        $(this).attr('isLoad','1');
        	}
        });
        
        $('.close').click(function(){
			$('#mask-eventAttr').attr('src','');
			$('#eventAttr').hide();
		});
       
    </script>
</body>
</html>
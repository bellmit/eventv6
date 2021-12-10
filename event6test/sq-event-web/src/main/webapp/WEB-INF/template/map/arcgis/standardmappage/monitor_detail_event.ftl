<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>综治智能监控指挥中心</title>
	<!--zTree 原生样式 -->
	
	<!-- 引入layUI样式 优化样式 -->
	<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/plugins/layui-v2.4.5/layui/css/layui.css"/>
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-1.3.5/themes/gray/easyui.css" />
	<!--本部样式-->
	<!--引入 重置默认样式 statics/zhxc -->
	<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/plugins/swiper-4.1.6/dist/css/swiper.min.css"/>
	<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/common/css/reset.css"/>
	<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/common/css/basic/monitor-air.css"/>
	<#include "/component/maxJqueryEasyUIWin.ftl" />
	<!-- zTree 原生脚本 -->
	<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
	<script src="${uiDomain!''}/web-assets/plugins/layui-v2.4.5/layui/layui.js" charset="utf-8"></script>
	<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-1.3.5/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-last/locale/easyui-lang-zh_CN.js"></script>
</head>

<body>
	<input type="hidden" name="x" id="x" value="${event.resMarker.x!''}" />
	<input type="hidden" name="y" id="y" value="${event.resMarker.y!''}" />
	<input type="hidden" name="eventId" id="eventId" value="${eventId!''}" />
	<!--下面为页面 层-->

	<div class="layer-con-dom" width="100%" height="100%">
		<!--在此处加 页面格式-->
		<!--go-->
		<div class="mr-main-jainc mr-main-jainc1" style="display: block;">
			<!--事件详情-->
			<div class="evt-det clearfix mt25">
				<div class="evt-det-pic fl">
					<div class="swiper-container">
						<div class="swiper-wrapper">
							<div class="swiper-slide">
								<div class="evt-det-pict">
									<#if path1??>
									<img src="${path1}" />
									<#else>
									<img src="${rc.getContextPath()}/images/zanwutupian.png" />
									
									</#if>
								</div>
								<div class="det-ann-des">处理前</div>
							</div>
							<div class="swiper-slide">
								<div class="evt-det-pict">
									<#if path2??>
									<img src="${path2}" />
									<#else>
									<img src="${rc.getContextPath()}/images/zanwutupian.png" />
									
									</#if>
								</div>
								<div class="det-ann-des">处理中</div>
							</div>
							<div class="swiper-slide">
								<div class="evt-det-pict">
									<#if path3??>
									<img src="${path3}" />
									<#else>
									<img src="${rc.getContextPath()}/images/zanwutupian.png" />
									
									</#if>
								</div>
								<div class="det-ann-des">处理后</div>
							</div>
						</div>
						<!-- Add Arrows -->
						<div class="swiper-button-next"></div>
						<div class="swiper-button-prev"></div>
					</div>
				</div>
				<div class="evt-det-cont fl">
					<h1>${event.eventName!''}</h1>
					<ul class="evt-det-content">
						<li>
							<h5>时间：</h5>
							<p class="evt-cor-red">${event.happenTimeStr!''}</p>
						</li>
						<li>
							<h5>地址：</h5>
							<p class="evt-cor-red">${event.occurred!''}</p>
						</li>
						<li>
							<h5>类型：</h5>
							<p>${event.typeName!''}</p>
						</li>
						<li>
							<h5>描述：</h5>
							<p style="max-width: 90%;height: 150px;overflow-y:auto">${event.content!''}</p>
						</li>
					</ul>
				</div>
			</div>
			
			<!--导航-->
			<ul class="evt-nav evt-nav-3 mt30 clearfix">
				<li>
					<a href="javascript: void(0)" onclick="globalsEyeList()"><img src="${uiDomain!''}/web-assets/common/images/basic/mapgrid/icon-evt-nav-1.png" />周边智能监控 </a>
				</li>
				<li>
					<a href="javascript: void(0)" onclick="loadDataList()"><img src="${uiDomain!''}/web-assets/common/images/basic/mapgrid/icon-evt-nav-2.png" />周边网格员 </a>
				</li>
				<li>
					<a href="javascript: void(0)" onclick="disposalEvent()"><img src="${uiDomain!''}/web-assets/common/images/basic/mapgrid/icon-evt-nav-3.png" />事件处置 </a>
				</li>
			</ul>					
			
		</div>
		
		<div class="mr-main-jainc mr-main-jainc2">
		<!--范围选择-->
		<div class="evt-jk-dis clearfix">
			<h5>请选择范围</h5>
			<ul class="evt-jk-dist clearfix" id = "globalsEyeLDistance">
				<li>
					<i class="evt-jk-dist-sel evt-dist-active"></i><!--选中加类evt-dist-active-->
					<em>500m</em>
				</li>
				<li>
					<i class="evt-jk-dist-sel"></i>
					<em>1000m</em>
				</li>
				<li>
					<i class="evt-jk-dist-sel"></i>
					<em>1500m</em>
				</li>
				<li>
					<i class="evt-jk-dist-sel"></i>
					<em>2000m</em>
				</li>
			</ul>
			<div style="float: right;padding-right: 10px">
				<ul class="evt-nav evt-nav-1 mt10 clearfix">
				<li >
					<a style="width:50px " href="javascript: void(0)" onclick="javaScript:$('.mr-main-jainc1').show().siblings().hide()">返回 </a>
				</li>
			</ul>
		</div>
		</div>
		
		<!--视频切换-->
		<div class="evt-jk-con clearfix">
			<ul class="evt-jk-list" id = "globalsEyeL">
				
			</ul>
			<div class="evt-jk-vedio fl">
<!-- 				<div class="evt-jk-ved-top clearfix">
					<div class="evt-jk-ved-big fr">
						<em>监控当前画面</em>
						<a href="javascript: void(0)"></a>
					</div>
				</div> 
				<img src="${uiDomain!''}/web-assets/common/images/basic/mapgrid/icon-evt-jk-vedio.png" /> -->
		      <iframe style="min-width:450;min-height:380"  id="paly_frame" width="100%" height="100%" frameborder="no" scrolling="no" overflow="hidden" src=""></iframe>
			</div>
		</div>
	</div>
	<div class="mr-main-jainc mr-main-jainc3">
		<!--范围选择-->
		<div class="evt-jk-dis clearfix">
			<h5>请选择范围</h5>
			<ul class="evt-jk-dist clearfix" id = "gridDistance"> 
				<li>
					<i class="evt-jk-dist-sel evt-dist-active"></i><!--选中加类evt-dist-active-->
					<em>500m</em>
				</li>
				<li>
					<i class="evt-jk-dist-sel"></i>
					<em>1000m</em>
				</li>
				<li>
					<i class="evt-jk-dist-sel"></i>
					<em>1500m</em>
				</li>
				<li>
					<i class="evt-jk-dist-sel"></i>
					<em>2000m</em>
				</li>
			</ul>
			<div style="float: right;padding-right: 10px">
				<ul class="evt-nav evt-nav-1 mt10 clearfix">
					<li >
						<a style="width:50px " href="javascript: void(0)" onclick="javaScript:$('.mr-main-jainc1').show().siblings().hide()">返回 </a>
					</li>
				</ul>
			</div>
		</div>
		<div class="evt-wgy-con">
			<ul class="evt-wgy-list" id= "gridPerson">
				
			</ul>
		</div>
		
		<!--导航-->
		<ul class="evt-nav evt-nav-1 mt10 clearfix">
			<li>
				<a href="javascript: void(0)" onclick="sendSMS()">指派至事件现场 </a>
			</li>
		</ul>
	</div>
	
	
	<div class="mr-main-jainc mr-main-jainc5"  id="sms_div">
		<div class="mr-mj-event-top">
			<div class="mrmet-item clearfix mt10">
				<div class="mrmet-item-left fl">
					<p>联&nbsp;&nbsp;系&nbsp;&nbsp;人</p>
				</div>
				<input type="text" class="text2" placeholder="电话号码" id="mobilePhones"/>
			</div>
			<div class="mrmet-item-title mt10">
				<p>提示：请直接输入联系人手机号码，若发送多个号码，请用“；”隔开，例如：1890000000；18911111111</p>
			</div>
			<div class="mrmet-item clearfix mt40">
				<div class="mrmet-item-left fl">
					<p>短信内容</p>
				</div>
				<textarea class="text2" id="smsContent"></textarea>
			</div>
		</div>
		<div class="mr-mj-event-button">
			<a href="javascript:fnSendSMS();" class="fl mr-submit">
				<p>发送</p>
			</a>
			<a href="javascript:closeMaxJqueryWindow();" class="fr mr-cancel">
				<p>取消</p>
			</a>
		</div>
	</div>
	
	
	
	
</div>



<script src="${uiDomain!''}/web-assets/plugins/jquery-nicescroll/jquery.nicescroll.js" type="text/javascript" charset="utf-8"></script>

<script src="${uiDomain!''}/web-assets/plugins/echarts-3.2.3/echarts.js"></script>
<script src="${uiDomain!''}/web-assets/common/js/basic/monitor-air.js" type="text/javascript" charset="utf-8"></script>
<script src="${uiDomain!''}/web-assets/common/js/basic/xm-echarts-b.js"></script>
<script src="${uiDomain!''}/web-assets/plugins/swiper-4.1.6/dist/js/swiper.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${uiDomain!''}/web-assets/plugins/jquery-nicescroll/jquery.nicescroll.js" type="text/javascript" charset="utf-8"></script>
</body>

<script>
$(function() {
    //图片居中
	var imgH, imgW, imgboxH, imgboxW;
	var rcontH, contschH, rlistboxH, rightcontentH, titleH ,toptebH;
	$(window).on('load resize', function () {
		imgH = $('.map-imgbox>img').height();
		imgW= $('.map-imgbox>img').width();
		imgboxW = $('.map-imgbox').width();
		imgboxH = $('.map-imgbox').height();
		$('.map-imgbox>img').css({'top': -(imgH - imgboxH)/2, 'left': -(imgW - imgboxW)/2});
	//右边栏目
        rightcontentH = $(window).height();
        titleH = $('.right-content-title').height();
        console.log(titleH);
        toptebH = $('.top-tabbar').height();
        $('.right-content-first').height(rightcontentH - titleH - toptebH);
        rcontH = $('.right-content-first').height(); //
        contschH = $('.cont-sch').height();
        $('.mr-cont-list-box').height(rcontH - contschH + 25); //高度：mr-cont-list-box

	});

	//选择top-tabbar
    $('.top-tabbar-con').on('click','li', function () {
        $(this).addClass('tabbar-active').siblings().removeClass('tabbar-active');
    });
});
</script>

<script>
$(function(){
	//短信推送的页面隐藏
	$('#sms_div').hide();
	
	$('.evt-jk-list, .evt-wgy-list').niceScroll({
	    cursorcolor: "rgba(0,0,0,.2)",//#CC0071 光标颜色
	    cursoropacitymax: 1, //改变不透明度非常光标处于活动状态（scrollabar“可见”状态），范围从1到0
	    touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
	    cursorwidth: "5px", //像素光标的宽度
	    cursorborder: "0", // 游标边框css定义
	    cursorborderradius: "5px",//以像素为光标边界半径
	    autohidemode: false //是否隐藏滚动条
	});
	$('.evt-nav-3').on('click', 'li', function(){
		var index = $(this).index() + 2;
		if(index < 4){
			setTimeout(function(){$(".evt-jk-list, .evt-wgy-list").getNiceScroll().resize();}, 200);
		}
		$('.mr-main-jainc'+index+'').show().siblings().hide();
		$('.layer-con-title').hide();
		$('.evt-title'+index+'').show();
	});
	//手写 弹窗 start
	//页面加载完成后弹窗显示
//     $('.layer-con').show().removeClass('bounceInScale2').addClass('bounceInScale');
//		弹窗淡入
    //$('.pon-w').on('click', function(){
        $('.layer-con').show().removeClass('bounceInScale2').addClass('bounceInScale');
        $(".mr-container").addClass('boxfilter');
        setTimeout(function () {intChart.resize();}, 0);
		setTimeout(function(){
			var swiper = new Swiper('.swiper-container', {
				navigation: {
					nextEl: '.swiper-button-next',
					prevEl: '.swiper-button-prev',
				},
			});
		}, 200);
   // });
//		弹窗淡出
    $('.layer-con-close').on('click', function(){
        $('.layer-con').addClass('bounceInScale2').removeClass('bounceInScale');
        setTimeout(function(){
        	$('.layer-con').hide();
        	$('.mr-main-jainc1').show().siblings().hide();
        	$('.layer-con-title').hide();
			$('.evt-title1').show();
			$(".evt-jk-list, .evt-wgy-list").getNiceScroll().resize();
        }, 300);
        $(".mr-container").removeClass('boxfilter');
    });
    //手写 弹窗 end
    //弹窗内容的切换
    var mrIndex;
    $('.mjtb-box').on('click', 'a', function(){
    	$(this).addClass('active').siblings().removeClass('active');
    	mrIndex = $(this).index();
    	if (mrIndex == 0) {
    		$('.mr-main-jainc2').show().siblings().hide();
    	} else if (mrIndex == 1) {
    		$('.mr-main-jainc3').show().siblings().hide();
    	}
    });
    $('.mr-cancel').click(function(){
    	$('.mr-main-jainc1').show().siblings().hide();
    });
    
    var hdH = $('.header-items').height();
    $('.header-cont').height(hdH);
    var warnH = $('.warn').height();
    $('.warn-l').height(warnH);
    $('.warn-l').on('click', function(){
    	if($(this).hasClass('warn-on')){
    		$(this).removeClass('warn-on');
    		$('.warn').animate({'right': '2px'});
    	}else{
    		$(this).addClass('warn-on');
    		$('.warn').animate({'right': '-310px'});
    	}
    });
    
    //添加周边网格员复选框和单选框的点击事件
    $('.evt-wgy-list').on('click', 'li i', function(){
		$(this).toggleClass('evt-wgy-checked');
	});
	$('#gridDistance').on('click','li i',function(){
		$(this).addClass('evt-dist-active').parent().siblings().children('i').removeClass('evt-dist-active');
		//周边网格员的请求范围距离
		loadDataList();
	});
	
	$('#globalsEyeLDistance').on('click','li i',function(){
		$(this).addClass('evt-dist-active').parent().siblings().children('i').removeClass('evt-dist-active');
		//周边全球眼的请求范围距离
		globalsEyeList();
	});
	
    
});

//获取网格周边信息
function loadDataList() {
	var x = $("#x").val();
	var y = $("#y").val();
	var distance = $("#gridDistance .evt-dist-active").siblings('em').text();
	//清空ul
	$('#gridPerson li').remove();
	$.ajax({
		type: "POST",
		url: '${rc.getContextPath()}/zhsq/map/nanpingVideoDataController/gridListData.json',
		data:{x:x,y:y,distance:distance,time:1},
		dataType:"json",
		success: function(data){
			if(data != null && data!=""){
				for(var i=0;i<data.length;i++){
					var partyName = data[i].PARTY_NAME;
					var mobileTelephone = data[i].MOBILE_TELEPHONE;
					var distance = data[i].DISTANCE;
					if(partyName == null){
						partyName="暂无姓名";
						}
					if(mobileTelephone == null){
						mobileTelephone="暂无电话";
						}
					if(distance == null){
						distance="暂无距离";
						}
					$("#gridPerson").append($("<li><i class='evt-wgy-check'></i>"+
								"<p class='evt-wgy-name'>"+partyName+"</p>"+
								"<p class='evt-wgy-tel'>"+mobileTelephone+"</p>"+
								"<p class='evt-wgy-dis'>距 <em>"+distance+"</em></p></li>"));		
				}
			}
		},
		error:function(data){
			$.messager.alert('错误','获取网格周边失败！','error');
		}
	});
}

//短信页面编辑
function sendSMS() {
	var sms="";
	$(".evt-wgy-checked").each(function(data,i){
		sms+= $($(this).siblings()[1]).html().trim()+";";
	})
	if(sms == "") {
		$.messager.alert('提示','未勾选网格员！','error');
		return;
	}else {
		sms = sms.substring(0,sms.length - 1);//把最后一位的;号去除
	}
	//弹出短信推送的页面，隐藏其他页面
	$('.mr-main-jainc5').show().siblings().hide();
	//把数值添加上去
	$("#mobilePhones").val(sms);
	$("#smsContent").val("${event.occurred}附近发生案件，请速到现场！");
}

//短信发送
function fnSendSMS(){
	var mobilePhoneVal = $("#mobilePhones").val();
	if(mobilePhoneVal.length == 0){
		$.messager.alert('提示','请输入联系人电话号码!','error');
		return;
	}
	
var smsContent = $("#smsContent").val();
	if(smsContent.length==0){
		$.messager.alert('提示','短信内容不能为空!','error');
		return;
	}

	$.ajax({
        url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfNanpingAqiController/sendsms.json',
        type: 'POST',
       data: { mobilePhones:mobilePhoneVal,smsContent:smsContent},
        dataType:"json",
        error: function(data){
        	 $.messager.alert('提示','短信发送异常!','error');
        },
        success: function(data){
			if(data.flag){
				$.messager.alert('提示','短信推送成功！','info');
				$("#smsContent").val('');
				$("#mobilePhones").val('');
				$('.mr-main-jainc1').show().siblings().hide();
			}else {
				 $.messager.alert('提示','短信发送失败!','error');
			}
		}
	});
}


//获取全球眼周边信息
function globalsEyeList() {
	var x = $("#x").val();
	var y = $("#y").val();
	var distance = $("#globalsEyeLDistance .evt-dist-active").siblings('em').text();
	//清空ul
	$('#globalsEyeL li').remove();
	$.ajax({
		type: "POST",
		url: '${rc.getContextPath()}/zhsq/map/nanpingVideoDataController/globalsEyeList.json',
		data:{x:x,y:y,distance:distance,time:1},
		dataType:"json",
		success: function(data){
			if(data != null && data!=""){
				for(var i=0;i<data.length;i++){
					if(i==0){
						playVideo(data[i].MONITOR_ID);
					}
					var platformName = data[i].PLATFORM_NAME;
					var distance = data[i].DISTANCE;
					if(platformName == null){
						platformName="暂无姓名";
						}
					if(distance == null){
						distance="暂无距离";
						}
					$("#globalsEyeL").append($("<li onclick='playVideo("+data[i].MONITOR_ID+")'><p>"+platformName+"</p>"+
								"<em>距<i>"+distance+"</i></em></li>"));		
				}
			}
		},
		error:function(data){
			$.messager.alert('错误','无法获取地图引擎！','error');
		}
	});
}
//播放视频
function playVideo(id){
	if($("#paly_frame").attr("src")!=''){
		　　$(window.document).contents().find("#paly_frame")[0].contentWindow.closeW();
	}
	$("#paly_frame").attr("src","${SQ_ZZGRID_URL!''}/zzgl/map/data/situation/globalEyesPlay.jhtml?monitorId="+id);
}

//条用事件处置详情信息
function disposalEvent() {
	var eventId = $("#eventId").val();
	var url = '${rc.getContextPath()}/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=todo&eventId='+eventId;
	
	//MaxJqueryWindow_0
	parent.showMaxJqueryWindow1("事件详情", url,1000,550); 
}

</script>
</html>
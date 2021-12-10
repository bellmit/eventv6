<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"  style="background: transparent;overflow:hidden">
<head>
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>信息定位</title>
	<!--引入 重置默认样式 statics/zhxc -->
	<link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/common/css/reset.css" />
	<link rel="stylesheet" type="text/css"
		href="${uiDomain}/web-assets/_big-screen/nanchang-cc/css/nc-rightlist.css" />
	<!-- zTree 原生脚本 -->
	<script src="${uiDomain}/web-assets/common/js/jqry-9-1-12-4.min.js" type="text/javascript" charset="utf-8">
	</script>
	<script src="${uiDomain}/web-assets/common/js/basic/jquery.nicescroll.js" type="text/javascript" charset="utf-8"></script>
	

	<script type="text/javascript">
		var winW, scale;
		$(window).on('load resize', function () {
			fullPage();
			setTimeout(fullPage, 10); //二次执行页面缩放，解决全屏浏览时滚动条问题
			function fullPage() { //将页面等比缩放
				winW = $(window.parent.parent).width();//获取iframe外层window的宽度，只在服务器模式下生效，本地文件调试时需指定屏幕宽度
				//winW = 1920; //本地调试时应指定屏幕宽度，以开发者评估分辨率为准
				winH = $(window).height();
				var whdef = 100 / 1920;
				var rem = winW * whdef; // 以默认比例值乘以当前窗口宽度,得到该宽度下的相应FONT-SIZE值
				$('html').css('font-size', rem + "px");
			}
		});
	</script>
</head>

<body style="background-color: transparent;">
<div class="dianwei-top">
				<a href="#" class="rc-content-items rc-content-items3  flex flex-clm">
					<div class="rcc-items-tital flex">

						<p>${pointInfo.pointName!''}
						</p>

					</div>
					<div class="rcc-items-row flex ">

						<p class="flex1">${pointInfo.pointNo!''}</p>
						<#if pointInfo.pointLevelStr='A'>
						<i class="bg-red">
						<#elseif pointInfo.pointLevelStr='B'>
						<i class="bg-green">
						<#else>
						<i class="bg-blue">
						</#if>
						
						${pointInfo.pointLevelStr!''}类点位</i>
					</div>
					<div class="rcc-items-row flex">

						<p class="flex1">${pointInfo.pointTypeStr!''}</p>
					</div>
					<div class="rcc-items-row flex mb10">

						<p class="rcc-font1 flex1">${pointInfo.pointAddress!''}</p>
					</div>
					<hr>
					<div class="rcc-items-row flex mtr10">

						<p class="font-blod flex1">点位负责人</p>
						<p class="flex1">${pointInfo.pointManager!''}</p>
					</div>
					<div class="rcc-items-row flex mtr10">

						<p class="font-blod flex1">负责人电话</p>
						<p class="flex1">${pointInfo.pmTel!''}</p>
					</div>
					<hr>
					<div class="rcc-items-row rcc-font2 mtr10">
					<p class="font-blod">实地考察标准</p>
						<p><#if text??>${text!''}</#if></p>
					</div>
				</a>

			</Div>



			<div class="xqiwr-center-title2">
				<div class="xqi-echarts-box-title2 clearfix">
					<a href="javascript:void(0);" class="fl active" style="width:50%">
						<p>责任管理</p>
					</a>
					<a href="javascript:void(0);" class="fl" style="width:50%">
						<p>巡访问题</p>
					</a>
				</div>
			</div>


			<div class="clearfix  xqi-video-box2">
				<div class="xqi-videx-item2 fl xqi-videx-item-on2">

					<div class="rc-content rc-content2">
						<div class="rc-content-box rc-content-box2" style="height:3.2rem;">

							<div class="right-content-second right-content-second1 right-content-second1-1">

								<a href="#" title="部门领导、点位主要领导、 区主管单位督导员" class="rc-content-items  rc-content-items2 flex flex-clm" style="border-bottom: 1px dashed #2064a4">
									<div class="rcc-items-tital flex">

										<img
											src="${uiDomain}/web-assets/_big-screen/nanchang-cc/images/icon-yuandian01.png" />
										<p>二级负责人
										</p>

									</div>
									<div class="rcc-items-row rcc-items-row2 flex">

										<p class="flex1">姓名：<span>${pointInfo.orgCodeManager!''}</span></p>

									</div>
									<div class="rcc-items-row rcc-items-row2 flex">

										<p class="flex1">电话:<span>${pointInfo.ocmTel!''}</span></p>
									</div>


								</a>
								<a href="#" title="县区级分管领导、市主管单位督导员" class="rc-content-items rc-content-items2  flex flex-clm" style="border-bottom: 1px dashed #2064a4">
									<div class="rcc-items-tital flex">

										<img
											src="${uiDomain}/web-assets/_big-screen/nanchang-cc/images/icon-yuandian01.png" />
										<p>三级负责人
										</p>

									</div>
									<div class="rcc-items-row rcc-items-row2 flex">

										<p class="flex1">姓名：<span>${pointInfo.countyManager!''}</span></p>

									</div>
									<div class="rcc-items-row rcc-items-row2 flex">

										<p class="flex1">电话:<span>${pointInfo.cmTel!''}</span></p>
									</div>


								</a>
							</div>


						</div>
					</div>
				</div>
				<div id="eventListDiv" class="xqi-videx-item2 fl ">
					<div class="rc-content">
						<div class="rc-content-box rc-content-box2" style="height: calc(100% - 0.4rem);    padding: 0rem 0.1rem;">

							<div  style="" class="right-content-second right-content-second1 right-content-second1-1">
								<div id="reEventList"></div>
							</div>


							<div class="rc-bottom ">
								<div class="rc-bottom-row">
									<span class="fl ml5">第</span>
									<input id="pagination-num" onfocus="this.select()"; type="text" class="page fl" value="1" />
									<span id="totalpage" class="fl"></span>
									<!--active-page-->
									<a id="next_page" href="#" class="fr mr15">
										<img
											src="${uiDomain}/web-assets/_big-screen/nanchang-cc/images/right_icon-1a.png" />
									</a>
									<!--当不可点击是添加类 active1-->
									<a id="prev_page" href="#" class="fr mr5">
										<img src="${uiDomain}/web-assets/_big-screen/nanchang-cc/images/right_icon-1a.png"
											class="rotate180" />
									</a>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="xqi-videx-item2 fl ">


				</div>
			</div>
			
			<script>
			
			var pageSizeNum=2;
			
		$(function () {
		    //console.log($(window).height());
		    
		    $('#eventListDiv').css("height",($(window).height())*0.63);

            getPointReEventInfo(1,'3601');
			//滚动条美化

			$('.xqi-videx-item, .dianwei-top').niceScroll({
				cursorcolor: "rgba(0,0,0,.2)", //#CC0071 光标颜色
				cursoropacitymax: 1, //改变不透明度非常光标处于活动状态（scrollabar“可见”状态），范围从1到0
				touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
				cursorwidth: "4px", //像素光标的宽度
				cursorborder: "0", // 游标边框css定义
				cursorborderradius: "4px", //以像素为光标边界半径
				autohidemode: false //是否隐藏滚动条
			});
			//滚动条美化

			//设备点位列表切换
			var ind;
			$('.xqi-echarts-box-title').on('click', ' a', function () {
				$(this).addClass('active').siblings('a').removeClass('active');
				ind = $(this).index();
				$('.xqi-videx-item').eq(ind).addClass('xqi-videx-item-on').siblings().removeClass('xqi-videx-item-on');
			})
			//设备点位列表切换

			//点位详情切换
			var ind;
			$('.xqi-echarts-box-title2').on('click', ' a', function () {
				$(this).addClass('active').siblings('a').removeClass('active');
				ind = $(this).index();
				$('.xqi-videx-item2').eq(ind).addClass('xqi-videx-item-on2').siblings().removeClass(
					'xqi-videx-item-on2');
				$('.xqi-videx-item-on2').niceScroll().resize();
				
			})
			//点位详情切换

			$('.rc-content-items-a').click(function () {
				$('.xqiwr-center-1').css('display', 'none');
				$('.xqiwr-center-2').css('display', 'block');
				var dianH = $('.dianwei-top').height();
				var xqiwrH = $('.xqiwr-center-title2').height();
				//$('.xqi-video-box2').height(contH - headerH - 38 - dianH - xqiwrH);
			});
			
			$('.icon-fanghui').click(function () {
				$('.xqiwr-center-2').css('display', 'none');
				$('.xqiwr-center-1').css('display', 'block');
			});
			
			/* var opts = {center: [parseFloat(${gis.CEN_LON}),parseFloat(${gis.CEN_LAT}) ]}
			window.parent.parent.showPointById(opts); */
		});
		
		var currPageNum=1;
		var totalPageNum=0;

		
		
	
	    //获取点位所关联的事件详情
		function getPointReEventInfo(pageNo,infoOrgCode){
		$.ajax({
		type: "POST",
		url: '${rc.getContextPath()}/zhsq/nanChang3D/getPointReEventInfo.json',
		dataType : "json",
		data: {pointId:"${pointInfo.pointId}",pageNo:pageNo,pageSize:pageSizeNum}, 
		success: function (data) {
		    
		    //初始化页码
		    totalPageNum=((data.total)/pageSizeNum)>1?Math.ceil((data.total)/pageSizeNum):1;
		    $("#totalpage").html("页/"+totalPageNum+"页");
		    
		    formatPage(currPageNum,totalPageNum);
		    
			setReEvent(data);
			
		}

	    })
	    
	    }
	    
	    
	function setReEvent(data){
	var str = "",d="";
	for(var i=0,l=data.rows.length;i<l;i++){
		d = data.rows[i];
		str+="<a onclick='getDetailEvent("+d.eventId+","+d.bizPlatform+","+d.status+",1,"+d.instanceId+")' href='#' class='rc-content-items rc-content-items2  flex flex-clm'>"+
		"<div class='rcc-items-tital flex'>"+
		"<img src='${uiDomain}/web-assets/_big-screen/nanchang-cc/images/icon-yuandian01.png' />"+
		"<p>"+d.eventName+"</p>"+
		"</div>"+
		"<div class='rcc-items-row rcc-items-row2 flex'>"+
		"<p class='flex1'>"+fomPlatform(d.bizPlatform,d.patrolType)+"-"+d.eventClass+"</p>";
		var statusStr=d.status;
		if(statusStr=='03'||statusStr=='04'){
		    str+="<i class='color-blue'>已结案</i>";
		}else if(statusStr=='06'){
		    str+="<i class='color-black'>已作废</i>";
		}else{
		    str+="<i class='color-green'>处理中</i>";
		}
		str+="</div>"+
		"<div class='rcc-items-row rcc-items-row3 flex mt10'>"+
		"<p class='flex1'>于<span>"+d.happenTimeStr+"</span>在<span>"+d.occurred+"</span>发生</p>"+
		"</div></a>";

	}

	$("#reEventList").html(str);
	}
	
	
	function fomPlatform(str,type){
    if(str == null ||str == 'null' || str == undefined){
        return '';
    }else if(str=='3601002'){
        return 'AI分析事件';
    }else if(null!=type&&0!=type){
        return '巡访事件';
    }else{
        return '平台事件';
    }
}
	
	//页码
	$("#prev_page").click(function() {
		var currPageNumInput = parseInt($("#pagination-num").val());
		if (currPageNumInput > 1) {
		    currPageNum=currPageNum-1;
		    $("#pagination-num").val(currPageNum);
		    if(currPageNum>1){
		    $("#prev_page").removeClass("active-page");
		    }else{
		    $("#prev_page").addClass("active-page");
		    }
			getPointReEventInfo(currPageNum,'3601');
		}
	});
	
	$("#next_page").click(function() {
		var currPageNumInput = parseInt($("#pagination-num").val());
		if (currPageNumInput < totalPageNum) {
		    currPageNum=currPageNum+1;
		    $("#pagination-num").val(currPageNum);
		    if(currPageNum<totalPageNum){
		    $("#next_page").removeClass("active-page");
		    }else{
		    $("#next_page").addClass("active-page");
		    }
			getPointReEventInfo(currPageNum,'3601');
		}
	});
	
	$("#pagination-num").bind("input propertychange",function() {
		
			var currPageNumInput = parseInt($("#pagination-num").val());
			if(!isNaN(parseInt(currPageNumInput))){
			
			currPageNum=currPageNumInput;
			
			
			if (currPageNumInput > totalPageNum) {
			    currPageNum=totalPageNum;
			    $("#pagination-num").val(currPageNum);
				getPointReEventInfo(totalPageNum,'3601');
			} else if (currPageNum < 1) {
			    currPageNum=1;
			    $("#pagination-num").val(1);
				getPointReEventInfo(1,'3601');
			} else {
				getPointReEventInfo(currPageNum,'3601');
			}
			
			}else{
			    $("#pagination-num").val(currPageNum);
			}
			
			formatPage(currPageNum,totalPageNum);
		
	});
	
	
	//切换事件详情
	function getDetailEvent(eventid,bizplatform,status,instanceId){
	
		
	    $('#title_p',window.parent.parent.document).html("事件待办");
	    $('.mask-vedio',window.parent.parent.document).removeClass('mask-container-on');
	    $('.mask-dianwei',window.parent.parent.document).removeClass('mask-container-on');
	    
	    $('.mask-container',window.parent.parent.document).css('display', 'block');
	    $('.mci-big2',window.parent.parent.document).css('display', 'block');
		
		$('.mc-item',window.parent.parent.document).addClass('mci-big').removeClass('mci-small');
		$('.mask-container',window.parent.parent.document).fadeIn(200);
		$('.mask-container-on',window.parent.parent.document).removeClass('mask-container-on');
		$('.mask-event',window.parent.parent.document).addClass('mask-container-on');
		$('.mask-event',window.parent.parent.document).contents().find(".mae-event-handle").removeClass('mae-container-on');
		$('.mask-event',window.parent.parent.document).contents().find("#ascrail2000").css('display','none');
		$('.mask-event',window.parent.parent.document)[0].contentWindow.showEventInfo(eventid,bizplatform,status,'1',instanceId);
		
		
	}
	
	
	function formatPage(currPageNum,totalPageNum){
	
	        if(currPageNum>1){
		    $("#prev_page").addClass("active-page");
		    }else{
		    $("#prev_page").removeClass("active-page");
		    }
		    
		    if(currPageNum<totalPageNum){
		    $("#next_page").addClass("active-page");
		    }else{
		    $("#next_page").removeClass("active-page");
		    }
	}
		
		
	</script>
	
</body>
</html>

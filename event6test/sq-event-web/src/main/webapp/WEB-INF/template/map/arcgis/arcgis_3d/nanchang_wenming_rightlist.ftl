<!DOCTYPE html>
<html>

<head>
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>信息定位</title>
	<!--引入 重置默认样式 statics/zhxc -->
	<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/common/css/reset.css" />
	<link rel="stylesheet" type="text/css"
		href="${uiDomain!''}/web-assets/_big-screen/nanchang-cc/css/nc-rightlist.css" />
	<!-- zTree 原生脚本 -->
	<script src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js" type="text/javascript" charset="utf-8">
	</script>
	<script src="${rc.getContextPath()}/js/map/arcgis/arcgis_3d/ffcsMap3D.js"></script>
	<script src="${uiDomain!''}/web-assets/common/js/basic/jquery.nicescroll.js" type="text/javascript" charset="utf-8"></script>
	<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
	<script type="text/javascript">
		var winW, scale;
		$(window).on('load resize', function () {
			fullPage();
			setTimeout(fullPage, 10); //二次执行页面缩放，解决全屏浏览时滚动条问题
			function fullPage() { //将页面等比缩放
				winW = $(window.parent).width();//获取iframe外层window的宽度，只在服务器模式下生效，本地文件调试时需指定屏幕宽度
				//winW = 1920; //本地调试时应指定屏幕宽度，以开发者评估分辨率为准
				winH = $(window).height();
				var whdef = 100 / 1920;
				var rem = winW * whdef; // 以默认比例值乘以当前窗口宽度,得到该宽度下的相应FONT-SIZE值
				$('html').css('font-size', rem + "px");
			}
		});
	</script>
	<style>
	.ex{display:none}
	.div2{
		float: left;
	    width: 0.2rem;
	    height: 0.28rem;
	    margin-top: 5px;
	   }
	   .xqiwr-center-1 {
		    height: -webkit-calc(100% - .4rem);
		    height: -moz-calc(100% - .4rem);
		    height: calc(100% - .4rem);
		    background-image: linear-gradient(90deg, rgba(0, 41, 101, 0.7) 0%, rgba(37, 88, 165, 0.7) 100%), linear-gradient(90deg,rgba(0, 41, 101, 0.7) 0%, rgba(37, 88, 165, 0.7) 100%);
		}
	</style>
</head>

<body style="background-color: transparent;">


	<div class="container">
	<input type="hidden" name="orgCode" id="orgCode" value="${orgCode}"/>
	<input type="hidden" name="infoOrgCode" id="infoOrgCode" value="${infoOrgCode}"/>
	<input type="hidden" name="line" id="line" value="${line}"/>
	<input type="hidden" id="pageSize" value="5" />
	<input type="hidden" id="mapt" value="5" />
		<!-- 设备点位列表切换 -->
		<div class="xqiwr-center xqiwr-center-1 xqi-bs clearfix" id="xqiwr-center-1">
			<div class="xqiwr-center-title">
				<div class="xqi-echarts-box-title clearfix">
					<a href="javascript:void(0);" class="fl ex" id="device">
						<p>监控列表</p>
					</a>
					<a href="javascript:void(0);" class="fl ex active" id="point">
						<p>点位列表</p>
					</a>
				</div>
			</div>
			<div class="xqi-video-box clearfix" id="xqi-video-box">
				<div id="deviceDiv" class="xqi-videx-item fl" >
					<div class="rc-content">
						<div class="rc-content-box rc-content-box1">
								<div class="input-server">
										<ul>											
											<li class="li1">
												<Div class="div1"><input type="text" name="lname"  class="input-server1" id="camSearch" placeholder="请输入关键词"  /></Div>
												<Div class="div2"><img style="cursor:pointer"  src="${uiDomain!''}/web-assets/_big-screen/nanchang-cc/images/icon-rm-close.png" id="deleteBtn" /></Div>
											</li>
											<li class="li2">
												<img style="cursor:pointer"  src="${uiDomain!''}/web-assets/_big-screen/nanchang-cc/images/icon-server01.png" id="searchBtn" />
											</li>		
										</ul>
								</div>								
								<div class="right-content-second right-content-second1" id="camdiv" style="margin-bottom: 0.4rem;">								
							</div>
							<div class="rc-bottom " id="videoPageDiv">
								<div class="rc-bottom-row">
									<span class="fl ml5">第</span>
									<input type="text" id="curPageVideo" onfocus="this.select()"; class="page fl" value="1" />
									<span class="fl" >页/<span id="totalPage">1</span>页</span>
									<a href="#" class="fr mr15" id="nextVideoPage" onclick="nextVideoPage()">
										<img
											src="${uiDomain!''}/web-assets/_big-screen/nanchang-cc/images/right_icon-1a.png" />
									</a>
									<!--当不可点击是添加类 active1-->
									<a href="#" class="fr mr5 " id="preVideoPage" onclick="preVideoPage()">
										<img src="${uiDomain!''}/web-assets/_big-screen/nanchang-cc/images/right_icon-1a.png"
											class="rotate180" />
									</a>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div id="pointDiv" class="xqi-videx-item fl xqi-videx-item-on">
					<div class="rc-content">
						<div class="rc-content-box rc-content-box1">
							<div class="right-content-second right-content-second1">
								<div class="input-server">
										<ul>											
											<li class="li1">
												<Div class="div1"><input type="text" id="spointName" name="lname"  class="input-server1"  placeholder="请输入关键词" /></Div>
												<Div class="div2"><img style="cursor:pointer"  src="${uiDomain!''}/web-assets/_big-screen/nanchang-cc/images/icon-rm-close.png" id="dele_point" /></Div>
											</li>
											<li class="li2" onclick="searchPointListByName()">
												<img style="cursor:pointer"  src="${uiDomain!''}/web-assets/_big-screen/nanchang-cc/images/icon-server01.png" />
											</li>
										</ul>
								</div>
							     <!-- 点位项模板 -->
								<div style="display: none;">
									<a id="defaultPoint" href="#" class="rc-content-items rc-content-items-a  flex flex-clm" >
										<div class="rcc-items-tital flex">
											<img src="${uiDomain!''}/web-assets/_big-screen/nanchang-cc/images/icon-yuandian01.png" />
											<p id="pointName"> </p>
										</div>
										<div class="rcc-items-row flex">
											<p class="flex1" id="pointNo"></p>
											<i class="bg-red" id="pointLevel"></i>
										</div>
										<div class="rcc-items-row flex">
											<p class="flex1" id="pointType"></p>
										</div>
										<div class="rcc-items-row flex">
											<p class="flex1" id="pointAddress"></p>
										</div>
                                        <div class="rcc-items-row flex">
                                            <p class="flex1" id="gridPath"></p>
                                        </div>
									</a>
								</div>
								<div id="poinList" style="margin-bottom: 0.4rem;"> </div>
							</div>


							<div class="rc-bottom " id="pointPageDiv">
								<div class="rc-bottom-row">
									<span class="fl ml5">第</span>
									<input type="text" id="curPage" onfocus="this.select()"; class="page fl" value="1" />
									<span class="fl" >页/<span id="pageCount">1</span>页</span>
									<a href="#" class="fr mr15"  id="nextPage" onclick="nextPage()">
										<img src="${uiDomain!''}/web-assets/_big-screen/nanchang-cc/images/right_icon-1a.png" />
									</a>
									<!--当不可点击是添加类 active1-->
									<a href="#" class="fr mr5" id="prePage" onclick="prePage()">
										<img src="${uiDomain!''}/web-assets/_big-screen/nanchang-cc/images/right_icon-1a.png"
											class="rotate180" />
									</a>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>

		</div>
		<!-- 设备点位列表切换 -->


		<!-- 点位详情切换 -->
		<div class="xqiwr-center xqiwr-center-2 xqi-bs clearfix" id="xqiwr-center-2">

			<div class="dianwei-fanhui">
				<img class="icon-fanghui" style="cursor: pointer;" id="icon-fanghui" 
					src="${uiDomain}/web-assets/_big-screen/nanchang-cc/images/icon-fanhui.png" />

				<p class="p-fanghui" style="cursor: pointer;" id="p-fanghui">返回点位列表</p>
			</div>
			
			<div id="point_detail"></div>
			
			
		</div>

	</div>
	<!-- 点位详情切换 -->

	<script>
		var u = navigator.userAgent, app = navigator.appVersion;   
		var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android终端或者uc浏览器
	
		function tabChange(){
            if(window.parent.$("#deviceId").hasClass('to-analy-active')&&window.parent.pointPosition==true){//都有
				$('#point').show().css("width","50%");
				$('#device').show().css("width","50%");
				//$('#device').addClass('active').siblings().removeClass('active');
	            //$('#deviceDiv').eq(0).addClass('xqi-videx-item-on').siblings().removeClass('xqi-videx-item-on');
			}else if(window.parent.$("#deviceId").hasClass('to-analy-active')==false&&window.parent.pointPosition==true){//只有点位
				$('#device').hide();
				$('#point').show().css("width","100%");
				$('#point').addClass('active').siblings().removeClass('active');
	            $('#pointDiv').eq(0).addClass('xqi-videx-item-on').siblings().removeClass('xqi-videx-item-on');
			}else if(window.parent.$("#deviceId").hasClass('to-analy-active')&&window.parent.pointPosition==false){//只有设备
				$('#devicepoint').hide();
				$('#device').show().css("width","100%");
				$('#device').addClass('active').siblings().removeClass('active');
	            $('#deviceDiv').eq(0).addClass('xqi-videx-item-on').siblings().removeClass('xqi-videx-item-on');
			}
		}
		
		$(function () {

			//滚动条美化

			$('.xqi-videx-item, .xqi-videx-item-on,.right-content-second1').niceScroll({
				cursorcolor: "rgba(0,0,0,.2)", //#CC0071 光标颜色
				cursoropacitymax: 1, //改变不透明度非常光标处于活动状态（scrollabar“可见”状态），范围从1到0
				touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
				cursorwidth: "2px", //像素光标的宽度
				cursorborder: "0", // 游标边框css定义
				cursorborderradius: "2px", //以像素为光标边界半径
				autohidemode: false //是否隐藏滚动条
			});
			//滚动条美化

			//设备点位列表切换
			var ind;
			$('.xqi-echarts-box-title').on('click', ' a', function () {
				$(this).addClass('active').siblings('a').removeClass('active');
				ind = $(this).index();
				$('.xqi-videx-item').eq(ind).addClass('xqi-videx-item-on').siblings().removeClass('xqi-videx-item-on');
				$('.xqi-videx-item').niceScroll().resize();
			})
			//设备点位列表切换

			//点位详情切换
			/* var ind;
			$('.xqi-echarts-box-title2').on('click', ' a', function () {
				$(this).addClass('active').siblings('a').removeClass('active');
				ind = $(this).index();
				$('.xqi-videx-item2').eq(ind).addClass('xqi-videx-item-on2').siblings().removeClass('xqi-videx-item-on2');
				$('.xqi-videx-item-on2').niceScroll().resize();
				
			}) */
			//点位详情切换

			$('#defaultPoint').click(function () {
				$('#xqiwr-center-1').css('display', 'none');
				$('#xqiwr-center-2').css('display', 'block');
				var dianH = $('.dianwei-top').height();
				var xqiwrH = $('.xqiwr-center-title2').height();
				$('.xqi-video-box2').height(contH - headerH - 38 - dianH - xqiwrH);
			});
			
			$('#icon-fanghui ,#p-fanghui').click(function () {
				$('#xqiwr-center-2').css('display', 'none');
				$('#xqiwr-center-1').css('display', 'block');
				window.parent.$.fn.ffcsMap.removeLayer("highLightGridLayer", {}, function() {});
				window.parent.isPointDetail =false;
				window.parent.window.pointInfoListData();
				window.parent.window.camlistData();
				window.parent.window.defaultpointInfoByCiv();//显示区域责任人
			});
			tabChange();
		});
		//适配平板enter键
		$("#camSearch").keypress(function (e) {
            if (e.which == 13) {
         	   	camSearch=$("#camSearch").val();
	       			window.parent.$("#camSearch").val(camSearch);
	       			getCamlist(1,rows,orgCode,companyType,camSearch,"2");
	       			window.parent.getCamPoint('','4','2',camSearch);//撒点定位 
            }
		});
		$("#spointName").keypress(function (e) {
            if (e.which == 13) {
            		searchPointListByName();
            }
		});
		
		function showPointInfo1(pointid,pointType,infoOrgCode,pointTypeStr,strucPointAry){

            if (infoOrgCode.length >= 6) {
                window.parent.countrysideZoom()
            }
			window.parent.isPointListDetail=true;
			window.parent.pointDetailId=pointid;
			var strucPoint= window.parent.strucPointArray[pointid];
			var opts = {center: [parseFloat(strucPoint.x),parseFloat(strucPoint.y) ]}
			window.parent.showPointById(opts);
			showPointInfo(pointid,pointType,pointTypeStr,strucPointAry);
		}
		function showPointInfo(pointid,pointType,pointTypeStr,strucPointAry){
			
			$('#xqiwr-center-1').css('display', 'none');
		    $('#xqiwr-center-2').css('display', 'block');
		    window.parent.isPointDetail=true;
		    
			var str="<iframe class='mask-event' src='${rc.getContextPath()}/zhsq/nanChang3D/pointSelectionInfo.jhtml?pointId="+pointid+"' frameborder='0' width='100%' height='100%'></iframe>"
            $("#point_detail").html(str);
            $("#point_detail").height($('#myiframe1',window.parent.document).height());
			var dianH = $('.dianwei-top').height();
			var xqiwrH = $('.xqiwr-center-title2').height();
			$('.xqi-video-box2').height(contH - headerH - 38 - dianH - xqiwrH);
			if(window.parent.isPointListDetail==false){//撒点或轮廓点击
				window.parent.showPointDetail(pointid,strucPointAry);
			}
			//获取工作部责任人
			window.parent.workDepartmentInfoByCiv(pointType,pointTypeStr,window.parent.infoOrgCode);
		}

		// 弹窗高度设置
		var contH, headerH, footerH;
		$(window).on('load resize', function () {
			contH = $(window).height();
			headerH = $('.xqiwr-center-title').outerHeight(true);
			footerH = $('.rc-bottom').outerHeight(true);
			$('#xqi-video-box').height(contH - headerH - footerH);
		});

		//var orgCode = $("#infoOrgCode").val();
		//var orgCode = $("#orgCode").val();
		var orgCode = '';
		var mapt = $("#mapt").val();
		var companyType = "4";
		var currpage = 1;
		var rows=10;
		//var rows=$("#pageSize").val();
		var totalPage = 0;
		var camSearch=$("#camSearch").val();
		var line = "";
		$(function(){			
			 $("#curPageVideo").bind("input propertychange",function() {
					var curPageNum = parseInt(this.value);
					if(!isNaN(curPageNum)){
						currpage=curPageNum;
					} 
					getCamlist(currpage,rows,orgCode,companyType,camSearch,"2");
					
				
			});
			camSearch=window.parent.$("#camSearch").val();
			$("#camSearch").val(camSearch);
			var spointName =window.parent.$("#pointSearch").val();
			$("#spointName").val(spointName);
		});
		
			
		$("#deleteBtn").click(function(){
			$("#camSearch").val("");
			window.parent.$("#camSearch").val("");
			getCamlist(1,rows,orgCode,companyType,"","2");
			window.parent.getCamPoint('','4','2','');//撒点定位
		});
		$("#searchBtn").click(function(){
			camSearch=$("#camSearch").val();
			window.parent.$("#camSearch").val(camSearch);
			getCamlist(1,rows,orgCode,companyType,camSearch,"2");
			window.parent.getCamPoint('','4','2',camSearch);//撒点定位
		});
		 //上一页
		 function preVideoPage(){
			 if($("#videoPageDiv #preVideoPage").hasClass("active-page")){
				 getCamlist(currpage-1,rows,orgCode,companyType,camSearch,"2");  
			 };
			
		 }
		 //下一页
		 function nextVideoPage(){
			 if($("#videoPageDiv #nextVideoPage").hasClass("active-page")){
				 getCamlist(currpage+1,rows,orgCode,companyType,camSearch,"2");  
			 };
		 }
		 
		
		//监控设备列表
		function getCamlist(pageNo,size,orgCode_,companyType,camSearch,line){
			var pointTypeStr;
			if(window.parent.orgType=="1"){
				pointTypeStr="";
			}else{
				if(window.parent.$('#mask-dianwei').attr("src")==""){
					pointTypeStr="N";
				}else{
					pointTypeStr=window.parent.pointTypes;
				}
			}
			if(pageNo<1)pageNo=1;
			 if(totalPage!=0&&pageNo>totalPage){
				 pageNo=totalPage;
			 }
			 currpage=pageNo;//设置当前页
			 $("#curPageVideo").val(currpage);
			 orgCode = orgCode_;
			 //var extent = window.parent.$.fn.ffcsMap.getViewExtent();
			 //var xmax=extent.xmax,xmin=extent.xmin,ymax=extent.ymax,ymin=extent.ymin;
			$.ajax({
					type: 'POST',
					url: '${rc.getContextPath()}/zhsq/map/DDEarthController/listData.json',
					dataType: 'json',
					data:{
						/* "ymax":ymax,
				     	"xmin":xmin,
				     	"xmax":xmax,
				     	"ymin":ymin, */
						"page":pageNo,
						"rows":size,
						"camSearch":camSearch,
						"orgCode":orgCode,
						"line":line,
						"companyType":companyType,
						"pointTypes":pointTypeStr
					},
					success: function(data) {
						var pointName=$("#spointName").val();
						var total=data.total;//总条数
						totalPage=parseInt((total + rows -1) / rows);//总共页数
						if(pageNo==1){
							$("#preVideoPage").removeClass("active-page");
						}else{
							$("#preVideoPage").addClass("active-page");
						}
						if(pageNo==totalPage){
							$("#nextVideoPage").removeClass("active-page");
						}else{
							$("#nextVideoPage").addClass("active-page");
						}
						$("#totalPage").html(totalPage);
						
						
						$("#camdiv").empty();
						var html= '';
						if(data.rows && data.rows.length>0) {
							for(var i=0;i<data.rows.length;i++){  
								var camobj =data.rows[i];
								var cam ={code:camobj.channelId,name:camobj.platformName,useType:camobj.useType};
								window.parent.camLayerObj[camobj.monitorId]=cam;
								html += '<a onclick="getVideoDetail(\''+camobj.channelId+'\','+camobj.monitorId+','+camobj.useType+',\''+camobj.platformName+'\')" href="#" class="rc-content-items flex flex-clm">';
								html += '<div class="rcc-items-tital flex">';
								html += '<img src="${uiDomain}/web-assets/_big-screen/nanchang-cc/images/icon-yuandian01.png" />';
								html += '<p>'+ camobj.platformName+ '</p>';
								html += '</div>';
								html += '<div class="rcc-items-row clearfix">';
								if(camobj.loginStatus=="1"){
									html += '<i class="bg-green fr">'+"在线"+'</i>';
								}else{
									html += '<i class="bg-red fr">'+"离线"+'</i>';
								}
								html += '</div>';
								html += '<div class="rcc-items-row flex">';
								html += '<p class="flex1">'+ camobj.channelId +'</p>';
								html += '</div>';
								html += '</a>';
							}
						} else {
		                    html+='<div  style="text-align: center;font-size:12px;color:white" ><p class="flex1">暂无数据</p></div>';
		                    $("#totalPage").html(0);
		                    $("#curPageVideo").val(0);  
							$("#preVideoPage").removeClass("active-page");
							$("#nextVideoPage").removeClass("active-page");
		                }
						$("#camdiv").html(html);
						$('.xqi-videx-item').niceScroll().resize();
					},
					error: function(data) {
						alert("获取监控设备列表数据出错！");
					}
				});
		}
		
		
		
		//视频详情
		function getVideoDetail(indexCode,monitorId,useType,platformName){
			window.parent.camMonitorId=monitorId;
			window.parent.isCamListDetail=true;
			var x,y;
			$.ajax({
				type: 'POST',
				url: '${rc.getContextPath()}/zhsq/map/DDEarthController/getPointByResourcesId.json',
				dataType: 'json',
				async: false,
				data:{
					"monitorById":monitorId
				},
				success: function(data) {
					x=data.x;
					y=data.y;
					var opts = {
							center: [parseFloat(data.x),parseFloat(data.y) ]
						}
					window.parent.showPointById(opts);
					/* setTimeout(function () {
			        }, 500); */
						
				},
				error: function(data) {
					alert("获取详情定位数据出错！");
				}
			});
			
			if(isAndroid){ 
			//if(navigator.userAgent.match("ua_ffcs")){
				var durl;
				if(useType == '1'){
					$.ajax({
						type: "POST",
						url: '${rc.getContextPath()}/zhsq/map/DDEarthController/getVideoUrl.json',
						//dataType:"json",
						async: false,
						data:{
							"indexCode":indexCode
						},
						success: function(data){
							if(data && data.length == 0){
								return;
							}
							var json = eval('(' + data + ')'); 
							if(json.code=="0"){
								durl=json.data.url;
								
							}else{
								alert("获取预览url失败");
							}
						}
					});
				}else if(useType == '2'){
					$.ajax({
						type: "POST",
						url: '${rc.getContextPath()}/zhsq/map/DDEarthController/getAntelopeVideoUrl.json',
						//dataType:"json",
						async: false,
						data:{
							"indexCode":indexCode
						},
						success: function(data){
							durl=data;
						}
					});
				}
				
				var gridName=window.parent.gridName;
				var gridId=window.parent.gridId;
				var json = {"monitor_code":indexCode,"monitor_name":platformName,"url":durl,"x":x,"y":y,"grid_name":gridName,"grid_id":gridId} ;
				window.location.href="getPlayerUrl:" + JSON.stringify(json);
			}else{
				$('#title_p',window.parent.document).html("视频详情");
				window.parent.isPointDetail=true;
				window.parent.$('.mask-container').css('display', 'block');
				window.parent.$('.mask-container').css('height', '0%');
				window.parent.$('.mci-big2').css('display', 'block');
				window.parent.$('.mask-vedio mask-container-on').css('display', 'block');
				$('.mask-container .mask-event',window.parent.document).removeClass('mask-container-on');
				window.parent.getVideoDetail(indexCode,monitorId,useType);
				window.parent.$("#monitorId").val(monitorId);
				window.parent.$("#video_name").html(platformName);
				window.parent.$("#video_code").html(indexCode);
				
				$('#mc-item',window.parent.document).addClass('mci-big').removeClass('mci-small');
				$('#popupWindowDiv',window.parent.document).fadeIn(200);
				//$('#popupWindowDiv',window.parent.document).fadeOut(200);
				$('.mask-container-on',window.parent.document).removeClass('mask-container-on');
				$('#mask-vedio',window.parent.document).addClass('mask-container-on');
			}
			
			
			if(window.parent.lastGraphic != null){
				window.parent.lastGraphic.symbol=new window.parent.$.fn.ffcsMap.gPictureMarkerSymbol()({
					url : window.parent.lastpic,
					width : 28,
					height : 36,
					xoffset : 0,
					yoffset : 18
				});
			}
			
			/* window.parent.layerPointObj[monitorId].symbol= new window.parent.$.fn.ffcsMap.gPictureMarkerSymbol()({
				url : "${uiDomain!''}/web-assets/_big-screen/nanchang-cc/images/icon-point-online_click.png",
				width : 28,
				height : 36,
				xoffset : 0,
				yoffset : 18
			}); */
			
			//window.parent.lastGraphic=window.parent.layerPointObj[monitorId];
			//window.parent.lastpic=window.parent.layerPointObj[monitorId].attributes.data.iconUrl;
		}
		
	</script>
<script type="text/javascript">
 var curPage=1;//当前页
 var pageSize=10;//每页条数
 var infoOrgCode=""; 
 var pageCount=0;
 var pointLevels="";
 var pointTypes="";
 $(function(){
	 $("#curPage").bind("input propertychange",function() {
			
			var curPageNum = parseInt(this.value);
			if(!isNaN(curPageNum)){
				curPage=curPageNum;
			} 
			pointInfoList(curPage,pageSize,infoOrgCode);
		
	});


         searchPointListByName();


 });
 //上一页
 function prePage(){
	 if($("#pointPageDiv #prePage").hasClass("active-page")){
		 pointInfoList(curPage-1,pageSize,infoOrgCode);  
	 };
	
 }
 //下一页
 function nextPage(){
	 if($("#pointPageDiv #nextPage").hasClass("active-page")){
		 pointInfoList(curPage+1,pageSize,infoOrgCode);  
	 };
 }
 function searchPointListByName(){
	 spointName=$("#spointName").val();
	 window.parent.$("#pointSearch").val(spointName);
	 pointInfoList(1,pageSize,infoOrgCode);  
	 parent.drawPolygon();
 }
 var struc3DAry = new Array();
 //点位列表
 function pointInfoList(page,pageSize,infoOrgCode_,pointLevels_,pointTypes_){
     struc3DAry = []
	$('#xqiwr-center-2').css('display', 'none');
	$('#xqiwr-center-1').css('display', 'block');

	 infoOrgCode=infoOrgCode_;
	 if(page<1)page=1;
	 if(pageCount!=0&&page>pageCount){
		 page=pageCount;
	 }
	 curPage=page;//设置当前页
	 $("#curPage").val(curPage);
	 var pointName=$("#spointName").val();
	 var url='${rc.getContextPath()}/zhsq/nanChang3D/findPointInfoList.json?mapt=5&page='+page+'&rows='+pageSize+'&infoOrgCode='+infoOrgCode+'&pointName='+pointName;
	 if(typeof pointLevels_ != "undefined"){//点位
		 pointLevels=pointLevels_;
	 }
	 if(typeof pointTypes_ != "undefined"){//点位类型
		 pointTypes=pointTypes_;
	 }
	 url+='&pointLevels='+pointLevels;
	 url+='&pointTypes='+pointTypes;
	 //var extent = window.parent.$.fn.ffcsMap.getViewExtent();
	 //var maxlng=extent.xmax,minlng=extent.xmin,maxlat=extent.ymax,minlat=extent.ymin;
	 //url+='&maxlng='+maxlng+'&minlng='+minlng+'&maxlat='+maxlat+'&minlat='+minlat;
	// modleopen(); //打开遮罩层  
		$.ajax({
			type: 'POST',
			url: url,
			dataType: 'json',
			success: function(data) {  
				var total=data.total;//总条数
				window.parent.$("#pointTotal").text(total);
				if(total==0){//没有数据
					$("#pageCount").html(0);
					$("#curPage").val(0);  
					$("#prePage").removeClass("active-page");
					$("#nextPage").removeClass("active-page"); 
					$("#poinList").html("<div  style='text-align: center;font-size:12px;color:white' ><p class='flex1'>暂无数据</p></div>");  
			        return;
				}
				pageCount=parseInt((total + pageSize -1) / pageSize);//总共页数
				if(page==1){
					$("#prePage").removeClass("active-page");
				}else{
					$("#prePage").addClass("active-page");
				}
				if(page==pageCount){
					$("#nextPage").removeClass("active-page");
				}else{
					$("#nextPage").addClass("active-page");
				}
				$("#pageCount").html(pageCount);
				var clonePointObj;
				$("#poinList").html("");  
				var PointArray = {};

				for (var i = 0; i < data.rows.length; i++) { //点位列表
					strucPoint= {
							id : data.rows[i].id,   
							iconUrl :  "${uiDomain!''}/images/map/gisv0/map_config/unselected/icon-new-point-"+data.rows[i].pointLevel,
							iconWidth : 28,
							iconHeight : 38,
							fontColor :'#fff',// 文字颜色
							fontSize : "18pt",
							bizData : {},// 存储业务数据
							x : data.rows[i].x, 
							y : data.rows[i].y,
							text : data.rows[i].name,
							pointType : data.rows[i].pointType,
							pointTypeStr : data.rows[i].pointTypeStr
						}; 
					if(data.rows[i].checkType=='001' && data.rows[i].pointLevel=='A'){
						strucPoint.iconUrl+="x.png";
					}else{
						strucPoint.iconUrl+=".png";
					}
					PointArray[data.rows[i].id]=strucPoint;
					clonePointObj = $('#defaultPoint').clone();
					clonePointObj.attr("id","point_"+i);
					clonePointObj.attr("href","javaScript:showPointInfo1("+data.rows[i].id+",\""+data.rows[i].pointType+"\",\""+data.rows[i].infoOrgCode+"\",\""+data.rows[i].pointTypeStr+"\",\""+''+"\")");
					$("#poinList").append(clonePointObj);
					$("#point_"+i+" #pointName").html(data.rows[i].name);//点位名称
					$("#point_"+i+" #pointNo").html(data.rows[i].pointNo);//点位编号
					$("#point_"+i+" #pointNo").attr("title",data.rows[i].pointNo);
					$("#point_"+i+" #pointType").html(data.rows[i].pointTypeStr);
					$("#point_"+i+" #pointAddress").html(data.rows[i].pointAddress);
                    $("#point_"+i+" #gridPath").html(data.rows[i].gridPath);
					var pointLevelStr=data.rows[i].pointLevel+"类点位"
					$("#point_"+i+" #pointLevel").html(pointLevelStr);
					$("#point_"+i+" #pointLevel").removeClass();
					if(data.rows[i].pointLevel=='A'){
						$("#point_"+i+" #pointLevel").addClass("bg-red"); // 追加样式
					}else if(data.rows[i].pointLevel=='B'){
						$("#point_"+i+" #pointLevel").addClass("bg-green"); // 追加样式
					}
					else if(data.rows[i].pointLevel=='C'){
						$("#point_"+i+" #pointLevel").addClass("bg-blue"); // 追加样式
					}
                    var obj ={useType:"",
						text:data.rows[i].name,
						id:data.rows[i].id,
						indexCode:"",
						x:parseFloat(data.rows[i].x),
						y:parseFloat(data.rows[i].y),
                        iconWidth : 28,
                        iconHeight : 38,
                        fontColor :'#fff',// 文字颜色
                        fontSize : "18pt",
                        bizData : {},// 存储业务数据
                        x : data.rows[i].x,
                        y : data.rows[i].y,
                        text : data.rows[i].name,
                        pointType : data.rows[i].pointType,
                        pointTypeStr : data.rows[i].pointTypeStr,
                        infoOrgCode:data.rows[i].infoOrgCode,
                        iconUrl :  "${uiDomain!''}/images/map/gisv0/map_config/unselected/icon-new-point-"+data.rows[i].pointLevel
					}

                        if(data.rows[i].checkType=='001' && data.rows[i].pointLevel=='A'){
                            obj.iconUrl+="x.png";
                    }else{
                            obj.iconUrl+=".png";
                    }
                    struc3DAry.push(obj);

				}

                //搜索时撒点
                window.parent.cityPoint(struc3DAry)
				window.parent.strucPointArray=PointArray;
				$('.xqi-videx-item').niceScroll().resize();
			},
			error: function(data) {
				alert('点位列表获取出错！');
			},
			complete : function() {
				//modleclose(); //关闭遮罩层
			}
		}); 
	 
 }
	$("#dele_point").click(function(){
		$("#spointName").val("");
		searchPointListByName();
		 
	});
</script>
</body>

</html>
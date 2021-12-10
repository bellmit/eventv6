<!DOCTYPE html>
<html>

<head>	
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"> 
	<title>3D可配置地图首页</title>
	<!--引入 重置默认样式 statics/zhxc -->
	<!--zTree 原生样式 -->
	<link rel="stylesheet" href="${uiDomain!''}/app-assets/plugins/swiper-4.1.6/dist/css/swiper.min.css" />
	<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/plugins/zTree_v3/css/demo.css">
	<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/plugins/zTree_v3/css/zTreeStyle/zTreeStyle.css">
	<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/common/css/reset.css" />
	<link rel="stylesheet" href="${uiDomain!''}/web-assets/_big-screen/nanchang-cc/css/index-mask.css">
	<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/_big-screen/nanchang-cc/css/index.css" />
	<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/_frame/zhxc/css/ace/font-awesome.min.css" />
	<link rel="stylesheet" type="text/css" href="${uiDomain}/js/layer/skin/layer.css" />
	<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/_big-screen/nanchang-cc/css/index-new.css" />
	<script src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="${uiDomain!''}/app-assets/plugins/swiper-4.1.6/dist/js/swiper.min.js"  type="text/javascript" ></script>
<style>
		.esri-view .esri-view-root{position: static;flex:1 1 100%;border:none;padding:0;margin:0;-webkit-tap-highlight-color:rgba(0,0,0,0);-webkit-tap-highlight-color:transparent;}
		.w65{width:0.85rem;}
		.w50{width:0.65rem;}
		.map-wrapper{background:none;}
		.cont-data3-tt>li, .cont-data3-bt>li {
		    float: left;
		    width:49.5%;
		    height: 100%;
		    text-align: center;
		    font-weight: bold;
		    font-size: .14rem;
		}
		.mask-vedio{display:none}
		.mouseStyle{
			cursor:default;
		}
		#civ_name{
			max-width: 1.25rem;
		    overflow: hidden;
		    white-space: nowrap;
		    text-overflow: ellipsis;
		}
		.bg{background: black}
		.dianwei-shipin-content1 {
		    position: relative;
		    height: 4.4rem;
		    width: 100%;
		}
		.dianwei-shipin-content1 .ul-sipin1 li {
		    width: 100%;
		    height: 100%;
		}
		.dianwei-shipin-content1 .ul-sipin1>li>div {
		    width: 100%;
		    height: 100%;
		    overflow: hidden;
		}
		.dianwei-shipin-content1 .ul-sipin3>li {
		    display: inline-block;
		    float: left;
		    width: calc(100%/3);
		    height: 1.33rem;
		    box-sizing: border-box;
		    padding: 0.1rem;
		}
		.support-title{
			overflow: hidden;
			text-overflow: ellipsis;
			white-space: nowrap;
			text-align: left;
			vertical-align: top;
			line-height: 0.26rem;
			position: relative;
			top: 0.3rem;
		}
	</style>
	
	<script type="text/javascript">
		var u = navigator.userAgent, app = navigator.appVersion;   
		var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android终端或者uc浏览器
		function logout(){
			//if(navigator.userAgent.match("ua_ffcs_nc")){
			if(isAndroid){  
				window.location.href="protocol://android?out_account:" ;
			}else{
				window.location.href = "${SQ_UAM_URL}/admin/loginOut";
			}
		}
		
		function changeOrg(){
			//if(navigator.userAgent.match("ua_ffcs_nc")){
			if(isAndroid){ 
				window.location.href="protocol://android?switch_org:";
			}else{
				window.location.href = "${SQ_UAM_URL}/admin/switchOrg";
			}
		}
	
		function pwdModify(){
			 var pages2=layer.open({
			    type: 2, //page层
			    area: ['520px', '400px'],
			    title: false,
			    closeBtn: true,
			    shade: 0.6, //遮罩透明度
			    shift: 2, //0-6的动画形式，-1不开启
			    content: "${SQ_UAM_URL}/admin/pwdModifyv2.jhtml?loginName="+'${user.regisValue}'+"&loginPwd="+'${user.passwordShow}'+"&r="+Math.random()+"&isOrigLayerClose=1",	    
			    cancel: function(){
			    }
			}); 
		}
		function xffxShow(){
			$('#xffxDiv').css({"display":"block","height":"100%"});
			$('#xffxDiv').addClass('event-sw-on');
			$('#xffxIframe').attr('src',"${SQ_BI_URL}/report/cv/index.jhtml");
		}
		function xffxHide(){
			$('#xffxDiv').css({"display":"none","height":"0%"});
			$('#xffxDiv').removeClass('event-sw-on');
			$('#xffxIframe').attr('src',"");
		}
		function clsbShow(){
			$('#clsbDiv').css({"display":"block","height":"100%"});
			$('#clsbDiv').addClass('event-sw-on');
			//$('#clsbIframe').attr('src',"${OA_DOMAIN}/web/boardController/index.mhtml");
			$('#clsbIframe').attr('src',"http://192.168.52.127:10021/sq-oa-web/web/boardController/index.mhtml");
		}
		function clsbHide(){
			$('#clsbDiv').css({"display":"none","height":"0%"});
			$('#clsbDiv').removeClass('event-sw-on');
			$('#clsbIframe').attr('src',"");
		}
		function clsbsysShow(){
			$('#clsbsys').css({"display":"block"});
		}
		function clsbsysHide(){
			$('#clsbsys').css({"display":"none"});
		}
		function AIfxShow(){
			var partyName='${user.regisValue}';
			$('#AIfxfDiv').css({"display":"block","height":"100%"});
			$('#AIfxfDiv').addClass('event-sw-on');
			$('#AIfxIframe').attr('src',"http://192.168.52.127:17043/ctm01civilized-web/#/alarmList?username="+partyName);
		}
		function AIfxHide(){
			$('#AIfxfDiv').css({"display":"none","height":"0%"});
			$('#AIfxfDiv').removeClass('event-sw-on');
			$('#AIfxIframe').attr('src',"");
		}
		function szfxShow(){
			$('#szfxDiv').css({"display":"block","height":"100%"});
			$('#szfxDiv').addClass('event-sw-on');
			$('#szfxIframe').attr('src',"${SQ_BI_URL}/report/digitalAnalysis/index.jhtml");
		}
		function szfxHide(){
			$('#szfxDiv').css({"display":"none","height":"0%"});
			$('#szfxDiv').removeClass('event-sw-on');
			$('#szfxIframe').attr('src',"");
		}
		//医疗卫生
		function qsfxShow(){
			$('#qsfxDiv').css({"display":"block","height":"100%"});
			$('#qsfxDiv').addClass('event-sw-on');
			$('#qsfxIframe').attr('src',"${SQ_BI_URL}/report/nanChangEpidemicSituation/index.jhtml");
			//$('#qsfxIframe').attr('src',"${SQ_BI_URL}/report/digitalAnalysis/index.jhtml");
		}
		function qsfxHide(){
			$('#qsfxDiv').css({"display":"none","height":"0%"});
			$('#qsfxDiv').removeClass('event-sw-on');
			$('#qsfxIframe').attr('src',"");
		}
		//综合信息	zhxx
		function zhxxShow(){
			$('#zhxxDiv').css({"display":"block","height":"100%"});
			$('#zhxxDiv').addClass('event-sw-on');
			$('#zhxxIframe').attr('src',"http://192.168.52.127:10001/zzgrid/zzgl/grid/zhxx/zhxx.jhtml");
		}
		function zhxxHide(){
			$('#zhxxDiv').css({"display":"none","height":"0%"});
			$('#zhxxDiv').removeClass('event-sw-on');
			$('#zhxxIframe').attr('src',"");
		}
		//一人疫档	yryd
		function yrydShow(){
			$('#yrydDiv').css({"display":"block","height":"100%"});
			$('#yrydDiv').addClass('event-sw-on');
			$('#yrydIframe').attr('src',"${SQ_BI_URL}/report/nanChangPrevention/index.jhtml");
		}
		function yrydHide(){
			$('#yrydDiv').css({"display":"none","height":"0%"});
			$('#yrydDiv').removeClass('event-sw-on');
			$('#yrydIframe').attr('src',"");
		}
		//来蒙人员	lcry
		function lcryShow(){
			$('#lcryDiv').css({"display":"block","height":"100%"});
			$('#lcryDiv').addClass('event-sw-on');
			$('#lcryIframe').attr('src',"${SQ_BI_URL}/report/nanChangYiQing/index.jhtml");
		}
		function lcryHide(){
			$('#lcryDiv').css({"display":"none","height":"0%"});
			$('#lcryDiv').removeClass('event-sw-on');
			$('#lcryIframe').attr('src',"");
		}
		//社区工作  sqgz
		function sqgzShow(){
			$('#sqgzDiv').css({"display":"block","height":"100%"});
			$('#sqgzDiv').addClass('event-sw-on');
			$('#sqgzIframe').attr('src',"http://192.168.52.127:10008/report/nanChangBigScreen/index");
		}
		function sqgzHide(){
			$('#sqgzDiv').css({"display":"none","height":"0%"});
			$('#sqgzDiv').removeClass('event-sw-on');
			$('#sqgzIframe').attr('src',"");
		}
		//物资保障	wzbz
		function wzbzShow(){
			$('#wzbzDiv').css({"display":"block","height":"100%"});
			$('#wzbzDiv').addClass('event-sw-on');
			$('#wzbzIframe').attr('src',"http://192.168.52.127:10008/report/medical/index");
		}
		function wzbzHide(){
			$('#wzbzDiv').css({"display":"none","height":"0%"});
			$('#wzbzDiv').removeClass('event-sw-on');
			$('#wzbzIframe').attr('src',"");
		}
	</script>
</head>
<body>
	<!-- 登录基本信息 -->
	<form name="gridForm" method="post" action="${rc.getContextPath()}/admin/grid.shtml" target="_self">
	    <input type="hidden" name="gridId" id="gridId" value="${gridId?c}"/>
	    <input type="hidden" name="gridCode" id="gridCode" value="${gridCode}"/>
	    <input type="hidden" name="orgCode" id="orgCode" value="${orgCode}"/>
	    <input type="hidden" name="socialOrgCode" id="socialOrgCode" value="${socialOrgCode}"/>
	    <input type="hidden" name="homePageType" id="homePageType" value="${homePageType}"/>
	    <input type="hidden" name="gridName" id="gridName" value="${gridName}"/>
	    <input type="hidden" name="gridLevel" id="gridLevel" value="${gridLevel}"/>
	    <input type="hidden" name="SQ_ZZGRID_URL" id="SQ_ZZGRID_URL" value="${SQ_ZZGRID_URL}"/>
	    <input type="hidden" name="POPULATION_URL" id="POPULATION_URL" value="${POPULATION_URL}"/>
	    <input type="hidden" name="camSearch" id="camSearch" value=""/>
	    <input type="hidden" name="pointSearch" id="pointSearch" value=""/>
	    <input type="hidden" name="monitorId" id="monitorId" value=""/>
	</form>
	
	
	<!-- 热力图 -->
	<div class="container-fluid">
		<div class="wrap-container">
			<!-- 地图展示层 start -->
			<div id="map0" class="map-wrapper wrap-container">
			</div>
			<div id="heatLayer" >
			</div>
			<!-- 地图展示层 end -->

			<!-- 图表、点位展示 start -->
			<div class="chart-wrapper">
				<div class="chart-cont-hot">
				</div>
				<div class="chart-cont-bar">
				</div>
			</div>
			
			<div class="point-wrapper">
				<!-- 在线、离线点位 start -->
				<div class="point-online point-pos2" id="oint-online"></div>
				<div class="point-offline point-pos3" id="point-offline"></div>
				<!-- 在线、离线点位 end -->

				<!-- A、B、C类点位start -->
				<div class="point-sort-a point-pos4"></div>
				<div class="point-sort-b point-pos5"></div>		
				<!-- A、B、C类点位end -->
	
				
				<!-- 区域详细数据 start -->
				<div class="area-data evt-pos5" id="evt-pos5" style="display: none;">
					<div class="cont-panel cont-panel-deep">    
					<div style="float: right;height: 20px;line-height: 0px;padding:0px 5px 10px 5px;cursor: pointer;z-index: 10;position: relative;"  >
					<img onclick="javascript:$('#evt-pos5').hide()" alt="" src="${uiDomain!''}/web-assets/_big-screen/nanchang-cc/images/icon-rm-close.png"></div>
					
						<h1 id="p_name" style="width: 80%"></h1>
						<div class="cont-data1" id="civ_area">
							<p><span>责任人姓名</span><em id="pointManager"></em></p>
							<p><span>责任人电话</span><span id="pmTel"></span></p>
						</div>
						<div class="cont-data2">
							<h1>事件总数<em id="total_event">0</em></h1>
							<div class="cont-data3">
								<ul class="cont-data3-tt">
									<li>处理中</li>
									<li>已结案</li>
								</ul>
								<ul class="cont-data3-bt">
									<li id="doing_total_">0</li>
									<li id="settles_total_">0</li>
								</ul>
							</div>
							<h1>点位总数<em id="total_point">0</em></h1>
							<div class="cont-data3">
								<ul class="cont-data3-tt">
									<li>A类</li>
									<li>B类</li>
									<!-- <li>C类</li> -->
								</ul>
								<ul class="cont-data3-bt">
									<li id="total_a">0</li>
									<li id="total_b">0</li>
									<!-- <li id="total_c">0</li> -->
								</ul>
							</div>
							<h1>监控总数<em id="total_mont">0</em></h1>
							<div class="cont-data3">
								<ul class="cont-data3-tt">
									<li>在线</li>
									<li>离线</li>
								</ul>
								<ul class="cont-data3-bt">
									<li id="total_on">0</li>
									<li id="total_off">0</li>
								</ul>
							</div>
						</div>
					</div>
				</div>
				<!-- 区域详细数据 end -->
				
			</div>
			<!-- 图表、点位展示 end -->

			<!-- 弹窗层 start -->
			<div id="popupWindowDiv" class="mask-container" style="display: none">
				<div class="mc-item mci-big mci-big2" id="mc-item" style="display:block; top:2.29rem; margin-top:0">
                    <!-- 小号弹窗用mci-small -->
                    <div class="xqm-im-top">
						<i></i>
						<!-- <p>待办事件</p> -->
						<p id="title_p">待办事件</p>
						<a href="javascript:void(0);" class="mc-close" id="mc-close"></a>
					</div>
					<div class="big-mask-content">
						<!-- 待办事件 -->
						<iframe  id="mask-eventID" class="mask-event" src="${rc.getContextPath()}/zhsq/nanChang3D/eventList.jhtml" frameborder="0" width="100%" height="100%"></iframe>

						<!-- 视频播放、一键督办弹窗 -->
						<div class="mask-vedio" id="mask-vedio">
						<div class="mae-container mae-container-on dianwei-center1" id="dianwei-center1">
					        <div class="dianwei-shipin xqi-bs clearfix"  style="background:none; border:none;">
					            <div class="dianwei-shipin-title clearfix" id="dianwei-shipin-title">
					                <a href="javascript:void(0);" id="titleA" class="fl active">
					                    <p>单屏显示</p>
					                </a>
					                <a href="javascript:void(0);" class="fl">
					                    <p>四屏显示</p>
					                </a>
					                <a href="javascript:void(0);" class="fl">
					                    <p>九屏显示</p>
					                </a>
					            </div>
					            
					            <div class="clearfix  dianwei-shipin-content">
					                <div class="dianwei-shipin-content1 fl dianwei-shipin-content1-on">
					                    <ul id="videoUl" class="ul-sipin1" style="width:100%;height:4.0rem; border:solid 1px rgba(5, 197, 244, 0.4);box-sizing: border-box;">
					                        <li > 
							                    <div class='bg' onclick="selectDiv(this)" id="vli0" index=0  play="0"></div>
					                    	</li> 
					                    </ul> 
					                    <div class="shangbao" id="shangbao">
					                        <p class="p1">一键督办</p>
					                    </div>
										
					                </div>
									 <div style="font-size:0.2rem; color:#fff;line-height: 0.3rem;padding-left: 0.1rem;"><p>监控名称：<span id="video_name"></span></p></div>
									 <div style="font-size:0.2rem; color:#fff;line-height: 0.3rem;padding-left: 0.1rem;"><p>监控编号：<span id="video_code"></span></p></div>
					            </div>
					        </div>
					    </div>
					    
					    <div class="mae-container shijian-center1" id="shijian-center1" style="margin-left:-5rem">
					    	<div class="shangbao-content xqi-bs clearfix">
								<div class="clearfix  shangbao-content-right" style="width: 9.6rem">
					       	 		<iframe id="event_frame" width="100%" height="100%" frameborder="no" scrolling="no" overflow="hidden" ></iframe>
					     		</div>
					    	</div>
					    </div>
					    </div>
					   <iframe id="diyuIframe" class="mask-diyu" frameborder="0" width="100%" height="100%" src="${rc.getContextPath()}/zhsq/nanChang3D/toDiyu.jhtml?infoOrgCode=${orgCode}"></iframe>
					</div>
				</div>
			</div>
			<!-- 弹窗层 end -->
			<!-- 巡访分析start -->
			<div  id='xffxDiv'   style="z-index:50;position: absolute;width:100%; height: 0%;" >
				<iframe id='xffxIframe' src="" frameborder="0" width="100%" height="100%"></iframe>
			</div>
			<!-- 巡访分析end -->
			<!-- 材料上报start -->
			<div  id='clsbDiv'   style="z-index:50;position: absolute;width:100%; height: 0%;" >
				<iframe id='clsbIframe' src="" frameborder="0" width="100%" height="100%"></iframe>
			</div>
			<!-- 材料上报end -->
			<!-- AI分析start -->
			<div  id='AIfxfDiv'   style="z-index:50;position: absolute;width:100%; height: 0%;" >
				<iframe id='AIfxIframe' src="" frameborder="0" width="100%" height="100%"></iframe>
			</div>
			<!-- AI分析end -->
			<!-- 大数据分析start -->
			<div  id='szfxDiv'   style="z-index:50;position: absolute;width:100%; height: 0%;" >
				<iframe id='szfxIframe' src="" frameborder="0" width="100%" height="100%"></iframe>
			</div>
			<!-- 大数据分析end -->
			
			<!-- 医疗卫生start -->
			<div  id='qsfxDiv'   style="z-index:50;position: absolute;width:100%; height: 0%;" >
				<iframe id='qsfxIframe' src="" frameborder="0" width="100%" height="100%"></iframe>
			</div>
			<!-- 医疗卫生end -->
			
			<!-- 综合信息start -->
			<div  id='zhxxDiv'   style="z-index:50;position: absolute;width:100%; height: 0%;" >
				<iframe id='zhxxIframe' src="" frameborder="0" width="100%" height="100%"></iframe>
			</div>
			<!-- 综合信息end -->
			
			<!-- 一人疫档start -->
			<div  id='yrydDiv'   style="z-index:50;position: absolute;width:100%; height: 0%;" >
				<iframe id='yrydIframe' src="" frameborder="0" width="100%" height="100%"></iframe>
			</div>
			<!-- 一人疫档end -->
			
			<!-- 来蒙人员start -->
			<div  id='lcryDiv'   style="z-index:50;position: absolute;width:100%; height: 0%;" >
				<iframe id='lcryIframe' src="" frameborder="0" width="100%" height="100%"></iframe>
			</div>
			<!-- 来蒙人员end -->
			
			<!-- 社区工作start -->
			<div  id='sqgzDiv'   style="z-index:50;position: absolute;width:100%; height: 0%;" >
				<iframe id='sqgzIframe' src="" frameborder="0" width="100%" height="100%"></iframe>
			</div>
			<!-- 社区工作end -->
			
			<!-- 物资保障start -->
			<div  id='wzbzDiv'   style="z-index:50;position: absolute;width:100%; height: 0%;" >
				<iframe id='wzbzIframe' src="" frameborder="0" width="100%" height="100%"></iframe>
			</div>
			<!-- 物资保障end -->
			
			<!-- 头部、导航start -->
			<div class="header clearfix" style="z-index:100;">
				<div class="nav clearfix">
					<div class="struc-wrap clearfix" data-count='0'>
						<div class="struc-cont " >
	                        <div class="swiper-container swiper1">
	                            <div class="swiper-wrapper">
								<#if (zhz??)>	
									<div id="zhxx" class="swiper-slide struc-item nav-item-on" style="font-size:0.22rem">
										<a href="javascript:;"  >
											<p style="color: white;">综合信息</p>
										</a>
	                                </div>
								</#if>
								
								<#if (fcz??)>
									<div id="lcry" class="swiper-slide struc-item" style="font-size:0.22rem">
										<a href="javascript:;"  >
											<p style="color: white;">来蒙人员</p>
										</a>
	                                </div>
								</#if>	
								<#if (sqz??)>
									<div id="sqgz" class="swiper-slide struc-item" style="font-size:0.22rem">
										<a href="javascript:;"  >
											<p style="color: white;">社区工作</p>
										</a>
	                                </div>
								</#if>
								<#if (ylz??)>
									<div id="qsfx" class="swiper-slide struc-item" style="font-size:0.22rem">
										<a href="javascript:;"  >
											<p style="color: white;">医疗卫生</p>
										</a>
	                                </div>
								</#if>
								<#if (wzz??)>
									<div id="wzbz" class="swiper-slide struc-item" style="font-size:0.22rem">
										<a href="javascript:;"  >
											<p style="color: white;">物资保障</p>
										</a>
	                                </div>
								</#if>
								<#if (spz??)>	
									<div id="zhdd" class="swiper-slide struc-item" style="font-size:0.22rem">
										<a href="javascript:;" >
											<p style="color: white;">交通视频</p>
										</a>
									</div>
								</#if>
								<#if (yqfx??)>
									<div id="yqfxDiv" class="swiper-slide struc-item" style="font-size:0.22rem">
										<a href="http://syyq.cubigdata.cn/logon.action" target="_blank">
											<p style="color: white;">舆情分析</p>
										</a>
	                                </div>
								</#if>
								<#if (lly??)>
									<div id="managerDiv" class="swiper-slide struc-item" style="font-size:0.22rem">
										<a href="${SQ_UAM_URL}/admin/nccc_sub_admin?menuName=管理后台" target="_blank">
											<p style="color: white;">管理后台</p>
										</a>
	                                </div>
								</#if>
									<!--<div id="xxbdDiv" class="swiper-slide struc-item" style="font-size:0.22rem">
										<a href="http://10.18.247.130:8082/#/home/index" id="xxbd"  target='_blank'  rel="noreferrer">
											<p style="color: white;">信息比对</p>
										</a>
	                                </div>
									<#if (yryd??)>	
										<div id="yryd" class="swiper-slide struc-item" style="font-size:0.22rem">
											<a href="javascript:;"  >
												<p style="color: white;">一人疫档</p>
											</a>
										</div>
									</#if>
									<div id="sjypDiv" class="swiper-slide struc-item" style="font-size:0.22rem">
										<a href="http://10.18.247.130:8082/#/home/bigData" id="sjyp"  target='_blank'  rel="noreferrer">
											<p style="color: white;">数据研判</p>
										</a>
	                                </div>
									
	                            	<div id="szfx" class="swiper-slide struc-item nav-item-on" style="font-size:0.22rem">
										<a href="javascript:;"  >
											<p style="color: white;">大数据分析</p>
										</a>
	                                </div>
	                                <div id="zhdd" class="swiper-slide struc-item" style="font-size:0.22rem">
										<a href="javascript:;" >
											<p style="color: white;">视频监控</p>
										</a>
									</div>
									
									<div id="clsb" class="swiper-slide struc-item" style="font-size:0.22rem">
										<a href="javascript:;"  >
											<p style="color: white;">材料上报</p>
										</a>
	                                </div>
	                                <div id="aifxDiv" class="swiper-slide struc-item" style="font-size:0.22rem">
										<a href="" id="aifx"  target='_blank'  rel="noreferrer">
											<p style="color: white;">AI分析</p>
										</a>
	                                </div>
	                                <#if chiefLevel=="2" || chiefLevel=="3">
		                                <div id="xffx" class="swiper-slide struc-item" style="font-size:0.22rem">
		                                	<a href="javascript:;"  >
												<p style="color: white;">巡访分析</p>
											</a>
		                                </div>
	                                </#if>
	                                <div id="managerDiv" class="swiper-slide struc-item" style="font-size:0.22rem">
										<a href="${SQ_UAM_URL}/admin/nccc_sub_admin?menuName=管理后台" target="_blank">
											<p style="color: white;">管理后台</p>
										</a>
	                                </div>-->
	                            </div>
	                        </div>
	                        <div class="swiper-button-next"></div>
	                        <div class="swiper-button-prev"></div>
                    	</div>
					</div>
				</div>
					<a href="javascript:;" title="内蒙古自治区智慧城市指挥调度平台" class="logo">
						<img src="${uiDomain!''}/web-assets/_big-screen/nanchang-cc/images/shouye/top-title.png">
					</a>
				<div style="float:right;">
					<a href="javascript:;" id="gridNameClick" class="fr to-manage1" style="float:left" ><i></i><span id="diyu" gridId="${gridId}">${gridName}</span></a>
					
					<a href="${SQ_UAM_URL}/admin/nccc_sub_admin?menuName=材料上报" target="_blank" id="clsbsys" class="fr to-manage1" style="float:left;margin:0.2rem -0.1rem 0 0.1rem;display:none" >
						<span id="clsbrk" >材料上报系统入口</span>
					</a>
					<a href="javascript:;" id="todoTotalClick" class="fr to-manage-shijian" style="float:left">
	                    <p><i id="todoTotal"></i><img src="${uiDomain!''}/web-assets/_big-screen/nanchang-cc/images/shouye/pc-icon-xinxi01.png" id="todoEvent"></p>
	                </a>
					<a href="javascript:;" class="to-manage-person fl">
						<div class="user-info">
							<p><#if user??>
							        <#if user.photo??>									
										<img src="${IMG_URL!''}${user.photo}" class="nav-user-photo">
									<#else>
										<img src="${uiDomain!''}/web-assets/_big-screen/nanchang-cc/images/shouye/pc-icon-rentou.png" class="nav-user-photo">
									</#if>
	        				   </#if>
							</p>
							<p>
								<#if user??>
									<#if user.partyName??>${user.partyName}</#if>
								<#else>
									个人中心
			            		</#if>
			            	</p>
			            	<p><i class="nav-user-down"></i></p>
						</div>
					</a>
					<ul class="person-list" id="person-list">
							<li>
								<a href="javascript:void(0);" class="" onclick="pwdModify()">
									<i class="icon-user"></i>
									<b>修改密码</b>
								</a>
							</li>
							<li>
								<a href="javascript:void(0);" class="" onclick="changeOrg()">
									<i class="icon-refresh"></i>
									<b >切换组织</b>
								</a>
							</li>
							<li>
								<a href="javascript:void(0);" class="" onclick="logout()">
									<i class="icon-off"></i>
									<b >退出</b>
								</a>
							</li>
						</ul>
					</div>
					<div class="support-title">
						<p style="font-size:16px;color:#CADCF8;">中国电信提供技术支持
						</p>
					</div>
				</div>
			<!-- 头部、导航end -->
			

			<!-- 左侧 start -->
			<!-- 市级tabs -->
			<div class="event-wrap event-left1 event-sw event-sw-on evt-pos1" id="event-left1"  style="z-index:11;display:none;">
				<div class="tabs">
					<div class="tabs-item tabs-item1">
						<i class="pc-icon-tabs tabs-cont1"><img src="${uiDomain!''}/web-assets/_big-screen/nanchang-cc/images/icon-tabs-1.png"></i>
						<div class="tabs-cont tabs-cont1 ss"><!-- 显示 icon-tabs-0deg -->
							<p>事件热力</p>
						</div>
						<ul class="tabs-check" data-type="evt-chart"   >
							<!-- 选中用类tabs-checked -->
							<li data-chart="hot"   class="hot" >
								<i></i>
								<b>热力图</b>
							</li>
							<li data-chart="bar"  class="bar">
								<i></i>
								<b>柱状图</b>
							</li>
						</ul>
					</div>
					<div class="tabs-item" id="dataSync">
						<i class="pc-icon-tabs"><img src="${uiDomain!''}/web-assets/_big-screen/nanchang-cc/images/icon-tabs-2.png"></i>
						<div class="tabs-cont to-analy" id="to-analy">
							<p>数据分析</p>
						</div>
					</div>
				</div>
				
				<div class="cont-wrap event-analyse" id="event-analyse" style="left:0;">
					<div class="cont-panel">
						<h1><span class="gName" id="gName">${gridName}</span>事件总体分析</h1>
						<p class="cont-panel-t1">累计办结总量</p>
						<p class="evt-count">
							<span class="runNum1" id="runNum1"></span>
							<strong>件</strong>
						</p>
						<div class="cont-panel-t2 clearfix mtr30">
							<i class="cont-year">年</i>
							<b>本年办结总量</b>
							<p><span id="eventYear"></span>件</p>
						</div>
						<div class="cont-panel-t2 clearfix mtr20">
							<i class="cont-mouth">月</i>
							<b>本月办结总量</b>
							<p><span id="eventMonth"></span>件</p>
						</div>
					</div>

					<div class="cont-panel">
						<h1><span class="gName">${gridName}</span>事件数量波动图 <span id="typeForFigure"></span></h1>
						<div class="cont-ec1" id='eventWavePatternDiv'>
							<iframe id='eventWavePattern'></iframe>
		
						</div>
					</div>
				</div>
			</div>

			<!-- 街道tabs -->
			<div class="event-wrap event-left2 event-sw evt-pos1"  id="event-left2">
				<div class="tabs">
					<!-- 左侧事件热力 -->
					<div class="tabs-item tabs-item1"  style="display:none;">
						<i class="pc-icon-tabs tabs-cont1"><img src="${uiDomain!''}/web-assets/_big-screen/nanchang-cc/images/icon-tabs-1.png"></i>
						<div class="tabs-cont tabs-cont1 ss">
							<p>事件热力</p>
						</div>
						<ul class="tabs-check" data-type="evt-chart"   >
							<!-- 选中用类tabs-checked -->
							<li data-chart="hot"   class="hot" >
								<i></i>
								<b>热力图</b>
							</li>
							<li data-chart="bar"  class="bar">
								<i></i>
								<b>柱状图</b>
							</li>
						</ul>
					</div>
					<!-- 左侧事件热力 -->
					<!-- 左侧设备可视化 -->
					<#if (videoPosition??)>
					
						<div class="tabs-item tabs-item2">
			                <div id="deviceId" class="tabs-cont tabs-cont2 to-analy-active" data-type="evt-device">
			                    <i class="pc-icon-tabs tabs-cont2"><img src="${uiDomain!''}/web-assets/_big-screen/nanchang-cc/images/icon-tabs-1-2a.png"></i>
			                    <p>监控可视化</p>
			                </div>
			            </div>
					</#if>	
					<!-- 左侧设备可视化 -->
					<!-- 左侧点位可视化 -->
					<#if (pointPosition??)>
					
						<div class="event-point-select clearfix"  style="display:none;">
				            <div class="fl eps-left">
				                <img src="${uiDomain!''}/web-assets/_big-screen/nanchang-cc/images/icon-eps.png">
				            </div>
				            <div class="eps-content" id="eps-content">
				                <p>点位选择</p>
				            </div>
				        </div>
					</#if>	
					<!-- 左侧点位可视化 -->
				</div>
			</div>
			<!-- 左侧 end -->
			
			<!-- 点位选择 --><!--
		    <div class="dianwei-main" id="dianwei-main">
		    	<div class="dianwei-main-top">
					<p>点位选择<i id="dianwei_main_top_i"></i></p>
				</div>
		    	<iframe  id="mask-dianwei" class="mask-dianwei" src="" frameborder="0" width="100%" height="100%"></iframe>
			</div>-->
			
			<!-- 点位负责人 start --><!-- 
			<div class="event-wrap evt-pos2" id="civ_div" style="display:none;">
				<div class="cont-panel cont-panel-deep">
					<h1><span class="gName" id="civ_gName">${gridName}</span>负责人</h1>
					<div class="cont-data1">

						<p><span>责任人</span><span id="civ_name">-</span></p>

						<p><span>责任人电话</span><span id="civ_tel">-</span></p>
					</div>
				</div>
			</div> -->
			<!-- 点位负责人 end -->
			
			<!-- 区域、时间选择 strat -->
			<div class="event-wrap evt-pos3 clearfix" style="display:none;">
				<div class="left-nav">
					<div class="left-nav-item fl clearfix rc-cpops">
						<a href="javascript:void(0);" class="full-screen fl mouseStyle">
							<p class="cont-nav-t1 fl"><span>点位总数</span></p>
							<div>
                                <p id="pointTotal"></p>
                            </div>
						</a>
					</div>
				</div>
				<div class="left-nav left-nav-date" id="selectDateDiv" style="width: 2.58rem;">
					<select class="left-nav-item1 fl clearfix w65" name="year1" id="year"></select>
					<select class="left-nav-item1 fl clearfix w50" name="month1" id="month"></select>
					<select class="left-nav-item1 fl clearfix w50" name="day1" id="day"></select>
				</div>
			</div>
			<!-- 区域、时间选择 end -->

			<!-- 右侧 start -->
			<!-- 市级右侧 -->
			<div class="event-wrap event-right1 event-sw evt-pos4" id="event-right1"><!--用类event-sw-on控制弹窗显示-->
				<div class="snci-right-top snci-r-bg">
					<div class="snci-r-title clearfix">
						<i></i>   
						<p><span class="gName">${gridName}</span>事件多发地区TOP10</p>
					</div>
					<div class="snci-rt-content" id="multipleCounties">
						
					</div>
				</div>
				<div class="snci-right-bottom snci-r-bg">
					<div class="snci-r-title clearfix">
						<i></i>
						<p><span class="gName">${gridName}</span>问题多发点位T0P10</p>
					</div>
					<div class="snci-rb-content" id="morePoint">
						
					</div>
				</div>
			</div>
			
			<!-- 街道右侧 -->

			<div class="event-wrap event-right2 event-sw evt-pos4" id="event-right2">

				<div class="snci-right-top1 snci-r-bg1" id="jdRight">
					<div class="map-iframe" id="iframe1">
						<iframe src="" width="100%" height="100%" id='myiframe1'></iframe>
				</div>
				</div>
			</div>
			<!-- 右侧 end -->
		</div>
		<!--wrap_container-->
	</div>
	<!--container-fluid-->
	<script src="${uiDomain!''}/web-assets/common/js/basic/jquery.nicescroll.js" type="text/javascript" charset="utf-8"></script>
	
	<script>
	
	   //热力图对象
	    var heatMap="";
	    var heatSwitch=1;
	    var geometrySwitch=1;
	    var heatGeomeSwitch=0;
	     
		var searchFlag = "";
		//默认时间,年月日
		var dateDefault = "${dateNow}";
		var yearDefault = ""; 
		var monthDefault = "";
		var dayDefault = "";
		
		//组织编码
		var infoOrgCode = "${orgCode}";
		var gridName = "${gridName}";
		var gridLevel= "${gridLevel}";
	
		//用户选择的年月日
		var yearChoose = 0;
		var monthChoose = 0;
		var dayChoose = 0;
		var gridId = "${gridId}"; 
		var defaultGridId = "${gridId}"; 
		//日期类型
		var dateType = "day";
		
		//时间参数
		var createTimeStart = "";
		var createTimeEnd = "";
		
		var initX = "";
		var initY="";
		var initZoom = "";
		
		var pointPosition =${pointPosition!'false'};
		var videoPosition =${videoPosition!'false'};
		var orgType="${orgType}";
		initRightList();
		
		function initFn1 () {
			
			$('#gridNameClick').click(function () {
				$('#title_p').html("行政区划选择");
                $('#mc-item').addClass('mci-big').removeClass('mci-small');
                $('#popupWindowDiv').fadeIn(200);
                $('.mask-container-on').removeClass('mask-container-on')
                $('#diyuIframe').addClass('mask-container-on');
				//$('.mci-big2').css('display', 'block');
				document.getElementById("diyuIframe").contentWindow.showPathByGridName($("#diyu").attr("gridId"));
            });
		
			//if(navigator.userAgent.match("ua_ffcs_nc")){
			if(isAndroid){ 
				//$("#aifxDiv").hide();
				//$("#szfx").hide();
				$("#clsbsys").hide();
				$("#managerDiv").hide();
				$("#struc-item1").hide();
				$("#struc-item2").hide();
				$("#struc-item3").hide();
				$("#struc-item4").hide();
				$("#struc-item5").hide();
				$("#struc-item6").hide();
				$("#struc-item11").hide();
				//$(".to-manage").hide();
			}

			if(infoOrgCode.length > 6){
		           $('#event-left1, #event-right1').removeClass('event-sw-on');			
				$('#event-left2').addClass('event-sw-on');
				if(pointPosition){
					$('#event-right2').addClass('event-sw-on');
				}
					initRightList();//初始化设备点位列表页面
			}

			yearDefault = dateDefault.substring(0,4);
			monthDefault = dateDefault.substring(5,7);
			dayDefault = dateDefault.substring(8,10);
			createTimeStart = dateDefault;
			createTimeEnd = dateDefault;
			yearChoose = yearDefault;
			
			//初始化网格树
			//findGridData(infoOrgCode);
			//点位总数
			////getPointTotal();
			
			//设置时间控件的值
			////setYear();
			////setMonth(yearDefault);
			////setDay(yearDefault,monthDefault);
			
			//年月日点击事件
			/*$("#year").change(function(){
				var monthByClickYear = ""; 
				yearChoose = $("#year").val();
				setMonth(yearChoose);
				setDay(yearChoose,0);
				createTimeStart = yearChoose +"-"+ monthDefault + "-01";
				createTimeEnd = yearChoose + "-" + monthDefault + "-" + getDaysInMonth(yearChoose,monthDefault);
				
				if(monthChoose == 0){
					createTimeStart = yearChoose +"-01-01";
					createTimeEnd = yearChoose +"-12-31";
				}
				if(yearChoose == yearDefault){
					createTimeEnd = dateDefault;
				}
				clearTimeOut(searchFlag);
				searchFlag = setTimeout(function(){ 
					dateType = "year";
					searchDataByYearType(createTimeStart,createTimeEnd);
					dateChange();
					loadGeometryData();
					if($("#evt-pos5").css("display")=='block'&& bData!=null){
						showPointInfoData(bData);
					}
				}, 3000);
				
			});
			
			$("#month").change(function(){
				if(yearChoose == 0){
					yearChoose = yearDefault;
				}
				clearTimeOut(searchFlag);
				monthChoose = $("#month").val();
				setDay(yearChoose,monthChoose);
				if(monthChoose > 0){
					monthChoose =  monthChoose.length == 1?'0'+monthChoose:monthChoose;
					createTimeStart = yearChoose +"-"+monthChoose+"-01";
					createTimeEnd = yearChoose +"-"+monthChoose+"-"+ getDaysInMonth(yearChoose,monthChoose);
					if(yearChoose == yearDefault && monthChoose == monthDefault){
						createTimeEnd = dateDefault;
					}
					searchFlag = setTimeout(function(){ 
						dateType = "month";
						searchDataByMonthType(createTimeStart,createTimeEnd);
						dateChange();
						loadGeometryData();
						if($("#evt-pos5").css("display")=='block'&& bData!=null){
							showPointInfoData(bData);
						}
					}, 3000);
				}else{
					createTimeStart = yearChoose +"-01-01";
				    createTimeEnd = yearChoose +"-12-31";
				    if(yearChoose == yearDefault){
				    	createTimeEnd = dateDefault;
				    }
					searchFlag = setTimeout(function(){ 
						dateType = "year";
						searchDataByYearType(createTimeStart,createTimeEnd);
						dateChange();
						loadGeometryData();
						if($("#evt-pos5").css("display")=='block'&& bData!=null){
							showPointInfoData(bData);
						}
						
					}, 3000);
				}
				
				 
			});
			
			
			$("#day").change(function(){
				dayChoose = $("#day").val();
				clearTimeOut(searchFlag);
				if(yearChoose == 0){
					yearChoose = yearDefault;
				}
				if(monthChoose == 0){
					monthChoose = monthDefault;
				}
				monthChoose =  monthChoose.length == 1?'0'+monthChoose:monthChoose;
				if(dayChoose > 0){
					dayChoose =  dayChoose.length == 1?'0'+dayChoose:dayChoose;
					createTimeStart = yearChoose +"-"+monthChoose+"-" + dayChoose;
					createTimeEnd = yearChoose +"-"+monthChoose+"-"+ dayChoose;
					searchFlag = setTimeout(function(){ 
						dateType = "day";
						searchDataByDayType(createTimeStart,createTimeEnd);
						dateChange();
						loadGeometryData();
						if($("#evt-pos5").css("display")=='block'&& bData!=null){
							showPointInfoData(bData);
						}
					}, 3000);
					
				}else if(dayChoose == 0){
					createTimeStart = yearChoose +"-"+monthChoose+"-01";
					createTimeEnd = yearChoose +"-"+monthChoose+"-"+ getDaysInMonth(yearChoose,monthChoose);
					
					if(yearChoose == yearDefault && monthChoose == monthDefault){
						createTimeEnd = dateDefault;
					}
					searchFlag = setTimeout(function(){ 
						dateType = "month";
						searchDataByMonthType(createTimeStart,createTimeEnd);
						dateChange();
						loadGeometryData();
						if($("#evt-pos5").css("display")=='block'&& bData!=null){
							showPointInfoData(bData);
						}
					}, 3000);
				}
			});*/
			
			
			//事件待办页面按钮点击事件
			$('#todoEvent').on('click', function () {
			    //换标题
			    $('#title_p').html("事件待办");
			   // $('.mask-container').css('display', 'block');
			    //$('.mci-big2').css('display', 'block');
			    //关闭之前的弹窗
			    //$('.mask-vedio').removeClass('mask-container-on');
			    //$('.mask-dianwei').removeClass('mask-container-on');
				$('#mc-item').addClass('mci-big').removeClass('mci-small');
				$('#popupWindowDiv').fadeIn(200);
				$('.mask-container-on').removeClass('mask-container-on');
				var maskEvent = $("#mask-eventID");
				maskEvent.addClass('mask-container-on');
				var maskEventWin = maskEvent[0].contentWindow;
				maskEventWin.$("#todoEvent").addClass('active');
				//$(".mask-event").contents().find("#todoEvent").addClass('active');
				maskEventWin.$("#allEvent").removeClass('active');
				maskEventWin.getList('todo',maskEventWin.$("#eventTypePage>.current").html(),8);
				maskEventWin.$("#mae-event-detail").removeClass('mae-container-on');
				maskEventWin.$("#mae-event-add").removeClass('mae-container-on');
				maskEventWin.$("#mae-event-handle").removeClass('mae-container-on');
				maskEventWin.$("#mae-event-list").addClass('mae-container-on');
		
			});
			pointInfoByCiv();//责任人
			//$("#mask-eventID").attr('src','${rc.getContextPath()}/zhsq/nanChang3D/eventList.jhtml');
			
			//AI分析url  pc与平板不同
			//if(navigator.userAgent.match("ua_ffcs_nc")){
			if(isAndroid){ 
				$('#aifxDiv').empty();
				var html = '<a href="javascript:;"><p style="color: white;">AI分析</p></a>';
				$('#aifxDiv').html(html);
				//$("#aifx").attr('href','https://10.20.111.27/ctm01civilized-web');
			}else{
				$.ajax({
		    		url :"${rc.getContextPath()}/zhsq/nanChang3D/getOutUrl.json",
					type : 'POST',
					data :{'host':'https://10.20.111.27'},
					dataType : "json",
					success: function(data) {		
						$("#aifx").attr('href',data.aifx);
					}
				});
			}
			
			
		};
		
		//事件数量波动图
		function eventWavePattern(createTimeStart,createTimeEnd,dateType){

			//判断是否是平板访问
			var isIpad = "F";
			//if(navigator.userAgent.match("ua_ffcs_nc")){
			if(isAndroid){ 
				//适配平板样式问题  测试机型华为M6
					var width = $("#eventWavePatternDiv").width();
					var height = $("#eventWavePatternDiv").height();
					$("#eventWavePattern").css({'width':(width*1.4),'height':height});
					 isIpad = "T";
				}else{
					$("#eventWavePattern").css({'width':'100%','height':'100%'});
				}
				var src = "${SQ_BI_URL}/report/nanChang3D/eventWavePattern.jhtml?orgCode="+infoOrgCode+"&beginTime="+createTimeStart+"&endTime="+createTimeEnd+"&dateType="+dateType +"&isIpad=" + isIpad;
				$("#eventWavePattern").attr("src",src);
		}
		
		//清除上一次的延迟
		function clearTimeOut(searchFlag){
			if(searchFlag > 0){
				clearTimeout(searchFlag);
			}
		}
		
		var mapCenterLevel = 0;
		function diyuNodeClick(param) {
			gridName = param.gridName;
			infoOrgCode =param.infoOrgCode;
			gridId = param.gridId;
			gridLevel=param.gridLevel;
			
			if(param.centerX !=undefined && param.centerX != "" && param.centerY != "" && param.centerLevel != ""){
				var opts = {
					center: [parseFloat(param.centerX), parseFloat(param.centerY)],
					zoom: parseInt(param.centerLevel)
				}					
				locateCenterAndLevel(opts);
			}
			if(param.centerLevel!=undefined){
				mapCenterLevel = param.centerLevel;
			}
			if(param.infoOrgCode.length <= 6){
				$('#event-left1').addClass('event-sw-on');
				$('#event-left2').removeClass('event-sw-on');
				$('#event-right2').removeClass('event-sw-on');
				$.fn.ffcsMap.removeLayer("pointGridLayer", {}, function() {});   //清图层
				drawPolygon();
				$.fn.ffcsMap.removeLayer("layerPoint");
				if(dateType == "day" ){
					searchDataByDayType(createTimeStart,createTimeEnd);
				}else if(dateType == "month"){
					searchDataByMonthType(createTimeStart,createTimeEnd);
				}else if(dateType == "year"){
					searchDataByYearType(createTimeStart,createTimeEnd);
				}
				
			}else{
	            $('#event-left1, #event-right1').removeClass('event-sw-on');			

				//隐藏数据分析的四个图
				$('#event-analyse').fadeOut(200);
				$('#event-right1').fadeOut(200);
				$('#to-analy').removeClass('to-analy-on')
					$('#event-left2').addClass('event-sw-on');
				if(pointPosition){
					$('#event-right2').addClass('event-sw-on');

				}
				initRightList();//初始化设备点位列表页面				
			}
			$(".gName").text(param.gridName);
			$('#evt-pos5').hide();			
			getPointTotal();			
			isopen(false);
			pointInfoByCiv();//责任人
		}
		
		
		//定位中心点
		var isTreeClick = true;
		function locateCenterAndLevel(opts) {
			isTreeClick = true;
			$.fn.ffcsMap.goToView(opts);
		}
			
		//画轮廓
		var pointLayerObj={};
		var hightLightLayer;
		var pointTypes="";
	  	var pointLevels="A";
		function drawPolygon(){
			var url='${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDataOfGrids.json?gridId='+gridId+'&gridCode=&gridLevel='+(parseInt(gridLevel)+1)+'&mapt=5';
			if(gridLevel==4){
				var spointName= $("#myiframe1").contents().find("#spointName").val();
				url='${rc.getContextPath()}/zhsq/nanChang3D/getArcgisDrawDataOfPointsByCode.json?infoOrgCode='+''+"&pointLevels="+pointLevels+"&pointTypes="+pointTypes+"&pointName="+spointName+'&mapt=5';
				var zoomRound = getZoomRound();
				var proportion=2**(zoomRound-10);//地图缩放系数
				var extent = $.fn.ffcsMap.getViewExtent();
				var xmax=extent.xmax,xmin=extent.xmin,ymax=extent.ymax,ymin=extent.ymin;
				//url+='&xmax='+xmax+'&xmin='+xmin+'&ymax='+ymax+'&ymin='+ymin;
				url+='&xmax='+(xmax-0.091/proportion)+'&xmin='+(xmin+0.091/proportion)+'&ymax='+(ymax+0.148/proportion)+'&ymin='+(ymin-0.031/proportion);
			}
			
			$.fn.ffcsMap.removeLayer("gridLayer", {}, function() {});   //清图层
			$.fn.ffcsMap.removeLayer("pointLayer", {}, function() {});   //清图层
			$.fn.ffcsMap.removeLayer("pointGridLayer", {}, function() {});   //清图层
			$.fn.ffcsMap.removeLayer("highLightGridLayer", {}, function() {});   //清高亮图层
			$.ajax({
				type: 'POST',
				url: url,
				dataType: 'json',
				success: function(data) { 
					var strucDataAry = [];
					if(gridLevel==4){
						var strucPointAry = [];
						var strucPoint;
					}
					
					if(data.length>0){
						var temp_hs;
						var _data;
						var defStyle = {
								fillStyle : "solid",// 填充样式
								lineStyle : "solid",// 边框样式
						};
						var isPointLevel = false;
						if(mapCenterLevel > 14 && mapCenterLevel <19){//不显示面 名称
							isPointLevel = true;
						}
						var gLevel = gridLevel==4?true:false;
						var opacity = toRGBA();
						for (var i = 0; i < data.length ;i++) {  
							temp_hs=data[i].hs;
							data[i].opacity = opacity ; 
							if(gLevel){
								strucPoint= {
										id : data[i].id,   
										iconUrl : uiDomain + "/images/map/gisv0/map_config/unselected/icon-new-point-"+data[i].pointLevel,
										//iconUrl : "${uiDomain!''}/web-assets/_big-screen/nanchang-cc/images/icon-point-sort-"+data[i].pointLevel+"x.png",
										iconWidth : 28,
										iconHeight : 38,
										fontColor :'#fff',// 文字颜色
										fontSize : "18pt",
										bizData : {},// 存储业务数据
										x : parseFloat(data[i].x), 
										y : parseFloat(data[i].y),
										text : data[i].name,
										pointType : data[i].pointType,
										pointTypeStr : data[i].pointTypeStr
									};
								if(data[i].checkType=='001' && data[i].pointLevel=='A'){
									strucPoint.iconUrl+="x.png";
								}else{
									strucPoint.iconUrl+=".png";
								}
								strucPointAry.push(strucPoint); 
							}
							if(isPointLevel){
								data[i].colorNum = data[i].name ; 
								data[i].name = '' ; 
							}
							//面数据
							if(temp_hs != null || temp_hs != ""){
							  _data = DataCvtUtil(data[i], true);//转换
							  strucDataAry.push(_data);
						    } 
						}
						if(gLevel){
								$.fn.ffcsMap.renderPoint("pointLayer", strucPointAry);
								$.fn.ffcsMap.renderPolygon("pointGridLayer", strucDataAry, defStyle,function(graphicsLayer){
									hightLightLayer=graphicsLayer;
								});
						}else{
							$.fn.ffcsMap.renderPolygon("gridLayer", strucDataAry, defStyle);
						}
						
					}
				}
			});
			
		}
		function showPointMark(bizData){
			if(isPoint){
				strucPoint= {
					id : bizData.id,   
					iconUrl : bizData.iconUrl,
					iconWidth : bizData.iconWidth,
					iconHeight : bizData.iconHeight,
					fontColor :bizData.fontColor,// 文字颜色
					fontSize : bizData.fontSize,
					bizData : bizData.bizData,// 存储业务数据
					x : bizData.x, 
					y : bizData.y,
					text : bizData.text,
					pointType : bizData.pointType,
					pointTypeStr : bizData.pointTypeStr
				};
			}else{
				strucPoint= {
					id : bizData.id,   
					iconUrl : "${uiDomain!''}/images/map/gisv0/map_config/unselected/icon-new-point-"+bizData._oldData.pointLevel,
					iconWidth : 28,
					iconHeight : 38,
					fontColor :'#fff',// 文字颜色
					fontSize : bizData.fontSize,
					bizData : bizData.bizData,// 存储业务数据
					x : bizData.centerX, 
					y : bizData.centerY,
					text : bizData.colorNum,
					pointType : bizData.pointType,
					pointTypeStr : bizData.pointTypeStr
				};
				if(bizData._oldData.checkType=='001' && bizData._oldData.pointLevel=='A'){
					strucPoint.iconUrl+="x.png";
				}else{
					strucPoint.iconUrl+=".png";
				}
			}
			isPoint=true;
			var strucPointAry = [];
			strucPointAry.push(strucPoint);
			isPointListDetail=false;
			var opts = {center: [parseFloat(strucPoint.x),parseFloat(strucPoint.y) ]}
			showPointById(opts);
			var myiframe1 = $("#myiframe1")[0].contentWindow;
			$('#event-right2').addClass('event-sw-on');
			myiframe1.tabChange();
			myiframe1.$('#point').addClass('active').siblings().removeClass('active');
            myiframe1.$('#pointDiv').addClass('xqi-videx-item-on').siblings().removeClass('xqi-videx-item-on');
			myiframe1.showPointInfo(bizData.id,bizData.pointType,bizData.pointTypeStr,strucPointAry);
		}
		var bData=null;
		//点击轮廓显示责任人
		function showPointInfoData(bizData){
			if(gridLevel==4){//不是街道级点击轮廓则不显示统计数据
				isPoint=false;
				showPointMark(bizData);
				return;
			}
			//隐藏点击轮廓弹出的窗口
			/*bData=bizData;
			$("#evt-pos5").show();
			var url='${rc.getContextPath()}/zhsq/nanChang3D/pointStat.json';
			$.ajax({
				type: 'POST',
				url: url,
				data: {infoOrgCode:bizData.infoOrgCode,beginTime:createTimeStart,endTime:createTimeEnd,'dateType':dateType,gridLevel:gridLevel},
				dataType: 'json',
				success: function(data) { 
					$("#p_name").html(fnText(bizData.text,'--'));//责任区
					$("#pmTel").html(fnText(data.pmTel,'--'));
					$("#pointManager").html(fnText(data.pointManager,'--'));
					if(data.pointManager==undefined||data.pointManager==""){
						$('#civ_area').hide();
					}else{
						$('#civ_area').show();
					}
					$("#total_a").html(fnText(data.TOTAL_A,'0'));
					$("#total_b").html(fnText(data.TOTAL_B,'0'));
					$("#total_point").html(fnText(data.TOTAL_POINT,'0'));
					$("#total_on").html(fnText(data.TOTAL_ON,'0'));
					$("#total_off").html(fnText(data.TOTAL_OFF,'0'));
					$("#total_mont").html(fnText(data.TOTAL_MONT,'0'));
					$("#total_event").html(fnText(data.TOTAL_,'0'));
					$("#settles_total_").html(fnText(data.SETTLES_TOTAL_,'0'));
					$("#doing_total_").html(fnText(data.DOING_TOTAL_,'0'));
					
				}
			});*/
		}
		
		function fnText(value,returnV){
			return value==undefined?returnV:value;
		}
 		var isPointDetail =false;//是否为点位详情
 		var isPointListDetail =true;//是否为点位列表详情
 		var pointDetailId ;//点位列表详情id
 		var isPoint=true; //点位/点位轮廓
 		//显示点位详情
 		var strucPointArray;//点位列表对象储存
		function showPointDetail(bizDataId,strucPointAry){
			isPointDetail=true;
			$.fn.ffcsMap.removeLayer("gridLayer", {}, function() {});   //清图层
			$.fn.ffcsMap.removeLayer("pointLayer", {}, function() {});   //清点位图层
			$.fn.ffcsMap.removeLayer("highLightGridLayer", {}, function() {});   //清高亮图层
			//创高亮图层
			var _data;
			var hightLightData=[];
			for(var i=0;i<hightLightLayer.graphics.length;i++){
				if(bizDataId==hightLightLayer.graphics.items[i].attributes.data._oldData.id){
					_data = DataCvtUtil(hightLightLayer.graphics.items[i].attributes.data._oldData, true);//转换
					_data.lineColor = "#FF2800"; //边界线颜色
					_data.lineWidth = 3; //边界线宽度
					//_data.areaColor = "#FF2800";//区域颜色
					hightLightData.push(_data);
				}else{
					//_data = DataCvtUtil(hightLightLayer.graphics.items[i].attributes.data._oldData, true);//转换
					//hightLightData.push(_data);
				}
				
			}
			var defStyle = {
					fillStyle : "solid",// 填充样式
					lineStyle : "solid",// 边框样式
			};
			$.fn.ffcsMap.renderPolygon("highLightGridLayer", hightLightData, defStyle);//高亮图层
			if(strucPointAry!="" && strucPointAry!=undefined){//地图点位与轮廓点击
				$.fn.ffcsMap.renderPoint("pointLayer", strucPointAry);
			}else{
				var pointAry=[];
				pointAry.push(strucPointArray[bizDataId]);
				$.fn.ffcsMap.renderPoint("pointLayer", pointAry);
			}
		}
 		
		function showListPointDetail(bizDataId){
			isPointDetail=true;
			if(pointPosition){
				if(gridLevel==4){//网格级别是街道
					var spointName= $("#myiframe1").contents().find("#spointName").val();
					url='${rc.getContextPath()}/zhsq/nanChang3D/getArcgisDrawDataOfPointsByCode.json?infoOrgCode='+''+"&pointLevels="+pointLevels+"&pointTypes="+pointTypes+"&pointName="+spointName+'&mapt=5';
					var extent = $.fn.ffcsMap.getViewExtent();
					var xmax=extent.xmax,xmin=extent.xmin,ymax=extent.ymax,ymin=extent.ymin;
					url+='&xmax='+xmax+'&xmin='+xmin+'&ymax='+ymax+'&ymin='+ymin;
					
					$.fn.ffcsMap.removeLayer("gridLayer", {}, function() {});   //清图层
					$.fn.ffcsMap.removeLayer("pointLayer", {}, function() {});   //清图层
					$.fn.ffcsMap.removeLayer("pointGridLayer", {}, function() {});   //清图层
					$.fn.ffcsMap.removeLayer("highLightGridLayer", {}, function() {});   //清高亮图层
					$.ajax({
						type: 'POST',
						url: url,
						dataType: 'json',
						success: function(data) { 
							var strucDataAry = [];
							if(gridLevel==4){
								var strucPointAry = [];
								var strucPoint;
							}
							
							if(data.length>0){
								var temp_hs;
								var _data;
								var defStyle = {
										fillStyle : "solid",// 填充样式
										lineStyle : "solid",// 边框样式
								};
								var isPointLevel = false;
								if(mapCenterLevel > 14 && mapCenterLevel <19){//不显示面 名称
									isPointLevel = true;
								}
								var gLevel = gridLevel==4?true:false;
								var opacity = toRGBA();
								for (var i = 0; i < data.length ;i++) {  
									temp_hs=data[i].hs;
									data[i].opacity = opacity ; 
									
									if(isPointLevel){
										data[i].colorNum = data[i].name ; 
										data[i].name = '' ; 
									}
									//面数据
									if(temp_hs != null || temp_hs != ""){
									  _data = DataCvtUtil(data[i], true);//转换
									  strucDataAry.push(_data);
								    } 
								}
								if(gLevel){
										$.fn.ffcsMap.renderPoint("pointLayer", strucPointAry);
										$.fn.ffcsMap.renderPolygon("pointGridLayer", strucDataAry, defStyle,function(graphicsLayer){
											hightLightLayer=graphicsLayer;
										});
								}
								
							}
						}
					});
					
				}
			}
			//创高亮图层
			var _data;
			var hightLightData=[];
			for(var i=0;i<hightLightLayer.graphics.length;i++){
				if(bizDataId==hightLightLayer.graphics.items[i].attributes.data._oldData.id){
					_data = DataCvtUtil(hightLightLayer.graphics.items[i].attributes.data._oldData, true);//转换
					_data.lineColor = "#FF2800"; //边界线颜色
					_data.lineWidth = 3; //边界线宽度
					hightLightData.push(_data);
				}
				
			}
			var defStyle = {
					fillStyle : "solid",// 填充样式
					lineStyle : "solid",// 边框样式
			};
			$.fn.ffcsMap.renderPolygon("highLightGridLayer", hightLightData, defStyle);//高亮图层
			//保留图标
			var pointAry=[];
			pointAry.push(strucPointArray[bizDataId]);
			$.fn.ffcsMap.renderPoint("pointLayer", pointAry);
			
		}
		
		function  pointInfoByCiv(){//区域责任人
			var url='${rc.getContextPath()}/zhsq/nanChang3D/findCivilizedPerInfoData.json?infoOrgCode='+infoOrgCode;
			$.ajax({
				type: 'POST',
				url: url,
				dataType: 'json',
				success: function(data) { 
					var name ='--',tel='--';
					if(data.length>0){
						if(data[0].NAME){name =data[0].NAME };
						if(data[0].MOBILE_TELEPHONE){tel =data[0].MOBILE_TELEPHONE };
						$("#civ_div").show();
						$(".evt-pos1").css('top','3.2rem');
					} else{
						$("#civ_div").hide();
						$(".evt-pos1").css('top','2rem');
					}
					$("#civ_name").html(name);
					$("#civ_name").attr("title",name);
					$("#civ_tel").html(tel);
				}
			});
		}
		
		function workDepartmentInfoByCiv(pointType,pointTypeStr,infoOrgCode){//工作部责任人
			if('0'=='${orgType}'){
				$("#civ_gName").html('${workDepartment}');
				$("#civ_name").html('${pointManager}');
				$("#civ_name").attr("title",'${pointManager}');
				$("#civ_tel").html('${pmTel}'); 
				if('${pointManager}'==''||'${pointManager}'==undefined){
					$("#civ_div").hide();
					$(".evt-pos1").css('top','2rem');
				}else{
					$("#civ_div").show();
					$(".evt-pos1").css('top','3.2rem');
				}
			}else{
				$.ajax({
					type: 'POST',
					url: '${rc.getContextPath()}/zhsq/nanChang3D/workDepartmentPerson.json',
					data: {
						"pointType":pointType,
						"pointTypeStr":pointTypeStr,
						"infoOrgCode":infoOrgCode},
					dataType: 'json',
					success: function(data) { 
						var name ='--',tel='--',workDepartment='--';
						 if(data.length>0){
							if(data.pointManager){name =data.pointManager };
							if(data.pmTel){tel =data.pmTel };
							if(data.workDepartment){
								workDepartment=data.workDepartment
							};
							$("#civ_div").show();
							$(".evt-pos1").css('top','3.2rem');
						}else{
							$("#civ_div").hide();
							$(".evt-pos1").css('top','2rem');
						}
						$("#civ_gName").html(workDepartment);
						$("#civ_name").html(name);
						$("#civ_name").attr("title",name);
						$("#civ_tel").html(tel); 
					}
				});
			}
			
		}
		
		function defaultpointInfoByCiv(){
			pointInfoByCiv();
			$("#civ_gName").html(gridName);
		}
		
		
		//年数据
		function searchDataByYearType(createTimeStart,createTimeEnd){
			if(infoOrgCode.length <= 6 && $("#dataSync>div").hasClass('to-analy-on')){
				eventTotalSyc("year","eventTotal");
				eventTotalSyc("year","eventYear");
				eventTotalSyc("month","eventMonth");
				findMultipleCountiesData(createTimeStart,createTimeEnd,"year");
				eventWavePattern(createTimeStart,createTimeEnd,"year");
				findPointData("year");
				setTypeForFigure("year");
			}
		}
		//月数据
		function searchDataByMonthType(createTimeStart,createTimeEnd){
			if(infoOrgCode.length <= 6 && $("#dataSync>div").hasClass('to-analy-on')){
				eventTotalSyc("month","eventTotal");
				eventTotalSyc("year","eventYear");
				eventTotalSyc("month","eventMonth");
				findMultipleCountiesData(createTimeStart,createTimeEnd,"month");
				eventWavePattern(createTimeStart,createTimeEnd,"month");
				findPointData("month");
				setTypeForFigure("month");
			}
		}
		
		//天数据
		function searchDataByDayType(createTimeStart,createTimeEnd){
			if(infoOrgCode.length <= 6 && $("#dataSync>div").hasClass('to-analy-on')){
				eventTotalSyc("day","eventTotal");
				eventTotalSyc("year","eventYear");
				eventTotalSyc("month","eventMonth");
				findMultipleCountiesData(createTimeStart,createTimeEnd,"day");
				eventWavePattern(createTimeStart,createTimeEnd,"day");
				findPointData("day");
				setTypeForFigure("day");
			}
		}
		
		function pointInfoListData(){
			$.fn.ffcsMap.removeLayer("gridLayer", {}, function() {});   //清图层
			$.fn.ffcsMap.removeLayer("pointLayer", {}, function() {});   //清图层
			$.fn.ffcsMap.removeLayer("pointGridLayer", {}, function() {});   //清图层
			if(pointPosition){
				if(gridLevel==4){//网格级别是街道
					drawPolygon();
					//var pointSearch= $("#pointSearch").val();
					var myiframe1 = $("#myiframe1")[0].contentWindow;
					//myiframe1.$("#spointName").val(pointSearch);
					myiframe1.pointInfoList(1,10,infoOrgCode,pointLevels,pointTypes);//点位列表  
				}
			}
		}

		function camlistData(){
			if($("#deviceId").hasClass('to-analy-active') && gridLevel == 4){
				$('#event-right2').addClass('event-sw-on');////
				//var Search= $("#camSearch").val();
				var myiframe1 = $("#myiframe1")[0].contentWindow;
				var Search=myiframe1.$("#camSearch").val();
				$("#camSearch").val(Search);
				getCamPoint(infoOrgCode,'4',"2",$("#camSearch").val());//撒点定位
				if(isPointDetail){
					myiframe1.getCamlist(myiframe1.currpage,10,infoOrgCode,"4",Search,"2");//设备列表
				}else{
					myiframe1.getCamlist(1,10,infoOrgCode,"4",Search,"2");//设备列表
				}
				myiframe1.tabChange();//更改样式
			}
		}
		function initRightList(){
			//点位类型初始化
			if(pointPosition){
				if($('#mask-dianwei').attr("src")==""){
					if(orgType=="0"){
						var professionCode = "${professionCode}";
					}else{
						var professionCode ="";
					}
					$('#mask-dianwei').attr("src","${rc.getContextPath()}/zhsq/nanChang3D/pointTypeCheck.jhtml?pointTypes=&professionCode="+professionCode);
				}
			}
			if(pointPosition || videoPosition){
				if($("#myiframe1").attr("src")==''){
					$("#myiframe1").attr("src","${rc.getContextPath()}/zhsq/map/DDEarthController/camList.jhtml?line=&infoOrgCode=");
				}
			}
		}
		
    
    //获取事件区县TOP10
    function findMultipleCountiesData(createTimeStart,createTimeEnd,dateType){
    	$.ajax({
    		url :"${rc.getContextPath()}/zhsq/nanChang3D/findMultipleCountiesData.json",
			type : 'POST',
			dataType : "json",
			data: {'infoOrgCode':infoOrgCode,'eventDateBegin':createTimeStart,'eventDateEnd':createTimeEnd,'dateType':dateType},
			success: function(data) {		
				showMultipleCountiesData(data);
			},
			error : function() {
				$.messager.alert('获取事件区县TOP10!');
			}
		});
    }	
    
    //获取点位总数
    function getPointTotal(){
    	$.ajax({
    		url :"${rc.getContextPath()}/zhsq/nanChang3D/getPointTotal.json",
			type : 'POST',
			dataType : "json",
			data: {'infoOrgCode':infoOrgCode},
			success: function(data) {		
				$("#pointTotal").text(data);
			}
		});
    }	
    
    //展示事件区县TOP10
    function showMultipleCountiesData(data){    	
    	var html = "";
    	for(var i =0,l=data.length>10?10:data.length;i<l;i++){
    		html += "<div class='snci-rtc-item clearfix'>";
    		if(i<3){
    			html += "<i class='snci-rt-icon"+(i*1+1)+"'>"+(i*1+1)+"</i>";
    		}else{
    			html += "<i class='snci-rt-icon'>"+(i*1+1)+"</i>";
    		}
    		html += "<div class='snci-rti-center'><p>"+data[i].GRID_NAME+"</p><div class='snci-rtic-progress'>";
    		if(i==0){
    			if(data[i].TOTAL_ != 0){
    				html += "<i style='width:100%'></i>";
    			}
    		}else{
    			if(data[0].TOTAL_ != 0){
    				html += "<i style='width:"+(data[i].TOTAL_/data[0].TOTAL_)*100+"%'></i>";
    			}
    		}
    		html += "</div></div><p>"+data[i].TOTAL_+"</p></div>";
    	}
    	$("#multipleCounties").html(html);
    }
    
    //南昌市事件总体分析
    function eventTotalSyc(dateType,jsonpcallback){
    
    	var year = yearDefault;
    	var month = monthDefault;
		var day = dayDefault;		
		var createTimeStart1 = createTimeStart;
		var createTimeEnd1 = createTimeEnd;
		
		if(yearChoose > 0){
			year = yearChoose;
		}
		if(monthChoose > 0){
			month = monthChoose;
		}
		if(dayChoose > 0){
			day = dayChoose;
		}
    	
    	if("year" == dateType){
    		createTimeStart1 = year+'-01-01';
    		createTimeEnd1 = year+'-12-31';
    		if(year == yearDefault){
    			createTimeEnd1 = dateDefault;
    		} 
    	}else if("month" == dateType){
    		var days = getDaysInMonth(year,month);
    		var m = month;
    		if(month.length == 1){
    			month = "0"+month;
    		}
    		createTimeStart1 = year +"-"+ month +'-01';
    		createTimeEnd1 = year +"-"+ month +'-' + days;
    		if(year == yearDefault && m == monthDefault){
    			createTimeEnd1 = dateDefault;
    		}
    	}else if("day" == dateType){
    		if(monthChoose == 0){
				createTimeStart1 = year+'-01-01';
				createTimeEnd1 = year+'-12-31';
			}
    	}
    	$.ajax({
			type : 'POST',
			dataType : "jsonp",
			data: {'listType':'5','infoOrgCode':infoOrgCode,'jsonpcallback':jsonpcallback,'createTimeStart':createTimeStart1,'createTimeEnd':createTimeEnd1},
			url : "${SQ_EVENT_URL}/zhsq/event/eventDisposal4OuterController/fetchDataCount4Jsonp.json",
			success: function(data) {
			}
		});
    }
    
    
    function getDaysInMonth(year,month){
		month = parseInt(month,10);  
		var temp = new Date(year,month,0);
		return temp.getDate();
	}
    
    function eventTotal(data){
    	var num2 = "";
    	var total = data.total + "";
    	for(var i=0,l=7-total.length;i<l;i++){
    		num2 += "0";
    	}
    	//初始值，真实值，卡片宽度，卡片高度, 行高
		$("#runNum1").runNum({
			'num1': '0000000',
			'num2': num2+total,
			'width': '.3',
			'height': '.34',
			'lineHeight': '.38'
		});
    }
    
    function eventYear(data){
    	$("#eventYear").text(data.total);
    }
    
    function eventMonth(data){
    	$("#eventMonth").text(data.total);
    }
    
    function setYear(){
    	var html = "";
    	for(var i=0,l=yearDefault;i<5;i++){
    		html += "<option value='"+(yearDefault-i)+"'>"+(yearDefault-i)+"年</option>";
    	}
    	$("#year").append(html);
    }
    
    var countM = 1;
    
    function setMonth(year){
    	monthChoose = 0;
    	var month = 12;
    	if(year == yearDefault){
    		month = monthDefault;
    	}
     	var monthSelect = $("#month");
     	monthSelect.html("");
     	var html = "<option value='0'>全年</option>";
    	for(var i=1,l=month;i<=l;i++){
    		if(countM == 1 && parseInt(i,10) == monthDefault){
    			countM = 0;
    			html += "<option selected='selected' value='"+i+"'>"+i+"月</option>";
    		}else{
    			html += "<option value='"+i+"'>"+i+"月</option>";
    		}
    	}
    	monthSelect.append(html);
    }
    
    var countD = 1;
    function setDay(year,month){
    	dayChoose = 0;
    	var days = getDaysInMonth(year,month);
    	if(year == yearDefault && month == parseInt(monthDefault)){
    		days = parseInt(dayDefault);
    	}
    	var html = "";
    	var daySelect = $("#day");
     	daySelect.html("");
    	if(month == 0){
			$("#selectDateDiv").css("width","1.66rem");
			//$("#day").css("display","none");
    		daySelect.hide().html("");
    		return;
    	}
    	$("#selectDateDiv").css("width","2.58rem");
		daySelect.show();
    	var html = "<option value='0'>当月</option>";
    	for(var i=1,l=days;i<=l;i++){
    		if(countD == 1 && parseInt(i,10) == dayDefault){
    			countD = 0;
    			html += "<option selected='selected' value='"+i+"'>"+i+"日</option>";
    		}else{
    			html += "<option value='"+i+"'>"+i+"日</option>";
    		}
    	}
    	daySelect.html(html);
    }
    
    
    // 数据分析
	$('#to-analy').on('click', function () {
		if ($(this).hasClass('to-analy-on')) {
			$('#event-analyse').fadeOut(200);
			$('#event-right1').fadeOut(200);;
			$(this).removeClass('to-analy-on')
		} else {
			$(this).addClass('to-analy-on')
			$('#event-analyse').fadeIn(200);
			$('#event-right1').fadeIn(200);
			searchDataByDayType(createTimeStart,createTimeEnd);
		}
	});
	
	//点位TOP10
	function findPointData(dateType){
		var year = yearChoose;
    	var month = monthChoose;
		var day = dayChoose;
		var date_ = "";
		var type = "";
		
		if(yearChoose == 0){
			year = yearDefault;
		}
		if(monthChoose == 0){
			month = monthDefault;
		}
		if(dayChoose == 0){
			day = dayDefault;
		}
    	
    	if("year" == dateType){
    		date_ = year;
    		type = "1";
    	}else if("month" == dateType){
    		date_ = year + month;
    		type = "2";
    	}else if("day" == dateType){
    		date_ = year +  month + day;
    	}
    	var gid = gridId;
    	if(infoOrgCode.length == 4){
    		gid = "";
    	}
	
		$.ajax({
    		url :"${rc.getContextPath()}/zhsq/nanChang3D/findPointData.json",
			type : 'POST',
			dataType : "json",
			data: {'infoOrgCode':infoOrgCode,'gridId':gid,'date_':date_,'dateType':dateType,'type':type},
			success: function(data) {		
				showPointData(data);
			}
		});
	}
	
	//展示多发点位TOP10
    function showPointData(data){
    	
    	var html = "";
    	var morePoint = $("#morePoint");
    	morePoint.html(html);
    	
    	var len = data.length;
    	if(len > 10){
    		len = 10;
    	}
    	for(var i =0;i<len;i++){
    		html += "<div class='snci-rbc-item clearfix mouseStyle'>";
    		html += "<i>"+(i*1+1)+"</i>";
    		html += "<div class='snci-rti-center'>";
    		html += "<p>"+data[i].POINT_NAME+"</p>";
    		html += "<div class='snci-rtic-progress'>";
    		if(i==0){
    			if(data[i].TOTAL_ != 0){
    				html += "<i style='width:100%'></i>";
    			}
    		}else{
    			if(data[0].TOTAL_ != 0){
    				html += "<i style='width:"+(data[i].TOTAL_/data[0].TOTAL_)*100+"%'></i>";
    			}
    		}
    		html += "</div>";
    		html += "</div>";
    		html += "<p>"+data[i].TOTAL_+"</p>"
    		html += "</div>";
    	}
    	morePoint.append(html);
    }
    

	
		function initFn2 () {
			//导航菜单
			var swiper1 = new Swiper('.swiper1', {
                //freeMode : true,
                slidesPerView : 'auto',
                watchOverflow: true,  //因为仅有1个slide，swiper无效
                observer: true,//修改swiper自己或子元素时，自动初始化swiper
                observeParents: true,//修改swiper的父元素时，自动初始化swiper
                loop:false,//设置 active slide 居中后，会有左右留白现象，添加此会让未尾的导航补齐前后空白
                slideToClickedSlide: true,//设置为true则点击slide会过渡到这个slide。
                //centeredSlides: true,//设定为true时，active slide会居中，而不是默认状态下的居左。
                //initialSlide :1,     //默认第二张为当前项
                //resistanceRatio :0,  //禁止两端被拉动
                navigation: {
                    nextEl: '.swiper-button-next',
                    prevEl: '.swiper-button-prev',
                  	//disabledClass: 'my-button-disabled', //按钮不可用时样式
                },
                on:{
                    init(){ //初始化的时候也要设置一下
                        //this.setTranslate(6);  
                    }
                    /* ,
                    transitionStart(){  //开始translate之前
                        var index=this.activeIndex;
                        //第一张
                        if(this.activeIndex==0){ 
                            this.setTranslate(6)
                        }
                        //最后一张
                        if(this.activeIndex>0 && this.activeIndex==this.slides.length-1){
                            var w=this.width //容器的宽度
                            var slideW=(this.width/3).toFixed(3) //每个slide的宽度
                            var keepW=slideW*(this.slides.length - 3)-30  //需要设置translate的宽度
                            this.setTranslate(-keepW);
                        }
                    } */
                }
			});
			$("#wrap_media").css("left",(document.body.offsetWidth-604)/2);
			//滚动条美化
			// 导航
			/* $('.nav-item').on('click',function () {
				$('.nav-item').removeClass('nav-item-on');
				$(this).addClass('nav-item-on');
			}); */
			$('.swiper1 .swiper-slide').on('click', function () {
                $(this).addClass('nav-item-on').siblings().removeClass('nav-item-on');
                if($("#zhdd").hasClass('nav-item-on')||$("#aifxDiv").hasClass('nav-item-on')||$("#managerDiv").hasClass('nav-item-on')){
                	$("#gridNameClick").show();
                	$("#todoTotalClick").show();
                }else{
                	$("#gridNameClick").hide();
                	$("#todoTotalClick").hide();
                }
                if($("#xffx").hasClass('nav-item-on')){
                	xffxShow();
                }else{
                	xffxHide();
                }
                if($("#clsb").hasClass('nav-item-on')){
                	clsbShow();
                	//if(navigator.userAgent.match("ua_ffcs_nc")){
					if(isAndroid){ 
                		clsbsysHide();
                	}else{
                		clsbsysShow();
                	}
                }else{
                	clsbHide();
                	clsbsysHide();
                }
                //if(navigator.userAgent.match("ua_ffcs_nc")){
				if(isAndroid){ 
                	if($("#aifxDiv").hasClass('nav-item-on')||$("#xffx").hasClass('nav-item-on')||$("#clsb").hasClass('nav-item-on')||$("#szfx").hasClass('nav-item-on')||$("#qsfx").hasClass('nav-item-on')||$("#zhxx").hasClass('nav-item-on')||$("#yryd").hasClass('nav-item-on')||$("#lcry").hasClass('nav-item-on')||$("#sqgz").hasClass('nav-item-on')||$("#wzbz").hasClass('nav-item-on')){
                    	$("#gridNameClick").hide();
                    	$("#todoTotalClick").hide();
                    }else{
                    	$("#gridNameClick").show();
                    	$("#todoTotalClick").show();
                    }
                	if($("#aifxDiv").hasClass('nav-item-on')){
                    	AIfxShow();
                    }else{
                    	AIfxHide();
                    }
                }
                
                if($("#szfx").hasClass('nav-item-on')){
                	szfxShow();
                }else{
                	szfxHide();
                }
				//医疗卫生
				if($("#qsfx").hasClass('nav-item-on')){
                	qsfxShow();
                }else{
                	qsfxHide();
                }
				//综合信息
				if($("#zhxx").hasClass('nav-item-on')){
					zhxxShow();
				}else{
					zhxxHide();
				}
				//一人疫档
				if($("#yryd").hasClass('nav-item-on')){
					yrydShow();
				}else{
					yrydHide();
				}
				//来蒙人员
				if($("#lcry").hasClass('nav-item-on')){
					lcryShow();
				}else{
					lcryHide();
				}
				//社区工作
				if($("#sqgz").hasClass('nav-item-on')){
					sqgzShow();
				}else{
					sqgzHide();
				}
				//物资保障
				if($("#wzbz").hasClass('nav-item-on')){
					wzbzShow();
				}else{
					wzbzHide();
				}
            });
	      //个人中心
			$('.to-manage-person').click(function () {
				$('#person-list').toggleClass('person-list-on');
			});
			$('#person-list>li').click(function () {
				$('#person-list').removeClass('person-list-on');
			});


			// tabs切换
			$('.tabs-cont').on('click', function () {
				var flag = $(this).siblings().hasClass('tabs-check'),//点击按钮判断子节点是否存在复选框
					type = $(this).siblings('ul').data('type');//判断复选框类型（图表或点位）

				// 显示/隐藏复选框
				if (flag) {
					if ($(this).hasClass('pc-icon-tabs-0deg')) {
						$(this).removeClass('pc-icon-tabs-0deg');//按钮形态修改
						$(this).siblings('.tabs-check').removeClass('tabs-check-on'); //隐藏复选框
						if (type == 'evt-chart') {// 关闭热力图
							$('div[class^="chart-cont-"]').fadeOut(200);
							heatGeomeSwitch=0;
						    if(isNotUnll(heatMap)){
						        heatMap.removeLayer();
						     }
							 $.fn.ffcsMap.Geometry.RemoveBox("boxLayer");
							 $.fn.ffcsMap.Geometry.RemoveText("textLayer");
						     $.fn.ffcsMap.removeCube();
							isopen(true);
						}
					} else {
						$(this).addClass('pc-icon-tabs-0deg');//按钮形态修改
						$(this).siblings('.tabs-check').addClass('tabs-check-on'); //显示复选框
						if (type == 'evt-chart') {// 热力图
							heatGeomeSwitch=1;
							isopen(true);
						}
					}		
				}else{
					if ($(this).data('type') == 'evt-device') {
						if ($(this).hasClass('to-analy-active')) {
                            $(this).removeClass('to-analy-active');
                            $.fn.ffcsMap.removeLayer("layerPoint"); //取消设备撒点
                            $("#myiframe1")[0].contentWindow.tabChange();//更改设备列表样式
                        } else {
                            $(this).addClass('to-analy-active');
                            $('#event-right2').addClass('event-sw-on');
                          	$("#myiframe1")[0].contentWindow.$('#device').addClass('active').siblings().removeClass('active');
            	            $("#myiframe1")[0].contentWindow.$('#deviceDiv').eq(0).addClass('xqi-videx-item-on').siblings().removeClass('xqi-videx-item-on');
                        }
                            camlistData();//查询监控列表
							
						
                    }
				}
					
				if (!$("#deviceId").hasClass('to-analy-active') && pointPosition==false) {
					//当设备可视化按钮失焦和点位可视化无权限时，右侧弹窗隐藏
					$('.event-right2').removeClass('event-sw-on');
					$("#camSearch").val("");
					$("#pointSearch").val("");
				}
			});

			// 显示/隐藏图表和相应点位
			$('.tabs-check').on('click', 'li', function () {
				var type = $(this).parent().data('type');//判断图表类型
				if (type == 'evt-chart') {//如果图表为chart
					$(this).toggleClass('tabs-checked');
					var chart = $(this).data('chart');
					if ($(this).hasClass('tabs-checked')) {
						if(chart=='hot'){
							heatSwitch=1;
							dateChange();
						}else if(chart=='bar'){
							 geometrySwitch=1;
							// iniTilt = 45;
							// $.fn.ffcsMap.goToView({ tilt: 45});
							loadGeometryData();	
						}
					} else {
						$('.chart-cont-' + chart).fadeOut(200);//对应热力图隐藏
					   if(chart=='hot'){
					   if(heatMap !== null && heatMap !== undefined && heatMap !== ''){
				    	 heatMap.removeLayer();
					   }
					   heatSwitch=0;
						}else if(chart=='bar'){
						   $.fn.ffcsMap.Geometry.RemoveBox("boxLayer");
						  $.fn.ffcsMap.Geometry.RemoveText("textLayer");
						    geometrySwitch=0;
						}
					}
				}
			});

			
			
			

			//	        卡片式字数滚动
			$.fn.extend({
				/*
				 *	滚动数字
				 *	@ val 值，	params 参数对象
				 *	params{addMin(随机最小值),addMax(随机最大值),interval(动画间隔),speed(动画滚动速度),width(列宽),height(行高)}
				 */
				runNum: function (val, params) {
					/*初始化动画参数*/

					var valString = val.num1;
					var valString2 = val.num2;
					var width = val.width;
					var height = val.height;
					var lineHheight = val.lineHeight;
					var par = params || {};
					var runNumJson = {
						el: $(this),
						value: valString,
						val2: valString2,
						valueStr: valString.toString(10),
						width: width,
						height: height,
						lineHheight: lineHheight,
						addMin: par.addMin || 10000,
						addMax: par.addMax || 99999,
						interval: par.interval || 3000,
						speed: par.speed || 1000,
						length: valString.toString(10).length
					};
					$._runNum._list(runNumJson.el, runNumJson);
					$._runNum._interval(runNumJson.el.children("i"), runNumJson);
				}
			});
			/*jQuery对象添加  _runNum  属性*/
			$._runNum = {
				/*初始化数字列表*/
				_list: function (el, json) {
					var str = '';
					for (var i = 0; i < json.length; i++) {
						var w = json.width * i;
						var t = json.height * parseInt(json.valueStr[i]);
						var h = json.height * 10;
						str += '<i style="width:' + json.width + 'rem;left:0rem;top: ' + -t +
							'rem;height:' +
							h + 'rem;">';
						for (var j = 0; j < 10; j++) {
							str += '<div style="height:' + json.height + 'rem;line-height:' + json
								.lineHheight + 'rem;">' + j + '</div>';
						}
						str += '</i>';
					}
					el.css('height', json.height + 'rem').html(str);
				},
				/*生成随即数*/
				_random: function (json) {
					var Range = json.addMax - json.addMin;
					var Rand = Math.random();
					var num = json.addMin + Math.round(Rand * Range);
					return num;
				},
				/*执行动画效果*/
				_animate: function (el, value, json) {
					for (var x = 0; x < json.length; x++) {
						var topPx = value[x] * json.height;
						el.eq(x).animate({
							top: -topPx + 'rem'
						}, json.speed);
					}
				},
				/*定期刷新动画列表*/
				_interval: function (el, json) {
					var val = json.val2;
					setTimeout(function () {
						//                  val+=$._runNum._random(json);
						$._runNum._animate(el, val.toString(10), json);
					}, 200);
				}
			}

			// 右侧顶部滚动条优化
			$('#multipleCounties,#morePoint').niceScroll({
				cursorcolor: "rgba(33,110,197,.65)", //#CC0071 光标颜色
				cursoropacitymax: 1, //改变不透明度非常光标处于活动状态（scrollabar“可见”状态），范围从1到0
				touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
				cursorwidth: "4px", //像素光标的宽度
				cursorborder: "0", // 游标边框css定义
				cursorborderradius: "4px", //以像素为光标边界半径
				autohidemode: true //是否隐藏滚动条
			});

			$('#point-offline, #point-online').on('click', function () {
				$('#mc-item').addClass('mci-big').removeClass('mci-small');
				$('#popupWindowDiv').fadeIn(200);
				$('.mask-container-on').removeClass('mask-container-on');
				$('#mask-vedio').addClass('mask-container-on');
			});
			
			$('#mc-close').on('click', function () {
				$('#popupWindowDiv').fadeOut(200);
				if($('#title_p').html()=="视频详情"){
					$('#videoUl>li').remove();
					//清空li，再添加一个
					$('#videoUl').append('<li ><div class="bg" onclick="selectDiv(this)" id="vli0" index=0  play="0"></div></li>');
					$("#titleA").addClass('active').siblings('a').removeClass('active');
					$("#videoUl").addClass('ul-sipin1').removeClass('ul-sipin3').removeClass('ul-sipin2');
					isPointDetail=false;
					//camlistData();//监控设备列表
					pointInfoListData();//点位列表
					//$('.mask-container').css('height', '100%');
					$('#popupWindowDiv').css('height', '100%');
					indx=0;
					videoNum=1;
					selected='';//第i个
			        videoSelect='';//预选下一个播放框
			        selectByHand='';//手动选择第i个
			        videoSelectByHand='';//手动选择
	            	playArr=[0];
				}
			});

		};
		function setIsPointDetailFalse(){//给手机端调用的方法
			isPointDetail=false;
		}

		/*			楼宇简介切换
		$.fn.struc = function () {
			$(this).each(function () {
				if ($(this).find('.struc-item').length <= 6) {
					$(this).children('.struc-next, .struc-prev').css({
						'opacity': '0',
						'cursor': 'default'
					});
				} else {
					// $(this).find('.struc-item').hide();
					$(this).children('.struc-prev').css({
						'opacity': '.5',

					});
				}
			});
			var dataC, dataCY, items, items1;
			items1 = $('.struc-item');

			for (var k = 6; k <= items1.length; k++) {
				$('#struc-item' + k).find('a').css({
					'display': 'none'
				});
			};

			$(this).find('.struc-prev').on('click', function () {
				var _this = this;
				dataC = $(_this).parent().data('count'),
					items = $(_this).siblings('.struc-cont').find('.struc-items').children('.struc-item'),
					dataCY = items.length - 6;
				if (dataCY - dataC > -1) {
					if (dataC > 0) {
						var ii = dataC - 1;
						$('#struc-item' + ii).find('a').css({
							'display': 'block'
						});
						var jj = ii + 6;
						$('#struc-item' + jj).find('a').css({
							'display': 'none'
						});
						dataC -= 1;
						$(_this).parent().data('count', dataC);
						$(_this).siblings('.struc-cont').find('.struc-items').css({
							'left': -dataC * 1.29 + 'rem'
						});
						if (dataC == 0) {
							$(_this).css({
								'opacity': '.5'
							});
							$(_this).siblings('.struc-next').css({
								'opacity': '1'
							});
						} else if (dataC == dataCY) {
							$(_this).css({
								'opacity': '1'
							});
							$(_this).siblings('.struc-next').css({
								'opacity': '.5'
							});
						} else {
							$(_this).css({
								'opacity': '1'
							});
							$(_this).siblings('.struc-next').css({
								'opacity': '1'
							});
						}
					}
				}
			});
			$(this).find('.struc-next').on('click', function () {
				var _this = this;
				dataC = $(_this).parent().data('count'),
					items = $(_this).siblings('.struc-cont').find('.struc-items').children('.struc-item'),
					dataCY = items.length - 6;

				if (dataCY - dataC > 0) {
					if (dataC < dataCY) {
						for (var i = 0; i <= dataC; i++) {
							$('#struc-item' + i).find('a').css({
								'display': 'none'
							});
							var j = i + 6;
							$('#struc-item' + j).find('a').css({
								'display': 'block'
							});
						};
						dataC += 1;
						$(_this).parent().data('count', dataC);
						$(_this).siblings('.struc-cont').find('.struc-items').css({
							'left': -dataC * 1.29 + 'rem'
						});
						if (dataC == 0) {
							$(_this).css({
								'opacity': '1'
							});
							$(_this).siblings('.struc-prev').css({
								'opacity': '.5'
							});
						} else if (dataC == dataCY) {
							$(_this).css({
								'opacity': '.5'
							});
							$(_this).siblings('.struc-prev').css({
								'opacity': '1'
							});
						} else {
							$(_this).css({
								'opacity': '1'
							});
							$(_this).siblings('.struc-prev').css({
								'opacity': '1'
							});
						}
					}
				}
			});
		}
		$('.struc-wrap').struc();*/
		

  //热力图
function dateChange(){


	  if(heatGeomeSwitch==0){
		return ;
		}
		if(heatSwitch!=1  ){
		return ;
		}

      var dataMap={infoOrgCode:infoOrgCode,beginTime:createTimeStart,endTime:createTimeEnd};
      var year = yearChoose;
    	var month = monthChoose;
		var day = dayChoose;
		
		var date_ = "";
		var type = "";
		
		if(yearChoose == 0){
			year = yearDefault;
		}
		if(monthChoose == 0){
			month = monthDefault;
		}
		if(dayChoose == 0){
			day = dayDefault;
		}

    	if("year" == dateType){
    		date_ = year;
    		type = "1";
    	}else if("month" == dateType){
    		date_ = year + month;
    		type = "2";
    	}else if("day" == dateType){
    		date_ = year + month  +day;
    	}
      
     dataMap.date_=date_;
     dataMap.dateType=dateType;
     dataMap.type=type;

       $.ajax({
        url: '${rc.getContextPath()}/zhsq/map/eventHeatController/findEventHeatData.json',
        type: 'POST',
       data: dataMap,
        dataType:"json",
        error: function(data){
   
       layer.msg('热力图信息获取失败!！', {
				icon : 5
			});
        },
        success: function(data){
        	if(isNotUnll(heatMap)){
		        heatMap.removeLayer();
		     }
			var list = data['list'];
			if(!isNotUnll(list)){
			return ;
			}
	           var struc3DAry = new Array();
				 var config = {
                    container: this.box,
                    radius: 30,
                    maxOpacity: .7,
                    minOpacity: 0,
                    blur: .85,
                    gradient: {
                        0.10: "rgba(188, 255, 27, 0.8)",
	                    0.50: "rgba(255, 194, 76, 0.8)",
	                    1.00: "rgba(254, 75, 62, 0.8)"
                    }
                };

				for(var i=0,l=list.length;i<l;i++){
			   
					var xy=list[i].split(":");
					// 三维坐标位置
					var struc3D=[xy[0],xy[1],1]; 
					struc3DAry.push(struc3D);
				}	
					
				if(isNotUnll(heatMap)){
                    if(isNotUnll(data['max'])){
                              heatMap.setData(struc3DAry,data['max'].MAX);
				              heatMap.freshenLayer();
                     }else{
                              heatMap.removeLayer();
                     }      
				    	 
		       }else{
		       
		        if(isNotUnll(data['max']) ){
                    heatMap=$.fn.ffcsMap.createHeatMap.Box("heatLayer",config,struc3DAry,data['max'].MAX);   
                 }
		       }
		}
    });
                
 }
	//3d柱状图	
   function loadGeometryData(){  
	   if(heatGeomeSwitch==0){
			return ;
		}
       if(geometrySwitch!=1 ){
		return ;
		}

	  var dataMap={'infoOrgCode':infoOrgCode,'beginTime':createTimeStart,'endTime':createTimeEnd,'dateType':dateType};
	  
	  if(infoOrgCode.length >6 ){ 
		  var year = yearChoose, month = monthChoose,day = dayChoose;
		  var extent = $.fn.ffcsMap.getViewExtent();
		  var xmax=extent.xmax,xmin=extent.xmin,ymax=extent.ymax,ymin=extent.ymin;
		  var date_ = "", type = "";
			if(yearChoose == 0){
				year = yearDefault;
			}
			if(monthChoose == 0){
				month = monthDefault;
			}
			if(dayChoose == 0){
				day = dayDefault;
			}
	   	
	   	if("year" == dateType){
	   		date_ = year;
	   		type = "1";
	   	}else if("month" == dateType){
	   		date_ = year + month;
	   		type = "2";
	   	}else if("day" == dateType){
	   		date_ = year +  month + day;
	   	}
	     dataMap.date_=date_;
	     dataMap.type=type;
	     dataMap.ymax=ymax;
	     dataMap.xmin=xmin;
	     dataMap.xmax=xmax;
	     dataMap.ymin=ymin;
	
	  }
	  
	  
       $.ajax({
        url: '${rc.getContextPath()}/zhsq/map/eventHeatController/findEventGeometryData.json',
        type: 'POST',
       data: dataMap,
        dataType:"json",
        error: function(data){
        
         layer.msg('柱状图信息获取失败！', {
				icon : 5
			});
        
        },
        success: function(data){
	     //	var	zz = {"TOTAL":11,"GRID_ID":217292,"X":115.94102452005352,"Y":28.562854769142383,"GRID_NAME":"南昌县"};
				var list = data['list'];
				var max =  data['max'];
				var struc3DAry = new Array();
			    var strucTextAry = new Array();
			    $.fn.ffcsMap.Geometry.RemoveBox("boxLayer");
				 $.fn.ffcsMap.Geometry.RemoveText("textLayer");
			     $.fn.ffcsMap.removeCube();
			    
		      var level=parseInt(gridLevel);
              var width=2000,height=2000,size=800,z=100;
		      
		      switch(level) {
				    case 3:
				     level=3000; width=300;height=300;size=120;z=30;
				        break;
				     case 4:
				     level=600;  width=80;height=80;size=40;z=10;
				        break;    
				     default:
				      level=20000;
				} 
				var sum=0,p=0, total=0,color='';
				for(var i=0,l=list.length;i<l;i++){
			      total= list[i].TOTAL;
			   sum=0;p=0;
			   color="rgba(255, 194, 76, 0.8)";
			    if(isNotUnll(total)  && max>0){
			    	p = total/max;
                    sum=p*level;
                 	p=parseInt(p*10);
                 		if(p>=9){
                 		 color="rgba(254, 75, 62, 0.8)";
                 		}else if(p<4){
                 		 color="rgba(188, 255, 27, 0.8)";
                 		}
                 					
                 }else{
                 	total=0;
                 	color="rgba(188, 255, 27, 0.8)";
                 }
                 
			    
			    if(total==0){
			    	continue;
			    }
 
			   var struc3D = {
					geometry : {// 几何图形三维
						width : width,
						height : height,
						depth : sum
					},
					position : {// 三维坐标位置	
					},
					material : {// 材质
						opacity : 0.9,
						transparent : true,
						color : color
					},
					rotate : {// 旋转角度
						x : 0,
						y : 0,
						z : 0
					}
				};
					// 三维坐标位置

					struc3D.position ={x:list[i].X,y:list[i].Y,z:0}; 
					

	
	var strucText = {
		font : {// 字体属性
			size : size,
			height : 1,
			color : 0xffffff,
			bevelEnabled : false,
			bevelSize : 1,
			text : total+""
		},
		position : {// 三维坐标位置	
					},
		rotate : {// 旋转角度
			x : 90,
			y : 0,
			z : 0
		}
	};
	
	strucText.position ={x:list[i].X,y:list[i].Y,z:z+sum}; 

			if(isNotUnll(list[i].X)  &&    isNotUnll(list[i].Y)){
					 struc3DAry.push(struc3D);
					 strucTextAry.push(strucText);
			}			
					
		}
	     $.fn.ffcsMap.Geometry.Box("boxLayer", struc3DAry);
	     $.fn.ffcsMap.Geometry.Text("textLayer", strucTextAry);	
		}
    });

}
      //非空判断
   function isNotUnll(str){     
       if(str !== null && str !== undefined && str !== ''){
		return true;
		}                 
       return false ;                 
  }       
 	
    
   
   function isopen(boo){  
	   if(heatGeomeSwitch==1){
		   $('.ss').addClass('pc-icon-tabs-0deg');//按钮形态修改
		   $('.ss').siblings('.tabs-check').addClass('tabs-check-on'); //显示复选框
	   }else{

			$('.ss').removeClass('pc-icon-tabs-0deg');//按钮形态修改
			$('.ss').siblings('.tabs-check').removeClass('tabs-check-on'); //隐藏复选框	
	   }
	   if(heatSwitch==1){
		$(".hot").addClass('tabs-checked');
			dateChange();
		}else{
			$(".hot").removeClass('tabs-checked');
		} 
		if(geometrySwitch==1){
		 loadGeometryData();
		$(".bar").addClass('tabs-checked');
		  if(heatGeomeSwitch==1 && boo){
				//iniTilt = 45;
			  //$.fn.ffcsMap.goToView({ tilt: 45});
		  }
		}else{
			$(".bar").removeClass('tabs-checked');
		}
  }  
		var layerPointObj = {};
		var camLayerObj={};//储存监控撒点与列表的编号与名称信息,id为key
		//撒点定位
		function getCamPoint(orgCode,companyType,line,camSearch){
			var pointTypeStr;
			if(orgType=="1"){
				pointTypeStr="";
			}else{
				if($('#mask-dianwei').attr("src")==""){
					pointTypeStr="N";
				}else{
					pointTypeStr=pointTypes;
				}
			}
			$.fn.ffcsMap.removeLayer("layerPoint");
			
			var zoomRound = getZoomRound();
			var proportion=2**(zoomRound-10);//地图缩放系数
			var extent = $.fn.ffcsMap.getViewExtent();
			//var xmax=extent.xmax,xmin=extent.xmin,ymax=extent.ymax,ymin=extent.ymin;
			var xmax=extent.xmax-0.091/proportion,xmin=extent.xmin+0.091/proportion,ymax=extent.ymax+0.148/proportion,ymin=extent.ymin-0.031/proportion;
			$.ajax({
					type: 'POST',
					url: '${rc.getContextPath()}/zhsq/map/DDEarthController/pointListData.json',
					dataType: 'json',
					data:{
						"ymax":ymax,
				     	"xmin":xmin,
				     	"xmax":xmax,
				     	"ymin":ymin,
						"orgCode":"",
						"line":line,
						"camSearch":camSearch,
						"companyType":companyType,
						"pointTypes":pointTypeStr
					},
					success: function(data) {
						var struc3DAry = new Array();
						if(data && data.length>0) {
							var lastG =lastGraphic!=null?true: false;
							for(var i=0;i<data.length;i++){
								var cam ={code:data[i].INDEXCODE,name:data[i].NAME};
								camLayerObj[data[i].ID]=cam;
								var obj ={text:data[i].NAME,monitorId:data[i].ID,indexCode:data[i].INDEXCODE,x:parseFloat(data[i].X),y:parseFloat(data[i].Y),iconUrl:'${uiDomain!''}/web-assets/_big-screen/nanchang-cc/images/',iconWidth:28,iconHeight:36 }
								if(lastG && lastGraphic.attributes.data.monitorId == obj.monitorId){
									obj.iconUrl +='icon-point-online_click.png';
								}else if(data[i].LINE=="1"){
									obj.iconUrl +='icon-point-online.png'; 
									}else{
									obj.iconUrl +='icon-point-offline.png'; 
								}
								struc3DAry.push(obj);
							}
							$.fn.ffcsMap.renderPoint("layerPoint", struc3DAry);
							layerPointObj={};
							var graphicsLayer = $.fn.ffcsMap.getMap().findLayerById("layerPoint");
							for(var i=0;i<graphicsLayer.graphics.length;i++){
								layerPointObj[graphicsLayer.graphics.items[i].attributes.data.monitorId]=graphicsLayer.graphics.items[i];
								if(lastG && lastGraphic.attributes.data.monitorId == graphicsLayer.graphics.items[i].attributes.data.monitorId){
									lastGraphic = graphicsLayer.graphics.items[i];
								}
							}
							if(isCamListDetail==true){
								showCamListPoint(camMonitorId);
							}
						}
					},
					error: function(data) {
						//alert("获取撒点定位数据出错！");
					}
				});
			
			
		}
		var lastGraphic = null;
		var lastpic = null;
		//视频详情页面
		function showCamPoint(data,graphic){
			if(lastGraphic != null){
				lastGraphic.symbol=new $.fn.ffcsMap.gPictureMarkerSymbol()({
					url : lastpic,
					width : 28,
					height : 36,
					xoffset : 0,
					yoffset : 18
				});
			}
			isCamListDetail=false;
			//if(navigator.userAgent.match("ua_ffcs_nc")){
			if(isAndroid){ 
				var durl;
				$.ajax({
					type: "POST",
					url: '${rc.getContextPath()}/zhsq/map/DDEarthController/getVideoUrl.json',
					//dataType:"json",
					async: false,
					data:{
						"indexCode":data.indexCode
					},
					success: function(datas){
						if(datas == undefined || datas == null || datas.length == 0){
							return;
						}
						var json = eval('(' + datas + ')'); 
						if(json.code=="0"){
							durl=json.data.url;
							
						}else{
							alert("获取预览url失败");
						}
					}
				});
				var json = {"monitor_code":data.indexCode,"monitor_name":data.text,"url":durl,"x":data.x,"y":data.y,"grid_name":gridName,"grid_id":gridId} ;
				window.location.href="getPlayerUrl:" + JSON.stringify(json);
			}else{
				$('#title_p').html("视频详情");
				isPointDetail=true;
				//$('.mask-container').css('display', 'block');
				$('#popupWindowDiv').css('height', '0%');
				$('#mc-item').css('display', 'block');
				//$('#mask-vedio mask-container-on').css('display', 'block');
				$('#popupWindowDiv .mask-event').removeClass('mask-container-on');
				getVideoDetail(data.indexCode,data.monitorId);
				$("#monitorId").val(data.monitorId);
				$('#mc-item').addClass('mci-big').removeClass('mci-small');
				$('#popupWindowDiv').fadeIn(200);
				//$('#popupWindowDiv').fadeOut(200);
				$('.mask-container-on').removeClass('mask-container-on');
				$('#mask-vedio').addClass('mask-container-on');
				$("#video_name").html(data.text);
				$("#video_code").html(data.indexCode);
			}
			
			
			
			graphic.symbol = new $.fn.ffcsMap.gPictureMarkerSymbol()({
				url : "${uiDomain!''}/web-assets/_big-screen/nanchang-cc/images/icon-point-online_click.png",
				width : 28,
				height : 36,
				xoffset : 0,
				yoffset : 18
			});
			
			lastGraphic=graphic;
			lastpic=graphic.attributes.data.iconUrl;
			var opts = {
					center: [data.x, data.y]
				}
			setTimeout(function () {
				$.fn.ffcsMap.goToView(opts);
	        }, 500); 
				
		}
		var isCamListDetail=false;
		var camMonitorId;
		//监控图标替换
		function showCamListPoint(monitorId){
			if(lastGraphic != null){
				lastGraphic.symbol=new $.fn.ffcsMap.gPictureMarkerSymbol()({
					url : lastpic,
					width : 28,
					height : 36,
					xoffset : 0,
					yoffset : 18
				});
			}
			layerPointObj[monitorId].symbol= new $.fn.ffcsMap.gPictureMarkerSymbol()({
				url : "${uiDomain!''}/web-assets/_big-screen/nanchang-cc/images/icon-point-online_click.png",
				width : 28,
				height : 36,
				xoffset : 0,
				yoffset : 18
			});
			
			lastGraphic=layerPointObj[monitorId];
			lastpic=layerPointObj[monitorId].attributes.data.iconUrl;
		}
		function showPointById(opts){
			$.fn.ffcsMap.goToView(opts);
			//pointInfoListData();//刷新点位页面，重新撒点
		}
		function showClickPoint(struc3DAry){
			$.fn.ffcsMap.renderPoint("clickPoint", struc3DAry);
		}
		function removeClickPoint(){
			$.fn.ffcsMap.removeLayer("clickPoint");
		}
		
		var lastZoom=-1;//,iniTilt=45;
		var diyuIframeWin = null;
		function mapZoomChange(view){
			var zoom = Math.round(view.zoom);
			console.log(zoom);
			mapCenterLevel = zoom;
			if(diyuIframeWin == null){
				diyuIframeWin = document.getElementById("diyuIframe").contentWindow;
			}
			if( $.fn.ffcsMap.createHeatMap.heatMapLayer  !== undefined&& $.fn.ffcsMap.createHeatMap.heatMapLayer.data !== undefined&& $.fn.ffcsMap.createHeatMap.heatMapLayer.data.length>0 ){
				$.fn.ffcsMap.createHeatMap.heatMapLayer.freshenLayer();
				$.fn.ffcsMap.createHeatMap.isClear=true;
			}
			
			if(isTreeClick){
				isTreeClick = false;
				if(zoom>14 ){
					camlistData();//监控设备列表
					pointInfoListData();//点位列表
				}
				return;
			}
			gridLevel = 3,polygonLayer='gridLayer';//默认区县
			
			if(zoom<11){ //市级级别
				isPointDetail=false;
				gridLevel = 2;
				setPolygon(zoom,polygonLayer);
				if(gridId==${gridId?c} || gridId == ''){
					return;
				}
				gridId=${gridId?c};
				$("#diyu").attr("gridId",gridId).html($("#gridName").val());
				var rsNode = diyuIframeWin.initGridData[gridId];
				diyuNodeClick({infoOrgCode : rsNode.infoOrgCode,gridName : rsNode.name,gridId : rsNode.gridId,gridLevel : rsNode.gridLevel});
				return ;
			}else if(zoom>14 ){//街道
				gridLevel = 4;
				polygonLayer='pointLayer';
				loadGeometryData();
				//if(isCamListDetail==true){
					camlistData();//监控设备列表
					//showCamListPoint(camMonitorId);
				//}
				if(isPointDetail==false){
					pointInfoListData();//点位列表
				}else{
					if(isPointListDetail){//列表点击详情
						//pointInfoListData();
						//showPointDetail(pointDetailId,"");
						showListPointDetail(pointDetailId);
					}
				}
				
			}else{//区县
				//gridLevel = 3;
				initRightList();//初始化设备点位列表页面
				isPointDetail=false;
				setPolygon(zoom,polygonLayer);
			}
			$.ajax({
					type: 'POST',
					url: '${rc.getContextPath()}/zhsq/map/DDEarthController/getGridByXY.json',
					dataType: 'json',
					data:{
						"x":view.center.x,
						"y":view.center.y,
						"gridLevel":gridLevel
					},
					success: function(data) {
						if(data.list!=null && data.list.length>0){
							var g = data.list[0];
							if(gridId==g.gridId){
								return;
							}
							gridId=g.gridId;
							$("#diyu").attr("gridId",gridId).html(g.gridName);
							var rsNode = diyuIframeWin.initGridData[gridId];
							diyuNodeClick({					
								infoOrgCode : rsNode.infoOrgCode,
								gridName : rsNode.name,
								gridId : rsNode.gridId,
								gridLevel : rsNode.gridLevel
							});
						}
					},
					error: function(data) {
						 alert('获取当前中心经纬度区域异常');
					}
				});
		}
		function setPolygon(zoom,layerName){
			if(lastZoom != zoom ){
				lastZoom = zoom;
				var opacity = toRGBA();
				$.fn.ffcsMap.cloneLayerSetOpacity('gridLayer',{opacity :opacity });
				$.fn.ffcsMap.cloneLayerSetOpacity('pointGridLayer',{opacity :opacity });
			}
		}
		function initMapCallback(){
			opts = {
				center: [initX, initY],
				zoom: initZoom,tilt: 45
			}
			locateCenterAndLevel(opts);
			//showSzfxFirst();
			var zhz =${zhz!'false'};
			if(zhz){
				showZhxxFirst();
			}
			initFn1();
			initFn2();
			initFn3();
			initFn4();
			lastZoom = Math.round($.fn.ffcsMap.getView().zoom);
			drawPolygon();//画轮廓
			//setYiQing();
			//setKaKou();
		}
		function showSzfxFirst(){
			szfxShow();
			$("#gridNameClick").hide();
        		$("#todoTotalClick").hide();
		}
		function showZhxxFirst(){
			zhxxShow();
			$("#gridNameClick").hide();
        		$("#todoTotalClick").hide();
		}
	
		function setTypeForFigure(dateType){
			if("year" == dateType){
				$("#typeForFigure").text("--月份");
			}else if("month" == dateType){
				$("#typeForFigure").text("--日期");
			}else if("day" == dateType){
				$("#typeForFigure").text("--小时");
			}
		}
		
		function mapZoomBegin(view){
			 if( $.fn.ffcsMap.createHeatMap.heatMapLayer  !== undefined && $.fn.ffcsMap.createHeatMap.isClear 
						&& $.fn.ffcsMap.createHeatMap.heatMapLayer.data !== undefined 
						&& $.fn.ffcsMap.createHeatMap.heatMapLayer.data.length>0   ){
					$.fn.ffcsMap.createHeatMap.isClear=false;
					$.fn.ffcsMap.createHeatMap.heatMapLayer.clearData();
					}
			 
		}
		function toRGBA(){
			var zoom = Math.round($.fn.ffcsMap.getView().zoom),a=0.6;

				switch(zoom){
					case 9:case 12:case 17: a=0.4;break;
					case 10:case 13:case 18: a=0.2;break;
					case 11:case 16: a=0.6;break; 
					case 14:case 19:case 20: a=0.1;break;
				}
			
			return a;
		}
		function getZoomRound(){
			var zoom = Math.round($.fn.ffcsMap.getView().zoom);
			return zoom;
		}
	function mapClick(graphic){
		var bizData =graphic.getAttribute("data");
		if(bizData){
			if(bizData.layerName=='layerPoint'){
				showCamPoint(bizData,graphic);
			}else if(bizData.layerName=='gridLayer'){
				showPointInfoData(bizData,graphic);
			}else if(bizData.layerName=='pointLayer'){
				showPointMark(bizData);
			}else if(bizData.layerName=='pointGridLayer'){
				//showPointGridMark(graphic);
				showPointInfoData(bizData);
			}else if(bizData.layerName=='highLightGridLayer'){
				showPointInfoData(bizData);
			}
		}
		
	}
	function mapDoubleClick(view,results,event){
		var bizData =results.graphic.getAttribute("data");
		if(bizData){
			if(bizData.layerName=='gridLayer'){
				screenPoint = view.toMap({ x : event.x, y : event.y });
				var zooms = Math.round(view.zoom);
				var opts = {
					center: [screenPoint.x, screenPoint.y],
					zoom: 15
				}
				if(zooms<11){ //市级级别
					opts.zoom = 11;
				}
				view.goTo(opts);
			}
		}
	}
	/* $(document).click(function () {
		//if($("#popupWindowDiv").attr){
			selected='';
			videoSelect='';
			$("#video_name").html("");
			$("#video_code").html("");
			$("#videoUl>li>div").css('border','none');
		//}
	}); */
	function stopBubbling(e) {
        e = window.event || e;
        if (e.stopPropagation) {
            e.stopPropagation();      //阻止事件 冒泡传播
        } else {
            e.cancelBubble = true;   //ie兼容
        }
    }
	//未播视频点击选中
	function selectDiv(e){
    	videoSelect=$(e);
    	$("#video_name").html("");
		$("#video_code").html("");
    	selected=parseInt($("#"+videoSelect[0].id).attr("index"));
    	selectByHand=selected;
    	videoSelectByHand=videoSelect;
    	$(e).css('border','3px solid rgba(255, 130, 62, 1)').parent().siblings().find('div').css('border','none');
    	stopBubbling(e);
	}

	var indx=0;
    function initFn3 () {
        //点位视频弹窗切换
        //var videoNum=1;
        //var selected='';//第i个
        //var videoSelect='';
        //var playArr = [0,0,0,0,0,0,0,0,0];//播放9宫格，0-未在播，1-在播
        $('#dianwei-shipin-title').on('click', 'a', function (e) {
            $(this).addClass('active').siblings('a').removeClass('active');
            indx = $(this).index();//0,1,2
            stopBubbling(e);
            if(indx==1){
            	
            	$("#videoUl").addClass('ul-sipin2').removeClass('ul-sipin1').removeClass('ul-sipin3');
            	
            	var html = '';
            	if(videoNum<4){
            		//1切4
            		for(var i=1;i<4;i++){
                		html += '<li > <div class="bg" onclick="selectDiv(this)" id="vli'+(i)+'" index='+(i)+'  play="0"></div></li>';
                	}
            		playArr=[1,0,0,0];
                	$("#videoUl").append(html);
            	}else if(videoNum>4){            		
            		//9切4            		
            		var saveVideo=[];//预保留
            		var saveArr=[];//保留
            		var delArr=[];
            		var vnum=0;//
            		var isTop4 = false;//被选中探头是否在前4
            		
            		//selected 下标从0开始
        			//playArr 1,1,0,0,0,0,2,0,1
        			for(var i=0;i<playArr.length;i++){
        				if(playArr[i]==1 && saveVideo.length<4){//保留前4个
        					saveVideo.push(i);
        					if(i === selectByHand){
        						 isTop4 =true;
        						 selectByHand=saveVideo.indexOf(selectByHand);
        					}
        				}else{//其他
        					delArr.push(i);
        				}
        			}
        			if(selectByHand!=''){
        				if(!isTop4){//如果被选中的不在前4
        				//1,0,0,0,0,2,0,0,0
        					if(saveVideo.length==4){//替换第4个框为选中
            					delArr[delArr.indexOf(selectByHand)]=saveVideo[3];
    	        				saveVideo[3] = selectByHand;
    	        				//selectByHand=3;
            				}else{
            					delArr[delArr.indexOf(selected)]=selected;
            					//selected='';
            				}
        				}
        				
        			}
        			if(saveVideo.length < 4){
        				playArr = [0,0,0,0];
        				for(var l=saveVideo.length,i=0;i<l;i++){
        					playArr[i]=1;
                		}
        				//saveVideo 2 
        				//delArr 7	
        				for(var i=saveVideo.length,j=delArr.length-1-(3-i);i<4;i++,j++){
        					saveVideo.push(delArr[j]);  
                		}
        			}else{
        				playArr = [1,1,1,1];
        				if(selectByHand!=''){
        					if(videoSelect.attr("play")==="0"){
            					playArr[selectByHand]=0;
            				}
        				}
        				
        			}
					
					//手动选中的框放在第一个
					if(selectByHand!=''){
            			for(var i=0;i<3;i++){
            				saveArr.push(saveVideo[i]);
            			}
            		    saveArr.push(selectByHand);
            		}else{
            			for(var i=0;i<4;i++){
            				saveArr.push(saveVideo[i]);
            			}
            		}
					
            		var delLi = $("#videoUl>li");
            		//saveVideo	[0,5]
            		//先删除多余li  例:[1,2,3,4,6,7,8]
            		for(var i=0, l=delArr.length>5?5:delArr.length;i<l;i++){
            			delLi.eq(delArr[i]).remove();
            		}
            		//再将id重新分配 0,1,6,8
            		//if(selectByHand==''){
            			for(var f=0;f<4;f++){
                			$("#vli"+saveArr[f]).attr({"index":f,"id":("vli"+f)});//重新设置div的index
                			if($("#vli"+f).attr("play")=="1"){
    	            			$("#videoUrl"+saveArr[f])[0].contentWindow.iframeId="videoUrl"+f;
                			}
                			$("#videoUrl"+saveArr[f]).attr("id","videoUrl"+f);//重新设置iframe的id
                		}
            		//}
            		/* else{//手动选
            			for(var f=0;f<3;f++){
                			$("#vli"+saveVideo[f]).attr({"index":f+1,"id":("vli"+(f+1))});//重新设置div的index
                			if($("#vli"+(f+1)).attr("play")=="1"){
    	            			$("#videoUrl"+saveVideo[f])[0].contentWindow.iframeId="videoUrl"+(f+1);
                			}
                			$("#videoUrl"+saveVideo[f]).attr("id","videoUrl"+(f+1));//重新设置iframe的id
                		}
            			if($("#vli"+selectByHand).attr("play")=="1"){
	            			$("#videoUrl"+selectByHand)[0].contentWindow.iframeId="videoUrl0";
            			}
            			$("#vli"+selectByHand).attr({"index":0,"id":"vli0"});//重新设置div的index
            			$("#videoUrl"+selectByHand).attr("id","videoUrl0");//重新设置iframe的id
            		} */
            		
            	}
            	selectByHand='';//手动选择第i个
                videoSelectByHand='';//手动选择
            	videoNum=4;
            	selectNext();
            }else if(indx==2){
            	selectByHand='';//手动选择第i个
                videoSelectByHand='';//手动选择
            	$("#videoUl").addClass('ul-sipin3').removeClass('ul-sipin1').removeClass('ul-sipin2');
            	var html = '';
            	for(var i=videoNum;i<9;i++){
            		html += '<li > <div class="bg" onclick="selectDiv(this)" id="vli'+(i)+'" index='+(i)+' play="0"></div></li>';
            		playArr.push(0);
            	}
            	$("#videoUl").append(html);
            	//playArr = [1,0,0,0,0,0,0,0,0];
            	videoNum=9;
            	//select='';
            	selectNext();
            }else if(indx==0){
            	$("#videoUl").addClass('ul-sipin1').removeClass('ul-sipin3').removeClass('ul-sipin2');
            	if(selectByHand!=''){
            		$("#videoUl>li").not(":eq("+selectByHand+")").remove();
            		if($("#vli"+selectByHand).attr("play")=="1"){
            			$("#videoUrl"+selectByHand)[0].contentWindow.iframeId="videoUrl0";
        			}
            		$("#vli"+selectByHand).attr({"index":0,"id":"vli0"});//重新设置div的index
        			$("#videoUrl"+selectByHand).attr("id","videoUrl0");//重新设置iframe的id
        			selectByHand='';//手动选择第i个
        	        videoSelectByHand='';//手动选择
            	}else{
            		//除第1个外都移除
            		$("#videoUl>li").not(":first").remove();
            	}
            	selected=0;
            	selectByHand='';
            	videoSelectByHand='';//手动选择
            	playArr=[1];
            	videoNum=1;
            	//select='';
            	selectNext();
            	
            }
        });
        // 事件上报
        $('#shangbao').click(function () {
        	var videoUrl =document.getElementById("videoUrl"+currentIndex).contentWindow;
			//var con =videoUrl.document.getElementById("content").contentWindow;
			var Snapshot = videoUrl.document.getElementsByClassName('vjs-control-content')[0];
			$(Snapshot).click();	
        });

        //提交取消按钮
       /* $('.submit1').click(function () {
            $(this).addClass('active').siblings().removeClass('active');
        });*/
    };
	
		//视频详情
		//var playRule=1;//播放规则 0-通用；  1-南昌
		var selected='';//第i个
        var videoSelect='';//预选下一个播放框
        var selectByHand='';//手动选择第i个
        var videoSelectByHand='';//手动选择
        var currentIndex='';//当前第i个
		var videoNum=1;
		//var playArr = [0,0,0,0,0,0,0,0,0];//播放9宫格，0-未在播，1-在播
		playArr=[0];
		function getVideoDetail(indexCode,monitorId){ 
			$('#shijian-center1').hide();
			$('#dianwei-center1').show();
			var nextLi = -1;
			var videoIframe ;
			if(videoSelectByHand!=''){//手动选中
				if(videoSelectByHand.attr("play")==="0"){
					var html='<iframe src="" width="100%" height="100%" id=videoUrl'+(selectByHand)+' webkitallowfullscreen="" mozallowfullscreen="" allowfullscreen="" frameborder="0" allowtransparency="true" allowfullscreen=""  allow="autoplay"></iframe>';
					$("#vli"+selectByHand).attr({"play":"1","class":"play"}).append(html);					
				}
					nextLi = selectByHand;
				videoIframe = $("#videoUrl" + selectByHand);
				playArr.splice(selectByHand,1,1);
			}else{//自动预选下一个播放框
				if(videoSelect!=''){//如果有选中某个框
					if(videoSelect.attr("play")==="0"){
						var html='<iframe src="" width="100%" height="100%" id=videoUrl'+(selected)+' webkitallowfullscreen="" mozallowfullscreen="" allowfullscreen="" frameborder="0" allowtransparency="true" allowfullscreen=""  allow="autoplay"></iframe>';
						$("#vli"+selected).attr({"play":"1","class":"play"}).append(html);					
					}
						nextLi = selected;
					videoIframe = $("#videoUrl" + selected);
					playArr.splice(selected,1,1);				
				}else{
					//首次进入1屏播放
					for(var i=0;i<playArr.length;i++){//查找下一个视频播放框下标
						if(playArr[i] == 0){
							nextLi = i;
							var html='<iframe src="" width="100%" height="100%" id=videoUrl'+i+' webkitallowfullscreen="" mozallowfullscreen="" allowfullscreen="" frameborder="0" allowtransparency="true" allowfullscreen=""  allow="autoplay"></iframe>';
							$("#vli"+i).attr({"play":"1","class":"play"}).append(html);
							videoIframe = $("#videoUrl" + i);
							playArr.splice(i,1,1);	
							break;
						}
					}
					
				}
			}
			
			currentIndex=nextLi;
			$.ajax({
				type: "POST",
				url: '${rc.getContextPath()}/zhsq/map/DDEarthController/getVideoUrl.json',
				data:{
					"indexCode":indexCode
				},
				success: function(data){
					if(data == undefined || data == null || data.length == 0){
						return;
					}
					var json = eval('(' + data + ')'); 
					if(json.code=="0"){
						var url ="${rc.getContextPath()}/zhsq/nanChang3D/index.jhtml?streamAddr="+json.data.url+"&live=1&liveStream=true&iframeId=videoUrl"+nextLi+"&monitorId="+monitorId;
						videoIframe.attr("src",url);
					}else{
						alert("获取预览url失败");
					}
					//本地环境测试用
					//var url ="http://kf.v6.aishequ.org:8080/zhsq_event/zhsq/nanChang3D/index.jhtml?streamAddr=rtmp://live.hkstv.hk.lxdns.com/live/hks&live=1&liveStream=true&iframeId=videoUrl"+nextLi+"&monitorId="+monitorId;
					//videoIframe.attr("src",url);
					
				}
			});
			selectByHand='';
			videoSelectByHand='';
			selectNext();
		}
		function selectNext(){
			var sum=0;
			for(var i=0;i<playArr.length;i++){
				if(playArr[i]===0){
					videoSelect=$("#vli"+i);
					selected=i;
					break;
				}else{
					sum+=1;
				}
			}
			if(sum==playArr.length){//都是1
				if(selected<playArr.length-1){
					videoSelect=$("#vli"+(selected+1));
					selected=selected+1;
				}else{
					selected=0;
					videoSelect=$("#vli0");
				}
			}
			if(indx==0){
				videoSelect.css('border','none');
			}else{
				videoSelect.css('border','3px solid rgba(255, 130, 62, 1)').parent().siblings().find('div').css('border','none');
			}
		}
	function eventReport(attrId){
		var monitorById =$("#monitorId").val();
		var event = JSON.stringify({"isReport":false} );  
		$("#event_frame").attr("src","${rc.getContextPath()}/zhsq/event/eventDisposalController/toAddEventByVideo.jhtml?from=_nc3d&picName=&monitorById="+monitorById+"&attrId="+attrId+"&eventJson="+encodeURIComponent(event));
		$('#shijian-center1').css('display', 'block');
		$('#dianwei-center1').css('display', 'none');
	}
     
        function initFn4 () {
            
            $('#eps-content').click(function () {
                $('#dianwei-main').addClass('dianwei-main-on');
                if($('#mask-dianwei').attr("src")==""){
    				$('#mask-dianwei').attr("src","${rc.getContextPath()}/zhsq/nanChang3D/pointTypeCheck.jhtml?pointTypes=");
    			}
            });
            //点位X按钮
            $('#dianwei_main_top_i').click(function () {
                $('#dianwei-main').removeClass('dianwei-main-on');
            });

            //左边点位选择弹窗切换
            var ind;
            var inh;
            //右边边点位选择弹窗切换
            $('#dianwei-title1').on('click', 'a', function () {
                $(this).addClass('active').siblings('a').removeClass('active');
                ind = $(this).index();
                inh = $('#dianwei-title1').scrollTop();
                $('#dianwei-content1>div').eq(ind).addClass('dianwei-xianze-content1-on').siblings().removeClass('dianwei-xianze-content1-on');
            });

         
        };
		
		function setYiQing(){
			$.get('${uiDomain!''}/web-assets/_big-screen/nanchang-cc/js/yiqing.txt', function(data){
				data = eval('(' + data + ')');
				var  strucPointAry = [];
				for(var i=0;i<data.length;i++){
					strucPointAry.push({
							id : i,   
							iconUrl : "${uiDomain!''}/web-assets/_big-screen/nanchang-cc/images/icon-yqgkqy.png",
							iconWidth : 28,
							iconHeight : 38,
							fontColor :'#fff',// 文字颜色
							fontSize : "16pt",
							bizData : {},// 存储业务数据
							x : data[i].x, 
							y : data[i].y,
							text : data[i].name,
							name : data[i].name
						});
				}
				
				$.fn.ffcsMap.renderPoint("YQLayer", strucPointAry);
			});
		}
		function setKaKou(){
			$.get('${uiDomain!''}/web-assets/_big-screen/nanchang-cc/js/kakou.txt', function(data){
				data = eval('(' + data + ')');
				var  strucPointAry = [];
				for(var i=0;i<data.length;i++){
					strucPointAry.push({
							id : i,   
							iconUrl : "${uiDomain!''}/web-assets/_big-screen/nanchang-cc/images/icon-yqkakou.png",
							iconWidth : 28,
							iconHeight : 38,
							fontColor :'#fff',// 文字颜色
							fontSize : "16pt",
							bizData : {},// 存储业务数据
							x : data[i].x, 
							y : data[i].y,
							text : data[i].name,
							name : data[i].name
						});
				}
				
				$.fn.ffcsMap.renderPoint("KKLayer", strucPointAry);
			});
		}

		var winW, scale;
		$(window).on('load resize', function () {
			setTimeout(fullPage, 10);//二次执行页面缩放，解决全屏浏览时滚动条问题
		});	
		function fullPage() {//将页面等比缩放
				winW = $(window).width();
				winH = $(window).height();

				var whdef = 100 / 1920;
				var rem = winW * whdef;// 以默认比例值乘以当前窗口宽度,得到该宽度下的相应FONT-SIZE值
				$('html').css('font-size', rem + "px");
				scale = winW / 1920;
				$('.container-fluid').css({ 'height': winH, 'overflow': 'hidden' });
				if ((!!window.ActiveXObject || "ActiveXObject" in window)) {
					$('.container-fluid').css({ 'height': winH - 1, 'overflow': 'hidden' });
				}
			}
		fullPage();
		
		
    </script>

</body>
</html>
<!-- 地图相关文件 -->
	<#include "/map/arcgis/arcgis_3d/arcgis3d_common_versionnoe.ftl" />
	<style>
		.esri-ui-top-left{bottom:0;left:0;top:auto;position:absolute;z-index:10;}
		.esri-attribution__powered-by{display:none;}
	</style>
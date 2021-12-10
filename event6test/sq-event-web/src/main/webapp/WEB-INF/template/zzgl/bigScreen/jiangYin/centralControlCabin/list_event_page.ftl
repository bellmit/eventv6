<!DOCTYPE html>
<html>
	<head>
	    <meta charset="UTF-8">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	    <title>江阴市中控舱-事件列表详情</title>
	    <link rel="stylesheet" href="${uiDomain!''}/web-assets/common/css/reset.css">
		<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/plugins/swiper-4.1.6/dist/css/swiper.min.css"/>
	    <link rel="stylesheet" href="${uiDomain!''}/web-assets/_big-screen/jiangying-cs-new/css/xxbh-mask.css">
	    <script src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js" type="text/javascript" charset="utf-8"></script>
		<#include "/component/ComboBox.ftl" />
		<link rel="stylesheet" href="${ANOLE_COMPONENT_URL}/js/components/date/css/date.css" type="text/css"/>
		<script type="text/javascript" src="${ANOLE_COMPONENT_URL}/js/components/date/jquery.anole.date.js"></script>
		<script type="text/javascript" src="${uiDomain!''}/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
		<link rel="stylesheet" type="text/css" href="${uiDomain!''}/js/jquery-easyui-1.4/themes/gray/easyui.css">
		<script type="text/javascript" src="${uiDomain!''}/js/jquery-easyui-1.4/locale/easyui-lang-zh_CN.js"></script>
		<script src="${uiDomain!''}/web-assets/plugins/swiper-4.1.6/dist/js/swiper.min.js"></script>
		<script src="${uiDomain!''}/web-assets/plugins/jquery-nicescroll/jquery.nicescroll-1.js"></script>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
		<script type="text/javascript" src="${uiDomain!''}/js/paging/paging.js"></script>
		<link rel="stylesheet" href="${uiDomain!''}/js/paging/paging.css">
		<script type="text/javascript" src="${uiDomain!''}/js/zzgl_core.js"></script>
		<link rel="stylesheet" type="text/css" href="${uiDomain!''}/css/jquery.mCustomScrollbar.css" />
		<script type="text/javascript" src="${uiDomain!''}/js/jquery.mCustomScrollbar.concat.min.js"></script>
		
		<script type="text/javascript">
			var winW, scale,rem;
			$(window).on('load resize', function () {
				fullPage();
				function fullPage() {//将页面等比缩放
					winW = <#if pwidth??>${pwidth}<#else>$(window.parent).width()</#if>
					if (winW < 1000) {
						winW = 1000;
					}
					var whdef =  100 / 1920;
					rem = winW * whdef;// 以默认比例值乘以当前窗口宽度,得到该宽度下的相应FONT-SIZE值
					$('html').css('font-size', rem + "px");
					line();
					$('.mt-tbody').getNiceScroll().resize();
				}
			});
		</script>
		<style>
		.mt-tbody {max-height: 4.5rem;}
		.mask-c-bottom {margin-top: .5rem;}
		.FromTo{margin-top:-0.05rem}
		.TimeControl{font-size:12px;}
		.mf-item>input {background-color: rgba(29, 32, 136, .1) ! important;color: white ! important}
		.ztree{background-color: rgba(7, 44, 86, .9) !important;}
		.ztree *{color:white!important;}
		.TimeControl .current,.TimeControl .TimeSearchBtn{margin-left:0;background-color:#00d8ff !important;}
		.TimeControl *{color:white!important;background-color: rgba(7, 44, 86, .9) !important;}
		.TimeControl .YearNav{background-color: rgba(0, 132, 255, 0) !important;}
		.swiper-pagination {height: 0.12rem;bottom: 1rem !important;}
		.datagrid-mask-msg{font-size:.15rem;border: solid .01rem rgba(0, 255, 252, 0.2);}
		.aj_items_t_yellow {text-align: left;}
		.TimeControl .YearNav ul li,.TimeControl .hide{background-color:transparent !important}
		.mCSB_inside > .mCSB_container{margin-right:.1rem};
		</style>
	</head>
	<body style="background-color: transparent;">
		<div class="container-fluid">
			<div class="wrap_container">
				<div class="mask-title flex flex-jc mtr30">
					<p>事件详情</p>
				</div>
				<div class="mask-content mask-content1" style="display: block;">
					<div class="mc1-content" style="display: block;">
						<div class="mc-text flex">
							<div class="mct-item clearfix bs">
								<div class="clearfix fl mlr10">
									<p>满意率：</p>
									<p class="variableList" id="satisfiedRate"></p>
								</div>
							</div>
							<div class="mct-item mct-item1 clearfix flex1 bs mlr15" id="loadOvertimeListDiv" onclick="loadOvertimeList()">
								<p>超时数：</p>
								<p>共<span class="variableList" id="overtimeEventCount"></span>条</p>
							</div>
							<div class="mct-item mct-item1 clearfix flex1 bs mlr15" id="loadSuperviseListDiv" onclick="loadSuperviseList()">
								<p>督办数：</p>
								<p>共<span class="variableList" id="superviseEventCount"></span>条</p>
							</div>
						</div>
						<div class="mask-filter clearfix">
							<input id="gridId" name="gridId" type="text" class="hide queryParam"/>
							<input class="inp1 hide queryParam" type="text" id="createTimeStart" name="createTimeStart"></input>
	                		<input class="inp1 hide queryParam" type="text" id="createTimeEnd" name="createTimeEnd"></input>
							<div class="mf-item mf-item2 clearfix fl">
								<p>所属网格：</p>
								<input class="inp1 InpDisable maet-ti-select" style="color:white;width:100px; background-color: rgba(29, 32, 136, .1);box-shadow: inset 0 0 .08rem rgba(0, 176, 255, .4);border: solid .01rem rgba(0, 176, 255, 0.5);" id="gridName" name="gridName" type="text"/></p>
							</div>
							<div class="mf-item mf-item3 clearfix fl">
								<p>日期：</p>
								<input type="text" id="_createTimeDateRender" class="inp1 InpDisable maet-ti-select" style="color:white;width:2.8rem;font-size:.14rem" value="${createTimeStart!}<#if createTimeStart?? && createTimeEnd??> ~ </#if>${createTimeEnd!}"/></p>
							</div>
							<div class="mf-item clearfix fl">
								<p>关键字：</p>
								<input type="text" id="keyRemarkWord" placeholder="事件描述/标题/事发详址/补充描述" class="bs" style="width: 4.0rem">
							</div>
							<div class="mf-btn clar fr">
								<a onclick="searchEventList()" href="javascript:void(0);" class="mfb-search fl clearfix">
									<i></i>
									<p>查询</p>
								</a>
								<a onclick="resetEventList()" href="javascript:void(0);" class="mfb-reset fr clearfix">
									<i></i>
									<p>重置</p>
								</a>
							</div>
						</div>
						<div class="mask-table">
							<div class="mask-table-content" style="display: block;">
								<div class="mt-thead bs">
									<table>
										<colgroup>
											<col style="width: auto;">
											<col style="width: 1.2rem;">
											<col style="width: 2rem;">
											<col style="width: 3rem;">
											<col style="width: 2.4rem;">
											<col style="width: 1.2rem;">
											<col style="width: 1.2rem;">
										</colgroup>
										<thead>
											<tr>
												<th>事件标题</th>
												<th>事发时间</th>
												<th>办结期限</th>
												<th>事件分类</th>
												<th>所属网格</th>
												<th>当前状态</th>
												<th>采集时间</th>
											</tr>
										</thead>
									</table>
								</div>
						
								<div class="mt-tbody bs">
									<table>
										<colgroup>
											<col style="width: auto;">
											<col style="width: 1.2rem;">
											<col style="width: 2rem;">
											<col style="width: 3rem;">
											<col style="width: 2.4rem;">
											<col style="width: 1.2rem;">
											<col style="width: 1.2rem;">
										</colgroup>
										<tbody id="eventListBodyContent" class="variableTable">
											
										</tbody>
									</table>
								</div>
							</div>
						</div>
						<div class="mask-c-bottom clearfix">
							<div class="fr mcb-pagination clearfix" id="pageDiv">
								<div id="eventTypePage"></div>
							</div>
						</div>
					</div>
				</div>
				<div class="mask-content mask-content2">
					<a href="javascript:void(0);" class="mc2-back" id="mc2-back">返回</a>
					<div class="mc-box flex">
						<div class="mcb-left bs">
							<div class="mc-title bs">
								<p>事件属性</p>
							</div>
							<div class="mtr20 mask-device">
								<div class="swiper-container md-swiper-container">
									<div class="swiper-wrapper variable" id="attrDiv">
									
									</div>
									<!-- Add Pagination -->
									<div class="swiper-pagination"></div>
								</div>
								
							</div>
							<div class="mdt-box mtr20">
								<div class="md-text mcbl-bg bs">
									<div class="angle top-left"></div>
									<div class="angle top-right"></div>
									<div class="angle bottom-left"></div>
									<div class="angle bottom-right"></div>
									<div class="mdt-item flex flex-ac">
										<div>
											<p class="mdt-icon">紧急程度:</p>
										</div>
										<!-- 一般添加general，正常添加normal，紧急添加urgent -->
										<p class="general" id="urgenceDegreeStr"></p>
									</div>
									<div class="mdt-item flex">
										<div>
											<p class="mdt-icon">影响范围:</p>
										</div>
										<p class="white flex1 variable" id="influenceStr"></p>
									</div>
									<div class="mdt-item flex">
										<div>
											<p class="mdt-icon">涉及人员:</p>
										</div>
										<p class="white flex1 variable" id="involvePeopleStr"></p>
									</div>
									<div class="mdt-item flex">
										<div>
											<p class="mdt-icon">联系人员:</p>
										</div>
										<p class="white flex1 variable" id="contactor"></p>
									</div>
								</div>
								<div class="md-text md-text1 mcbl-bg bs">
									<div class="angle top-left"></div>
									<div class="angle top-right"></div>
									<div class="angle bottom-left"></div>
									<div class="angle bottom-right"></div>
									<div class="mdt-item flex">
										<div>
											<p class="mdt-icon">信息来源:</p>
										</div>
										<p class="white flex1 variable" id="sourceStr"></p>
									</div>
									<div class="mdt-item flex">
										<div>
											<p class="mdt-icon">采集渠道:</p>
										</div>
										<p class="white flex1 variable" id="collectStr"></p>
									</div>
									<div class="mdt-item flex">
										<div>
											<p class="mdt-icon">所属网格:</p>
										</div>
										<p class="white flex1 variable" id="gridPathStr"></p>
									</div>
									<div class="mdt-item flex">
										<div>
											<p class="mdt-icon">事件编号:</p>
										</div>
										<p class="white flex1 variable" id="eventNumStr"></p>
									</div>
								</div>
							</div>
						</div>
						<div class="mcb-right flex1 mlr10">
							<div class="mcb-r-top" style="height:2rem">
								<div class="mc-title bs flex flex-jb">
									<p class="variable" id="eventNameStr"></p>
									<div class="mcb-rtc-time clearfix">
										<p>事发时间:</p>
										<p class="variable" id="cerateTimeStr"></p>
									</div>
								</div>
								<div class="mcb-rt-content" style="height:1.8rem">
									<div class="mcb-rtc-title flex flex-ac flex-jb" style="height:0.5rem">
										<p class="mcb-rtc-name variable" id="eventContentStr"></p>
									</div>
									<div class="clearfix mcb-rtc-item flex">
										<p>地点:</p>
										<p class="variable" id="occurdStr"></p>
									</div>
									<div class="clearfix mcb-rtc-item flex">
										<p>当前环节:</p>
										<p class="variable" id="curTask"></p>
										<p class="mlr20">当前状态:</p>
										<p class="variable" id="curStatus"></p>
									</div>
								</div>
								<a href="javascript:void(0);" id="db-btn" class="mcb-r-btn db-btn">督办</a>
								<a href="javascript:void(0);" id="sjhj-btn" class="mcb-r-btn sjhj-btn" style="display: block;">历史环节</a>
							</div>
							<div class="mcb-r-bottom mcb-r-bottom1 bs" style="height:4.2rem">
								<div class="mc-title bs flex flex-ac flex-jb">
									<p>流程详情</p>
									<div class="det-links-des">
										<ul class="flex layer_aj_bt_n flex-ac flex-je">
											<li class="flex flex-ac">
												<div class="aj_ks aj_ks_blue">
													<div class="aj_ks1"></div>
													<div class="aj_ks2"></div>
												</div>
												<p>当前环节</p>
											</li>
											<li class="flex flex-ac" style="margin-right:0.5rem">
												<div class="aj_ks aj_ks_gray">
													<div class="aj_ks1"></div>
													<div class="aj_ks2"></div>
												</div>
												<p>历史环节</p>
											</li>
											<li class="flex flex-ac">
					        					<div class="aj_ks aj_ks_gray">
													<div class="aj_ks1" style="background-color: #FFB90F"></div>
													<div class="aj_ks2"></div>
						        				</div>
					        					<p>驳回环节</p>
					       					 </li>
										</ul>
									</div>
								</div>
								<div class="mcb-rb-content mcb-rb-content1" style="height:3.7rem">
									<div class="layer_aj_bt_b bs flex flex-ac">
										<p class="layer_aj_bt_b1">办理环节</p>
										<p class="layer_aj_bt_b2">办理人/办理时间</p>
										<p class="layer_aj_bt_b3">办理意见</p>
									</div>
									<div class="layer_aj_bt_s">
										<div class="layer_aj_bt_line"></div>
										<ul class="layer_aj_bt_items variable" id="flowDetail">
											
										</ul>
									</div>
								</div>	
							</div>
							<div class="mcb-r-bottom mcb-r-bottom2 bs" style="display: block;height:4.2rem">
								<div class="mc-title bs flex flex-ac flex-jb">
									<p>督办</p>
								</div>
								<div class="mcb-rb-content mcb-rb-content2">
									<form id="superviseRemarkForm" name="superviseRemarkForm">
									<div class="mcb-rbc2-item mcb-rbc2-item1 flex" style="height:1.0rem">
										<p>督办意见：</p>
										<div class="flex1 mcb-rbc2i-right">
											<textarea class="bs variable" id="superviseRemark"></textarea>
										</div>
									</div>
									</form>
									<form id="smsContentForm" name="smsContentForm">
									<div class="mcb-rbc2-item mcb-rbc2-item2 flex">
										<p>发送短信：</p>
										<div class="flex1 mcb-rbc2i-right flex flex-ac flex1">
											<div id="sendMessageButton" class="mcb-rbci-ring flex flex-ac flex-jc">
												<i id="sendmsg"></i>
											</div>
											<div class="flex1 mcb-text mlr10" id="otherMobileNumsDiv">
												<input type="text" id="otherMobileNums" class="bs variable" placeholder="可以输入额外需要发送短信的手机号码,以英文逗号(,)分隔" />
											</div>
										</div>
									</div>
									<div class="mcb-rbc2-item mcb-rbc2-item3 flex" id="smsContentDiv">
										<p>短信内容：</p>
										<div class="flex1 mcb-rbc2i-right">
											<textarea class="bs variable" id="smsContent"></textarea>
										</div>
									</div>
									</form>
									<div class="mcb-rbc2-btn mtr20 clearfix">
										<div class="mf-btn flex flex-jc fr">
											<a href="javascript:void(0);" id="cancelSupervise" class="mfb-reset bs">
												<p>取消</p>
											</a>
											<a href="javascript:void(0);" id="submitSupervise" class="mfb-search mlr20">
												<p>确认督办</p>
											</a>
										</div>
									</div>
								</div>	
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<script>
			
			$('#superviseRemark').validatebox({
				required:true,validType:['maxLength[250]','characterCheck'],tipPosition:'bottom'
			})
			$('#smsContent').validatebox({
				required:true,validType:['maxLength[500]','characterCheck'],tipPosition:'top'
			})
			
			//初始化时间控件
			var createTimeDateRender = $('#_createTimeDateRender').anoleDateRender({
			BackfillType : "1",
			ShowOptions : {
				TabItems : ["常用", "年", "季", "月", "清空"]
			},
			BackEvents : {
				OnSelected : function(api) {
					$("#createTimeStart").val(api.getStartDate());
					$("#createTimeEnd").val(api.getEndDate());
				},
				OnCleared : function() {
					$("#createTimeStart").val('');
					$("#createTimeEnd").val('');
				}
			}
			}).anoleDateApi();
			
			
			
			// 滚动条优化
			$('.mt-tbody,.mdt-box,.mcb-rb-content1,#eventContentStr').niceScroll({
				cursorcolor: '#00a0e9',
				cursoropacitymax: '.4',
				cursorwidth: ".05rem",
				cursorborderradius: ".05rem",
				cursorborder: 'none',
				autohidemode: false,
			})
			
			//顶部页签点击事件
			$('.mct-item1').click(function(){
				$(this).toggleClass('active').siblings('.mct-item1').removeClass('active');
			})
		
			//发送短信按钮是否选中
			$('#sendmsg').click(function(){
				if($('#sendMessageButton').hasClass('active')){
					$('#sendMessageButton').removeClass('active');
					$('#otherMobileNums').val('');
					$('#smsContent').val('');
					$('#otherMobileNumsDiv').css("visibility","hidden");
					$('#smsContentDiv').css("visibility","hidden");
				}else{
					var isValid =  $("#superviseRemarkForm").form("validate");
					if(isValid){
						$('#sendMessageButton').addClass('active');
						$('#otherMobileNumsDiv').css("visibility","visible");
						$('#smsContentDiv').css("visibility","visible");
					}
				}
			});
			
			// 历史环节的线
			function line(){
				$('.layer_aj_bt_line').height($('.layer_aj_bt_items').height()+38);
			}
			// 内容的切换
			$('.mt-tbody>table tbody tr td+td').click(function(){
				$('.mask-content1').hide().siblings('.mask-content2').show();
				$('.mt-tbody,.mdt-box').getNiceScroll().resize();
			})
			
			//返回键点击事件
			$('.mc2-back').click(function(){
				$('.mask-content2').hide().siblings('.mask-content1').show();
				$('.mt-tbody,.mdt-box,.mcb-rb-content1').getNiceScroll().resize();
				$('.variable').html('');
				$('#superviseRemark').val('');
				$('#otherMobileNums').val('');
				destroySwiper();
			})
			
			
			// 事件属性的swiper的初始化
			var mdSwiper;
			function eventSwiper(attrflag){
				
				if(attrflag>1){
				
				mdSwiper = new Swiper('.md-swiper-container', {
					loop : true,
                    autoplay: {
                        delay: 3000,
                        stopOnLastSlide: false,
                        disableOnInteraction: false,
                    },
					pagination: {
						el: '.swiper-pagination',
					},
				});
				
				mdSwiper.autoplay.start();
				}
			}
			// 注销swiperd方法
			function destroySwiper(){
				//mdSwiper.destroy(false);
			}
			// 事件环节和督办的切换
			$('#sjhj-btn').click(function(){
				$(this).hide().siblings('#db-btn').show();
				$('.mcb-r-bottom1').show().siblings('.mcb-r-bottom2').hide();
				line();
				$('.mcb-rb-content1').getNiceScroll().resize();
			})
			$('#db-btn').click(function(){
				$(this).hide().siblings('#sjhj-btn').show();
				$('.mcb-r-bottom2').show().siblings('.mcb-r-bottom1').hide();
				$('.mcb-rb-content1').getNiceScroll().resize();
			})
			
			
			window.addEventListener("message",function(e){  
				eval(e.data);
			},false);
			
			var curEventId;
			var taskId;
			var instanceId;
			
			var imgList=[];//图片集合用于展示使用
			
			function initSupervise(eventId,type){
				
				modleopen();
		 		$(".mask-content2").addClass('db-mc').show().siblings('.mask-content1').hide();
		 		$('#sendMessageButton').removeClass('active');
				$('#otherMobileNumsDiv').css("visibility","hidden");
				$('#smsContentDiv').css("visibility","hidden");
			 	//获取事件详情
			 	$('.layer_aj_bt_line').height(0);//清空历史环节线
			 	
			 	
			 	if(type=='list'){
		 			$(".mc2-back").css('display','unset');
		 			//列表入口需要判断是否显示督办按钮（默认显示历史环节页面）
		 			$('#sjhj-btn').hide().siblings('#db-btn').show();
					$('.mcb-r-bottom1').show().siblings('.mcb-r-bottom2').hide();
					$('.mcb-rb-content1').getNiceScroll().resize();
					
					//点击取消则返回列表页面（相当于点击左上角的返回）
					$('#cancelSupervise').click(function(){
						$('#mc2-back').click();
					});
					
					
		 			
		 					
				}else{//督办入口进入，默认显示督办页面
						
					$('#db-btn').hide().siblings('#sjhj-btn').show();
					$('.mcb-r-bottom2').show().siblings('.mcb-r-bottom1').hide();
					$('.mcb-rb-content1').getNiceScroll().resize();
					
					//点击取消则返回大屏页面（相当于点击右上角的×）
					$('#cancelSupervise').click(function(){
						closeIframe();
						window.parent.postMessage('addRemindCallBack()','${biDomain!""}/bigScreen/cityManagement/index.jhtml');
					});
					
				}
				
				//点击提交则执行督办提交方法addRemind
				$('#submitSupervise').click(function(){
					addRemind(type);
				});
			 	
			 	$.ajax({
					type: "POST",
					url: '${rc.getContextPath()}/zhsq/event/centralControlCabinController/getEventInfo.json',
					data: {"eventId":eventId},
					dataType:"json",
					success: function(data){
						console.log(data);
						var event=data.event;
						
						//设置全局变量
						curEventId=event.eventId;
						taskId=data.process[0].TASK_ID;
						instanceId=data.instanceId;
						
						$('#urgenceDegreeStr').removeClass();
						var urgenceDegree=event.urgencyDegree;
						if(urgenceDegree=='03'){
							$('#urgenceDegreeStr').addClass('urgent');
						}else if(urgenceDegree=='02'){
							$('#urgenceDegreeStr').addClass('normal');
						}else{
							$('#urgenceDegreeStr').addClass('general');
						}
						$('#urgenceDegreeStr').addClass('variable')
						$('#urgenceDegreeStr').html(event.urgencyDegreeName);
						$('#influenceStr').html(event.influenceDegreeName);
						$('#involvePeopleStr').html(event.involvedNumName);
						$('#contactor').html(event.contactUser+'('+event.tel+')');
						$('#sourceStr').html(event.sourceName);
						$('#collectStr').html(event.collectWayName);
						$('#gridPathStr').html(event.gridPath);
						$('#eventNumStr').html(event.code);
						$('#cerateTimeStr').html(event.happenTimeStr);
						$('#eventContentStr').html(event.content);
						$('#occurdStr').html(event.occurred);
						$('#curStatus').html(event.statusName);
						$('#eventNameStr').html(event.eventName);
						
						if(data.process[0].IS_CURRENT_TASK){
							$('#curTask').html(formatStr(data.process[0].TASK_NAME));
						}else{
							$('#curTask').html('结案');
						}
						
								
		 				if(event.status=='04'){//已结案，不显示督办按钮
		 					$('#sjhj-btn').hide().siblings('#db-btn').show();
							$('#db-btn').hide();
							$('.mcb-r-bottom1').show().siblings('.mcb-r-bottom2').hide();
							$('.mcb-rb-content1').getNiceScroll().resize();
		 				}
						
						//填充环节信息
						var flow=data.process;
						var flowStr="";
						for(var i=0,j=flow.length;i<j;i++){
							var d=flow[i];
							//如果是当前环节
							if(d.IS_CURRENT_TASK){
								flowStr+='<li class="flex flex-ac">'+
								'<h5 class="aj_items_h aj_items_h_green">'+d.TASK_NAME+'</h5>'+
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
							
								flowStr+='<li class="flex flex-ac">'+
								'<h5 class="aj_items_h aj_items_h_green">'+d.TASK_NAME+'</h5>';
								
								if(d.OPERATE_TYPE==2){//驳回环节
									flowStr+='<div class="aj_ks aj_ks_gray">'+
										'<div class="aj_ks1" style="background-color: #FFB90F"></div>'+
										'<div class="aj_ks2"></div>'+
										'</div>';
								}else{
								
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
						
						line();
						
						if(type=='list'){
		 					$(".mc2-back").css('display','unset');
						}else{//督办入口进入，默认显示督办页面
						
						}
						
						
						// 滚动条优化
						$('.mcb-rb-content1').getNiceScroll().resize();
						
						
						//填充附件轮播信息
						var attrStr="";
						var imgs=data.imgs;
						
						var sounds=data.sounds;
						var videos=data.videos;
						var attrflag=0;
						if(imgs.length>0){
							for(var i=0,j=imgs.length;i<j;i++){
								var d=imgs[i];
								imgList.push("${IMG_URL}"+d.filePath);
								attrStr+='<div class="swiper-slide">'+
								'<img style="cursor:pointer;z-index:10" onclick="showImg('+i+')" src="${IMG_URL}'+d.filePath+'" >'+
								'</div>';
								attrflag+=1;
							}
						}
						if(sounds.length>0){
							for(var i=0,j=sounds.length;i<j;i++){
								var d=sounds[i];
								attrStr+='<div class="swiper-slide">'+
								'<img  style="cursor:pointer;z-index:10" onclick="showVideo('+d.attachmentId+')"  src="${rc.getContextPath()}/scripts/updown/swfupload/images/thumbnail/audio.jpg">'+
								'</div>';
								attrflag+=1;
							}
						}
						if(videos.length>0){
							for(var i=0,j=videos.length;i<j;i++){
								var d=videos[i];
								attrStr+='<div class="swiper-slide">'+
								'<img style="cursor:pointer;z-index:10" onclick="showVideo('+d.attachmentId+')"  src="${rc.getContextPath()}/scripts/updown/swfupload/images/thumbnail/audio.jpg">'+
								'</div>';
								attrflag+=1;
							}
						}
						
						if(attrStr==''){
							attrStr+='<div class="swiper-slide">'+
							'<img src="${rc.getContextPath()}/images/nodata.png">'+
							'</div>';
						}
				
						
						$('#attrDiv').html(attrStr);
						
						eventSwiper(attrflag);//初始化轮播
						
					},
					complete:function(){
						modleclose();
					}
				});
				
			}
			
			var begintime;
			var endtime;
			
			function initTime(){
        		var begin=new Date();
        		var end=new Date();
        		new Date(begin.setYear((new Date().getFullYear()-1)));
        		new Date(begin.setMonth((new Date().getMonth()+1)));
        		begintime= begin.format("yyyy-MM")+'-01';
        		endtime=end.format("yyyy-MM-dd");
        	}
        	
        	Date.prototype.format = function(format) {
    			var o = {
        		"M+" : this.getMonth() + 1, // month
        		"d+" : this.getDate(), // day
        		"h+" : this.getHours(), // hour
        		"m+" : this.getMinutes(), // minute
        		"s+" : this.getSeconds(), // second
        		"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
        		"S" : this.getMilliseconds()
    			// millisecond
    			}
    			if (/(y+)/.test(format))
        			format = format.replace(RegExp.$1, (this.getFullYear() + "")
                		.substr(4 - RegExp.$1.length));
    				for ( var k in o)
        				if (new RegExp("(" + k + ")").test(format))
            				format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
                    			: ("00" + o[k]).substr(("" + o[k]).length));
    			return format;
			}
			
			var curPage=1;
			var pageSize=10;
			var totalPage=1;
			
			var infoOrgCode;
			var defaultOrgCode;
			var urgencyDegree;
			var defaultGridName;
			var status;
			var startGridId;
			var eventStatus='00,01,02,03';//大屏上的列表暂时都只显示未办结的事件，排除已归档的事件
			
			
			function initEventList(urgencyDegreeStr,infoOrgCodeStr){
				modleopen();
				
				if(urgencyDegreeStr){
					urgencyDegree='0'+urgencyDegreeStr;
				}
				
				if(infoOrgCodeStr){
					infoOrgCode=infoOrgCodeStr;
					//赋予默认查询初始网格编码
					defaultOrgCode=infoOrgCodeStr;
				}
				
				
				$.ajax({
					type: "POST",
					async: false,
					url: '${rc.getContextPath()}/zhsq/event/centralControlCabinController/getGridInfo.json',
					data: {"infoOrgCode":defaultOrgCode},
					dataType:"json",
					success: function(data){
						console.log(data);
						startGridId=data.gridId;
						defaultGridName=data.gridName;
						$('#gridName').val(defaultGridName);
						
						//初始化网格树
			
						var ztreeApi=AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId, items){
								if(items!=undefined && items!=null && items.length>0){
									var grid = items[0];
									infoOrgCode=grid.orgCode;
								} 
						}, {
						OnCleared: function() {
							infoOrgCode=defaultOrgCode;
						},
						ShowOptions: {
							EnableToolbar : true
						},
						
						startGridId : startGridId
						});
						
						$(".ztree").parent().mCustomScrollbar();
						
						
					}
				});
				
				
				
				
				
				initTime();
				$('#createTimeStart').val(begintime);
				$('#createTimeEnd').val(endtime);
				$('#_createTimeDateRender').val(begintime+' ~ '+endtime);
				
				$(".mask-content1").show().siblings('.mask-content2').hide();
				
				getQueryParams();
				queryParams.infoOrgCode=defaultOrgCode;
				
				if(urgencyDegree){
					queryParams.urgencyDegree=urgencyDegree;
				}
				
				//获取事件列表页信息
				$.ajax({
					type: "POST",
					url: '${rc.getContextPath()}/zhsq/event/centralControlCabinController/getEventListPageData.json',
					data: queryParams,
					dataType:"json",
					success: function(data){
						console.log(data);
						var list=data.eventList.rows;
						totalPage=Math.ceil(data.eventList.total/pageSize);
						
						var str="";
						
						for(var i=0,j=list.length;i<j;i++){
						
							var d=list[i];
							str+='<tr style="cursor:pointer" onclick="initSupervise('+d.eventId+',\''+'list'+'\')">'+
							'<td title="'+d.eventName+'">'+d.eventName+'</td>'+
							'<td>'+new Date(d.happenTime).format('yyyy-MM-dd')+'</td>'+
							'<td>'+d.handleDateStr+'</td>'+
							'<td>'+formatStr(d.eventClass)+'</td>'+
							'<td title="'+d.gridPath+'">'+d.gridPath+'</td>'+
							'<td>'+d.statusName+'</td>'+
							'<td>'+new Date(d.createTime).format('yyyy-MM-dd')+'</td>'+
							'</tr>';
							
						
						}
						
						//填充满意度
						if(data.allEvaEvent==0){
							$('#satisfiedRate').html('暂无归档评价事件');
						}else{
							$('#satisfiedRate').html(formatDouble(data.satisfyEvaEvent/data.allEvaEvent))
						}
						
						//填充超时事件数
						$('#overtimeEventCount').html(data.findOvertimeEventCount);
						
						//填充督办事件数
						$('#superviseEventCount').html(data.findSuperviseEventCount);
						
						$('#eventListBodyContent').html(str);
						
						
						//设置分页
						if(data.eventList.total<pageSize){
			    			$("#pageDiv").hide();
						}else{
			   				$("#pageDiv").show();
							$("#eventTypePage").remove();
							$("#pageDiv").html('<div id="eventTypePage"></div>');
							$("#eventTypePage").createPage({
								pageNum: (Math.floor((data.eventList.total-1)/pageSize)+1),
								current: curPage,
								backfun: function(e) {
									curPage=e.current;
									searchEventList(1);
								}
							});
						}
						
					},
					complete:function(){ 
						modleclose();
					}
				});
			
			}
			
			//重置按钮
			function resetEventList(){
				$('#loadOvertimeListDiv').removeClass('active');
				$('#loadSuperviseListDiv').removeClass('active');
				delete(queryParams.handleDateFlag);
				delete(queryParams.superviseMark);
				$('#keyRemarkWord').val('');
				infoOrgCode=defaultOrgCode;
				$('#gridName').val(defaultGridName);
				curPage=1;
				$('.variableList').html('');
				$('.variableTable').html('');
				initEventList();
				
			}
			
			var queryParams={};
			function getQueryParams(){
			
				queryParams.eventType="all";
				queryParams.isEntryMatter=true;
				queryParams.page=curPage;
				queryParams.rows=pageSize;
				queryParams.createTimeStart=$('#createTimeStart').val();
				queryParams.createTimeEnd=$('#createTimeEnd').val();
				queryParams.keyRemarkWord=$('#keyRemarkWord').val();
				queryParams.status=eventStatus;
				
			}
			
			
			//查询按钮
			function searchEventList(type){
				modleopen();
			
				if(!type){
					curPage=1;
				}
			
				$('.variableList').html('');
				$('.variableTable').html('');
				
				getQueryParams();
			
				queryParams.infoOrgCode=infoOrgCode;
				
				if(urgencyDegree){
					queryParams.urgencyDegree=urgencyDegree;
				}
				
				//获取事件列表页信息
				$.ajax({
					type: "POST",
					url: '${rc.getContextPath()}/zhsq/event/centralControlCabinController/getEventListPageData.json',
					data: queryParams,
					dataType:"json",
					success: function(data){
						console.log(data);
						var list=data.eventList.rows;
						totalPage=Math.ceil(data.eventList.total/pageSize);
						
						var str="";
						
						for(var i=0,j=list.length;i<j;i++){
						
							var d=list[i];
							str+='<tr style="cursor:pointer" onclick="initSupervise('+d.eventId+',\''+'list'+'\')">'+
							'<td title="'+d.eventName+'">'+d.eventName+'</td>'+
							'<td>'+new Date(d.happenTime).format('yyyy-MM-dd')+'</td>'+
							'<td>'+d.handleDateStr+'</td>'+
							'<td>'+formatStr(d.eventClass)+'</td>'+
							'<td title="'+d.gridPath+'">'+d.gridPath+'</td>'+
							'<td>'+d.statusName+'</td>'+
							'<td>'+new Date(d.createTime).format('yyyy-MM-dd')+'</td>'+
							'</tr>';
							
						
						}
						
						//填充满意度
						if(data.allEvaEvent==0){
							$('#satisfiedRate').html('暂无归档评价事件');
						}else{
							$('#satisfiedRate').html(formatDouble(data.satisfyEvaEvent/data.allEvaEvent))
						}
						
						//填充超时事件数
						$('#overtimeEventCount').html(data.findOvertimeEventCount);
						
						//填充督办事件数
						$('#superviseEventCount').html(data.findSuperviseEventCount);
						
						$('#eventListBodyContent').html(str);
						
						//设置分页
						if(data.eventList.total<pageSize){
			    			$("#pageDiv").hide();
						}else{
			   				$("#pageDiv").show();
							$("#eventTypePage").remove();
							$("#pageDiv").html('<div id="eventTypePage"></div>');
							$("#eventTypePage").createPage({
								pageNum: (Math.floor((data.eventList.total-1)/pageSize)+1),
								current: curPage,
								backfun: function(e) {
									curPage=e.current;
									searchEventList(1);
								}
							});
						}
						
					},
					complete:function(){ 
						modleclose();
					}
				});
			
			}
			
			
			function closeIframe(){
				$('.variable').html('');
				$('.variableList').html('');
				$('#superviseRemark').val('');
				$('#otherMobileNums').val('');
				$('#smsContent').val('');
				$('.variableTable').html('');
				$(".mask-content2").removeClass('db-mc');
				$(".mc2-back").css('display','none');
				
				destroySwiper();
			}
			
			function formatStr(str){
				if(str == null ||str == 'null' || str == undefined ){
					return '';
				}else{
					return str;				
				}
			}
			
			//督办提交操作
			function addRemind(type){
			
				var isValid =  $("#superviseRemarkForm").form("validate");
				if(isValid){//验证督办消息是否为空
					if($('#sendMessageButton').hasClass('active')){
						isValid =  $("#smsContentForm").form("validate");
					}
				}
				
				if(isValid){
				
				modleopen();
				//执行督办提交
				
				$.ajax({
					type: "POST",
					url: '${rc.getContextPath()}/zhsq/event/eventDisposalController/addRemind.json',
					data: {"eventId":curEventId,"taskId":taskId,"instanceId":instanceId,"remarks":$('#superviseRemark').val(),"otherMobileNums":$('#otherMobileNums').val(),"smsContent":$('#smsContent').val()},
					dataType:"json",
					success: function(data){
					
						if(data.result){
						
							closeIframe();
				
							//如果入口是list页面，则执行完返回list页面，并刷新页面
							if(type!=null&&type=='list'){
								initEventList();
								window.parent.postMessage('addRemindCallBack('+2+')','${biDomain!""}/bigScreen/cityManagement/index.jhtml');
							}else{//如果入口是督办按钮或其他，则返回中控大屏并刷新对应模块
								window.parent.postMessage('addRemindCallBack('+1+')','${biDomain!""}/bigScreen/cityManagement/index.jhtml');
							}
						
						}
						
					},
					complete:function(){ 
						modleclose();
					}
				});
				
				}
			
				
			}
			
			function showImg(i){
				window.open('${rc.getContextPath()}/zhsq/showImage/indexOfPath.jhtml?fieldId=playImg&index='+i+'&paths='+imgList.toString());
			}
		
			function showVideo(id){//eventSeq
				window.open('${rc.getContextPath()}/zhsq/att/toSeeVideo.jhtml?videoType=2&attachmentId='+id);
			}
			
			//每隔三位加个逗号
			function formatInteger(nStr){
		
	 			nStr += '';  
        		x = nStr.split('.');  
        		x1 = x[0];  
        		x2 = x.length > 1 ? '.' + x[1] : '';  
        		var rgx = /(\d+)(\d{3})/;  
        		while (rgx.test(x1)) {  
            		x1 = x1.replace(rgx, '$1' + ',' + '$2');  
        		}  
        		return x1 + x2;  
	
			}
	
			//计算百分数保留2位小数并加上百分号
			function formatDouble(double){
	
				var str=Number(double*100).toFixed(2);
    			str+="%";
    			return str;
	
			}
			//两个条件不会共存，调用时先删除另外一个key
			var handleDateFlag='3';
			var superviseMark='1';
			
			function loadOvertimeList(){
				delete(queryParams.superviseMark);
				
				if(!$('#loadOvertimeListDiv').hasClass('active')){
					queryParams.handleDateFlag=handleDateFlag;
				}else{
					delete(queryParams.handleDateFlag);
				}
				
				searchEventList();
			}
			
			function loadSuperviseList(){
				delete(queryParams.handleDateFlag);
				
				if(!$('#loadSuperviseListDiv').hasClass('active')){
					queryParams.superviseMark=superviseMark;
				}else{
					delete(queryParams.superviseMark);
				}
				
				searchEventList();
			}
			
		</script>
	</body>
</html>

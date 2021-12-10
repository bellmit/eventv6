<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8"/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>晋江-事件信息详情</title>
	
	<!-- 命案防控-->
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/main-shce.css"/>
	<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/common/css/reset.css"/>
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/sweepBlackRemoveEvil.css"/>
	
	<#include "/component/commonFiles-1.1.ftl" />
	<link rel="stylesheet" type="text/css" href="${SQ_GMIS_URL}/css/jj/css/add.css" />
	<link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css" />
	<link href="${rc.getContextPath()}/js/nbspslider-1.0/css/css.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="${rc.getContextPath()}/js/nbspslider-1.0/js/jquery.nbspSlider.1.0.min.js" ></script>
	<script type="text/javascript" src="${rc.getContextPath()}/js/event/event.js"></script>
	<!--插件如语音盒 使用js-->
	<script type="text/javascript" src="${rc.getContextPath()}/js/plugIn/plug_in.js"></script>
	<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
	
	<script>
		<#if eventType=="workStation">
        document.domain = "${domain}";
        var api = frameElement.api, W = api.opener, D = W.document;
		</#if>
		<#if source?? && source = 'workPlatform'>
        document.domain = "${domain}";
		<#elseif source?? && source = 'oldWorkPlatform'>
        //document.domain = "${domain}";
		<#else>
		</#if>
    </script>
	
	<#if isDetail2Edit?? && isDetail2Edit>
		<#include "/component/ComboBox.ftl" />
		<script type="text/javascript" src="${(GEO_URL)}/js/components/geoAddressPlugin/jquery.anole.address.js"></script>
	</#if>
	
	<style type="text/css">
		.fx-spotlist .LinkDotNow {
    		margin-left: -3px !important;
    		width: 14px !important;
    		height: 15px !important;
    	}
    	.fx-spotlist .LinkDot, .LinkDotReject {
    		margin-left: 0px !important;
    		width: 9px !important;
    		height: 9px !important;
		}
		.leftTdWidth{
			width:50%;
		}
		.WindowLeftMenu .list {margin-left: 0; width: 150px;}
		.WindowLeftMenu .list dd a {text-decoration : none;}
		.autoCellWidth{width: 78%;}
		.DetailEdit .LabName{width:75px;}
		.DetailEdit .inpWidth{width: 89%;}
		.EventLabelTitle{
			height:28px;
			padding:0 10px;
			font-family:Microsoft YaHei;
			font-size:16px;
			line-height:28px;
			border-left:2px solid #36c;
			background:#eff2f4;
		}
		.Check_Radio {
   			 margin-top: 5px;
		}
		.BlockBorderTop{border-top: 1px solid #c5d0dc}
		
		
	</style>
		
	<#include "/component/ImageView.ftl" />

</head>

<body onload="checkMetterHeight();">
	<div class="OpenWindow">
		<!--left menu begin-->
		<div class="WindowLeftMenu fl hide" id="leftMenuDiv" style="padding:0;">
			<div class="list">
				<dl>
					<dd id="leftMenuDD">
						<p class="current finish"><a href="#eventBasicInfo">基本信息</a></p>
						<#if careRoads??>
							<p labelTypeName="careRoads" ><a href="#careRoads">涉及线路案(事)件</a></p>
						</#if>
						<#if majorRelatedEvents??>
							<p labelTypeName="majorRelatedEvents"><a href="#majorRelatedEvents">重特大案(事)件</a></p>
						</#if>
						<#if homicideCase??>
							<p labelTypeName="homicideCase" ><a href="#homicideCase">命案防控</a></p>
						</#if>
						<#if disputeMediation??>
							<p labelTypeName="disputeMediation" ><a href="#disputeMediation">矛盾纠纷排查化解</a></p>
						</#if>
						<#if schoolRelatedEvents??>
							<p labelTypeName="schoolRelatedEvents" ><a href="#schoolRelatedEvents">涉及师生(事)件</a></p>
						</#if>
					</dd>
				</dl>
			</div>
		</div>
		<!--left menu end-->
		
		<div class="MC_con content light" style="overflow-x:hidden">
			<!--NorForm begin-->
			<div class="con BaseInfo" id="norFormDiv">
				<form id="flowSaveForm"	action="" method="post" enctype="multipart/form-data">
					<!--right content begin-->
					<div class="WindowRihgtCon fl MetterList"  id="rightContentDiv" style="position: fixed;">
						<div class="MC_con content light" >
							<div id="eventLabelContentIncludeDiv">
								<!--基本信息-->
								<#if isDetail2Edit?? && isDetail2Edit>
									<div class="con BaseInfo NorForm" id="norFormDiv">
										<input type="hidden" name="isDetail2Edit" value="<#if isDetail2Edit??>${isDetail2Edit}</#if>" />
										<#include "/zzgl/event/jinJiangShi/eventLabelContent/addInfo/add_event_basic.ftl" />
									</div>
								<#else>
									<#include "/zzgl/event/jinJiangShi/eventLabelContent/detailInfo/detail_event_handle_basic.ftl" />
								</#if>
								<!--涉及线路案(事)件-->
								<#if careRoads??>
									<#include "/zzgl/event/jinJiangShi/eventLabelContent/detailInfo/detail_event_careRoads.ftl" />
								</#if>
								<!--重特大案(事)件-->
								<#if majorRelatedEvents??>
									<#include "/zzgl/event/jinJiangShi/eventLabelContent/detailInfo/detail_event_majorRelatedEvents.ftl" />
								</#if>
								<!--命案防控-->
								<#if homicideCase??>
									<#include "/zzgl/event/jinJiangShi/eventLabelContent/detailInfo/detail_event_homicideCase.ftl" />
								</#if>
								<!--矛盾纠纷排查化解-->
								<#if disputeMediation??>
									<#include "/zzgl/event/jinJiangShi/eventLabelContent/detailInfo/detail_event_disputeMediation.ftl" />
								</#if>
								<!--涉及师生(事)件-->
								<#if schoolRelatedEvents??>
									<#include "/zzgl/event/jinJiangShi/eventLabelContent/detailInfo/detail_event_schoolRelatedEvents.ftl" />
								</#if>
								<div class="MetterContent" style="margin:0 auto; padding: 0;">
									<#if isHandle?? && isHandle && handleEventPage??>
										<div class="h_20"></div>
										<#include "/zzgl/event/workflow/${handleEventPage}" />
									</#if>
									
									<#if instanceId??>
										<div class="h_20"></div>
										<div class="ConList">
											<div class="nav" id="tab">
												<ul>
													<li id="01_li" class="current">处理环节</li>
													<li id="02_li" class="hide">评价列表</li>
													<li id="03_li" class="hide">督办记录</li>
													<li id="04_li" class="hide">时限申请</li>
													<li id="05_li" class="hide">关联历史事件</li>
												</ul>
											</div>
											
											<div class="ListShow ListShow2">
												<div id="01_li_div" class="t_a_b_s">
													<div id="workflowDetail" border="false"></div>
												</div>
												<div id="02_li_div" class="t_a_b_s hide">
													<div class="tabss">
														<div id="evaResultContent" class="appraise"></div>
													</div>
												</div>
												<div id="03_li_div" class="t_a_b_s hide">
													<div class="tabss">
														<div id="superviseResultContent" class="appraise"></div>
													</div>
												</div>
												<div id="04_li_div" class="t_a_b_s hide">
													<div class="tabss">
														<div id="timeApplicationContent" class="appraise"></div>
													</div>
												</div>
												<div id="05_li_div" class="t_a_b_s hide">
													<div class="tabss">
														<div id="eventAncestorListDiv"></div>
													</div>
												</div>
											</div>
											
										</div>
										<div class="h_20"></div>
									</#if>
					                
								</div>
	                
							</div>
						</div>
					</div>
					<!--right content end-->
				</form>
			</div>
			<!--NorForm end-->
		</div>
	</div>
	
</body>

<script type="text/javascript">
	var $model = "${model!}";
	var downPath = "${downPath}";
	var contextPath = "${rc.getContextPath()}";
	var _winHeight = 0;
	var _winWidth = 0;
	
	$(function(){
		var eventId = $("#eventId").val();
		_winHeight = $(window).height();
		_winWidth = $(window).width();
		
		<#if msgWrong??>
			$.messager.alert("错误", "${msgWrong}", "error");
		</#if>
		
		getImages(eventId, '${EVENT_ATTACHMENT_TYPE!}');
        
        <#if instanceId??>          	
			$("#workflowDetail").panel({
				height:'auto',
				width:'auto',
				overflow:'no',
				href: "${rc.getContextPath()}/zhsq/workflow/workflowController/flowDetail.jhtml?instanceId=${instanceId}",
				onLoad:function(){//配合detail_workflow.ftl使用
					var workflowDetailWidth = $("#workflowDetail").width() - 10 - 10;//10px分别为左右侧距离
					var maxHandlePersonAndTimeWidth = workflowDetailWidth * 0.4;//人员办理意见的最大宽度，为了使人员信息过长时，办理意见不分行
					var taskListSize = $("#taskListSize").val();	//任务记录数
					var handleTaskNameWidth = 115;		//处理环节总宽度
					var handleLinkWidth = 21;			//办理环节宽度
					var handlePersonAndTimeWidth = 0;	//办理人/办理时间宽度
					var handleRemarkWidth = 0;			//办理意见宽度
					
					var remindSize = 0;					//催办记录数
					var remindPersonAndTimeWidth = 0;	//催办人和催办时间宽度
					var remindRemarkWidth = 0;			//催办意见宽度
					
					for(var index = 0; index < taskListSize; index++){
						remindSize = $("#remindListSize_"+index).val();//催办记录数
						remindPersonAndTimeWidth = 0;
						remindRemarkWidth = 0;
						
						handlePersonAndTimeWidth = $("#handlePersonAndTime_"+index).outerWidth();
						
						if(handlePersonAndTimeWidth > maxHandlePersonAndTimeWidth) {
							$("#handlePersonAndTime_"+index).width(maxHandlePersonAndTimeWidth);
							handlePersonAndTimeWidth = $("#handlePersonAndTime_"+index).outerWidth();
						}
						
						handleRemarkWidth = workflowDetailWidth - handleTaskNameWidth - handleLinkWidth - handlePersonAndTimeWidth;
						
						$("#handleRemark_"+index).width(handleRemarkWidth);//办理意见宽度
						
						for(var index_ = 0; index_ < remindSize; index_++){
							remindPersonAndTimeWidth = $("#remindPersonAndTime_"+index+"_"+index_).outerWidth();
							remindRemarkWidth = workflowDetailWidth - handleTaskNameWidth - handleLinkWidth - remindPersonAndTimeWidth;
							$("#remindRemark_"+index+"_"+index_).width(remindRemarkWidth);//催办意见宽度
						}
					}
					
					adjustSubTaskWidth();//调整子任务(会签任务和处理中任务)办理意见宽度
				}
			});
			
			<#if isHandle?? && isHandle && handleEventPage??>
	        	//增添任务接收时间
	        	$.ajax({
					type: "POST",
		    		url : '${rc.getContextPath()}/zhsq/event/eventDisposalController/receiveTask.jhtml',
					data: 'eventId='+eventId+'&instanceId=${instanceId}'+'&taskId=${taskId}',
					dataType:"json",
					success: function(data){
					},
					error:function(data){
						$.messager.alert("错误", "任务接收失败！", "error");
					}
		    	});
	        </#if>
		</#if>
		
		var options = { 
            axis : "yx", 
            theme : "minimal-dark" 
        };
        
        var leftMenuDivWidth = 0;
        
        leftMenuDivWidth=initLabelLeft(leftMenuDivWidth);
        
        $('#leftMenuDiv').height(_winHeight);
		$('#rightContentDiv').height(_winHeight)
										.width(_winWidth - leftMenuDivWidth)
										.css('left', leftMenuDivWidth);
		//310为图片展示预留的宽度 20为左右内容各自的左右边距
		$("#contentDiv").width($('#rightContentDiv').outerWidth(true) - 310 - 20 - 20);
		
		$('#workflowDetail').width($('#rightContentDiv').width());
		
	    enableScrollBar('MetterBrief',options);
        enableScrollBar('MetterMore',options);
        
        //自动调整需要自适应宽度的组件，由于受滚动条组件的影响，会加载多次，从而添加isSettledAutoWidth用于防止多次初始化
		$("#flowSaveForm .autoDoubleCell").not(".isSettledAutoWidth2").each(function() {
			$(this).width((_winWidth - $(this).siblings(".LabName").eq(0).outerWidth(true) - leftMenuDivWidth) * 0.95)
				   .addClass("isSettledAutoWidth2");
		});
		
        fetchEventExtraAttr();
        
        <#if isDetail2Edit?? && isDetail2Edit>
        	initItem4Basic();
        	$("#eventLabelContentIncludeDiv .autoCellWidth:visible").not(".isSettledAutoWidth").each(function() {
        		//4像素为偏移量，可能会随着样式变更等进行调整
        		$(this).width($('#contactUser').parent().parent().find('td').eq(0).outerWidth(true) + $('#contactUser').outerWidth(true) + 4)
        		         .addClass("isSettledAutoWidth");
        	});
        </#if>
        
        <#if homicideCase??>
        	initItem4HomicideCase();
        </#if>
		<#if disputeMediation??>
			initItem4DisputeMediation();
		</#if>
	});
	
	$(document).ready(function() {
		var lis = $("#tab").find("li");
		lis.each(function() {
			$(this).bind("click", function() {
				if (typeof $(this).attr("id") != "undefined") {
					lis.each(function() {
						$(this).removeClass("current");
					});
					$(this).addClass("current");
					var li_id = $(this).attr("id");
					$(".t_a_b_s").each(function() {
						var obj = $(this);
						if (obj.attr("id") == li_id + "_div") {
							var iframeItemList = $("#" + li_id + "_div iframe"),
								iframeLen = iframeItemList.length;
							
							if(iframeLen == 1) {
								var iframeItem = iframeItemList.eq(0),
									iframeLoaded = iframeItem.attr("_loadflag");
								
								if(isBlankStringTrim(iframeLoaded)) {
									iframeItem.attr("_loadflag", true);//用于防止重复加载
									iframeItem.attr('src', iframeItem.attr('iframeSrc'));//为了调整因页签切换而导致的列宽不足
								}
							}
							
							$(this).removeClass("hide");
						} else {
							$(this).addClass("hide");
						}
					});
				}
			});
		});
	});
	
	function checkMetterHeight() {//需要在页面渲染完成后，执行，因为"MetterMore > table"的高度会因为折行效果发生变化
		var moreTableDefault = 210;
		var moreTableHeight = $("#MetterMore table").height();//由于添加的滚动条，因此table不再是MetterMore的直接下级
		var briefContentHeight = $("#MetterBrief ul > li").height();
		var briefHeight = 0;
		var dubanIcon = $(".dubanIcon").length;
		
		if(moreTableHeight > moreTableDefault) {
			moreTableHeight = moreTableDefault;
		}
		
		briefHeight = $("#contentDiv").height() - moreTableHeight - 1 - 1;//1为底部界限边距，共两条
		
		if(briefContentHeight < briefHeight && dubanIcon > 0) {//防止内容不足时，导致督办图标显示不完全
			$("#MetterBrief ul > li").height(briefHeight);
		}
		
		$("#MetterMore").height(moreTableHeight);
		$("#MetterBrief").height(briefHeight);
		
		//显示设置width样式，为了防止菜单页签切换时，宽度被修改
		$("#MetterBrief ul").width($("#MetterBrief").width());
		$("#MetterMore table").width($("#MetterMore").width());
	}
	
	function checkWorkFlow(){
		$.ajax({
			type: "POST",
    		url : '${rc.getContextPath()}/zhsq/event/eventDisposalController/checkWorkFlow.jhtml',
			data: 'taskId=${taskId}',
			dataType:"json",
			success: function(data){
				if(data){
				}else{
					$("#workflowFrame").remove();
					$.messager.alert("提示","该事件已经办理！","info");
				}
			},
			error:function(data){
				alert(data);
			}
    	});
	}
	
	function flashData() {
		parent.winType = '0';
		
		try{
			parent.flashData();
		}catch(e){
			<#if eventType??&&eventType=="workStation">
				api.close();
			</#if>
		}
	}
	
	function showDetail(){
		<#if isDomain??>
			var isDomain = "${isDomain}";
		</#if>
		var url;
		<#if bizDetailUrl??>
			url = '${bizDetailUrl}';
			if(isDomain){
				url += "&isDomain=" + isDomain;
			}
		</#if>
		<#if source?? && source = 'workPlatform'>
			url += "&source=${source}";
			parent.parent.top.topDialog.openDialog("查看详情", 400, 900, url)
		<#elseif source?? && source = 'oldWorkPlatform'>
			url += "&source=${source}";
			//parent.showEventContent(url,'查看详情');
			showMaxJqueryWindow("查看详情", url,880,400, false, 'no');
		<#else>
			parent.showMaxJqueryWindow("查看详情", url,900,400, false, 'no');
		</#if>
		
	}
	
	function showMap(){
		var callBackUrl = '${SQ_ZHSQ_EVENT_URL}/zhsq/map/arcgis/arcgis/toArcgisCrossDomain.jhtml';
		var width = 480;
		var height = 360;
		var gridId = $("#gridId").val();
		var markerOperation = $('#markerOperation').val();
		var id = $('#eventId').val();
		var mapType = 'EVENT_V1';
		var isEdit = false;
		
		<#if isDetail2Edit?? && isDetail2Edit>
			isEdit = true;
		</#if>
		
		showMapWindow(gridId,callBackUrl,width,height,isEdit,mapType);
	}
	
	function showMix(fieldId, index){
		<#if !(isOpenInNewWindows?? && isOpenInNewWindows)>
			ffcs_viewImg_win(fieldId, index);
		<#else>
			ffcs_viewImg(fieldId, index);
		</#if>
	}
	
	function openPostWindow(url, data, titles){
		var tempForm = document.createElement("form");
		tempForm.id="tempForm1";
		tempForm.method="post";
		tempForm.action=url;
		tempForm.target="图片查看";
		var hideInput = document.createElement("input");
		hideInput.type="hidden";
		hideInput.name= "paths";
		hideInput.value= data;
		tempForm.appendChild(hideInput);
		var hideInput = document.createElement("input");
		hideInput.type="hidden";
		hideInput.name= "titles";
		hideInput.value= titles;
		tempForm.appendChild(hideInput);
		tempForm.submit(function(){
			openWindow("图片查看");
		});
// 		tempForm.attachEvent("onsubmit",function(){
// 			openWindow(name);
// 		});
		document.body.appendChild(tempForm);
//		tempForm.fireEvent("onsubmit");
		tempForm.submit();
		document.body.removeChild(tempForm);
	}

	function openWindow(name){
		window.open('about:blank',name,'height=400, width=400, top=0, left=0, toolbar=yes, menubar=yes, scrollbars=yes, resizable=yes,location=yes, status=yes');
	}

	function fetchEventExtraAttr() {
		$.ajax({
			type: "POST",
			url : '${rc.getContextPath()}/zhsq/event/eventDisposalController/fetchEventExtraAttr.jhtml',
			data: {'eventId': $("#eventId").val()},
			dataType:"json",
			success: function(data){
				var evaResultList = data.evaResultList,
					superviseResultList = data.superviseResultList,
					timeAppList = data.timeAppList,
					ancestorCount = data.ancestorCount;
				
				if(evaResultList && evaResultList.length > 0) {
					var evaResultContent = "",
						evaItem = null;
					$("#02_li").show();
					
					for(var index in evaResultList) {
						evaItem = evaResultList[index];
						
						evaResultContent += '<div class="list">';
                        evaResultContent += '	<span>';
                        evaResultContent += '		<p>';
                        
                        if(evaItem.creatorName) {
                        	evaResultContent += '		<em class="FontDarkBlue">' + evaItem.creatorName + '</em>';
                        }
                        if(evaItem.createDateStr) {
                        	evaResultContent += '		于 ' + evaItem.createDateStr;
                        }
                        if(evaItem.evaLevelName) {
                        	evaResultContent += '		评价 <b class="FontRed">' + evaItem.evaLevelName + '</b>';
                        }
                        evaResultContent += '		</p>';
                        if(evaItem.evaContent) {
                        	evaResultContent += '	<p>' + evaItem.evaContent + '</p>';
                        }
                        evaResultContent += '	 </span>';
                        evaResultContent += '</div>';
					}
					
					$("#evaResultContent").html(evaResultContent);
				}
				
				if(superviseResultList && superviseResultList.length > 0) {
					var superviseResultContent = "",
						supervise = null;
					$("#03_li").show();
					$("#dubanIconDiv").show();
					
					for(var index in superviseResultList) {
						supervise = superviseResultList[index];
						
						superviseResultContent += '<div class="list">';
                        superviseResultContent += '    <span>';
                        superviseResultContent += '        <p>';
                        if(supervise.remindUserName) {
                        	superviseResultContent += '			<em class="FontDarkBlue">' + supervise.remindUserName + '</em>';
                        }
                        if(supervise.remindDateStr) {
                        	superviseResultContent += '			于 ' + supervise.remindDateStr;
                        }
                        superviseResultContent += '				<b class="FontRed">督办</b>';
                        superviseResultContent += '			</p>';
                        if(supervise.remarks) {
                        	superviseResultContent += '			' + supervise.remarks;
                        }
                        superviseResultContent += '    </span>';
                        superviseResultContent += '</div>';
					}
					
					$("#superviseResultContent").html(superviseResultContent);
				}
				
				if(timeAppList && timeAppList.length > 0) {
					var timeAppContent = "",
						timeApp = null,
						checkAdvice = null,
						auditorName = null,
						reason = null;
					
					$("#04_li").show();
					
					for(var index in timeAppList) {
						timeApp = timeAppList[index];
						checkAdvice = timeApp.checkAdvice || '';
						auditorName = timeApp.auditorName || '';
						reason = timeApp.reason || '';
						
						timeAppContent += '<div class="list">';
						timeAppContent += '    <span>';
						timeAppContent += '        <p>';
						timeAppContent += '				该事件于 <em class="FontDarkBlue">' + timeApp.createTimeStr + '</em>';
						timeAppContent += '   			增添时限 <b class="FontRed">' + timeApp.intervalName + '</b><b>【' + timeApp.applicationTypeName + '】</b>';
						timeAppContent += '        </p>';
						
						if(reason) {
							timeAppContent += 		   '<b>申请原因：</b>' + reason;
						}
						
						timeAppContent += '        <p>';
						timeAppContent += '				<em class="FontDarkBlue">' + auditorName + '(' + timeApp.auditorOrgName + ')' + '</em>';
						timeAppContent += '				<b class="FontRed">'+ timeApp.timeAppCheckStatusName +'</b>';
						timeAppContent += '        </p>';
						
						if(checkAdvice) {
							timeAppContent += 		   '<b>审核意见：</b>' + checkAdvice;
						}
						
						timeAppContent += '    </span>';
						timeAppContent += '</div>';
					}
					
					$("#timeApplicationContent").html(timeAppContent);
				}
				
				if(ancestorCount > 0) {
					$("#05_li").show();
					
					var url = "${rc.getContextPath()}/zhsq/event/eventDisposalController/toListEventAncestor.jhtml?eventId=" + $("#eventId").val();
					
					$("#eventAncestorListDiv").append('<iframe id="eventAncesorIframe" iframeSrc="'+ url +'" scrolling="no" frameborder="0" style="width:100%; height:100%;" />');
					
					$("#eventAncestorListDiv > iframe").width($("#workflowDetail").width());
					$("#eventAncestorListDiv").height($("#workflowDetail").height());
				}
			},
			error:function(data){
				$.messager.alert("错误", "获取事件额外信息失败！", "error");
			}
		});
	}
	
	$(window).resize(function(){
		var winHeight = $(window).height();
		var winWidth = $(window).width();
		
		if(winHeight != _winHeight || winWidth != _winWidth) {
			location.reload();
		}
    });
</script>
<#include "/zzgl/event/jinJiangShi/eventLabelJs/detail_event_js.ftl" />
<#if isDetail2Edit?? && isDetail2Edit>
	<#include "/zzgl/event/jinJiangShi/eventLabelJs/add_event_js.ftl" />
</#if>
<#if homicideCase??>
	<#include "/zzgl/event/jinJiangShi/eventLabelJs/detail_homicideCase_js.ftl" />
</#if>
<#if disputeMediation??>
	<#include "/zzgl/event/jinJiangShi/eventLabelJs/detail_disputeMediation_js.ftl" />
</#if>

</html>

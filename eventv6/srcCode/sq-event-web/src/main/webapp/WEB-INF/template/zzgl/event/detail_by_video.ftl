<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><@block name="pageTitle">事件信息详情</@block></title>
<#include "/component/commonFiles-1.1.ftl" />
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

<style type="text/css">
	.DetailEdit .LabName{width:75px;}
	.DetailEdit .inpWidth{width: 89%;}
	.BigTool{ position:fixed; left:0px; bottom:0px; width:100%; z-index:9999;}
 
	
</style>
	
<#include "/component/ImageView.ftl" />
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
		
		//390为：为图片展示预留的宽度；
		var width = _winWidth - 390;
		var options = { 
            axis : "yx", 
            theme : "minimal-dark" 
        };
        
        $("#contentDiv").width(width);
        
	    enableScrollBar('MetterBrief',options);
        enableScrollBar('MetterMore',options);
        
        fetchEventExtraAttr();
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
				} else {
					showWorkFlowPic();
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
	
	<#if instanceId??>		
		function showWorkFlowPic() {
			var url = "${workflowCtx}/flowDetail.shtml?method=flowChart&instanceId=${instanceId}&taskId=${taskId}";
			try{
				parent.showMaxJqueryWindow("流程图窗口", url, null, null, null, "auto");
			}catch(e){
				showMaxJqueryWindow("流程图窗口", url, null, null, null, "auto");
			}
		}
	</#if>
</script>
</head>

<body onload="checkMetterHeight();">
	<form id="flowSaveForm"	action="" method="post" enctype="multipart/form-data">
		<input type="hidden" id="eventId" name="eventId" value="<#if event.eventId??>${event.eventId?c}</#if>" />
		<input type="hidden" name="isDetail2Edit" value="<#if isDetail2Edit??>${isDetail2Edit}</#if>" />
		<!--防止以下属性被设置为默认值，故而需要携带-->
		<input type="hidden" name="happenTimeStr" value="${event.happenTimeStr!}" />
		<input type="hidden" name="urgencyDegree" value="${event.urgencyDegree!}" />
		<input type="hidden" name="eventInvolvedPeople" value="${event.eventInvolvedPeople!}" />
		
		<!--用于地图-->
		<input type="hidden" id="id" name="id" value="<#if event.eventId??>${event.eventId?c}</#if>" /> 
		<input type="hidden" id="markerOperation" name="markerOperation" value="<#if markerOperation??>${markerOperation}</#if>"/>
		<input type="hidden" id="gridId" name="gridId" value="<#if event.gridId??>${event.gridId?c}</#if>">
		
	    <div class="MetterList" style="margin:0 auto;background-color:#F9F9F9;height:100%">
	    	<div id="content-d" class="MC_con content light" style="position:relative;left:0;top:0;overflow-x:hidden;overflow-y:auto"><!--使得事件简介能随着滚动条移动，隐藏横向滚动条-->
	        	<div style="margin:0 auto;">
						<h3 style="margin-top: 10px;margin-left: 10px">事件流转</h3>
	                	<div class="h_20"></div>
	                	<#include "/zzgl/event/workflow/handle_event_video.ftl" />
	            </div>
	        </div>
	    </div>
	</form>
<#include "/component/maxJqueryEasyUIWin.ftl" />
<script type="text/javascript">
	
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
	
	
	function showMix(fieldId, index){
		<#if !(isOpenInNewWindows?? && isOpenInNewWindows)>
			ffcs_viewImg_win(fieldId, index);
		<#else>
			ffcs_viewImg(fieldId, index);
		</#if>
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
</body>
</html>

<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title><@block name="eventDetailPageTitle">事件信息详情</@block></title>
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
	<#include "/component/standard_common_files-1.1.ftl" />
	<script type="text/javascript" src="${GIS_DOMAIN}/js/gis/base/mapMarker.js"></script>
<#--	<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />-->
	<script type="text/javascript" src="${COMPONENTS_URL}/js/fastreply/fastReply.js" defer="true"></script>
	<@block name="extraJs"></@block>
	<#include "/component/bigFileUpload.ftl" />
	
	<style>
		.LabName span{padding-right: 5px;}
	</style>
	<script>
		<#if source?? && source = 'workPlatform'>
			document.domain = "${domain}";
		<#elseif source?? && source = 'oldWorkPlatform'>
			document.domain = "${domain}";
		<#else>
		</#if>
		
	</script>
</head>
<body>
	<input type="hidden" id="eventId" name="eventId" value="<#if event.eventId??>${event.eventId?c}</#if>" />
		
	<!--用于地图-->
	<input type="hidden" id="id" name="id" value="<#if event.eventId??>${event.eventId?c}</#if>" /> 
	<input type="hidden" id="markerOperation" name="markerOperation" value="<#if markerOperation??>${markerOperation}</#if>"/>	
	
    <div class="MetterList" style="margin:0 auto; position:relative;">
    	<div id="operateMask" class="MarskLayDiv hide"></div>
    	<div id="adviceDiv" class="clear PopDiv NorForm hide" style="width: 604px;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td colspan="2" class="LeftTd" style="padding-left: 10px;">
						<span><label class="Asterik">*</label>事件结案请填写办理意见：</span>
					</td>
				</tr>
				<tr>
		    		<td colspan="2" class="LeftTd" style="padding-left: 10px;">
		    			<textarea rows="3" style="width:578px;height:80px;" id="advice" name="advice" class="area1 easyui-validatebox fast-reply" data-options="tipPosition:'top',validType:['maxLength[2048]','characterCheck']"></textarea>
		    		</td>
		    	</tr>
		    	<tr>
		    		<td>
		    			<a href="###" class="BigNorToolBtn BigJieAnBtn" style="float:right;" onclick="startWorkFlow(1);">结案</a>
		    		</td>
		    		<td>
						<a href="###" onclick="closeAdviceDiv();" class="BigNorToolBtn CancelBtn">取消</a>
					</td>
		    	</tr>
		    </table>
		</div>
    	<div id="content-d" class="box content light" style="height:433px;">
        	<div id="metterContentDiv" class="MetterContent">
                <div class="ConList">
                    <div class="ListShow ListShow2" style="border-top:1px solid #c5d0dc;">
                    	<div class="NorForm">
	                        <div id="01_li_div" class="t_a_b_s">
	                        	<table id="formInfo" width="100%" border="0" cellspacing="0" cellpadding="0">
	                              <tr>
	                                <td class="LeftTd" style="width:30%"><label class="LabName"><span>事件编号：</span></label><div class="Check_Radio FontDarkBlue"><#if event.code??>${event.code}</#if></div></td>
	                                <@block name="eventTitleInput">
	                                <td colspan="3"><label class="LabName"><span><label class="Asterik">*</label>事件标题：</span></label><div class="Check_Radio FontDarkBlue" style="width:81%"><#if event.eventName??>${event.eventName}</#if></div></td>
								    </@block>
	                              </tr>
	                              <tr>
								    <@block name="gridPathTd">
	                                <td class="LeftTd"><label class="LabName"><span>所属网格：</span></label><div class="Check_Radio FontDarkBlue" style="width:62%"><#if event.gridPath??>${event.gridPath}</#if></div></td>
								    </@block>
	                                <@block name="contactUserTd">
	                                <td><label class="LabName"><span id="contactUserLabelSpan">联系人员：</span></label><div class="Check_Radio FontDarkBlue"><#if event.contactUser??>${event.contactUser}</#if></div></td>
	                                </@block>
	                                <@block name="contactUserTelTd">
	                                <td><label class="LabName"><span id="contactTelLabelSpan">联系电话：</span></label><div class="Check_Radio FontDarkBlue"><#if event.tel??>${event.tel}</#if></div></td>
	                                </@block>
	                              </tr>
	                              <tr>
	                                <td class="LeftTd"><label class="LabName"><span>信息来源：</span></label><div class="Check_Radio FontDarkBlue"><#if event.sourceName??>${event.sourceName}</#if></div></td>
	                                <td><label class="LabName"><span>采集渠道：</span></label><div class="Check_Radio FontDarkBlue"><#if event.collectWayName??>${event.collectWayName}</#if></div></td>
	                                <td><label class="LabName"><span>影响范围：</span></label><div class="Check_Radio FontDarkBlue"><#if event.influenceDegreeName??>${event.influenceDegreeName}</#if></div></td>
	                              </tr>
	                              <tr>
	                                <td class="LeftTd"><label class="LabName"><span>紧急程度：</span></label>
	                                	<#if event.urgencyDegree?? &&event.urgencyDegree!='01'>
											<div class="Check_Radio FontRed"><span style="color:red"><#if event.urgencyDegreeName??>${event.urgencyDegreeName}</#if></span>
										<#elseif event.urgencyDegreeName??>
											<div class="Check_Radio FontDarkBlue">${event.urgencyDegreeName}
										</#if>
										</div>
									</td>
									<td style="width: 40%;"><label class="LabName"><span><label class="Asterik">*</label>事件类型：</span></label><div class="Check_Radio FontDarkBlue" style="width: 70%;"><#if event.eventClass??>${event.eventClass}</#if></div></td>
	                              	<td><label class="LabName"><span>采集时间：</span></label><div class="Check_Radio FontDarkBlue"><#if event.createTimeStr??>${event.createTimeStr}</#if></div></td>
	                              </tr>
	                              <@block name="singleLineExtraInfoTr"></@block>
	                              <@block name="occurInputBlock">
	                              <tr>
	                                <td colspan="3" class="LeftTd">
										<label class="LabName">
											<span><label class="Asterik">*</label>事发详址：</span>
										</label>
										<div class="Check_Radio FontDarkBlue" style="width:87%"><#if event.occurred??>${event.occurred}</#if></div></td>
	                              </tr>
	                              </@block>
	                              <@block name="geographicalLabelingInput">
									  <tr class="DotLine">
										  <td colspan="3" class="LeftTd">
											  <label class="LabName"><span><label id="mapAsterik" class="Asterik hide">*</label>地理位置：</span></label>
											  <div id="map" style="display:inline"></div>
										  </td>
									  </tr>
<#--	                              <tr class="DotLine">-->
<#--			                        <td colspan="3" class="LeftTd"><label class="LabName"><span>地理位置：</span></label>-->
<#--			                        	<#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/>-->
<#--						    			<input type="hidden" id="gridId" name="gridId" value="<#if event.gridId??>${event.gridId?c}</#if>">-->
<#--									</td>-->
<#--			                      </tr>-->
								  </@block>
	                              <tr>
	                                <td colspan="3" class="LeftTd"><label class="LabName"><span><label class="Asterik">*</label>事件描述：</span></label><div class="Check_Radio FontDarkBlue" style="width:87%"><#if event.content??>${event.content}</#if></div></td>
	                              </tr>
	                              <tr>
	                              	<td colspan="3" class="LeftTd">
	                              		<label class="LabName"><span>涉及人员：</span></label>
		                              	<div class="Check_Radio FontDarkBlue">
		                              		<#if event.involvedNumName??>(<b>${event.involvedNumName}</b>)</#if><#if event.involvedPersion??>${event.involvedPersion}</#if>
		                              	</div>
	                              	</td>
	                              </tr>
	                              <tr id="bigFileUploadTr">
	                              	<td colspan="3" class="LeftTd">
	                              		<label class="LabName"><span><label id="bigFileUploadAsterik" class="Asterik hide">*</label>图片上传：</span></label><div id="bigFileUploadDiv"></div>
	                              	</td>
	                              </tr>
	                            </table>
	                      </div>
	                      <#if bizDetailUrl??>
			                <div class="WebNotice">
								<p>此事件关联了${eventReportBizTypeName!}业务单信息，<a href="###" onclick="showDetail()">点击查看</a></p>
							</div>
						 </#if>
                      </div>
                    </div>
                </div>
            </div>
        </div>
		
		<div class="BigTool">
        	<div class="BtnList">
        		<a href="###" class="BigNorToolBtn BigShangBaoBtn" onclick="startWorkFlow(0);">提交</a>
        		<a href="###" id="archiveButton" class="BigNorToolBtn BigJieAnBtn" onclick="startWorkFlow(1);">结案</a>
        		<a href="###" class="BigNorToolBtn CancelBtn" onclick="cancel();">关闭</a>
            </div>
        </div>
    </div>
<#include "/component/maxJqueryEasyUIWin.ftl" />
<script type="text/javascript">
	var _winHeight = 0;
	var _winWidth = 0;
	var $model = "${model}";
	var downPath = "${downPath}";
	var contextPath = "${rc.getContextPath()}";
	
	$(function(){
		var eventId = "${event.eventId?c}";
		var h = 0;
		if ($(".BigTool").length != 0) {
			h = $(".BigTool").height();
		}
		setInterval(function(){
			var height = $(window).height();
			$(".MetterList .box").css("height", height- 20 - h);
		}, 3);
		
		_winHeight = $(window).height();
		_winWidth = $(window).width();
		
		var bigFileUploadOpt = {
        	useType: 'view',
        	fileExt: '.jpg,.gif,.png,.jpeg,.webp',
        	attachmentData: {bizId: $('#eventId').val(), attachmentType:'${EVENT_ATTACHMENT_TYPE!}', eventSeq: '1,2,3'},
        	individualOpt : {
        		isUploadHandlingPic : <#if isUploadHandlingPic??>${isUploadHandlingPic?c}<#else>false</#if>
        	}
        };
        
        <@block name="bigFileUploadInitOption"></@block>

        bigFileUpload_initFileUploadDiv('bigFileUploadDiv', bigFileUploadOpt);

        /*地图控件初始化值判断*/
		<@block name="mapMarkerDiv"></@block>
		
		<@block name="initExpandScript"></@block>
		
		var options = {
            axis : "yx", 
            theme : "minimal-dark" 
        }; 
        enableScrollBar('content-d',options); 
        
        $("#metterContentDiv").width($("#metterContentDiv").width());//显示设置width样式，为了防止菜单页签切换时，宽度被修改
	});
	
	function startWorkFlow(toClose){
		var advice = $("#advice").val(),
			  isAdviceVisible = $("#adviceDiv").is(":visible");
		
		<@block name="attachmentCheck"></@block>
		
		if(isAdviceVisible && advice=="") {
			$.messager.alert('警告','请填写办理意见！','warning');
		} else if(toClose=="1" && !isAdviceVisible) {
			openAdviceDiv();
		} else {
			modleopen();
			
			$.ajax({
				type: "POST",
	    		url : '${rc.getContextPath()}/zhsq/event/eventDisposalController/startWorkFlow.jhtml',
				data: 'eventId=${event.eventId?c}'+'&toClose='+toClose+'&advice='+encodeURIComponent($("#advice").val()),
				dataType:"json",
				success: function(data){
					parent.startWorkFlow(data, '0');
				},
				error:function(data){
					$.messager.alert('错误','连接错误！','error');
				}
	    	});
    	}
	}
	
	function flashData() {
		parent.closeMaxJqueryWindow();
		parent.flashData();
	}
	
	function cancel(){
		parent.closeMaxJqueryWindow();
	}
	
	function openAdviceDiv() {
		$('.PopDiv').css({'top':_winHeight/4});
		$("#operateMask").show();
		$("#adviceDiv").show();
		$("#advice").focus();
	}
	
	function closeAdviceDiv() {
		$("#adviceDiv").hide();
		$("#operateMask").hide();
		$("#advice").val("");
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
			parent.parent.top.topDialog.openDialog("查看事件信息", 400, 900, url)
		<#elseif source?? && source = 'oldWorkPlatform'>
			url += "&source=${source}";
			//parent.showEventContent(url,'查看详情');
			showMaxJqueryWindow("查看详情", url,880,400,'no');
		<#else>
			parent.showMaxJqueryWindow("查看详情", url,900,400,'no');
		</#if>
		
	}

	var mapMarker= new MapMarker({
		el:"map",//div挂载点
		context:"${GIS_DOMAIN}",//gis域名
		data:{ //业务数据
			id : "${event.eventId!}",//业务标识
			markerType :'0301',//模块类型
			markerOperation :2,//地图操作类型 0和1为添加修改标注，2为查看标注
			gridId : $("#gridId").val(),//网格标识，用于打开地图初始的默认位置
			<#if event.resMarker??>
				<#if event.resMarker.x?? && event.resMarker.y??>
					initPosType:1,
					initPosVal:{x:'${event.resMarker.x!0}',y:'${event.resMarker.y!0}',z:'${event.resMarker.z!0}'},
				</#if>
			</#if>
			//type:undefined,//用于部分表主键改为uuuId的类型，如果不是uuuId的类型，选择不传
			width:480,//弹框宽度，可以不传，默认480px
			height:360,//弹框高度，可以不传，默认360px
		},
		done:function (data) {//弹框确认回调，已回填了xyz到页面元素

		}
	});
	/*	function showMap(){
            var callBackUrl = '${SQ_ZHSQ_EVENT_URL}/zhsq/map/arcgis/arcgis/toArcgisCrossDomain.jhtml';
		var width = 480;
		var height = 360;
		var gridId = $("#gridId").val();
		var markerOperation = $('#markerOperation').val();
		var id = $('#eventId').val();
		var mapType = 'EVENT_V1';
		var isEdit = false;
		showMapWindow(gridId,callBackUrl,width,height,isEdit,mapType);
	}
	*/
	$(window).resize(function(){
		var winHeight = $(window).height();
		var winWidth = $(window).width();
		
		if(winHeight != _winHeight || winWidth != _winWidth) {
			location.reload();
		}
    });
</script>
<@block name="eventExtraJs"></@block>
<iframe id="crossOverIframe" name="crossOverIframe" src="" style="display:none;" >  </iframe>
</body>
</html>

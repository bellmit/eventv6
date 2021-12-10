<!DOCTYPE html>
<html>
<head>
<meta http-equiv="x-ua-compatible" content="ie=8" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><@block name="eventMenuPageTitle">事件采集</@block></title>
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
<#include "/component/standard_common_files-1.1.ftl" />
<script type="text/javascript" src="${rc.getContextPath()}/js/plugIn/plug_in.js"></script>
<script type="text/javascript" src="${SQ_ZZGRID_URL}/theme/scim/scripts/jq/plugins/json/json2.js"></script>
<script type="text/javascript" src="${COMPONENTS_URL}/js/fastreply/fastReply.js" defer="true"></script>
<@block name="extraJs"></@block>
<script type="text/javascript" src="${GIS_DOMAIN}/js/gis/base/mapMarker.js"></script>
<#--<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />-->
<#include "/component/bigFileUpload.ftl" />

<style type="text/css">
	.LabName{height:30px;}
</style>
<#include "/component/ComboBox.ftl" />
</head>
<body>
<div>
	<form id="tableForm" name="tableForm" action="" method="post" enctype="multipart/form-data">
		<input type="hidden" id="gridId" name="gridId" value="<#if event.gridId??>${event.gridId?c}</#if>">
		<input type="hidden" id="gridCode" name="gridCode" value="${event.gridCode!}">
		<input type="hidden" id="type" name="type" value="${event.type!}" />
		<input type="hidden" id="code" name="code" value="${event.code!}" />
		<input type="hidden" id="eventId" name="eventId" value="<#if event.eventId??>${event.eventId?c}</#if>" />
		<!--办理意见-->
		<input type="hidden" id="result" name="result" value="${event.result!}" />
		<!--事件对接业务平台-->
		<input type="hidden" name="bizPlatform" value="${event.bizPlatform!}" />
		
		<!--用于地图-->
		<input type="hidden" id="id" name="id" value="<#if event.eventId??>${event.eventId?c}</#if>" /> 
		<input type="hidden" id="markerOperation" name="markerOperation" value="<#if markerOperation??>${markerOperation}</#if>"/>
		<!--事件上报关联模块-->
		<input type="hidden" name="eventReportRecordInfo.bizId" value="<#if event.eventReportRecordInfo?? && event.eventReportRecordInfo.bizId??>${event.eventReportRecordInfo.bizId?c}</#if>" />
		<input type="hidden" name="eventReportRecordInfo.bizType" value="<#if event.eventReportRecordInfo??>${event.eventReportRecordInfo.bizType!}</#if>" />
		
		<!--外部传入的办理意见-->
		<input type="hidden" id="_outerAdvice" value="${advice!}" />
		
		<input type="hidden" name="redisKey" value="${redisKey!}" />
		
		<div id="wholePageDiv" style="margin: 0 auto; background-color:#F9F9F9; position:relative;">
			<@block name="eventAddExtendContent"></@block>
			
			<div id="content-d" class="MC_con content light" style="overflow-x:hidden">
				<div id="norFormDiv" class="NorForm" style="width:784px;">
					<div class="fl" style="width:67%;">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td class="LeftTd">
									<label class="LabName"><span>事件分类：</span></label><input type="text" class="inp1 InpDisable easyui-validatebox" style="width:155px;" data-options="required:true" id="typeName" name="typeName" maxlength="100" value="<#if event.typeName??>${event.typeName}<#else><#if event.eventClass??>${event.eventClass}</#if></#if>" />
								</td>
								<@block name="gridNameTd">
								<td>
									<label class="LabName"><span>所属网格：</span></label><input type="text" class="inp1 InpDisable easyui-validatebox" style="width:122px;" data-options="required:true" id="gridName" name="gridName" value="${event.gridName!}" />
								</td>
								</@block>
							</tr>
					    	<tr>
					    		<td colspan="2" class="LeftTd">
					    			<label class="LabName"><span><label class="Asterik">*</label>事件标题：</span></label><input type="text" class="inp1 easyui-validatebox" style="width:405px;" data-options="required:true,tipPosition:'bottom',validType:['maxLength[100]','characterCheck']" name="eventName" id="eventName" value="${event.eventName!}" />
					    		</td>
					    	</tr>
					    	<@block name="happenTimeInput">
					    	<tr>
				    		    <td colspan="2" class="LeftTd">
				    			    <label class="LabName"><span><label class="Asterik">*</label>事发时间：</span></label><input type="text" id="happenTimeStr" name="happenTimeStr" class="inp1 Wdate easyui-validatebox" style="width:170px; cursor:pointer;" data-options="required:true" onclick="WdatePicker({readOnly:true, maxDate:'${(event.happenTimeStr!maxHappenTime)?substring(0,10)} 23:59:59', dateFmt:'yyyy-MM-dd HH:mm:ss', isShowClear:false, isShowToday:false})" value="${event.happenTimeStr!}" readonly="readonly"></input>
				    		    </td>
					    	</tr>
				    		</@block>
							<@block name="eventOccurredInput">
					    	<tr>
					    		<td colspan="2" class="LeftTd">
					    			<label class="LabName"><span><label class="Asterik">*</label>事发详址：</span></label><input type="text" class="inp1 easyui-validatebox" style="width:405px;" data-options="required:true,tipPosition:'bottom',validType:['maxLength[255]','characterCheck']" name="occurred" id="occurred" value="${event.occurred!}" />
					    		</td>
					    	</tr>
							</@block>
					    	<@block name="geographicalLabelingInput">
<#--					    	<tr>-->
<#--				    		    <td colspan="2" class="LeftTd">-->
<#--				    			    <label class="LabName"><span>地理标注：</span></label>-->
<#--				    			    <#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/>-->
<#--				        	    </td>-->
<#--					        </tr>-->
								<tr>
									<td colspan="2" class="LeftTd">
										<label class="LabName"><span><label id="mapAsterik" class="Asterik hide">*</label>地理标注：</span></label>
										<div id="map" style="display:inline"></div>
									</td>
								</tr>
				        	</@block>
					    	<tr>
					    		<td colspan="2" class="LeftTd" style="border-bottom:none;">
					    			<label class="LabName"><span><label class="Asterik">*</label>事件描述：</span></label><textarea name="content" id="content" cols="" rows="" class="area1 easyui-validatebox" style="width:400px; height:64px;resize: none;" data-options="required:true,tipPosition:'bottom',validType:[<#if characterLimit??>'minLength[${characterLimit?c}]',</#if>'maxLength[1024]','characterCheck']" >${event.content!}</textarea>
						        </td>
					    	</tr>
					    </table>
					</div>
					<div class="fr" style="width:33%;">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td>
									<#if event.code??>
										<label class="LabName"><span>事件编号：</span></label><div class="Check_Radio">${event.code}</div>
									<#else>
									<@block name="patrolTypeInput">
									    <label class="LabName"><span></span></label>
								    </@block>
									</#if>
								</td>
							</tr>
					    	<tr>
					    		<td>
						    		<label class="LabName"><span>影响范围：</span></label>
									<input type="hidden" id="influenceDegree" name="influenceDegree" value="${event.influenceDegree!}" />
									<input type="text" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:'bottom'" id="influenceDegreeName" value="${event.influenceDegreeName!}" />
								</td>
					    	</tr>
					    	<tr>
						        <td>
							        <label class="LabName"><span>信息来源：</span></label>
							        <input type="hidden" id="source" name="source" value="${event.source!}" />
									<input type="text" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:'bottom'" id="sourceName" value="${event.sourceName!}" />
					    		</td>
					        </tr>
					    	<tr>
					    		<td>
						    		<label class="LabName"><span>紧急程度：</span></label>
						    		<input type="hidden" id="urgencyDegree" name="urgencyDegree" value="${event.urgencyDegree!}" />
									<input type="text" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:'bottom'" id="urgencyDegreeName" value="${event.urgencyDegreeName!}" />
					    		</td>
					    	</tr>
							<@block name="involvedNumInput">
					    	<tr>
					    		<td>
						    		<label class="LabName"><span>涉及人数：</span></label>
						    		<input type="hidden" id="involvedNum" name="involvedNum" value="${event.involvedNum!}" />
									<input type="text" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:'bottom'" id="involvedNumName" value="${event.involvedNumName!}" />
								</td>
					    	</tr>
							</@block>
					    	<@block name="contactUserTr">
					    	<tr>
					    		<td style="border-bottom:none;">
					    			<label class="LabName"><span id="contactUserLabelSpan">联系人员：</span></label><input  id="contactUser" name="contactUser" type="text" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:'bottom',validType:['maxLength[30]','characterCheck']" value="${event.contactUser!}" />
					    		</td>
					    	</tr>
					    	</@block>
					    	<@block name="contactTelTr">
					    	<tr>
					    		<td style="border-bottom:none;">
					    			<label class="LabName"><span id="contactTelLabelSpan">联系电话：</span></label><input name="tel" id="tel" type="text" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:'bottom',validType:'mobileorphone'" value="${event.tel!}" />
					    		</td>
					    	</tr>
					    	</@block>
					    	<@block name="singleLineExtraInfoTr"></@block>
					    </table>
					</div>
					<div class="clear" style="border-top:1px dotted #cecece;">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr id="adviceTr" class="hide">
					    		<td class="LeftTd">
					    			<label class="LabName"><span>办理意见：</span></label><textarea rows="3" style="height:80px;" id="advice" name="advice" class="area1 easyui-validatebox fast-reply" data-options="tipPosition:'bottom',validType:['maxLength[2048]','characterCheck']">${advice!}</textarea>
					    		</td>
					    	</tr>
							<tr>
								<td class="LeftTd">
					    			<label class="LabName"><span>涉及人员：</span></label>
						    		<input type="hidden" name="eventInvolvedPeople" id="eventInvolvedPeople" value="<#if event.eventInvolvedPeople??>${event.eventInvolvedPeople}</#if>" />
						    		<input type="hidden" name="involvedPersion" id="involvedPersion" value="<#if event.involvedPersion??>${event.involvedPersion}</#if>" />
						        	<div class="addinfo" style="width:667px;">
						            	<code class="fl FontDarkBlue" onclick="showInvoledPeopleSelector();"><a href="###">点击添加人员</a></code>
						            	<div id="involvedPeopleName">
						            		<#if involvedPeopleList?? >
						            			<#list involvedPeopleList as l>
										    		<p title="${l.name!}(${l.idCard!})">${l.name!}<img src='${rc.getContextPath()}/images/sys1_29.png' onclick='removeInvolvedPeople("${l.name!}","${l.idCard!}", $(this).parent());'/></p>
										    	</#list>
						            		</#if>
						            	</div>
						            </div>
					    		</td>
							</tr>
					    	<tr id="bigFileUploadTr">
					    		<td class="LeftTd">
					    			<label class="LabName"><span><label id="bigFileUploadAsterik" class="Asterik hide">*</label>图片上传：</span></label><div id="bigFileUploadDiv"></div>
					    		</td>
					    	</tr>
						</table>
					</div>
				</div>
			</div>
			
			<div class="BigTool">
			<@block name="ButtonListBlock">
				<div class="BtnList">
					<#if isShowSaveBtn?? && !isShowSaveBtn>
		        	<#else>
	        		<a href="###" onclick="showAdvice('saveEvent', null, '-1');" class="BigNorToolBtn SaveBtn">保存</a>
	        		</#if>
	        		
	        		<#if (isReport?? && isReport)>
	            	<a href="###" onclick="showAdvice('saveEventAndReport', null, '0');" class="BigNorToolBtn BigShangBaoBtn">提交</a>
	            	<#else>
	        		<a href="###" onclick="showAdvice('saveEventAndStart', 'startWorkFlow', '0');" class="BigNorToolBtn BigShangBaoBtn">提交</a>
	        		</#if>
	        		
	        		<#if isShowCloseBtn?? && !isShowCloseBtn>
		        	<#else>
		        	<@block name="closeButtonBlock">
	        		<a href="###" id="archiveButton" onclick="showAdvice(null, null, '1');" class="BigNorToolBtn BigJieAnBtn">结案</a>
	        		</@block>
	        		</#if>
	            </div>
	        </@block>
	        </div>	
		</div>
	</form>
</div>
	<iframe id="crossOverIframe" name="crossOverIframe" src="" style="display:none;" ></iframe>
	<#include "/component/involvedPeopleSelector.ftl">
	<#include "/component/maxJqueryEasyUIWin.ftl" />
</body>

<script type="text/javascript">
	var eventTreeApi;
	
	$(function() {
		var options = { 
            axis : "yx", 
            theme : "minimal-dark" 
        };
		
        enableScrollBar('content-d',options); 
        
        $("#norFormDiv").width($(window).width());
        $("#advice").width($(window).width() * 0.85);
        
        var bigFileUploadOpt = {
        	useType: 'add',
        	fileExt: '.jpg,.gif,.png,.jpeg,.webp',
        	attachmentData: {attachmentType:'${EVENT_ATTACHMENT_TYPE!}'},
        	module: 'event',
        	individualOpt : {
        		isUploadHandlingPic : <#if isUploadHandlingPic??>${isUploadHandlingPic?c}<#else>false</#if>
        	}
        };
        
        <#if event.eventId?? || attachmentIds??>
        	bigFileUploadOpt["useType"] = 'edit'; 
			bigFileUploadOpt["attachmentData"].eventSeq = "1,2,3";
			
			<#if event.eventId??>
				bigFileUploadOpt["attachmentData"].bizId = '${event.eventId?c}';
			</#if>
			
			<#if attachmentIds??>
				bigFileUploadOpt["attachmentData"].attachmentIds = "${attachmentIds!}";
			</#if>
        </#if>
        
        <@block name="bigFileUploadInitOption"></@block>
        
        bigFileUpload_initFileUploadDiv('bigFileUploadDiv', bigFileUploadOpt);
        
        var typesDictCode = "${typesDictCode!}";
        if(isNotBlankString(typesDictCode) && typesDictCode!="null") {
        	eventTreeApi=AnoleApi.initTreeComboBox("typeName", "type", { 
        		"${bigTypePcode!}" : [${typesDictCode!}] 
        	}, <@block name="callBackSelected4eventType">null</@block>, null, {//0 展示指定的字典；1 去除指定的字典；
				FilterType : "<#if isRemoveTypes?? && isRemoveTypes>1<#else>0</#if>",
				<@block name="extraSetting4eventType"></@block>
				EnabledSearch : true
        	});
        } else {
        	eventTreeApi=AnoleApi.initTreeComboBox("typeName", "type", "${bigTypePcode!}", <@block name="callBackSelected4eventType">null</@block>, null, {
        		<@block name="extraSetting4eventType"></@block>
        		EnabledSearch : true
        	});
        }
        
        <@block name="gridTreeInitMethod">
        	var gridOpt = {};
        	
        	<#if rootGridId?? && (rootGridId > 0)>
        		gridOpt.startGridId = '${rootGridId?c}';
        	</#if>
        	
        	<@block name="extraGridOption"></@block>
        	
        	AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId, items) {
        		if(isNotBlankParam(items) && items.length > 0) {
        			var grid = items[0];
        			$("#gridCode").val(grid.orgCode);
        		}
        	}, gridOpt);
        </@block>
        
        AnoleApi.initListComboBox("influenceDegreeName", "influenceDegree", "${influenceDegreePcode}", null, [<#if event.influenceDegree??>"${event.influenceDegree}"<#else>"01"</#if>]);
        AnoleApi.initListComboBox("urgencyDegreeName", "urgencyDegree", "${urgencyDegreePcode}", null, [<#if event.urgencyDegree??>"${event.urgencyDegree}"<#else>"01"</#if>]);
        AnoleApi.initListComboBox("involvedNumName", "involvedNum", "${involvedNumPcode}", null, [<#if event.involvedNum??>"${event.involvedNum}"<#else>"00"</#if>]);
        var sourceDictCode = "${sourceDictCode!}";
		if(sourceDictCode!=null && sourceDictCode!="null" && sourceDictCode!="") {
			AnoleApi.initTreeComboBox("sourceName", "source", { 
				"${sourcePcode!}" : [${sourceDictCode!}] 
			}, null, [<@block name="defaultEventSourceValue"><#if event.source??>"${event.source}"<#else>"01"</#if></@block>], {//0 展示指定的字典；1 去除指定的字典；
				FilterType : "<#if isRemoveSource?? && isRemoveSource>1<#else>0</#if>"
				<@block name="sourceDictExtraOption"></@block>
			});
		} else {
			AnoleApi.initListComboBox("sourceName", "source", "${sourcePcode}", null, [<@block name="defaultEventSourceValue"><#if event.source??>"${event.source}"<#else>"01"</#if></@block>], {
				FilterType : 0
				<@block name="sourceDictExtraOption"></@block>
			});
		}
        <@block name="initExpandScript"></@block>

		var mapMarker= new MapMarker({
			el:"map",//div挂载点
			context:"${GIS_DOMAIN}",//gis域名
			data:{ //业务数据
				id : "${event.eventId!}",//业务标识
				markerType :'0301',//模块类型
				markerOperation :0,//地图操作类型 0和1为添加修改标注，2为查看标注
				gridId : $("#gridId").val(),//网格标识，用于打开地图初始的默认位置
				<#if event.resMarker??>
				<#if event.resMarker.x?? && event.resMarker.y??>
				initPosType:1,
				initPosVal:{x:'${event.resMarker.x!0}',y:'${event.resMarker.y!0}',z:'${event.resMarker.z!0}'},
				</#if>
				</#if>
				width:480,//弹框宽度，可以不传，默认480px
				height:360,//弹框高度，可以不传，默认360px
			},
			done:function (data) {//弹框确认回调，已回填了xyz到页面元素

			}
		});
		<#--        <#if event.resMarker??>-->
<#--        	var resMarkerX = "${event.resMarker.x!}",-->
<#--        		resMarkerY = "${event.resMarker.y!}",-->
<#--        		resMarkerMapType = "${event.resMarker.mapType!}";-->
<#--        		-->
<#--        	if(resMarkerX && resMarkerY && resMarkerMapType) {-->
<#--        		callBackOfData(resMarkerX, resMarkerY, null, resMarkerMapType);-->
<#--        	}-->
<#--        </#if>-->
		
	});
	
	function showAdvice(m, callback, toClose) {//展示办理意见
		if(toClose && toClose == '1') {
			$('#advice').validatebox({
				required: true
			});
			
			$("#adviceTr").show();
			
			closed();
		} else {
			$("#adviceTr").hide();
			
			$('#advice').validatebox({
				required: false
			});
			
			$('#advice').val($('#_outerAdvice').val());
			
			tableSubmit(m, callback, toClose);
		}
	}
		
	function tableSubmit(m, callback, toClose) {
		var isValid =  $("#tableForm").form('validate'),
			advice = $("#advice").val(),
			isAdviceVisible = $("#adviceDiv").is(":visible");
		
		if(isValid) {
			isValid = checkAttachmentStatus4BigFileUpload('bigFileUploadDiv');
		}
		
		<@block name="attachmentCheck"></@block>
		
		if(isValid) {
			var type = $("#type").val();
			
			if(isBlankParam(toClose)){
				toClose = "";
			} else if(toClose == "1") {
				$("#result").val($("#advice").val());
			}
			
			$("#tableForm").attr("action","${rc.getContextPath()}/zhsq/event/eventDisposalController/"+m+"/"+type+".jhtml?toClose="+toClose);
	      	
	      	modleopen();
		  	$("#tableForm").ajaxSubmit(function(data) {
		  		if(data.eventId) {
	  				var iframeUrl = "${iframeUrl!}",
						outerCallBack = "${callBack!}";
					
					 if(callback) {//为了保证提交、上报、结案能正常操作
						eval(callback)(data);
					} else if(iframeUrl && outerCallBack){//跨域回调
						<@block name="addEventMenuTableSubmitCrossOverHandler">
						if(iframeUrl.indexOf('?') != -1) {
							iframeUrl += "&";
						} else {
							iframeUrl += "?";
						}
						
						data.isCrossDomain = true;
						iframeUrl += "callBack=" + outerCallBack + "&callBackParams="+ JSON.stringify(data);
						$("#crossOverIframe").attr("src", iframeUrl);
						</@block>
					} else if(outerCallBack) {//本域回调
						eval(outerCallBack)(data);
					} else {//自身调用回调
						var msg = "添加";
						<#if event.eventId??>
							m = "editEvent";
							msg = "更新";
						</#if>
						<#if (isReport?? && isReport)>
							msg = "上报";
						</#if>
						
						if(data.result){
		  					msg += "成功";
		  				}else{
		  					msg += "失败";
		  				}
		  				
						flashData(msg, data.type);
					}
				} else {
					modleclose();
					$.messager.alert('错误', '保存事件失败，请重试！', 'error');
				}
			});
	  	}
	}
	
	function closed() {//事件结案
		var isValid =  $("#tableForm").form('validate');
		if(isValid){
			var eventId = $("#eventId").val();
			
			if(eventId && eventId != "") {
				modleopen();
				$.ajax({
					type: "POST",
		    		url : '${rc.getContextPath()}/zhsq/event/eventDisposalController/startWorkFlow.jhtml',
					data: 'eventId='+ eventId +'&toClose=1&advice='+$("#advice").val(),
					dataType:"json",
					success: function(data) {
						if(startWorkFlow != undefined) {
				   			startWorkFlow(data);
				   		} else {
				   			$.messager.alert('错误','事件结案失败！','error');
				   		}
					},
					error:function(data){
						$.messager.alert('错误','事件结案，连接错误！','error');
					}
		    	});
			} else {
				tableSubmit('saveEventAndStart', 'startWorkFlow', "1");
			}
		}
	}
	
	function startWorkFlow(data) {//启动流程
		var formId = data.formId,
			new_workFlowId = data.workflowId,
			wftypeId = data.wftypeId,
			orgCode = data.orgCode,
			orgType = data.orgType,
			toClose = data.toClose,
			advice = data.advice;
		
		//启动流程
		$.ajax({
			//type: "POST",
			url : '${rc.getContextPath()}/zhsq/workflow/workflowController/startFlow.jhtml',
			data: {'formId': formId ,'workFlowId': new_workFlowId,'wftypeId': wftypeId, 'orgCode': orgCode, 'orgType': orgType, 'toClose': toClose, 'advice': advice},
			dataType:"json",
			success: function(data) {
				modleclose();
			    if(data.result){
			    	var instanceId = data.instanceId;
			    	if(isNotBlankString(instanceId)) {
			    		var iframeUrl = "${iframeUrl!}",
							outerCallBack = "${callBack!}";
						
						if(iframeUrl && outerCallBack) {//跨域回调
							<@block name="addEventMenuStartWorkflowCrossOverHandler">
							if(iframeUrl.indexOf('?') != -1) {
								iframeUrl += "&";
							} else {
								iframeUrl += "?";
							}
							
							data.isCrossDomain = true;
							data.eventId = formId;
							
							iframeUrl += "callBack=" + outerCallBack + "&callBackParams="+ JSON.stringify(data);
							$("#crossOverIframe").attr("src", iframeUrl);
							</@block>
						} else {
							var url = "${rc.getContextPath()}/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=todo&instanceId="+instanceId+"&workFlowId="+new_workFlowId+"&eventId="+formId+"&cachenum=" + Math.random();
							if(outerCallBack) {
								if(toClose == '0') {
									window.location = url;
								}
								eval(outerCallBack)(data);
							} else {
								if(toClose == '0') {
									showMaxJqueryWindow("事件办理", url, null, null, null, null, flashData);
								} else if(toClose == '1') {
									try{
			    						closeMaxJqueryWindow();//新增弹出窗口的关闭方法
			    					}catch(e){}
			    					$.messager.alert('','事件结案成功！','info',function() {
			    						flashData();
			    					});
								}
							}
						}
			    	}
			    } else {
			    	var msg = data.msgWrong || "事件启动失败！";
			    	
			    	try {
  						closeMaxJqueryWindow();//新增弹出窗口的关闭方法
  					} catch(e) {
  					}
  					
			    	$.messager.alert('错误',msg,'error', function() {
			    		location.reload();
			    	});
			    }
			},
			error:function(data) {
				$.messager.alert('错误','事件流程未启动成功！','error');
			}
		});
	}
	
	<#--function showMap(){//地图展示-->
	<#--	var callBackUrl = '${SQ_ZHSQ_EVENT_URL}/zhsq/map/arcgis/arcgis/toArcgisCrossDomain.jhtml';-->
	<#--	var width = 480;-->
	<#--	var height = 360;-->
	<#--	var gridId = $("#gridId").val();-->
	<#--	var markerOperation = $('#markerOperation').val();-->
	<#--	var id = $('#eventId').val();-->
	<#--	var mapType = 'EVENT_V1';-->
	<#--	var isEdit = true;-->
	<#--	showMapWindow(gridId,callBackUrl,width,height,isEdit,mapType);-->
	<#--}-->
	
	function involedPeopleCallback(users) {
		if(users == ""){
			$("#involvedPeopleName").html("");//用于页面显示
			$("#eventInvolvedPeople").val("");//用于后台保存
			$("#involvedPersion").val("");
			return;
		}
		var usersDiv = "";
		var userNames = "";
		var userArray = users.split("；");
		if(userArray != ""){
			$.each(userArray, function(i, n){
				var items = n.split("，");
				if(typeof(items[1])!="undefined" ){
					var userName = items[1];
					if(userName.length > 3){//名字显示前三个字
						userName = userName.substr(0, 3);
					}
					usersDiv += "<p title="+items[1]+"("+items[2]+")>"+userName+"<img src='${rc.getContextPath()}/images/sys1_29.png' onclick='removeInvolvedPeople(\""+items[1]+"\",\""+items[2]+"\", $(this).parent());'/></p>";
					userNames += items[1] + "，";
				}
			});
			
			userNames = userNames.substr(0, userNames.length - 1);
			$("#involvedPeopleName").html(usersDiv);//用于页面显示
			$("#eventInvolvedPeople").val(users);//用于后台保存
			$("#involvedPersion").val(userNames);
		}else{
			$("#involvedPeopleName").html("");//用于页面显示
			$("#eventInvolvedPeople").val("");//用于后台保存
			$("#involvedPersion").val("");
		}
	}
	
	function flashData(msg) {
		$("#tableForm").attr("action", window.location.href);
		if(isNotBlankString(msg)) {
			$.messager.alert('', msg, 'info',function(){
				$("#tableForm").submit();
			});
		} else {
			$("#tableForm").submit();
		}
	}
	<@block name="extraScriptFunction"></@block>
</script>
<@block name="extraFtlInclude"></@block>
</html>
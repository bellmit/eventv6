<!DOCTYPE html>
<html>
<head>
<meta http-equiv="x-ua-compatible" content="ie=8" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><@block name="eventAddPageTitle">事件采集</@block></title>
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
	.LabName span{padding-right: 5px;}
</style>
<#include "/component/ComboBox.ftl" />
</head>
<body>
<div>
	<form id="tableForm" name="tableForm" action="" method="post" enctype="multipart/form-data">
		<input type="hidden" id="name" name="name" value="<#if event.eventName??>${event.eventName}</#if>" />
		
		<input type="hidden" id="gridId" name="gridId" value="<#if event.gridId??>${event.gridId?c}</#if>">
		<input type="hidden" id="gridCode" name="gridCode" value="<#if event.gridCode??>${event.gridCode}</#if>">
		<input type="hidden" id="type" name="type" value="<#if event.type??>${event.type}</#if>" />
		<input type="hidden" id="code" name="code" value="<#if event.code??>${event.code}</#if>" />
		<input type="hidden" id="eventId" name="eventId" value="<#if event.eventId??>${event.eventId?c}</#if>" />
		<input type="hidden" name="parentEventId" value="<#if parentEventId??>${parentEventId?c}</#if>" />
		<!--办理意见-->
		<input type="hidden" id="result" name="result" value="${event.result!}" />
		
		<!--用于地图-->
		<input type="hidden" id="id" name="id" value="<#if event.eventId??>${event.eventId?c}</#if>" /> 
		<input type="hidden" id="markerOperation" name="markerOperation" value="<#if markerOperation??>${markerOperation}</#if>"/>
		<!--事件上报关联模块-->
		<input type="hidden" name="eventReportRecordInfo.bizId" value="<#if event.eventReportRecordInfo?? && event.eventReportRecordInfo.bizId??>${event.eventReportRecordInfo.bizId?c}</#if>" />
		<input type="hidden" name="eventReportRecordInfo.bizType" value="<#if event.eventReportRecordInfo??>${event.eventReportRecordInfo.bizType!}</#if>" />
		
		<input type="hidden" name="redisKey" value="${redisKey!}" />
		
		<div style="margin: 0 auto; background-color:#F9F9F9; position:relative;">
		<div id="operateMask" class="MarskLayDiv hide"></div>
		<div id="adviceDiv" class="clear PopDiv NorForm hide">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td colspan="2" class="LeftTd" style="padding-left: 10px;">
						<span><label class="Asterik">*</label>事件结案请填写办理意见：</span>
					</td>
				</tr>
				<tr>
		    		<td colspan="2" class="LeftTd" style="padding-left: 10px;">
		    			<textarea rows="3" style="width:510px;height:80px;" id="advice" name="advice" class="area1 easyui-validatebox fast-reply" data-options="tipPosition:'top',validType:['maxLength[2048]','characterCheck']">${advice!}</textarea>
		    		</td>
		    	</tr>
		    	<tr>
		    		<td>
		    			<a href="###" onclick="tableSubmit('saveEvent', '2', 'parent.startWorkFlow', '1');" class="BigNorToolBtn BigJieAnBtn" style="float:right;">结案</a>
		    		</td>
		    		<td>
						<a href="###" onclick="closeAdviceDiv();" class="BigNorToolBtn CancelBtn">取消</a>
					</td>
		    	</tr>
		    </table>
		</div>
		
		<@block name="eventAddExtendContent"></@block>
		
		<div id="content-d" class="MC_con content light" style="overflow-x:hidden">
			<div id="norFormDiv" class="NorForm" style="width:784px;">
				<div class="fl" style="width:67%;">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td class="LeftTd">
								<label class="LabName">
									<span>
										<label class="Asterik">*</label>事件分类：
									</span>
								</label>
								<input type="text" class="inp1 InpDisable easyui-validatebox" style="width:155px;"
									   data-options="required:true" id="typeName" name="typeName" maxlength="100"
									   value="<#if event.typeName??>${event.typeName}<#else><#if event.eventClass??>${event.eventClass}</#if></#if>" />
							</td>
							<@block name="gridNameTd">
							<td>
								<label class="LabName"><span>所属网格：</span></label><input type="text" class="inp1 InpDisable easyui-validatebox" style="width:122px;" data-options="required:true" id="gridName" name="gridName" value="<#if event.gridName??>${event.gridName}</#if>" />
							</td>
							</@block>
						</tr>
				    	<tr>
				    		<td colspan="2" class="LeftTd">
				    			<label class="LabName"><span><label class="Asterik">*</label>事件标题：</span></label><input type="text" class="inp1 easyui-validatebox" style="width:405px;" data-options="required:true,tipPosition:'bottom',validType:['maxLength[100]','characterCheck']" name="eventName" id="eventName" value="<#if event.eventName??>${event.eventName}</#if>" />
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
				    			<label class="LabName">
									<span>
										<label class="Asterik">*</label>事发详址：
									</span>
								</label>
									<input type="text" class="inp1 easyui-validatebox" style="width:405px;"
										   data-options="required:true,tipPosition:'bottom',validType:['maxLength[255]','characterCheck']"
										   name="occurred" id="occurred" value="<#if event.occurred??>${event.occurred}</#if>" />
				    		</td>
				    	</tr>
						</@block>
				    	<@block name="geographicalLabelingInput">
<#--				    	<tr>-->
<#--				    		<td colspan="2" class="LeftTd">-->
<#--				    			<label class="LabName"><span>地理标注：</span></label>-->
<#--				    			<#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/>-->
<#--				        	</td>-->
<#--				        </tr>-->
							<tr>
								<td colspan="2" class="LeftTd">
									<label class="LabName"><span><label id="mapAsterik" class="Asterik hide">*</label>地理标注：</span></label>
									<div id="map" style="display:inline"></div>
								</td>
							</tr>
				        </@block>
				    	<tr>
				    		<td colspan="2" class="LeftTd" style="border-bottom:none;">
				    			<label class="LabName"><span><label class="Asterik">*</label>事件描述：</span></label><textarea name="content" id="content" cols="" rows="" class="area1 easyui-validatebox" style="width:400px; height:64px;resize: none;" data-options="required:true,tipPosition:'bottom',validType:[<#if characterLimit??>'minLength[${characterLimit?c}]',</#if>'maxLength[1024]','characterCheck']" ><#if event.content??>${event.content}</#if></textarea>
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
								<input type="hidden" id="influenceDegree" name="influenceDegree" value="<#if event.influenceDegree??>${event.influenceDegree}</#if>" />
								<input type="text" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:'bottom'" id="influenceDegreeName" value="<#if event.influenceDegreeName??>${event.influenceDegreeName}</#if>" />
							</td>
				    	</tr>
				    	<tr>
					        <td>
						        <label class="LabName"><span>信息来源：</span></label>
						        <input type="hidden" id="source" name="source" value="<#if event.source??>${event.source}</#if>" />
								<input type="text" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:'bottom'" id="sourceName" value="<#if event.sourceName??>${event.sourceName}</#if>" />
				    		</td>
				        </tr>
				    	<tr>
				    		<td>
					    		<label class="LabName"><span>紧急程度：</span></label>
					    		<input type="hidden" id="urgencyDegree" name="urgencyDegree" value="<#if event.urgencyDegree??>${event.urgencyDegree}</#if>" />
								<input type="text" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:'bottom'" id="urgencyDegreeName" value="<#if event.urgencyDegreeName??>${event.urgencyDegreeName}</#if>" />
				    		</td>
				    	</tr>
						<@block name="involvedNumInput">
				    	<tr>
				    		<td>
					    		<label class="LabName"><span>涉及人数：</span></label>
					    		<input type="hidden" id="involvedNum" name="involvedNum" value="<#if event.involvedNum??>${event.involvedNum}</#if>" />
								<input type="text" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:'bottom'" id="involvedNumName" value="<#if event.involvedNumName??>${event.involvedNumName}</#if>" />
							</td>
				    	</tr>
						</@block>
				    	<@block name="contactUserTr">
				    	<tr>
				    		<td style="border-bottom:none;">
				    			<label class="LabName"><span id="contactUserLabelSpan">联系人员：</span></label>
				    			<input  id="contactUser" name="contactUser" type="text" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:'bottom',validType:['maxLength[30]','characterCheck']" value="<#if event.contactUser??>${event.contactUser}</#if>" />
				    		</td>
				    	</tr>
				    	</@block>
				    	<@block name="contactTelTr">
				    	<tr>
				    		<td style="border-bottom:none;">
				    			<label class="LabName"><span id="contactTelLabelSpan">联系电话：</span></label>
				    			<input name="tel" id="tel" type="text" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:'bottom',validType:'mobileorphone'" value="<#if event.tel??>${event.tel}</#if>" />
				    		</td>
				    	</tr>
				    	</@block>
				    	<@block name="singleLineExtraInfoTr"></@block>
				    </table>
				</div>
				<div class="clear" style="border-top:1px dotted #cecece;">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
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
									    		<p title="<#if l.name??>${l.name}</#if>(<#if l.idCard??>${l.idCard}</#if>)"><#if l.name??>${l.name}</#if><img src='${rc.getContextPath()}/images/sys1_29.png' onclick='removeInvolvedPeople("<#if l.name??>${l.name}</#if>","<#if l.idCard??>${l.idCard}</#if>", $(this).parent());'/></p>
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
			<div class="BtnList">
	            <#if (isReport?? && isReport)>
	            	<a href="###" onclick="tableSubmit('saveEventAndReport', '3', <#if callBack??>'${callBack}'<#else>null</#if>, '0');" class="BigNorToolBtn BigShangBaoBtn">提交</a>
					<a href="###" onclick="cancel();" class="BigNorToolBtn CancelBtn">取消</a>
					<!--
			    	<a href="###"><img src="${rc.getContextPath()}/images/sys1_25.png" />越级上报</a>
			    	-->
		        <#else>
		        
		        	<#if isShowSaveBtn?? && !isShowSaveBtn>
		        	<#else>
	        		<a href="###" onclick="tableSubmit('saveEvent', '0', null, '-1');" class="BigNorToolBtn SaveBtn">保存</a>
	        		</#if>
	        		<#if !(instanceId?? && (instanceId > 0))>
	        		<a href="###" onclick="tableSubmit('saveEvent', '1', 'parent.startWorkFlow', '0');" class="BigNorToolBtn BigShangBaoBtn">提交</a>
	        		<@block name="closeButtonBlock">
	        		<a href="###" id="archiveButton" onclick="tableSubmit('saveEvent', '2', 'parent.startWorkFlow', '1');" class="BigNorToolBtn BigJieAnBtn">结案</a>
	        		</@block>
	        		</#if>
					<a href="###" onclick="cancel();" class="BigNorToolBtn CancelBtn">取消</a>
					<!--
			    	<a href="###"><img src="${rc.getContextPath()}/images/sys1_25.png" />越级上报</a>
			    	-->
	            </#if>
            </div>
        </div>	
        </div>
	</form>
</div>
	<iframe id="crossOverIframe" name="crossOverIframe" src="" style="display:none;" ></iframe>
	<#include "/component/customEasyWin.ftl" />
	<#include "/component/involvedPeopleSelector.ftl">
	<#include "/component/maxJqueryEasyUIWin.ftl" />
</body>
<script type="text/javascript">
	var type = "${event.type!}";
	var _winHeight = 0;
	var _winWidth = 0;
	var eventTreeApi;
	
	$(function(){
		_winHeight = $(window).height();
		_winWidth = $(window).width();
		
		var options = { 
            axis : "yx", 
            theme : "minimal-dark" 
        }; 
        enableScrollBar('content-d',options);
        
        $("#norFormDiv").width($(window).width());
        
        $("#advice").width($('#adviceDiv').width() * 0.95);
		
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
		if(typesDictCode!=null && typesDictCode!="null" && typesDictCode!="") {
			eventTreeApi = AnoleApi.initTreeComboBox("typeName", "type", { 
				"${bigTypePcode!}" : [${typesDictCode!}] 
			}, <@block name="callBackSelected4eventType">null</@block>, [<#if event.type??>"${event.type}"</#if>], {//0 展示指定的字典；1 去除指定的字典；
				FilterType : "<#if isRemoveTypes?? && isRemoveTypes>1<#else>0</#if>",
				<@block name="extraSetting4eventType"></@block>
				EnabledSearch : true
			});
		} else {
			eventTreeApi = AnoleApi.initTreeComboBox("typeName", "type", "${bigTypePcode!}", <@block name="callBackSelected4eventType">null</@block>, [<#if event.type??>"${event.type}"</#if>], {
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
				//type:undefined,//用于部分表主键改为uuuId的类型，如果不是uuuId的类型，选择不传
				width:480,//弹框宽度，可以不传，默认480px
				height:360,//弹框高度，可以不传，默认360px
			},
			done:function (data) {//弹框确认回调，已回填了xyz到页面元素

			}
		});
<#--				<#if event.resMarker??>-->
<#--			var resMarkerX = "${event.resMarker.x!}",-->
<#--				resMarkerY = "${event.resMarker.y!}",-->
<#--				resMarkerMapType = "${event.resMarker.mapType!}";-->

<#--			if(resMarkerX && resMarkerY && resMarkerMapType) {-->
<#--				callBackOfData(resMarkerX, resMarkerY, null, resMarkerMapType);-->
<#--			}-->
<#--		</#if>-->
	});
	
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
	
	//btnType：0 保存；1 提交；2 结案；3 上报；
	function tableSubmit(m, btnType, callback, toClose){
		var isValid =  $("#tableForm").form('validate');
		var advice = $("#advice").val();
		var isAdviceVisible = $("#adviceDiv").is(":visible");

		if(!type) {
			type = $("#type").val();
		}
		
		if(isValid) {
			isValid = checkAttachmentStatus4BigFileUpload('bigFileUploadDiv');
		}
		
		<@block name="attachmentCheck"></@block>
		
		if(isValid && isAdviceVisible && advice=="") {
			$.messager.alert('警告','请填写办理意见！','warning');
		} else if(isValid && toClose=="1" && !isAdviceVisible) {
			openAdviceDiv();
		} else if(isValid){
			var msg = "添加";
			<#if event.eventId??>
				m = "editEvent";
				msg = "更新";
			</#if>
			<#if (isReport?? && isReport)>
				msg = "上报";
			</#if>

			if(isBlankString(toClose)){
				toClose = "";
			} else if(toClose == "1") {
				$("#result").val($("#advice").val());
			}
			
			if(isNotBlankStringTrim(callback)){
				<#if !(isReport?? && isReport)>
					m += "AndStart";
				</#if>
			}
			
			$("#tableForm").attr("action","${rc.getContextPath()}/zhsq/event/eventDisposalController/"+m+"/"+type+".jhtml?toClose="+toClose);
	      	
	      	modleopen();
		  	$("#tableForm").ajaxSubmit(function(data) {
		  		modleclose();
		  		
		  		data.btnType = btnType;//设置按钮类型
		  		
		  		if(data.eventId){
		  			var iframeUrl = "${iframeUrl!}",
		  				iframeCallBack = "${callBack!}";
		  			
	  				if(iframeUrl && iframeCallBack){//跨域回调
	  					if(iframeUrl.indexOf('?') != -1){
							iframeUrl += "&";
						}else{
							iframeUrl += "?";
						}
						
						data.isCrossDomain = true;
						iframeUrl += "callBack=" + iframeCallBack + "&callBackParams="+ encodeURIComponent(JSON.stringify(data));
						$("#crossOverIframe").attr("src", iframeUrl);
	  				}else if(callback!=undefined && callback!=null && callback!=""){//本项目回调
		  				eval(callback)(data);
		  			}else{
		  				if(data.result){
		  					msg += "成功";
		  				}else{
		  					msg += "失败";
		  				}
		  				parent.reloadDataForSubPage(msg,data.type);
		  			}
				}else{
					$.messager.alert('错误','连接超时，请重试！', 'error');
				}
			});
	  	}
	}
	
	function cancel(){
		var closeCallBack = "${iframeCloseCallBack!}",
			iframeUrl = "${iframeUrl!}";
		
		if(iframeUrl && closeCallBack){
			if(iframeUrl.indexOf('?') != -1){
				iframeUrl += "&";
			}else{
				iframeUrl += "?";
			}
			
			iframeUrl += "callBack=" + closeCallBack;
			$("#crossOverIframe").attr("src", iframeUrl);
		}else{
			parent.closeMaxJqueryWindow();
		}
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
	
	function closed(){
		var isValid =  $("#tableForm").form('validate');
		if(isValid){
			modleopen();
			var eventId = $("#eventId").val();
			if(isNotBlankStringTrim(eventId)){
				$.ajax({
					type: "POST",
		    		url : '${rc.getContextPath()}/zhsq/event/eventDisposalController/startWorkFlow.jhtml',
					data: 'eventId='+ eventId +'&toClose=1',
					dataType:"json",
					success: function(data){
						if(parent.startWorkFlow != undefined){
				   			parent.startWorkFlow(data);
				   		}else{
				   			$.messager.alert('错误','连接错误！','error');
				   		}
					},
					error:function(data){
						$.messager.alert('错误','连接错误！','error');
					}
		    	});
			}else{
				tableSubmit('saveEvent', '2', parent.startWorkFlow, "1");
			}
		}
	}
	
	/*
		地图标注改造 2015-06-29，参考geo项目行政区划地图标注
		markerOperation:地图标注操作类型
						 0表示添加
						 1表示编辑
						 2表示查看
		isEdit:是否是编辑状态
	*/
	
/*	function showMap(){
		var callBackUrl = '${SQ_ZHSQ_EVENT_URL}/zhsq/map/arcgis/arcgis/toArcgisCrossDomain.jhtml';
		var width = 480;
		var height = 360;
		var gridId = $("#gridId").val();
		var markerOperation = $('#markerOperation').val();
		var id = $('#eventId').val();
		var mapType = 'EVENT_V1';
		var isEdit = true;
		var parameterJson = getParameterJson();
		showMapWindow(gridId,callBackUrl,width,height,isEdit,mapType,parameterJson,mapType);
	}
	function getParameterJson() {
		var parameterJson={
			"id":$("#id").val(),
			"name":$("#name").val()
		}
		return parameterJson;
	}*/

	<@block name="extraScriptFunction"></@block>

	$(window).resize(function(){
		var winHeight = $(window).height();
		var winWidth = $(window).width();
		
		if(winHeight != _winHeight || winWidth != _winWidth) {
			location.reload();
		}
    });
</script>
<@block name="extraFtlInclude"></@block>
</html>
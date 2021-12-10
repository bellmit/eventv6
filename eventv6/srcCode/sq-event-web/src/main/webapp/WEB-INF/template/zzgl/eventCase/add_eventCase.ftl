<!DOCTYPE html>
<html>
<head>
<meta http-equiv="x-ua-compatible" content="ie=8" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>案件采集</title>
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
<#include "/component/commonFiles-1.1.ftl" />
<script type="text/javascript" src="${COMPONENTS_URL}/js/fastreply/fastReply.js" defer="true"></script>
<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
<#include "/component/bigFileUpload.ftl" />

<style type="text/css">
	.LabName{height:30px;}
</style>
<#include "/component/ComboBox.ftl" />
</head>
<body>
<div>
	<form id="tableForm" name="tableForm" action="" method="post" enctype="multipart/form-data">
		<input type="hidden" id="gridId" name="gridId" value="<#if eventCase.gridId??>${eventCase.gridId?c}</#if>">
		<input type="hidden" id="infoOrgCode" name="infoOrgCode" value="${eventCase.infoOrgCode!}">
		<input type="hidden" id="type" name="type" value="${eventCase.type!}" />
		<input type="hidden" id="code" name="code" value="${eventCase.code!}" />
		<input type="hidden" id="caseId" name="caseId" value="<#if eventCase.caseId??>${eventCase.caseId?c}</#if>" />
		<input type="hidden" name="collectWay" value="${eventCase.collectWay!'02'}" />
		
		<input type="hidden" name="isAlterResMarker" value="true" />
		<input type="hidden" name="isAlterInvolvedPeople" value="true" />
		<input type="hidden" name="isAlterAttachment" value="true" />
		<input type="hidden" id="isStart" name="isStart" value="false" />
		<input type="hidden" id="isClose" name="isClose" value="false" />
		<input type="hidden" id="attachment_attachmentIds" name="attachment.attachmentIds" value="" />
		
		<!--用于地图-->
		<input type="hidden" id="name" name="name" value="${eventCase.caseName!}" />
		<input type="hidden" id="id" name="id" value="<#if eventCase.caseId??>${eventCase.caseId?c}</#if>" />
		<input type="hidden" id="markerOperation" name="markerOperation" value="<#if markerOperation??>${markerOperation}</#if>"/>
		<input type="hidden" id="module" value="${mapModuleCode!}" />
		
		<div style="margin: 0 auto; background-color:#F9F9F9; position:relative;">
		<div id="content-d" class="MC_con content light" style="overflow-x:hidden">
			<div id="norFormDiv" class="NorForm" style="width:784px;">
				<div class="fl" style="width:67%;">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>案件分类：</span></label><input type="text" class="inp1 InpDisable easyui-validatebox" style="width:155px;" data-options="required:true" id="typeName" name="typeName" maxlength="100" value="<#if eventCase.typeName??>${eventCase.typeName}<#else>${eventCase.eventClass!}</#if>" />
							</td>
							<td>
								<label class="LabName"><span>所属网格：</span></label><input type="text" class="inp1 InpDisable easyui-validatebox" style="width:122px;" data-options="required:true" id="gridName" name="gridName" value="<#if eventCase.gridName??>${eventCase.gridName}</#if>" />
							</td>
						</tr>
				    	<tr>
				    		<td colspan="2" class="LeftTd">
				    			<label class="LabName"><span><label class="Asterik">*</label>案件标题：</span></label><input type="text" class="inp1 easyui-validatebox" style="width:405px;" data-options="required:true,tipPosition:'bottom',validType:['maxLength[100]','characterCheck']" name="caseName" id="caseName" value="${eventCase.caseName!}" />
				    		</td>
				    	</tr>
				    	<tr>
				    		<td colspan="2" class="LeftTd">
				    			<label class="LabName"><span><label class="Asterik">*</label>案发时间：</span></label><input type="text" id="happenTimeStr" name="happenTimeStr" class="inp1 Wdate easyui-validatebox" style="width:155px; cursor:pointer;" data-options="required:true" onclick="WdatePicker({readOnly:true, maxDate:'${(eventCase.happenTimeStr!maxHappenTime)?substring(0,10)} 23:59:59', dateFmt:'yyyy-MM-dd HH:mm:ss', isShowClear:false, isShowToday:false})" value="${eventCase.happenTimeStr!}" readonly="readonly"></input>
				    		</td>
				    	</tr>
				    	<tr>
				    		<td colspan="2" class="LeftTd">
				    			<label class="LabName"><span><label class="Asterik">*</label>案发详址：</span></label><input type="text" class="inp1 easyui-validatebox" style="width:405px;" data-options="required:true,tipPosition:'bottom',validType:['maxLength[255]','characterCheck']" name="occurred" id="occurred" value="<#if eventCase.occurred??>${eventCase.occurred}</#if>" />
				    		</td>
				    	</tr>
				    	<tr>
				    		<td colspan="2" class="LeftTd">
				    			<label class="LabName"><span>地理标注：</span></label>
				    			<#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/>
				        	</td>
				        </tr>
				    	<tr>
				    		<td colspan="2" class="LeftTd" style="border-bottom:none;">
				    			<label class="LabName"><span><label class="Asterik">*</label>案件描述：</span></label><textarea name="content" id="content" cols="" rows="" class="area1 easyui-validatebox" style="width:400px; height:64px;resize: none;" data-options="required:true,tipPosition:'bottom',validType:['maxLength[1024]','characterCheck']" ><#if eventCase.content??>${eventCase.content}</#if></textarea>
					        </td>
				    	</tr>
				    </table>
				</div>
				<div class="fr" style="width:33%;">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td>
								<#if eventCase.code??>
									<label class="LabName"><span>案件编号：</span></label><div class="Check_Radio">${eventCase.code}</div>
								<#else>
									<label class="LabName"><span></span></label>
								</#if>
							</td>
						</tr>
				    	<tr>
				    		<td>
					    		<label class="LabName"><span>影响范围：</span></label>
								<input type="hidden" id="influenceDegree" name="influenceDegree" value="<#if eventCase.influenceDegree??>${eventCase.influenceDegree}</#if>" />
								<input type="text" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:'bottom'" id="influenceDegreeName" value="<#if eventCase.influenceDegreeName??>${eventCase.influenceDegreeName}</#if>" />
							</td>
				    	</tr>
				    	<tr>
					        <td>
						        <label class="LabName"><span>信息来源：</span></label>
						        <input type="hidden" id="source" name="source" value="${eventCase.source!}" />
								<input type="text" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:'bottom'" id="sourceName" value="${eventCase.sourceName!}" />
				    		</td>
				        </tr>
				    	<tr>
				    		<td>
					    		<label class="LabName"><span>紧急程度：</span></label>
					    		<input type="hidden" id="urgencyDegree" name="urgencyDegree" value="${eventCase.urgencyDegree!}" />
								<input type="text" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:'bottom'" id="urgencyDegreeName" value="${eventCase.urgencyDegreeName!}" />
				    		</td>
				    	</tr>
				    	<tr>
				    		<td>
					    		<label class="LabName"><span>涉及人数：</span></label>
					    		<input type="hidden" id="involvedNum" name="involvedNum" value="${eventCase.involvedNum!}" />
								<input type="text" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:'bottom'" id="involvedNumName" value="${eventCase.involvedNumName!}" />
							</td>
				    	</tr>
				    	<tr>
				    		<td style="border-bottom:none;">
				    			<label class="LabName"><span>联系人员：</span></label><input  id="contactUser" name="contactUser" type="text" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:'bottom',validType:['maxLength[30]','characterCheck']" value="${eventCase.contactUser!}" />
				    		</td>
				    	</tr>
				    	<tr>
				    		<td style="border-bottom:none;">
				    			<label class="LabName"><span>联系电话：</span></label><input name="tel" id="tel" type="text" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:'bottom',validType:'mobileorphone'" value="${eventCase.tel!}" />
				    		</td>
				    	</tr>
				    </table>
				</div>
				<div class="clear" style="border-top:1px dotted #cecece;">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr id="adviceTr" class="hide">
							<td colspan="2" class="LeftTd">
								<label class="LabName"><span>办理意见：</span></label><textarea rows="3" style="height:80px;" id="advice" name="advice" class="area1 easyui-validatebox fast-reply" data-options="required:true,tipPosition:'bottom',validType:['maxLength[2048]','characterCheck']"></textarea>
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
				    			<label class="LabName"><span>涉及人员：</span></label>
					    		<input type="hidden" name="involvedPeople.eventInvolvedPeople" id="eventInvolvedPeople" value="${eventInvolvedPeople!}" />
					    		<input type="hidden" name="involvedPersonName" id="involvedPersion" value="<#if eventCase.involvedPersonName??>${eventCase.involvedPersonName}</#if>" />
					        	<div class="addinfo" style="width:667px;">
					            	<code class="fl FontDarkBlue" onclick="showInvoledPeopleSelector();"><a href="#">点击添加人员</a></code>
					            	<div id="involvedPeopleName">
					            		<#if involvedPeople?? >
					            			<#list involvedPeople as l>
									    		<p title="<#if l.name??>${l.name}</#if>(<#if l.idCard??>${l.idCard}</#if>)"><#if l.name??>${l.name}</#if><img src='${rc.getContextPath()}/images/sys1_29.png' onclick='removeInvolvedPeople("<#if l.name??>${l.name}</#if>","<#if l.idCard??>${l.idCard}</#if>", $(this).parent());'/></p>
									    	</#list>
					            		</#if>
					            	</div>
					            </div>
				    		</td>
						</tr>
				    	<tr>
				    		<td class="LeftTd">
				    			<label class="LabName"><span>图片上传：</span></label><div id="bigFileUploadDiv"></div>
				    		</td>
				    	</tr>
					</table>
				</div>
			</div>
		</div>
		
		<div class="BigTool">
			<div class="BtnList">
        		<a href="###" onclick="showAdvice(false, false);" class="BigNorToolBtn SaveBtn">保存</a>
        		<a href="###" onclick="showAdvice(true, false);" class="BigNorToolBtn BigShangBaoBtn">提交</a>
        		<a href="###" onclick="showAdvice(true, true);" class="BigNorToolBtn BigJieAnBtn">结案</a>
				<a href="###" onclick="cancel();" class="BigNorToolBtn CancelBtn">取消</a>
            </div>
        </div>	
        </div>
	</form>
</div>
	<#include "/component/involvedPeopleSelector.ftl">
	<iframe id="crossOverIframe" name="crossOverIframe" src="" style="display:none;" ></iframe>
</body>

<script type="text/javascript">
	var _winHeight = 0;
	var _winWidth = 0;
	
	$(function(){
		_winHeight = $(window).height();
		_winWidth = $(window).width();
		
		var options = {
			 axis : "yx",
			 theme : "minimal-dark" 
		}; 
		enableScrollBar('content-d',options);
		
		$("#norFormDiv").width($(window).width());
		
		var caseId = $("#caseId").val(),
		    bigFileUploadOpt = {
		    	useType: 'add',
		    	fileExt: '.jpg,.gif,.png,.jpeg',
		    	attachmentData: {'attachmentType':'${attachmentType!}', 'eventSeq': 1},
		    	module: 'zhsq_event_case',
		    	individualOpt : {
		    		isUploadHandlingPic : true
		    	}
		    };
	    	
		if(caseId) {
			bigFileUploadOpt["useType"] = 'edit';
			bigFileUploadOpt["attachmentData"].bizId = caseId;
			bigFileUploadOpt["attachmentData"].eventSeq = '1,2,3';
		}
		
		bigFileUpload_initFileUploadDiv('bigFileUploadDiv', bigFileUploadOpt);
		
		AnoleApi.initTreeComboBox("typeName", "type", "${bigTypePcode!}");
		
		AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId, items) {
			if(isNotBlankParam(items) && items.length > 0) {
				var grid = items[0];
				$("#infoOrgCode").val(grid.orgCode);
			}
		});
		
		AnoleApi.initListComboBox("influenceDegreeName", "influenceDegree", "${influenceDegreePcode}", null, [<#if eventCase.influenceDegree??>"${eventCase.influenceDegree}"<#else>"01"</#if>]);
		AnoleApi.initListComboBox("urgencyDegreeName", "urgencyDegree", "${urgencyDegreePcode}", null, [<#if eventCase.urgencyDegree??>"${eventCase.urgencyDegree}"<#else>"01"</#if>]);
		AnoleApi.initListComboBox("involvedNumName", "involvedNum", "${involvedNumPcode}", null, [<#if eventCase.involvedNum??>"${eventCase.involvedNum}"<#else>"00"</#if>]);
		AnoleApi.initListComboBox("sourceName", "source", "${sourcePcode}", null, [<#if eventCase.source??>"${eventCase.source}"<#else>"01"</#if>]);
		
		$("#advice").width($(window).width() * 0.85);
		
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
	
	function tableSubmit(isStart, isClose){
		var isValid =  $("#tableForm").form('validate');
		
		$("#tableForm").attr("action","${rc.getContextPath()}/zhsq/eventCase/saveEventCase.jhtml");
		
		if(isValid) {
			isValid = checkAttachmentStatus4BigFileUpload('bigFileUploadDiv');
		}
		
		if(isValid) {
			modleopen();
			
			$("#isStart").val(isStart);
			$("#isClose").val(isClose);
			
			var attachmentId = "";
			
			$('#tableForm input[name="attachmentId"]').each(function() {
				attachmentId += "," + $(this).val();
			});
			
			if(attachmentId.length > 0) {
				attachmentId = attachmentId.substr(1);
			}
			
			$("#attachment_attachmentIds").val(attachmentId);
			
			$("#tableForm").ajaxSubmit(function(data) {
		  		modleclose();
	  				
		  		if(data.success && data.success == true) {
	  				var caseId = $("#caseId").val() || data.caseId,
	  					isCurrent = caseId && caseId > 0;
	  				
	  				if(isStart && !isClose) {
	  					parent.searchData();
	  					parent.detail(caseId, "2");
	  					if(typeof(parent.closeBeforeMaxJqueryWindow) == 'function') {
	  						parent.closeBeforeMaxJqueryWindow();
	  					}
	  				} else {
	  					if(isClose) {
	  						data.tipMsg = "结案成功！";
	  					}
	  					parent.reloadDataForSubPage(data.tipMsg, isCurrent);
	  				}
		  		} else {
		  			if(data.tipMsg) {
		  				$.messager.alert('错误', data.tipMsg, 'error');
		  			} else {
		  				$.messager.alert('错误', '操作失败！', 'error');
		  			}
		  		}
			});
		}
	}
	
	function cancel(){
		parent.closeMaxJqueryWindow();
	}
	
	function showAdvice(isStart, isClose) {
		if(isClose && isClose == true) {
			$('#advice').validatebox({
				required: true
			});
			
			$("#adviceTr").show();
		} else {
			$("#adviceTr").hide();
			
			$('#advice').validatebox({
				required: false
			});
			
			$('#advice').val("");
		}
		
		tableSubmit(isStart, isClose);
	}
	
	/*
		地图标注改造 2015-06-29，参考geo项目行政区划地图标注
		markerOperation:地图标注操作类型
						 0表示添加
						 1表示编辑
						 2表示查看
		isEdit:是否是编辑状态
	*/
	function showMap(){
		var callBackUrl = '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toArcgisCrossDomain.jhtml',
			width = 480,
			height = 360,
			gridId = $("#gridId").val(),
			markerOperation = $('#markerOperation').val(),
			mapType = $("#module").val(),
			isEdit = true,
			parameterJson = {
				"id": $("#id").val(),
				"name": $("#name").val()
			};
		
		showMapWindow(gridId,callBackUrl,width,height,isEdit,mapType,parameterJson,mapType);
	}
	
	$(window).resize(function(){
		var winHeight = $(window).height();
		var winWidth = $(window).width();
		
		if(winHeight != _winHeight || winWidth != _winWidth) {
			location.reload();
		}
    });
</script>

</html>
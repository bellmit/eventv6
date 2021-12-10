<!DOCTYPE html>
<html>
<head>
<meta http-equiv="x-ua-compatible" content="ie=8" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><@block name="pageTitle">事件采集</@block></title>
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
<#include "/component/commonFiles-1.1.ftl" />
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/scripts/updown/swfupload/css/swfupload.css" />
	<script type="text/javascript" src="${rc.getContextPath()}/scripts/updown/swfupload/swfupload.js"></script>
	<script type="text/javascript" src="${rc.getContextPath()}/scripts/updown/swfupload/handlers.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/plugIn/plug_in.js"></script>
<script type="text/javascript" src="${COMPONENTS_URL}/js/fastreply/fastReply.js" defer="true"></script>
<@block name="extraJs"></@block>

<style type="text/css">
	.LabName{height:30px;}
	.ImgUpLoad .upload_table {
	 width:70%;
	}
</style>
<#include "/component/ComboBox.ftl" />
</head>
<body>
<div>
	<form id="tableForm" name="tableForm" action="" method="post" enctype="multipart/form-data">
	
	
	
	  <!--  <input type="hidden" id="attachmentId" name="attachmentId" value="${att.attachmentId!}" /> -->
	   
	     <input type="hidden" id="attachmentIds" name="attachmentIds" value="" />
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
		
		<div style="margin: 0 auto; background-color:#F9F9F9; position:relative;">
			<div id="content-d" class="MC_con content light" style="overflow-x:hidden">
				<div id="norFormDiv" class="NorForm" style="width:784px;">
					<div class="fl" style="width:67%;">
					<h3 style="margin-top: 10px;margin-left: 10px">事件上报</h3>
				<!-- 	  <div style="text-align: center;"> 
					 <img style="margin-right: 80px;"  height="130px" src="${imgDomain!}${att.filePath}" alt="视频截图" />   
					  </div>	 -->
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
						 
					    	<tr>
					    		<td  class="LeftTd">
					    			<label class="LabName"><span><label class="Asterik">*</label>监控名称：</span></label><input type="text" class="inp1 easyui-validatebox"  data-options="required:true,tipPosition:'bottom',validType:['maxLength[100]','characterCheck']" name="eventName" id="eventName" value="${event.eventName!}" />
					    		</td>
					    	</tr>
					    	
					    	<tr>
								<td  >
									<label class="LabName"><span><label class="Asterik">*</label>事件分类：</span></label><input type="text" class="inp1 InpDisable easyui-validatebox" style="width:155px;" data-options="required:true" id="typeName" name="typeName" maxlength="100" value="<#if event.typeName??>${event.typeName}<#else><#if event.eventClass??>${event.eventClass}</#if></#if>" />
								</td>
							</tr>
					    	<tr>
					    		<td  class="LeftTd">
					    			<label class="LabName"><span><label class="Asterik">*</label>事发时间：</span></label><input type="text" id="happenTimeStr" name="happenTimeStr" class="inp1 Wdate easyui-validatebox" style="width:155px; cursor:pointer;" data-options="required:true" onclick="WdatePicker({readOnly:true, maxDate:'${(event.happenTimeStr!maxHappenTime)?substring(0,10)} 23:59:59', dateFmt:'yyyy-MM-dd HH:mm:ss', isShowClear:false, isShowToday:false})" value="${event.happenTimeStr!}" readonly="readonly"></input>
					    		</td>
					    	</tr>
					    	<tr>
					    		<td class="LeftTd" style="border-bottom:none;">
					    			<label class="LabName"><span><label class="Asterik">*</label>事件描述：</span></label><textarea name="content" id="content" cols="" rows="" class="area1 easyui-validatebox" style="height:64px;resize: none;" data-options="required:true,tipPosition:'bottom',validType:[<#if characterLimit??>'minLength[${characterLimit?c}]',</#if>'maxLength[1024]','characterCheck']" >${event.content!}</textarea>
						        </td>
					    	</tr>
					    	<tr>
					    		<td class="LeftTd" style="border-bottom:none;">
					    			<label class="LabName"><span>附件：</span></label>
						      	    <div id="fileupload" class="ImgUpLoad" style="padding-top:4px;"></div>
						        </td>
					    	</tr>
					    </table>
					</div>
					

				</div>
			</div>
			
			<div class="BigTool">
				<div class="BtnList">
	        		<a href="javascript:$(window.parent.document).find('.event_con').hide();"  class="BigNorToolBtn CancelBtn">取消</a>
	        		<#if (isReport?? && isReport)>
	            	<a href="###" onclick="showAdvice('saveEventAndReport', null, '0');" class="BigNorToolBtn BigShangBaoBtn">提交</a>
	            	<#else>
	        		<a href="###" onclick="showAdvice('saveEventAndStart', 'startWorkFlow', '0');" class="BigNorToolBtn BigShangBaoBtn">提交</a>
	        		</#if>
	            </div>
	        </div>	
		</div>
	</form>
</div>
	<iframe id="crossOverIframe" name="crossOverIframe" src="" style="display:none;" ></iframe>
	<#include "/component/involvedPeopleSelector.ftl">
	<#include "/component/maxJqueryEasyUIWin.ftl" />
</body>

<script type="text/javascript">
	$(function() {
		fileUpload({ 
			positionId:'fileupload',//附件列表DIV的id值',
			type:'add',//add edit detail
			initType:'ajax',//ajax、hidden编辑表单时获取已上传附件列表方式
			context_path:'${rc.getContextPath()}',
			ajaxData: {'eventSeq':1}//未处理
		});
        
        var typesDictCode = "${typesDictCode!}";
        if(isNotBlankString(typesDictCode) && typesDictCode!="null") {
        	AnoleApi.initTreeComboBox("typeName", "type", { 
        		"${bigTypePcode!}" : [${typesDictCode!}] 
        	}, null, null, {//0 展示指定的字典；1 去除指定的字典；
				FilterType : "<#if isRemoveTypes?? && isRemoveTypes>1<#else>0</#if>"
        	});
        } else {
        	AnoleApi.initTreeComboBox("typeName", "type", "${bigTypePcode!}");
        }
        
        <#if rootGridId?? && (rootGridId > 0)>
        	AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId, items) {
        		if(isNotBlankParam(items) && items.length > 0) {
        			var grid = items[0];
        			$("#gridCode").val(grid.orgCode);
        		}
        	}, {
        		Async : {
        			enable : true,
        			autoParam : [ "id=gridId" ],
        			dataFilter : _filter,
        			otherParam : {
        				"startGridId" : ${rootGridId?c}
        			}
        		}
        	});
        <#else>
        	AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId, items) {
        		if(isNotBlankParam(items) && items.length > 0) {
        			var grid = items[0];
        			$("#gridCode").val(grid.orgCode);
        		}
        	});
        </#if>
        
        
        <#if event.resMarker??>
        	var resMarkerX = "${event.resMarker.x!}",
        		resMarkerY = "${event.resMarker.y!}",
        		resMarkerMapType = "${event.resMarker.mapType!}";
        		
        	if(resMarkerX && resMarkerY && resMarkerMapType) {
        		callBackOfData(resMarkerX, resMarkerY, null, resMarkerMapType);
        	}
        </#if>
		
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
			
			$('#advice').val("");
			
			tableSubmit(m, callback, toClose);
		}
	}
		
	function tableSubmit(m, callback, toClose) {
		var isValid =  $("#tableForm").form('validate'),
			advice = $("#advice").val(),
			isAdviceVisible = $("#adviceDiv").is(":visible");
		
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
						if(iframeUrl.indexOf('?') != -1) {
							iframeUrl += "&";
						} else {
							iframeUrl += "?";
						}
						
						data.isCrossDomain = true;
						iframeUrl += "callBack=" + outerCallBack + "&callBackParams="+ JSON.stringify(data);
						$("#crossOverIframe").attr("src", iframeUrl);
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
							if(iframeUrl.indexOf('?') != -1) {
								iframeUrl += "&";
							} else {
								iframeUrl += "?";
							}
							
							data.isCrossDomain = true;
							data.eventId = formId;
							
							iframeUrl += "callBack=" + outerCallBack + "&callBackParams="+ JSON.stringify(data);
							$("#crossOverIframe").attr("src", iframeUrl);
						} else {
							var url = "${rc.getContextPath()}/zhsq/event/eventDisposalController/detailByVideo.jhtml?eventType=todo&instanceId="+instanceId+"&workFlowId="+new_workFlowId+"&eventId="+formId+"&cachenum=" + Math.random();
							if(outerCallBack) {
								if(toClose == '0') {
									window.location = url;
								}
								eval(outerCallBack)(data);
							} else {
								if(toClose == '0') {
									window.location.href = url;
									//showMaxJqueryWindow("事件办理", url, null, null, null, null, flashData);
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
	
	function showMap(){//地图展示
		var callBackUrl = '${SQ_ZHSQ_EVENT_URL}/zhsq/map/arcgis/arcgis/toArcgisCrossDomain.jhtml';
		var width = 480;
		var height = 360;
		var gridId = $("#gridId").val();
		var markerOperation = $('#markerOperation').val();
		var id = $('#eventId').val();
		var mapType = 'EVENT_V1';
		var isEdit = true;
		showMapWindow(gridId,callBackUrl,width,height,isEdit,mapType);
	}
	
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
		$("#tableForm").attr("action",'${rc.getContextPath()}/zhsq/event/eventDisposalController/toAddEventByMenu.jhtml?eventJson={isReport:false}');
		if(isNotBlankString(msg)) {
			$.messager.alert('', msg, 'info',function(){
				$("#tableForm").submit();
			});
		} else {
			$("#tableForm").submit();
		}
	}
	/**
	 * 添加图片列表
	 * @param result
	 * 			isFromQueued	//true表示文件来自队列，否则表示文件来自外部
	 * @param type
	 */
	function addImgList(result, type) {
		var showPattern = result.showPattern;
		var imgStr = "bmp,doc,docx,txt,xls,tif,rtf,xml,ppt,ppx,png,pdf,jpg,gif,amr,mp3";
		var suffix = getFileSuffix(result.fileName);
		if(imgStr.indexOf(suffix)<0){
			suffix = "epub";
		}
		
		var row = new StringBuffer();
		var eventSeqName = capEventSeqName(result);

		if(showPattern == 'list' || showPattern == 'all') {
			row.append('<tr ');
		} else {
			row.append('<tr class="hide" ');
		}
		row.append(' id="').append(result.fileId).append('" type="').append(result.type)
		   .append('" init_type="').append(result.initType).append('"')
		   .append(' path="').append(result.filePath+'">');
		row.append('<td style="width: 30px;" align="center">');
		row.append('<img style="margin-top:3px;" src="').append(default_config.script_path).append('images/icon/icon_').append(suffix).append('.gif" width="16" height="16" />');
		row.append('</td>');
		row.append('<td align="left">');
		if(eventSeqName!=undefined && eventSeqName!=""){
			row.append('<label class="FontDarkBlue">').append('['+eventSeqName+']').append('</label>');
		}
		row.append(displayFileName(result.fileName)).append('</td>');
		//row.append('<td width="50" align="center">').append(result.fileSize).append('</td>');
		row.append('<td align="center">');
		
		if(!result.isFromQueued) {//为了让传递attachmentId的附件也能正常关联上业务id
			var fileType = 'fileType';
			row.append("<input type='hidden' id='attachmentId' name='attachmentId' value='").append(result.fileId).append("'/>");
			row.append("<input type='hidden' id='filePath' name='filePath' value='").append(result.filePath).append("'/>");
			row.append("<input type='hidden' id='fileType' name='fileType' value='").append(fileType).append("'/>");
			row.append("<input type='hidden' id='fileSize' name='fileSize' value='").append(formatFileSize(result.fileSize)).append("'/>");
		}
		row.append('</td>');
		row.append('<td style="width: 100px;" align="center">');
		if(result.type != 'detail' && type==null){//文件详情无删除图片
			row.append('<img src="').append(default_config.script_path).append('images/icon/folder_del.gif" width="16" height="16" ')
			   .append('style="cursor:pointer;vertical-align:middle;"')
			   .append(' onclick="deleteFile(').append(result.fileId).append(',\'').append(result.cancel_button).append('\',\'').append(result.table).append('\');" alt="删除文件" title="删除"/>&nbsp;');

		} 
		if('add' != result.type){
			row.append('<a href="').append(result.download_url).append('&attachmentId=').append(result.fileId).append('" target="_blank" >')
			   .append('<img src="').append(default_config.script_path).append('images/icon/down.png" width="16" height="16"')
			   .append(' style="vertical-align:middle;" alt="查看文件" title="查看" /></a>');		 
		}
		row.append('</td>');
		row.append('</tr>');
		
		$("#"+result.table).append(row.toString());
		$("#"+result.table).removeClass("hide");
	}

	/**
	 * 文件上传成功
	 * @param file
	 * @param serverData 服务端返回的数据
	 */
	function uploadSuccess(file, serverData) {
		
		try {
			var data = $.parseJSON(serverData);
			file.attachmentId = data.attachmentId;
			file.filePath = data.filePath;
			
			var row = document.getElementById(file.id);
			var _fileState = new StringBuffer();
			
			var cancel_button = this.customSettings.cancel_upload;
			var upload_table = this.customSettings.upload_table;
			
			var fileType = this.customSettings.fileType;
			_fileState.append("<input type='hidden' id='attachmentId' name='attachmentId' value='").append(file.attachmentId).append("'/>");
			_fileState.append("<input type='hidden' id='filePath' name='filePath' value='").append(file.filePath).append("'/>");
			_fileState.append("<input type='hidden' id='fileType' name='fileType' value='").append(fileType).append("'/>");
			_fileState.append("<input type='hidden' id='fileSize' name='fileSize' value='").append(formatFileSize(file.size)).append("'/>");
			//_fileState.append("文件上传完成");
		

			_fileState.append('<img src="').append(default_config.script_path).append('images/icon/folder_del.gif" width="16" height="16" ')
			   .append('style="cursor:pointer;vertical-align:middle;"')
			   .append('id=img_').append(file.attachmentId)
			   .append(' onclick="deleteFile(').append(file.attachmentId).append(',\'').append(cancel_button).append('\',\'').append(upload_table).append('\');" alt="删除文件" title="删除"/>&nbsp;');
			
			_fileState.append('<a href="').append(this.customSettings.download_url)
					 .append('&attachmentId=').append(file.attachmentId).append('" target="_blank">');
			_fileState.append('<img src="').append(default_config.script_path);
			_fileState.append('images/icon/down.png" width="16" height="16" style="vertical-align:middle;" alt="查看文件" title="查看"/></a>');
			$(row.cells[3]).append(_fileState.toString());
			
			$("#img_"+file.attachmentId).closest("tr").attr("id", file.attachmentId);//为了使得新增的图片的删除功能和取消上传功能有效  20140705
			$("#img_"+file.attachmentId).closest("tr").attr("path", file.filePath);
			
			var imgStr = "png,jpg,gif,jpeg",		//可查看缩略图的附件类型
				amrStr = "amr",
				audioStr = "amr,mp3",				//音频附件类型
				suffix = getFileSuffix(file.name),
				thumbImgSrc = "",
				downloadUrl = this.customSettings.download_url+'&attachmentId='+file.attachmentId;
			
			if(imgStr.indexOf(suffix) >= 0) {
				thumbImgSrc = this.customSettings.imgDomain+"/"+file.filePath;
			} else if(audioStr.indexOf(suffix) >= 0) {
				thumbImgSrc = default_config.script_path + 'images/thumbnail/audio.jpg';
				downloadUrl = this.customSettings.download_video_url +'&attachmentId='+file.attachmentId + '&videoType=2';
			}
			
			$("#"+file.id+"_thumbImg").attr("src", thumbImgSrc);//修改缩略图的文件路径
			$("#"+file.id+"_thumbClose").attr("onclick", "deleteFile("+file.attachmentId+",'"+cancel_button+"','"+upload_table+"')");//修改缩略图删除图片操作
			$("#"+file.id+"_thumbAnchor").attr("href", downloadUrl);//修改缩略图查看原图连接
			$("#"+file.id+"_thumb").attr("id", file.attachmentId+"_thumb");//修改缩略图的a标签的id
			$("#"+file.id+"_thumbSpan").attr("id", file.attachmentId+"_thumbSpan");//修改删除图标的span的id
			
			$("#fileNum_"+cancel_button).val(parseInt($("#fileNum_"+cancel_button).val())+1);//上传成功后 +1
		} catch (ex) {
			this.debug(ex);
		}
	}
</script>
</html>
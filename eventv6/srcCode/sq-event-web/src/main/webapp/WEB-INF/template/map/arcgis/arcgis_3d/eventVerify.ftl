<!DOCTYPE html>
<html style="overflow:hidden">
<head>
<meta http-equiv="x-ua-compatible" content="ie=8" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><@block name="pageTitle">事件审核</@block></title>
    <link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
	<#include "/component/commonFiles-1.1.ftl" />
	
    <link rel="stylesheet" href="${uiDomain!''}/web-assets/common/css/reset.css">
    <link rel="stylesheet" href="${uiDomain!''}/web-assets/_big-screen/nanchang-cc/css/index-mask.css">	
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/scripts/updown/swfupload/css/swfupload.css" />
	<script type="text/javascript" src="${rc.getContextPath()}/scripts/updown/swfupload/swfupload.js"></script>
	<script type="text/javascript" src="${rc.getContextPath()}/scripts/updown/swfupload/handlers.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/plugIn/plug_in.js"></script>
<script type="text/javascript" src="${COMPONENTS_URL}/js/fastreply/fastReply.js" defer="true"></script>
<@block name="extraJs"></@block>
<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
<style type="text/css">
	.LabName{height:30px;}
	.ImgUpLoad .upload_table {
	 width:70%;
	}
	.LabName{
	color:  #b2cdff;
	}
	.BigTool{
	background: transparent;
	}
	.BigShangBaoBtn{
		background-color: rgba(0, 160, 233, 0.4);
		color: #4ce7ff;
	}
	td{
	    border-bottom:none !important;   
	}
	

.ztree * {
    padding: 0;
    margin: 0;
    font-size: 12px;
    font-family: Verdana, Arial, Helvetica, AppleGothic, sans-serif;
    color: #fff !important;
}
.inpt1_bg {
    background-color: #084bae !important;
    border-radius: 0.04rem ;
    border: solid 1px #2e7bec;
    color: #fff !important;
}
.FontDarkBlue {
    color: #fff;
}	
.vv{
    width: 1.2rem;
    height: 0.38rem;
    line-height: 0.38rem;
    color: #4ce7ff;
    background-color: rgba(0, 160, 233, 0.2);
    border-radius: 0.19rem;
    font-size: 0.18rem;
    text-align: center;
    float: left;
    margin: 0.1rem;
    padding: 0 12px 0 35px;
    cursor: pointer;	
    }
    .BigNorToolBtn{
    display: inline-block;
    float: left;
    height: 32px;
    margin: 0 10px;
    padding: 0 12px 0 35px;
    font-family: Microsoft YaHei;
    font-size: 18px;
    line-height: 32px;
    color: #fff;
    text-align: center;
    border-radius: 3px;
    background-repeat: no-repeat;
    background-color: #2980B9;
    background-position: 12px 8px;
    transition: all 0.5s;
    }
</style>
<#include "/component/ComboBox.ftl" />
</head>
<body style="background: transparent;">
<div>
	<form id="tableForm" name="tableForm" action="" method="post" enctype="multipart/form-data" style="margin-left:30px">
	    <input type="hidden" id="eventVerifyId" name="eventVerifyId" value="<#if eventWechat.eventVerifyId??>${eventWechat.eventVerifyId?c}</#if>"/>
		<input type="hidden" id="verifyType" name="verifyType" value="${verifyType!}" />
		
		<!--用于地图-->
		<input type="hidden" id="id" value="<#if eventWechat.eventVerifyId??>${eventWechat.eventVerifyId?c}</#if>" />
		<input type="hidden" id="module" value="${markerType!}"/>
		<input type="hidden" id="markerOperation" value="${markerOperation!}"/>
		<input type="hidden" id="dataJsonMap" name="dataJsonMap" value="" />
		
		
 		<div style="margin: 0 auto; position:relative;">
			<div id="content-d" class="MC_con content light" style="overflow-x:hidden;height: 360px;margin-top: 40px;">
				<div id="norFormDiv" class="NorForm" style="width:784px;">
					<div id="tablediv" class="fl" style="width:40rem;margin-left:1.5rem">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
						    <tr>
								<td <#if eventWechat.isShow2Public??><#else>colspan="2"</#if> class="LeftTd">
									<label class="LabName"><span>事件标题：</span></label><div class="Check_Radio FontDarkBlue <#if eventWechat.isShow2Public??>singleCellClass<#else>doubleCellClass</#if>">${eventWechat.eventName!''}</div>
								</td>
								<#if eventWechat.isShow2Public??>
							    <td class="LeftTd">
			    				<#if eventWechat.isShow2Public == '1'>
			    					<label class="LabName"><span></span></label>
			    					<div class="Check_Radio"><span><input type="checkbox" id="isShow2Public" name="isShow2Public" class="dataJsonMap" onclick="isShow2PublicCheck(this);" value="${eventWechat.isShow2Public}" checked /><label for="isShow2Public" style="cursor:pointer">&nbsp;公开事件</label></span></div>
			    				<#elseif eventWechat.isShow2Public == '0'>
			    					<label class="LabName"><span>公开事件：</span></label><div class="Check_Radio FontDarkBlue">否</div>
			    				</#if>
					            </td>
					            </#if>
							</tr>
							<tr>
					    		<td class="LeftTd" >
									<label class="LabName"><span>所属网格：</span></label>
									<input type="hidden" id="gridId" value="<#if gridId??>${gridId?c}</#if>">
									<input type="hidden" id="infoOrgCode" name="infoOrgCode" value="${eventWechat.infoOrgCode!}" />
									<input type="text" class="inp1 easyui-validatebox" style="width:122px;" data-options="required:true,tipPosition:'bottom',validType:'characterCheck'" id="gridName" value="${gridName!''}" />
				        	    </td>
				    			<td class="LeftTd" width="300px;">
									<label class="LabName"><span>事发时间：</span></label><div class="Check_Radio FontDarkBlue">${eventWechat.happenTimeStr!''}</div>
								</td>
					    	</tr>
							<tr>
					    		<td colspan="2" class="LeftTd">
								    <label class="LabName"><span>事发地址：</span></label><div><#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/><div class="Check_Radio doubleCellClass FontDarkBlue">${eventWechat.occurred!''}&nbsp;<div></div>
				        	    </td>
					    	</tr>
							<tr>
								<td colspan="2" class="LeftTd">
								    <label class="LabName"><span>事件内容：</span></label><div style="width:29rem" class="Check_Radio areaClass FontDarkBlue">${eventWechat.content!''}</div>
				        	    </td>
					    	</tr>
					    
					    	
					    	
				    	<tr>
				    		<td class="LeftTd">
								<label class="LabName"><span>上报人员：</span></label><div class="Check_Radio FontDarkBlue">${eventWechat.contactUser!}</div>
							</td>
							<td class="LeftTd">
								<label class="LabName"><span>联系电话：</span></label><div class="Check_Radio FontDarkBlue">${eventWechat.tel!}</div>
							</td>
				    	</tr>
				    	
					    	<tr>
					    		<td class="LeftTd" style="border-bottom:none;"  colspan="2">
					    			<label class="LabName"><span>相关附件：</span></label><div class="ImgUpLoad" id="fileupload"></div>
						        </td>
					    	</tr>
					    	<tr>
					    		<td colspan="2" class="LeftTd" style="border-bottom:none;">
					    			<label class="LabName"><span><label class="Asterik">*</label>办理意见：</span></label><textarea name="remark" id="remark" cols="" rows="" class="area1 easyui-validatebox inpt1_bg" style="width:29rem; height:64px;resize: none;" data-options="required:true,tipPosition:'bottom',validType:[<#if characterLimit??>'minLength[${characterLimit?c}]',</#if>'maxLength[1024]','characterCheck']" ><#if eventWechat.remark??>${eventWechat.remark}</#if></textarea>
						        </td>
					    	</tr>
					    </table>
					</div>
					

				</div>
			</div>
			
			
		</div>  
	</form>
</div>
	<#include "/component/involvedPeopleSelector.ftl">
	<#include "/component/maxJqueryEasyUIWin.ftl" />
</body>

<#include "/map/arcgis/arcgis_3d/eventWechat_js.ftl" />
<script type="text/javascript">
	var u = navigator.userAgent, app = navigator.appVersion;   
	var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android终端或者uc浏览器
	
	$(function() {
		if(isAndroid){ 
		//if(navigator.userAgent.match("ua_ffcs")){
			$('#tablediv').css('margin-left','0rem');			
		}else{
			$('#tablediv').css('margin-left','2.0rem');			
		}
	
	    AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId, items) {
				if(isNotBlankParam(items) && items.length > 0) {
					var grid = items[0];
					$("#infoOrgCode").val(grid.orgCode);
				} 
			}, {
				Async : {
					enable : true,
					autoParam : [ "id=gridId" ],
					dataFilter : _filter,
					otherParam : {
						"startGridId" : <#if startGridId??>${startGridId?c}</#if>
					}
				}
		});
		
	});
	
	function tableSubmit(operateType) {
		    var isValid = $("#tableForm").form('validate');
		    
			if(isValid){
				modleopen();
				
				var dataJsonMap = {};
				$('#tableForm .dataJsonMap').each(function() {
					dataJsonMap[$(this).attr('name')] = $(this).val();
				});
				
				$('#dataJsonMap').val(JSON.stringify(dataJsonMap));
				
				$("#tableForm").attr("action","${rc.getContextPath()}/zhsq/eventWechat/saveEventWechat.jhtml");
	      	
			  	$("#tableForm").ajaxSubmit(function(data) {
	  				modleclose();
	  				
	  				if(data.success && data.success == true) {
	  					switch(operateType) {
	  						case 0: {
	  							report4Event(); break;
	  						}
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
		
		function setInvalid() {
		    
		    $.messager.confirm('提示', '您确定作废该记录吗？', function(isCancel) {
	  			if(isCancel) {
	  			    modleopen();
	  			    $.ajax({
				        type: "POST",
	    		        url : '${rc.getContextPath()}/zhsq/eventWechat/invalidEventVerify.json',
				        data: {'eventVerifyId': $("#id").val()},
				        dataType:"json",
				        success: function(data){
					    	modleclose();
					
					    	if(data.success && data.success == true){
			  					parent.reloadDataForSubPage('作废成功', true);
			  				} else {
			  					if(data.tipMsg) {
			  						$.messager.alert('错误', '作废成功！', 'error');
			  					} else {
			  						$.messager.alert('错误', '作废失败！', 'error');
			  					}
			  				}
				    	},
				    	error:function(data){
					    	modleclose();
					
					    	$.messager.alert('错误','事件作废失败 ！','error');
				    	}
	    			});
	  			
	  			}
	  		});
		    
	    }
		
		function report4Event() {
			modleopen();
			
			$.ajax({
				type: "POST",
	    		url : '${rc.getContextPath()}/zhsq/eventWechat/fetchParam4Report.jhtml',
				data: {'eventVerifyId': $("#eventVerifyId").val(), 'verifyType': $('#verifyType').val()},
				dataType:"json",
				success: function(data){
					modleclose();
					
					reportEvent(data);
				},
				error:function(data){
					modleclose();
					
					$.messager.alert('错误','获取事件上报信息失败！','error');
				}
	    	});
		}
		
		function reportEvent(data) {
			var isReport = data.isReport,
				eventWechat = null,
				isShowCloseBtn = data.isShowCloseBtn,
				trigger = data.trigger,
				eventBizPlatform = '${eventWechat.eventBizPlatform!}',
				bizType = '${moduleCode!}';
			
			if(isBlankParam(isReport)) {
				isReport = true;
			}
			if(isBlankParam(isShowCloseBtn)) {
				isShowCloseBtn = false;
			}
			if(isBlankParam(trigger)) {
				trigger = '';
			}
			
			eventWechat = {
				"eventName" : '${eventWechat.eventName!}',
				"type" : '${eventWechat.eventType!}',
				"happenTimeStr" : '${eventWechat.happenTimeStr!}',
				"occurred" : '${eventWechat.occurred!}',
				"content" : '${eventWechat.content!}',
				"gridId" : $("#gridId").val(),
				"gridName" : $("#gridName").val(),
				"trigger" : trigger,
				"contactUser" : '${eventWechat.contactUser!}',
				"tel" : '${eventWechat.tel!}',
				"advice" : $("#remark").val(),
				"source": '${eventWechat.source!}',
				"isShowSaveBtn" : false,
				"isShowCloseBtn" : isShowCloseBtn,
				"isReport" : isReport,
				"callBack" : 'parent.reportCallBack',
				"outerAttachmentIds" : data.outerAttachmentIds || ''
			},
			longitude = '${eventWechat.longitude!}',
			latitude = '${eventWechat.latitude!}';
			
			if(longitude && latitude) {
				eventWechat.resMarker = {
					'x': longitude,
					'y': latitude,
					'mapt': $("#mapt").val() || ''
				};
			}
			
			if(isNotBlankParam(bizType)) {
				eventWechat.eventReportRecordInfo = {
					'bizId' : $("#eventVerifyId").val(),
					'bizType' : '${moduleCode!}'
				};
			}
			
			if(eventBizPlatform) {
				eventWechat.bizPlatform = eventBizPlatform;
			}
			
			reportEventWechat(eventWechat);
			
			if(typeof(parent.closeBeforeMaxJqueryWindow) === 'function') {//关闭审核窗口
				parent.closeBeforeMaxJqueryWindow();
			}
		}
		
		
	function reportEventWechat(eventWechat) {
		var event = eventWechat;
		
		//console.log(JSON.stringify(event));
		
		
		var reportForm = '<form id="_report4eventForm" name="_report4eventForm" action="" target="event_add_page" method="post">'+
						 '</form>';
		
		$("#jqueryToolbar",window.parent.document).append($(reportForm));
		$("#_report4eventForm",window.parent.document).append($('<input type="hidden" id="_reportEventJson" name="eventJson" value="" />'));
		
		$("#_reportEventJson",window.parent.document).val(JSON.stringify(event));
		$("#_report4eventForm",window.parent.document).attr('action', '${rc.getContextPath()}/zhsq/nanChang3D/toAddEventByMenu.jhtml');
		
		var str="<iframe id='event_add_page' name='event_add_page' class='mask-event' frameborder='0' width='100%' height='100%'></iframe>"
    
        $("#event_add",window.parent.document).html(str);
        
        $('.mae-event-handle',window.parent.document).removeClass('mae-container-on');
		$('.mae-event-detail',window.parent.document).removeClass('mae-container-on');
        $('#ascrail2000',window.parent.document).css('display','none');
        $('.mae-event-list',window.parent.document).removeClass('mae-container-on');
        $('.mae-event-add',window.parent.document).addClass('mae-container-on');
        
		$("#_report4eventForm",window.parent.document).submit();
		
		$("#_report4eventForm",window.parent.document).remove();
		
		
		
	  	
	}
		
		
		
		function isShow2PublicCheck(checkObj) {
			var isShow2Public = 0;
			
			if($(checkObj).is(':checked')) {
				isShow2Public = 1;
			}
			
			$(checkObj).val(isShow2Public);
		}
</script>
</html>
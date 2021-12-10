<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>微信事件-编辑</title>
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
	<#include "/component/bigFileUpload.ftl" />
	
	<style type="text/css">
		.singleCellClass{width:72%;}
		.doubleCellClass{width:80%;}
		.areaClass{width:85%;}
	</style>
</head>
<body>
	<#include "/component/ComboBox.ftl" />
	
	<form id="tableForm" name="tableForm" action="" method="post">
		<input type="hidden" id="eventVerifyHashId" name="eventVerifyHashId" value="${eventWechat.eventVerifyHashId!}"/>
		<input type="hidden" id="verifyType" name="verifyType" value="${verifyType!}" />
		<input type="hidden" id="gridLevel" name="gridLevel" value="${gridLevel!''}" />
		
		<!--用于地图-->
		<input type="hidden" id="id" value="<#if eventWechat.eventVerifyId??>${eventWechat.eventVerifyId?c}</#if>" />
		<input type="hidden" id="module" value="${markerType!}"/>
		<input type="hidden" id="markerOperation" value="${markerOperation!}"/>
		<input type="hidden" id="dataJsonMap" name="dataJsonMap" value="" />
		
		<div id="content-d" class="MC_con content light">
			<div id="eventWechatNorformDiv" class="NorForm" style="width:100%;">
				<div>
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
						
							<@block name="eventType"> 
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
					        </@block>
					        
						</tr>
						<tr>
							<td class="LeftTd" >
								<label class="LabName"><span>所属网格：</span></label>
								<input type="hidden" id="gridId" value="<#if gridId??>${gridId?c}</#if>">
								<input type="hidden" id="infoOrgCode" name="infoOrgCode" value="${eventWechat.infoOrgCode!}" />
								<input type="text" class="inp1 easyui-validatebox" style="width:122px;" data-options="required:true,tipPosition:'bottom',validType:'characterCheck'" id="gridName" value="${gridName!''}" />
				        	</td>
				    		<td class="LeftTd" width="50%;">
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
								<label class="LabName"><span>事件内容：</span></label><div class="Check_Radio areaClass FontDarkBlue">${eventWechat.content!''}</div>
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
				    		<td colspan="2" class="LeftTd">
				    			<label class="LabName"><span>相关附件：</span></label><div id="bigFileUploadDiv"></div>
				    		</td>
				    	</tr>
						<tr>
				    		<td colspan="2" class="LeftTd" style="border-bottom:none;">
				    			<label class="LabName"><span>办理意见：</span></label><textarea name="remark" id="remark" cols="" rows="" class="area1 easyui-validatebox" style="width:84%; height:64px;resize: none;" data-options="required:true,tipPosition:'bottom',validType:['maxLength[256]','characterCheck']" >${eventWechat.remark!}</textarea>
					        </td>
				    	</tr>
					</table>
				</div>
			</div>
		</div>
		
		<div class="BigTool">
			<div class="BtnList">
				<a href="###" onclick="tableSubmit(0);" class="BigNorToolBtn BigShangBaoBtn">上报</a>
				<a href="###" onclick="tableSubmit(1);" class="BigNorToolBtn RejectBtn">驳回</a>
				<#if isShowInvalid??>
				<a href="###" onclick="invalidEventVerify();" class="BigNorToolBtn RejectBtn">作废</a>
				</#if>
				<a href="###" onclick="closeWin();" class="BigNorToolBtn CancelBtn">取消</a>
			</div>
		</div>
	</form>
	
	<#include "/zzgl/intermediateData/eventWechat/eventWechat_js.ftl" />
	
	<script type="text/javascript">
		$(function(){
			var status = "${eventWechat.status!}";
			
			if(status && status != '01') {
				parent.reloadDataForSubPage('该记录已被审核！', true);
			}
			
			<@block name="initGridZtree">
			AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId, items) {
				if(isNotBlankParam(items) && items.length > 0) {
					var grid = items[0];
					$("#infoOrgCode").val(grid.orgCode);
					$("#gridLevel").val(grid.gridLevel);
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
			</@block>
			
			<@block name="eventTypeInit"></@block>
			$("#remark").width($(window).width() * 0.85);
			
			$(window).resize( function (){
			     location.reload();//页面大小变化时自动刷新页面
			});
	    });
	    
		function tableSubmit(operateType) {
		<@block name="checkLevel"></@block>
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
	  						case 1: {
	  							$.messager.confirm('提示', '您确定驳回该记录吗？', function(isReject) {
	  								if(isReject) {
	  									var backData = {
	  										'result' : true 
	  									};
	  									
	  									parent.reportCallBack(backData, operateType);
	  								}
	  							});
	  						}
	  					}
	  				} else {
	  					parent.searchData(true);
	  					
	  					if(data.tipMsg) {
	  						$.messager.alert('错误', data.tipMsg, 'error');
	  					} else {
	  						$.messager.alert('错误', '操作失败！', 'error');
	  					}
	  				}
				});
			}
		}
		
		function invalidEventVerify() {
		    
		    $.messager.confirm('提示', '您确定作废该记录吗？', function(isCancel) {
	  			if(isCancel) {
	  			    modleopen();
	  			    $.ajax({
				        type: "POST",
	    		        url : '${rc.getContextPath()}/zhsq/eventWechat/invalidEventVerify.json',
				        data: {'eventVerifyHashId': $("#eventVerifyHashId").val()},
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
				data: {'eventVerifyHashId': $('#eventVerifyHashId').val(), 'verifyType': $('#verifyType').val()},
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
			var isReport = "${isReport!}" || data.isReport,
				eventWechat = null,
				isShowCloseBtn = "${isShowCloseBtn!}" || data.isShowCloseBtn,
				trigger = data.trigger,
				eventBizPlatform = '${eventWechat.eventBizPlatform!}',
				bizType = '${moduleCode!}';
				
			<@block name="eventTypeVal"></@block>
			
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
				<@block name="eventTypeEva"></@block>
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
				"outerAttachmentIds" : data.outerAttachmentIds || '',
				"redisKey" : 'T_EVENT_VERIFY_MODULE_TRANSFORM_' + $('#eventVerifyHashId').val()
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
					'bizHashId' : $('#eventVerifyHashId').val(),
					'bizType' : '${moduleCode!}',
					'isDuplicatedCheck': true
				};
			}
			
			if(eventBizPlatform) {
				eventWechat.bizPlatform = eventBizPlatform;
			}
			
			parent.reportEventWechat(eventWechat);
			
			if(typeof(parent.closeBeforeMaxJqueryWindow) === 'function') {//关闭审核窗口
				parent.closeBeforeMaxJqueryWindow();
			}
		}
		
		function isShow2PublicCheck(checkObj) {
			var isShow2Public = 0;
			
			if($(checkObj).is(':checked')) {
				isShow2Public = 1;
			}
			
			$(checkObj).val(isShow2Public);
		}
	</script>
	
</body>
</html>

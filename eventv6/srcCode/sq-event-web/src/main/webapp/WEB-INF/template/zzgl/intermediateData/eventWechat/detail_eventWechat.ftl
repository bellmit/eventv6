<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>微信事件-详情</title>
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
	<form id="tableForm" name="tableForm" action="" method="post">
		<!--用于地图-->
		<input type="hidden" id="id" value="<#if eventWechat.eventVerifyId??>${eventWechat.eventVerifyId?c}</#if>" />
		<input type="hidden" id="module" value="${markerType!}"/>
		<input type="hidden" id="markerOperation" value="${markerOperation!}"/>
		<input type="hidden" id="gridId" value="<#if gridId??>${gridId?c}</#if>" />
		
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
								<label class="LabName"><span>公开事件：</span></label><div class="Check_Radio FontDarkBlue"><#if eventWechat.isShow2Public == '1'>是<#else>否</#if></div>
					        </td>
					        </#if>
					        </@block>
					        
						</tr>
						
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>所属网格：</span></label><div class="Check_Radio FontDarkBlue singleCellClass">${eventWechat.gridPath!''}</div>
				        	</td>
							<td class="LeftTd" width="50%;">
								<label class="LabName"><span>事发时间：</span></label><div class="Check_Radio FontDarkBlue">${eventWechat.happenTimeStr!''}</div>
							</td>
				        </tr>
						<tr></tr>
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
						<tr></tr>
						<tr>
				    		<td colspan="2" class="LeftTd">
				    			<label class="LabName"><span>相关附件：</span></label><div id="bigFileUploadDiv"></div>
				    		</td>
				    	</tr>
						<#if eventWechat.remark??>
						<tr>
							<td class="LeftTd" colspan="2" >
				    			<label class="LabName"><span>办理意见：</span></label><div class="Check_Radio FontDarkBlue areaClass">${eventWechat.remark!}</div>
					        </td>
						</tr>
						</#if>
					</table>
				</div>
			</div>
		</div>
		
		<#if isClosable?? && isClosable>
		<div class="BigTool">
        	<div class="BtnList">
        		<#if eventWechat.eventId??>
        			<@block name="showEventWin">
        			<a href="#" class="BigNorToolBtn PreviewBtn" onclick="parent.showEventWin('${eventWechat.eventId?c}');">查看进度</a>
        			</@block>
        		</#if>
				<a href="#" class="BigNorToolBtn CancelBtn" onclick="closeWin();">关闭</a>
            </div>
        </div>
        </#if>
	</form>
	
	<script type="text/javascript">
		$(window).resize( function (){
		     location.reload();//页面大小变化时自动刷新页面
		});
	</script>
	
	<#include "/zzgl/intermediateData/eventWechat/eventWechat_js.ftl" />
</body>
</html>

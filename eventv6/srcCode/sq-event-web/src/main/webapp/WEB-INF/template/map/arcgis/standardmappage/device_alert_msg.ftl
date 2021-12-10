<!DOCTYPE html>
<html>
<head>
	<title>设备信息详情</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<style>
	a:link {
		color:blue;
	    text-decoration:underline;
	}
	</style>
</head>
<body>
	<div id="content-d" class="MC_con content light">
		<div name="tab" id="div0" class="NorForm">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>
						<label class="LabName"><span>设备名称：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.deviceName)!}</span>
					</td>
					<td>
						<label class="LabName"><span>设备类型： </span></label>
						<span class="Check_Radio FontDarkBlue">
							<#if deviceTypeDD ?? && bo.deviceType??>
								<#list deviceTypeDD as item>
									<#if item.dictGeneralCode == bo.deviceType>
										${item.dictName!'' }
									</#if>
								</#list>
							</#if>
						</span>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<label class="LabName"><span>设备地址：</span></label>
						<span class="Check_Radio FontDarkBlue" >${(bo.deviceInstallAddress)!}</span>
						
					</td>
				</tr>
					<tr>
					<td >
						<label class="LabName"><span>告警时间：</span></label>
						<span class="Check_Radio FontDarkBlue" style="width:160px;">${msgDevice.ALERT_TIME?string("yyyy-MM-dd HH:mm:ss")}</span>
					</td>
					<td>
					</td>
				</tr>
					<tr>
					<td colspan="2">
						<label class="LabName"><span>告警内容：</span></label>
						<span class="Check_Radio FontDarkBlue" >${(msgDevice.ALERT_CONTENT)!}</span>
						
					</td>
				</tr>
			</table>
		</div>
	</div>
	<#if msgDevice ?? && msgDevice.TYPE?? &&msgDevice.TYPE=='0'>
	<div class="BigTool">
    	<div style="width:100px !impoartant;text-align: right;margin-right:50px;">   
    		<a href="javascript:void(0)" onclick="showDeviceByType()">进入处理</a>
        </div> 
    </div>
    </#if>
</body>
<script type="text/javascript">
	
$(function() {
	//添加滚动条样式
	$("#content-d").mCustomScrollbar({theme:"minimal-dark"});
});	
function showDeviceByType(){
	var url=window.parent.gmisDomain+'/gmis/deviceInfo/index.jhtml?deviceType=${(bo.deviceType)!}';  
	window.parent.showMaxJqueryWindow("设备信息",url,1050,450)
	
}
	
</script>
</html>

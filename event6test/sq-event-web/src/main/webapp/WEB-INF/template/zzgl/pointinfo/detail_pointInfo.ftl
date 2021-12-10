<!DOCTYPE html>
<html>
<head>
	<title>详情</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
    <style>
        .BigNorToolBtn{
            font-size: 15px!important;
        }
    </style>
</head>
<body>
	<div id="content-d" class="MC_con content light">
		<div name="tab" id="div0" class="NorForm">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td>
                        <label class="LabName"><span>网格名称：</span></label>
                        <span class="Check_Radio FontDarkBlue">${(bo.gridPath)!}</span>
                    </td>
                    <td>
                    </td>
                </tr>
				<tr>
                    <td>
                        <label class="LabName"><span>设备名称：</span></label>
                        <span class="Check_Radio FontDarkBlue">${(bo.deviceName)!}</span>
                    </td>
					<td>
						<label class="LabName"><span>设备id：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.deviceId)!}</span>
					</td>

				</tr>
				<tr>
					<td>
						<label class="LabName"><span>设备纬度：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.latitude)!}</span>
					</td>
					<td>
						<label class="LabName"><span>设备经度：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.longitude)!}</span>
					</td>
				</tr>
                <tr>
                    <td>
                        <label class="LabName"><span>设备编码：</span></label>
                        <span class="Check_Radio FontDarkBlue">${(bo.deviceCid)!}</span>
                    </td>
                    <td>
                        <label class="LabName"><span>设备序列号：</span></label>
                        <span class="Check_Radio FontDarkBlue">${(bo.deviceSn)!}</span>
                    </td>
                </tr>
			</table>
		</div>
	</div>
	<div class="BigTool">
    	<div class="BtnList">
    		<a href="javascript:;" class="BigNorToolBtn CancelBtn" onClick="cancel();">关闭</a>
        </div>
    </div>
</body>
<script type="text/javascript">
	//关闭
	function cancel() {
		parent.closeMaxJqueryWindow();
	}
</script>
</html>

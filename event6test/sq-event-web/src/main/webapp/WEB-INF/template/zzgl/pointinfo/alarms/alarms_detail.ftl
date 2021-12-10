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
        .w50p{width:50%;}
    </style>
</head>
<body>
	<div id="content-d" class="MC_con content light">
		<div name="tab" id="div0" class="NorForm">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
                    <td class="w50p">
                        <label class="LabName"><span>所属区域：</span></label>
                        <span class="Check_Radio FontDarkBlue">${(bo.placeName)!}</span>
                    </td>
					<td class="w50p">
						<label class="LabName"><span>所在任务：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.taskName)!}</span>
					</td>
				</tr>
				<tr>
                    <td colspan="w50p">
                        <label class="LabName"><span>设备名称：</span></label>
                        <span class="Check_Radio FontDarkBlue" style="width: 75%;">${(bo.deviceName)!}</span>
                    </td>
                    <td class="w50p">
						<label class="LabName"><span>触发时间：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.alarmTime)!}</span>
					</td>
                </tr>
				<tr>
					<td colspan="2">
						<label class="LabName"><span>设备地址：</span></label>
						<span class="Check_Radio FontDarkBlue" style="width: 85%;">${(bo.deviceAddress)!}</span>
					</td>
				</tr>
                <tr>
                    <td colspan="2">
                        <label class="LabName"><span>持续时间：</span></label>
                        <span class="Check_Radio FontDarkBlue">${(bo.durationString)!}</span>
                    </td>
                </tr>
                
                <tr>
                    <td colspan="2">
                        <label class="LabName"><span>图片：</span></label>
                        <#if bo.imageUrl??> 
                        	<img src="${bo.imageUrl}" width="620" height="450" />
                        </#if>
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
	var h = 0;
		if ($(".BigTool").length != 0) {
			h = $(".BigTool").height();
		}
	$(function(){
		
		setTimeout(function(){
			 pageLoad();
		}, 30);
	});
	function  pageLoad(){
		var height = $(window).height();
			$("#content-d").css("height", height- 20 - h);
	}
	$(window).on('load resize', function () {
            pageLoad();
        });
</script>
</html>

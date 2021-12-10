<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>消防部件（甘肃）概要信息</title>
<#include "/component/commonFiles-1.1.ftl" />

    <style type="text/css">
        .titleWidth{width: 95%}
        .divWidth{width: 64%;}
        .LabName{width: 65px;}
    </style>
</head>
<body>
	<div id="content-d" class="MC_con content light">
		<div id="norFormDiv" class="NorForm">
			<div>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="LeftTd" colspan="2">
							<div class="Check_Radio FontDarkBlue titleWidth" style="font-size: 14px; padding-left: 5px;font-weight: bold;">
								<#if data.name??>
									${data.name!''}
							  	</#if>
							</div>
						</td>
					</tr>
                    <tr>
                        <td class="LeftTd" colspan="2">
                            <label class="LabName"><span>位&nbsp;&nbsp;置：</span></label>
                            <div class="Check_Radio FontDarkBlue divWidth">
								<#if data.location??>
									${data.location!''}
								</#if>
                            </div>
                        </td>
                    </tr>
					<tr>
						<td class="LeftTd" colspan="2">
							<label class="LabName"><span>所属网格：</span></label>
							<div class="Check_Radio FontDarkBlue divWidth">
								<#if data.gridName??>
									${data.gridName!''}
								</#if>
							</div>
						</td>
					</tr>
					<tr>
						<td class="LeftTd">
							<label class="LabName"><span>状&nbsp;&nbsp;态：</span></label>
							<div class="Check_Radio FontDarkBlue">
								<#if data.status??>
									<#if data.status == '1'>
										正常
									<#elseif data.status == '2'>
										破损
									<#else>
										未知
									</#if>
								</#if>
							</div>
						</td>
                        <td>
                            <label class="LabName"><span>创建时间：</span></label>
                            <div class="Check_Radio FontDarkBlue" style="width: 65px">
							<#if data.createTime??>
									${data.createTime!''}
								</#if>
                            </div>
                        </td>
					</tr>
					<tr>
						<td class="LeftTd" colspan="2">
							<div class="Check_Radio FontDarkBlue" align="center">
								<img id="preview"  src="<#if data.photoUrl??>${data.photoUrl!''}<#else>${rc.getContextPath()}/images/notbuilding.gif</#if>" border=0 style="width:180px;height:180px;margin-top: 5px;margin-bottom: 15px;"/>
							</div>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
    <script type="text/javascript">
        $(function() {
            var winWidth = $(document).width();//扣除左右边距

            $("#content-d").width(winWidth);
            $("#norFormDiv").width(winWidth);

            var options = {
                axis : "yx",
                theme : "minimal-dark"
            };
            enableScrollBar('content-d',options);
        });
    </script>
</body>
</html>

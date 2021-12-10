<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>消防安全检查（甘肃）概要信息</title>
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
                    <td class="LeftTd">
                        <div class="Check_Radio FontDarkBlue titleWidth" style="font-size: 14px; padding-left: 5px;font-weight: bold;">
                            消防安全检查
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="LeftTd">
                        <label class="LabName"><span>检查单位：</span></label>
                        <div class="Check_Radio FontDarkBlue divWidth">
                            <#if data.name??>
                                ${data.name!''}
                            </#if>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="LeftTd">
                        <label class="LabName"><span>检查结果：</span></label>
                        <div class="Check_Radio FontDarkBlue divWidth">
                            <#if data.checkInfo??>
                                ${data.checkInfo!''}
                            </#if>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="LeftTd">
                        <label class="LabName"><span>检查日期：</span></label>
                        <div class="Check_Radio FontDarkBlue">
                            <#if data.checkTime??>
                                ${data.checkTime!''}
                            </#if>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="LeftTd">
                        <div class="Check_Radio FontDarkBlue" align="center">
                            <img id="preview"  src="<#if data.inspectionPhotoPath??>${data.inspectionPhotoPath!''}<#else>${rc.getContextPath()}/images/notbuilding.gif</#if>" border=0 style="width:180px;height:180px;margin-top: 5px;margin-bottom: 15px;"/>
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

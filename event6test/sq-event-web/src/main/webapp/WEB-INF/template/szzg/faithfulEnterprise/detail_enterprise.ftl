<!DOCTYPE html>
<html>
<head>
    <title>守重企业信息详情</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<#include "/component/commonFiles-1.1.ftl" />
<#include "/component/ComboBox.ftl" />
</head>
<body>
<div id="content-d" class="MC_con content light">
    <div name="tab" id="div0" class="NorForm">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td style="width: 300px;">
                    <label class="LabName"><span>所属网格：</span></label>
                    <span class="Check_Radio FontDarkBlue" style="width:160px;">${(enterprise.gridPath)!}</span>
                </td>
                <td style="width: 300px;">
                    <label class="LabName"><span>企业名称：</span></label>
                    <span class="Check_Radio FontDarkBlue" style="width: 160px;">${(enterprise.enterpriseName)!}</span>
                </td>
            </tr>
            <tr>
                <td style="width: 300px;">
                    <label class="LabName"><span>工商注册号：</span></label>
                    <span class="Check_Radio FontDarkBlue" style="width: 160px;">${(enterprise.registrationId)!}</span>
                </td>
                <td>
                    <label class="LabName"><span>评定时间：</span></label>
                    <span class="Check_Radio FontDarkBlue"><#if enterprise.evaluationTime??>${(enterprise.evaluationTime)?string('yyyy-MM-dd')}</#if></span>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <label class="LabName"><span>企业地址：</span></label>
                    <span class="Check_Radio FontDarkBlue" style="width:450px;">${(enterprise.enterpriseAddress)!}</span>
                </td>

            </tr>
            <tr>
                <td colspan="2">
                    <label class="LabName"><span>评定级别：</span></label>
                    <span class="Check_Radio FontDarkBlue" style="width:450px;">${(enterprise.evaluationLevel)!}</span>
                </td>
            </tr>
        </table>
    </div>
</div>
<#if !(showClose??&&showClose=='false')>
<div class="BigTool">
    <div class="BtnList">
        <a href="javascript:;" class="BigNorToolBtn CancelBtn" onClick="cancel();">关闭</a>
    </div>
</div>
</#if>
</body>
<script type="text/javascript">

    //关闭
    function cancel() {
        parent.closeMaxJqueryWindow();
    }
</script>
</html>

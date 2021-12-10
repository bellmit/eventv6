<!DOCTYPE html>
<html>
<head>
    <title>商标信息详情</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<#include "/component/commonFiles-1.1.ftl" />
<#include "/component/ComboBox.ftl" />
</head>
<body>
<div id="content-d" class="MC_con content light">
    <div name="tab" id="div0" class="NorForm">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td>
                    <label class="LabName"><span>所在网格：</span></label>
                    <span class="Check_Radio FontDarkBlue" style="width:160px;">${(penalty.gridPath)!}</span>
                </td>
            </tr>
            <tr>
                <td>
                    <label class="LabName"><span>工商注册号：</span></label>
                    <span class="Check_Radio FontDarkBlue" style="width: 200px;">${(penalty.registrationId)!}</span>
                </td>
            </tr>
            <tr>
                <td>
                    <label class="LabName"><span>企业名称：</span></label>
                    <span class="Check_Radio FontDarkBlue" style="width: 200px;">${(penalty.enterpriseName)!}</span>
                </td>
            </tr>
            <tr>
                <td>
                    <label class="LabName"><span>决定文书号：</span></label>
                    <span class="Check_Radio FontDarkBlue" >${(penalty.decisionInstrument)!}</span>
                </td>
            </tr>
            <tr>
                <td>
                    <label class="LabName"><span>处罚日期：</span></label>
                    <span class="Check_Radio FontDarkBlue"><#if penalty.penaltyDate??>${(penalty.penaltyDate)?string('yyyy-MM-dd')}</#if></span>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <label class="LabName"><span>处罚依据：</span></label>
                    <span class="Check_Radio FontDarkBlue" style="width:450px;">${(penalty.penaltyBases)!}</span>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <label class="LabName"><span>处罚结论：</span></label>
                    <span class="Check_Radio FontDarkBlue" style="width:450px;">${(penalty.penaltyConclusion)!}</span>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <label class="LabName"><span>处罚事由：</span></label>
                    <span class="Check_Radio FontDarkBlue" style="width:450px;">${(penalty.penaltyCause)!}</span>
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

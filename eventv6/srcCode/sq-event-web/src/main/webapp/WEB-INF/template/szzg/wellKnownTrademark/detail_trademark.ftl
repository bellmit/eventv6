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
                    <label class="LabName"><span>所属网格：</span></label>
                    <span class="Check_Radio FontDarkBlue" style="width:160px;">${(trademark.gridPath)!}</span>
                </td>
                <td rowspan="4">
                    <label class="LabName"><span>商标图样：</span></label>
                    <img style="filter: alpha(opacity = 0);width: 150px;height:130px;" src="<#if trademark.trademarkImg?? >${RESOURSE_SERVER_PATH!}${trademark.trademarkImg!}</#if> <#--<#if !trademark.trademarkImg?? > ${rc.getContextPath()}/ui/images/defalt.jpg</#if>--> ">
                </td>
            </tr>
            <tr>
                <td>
                    <label class="LabName"><span>单位名称：</span></label>
                    <span class="Check_Radio FontDarkBlue" style="width: 160px;">${(trademark.unitName)!}</span>
                </td>
            </tr>
            <tr>
                <td>
                    <label class="LabName"><span>商标内容：</span></label>
                    <span class="Check_Radio FontDarkBlue" style="width: 160px; ;">${(trademark.brand)!}</span>
                </td>
            </tr>
            <tr>
                <td>
                    <label class="LabName"><span>认定日期：</span></label>
                    <span class="Check_Radio FontDarkBlue"><#if trademark.thatTimeDate??>${(trademark.thatTimeDate)?string('yyyy-MM-dd')}</#if></span>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <label class="LabName"><span>单位地址：</span></label>
                    <span class="Check_Radio FontDarkBlue" style="width:450px;">${(trademark.unitAddress)!}</span>
                </td>

            </tr>
            <tr>
                <td colspan="2">
                    <label class="LabName"><span>核定商品范围：</span></label>
                    <span class="Check_Radio FontDarkBlue" style="width:450px;">${(trademark.scope)!}</span>
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

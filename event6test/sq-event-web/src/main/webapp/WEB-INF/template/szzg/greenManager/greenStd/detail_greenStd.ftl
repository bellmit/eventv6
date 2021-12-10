<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="x-ua-compatible" content="ie=8" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>编辑国家森林指标数据</title>
<#include "/component/commonFiles-1.1.ftl" />
    <link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/scripts/updown/swfupload/css/swfupload.css" />
    <script type="text/javascript" src="${rc.getContextPath()}/scripts/updown/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="${rc.getContextPath()}/scripts/updown/swfupload/handlers.js"></script>
</head>
<style>
    .inp2{width: 140px;}
</style>
<body>
<div>
    <div id="content-d" class="MC_con content light" style="overflow-x:hidden">
        <div class="NorForm NorForm2">
            <form id="tableForm" name="tableForm" action="${rc.getContextPath()}/zhsq/szzg/greenstd/saveOrUpdate.jhtml"  method="post">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">


                   <input type="hidden" name="seqid" id="seqid" value="<#if greenStd.seqid??>${greenStd.seqid}</#if>">

                    <tr>
                        <td colspan="2" class="LeftTd"><label class="LabName"><span>指标名称：</span></label>
                            <span class="Check_Radio FontDarkBlue">  <#if greenStd.seqid??>${greenStd.seqid}</#if></span>
                           </td>
                    </tr>

                    <tr>
                        <td class="LeftTd"><label class="LabName"><span>所属年份：</span></label>
                            <span class="Check_Radio FontDarkBlue">  <#if greenStd.syear??>${greenStd.syear}</#if></span>

                        </td>
                        <td><label class="LabName"><span>类型：</span></label>
                            <span class="Check_Radio FontDarkBlue"><#if greenStd.typeName??>${greenStd.typeName}</#if></span>


                        </td>
                    </tr>
                    <tr>
                        <td class="LeftTd"><label class="LabName"><span>指标标准：</span></label>
                            <span class="Check_Radio FontDarkBlue"> <#if greenStd.stdval??>${greenStd.stdval}</#if></span>

                        </td>
                        <td><label class="LabName"><span>我市标准：</span></label>
                            <span class="Check_Radio FontDarkBlue">    <#if greenStd.actval??>${greenStd.actval}</#if></span>

                        </td>
                    </tr>



                    <tr>
                        <td colspan="2" class="LeftTd RightTd"><label class="LabName"><span>指标说明：</span></label>
                            <p><span class="Check_Radio FontDarkBlue"
                                     style="width: 85%"> <#if greenStd.content??>${greenStd.content}</#if></span></p>
                          </td>
                    </tr>


                </table>
            </form>
        </div>
    </div>
    <div class="BigTool">
        <div class="BtnList">

            <a href="#" class="BigNorToolBtn CancelBtn" onclick="javascript:cancel();">关闭</a>
        </div>
    </div>
</div>
<#include "/component/maxJqueryEasyUIWin.ftl" />
<#include "/component/ComboBox.ftl" />
<#include "/component/customEasyWin.ftl" />
</body>

<script type="text/javascript">


    $(function () {


        //加载数据字典：类型
        AnoleApi.initTreeComboBox("typeName", null, 'S002001', function (gridId, items) {
            if (isNotBlankParam(items) && items.length > 0) {
                document.getElementById('type').value = items[0].dictCode;
            }
        }, null, {ChooseType: '1', ShowOptions: {EnableToolbar: true}});



        $("#content-d").mCustomScrollbar({theme:"minimal-dark"});

    });



    //重复验证，并提交
    function checkSubmit() {
        var isValid = $("#tableForm").form('validate');
        if (isValid) {
            $("#tableForm").ajaxSubmit(function (data) {
                if (data.result) {
                    parent.reloadDataForSubPage(data.msg);
                } else {
                    modleclose();

                    if (data.msg) {
                        $.messager.alert('错误', data.msg, 'error');
                    } else {
                        $.messager.alert('错误', '操作失败！', 'error');
                    }
                }
            });
        }
    }

    //取消
    function cancel() {
        parent.closeMaxJqueryWindow()

    }

    //-- 供子页调用的重新载入数据方法
    function reloadDataForSubPage(result) {
        closeMaxJqueryWindow();
        $.messager.alert('提示', result, 'info');
        $("#list").datagrid('load');
    }




</script>
</html>
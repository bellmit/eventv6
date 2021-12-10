<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="x-ua-compatible" content="ie=8"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>预案信息</title>
<#include "/component/commonFiles-1.1.ftl" />
    <link rel="stylesheet" href="${rc.getContextPath()}/js/kindeditor-4.1.10/themes/default/default.css"/>
    <script type="text/javascript" src="${rc.getContextPath()}/js/kindeditor-4.1.10/kindeditor-min.js"
            charset="UTF-8"></script>
    <script type="text/javascript" src="${rc.getContextPath()}/js/kindeditor-4.1.10/kindeditor-all.js"
            charset="UTF-8"></script>
    <script type="text/javascript" charset="utf-8"
            src="${rc.getContextPath()}/js/kindeditor-4.1.10/lang/zh_CN.js"></script>

</head>
<style>
    .inp2 {
        width: 140px;
    }

    .con{
        top:10px;
        height: 300px;
        width: 650px;
        padding: 10px;
        font-size: 14px;
        line-height: 34px;
        overflow: auto;
        white-space:normal;
        word-break:break-all;
        text-align:left
    }

</style>
<body>
<div>
    <div id="content-d" class="MC_con content light" style="overflow-x:hidden">
        <div class="NorForm NorForm2">
            <form id="tableForm" name="tableForm"
                  action="${rc.getContextPath()}/zhsq/szzg/emeryencyplan/updateContent.jhtml"
                  method="post">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td class="LeftTd" colspan="2" style="width: 336.8px;height: 27.6px"><label class="LabName"><span><b style="color: red;">*</b>标题：</span></label>
                            <span class="Check_Radio FontDarkBlue" style="width: 70%"><#if emeryencyPlanContent.title??>${emeryencyPlanContent.title}</#if></span>
                        </td>
                    </tr>

                    <tr>
                        <td ><label class="LabName"><span>排序：</span></label>
                            <span class="Check_Radio FontDarkBlue" style="width: 70%"><#if emeryencyPlanContent.priority??>${emeryencyPlanContent.priority}</#if></span>
                        </td>

                        <td class="LeftTd"><label class="LabName"><span>是否启用：</span></label>
                            <span class="Check_Radio FontDarkBlue">
                                <#if (emeryencyPlanContent.status?? && emeryencyPlanContent.status=='1')>
                                是
                                </#if>
                                <#if (emeryencyPlanContent.status?? && emeryencyPlanContent.status!='1')>
                                否
                                </#if>
                            </span>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="3" class="RightTd" style="padding: 0px;height: 410px"><label class="LabName"><span><b style="color: red;">*</b>内容：</span></label>
                            <pre id="content" style="height:300px;overflow: auto;margin: 0px;top: 0px;padding: 0px;line-height: 30px" class="con"><#if emeryencyPlanContent.txtStr??>${emeryencyPlanContent.txtStr}</#if></pre>
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
    $("#content-d").mCustomScrollbar({theme: "minimal-dark"});
    $("#content").mCustomScrollbar({theme: "minimal-dark"});
    function changeStdValue() {
        var std = $("#status").is(":checked");
        if (std) {
            $("#status").val(1);
        } else {
            $("#status").removeAttr("checked");
            $("#status").val(0);
        }
    }

    $("#content-d").mCustomScrollbar({theme: "minimal-dark"});

    //内容编辑器
    var editor;
    KindEditor.ready(function (K) {
        editor = K.create('textarea[name="txt"]', {
            width: "700px",
            height: "250px",
            resizeMode: 1,
            allowFileManager: true,
            uploadJson: '${rc.getContextPath()}/zhsq/upLoadFile4x.jhtml'
        });
    });


    //重复验证，并提交
    function checkSubmit() {
        var isValid = $("#tableForm").form('validate');
        if ($('#overView').val() == null || $('#overView').val() == "") {

            $.messager.alert('提示', '预案内容不能为空！！！', 'info');
            return;
        }

        if (isValid) {
            $("#tableForm").ajaxSubmit(function (data) {
                if (data) {
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
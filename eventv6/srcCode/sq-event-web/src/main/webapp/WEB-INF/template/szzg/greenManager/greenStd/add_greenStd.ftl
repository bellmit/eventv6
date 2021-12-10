<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="x-ua-compatible" content="ie=8" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>新增国家森林指标数据</title>
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


                    <tr>

                    <tr>
                        <td colspan="2" class="LeftTd"><label class="LabName"><span>指标名称：</span></label>
                            <input name="name" id="name" maxLength="100" type="text" class="inp1 easyui-validatebox" style="width: 510px;" data-options="tipPosition:'bottom',required:true,validType:['charts','length[0,20]']"/></td>
                    </tr>

                    <tr>
                        <td class="LeftTd"><label class="LabName"><span>所属年份：</span></label>
                            <input class="inp1 inp2 InpDisable  Wdate timeClass easyui-validatebox" type="text" id="syear" style="width:160px"
                                   data-options="required:true" name="syear"   onClick="WdatePicker({isShowClear:false,maxDate:'%y',dateFmt:'yyyy'})" readonly="true"/>
                        </td>
                        <td><label class="LabName"><span>类型：</span></label>
                            <input id="type" name="type" type="hidden"/>
                            <input id="typeName" name="typeName" data-options="required:true" type="text" class="inp1 InpDisable  easyui-validatebox" style="width:160px;"
                        </td>
                    </tr>
                    <tr>
                        <td class="LeftTd"><label class="LabName"><span>指标标准：</span></label>
                            <input name="stdval" id="stdval" type="text"   style="height: 28px;width: 160px;" class="inp1 inp2 easyui-validatebox" data-options="tipPosition:'bottom',required:true,validType:['maxLength[15]','characterCheck']"/>
                        </td>
                        <td><label class="LabName"><span>我市标准：</span></label>
                            <input name="actval" id="actval"  style="height: 28px;width: 160px;" type="text" class="inp1 inp2 easyui-validatebox"  data-options="tipPosition:'bottom',required:true,validType:['maxLength[15]','characterCheck']"/>
                        </td>
                    </tr>



                    <tr>
                        <td colspan="2" class="LeftTd RightTd"><label class="LabName"><span>指标说明：</span></label>
                            <textarea name="content" id="content" cols="45" rows="60" style="width: 510px;height: 160px;" class="area1 easyui-validatebox" maxLength="251" data-options="tipPosition:'bottom',validType:['maxLength[250]','characterCheck']"></textarea></td>
                    </tr>


                </table>
            </form>
        </div>
    </div>
    <div class="BigTool">
        <div class="BtnList">
            <a href="#" class="BigNorToolBtn JieAnBtn" onclick="javascript:checkSubmit();">保存</a>
            <a href="#" class="BigNorToolBtn CancelBtn" onclick="javascript:cancel();">取消</a>
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
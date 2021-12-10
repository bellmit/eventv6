<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="x-ua-compatible" content="ie=8"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>编辑应急预案树</title>
<#include "/component/commonFiles-1.1.ftl" />
    <link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/scripts/updown/swfupload/css/swfupload.css"/>
    <script type="text/javascript" src="${rc.getContextPath()}/scripts/updown/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="${rc.getContextPath()}/scripts/updown/swfupload/handlers.js"></script>
    <link rel="stylesheet" type="text/css" href="${COMPONENTS_URL}/css/zzForm/component.css" />
    <script type="text/javascript" src="${COMPONENTS_URL}/js/zzForm/zz-form.js"></script>
</head>
<style>
    .inp2 {
        width: 140px;
    }
</style>
<body>
<div>
    <div id="content-d" class="MC_con content light" style="overflow-x:hidden">
        <div class="NorForm NorForm2">
            <form id="tableForm" name="tableForm" action="${rc.getContextPath()}/zhsq/szzg/emeryencyplan/updateTree.jhtml"
                  method="post">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">

                    <tr>

                        <td colspan="2" class="LeftTd"><label class="LabName"><span>父栏目：</span></label>
                            <input type="hidden" id="parentId" name="parentId" value="<#if emeryencyPlanTree.parentId??>${emeryencyPlanTree.parentId}</#if>" >
                            <input type="hidden" id="id" name="id" value="<#if emeryencyPlanTree.id??>${emeryencyPlanTree.id}</#if>" >


                        <#if emeryencyPlanTree.parentName??> ${emeryencyPlanTree.parentName}</#if><#if emeryencyPlanTree.parentId==0><label class="LabName" style="text-align: left;"><span>应急预案</span></label></#if>
                        </td>
                    </tr>

                    <tr>
                        <td><label class="LabName"><span><b style="color: red;">*</b>栏目名称：</span></label>
                            <input class="inp1 inp2 InpDisable easyui-validatebox" type="text" id="name"  style="width: 195px;height:30px;" maxlength="101"
                                   data-options="tipPosition:'bottom',required:true,validType:['maxLength[100]','characterCheck']" name="name" value="<#if emeryencyPlanTree.name??>${emeryencyPlanTree.name}</#if>"/>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" class="LeftTd"><label class="LabName"><span><b style="color: red;">*</b>栏目排序：</span></label>
                            <input name="priority" id="priority" maxLength="30" type="text" class="inp1 inp2 InpDisable easyui-numberbox"
                                   style="width: 200px;height:30px;"
                                   data-options="tipPosition:'bottom',required:true,max:99999999,min:0" value="<#if emeryencyPlanTree.priority??>${emeryencyPlanTree.priority}</#if>"/>
                        </td>
                    </tr>

                    <tr>
                        <td class="LeftTd">
                            <label class="LabName"><span>是否启用：</span></label>
                            <div class="zz-form" zz-form-filter="careRoadsIsDetection">
                                <input type="checkbox" zz-form-filter="_careRoadsIsDetection" name="status" id="status" onclick="changeStdValue()" value="1" <#if (emeryencyPlanTree.status?? && emeryencyPlanTree.status=='1')>checked</#if>>
                                <#--<input type="hidden" id="careRoads_isDetection"  name="careRoads.isDetection"  value="<#if careRoads?? && careRoads.isDetection??>${careRoads.isDetection}<#else>0</#if>">
                                <input class="toggle" style="cursor: pointer;outline:none;" type="checkbox"  id="careRoads_isDetectionCheck" onclick="changeDetectionValue('careRoads');" <#if (careRoads?? && careRoads.isDetection?? && careRoads.isDetection=='1')>checked</#if> />-->
                            </div>
                        </td>
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
        zzForm.render('checkbox','careRoadsIsDetection');
        zzForm.on('checkbox(_careRoadsIsDetection)', function (data) {
            if(data.checked){
                $(this).val('1');
            }else {
                $(this).val('0');
            }
        });
    });

    function changeStdValue(){
        var std = $("#status").is(":checked");
        if(std){
            $("#status").val(1);
        }else{
            $("#status").removeAttr("checked");
            $("#status").val(0);
        }
    }

    //重复验证，并提交
    function checkSubmit() {
        var isValid = $("#tableForm").form('validate');
        if (isValid) {
            $("#tableForm").ajaxSubmit(function (data) {
                if (data) {
                    parent.reloadDataForSubPage(data.msg);
                } else {
                    modleclose();

                    if (data.msg) {
                        $.messager.alert('成功', data.msg, 'info');
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

</script>
</html>
<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="x-ua-compatible" content="ie=8"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>新增应急预案</title>
<#include "/component/commonFiles-1.1.ftl" />
    <link rel="stylesheet" href="${rc.getContextPath()}/js/kindeditor-4.1.10/themes/default/default.css"/>
    <script type="text/javascript" src="${rc.getContextPath()}/js/kindeditor-4.1.10/kindeditor.js"
            charset="UTF-8"></script>
    <script type="text/javascript" src="${rc.getContextPath()}/js/kindeditor-4.1.10/kindeditor-all.js"
            charset="UTF-8"></script>
    <script type="text/javascript" charset="utf-8"
            src="${rc.getContextPath()}/js/kindeditor-4.1.10/lang/zh_CN.js"></script>
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
            <form id="tableForm" name="tableForm"
                  action="${rc.getContextPath()}/zhsq/szzg/emeryencyplan/insertContent.jhtml"
                  method="post">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">

                    <input type="hidden" name="treeId" value="${id}">
                    <tr>
                        <td class="LeftTd" colspan="2"><label class="LabName"><span><b style="color: red;">*</b>标题：</span></label>
                            <input name="title" id="title" type="text" style="height: 30px;width: 80.5%;"
                                   class="inp1 easyui-validatebox"
                                   data-options="tipPosition:'bottom',required:true,validType:['charts','length[0,20]']"/>
                        </td>
                    </tr>

                    <tr>

                        <td class="LeftTd"><label class="LabName"><span>排序：</span></label>
                            <input name="priority" id="priority" style="height: 30px;width: 160px;" type="text"
                                   class="inp1 inp2 easyui-numberbox"
                                   data-options="tipPosition:'bottom',max:99999999,min:0"/>
                        </td>
<#--                        <td class="LeftTd"><label class="LabName"><span>是否启用：</span></label>-->
<#--                            <div class="Check_Radio"><input type="checkbox" name="status" id="status" checked value="1" onclick="changeStdValue();"/><label for="status" style="cursor:pointer;">是</label>-->
<#--                            </div>-->
<#--                        </td>-->
                        <td class="LeftTd">
                            <label class="LabName"><span>是否启用：</span></label>
                            <div class="zz-form" zz-form-filter="careRoadsIsDetection">
                                <input type="checkbox" zz-form-filter="_careRoadsIsDetection" name="status" id="status" onclick="changeStdValue()" value="1" checked>
                                <#--<input type="hidden" id="careRoads_isDetection"  name="careRoads.isDetection"  value="<#if careRoads?? && careRoads.isDetection??>${careRoads.isDetection}<#else>0</#if>">
                                <input class="toggle" style="cursor: pointer;outline:none;" type="checkbox"  id="careRoads_isDetectionCheck" onclick="changeDetectionValue('careRoads');" <#if (careRoads?? && careRoads.isDetection?? && careRoads.isDetection=='1')>checked</#if> />-->
                            </div>
                        </td>
                    </tr>


                    <tr>
                        <td colspan="3" class="LeftTd"><label class="LabName"><span><b style="color: red;">*</b>内容：</span></label>
                            <textarea rows="4" style="width:300px;margin:10px auto;" id="content" name="content"></textarea>
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
    var editor;
    KindEditor.ready(function (K) {
        editor = K.create('textarea[name="content"]', {
            width: "680px",
            height: "388px",
            resizeMode: 1,
            allowFileManager: false,
            uploadJson: '${rc.getContextPath()}/zhsq/upLoadFile4x.jhtml',
            afterBlur: function () {
                this.sync();
            }
        });
    });

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


    //重复验证，并提交
    function checkSubmit() {
        var isValid = $("#tableForm").form('validate');

        if ($('#content').val() == null || $('#content').val() == "") {
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
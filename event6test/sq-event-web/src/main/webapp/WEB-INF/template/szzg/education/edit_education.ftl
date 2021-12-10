<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="x-ua-compatible" content="ie=8"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>编辑人文素质</title>
<#include "/component/commonFiles-1.1.ftl" />
    <link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/scripts/updown/swfupload/css/swfupload.css"/>
    <script type="text/javascript" src="${rc.getContextPath()}/scripts/updown/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="${rc.getContextPath()}/scripts/updown/swfupload/handlers.js"></script>
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
            <form id="tableForm" name="tableForm" action="${rc.getContextPath()}/zhsq/szzg/education/saveOrUpdate.jhtml"
                  method="post">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <input type="hidden" id="seqid" name="seqid"
                           value="<#if educationBO.seqid??>${educationBO.seqid!''}</#if>">
                    <tr>

                        <td colspan="2" class="LeftTd"><label class="LabName"><span>所属年份：</span></label>
                            <input class="inp1 inp2 InpDisable  Wdate timeClass easyui-validatebox" type="text"
                                   id="yearStr" value="<#if educationBO.yearStr??>${educationBO.yearStr!''}</#if>"
                                   data-options="required:true" name="yearStr" readonly="true"/>

                        </td>
                    </tr>
                    <tr>


                        <td colspan="2" class="LeftTd"><label class="LabName"><span>所属网格：</span></label>
                            <input type="hidden" id="orgCode" name="orgCode"
                                   value="<#if educationBO.orgCode??>${educationBO.orgCode!''}</#if>">
                            <input class="inp1 inp2 InpDisable easyui-validatebox" type="text" id="orgName"
                                   data-options="required:true" name="orgName"
                                   value="<#if educationBO.orgName??>${educationBO.orgName!''}</#if>"/>
                        </td>
                    </tr>

                    <tr>

                        <td><label class="LabName"><span>文化程度：</span></label>
                            <input type="hidden" id="type" name="type"
                                   value="<#if educationBO.type??>${educationBO.type!''}</#if>">
                            <input type="text" id="typeName" name="typeName" data-options="required:true"
                                   class="inp1 Wdate fl" style="width:140px; cursor:pointer;"
                                   value="<#if educationBO.typeName??>${educationBO.typeName!''}</#if>"/>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" class="LeftTd"><label class="LabName"><span>男性人数：</span></label>
                            <input name="males" id="males" maxLength="30" type="text" class="inp1 easyui-numberbox"
                                   style="width: 100px;height:30px"
                                   value="<#if educationBO.males??>${educationBO.males!''}</#if>"
                                   data-options="tipPosition:'bottom',required:true,max:99999999,min:0"/>&nbsp;(人)
                        </td>
                    </tr>

                    <tr>
                        <td colspan="2" class="LeftTd"><label class="LabName"><span>女性人数：</span></label>
                            <input name="females" id="females" maxLength="30" type="text" class="inp1 easyui-numberbox"
                                   style="width: 100px;height:30px"
                                   value="<#if educationBO.females??>${educationBO.females!''}</#if>"
                                   data-options="tipPosition:'bottom',required:true,max:99999999,min:0"/>&nbsp;(人)
                        </td>
                    </tr>

                    <tr>
                        <td colspan="2" class="LeftTd"><label class="LabName"><span>总人数：</span></label>
                            <input name="totalPeople" id="totalPeople" maxLength="30" type="text"
                                   class="inp1 easyui-numberbox"
                                   style="width: 100px;height:30px"
                                   value="<#if educationBO.totalPeople??>${educationBO.totalPeople!''}</#if>"
                                   data-options="tipPosition:'bottom',required:true,max:99999999,min:0"/>&nbsp;(人)
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
        $("#content-d").mCustomScrollbar({theme:"minimal-dark"});
        $("input", $("#totalPeople").next("span")).click(function () {
            var males = parseInt($("#males").val());
            var females = parseInt($("#females").val());
            if (!isNaN(males) && !isNaN(females)) {
                var total = males + females
                $("#totalPeople").val(total)
                $('#totalPeople').textbox('setValue', total);

            }
            else if (!isNaN(males) && isNaN(females)) {
                var total = males + 0
                $("#totalPeople").val(total)
                $('#totalPeople').textbox('setValue', total);

            }

            else if (isNaN(males) && !isNaN(females)) {
                var total = 0 + females
                $("#totalPeople").val(total)
                $('#totalPeople').textbox('setValue', total);

            }
            else {
            }

        })
    });
        $(function () {
            AnoleApi.initGridZtreeComboBox("orgName", null, function (gridId, items) {
                if (items && items.length > 0) {
                    document.getElementById('orgCode').value = items[0].orgCode;
                }
            }, {
                rootName: "行政区划",
                ChooseType: '1',
                isShowPoorIcon: "0",
                ShowOptions: {EnableToolbar: true},
                OnCleared: function () {
                    document.getElementById('orgCode').value = '';
                }
            });

            //加载数据字典：文化程度
            AnoleApi.initTreeComboBox("typeName", null, 'D060001', function (gridId, items) {
                if (isNotBlankParam(items) && items.length > 0) {
                    document.getElementById('type').value = items[0].value;
                }
            }, null, {ChooseType: '1', ShowOptions: {EnableToolbar: true}});


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
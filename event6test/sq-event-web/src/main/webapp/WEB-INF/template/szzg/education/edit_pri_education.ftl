<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="x-ua-compatible" content="ie=8"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>编辑小学就读情况</title>
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
            <form id="tableForm" name="tableForm" action="${rc.getContextPath()}/zhsq/szzg/education/updatePriEdu.jhtml"
                  method="post">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <input type="hidden" id="seqid" name="seqid"
                           value="<#if priEdu["SEQID"]??>${priEdu["SEQID"]!''}</#if>">
                    <tr>
                        <td colspan="2" class="LeftTd"><label class="LabName"><span>所属网格：</span></label>
                            <input type="hidden" id="orgCode" name="orgCode"
                                   value="<#if priEdu["DICT_CODE"]??>${priEdu["DICT_CODE"]!''}</#if>">
                            <input class="inp1 inp2 InpDisable easyui-validatebox" readonly="true" type="text" id="orgName"
                                   data-options="required:true" name="orgName"
                                   value="<#if priEdu["AREA"]??>${priEdu["AREA"]!''}</#if>"/>
                        </td>
                    </tr>
                    
                    <tr>
                        <td colspan="2" class="LeftTd"><label class="LabName"><span>所属年份：</span></label>
                            <input class="inp1 inp2 InpDisable  Wdate timeClass easyui-validatebox" type="text"
                                   id="yearStr" value="<#if priEdu["SYEAR"]??>${priEdu["SYEAR"]!''}</#if>"
                                   data-options="required:true" name="yearStr" readonly="true"/>

                        </td>
                    </tr> 
                    
                    <tr>
                        <td colspan="2" class="LeftTd"><label class="LabName"><span>本年新增适龄儿童：</span></label>
                            <input name="newage" id="newage" maxLength="30" type="text" class="inp1 easyui-numberbox"
                                   style="width: 100px;height:30px" data-options="required:true"
                                   value="<#if priEdu["S1"]??>${priEdu["S1"]!''}</#if>"
                                   data-options="tipPosition:'bottom',required:true,max:99999999,min:0"/>&nbsp;(人)
                        </td>
                    </tr>
                    
                    <tr>
                        <td colspan="2" class="LeftTd"><label class="LabName"><span>本年新增就读儿童：</span></label>
                            <input name="newread" id="newread" maxLength="30" type="text" class="inp1 easyui-numberbox"
                                   style="width: 100px;height:30px" data-options="required:true"
                                   value="<#if priEdu["S2"]??>${priEdu["S2"]!''}</#if>"
                                   data-options="tipPosition:'bottom',required:true,max:99999999,min:0"/>&nbsp;(人)
                        </td>
                    </tr>
                    
                 


                </table>
            </form>
        </div>
    </div>
    <div class="BigTool">
        <div class="BtnList">


            <a href="#" class="BigNorToolBtn JieAnBtn" onclick="checkSubmit();">保存</a>
            <a href="#" class="BigNorToolBtn CancelBtn" onclick="cancel();">取消</a>
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
      
        
    });
    
    
     //重复验证，并提交
        function checkSubmit() {
            var isValid = $("#tableForm").form('validate');
            if (isValid) {
                $("#tableForm").ajaxSubmit(function (data) {
                    if (data.result) {
                        parent.reloadDataForSubPage('修改成功');
                    } else {
                        modleclose();

                        if (data.msg) {
                            $.messager.alert('错误', '修改成功', 'error');
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
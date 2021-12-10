<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新增</title>
<#include "/component/commonFiles-1.1.ftl" />
</head>
<body>
<div id="content-d" class="MC_con content light" style="overflow-x:hidden">
    <div class="NorForm NorForm2">
        <form id="tableForm" name="tableForm" action="${rc.getContextPath()}/zhsq/timerManage/saveOrUpdate.jhtml"  method="post">
            <input type="hidden" name="timerId" value="${timerManage.timerId!''}">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td colspan="2" class="LeftTd"><label class="LabName"><span>名称：</span></label>
                        <input name="timerName" id="timerName" type="text" class="inp1 easyui-validatebox" style="width: 440px;"
                               data-options="tipPosition:'bottom',required:true,validType:['maxLength[100]','characterCheck']"
                               value="<#if timerManage.timerName??>${timerManage.timerName}</#if>"/>
					</td>
                </tr>
                <tr>
                    <td colspan="2" class="LeftTd"><label class="LabName"><span>任务类：</span></label>
                        <input name="taskClass" id="taskClass" type="text" class="inp1 easyui-validatebox"
                               style="width: 440px;" data-options="tipPosition:'bottom',required:true,validType:['maxLength[100]','characterCheck']"
                               value="<#if timerManage.taskClass??>${timerManage.taskClass}</#if>"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" class="LeftTd"><label class="LabName"><span>表达式：</span></label>
                        <input name="expression" id="timerName" type="text" class="inp1 easyui-validatebox" style="width: 440px;"
                               data-options="tipPosition:'bottom',required:true,validType:['maxLength[100]','characterCheck']"
                               value="<#if timerManage.expression??>${timerManage.expression}</#if>"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" class="LeftTd"><label class="LabName"><span>备注：</span></label>
                        <input name="timerRemark" id="timerRemark" type="text" class="inp1 easyui-validatebox" style="width: 440px;"
                               data-options="tipPosition:'bottom',required:true,validType:['maxLength[100]','characterCheck']"
                               value="<#if timerManage.timerRemark??>${timerManage.timerRemark}</#if>"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" class="LeftTd"><label class="LabName"><span>AppCode：</span></label>
                        <input name="appCode" id="appCode" type="text" class="inp1 easyui-validatebox"
                               style="width: 440px;" data-options="tipPosition:'bottom',required:true,validType:['maxLength[100]','characterCheck']"
                               value="<#if timerManage.appCode??>${timerManage.appCode}</#if>"/>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div class="BigTool">
    <div class="BtnList">
        <a href="#" class="BigNorToolBtn SaveBtn" onclick="javascript:tableSubmit();">保存</a>
        <a href="#" class="BigNorToolBtn CancelBtn" onclick="javascript:cancl();">取消</a>
    </div>
</div>
</body>

<script type="text/javascript">

	$(function(){

	});

	function tableSubmit(){
        var isValid =  $("#tableForm").form('validate');
        if(isValid){
            modleopen();
            $("#tableForm").ajaxSubmit(function (data) {
                if (data.result) {
                    parent.reloadDataForSubPage(data.msg);
                }
            });
        }
	}
	
	function cancel(){
		parent.closeMaxJqueryWindow();
	}

</script>
</html>
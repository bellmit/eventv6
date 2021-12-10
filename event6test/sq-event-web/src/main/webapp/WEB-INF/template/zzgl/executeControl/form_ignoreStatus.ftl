<!DOCTYPE html>
<html>
<head>
    <title>设置状态</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<#include "/component/maxJqueryEasyUIWin.ftl" />
	<#include "/component/listSet.ftl" />
    <style type="text/css">
        .inp1 {width:220px;}
        i.spot-xh{
            display: inline-block;
            color: #f54952;
            padding-right: 5px;
        }
        .LabName{
            width: 120px;
        }
    </style>
</head>
<body>
<form id="submitForm">
    <div id="content-d" class="MC_con content light">
        <div name="tab" class="NorForm">
            <input type="hidden" id="controlTaskId" name="controlTaskId" value="${bo.controlTaskId}"/>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td>
                        <label class="LabName"><span><i class="spot-xh" >*</i>是否忽略报警消息:</span></label>
                        <input type="hidden" id="ignoreStatus" name="ignoreStatus" value="${bo.ignoreStatus}"/>
                        <input type="text" id="ignoreStatusCN" name="ignoreStatusCN" value="${(bo.ignoreStatus)!}" class="inp1 easyui-validatebox" data-options="required:true,validType:'maxLength[20]', tipPosition:'bottom'" placeholder="请设置是否忽略报警消息" />
                    </td>
                </tr>
            </table>
        </div>
    </div>
    <div class="BigTool">
        <div class="BtnList">
            <a href="javascript:;" class="BigNorToolBtn SaveBtn" onClick="save();">保存</a>
            <a href="javascript:;" class="BigNorToolBtn CancelBtn" onClick="cancel();">取消</a>
        </div>
    </div>
</form>
</body>

<script type="text/javascript">

    $(function() {

        nationComboBox = AnoleApi.initListComboBox("ignoreStatusCN", "ignoreStatus", "${ignoreStatus}", null, ["${bo.ignoreStatus!}"],{
            ShowOptions : {
                EnableToolbar : true
            }
        });
    });

    //保存
    function save() {
        var isValid = $('#submitForm').form('validate');
        if (isValid) {
            modleopen(); //打开遮罩层
            $.ajax({
                type: 'POST',
                url: "${rc.getContextPath()}/zhsq/event/monitorTask/editIgnoreStatus.json",
                data: $('#submitForm').serializeArray(),
                dataType: 'json',
                success: function(data) {
                    console.log(data);
                    if (data.result == 'fail') {
                        $.messager.alert('错误', '保存失败！', 'error');
                    } else {
                        $.messager.alert('提示', '保存成功！', 'info', function() {
                            parent.closeMaxJqueryWindow();
                        });
                        parent.searchData();
                    }
                },
                error: function(data) {
                    $.messager.alert('错误', '连接超时！', 'error');
                },
                complete : function() {
                    modleclose(); //关闭遮罩层
                }
            });
        }
    }

    //取消
    function cancel() {
        parent.closeMaxJqueryWindow();
    }
</script>
</html>

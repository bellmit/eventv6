<!DOCTYPE html>
<html>
<head>
    <title>详情</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<#include "/component/commonFiles-1.1.ftl" />
<#include "/component/ComboBox.ftl" />
    <script type="text/javascript" src="${uiDomain!''}/js/openJqueryEasyUIWin.js"></script>
</head>
<body>

<form id="tableForm" method="post">
    <input type="hidden" id="appealId" name="appealId" value="${(bo.appealId)!}" />
    <div id="content-d" class="MC_con content light">
        <div name="tab" id="div0" class="NorForm">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td>
                        <label class="LabName"><span>驳回意见：</span></label>
                        <textarea name="handleRs" id="handleRs" cols="45" rows="5" style="width: 432px;height: 45px;"
                                  class="area1 easyui-validatebox"
                                  data-options="tipPosition:'bottom',required:true,validType:['maxLength[500]','characterCheck']"></textarea>
                    </td>
                </tr>
            </table>
        </div>
    </div>
</form>
<div class="BigTool">
    <div class="BtnList">
        <a href="javascript:;" class="BigNorToolBtn SaveBtn" onClick="report();">确定</a>
        <a href="javascript:;" class="BigNorToolBtn CancelBtn" onClick="cancel();">取消</a>
    </div>
</div>


</body>
<script type="text/javascript">

    var appealId = "${(bo.appealId)!}";
    function report() {
        $("#tableForm").attr("action", "${rc.getContextPath()}/zhsq/publicAppeal/reject.jhtml");

        var isValid =  $("#tableForm").form('validate');
        if(isValid){
            modleopen();
            $("#tableForm").ajaxSubmit(function(data) {
                parent.close();
            });
        }
    }

    //关闭
    function cancel() {
        parent.closeMaxJqueryWindow();
    }

</script>
</html>

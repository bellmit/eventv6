<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="x-ua-compatible" content="ie=8" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>督办事件</title>
    <link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
    <#include "/component/commonFiles-1.1.ftl" />
    <#include "/component/ComboBox.ftl" />
    <@block name="styleNameBlock"></@block>
</head>
<body>
<form id="remindForm" name="remindForm" action="${rc.getContextPath()}/zhsq/event/eventDisposalController/addRemind.jhtml" method="post" enctype="multipart/form-data">
    <input type="hidden" id="eventId" name="eventId" value="<#if eventId??>${eventId}</#if>">
    <input type="hidden" id="taskId" name="taskId" value="<#if taskId??>${taskId}</#if>">
    <input type="hidden" id="instanceId" name="instanceId" value="<#if instanceId??>${instanceId}</#if>">

    <div id="content-d" class="MC_con content light" style="overflow-x:hidden">
        <div class="NorForm">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
			    <input type="text" id="supervisionType" name="supervisionType" class="hide queryParam" <#if isShowSupervisionType?? && (isShowSupervisionType=="true")><#else>value="2"</#if>/>
                <tr id="supervisionTypeTr" <#if isShowSupervisionType?? && (isShowSupervisionType=="true")><#else>class="hide"</#if>>
                    <td class="LeftTd">
                        <label class="LabName"><span>督办类型：</span></label>
                        <input type="text" id="supervisionTypeName" class="inp1 easyui-validatebox" data-options="<#if isShowSupervisionType?? && (isShowSupervisionType=="true")>required:true,</#if> tipPosition:'bottom'" />
                    </td>
                </tr>
                <tr>
                    <td class="LeftTd">
                        <label class="LabName"><span><label class="Asterik">*</label>督办意见：</span></label><textarea name="remarks" id="remarks" cols="" rows="" class="area1 easyui-validatebox" data-options="required:true,validType:['maxLength[250]','characterCheck'],tipPosition:'bottom'" style="width:350px;height:50px;"></textarea>
                    </td>
                </tr>
                <tr <#if isShowSendMsg?? && isShowSendMsg><#else>class="hide"</#if>>
                    <td class="LeftTd">
                        <label class="LabName"><span>发送短信：</span></label>
                        <div class="Check_Radio"><input type='checkbox' id='sendSms_' onclick="showSmsCont();"/></div>
                        <div id="otherMobile" style="display:none;">
                            <input type="text" id="otherMobileNums" name="otherMobileNums" class="inp1" style="width:335px;" value="可以输入额外需要发送短信的手机号码，以英文逗号(,)分隔"/>
                        </div>
                    </td>
                </tr>
                <tr id="sendCont" style="display:none;">
                    <td class="LeftTd">
                        <label class="LabName"><span>短信内容：</span></label><textarea rows="3"  id="smsContent" name="smsContent" class="area1 easyui-validatebox" data-options="validType:['maxLength[500]','characterCheck'],tipPosition:'top'"  style="width:350px;height:75px;"></textarea>
                    </td>
                </tr>
            </table>
        </div>
    </div>

    <div class="BigTool">
        <div class="BtnList">
            <a href="###" onclick="tableSubmit();" class="BigNorToolBtn BigShangBaoBtn">提交</a>
            <a href="###" onclick="cancel();" class="BigNorToolBtn CancelBtn">取消</a>
        </div>
    </div>
</form>
</body>

<script type="text/javascript">
    //督办类别
    var dubanType='${isShowSupervisionType!}';

    if(dubanType!=null&&dubanType=='true'){
        AnoleApi.initTreeComboBox("supervisionTypeName", "supervisionType", "A001093091", null, ["3"]);
    }

    var defaultMsg='可以输入额外需要发送短信的手机号码，以英文逗号(,)分隔';
    $('#otherMobileNums')
            .focus(function(){
                if(defaultMsg == $('#otherMobileNums').val()){
                    $('#otherMobileNums').val("");
                }
            })
            .blur(function(){
                if($('#otherMobileNums').val() == ""){
                    $('#otherMobileNums').val(defaultMsg);
                }
            });

    (function($){
        $(window).load(function(){
            var options = {
                axis : "yx",
                theme : "minimal-dark"
            };
            enableScrollBar('content-d',options);
        });
    })(jQuery);

    function tableSubmit(){
        var isValid =  $("#remindForm").form('validate');

        if(isValid){
            if($("#smsContent").is(":visible")){
                var smsContent = $("#smsContent").val();
                try{
                    smsContent = smsContent.trim();
                }catch(e){
                    if(typeof trimWords != 'undefined'){
                        smsContent = trimWords(smsContent);
                    }
                }

                if(smsContent != ""){
                    isValid = true;
                }else{
                    isValid = false;
                    $.messager.alert('提示','请填写短信内容！','info');
                }

                $("#smsContent").val(smsContent);
            }
        }

        if(isValid){
            modleopen();

            var otherMobileNums = $('#otherMobileNums').val();
            if(otherMobileNums == defaultMsg) {
                otherMobileNums = "";
            }

            $("#remindForm").ajaxSubmit(function(data) {
                if(data.result){
					<@block name="tableSubmitBlock">
                    parent.noReloadDataForSubPage("督办成功！","");
					</@block>
                }else{
                    modleclose();
                    $.messager.alert('错误','督办失败！','error');
                }
            });
        }
    }

    function showSmsCont() {
        var isValid =  $("#remindForm").form('validate');
        if(isValid){
            if($("#sendSms_").attr("checked")){
                modleopen();
                $.ajax({
                    type: "POST",
                    url : '${rc.getContextPath()}/zhsq/workflow/workflowController/eventNoteContent.jhtml',
                    data: 'formId='+ $("#eventId").val()+ "&taskId="+ $("#taskId").val() +"&curnodeName=&nodeName=督办&smsContent="+$("#smsContent").val(),
                    dataType:"json",
                    success: function(data){
                        if(data.msgWrong) {
                            $.messager.alert('错误', data.msgWrong, 'error');
                            $("#sendSms_").attr("checked", false);
                        } else {
                            var smsContent = data.smsContent;
                            smsContent = smsContent.replace('@advice',$("#remarks").val());
                            $("#smsContent").val(smsContent);
                            $("#sendCont").show();
                            $("#otherMobile").show();
                        }
                        modleclose();
                    },
                    error:function(data){
                        modleclose();
                        $.messager.alert('错误','连接错误！','error');
                        $("#sendSms_").attr("checked", false);
                    }
                });
            }else{
                $("#sendCont").hide();
                $("#otherMobile").hide();
            }
        }else{
            $("#sendSms_").attr("checked", false);
        }
    }
	
	<@block name="cancelBlock">
    function cancel(){
        parent.closeMaxJqueryWindow();
    }
	</@block>
</script>
</html>
<!DOCTYPE html>
<html>
<head>
    <title>新增</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<#include "/component/maxJqueryEasyUIWin.ftl" />
	<#include "/component/listSet.ftl" />
    <style type="text/css">
        .inp1 {width:220px;}
        .l-btn-text {
            display: inline-block;
            vertical-align: top;
            width: auto;
            margin: 0px 4px;
            padding-top: 0px;
            line-height: inherit;
            font-size: initial;
        }
        .l-btn{
            margin: 2px 4px;
        }
        i.spot-xh{
            display: inline-block;
            color: #f54952;
            padding-right: 5px;
        }
        .LabName{
            width: 120px;
        }
        .area1:hover {
            border: 1px solid #7ecef4;
            box-shadow: #7ecef4 0 0 5px;
        }
        .area1 {
            height: 84px;
            padding-left: 3px;
            font-size: 12px;
            color: #000;
            line-height: 22px;
            border-radius: 3px;
            transition: border .5s;
        }
    </style>
</head>
<body>
<form id="submitForm">
    <div id="content-d" class="MC_con content light">
        <div name="tab" class="NorForm">
            <input type="hidden" id="id" name="id" value="${bo.id}"/>
            <input type="hidden" id="libIds" name="libIds" value="${bo.libIds}"/>
            <input type="hidden" id="controlTaskId" name="controlTaskId" value="${bo.controlTaskId}"/>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td>
                        <label class="LabName"><span><i class="spot-xh">*</i>任务名称:</span></label>
                        <input type="text" id="name" name="name" value="${(bo.name)!}" class="inp1 easyui-validatebox" data-options="required:true,validType:'maxLength[20]', tipPosition:'bottom'" placeholder="请输入任务名称" />
                    </td>
                    <td>
                        <label class="LabName"><span><i class="spot-xh">*</i>任务类型:</span></label>
                        <input type="hidden" id="taskType" name="taskType" value="${bo.taskType}"/>
                        <input type="text" id="taskTypeCN" name="taskTypeCN" value="${(bo.taskTypeCN)!}" class="inp1 easyui-validatebox" data-options="required:true,validType:'maxLength[20]', tipPosition:'bottom'" placeholder="请输入任务类型" />
                    </td>
                </tr>
                <tr>
                   <td>
                       <label class="LabName"><span><i class="spot-xh">*</i>任务开始时间:</span></label>
                       <input type="text" id="validTime" name="validTime" placeholder="请输入任务开始时间" value="${(bo.validTime)!''}" class="inp1 Wdate"
                              data-options="required:true,tipPosition:'bottom'" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});" readonly />
                   </td>
                    <td>
                        <label class="LabName"><span><i class="spot-xh">*</i>任务结束时间:</span></label>
                        <input type="text" id="invalidTime" name="invalidTime" placeholder="请输入任务结束时间" value="${(bo.invalidTime)!''}" class="inp1 Wdate"
                               data-options="required:true,tipPosition:'bottom'" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});" readonly />
                    </td>
                </tr>
                <tr>
                    <td>
                        <label class="LabName"><span><i class="spot-xh">*</i>抓拍开始时间:</span></label>
                        <input type="text" id="captureStartTime" name="captureStartTime" placeholder="请输入抓拍开始时间" value="${(bo.captureStartTime)!(.now?string('yyyy-MM-dd HH:mm:ss'))}" class="inp1 Wdate"
                               data-options="required:true,tipPosition:'bottom'" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});" readonly />
                    </td>
                    <td>
                        <label class="LabName"><span><i class="spot-xh">*</i>抓拍结束时间:</span></label>
                        <input type="text" id="captureEndTime" name="captureEndTime" placeholder="请输入抓拍结束时间" value="${(bo.captureEndTime)!''}" class="inp1 Wdate"
                               data-options="required:true,tipPosition:'bottom'" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',onpicked:function(){implement()}});" readonly />
                    </td>
                </tr>
                <tr>
                    <td>
                        <label class="LabName"><span><i class="spot-xh">*</i>报警消息接收者:</span></label>
                        <#--<input type="text" id="acceptAlarmUserIds" name="acceptAlarmUserIds" placeholder="请输入报警消息接收者" value="18970005533_ysq" class="inp1 easyui-validatebox"
                               data-options="required:true,validType:'maxLength[32]', tipPosition:'bottom'" readonly />-->
                        <span style="line-height: 28px" id="acceptAlarmUserIds" name="acceptAlarmUserIds">18970005533_ysq</span>
                    </td>
                    <td>
                        <label class="LabName"><span>布控报警阈值:</span></label>
                        <input type="text" id="alarmThreshold" name="alarmThreshold" placeholder="请输入布控报警阈值" value="${(bo.alarmThreshold=='')?string("85","${bo.alarmThreshold}")!}"
                               class="inp1 easyui-validatebox" data-options="validType:'maxLength[100]', tipPosition:'bottom'"  />
                    </td>
                </tr>
                <tr>
                    <td>
                        <label class="LabName"><span><i class="spot-xh">*</i>报警推送方式:</span></label>
                        <input type="hidden" id="alarmMode" name="alarmMode" value="${bo.alarmMode}"/>
                        <input type="text" id="alarmModeCN" name="alarmModeCN" value="${(bo.alarmMode)!}" placeholder="请输入报警推送方式"
                               class="inp1 InpDisable"  data-options="required:true,validType:'maxLength[24]', tipPosition:'bottom'"  />
                    </td>
                    <td>
                        <label class="LabName"><span><i class="spot-xh">*</i>任务多重方式:</span></label>
                        <input type="hidden" id="repeatMode" name="repeatMode" value="${bo.repeatMode}"/>
                        <input type="text" id="repeatModeCN" name="repeatModeCN" value="${(bo.repeatMode)!}" placeholder="请输入任务多重方式"
                               class="inp1 InpDisable" readonly="readonly" data-options="required:true,validType:'maxLength[24]', tipPosition:'bottom'"  />
                    </td>
                </tr>
                <tr>
                    <td>
                        <label class="LabName"><span>报警存储时间:</span></label>
                        <input type="hidden" id="expireType" name="expireType" value="${bo.expireType}"/>
                        <input type="text" id="expireTypeCN" name="expireTypeCN" value="${(bo.expireType)!}" placeholder="请输入报警存储时间"
                               class="inp1 InpDisable" data-options="validType:'maxLength[100]', tipPosition:'bottom'"  />
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <label class="LabName"><span>任务描述:</span></label>
                        <input type="text" id="description" name="description" value="${(bo.description)!}" placeholder="请输入任务描述"
                               class="inp1 easyui-validatebox" data-options="validType:'maxLength[100]', tipPosition:'bottom'" style="width: 712px"  />
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                            <label class="LabName"><i class="spot-xh">*</i>布控设备:</span></label>
                            <input type="radio" onclick="select_pm(1)" id="scope" name="scope" value="1"
                                         class="easyui-validatebox" validType="radio['frm','scope']" required="true" tipPosition="bottom"
                                    <#if bo.scope == '1'>checked="checked"</#if> style="height:15px;"/>全部设备
                            <input type="radio" onclick="select_pm(2)" id="scope" name="scope" value="2"
                                         class="easyui-validatebox" validType="radio['frm','scope']" required="true" tipPosition="bottom"
                                    <#if bo.scope == '2'>checked="checked"</#if> style="height:15px;"/>指定设备
                    </td>
                    <td>
                    </td>
                </tr>
                <tr id="memberListTr" <#if bo.scope == '1'>style="display:none;"</#if>>
                    <td colspan="2">
                        <label class="LabName"><span></span></label>
                        <input type="hidden" id="deviceIds" name="deviceIds" value="${(bo.deviceIds)!}" />
                        <textarea onclick="editdeviceIds()" id="deviceNames" name="deviceNames"
                                  class="readonly-validate area1 easyui-validatebox" style="width:80%;" require="true"
                                  data-options="tipPosition:'bottom'" readonly>${(bo.deviceNames)!}</textarea>
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

    var deviceIds = "";
    var deviceNames = "";

    $(function() {

        nationComboBox = AnoleApi.initListComboBox("taskTypeCN", "taskType", "${taskType}", null, ["${bo.taskType!}"],{
            ShowOptions : {
                EnableToolbar : true
            }
        });

        nationComboBox = AnoleApi.initListComboBox("alarmModeCN", "alarmMode", "${alarmMode}", null, ['${bo.alarmMode!}'],{
            ShowOptions : {
                EnableToolbar : true
            }
        });

        nationComboBox = AnoleApi.initListComboBox("repeatModeCN", "repeatMode", "${repeatMode}", null, ['${bo.repeatMode!}'],{
            ShowOptions : {
                EnableToolbar : true
            }
        });

        nationComboBox = AnoleApi.initListComboBox("expireTypeCN", "expireType", "${expireType}", null, ['${bo.expireType!}'],{
            ShowOptions : {
                EnableToolbar : true
            }
        });

        deviceNames = "${bo.deviceNames}";
        deviceIds = "${bo.deviceIds}";
    });

    function implement() {
        var captureStartTime = $("#captureStartTime").val();
        var captureEndTime = $("#captureEndTime").val();
        if (!isSameDay(captureStartTime,captureEndTime)) {
            $.messager.alert('提示', '任务抓拍时间必须为同一天！', 'info')
        }
    }

    function select_pm(isAll) {
        if (isAll == '1') {
            $('#deviceNames').val('全部设备');
            return;
        } else if (isAll == '2') {
            if (deviceNames == '全部设备'){
                $("#deviceNames").val('');
            } else {
                $("#deviceNames").val(deviceNames);
            }
            $('#memberListTr').show();
        }
    }

    //布控设备
    function editdeviceIds() {
        var _scope = $("input[name='scope']:checked").val();
        if (_scope == undefined || _scope == 'undefined' || _scope == '' || _scope == '1') {
            return false;
        } else {
            var selected_pms = {};
            var id = $('#deviceIds').val();
            if(id != '' || _scope == '2') {
                var ids = '';
                var names = '';
                if (id != '') {
                    ids = id.split(',');
                    names = $('#deviceNames').val().split(',');
                }
                for(var i=0; i<ids.length; i++){
                    selected_pms[ids[i]] = {
                        deviceId: ids[i],
                        deviceName: names[i]
                    }
                }
            }
            if ($("#deviceNames").val() == ''){
                selected_pms = {};
            }
            console.log(selected_pms)
            parent.selectPms({
                _selected_pms: selected_pms,
                selectNum: 0,
                callback: function(data){
                    if(JSON.stringify(data) != '{}') {
                        var ids = new Array();
                        var names = new Array();
                        for(var k in data) {
                            ids.push(data[k].deviceId);
                            names.push(data[k].deviceName);
                        }
                        $('#deviceIds').val(ids.join(','));
                        $('#deviceNames').val(names.join(','));
                    } else {
                        $('#deviceIds').val('');
                        $('#deviceNames').val('');
                    }
                }
            });
        }
    }

    //保存
    function save() {
        var isValid = $('#submitForm').form('validate');
        if (isValid && valid()) {
            var _scope = $("input[name='scope']:checked").val();
            if (_scope == '1') {
                $.ajax({
                    type: 'POST',
                    url: '${rc.getContextPath()}/zhsq/event/pointInfo/listAllData.jhtml',
                    data: {deviceStatus: 1},
                    dataType: 'json',
                    success: function(data) {
                        var deviceIds_ = '';
                        for (var i = 0; i < data.total; i++) {
                            deviceIds_ += data.rows[i].deviceId + ',';
                            $("#deviceIds").val(deviceIds_);
                            $("#deviceNames").val('全部设备');
                        }
                        var data_ = $('#submitForm').serializeArray();
                        modleopen(); //打开遮罩层
                        $.ajax({
                            type: 'POST',
                            url: '${rc.getContextPath()}/zhsq/event/monitorTask/save.json',
                            data: data_,
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
                    },
                    error: function(data) {
                        $.messager.alert('错误', '连接超时！', 'error');
                    },
                    complete : function() {
                        modleclose(); //关闭遮罩层
                    }
                });
            }else {
                var data_ = $('#submitForm').serializeArray();
                modleopen(); //打开遮罩层
                $.ajax({
                    type: 'POST',
                    url: '${rc.getContextPath()}/zhsq/event/monitorTask/save.json',
                    data: data_,
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
    }
    //校验格式
    function valid() {
        var validTime = $("#validTime").val();
        var invalidTime = $("#invalidTime").val();
        var captureStartTime = $("#captureStartTime").val();
        var captureEndTime = $("#captureEndTime").val();
        var alarmThreshold = $("#alarmThreshold").val();
        if (validTime == null || validTime ==''){
            $.messager.alert('错误', '请输入任务开始时间！', 'error');
            return;
        }
        if (invalidTime == null || invalidTime ==''){
            $.messager.alert('错误', '请输入任务结束时间！', 'error');
            return;
        }
        if (captureStartTime == null || captureStartTime ==''){
            $.messager.alert('错误', '请输入任务抓拍开始时间！', 'error');
            return;
        }
        if (captureEndTime == null || captureEndTime ==''){
            $.messager.alert('错误', '请输入任务结束时间！', 'error');
            return;
        }
        if (captureStartTime < validTime || captureStartTime > invalidTime) {
            $.messager.alert('错误', '任务抓拍开始时间必须在任务有效前内！', 'error');
            return;
        }
        if (captureEndTime < validTime || captureEndTime > invalidTime) {
            $.messager.alert('错误', '任务抓拍结束时间必须在任务有效前内！', 'error');
            return;
        }
        if(alarmThreshold <0 || alarmThreshold >100){
            $.messager.alert('提示', '请输入合适的布控报警阈值!', 'error');
            return;
        }
        if ($("#deviceNames").val() == null ||  $("#deviceNames").val() == ""){
            $.messager.alert('提示', '请至少选择一个设备!', 'error');
            return;
        }
        if (captureEndTime > captureStartTime && isSameDay(captureStartTime,captureEndTime)) {
            return true;
        }else {
            $.messager.alert('错误', '任务抓拍时间必须为同一天！', 'error');
            return;
        }
    }
    //判断日期是否是同一天
    function isSameDay(timeStampA, timeStampB) {
        var dateA = new Date(timeStampA);
        var dateB = new Date(timeStampB);
        return (dateA.setHours(0, 0, 0, 0) == dateB.setHours(0, 0, 0, 0));
    }

    //取消
    function cancel() {
        parent.closeMaxJqueryWindow();
    }
</script>
</html>

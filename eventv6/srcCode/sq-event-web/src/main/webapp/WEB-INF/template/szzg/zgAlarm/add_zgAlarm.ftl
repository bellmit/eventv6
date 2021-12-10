<!DOCTYPE html>
<html>
<head>
	<title>保存</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<style type="text/css">
		.LabName{
			width:95px;
		}
		.inp1 {width:160px;}
		.numberbox{
			height:28px !important;
		}
	</style>
</head>
<body>
	<form id="submitForm">
		<input type="hidden" id="alarmId" name="alarmId" value="<#if bo.alarmId??>${bo.alarmId?c}</#if>" />
		<div id="content-d" class="MC_con content light">
			<div name="tab" class="NorForm">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td >
								<label class="LabName"><span>告警类型：</span></label>
								<input type="hidden" name="alarmType" id="alarmType" value='${(bo.alarmType)!}' />
								<input type="text" id="alarmTypeName" name="alarmTypeName" value="" class="inp1 easyui-validatebox" data-options="tipPosition:'bottom',required:true" />
						</td>
						<td >
								<label class="LabName"><span>告警级别：</span></label>
								<input type="hidden" name="alarmLevel" id="alarmLevel" value='${(bo.alarmLevel)!}' />
								<input type="text" id="alarmLevelName" name="alarmLevelName" value="" class="inp1 easyui-validatebox" data-options="validType:'maxLength[10]', tipPosition:'bottom',required:true" />
						</td>
					</tr>
				   <tr>
						<td>
							<label class="LabName"><span>链接地址：</span></label>
							<input  type="text" id="alarmUrl" name="alarmUrl" value="${(bo.alarmUrl)!}" class="inp1 easyui-validatebox" data-options="validType:['url','length[0,200]'], tipPosition:'bottom'" />
						</td>
						<td>
							<label class="LabName"><span>告警来源：</span></label>
							<input  type="text" id="alarmSource" name="alarmSource" value="${(bo.alarmSource)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[20]', tipPosition:'bottom'" />
						</td>
					</tr>
					<tr>
  						<td class="LeftTd"><label class="LabName"><span>失效时间：</span></label>
                        	<input id="invalidDate" name="invalidDate" type="text" data-options="tipPosition:'bottom'" class="inp1 fl Wdate easyui-validatebox"  onclick="WdatePicker({isShowWeek:true,el:'invalidDate',dateFmt:'yyyy-MM-dd HH:mm:ss'})" value="<#if bo.invalidDate??>${bo.invalidDate?string('yyyy-MM-dd HH:mm:ss')}</#if>"/>
                        </td>
                        <td>
                        </td>
                    </tr>
            		 <tr>
                        <td colspan="2" class="LeftTd RightTd"><label class="LabName"><span>告警内容：</span></label>
					   <textarea name="alarmContent" id="alarmContent" cols="45" rows="30" style="width: 459px;height: 80px;" class="area1 easyui-validatebox" maxLength="500" data-options="tipPosition:'bottom',validType:['maxLength[500]','characterCheck'],required:true">${(bo.alarmContent)!}</textarea></td>
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
    $(function(){
    //dictCode      value
     	 //加载数据字典：告警类型
        AnoleApi.initTreeComboBox("alarmTypeName", null, '${ZG_ALARM_TYPE!}', function (gridId, items) {
            if (isNotBlankParam(items) && items.length > 0) {
                document.getElementById('alarmType').value = items[0].value;
            }
        }, ["${(bo.alarmType)!}"], {ChooseType: '1', ShowOptions: {EnableToolbar: true}});
        
        //加载数据字典：优先级
        AnoleApi.initTreeComboBox("alarmLevelName", null, '${ZG_ALARM_LEVEL!}', function (gridId, items) {
            if (isNotBlankParam(items) && items.length > 0) {
                document.getElementById('alarmLevel').value = items[0].value;
            }
        }, ["${(bo.alarmLevel)!}"], {ChooseType: '1', ShowOptions: {EnableToolbar: true}});
        
        
    });
	//保存
	function save() {
		var isValid = $('#submitForm').form('validate');
		if (isValid) {
			modleopen(); //打开遮罩层
			$.ajax({
				type: 'POST',
				url: '${rc.getContextPath()}/zhsq/szzg/zgAlarm/save.jhtml',
				data: $('#submitForm').serializeArray(),
				dataType: 'json',
				success: function(data) {
					if (!data.success) {
						$.messager.alert('错误', data.tipMsg, 'error');
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

<@override name = "eventType">
	<td  class="LeftTd">
			<label class="LabName"><span>事件标题：</span></label><div style="width: calc(50vw - 100px);" class="Check_Radio FontDarkBlue">${eventWechat.eventName!}</div>
	</td>
	 <td class="LeftTd">
			<label class="LabName"><span>事件分类：</span></label>
			<input type="hidden" id="eventType" name="eventType" />
			<input type="text" class="inp1 easyui-validatebox" style="width:122px;" id="eventTypeName"  />
	 </td>
</@override>
<@override name = "eventTypeInit">
	AnoleApi.initTreeComboBox("eventTypeName", "eventType", "B508", null, ["${eventWechat.eventType!}"], {
            	ShowOptions: {
            		EnableToolbar : true
            	}
            });
</@override>
<@override name="eventTypeEva">
	"eventType" : $("#eventType").val(),
	"infoOrgCode" : $("#infoOrgCode").val(),
	"longitude" : '${eventWechat.longitude!}',
	"latitude" : '${eventWechat.latitude!}',
</@override>
<@override name = "checkLevel">
	
	if(operateType =='0'){
	
		var eventType = $("#eventType").val();
		if(eventType !='99'){//为入格事件 所属网格需要选择到网格层级或村社区层级
			var gridLevel = $("#gridLevel").val();
			if(gridLevel !='5' && gridLevel !='6'){
				$.messager.alert('提示', '所属网格需要选择到网格或者村社区层级！', 'warning');
				return;
			}
		}
		
		$('#eventTypeName').validatebox({
					required: true
				});
		
	}else if(operateType =='1'){
	
		$('#eventTypeName').validatebox({
					required: false
				});
	}
	
	
</@override>
<@override name = "initGridZtree">
AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId, items) {
				if(isNotBlankParam(items) && items.length > 0) {
					var grid = items[0];
					$("#infoOrgCode").val(grid.orgCode);
					$("#gridLevel").val(grid.gridLevel);
				} 
			});
</@override>
<@extends name="/zzgl/intermediateData/eventWechat/add_eventWechat.ftl" />
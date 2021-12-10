<@override name="handlePageTitle">南昌指挥调度大数据平台工作流办理页面</@override>
<@override name="otherFillingItem">
		<tr id="intervalTr" style="display:none">
			<td>
				<label class="LabName"><span>办理时限：</span></label>
				<input type="text" id="eventHandleInterval" name="eventHandleInterval" class="inp1 easyui-numberbox" data-options="tipPosition:'bottom',max:7,min:1" style="width: 210px; height: 28px;" value="${eventDefaultHandleInterval!'7'}" />
				<label class="LabName" style="float: none; display: inline-block; margin-left: 0; width: 25px;">(天)</label>
			</td>
		</tr>
</@override>

<@override name="isShowOtherFillingItem">	
		//县区分派事件给乡镇街道或县区职能部门时，可设置处理时间
		if(data.isShowInterval) {
			$("#intervalTr").show();
		}else{
			$("#intervalTr").hide();
		}
</@override>
					
					
<@extends name="/zzgl/event/workflow/handle_event_node.ftl" />	
<@override name="extraHiddenParam">
	<input type="hidden" name="isTransferDict" class="queryParam" value="true" />
</@override>
<@override name="createTimeQueryLableName">核查时间：</@override>
<@override name="extraParamTopTr">
	<tr>
		<td>
			<label class="LabName width65px"><span>督查程度：</span></label>
			<input id="inspectDegree" name="inspectDegree" type="text" value="" class="queryParam hide"/>
			<input id="inspectDegreeName" type="text" class="inp1 InpDisable w150" />
		</td>
	</tr>
	<tr>
		<td>
			<label class="LabName width65px"><span>督查类型：</span></label>
			<input id="inspectType" name="inspectType" type="text" value="" class="queryParam hide"/>
			<input id="inspectTypeName" type="text" class="inp1 InpDisable w150" />
		</td>
	</tr>
	<@super></@super>
</@override>
<@override name="extraParamInit">
	AnoleApi.initListComboBox("inspectDegreeName", "inspectDegree", "B191", null, null, {
		IsTriggerDocument: false,
		ShowOptions:{
			EnableToolbar : true
		}
	});
	AnoleApi.initListComboBox("inspectTypeName", "inspectType", "B197", null, null, {
		IsTriggerDocument: false,
		ShowOptions:{
			EnableToolbar : true
		}
	});
	timeAppCheckStatusComboBox = AnoleApi.initListComboBox("timeAppCheckStatusName", "timeAppCheckStatus", null, null, ["3"], {
		DataSrc : [{"name":"待复核", "value":"3"},{"name":"已恢复", "value":"2"},{"name":"已督查", "value":"1"}],
		IsTriggerDocument: false,
		ShowOptions:{
			EnableToolbar : true
		}
	});
</@override>

<@override name="delEventAuditColumns">
{field:'applicationId',checkbox:true,width:40,hidden:'true'},
{field:'timeAppCheckStatusName',title:'复核情况', align:'center',width:fixWidth(0.08),sortable:true},
{field:'eventName',title:'事件标题', align:'center',width:fixWidth(0.2),sortable:true, formatter: clickFormatter},
{field:'createTimeStr',title:'核查时间', align:'center',width:fixWidth(0.1),sortable:true, formatter: dateFormatter},
{field:'reason',title:'督查原因', align:'center',width:fixWidth(0.2),sortable:true, formatter: titleFormatter},
{field:'inspectDegreeName',title:'督查程度', align:'center',width:fixWidth(0.1)},
{field:'inspectTypeName',title:'督查类型', align:'center',width:fixWidth(0.1)},
{field:'checkAdvice',title:'复核意见', align:'center',width:fixWidth(0.2),sortable:true, formatter: titleFormatter},
{field:'eventClass',title:'事件分类', align:'center',width:fixWidth(0.2),sortable:true, formatter: titleFormatter},
{field:'gridPath',title:'所属网格', align:'center',width:fixWidth(0.2),sortable:true, formatter: titleFormatter},
{field:'applicantName',title:'办理人', align:'center',width:fixWidth(0.1),sortable:true, formatter: titleFormatter}
</@override>

<@extends name="/zzgl/timeApplication/delEventApplication/list_app_delEvent_audit.ftl" />
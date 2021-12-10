<@override name="extraHiddenParam">
	<input type="hidden" name="isTransferDict" class="queryParam" value="true" />
</@override>
<@override name="createTimeQueryLableName">核查时间：</@override>
<@override name="detailDataReportWindowTitle">事件督查统计</@override>
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
</@override>

<@override name="delEventColumns">
{field:'applicationId',checkbox:true,width:40,hidden:'true'},
{field:'eventName',title:'事件标题', align:'center',width:fixWidth(0.2),sortable:true, formatter: clickFormatter},
{field:'eventCreateTimeStr',title:'采集时间', align:'center',width:fixWidth(0.1),sortable:true, formatter: dateFormatter},
{field:'createTimeStr',title:'核查时间', align:'center',width:fixWidth(0.1),sortable:true, formatter: dateFormatter},
{field:'reason',title:'督查原因', align:'center',width:fixWidth(0.2),sortable:true, formatter: titleFormatter},
{field:'inspectDegreeName',title:'督查程度', align:'center',width:fixWidth(0.1)},
{field:'inspectTypeName',title:'督查类型', align:'center',width:fixWidth(0.1)},
{field:'eventClass',title:'事件分类', align:'center',width:fixWidth(0.2),sortable:true, formatter: titleFormatter},
{field:'gridPath',title:'所属网格', align:'center',width:fixWidth(0.2),sortable:true, formatter: titleFormatter}
</@override>

<@extends name="/zzgl/timeApplication/delEventApplication/list_app_delEvent.ftl" />
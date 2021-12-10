<@override name="eventExcelPageTitle">
	莘县事件导出列表
</@override>

<@override name="gridPathField">
	{field:'gridPath',title:'所属区域', align:'center',width:fixWidth(0.2),sortable:true, formatter: titleFormatter},
</@override>
							
<@extends name="/zzgl/event/excel_event.ftl" />
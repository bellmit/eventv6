<@override name = "happenTimeColumnBlock">
{field:'happenTimeStr',title:'事发时间',align:'center',width:fixWidth(0.15),sortable:true},
</@override>

<@override name = "occurredColumnBlock">
{field:'occurred',title:'事发地址',align:'center',width:fixWidth(0.15),sortable:true, formatter: titleFormatter},
</@override>

<@extends name="/zzgl/intermediateData/eventWechat/list_eventWechat.ftl" />
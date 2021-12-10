<@override name = "toolbar">
	<div class="MainContent">
	<#include "/zzgl/intermediateData/eventWechat/nanAn/toolbar_eventWechat.ftl" />
</div>
</@override>

<@override name = "detailToEventName">
{field:'eventName',title:'事件标题', align:'left',width:fixWidth(0.2),sortable:true,
				formatter:function(value,rec,rowIndex){
					if(value == null){
							value = "";
				}
				var f = '<a class="eName" href="###" title="'+ rec.eventName +'" onclick="detailToEventVerify(\'' + rec.eventVerifyHashId + '\')")>'+value+'</a>';
				return f;
			}	
},
</@override>
<@extends name="/zzgl/intermediateData/eventWechat/list_eventWechat.ftl" />
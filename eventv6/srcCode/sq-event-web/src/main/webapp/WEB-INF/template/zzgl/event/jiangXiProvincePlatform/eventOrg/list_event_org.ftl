<@override name="extraFormAttributes">
	<input type="hidden" name="isCapEventExtend" class="queryParam" value="${isCapEventExtend!}"/>
</@override>
<@override name="function_addIcons_body">
	var iconDivLength = $("#iconDiv").length;
	if(iconDivLength == 0) {
		var icons = 
		'<div id="iconDiv" class="fl">'+
			'<a href="###" id="_allSearchAnchor" class="icon_select" onclick="iconSearchData(0, this);"><i class="ToolBarAll"></i>所有</a>'+
			'<a href="###" onclick="iconSearchData(1, this);"><i class="ToolBarUrgency"></i>紧急</a>'+
			'<a href="###" onclick="iconSearchData(2, this);"><i class="ToolBarDue"></i>即将逾期</a>'+
			'<a href="###" onclick="iconSearchData(3, this);"><i class="ToolBarOverDue"></i>逾期</a>'+
			'<a href="###" onclick="iconSearchData(4, this);"><i class="ToolBarNormal"></i>正常</a>'+
		'</div>';
		$('.ToolBar').append(icons);
	}
</@override>
<@override name="function_iconSearchData_body">
	var searchArray = new Array();
	
	iconSelect(obj);//选中点击的图标
	switch(index) {
		case 0: {//点击所有图标
			break;
		}
		case 1: {//点击紧急图标
			var urgencyDegree = "02,03,04";
			
			if(urgencyDegree) {
				searchArray["urgencyDegree"] = urgencyDegree;
			}
			
			break;
		}
		case 2: {//点击将到期图标
			searchArray["overdue"] = "1";
			break;
		}
		case 3: {//点击已过期图标
			searchArray["overdue"] = "2";
			break;
		}
		case 4: {//点击正常图标
			searchArray["overdue"] = "0";
			break;
		}
	}
	
	searchData(searchArray);
</@override>

<@extends  name="/zzgl/event/eventOrg/list_event_org.ftl" />
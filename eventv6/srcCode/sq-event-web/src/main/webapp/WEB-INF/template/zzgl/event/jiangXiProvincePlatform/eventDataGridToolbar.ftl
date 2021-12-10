<@override name="eventStatusTrBlock">
<#if eventType?? && eventType != 'draft' && eventType != 'history'>
<tr>
	<td><label class="LabName width65px"><span>事件状态：</span></label>
         <select name="statuss" id="statuss" class="easyui-combobox" style="width: 170px; height:28px;" data-options="panelHeight:100,multiple:true,onSelect:function(record){comboboxSelect(record, this.id);},onUnselect:function(record){comboboxUnselect(record, this.id)}">
			<#if statusDC??>
                <option value="">不限</option>
				<#list statusDC as l>
					<#if l.dictGeneralCode != "06" && l.dictGeneralCode != "99" && l.dictGeneralCode != "05">
                        <option value="${l.dictGeneralCode}">${l.dictName}</option>
					</#if>
				</#list>
			</#if>
        </select>
    </td>
</tr>
<tr>
	<td>
		<label class="LabName width65px"><span>事件子状态：</span></label>
		<input id="subStatus" name="subStatus" type="text" value="" class="queryParam hide"/>
		<input id="subStatusName" name="subStatusName" type="text" class="inp1 selectWidth"/>
	</td>
</tr>
</#if>
</@override>
        
<@override name="extendConditionInit">
<#if eventType?? && eventType != 'draft' && eventType != 'history'>
AnoleApi.initTreeComboBox("subStatusName", "subStatus", "B900", null, null, {
	RenderType : "01",
	ShowOptions: {
		EnableToolbar : true
	}
});
</#if>
</@override>

<@override name="statussResetCondition">
	<#if eventType?? && eventType != 'draft' && eventType != 'history'>
		$('#statuss').combobox('setValue', "");
		<#if statuss??>
			var statusArr = "${statuss}".split(',');
			if($('#statuss').val()!=null) {
				$('#statuss').combobox('setValues', statusArr);
			}
		</#if>
	</#if>
</@override>

<@override name="advancedStatussSearch">
	<#if eventType?? && eventType != 'draft' && eventType != 'history'>
        var eventStatus = "";
        var statuss = $('#statuss').combobox('getValues');

        if(statuss.length > 0){
            var status = "";

            for(var i=0;i<statuss.length;i++){
                status = statuss[i];

                if(status == 'do'){
                    eventStatus += ',00,01,02,03';
                } else if(status == 'end'){
                    eventStatus += ',04';
                } else {
                    eventStatus += "," + status;
                }
            }

            if(eventStatus && eventStatus.length > 0) {
                eventStatus = eventStatus.substring(1);
            }
        }
        if(eventStatus!=null && eventStatus!="") {
            postData["eventStatus"]=eventStatus;
        }
    </#if>
</@override>
        
<@override name="function_addIcons_body">
var iconDivLength = $("#iconDiv").length;
if(iconDivLength == 0){
    var handleDateFlag = $("#handleDateFlag").val();
    var iconObj = {'handleDateFlag_1': 'ToolBarNormal', 'handleDateFlag_2': 'ToolBarDue', 'handleDateFlag_3': 'ToolBarOverDue'};
    var icons = '<div id="iconDiv" class="fl">'+
	<#if eventType??>
		<#if eventType != 'draft'>
			'<a href="###" id="_allSearchAnchor" class="icon_select" onclick="allSearchData(this);"><i class="ToolBarAll"></i>所有</a>'+
		</#if>
		<#if eventType == 'todo'>
			'<a href="###" id="_remindSearchAnchor" onclick="remindSearchData(1, this);"><i class="ToolBarRemind"></i>催办</a>'+
		</#if>
		<#if eventType != 'draft'>
			'<a href="###" id="_urgencySearchAnchor" onclick="urgencySearchData(this);"><i class="ToolBarUrgency"></i>紧急</a>'+
			<#if eventType == 'all'>
				'<a href="###" id="_superviseSearchAnchor" onclick="superviseSearchData(this);"><i class="ToolBarSupervise"></i>已督办</a>'+
			</#if>
			<#if eventType != 'done'>
			'<a href="###" id="_dueSearchAnchor" onclick="handleSearchData(\'1\', this);"><i class="ToolBarDue"></i>即将逾期</a>'+
			</#if>
			'<a href="###" id="_overDueSearchAnchor" onclick="handleSearchData(\'2\', this);"><i class="ToolBarOverDue"></i>逾期</a>'+
			'<a href="###" id="_normalSearchAnchor" onclick="handleSearchData(\'0\', this);"><i class="ToolBarNormal"></i>正常</a>'+
		</#if>
	</#if>
	'</div>';
	
    $('#ToolBar').append(icons);
    var iconClass = iconObj["handleDateFlag_" + handleDateFlag];
    if(iconClass) {
        $("#iconDiv > a").hide();
        $("#iconDiv > a > i." + iconClass).parent().addClass("icon_select icon_select_pre").css("display", "inline-block");
        $("#iconDiv > a:hidden").remove();
    }
    
    //快捷图标有变更，需要重新获取
    iconLen = $('#iconDiv > a').length;
    
    if(iconLen > 5) {
    	$('#iconDiv').width($('#iconDiv > a').outerWidth() * iconLen);
    }
    
    var toolBarWidth = $('#toolBarFrDiv').outerWidth(true),
    	toolBarArchorWidth = 0;
    	
    $('#toolBarFrDiv > a').each(function() {
    	toolBarArchorWidth += $(this).outerWidth(true);
    });
    
    if(toolBarArchorWidth < toolBarWidth || toolBarArchorWidth == 0) {
    	$('#toolBarFrDiv').width(toolBarArchorWidth);
    }
}
</@override>


<@override name="iconDivParamsBlock">
if(iconDivLen > 0) {
	var iconId = "";
	
	iconDiv.each(function(){
		iconId = $(this).attr("id");
	});
	
	if(iconId == "_remindSearchAnchor") {//催办
		postData["remindStatus"] = "1";
	} else if(iconId == "_urgencySearchAnchor") {//紧急
		postData["urgencyDegree"] = "02,03,04";
	} else if(iconId == "_dueSearchAnchor") {//即将逾期
		postData["overdue"] = "1";
	} else if(iconId == "_overDueSearchAnchor") {//逾期
		postData["overdue"] = "2";
	} else if(iconId == "_normalSearchAnchor") {//正常
		postData["overdue"] = "0";
	}
}
</@override>





<@override name="iconDivClickBlock">
function allSearchData(obj) {//点击所有图标
    iconSelect(obj);
    searchData();
}

function urgencySearchData(obj){//点击紧急图标
    var searchArray = {};
    iconSelect(obj);

    searchArray["urgencyDegree"] = "02,03,04";
    searchData(null, searchArray);
}

function superviseSearchData(obj) {//点击督办图标
	var searchArray = {};
	iconSelect(obj);
	
	searchArray["superviseMark"] = "1";
	searchData(null, searchArray);
}

function handleSearchData(handleStatus, obj){//点击超时、即将超时图标
    var searchArray = {};
    iconSelect(obj);

    searchArray["overdue"] = handleStatus;
    searchData(null, searchArray);
}

function remindSearchData(remindStatus, obj){//点击催办图标
    var searchArray = {};
    iconSelect(obj);

    searchArray["remindStatus"] = remindStatus;
    searchData(null, searchArray);
}

</@override>
<@override name="detailEventPageInfo">
showMaxJqueryWindow("查看事件信息", url, fetchWinWidth({maxWidth:1000}), defaultShowWindowHeight && defaultShowWindowHeight<document.body.clientHeight?defaultShowWindowHeight:undefined, true);
</@override>

<@extends name="/zzgl/event/eventDataGridToolbar.ftl" />
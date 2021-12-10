<#include "/component/ComboBox.ftl" />


<style type="text/css">
    .width65px{width:105px;}
    .selectWidth{width: 159px;}
    /*图标选中凹陷效果只有在ie9及其以上才有效果*/
    .icon_select{ background:#ccc;box-shadow:inset 1px 1px 0px 0px #999; border-radius:3px; height:23px; line-height:23px; display:inline-block; padding:0 15px 0 0;text-align:center; margin-left: 10px;}
	<@block name="extraStyle"></@block>
	.textbox-text{padding:4px 0px !important;}
	.dateRenderWidth{width: 195px;}
</style>
<script type="text/javascript" src="${rc.getContextPath()}/js/openWin.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/plugIn/plug_in.js"></script>
<script type="text/javascript" src="${ANOLE_COMPONENT_URL}/js/components/date/jquery.anole.date.js"></script>
<link rel="stylesheet" href="${ANOLE_COMPONENT_URL}/js/components/date/css/date.css" type="text/css"/>
<div id="jqueryToolbar">
    <form id="eventQueryForm">

        <div class="ConSearch">
            <input type="hidden" id="handleDateFlag" name="handleDateFlag" value="${handleDateFlag!}" class="queryParam" />
            <input type="hidden" id="typesForList" name="typesForList" value="${typesForList!}" class="queryParam" />
            <input type="hidden" id="eventAttrTrigger" name="eventAttrTrigger" value="${eventAttrTrigger!}" class="queryParam" />
            <input type="hidden" id="isRemoveTypes" name="isRemoveTypes" value="<#if isRemoveTypes??>${isRemoveTypes?c}<#else>false</#if>" class="queryParam" />
            <input type="hidden" id="eventType" name="eventType" value ="${eventType!}" class="queryParam"/>
            <input type="hidden" id="listModel" value="${model!}" />

            <div class="fl">
                <ul>
                    <li>事件状态：</li>
                    <li>
                        <select name="statuss" id="statuss" class="easyui-combobox" style="width:135px; height:26px; *height:28px;" data-options="panelHeight:100,multiple:true,onSelect:function(record){comboboxSelect(record, this.id);},onUnselect:function(record){comboboxUnselect(record, this.id)}">
							<#if statusDC??>
                                <option value="">不限</option>
								<#list statusDC as l>
									<#if l.dictGeneralCode != "06" && l.dictGeneralCode != "99" && l.dictGeneralCode != "05">
                                        <option value="${l.dictGeneralCode}">${l.dictName}</option>
									</#if>
								</#list>
							</#if>
                        </select>
                    </li>
                    <li>事发时间：</li>
                    <li>
                    	<input class="inp1 hide queryParam" type="text" id="happenTimeStart" name="happenTimeStart" value="${happenTimeStart!}"></input>
                    	<input class="inp1 hide queryParam" type="text" id="happenTimeEnd" name="happenTimeEnd" value="${happenTimeEnd!}"></input>
                    	<input type="text" id="_happenTimeDateRender" class="inp1 InpDisable dateRenderWidth" value="${happenTimeStart!}<#if happenTimeStart?? && happenTimeEnd??> ~ </#if>${happenTimeEnd!}"/>
                    </li>
                </ul>
            </div>
            <div class="btns">
                <ul>
                    <li><a href="###" class="chaxun" title="查询按钮" onclick="conditionSearch()">查询</a></li>
                    <li><a href="###" class="chongzhi" title="重置查询条件" onclick="resetCondition()">重置</a></li>
                </ul>
            </div>
            <div class="clear"></div>‍

        </div>
        <div class="h_10 clear"></div>
        <div class="ToolBar" id="ToolBar">
            <div class="blind"></div><!-- 文字提示 -->
            <script type="text/javascript">
                function DivHide(){
                    $(".blind").slideUp();//窗帘效果展开
                }
                function DivShow(msg){
                    $(".blind").html(msg);
                    $(".blind").slideDown();//窗帘效果展开
                    setTimeout("this.DivHide()",800);
                }
            </script>
            <@block name="changeEventList">
             <div class="tool fr">
	            <#if model?? && model=='e'>
		           <a id="export" href="javascript:void(0)" onclick="doExportExcel()" class="NorToolBtn ExportBtn">确认导出</a>
		        </#if> 
			</div>
			</@block>
        </div>
    </form>
    <!-- 导出表单 -->
     <form id="excelForm" name="excelForm" action="toExport.jhtml" method="post">
			<input type="hidden" id="eventIdList" name="eventIdList" value="" />
			<input type="hidden" id="imageList" name="imageList" value="" />
			<input type="hidden" name="listType" value="9" />
			<input type="hidden" id="exportName" name="exportName" value="事件导出" />
			<input type="hidden" name="excelType" value="3" />
			<input type="hidden" name="isCapCurHandlerName" value="true" />  
		</form>

</div>

<script type="text/javascript">

    var startInfoOrgCode = '${infoOrgCode!}',
    	taskReceivedStausComboBox = null,
    	orderByFieldComboBox = null,
    	happenTimeDateRender = null;

    $(function(){
		<#if statuss??>
	        var statusArr = "${statuss}".split(',');
	        if($('#statuss').val()!=null)
	            $('#statuss').combobox('setValues', statusArr);
		</#if>
		
		if($('#_happenTimeDateRender').length > 0) {
			happenTimeDateRender = init4DateRender('_happenTimeDateRender', 'happenTimeStart', 'happenTimeEnd');
		}
    });

    function comboboxSelect(record, id, exceptValue){
        var exceptAttr = [];

        if(exceptValue==undefined || exceptValue==null){
            exceptValue = "";
            exceptAttr[0] = "";
        }else if(typeof exceptValue == 'string'){
            exceptAttr = exceptValue.split(',');
        }

        if(exceptValue.indexOf(record.value) != -1){
            $('#'+id).combobox("setValue", record.value);
        }else{
            for(var index = 0, len = exceptAttr.length; index < len; index++){
                $('#'+id).combobox("unselect", exceptAttr[index]);
            }
        }
    }

    function comboboxUnselect(record, id){
        var values = $('#'+id).combobox("getValues");
        if(values.length == 0){
            $('#'+id).combobox("setValue", "");
        }
    }

    function comboboxSetSelect(id, selectStr){//点击下拉列表对应图标
        if(id!=undefined && id!=null && id!="" && selectStr!=undefined && selectStr!=null && selectStr!=""){
            var comboboxItems = $('#'+id).combobox('getData');
            for(var index = 0, len = comboboxItems.length; index < len; index++){
                var itemValue = comboboxItems[index].value;
                if(itemValue!="" && selectStr.indexOf(itemValue)!=-1){
                    $('#'+id).combobox('select', itemValue);
                }
            }
        }
    }

    function radionCheck(orderType) {
        var orderField = $("#orderByFieldInput").val();

        if(isBlankStringTrim(orderType)) {
            orderType = $('#orderByFieldRadioTr input[name="orderByRadio"]:checked').eq(0).val();
        }

        $("#orderByField").val(orderField + " " + orderType);
    }

    function sendMsg(type,table) {
        var nameAndPhones = "";
        //判断是否显示了列表
        if($('#drugRecordContentDiv').length==0 || document.getElementById('drugRecordContentDiv').style.display != 'none'){
            var rows = $('#' + table).datagrid('getSelections');
            for ( var i = 0; i < rows.length; i++) {
                if (type == "TXT" || type == "M_VOX") {
                    if (rows[i].residentMobile != null && nameAndPhones.indexOf(rows[i].residentMobile)<0){
                        nameAndPhones += rows[i].name + "(" + rows[i].residentMobile + ");";
                        continue;
                    }
                }else{
                    if (rows[i].phone != null && nameAndPhones.indexOf(rows[i].phone)<0){
                        nameAndPhones += rows[i].name + "(" + rows[i].phone + ");";
                    }
                }
            }
            if("TXT"==type||"M_VOX"==type){
                $('#commonMsgForm').attr("commonMsgType","sms");
            }else{
                $('#commonMsgForm').attr("commonMsgType","vox");
            }
            $('#commonNameAndPhones').val(nameAndPhones);
            $('#commonMsgForm').submit();
        }
    }

    function add() {
        var url = '${rc.getContextPath()}/zhsq/event/eventDisposalController/toAddEventByType.jhtml?typesForList=${typesForList!}&trigger=${trigger!}';
	<#if isRemoveTypes??>
        url += '&isRemoveTypes=${isRemoveTypes?c}';
	</#if>
        showMaxJqueryWindow("新增事件信息", url, fetchWinWidth(), fetchWinHeight(), true);
    }

    function edit() {
        if(idStr==null || idStr=="") {
            $.messager.alert('提示','请选择一条编辑的数据再执行此操作!','warning');
            return;
        }
        var url = '${rc.getContextPath()}/zhsq/event/eventDisposalController/toAddEventByType.jhtml?eventId='+idStr+'&typesForList=${typesForList!}&trigger=${trigger!}';
	<#if isRemoveTypes??>
        url += '&isRemoveTypes=${isRemoveTypes?c}';
	</#if>
        showMaxJqueryWindow("编辑事件信息", url, fetchWinWidth(), fetchWinHeight(), true);
    }

    function print() {
        if(idStr==null || idStr=="") {
            $.messager.alert('提示','请选中一条要打印的数据再执行此操作!','warning');
            return;
        }
        if(_bizPlatform == "001"){
            $.messager.alert('提示','旧平台事件暂不支持打印!','warning');
            return;
        }else{
            var url = '${rc.getContextPath()}/zhsq/event/eventDisposalController/print.jhtml?eventId='+idStr;
            winOpenFullScreen(url, "打印事件详情");
        }
    }

    function del() {
        if(idStr==null || idStr=="") {
            $.messager.alert('提示','一次只能删除一条事件,不允许批量删除!','warning');
            return;
        }
        $.messager.confirm('提示', '您确定删除选中的事件吗？', function(r){
            if(_bizPlatform == "001"){
                $.ajax({
                    type: "POST",
                    url: '${rc.getContextPath()}/zhsq/event/eventDisposalController/delOldEvent.jhtml',
                    data: 'oldidStr='+idStr,
                    dataType:"json",
                    success: function(data){
                        modleclose();
                        $.messager.alert('提示', '事件删除成功！', 'info');
					<#if model?? && model == 'l'>
                        loadMessage((pageNum?pageNum:1),$('#pageSize').val());
					<#else>
                        flashData(true);
					</#if>

                    },
                    error:function(data){
                        $.messager.alert('错误','连接超时！','error');
                    }
                });
            }else{
                if (r){
                    modleopen();
                    $.ajax({
                        type: "POST",
                        url: '${rc.getContextPath()}/zhsq/event/eventDisposalController/delEvent.jhtml',
                        data: 'idStr='+idStr,
                        dataType:"json",
                        success: function(data){
                            modleclose();
                            $.messager.alert('提示', '事件删除成功！', 'info');
						<#if model?? && model == 'l'>
                            loadMessage((pageNum?pageNum:1),$('#pageSize').val());
						<#else>
                            flashData(true);
						</#if>
                        },
                        error:function(data){
                            $.messager.alert('错误','事件删除连接超时！','error');
                        }
                    });
                }
            }
        });
    }

    function restart() {
        if(idStr) {
            var status = null;

            if($('#list').length) {
                var item = $('#list').datagrid('getSelected');
                if(item.length == 0) {
                    $.messager.alert('警告','请选择需要重启操作的事件!','warning');
                } else {
                    status = item.status;
                }
            } else if($("#"+ idStr +"_status").length) {
                status = $("#"+ idStr +"_status").val();
            }

            if(status && status != '04') {
                $.messager.alert('警告', '该事件尚未归档!', 'warning');
            } else {
                var url = '${rc.getContextPath()}/zhsq/event/eventDisposalController/toRestartEvent.jhtml?eventId='+idStr+'&typesForList=${typesForList!}&trigger=${trigger!}';
			<#if isRemoveTypes??>
                url += '&isRemoveTypes=${isRemoveTypes?c}';
			</#if>
                showMaxJqueryWindow("重启事件信息", url, 800, 400, true);
            }
        } else {
            $.messager.alert('警告','请选择需要重启操作的事件!','warning');
        }
    }

    function reportToDs(){
        if(idStr==null || idStr=="") {
            $.messager.alert('提示','请选中要上报的数据再执行此操作!','warning');
            return;
        }else{
            $.messager.confirm('提示', '您确定上报选中的事件吗？', function(r){
                if(r){
                    modleopen();
                    $.ajax({
                        type: "POST",
                        url : '${rc.getContextPath()}/zhsq/event/eventDisposalDocking/toAddEventInter.jhtml?destPlatform=004&eventId='+idStr,
                        data: '',
                        dataType:"json",
                        success: function(data){
                            var result = data.result;
                            if(result > 0){
                                $.messager.alert('提示','事件上报成功！','info');
                            }else if(result == 0){
                                $.messager.alert('提示','事件已上报！','info');
                            }else{
                                $.messager.alert('提示','事件上报失败！','info');
                            }
                            searchData();
                            modleclose();
                        },
                        error:function(data){
                            $.messager.alert('错误','连接错误！','error');
                            modleclose();
                        }
                    });
                }
            });
        }

    }

    function changeEventList() {
        var urlSearch = window.location.search;//获取当前连接携带的参数
        var extraParams = '${extraParams!}';
        var url = '${rc.getContextPath()}/zhsq/event/eventDisposalController/listEvent.jhtml?t=${eventType}&model=${model}&trigger=${trigger!}&eventAttrTrigger=${eventAttrTrigger!}';

        if(extraParams != ''){
            url += '&extraParams='+encodeURIComponent(extraParams);

        }

        if(isNotBlankStringTrim(urlSearch)) {
            var codeKey = "system_privilege_code",
                    actionKey = "system_privilege_action",
                    codeIndex = urlSearch.indexOf(codeKey),
                    actionIndex = urlSearch.indexOf(actionKey),
                    nameKey = "";

            if(codeIndex > 0 || actionIndex > 0) {
                urlSearch = urlSearch.substring(1);//去除起始的问号

                var paramsArray = urlSearch.split('&'),//分离各个参数
                        paramArray = null,		//[0]key [1]value
                        paramItem = null,		//paramsArray的组员
                        paramObj = {},			//转换后的参数对象
                        paramArrayKey = null,	//参数key
                        paramArrayVal = null;	//参数value

                for(var index = 0, len = paramsArray.length; index < len; index++) {//将url参数转换为对象
                    paramItem = paramsArray[index];

                    if(isNotBlankStringTrim(paramItem) && paramItem.indexOf("=") > 0) {
                        paramArray = paramItem.split("=");//分离参数的key和value，但是如果参数值中含有等号，则会导致使用有误

                        if(paramArray.length == 2) {
                            paramArrayKey = paramArray[0];
                            paramArrayVal = paramArray[1];

                            if(isNotBlankStringTrim(paramArrayKey)) {//参数Key有效时，才添加参数
                                paramArrayKey = $.trim(paramArrayKey);//去除收尾空格

                                if(isBlankStringTrim(paramArrayVal)) {//防止添加了undefined
                                    paramArrayVal = "";
                                }

                                paramObj[paramArrayKey] = paramArrayVal;
                            }
                        }
                    }
                }

                if(codeIndex > 0) {
                    nameKey = codeKey;
                } else if(actionIndex > 0) {
                    nameKey = actionKey;
                }

                if(isNotBlankStringTrim(nameKey)) {
                    url += '&' + nameKey + '=' + paramObj[nameKey];//增添权限参数
                }
            }
        }

        $("#changeEventList").attr("href", url);
    }

    function resetCondition() {//重置
    	clear4DateRender();
    	
        $('#eventQueryForm')[0].reset();

        $("#keyWord").val("事件描述/标题/事发详址");
        $("#keyWord").attr('style','font-size:12px;color:gray; width:150px;');
	<#if eventType=="all" || eventType=="attention">
        $('#statuss').combobox('setValue', "");
		<#if statuss??>
            var statusArr = "${statuss}".split(',');
            if($('#statuss').val()!=null)
                $('#statuss').combobox('setValues', statusArr);
		</#if>
	</#if>

        allSearchData($("#_allSearchAnchor"));
    }

    function conditionSearch(){//查询
        var iconDiv = $("#iconDiv > a[class='icon_select']");
        if(iconDiv.length > 0) {
            iconDiv.eq(0).click();
        } else {
            searchData();
        }

    }

    function _onkeydown(){
        var keyCode = event.keyCode;
        if(keyCode == 13){
            conditionSearch();
        }
    }

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

    function handleSearchData(handleStatus, obj){//点击超时、即将超时图标
        var searchArray = {};
        iconSelect(obj);

        //searchArray["handleStatus"] = handleStatus;
        searchArray["handleDateFlag"] = handleStatus;
        searchData(null, searchArray);
    }

    function remindSearchData(remindStatus, obj){//点击催办图标
        var searchArray = {};
        iconSelect(obj);

        searchArray["remindStatus"] = remindStatus;
        searchData(null, searchArray);
    }
    
    function addIcons(){//id在column_event.ftl中使用
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
                        '<a href="###" id="_dueSearchAnchor" onclick="handleSearchData(\'2\', this);"><i class="ToolBarDue"></i>将到期</a>'+
                        '<a href="###" id="_overDueSearchAnchor" onclick="handleSearchData(\'3\', this);"><i class="ToolBarOverDue"></i>已过期</a>'+
                        '<a href="###" id="_normalSearchAnchor" onclick="handleSearchData(\'1\', this);"><i class="ToolBarNormal"></i>正常</a>'+
				</#if>
			</#if>
                    '</div>';
            $('.ToolBar').append(icons);
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
        }
    }

    function iconSelect(obj){//为选择的图标增添凹陷效果
        if($("#iconDiv > a > i.icon_select_pre").length == 0) {
            if(isNotBlankParam(obj)){
                iconUnSelect();
                $(obj).addClass('icon_select');
            }
        }
    }

    function iconUnSelect(){//去除图片的凹陷效果
        $("#iconDiv > a[class='icon_select']").each(function(){
            $(this).removeClass('icon_select');
        });
    }

    function queryData(searchArray){
        var postData = {};
        if(searchArray!=undefined && searchArray!=null){
            postData = searchArray;
        }
        
        <@block name="extraQueryParams"></@block>

        $("#eventQueryForm .queryParam").each(function() {
            var name = $(this).attr("name"), val = $(this).val();

            if(name && val) {
                postData[name] = val;
            }
        });

        $("#eventQueryForm .queryComboboxId").each(function() {
            var name = $(this).val(), val = $('#' + name).combobox('getValues');

            if(name && val && isBlankParam(postData[name])) {
                postData[name] = val;
            }
        });

        var keyWord = $("#keyWord").val();
        if(keyWord!=null && keyWord!="" && keyWord!="事件描述/标题/事发详址") {
            postData["keyWord"]=keyWord;
        }
        
        <#if eventType=="view" || eventType=="all" || eventType=="toRemind" || eventType=="history">
        	var infoOrgCode = $("#infoOrgCode").val();
        	if(infoOrgCode==null || infoOrgCode=="") {
        		 postData['eInfoOrgCode'] = startInfoOrgCode;
        	}
        </#if>

        //高级查询
        <#if eventType=="all" || eventType=="attention">
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
        
        postData["typesForList"] = "${typesForList!}";
        postData["eventAttrTrigger"] = "${eventAttrTrigger!}";
        postData["isRemoveTypes"] = "<#if isRemoveTypes??>${isRemoveTypes?c}</#if>";
        
        var iconDiv = $("#iconDiv > a[class='icon_select']"),
        	  iconDivLen = iconDiv.length;
		
		if(iconDivLen > 0) {
			var iconId = "";
			
			iconDiv.each(function(){
				iconId = $(this).attr("id");
			});
			
			if(iconId == "_remindSearchAnchor") {//催办
				postData["remindStatus"] = "1";
			} else if(iconId == "_urgencySearchAnchor") {//紧急
				postData["urgencyDegree"] = "02,03,04";
			} else if(iconId == "_dueSearchAnchor") {//将到期
				postData["handleDateFlag"] = "2";
			} else if(iconId == "_overDueSearchAnchor") {//已过期
				postData["handleDateFlag"] = "3";
			} else if(iconId == "_normalSearchAnchor") {//正常
				postData["handleDateFlag"] = "1";
			}
		}
		
        return postData;
    }

    var isEditFlag;
    function locationTagging(gridId,x,y,mapt,isEdit){
        var callBackUrl = '${SQ_ZHSQ_EVENT_URL}/zhsq/map/arcgis/arcgis/toArcgisCrossDomain.jhtml'
        var width = 480;
        var height = 380;
        var mapType = 'EVENT_V1';
        isEditFlag = isEdit;
        initMapMarkerInfoSelector(gridId,callBackUrl,x,y,mapt,width,height,isEdit,mapType)
    }
    function mapMarkerSelectorCallback(mapt, x, y){//将标注信息传递到新增、编辑页面
        var childIframeContents = $("#"+maxJqueryWindowId).find("iframe").contents();
        childIframeContents.find("#mapt").val(mapt);
        childIframeContents.find("#x").val(x);
        childIframeContents.find("#y").val(y);
        var showName = "修改地理位置";
        if(isEditFlag == false) {
            showName = "查看地理位置";
        }
        childIframeContents.find("#mapTab").html(showName);
        childIframeContents.find(".mapTab").addClass("mapTab2");
        closeMaxJqueryWindowForCross();
    }
    //导出
    function doExportExcel(){    
    	$.messager.confirm('提示', '是否导出查询数据？', function(r){
    		if (r) {  
    			document.excelForm.submit();  
    		}
    	});

    }
    
    function init4DateRender(renderId, startTimeId, endTimeId) {
		var dateRender = null;
		
		dateRender = $('#' + renderId).anoleDateRender({
			BackfillType : "1",
			ChoiceType : "1",		// 选择方式（0-起始和结束时间必须都有，1-起始和结束时间有一个即可，2-起始和结束时间随意）
			ShowOptions : {
				TabItems : ["常用", "年", "季", "月", "清空"]
			},
			BackEvents : {
				OnSelected : function(api) {
					$("#" + startTimeId).val(api.getStartDate());
					$("#" + endTimeId).val(api.getEndDate());
				},
				OnCleared : function() {
					$("#" + startTimeId).val('');
					$("#" + endTimeId).val('');
				}
			}
		}).anoleDateApi();
		
		return dateRender;
	}
	
	function clear4DateRender() {
		if(happenTimeDateRender != null) {
			happenTimeDateRender.doClear();
		}
	}
</script>
<#include "/component/ComboBox.ftl" />
<style type="text/css">
.width65px{width:75px;}
/*图标选中凹陷效果只有在ie9及其以上才有效果*/
.icon_select{ background:#ccc;box-shadow:inset 1px 1px 0px 0px #999; border-radius:3px; height:23px; line-height:23px; display:inline-block; padding:0 15px 0 0;text-align:center; margin-left: 10px;}
</style>
<script type="text/javascript" src="${rc.getContextPath()}/js/openWin.js"></script>
<div id="jqueryToolbar">
	<div class="ConSearch">
        <form id="searchForm" name="searchForm" action="">
        <div class="fl">
        	<ul>
        		<li>所属网格：</li>
                <li><input id="infoOrgCode" name="infoOrgCode" type="text" value="${infoOrgCode!''}" style="display:none;"/><input id="gridId" name="gridId" type="text" value="${gridId!''}" style="display:none;"/><input id="gridName" name="gridName" type="text" class="inp1 InpDisable" style="width:150px;" value="${gridName!''}"/></li>
            	<li>事件分类：</li>
                <li>
                	<input id="type" name="type" type="text" value="" style="display:none;"/>
                	<input id="typeName" name="typeName" type="text" class="inp1 InpDisable" style="width:150px;"/>
                </li>
            	<li>关键字：</li>
                <li><input name="keyWord" type="text" class="inp1" id="keyWord" value="事件描述/标题/事发详址" style="color:gray; width:200px;" onfocus="if(this.value=='事件描述/标题/事发详址'){this.value='';}$(this).attr('style','width:200px;');" onblur="if(this.value==''){$(this).attr('style','color:gray;width:200px;');this.value='事件描述/标题/事发详址';}" onkeydown="_onkeydown();" /></li>
            	<li style="position:relative;">
            		<a href="javascript:void(0)" class="AdvanceSearchBtn FontDarkBlue">高级查询</a>
            		<div class="AdvanceSearch DropDownList hide" style="width:375px; top: 42px; left: -130px;">
                        <div class="LeftShadow">
                            <div class="RightShadow">
                                <div class="list NorForm">
                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                      <tr>
                                        <td><label class="LabName width65px"><span>事件编号：</span></label><input class="inp1" type="text" id="code" name="code" style="width:135px;"></input></td>
                                      </tr>
                                      <tr>
                                        <td><label class="LabName width65px"><span>发生时间：</span></label><input class="inp1 Wdate fl" type="text" id="happenTimeStart" name="happenTimeStart" style="width:110px; *width:100px; cursor:pointer;" onclick="WdatePicker({readOnly:true})" readonly="readonly"></input><span class="Check_Radio" style="padding:0 5px;">至</span><input class="inp1 Wdate fl" type="text" id="happenTimeEnd" name="happenTimeEnd" style="width:110px; *width:100px; cursor:pointer;" onclick="WdatePicker({readOnly:true})" readonly="readonly"></input></td>
                                      </tr>
                                      <tr>
					                    <td><label class="LabName width65px"><span>采集时间：</span></label><input class="inp1 Wdate fl" type="text" id="createTimeStart" name="createTimeStart" style="width:110px; *width:100px; cursor:pointer;" onclick="WdatePicker({readOnly:true})" readonly="readonly" value="${createTimeStart!''}"></input><span class="Check_Radio" style="padding:0 5px;">至</span><input class="inp1 Wdate fl" type="text" id="createTimeEnd" name="createTimeEnd" style="width:110px; *width:100px; cursor:pointer;" onclick="WdatePicker({readOnly:true})" readonly="readonly" value="${createTimeEnd!''}"></input></td>
					                  </tr>
					                  <tr>
					                    <td><label class="LabName width65px"><span>采集人：</span></label><input class="inp1" type="text" id="contactUser" name="contactUser" style="width:135px;"></input></td>
					                  </tr>
                                      <tr>
					                    <td><label class="LabName width65px"><span>采集渠道：</span></label>
					                    	<select name="collectWays" id="collectWays" class="easyui-combobox" style="width:135px; height:26px; *height:28px;" data-options="panelHeight:100,multiple:true,onSelect:function(record){comboboxSelect(record, this.id);},onUnselect:function(record){comboboxUnselect(record, this.id)}">
								    			<#if collectWayDC??>
								    				<option value="">不限</option>
													<#list collectWayDC as l>
														<option value="${l.dictGeneralCode}">${l.dictName}</option>
													</#list>
												</#if>
								    		</select>
					                    </td>
					                  </tr>
					                  <tr>
					                    <td><label class="LabName width65px"><span>信息来源：</span></label>
					                    	<select name="sources" id="sources" class="easyui-combobox" style="width:135px; height:26px; *height:28px;" data-options="panelHeight:100,multiple:true,onSelect:function(record){comboboxSelect(record, this.id);},onUnselect:function(record){comboboxUnselect(record, this.id)}">
								    			<#if sourceDC??>
								    				<option value="">不限</option>
													<#list sourceDC as l>
														<option value="${l.dictGeneralCode}">${l.dictName}</option>
													</#list>
												</#if>
								    		</select>
					                    </td>
					                  </tr>
					                  <tr>
					                    <td>
					                    	<label class="LabName width65px"><span>影响范围：</span></label>
					                        <select name="influenceDegrees" id="influenceDegree" class="easyui-combobox" style="width:135px; height:26px; *height:28px;" data-options="panelHeight:100,multiple:true,onSelect:function(record){comboboxSelect(record, this.id);},onUnselect:function(record){comboboxUnselect(record, this.id)}">
								    			<#if influenceDegreeDC??>
								    				<option value="">不限</option>
													<#list influenceDegreeDC as l>
														<option value="${l.dictGeneralCode}">${l.dictName}</option>
													</#list>
												</#if>
								    		</select>
					                    </td>
					                  </tr>
					                  <tr>
					                    <td><label class="LabName width65px"><span>紧急程度：</span></label>
					                        <select name="urgencyDegrees" id="urgencyDegree" class="easyui-combobox" style="width:135px; height:26px; *height:28px;" data-options="panelHeight:100,multiple:true,onSelect:function(record){comboboxSelect(record, this.id);},onUnselect:function(record){comboboxUnselect(record, this.id)}">
								    			<#if urgencyDegreeDC??>
								    				<option value="">不限</option>
													<#list urgencyDegreeDC as l>
														<option value="${l.dictGeneralCode}">${l.dictName}</option>
													</#list>
												</#if>
								    		</select>
					                    </td>
					                  </tr>
                                    </table>
                                </div>
                                <div class="BottomShadow"></div>
                            </div>
                        </div>
                    </div>
            	</li>
            </ul>
        </div>
        </form>
        <div class="btns">
        	<ul>            	
            	<li><a href="#" class="chaxun" title="查询按钮" onclick="conditionSearch()">查询</a></li>
            	<li><a href="#" class="chongzhi" title="重置查询条件" onclick="resetCondition()">重置</a></li>
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
    </div>
	
</div>

<script type="text/javascript">
	
	function comboboxSelect(record, id){
		if(record.value == ''){
    		$('#'+id).combobox("setValue", "");
    	}else{
    		$('#'+id).combobox("unselect", "");
    	}
	}
	
	function onGridTreeSelected(gridId, items){
		if(items!=undefined && items!=null && items.length>0){
			var grid = items[0];
			$("#infoOrgCode").val(grid.orgCode);
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
	var bigTypeApi = null;
	var pCode = "A001093199";
	<#if pCode??>
		pCode = ${pCode};
	</#if>
	$(function() {
		bigTypeApi = AnoleApi.initTreeComboBox("typeName", "type", pCode, null, ["${type!''}"], {
			ChooseType : "1"
		});
		AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId, items){
			if(items!=undefined && items!=null && items.length>0){
				var grid = items[0];
				$("#infoOrgCode").val(grid.orgCode);
			} 
		});
	});
	
	function add() {
		var url = '${rc.getContextPath()}/zhsq/event/eventDisposalController/toAddEventByType.jhtml';
		showMaxJqueryWindow("新增采集事件", url, 800, 400);
	}
	
	function edit() {
		if(idStr==null || idStr=="") {
			$.messager.alert('提示','请选中要编辑的数据再执行此操作!','warning');
			return;
		}
		//var url = '${rc.getContextPath()}/zhsq/event/eventDisposalController/toEditEvent.jhtml?eventId='+idStr+'&type='+typeVal;
		var url = '${rc.getContextPath()}/zhsq/event/eventDisposalController/toAddEventByType.jhtml?eventId='+idStr;
		showMaxJqueryWindow("编辑采集事件", url, 800, 400);
	}
	
	function print() {
		if(idStr==null || idStr=="") {
			$.messager.alert('提示','请选中要打印的数据再执行此操作!','warning');
			return;
		}
		var url = '${rc.getContextPath()}/zhsq/event/eventDisposalController/print.jhtml?eventId='+idStr+'&instanceId='+_instanceId;
		winOpenFullScreen(url, "打印事件详情");
	}
	
	function del() {
		if(idStr==null || idStr=="") {
			$.messager.alert('提示','请选中要删除的数据再执行此操作!','warning');
			return;
		}
		$.messager.confirm('提示', '您确定删除选中的事件吗？', function(r){
			if (r){
				modleopen();
				$.ajax({
					type: "POST",
					url: '${rc.getContextPath()}/zhsq/event/eventDisposalController/delEvent.jhtml',
					data: 'idStr='+idStr,
					dataType:"json",
					success: function(data){
						modleclose();
						$.messager.alert('提示', '您成功删除'+data.result+'条事件！', 'info');
						$("#list").datagrid('load');
					},
					error:function(data){
						$.messager.alert('错误','连接超时！','error');
					}
				});
			}
		});
	}
	
	function closed(){
		if(idStr==null || idStr=="") {
			$.messager.alert('提示','请选中要结案的数据再执行此操作!','warning');
			return;
		}
		$.messager.confirm('提示', '您确定结案选中的事件吗？', function(r){
			if(r){
				modleopen();
				$.ajax({
					type: "POST",
		    		url : '${rc.getContextPath()}/zhsq/event/eventDisposalController/startWorkFlow.jhtml',
					data: 'eventId='+idStr+'&toClose=1',
					dataType:"json",
					success: function(data){
						startWorkFlow(data);
					},
					error:function(data){
						modleclose();
						$.messager.alert('错误','连接错误！','error');
					}
		    	});
			}
		});
	}
	function resetCondition() {
		gridFlag="0";
		$("#searchForm")[0].reset();
		var type = $("#type").val();
		if(type!=null && type!="") {
			var ary = eval('([' + type + '])');
	    	bigTypeApi.setSelectedNodes(ary);
	    	$("#typeName").val(bigTypeApi.getDataName());
		}
		/*$("#keyWord").val("事件描述/标题/事发详址");
		$("#keyWord").attr('style','font-size:12px;color:gray; width:200px;');
		$("#type").val("");
		$("#typeName").val("");
		$('#gridName').val("");
		$('#infoOrgCode').val("");
		$('#gridId').val("");
		$('#code').val("");
		$('#happenTimeStart').val("");
		$('#happenTimeEnd').val("");
		$('#createTimeStart').val("");
		$('#createTimeEnd').val("");
		$('#contactUser').val("");*/
		$('#collectWays').combobox('setValue', "");
		$('#sources').combobox('setValue', "");
		$('#influenceDegree').combobox('setValue', "");
		$('#urgencyDegree').combobox('setValue', "");
		
		conditionSearch();
	}
	
	function conditionSearch(){//查询
		allSearchData($("#_allSearchAnchor"));
	}
	
	function _onkeydown(){
		var keyCode = event.keyCode;
		if(keyCode == 13){
			searchData(1);
		}
	}
	
	function allSearchData(obj) {//点击所有图标
		iconSelect(obj);
		searchData();
    }
    
	function urgencySearchData(obj){//点击紧急图标
    	var urgencyDegrees = $('#urgencyDegree').combobox('getValues');
    	iconSelect(obj);
    	
		$('#urgencyDegree').combobox('setValue', "");
    	comboboxSetSelect('urgencyDegree', "02,03,04");
    	searchData();
    	$('#urgencyDegree').combobox('setValue', "");
    	comboboxSetSelect('urgencyDegree', urgencyDegrees.toString());
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
    
    function iconSelect(obj){//为选择的图标增添凹陷效果
    	if(isNotBlankParam(obj)){
    		iconUnSelect();
    		$(obj).addClass('icon_select');
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
		var type = $("#type").val();
		if (type != null && type != "") {
			postData["type"]=type;
		} else {
			postData["type"] = "${type!''}";
		}
		var keyWord = $("#keyWord").val();
		if(keyWord!=null && keyWord!="" && keyWord!="事件描述/标题/事发详址") {
			postData["keyWord"]=keyWord;
		}
		var gridId = $("#gridId").val();
		if(gridId!=null && gridId!=""){
			postData['gridId']=gridId;
		}
		var infoOrgCode = $("#infoOrgCode").val();
		if(infoOrgCode!=null && infoOrgCode!=""){
			postData['infoOrgCode']=infoOrgCode;
		}
		postData["eventType"]=eventType;
		postData["eventStatus"]=eventStatus;
		postData["remindStatus"]=remindStatus;
		//高级查询
		var code = $("#code").val();
		if(code!=null && code!="") {
			postData["code"]=code;
		}
		var contactUser = $("#contactUser").val();
		if(contactUser!=null && contactUser!="") {
			postData["contactUser"]=contactUser;
		}
		var happenTimeStart = $('#happenTimeStart').val();
		if(happenTimeStart!=null && happenTimeStart!="") {
			postData["happenTimeStart"]=happenTimeStart;
		}
		var happenTimeEnd = $('#happenTimeEnd').val();
		if(happenTimeEnd!=null && happenTimeEnd!="") {
			postData["happenTimeEnd"]=happenTimeEnd;
		}
		var createTimeStart = $('#createTimeStart').val();
		if(createTimeStart!=null && createTimeStart!="") {
			postData["createTimeStart"]=createTimeStart;
		}
		var createTimeEnd = $('#createTimeEnd').val();
		if(createTimeEnd!=null && createTimeEnd!="") {
			postData["createTimeEnd"]=createTimeEnd;
		}
		
		var collectWays = $('#collectWays').combobox('getValues');
		if(collectWays!=null && collectWays!="") {
			postData["collectWays"]=collectWays;
		}
		var sources = $('#sources').combobox('getValues');
		if(sources!=null && sources!="") {
			postData["sources"]=sources;
		}
		
		var influenceDegrees = $('#influenceDegree').combobox('getValues');
		if(influenceDegrees!=null && influenceDegrees!="") {
			postData["influenceDegrees"]=influenceDegrees;
		}
		var urgencyDegrees = $('#urgencyDegree').combobox('getValues');
		if(urgencyDegrees!=null && urgencyDegrees!="") {
			postData["urgencyDegrees"]=urgencyDegrees;
		}
		if (handleDateFlag != "") {
			postData["handleDateFlag"]=handleDateFlag;
		}
		return postData;
    }
</script>
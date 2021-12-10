<!DOCTYPE html>
<html>
<head>
	<title>所有列表</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<#include "/component/maxJqueryEasyUIWin.ftl" />
	<#include "/component/listSet.ftl" />
	<style type="text/css">
		.width65px{width:75px;}
		.w150{width:150px;}
		.keyBlank{color:gray;}
	</style>
</head>
<body class="easyui-layout">
	<div id="_DivCenter" region="center" >
	   <table id="list"></table>
	</div>
	<div id="jqueryToolbar">
		<div class="ConSearch">
			<form id="searchForm">
			<div class="fl">
				<ul>
					<li>关键字：</li>
	                <li>
		                <input name="keyWord" type="text" class="inp1" id="keyWord" value="可输入案件编号、标题等关键字" style="color:gray; width:170px;" onfocus="if(this.value=='可输入案件编号、标题等关键字'){this.value='';}$(this).attr('style','width:170px;');" onblur="if(this.value==''){$(this).attr('style','color:gray;width:170px;');this.value='可输入案件编号、标题等关键字';}" onkeydown="_onkeydown();" />
	            	</li>
					<li>事件类别：</li>
	                <li>
	                	<input id="type" name="type" type="text" value="" class="queryParam hide"/>
	                	<input id="typeName" name="typeName" type="text" class="inp1 InpDisable w150" />
	                </li>
	                <li style="position:relative;">
	            		<a href="javascript:void(0)" class="AdvanceSearchBtn FontDarkBlue">高级查询</a>
	            		<div class="AdvanceSearch DropDownList hide" style="width:430px; top: 42px; left: -130px;">
	                        <div class="LeftShadow">
	                            <div class="RightShadow">
	                                <div class="list NorForm" style="position:relative;">
	                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	                                    	<tr>
	                                    		<td>
	                                    			<label class="LabName width65px"><span>办理部门：</span></label>
	                                    			<input id="wfCurOrg" name="wfCurOrg" type="text" class="inp1 InpDisable w150" />
	                                    		</td>
	                                    	</tr>
	                                    	<tr>
	                                    		<td>
	                                    			<label class="LabName width65px"><span>提交时间：</span></label>
	                                    			<input type="text" id="happenTimeStr" name="happenTimeStr" class="inp1 Wdate easyui-validatebox" style="width:155px; cursor:pointer;" data-options="required:true" onclick="WdatePicker({readOnly:true, maxDate:'endTimeStr', dateFmt:'yyyy-MM-dd HH:mm:ss', isShowClear:false, isShowToday:false})"  readonly="readonly">
								                		~
								                	<input type="text" id="endTimeStr" name="endTimeStr" class="inp1 Wdate easyui-validatebox" style="width:155px; cursor:pointer;" data-options="required:true" onclick="WdatePicker({readOnly:true, minDate:'happenTimeStr', dateFmt:'yyyy-MM-dd HH:mm:ss', isShowClear:false, isShowToday:false})"  readonly="readonly">
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
	        <div class="btns">
				<ul>
					<li><a href="javascript:;" class="chaxun" title="查询数据" onclick="searchData()">查询</a></li>
		            <li><a href="javascript:;" class="chongzhi" title="重置查询条件" onclick="resetCondition()">重置</a></li>
				</ul>
	        </div>
			</form>
		</div>
		<div class="h_10" id="TenLineHeight1"></div>
		<div class="ToolBar">
			<div class="tool fr">
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	$(function() {
		//加载数据字典：案件类型
		AnoleApi.initListComboBox("typeName", "type", "${caseTypeDict}", null, null, {
			ShowOptions : {
				EnableToolbar : true
			}
		});
		
		loadList(); //加载列表
	});
	
	//加载列表
	function loadList() {
		$('#list').datagrid({
			rownumbers: true, //行号
			fitColumns: true, //自适应宽度
			nowrap: true,
			striped: true,
			singleSelect: true,
			fit: true,
			url: '${rc.getContextPath()}/zhsq/requestion/listDataAll.jhtml',
			//queryParams: $('#searchForm').serializeJson(),
			columns: [[
				{field:'code', title:'案件编号', align:'center', width:100,formatter : function(val, rec) {
					return '<a href="javascript:detail(\''+rec.reqId+'\',\''+rec.tasktype+'\');" >'+val+'</a>';
				}},
				{field:'creatTimeStr', title:'提交时间', align:'center', width:100},
				{field:'typeStr', title:'案件类型', align:'center', width:100},
				{field:'title', title:'案件标题', align:'center', width:100},
				{field:'wfCurOrg', title:'办理部门', align:'center', width:100},
				{field:'state', title:'办理状态', align:'center', width:100,formatter : function(val, rec) {
					var str = '';
					if(rec.tasktype=='2'){
						str = '(子)';
					}
					if(val=='1'){
						return '登记'+str;
					}else if(val=='2'){
						return '受理'+str;
					}else if(val=='3'){
						return '退回'+str;
					}else if(val=='4'){
						return '归档'+str;
					}
					return '';
				}},
			]],
			pagination: true,
			pageSize: 20,
			toolbar: '#jqueryToolbar',
			onLoadSuccess: function(data) {
				listSuccess(data); //暂无数据提示
			},
			onLoadError: function() {
				listError();
			}
		});
	}
	
	//详情
	function detail(id,tasktype) {
		var url = "${rc.getContextPath()}/zhsq/requestion/detail.jhtml?id=" + id+"&tasktype="+tasktype;
		showMaxJqueryWindow('详情', url, 800, null);
	}
	
	//查询
	function searchData() {
		var a = new Array();
		var type = $("#type").val();
		a["type"] = type;
		var happenTimeStr = $('#happenTimeStr').val();
		a["happenTimeStr"] = happenTimeStr;
		var endTimeStr = $('#endTimeStr').val();
		a["endTimeStr"] = endTimeStr;
		var keyWord = $("#keyWord").val();
		if(keyWord!=null && keyWord!="" && keyWord!="可输入案件编号、标题等关键字") {
			a["keyWord"] = keyWord;
		}
		var wfCurOrg = $("#wfCurOrg").val();
		a["wfCurOrg"] = wfCurOrg;
		
		doSearch(a);
	}
	
	//重置
	function resetCondition() {
		$("#searchForm")[0].reset();
		$("#keyWord").attr('style','font-size:12px;color:gray; width:170px;');
		searchData();
	}
	
	function doSearch(queryParams) {
		// resetCondition();
		$('#list').datagrid('clearSelections');
		$("#list").datagrid('options').queryParams = queryParams;
		$("#list").datagrid('load');
	}
	
	function _onkeydown(){
		var keyCode = event.keyCode;
		if(keyCode == 13){
			searchData();
		}
	}
	
</script>
</html>

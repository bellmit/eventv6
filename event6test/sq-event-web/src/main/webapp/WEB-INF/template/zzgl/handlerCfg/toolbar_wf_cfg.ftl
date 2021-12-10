<#include "/component/ComboBox.ftl" />
<style type="text/css">
	.w150{width:150px;}
</style>
<div id="jqueryToolbar" class="MainContent">
	<div class="ConSearch">
		<form id="wfCfgForm">
	        <div class="fl">
	        	<ul>
	                <li>所属地域：</li>
	                <li>
		                <input id="infoOrgCode" name="regionCode" value="" class="queryParam hide" />
		                <input type="text" id="gridName" name="gridName" value="" class="inp1 InpDisable w150"/>
		            </li>
	            	<li style="position:relative; display:none;">
	            		<a href="javascript:void(0)" class="AdvanceSearchBtn FontDarkBlue">高级查询</a>
	            		<div class="AdvanceSearch DropDownList hide" style="width:370px; top: 42px; left: -130px;">
	                        <div class="LeftShadow">
	                            <div class="RightShadow">
	                                <div class="list NorForm">
	                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	                                    </table>
	                                </div>
	                            </div>
	                        </div>
	                     </div>
	            	</li>
	            </ul>
	        </div>
	        <div class="btns">
	        	<ul>            	
	            	<li><a href="#" class="chaxun" title="查询按钮" onclick="searchData()">查询</a></li>
	            	<li><a href="#" class="chongzhi" title="重置查询条件" onclick="resetCondition()">重置</a></li>
	            </ul>
	        </div>‍
        </form>
	</div>
	<div class="h_10 clear"></div>
	<div class="ToolBar" id="ToolBarDIV">
    	<div class="blind"></div>
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
		<!--toolFrDIV代码格式不可换行，否则会影响布局-->
		<div class="tool fr" id="toolFrDIV"><@actionCheck></@actionCheck></div>	
    </div>
	
</div>

<script type="text/javascript">
	var defaultWinOpt = {
		'maxWidth': 735,
		'maxHeight': 400
	};
	
	$(function(){
		if($("#toolFrDIV").html() == ''){
			$("div").remove("#ToolBarDIV");
		}
	
        AnoleApi.initGridZtreeComboBox("gridName", "infoOrgCode", function(gridId, items){
			if(isNotBlankParam(items) && items.length>0){
				var grid = items[0];
				$("#infoOrgCode").val(grid.orgCode);
			} 
		}, {
            rootName: "行政区划"
        });
	});
	
	function add() {
		var url = '${rc.getContextPath()}/zhsq/event/eventHandlerWfCfg/toAdd.jhtml',
			opt = {
				'title' : "新增业务配置信息",
				'targetUrl' : url
			};
	   
	   	openJqueryWindowByParams($.extend({}, defaultWinOpt, opt));
	}
	
	function edit() {
		var wfcId = "";
		$("input[name='wfcId']:checked").each(function() {
			wfcId = $(this).val();
		});
		if(wfcId == "") {
			$.messager.alert('警告','请选中要编辑的数据再执行此操作!','warning');
		} else {
			var url = '${rc.getContextPath()}/zhsq/event/eventHandlerWfCfg/toAdd.jhtml?wfcId=' + wfcId,
				opt = {
					'title' : "编辑业务配置信息",
					'targetUrl' : url
				};
		   
		   	openJqueryWindowByParams($.extend({}, defaultWinOpt, opt));
		}
	}
	
	function detail(wfcId) {
		var url = '${rc.getContextPath()}/zhsq/event/eventHandlerWfProcCfg/toList.jhtml?wfcId=' + wfcId,
			opt = {
			'maxWidth': $(window).width(),
			'maxHeight': $(window).height(),
			'isAutoHeight': false,
			'isAutoWidth': false,
			'title' : "流程环节配置信息",
			'targetUrl' : url
		};
	   
	   	openJqueryWindowByParams($.extend({}, defaultWinOpt, opt));
	}
	
	function del(){
		var wfcId = "";
		
		$("input[name='wfcId']:checked").each(function() {
			wfcId = $(this).val();
		});
		
		if(wfcId == "") {
			$.messager.alert('警告','请选中要删除的数据再执行此操作!','warning');
		} else {
			$.messager.confirm('提示', '您确定删除选中的业务配置信息吗？', function(r) {
				if (r){
					modleopen();
					
					$.ajax({
						type: "POST",
						url: '${rc.getContextPath()}/zhsq/event/eventHandlerWfCfg/delWfCfg.jhtml',
						data: 'wfcId='+wfcId,
						dataType:"json",
						success: function(data) {
							modleclose();
							if(data.success && data.success == true) {
								flashData(data.tipMsg, true);
							} else {
								$.messager.alert('错误', data.tipMsg, 'error');
							}
						},
						error:function(data){
							modleclose();
							$.messager.alert('错误','删除业务配置连接超时！','error');
						}
					});
				}
			});
		}
	}
	
	function searchData() {
		var searchArray = new Array();
		
		$("#wfCfgForm .queryParam").each(function(index, param) {
			var name = $(param).attr("name"),
				value = $(param).val();
			
			if(isNotBlankString(value)) {
				searchArray[name] = value;
			}
		});
		
		doSearch(searchArray);
	}
	
	function doSearch(queryParams){
		$('#list').datagrid('clearSelections');
		$("#list").datagrid('options').queryParams = queryParams;
		flashData();
	}
	
	function resetCondition() {
		$('#wfCfgForm')[0].reset();
		searchData();
	}
	
	function _onkeydown(){
		var keyCode = event.keyCode;
		
		if(keyCode == 13){
			searchData();
		}
	}
</script>
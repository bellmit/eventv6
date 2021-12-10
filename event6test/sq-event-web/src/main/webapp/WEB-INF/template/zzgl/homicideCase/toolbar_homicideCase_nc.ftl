<#include "/component/ComboBox.ftl" />
<style type="text/css">
	.w100{width:100px;}
	.w150{width:150px;}
	.keyBlank{color:gray;}
	.wDate{width:110px; *width:100px; cursor:pointer;}
</style>
<div id="jqueryToolbar" class="MainContent">
	<div class="ConSearch">
		<form id="homicideForm">
			<input type="hidden" id="bizType" name="bizType" class="queryParam" value="4" />
	        <div class="fl">
	        	<ul>
	        		<li>关键字：</li>
	                <li><input type="text" id="keyWord" name="keyWord" class="inp1 keyBlank" style="width:150px;" value="案件名称/简要情况" defaultValue="案件名称/简要情况" onfocus="_onfocus(this);" onblur="_onblur(this);" onkeydown="_onkeydown();" /></li>
	               
	                <li>
		                <input id="infoOrgCode" name="gridCode" value="${infoOrgCode!}" class="queryParam" style="display:none;" />
		                <input type="hidden" id="gridName" name="gridName" value="${gridName!}" class="inp1 InpDisable w150"/>
		            </li>
		            <li style="position:relative;">
	            		<a href="javascript:void(0)" class="AdvanceSearchBtn FontDarkBlue">高级查询</a>
	            		<div class="AdvanceSearch DropDownList hide" style="width:390px; top: 42px; left: -130px;">
	                        <div class="LeftShadow">
	                            <div class="RightShadow">
	                                <div class="list NorForm" style="position:relative;">
	                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	                                    	<tr>
	                                    		<td>
	                                    			<label class="LabName w100"><span>案件编号：</span></label><input class="inp1 queryParam" type="text" id="reNo" name="reNo" style="width:135px;"></input>
	                                    		</td>
	                                    	</tr>
	                                    	<tr>
	                                    		<td>
	                                    			<label class="LabName w100"><span>发生开始日期：</span></label>
	                                    			<input type="text" id="occuDateStartStr" name="occuDateStartStr" class="inp1 Wdate wDate fl queryParam" onclick="WdatePicker({readOnly:true,maxDate:'#F{$dp.$D(\'occuDateEndStr\')}'})" readonly="readonly"></input><span class="Check_Radio" style="padding:0 5px;">至</span><input type="text" id="occuDateEndStr" name="occuDateEndStr" class="inp1 Wdate wDate fl queryParam" onclick="WdatePicker({readOnly:true,minDate:'#F{$dp.$D(\'occuDateStartStr\')}'})" readonly="readonly"></input></td>
	                                    		</td>
	                                    	</tr>
	                                    	<tr>
	                                    		<td>
	                                    			<label class="LabName w100"><span>侦查结束日期：</span></label>
	                                    			<input type="text" id="spyEndDateStartStr" name="spyEndDateStartStr" class="inp1 Wdate wDate fl queryParam" onclick="WdatePicker({readOnly:true,maxDate:'#F{$dp.$D(\'spyEndDateEndStr\')}'})" readonly="readonly"></input><span class="Check_Radio" style="padding:0 5px;">至</span><input type="text" id="spyEndDateEndStr" name="spyEndDateEndStr" class="inp1 Wdate wDate fl queryParam" onclick="WdatePicker({readOnly:true,minDate:'#F{$dp.$D(\'spyEndDateStartStr\')}'})"  readonly="readonly"></input></td>
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
	var opt = {
		"maxWidth": $(document).width() - 20,
		"padding_left": 10,
		"padding_right": 10,
		"padding_top": 10,
		"padding_bottom": 10
	};
	
	$(function(){
		
		if(document.getElementById('toolFrDIV').innerHTML==''){
			$("div").remove("#ToolBarDIV");
		}
	
        AnoleApi.initGridZtreeComboBox("gridName", null, function(gridId, items){
			if(isNotBlankParam(items) && items.length>0){
				var grid = items[0];
				$("#infoOrgCode").val(grid.orgCode);
			} 
		});
	});
	
	function add() {
		var url = '${rc.getContextPath()}/zhsq/relatedEvents/homicideCase/toAddHomicideCase.jhtml';
		showMaxJqueryWindow("新增命案防控信息", url, fetchWinWidth(opt), fetchWinHeight(opt));
	}
	
	function edit(reId, tabIndex) {
		if(isBlankString(reId)) {
			$("input[name='reId']:checked").each(function() {
				reId = $(this).val();
			});
		}
		
		if(isBlankString(reId)) {
			$.messager.alert('警告','请选中要编辑的数据再执行此操作!','warning');
		} else {
			var url = '${rc.getContextPath()}/zhsq/relatedEvents/homicideCase/toEditHomicideCase.jhtml?reId=' + reId;
			
			if(isNotBlankString(tabIndex)) {
				url += "&tabIndex="+ tabIndex;
			}
			
			showMaxJqueryWindow("编辑命案防控信息", url, fetchWinWidth(opt), fetchWinHeight(opt));
		}
	}
	
	function detail(reId) {
		var url = '${rc.getContextPath()}/zhsq/relatedEvents/homicideCase/toDetailHomicideCase.jhtml?reId='+reId;
		showMaxJqueryWindow("查看命案防控信息", url, fetchWinWidth(opt), fetchWinHeight(opt));
	}
	
	function del(){
		var reId = "";
		$("input[name='reId']:checked").each(function() {
			reId = $(this).val();
		});
		if(reId == "") {
			$.messager.alert('警告','请选中要删除的数据再执行此操作!','warning');
		} else {
			$.messager.confirm('提示', '您确定删除选中的命案防控信息吗？', function(r) {
				if (r){
					modleopen();
					
					$.ajax({
						type: "POST",
						url: '${rc.getContextPath()}/zhsq/relatedEvents/homicideCase/delHomicideCase.jhtml',
						data: 'reId='+reId,
						dataType:"json",
						success: function(data) {
							if (data.result) {
								var msg = "命案防控信息删除成功！";
								modleclose();
								
								reloadDataForSubPage(msg);
							}
						},
						error:function(data){
							$.messager.alert('错误','连接超时！','error');
						}
					});
				}
			});
		}
	}
	
	function gridTreeClickCallback(gridId, gridName, infoOrgId, infoOrgCode, gridInitPhoto) {
		
		$("#infoOrgCode").val(infoOrgCode);
	
		searchData();
	}
	
	function searchData() {
		var searchArray = new Array();
		
		if($("#keyWord").hasClass("keyBlank")){
			$("#keyWord").removeClass("queryParam");
		} else {
			$("#keyWord").addClass("queryParam");
		}
		
		$(".queryParam").each(function() {
			var val = $(this).val();
			var key = $(this).attr("name");
			
			if(isNotBlankString(val)){
				searchArray[key] = val;
			}
		});
		
		doSearch(searchArray);
	}
	
	function doSearch(queryParams){
		$('#list').datagrid('clearSelections');
		$("#list").datagrid('options').queryParams = queryParams;
		$("#list").datagrid('load');
	}
	
	function resetCondition() {
		$('#homicideForm')[0].reset();
		$('#keyWord').addClass('keyBlank');
		$('#infoOrgCode').val('${infoOrgCode!}');
		searchData();
	}
	
	function _onfocus(obj) {
		if($(obj).hasClass("keyBlank")){
			$(obj).val("");
			$(obj).removeClass('keyBlank')
		}
	}
	
	function _onblur(obj) {
		var keyWord = $(obj).val();
		
		if(keyWord == ''){
			$(obj).addClass('keyBlank');
			$(obj).val($(obj).attr("defaultValue"));
		}
	}
	
	function _onkeydown(){
		var keyCode = event.keyCode;
		
		if(keyCode == 13){
			searchData();
		}
	}
</script>
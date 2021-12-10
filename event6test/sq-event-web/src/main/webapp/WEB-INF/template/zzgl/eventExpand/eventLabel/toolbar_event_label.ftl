<#include "/component/ComboBox.ftl" />
<style type="text/css">
	.w150{width:150px;}
	.keyBlank{color:gray;}
</style>
<div id="jqueryToolbar" class="MainContent">
	<div class="ConSearch">
		<form id="eventWechatForm">
			<input type="hidden" class="queryParam" id="verifyType" name="verifyType" value="EventCase" />
			
	        <div class="fl">
	        	<ul>
	        		<li>所属网格</li>
	        		<li>
	        			<input type="hidden" id="gridId" value="">
	        			<input type="text" class="hide queryParam" id="infoOrgCode" name="infoOrgCode" value="${infoOrgCode!}" />
	        			<input type="text" class="inp1" style="width:122px;" id="gridName" value="" />
	        		</li>
	        		<li>标签名称：</li>
	        		<li><input type="text" id="labelName" name="labelName" class="inp1 w150 keyBlank" value="标签名称" defaultValue="标签名称" onfocus="_onfocus(this);" onblur="_onblur(this);" onkeydown="_onkeydown();" /></li>
	        		<li>所属模块：</li>
	        		<li>
	        			<input class="hide queryParam" id="labelModel" name="labelModel" />
	        			<input type="text" class="inp1 w150" id="labelModelStr" />
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
    	<div class="tool fr" id="toolFrDIV"><@actionCheck></@actionCheck>
    		<a id="del" href="javascript:void(0)" onclick="del()" class="NorToolBtn DelBtn">删除</a>
    		<a id="edit" href="javascript:void(0)" onclick="edit()" class="NorToolBtn EditBtn">编辑</a>
    		<a id="add" href="javascript:void(0)" onclick="add()" class="NorToolBtn AddBtn">新增</a>
    	</div>
    </div>
	
</div>

<script type="text/javascript">
	$(function(){
		if($("#toolFrDIV").find("a").length == 0){
			$("#ToolBarDIV").hide();
		}
		
		
		AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId, items) {
			if(isNotBlankParam(items) && items.length > 0) {
				var grid = items[0];
				$("#infoOrgCode").val(grid.orgCode);
			} 
		}, {
			OnCleared : function() {
				$("#infoOrgCode").val("");
			},
			ShowOptions : {
				EnableToolbar : true
			}
		});
			
		<#if extraParam??>
			<#list extraParam?keys as mapKey>
				var inputObj = $('#eventExtendQueryForm input[name="${mapKey}"]');
				if(inputObj.length) {
					inputObj.val('${extraParam[mapKey]}');
				} else {
					$("#eventWechatForm").prepend('<input type="hidden" class="queryParam" name="${mapKey}" value="${extraParam[mapKey]}" />');
				}
			</#list>
		</#if>
		
	
		AnoleApi.initListComboBox("labelModelStr", "labelModel", "B596", null, null, {
            ShowOptions:{
                EnableToolbar : true
            }
        });
		
		
	});
	
	function add() {
        var url = "${rc.getContextPath()}/zhsq/event/eventLabelController/toAddPage.jhtml";
    	showMaxJqueryWindow("新增标签", url, 400, 350);
	}
	
	function edit() {
		var labelId = "";
		$("input[name='labelId']:checked").each(function() {
			labelId = $(this).val();
		});
		if(labelId == "") {
			$.messager.alert('提示','请选中要编辑的记录再执行此操作!','info');
		} else {
        	var url = "${rc.getContextPath()}/zhsq/event/eventLabelController/toAddPage.jhtml?labelId="+labelId;
    		showMaxJqueryWindow("编辑标签", url, 400, 350);
		}
	}
	
	function del() {
		var labelId = "";
		$("input[name='labelId']:checked").each(function() {
			labelId = $(this).val();
		});
		if(labelId == "") {
			$.messager.alert('提示','请选中要删除的记录再执行此操作!','info');
		} else {
			$.messager.confirm('提示', '是否删除选中的标签？', function(r) {
				if (r){
					modleopen();
					$.ajax({
						type: "POST",
			    		url : '${rc.getContextPath()}/zhsq/event/eventLabelController/delLabel.jhtml?labelId='+labelId,
						dataType:"json",
						success: function(data){
							modleclose();
							if (data.result == 'fail') {
								$.messager.alert('错误', '删除失败！', 'error');
							} else {
								$.messager.alert('提示', '删除成功！', 'info', function() {
									closeMaxJqueryWindow();
								});
								searchData();
							}
						},
						error:function(data){
							$.messager.alert('错误','连接错误！','error');
						}
			    	});
				}
			});
        	
		}
	}
	
	
	
	function detail(id) {
		var opt = {
			'maxHeight': 400,
			'maxWidth': 350
		};
		var url = '${rc.getContextPath()}/zhsq/event/eventLabelController/toDetailPage.jhtml?labelId='+id;
		
		opt.title = "查看标签信息";
		opt.targetUrl = url;
		
		openJqueryWindowByParams(opt);
	}
	
	
	function authority(selectedRow) {
		if(selectedRow) {
			var showBtn = ["AddBtn","EditBtn","DelBtn"];
			
			$("#toolFrDIV > a").hide();
			
			for(var index in showBtn) {
				$("#toolFrDIV ." + showBtn[index]).show();
			}
		}
	}
	
	function showEventWin(caseId) {
		if(caseId) {
			var opt = null;
			
			opt = {
				maxWidth: 1000,
				title: "查看案件信息",
				targetUrl: '${rc.getContextPath()}/zhsq/eventCase/toDetail.jhtml?listType=5&caseId=' + caseId
			};
			
			openJqueryWindowByParams(opt);
		}
	}
	
	function fetchQueryParams() {
		var searchArray = new Array();
		
		if($("#labelName").hasClass("keyBlank")){
			$("#labelName").removeClass("queryParam");
		} else {
			$("#labelName").addClass("queryParam");
		}
		
		$('#eventWechatForm .queryParam').each(function() {
			var queryObj = $(this),
				value = queryObj.val(),
				name = queryObj.attr("name");
			
			if(value) {
				searchArray[name] = value;
			}
			
		});
		
		return searchArray;
	}
	
	function searchData(isCurrent) {
		$('#list').datagrid('clearSelections');
		$("#list").datagrid('options').queryParams = fetchQueryParams();
		
		if(isCurrent && isCurrent == true) {
			$("#list").datagrid('reload');
		} else {
			$("#list").datagrid('load');
		}
	}
	
	function resetCondition() {
		$('#eventWechatForm')[0].reset();
		
		$('#keyWord').addClass('keyBlank');
		$("#status").val("");
		
		searchData();
	}
	
	function flashData() {//案件办理页面回调
		reloadDataForSubPage(null, true);
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
	
	function DivHide(){
		$("#ToolBarDIV .blind").slideUp();//窗帘效果展开
	}
	
	function DivShow(msg){
		$("#ToolBarDIV .blind").html(msg);
		$("#ToolBarDIV .blind").slideDown();//窗帘效果展开
		setTimeout("this.DivHide()",800);
	}
	
</script>
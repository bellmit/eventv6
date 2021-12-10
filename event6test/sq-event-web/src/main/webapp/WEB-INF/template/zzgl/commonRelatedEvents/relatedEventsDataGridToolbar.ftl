<#include "/component/ComboBox.ftl" />
<style type="text/css">
.width65px{width:65px;}
</style>
<div id="jqueryToolbar" class="MainContent">
	<div class="ConSearch">
        <div class="fl">
        	<ul>
                <li>所属网格：</li>
                <li><input id="startGridId" type="hidden"/><input id="gridName" name="gridName" type="text" class="inp1 InpDisable" style="width:150px;"/></li>
            	<li>关键字：</li>
                <li><input name="keyWord" type="text" class="inp1" id="keyWord" value="案(事)件名称/案件情况/侦破情况" style="font-size:12px;color:gray;width:250px;" onfocus="if(this.value=='案(事)件名称/案件情况/侦破情况'){this.value='';}$(this).attr('style','width:250px;');" onblur="if(this.value==''){$(this).attr('style','font-size:12px;color:gray;width:250px;');this.value='案(事)件名称/案件情况/侦破情况';}" onkeydown="_onkeydown();"/></li>
                <li style="position:relative;">
            		<a href="javascript:void(0)" class="AdvanceSearchBtn FontDarkBlue">高级查询</a>
            		<div class="AdvanceSearch DropDownList hide" style="width:370px; top: 42px; left: -130px;">
                        <div class="LeftShadow">
                            <div class="RightShadow">
                                <div class="list NorForm">
                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                      <tr>
                                        <td><label class="LabName width65px"><span>案件编号：</span></label><input class="inp1" type="text" id="reNo" name="reNo" style="width:150px;"></input></td>
                                      </tr>
                                      <tr>
                                      	<td>
                                      		<label class="LabName width65px"><span>是否破案：</span></label>
                                      		<input type="hidden" id="isDetection" name="isDetection" />
            								<input type="text" class="inp1" style="width:150px;" id="isDetectionName" />
            							</td>
                                      </tr>
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
            	<li><a href="#" class="chaxun" title="查询按钮" onclick="searchData(1)">查询</a></li>
            	<li><a href="#" class="chongzhi" title="重置查询条件" onclick="resetCondition()">重置</a></li>
            </ul>
        </div>‍
        
	</div>
	<div class="h_10 clear"></div>
	<div class="ToolBar" id="ToolBar">
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
        <div class="tool fr">
    		<@ffcs.right rightCode="del" parentCode="${system_privilege_action?default('')}">
    			<a href="#" class="NorToolBtn DelBtn" onclick="del();">删除</a>
			</@ffcs.right>
			<@ffcs.right rightCode="edit" parentCode="${system_privilege_action?default('')}">
				<a href="#" class="NorToolBtn EditBtn" onclick="edit();">编辑</a>
			</@ffcs.right>
			<@ffcs.right rightCode="add" parentCode="${system_privilege_action?default('')}">
				<a href="#" class="NorToolBtn AddBtn" onclick="add();">新增</a>
			</@ffcs.right>
        </div>
    </div>
	
</div>

<script type="text/javascript">
	var queryValue = "";
	var lotUrl = "${rc.getContextPath()}/zhsq/relatedEvents/CommonRelatedEventsController/listCareRoads.jhtml?lotName=";
	
	$(function(){
        AnoleApi.initGridZtreeComboBox("gridName", "startGridId");
        AnoleApi.initListComboBox("isDetectionName", "isDetection", null, null, [""], {
        	DataSrc: [{"name":"不限", "value":""},{"name":"已破案", "value":"1"},{"name":"未破案", "value":"0"}]
        });
	});
	
	function add() {
		var url = '${rc.getContextPath()}/zhsq/relatedEvents/CommonRelatedEventsController/toAddRelatedEvents.jhtml';
		var opt = $.extend({}, _win_opt, {
	    	title: "新增涉事案件",
	    	targetUrl: url,
	    	width: 820,
	    	maxHeight: 406
	    });
		openJqueryWindowByParams(opt);
	}
	
	function edit() {
		if(idStr==null || idStr=="") {
			$.messager.alert('提示','请选中要编辑的数据再执行此操作!','warning');
			return;
		}
		var url = '${rc.getContextPath()}/zhsq/relatedEvents/CommonRelatedEventsController/toAddRelatedEvents.jhtml?reId='+idStr;
		var opt = $.extend({}, _win_opt, {
	    	title: "编辑涉事案件",
	    	targetUrl: url,
	    	width: 820,
	    	maxHeight: 406
	    });
		openJqueryWindowByParams(opt);
	}
	
	function del() {
		if(idStr==null || idStr=="") {
			$.messager.alert('提示','请选中要删除的数据再执行此操作!','warning');
			return;
		}
		$.messager.confirm('提示', '您确定删除选中的记录吗？', function(r){
			if (r){
				modleopen();
				$.ajax({
					type: "POST",
					url: '${rc.getContextPath()}/zhsq/relatedEvents/CommonRelatedEventsController/delRelatedEvents.jhtml',
					data: 'idStr='+idStr,
					dataType:"json",
					success: function(data){
						modleclose();
						$.messager.alert('提示', '您成功删除'+data.result+'条记录！', 'info');
						$("#list").datagrid('load');
					},
					error:function(data){
						$.messager.alert('错误','连接超时！','error');
					}
				});
			}
		});
	}
	
	function resetCondition() {
		$("#keyWord").val("案(事)件名称/案件情况/侦破情况");
		$("#keyWord").attr('style','font-size:12px;color:gray;width:250px;');
		$('#bizId').val("");
		$("#bizName").val("");
		queryValue = "";
		$("#gridName").val("");
		$("#startGridId").val("");
		$("#reNo").val("");
		$("#isDetection").val("");
		$("#isDetectionName").val("不限");
		searchData(1);
	}
	
	function _onkeydown(){
		var keyCode = event.keyCode;
		if(keyCode == 13){
			searchData(1);
		}
	}
	
</script>
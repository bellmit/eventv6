<#include "/component/ComboBox.ftl" />
<style type="text/css">
	.w150{width:150px;}
	.keyBlank{color:gray;}
</style>
<div id="jqueryToolbar" class="MainContent">
	<div class="ConSearch">
		<form id="eventWechatForm">
			<input type="hidden" class="queryParam" name="infoOrgCode" value="${infoOrgCode!}" />
	        <div class="fl">
	        	<ul>
	                <li>关键字：</li>
	                <li><input type="text" id="keyWord" name="keyWord" class="inp1 w150 keyBlank" value="事件内容/标题/事发地址" defaultValue="事件内容/标题/事发地址" onfocus="_onfocus(this);" onblur="_onblur(this);" onkeydown="_onkeydown();" /></li>
	                <li>状态：</li>
	                <li>
            			<input class="hide queryParam" id="status" name="status" />
            			<input type="text" class="inp1 w150" id="statusName" />
            		</li>
	                
	            	<li style="position:relative;">
	            		<a href="javascript:void(0)" class="AdvanceSearchBtn FontDarkBlue">高级查询</a>
	            		<div class="AdvanceSearch DropDownList hide" style="width:370px; top: 42px; left: -130px;">
	                        <div class="LeftShadow">
	                            <div class="RightShadow">
	                                <div class="list NorForm">
	                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	                                    	<tr>
	                                    		<td>
		                                        	<label class="LabName width65px"><span>事发时间：</span></label><input class="inp1 Wdate fl queryParam" type="text" id="happenTimeStart" name="happenTimeStart" value="${happenTimeStart!''}" style="width:110px; *width:100px; cursor:pointer;" onclick="WdatePicker({readOnly:true})" readonly="readonly"></input><span class="Check_Radio" style="padding:0 5px;">至</span><input class="inp1 Wdate fl queryParam" type="text" id="happenTimeEnd" name="happenTimeEnd" value="${happenTimeEnd!''}" style="width:110px; *width:100px; cursor:pointer;" onclick="WdatePicker({readOnly:true})" readonly="readonly"></input>
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
	            	<li><a href="#" class="chaxun" title="查询按钮" onclick="searchData()">查询</a></li>
	            	<li><a href="#" class="chongzhi" title="重置查询条件" onclick="resetCondition()">重置</a></li>
	            </ul>
	        </div>‍
        </form>
	</div>
	<div class="h_10 clear"></div>
	<div class="ToolBar" id="ToolBarDIV">
    	<div class="blind"></div>
    	<div class="tool fr" id="toolFrDIV"><@actionCheck></@actionCheck></div>
    </div>
	
</div>

<script type="text/javascript">
	$(function(){
		if($("#toolFrDIV").find("a").length == 0){
			$("#ToolBarDIV").hide();
		}
		
		AnoleApi.initListComboBox("statusName", "status", null, null, null, {
        	DataSrc: [{"name":"待审核", "value":"01"},{"name":"已上报", "value":"02"},{"name":"被驳回", "value":"03"}],
        	ShowOptions: {
				EnableToolbar : true
			},
        	IsTriggerDocument: false
        });
		
	});
	
	function edit() {
		var eventWechatId = "";
		$("input[name='eventWechatId']:checked").each(function() {
			eventWechatId = $(this).val();
		});
		if(eventWechatId == "") {
			$.messager.alert('警告','请选中要审核的数据再执行此操作!','warning');
		} else {
			var opt = {
				'maxHeight': 400,
				'maxWidth': 720
			};
			var url = '${rc.getContextPath()}/zhsq/eventWechat/toEdit.jhtml?eventWechatId=' + eventWechatId;
			
			opt.title = "审核微信事件";
			opt.targetUrl = url;
			
			openJqueryWindowByParams(opt);
		}
	}
	
	function detail(eventWechatId) {
		var opt = {
			'maxHeight': 400,
			'maxWidth': 720
		};
		var url = '${rc.getContextPath()}/zhsq/eventWechat/toDetail.jhtml?eventWechatId='+eventWechatId;
		
		opt.title = "查看微信事件";
		opt.targetUrl = url;
		
		openJqueryWindowByParams(opt);
	}
	
	function del(){
		var eventWechatId = "";
		$("input[name='eventWechatId']:checked").each(function() {
			eventWechatId = $(this).val();
		});
		if(eventWechatId == "") {
			$.messager.alert('警告','请选中要删除的数据再执行此操作!','warning');
		} else {
			$.messager.confirm('提示', '您确定删除选中的微信事件吗？', function(r) {
				if (r){
					modleopen();
					
					$.ajax({
						type: "POST",
						url: '${rc.getContextPath()}/zhsq/eventWechat/delEventWechat.jhtml',
						data: 'eventWechatId='+eventWechatId,
						dataType:"json",
						success: function(data) {
							modleclose();
							
							if(data.success && data.success == true){
			  					reloadDataForSubPage(data.tipMsg, true);
			  				} else {
			  					if(data.tipMsg) {
			  						$.messager.alert('错误', data.tipMsg, 'error');
			  					} else {
			  						$.messager.alert('错误', '删除失败！', 'error');
			  					}
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
	
	function reportEventWechat(eventWechat) {
		var event = eventWechat;
		
		var url = '${rc.getContextPath()}/zhsq/event/eventDisposalController/toAddEventByJson.jhtml';
		
		
	  	var reportWinId = openJqueryWindowByParams({
	  		maxWidth: 800,
	  		maxHeight: 400,
	  		title: "上报微信事件"
		});
	  	
	  	var reportForm = '<form id="_report4eventForm" name="_report4eventForm" action="" target="'+ reportWinId +'_iframe" method="post">'+
						 '</form>';
		
		$("#jqueryToolbar").append($(reportForm));
		$("#_report4eventForm").append($('<input type="hidden" id="_reportEventJson" name="eventJson" value="" />'));
		
		$("#_reportEventJson").val(JSON.stringify(event));
		$("#_report4eventForm").attr('action', url);
		
		$("#_report4eventForm").submit();
		
		$("#_report4eventForm").remove();
	}

	function reportCallBack(data, operateType) {
		if(data.result) {
			var selectedRow = $('#list').datagrid('getSelected'),
				operateTypeName = "上报",
				status = "02";
			
			operateType = operateType || 0;//为空时，默认设置为0
			
			switch(operateType) {
				case 1: {
					operateTypeName = "驳回"; 
					status = "03";
					break;
				}
			}
			
			$.ajax({
				type: "POST",
				url: "${rc.getContextPath()}/zhsq/eventWechat/saveEventWechat.jhtml",
				data: {'eventWechatId': selectedRow.eventWechatId, "status": status, "operateType": operateType},
				dataType:"json",
				success: function(data) {
					modleclose();
					
					if(data.success && data.success == true) {
	  					reloadDataForSubPage(operateTypeName + '成功！', true);
	  				} else {
	  					if(data.tipMsg) {
	  						$.messager.alert('错误', data.tipMsg, 'error');
	  					} else {
	  						$.messager.alert('错误', operateTypeName + '失败！', 'error');
	  					}
	  				}
				},
				error:function(data){
					modleclose();
					$.messager.alert('错误','连接超时！','error');
				}
			});
			
		} else {
			$.messager.alert('错误', operateTypeName + '失败！','error');
		}
	}
	
	function authority(selectedRow) {
		if(selectedRow) {
			var showBtn = [];
			
			if(selectedRow.status == '01') {
				showBtn = ["EditBtn", "DelBtn"];
			}
			
			$("#toolFrDIV > a").hide();
			
			for(var index in showBtn) {
				$("#toolFrDIV ." + showBtn[index]).show();
			}
		}
	}
	
	function fetchQueryParams() {
		var searchArray = new Array();
		
		if($("#keyWord").hasClass("keyBlank")){
			$("#keyWord").removeClass("queryParam");
		} else {
			$("#keyWord").addClass("queryParam");
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
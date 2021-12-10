<#include "/component/ComboBox.ftl" />
<style type="text/css">
	.w150{width:150px;}
	.keyBlank{color:gray;}
</style>
<div id="jqueryToolbar">
	<div class="ConSearch">
		<form id="eventImportForm" action='' method="post" enctype="multipart/form-data">
			<input type="hidden" id="isEnd" value=""/>
			<input type="text" id="fileSuffix" name="fileSuffix" class="hide"/>
			
			<div style="margin: 0 auto; background-color:#F9F9F9; position:relative;">
				<div id="operateMask" class="MarskLayDiv hide" style="margin-top: -11px; margin-left: -10px;"></div>
				<div id="importDiv" class="clear PopDiv NorForm hide" style="height:285px; width:630px; top: 135px;">
					<div class="ConSearch">
						<div class="NorForm">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td colspan="2" class="LeftTd">
										<span>请选择导入文件(文件格式为：<label class="FontRed"><b>csv、xls、xlsx</b></label>)</span>
									</td>
								</tr>
								<tr>
									<td colspan="2" class="LeftTd">
										<span>
											导入说明：<br/>
											1、导入文件中，首行为标题行，字段设置点击“<b>中间表字段说明</b>”查看，设置为表字段名称<br/>
											2、日期数据项：格式为yyyy-mm-dd hh:mi:ss，如：2017-12-12 12:12:12<br/>
											3、数据量较大时，请优先使用数据层面导入或者plsql中的“Tools->Importer”、“Tools->ODBC Importer”导入<br/>
											4、三种文件格式的导入优先级为：csv > xls > xlsx<br/>
											5、<a href="###" onclick="openDialog('importFileDiv');" style="text-decoration: none; color: #000;"><b>导入文件说明</b></a><br/>
											6、<a href="###" onclick="fetchColumInfo();" style="text-decoration: none; color: #000;"><b>中间表字段说明</b></a>
										</span>
									</td>
								</tr>
								<tr>
									<td colspan="2" class="LeftTd">
										<input type="file" id="importFile" name="csvImportFile" />
									</td>
								</tr>
								<tr>
									<td><a href="###" class="BigNorToolBtn SubmitBtn" style="float:right;" onclick="eventImport()">确定</a></td>
									<td><a href="###" class="BigNorToolBtn CancelBtn" onclick="closeDialog('importDiv')">取消</a></td>
								</tr>
							</table>
						</div>
					</div>
				</div>
				
				<div id="importFileDiv" class="clear PopDiv NorForm hide" style="height:375px; width:705px; top: 78px;">
					<div class="ConSearch">
						<div class="NorForm">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td class="LeftTd">
										<span>
											导入文件说明
										</span>
									</td>
									<td>
										<a href="###" class="NorToolBtn StopUseBtn" onclick="closeDialog('importFileDiv', false);" style="float: right; margin-right: 0px;">关闭</a>
									</td>
								</tr>
								<tr>
									<td colspan="2" class="LeftTd">
										<span>
											<b>csv格式文件要求：</b><br/>
											1、文件内容中不可包含字段分隔符，如：各字段间以英文逗号分隔，则文件中不可包含英文逗号，否则会导致数据解析不正确。</br>
											2、文件内容中不可包含回车换行字符，否则会导致数据解析不正确。
										</span>
									</td>
								</tr>
								<tr>
									<td colspan="2" class="LeftTd">
										<span>
											<b>xls格式文件要求：</b><br/>
											1、文件为Microsoft Excel 97/2000/XP/2003 文件(*.xls)
										</span>
									</td>
								</tr>
								<tr>
									<td colspan="2" class="LeftTd">
										<span>
											<b>xlsx格式文件要求：</b><br/>
											1、各导入项设置为文本格式
										</span>
									</td>
								</tr>
							</table>
						</div>
					</div>
				</div>
				
				<div id="tableColDiv" class="clear PopDiv NorForm hide" style="height:375px; width:705px; top: 78px;">
					<div class="ConSearch">
						<div class="NorForm">
							<table width="100%" border="0" cellspacing="0" cellpadding="0" style="width: 100%; height: 100%;">
								<tr>
									<td>
										<span>事件导入中间表T_EVENT_IMPORT</span>
									</td>
									<td>
										<a href="###" class="NorToolBtn StopUseBtn" onclick="closeDialog('tableColDiv', false);" style="float: right; margin-right: 0px;">关闭</a>
									</td>
								</tr>
								<tr>
									<td colspan="2" class="LeftTd">
										<span>导入项涉及字典：1、事件类别：A001093199；2、影响范围：A001093094；3、紧急程度：A001093271。</span>
									</td>
								</tr>
								<tr>
									<td colspan="2" class="LeftTd">
										<span>事件类别中若有多个层级，逐级请使用“-”连接，如“城市管理-市容管理”。</span>
									</td>
								</tr>
							</table>
							<div id="tableColInfoDiv" style="height:265px; width:695px;">
								<table id="tableColInfo" width="100%" border="0" cellspacing="0" cellpadding="0" style="width: 100%; height: 100%;">
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
			
	        <div class="fl">
	        	<ul>
	            	<li>关键字：</li>
	                <li><input type="text" id="keyWord" name="keyWord" class="inp1 keyBlank w150 queryParam" value="事件描述/标题/事发详址" defaultValue="事件描述/标题/事发详址" onfocus="_onfocus(this);" onblur="_onblur(this);" onkeydown="_onkeydown();" /></li>
	                <li>导入状态</li>
	                <li>
	                	<input type="text" id="status" name="status" class="hide queryParam" />
	                	<input type="text" id="statusName" class="inp1 w150" />
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
	            	<li><a href="###" class="chaxun" title="查询按钮" onclick="searchData()">查询</a></li>
	            	<li><a href="###" class="chongzhi" title="重置查询条件" onclick="resetCondition()">重置</a></li>
	            </ul>
	        </div>
        </form>
        
        <div class="clear"></div>‍
        
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
        <div class="tool fr" id="toolFrDIV"><@actionCheck></@actionCheck></div>	
    </div>
	
</div>

<script type="text/javascript">
	$(function() {
		<#if msgWrong??>
			$.messager.alert('错误', "${msgWrong}", 'error');
		<#elseif successTotal?? && total??>
			$.messager.alert('提示', '共需导入${total}条记录，成功导入${successTotal}条记录！', 'info');
		</#if>
		
		if($("#toolFrDIV").find("a").length == 0) {
			$("#ToolBarDIV").hide();
		}
		
		AnoleApi.initListComboBox("statusName", "status", null, null, null, {
        	DataSrc : [{"name":"导入失败", "value":"0"},{"name":"导入成功", "value":"1"},{"name":"待导入", "value":"2"}],
        	IsTriggerDocument: false,
        	ShowOptions:{
        		EnableToolbar : true
        	}
        });
        
        $("#operateMask").height($(document).height());
        $("#operateMask").width($(document).width());
	});
	
	function eventImport() {
		var filePath = $("#importFile").val(),
			filePathLen = filePath.length,
			fileSuffixIndex = filePath.lastIndexOf('.'),
			warningMsg = "";
		
		if(filePathLen > 0 && fileSuffixIndex > 0) {
			var fileType = filePath.substring(fileSuffixIndex + 1);
			
			fileType = fileType.toLowerCase();
			
			if(fileType != "csv" && fileType != "xls" && fileType != "xlsx") {
				warningMsg = '请选择如下格式的文件：csv、xls、xlsx！';
			}
		} else {
			warningMsg = '请选择导入文件！';
		}
		
		if(warningMsg != "") {
			$.messager.alert('警告', warningMsg, 'warning');
		} else {
			modleopen();
			closeDialog('importDiv');
			$("#fileSuffix").val(fileType);
			
	      	$("#eventImportForm").attr('action', '${rc.getContextPath()}/zhsq/event/importEvent4File/importData.jhtml');
      		
      		$("#eventImportForm").submit();
		}
	}
	
	//打开初始化对话框
	function openDialog(divId) {
		$("#operateMask").show();
		$('#'+divId).show();
	}
	
	//关闭初始化对话框
	function closeDialog(divId, isCloseMask) {
		$('#'+divId).hide();
		
		if(isBlankParam(isCloseMask) || isCloseMask == true) {
			$("#operateMask").hide();
		}
	}
	
	function startThread() {
		var isEnd = $("#isEnd").val();
		
		if(isEnd) {
			$.messager.alert('提示', '事件归档线程已启动！', 'info');
		} else {
			$.messager.confirm('提示', '您确定启动事件归档线程？', function(r) {
				if(r){
					$.ajax({
						type: "POST",
			    		url : '${rc.getContextPath()}/zhsq/event/importEvent4File/startOrEndThread4Archive.jhtml',
						data: '',
						dataType:"json",
						success: function(data){
					   		if(data.success) {
					   			$("#isEnd").val(true);
					   		
			  					reloadDataForSubPage(data.tipMsg, true);
			  				} else {
			  					if(data.tipMsg) {
			  						$.messager.alert('错误', data.tipMsg, 'error');
			  					} else {
			  						$.messager.alert('错误', '事件归档线程启动失败！', 'error');
			  					}
			  				}
						},
						error:function(data){
							$.messager.alert('错误','事件归档线程启动连接错误！','error');
						}
			    	});
				}
			});
		}
	}
	
	function endThread() {//为了保证刷新页面后，仍可以正常关闭已启动的线程
		$.messager.confirm('提示', '您确定关闭事件归档线程？', function(r) {
			if(r){
				$.ajax({
					type: "POST",
		    		url : '${rc.getContextPath()}/zhsq/event/importEvent4File/startOrEndThread4Archive.jhtml',
					data: 'isEnd=true',
					dataType:"json",
					success: function(data){
				   		if(data.success) {
				   			$("#isEnd").val("");
				   			
		  					reloadDataForSubPage(data.tipMsg, true);
		  				} else {
		  					if(data.tipMsg) {
		  						$.messager.alert('错误', data.tipMsg, 'error');
		  					} else {
		  						$.messager.alert('错误', '事件归档线关闭失败！', 'error');
		  					}
		  				}
					},
					error:function(data){
						$.messager.alert('错误','事件归档线程关闭连接错误！','error');
					}
		    	});
			}
		});
	}
	
	function edit() {
		var rowid = "";
		
		$("input[name='ROW_ID']:checked").each(function() {
			rowid = $(this).val();
		});
		
		if(rowid == "") {
			$.messager.alert('警告','请选中要编辑的数据再执行此操作!','warning');
		} else {
			var url = "${rc.getContextPath()}/zhsq/event/importEvent4File/toEdit.jhtml";
			
		  	var reportWinId = openJqueryWindowByParams({
		  							maxWidth: 700,
		  							title: "编辑记录信息",
		  							targetUrl: url
							  });
		  	
		  	var reportForm = '<form id="_report4eventForm" name="_report4eventForm" action="" target="'+ reportWinId +'_iframe" method="post">'+
							 '</form>';
			
			$("#jqueryToolbar").append($(reportForm));
			$("#_report4eventForm").append($('<input type="hidden" id="rowid" name="rowid" value="" />'));
			
			$("#rowid").val(rowid);
			$("#_report4eventForm").attr('action', url);
			
			$("#_report4eventForm").submit();
			
			$("#_report4eventForm").remove();
		}
	}
	
	function del() {
		var rowid = "";
		$("input[name='ROW_ID']:checked").each(function() {
			rowid = $(this).val();
		});
		if(rowid == "") {
			$.messager.alert('警告','请选中要删除的数据再执行此操作!','warning');
		} else {
			$.messager.confirm('提示', '您确定删除选中的数据吗？删除后，不可恢复！', function(r) {
				if(r){
					var url = '${rc.getContextPath()}/zhsq/event/importEvent4File/delData.jhtml',
						reportForm = '<form id="_report4eventForm" name="_report4eventForm" action="" method="post"></form>';
			
					$("#jqueryToolbar").append($(reportForm));
					$("#_report4eventForm").append($('<input type="hidden" id="rowid" name="rowid" value="" />'));
					
					$("#rowid").val(rowid);
					
					$("#_report4eventForm").attr('action', url);
					
					modleopen();
					
					$("#_report4eventForm").ajaxSubmit(function(data) {//使用form提交，是为了保证rowid中的+这样的字符能正常传输
						modleclose();
						$("#_report4eventForm").remove();
						
						if(data.success) {
		  					reloadDataForSubPage(data.tipMsg, true);
		  				} else {
		  					if(data.tipMsg) {
		  						$.messager.alert('错误', data.tipMsg, 'error');
		  					} else {
		  						$.messager.alert('错误', '删除操作失败！', 'error');
		  					}
		  				}
					});
				}
			});
		}
	}
	
	function detailRecord(rowid){
		if(rowid) {
		    var url = "${rc.getContextPath()}/zhsq/event/importEvent4File/toDetail.jhtml";
			
		  	var reportWinId = openJqueryWindowByParams({
		  							maxWidth: 700,
		  							title: "查看记录信息",
		  							targetUrl: url
							  });
		  	
		  	var reportForm = '<form id="_report4eventForm" name="_report4eventForm" action="" target="'+ reportWinId +'_iframe" method="post">'+
							 '</form>';
			
			$("#jqueryToolbar").append($(reportForm));
			$("#_report4eventForm").append($('<input type="hidden" id="rowid" name="rowid" value="" />'));
			
			$("#rowid").val(rowid);
			$("#_report4eventForm").attr('action', url);
			
			$("#_report4eventForm").submit();
			
			$("#_report4eventForm").remove();
		} else {
	    	$.messager.alert('警告','请选择需要查看的记录！','warning');
		}
	}
	
	function detail(eventId){
		if(eventId) {
		    var url = "${rc.getContextPath()}/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=all&eventId="+eventId+"&cachenum=" + Math.random();
		    
		    openJqueryWindowByParams({
		    	title: "查看事件信息",
		    	targetUrl: url
		    });
		} else {
	    	$.messager.alert('警告','该记录未有关联的事件！','warning');
		}
	}
	
	function fetchColumInfo() {
		var trLen = $("#tableColInfo tr").length;
		
		if(trLen > 0) {
			$("#tableColInfo").parent().width($("#tableColDiv").width() - 10 - 10);
			openDialog('tableColDiv');
		} else {
			$.ajax({
				type: "POST",
				url : '${rc.getContextPath()}/zhsq/event/importEvent4File/fetchColumnInfo.jhtml',
				data: '',
				dataType:"json",
				success: function(data) {
					var html = "",
						options = { 
				            axis : "yx", 
				            theme : "minimal-dark" 
				        }; 
			        enableScrollBar('tableColInfoDiv',options);
			        
					for(var key in data) {
						html += "<tr>" +
									"<td class='LeftTd'>" +
										"<span>" + data[key].COMMENTS + "</span>" +
									"</td>" +
									"<td>" +
										"<span>" + data[key].COLUMN_NAME + "</span>" +
									"</td>" +
								"</tr>"; 
					}
					
					$("#tableColInfo").append(html);
			        
			        $("#tableColInfo").parent().width($("#tableColDiv").width() - 10 - 10);//扣除左右边距
			        
					openDialog('tableColDiv');
				},
				error:function(data) {
					$.messager.alert('错误','获取事件导入中间表列信息失败！','error');
				}
			});
		}
	}
	
	function resetCondition() {//重置
		$('#eventImportForm')[0].reset();
		$('#keyWord').addClass('keyBlank');
		
		searchData();
	}
	
	function searchData(isCurrent){//查询
		var searchArray = new Array();
		
		$("#eventImportForm .queryParam").each(function() {
			var val = $(this).val(), key = $(this).attr("name");
			
			if($(this).hasClass("keyBlank")) {
				val = "";
			}
			
			if(isNotBlankString(val) && isBlankString(searchArray[key])){
				searchArray[key] = val;
			}
		});
		
		doSearch(searchArray, isCurrent);
	}
    
    function doSearch(queryParams, isCurrent){
		$('#list').datagrid('clearSelections');
		$("#list").datagrid('options').queryParams = queryParams;
		
		if(isCurrent && isCurrent == true) {
			$("#list").datagrid('reload');
		} else {
			$("#list").datagrid('load');
		}
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
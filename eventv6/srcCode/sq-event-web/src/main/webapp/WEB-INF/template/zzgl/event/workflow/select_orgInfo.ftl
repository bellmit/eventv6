<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>新组织-组织选择</title>
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/ztree/zTreeStyle/zTreeStyle.css" />
	
	<script type="text/javascript" src="${rc.getContextPath()}/js/ztree/jquery.ztree.core-3.5.js"></script>
	<script type="text/javascript" src="${rc.getContextPath()}/js/workflow/easyui-utils.js"></script>
	<script type="text/javascript" src="${ANOLE_COMPONENT_URL}/js/ztree/search_tree.js"></script>
	
	<style type="text/css">
		html, body{overflow:hidden;}
		/**表格列表总样式*/
		.select-table{
			font-size:12px;
			border-collapse:collapse;
		}
		.select-table .select-th {
			height:28px;
			line-height:28px;
			color:#000000;
		
			border-bottom:1px solid #39BBF8;
			border-right:1px solid #39BBF8;
			background-color: #F3F8FE;
		}
		.select-table .select-td {
		
			border-bottom:1px solid #39BBF8;
			border-right:1px solid #39BBF8;
			background-color: #F3F8FE;
		}
		
		.user-table-cell {
		  margin: 0;
		  padding: 0 4px;
		  white-space: nowrap;
		  word-wrap: normal;
		  overflow: hidden;
		  height: 18px;
		  line-height: 18px;
		  font-size: 12px;
		}

	</style>	
</head>

<body>
	<!-- 组织、角色、职位人员选择面板 -->
	<div id="orgSelectorWin" class="easyui-window">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'center',border:false" style="overflow:hidden;">
				<table width="100%" border="0" cellspacing="0" cellpadding="0" class="select-table" style="height:255px;">
					<tr>
						<td width="50%" style="background-color: #FFFFFF;" valign="top" class="select-td">
						 	<div id="orgInfoLeftPanel" class="easyui-panel"  data-options="title:'组织机构'" style="overflow:hidden;">
						 		<input type="text" id="orgInfoSearchCondition" title="请输入组织名称后回车" placeholder="请输入组织名称后回车" class="inp1 hide" style="width: 96%; margin: 4px 0 4px 4px;" value="" onkeydown = "SelectWin4OrgInfo._ztreeOnkeydown(this);" />
			            		<div id="orgInfoLeftTree" class="ztree" style="height:220px; overflow:auto;"></div>
			            	</div>
			            </td>
						<td style="background-color: #FFFFFF;" valign="top" class="select-td">
							<table id="selectedOrgTable"></table>
						</td>
					</tr>
				</table>
			</div>
			<div data-options="region:'south',border:false" style="padding-bottom:10px;">
	        	<div class="BtnList">
		        	<a href="###" class="BigNorToolBtn BigJieAnBtn" onclick="SelectWin4OrgInfo.saveNextOrgInfo();">确定</a>
	        		<a href="###" class="BigNorToolBtn CancelBtn" onclick="SelectWin4OrgInfo.closeWin();">取消</a>
	            </div>
	        </div>
	       
		</div>
	</div>
</body>

<script type="text/javascript">
	(function($) {
		var _nodeId = '',
			nodeType_ = 'task',
			nextOrgIdElId = null,
			nextOrgNameElId = null,
			nextOrgNameHtmlId = null,
			$userSelectedLimit = null,//已选择的用户记录数限制
			$formId	= "<#if formId??>${formId?c}</#if>",//表单id
			$formType = "";//表单类型
			
		$(function() {
			$("#orgSelectorWin").window('close');
			// 已选择组织列表
			$("#selectedOrgTable").datagrid({
				title : '已选择组织<b style="color:red">【双击移除】</b>',
				nowrap : false,
				fit : true,// 自动大小
				border : false,
				method : 'post',
				singleSelect : true,
				rownumbers : true,
				url : "",
				idField : 'orgId',
				// pagination:false,
				columns : [ [ 
					{ field : 'orgId', hidden : true }, 
					{ title : '组织名称', field : 'orgName', width : '315', align : 'center',
						formatter:function(value,rec,rowIndex) {
							var f = '<span title="'+ rec.orgName +'">'+value+'</span>';
							return f;
						}
					} 
				] ],
				queryParams : {
					'type' : '1'
				},
				onDblClickRow : function(rowIndex, rowData) {
					$("#selectedOrgTable").datagrid('deleteRow', rowIndex);
					
					event.stopPropagation();
				}
			});
		});
		
		//对外开放方法
		SelectWin4OrgInfo = {
			selectOrgInfoByObj	:	selectOrgInfoByObj,
			saveNextOrgInfo		:	saveNextOrgInfo,
			closeWin			:	closeWin,
			_ztreeOnkeydown		:	_ztreeOnkeydown
		};
		
		/********************************************************************
		* nextOrgIdElId				下一环节办理人员组织id存放的元素id
		* nextOrgNameElId			下一环节办理人员组织名称存放的元素id
		* nextOrgNameHtmlId			下一环节办理人员组织名称页面展示元素id
		* curNodeName				当前节点名称
		* nextNodeName				下一节点名称
		* nextNodeId				下一节点id
		* nextNodeType				下一节点类型
		* formId					表单id
		* formType					表单类型
		* userSelectedLimit			已选择的用户记录数限制
		* isShowOrgNameFuzzyQuery	是否展示组织名称模糊查询条件，true为展示；默认为false
		********************************************************************/
		function selectOrgInfoByObj(obj) {
			if(obj == null) {
				obj = {};
			}
			
			var isShowOrgNameFuzzyQuery = obj.isShowOrgNameFuzzyQuery || false,
				_ztreeOnNodeCreated = null;
			_nodeId = obj.nextNodeId || $("#nodeId").val();
			nodeType_ = obj.nextNodeType || '';
			nextOrgIdElId = obj.nextOrgIdElId || '';
			nextOrgNameElId = obj.nextOrgNameElId || '';
			nextOrgNameHtmlId = obj.nextOrgNameHtmlId;
			$userSelectedLimit = obj.userSelectedLimit || null;
			$formId = obj.formId || '';
			$formType = obj.formType || '';
			
			if (nodeType_ == 'fork' && !$("#" + nodeId).attr('checked')) {
				return false;
			}
			
			// 清空人员选择器
			$('#selectedOrgTable').datagrid('loadData', { total : 0, rows : [] });
			
			//清空查询条件
			$('#orgInfoSearchCondition').val('');
			
			if(isShowOrgNameFuzzyQuery == true) {
				$('#orgInfoLeftTree').height($('#orgInfoLeftTree').height() - $('#orgInfoSearchCondition').height());
				$('#orgInfoSearchCondition').show();
				_ztreeOnNodeCreated = _ztreeExpandAll;
			} else {
				$('#orgInfoLeftTree').height($('#orgInfoLeftTree').height() - 10);//扣除orgInfoSearchCondition的上下边距，共10px
			}
			
			// start:组织树渲染
			var nodeCode = g_EventNodeCode.nodeCode;
			$('#orgInfoLeftPanel').panel({ title : '组织机构', fit : true, border : false });
			var setting = {
				view: {
					expandSpeed: "",	//不设置折叠、打开的动画效果
					showLine: false,	//不展示连线
					fontCss: setFontCss_ztree
				},
				async : {
					enable : true,
					url : "${rc.getContextPath()}/zhsq/keyelement/keyElementController/getTreeForEvent.jhtml",
					autoParam : [ 'id', 'gridLevel=level', 'professionCode' ],
					otherParam : {
						nodeId : _nodeId,
						nodeCode : nodeCode,
						formId : $formId,
						formType : $formType,
						orgRootId : '<#if orgRootId??>${orgRootId?c}</#if>'
					},
					type : "get"
				},
				callback : {
					onAsyncSuccess : function(event, treeId, treeNode, msg) {
						var leftHtml = $("#orgInfoLeftTree").html();
						
						if(leftHtml.length == 0) {
							$.messager.alert('警告','没有可使用的组织机构，请先配置！','warning');
						}
					},
					onClick : function(event, treeId, treeNode, clickFlag) {
						var level = parseInt(treeNode.gridLevel);
						if (isNaN(level) || treeNode.clickable) {
							appendRow($("#selectedOrgTable"), {
								orgId : treeNode.id,
								orgName : treeNode.name
							});
						} 
						
						event.stopPropagation();
					},
					onNodeCreated : _ztreeOnNodeCreated
				}
			};
			$.fn.zTree.init($("#orgInfoLeftTree"), setting);
			// end:组织树渲染
			
			$("#orgSelectorWin").window({
				title : '组织选择',
				width : 700,
				height : 350,
				left : ($(window).width() - 700) / 2,
				top : ($(window).height() - 350) / 2 + $(window).scrollTop(),
				modal : true,
				minimizable : false,
				maximizable : false,
				collapsible : false
			});
			
			setSelOrgInfo();
			alterOrgDatagrid();
		}
		
		function saveNextOrgInfo() {//点击确定回调方法
			var orgIds = '',
				orgNames = '',
				rows = $("#selectedOrgTable").datagrid('getRows');
		
			$.each(rows, function(i, val) {
				orgIds += ',' + val.orgId;
				orgNames += ',' + val.orgName;
			});
			
			if(nextOrgIdElId) {
				$('#' + nextOrgIdElId).val(orgIds.substring(1));
			}
			if(nextOrgNameElId) {
				$('#' + nextOrgNameElId).val(orgNames.substring(1));
			}
			if(nextOrgNameHtmlId) {
				$('#' + nextOrgNameHtmlId).html(orgNames.substring(1));
			}
			
			closeWin();
		}
		
		function alterOrgDatagrid() {//调整表格行高、列宽
			$("#orgSelectorWin .datagrid-cell, #orgSelectorWin .datagrid-cell-group, #orgSelectorWin .datagrid-header-rownumber, #orgSelectorWin .datagrid-cell-rownumber").addClass("user-table-cell");
			
			$("#orgSelectorWin .datagrid-header-row, #orgSelectorWin .datagrid-row").height(25);
			
			$("#orgSelectorWin .datagrid-cell-rownumber, #orgSelectorWin .datagrid-header-rownumber").css({"padding": 0});
			
			//调整选人弹出框内部的标题颜色
			$("#orgSelectorWin td div.panel-title").css('color', '#000');
			$("#orgSelectorWin td div.panel-title").parent()
									 .css('background', '#eeeeee')
									 .css('filter', 'progid:DXImageTransform.Microsoft.gradient(startColorstr=#F8F8F8,endColorstr=#eeeeee,GradientType=0)');//IE下生效
		}
		
		function destoryTree() {
			orgInfoLeftTree = null;
			$("#selectedOrgTable").datagrid('reload');
		}
		
		// 关闭窗口
		function closeWin() {
			$("#orgSelectorWin").window('close');
			clearAll();
			event.stopPropagation();
		}
		
		function appendRow(tableObj, rows) {//添加行
			var selectedRows = $(tableObj).datagrid('getRows');
			
			if(isExistRow(selectedRows, rows.orgId)) {
				$(tableObj).datagrid("appendRow", rows);
			}
			
			alterOrgDatagrid();
		}
		
		function isExistRow(rows, uOrgId) {//判断用户是否已选择
			var tag = true;
			if($userSelectedLimit && rows.length >= $userSelectedLimit) {
				tag = false;
				$.messager.alert('警告', '已选择组织记录不得超过'+$userSelectedLimit+'条！', 'warning');
			} else {
				$.each(rows, function(i, val) {
					if (val.orgId == uOrgId) {
						tag = false; return false;
					}
				});
				
				if(!tag) {
					$.messager.alert('提示', '该组织已添加过！', 'info');
				}
			}
			return tag;
		}
		
		/**
		 * 清空选中表
		 */
		function clearAll() {
			$('#selectedOrgTable').datagrid('loadData', {
				total : 0,
				rows : []
			});
		}
		
		// 将saveForm中的usernames对应到已选人员选择面板
		function setSelOrgInfo() {
			var orgIds = null,
				orgNames = null;
			
			if(nextOrgIdElId) {
				orgIds = $("#" + nextOrgIdElId).val();
			}
			if(nextOrgNameElId) {
				orgNames = $("#" + nextOrgNameElId).val();
			}
			
			if(orgIds && orgNames) {
				var arr_orgIds = orgIds.split(","),
					arr_orgNames = orgNames.split(",");
		
				for(var i = 0, len = arr_orgIds.length; i < len; i++) {
					appendRow($('#selectedOrgTable'), {
						orgId : arr_orgIds[i],
						orgName : arr_orgNames[i]
					});
				}
			}
		}
		
		function _ztreeOnkeydown(obj) {
			var keyCode = event.keyCode,
				keyValue = $(obj).val();
				
			if(keyCode == 13 && keyValue) {
				var treeId = 'orgInfoLeftTree';  
				
				_ztreeExpandAll(null, treeId);
			}
		}
		
		function _ztreeExpandAll(event, treeId, treeNode) {
			var orgInfoSearchCondition = $('#orgInfoSearchCondition').val();
			
			if(isNotBlankStringTrim(orgInfoSearchCondition)) {
				var treeObj = $.fn.zTree.getZTreeObj(treeId),
					nodeArray = [],
					node = null;
				
				if(treeNode) {
					nodeArray.push(treeNode);
				} else {
					nodeArray = treeObj.transformToArray(treeObj.getNodes())
				}
				
				for(var index in nodeArray) {
					node = nodeArray[index];
					
					if(node.isParent == true) {
						treeObj.expandNode(node, true, true);
					}
				}
			
				search_ztree(treeId, 'orgInfoSearchCondition');
			}
		}
	
	})(jQuery);
	
</script>

</html>
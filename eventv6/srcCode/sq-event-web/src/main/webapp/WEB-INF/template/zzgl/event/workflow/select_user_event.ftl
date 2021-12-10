<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>人员选择</title>
	<link href="${uiDomain!''}/css/normal.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/ztree/zTreeStyle/zTreeStyle.css" />
	
	<script type="text/javascript" src="${rc.getContextPath()}/js/ztree/jquery.ztree.core-3.5.js"></script>
	<script type="text/javascript" src="${rc.getContextPath()}/js/workflow/easyui-utils.js"></script>
	
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
		.mulit-table {
			width:200px;
		
		}
		
		.mulit-table td{
			height:25px;
			line-height:25px;
			border-bottom:1px solid #CCC;
			background-color: #FFFFFF;
		}
	
	</style>	
</head>

<body>
	<!-- 组织、角色、职位人员选择面板 -->
	<div id="userSelectorWin" class="easyui-window">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'center',border:false" style="overflow:hidden;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="select-table" style="height:255px;">
				<input type="hidden" name="preNode" id="preNode" />
				<input type="hidden" name="targetNode" id="targetNode" />
				<tr>
					<th class="select-th">快速定位</th>
					<td colspan="3" class="select-td">
						<input type="text" name="fastSearchTxt" id="fastSearchTxt"
								onkeyup="fastSearchUser();" onfocus="searchInputFocus();"
								onblur="searchInputBlur();" value="请输入人员姓名或者帐号..."
								style="width: 240px; margin-right:10px;" class="fl" />
						<!-- <div id="fast_user_div" style="position:absolute;z-index:99;border:1px solid #817F82;background:white;width:238px;display:none;"></div> -->
						
						<div id="fast_user_div" tabindex="0" hidefocus="true" onmouseout="hideFastUser();"
							onmousedown="displayFastUser();" onmouseover="displayFastUser();"
									style="position:absolute;z-index:99;border:1px solid #817F82;background:white;width:238px;display:none;"></div>
						<div class="Check_Radio">
							<span>
								<input type='checkbox' id='promoter' userId="<#if (proInstance?? && proInstance.userId??)>${proInstance.userId?c}</#if>" userName="<#if (proInstance?? && proInstance.userName??)>${proInstance.userName}</#if>" orgName="<#if proInstance?? && proInstance.orgName??>${proInstance.orgName}</#if>" orgId="<#if proInstance?? && proInstance.orgId??>${proInstance.orgId}</#if>"/><label for="promoter" style="cursor:pointer;">流程发起人</label>
							</span>
						</div>
						
					</td>
				</tr>
				<tr>
					<td width="25%" style="background-color: #FFFFFF;" valign="top" class="select-td">
					 	<div id="leftPanel" class="easyui-panel"  data-options="title:'组织机构'" >
		            		<div id="leftTree" class="ztree" style="height:192px; overflow:auto;"></div>
		            	</div>
		            </td>
		            <td width="35%" style="background-color: #FFFFFF;" valign="top" class="select-td">
		            	<table id="userTable"></table>
						
		            </td>
		           	<td width="10%" align="center" class="select-td">
		           		<div style="width:57px; margin:0 auto;">
			           		<a href="###" class="NorToolBtn AddBtn" onclick="addCheckUser();" style="margin:0 0 10px 0;">添加</a>
			           		<a href="###" class="NorToolBtn DelBtn" onclick="delUser();" style="margin:0 0 10px 0;">删除</a>
							<a href="###" class="NorToolBtn DelBtn" onclick="delAllUser();" style="margin:0 0 10px 0;">全删</a><br/>
						</div>
					</td>
					<td width="30%" style="background-color: #FFFFFF;" valign="top" class="select-td">
						<table id="selUserTable"></table>
					</td>
				</tr>
			</table>
			</div>
			<div class="" data-options="region:'south',border:false" style="padding-bottom:10px;">
	        	<div class="BtnList">
		        	<a href="###" class="BigNorToolBtn BigJieAnBtn" onclick="saveNextUser();" style="width:36px;">确定</a>
	        		<a href="###" class="BigNorToolBtn CancelBtn" onclick="closeWin();" style="width:36px;">取消</a>
	            </div>
	        </div>
	       
		</div>
	</div>
</body>

<script type="text/javascript">
	
	/**
	 * 人员选择器
	 * @param {Object} nodeId
	 */
	 
	var _nodeId = '';
	var _curnodeName = '';
	var _nodeName = '';
	/*-------------导致滚动条异常----------------*/
	var nodeType_ = 'task';
	
	var hiddenId = 'userIds';
	var fileldId = 'userNames';
	var curOrgIds = 'curOrgIds';
	var targetIndex = "undefinded";//选择列表中被选中行的索引
	
	function selectUser(_hiddenId, _fileldId, _curOrgIds, _nodeType, nodeId, curNodeName, nodeName){
	
		if(_nodeType=='fork' && !$("#"+nodeId).attr('checked')) {
			return false;
		}
		_nodeId = nodeId||$("#nodeId").val();//145001
		_curnodeName = curNodeName||$("#curNodeName").val();
		$("#preNode").val(_curnodeName);
		_nodeName = nodeName||$("#nodeName_").val();
		$("#targetNode").val(_nodeName);
		hiddenId = _hiddenId;
		fileldId = _fileldId;
		nodeType_ = _nodeType;
		curOrgIds = _curOrgIds;
		
		//清空人员选择器 
		$('#userTable').datagrid('loadData', { total: 0, rows: [] });
		$('#selUserTable').datagrid('loadData', { total: 0, rows: [] });
		//$("#promoter").attr("checked",false);
		$("#fast_user_div").hide();
		
		$.ajax({ 
			type: "POST",
			url: "${rc.getContextPath()}/zhsq/workflow/workflowController/getNodeActorForEvent.jhtml",
			data:{
				nodeId:_nodeId,
				curnodeName:_curnodeName,
				nodeName:_nodeName
			},
			dataType:"json",
			success: function(result){
				var dataObj=eval("("+result+")");
	
				if(result != null && result.length > 0){
					var rootText = null;
					if(rootText == null){
						
						if(dataObj[0].ACTOR_TYPE=='1'){
							$('#leftPanel').panel({title:'组织机构',fit:true, border:false});
							selectUserWin(dataObj,_curnodeName,_nodeName);
							setSelUser();
						} else if(dataObj[0].ACTOR_TYPE=='2') {
							$('#leftPanel').panel({title:'角色列表',fit:true, border:false});
							selectUserWin(dataObj);
							setSelUser();
						} else if(dataObj[0].ACTOR_TYPE=='3') {
							openSignleUserWin(dataObj);//用户列表
							setSelUser();
						} else if(dataObj[0].ACTOR_TYPE=='4') {
							$('#leftPanel').panel({title:'职位列表',fit:true, border:false});
							selectUserWin(dataObj,_curnodeName,_nodeName);
							setSelUser();
						}
						//$('#leftPanel').panel({title:'组织机构',fit:true, border:false});
						//selectUserWin(dataObj);
					}
	
				}
			}
		});
	}
	
	
	function destoryTree(){
		leftTree = null;
		$("#userTable").datagrid('reload');
		$("#selUserTable").datagrid('reload');
	}
	
	
	function selectUserWin(result,curNodeName,nodeName){
		//组织、角色、职位人员选择面板
		var leftTree = null;
		var leftSetting = {
			async: {
				enable: true,
				autoParam:['id'],
				otherParam:{"curnodeName":curNodeName,"nodeName":nodeName},
				//otherParam:{"id":"-1"}
				type:"post",
				url:"${rc.getContextPath()}/zhsq/workflow/workflowController/getOrgTreeForEvent.jhtml"
			},
			callback: {
				onClick: function(event, treeId, treeNode, clickFlag){
					$("#userTable").egrid('reload',{
						'type':treeNode.type,
						 id :treeNode.id
					});
					
				}
			}
			
		};
		if(nodeType_ == 'task' && (_nodeId != $("#nodeId").val())){
			destoryTree();
		} else if(nodeType_ == 'fork' && (_nodeId != $("#forkNodeId").val())){
			destoryTree();
		}
		
		leftTree = $.fn.zTree.init($("#leftTree"), leftSetting);
		for(var i=0; i < result.length; i++){
			leftTree.addNodes(null, {
				id:result[i].ACTOR_ID, 
				name:result[i].ACTOR_NAME, 
				open:true, 
				isParent:true, 
				'type':result[i].ACTOR_TYPE});
		}
	
		left = parseInt(($(window).width() - 700)/2);
		
		$("#userSelectorWin").window({
			title:'人员选择',
		    width:700,    
		    height:350, 
		    left:left,
		    top:($(window).height()-350)/2+$(window).scrollTop(),
	    	modal:true,
	    	minimizable:false,
	    	maximizable:false,
	    	collapsible:false  
		});
	}
	
	function delUser(){
		var row = $('#selUserTable').datagrid('getSelected');
		//$('#selUserTable').datagrid('deleteRow', row.index);
		if(row) {
			$('#selUserTable').datagrid('deleteRow',targetIndex);
			if(row.userId == $("#promoter").attr("userId")){
				$("#promoter").attr("checked",false);
			}
		} else {
			$.messager.alert('提示','请选择一条记录!');
		}
	}
	
	function delAllUser(){
		clearAll();
		$("#promoter").attr("checked",false);
		/* var rows = $("#selUserTable").datagrid('getRows');
		$.each(rows, function(i, val){
			$("#selUserTable").datagrid('deleteRow',i);
		}); */
	}
	
	
	function saveNextUser(){
		var ids = '';
		var names = '';
		var orgIds ='';
		var rows = $("#selUserTable").datagrid('getRows');
	
		$.each(rows, function(i, val){
			ids += val.userId+',';
			names += val.partyName+',';
			orgIds += val.orgId+',';
		});
		
		$('#'+hiddenId).val(ids.substring(0, ids.length-1));
		$('#'+fileldId).html(names.substring(0, names.length-1));
		$('#'+curOrgIds).val(orgIds.substring(0, orgIds.length-1));
		closeWin(); 
		
	}
	
	//关闭窗口
	function closeWin(){
		$("#userSelectorWin").window('close'); 
		clearAll();
	}
		
	
	$(function(){
		$("#userSelectorWin").window('close'); 
		//用户列表
		$("#userTable").datagrid({
			title:'人员列表',
			nowrap:false,
			fit: true,//自动大小
			border:false,
			method:'post',
			loadMsg:'数据加载中...',
			singleSelect:true,
			rownumbers:true,
			url : "${rc.getContextPath()}/zhsq/workflow/workflowController/getUserListByOrgId.jhtml",
			idField:'userId',
			onDblClickRow:function(rowIndex, rowData){
	
				var rows = $("#selUserTable").datagrid('getRows');
	
				if(isExistRow(rows, rowData.userId)) {
					$("#selUserTable").datagrid('appendRow',{
					  	userId:rowData.userId,
					  	partyName:rowData.partyName,
					  	orgId:rowData.orgId
				  	});
				  	if(rowData.userId == $("#promoter").attr("userId")){
						$("#promoter").attr("checked",true);
					}
				}
	
			},
			columns:[[
			  	{field:'userId',hidden:true},
			  	{field:'orgId',hidden:true},
	          	{title:'用户名称',field:'partyName', width:'213',align:'center'}
	
			]],
			queryParams:{
				'type':'1'
			}
		});
		//已选择用户列表
		$("#selUserTable").datagrid({
			title:'已选择人员',
			nowrap:false,
			fit: true,//自动大小
			border:false,
			method:'post',
			singleSelect:true,
			rownumbers:true,
			url : "",
			idField:'userId',
			//pagination:false,
			columns:[[
			  	{field:'userId',hidden:true},
			  	{field:'orgId',hidden:true},
	          	{title:'用户名称',field:'partyName',width:'178',align:'center'}
			]],
			queryParams:{
				'type':'1'
			},
			onClickRow: function(rowIndex,rowData){
				targetIndex = rowIndex;
			},
			onDblClickRow:function(rowIndex, rowData){
				$("#selUserTable").datagrid('deleteRow',rowIndex);
				if(rowData.userId == $("#promoter").attr("userId")){
					$("#promoter").attr("checked",false);
				}
			}
		});
		//快速定位
		$("#promoter").click(function(){
			var rows = $("#selUserTable").datagrid('getRows');
			if($(this).attr('checked')){
				if(isExistRow(rows, $(this).attr('userId'))) {
					$("#selUserTable").datagrid('appendRow',{
					  	userId:$(this).attr('userId'),
					  	orgId:$(this).attr('orgId'),
					  	partyName:$(this).attr('userName')
				  	});
				}
			} else {
				deleteRow("#selUserTable", rows, $(this).attr('userId'));
			}
		});
	});
	
	function deleteRow(tableId, rows, uId){
		$.each(rows, function(i, val){
			if(val.userId == uId){
				var index = $("#selUserTable").datagrid('getRowIndex', val);
				$(tableId).datagrid('deleteRow', index);
				
			}
		});
	}
	
	function isExistRow(rows, uId){
		var tag = true;
		$.each(rows, function(i, val){
			if(val.userId == uId){
				tag = false;
			}
		});
		return tag;
	}
	
	
	
	/**********************************用户人员选择面板**********************************/
	
	//打开人员选择面板
	function openSignleUserWin(result){
		if(_nodeId != $("#nodeId").val()){
			$("#signleUserTree").datagrid('reload');
			$("#signleSelUserTree").datagrid('reload');
		}
		
		$('#signleUserTree').datagrid('loadData', { total: 0, rows: [] });
		
		for(var i=0; i < result.length; i++){
			$("#signleUserTree").datagrid('appendRow',{
			  	userId:result[i].ACTOR_ID,
			  	partyName:result[i].ACTOR_NAME
		  	});
		}
	
		left = parseInt(($(window).width() - 700)/2);
		
		$("#signleUserWin").window({
			title:'人员选择',
		    width:650,    
		    height:350, 
		    left:left,
		    top:($(window).height()-350)/2+$(window).scrollTop(),
	    	modal:true,
	    	minimizable:false,
	    	maximizable:false,
	    	collapsible:false 
		}); 
	}
	
	//保存人员选择面板
	function signleSaveNextUser(){
		var ids = '';
		var names = '';
		
		var rows = $("#signleSelUserTree").datagrid('getRows');
	
		$.each(rows, function(i, val){
			ids += val.userId+',';
			names += val.partyName+',';
		});
	
		$("#signleUserWin").window('close'); 
		$('#'+hiddenId).val(ids.substring(0, ids.length-1));
		$('#'+fileldId).html(names.substring(0, names.length-1));
		
	}
	
	//关闭人员选择面板
	function signleCloseWin(){
		$("#signleUserWin").window('close'); 
	}
	
	$(function(){
		//用户列表
		$("#signleUserTree").datagrid({
			title:'人员列表',
			nowrap:false,
			fit: true,//自动大小
			method:'post',
			singleSelect:true,
			rownumbers:true,
			idField:'userId',
			onDblClickRow: function(rowIndex, rowData){
	
				var rows = $("#signleSelUserTree").datagrid('getRows');
	
				if(isExistRow(rows, rowData.userId)) {
					$("#signleSelUserTree").datagrid('appendRow',{
					  	userId:rowData.userId,
					  	partyName:rowData.partyName
				  	});
				}
			},
			columns:[[
			  	{field:'userId',hidden:true},
	          	{title:'用户名称',field:'partyName', width:'200',align:'center'}
	
			]]
		});
	
		//已选择用户列表
		$("#signleSelUserTree").datagrid({
			title:'已选择人员',
			nowrap:false,
			fit: true,//自动大小
			method:'post',
			singleSelect:true,
			rownumbers:true,
			url : "",
			idField:'userId',
			columns:[[
			  	{field:'userId',hidden:true},
	          	{title:'用户名称',field:'partyName',width:'200',align:'center'}
			]],
			onDblClickRow:function(rowIndex, rowData){
				$("#signleSelUserTree").datagrid('deleteRow',rowIndex);
			}
		});
		
		$("#signlePromoter").click(function(){
			var rows = $("#signleSelUserTree").datagrid('getRows');
			if($(this).attr('checked')){
				if(isExistRow(rows, $(this).attr('userId'))) {
					$("#signleSelUserTree").datagrid('appendRow',{
					  	userId:$(this).attr('userId'),
					  	partyName:$(this).attr('userName')
				  	});
				}
			} else {
				deleteRow("#signleSelUserTree", rows, $(this).attr('userId'));
			}
		});
		
	});
	
	/**
	 * 清空选中表
	 */
	function clearAll() {
		$('#userTable').datagrid('loadData', { total: 0, rows: [] });
		$('#selUserTable').datagrid('loadData', { total: 0, rows: [] });
	}
	
	// 取消
	function cancle() {
		clearAll();
		closeWin();
	}
	
	// 添加用户，同一组织下的同一用户不能重复添加
	function addCheckUser() {
		var row = $('#userTable').datagrid('getSelected');
		if(row){
			var userId = row.userId;
			var partyName = row.partyName;
			var orgId = row.orgId;
			var data=$('#selUserTable').datagrid('getData');
			var falg = true;
			for(i=0; i<data.rows.length; i++){
				var drow = data.rows[i];
				if(drow.userId == userId) {
					falg = false;
					$.messager.alert('提示','该用户已添加过！', 'info');
					break;
				}
			}
			if (falg) {
				$('#selUserTable').datagrid('appendRow',{
					userId: userId,
					orgId: orgId,
				  	partyName: partyName
				});
			}
		} else{
			$.messager.alert('提示','请选择一条记录!');
		}
	}
	
	//将saveForm中的usernames对应到已选人员选择面板
	function setSelUser() {
		var userIds = $("#"+hiddenId).val();
		var userNames = $("#"+fileldId).html();
		var orgIds = $("#"+curOrgIds).val();
		if(userIds != "" && userNames != "") {
			var arr_userIds = userIds.split(",");
			var arr_userNames = userNames.split(",");
			var arr_orgIds = orgIds.split(",");
			var isPromoterSel = false;
			
			for(var i=0; i<arr_userIds.length; i++) {
				$('#selUserTable').datagrid('appendRow',{
					userId: arr_userIds[i],
					orgId:arr_orgIds[i],
					partyName : arr_userNames[i]
				});
				
				if(arr_userIds[i] == $("#promoter").attr("userId")){
					isPromoterSel = true;
				}
			}
			
			$("#promoter").attr("checked",isPromoterSel);
		}else{
			$("#promoter").attr("checked",false);
		}
	}
	
	// 快速定位
	function searchInputFocus() {
		$("#fastSearchTxt").val("");
	}
		
	function searchInputBlur() {
		$("#fastSearchTxt").val("请输入人员姓名或者帐号...");
	}
	
	function fastSearchUser() {
		_curnodeName = $("#preNode").val();
		_nodeName = $("#targetNode").val();
		var fastSearchTxt = $("#fastSearchTxt").val();
		if(fastSearchTxt==null || fastSearchTxt=="") {
			$("#fast_user_div").hide();
			return;
		} else {
			$("#fast_user_div").html("数据加载中...");
			$("#fast_user_div").show();
			$.post("${rc.getContextPath()}/zhsq/workflow/workflowController/searchUser.jhtml?t="+Math.random(), 
					{
						"inputName" : fastSearchTxt,
						curnodeName:_curnodeName,
						nodeName:_nodeName
					}, 
					function(data) {
						if(data.length>0) {
							var itemsHtml = '<table width="100%" class="select-table">';
							for(var i=0; i<data.length; i++) {
								//itemsHtml += '<tr style="cursor:pointer" ondblclick="itemSelected('+data[i].userId+',\''+data[i].partyName+'-'+data[i].userName+'\')">';
								itemsHtml += '<tr style="cursor:pointer" ondblclick="itemSelected('+data[i].orgId+',\''+data[i].userId+','+data[i].partyName+'-'+data[i].userName+'\')">';
								itemsHtml += '<td class="select-td" >'+data[i].partyName+'-'+data[i].userName+'-'+data[i].orgName+'</td>';
								itemsHtml += '</tr>';
							}
							itemsHtml += '</table>';
							$("#fast_user_div").html(itemsHtml);
						} else {
							$("#fast_user_div").html("<table width='100%'><tr><td><span style='color:red;'>没有找到相应数据</span></td><td><a href='javascript:void(0)' onclick='hideFastUser()'>关闭</a></td></tr></table>");
						}
					}, 
					"json");
		}
	}
	
	function itemSelected(orgId,userIdName) {
		userId = userIdName.split(",")[0];
		userName = userIdName.split(",")[1];
		var data=$('#selUserTable').datagrid('getData');
		var falg = true;
		for(i=0; i<data.rows.length; i++){
			var drow = data.rows[i];
			if(drow.userId == userId) {
				falg = false;
				$.messager.alert('提示','该用户已添加过!');
				break;
			}
		}
		if(falg) {
			$('#selUserTable').datagrid('appendRow',{
				userId: userId,
				orgId: orgId,
				partyName: userName
			});
			if(userId == $("#promoter").attr("userId")){
				$("#promoter").attr("checked",true);
			}
		}
		$("#fast_user_div").hide();
			
	}
	
	function hideFastUser() {
		$("#fast_user_div").hide();
	}
	
	function displayFastUser() {
		$("#fast_user_div").show();
	}

</script>

</html>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>管理页面</title>
<#include "/component/commonFiles-1.1.ftl" />
<link rel="stylesheet" href="${rc.getContextPath()}/js/zTree_v3/css/demo.css" />
<link rel="stylesheet" href="${rc.getContextPath()}/js/zTree_v3/css/zTreeStyle/zTreeStyle.css" />
<#include "/component/ComboBox.ftl" />
<script src="${rc.getContextPath()}/js/zTree_v3/js/jquery.ztree.core-3.5.js"></script> 
<script src="${rc.getContextPath()}/js/zTree_v3/js/jquery.ztree.excheck-3.5.js"></script> 
<script src="${rc.getContextPath()}/js/zTree_v3/js/jquery.ztree.exedit-3.5.js"></script> 
<script src="${rc.getContextPath()}/js/map/arcgis/arcgis_base/menu_conf.js"></script> 
<style type="text/css">
.ztree li span.button.add {margin-left:2px; margin-right: -1px; background-position:-144px 0; vertical-align:top; *vertical-align:middle}
</style>
</head>
<body>
<form id="tableForm" name="tableForm" action="" method="post" enctype="multipart/form-data">
		<input type="hidden" id="pgIdxId" name="pgIdxId" value="${pageIndexCfg.pgIdxCfgId!''}"/>
		<div id="content-d" class="MC_con content light">
			<div class="NorForm">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
				    		<td>
				    			<label class="LabName"><span>所属网格：</span></label>
				    			<input type="text" id="orgCode" value="<#if !admin>${pageIndexCfg.regionCode!''}</#if>" style="display:none;"/>
			        			<input type="text" id="gridId" value="<#if !admin>${pageIndexCfg.gridId!''}</#if>" style="display:none;"/>
			        			<input type="text" id="gridName" value="<#if !admin>${pageIndexCfg.gridName!''}</#if>" class="inp1 InpDisable" style="width:150px;" />
				    		</td>
				    		<td>
				    			<label class="LabName"><span>首页类型：</span></label>
				    			<input type="text" id="pgIdxType" name="pgIdxType" value="${pageIndexCfg.pgIdxType!''}" style="display:none;"/>
        						<input type="text" id="pgIdxTypeName" name="pgIdxTypeName" value="${pageIndexCfg.pgIdxTypeName!''}" class="inp1 InpDisable" style="width:150px;" />
				    		</td>
				    	</tr>
				    	<tr>
				    		<td>
				    			<label class="LabName"><span>展示方式：</span></label>
				    			<select id="displayStyle" class="sel1" style="width:80px">
			                		<option value="0" <#if pageIndexCfg.displayStyle=='0'>selected</#if>>平铺</option>
			                		<option value="1" <#if pageIndexCfg.displayStyle=='1'>selected</#if>>树形</option>
			                	</select>
				    		</td>
				    		<td>
				    			<label class="LabName"><span>状态：</span></label>
				    			<select id="status" class="sel1" style="width:80px">
			                		<option value="1" <#if pageIndexCfg.status=='1'>selected</#if>>启用</option>
			                		<option value="2" <#if pageIndexCfg.status=='2'>selected</#if>>禁用</option>
			                	</select>
				    		</td>
				    	</tr>
				    	<tr>
				    		<td colspan="2">
				    			<label class="LabName"><span> </span></label>
				    			<div id="con">
				    				<p style="color:red;">*选择左边资源池中的图层拖拽到右边的已配置图层中</p>
									<div class="content_wrap" style="margin-left:58px; padding-top:0px; width: 810px;height:260px;">
										<div class="zTreeDemoBackground left">
											<ul id="treeDemo" class="ztree" style="width:300px;"></ul>
											<p style="margin-left:130px;">图层资源池</p>
										</div>
										<div class="right">
											<ul id="treeDemo2" class="ztree" style="width:300px;"></ul>
											<p style="margin-left:130px;">已配置图层</p>
										</div>
									</div>
								</div>
				    		</td>
				    	</tr>
				    </table>
			</div>
		</div>
		<div class="BigTool">
        	<div class="BtnList">
        		<a href="#" onclick="save('saveRelatedEvents');" class="BigNorToolBtn BigJieAnBtn">保存</a>
				<a href="#" onclick="cancel();" class="BigNorToolBtn CancelBtn">取消</a>
            </div>
        </div>	
	</form>
<#include "/component/AnoleDate.ftl">
<#include "/component/maxJqueryEasyUIWin.ftl" />

</div>

<script type="text/javascript">
	var contextPath = "${rc.getContextPath()}";
	var srcOrgCode = "${pageIndexCfg.regionCode!''}";
	var srcPgIdxType = "${pageIndexCfg.pgIdxType!''}";
	
	$(function() {
		var orgCode = $("#orgCode").val();
		var pgIdxType = $("#pgIdxType").val();
		var pgIdxId = $("#pgIdxId").val();
		
		initDTree();
		initTree(pgIdxId, orgCode, pgIdxType);
		
		var height=$(window).height();
		$("#con").css("height",height-147);

		AnoleApi.initListComboBox("pgIdxTypeName", "pgIdxType", "B559", null, ["${pageIndexCfg.pgIdxType!''}"]);
		
		AnoleApi.initGridZtreeComboBox("gridName", null, function(gridId, items) {
			if (items && items.length > 0) {
				var orgCode = items[0].orgCode;
				$("#orgCode").val(orgCode);
			}
		}<#if admin>,{
			ShowOptions:{
				EnableToolbar : true
			},
			OnCleared : function(){
				$("#orgCode").val('');
			}
		}</#if>);
/*
		AnoleApi.initListComboBox("pgIdxTypeName", "pgIdxType", "B559", function(value, items) {
			var orgCode = $("#orgCode").val();
			$("#orgCode").val(orgCode);
			initTree("", orgCode, value);
		}, ["${pageIndexCfg.pgIdxType!''}"]);
		
		AnoleApi.initGridZtreeComboBox("gridName", null, function(gridId, items) {
			if (items && items.length > 0) {
			    var orgCode = items[0].orgCode;
				$("#orgCode").val(orgCode);
				var pgIdxType = $("#pgIdxType").val();
				initTree("", orgCode, pgIdxType);
			}
		});*/
	});
	
	function searchData() {
		var orgCode = $("#orgCode").val();
		var pgIdxType = $("#pgIdxType").val();
		initTree(orgCode, pgIdxType);
// 		doSearch(getQueryData());
	}
	
	function resetCondition() {
		$("#searchForm")[0].reset();
	}
	
	function save(){
		var tree = $.fn.zTree.getZTreeObj("treeDemo2");	
		var _firstL = tree.getNodes();
		for(var j=0;j<_firstL.length;j++){
			generateIndex(_firstL[j],tree);
		}
		var nodes = tree.getNodes();
		nodes = tree.transformToArray(nodes);	
		for(var i =0;i<nodes.length;i++){
			nodes[i].children = "";
		}
		var strNodes = JSON.stringify(nodes);
		var orgCode = $("#orgCode").val();
		var pgIdxType = $("#pgIdxType").val();
		var pgIdxId = $("#pgIdxId").val();
		var status = $("#status").val();
		var displayStyle = $("#displayStyle option:selected").val();
		
		<#if save??&&save=='copy'>
		pgIdxId = "";
		</#if>
		
		modleopen();
		$.ajax({
			type: 'POST',
			dataType: "json",
			data: {"orgCode":orgCode, "pgIdxId":pgIdxId, "pgIdxType":pgIdxType},
			url: "${rc.getContextPath()}/zhsq/map/menuconfigure/menuConfig/exists.jhtml",
			error: function () {
				alert('请求失败');
				modleclose();
			},
			success: function(data){
				if(data.result == true){
					saveData(orgCode,strNodes,pgIdxType,displayStyle,pgIdxId,status);
				}else{
					modleclose();
					$.messager.alert('提示','该网格已存在该首页类型的记录！','info');
				}
			}
		});
	}
	
	function saveData(orgCode,strNodes,pgIdxType,displayStyle,pgIdxId,status){
		$.ajax({
			type: 'POST',
			dataType: "json",
			data: {"srcPgIdxType":srcPgIdxType,"srcOrgCode":srcOrgCode,"orgCode":orgCode, "strNodes":strNodes, "pgIdxType":pgIdxType, "displayStyle":displayStyle, "pgIdxId":pgIdxId, "status":status},
			url: "${rc.getContextPath()}/zhsq/map/menuconfigure/menuConfig/saveMenuConfig.jhtml",
			error: function () {
				alert('请求失败');
				modleclose();
			},
			success: function(data){
				modleclose();
  				parent.flashData("保存成功！", "1");
			}
		});
	}
	
	function generateIndex(treeNode, tree){
		treeNode.index = tree.getNodeIndex(treeNode);
		if (treeNode.isParent){
	        var childrenNodes = treeNode.children; 
	        if (childrenNodes){
	            for (var i = 0; i < childrenNodes.length; i++) {
	                generateIndex(childrenNodes[i], tree);
	            }
	        }
	     }
	}

	function cancel(){
		parent.closeMaxJqueryWindow();
	}
</script>

</body>
</html>
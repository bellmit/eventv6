<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新增资源</title>
<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/maxJqueryEasyUIWin.ftl" />
<style>
.LabName{margin-left:10px;}
.NorForm td{padding-left:0px;vertical-align:middle;}
</style>
</head>
<body>
	<form id="tableForm" name="tableForm" action="${rc.getContextPath()}/zhsq/szzg/zgResourceController/saveOrUpdate.jhtml" method="post" enctype="multipart/form-data" >
		<input type="hidden" id="resTypeId" name="resTypeId" value="${entity.resTypeId!''}" />
		<input type="hidden" id="userId" name="userId" value="${userId!''}" />
		<input type="hidden" id="menuId" name="menuId" value="${entity.menuId!''}" />
		<input type="hidden" id="parentTypeId" name="parentTypeId" value="${entity.parentTypeId!''}" />
		
		
		<div id="content-d" class="MC_con content light">
			<div class="NorForm">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td class="LeftTd" style="width:100px;">
								<label class="LabName" ><span>父资源名称:</span></label>
							</td>
							<td class="LeftTd" style="width:200px;">
								<span>${entity.parentTypeName!''}</span>
							</td>
						</tr>
						<tr>
							<td class="LeftTd" style="width:100px;">
								<label class="LabName" ><span>资源名称:</span></label>
							</td>
							<td class="LeftTd" style="width:200px;">
								<input value="${entity.resTypeName!''}" id="resTypeName" name="resTypeName" type="text" class="inp1 easyui-validatebox" style="width:150px;" data-options="validType:'maxLength[24]',tipPosition:'bottom',required:true"/>
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>资源编码:</span></label>
							</td>
							<td class="LeftTd">
								<input value="${entity.typeCode!''}" id="typeCode" name="typeCode" type="text" class="inp1 easyui-validatebox" style="width:150px;"  data-options="validType:'maxLength[24]',tipPosition:'bottom',required:true"/>
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>资源图片名称:</span></label>
							</td>
							<td class="LeftTd">
								<input value="${entity.icon!''}" id="icon" name="icon" type="text" class="inp1 easyui-validatebox"  style='width:150px'  data-options="validType:'maxLength[20]',tipPosition:'bottom'">
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>专题图层名称:</span></label>
							</td>
							<td class="LeftTd">
								<input value="${entity.menuName!''}" id="menuName" name="menuName" type="text" class="inp1"  style='width:150px' onclick="getMenuName()" readonly="true" placeholder="点击选择图层">
							</td>
						</tr>
				    </table>
			</div>
		</div>
		<div class="BigTool">
        	<div class="BtnList">
        		<a href="#" onclick="tableSubmit();" class="BigNorToolBtn BigJieAnBtn">保存</a>
				<a href="#" onclick="cancel();" class="BigNorToolBtn CancelBtn">关闭</a>
            </div>
        </div>	
	</form>
	
	<#include "/component/ComboBox.ftl" />
	
</body>

<script type="text/javascript">
	$(function(){
		var id = document.getElementById('resTypeId').value;
		if(id.length>0){
			document.getElementById('typeCode').disabled = true;
		}
	});
	function getMenuName(){
		showMaxJqueryWindow("专题图层资源", "${rc.getContextPath()}/zhsq/szzg/zgResourceController/page.jhtml?page=menu_resource",250,400);
	}
	function tableSubmit(m){
		var isValid =  $("#tableForm").form('validate');
		if(!isValid){
			return;
		}
		var resTypeName = document.getElementById('resTypeName').value;
		var typeCode = document.getElementById('typeCode').value;
		if(resTypeName.length ==0){
			$.messager.alert('提示', "请填写资源名称!", 'error');return;
		}
		if(typeCode.length ==0){
			$.messager.alert('提示', "请填写资源编码!", 'error');return;
		}
		if(document.getElementById('resTypeId').value.length>0){
			$("#tableForm").ajaxSubmit(function(data) {
					if(data !=null && data.tipMsg == undefined && data.length>100){
						$.messager.alert('错误','提交保存出现异常，请联系管理员！','error');
						return;
					}
					window.parent.reloadDataForSubPage(data);
				});
			return;
		}
		var param = {'typeCode':typeCode};
		 $.ajax({
			type: "POST",
			url: "${rc.getContextPath()}/zhsq/szzg/zgResourceController/findByTypeCode.jhtml",
			data:param,
			dataType: "json",
			success: function(data) {
				if(data>0 ){
					$.messager.alert('提示',"相同资源编码已经存在！", 'error');
					return;
				}
				$("#tableForm").ajaxSubmit(function(data) {
					if(data !=null && data.tipMsg == undefined && data.length>100){
						$.messager.alert('错误','提交保存出现异常，请联系管理员！','error');
						return;
					}
					window.parent.reloadDataForSubPage(data);
				});
			},
			error:function(data){
				$.messager.alert('错误','连接超时！','error');
			}
		});
	}
	
	function cancel(){
		parent.closeMaxJqueryWindow();
	}
	
</script>
</html>
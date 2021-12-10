<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新增资源</title>
<#include "/component/commonFiles-1.1.ftl" />
<style>
.LabName{margin-left:10px;}
.NorForm td{padding-left:0px;vertical-align:middle;}
</style>
</head>
<body   class="easyui-layout">
	<form id="tableForm" name="tableForm" action="${rc.getContextPath()}/zhsq/szzg/zgResourceController/saveOrUpdate.jhtml" method="post" enctype="multipart/form-data" >
		<input type="hidden" id="resTypeId" name="resTypeId" value="${entity.resTypeId!''}" />
		<input type="hidden" id="userId" name="userId" value="${userId!''}" />
		<input type="hidden" id="menuId" name="menuId" value="${entity.menuId!''}" />
		
		
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
								<span>${entity.resTypeName!''}</span>
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>资源编码:</span></label>
							</td>
							<td class="LeftTd">
							<span>${entity.typeCode!''}</span>
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>资源图片名称:</span></label>
							</td>
							<td class="LeftTd"><span>${entity.icon!''}</span>
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>专题图层名称:</span></label>
							</td>
							<td class="LeftTd"><span>${entity.menuName!''}</span>
							</td>
						</tr>
				    </table>
			</div>
		</div>
		<div class="BigTool">
        	<div class="BtnList">
				<a href="#" onclick="cancel();" class="BigNorToolBtn CancelBtn">关闭</a>
            </div>
        </div>	
	</form>
	
</body>

<script type="text/javascript">
	
	function cancel(){
		parent.closeMaxJqueryWindow();
	}
	
</script>
</html>
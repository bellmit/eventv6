<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>地图图层菜单新增</title>
<#include "/component/commonFiles-1.1.ftl" />
<#include "/component/menuTreeBox.ftl">
</head>
	<body>
		<form id="tableForm" name="tableForm" action="${rc.getContextPath()}/zhsq/map/menuconfigure/menuConfig/update.jhtml" method="post" enctype="multipart/form-data">
			<div>
				<div id="content-d" class="MC_con content light" style="height:362px;">
			    <div class="NorForm">
			    	<input type="hidden" name="rootGdcPid" id="rootGdcPid" value="<#if rootGisDataCfg??>${rootGisDataCfg.gdcId?c}</#if>"/>
			    	<input type="hidden" name="gdcPid" id="gdcPid" value="${gisDataCfg.gdcPid?c}"/>
					<input type="hidden" name="gdcId" id="gdcId" value="${gisDataCfg.gdcId?c}"/>
					<input type="hidden" name="sort" id="sort" value="<#if gisDataCfg.sort??>${gisDataCfg.sort?c}</#if>"/>
					<input type="hidden" name="isLeaf" id="isLeaf" value="<#if gisDataCfg.isLeaf??>${gisDataCfg.isLeaf}</#if>"/>
					<table width="100%" border="0" cellspacing="0" cellpadding="0" class="border-t">
						<tr >
							<td class="LeftTd necessarily" colspan="2"><label class="LabName"><span>图层菜单名称</span></label>
								<input type="text" name="menuName" id="menuName"  class="inp1 InpDisable easyui-validatebox" style="width:638px;" data-options="required:true" value="<#if gisDataCfg.menuName??>${gisDataCfg.menuName}</#if>" />
							</td>
							<td class="LeftTd necessarily" style="display:none;"><label class="LabName"><span>图层菜单显示名称</span></label>
								<input type="text" name="menuDetailName" id="menuDetailName"  class="inp1 InpDisable easyui-validatebox" style="width:250px;" value="<#if gisDataCfg.menuDetailName??>${gisDataCfg.menuDetailName}</#if>" />
							</td>
						</tr>
						<tr >
							<td class="LeftTd necessarily"><label class="LabName"><span>图层菜单编码</span></label>
								<input type="text" name="menuCode" id="menuCode"  class="inp1 InpDisable easyui-validatebox" style="width:250px;" value="<#if gisDataCfg.menuCode??>${gisDataCfg.menuCode}</#if>" />
							</td>
							<td class="LeftTd necessarily"><label class="LabName"><span>所属上级菜单</span></label>
								<input type="text" name="parentMenuName" id="parentMenuName"  class="inp1 InpDisable easyui-validatebox" style="width:226px;" value="<#if gisDataCfg.parentMenuName??>${gisDataCfg.parentMenuName}</#if>" />
							</td>
						</tr>
						<tr>
							<td class="LeftTd necessarily"><label class="LabName"><span>地图图层名称</span></label>
								<input type="text" name="menuLayerName" id="menuLayerName"  class="inp1 InpDisable easyui-validatebox" style="width:250px;"    value="<#if gisDataCfg.menuLayerName??>${gisDataCfg.menuLayerName}</#if>" />
							</td>
							<td class="LeftTd necessarily"><label class="LabName"><span>回调方法</span></label>
								<input type="text" name="callBack" id="callBack"  class="inp1 InpDisable easyui-validatebox" style="width:250px;" value="<#if gisDataCfg.callBack??>${gisDataCfg.callBack}</#if>" />
							</td>
						</tr>
						<tr>
							<td class="LeftTd necessarily"><label class="LabName"><span>图层菜单样式</span></label>
								<input type="text" name="className" id="className" class="inp1 InpDisable easyui-validatebox" style="width:250px;" value="<#if gisDataCfg.className??>${gisDataCfg.className}</#if>" />
							</td>
							<td>
								<label class="LabName"><span>启用配置：</span></label>
								<input id="status" type="checkbox" name="status" <#if gisDataCfg.status??><#if gisDataCfg.status =='001'>checked</#if></#if> value="001" />
							</td>
						</tr>
						<tr>
							<td class="LeftTd necessarily" colspan="2"><label class="LabName"><span>菜单图标</span></label>
								<input type="text" name="largeIco" id="largeIco"  class="inp1 InpDisable easyui-validatebox" style="width:638px;" value="<#if gisDataCfg.largeIco??>${gisDataCfg.largeIco}</#if>" />
							</td>
						</tr>
						<tr>
							<td class="LeftTd necessarily" colspan="2"><label class="LabName"><span>树形小图标</span></label>
								<input type="text" name="treeIcon" id="treeIcon"  class="inp1 InpDisable easyui-validatebox" style="width:638px;" value="<#if gisDataCfg.treeIcon??>${gisDataCfg.treeIcon}</#if>" />
							</td>
						</tr>
						<tr>
							<td class="LeftTd necessarily" colspan="2"><label class="LabName"><span>定位图标</span></label>
								<input type="text" name="smallIco" id="smallIco"  class="inp1 InpDisable easyui-validatebox" style="width:638px;" value="<#if gisDataCfg.smallIco??>${gisDataCfg.smallIco}</#if>" />
							</td>
						</tr>
						<tr>
							<td class="LeftTd necessarily" colspan="2"><label class="LabName"><span>选中图标</span></label>
								<input type="text" name="smallIcoSelected" id="smallIcoSelected"  class="inp1 InpDisable easyui-validatebox" style="width:638px;" value="<#if gisDataCfg.smallIcoSelected??>${gisDataCfg.smallIcoSelected}</#if>" />
							</td>
						</tr>
						<tr>
							<td class="LeftTd necessarily"><label class="LabName"><span>框选服务</span></label>
								<input type="text" name="kuangxuanName" id="kuangxuanName"  class="inp1 InpDisable easyui-validatebox" style="width:250px;"    value="<#if gisDataCfg.kuangxuanName??>${gisDataCfg.kuangxuanName}</#if>" />
							</td>
							<td class="LeftTd necessarily"><label class="LabName"><span>周边资源服务</span></label>
								<input type="text" name="zhoubianName" id="zhoubianName"  class="inp1 InpDisable easyui-validatebox" style="width:250px;"    value="<#if gisDataCfg.zhoubianName??>${gisDataCfg.zhoubianName}</#if>" />
							</td>
						</tr>
						<tr>
							<td class="LeftTd necessarily" colspan="2"><label class="LabName"><span>汇聚数据链接</span></label>
								<input type="text" name="convergeUrl" id="convergeUrl"  class="inp1 InpDisable easyui-validatebox" style="width:638px;" value="<#if gisDataCfg.convergeUrl??>${gisDataCfg.convergeUrl}</#if>"/>
							</td>
						</tr>
						<tr>
							<td class="LeftTd necessarily" colspan="2"><label class="LabName"><span>汇聚撒点链接</span></label>
								<input type="text" name="scatterPointUrl" id="scatterPointUrl"  class="inp1 InpDisable easyui-validatebox" style="width:638px;" value="<#if gisDataCfg.scatterPointUrl??>${gisDataCfg.scatterPointUrl}</#if>"/>
							</td>
						</tr>
						<tr>
							<td class="LeftTd necessarily" colspan="2"><label class="LabName"><span>图层列表链接</span></label>
								<input type="text" name="menuListUrl" id="menuListUrl"  class="inp1 InpDisable easyui-validatebox" style="width:638px;"    value="<#if gisDataCfg.menuListUrl??>${gisDataCfg.menuListUrl}</#if>" />
							</td>
						</tr>
						<tr>
							<td class="LeftTd necessarily" colspan="2"><label class="LabName"><span>概要信息链接</span></label>
								<input type="text" name="menuSummaryUrl" id="menuSummaryUrl"  class="inp1 InpDisable easyui-validatebox" style="width:638px;"    value="<#if gisDataCfg.menuSummaryUrl??>${gisDataCfg.menuSummaryUrl}</#if>" />
							</td>
						</tr>
						<tr>
							<td class="LeftTd necessarily" colspan="2"><label class="LabName"><span>详细信息链接</span></label>
								<input type="text" name="menuDetailUrl" id="menuDetailUrl"  class="inp1 InpDisable easyui-validatebox" style="width:638px;"    value="<#if gisDataCfg.menuDetailUrl??>${gisDataCfg.menuDetailUrl}</#if>" />
							</td>
						</tr>
						<tr>
							<td class="LeftTd necessarily"><label class="LabName"><span>概要信息宽度</span></label>
								<input type="text" name="menuSummaryWidth" id="menuSummaryWidth" class="inp1 InpDisable easyui-validatebox" style="width:250px;"   value="<#if gisDataCfg.menuSummaryWidth??>${gisDataCfg.menuSummaryWidth}</#if>" />(px)
							</td>
							<td class="LeftTd necessarily"><label class="LabName"><span>概要信息高度</span></label>
								<input type="text" name="menuSummaryHeight" id="menuSummaryHeight" class="inp1 InpDisable easyui-validatebox" style="width:250px;"   value="<#if gisDataCfg.menuSummaryHeight??>${gisDataCfg.menuSummaryHeight}</#if>" />(px)
							</td>
						</tr>
						<tr>
							<td class="LeftTd necessarily"><label class="LabName"><span>详细信息宽度</span></label>
								<input type="text" name="menuDetailWidth" id="menuDetailWidth"  class="inp1 InpDisable easyui-validatebox" style="width:250px;"    value="<#if gisDataCfg.menuDetailWidth??>${gisDataCfg.menuDetailWidth}</#if>" />(px)
							</td>
							<td class="LeftTd necessarily"><label class="LabName"><span>详细信息高度</span></label>
								<input type="text" name="menuDetailHeight" id="menuDetailHeight"  class="inp1 InpDisable easyui-validatebox" style="width:250px;"    value="<#if gisDataCfg.menuDetailHeight??>${gisDataCfg.menuDetailHeight}</#if>" />(px)
							</td>
						</tr>
						<tr>
							<td colspan="4" class="LeftTd necessarily"><label class="LabName"><span>备注</span></label>
								<textarea style="width:638px; height:50px;" name="remark" id="remark"  class="inp1 InpDisable easyui-validatebox" data-options="required:false,validType:'maxLength[250]'" ><#if gisDataCfg.remark??>${gisDataCfg.remark}</#if></textarea>
							</td>
						</tr>
					</table>
			    </div>
				</div>
				<div class="BigTool">
				   	<div class="BtnList">
			            <a href="#" class="BigNorToolBtn SaveBtn" onclick="javascript:tableSubmit();">保存</a>
			   		    <a href="#" class="BigNorToolBtn CancelBtn" onclick="javascript:cancel();">取消</a>
			        </div>
			    </div>
			    <#include "/component/ComboBox.ftl">
			</div>
		</form>
	</body>
	<script type="text/javascript">
		$(function(){
		    MenuTreeApi.initMenuZtreeComboBox("parentMenuName", "gdcPid", function(divisionId, items){
				if(items && items!=undefined && items!=null && items.length>0){
					var item = items[0];
					$("#gdcPid").val(item.id);
				} 
			});
	    });
	
		var oldMenuName = "<#if gisDataCfg.menuName??>${gisDataCfg.menuName}</#if>";
		
		// 判断同一个节点下菜单名称是否唯一
		function checkMenuName() {
			var isOK = true;
			var menuName = $('#menuName').val();
		
			if (oldMenuName != menuName) {
				$.ajax({
					type: "POST",
					async : false,
					url:'${rc.getContextPath()}/zhsq/map/menuconfigure/menuConfig/checkMenuName.json',  
					data: {gdcPid:$('#gdcPid').val(),menuName:menuName},
					dataType:"json",
					success: function(result){
						if (result && result["check"]==false) {
							$('#menuName').focus();
							isOK = false;
							$.messager.alert("操作提示", "该节点下已存在名称为" + menuName + "的菜单","info");
						}
					},
					error:function(data){
						$.messager.alert('错误','连接超时！','error');
					}
				});	
			}
			
			return isOK;	
		}
		
		function tableSubmit(){
			var menuName = $('#menuName').val();
		
			if (menuName == '') {
				$.messager.alert("操作提示", "图层菜单名称必填！","info");	
				return false;	
			}
		
			if(!checkMenuName()) {
				return false;
			}
			
			var gdcPid = $("#gdcPid").val();
			var gdcId = $("#gdcId").val();
			
			if (gdcPid == gdcId) {
				$.messager.alert("操作提示", "不能选择自己为父节点！","info");	
				return false;
			}
		
		    var isValid =  $("#tableForm").form('validate');
		    
			if(isValid){
				$("#tableForm").submit();
			}
		}
		
		function cancel() {
			parent.closeMaxJqueryWindow();
		}
	</script>
</html>
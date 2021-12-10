<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<#include "/component/commonFiles-1.1.ftl" />

<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/scripts/updown/swfupload/css/swfupload.css" />
<script type="text/javascript" src="${rc.getContextPath()}/scripts/updown/swfupload/swfupload.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/scripts/updown/swfupload/handlers.js"></script>

</head>


<body>
<form id="tableForm" name="tableForm" action="${rc.getContextPath()}/zhsq/map/thresholdcolorcfg/thresholdColorCfg/save.jhtml" method="post" enctype="multipart/form-data">
<div>
     <div id="content-d" class="MC_con content light" style="height:440px;">
                <div class="NorForm">
                	<input type="hidden" name="tcId" id="tcId" value="<#if thresholdColorCfg.tcId??>${thresholdColorCfg.tcId}</#if>"/>
                	<input type="hidden" name="gridId" id="gridId" value="<#if thresholdColorCfg.gridId??>${thresholdColorCfg.gridId}</#if>"/>
                	<input type="hidden" name="orgCode" id="orgCode" value="<#if thresholdColorCfg.orgCode??>${thresholdColorCfg.orgCode}</#if>"/>
                	
                	
                	<table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td class="LeftTd necessarily"><label class="LabName"><span>所属网格：</span></label>
                        	<input style="width: 155px;" class="inp1 InpDisable easyui-validatebox" type="text" id="gridName" data-options="required:false,tipPosition:'bottom'" name="gridName" value="<#if thresholdColorCfg.gridName??>${thresholdColorCfg.gridName}</#if>" />
                        <td class="RightTd necessarily"><label class="LabName"><span>配置类别：</span></label>
                        	<input type="hidden" id="class_" name="class_" value="<#if thresholdColorCfg.class_??>${thresholdColorCfg.class_}</#if>" />
				            <input type="text" class="inp1 InpDisable easyui-validatebox" style="width:155px;" data-options="required:true" id="className" name="className" maxlength="100" value="<#if thresholdColorCfg.className??>${thresholdColorCfg.className}<#else><#if thresholdColorCfg.class_??>${thresholdColorCfg.class_}</#if></#if>" />
			          	</td>                        
                      </tr>
                      <tr>
                        <td calss="LeftTd">
							<label class="LabName"><span>配置状态：</span></label>
							
							<select name="status_" id="status_" class="sel1" data-options="required:true" style="width: 155px;">
									<option value="1" <#if ('${thresholdColorCfg.status_}'=='1')>selected="selected"</#if>>启用</option>
									<option value="2" <#if ('${thresholdColorCfg.status_}'=='2')>selected="selected"</#if>>禁用</option>
		              		</select>
						</td>
                        <td class="RightTd"><label class="LabName"><span>配置说明：</span></label>
                          <input style="width: 155px;height:28px" class="inp1 easyui-validatebox" data-options="required:false,validType:['maxLength[128]','characterCheck']" name="remark_" id="remark_" value="<#if thresholdColorCfg.remark_??>${thresholdColorCfg.remark_}</#if>" />
                        </td>
                        </tr>
                        <tr>
	                        <td colspan="2" class="LeftTd RightTd necessarily"><label class="LabName"><span>排序：</span></label>
	                        	<input style="width: 155px;height:28px" class="inp1 easyui-numberbox" data-options="required:true,validType:['maxLength[10]','characterCheck']" name="indexNum" id="indexNum" value="<#if thresholdColorCfg.indexNum??>${thresholdColorCfg.indexNum}</#if>" />
	                        </td>
                        </tr>
                        <tr>
	                        <td colspan="2" class="LeftTd RightTd necessarily"><label class="LabName"><span>参数配置：</span></label>
	                        	
	                        	
	                        	<textarea name="paramCfgStr" id="paramCfgStr" style="width: 445px; height: 110px;" cols="45" rows="5" class="area1 easyui-validatebox" data-options="required:true,validType:'maxLength[128]',tipPosition:'bottom'"/><#if thresholdColorCfg.paramCfgStr??>${thresholdColorCfg.paramCfgStr}</#if></textarea>
								<br/><span style="color:red;">注意：参数配置格式为json数据格式，一共四个参数，如：<br/>{"colorVal":"#FF0000","minValue":"15","maxValue":"100","colorNum":"0.5"}</span>
	                        </td>
                        </tr>
                        
                    </table>
                </div>
	</div>
	<div class="BigTool">
	   	<div class="BtnList">
            <a href="#" class="BigNorToolBtn SaveBtn" onclick="javascript:tableSubmit();">保存</a>
   		    <a href="#" class="BigNorToolBtn CancelBtn" onclick="javascript:window.parent.closeMaxJqueryWindow();">取消</a>
        </div>
    </div>
    <#include "/component/ComboBox.ftl">
</div>
</form>
<script type="text/javascript">
		
	$(function(){
	    AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId, items) {
			if(isNotBlankParam(items) && items.length > 0) {
				var grid = items[0];
				$("#gridCode").val(grid.orgCode);
			}
		});
		AnoleApi.initListComboBox("className", "class_", "B898", null, [<#if thresholdColorCfg.class_??>"${thresholdColorCfg.class_}"<#else>"PRECISION_POVERTY"</#if>]);
    });
	function tableSubmit(){
	    var isValid =  $("#tableForm").form('validate');
	    var msg = "";
		if(isValid){
			modleopen();
			$("#tableForm").ajaxSubmit(function(data) {
		  		if(data.flag == true){
		  			msg = "保存成功！"
		  			modleclose();
		  			parent.reloadDataForSubPage(msg);
				}else{
					modleclose();
					$.messager.alert('错误','保存失败，请重试！', 'error');
				}
			});
		}
	}
	</script>
</body>
</html>


<!DOCTYPE html>
<html>
<head>
	<title>保存</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<style type="text/css">
		.inp1 {width:100px;}
	</style>
</head>
<body>
	<form id="submitForm">
		<input type="hidden" id="" name="" value="${(bo.hydropowerCoalId)!}" />
		<div id="content-d" class="MC_con content light">
			<div name="tab" class="NorForm">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<input type="hidden" id="hydropowerCoalId" name="hydropowerCoalId"  value="${(bo.hydropowerCoalId)!}" >
					<tr>
						<td class="LeftTd">
								<label class="LabName"><span>所属区域：</span></label>
								<input type="hidden" id="orgCode" name="orgCode" value="${(bo.orgCode)!}" />
								<input type="text" class="inp1 inp2 InpDisable easyui-validatebox"   data-options="required:true" style="width: 100px;" id="orgName" value="${(bo.orgName)!}" />
						</td>
						<td class="LeftTd"><label class="LabName"><span>所属年月：</span></label>
                            <input class="inp1 inp2 InpDisable  Wdate timeClass easyui-validatebox" type="text" id="syear" value="${(bo.syear)!}"
                                   data-options="required:true" name="syear"   onClick="WdatePicker({isShowClear:false,maxDate:'%y',dateFmt:'yyyyMM'})" readonly="true"/>

                        </td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>类型：</span></label>
							<input type="hidden" name="type" id="type" value='${(bo.type)!}' />
							<input type="text" id="typeName" name="typeName" value="" class="inp1 easyui-validatebox" data-options="required:true,validType:'maxLength[10]', tipPosition:'bottom'" />
						</td>
						<td>
							<label class="LabName"><span>使用量<font id="unit"></font>：</span></label>
							<input type="text" id="usage"  data-options="required:true" name="usage" value="${(bo.usage)!}" style="height:30px;" maxlength="8" class="inp1 easyui-numberbox" min="0" max="99999999"  data-options="tipPosition:'bottom'" />  
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>同比（%）：</span></label>
							<input type="text" id="yearBasis" data-options="required:true" name="yearBasis" value="<#if bo.yearBasis??>${bo.yearBasis?string("0.##")}</#if>" style="height:30px;" class="inp1 easyui-numberbox" min="0" max="999" precision="2"   data-options="tipPosition:'bottom'" />
						</td>
						<td>
							<label class="LabName"><span>环比（%）：</span></label>
							<input type="text" id="linkRelRatio" data-options="required:true" name="linkRelRatio" value="<#if bo.linkRelRatio??>${bo.linkRelRatio?string("0.##")}</#if>" style="height:30px;" class="inp1 easyui-numberbox"  min="0" max="999" precision="2"  data-options="tipPosition:'bottom'" />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>同比增长（%）：</span></label>
							<input type="text" id="yearBasisInc" data-options="required:true" name="yearBasisInc" value="<#if bo.yearBasisInc??>${bo.yearBasisInc?string("0.##")}</#if>" style="height:30px;" class="inp1 easyui-numberbox"  min="0" max="999" precision="2"  data-options="tipPosition:'bottom'" />
						</td>
						<td>
							<label class="LabName"><span>环比增长（%）：</span></label>
							<input type="text" id="linkRelRatioInc" data-options="required:true" name="linkRelRatioInc" value="<#if bo.linkRelRatioInc??>${bo.linkRelRatioInc?string("0.##")}</#if>" style="height:30px;" class="inp1 easyui-numberbox"  min="0" max="999" precision="2"  data-options=" tipPosition:'bottom'" />
						</td>
					</tr>
				</table>
			</div>
		</div>
		<div class="BigTool">
	    	<div class="BtnList">
	    		<a href="javascript:;" class="BigNorToolBtn SaveBtn" onClick="save();">保存</a>
	    		<a href="javascript:;" class="BigNorToolBtn CancelBtn" onClick="cancel();">取消</a>
	        </div>
	    </div>
	</form>
</body>
<script type="text/javascript">
$(function(){
	AnoleApi.initGridZtreeComboBox("orgName", null, function(gridId, items) {
            if (items && items.length > 0) {
                document.getElementById('orgCode').value=items[0].orgCode;
            }
        }, {
            rootName: "所属区域",
            ChooseType : '1',
            isShowPoorIcon: "1",
            ShowOptions : { GridShowToLevel : 4,EnableToolbar : true},
            OnCleared:function(){
                document.getElementById('orgCode').value='';
            }
     });
	AnoleApi.initListComboBox("typeName", "type", null,  function(val, items){
        	if(val=='2'){
        		$("#unit").html("（度）");
        	}else{
        		$("#unit").html("（吨）");
        	}
        }, ["${(bo.type)!}"], {
		DataSrc : [{"name":"水", "value":"1"},{"name":"电", "value":"2"},{"name":"煤", "value":"3"}]
	});
});
	//保存
	function save() {
		var type=$("#type").val();;
		var isValid = $('#submitForm').form('validate');
		if (isValid) {
			modleopen(); //打开遮罩层
			$.ajax({
				type: 'POST',
				url: '${rc.getContextPath()}/zhsq/szzg/hydropowerCoal/save.json',
				data: $('#submitForm').serializeArray(),
				dataType: 'json',
				success: function(data) {
					if (data.success) {
					    parent.setType(type);
	                    parent.reloadDataForSubPage(data.tipMsg);
	                } else {
	                    modleclose();
	                    if (data.tipMsg) {
	                        $.messager.alert('错误', data.tipMsg, 'error');
	                    } else {
	                        $.messager.alert('错误', '操作失败！', 'error');
	                    }
	                }
				},
				error: function(data) {
					$.messager.alert('错误', '连接超时！', 'error');
				},
				complete : function() {
					modleclose(); //关闭遮罩层
				}
			});
		}
	}
	
	//取消
	function cancel() {
		parent.closeMaxJqueryWindow();
	}

</script>
</html>

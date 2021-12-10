<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新增人口基本情况</title>
<#include "/component/commonFiles-1.1.ftl" />
<style>
.LabName{margin-left:10px;}
.NorForm td{padding-left:0px;}
</style>
</head>
<body>
	<form id="tableForm" name="tableForm" action="${rc.getContextPath()}/zhsq/szzg/zgStatsPopController/saveOrUpdate.jhtml" method="post" enctype="multipart/form-data" >
		<input type="hidden" id="seqId" name="seqId" value="${entity.seqId!''}" />
		<input type="hidden" id="orgCode" name="orgCode" value="${entity.orgCode!''}" />
		<input type="hidden" id="stype" name="stype" value="${entity.stype!''}" />
		<input type="hidden" id="createId" name="createId" value="${userId}" />
		<input type="hidden" id="updateId" name="updateId" value="${userId}" />
		
		
		<div id="content-d" class="MC_con content light">
			<div class="NorForm">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td class="LeftTd" style="width:100px;">
								<label class="LabName" ><span>所属网格:</span></label>
							</td>
							<td class="LeftTd" style="width:200px;">
								<input value="${entity.orgName}" id="orgName" type="text" class="inp1 InpDisable" style="width:150px;"/>
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>统计年份:</span></label>
							</td>
							<td class="LeftTd">
								<input type="text" class="inp1 Wdate timeClass" id="syear" name='syear'  value="${entity.syear}"
					onClick="WdatePicker({readOnly:true,isShowOK:false,isShowClear :false, isShowToday:false,dateFmt:'yyyy'});"  style='width:149px'>
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>统计类型:</span></label>
							</td>
							<td class="LeftTd">
								<input name="stypeStr" id="stypeStr" type="text" class="inp1"  style='width:150px' value="${entity.stypeStr!''}">
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>统计数量:</span></label>
							</td>
							<td class="LeftTd">
								<input name="snum" id="snum" value="${entity.snum!'0'}" type="text" style='width:154px;height:30px;' 
								class="inp1 easyui-numberbox" data-options="required:true,max:999999999,min:0"/>
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
		AnoleApi.initGridZtreeComboBox("orgName", null, function(gridId, items) {
			if (items && items.length > 0) {
				document.getElementById('orgCode').value=items[0].orgCode;
			}
		}, { 
			isShowPoorIcon: "0",
			ShowOptions : {EnableToolbar : false}
		});
       AnoleApi.initTreeComboBox("stypeStr", null,'${pcode}',function(gridId, items){
				if(isNotBlankParam(items) && items.length>0){
					document.getElementById('stype').value=items[0].dictCode;
				} 
			},null,{ChooseType:'1'});
	});
	
	
	
	function tableSubmit(m){
		var stype = document.getElementById('stype').value;
		var syear = document.getElementById('syear').value ;
		var orgCode =document.getElementById('orgCode').value;
		if(stype.length ==0){
			$.messager.alert('提示', "请选择类型!", 'error');return;
		}
		var snum =document.getElementById('snum').value;
		if(snum.length ==0){
			$.messager.alert('提示', "请填写数量!", 'error');return;
		}
		var seqId = document.getElementById("seqId").value;
		var param = {'orgCode':orgCode,'syear': syear,'stype':stype};
		if(seqId.length>0){
			param['seqId'] = seqId;
		}
		 $.ajax({
			type: "POST",
			url: "${rc.getContextPath()}/zhsq/szzg/zgStatsPopController/findCount.jhtml",
			data:param,
			dataType: "json",
			success: function(data) {
				if(data>0){
					$.messager.alert('提示',"相同'网格/年份/类型'数据已经存在", 'error');
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
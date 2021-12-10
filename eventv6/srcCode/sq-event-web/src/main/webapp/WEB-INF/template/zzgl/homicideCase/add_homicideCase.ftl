<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>命案防控-新增</title>
	<#include "/component/standard_common_files-1.1.ftl" />
	<link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css">
	<style>
		.LabName span{padding-right: 0px;}
		.Asterik{color:red;}
		.NorForm td{    padding: 12px 0px 6px 10px;}
	</style>
</head>
<body>
	<#include "/component/ComboBox.ftl" />
	<#include "/component/FieldCfg.ftl" />
	<div id="content-d" class=" content light">
		<div class="NorForm">
			<form id="homicideCaseForm" name="homicideCaseForm" action="" method="post">
				<input type="hidden" id="hashId" name="hashId" value="<#if relatedEvent.hashId??>${relatedEvent.hashId}</#if>"/>
				<input type="hidden" name="bizType" value="4" />
		
				<div id="homicideCaseTabDiv" class="homicideTabDiv">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td colspan="2" class="LeftTd">
								<label class="LabName"><span><label class="Asterik">*</label>案件名称：</span></label>
								<input type="text" class="inp1 easyui-validatebox" style="width:65%;" data-options="required:true,tipPosition:'bottom',validType:['maxLength[100]','characterCheck']" name="reName" id="reName" value="${relatedEvent.reName!''}" />
							</td>
							<td class="LeftTd">
								<label class="LabName"><span>所属网格：</span></label>
								<input type="hidden" id="gridCode" name="gridCode" value="${relatedEvent.gridCode!}"/>
								<input type="text" class="inp1 easyui-validatebox" style="width:122px;" data-options="required:true,tipPosition:'bottom',validType:'characterCheck'" name="gridName" id="gridName" value="${relatedEvent.gridName!''}" />
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName" style="width:100px"><span><label class="Asterik">*</label>发生开始日期：</span></label>
								<input type="text" class="easyui-datebox" style="width: 110px;height: 28px;" data-options="required:true,tipPosition:'bottom'" name="occuDateStr" id="occuDateStr" value="${relatedEvent.occuDateStr!''}" editable="false" />
							</td>
							<td class="LeftTd">
								<label class="LabName" style="width:100px"><span><label class="Asterik">*</label>侦查结束日期：</span></label>
								<input type="text" class="inp1 easyui-datebox" style="width: 110px;height: 28px;" name="spyEndDateStr" id="spyEndDateStr" value="${relatedEvent.spyEndDateStr!''}" editable="false" />
							</td>
							<td class="LeftTd">
								<#if relatedEvent.reNo??>
									<label class="LabName"><span>案件编号：</span></label><div id="reNoDiv" class="Check_Radio">${relatedEvent.reNo!''}</div>
								<#else>
									&nbsp;<!--修复IE兼容模式下边线为空的问题-->
								</#if>
							</td>
						</tr>
						<tr>
							<td colspan="3" class="LeftTd">
				    			<label class="LabName"><span>简要情况：</span></label><textarea name="situation" id="situation" class="area1 easyui-validatebox" style="width:85%; height:125px;resize: none;" data-options="tipPosition:'bottom',validType:['maxLength[4000]','characterCheck']" >${relatedEvent.situation!}</textarea>
					        </td>
						</tr>
					</table>
				</div>
			</form>
		</div>
	</div>
	
	<div class="BigTool" style="position: absolute;bottom: <#if relatedEvent.hashId??>10px<#else>0px</#if>;">
    	<div class="BtnList1" id="btnList" style="position: relative;width: 230px;    height: 32px;    margin: 0 auto;">
    		<#if relatedEvent.hashId??>
    			<a href="###" onclick="tableSubmit();" class="BigNorToolBtn SaveBtn">保存</a>
    			<a href="###" onclick="cancel(parent.parent);" class="BigNorToolBtn CancelBtn">取消</a>
    		<#else>
    			<a href="###" onclick="tableSubmit();" class="BigNorToolBtn SaveBtn">下一步</a>
				<a href="###" onclick="cancel(parent);" class="BigNorToolBtn CancelBtn">取消</a>
    		</#if>
        </div>
    </div>
	
	<script type="text/javascript">
		$(function(){
			layer.load(0);// 加载遮罩层
			$.excuteFieldCfg({
				moduleCode: "homicideCase",// 必传，模块编码
				infoOrgCode: ""// 可选，不传取默认登录信息域编码
			}, function(isSuccess, msg) {// 回调函数，isSuccess：true成功/false失败
				if(isSuccess != true) {
					$.messager.alert('错误', msg, 'error');
				}
				
				initElement();
				layer.closeAll('loading'); // 关闭加载遮罩层
			});

            //发生开始日期不能大于当前日期
            $('#occuDateStr').datebox().datebox('calendar').calendar({
                validator : function(date){
                    var curDate = new Date();
                    var d1 = new Date(curDate.getFullYear(),curDate.getMonth(),curDate.getDate());
                    return d1 >= date;
                }
            })
	    });
	    
	    function initElement() {
	    	AnoleApi.initGridZtreeComboBox("gridName", null, function(gridId, items){
				if(isNotBlankParam(items) && items.length>0){
					var grid = items[0];
					$("#gridCode").val(grid.orgCode);
				} 
			});
	    }
	    
		function tableSubmit(){
		    var isValid = $("#homicideCaseForm").form('validate');
		    
			if(isValid){
				modleopen();

                var occuDateStr = $("input[name = 'occuDateStr']").val();
                var spyEndDateStr = $("input[name = 'spyEndDateStr']").val();
                console.log("发生开始日期: "+occuDateStr);
                console.log("侦察结束日期: "+spyEndDateStr);
                if(occuDateStr > spyEndDateStr){
                    $.messager.alert('提示','侦察结束日期不能小于发生开始日期','info');
                    modleclose();
                    return;
                }
				
				$("#homicideCaseForm").attr("action","${rc.getContextPath()}/zhsq/relatedEvents/homicideCase/saveHomicideCase.jhtml");
	      	
			  	$("#homicideCaseForm").ajaxSubmit(function(data) {
			  		var msg = '命案防控信息保存成功！';
			  		
			  		if(data.result){
			  			var flashObj = parent.parent;
			  			
			  			if(data.relatedEvent) {
			  				flashObj = parent;
			  			}
			  			
			  			flashObj.searchData();//刷新列表
			  			
			  			if(data.relatedEvent) {
			  				if(data.relatedEvent.hashId) {
			  					parent.edit(data.relatedEvent.hashId, "1");//跳转编辑页面
			  				}
			  			} else {
			  				flashObj.$.messager.alert('提示', msg, 'info');
			  				flashObj.closeWinExt();
				  		}
			  			
			  			//$.messager.alert('提示', msg, 'info');
	  				} else {
	  					if(data.msg) {
	  						$.messager.alert('错误', data.msg, 'error');
	  					} else {
	  						$.messager.alert('错误', '操作失败！', 'error');
	  					}
	  				}
	  				
	  				modleclose();
				});
			}
		}
		
		function cancel(obj){
			//obj.closeMaxJqueryWindow();
			obj.closeWinExt();
		}
	</script>
	
</body>
</html>

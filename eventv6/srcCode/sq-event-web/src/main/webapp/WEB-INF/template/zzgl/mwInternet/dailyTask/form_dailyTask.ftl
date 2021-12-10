<!DOCTYPE html>
<html>
<head>
	<title>保存</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/scripts/updown/swfupload/css/swfupload.css" />
	<script type="text/javascript" src="${rc.getContextPath()}/scripts/updown/swfupload/swfupload.js"></script>
	<script type="text/javascript" src="${rc.getContextPath()}/scripts/updown/swfupload/handlers.js"></script>
	<style type="text/css">
		.inp1 {width:220px;}
	</style>
</head>
<body>
	<form id="submitForm">
		<input type="hidden" id="buttonType" name="buttonType" value="" />
		<input type="hidden" id="ddtId" name="ddtId" value="${(bo.ddtId)!}" />
		<div id="content-d" class="MC_con content light">
			<div name="tab" class="NorForm">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<input type="hidden" id="infoOrgCode" name="infoOrgCode" value="<#if bo.infoOrgCode??>${bo.infoOrgCode}</#if>">
						<input type="hidden" id="infoOrgName" name="infoOrgName" value="<#if bo.infoOrgName??>${bo.infoOrgName}</#if>">
					<#--<tr>
                       <td colspan="2" class="LeftTd"><label class="LabName"><span>所属小区：</span></label>
				 		<input type="hidden" id="infoOrgCode" name="infoOrgCode" value="<#if bo.infoOrgCode??>${bo.infoOrgCode}</#if>">
                        <input class="inp1 inp2 InpDisable easyui-validatebox" type="text" id="infoOrgName" data-options="required:true" name="infoOrgName" style="width: 150px;" value="<#if bo.infoOrgName??>${bo.infoOrgName}</#if>" />
                       </td>
                    </tr>-->
					<tr>
						<td>
							<label class="LabName"><span>任务标题：</span></label>
							<input name="taskTitle" id="taskTitle" maxLength="100" type="text" class="inp1 easyui-validatebox" style="width: 85%;" data-options="tipPosition:'bottom',required:true,validType:['maxLength[100]','characterCheck']" value="<#if bo.taskTitle??>${bo.taskTitle}</#if>"/>
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>任务描述：</span></label>
							<textarea name="taskDesc" id="taskDesc" cols="45" rows="5" style="width: 85%;height: 70px;" class="area1 easyui-validatebox" maxLength="250" data-options="tipPosition:'bottom',required:true,validType:['maxLength[250]','characterCheck']"><#if bo.taskDesc??>${bo.taskDesc}</#if></textarea>
						</td>
					</tr>
					<tr>
                    	<td colspan="2" class="LeftTd RightTd"><label class="LabName"><span>附件：</span></label>
                    	<div id="fileupload" class="ImgUpLoad" style="padding-top:4px;"></div></td>
            		</tr>
				</table>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
	                    <td class="LeftTd" colspan="2"><div class="title FontDarkBlue">派发信息</div></td>
	                </tr>
					<tr>
		                <td colspan="5">
							<label class="LabName"><span>选择办理人：</span></label>
							<div class="Check_Radio">
						    	<a href="#" class="NorToolBtn EditBtn fl" id="userSelectBtn" onclick="selectUser('userIds', 'userNames', 'curOrgIds', 'curOrgNames', '${flag}');">选择办理人</a>
							</div>
							<input type="hidden" id="userIds" name="userIds" value="${userIds}"/>
							<input type="hidden" id="curOrgIds" name="curOrgIds" value="${curOrgIds}"/>
							<input type="hidden" id="curOrgNames" name="curOrgNames" value="${curOrgNames}"/>
							<div class="FontDarkBlue fl DealMan Check_Radio"><b name="userNames" id="userNames">${userNames}</b></div>
						</td>
		    	    </tr>
		    	    <tr>
						<td colspan="5">
							<label class="LabName"><span>处理时限：</span></label>
							<input type="text" class="inp1 Wdate easyui-validatebox timeCell" style="cursor: pointer;width:150px;" data-options="tipPosition:'bottom'" onclick="WdatePicker({startDate:'', dateFmt:'yyyy-MM-dd', readOnly:true, alwaysUseStartDate:true, isShowClear:false})" name="overTime" id="overTime" value="" />
						</td>
					</tr>
		    	    <tr>
						<td colspan="5">
							<label class="LabName"><span>受理意见：</span></label>
							<textarea name="overview" id="overview" cols="" rows="" class="area1 easyui-validatebox" data-options="validType:['maxLength[1000]','characterCheck']"  style="width:650px;float: left;margin-bottom:4px;margin-top:4px;"></textarea>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<div class="BigTool">
	    	<div class="BtnList">
	    		<a href="javascript:;" class="BigNorToolBtn SaveBtn" onclick="save('1');">保存</a>
	    		<a href="javascript:;" class="BigNorToolBtn SaveBtn" onClick="save('2');">提交</a>
	    		<a href="javascript:;" class="BigNorToolBtn CancelBtn" onClick="cancel();">取消</a>
	        </div>
	    </div>
	</form>
	<#include "/zzgl/mwInternet/warnTask/select_user.ftl" />
</body>
<script type="text/javascript">
	//保存
	function save(e) {
		$('#buttonType').val(e);
		var isValid = $('#submitForm').form('validate');
		if (isValid) {
			if("2"==e){
				if($("#curOrgIds").val()==null||$("#curOrgIds").val()==''
		    		||$("#curOrgIds").val()==null||$("#curOrgIds").val()==''){
		    		 parent.$.messager.alert('提示', '请选择办理人', 'info', function () {
		    		 		flag = false;
			                return;
			         });
			         return;
				}
				var overTime = $("#overTime").val();
				if(overTime==null||overTime==""){
			 		parent.$.messager.alert('提示', '请填写处理时限', 'info');
			        return;
				}
			}
		
			modleopen(); //打开遮罩层
			$.ajax({
				type: 'POST',
				url: '${rc.getContextPath()}/zhsq/dailyTask/save.json',
				data: $('#submitForm').serializeArray(),
				dataType: 'json',
				success: function(data) {
					if (data.result == 'fail') {
						if(e=='1'){
							$.messager.alert('错误', '保存失败！', 'error');
						}else{
							$.messager.alert('错误', '提交失败！', 'error');
						}
					} else {
						parent.searchData();
						if(e=='1'){
							$.messager.alert('提示', '保存成功！', 'info', function() {
								parent.closeMaxJqueryWindow();
							});
						}else{
							$.messager.alert('提示', '提交成功！', 'info', function() {
								parent.closeMaxJqueryWindow();
							});
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
	AnoleApi.initGridZtreeComboBox("infoOrgName", "infoOrgCode",function (infoOrgCode, items){
            if(items!=undefined && items!=null && items.length>0){
                var grid = items[0];
                $("#infoOrgCode").val(grid.orgCode);
            }
        });
	
	<#if bo.ddtId??>
			fileUpload({
				positionId:'fileupload',//附件列表DIV的id值',
				type:'edit',//add edit detail
				initType:'ajax',//ajax、hidden编辑表单时获取已上传附件列表方式
				context_path:'${rc.getContextPath()}',
				ajaxData: {'bizId':${bo.ddtId?c},'attachmentType':'${bizType}'},
				ajaxUrl:'${rc.getContextPath()}/zhsq/att/getList.jhtml' 	//获取上传附件的URL （新增页面可不写）	
			});
			
		<#else>
			fileUpload({ 
				positionId:'fileupload',//附件列表DIV的id值',
				type:'add',//add edit detail
				initType:'ajax',//ajax、hidden编辑表单时获取已上传附件列表方式
				context_path:'${rc.getContextPath()}',
				ajaxData: {'eventSeq':1}//未处理
			});
		</#if>
	//取消
	function cancel() {
		parent.closeMaxJqueryWindow();
	}
</script>
</html>

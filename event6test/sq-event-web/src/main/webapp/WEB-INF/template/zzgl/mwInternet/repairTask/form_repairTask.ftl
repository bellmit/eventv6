<!DOCTYPE html>
<html>
<head>
	<title>保存</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
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
		<input type="hidden" id="drtId" name="drtId" value="${(bo.drtId)!}" />
		<div id="content-d" class="MC_con content light">
			<div name="tab" class="NorForm">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<input type="hidden" id="infoOrgCode" name="infoOrgCode" value="<#if bo.infoOrgCode??>${bo.infoOrgCode}</#if>">
						<input type="hidden" id="infoOrgName" name="infoOrgName" value="<#if bo.infoOrgName??>${bo.infoOrgName}</#if>">
					<tr>
                       <#--<td>
                       	<label class="LabName"><span>所属小区：</span></label>
				 		<input type="hidden" id="infoOrgCode" name="infoOrgCode" value="<#if bo.infoOrgCode??>${bo.infoOrgCode}</#if>">
                        <input class="inp1 inp2 InpDisable easyui-validatebox" type="text" id="infoOrgName" data-options="required:true" name="infoOrgName" value="<#if bo.infoOrgName??>${bo.infoOrgName}</#if>" />
                       </td>-->
                        <td>
							<label class="LabName"><span>故障类型：</span></label>
							<input id="faultType" name="faultType" type="text" value="<#if bo.faultType??>${bo.faultType}</#if>" class="queryParam hide"/>
	                		<input id="faultTypeName" name="faultTypeName" value="<#if bo.faultTypeStr??>${bo.faultTypeStr}</#if>" type="text" class="inp1 InpDisable w150 easyui-validatebox" data-options="required:true"/>
						</td>
						<td>
							<label class="LabName"><span>预约时间：</span></label>
							<input type="text" id="orderTime" name="orderTime" value="${bo.orderTimeStr}" data-options="tipPosition:'bottom',required:true" class="inp1 fl Wdate easyui-validatebox"  onclick="WdatePicker({el:'orderTime',dateFmt:'yyyy-MM-dd'});" readonly />
						</td>
                    </tr>
                    <tr>
						<td>
							<label class="LabName"><span>联系人：</span></label>
							<input type="text" id="linkUser" name="linkUser" value="${(bo.linkUser)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[24]', tipPosition:'bottom',required:true" />
						</td>
						<td>
							<label class="LabName"><span>电话：</span></label>
							<input type="text" id="linkTel" name="linkTel" value="${(bo.linkTel)!}" class="inp1 easyui-validatebox" data-options="tipPosition:'bottom',validType:'maxLength[15]',validType:'mobileorphone',required:true"  />
						</td>
					</tr>
					<tr>
						<td colspan="2" class="LeftTd RightTd">
							<label class="LabName"><span>故障地址：</span></label>
							<input name="faultAddr" id="faultAddr" maxLength="100" type="text" class="inp1 easyui-validatebox" style="width: 615px;" maxLength="100" data-options="tipPosition:'bottom',required:true,validType:['maxLength[100]','characterCheck']" value="<#if bo.faultAddr??>${bo.faultAddr}</#if>"/>
						</td>
					</tr>
					<tr>
						<td colspan="2" class="LeftTd RightTd">
							<label class="LabName"><span>故障描述：</span></label>
							<textarea name="faultDesc" id="faultDesc" cols="45" rows="5" style="width: 615px;height: 60px;" class="area1 easyui-validatebox" maxLength="250" data-options="tipPosition:'bottom',required:true,validType:['maxLength[250]','characterCheck']"><#if bo.faultDesc??>${bo.faultDesc}</#if></textarea>
						</td>
					</tr>
					<tr>
                    	<td colspan="2" class="LeftTd RightTd"><label class="LabName"><span>附件：</span></label>
                    	<div id="fileupload" class="ImgUpLoad" style="padding-top:4px;"></div></td>
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
	<#include "/component/ComboBox.ftl" />
</body>
<script type="text/javascript">
	$(function() {
		/*AnoleApi.initGridZtreeComboBox("infoOrgName", "infoOrgCode",function (infoOrgCode, items){
            if(items!=undefined && items!=null && items.length>0){
                var grid = items[0];
                $("#infoOrgCode").val(grid.orgCode);
            }
        });*/
		//加载数据字典：案件类型
		AnoleApi.initListComboBox("faultTypeName", "faultType", "${faultType}", null, null, {
			ShowOptions : {
				EnableToolbar : true
			}
		});
	});
	//保存
	function save(e) {
		$('#buttonType').val(e);
		var isValid = $('#submitForm').form('validate');
		
		if (isValid) {
			modleopen(); //打开遮罩层
			$.ajax({
				type: 'POST',
				url: '${rc.getContextPath()}/zhsq/repairTask/save.json',
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
	
	<#if bo.drtId??>
			fileUpload({
				positionId:'fileupload',//附件列表DIV的id值',
				type:'edit',//add edit detail
				initType:'ajax',//ajax、hidden编辑表单时获取已上传附件列表方式
				context_path:'${rc.getContextPath()}',
				ajaxData: {'bizId':${bo.drtId?c},'attachmentType':'${bizType}'},
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

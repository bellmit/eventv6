<!DOCTYPE html>
<html>
<head>
	<title>保存</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<style type="text/css">
		.inp1 {width:220px;}
	</style>
</head>
<body>
	<form id="submitForm">
		<input type="hidden" id="caseNo" name="caseNo" value="<#if bo.caseNo??>${bo.caseNo}</#if>" />
		<div id="content-d" class="MC_con content light">
			<div name="tab" class="NorForm">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
				
					<tr>
						<td class="LeftTd">
							<label class="LabName"><span>服务对象：</span></label>
							<input type="text" id="contactUser" name="contactUser" style="width: 155px;" value="<#if bo.contactUser??>${bo.contactUser}</#if>"
							 class="inp1 easyui-validatebox" data-options="required:true,validType:'maxLength[20]', tipPosition:'bottom'"  />
						</td>
						<td class="LeftTd">
							<label class="LabName"><span>来电号码：</span></label>
							<input type="text" id="tel" name="tel" style="width: 155px;" value="<#if bo.tel??>${bo.tel}</#if>" style="width: 155px;" 
							class="inp1 easyui-validatebox" data-options="required:true,validType:'maxLength[15]', tipPosition:'bottom'"  />
						</td>
						<td class="LeftTd">
							<label class="LabName"><span>性别：</span></label>
								<select id="sex" name="sex" class="sel1" style="width: 42px;">
			                        <option value="M" selected="selected">男</option>
                                	<option value="F" > 女</option>
			                  	</select>
						</td>
					</tr>
					<tr>
						<td class="LeftTd">
							<label class="LabName"><span>年龄范围：</span></label>
							<input type="hidden" id="age" name="age" value="<#if bo.age??>${bo.age}</#if>" />
							<input type="text" id="ageName" name="ageName" value="<#if bo.ageName??>${bo.ageName}</#if>" style="width: 155px;" 
							class="inp1 easyui-validatebox" data-options="required:true, tipPosition:'bottom'"  />
						</td>
						<td class="LeftTd">
							<label class="LabName"><span>来源渠道：</span></label>
							<input type="hidden" id="sources" name="sources" value="<#if bo.sources??>${bo.sources}</#if>" />
							<input type="text" id="sourcesName" name="sourcesName" value="<#if bo.sourcesName??>${bo.sourcesName}</#if>" style="width: 155px;" 
							class="inp1 easyui-validatebox" data-options="required:true,validType:'maxLength[10]', tipPosition:'bottom'"  />
						</td>
						<td class="LeftTd">
							<label class="LabName"><span>诉求类型：</span></label>
							<input type="hidden" id="appealType" name="appealType" value="<#if bo.appealType??>${bo.appealType}</#if>" />
							<input type="text" id="appealTypeName" name="appealTypeName" value="<#if bo.appealTypeName??>${bo.appealTypeName}</#if>" style="width: 155px;" 
							class="inp1 easyui-validatebox" data-options="required:true,validType:'maxLength[5]', tipPosition:'bottom'"  />
						</td>
					</tr>
					<tr>
						<td class="LeftTd">
							<label class="LabName"><span>来电时间：</span></label>
							<input type="text" id="callTime" name="callTime" value="<#if bo.callTime??>${bo.callTime}</#if>" style="width: 155px;" 
							class="inp1 Wdate" data-options="required:true,tipPosition:'bottom'" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});" readonly />
						</td>
						<td class="LeftTd">
							<label class="LabName"><span>事件发生地址：</span></label>
							<input type="text" id="occurred" name="occurred" value="<#if bo.occurred??>${bo.occurred}</#if>" style="width: 155px;" 
							 class="inp1 easyui-validatebox" data-options="required:true,validType:'maxLength[50]', tipPosition:'bottom'"  />
						</td>
						<td class="LeftTd">
							<label class="LabName"><span>电子邮箱：</span></label>
							<input type="text" id="email" name="email" value="<#if bo.email??>${bo.email}</#if>" style="width: 155px;" 
							class="inp1 easyui-validatebox" data-options="required:true,validType:'maxLength[50]', tipPosition:'bottom'"  />
						</td>
					</tr>
					
					<tr>
						<td class="LeftTd">
							<label class="LabName"><span>网站公示：</span></label>
							<input type="text" id="isWeb" name="isWeb" value="<#if bo.isWeb??>${bo.isWeb}</#if>" style="width: 155px;" 
							class="inp1 easyui-validatebox" data-options="required:true,validType:'maxLength[1]', tipPosition:'bottom'"  />
						</td>
						<td class="LeftTd">
							<label class="LabName"><span>归口类型：</span></label>
							<input type="hidden" id="underType" name="underType" value="<#if bo.underType??>${bo.underType}</#if>" />
							<input type="text" id="eventTypeName" name="eventTypeName" value="<#if bo.eventTypeName??>${bo.eventTypeName}</#if>"  style="width: 155px;" 
							class="inp1 easyui-validatebox" data-options="required:true,validType:'maxLength[50]', tipPosition:'bottom'"  />
						</td>
						<td class="LeftTd">
							<label class="LabName"><span>联系电话：</span></label>
							<input type="text" id="contactTel" name="contactTel" value="<#if bo.contactTel??>${bo.contactTel}</#if>" style="width: 155px;" 
							class="inp1 easyui-validatebox" data-options="required:true,validType:'maxLength[15]', tipPosition:'bottom'"  />
						</td>
					</tr>
					<tr>
						<td class="LeftTd">
							<label class="LabName"><span>紧急事项：</span></label>
							<input type="text" id="urgency" name="urgency" value="<#if bo.urgency??>${bo.urgency}</#if>" style="width: 155px;" 
							class="inp1 easyui-validatebox" data-options="required:true,validType:'maxLength[1]', tipPosition:'bottom'"  />
						</td>
						<td class="LeftTd">
							<label class="LabName"><span>诉求保密：</span></label>
							<input type="text" id="appealPublic" name="appealPublic" value="<#if bo.appealPublic??>${bo.appealPublic}</#if>" style="width: 155px;" 
							 class="inp1 easyui-validatebox" data-options="required:true,validType:'maxLength[1]', tipPosition:'bottom'"  />
						</td>
						<td class="LeftTd">
							<label class="LabName"><span>回访类型：</span></label>
							<input type="text" id="returnType" name="returnType" value="<#if bo.returnType??>${bo.returnType}</#if>" style="width: 155px;" 
							class="inp1 easyui-validatebox" data-options="required:true,validType:'maxLength[1]', tipPosition:'bottom'"  />
						</td>
					</tr>
					<tr>
						<td colspan="2" class="LeftTd">
							<label class="LabName"><span>诉求内容：</span></label>
							<textarea name="content" id="content" cols="" rows=""  class="area1 easyui-validatebox" 
							style="width:470px; height:64px;resize: none;" data-options="required:true,tipPosition:'bottom',validType:['maxLength[800]','characterCheck']" ></textarea>
						</td>
						
						<td class="LeftTd">
							<label class="LabName"><span>标题：</span></label>
							<textarea name="title" id="title" cols="" rows="" class="area1 easyui-validatebox" 
							style="width:155px;; height:64px;resize: none;" data-options="required:true,tipPosition:'bottom',validType:['maxLength[100]','characterCheck']" ></textarea>
						</td>
						
					</tr>
					<tr>
						<td colspan="2" class="LeftTd">
							<label class="LabName"><span>具体地址：</span></label>
							<textarea name="address" id="address" cols="" rows="" class="area1 easyui-validatebox" 
							style="width:470px;; height:64px;resize: none;" data-options="required:true,tipPosition:'bottom',validType:['maxLength[100]','characterCheck']" ></textarea>
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
    	AnoleApi.initListComboBox("sourcesName", "sources", "B0909002", null, [<#if bo.sources??>${bo.sources!''}<#else>""</#if>]);
        AnoleApi.initListComboBox("ageName", "age", "B0909003", null, [<#if bo.age??>${bo.age!''}<#else>""</#if>]);
    	AnoleApi.initListComboBox("appealTypeName", "appealType", "B0909004", null, [<#if bo.appealType??>${bo.appealType!''}<#else>""</#if>]);
    	AnoleApi.initListComboBox("eventTypeName", "underType", "A001093199789", null, [<#if bo.underType??>${bo.underType!''}<#else>""</#if>]);
        
    });



	//保存
	function save() {
		var isValid = $('#submitForm').form('validate');
		if (isValid) {
			modleopen(); //打开遮罩层
			$.ajax({
				type: 'POST',
				url: '${rc.getContextPath()}/zhsq/hotLine/save.json',
				data: $('#submitForm').serializeArray(),
				dataType: 'json',
				success: function(data) {
					if (data.result == 'fail') {
						$.messager.alert('错误', '保存失败！', 'error');
					} else {
						$.messager.alert('提示', '保存成功！', 'info', function() {
							parent.closeMaxJqueryWindow();
						});
						parent.searchData();
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

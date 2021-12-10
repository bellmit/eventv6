<!DOCTYPE html>
<html>
<head>
	<title>新增/编辑</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<style type="text/css">
		.inp1 {width:220px;}
		.Asterik{color: red;}
	</style>
</head>
<body>
	<form id="handleForm">
		<input type="hidden" id="synergismId" name="synergismId" value="${(bo.synergismId)!}" />
		<input type="hidden" id="nextNodeId" name="nextNodeId" value="${(nextNode.nodeName)!}" />
		<input type="hidden" id="nowNodeId" name="nowNodeId" value="${(nowNode.NODE_NAME)!}" />
		<input type="hidden" id="creator" name="creator" value="${(bo.creator)!}" />
		<input type="hidden" id="gridCode" name="gridCode" value="${(bo.gridCode)!}" />
		<div id="content-d" class="MC_con content light">
			<div name="tab" class="NorForm" style="border:1px solid #c5d0dc;margin-top:10px;">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td>
							<label class="LabName"><span><label class="Asterik">*</label>申请类别：</span></label>
							<span class="Check_Radio FontDarkBlue">${(bo.applyTypeCN)!}</span>
						</td>
						<td>
							<label class="LabName"><span><label class="Asterik">*</label>法官姓名：</span></label>
							<span class="Check_Radio FontDarkBlue">${(bo.courtName)!}</span>
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span><label class="Asterik">*</label>联系方式：</span></label>
							<span class="Check_Radio FontDarkBlue">${(bo.contactInformation)!}</span>
						</td>
						<td>
							<label class="LabName"><span><label class="Asterik">*</label>所属部门：</span></label>
							<span class="Check_Radio FontDarkBlue">${(bo.department)!}</span>
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span><label class="Asterik">*</label>申请时间：</span></label>
							<span class="Check_Radio FontDarkBlue">${(bo.applyDateStr)!}</span>
						</td>
						<td>
							<label class="LabName"><span>所属网格：</span></label>
							<span class="Check_Radio FontDarkBlue">${(bo.gridName)!}</span>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<label class="LabName"><span>事项说明：</span></label>
							<span class="Check_Radio FontDarkBlue" style="width:80%;">${(bo.itemDescription)!}</span>
						</td>
					</tr>
				</table>
			</div>
			<div name="tab" id="div0" class="NorForm" style="border:1px solid #c5d0dc;margin-top:10px;">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td>
							<label class="LabName"><span>当前环节：</span></label>
							<span class="Check_Radio FontDarkBlue span-text">
							<#if nowNode.WF_ACTIVITY_NAME_=="">-<#else>${(nowNode.WF_ACTIVITY_NAME_)!"-"}</#if>
							</span>
						</td>
						<td>
							<label class="LabName"><span>当前状态：</span></label>
							<span class="Check_Radio FontDarkBlue span-text">
							<#if bo.statusCN=="">-<#else>${(bo.statusCN)!"-"}</#if>
							</span>
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>当前办理人：</span></label>
							<span class="Check_Radio FontDarkBlue span-text">
							<#if bo.curUserName=="">-<#else>${(bo.curUserName)!"-"}</#if>
							</span>
						</td>
						<td>
							<label class="LabName"><span>当前办理组织：</span></label>
							<span class="Check_Radio FontDarkBlue span-text">
							<#if nowNode.WF_ORGNAME=="">-<#else>${(nowNode.WF_ORGNAME)!"-"}</#if>
							</span>
						</td>
					</tr>

					<tr>
						<td colspan="2">
							<label class="LabName"><span>下一环节：</span></label>
							<span class="Check_Radio FontDarkBlue span-text">
								<#if nextNode.nodeNameZH=="">-<#else>${(nextNode.nodeNameZH)!"-"}</#if>
							</span>
						</td>
					</tr>
					<!-- 办理意见 -->
					<tr class="org-tr">
						<td colspan="2">
							<label class="LabName"><span>办理意见：</span></label>
							<textarea style="height:150px;width:82%;" name="advice" id="advice" value="" class="inp1 InpDisable easyui-validatebox" data-options="required:false,validType:'maxLength[500]'" ></textarea>
						</td>
					</tr>
                    <!-- 网格员评价 -->
					<#if nowNode.NODE_NAME =="task5">
						<tr class="org-tr">
							<td colspan="2">
								<label class="LabName"><span><label class="Asterik">*</label>评价：</span></label>
                                <input type="hidden" name="satisfaction" id="satisfaction"/>
                                <input type="text" style="width: 100px" id="satisfactionCN" name="satisfactionCN" class="inp1 easyui-validatebox" data-options="required:true" readonly/>
                            </td>
						</tr>
                        <script>
                            AnoleApi.initListComboBox("satisfactionCN", "satisfaction", "B12322003",null);
                        </script>
					</#if>
				</table>
			</div>

		</div>
		<div class="BigTool">
			<div class="BtnList">
				<#if nowNode.NODE_NAME !="task1">
					<a href="javascript:;" class="BigNorToolBtn RejectBtn" onClick="reject();">驳回</a>
				</#if>
				<a href="javascript:;" class="BigNorToolBtn BigShangBaoBtn" onClick="submit();">提交</a>
				<a href="javascript:;" class="BigNorToolBtn CancelBtn" onClick="cancel();">取消</a>
			</div>
		</div>
	</form>
</body>
<script type="text/javascript">
	$(function () {
	})

	//驳回
	function reject(){
		//办理意见判空
		var advice=$("#advice").val();
		if(isBlankString(advice)){
			$.messager.alert('提示', "办理意见不能为空，请输入办理意见！", 'info');
			return;
		}

		let url = '${rc.getContextPath()}/zhsq/courtSynergism/reject.json';
		var subMig = '驳回成功！';
		modleopen(); //打开遮罩层
		$.ajax({
			type: 'POST',
			url: url,
			data: $('#handleForm').serializeArray(),
			dataType: 'json',
			success: function(data) {
				if (data.success) {
					$.messager.alert('提示', subMig, 'info', function() {
						parent.closeMaxJqueryWindow();
					});
					parent.searchData();
				} else {
					let type = data.type;
					let message = "驳回失败！"
					switch (type) {
						case '0':  message = "驳回失败！"
							break;
						case '-1':  message = "驳回异常！"
							break;
					}
					$.messager.alert('错误', message, 'error');
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

	//保存
	function submit() {
		var isValid = $('#handleForm').form('validate');
		if (isValid) {
			let url = '${rc.getContextPath()}/zhsq/courtSynergism/submit.json';
			var subMig = '保存成功！';
			modleopen(); //打开遮罩层
			$.ajax({
				type: 'POST',
				url: url,
				data: $('#handleForm').serializeArray(),
				dataType: 'json',
				success: function(data) {
					if (data.success) {
                        $.messager.alert('提示', subMig, 'info', function() {
                            parent.closeMaxJqueryWindow();
                        });
                        parent.searchData();
					} else {
					    let type = data.type;
					    let message = "保存失败！"
                        switch (type) {
                            case '0':  message = "提交失败！"
                                break;
                            case '-2':  message = "获取网格异常！"
                                break;
                            case '-1':  message = "未获取到下一环节办理人员！"
                                break;
                            case '-3':  message = "提交异常！"
                                break;
                        }
                        $.messager.alert('错误', message, 'error');
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

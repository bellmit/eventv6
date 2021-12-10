<!DOCTYPE html>
<html>
<head>
	<title>详情</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
</head>
<body>
	<div id="content-d" class="MC_con content light">
		<div name="tab" id="div0" class="NorForm">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>
						<label class="LabName"><span>联系人：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.contactUser)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>电话：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.tel)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>性别：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.sex)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>年龄范围：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.age)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>来源渠道：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.sources)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>诉求类型：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.appealType)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>来电时间：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.callTime)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>标题：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.title)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>诉求内容：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.content)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>电子邮箱：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.email)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>网站公示：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.isWeb)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>事件发生地址：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.occurred)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>具体地址：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.address)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>归口类型：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.underType)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>紧急事项：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.urgency)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>诉求保密：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.appealPublic)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>回访类型：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.returnType)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>状态：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.isValid)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>创建时间：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.createTime)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>创建人：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.creatorId)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>修改时间：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.updateTime)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>修改人：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.updateorId)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>事件状态：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.eventStatus)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>事件Id：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.eventId)!}</span>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<div class="BigTool">
    	<div class="BtnList">
    		<a href="javascript:;" class="BigNorToolBtn CancelBtn" onClick="cancel();">关闭</a>
        </div>
    </div>
</body>
<script type="text/javascript">
	//关闭
	function cancel() {
		parent.closeMaxJqueryWindow();
	}
</script>
</html>

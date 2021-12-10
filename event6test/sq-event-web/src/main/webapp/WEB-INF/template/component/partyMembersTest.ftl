<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>partyMembers Test</title>
	<#include "/component/commonFiles-1.1.ftl" />
	<script type="text/javascript" src="${COMPONENTS_URL}/js/rs/jquery.baseCombo.js"></script>
	<script type="text/javascript" src="${rc.getContextPath()}/js/components/partyMember/partyMembersSelector.js"></script>
</HEAD>

<BODY>
<div class="content_wrap">
	<input name="partyMemberName" id="partyMemberName" type="text" class="inp1 easyui-validatebox" value="" />
	<p>
		姓名<input name="partyName" id="partyName" type="text" class="inp1 easyui-validatebox" value=""/>
	</p>
	<p>

		性别<input name="genderCN" id="genderCN" type="text" class="inp1 easyui-validatebox" value=""/>
	</p>
	<p>

		身份证号<input name="identityCard" id="identityCard" type="text" class="inp1 easyui-validatebox" value=""/>
	</p>
	<p>

		家庭关系<input name="householderRelationCN" id="householderRelationCN" type="text" class="inp1 easyui-validatebox" value=""/>
	</p>
</div>

<script type="text/javascript">

	$(function () {
		var singleChoose = $('#partyMemberName').partyMembersSelector({
			height : 30,
			width : 250,
			panelHeight:300,
			panelWidth:454,
			editable:true,
			dataDomain : "${rc.getContextPath()}",
			onClickRow:function (index,row) {
				$('#partyName').val(row.partyName);
				$('#genderCN').val(row.genderCN);
				$('#identityCard').val(row.identityCard);
				$('#householderRelationCN').val(row.householderRelationCN);
			}
		},{partyId:${partyId}});
	});

</script>
</BODY>
</HTML>
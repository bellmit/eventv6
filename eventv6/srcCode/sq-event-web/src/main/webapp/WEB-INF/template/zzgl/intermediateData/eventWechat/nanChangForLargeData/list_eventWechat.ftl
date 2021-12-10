<@override name="eventWechatListTitle">南昌大数据平台微信事件列表</@override>

<@override name="happenTimeColumnBlock">

{field:'happenTimeStr',title:'事发时间',align:'center',width:fixWidth(0.15),sortable:true,
	formatter:function(value, rowData, rowIndex){
		if(!(value != null && value.length > 0)){
			value = '';
		}
		return value;
	}
},

</@override>

<@override name="advancedQueryBlock">

<li style="position:relative;">
	<a href="javascript:void(0)" class="AdvanceSearchBtn FontDarkBlue">高级查询</a>
	<div class="AdvanceSearch DropDownList hide" style="width:500px; top: 42px; left: -130px;">
		<div class="LeftShadow">
			<div class="RightShadow">
				<div class="list NorForm">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td>
								<label class="LabName width65px"><span>事发时间：</span></label><input class="inp1 Wdate fl queryParam" type="text" id="happenTimeStart" name="happenTimeStart" value="${happenTimeStart!''}" style="width:150px; *width:100px; cursor:pointer;" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly"></input><span class="Check_Radio" style="padding:0 5px;">至</span><input class="inp1 Wdate fl queryParam" type="text" id="happenTimeEnd" name="happenTimeEndDetail" value="${happenTimeEnd!''}" style="width:150px; *width:100px; cursor:pointer;" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly"></input>
							</td>
						</tr>
						<tr id='bizPlatformTr'>
							<td>
							    <label class="LabName width65px"><span>信息来源：</span></label>
							    <input class="hide queryParam" id="bizPlatform" name="bizPlatformForSearch" value="${bizPlatform!}" />
	                            <input type="text" class="inp1 w150" id="bizPlatformNameStr" />
							</td>
						</tr>
					</table>
				</div>
			</div>
		</div>
	</div>
</li>

</@override>

<@override name="editFunctionBlock">

function edit() {
	var eventVerifyHashId = "";
	$("input[name='eventVerifyHashId']:checked").each(function() {
		eventVerifyHashId = $(this).val();
	});
	if(eventVerifyHashId == "") {
		$.messager.alert('警告','请选中要审核的数据再执行此操作!','warning');
	} else {
		var url = '${rc.getContextPath()}/zhsq/eventWechat/toEdit.jhtml?eventVerifyHashId=' + eventVerifyHashId,
			verifyType = $('#verifyType').val();
		
		if(isNotBlankStringTrim(verifyType)) {
			url += '&verifyType=' + verifyType;
		}
		
		showMaxJqueryWindow("审核事件信息",url,720,410,true);
		
	}
}

</@override>
<@override name="detailFunctionBlock">

function detail(eventVerifyHashId) {
	var url = '${rc.getContextPath()}/zhsq/eventWechat/toDetail.jhtml?eventVerifyHashId=' + eventVerifyHashId + '&verifyType',
		verifyType = $('#verifyType').val();
	
	if(isNotBlankStringTrim(verifyType)) {
		url += '&verifyType=' + verifyType;
	}
	
	showMaxJqueryWindow("查看事件信息",url,720,410,true);
	
}

</@override>


<@extends name="/zzgl/intermediateData/eventWechat/list_eventWechat.ftl" />
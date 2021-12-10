<#if emtType??>
	<@override name="gridPathField">
		<@super></@super>
		{field:'emtTypeName',title:'八员单位', align:'center',width:fixWidth(0.08), formatter: titleFormatter},
	</@override>

	<@override name="curOrgIdsTr"></@override>
	
	<@override name="extendCondition">
		<tr>
			<td>
				<label class="LabName width65px"><span>八员单位：</span></label>
				<input type="text" id="emtType" name="emtType" class="hide queryParam" value="${emtType!}" defaultValue="${emtType!}"/>
				<input type="text" id="emtTypeName" class="inp1 selectWidth" />
			</td>
		</tr>
	</@override>
	
	<@override name="extendConditionInit">
		AnoleApi.initTreeComboBox("emtTypeName", "emtType", "B218", null, null, {
			OnCleared: function() {
				$('#emtType').val($('#emtType').attr('defaultValue'));
			},
			ShowOptions: {
				EnableToolbar : true
			},
			FilterData: function (data) {
				var dataRemain = [],
					emtType = $('#emtType').val(),
					dictValue = null,
					bitValue = null;
				
				if(emtType) {
					if(data && data.length > 0) {
						emtType = parseInt(emtType, 10);
						
						for(var index = 0, len = data.length; index < len; index++) {
							dictValue = parseInt(data[index].value, 10);
							bitValue = dictValue & emtType;
							
							if(bitValue === dictValue) {
								dataRemain.push($.extend({}, data[index]));
							}
						}
					}
				} else {
					dataRemain = data;
				}
				
				return dataRemain;
			}
		});
	</@override>
</#if>

<@extends  name="/zzgl/event/jiangXiProvincePlatform/eventOrg/list_event_org.ftl" />
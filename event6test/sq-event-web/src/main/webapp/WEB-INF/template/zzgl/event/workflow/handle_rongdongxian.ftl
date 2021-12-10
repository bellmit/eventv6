<@override name="radioCheckSuccessAfter">
	var scoreTr = $("#_handleEventTable tr._nps_score_tr");
	
	if(data.isShowPointConfirm && data.isShowPointConfirm == true) {
		if(scoreTr.length == 0) {
			var scoreTr = 
			'<tr class="_nps_score_tr">' +
				'<td>' +
					'<label class="LabName"><span><label class="Asterik">*</label>给予积分：</span></label>' +
					'	<div class="Check_Radio" style="margin-top:7px;">' + 
					'		<input type="radio" name="isBonusPoint" id="isBonusPoint_true" onclick="bonusPointCheck(this);" value="true" checked /><label for="isBonusPoint_true" style="cursor: pointer;">是&nbsp;</label>' +
					'		<input type="radio" name="isBonusPoint" id="isBonusPoint_false" onclick="bonusPointCheck(this);" value="false" /><label for="isBonusPoint_false" style="cursor: pointer;">否&nbsp;</label> ' +
					'	<span>'+
					'		<b style="color: red;">事件归档后，积分将发给市民，请根据实际核实情况。</b>' +
					'	<span>' +
					'	</div>' +
				'</td>' +
			'</tr>';
			
			if(false) {
				scoreTr += 
				'<tr id="_nps_bonus_point_tr" class="_nps_score_tr">' +
					'<td>' +
						'<label class="LabName"><span><label class="Asterik">*</label>所得积分：</span></label>' +
						'	<div class="Check_Radio" style="margin-top:7px;">' + 
						'		<input type="radio" name="bonusPoint" id="bonusPoint_50" value="50" checked /><label for="bonusPoint_50" style="cursor: pointer;">50&nbsp;</label>' +
						'		<input type="radio" name="bonusPoint" id="bonusPoint_30" value="30" /><label for="bonusPoint_30" style="cursor: pointer;">30&nbsp;</label> ' +
						'		<input type="radio" name="bonusPoint" id="bonusPoint_10" value="10" /><label for="bonusPoint_10" style="cursor: pointer;">10&nbsp;</label> ' +
						'	</div>' +
					'</td>' +
				'</tr>';
			}
			
			$('#evaluate_content').before(
				scoreTr
			);
		}
	} else {
		scoreTr.remove();
	}
</@override>

<@override name="extraHandleFunction">
	function bonusPointCheck(radioObj) {
		var val = $(radioObj).val();
		
		if(val == 'true') {
			$('#_nps_bonus_point_tr input[type=radio]').eq(0).attr('checked', true);
			$('#_nps_bonus_point_tr').show();
		} else {
			$('#_nps_bonus_point_tr').hide();
			$('#_nps_bonus_point_tr input[type=radio]').eq(0).attr('checked', false);
		}
	}
</@override>

<@extends name="/zzgl/event/workflow/handle_event_node.ftl" />
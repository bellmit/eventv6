<@override name="extraJs4EventAttrEdit">
	<@super></@super>
	<#include "/component/bigFileUpload.ftl" />
</@override>
<@override name="extraTr4EventAttrEdit">
	<@super></@super>
	<tr>
		<td class="LeftTd">
			<label class="LabName"><span>图片上传：</span></label><div id="bigFileUploadDiv"></div>
		</td>
	</tr>
</@override>
<@override name="extraInit4EventAttrEdit">
	<@super></@super>
	var bigFileUploadOpt = {
		useType: 'edit',
		fileExt: '.jpg,.gif,.png,.jpeg,.webp,.doc,.docx',
		attachmentData: {bizId: $('#eventId').val(), attachmentType:'ZHSQ_EVENT', eventSeq:'1,2,3', isBindBizId:'yes'},
		module: 'event',
		individualOpt : {
			isUploadHandlingPic : true
		}
	};
	
	bigFileUpload_initFileUploadDiv('bigFileUploadDiv', bigFileUploadOpt);
</@override>
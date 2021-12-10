<link href="${uiDomain!''}/web-assets/extend/search-downpage/jquery.multiple.select-1.0.0/css/component-chosen.css" rel="stylesheet" />
<script type="text/javascript" src="${uiDomain!''}/web-assets/extend/search-downpage/jquery.multiple.select-1.0.0/js/jquery.multiple.select-1.0.0.js"></script>

<style>
input[type='radio']:checked + label .radio-input1 {
    border: 1px solid #6071f6;
}
input[type='radio']:checked + label .radio-input1 div {
    box-shadow: 0px 4px 7px #5294e8 inset;
    background-color: #5294e8;
}
.radio-input1 div {
    position: absolute;
    top: 1px;
    left: 1px;
    border-radius: 50%;
    width: 8px;
    height: 8px;
    background-color: #c6c3c3;
    box-shadow: 0px 4px 7px #c6c3c3 inset;
}
.radio-input1{
    position: relative;
    width: 12px;
    height: 12px;
    border: 1px solid #cdcbcb;
    border-radius: 50%;
    background-color: #fff;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    display:inline-block;
    margin-right: 10px;
    margin-top:-3px;
    vertical-align:middle;
    line-height:1;
    cursor: pointer;
}
.radio-box {
    font-size: 14px;
    line-height: 18px;
    color: #666666;
    cursor: pointer;
}
.mr {
    margin-right: 10px;
}
.det-radio>dd {
    float: left;
    display: flex;
    align-items: center;
    height: 30px;
}
.det-radio {
    float: left;
}
td{
	padding-left:0
}
.td-title{
	width:100px !important;
}
.det-textarea{
	width: calc(100% - 120px);
}
.det-textarea>textarea {
	width: 100%;
    height: 66px;
    resize: none;
    box-sizing: border-box;
    padding: 8px 12px;
    font-size: 14px;
    background-color: #fafafa;
    border: solid 1px #cccccc;
    color: #333;
}
.span-text{
	width: calc(100% - 105px);
}
</style>
	<!-- 办理环节 handle -->
	<form id="handleForm">
	<input type="hidden" id="sendId" name="sendId" value="${(bo.sendId)!}">
	<input type="hidden" id="sendType" name="sendType" value="">
	<div name="tab" id="div0" class="NorForm" style="border:1px solid #c5d0dc;margin-top:10px;">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
					<label class="LabName"><span>当前环节：</span></label>
					<span class="Check_Radio FontDarkBlue span-text">
					<#if bo.currentNode=="">-<#else>${(bo.currentNode)!"-"}</#if>
					</span>
				</td>
				<td>
					<label class="LabName"><span>当前状态：</span></label>
					<span class="Check_Radio FontDarkBlue span-text">
					<#if bo.statusStr=="">-<#else>${(bo.statusStr)!"-"}</#if>
					</span>
				</td>
			</tr>
			<tr>
				<td>
					<label class="LabName"><span>当前办理人：</span></label>
					<span class="Check_Radio FontDarkBlue span-text">
					<#if bo.currentHandler=="">-<#else>${(bo.currentHandler)!"-"}</#if>
					</span>
				</td>
				<td>
					<label class="LabName"><span>当前办理组织：</span></label>
					<span class="Check_Radio FontDarkBlue span-text">
					<#if bo.currentCodeCn=="">-<#else>${(bo.currentCodeCn)!"-"}</#if>
					</span>
				</td>
			</tr>
			
			<#if bo.receiveState=="1">
			<tr>
				<td colspan="2">
					<label class="LabName"><span><label class="Asterik">*</label>送达图片：</span></label>
					<div id="attatch_div_handle"></div>
				</td>
			</tr>
			</#if>
			
			<#if bo.receiveState=="2">
			<tr>
				<td colspan="2">
					<label class="LabName"><span><label class="Asterik">*</label>未送达原因：</span></label>
					<span class="Check_Radio FontDarkBlue span-text">${(bo.advice)!}</span>
				</td>
			</tr>
			</#if>
			
		</table>
	</div>
	</form>
<script>

$(function(){
	createUploadeElem();
});

//附件上传初始化
function createUploadeElem(){
	$("#attatch_div_handle").bigfileUpload({
		useType: 'view',//附件上传的使用类型，add,edit,view，（默认edit）;
		imgDomain : imgDomain,//图片服务器域名
		uiDomain : uiDomain,//公共样式域名
		skyDomain : skyDomain,//网盘挂载IP
		componentsDomain : componentsDomain,//公共组件域名
		fileDomain : fileDomain,//文件服务域名
		showTip : false,
		// fileExt : '.jpeg,.jpg,.png,.gif,.doc,.xls,',//允许上传的附件类型
		appcode:"zhsq_event",//文件所属的应用代码（默认值components）
		module:"gdPersionSendFlow",//文件所属的模块代码（默认值bigfile）
		attachmentData:{bizId:'${bo.sendId!}',attachmentType:'GD_PERSION_FLOW_TYPE_HANDLE'},
		uploadSuccessCallback : function(file,response){
			console.log(file.id);
			console.log(response.attachmentId);
		}
	});
}

</script>




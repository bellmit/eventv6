<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>矛盾纠纷-详情</title>
<#include "/component/commonFiles-1.1.ftl" />
<script type="text/javascript">
	<#if source??>
		document.domain = "${updomain!}";
	</#if>
</script>
</head>
<body>
<div>
<form id="tableForm" name="tableForm" action="${rc.getContextPath()}/zzgl/disputeMediation/save.jhtml"  method="post" enctype="multipart/form-data">
    <input type="hidden" id="mediationId" name="mediationId" value="<#if disputeMediation.mediationId??>${disputeMediation.mediationId}</#if>">
    <input type="hidden" id="gridId" name="gridId" value="<#if disputeMediation.gridId??>${disputeMediation.gridId}</#if>">
	<div id="content-d" class="MC_con content light" style="overflow-x:hidden;">
	                <div class="NorForm NorForm2 ThreeColumn">
                	<div class="title FontDarkBlue">纠纷基本信息</div>
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      
                    <tr>
                        <td colspan="2" class="LeftTd"><label class="LabName"><span>事件名称：</span></label>
                        	<div class="Check_Radio FontDarkBlue" style="width:460px;"><#if disputeMediation.disputeEventName??>${disputeMediation.disputeEventName}</#if></div>
            		</tr>
                    <tr>
                        <td class="LeftTd"><label class="LabName"><span>所属网格：</span></label>
                        	<div class="Check_Radio FontDarkBlue" style="width:160px;"><#if disputeMediation.gridName??>${disputeMediation.gridName}</#if></div>
                        </td>
                        <td><label class="LabName"><span>发生日期：</span></label>
                        	<div class="Check_Radio FontDarkBlue" style="width:160px;">
                        	<#if disputeMediation.happenTimeStr??>${disputeMediation.happenTimeStr}</#if>
                        	</div>
                        </td>
                    </tr>
            		<tr>

                        <td class="LeftTd"><label class="LabName"><span>受理日期：</span></label>
                            <div class="Check_Radio FontDarkBlue" style="width:187px;">
							<#if disputeMediation.acceptedDateStr??>${disputeMediation.acceptedDateStr}</#if>
                            </div>
                        </td>

                        <td class="LeftTd"><label class="LabName"><span>调解时限：</span></label>
                            <div class="Check_Radio FontDarkBlue" style="width:187px;">
							<#if disputeMediation.mediationDeadlineStr??>${disputeMediation.mediationDeadlineStr}</#if>
                            </div>
                        </td>

					</tr>
                      <tr>
                        <td colspan="2" class="LeftTd RightTd"><label class="LabName"><span>发生地点：</span></label>
                        	<div class="Check_Radio FontDarkBlue" style="width:460px;">
                        	<#if disputeMediation.happenAddr??>${disputeMediation.happenAddr}</#if>
                        	</div>
            		</tr>
                    <tr>
                        <td class="LeftTd"><label class="LabName"><span>事件类别：</span></label>
                        <div class="Check_Radio FontDarkBlue" style="width:160px;">
                		  <#if disputeType_9x??>
                  	 		 <#list disputeType_9x as l>
                    		  	<#if disputeMediation.disputeType2??>
									<#if (l.dictGeneralCode?string==disputeMediation.disputeType2?string)>${l.dictName}</#if>
								</#if>
                   			 </#list>
                 		 </#if>
                 		 </div>
                        </td>
                        <td class=""><label class="LabName"><span>事件规模：</span></label>
                        <div class="Check_Radio FontDarkBlue" style="width:160px;">
                		  <#if disputeScaleDC??>
                  	 		 <#list disputeScaleDC as l>
                    		  	<#if disputeMediation.disputeScale??>
									<#if (l.dictGeneralCode?string==disputeMediation.disputeScale?string)>${l.dictName}</#if>
								</#if>
                   			 </#list>
                 		 </#if>
                 		 </div>
                        </td>
                   </tr>
            		<tr>
                        <td class="LeftTd"><label class="LabName"><span>涉及人数：</span></label>
                        	<div class="Check_Radio FontDarkBlue" style="width:160px;">
                        	<#if disputeMediation.involveNum??>${disputeMediation.involveNum}（人）</#if>
                        	</div>
                        <!-- <input name="involveNum" id="involveNum" type="text" class="inp1 easyui-numberbox" style="width: 220px;" value="" max="99999" maxlength="5" data-options="required:true"/> -->
                        </td>
                        <td class="LeftTd"><label class="LabName"><span>涉及金额：</span></label>
                        	<div class="Check_Radio FontDarkBlue" style="width:160px;">
                        	<#if disputeMediation.involvedAmount??>${disputeMediation.involvedAmount?string('0.00')}（元）</#if>
                        	</div>
                         </tr>
            		<tr>
                        <td colspan="2" class="LeftTd RightTd"><label class="LabName"><span>涉及单位：</span></label>
                        		<div class="Check_Radio FontDarkBlue" style="width:460px;"><#if disputeMediation.involvedOrgName??>${disputeMediation.involvedOrgName}</#if></div>
            		</tr>
            		<tr>
                        <td colspan="2" class="LeftTd RightTd"><label class="LabName"><span>事件简述：</span></label>
                        		<div class="Check_Radio FontDarkBlue" style="width:460px;"><#if disputeMediation.disputeCondition??>${disputeMediation.disputeCondition}</#if></div>
            		</tr>
            		<#if itype==''&&displayTaiwangFlag??&&displayTaiwangFlag=='true'>
                    <tr>
                    	<td class="LeftTd" colspan="2"><label class="LabName"><span>是否涉台：</span></label>
                    	<div class="Check_Radio FontDarkBlue" style="width:460px;"><#if disputeMediation.involveType??&&disputeMediation.involveType=="TAIWANG">是<#else>否</#if></div>
                        </td>
                    </tr>
                    <#else>
                    <#if itype??&&itype=='TAIWANG'>
					<input type="hidden" id="involveType" name="involveType" value="TAIWANG">
					<#else>
                    <input type="hidden" id="involveType" name="involveType" value="<#if disputeMediation.involveType??>${disputeMediation.involveType}</#if>">
                    </#if>
                    </#if>
            		 <tr>
                        <td colspan="2" class="LeftTd RightTd"><label class="LabName">附件：</label>
                        <div id="fileupload" class="ImgUpLoad"></div></td>
            		</tr>
                    </table>
                    <div class="title FontDarkBlue">主要当事人信息</div>
                <div id="main_people" class="title FontDarkBlue">化解信息</div>
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                       <td class="LeftTd"><label class="LabName"><span>化解方式：</span></label>
                       	<div class="Check_Radio FontDarkBlue" style="width:187px;">
                		  <#if mediationTypeDC_9x??>
                  	 		 <#list mediationTypeDC_9x as l>
                    		  	<#if disputeMediation.mediationType??>
									<#if (l.dictGeneralCode?string==disputeMediation.mediationType?string)>${l.dictName}</#if>
								</#if>
                   			 </#list>
                 		 </#if>
                 		 </div>
                        </td>
                       <td class="LeftTd"><label class="LabName"><span>化解日期：</span></label>
                       	<div class="Check_Radio FontDarkBlue" style="width:187px;">
                       <#if disputeMediation.mediationDateStr??>${disputeMediation.mediationDateStr}</#if>
                       </div>
                        </td>
                        </tr>
                	<tr>
                        <td class="LeftTd"><label class="LabName"><span>化解责任人姓名：</span></label>
                        	<div class="Check_Radio FontDarkBlue" style="width:187px;">
                        	<#if disputeMediation.mediator??>${disputeMediation.mediator}</#if>
                        	</div>
                        </td>
                        <td class="LeftTd"><label class="LabName"><span>化解责任人联系方式：</span></label>
                        	<div class="Check_Radio FontDarkBlue" style="width:187px;">
                        	<#if disputeMediation.mediationTel??>${disputeMediation.mediationTel}</#if>
                        	</div>
                        </td>
                         </tr>
                         <tr>
                        <td class="LeftTd"><label class="LabName"><span>化解组织：</span></label>
                        	<div class="Check_Radio FontDarkBlue" style="width:187px;">
                        	<#if disputeMediation.mediationOrgName??>${disputeMediation.mediationOrgName}</#if>
                        	</div>
                        </td>
                        <td class="necessarily"><label class="LabName"><span>化解是否成功：</span></label>
                        <div class="Check_Radio FontDarkBlue" style="width:187px;">
                        <#if disputeMediation.isSuccess??><#if disputeMediation.isSuccess=="1">成功<#else></#if></#if>
                        </div>
                        </td>
                         </tr>
            		<tr>
                        <td colspan="2" class="LeftTd RightTd"><label class="LabName"><span>化解情况：</span></label>
                        	<div class="Check_Radio FontDarkBlue" style="width:460px;">
                        	<#if disputeMediation.mediationResult??>${disputeMediation.mediationResult}</#if>
                        	</div>
            		</tr>
                </table>
				<div class="title FontDarkBlue">考评信息</div>
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                     <tr>
                        <td class="LeftTd"><label class="LabName"><span>考评日期：</span></label>
                        	<div>
                        	<#if disputeMediation.evaDateStr??>${disputeMediation.evaDateStr}</#if>
                        	</div>
                        </td>
                         </tr>
            		
                      <tr>
                        <td colspan="2" class="LeftTd RightTd"><label class="LabName"><span>考评意见：</span></label>
                        	<div class="Check_Radio FontDarkBlue" style="width:460px;">
                        	<#if disputeMediation.evaOpn??>${disputeMediation.evaOpn}</#if>
                        	</div>
            		</tr>
            		
                    </table>
                    </div>
                    
                </div>
	</div>
	<script type="text/javascript" src="${rc.getContextPath()}/ui/js/function.js"></script>
	<#if !(showClose?? && showClose == '1')>
	<div class="BigTool">
    	<div class="BtnList">
    		<a href="#" class="BigNorToolBtn CancelBtn" onclick="javascript:cancl();">关闭</a>
        </div>
    </div>
    </#if>
    </form>
</div>

<#include "/component/ComboBox.ftl">
<#include "/component/maxJqueryEasyUIWin.ftl" />

</body>

<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/scripts/updown-1.1/swfupload/css/swfupload.css" />
<script type="text/javascript" src="${rc.getContextPath()}/scripts/updown-1.1/swfupload/swfupload.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/scripts/updown-1.1/swfupload/handlers.js"></script>
<script type="text/javascript">
var index = 0;
$(function(){
	<#if involvedPeoples??>
	<#list involvedPeoples as l>
	editMainPeople("${l.ipId!''}","${l_index}");
	</#list>
	</#if>
	
	<#if disputeMediation.mediationId??>
		fileUpload({ 
			positionId:'fileupload',//附件列表DIV的id值',
			type:'detail',//add edit detail
			initType:'ajax',//ajax、hidden编辑表单时获取已上传附件列表方式
			context_path:'${rc.getContextPath()}',
			ajaxData: {'bizId':'${disputeMediation.mediationId?c}','attachmentType':'${bizType}'},//未处理
			ajaxUrl:'${rc.getContextPath()}/zzgl/att/getList.jhtml', 	//获取上传附件的URL （新增页面可不写）
			file_types:'*.jpg;*.gif;*.png;*.jpeg;*.zip;*.doc;*.docx;*.xls;*.txt'
		});
	</#if>
})

function tableSubmit(type){
	if(type == 'save'){
		var isValid =  $("#tableForm").form('validate');
		if(isValid){
			$("#tableForm").submit();	
		}
	}
	if(type == 'saveAndReport'){
		alert(type);
	}
	if(type == 'saveAndClose'){
		alert(type);
	}
}

function singleMixedGridSelectCallback(gridId,gridName,orgId,orgCode,gridPhoto) {
	if($('#gridId').val()!=gridId){
		$('#gridId').val(gridId);
		$('#gridName').val(gridName);
		$('#gridCode').val(orgCode);
	}
}

function showResidentSelector(){
	
}

function cancl(){
	<#if source?? && source = 'workPlatform'>
		var closeCallBack = "parent.parent.top.topDialog.closeDialog";
	<#elseif  source?? && source = 'oldWorkPlatform'>
		//var closeCallBack = "parent.closeLhgdialog";
		var closeCallBack = "parent.closeMaxJqueryWindow()";
	<#else>
		var closeCallBack = "${iframeCloseCallBack!}";
	</#if>
	var iframeUrl = "${iframeUrl!}";
	
	<#if  source?? && source = 'oldWorkPlatform'>
		parent.closeMaxJqueryWindow();
	<#else>
		if(iframeUrl!="" && closeCallBack!=""){
			if(iframeUrl.indexOf('?') != -1){
				iframeUrl += "&callBack=" + closeCallBack;
			}else{
				iframeUrl += "?callBack=" + closeCallBack;
			}
			
			<#if source?? && source = 'workPlatform'>
				iframeUrl += "&source=${source}";
			<#elseif  source?? && source = 'oldWorkPlatform'>
				iframeUrl += "&source=${source}";
			<#else>
			</#if>
			$("#crossOverIframe").attr("src", iframeUrl);
		}else{
			parent.closeMaxJqueryWindow();
		}
	</#if>
}

function editMainPeople(ipId,index){
	if(ipId!=null&&ipId!=""){
		var main_people_html = '<iframe id="involvedFrame'+index+'" onload="changeFreeHeight(this)" scrolling="no" width="100%" style="border:none;" frameborder="0" src="${rc.getContextPath()}/zzgl/involvedPeople/detail.jhtml?index='+index+'&ipId='+ipId+'"></iframe>';
		$('#main_people').before(main_people_html);
		index+=1;
	}
}

function changeFreeHeight(obj){
	var mainheight = $(obj).contents().find("body").height();
    $(obj).height(mainheight);
}

</script>
<iframe id="crossOverIframe" name="crossOverIframe" src="" style="display:none;" />
</html>
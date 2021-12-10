<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>禁毒事件新增/编辑</title>
	<link rel="stylesheet" type="text/css" href="${SQ_FILE_URL}/js/swfupload/css/swfupload.css" />
	<#include "/component/commonFiles-1.1.ftl" />
	<script type="text/javascript" src="${SQ_FILE_URL}/js/swfupload/swfupload.js"></script>
	<script type="text/javascript" src="${SQ_FILE_URL}/js/swfupload/handlers.js"></script>
	<script type="text/javascript" src="${COMPONENTS_URL}/js/fastreply/fastReply.js" defer="true"></script>
	<script type="text/javascript" src="${SQ_ZZGRID_URL}/es/component/comboselector/clientJs.jhtml"></script>
	
	<style type="text/css">
		.cellWidth{width: 128px;}
		.doubleCellWidth{width: 85%;}
	</style>
</head>
<body>
	<form id="tableForm" name="tableForm" action="" method="post">
		<input type="hidden" id="drugEnforcementId" name="drugEnforcementId" value="<#if drugEnforcementEvent.drugEnforcementId??>${drugEnforcementEvent.drugEnforcementId?c}</#if>"/>
		<input type="hidden" id="ciRsId" name="ciRsId" value="<#if drugEnforcementEvent.ciRsId??>${drugEnforcementEvent.ciRsId?c}</#if>"/>
		<input type="hidden" id="drugId" name="drugId" value="<#if drugEnforcementEvent.drugId??>${drugEnforcementEvent.drugId?c}</#if>"/>
		
		<div id="content-d" class="MC_con content light">
			<div id="norFormDiv" class="NorForm" style="width:718px;">
				<div>
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>姓名：</span></label>
								<input type="text" id="i_name" class="comboselector" data-options="required:true, tipPosition:'bottom', height: '28px', dType:'drug_nx', afterSelect:nameSelected, value: '<#if drugEnforcementEvent.ciRsId??>${drugEnforcementEvent.ciRsId?c}</#if>'" query-params="<#if startGridId??>gridId=${startGridId?c}</#if>"/>
							</td>
							<td>
								<label class="LabName"><span>公民身份号码：</span></label>
								<label id="idCard" class="Check_Radio"></label>
							</td>
						</tr>
						<tr id="drugGridNameTr" class="hide">
							<td colspan="2" class="LeftTd">
								<label class="LabName"><span>场所：</span></label>
								<label id="drugGridName" class="Check_Radio"></label>
							</td>
						</tr>
						<tr id="drugVarTr" class="hide">
							<td colspan="2" class="LeftTd">
								<label class="LabName"><span>吸食毒品种类：</span></label>
								<label id="drugVar" class="Check_Radio doubleCellWidth"></label>
							</td>
						</tr>
						<#if drugEventContentDict?? && (drugEventContentDict?size > 0)>
							<input type="hidden" id="content" name="content" value="${drugEnforcementEvent.content!}" />
							<#list drugEventContentDict as item>
								<#if item_index % 2 == 0 >
									<tr>
								</#if>
										<td class="LeftTd">
											<label class="LabName"></label><div class="Check_Radio"><span><input type="checkbox" id="${item.dictId?c}" name="contentCheckbox" value="${item.dictGeneralCode}" <#if drugEnforcementEvent.content?? && (drugEnforcementEvent.content?index_of(item.dictGeneralCode) >= 0)>checked</#if> onclick="contentCheck();" /><label for="${item.dictId?c}" style="cursor:pointer">${item.dictName}</label></span></div>
										</td>
								<#if item_index % 2 == 1 >
									</tr>
								</#if>
							</#list>
							
							<#if (drugEventContentDict?size % 2 == 1)>
									<td class="LeftTd"></td>
								</tr>
							</#if>
						</#if>
						<tr>
							<td colspan="2" class="LeftTd">
								<label class="LabName"><span>社会毒情：</span></label>
								<input type="hidden" id="drugSocialSituation" name="drugSocialSituation" value="${drugEnforcementEvent.drugSocialSituation!}" />
								<#if drugSocailSituationDict?? && (drugSocailSituationDict?size > 0) >
									<#list drugSocailSituationDict as dict>
										<div class="Check_Radio"><span><input type="radio" name="drugSocialSituationRadio" id="drugSocialSituation_${dict.dictGeneralCode}" onclick="drugSocialSituationVal('${dict.dictGeneralCode}')" <#if drugEnforcementEvent.drugSocialSituation?? && drugEnforcementEvent.drugSocialSituation == dict.dictGeneralCode>checked</#if> /><label for="drugSocialSituation_${dict.dictGeneralCode}" style="cursor:pointer">${dict.dictName}</label></span></div>
									</#list>
								</#if>
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>联系人员：</span></label><input  id="contactUser" name="contactUser" type="text" class="inp1 easyui-validatebox cellWidth" data-options="tipPosition:'bottom',validType:['maxLength[30]','characterCheck']" value="${drugEnforcementEvent.contactUser!}" />
							</td>
							<td>
								<label class="LabName"><span>联系电话：</span></label><input name="contactTel" id="contactTel" type="text" class="inp1 easyui-validatebox cellWidth" data-options="tipPosition:'bottom',validType:'mobileorphone'" value="${drugEnforcementEvent.contactTel!}" />
							</td>
						</tr>
						<tr id="adviceTr" class="hide">
							<td colspan="2" class="LeftTd">
								<label class="LabName"><span>办理意见：</span></label><textarea rows="3" style="height:80px;" id="advice" name="advice" class="area1 easyui-validatebox fast-reply" data-options="required:true,tipPosition:'bottom',validType:['maxLength[2048]','characterCheck']"></textarea>
							</td>
						</tr>
						<tr>
							<td colspan="2" class="LeftTd">
								<label class="LabName"><span>图片上传：</span></label><div class="ImgUpLoad" id="fileupload"></div>
							</td>
						</tr>
					</table>
				</div>
			</div>
		</div>
		
		<div class="BigTool">
			<div class="BtnList">
				<a href="###" onclick="showAdvice();" class="BigNorToolBtn SaveBtn">保存</a>
				<a href="###" onclick="showAdvice('1');" class="BigNorToolBtn BigShangBaoBtn">提交</a>
				<a href="###" onclick="showAdvice('1', '1');" class="BigNorToolBtn BigJieAnBtn">归档</a>
				<a href="###" onclick="cancel();" class="BigNorToolBtn CancelBtn">取消</a>
			</div>
		</div>
	</form>
	
	<script type="text/javascript">
		$(function() {
			var options = { 
				axis : "yx", 
				theme : "minimal-dark" 
			}; 
			
			$("#norFormDiv").width($(window).width());
			
			enableScrollBar('content-d',options); 
			
			var swfOpt = {
				positionId:'fileupload',//附件列表DIV的id值',
				type:'add',//add edit detail
				initType:'jsonp',//ajax、hidden编辑表单时获取已上传附件列表方式
				context_path:'${SQ_FILE_URL}',
				script_context_path:'${SQ_FILE_URL}',
				ajaxData: {'eventSeq':1},//未处理
				file_types:'*.jpg;*.gif;*.png;*.jpeg',
				radio_list: [{'name':'处理前', 'value':'1'},{'name':'处理中', 'value':'2'},{'name':'处理后', 'value':'3'}],
				appCode: 'zhsq_event',
				showPattern: 'pic',
				imgDomain: '${imgDownPath!}',
				initAttrParam: {'attachmentType':'${ATTACHMENT_TYPE!}'}
			},
			drugEnforcementId = $("#drugEnforcementId").val(),
			eventSeq = "1,2,3";
		    
			if(drugEnforcementId && drugEnforcementId > 0) {
				swfOpt["type"] = 'edit'; 
				swfOpt["ajaxData"] = {'bizId':drugEnforcementId,'attachmentType':'${ATTACHMENT_TYPE!}','eventSeq':eventSeq};
			}
			
			fileUpload(swfOpt);
			
			$("#advice").width($(window).width() * 0.85);
		});
	    
		function tableSubmit(isReport, isClose) {
		    var isValid = $("#tableForm").form('validate');
		    
			if(isValid){
				modleopen();
				
				$("#tableForm").attr("action","${rc.getContextPath()}/zhsq/drugEnforcementEvent/saveData.jhtml");
	      	
			  	$("#tableForm").ajaxSubmit(function(data) {
			  		if(data.success && data.success == true){
			  			modleclose();
			  			
			  			if(isReport && isReport == "1") {
			  				$("#drugEnforcementId").val(data.drugEnforcementId);//防止重复新增
			  				startWorkflow(data.drugEnforcementId, isClose);
			  			} else {
			  				var drugEnforcementId = $("#drugEnforcementId").val(),
			  					isCurrent = drugEnforcementId && drugEnforcementId > 0;
			  				
	  						parent.reloadDataForSubPage(data.tipMsg, isCurrent);
	  					}
	  				} else {
	  					modleclose();
	  					
	  					if(data.tipMsg) {
	  						$.messager.alert('错误', data.tipMsg, 'error');
	  					} else {
	  						$.messager.alert('错误', '操作失败！', 'error');
	  					}
	  				}
				});
			}
		}
		
		function showAdvice(isReport, isClose) {
			if(isClose && isClose == '1') {
				$('#advice').validatebox({
					required: true
				});
				
				$("#adviceTr").show();
			} else {
				$("#adviceTr").hide();
				
				$('#advice').validatebox({
					required: false
				});
				
				$('#advice').val("");
			}
			
			tableSubmit(isReport, isClose);
		}
		
		function startWorkflow(drugEnforcementId, isClose) {
			if(drugEnforcementId && drugEnforcementId > 0) {
				modleopen();
				
				isClose = isClose || '0';
				
				$.ajax({
					type: "POST",
		    		url : '${rc.getContextPath()}/zhsq/drugEnforcementEvent/startWorkflow4DrugEnforcement.jhtml',
					data: {'drugEnforcementId' : drugEnforcementId, 'isClose' : isClose, 'advice' : $("#advice").val()},
					dataType:"json",
					success: function(data){
						if(data.success && data.success == true) {
							if(isClose == '1') {
								parent.reloadDataForSubPage(data.tipMsg, true);
							} else {
								parent.searchData();
								parent.detail(drugEnforcementId, "2");
								if(typeof(parent.closeBeforeMaxJqueryWindow) == 'function'){
									parent.closeBeforeMaxJqueryWindow();
								}
							}
						} else {
							modleclose();
							
	  						parent.searchData(true);//为了能及时看到因提交失败时的新增/编辑操作结果
	  						
		  					if(data.tipMsg) {
		  						$.messager.alert('错误', data.tipMsg, 'error');
		  					} else {
		  						$.messager.alert('错误', '操作失败！', 'error');
		  					}
						}
					},
					error:function(data){
						$.messager.alert('错误','连接错误！','error');
					}
		    	});
	    	}
		}
			
		function cancel(){
			parent.closeMaxJqueryWindow();
		}
		
		function contentCheck() {
			var content = '';
			
			$('input[type="checkbox"][name="contentCheckbox"]:checked').each(function() {
				var val = $(this).attr('value');
				if(val) {
					content = content + ',' + val;
				}
			});
			
			if(content) {
				content = content + ',';
			}
			
			$("#content").val(content);
		}
		
		function drugSocialSituationVal(val) {
			$("#drugSocialSituation").val(val);
		}
		
		function nameSelected(data) {//姓名选择
			var ciRsId = null, drugId = null;
			
			if(data) {
				ciRsId = data.ciRsId;
				drugId = data.drId;
			}
			
			if(drugId) {
				$("#ciRsId").val(ciRsId);
				$("#idCard").html(data.identityCard);
				findDrugInfoByDrugId(drugId);
			} else {
				$("#ciRsId").val("");
				$("#idCard").html("");
				$("#drugGridNameTr").hide();
				$("#drugGridName").html("");
				$("#drugVarTr").hide();
				$("#drugVar").html("");
				$("#drugId").val("");
			}
		}
		
		function findDrugInfoByDrugId(drugId) {
			if(drugId) {
				$.ajax({
					type: "POST",
					url : '${rc.getContextPath()}/zhsq/drugEnforcementEvent/findDrugInfoById.jhtml',
					data: {'drugId' : drugId},
					dataType:"json",
					success: function(data){
						if(data) {
							var drugVar = data.drugVar || '无';
							$("#drugGridName").html(data.gridPath);
							$("#drugGridNameTr").show();
							$("#drugVar").html(drugVar);
							$("#drugVarTr").show();
							$("#drugId").val(data.drId);
						} else {
							$("#drugGridNameTr").hide();
							$("#drugGridName").html("");
							$("#drugVarTr").hide();
							$("#drugVar").html("");
							$("#drugId").val("0");
						}
					},
					error:function(data){
						$.messager.alert('错误','连接错误！','error');
					}
				});
			}
		}
	</script>
	
</body>
</html>

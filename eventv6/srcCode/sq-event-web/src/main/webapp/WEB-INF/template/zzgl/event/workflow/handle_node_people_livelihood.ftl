<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>工作流通用办理页面</title>
	
	<script type="text/javascript" src="${COMPONENTS_URL}/js/fastreply/fastReply.js" defer="true"></script>
	<style type="text/css">
		.DealProcess{background-color:#F9F9F9;}
		.DealProcess td{padding-bottom:10px;}
		.DealProcess .LabName{width:75px;}
		.DealMan{width:630px; line-height:24px;}
		.MarskDiv{width:844px; height:52px; position:absolute; z-index:2; top:0; left:0;}
		/*办理人员全选样式*/
		.SelectAll{padding:2px 8px; *padding:0 8px 2px 5px; display:block; float:left; border-radius:3px; background:#2dcc70; color:#fff; font-weight:normal; font-size:12px;}
		.SelectAll:hover{background:#27ae61;}
	</style>
</head>

<body>
	<form id="flowSaveForm"	name="flowSaveForm" action="" method="post" enctype="multipart/form-data">

		<input type="hidden" id="formId" name="formId" value="${formId?c}" />
		<input type="hidden" id="formType" value="${formType!}" />
		<input type="hidden" id="curNodeName" name="curNodeName" value="${curNodeName!}" /> 
		<input type="hidden" id="instanceId" name="instanceId" value="<#if instanceId??>${instanceId?c}</#if>" />

		<input type="hidden" id="nodeId" name="nodeId" />
		<input type="hidden" id="nodeName_" name="nextNodeName" />  
		
		<input type="hidden" id="selectedNodeValue" name="selectedNodeValue" value="" /> 
		<input type="hidden" id="selectedNodeDynamicSelect" name="selectedNodeDynamicSelect" value="" /> 
		<input type="hidden" id="selectedNodenNodeType" name="selectedNodenNodeType" value="" /> 
		
		<input type="hidden" id="specificUrl" winName="" value="" />
		
		<!--流程有关-->
		<table id="dealProcessTable" width="100%" border="0" cellspacing="0" cellpadding="0" class="DealProcess">
			<tr id="tr_epath">
				<td>
					<label class="LabName"><span><label class="Asterik">*</label>下一环节：</span></label>
					<#if nextTaskNodes??>
						<div class="Check_Radio" style="margin-top:7px; width: 90%; line-height: 20px;">
							<#list nextTaskNodes as node>
								<span>
									<input type="radio" id="_node_${node.nodeId?c}" name="nextNode" onclick="BaseWorkflowNodeHandle.checkRadio(this);"
									nodeName="${node.nodeName}" nodeType="${node.nodeType}" nodeCode="${node.transitionCode!''}"
									dynamicSelect="<#if node.dynamicSelect??>${node.dynamicSelect}</#if>" nodeId="${node.nodeId?c}" 
									value="${node.nodeName}"><label for="_node_${node.nodeId?c}" style="cursor:pointer"><#if node.nodeNameZH??>${node.nodeNameZH}<#else>${node.nodeName}</#if></label></input>
								</span>
							</#list>
						</div>
					</#if>
				</td>
			</tr>
			<tr id="userDiv" class="hide"> 
				<td>
					<label class="LabName"><span><label class="Asterik">*</label><label id="handlerLabel" defaultValue="办理人员">办理人员</label>：</span></label>
					<div class="Check_Radio">
						<a href="###" class="NorToolBtn EditBtn" id="userSelectBtn" onclick="BaseWorkflowNodeHandle.selectHandler();">选择人员</a>
					</div>
					<input type="hidden" name="nextUserIds" id="nextUserIds"/>
					<input type="hidden" name="nextOrgIds" id="nextOrgIds"/>
					<input type="hidden" id="nextOrgNames" />
					<div class="FontDarkBlue fl DealMan Check_Radio"><b name="userNames" id="userNames"></b></div>
					<div class="FontDarkBlue fl DealMan"><b name="htmlUserNames" id="htmlUserNames"></b></div>
					<input type="hidden" name="htmlUserIds" id="htmlUserIds"/>
					<!-- <input type="text" name="htmlUserNames" id="htmlUserNames" class="mustinput" readonly="readonly" style="width: 250px !important;"/> -->
				</td>
			</tr> 
			<tr id="assistOrgDiv" class="hide"> 
				<td>
					<label class="LabName"><span><label id="assistOrgLabelHtml" defaultValue="办理单位">办理单位</label>：</span></label>
					<div class="Check_Radio">
						<a href="###" class="NorToolBtn EditBtn" id="userSelectBtn" onclick="BaseWorkflowNodeHandle.selectOrgInfo();">选择单位</a>
					</div>
					<input type="hidden" name="assistOrgIds" id="assistOrgIds" />
					<input type="hidden" name="assistOrgNames" id="assistOrgNames" />
					<input type="hidden" name="assistOrgLabel" id="assistOrgLabel" />
					<div class="FontDarkBlue fl DealMan Check_Radio"><b  id="assistOrgNamesHtml"></b></div>
				</td>
			</tr>
			<#if isCurHandler?? && isCurHandler>
	            <tr>
		    		<td colspan="2" class="LeftTd">
		    			<label class="LabName"><span>相关附件：</span></label><div id="bigFileUploadDiv"></div>
		    		</td>
		    	</tr>
            <#else>
	    	</#if>
			<tr class="evaluate hide"> 
				<td>
					<label class="LabName"><span><label class="Asterik">*</label>评价等级：</span></label>
					<#if evaLevelDict??>
					<div class="Check_Radio" style="margin-top:7px;">
						<#list evaLevelDict as evaItem>
							<span><input type="radio" name="evaLevel" value="${evaItem.dictGeneralCode}" id="eva_${evaItem.dictId?c}" <#if evaItem.dictGeneralCode=='02'>checked</#if>/><label for="eva_${evaItem.dictId?c}" style="cursor:pointer;">${evaItem.dictName}</label></span>
						</#list>
					</div>
					</#if>
				</td>
			</tr>
			<tr class="evaluate hide">
				<td>
					<label class="LabName"><span><label class="Asterik">*</label>评价意见：</span></label>
					<textarea rows="3" style="height:50px;" id="evaContent" name="evaContent" class="area1 easyui-validatebox fast-reply autoDoubleCell" data-options="tipPosition:'bottom',validType:['maxLength[255]','characterCheck']"></textarea>
				</td>
			</tr>
			<tr id="remarkTr">
				<td>
					<label class="LabName"><span id="remarkSpanHtml"><label class="Asterik">*</label>办理意见：</span></label>
					<textarea rows="3" style="height:50px;" id="advice" name="advice" class="area1 easyui-validatebox fast-reply autoDoubleCell" data-options="tipPosition:'bottom',validType:['maxLength[2048]','characterCheck']"></textarea>
				</td>
			</tr>
		</table>
		<div id="smsContentDiv" class="hide">
			<table width="100%" border="0" cellspacing="0" class="DealProcess">
				<tr>
					<td>
						<label class="LabName"><span></span></label>
						<div class="Check_Radio">
							<input type='checkbox' id='sendSms_' name='sendSms_' onclick="BaseWorkflowNodeHandle.showSmsCont();" /><label for="sendSms_" style="cursor:pointer;">发送短信</label>
						</div>
					</td>
				</tr>
				<tr class="smsContentItem">
					<td>
						<label class="LabName"><span>接收人员：</span></label>
						<input type="hidden" id="smsReceiveUserIds" name="smsReceiveUserIds" />
						<div class="Check_Radio"><div id="smsReceiveUserNames" class="FontDarkBlue fl DealMan"></div></div>
					</td>
				</tr>
				<tr class="smsContentItem">
					<td>
						<label class="LabName"><span>手机号码：</span></label>
						<input type="hidden" id="otherMobileNumsHidden" name="otherMobileNums" value="" />
						<input id="otherMobileNums" class="inp1 autoDoubleCell" style="width:711px; *width:706px;" type="text" defaultValue="可以输入额外需要发送短信的手机号码，以英文逗号(,)分隔" value="可以输入额外需要发送短信的手机号码，以英文逗号(,)分隔">
					</td>
				</tr>
				<tr class="smsContentItem">
					<td>
						<label class="LabName"><span>短信内容：</span></label>
						<textarea rows="3"  id="smsContent" name="smsContent" class="area1 autoDoubleCell easyui-validatebox"	style="width:711px;" data-options="tipPosition:'bottom',validType:['maxLength[500]','characterCheck']"></textarea>
					</td>
				</tr>
			</table>
		</div>
		
		<div style="width:100%; background: #f4f4f4; padding:10px 0;position:relative;">
			<div id="operateBtnMask" class="MarskDiv hide"></div>
        	<div class="BtnList">
        		<#if operateList??>
					<#list operateList as l>
						<a href="###" onclick="BaseWorkflowNodeHandle.${l.operateEvent}();" class="BigNorToolBtn ${l.operateEvent}" style="padding:0 12px 0 10px;"><img src="${rc.getContextPath()}/images/${l.operateEvent}.png" style="vertical-align:middle; margin:-4px 6px 0 0;" />${l.anotherName}</a>
					</#list>
				</#if>
            </div>
        </div>	
	</form>
	
	<#include "/component/maxJqueryEasyUIWin.ftl" />
	<#include "/zzgl/event/workflow/select_user.ftl" />
	<#include "/zzgl/event/workflow/select_orgInfo.ftl" />
</body>

<script type="text/javascript">
	var g_EventNodeCode = null;// 全局用于存放事件节点信息，select_user.ftl页面有使用
	
	(function($) {
		var defaultParam = {
			subTask: {
				subTaskUrl: '',
				subTaskOperate: subTaskOperate,
				subTaskCallback: subTaskCallBack
			},
			reject: {
				rejectUrl: '',
				rejectOperate: rejectOperate,
				rejectCallback: rejectCallBack
			},
			checkRadio: {
				radioCheckOperate: radioCheckOperate,
				radioCheckCallback: null
			},
			sendSms: {
				smsContentUrl: null,
				isSendSms: false,
				isRemote: true
			},
			evaluate: {
				isShowEva: false
			},
			transactor: {
				handlerLabelName : '办理人员',
				isShowAssistOrg : false,
				assistOrgLabelName : '办理单位'
			},
			selectHandler: {//人员选择
				nextUserIdElId 		: 'nextUserIds',
				nextUserNameElId 	: 'userNames',
				nextOrgIdElId 		: 'nextOrgIds',
				nextOrgNameElId 	: 'nextOrgNames',
				nextNodeType 		: 'task',
				formId				: $("#formId").val(),
				formType			: $("#formType").val(),
				isShowOrgNameFuzzyQuery : false
			},
			selectOrgInfo: {//组织选择
				nextOrgIdElId			: 'assistOrgIds', 
				nextOrgNameElId			: 'assistOrgNames', 
				nextOrgNameHtmlId		: 'assistOrgNamesHtml', 
				formType				: $("#formType").val(),
				isShowOrgNameFuzzyQuery	: false
			},
			initDefaultValueSet : null
		},
		initParamObj = $.extend({}, defaultParam);
		
		$(function() {
			var defaultMsg = $("#otherMobileNums").attr("defaultValue"),
				winWidth = $(window).width();
			
			defaultRadioCheck();
			
			//自动调整需要自适应宽度的组件，由于受滚动条组件的影响，会加载多次，从而添加isSettledAutoWidth用于防止多次初始化
			$("#flowSaveForm .autoDoubleCell").not(".isSettledAutoWidth").each(function() {
				$(this).width((winWidth - $(this).siblings(".LabName").eq(0).outerWidth(true)) * 0.92)
					   .addClass("isSettledAutoWidth");
			});
			
			$('#otherMobileNums').focus(function(){ 
				if(defaultMsg == $('#otherMobileNums').val()) {
					$('#otherMobileNums').val("");
				}  
			}).blur(function(){
				if($('#otherMobileNums').val() == "") {
					$('#otherMobileNums').val(defaultMsg);
				} 
			});
		});
		
		//对外开放方法
		BaseWorkflowNodeHandle = {
			initParam 			: initParamOperate,
			selectHandler 		: _selectHandler,
			subTask				: initParamObj.subTask.subTaskOperate,
			reject 				: initParamObj.reject.rejectOperate,
			checkRadio 			: initParamObj.checkRadio.radioCheckOperate,
			showSmsCont			: showSmsCont,
			selectOrgInfo		: _selectOrgInfo,
			checkUser			: checkUser,
			checkAllUser		: checkAllUser,
			selectRadioUser		: selectRadioUser
		};
		
		//参数初始化设置
		function initParamOperate(option, parent) {
			var result = "";
			
			if(!parent) {
				parent = initParamObj;
			}
			
			for(var index in option) {
				if(typeof option[index] === 'object') {
					parent[index] = initParamOperate(option[index], parent[index]);
				} else {
					result = (option[index] ? option[index] : defaultParam[index]) || '';
					
					parent[index] = result;//通过变量result，防止initParamObj对defaultParam的引用
				}
			}
			
			if(parent.initDefaultValueSet && typeof parent.initDefaultValueSet === 'function') {
				parent.initDefaultValueSet();
			}
			
			return parent;
		}
		
		//表单验证
		function __validate(callback) {
			var validate = true,
				isPerson = false,
				isOrganization = false,
				execPath = $("#selectedNodeValue").val();//执行路径
			
			if(g_EventNodeCode != undefined) {
				isPerson = g_EventNodeCode.person;
				isOrganization = g_EventNodeCode.organization;
			}
			
			if(isBlankString(execPath)) {
				$.messager.alert('提示', "请选择下一环节!", 'info');
				validate = false;
			} else if($("#selectedNodeDynamicSelect").val()=='1' || isPerson || isOrganization || g_EventNodeCode==undefined) {
				//selectedNodenNodeType为3时表示结束节点；为1时表示办理节点
				//selectedNodeDynamicSelect为1时表示“动态分配”
				var nextUserIds = $("#nextUserIds").val(),
					nextOrgIds = $("#nextOrgIds").val();
				
				if(isBlankString(nextUserIds) && isBlankString(nextOrgIds)) {
					$.messager.alert('提示', "请选择办理人员!", 'info');
					validate = false;
				}
			}
			
			if(validate) {
				validate = __formValidate();
			}
			
		    callback(validate);
		}
		
		function __formValidate() {
			var validate = $("#flowSaveForm").form('validate');
			
			if(validate) {
				var mobileValid = "";
				
				if($("#otherMobileNums").is(":visible")) {
					var defaultMsg = $("#otherMobileNums").attr("defaultValue"),
						otherMobileNums = $("#otherMobileNums").val();
					
					if(defaultMsg != otherMobileNums) {
						var mobileArray = otherMobileNums.split(","),
							mobile = null,
							msgFail = "";
						
						for(var index = 0, len = mobileArray.length; index < len; index++) {
							mobile = mobileArray[index];
							
							if(mobile) {
								if(isMobile(mobile)) {
									mobileValid += "," + mobile;
								} else {
									msgFail += "[" + mobile + "] ";
								}
							}
						}
						
						if(msgFail.length > 0) {
							validate = false;
							$.messager.alert("警告", "存在如下不合理的手机号码：" + msgFail, "warning");
						} else if(mobileValid.length > 0) {
							mobileValid = mobileValid.substr(1);
						}
					}
				}
				
				$("#otherMobileNumsHidden").val(mobileValid);
			}
			
			return validate;
		}
		
		function defaultRadioCheck() {
			//设置默认选中第一个选项
			<#if nextTaskNodes??>
				<#list nextTaskNodes as node>
					<#if node_index == 0>
						var radioCheck = $('#_node_${node.nodeId}');
						$(radioCheck).attr('checked', true);
						BaseWorkflowNodeHandle.checkRadio(radioCheck);
					</#if>
				</#list>
			</#if>
		}
		
		//下一办理环节选中操作
		function radioCheckOperate(obj) {
			$('#nextUserIds').val('');
			$('#userNames').html('');
			$('#nextOrgIds').val('');
			$('#htmlUserIds').val('');
			$('#htmlUserNames').html('');
			$('#htmlUserNames').hide();
			$('#userSelectBtn').show();
			
			$('#userDiv').hide();
			$("#assistOrgDiv").hide();
			$('#nodeId').val($(obj).attr('nodeId'));
			$('#nodeName_').val($(obj).attr('nodeName'));
			
			$('#assistOrgIds').val('');
			$('#assistOrgNames').val('');
			$('#assistOrgLabel').val('');
			$('#assistOrgNamesHtml').html('');
		
			$('#selectedNodeValue').val($(obj).val());
			$('#selectedNodeDynamicSelect').val($(obj).attr('dynamicSelect'));
			$('#selectedNodenNodeType').val($(obj).attr('nodeType'));
			
			modleopen();
			g_EventNodeCode = null;
			var curnodeName_ = $("#curNodeName").val();
			var nodeName_ = $("#nodeName_").val();
			var nodeCode_ = $(obj).attr('nodeCode');
			var nodeId_ = $(obj).attr('nodeId');
			var formId_ = $("#formId").val();
			// 通过ajax请求判断是否隐藏人员选择器，如果是上报越级，隐藏人员选择器，并将值传入userIds和userNames
			$.ajax({
				type:"post",
				url:"${rc.getContextPath()}/zhsq/peopleLivelihood/getNodeInfo.jhtml",
				data:{
					formId : formId_,
					formType : $("#formType").val(),
					curnodeName: curnodeName_,
					nodeName: nodeName_,
					nodeCode: nodeCode_,
					nodeId: nodeId_,
					instanceId: $("#instanceId").val()
				},
				dataType:"json",
				success:function(data) {
					modleclose();
					if (data.msg && data.msg != "") {
						$.messager.alert("警告", data.msg, "warning");
						$('#userNames').hide();
						$('#htmlUserNames').hide();
						$("#userSelectBtn").hide();
						$('#userDiv').hide();
					} else {
						g_EventNodeCode = data.eventNodeCode;
						var isCollect = false,		//采集
							isClose = false,		//结案
							isPlaceFile = false,	//归档
							isDisplayUser = data.isDisplayUser,	//是否只展示可办理人员
							isUserRadioStyle = data.isUserRadioStyle,//可办理人员是否以radio单选风格显示
							isSelectUser = data.isSelectUser,		//是否选择办理人员
							isSelectOrg = data.isSelectOrg,			//是否选择组织
							isShowAssistOrg = data.isShowAssistOrg;	//是否显示组织选择
						
						$("#specificUrl").val(data.specificUrl || "");
						$("#specificUrl").attr("winName", data.specificUrlName || "");
						
						if(g_EventNodeCode != undefined){
							isCollect = g_EventNodeCode.collect;			//采集
							isClose = g_EventNodeCode.close;				//结案
							isPlaceFile = g_EventNodeCode.placeFile;		//归档
						}
						
						//将isShowAssistOrg调整到isSelectUser和isSelectOrg之前，是为了避免label名称变更导致一时的“恍惚”
						if(isShowAssistOrg) {
							$("#handlerLabel").html(initParamObj.transactor.handlerLabelName);
							
							if(initParamObj.transactor.isShowAssistOrg == true) {
								var assistOrgLabelHtml = initParamObj.transactor.assistOrgLabelName;
								
								$("#assistOrgLabelHtml").html(assistOrgLabelHtml);
								$('#assistOrgLabel').val(assistOrgLabelHtml);
								
								$("#assistOrgDiv").show();
							}
						} else {
							$("#handlerLabel").html($("#handlerLabel").attr("defaultValue"));
							$("#assistOrgLabelHtml").html($("#assistOrgLabelHtml").attr("defaultValue"));
						}

                        if (isDisplayUser) {//只展示人员信息，不可修改
                            $('#nextUserIds').val(data.userIds);
                            $('#nextOrgIds').val(data.orgIds);
                            $('#userNames').html(data.userNames);
                            $('#userNames').hide();

                            var htmlUserIds = $('#nextUserIds').val(),
                                htmlUserOrgIds = $('#nextOrgIds').val(),
                                htmlUserNames = $('#userNames').html();

                            if(htmlUserNames!=null && htmlUserNames.length>0){
                                var htmlUserContent = "",
                                        htmlUserArray = {},
                                        htmlIdArray = {},
                                        htmlUserOrgIdArray = {},
                                        len = 0,
                                        userLabelId = "";

                                htmlUserArray = htmlUserNames.split(',');
                                len = htmlUserArray.length;

                                if(len > 0){
                                    htmlUserContent += '<div class="Check_Radio">';

                                    for(var index = 0; index < len; index++){
                                        htmlUserContent += '<span class="SelectAll" style="margin-bottom: 3px;">' + htmlUserArray[index] + '</span>';
                                    }

                                    htmlUserContent += '</div>';
                                }

                                $('#htmlUserNames').html(htmlUserContent);
                            }
                            $('#htmlUserNames').show();
                            $('#htmlUserIds').val(htmlUserIds);
                            $('#userDiv').show();
                            $("#userSelectBtn").hide();
                        } else if (isSelectUser || isUserRadioStyle) {// 事件采集、指定到人、指定到组织
                            $('#nextUserIds').val(data.userIds);
                            $('#userNames').html(data.userNames);
                            $('#nextOrgIds').val(data.orgIds);
                            $('#userNames').hide();
                            var htmlUserIds = $('#nextUserIds').val(),
                                htmlUserOrgIds = $('#nextOrgIds').val(),
                                htmlUserNames = $('#userNames').html();

                            if(htmlUserNames!=null && htmlUserNames.length>0){
                                var htmlUserContent = "",
                                        htmlUserArray = {},
                                        htmlIdArray = {},
                                        htmlUserOrgIdArray = {},
                                        len = 0,
                                        userLabelId = "";

                                htmlUserArray = htmlUserNames.split(',');
                                htmlIdArray = htmlUserIds.split(',');
                                htmlUserOrgIdArray = htmlUserOrgIds.split(',');
                                len = htmlUserArray.length;

                                if(len > 0) {
                                	if(isUserRadioStyle) {
                                		var radioChecked = '';
                                		
                                		htmlUserContent += '<div class="Check_Radio" style="margin-top: auto">';
                                		for(var index = 0; index < len; index++) {
                                			radioChecked = '';
                                			userLabelId = htmlIdArray[index] + "_" + htmlUserOrgIdArray[index] + "_" + index;
                                			
                                			if(index == 0) {
                                				radioChecked = 'checked';
                                				
                                				BaseWorkflowNodeHandle.selectRadioUser(index, htmlIdArray[index], htmlUserOrgIdArray[index]);
                                			}
                                			
                                			htmlUserContent += "<input type='radio' name='userRadio' id='"+ userLabelId +"' userid='"+ htmlIdArray[index] +"' orgid='"+ htmlUserOrgIdArray[index] + "' onclick=\"BaseWorkflowNodeHandle.selectRadioUser('" + index + "','" + htmlIdArray[index] + "','" + htmlUserOrgIdArray[index] + "')\"" + radioChecked + "/>";
                                			htmlUserContent += "<label style='cursor:pointer;' for='"+ userLabelId +"'>"+htmlUserArray[index]+"</label>" + '&nbsp;&nbsp;';
                                		}
                                	} else if(isSelectUser) {
                                		htmlUserContent += '<div class="Check_Radio">';
                                		htmlUserContent += '<p style="display:block; height:28px;">';
                                		htmlUserContent += '<span class="SelectAll">';
                                		htmlUserContent += "<input type='checkbox'  id='htmlUserCheckAll' onclick='BaseWorkflowNodeHandle.checkAllUser()' checked/>";
                                		htmlUserContent += "<label style='cursor:pointer;' for='htmlUserCheckAll'>全选</label>";
                                		htmlUserContent += "</span>";
                                		htmlUserContent += '</p>';
                                		
                                		for(var index = 0; index < len; index++) {
                                			userLabelId = htmlIdArray[index] + "_" + htmlUserOrgIdArray[index] + "_" + index;
                                			
                                			htmlUserContent += "<input type='checkbox' name='htmlUserCheckbox' id='"+ userLabelId +"' userid='"+ htmlIdArray[index] +"' orgid='"+ htmlUserOrgIdArray[index] +"' onclick='BaseWorkflowNodeHandle.checkUser()' checked/>";
                                			htmlUserContent += "<label style='cursor:pointer;' for='"+ userLabelId +"'>"+htmlUserArray[index]+"</label>" + '&nbsp;&nbsp;';
                                		}
                                	}
                                    htmlUserContent += '</div>';
                                }

                                $('#htmlUserNames').html(htmlUserContent);
                            }
                            $('#htmlUserNames').show();
                            $('#htmlUserIds').val(htmlUserIds);
                            $('#userDiv').show();
                            $("#userSelectBtn").hide();
                        } else if(isSelectOrg) {// 下派、分流、上报或者采集人组织
                            $('#userNames').show();
                            $('#htmlUserNames').hide();
                            $("#userSelectBtn").show();
                            $('#userDiv').show();
                        }
						
						var isShowEva = initParamObj.evaluate.isShowEva && isPlaceFile;
						if(isShowEva) {
							$("#dealProcessTable .evaluate").show();
							$('#remarkTr').hide();
						} else {
							$("#dealProcessTable .evaluate").hide();
							$('#remarkTr').show();
						}
						
						$('#evaContent').validatebox({
							required: isShowEva
						});
						
						$('#advice').validatebox({
							required: !isShowEva
						});
					}
					
					showSmsCont(false);//重新初始化短信发送
					
					var radioCheckCallback = initParamObj.checkRadio.radioCheckCallback;
					if(radioCheckCallback && typeof radioCheckCallback === 'function') {
						var option = {
							nodeName: nodeName_
						};
						
						$.extend(option, data);
						
						radioCheckCallback(option);
					}
				},
				error:function() {
					modleclose();
					$.messager.alert('消息提示','网络异常!');
				}
			});
		}
		
		//打开人员选择窗口
		function _selectHandler() {
			var obj = initParamObj.selectHandler;
			
			selectUserByObj(obj);
		}
		
		//打开组织选择窗口
		function _selectOrgInfo() {
			var opt = initParamObj.selectOrgInfo;
			
			SelectWin4OrgInfo.selectOrgInfoByObj(opt);
		}
		
		//点击提交按钮操作
		function subTaskOperate() {
			var url = "";
			
			<#if bizDetailUrl??>
				url = $("#specificUrl").val();
			</#if>
			
			if(url) {
				var flag = true;
			
				__validate(function(data){
					flag = data;
				});
				
				if(flag) {
					url += $("#formId").val();
					
					showMaxJqueryWindow($("#specificUrl").attr("winName"), url, 555, 315);
				}
			} else {
				var subTaskCallback = initParamObj.subTask.subTaskCallback;
				if(subTaskCallback && typeof subTaskCallback === 'function') {
					subTaskCallback();
				}
			}
		}
		
		//待办提交
		function subTaskCallBack() {
			var url = $("#specificUrl").val();
			
			if(url) {//关闭打开的个性表单窗口
				closeMaxJqueryWindow();
			}
			
			var flag = true,
				subTaskUrl = initParamObj.subTask.subTaskUrl;
			
			if(subTaskUrl) {
				__validate(function(data) {
					flag = data;
				});
				
				if(flag){
					modleopen();
					
					$("#flowSaveForm").attr("action", subTaskUrl);
					    	
			    	$("#flowSaveForm").ajaxSubmit(function(data) {
			    		modleclose();
							
					    var result = data.success,
					    	msg = '';
					    
					    if(result && result == true) {
					    	msg = data.tipMsg || '操作成功！';
					    	cancel(msg);
					    } else {
					    	msg = data.tipMsg || '操作失败！';
					    	$.messager.alert('错误', msg, 'error');
					    }
			    	});
				}
			} else {
				$.messager.alert('警告','请设置提交请求，设置属性为[subTask.subTaskUrl]！','warning');
			}
			
		}
		
		function rejectOperate() {
			var rejectCallBack = initParamObj.reject.rejectCallback;
			
			if(rejectCallBack && typeof rejectCallBack === 'function') {
				rejectCallBack();
			}
		}
		
		//点击驳回按钮操作
		function rejectCallBack() {
			var flag = true,
				rejectUrl = initParamObj.reject.rejectUrl;
			
			if(rejectUrl) {
				flag = __formValidate();
				
				if(flag) {
					$.messager.confirm('驳回提示', '确定要驳回吗？流程将返回上一操作步骤！', function(r){
						if (r){
							if(flag){
								modleopen();
						
								$("#flowSaveForm").attr("action", rejectUrl);
								    	
						    	$("#flowSaveForm").ajaxSubmit(function(data) {
						    		modleclose();
										
									var result = data.success,
								    	msg = data.tipMsg;
								    
								    if(result && result == true) {
								    	msg = data.tipMsg || '操作成功！';
								    	cancel(msg);
								    } else {
								    	msg = data.tipMsg || '操作失败！';
								    	$.messager.alert('错误', msg, 'error');
								    }
						    	});
							}
						}
					});
				}
			} else {
				$.messager.alert('警告','请设置驳回请求，设置属性为[reject.rejectUrl]！','warning');
			}
		}
		
		function checkUser(isShowMsg){
			var nextUserIds = "";
			var userOrgIds = "";
			var userCheckbox = $("input[name='htmlUserCheckbox'][type='checkbox']:checked");
			var allCheckBoxLen = $("input[name='htmlUserCheckbox'][type='checkbox']").length;
			var checkedBoxLen = userCheckbox.length;
			
			userCheckbox.each(function() {
				nextUserIds += "," + $(this).attr("userid");
				userOrgIds += "," + $(this).attr("orgid");
			});
			
			$("#htmlUserCheckAll").attr('checked',allCheckBoxLen == checkedBoxLen);
			
			if(nextUserIds.length > 0){
				nextUserIds = nextUserIds.substr(1);
				userOrgIds = userOrgIds.substr(1);
			}else if(isShowMsg != false){
				$.messager.alert('提示', "请选择办理人员!", 'info');
			}
			
			$('#nextUserIds').val(nextUserIds);
			$('#nextOrgIds').val(userOrgIds);
		}
		
		function checkAllUser(){
			var userCheckbox = {};
			var isCheckAll = $("#htmlUserCheckAll").attr("checked"); 
			userCheckbox = $("input[name='htmlUserCheckbox'][type='checkbox']");
			
			for(var index = 0, len = userCheckbox.length; index < len; index++){
				userCheckbox[index].checked = isCheckAll;
			}
			
			checkUser(isCheckAll=='checked');
		}
		
		function selectRadioUser(index,userId,userOrgId) {
			$('#htmlUserIds').val(userId);
			$('#nextUserIds').val(userId);
			$('#nextOrgIds').val(userOrgId);
		}
		
		///取消
		function cancel(msg){
			modleclose();
			$("#operateBtnMask").removeClass("hide");//打开按钮屏蔽层，使按钮不可点击
			window.location.reload();//刷新当前页面
			
			try{
				flashData(msg);
			}catch(e){
				
			}
		}
		
		function closeWinDialog(){
			closeMaxJqueryWindow();
		}
		
		function showSmsCont(checkFlag) {
			var isSendSms = initParamObj.sendSms.isSendSms,
				isRemote = initParamObj.sendSms.isRemote,
				smsContentUrl = initParamObj.sendSms.smsContentUrl;
			
			if(isSendSms) {
				if(isRemote && isBlankStringTrim(smsContentUrl)) {
					$.messager.alert('警告','请设置获取短信内容请求，设置属性为[sendSms.smsContentUrl]！','warning');
				} else {
					$("#smsContentDiv").show();
					
					if(isNotBlankParam(checkFlag)) {
						$("#sendSms_").attr("checked", checkFlag)
					}
					
					if($("#sendSms_").is(":checked")) {
						var flag = false;
						__validate(function(data) {
							flag = data;
							$("#sendSms_").attr("checked", data);
						});
						if(flag) {
							var receiveUserNames = $("#userNames").html(),
								receiveUserOrgNames = $("#nextOrgNames").val(),
								receiveUserIds = $("#nextUserIds").val();
							
							if(receiveUserNames) {
								var receiveUserNameArray = receiveUserNames.split(","),
									receiveUsrOrgNameArray = receiveUserOrgNames.split(","),
									receiveUserHtml = "";
								
								for(var index in receiveUserNameArray) {
									receiveUserHtml += receiveUserNameArray[index];
									if(receiveUsrOrgNameArray[index]) {
										receiveUserHtml += "(" + receiveUsrOrgNameArray[index] + ")";
									}
									
									receiveUserHtml += ";";
								}
								
								$("#smsReceiveUserNames").html(receiveUserHtml);
								$("#smsReceiveUserIds").val(receiveUserIds);
							}
							
							if(isNotBlankStringTrim(smsContentUrl)) {
								modleopen();
						    	
						    	$("#flowSaveForm").attr("action", "${rc.getContextPath()}/zhsq/drugEnforcementEvent/capSmsContent.jhtml");
						    	
						    	$("#flowSaveForm").ajaxSubmit(function(data) {
						    		var smsContent = data.smsContent;
						    		
						    		$("#smsContent").validatebox({
						    			required: true
						    		});
						    		
									$("#smsContent").val(smsContent);
									$("#smsContentDiv .smsContentItem").show();
									modleclose();
						    	});
						    }
						} else {
							$("#smsContentDiv .smsContentItem").hide();
							$("#smsContent").validatebox({
								required: false
							});
						}
					} else {
						$("#smsContentDiv .smsContentItem").hide();
						$("#smsContent").validatebox({
							required: false
						});
						
						$("#smsReceiveUserNames").html("");
						$("#smsReceiveUserIds").val("");
						$("#otherMobileNumsHidden").val("");
						$("#otherMobileNums").val($("#otherMobileNums").attr("defaultValue"));
						$("#smsContent").val("");
					}
				}
			} else {
				$("#smsContentDiv").hide();
			}
		}
	})(jQuery);
</script>

</html>
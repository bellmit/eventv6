<!DOCTYPE html>
<html>
<head>
	<title>编辑</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<#include "/component/maxJqueryEasyUIWin.ftl" />
	<#include "/component/listSet.ftl" />
	<style type="text/css">
		.required {color: red;}
		.inp1 {width: 90%;}
		.sel1 {width: 100%;}
		.fr {width: auto;;float:right}
		.NorForm .sonTitle{
			background: #f9f7f7;
		    color: #000;
		    border-left: 0 solid #e60012;
		    font-size: 15px;
		    margin-top: 0px;
		    padding: 0px 20px;
		 }
		 .labelTd{width:10%;}
		 .contentTd{width:40%;}
		 .h_5{
		 	height: 5px;
		    font-size: 0;
		    line-height: 0;
		    overflow: hidden;
		 }
		 .titleTd{ text-align: center;padding-left: 0px !important;}
		 .textTd{ text-align: center;padding-left: 0px !important;}
		 .nodata{height:180px} 
		 
	</style>
	<link href="${rc.getContextPath()}/css/ypms/css/mscase.css" rel="stylesheet" />
	<link rel="stylesheet" type="text/css" href="${SQ_FILE_URL}/js/swfupload/css/swfupload.css" />
    <script type="text/javascript" src="${SQ_FILE_URL}/js/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="${SQ_FILE_URL}/js/swfupload/handlers.js"></script>
</head>
<body>
	<form id="submitForm">
		<input type="hidden" id="updateType" name="updateType" value="" />
		<input type="hidden" id="caseId" name="caseId" value="${(bo.caseId)!}" />
		<input type="hidden" id="ldIdArrStr" name="ldIdArrStr" value="${(ldIdArrStr)!}" />
		<input type="hidden" id="handleDeptName" name="handleDeptName" value="${(bo.handleDeptName)!}" />
		<input type="hidden" id="recevieDeptInfoStr" name="recevieDeptInfoStr" value="${(recevieDeptInfoStr)!}" />
		
		<div id="content-d" class="MC_con content light" style="overflow-x:hidden; position:relative;">
			<div class="NorForm">
		    		<div class="title FontDarkBlue">来电信息</div>
		    		<table width="100%" border="0" cellspacing="0" cellpadding="0" id="baseInfo">
						<tr>
							<td class="labelTd">
								<label class="LabName"><span><font class="required">*</font>主叫号码：</span></label>
							</td>
							<form id="searchFormForCallIn">
							<td class="contentTd">
								<span style="width:90%" class="Check_Radio FontDarkBlue">${(bo.callinNum)!}</span>
							</td>
							</form >
							<td class="labelTd">
								<label class="LabName"><span><font class="required">*</font>被叫号码：</span></label>
							</td>
							<td class="contentTd">
								<span style="width:90%" class="Check_Radio FontDarkBlue">${(bo.calledNum)!}</span>
							</td>
						</tr>
						<tr>
							<td class="labelTd">
								<label class="LabName"><span><font class="required">*</font>呼叫时间：</span></label>
							</td>
							<td  class="contentTd">
								<span style="width:90%" class="Check_Radio FontDarkBlue">${(bo.callinTimeStr)!}</span>
							</td>
							
							<td class="labelTd">
								<label disabled="true" class="LabName"><span><font class="required">*</font>呼叫类型：</span></label>
							</td>
							<td class="contentTd">
								<span style="width:90%" class="Check_Radio FontDarkBlue">呼入</span>
							</td>
						</tr>
						<tr>
							<td class="labelTd">
								<label class="LabName"><span><font class="required">*</font>呼叫结果：</span></label>
								
							</td>
							<td class="contentTd">
								<span style="width:90%" class="Check_Radio FontDarkBlue">成功</span>
							</td>
							
							<td class="labelTd">
								<label class="LabName"><span><font class="required">*</font>接听人员：</span></label>
							</td>
							<td class="contentTd">
								<span style="width:90%" class="Check_Radio FontDarkBlue">${(bo.receiverName)!}</span>
							</td>
						</tr>
						<tr>
							<td class="labelTd">
								<label class="LabName"><span><font class="required">*</font>客户姓名：</span></label>
							</td>
							<td class="contentTd">
								<span style="width:90%" class="Check_Radio FontDarkBlue">${(bo.customerName)!}</span>
							</td>
							
							<td class="labelTd">
								<label class="LabName"><span><font class="required">*</font>性别：</span></label>
							</td>
							<td class="contentTd">
								<#if bo.customerGender == "01">
									<span style="width:90%" class="Check_Radio FontDarkBlue">男</span>
								<#elseif bo.customerGender == "02">
									<span style="width:90%" class="Check_Radio FontDarkBlue">女</span>
								</#if>
							</td>
						</tr>
						
						
					</table>
					
					<div class="title FontDarkBlue">来电记录</div>
					<div class="h_5" id="TenLineHeight1"></div>
						<table id="callInList" ></table>
				   
				   <div class="title FontDarkBlue">案件登记</div>
		    		<table width="100%" border="0" cellspacing="0" cellpadding="0" id="caseRegister">
						<tr>
							<td class="labelTd">
								<label class="LabName"><span><font class="required">*</font>受理类型：</span></label>
							</td>
							<td class="contentTd">
	            				<#if bo.handleWay == "01">
									<span style="width:90%" class="Check_Radio FontDarkBlue">直接处理</span>
								<#elseif bo.handleWay == "02">
									<span style="width:90%" class="Check_Radio FontDarkBlue">12345平台</span>
								<#elseif bo.handleWay == "03">
									<span style="width:90%" class="Check_Radio FontDarkBlue">民生110</span>
								</#if>
	            				
							</td>
							<td class="labelTd">
								<label class="LabName"><span>重复案件号：</span></label>
							</td>
							<td class="contentTd">
								<span style="width:90%" class="Check_Radio FontDarkBlue">${(bo.relaCaseNo)!}</span>
							</td>
						</tr>
						<tr>
							<td class="labelTd">
								<label class="LabName"><span><font class="required">*</font>案件类型：</span></label>
							</td>
							<td class="contentTd">
	                            <#if bo.caseType == "01">
									<span style="width:90%" class="Check_Radio FontDarkBlue">意见投诉</span>
								<#elseif bo.caseType == "02">
									<span style="width:90%" class="Check_Radio FontDarkBlue">生活服务</span>
								<#elseif bo.caseType == "03">
									<span style="width:90%" class="Check_Radio FontDarkBlue">困难求助</span>
								<#elseif bo.caseType == "04">
									<span style="width:90%" class="Check_Radio FontDarkBlue">信息咨询</span>
								</#if>           
							</td>
							<td class="labelTd">
								<label class="LabName"><span><font class="required">*</font>案件归属地：</span></label>
							</td>
							<td class="contentTd">
								<span style="width:90%" class="Check_Radio FontDarkBlue">${(bo.caseFromName)!}</span>
							</td>
						</tr>
						<tr>
							<td class="labelTd">
								<label class="LabName"><span><font class="required">*</font>案件描述：</span></label>
							</td>
							<td colspan="3">
								<span style="width:90%" class="Check_Radio FontDarkBlue">${(bo.caseDesc)!}</span>
							</td>
						</tr>
						
					</table>
					
					
					<div class="title FontDarkBlue">案件派发
						<div class="tool fl fr">
					       	<a id="del" href="javascript: showKnowledge()" class="NorToolBtn  AddBtn" style="color: #fff;">查看知识库</a>       	
					       	<a id="del" href="javascript: showRejectInfo()" class="NorToolBtn  AddBtn" style="color: #fff;">查看驳回信息</a>       	
					    </div>
					</div>
					
					<!--联动队伍 -->	
					<div class="title FontDarkBlue sonTitle">联动队伍
						<div class="tool fl fr">
					      <#--  <a id="edit" href="javascript: toAddDept()"  class="NorToolBtn  AddBtn" style="color: #fff;">添加</a> -->
					    </div>
					</div>
					
					<table id="linkageTeam" style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
						<tr >
							<td style="width: 10%; padding-left: 3px;"></td>
							<td class="titleTd" style="width: 22.5%;" >联动队伍名称</td>
							<td class="titleTd" style="width: 22.5%;" >联络员</td>
							<td class="titleTd" style="width: 22.5%;" >分管领导</td>
							<td class="titleTd" style="width: 22.5%;" >发送短信至</td>
						</tr>
						<#if receiveDeptList??>
							<#list receiveDeptList as dept>
								<#if dept.ldType == '0'>
									<tr class="linkageTeamTr" index="${(dept.ldorgId)!}">
										<td style="padding-left:3px;" >	
											<a href="#" class="NorToolBtn DelBtn" onclick="delTr(this);" data-ldId="${(dept.ldorgId)!}" >删除</a>
										</td>
										<td class="textTd">
											<span class="FontDarkBlue" id="ldId_${(dept.ldorgId)!}" data-ldId="${(dept.ldorgId)!}" data-ldName="${(dept.ldName)!}" data-msg="${(dept.msgSendPepo)!}" >${(dept.ldName)!}</span>
										</td>
										
										<#if dept.addressBookList?? && (dept.addressBookList?size == 2)>
											<#list dept.addressBookList as ab>
												<#if ab.abRole == '1'>
													<td class="textTd" >
														<span class="FontDarkBlue" id="contacter_${(dept.ldorgId)!}" data-tel="${(ab.abMobile)!}" >${(ab.abName)!}(${(ab.abMobile)!})&nbsp;<i class="tel_icon" onclick="callOut(this)"></i></span>
													</td>
												<#elseif ab.abRole == '0'>
													<td class="textTd" >
														<span class="FontDarkBlue" id="leader_${(dept.ldorgId)!}" data-tel="${(ab.abMobile)!}">${(ab.abName)!}(${(ab.abMobile)!})&nbsp;<i class="tel_icon" onclick="callOut(this)"></i></span>
													</td>
												</#if>
											</#list>
										<#elseif dept.addressBookList?size == 1>
											<#list dept.addressBookList as ab>
												<#if ab.abRole == '1'>
													<td class="textTd" >
														<span class="FontDarkBlue" id="contacter_${(dept.ldorgId)!}" data-tel="${(ab.abMobile)!}" >${(ab.abName)!}(${(ab.abMobile)!})&nbsp;<i class="tel_icon" onclick="callOut(this)"></i></span>
													</td>
													<td class="textTd" >
														<span class="FontDarkBlue"  data-tel="" ></span>
													</td>
												<#elseif ab.abRole == '0'>
													<td class="textTd" >
														<span class="FontDarkBlue"  data-tel="" ></span>
													</td>
													<td class="textTd" >
														<span class="FontDarkBlue" id="leader_${(dept.ldorgId)!}" data-tel="${(ab.abMobile)!}">${(ab.abName)!}(${(ab.abMobile)!})&nbsp;<i class="tel_icon" onclick="callOut(this)"></i></span>
													</td>
												</#if>
											</#list>
										<#else>
											<td class="textTd" >
												<span class="FontDarkBlue" id="contacter_${(dept.ldorgId)!}" data-tel="" ></span>
											</td>
											<td class="textTd" >
												<span class="FontDarkBlue" id="leader_${(dept.ldorgId)!}" data-tel=""></span>
											</td>
										</#if>	
										
										<td class="textTd" >
											<div class="Check_Radio" style="width:100%" >
												<label class="radio-wrapper" onchange="setDataMsgTab(this)" >
													<input <#if userInfo.orgCode != bo.createOrgCode>disabled="true"</#if> type="checkbox"  name="publishPoin_${(dept.ldorgId)!}" data-ldId="${(dept.ldorgId)!}" value="01" <#if dept.msgSendPepo == '01' || dept.msgSendPepo == '12'>checked</#if> ><span class="FontDarkBlue">联络员</sapn>
												</label>
												<label class="radio-wrapper" onchange="setDataMsgTab(this)" >
													<input <#if userInfo.orgCode != bo.createOrgCode>disabled="true"</#if> type="checkbox" name="publishPoin_${(dept.ldorgId)!}" data-ldId="${(dept.ldorgId)!}" value="02" <#if dept.msgSendPepo == '02' || dept.msgSendPepo == '12'>checked</#if> ><span class="FontDarkBlue">分管领导</sapn>
												</label>
											</div >
										</td>
									</tr>
								</#if>
							</#list>
						</#if>
						
					</table>  
					
					<!--专业化队伍队伍 -->	
					<div class="title FontDarkBlue sonTitle">专业化队伍</div>
					
					<table id="professionalTeam" style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td style="width: 10%; padding-left: 3px;"></td>
							<td class="titleTd" style="width: 30%;" >专业化队伍名称</td>
							<td class="titleTd" style="width: 30%;" >业务热线</td>
							<td class="titleTd" style="width: 30%;" >服务项目</td>
						</tr>
						<#if receiveDeptList??>
							<#list receiveDeptList as dept>
								<#if dept.ldType == '1'>
									<tr class="professionalListTr" index="${(dept.ldorgId)!}">
										<td style="padding-left: 3px;">
											<a href="#" class="NorToolBtn DelBtn" onclick="delTr(this);" data-ldId="${(dept.ldorgId)!}">删除</a>
										</td>
										<td class="textTd">
											<span class="FontDarkBlue" id="ldId_p_${(dept.ldorgId)!}" data-ldId="${(dept.ldorgId)!}" data-ldName="${(dept.ldName)!}">${(dept.ldName)!}</span>
										</td>
										
										<#if dept.addressBookList?? && (dept.addressBookList?size > 0)>
											<#list dept.addressBookList as ab>
												<#if ab.abRole == '2'>
													<td class="textTd" >
														<span class="FontDarkBlue" id="tel_p_${(dept.ldorgId)!}" data-tel="${(ab.abMobile)!}" >${(ab.abName)!}(${(ab.abMobile)!})&nbsp;<i class="tel_icon" onclick="callOut(this)"></i></span>
													</td>
												</#if>
											</#list>
										<#else>
											<td class="textTd" >
												<span class="FontDarkBlue" id="tel_p_${(dept.ldorgId)!}" data-tel=""></span>
											</td>
										</#if>
										
										<td class="textTd" >
											<span class="FontDarkBlue" id="ldItem_${(dept.ldorgId)!}" >${(dept.ldItem)!}null</span>
										</td>
										<td class="textTd" >
											<span class="FontDarkBlue" id="blank" ></span>
										</td>
									</tr>

								</#if>
							</#list>
						</#if>
					</table>  
					
					
					<div class="title FontDarkBlue">案件处理</div>
						<table id="handleHis" ></table>
						<#--
						<div id="jqueryToolbar">
							<div class="h_10" id="TenLineHeight4"></div>
							<div class="ToolBar">
								<div class="tool fr">
									<a href="javascript:void(0)" class="NorToolBtn DelBtn" onclick="backToDept();">回退</a>
								</div>
							</div>
						</div>
						-->
						<div class="h_5" id="TenLineHeight3"></div>
		    		<table width="100%" border="0" cellspacing="0" cellpadding="0" id="caseHandle">
		    		<#--
						<tr>
							<td class="labelTd">
								<label class="LabName"><span><font class="required">*</font>处理过程：</span></label>
							</td>
							<td >
								<textarea rows="6" style="width: 90%;" id="handleProcess" name="handleProcess"  class="easyui-validatebox  validatebox-text" data-options="tipPosition:'bottom',validType:'maxLength[500]'">${(bo.handleProcess)!}</textarea>
							</td>
							<td class="labelTd">
								<label class="LabName"><span><font class="required">*</font>处理结果：</span></label>
							</td>
							<td >
								<textarea rows="6" style="width: 90%;" id="handleResult" name="handleResult"  class="easyui-validatebox  validatebox-text" data-options="tipPosition:'bottom',validType:'maxLength[500]'">${(bo.handleRusult)!}</textarea>
							</td>
						</tr>
					-->
						<tr>
							<td class="labelTd">
								<label class="LabName"><span>处理后照片：</span></label>
							</td>
							<td colspan="3">
								<div id="fileupload" class="ImgUpLoad" style="width:90%;"></div>
							</td>
						</tr>
						
						<tr>
							<td class="labelTd">
								<label class="LabName"><span><font class="required">*</font>专业化队伍原价(元)：</span></label>
							</td>
							<td>
								<span style="width:90%" class="Check_Radio FontDarkBlue">${(bo.pTPrice)!}</span>
							</td>
							<td class="labelTd">
								<label class="LabName"><span><font class="required">*</font>专业化队伍折后价(元)：</span></label>
							</td>
							<td>
								<span style="width:90%" class="Check_Radio FontDarkBlue">${(bo.pTDiscountPrice)!}</span>
							</td>
						</tr>
						<tr>
							<td class="labelTd">
								<label class="LabName"><span><font class="required">*</font>材料及服务费：</span></label>
							</td>
							<td>
								<span style="width:90%" class="Check_Radio FontDarkBlue">${(bo.pTServicePrice)!}</span>
							</td>
							
						</tr>
						<tr>
							<td class="labelTd">
								<label class="LabName"><span><font class="required">*</font>回访情况：</span></label>
							</td>
							<td class="contentTd">
								<span style="width:90%" class="Check_Radio FontDarkBlue">${(bo.returnRusult)!}</span>
							</td>
							<td class="labelTd">
								<label class="LabName"><span><font class="required">*</font>满意度：</span></label>
							</td>
							<td class="contentTd">
								<#if bo.satisfaction == "01">
									<span style="width:90%" class="Check_Radio FontDarkBlue">满意</span>
								<#elseif bo.satisfaction == "02">
									<span style="width:90%" class="Check_Radio FontDarkBlue">基本满意</span>
								<#elseif bo.satisfaction == "03">
									<span style="width:90%" class="Check_Radio FontDarkBlue">不满意</span>
								</#if>  
							</td>
						</tr>
						<tr>
							<td class="labelTd">
								<label class="LabName"><span><font class="required">*</font>备注：</span></label>
							</td>
							<td colspan="3">
								<span style="width:90%" class="Check_Radio FontDarkBlue">${(bo.remark)!}</span>
							</td>
						</tr>
						
					</table>
				   
				   
			 </div>
		</div>
	</div>
	<div class="BigTool">
    	<div class="BtnList">
    		<a href="javascript:;" class="BigNorToolBtn CancelBtn" onClick="cancel();">关闭</a>
        </div>
    </div>
	</form>
</body>
<script type="text/javascript">
	//关闭
	function cancel() {
		parent.closeMaxJqueryWindow();
	}

	var ldIdArr = [];//添加的联动单位的id数据

	$(function(){
		$("#content-d").mCustomScrollbar({theme:"minimal-dark"});
		
		//添加初始联动单位id到ldIdArr中
		<#if receiveDeptList??>
			<#list receiveDeptList as dept>
				ldIdArr.push(${(dept.ldorgId)!});
			</#list>
		</#if>
		
		//根据主叫号码获取相关信息		
		iniDataByCallinNum();
		
		//案件登记的所属辖区
		AnoleApi.initOrgEntityZtreeComboBox("caseFromCodeCN", null, function(gridId, items) {
			if (items && items.length > 0) {
				$("#caseFromCode").val(items[0].orgCode);
				$("#caseFromId").val(items[0].id);
			}else{
			
			}
		}, {
			ShowOptions : {
				EnableToolbar : true
			},
			OnCleared:function(){
				$("#regionCode").val("");
			}
		});
		$("#caseFromCodeCN").css("padding-right","0");
		$("#caseFromCodeCN").css("width","90%");
		
		//案件类型；01：意见投诉；02：生活服务；03：困难求助；04：信息咨询
		AnoleApi.initListComboBox("caseTypeCN", "caseType", null, null, ["<#if bo.caseId??>${(bo.caseType)!}<#else>01</#if>"], {
			ShowOptions : {
				EnableToolbar : true
			},
			DataSrc : [
				{"name":"意见投诉","value":"01"},
				{"name":"生活服务","value":"02"},
				{"name":"困难求助","value":"03"},
				{"name":"信息咨询","value":"04"}
			]
		});
		$("#caseTypeCN").css("padding-right","0");
		$("#caseTypeCN").css("width","90%");
		
		
		//处理方式；01：直接处理；02：12345平台；03：民生110
		AnoleApi.initListComboBox("handleWayCN", "handleWay", null, null, ["<#if bo.caseId??>${(bo.handleWay)!}<#else>01</#if>"], {
			ShowOptions : {
				EnableToolbar : true
			},
			DataSrc : [
				{"name":"直接处理","value":"01"},
				{"name":"12345平台","value":"02"},
				{"name":"民生110","value":"03"}
			]
		});
		$("#handleWayCN").css("padding-right","0");
		$("#handleWayCN").css("width","90%");
		
		//附件上传
		<#if bo.caseId??>
			fileUpload({
				positionId:'fileupload',//附件列表DIV的id值',
				type:'detail',//add edit detail
				initType:'jsonp',//ajax、hidden编辑表单时获取已上传附件列表方式
				context_path:'${SQ_FILE_URL}',
				ajaxData: {'bizId':${bo.caseId?c},'attachmentType':'${bizType}','eventSeq':1},
				file_types:'*.jpg;*.gif;*.png;*.jpeg;'
			});
		</#if>
		
		
		//移除派发单位id数组中的数据
		Array.prototype.remove = function(val) {
			var index = this.indexOf(val);
			if (index > -1) {
				this.splice(index, 1);
			}
		};
		Array.prototype.indexOf = function(val) {  
		    for ( var i = 0; i < this.length; i++) {  
		        if (this[i] == val)  
		            return i;  
		    }  
		    return -1;  
		};  
	});
	
	//页面加载时加载的初始数据
	function iniDataByCallinNum(){
		var callinNum = $("#callinNum").val();
		var caseId = $("#caseId").val();
		//来电客户信息
		loadCustomerData(callinNum);
		//来电记录
		loadCallInData(callinNum);
		//加载处理结果和处理过程
		loadHandleHis(caseId);
	}
	
	//加载来电客户信息
	function loadCustomerData(callinNum){
		$.ajax({
			type: 'POST',
			url: '${rc.getContextPath()}/zhsq/ypms/mscase/getCusetomerDataByTel.json',
			data: {callinNum:callinNum},
			dataType: 'json',
			success: function(data) {
				var callInPerson = data.callInPerson;
				if (callInPerson != null ){
					var cpName = callInPerson.cpName;
					var cpSex = callInPerson.cpSex;
					<#if bo.customerName??>
						
					<#else>
						$("#customerName").val(cpName)
						$(":radio[name='customerGender'][value='" + cpSex + "']").prop("checked", "checked");
					</#if>
				}else{
					<#if bo.customerName??>
						
					<#else>
						$("#customerName").val("")
						$(":radio[name='customerGender'][value='01']").prop("checked", "checked");
					</#if>
					
				}
			},
			error: function(data) {
				$.messager.alert('错误', '连接超时！', 'error');
			},
			complete : function() {
				modleclose(); //关闭遮罩层
			}
		});
	}
	//加载来电记录
	function loadCallInData(callinNum){
		$('#callInList').datagrid({
			nowrap : true,   //设置为true，当数据长度超出列宽时将会自动截取。
			striped : true, //设置为true将交替显示行背景。
			fitColumns:true, //设置为true将自动使列适应表格宽度以防止出现水平滚动
			singleSelect : false,
			//fit : true,
			pageSize: 5,
			pagination : true,//在DataGrid控件底部显示分页工具栏。
			pageList: [5],//选择一页显示多少数据   
			rownumbers : true,
			height:280,
			scrollbarSize :0,
			url: '${rc.getContextPath()}/zhsq/ypms/mscase/listDataCallInList.jhtml',
			//queryParams: $('#searchFormForCallIn').serializeJson(),
			queryParams: {callinNum:callinNum},
			columns: [[
				{field:'caseNo', title:'案件编号', align:'center', width:100},
				{field:'caseDesc', title:'案件描述', align:'center', width:100,
				formatter:function(value,rec,index){
					if(value != null ){
						var length = value.length;
						if (length > 15) {
							value = value.substring(0, 15)+"...";
						}
						var f = '<a class="" href="###" title="'+ rec.caseDesc +'" >'+ value +'</a>';
						return f;
					}	
				}},
				{field:'isRela', title:'是否重复案件', align:'center', width:100,
				formatter:function(value,rec,index){
					var relaCaseNo = rec.relaCaseNo
					if (relaCaseNo != null && relaCaseNo != ""){
						return "是";
					}else{
						return "否";
					}
				}},
				{field:'caseType', title:'案件类型', align:'center', width:100,
				formatter:function(value,rec,index){
					//01：意见投诉；02：生活服务；03：困难求助；04：信息咨询
					if (value == "01"){
						return "意见投诉";
					}else if (value == "02") {
						return "生活服务";
					}else if (value == "03") {
						return "困难求助";
					}else if (value == "04") {
						return "信息咨询";
					}
				}},
				{field:'handleStatus', title:'办理状态', align:'center', width:100,
				formatter:function(value,rec,index){
					var handleStatus = rec.handleStatus
					//01：受理中、02：回访中、03：已结案
					if (value == "01"){
						return "受理中";
					}else if (value == "02") {
						return "回访中";
					}else if (value == "03") {
						return "已结案";
					}
				}},
				{field:'handleDeptName', title:'办理部门', align:'center', width:100},
			]],
			//toolbar: '#jqueryToolbar',
			onLoadSuccess : function(data) {
			    //listSuccess(data); //暂无数据提示
				if(data.total == 0){
					$('.datagrid-body').eq(1).append('<div class="nodata"></div>');
				}
			},
			
			onLoadError : function() {
			  listError();
			}
		});
	}
	
	//加载处理过程，处理结果记录
	function loadHandleHis(caseId){
		
		$('#handleHis').datagrid({
			nowrap : true,   //设置为true，当数据长度超出列宽时将会自动截取。
			striped : true, //设置为true将交替显示行背景。
			fitColumns:true, //设置为true将自动使列适应表格宽度以防止出现水平滚动
			singleSelect : false,
			//fit : true,
			pageSize: 5,
			pagination : true,//在DataGrid控件底部显示分页工具栏。
			pageList: [5],//选择一页显示多少数据   
			rownumbers : true,
			height:310,
			scrollbarSize :0,
			url: '${rc.getContextPath()}/zhsq/ypms/mscase/listDataHandleHis.jhtml',
			//queryParams: $('#searchFormForCallIn').serializeJson(),
			queryParams: {caseId:caseId},
			columns: [[
				//{field:'ck', align:'center', width:100,checkbox:true},
				{field:'rdName', title:'办理单位', align:'center', width:100},
				{field:'createName', title:'办理人', align:'center', width:100},
				{field:'createTimeStr', title:'办理时间', align:'center', width:100},
				{field:'handleProcess', title:'处理过程', align:'center', width:100,
				formatter:function(value,rec,index){
					if(value != null ){
						var length = value.length;
						if (length > 15) {
							value = value.substring(0, 15)+"...";
						}
						var f = '<a class="" href="###" title="'+ rec.handleProcess +'" >'+ value +'</a>';
						return f;
					}
				}},
				{field:'handleResult', title:'处理结果', align:'center', width:100,
				formatter:function(value,rec,index){
					if(value != null ){
						var length = value.length;
						if (length > 15) {
							value = value.substring(0, 15)+"...";
						}
						var f = '<a class="" href="###" title="'+ rec.handleResult +'" >'+ value +'</a>';
						return f;
					}
				}}
			]],
			toolbar: '#jqueryToolbar',
			onLoadSuccess : function(data) {
			    listSuccess(data); //暂无数据提示
				if(data.total == 0){
					$('.datagrid-body').eq(3).append('<div class="nodata"></div>');
				}
				
				
			},
			
			onLoadError : function() {
			  listError();
			}
		});
	}
	
	//回退事件
	function backToDept(){
	
		var	caseId = $("#caseId").val();
		var rdIdStr = "";
		var rows = $('#handleHis').datagrid('getSelections');
		if (rows.length == 0) {
			$.messager.alert('提示', '请选择回退的记录!', 'warning');
		} else {
			for (var i=0;i<rows.length;i++){
				var tempObj = rows[i];//联动单位数据
				rdIdStr += tempObj.rdId + ",";
			}
		
			var url = '${rc.getContextPath()}/zhsq/ypms/mscase/toRejectContent.jhtml?caseId=' + caseId + '&chain=02&rdIdStr=' + rdIdStr;
			showMaxJqueryWindow('原因描述', url, 400, 265);
		}
	
	}
	
	//根据重复案件号获取案件描述
	function getCaseInfoByParams(){
		var caseNo = $("#relaCaseNo").val();
		$.ajax({
			type: 'POST',
			url: '${rc.getContextPath()}/zhsq/ypms/mscase/getCaseInfoByParams.json',
			data: {caseNo:caseNo},
			dataType: 'json',
			success: function(data) {
				if (data != null){
					var msCase = data.msCase;
					if (msCase != null ){
						var caseDesc = msCase.caseDesc;
						$("#caseDesc").html(caseDesc);
					}else{
						$("#caseDesc").html("");
					}
				}else{
					$("#caseDesc").html("");
				}
			},
			error: function(data) {
				$.messager.alert('错误', '连接超时！', 'error');
			},
			complete : function() {
				modleclose(); //关闭遮罩层
			}
		});
	}
	
	//添加联动单位，专业化队伍
	function toAddDept(){
		var url = '${rc.getContextPath()}/zhsq/ldOrg/indexList.jhtml';
		showMaxJqueryWindow('联动单位列表', url, 600, 400);
	}
	
	
	//添加联动单位数据,数据回填
	function addDataDept(data){
		debugger
		//把已有的ldid数组ldIdArr转成Str，再把data的所有ldid取出来，逐一比较，重复的，data删掉该数据
		removalData(data);
	
		var htmlLinkageTr = "";
		var htmlProfessionalTr = "";
		var hasLdId = []; //已有的联动单位id，用于比较是否已添加过了
		
		for (var i=0;i<data.length;i++){
			var tempObj = data[i];//联动单位数据
			ldIdArr.push(tempObj.ldId);
			
			//对数据进行联动队伍和专业化队伍的分类拼装
			if (tempObj.ldType == '0') {//联动队伍数据
				var ldId = tempObj.ldId; //联动队伍id
				var ldName = tempObj.ldName;//联动队伍名称
				var leaderText = "";
				var leaderTel = "";
				var contacterText = "";
				var contacterTel = "";
				
				var abList = data[i].addressBookList;//联动队伍通讯录
				if(abList.length > 0){
					for (var j=0;j<abList.length;j++){
						var ab = abList[j];
						if(ab.abRole == '0'){//0分管领导 
							leaderText = ab.abName + "("+ ab.abMobile + ")" ;
							leaderTel = ab.abMobile;
						}else if (ab.abRole == '1'){//1联络员
							contacterText = ab.abName + "("+ ab.abMobile + ")" ;	
							contacterTel = ab.abMobile;
						}
					}
				}
				
				//拼装页面行元素
				htmlLinkageTr += 
				'<tr class="linkageTeamTr" index="' + ldId + '">'+
					'<td style="padding-left:3px;" >'+	
						'<a href="#" class="NorToolBtn DelBtn" onclick="delTr(this);" data-ldId="' + ldId + '" >删除</a>'+
					'</td>'+
					'<td class="textTd">'+
						'<span class="FontDarkBlue" id="ldId_' + ldId + '" data-ldId="'+ ldId +'" data-ldName="'+ ldName +'" data-msg="" >' + ldName + '</span>'+
					'</td>'+
					'<td class="textTd" >';
						
					if(leaderTel != ""  ){
    					htmlLinkageTr += '<span class="FontDarkBlue" id="contacter_' + ldId + '" data-tel="' + contacterTel + '" >' + contacterText + '&nbsp;<i class="tel_icon" onclick="callOut(this)"></i></span>';
					}else {
						htmlLinkageTr += '<span class="FontDarkBlue" id="contacter_' + ldId + '" data-tel="' + contacterTel + '" >' + contacterText + '</span>';
					}
						
					htmlLinkageTr += '</td>'+
					'<td class="textTd" >';
					
					if(leaderTel != ""  ){
    					htmlLinkageTr += '<span class="FontDarkBlue" id="leader_' + ldId + '" data-tel="' + leaderTel + '">' + leaderText + '&nbsp;<i class="tel_icon" onclick="callOut(this)"></i></span>';
					}else {
						htmlLinkageTr += '<span class="FontDarkBlue" id="leader_' + ldId + '" data-tel="' + leaderTel + '">' + leaderText + '</span>';
					}
					
					htmlLinkageTr += 
					'</td>'+
					'<td class="textTd" >'+
						'<div class="Check_Radio" style="width:100%" >'+
							'<label class="radio-wrapper" onchange="setDataMsgTab(this)" >'+
								'<input type="checkbox"  name="publishPoin_' + ldId + '" data-ldId="'+ldId+'" value="01"  ><span class="FontDarkBlue">联络员</sapn>'+
							'</label>'+
							'&nbsp;'+
							'<label class="radio-wrapper" onchange="setDataMsgTab(this)" >'+
								'<input type="checkbox" name="publishPoin_' + ldId + '" data-ldId="'+ldId+'" value="02"  ><span class="FontDarkBlue">分管领导</sapn>'+
							'</label>'+
						'</div >'+
					'</td>'+
				'</tr>';
				
				
				
			} else if (tempObj.ldType == '1') {//专业化队伍数据
				var ldId = tempObj.ldId; //专业化队伍id
				var ldName = tempObj.ldName;//专业化队伍名称
				var ldItem = tempObj.ldItem;//服务项目编码
				//var ldItemName = tempObj.ldItemName;//服务项目名称
				var contacterTel = "";
				var contacterText = "";
				
				var abList = data[i].addressBookList;//专业化队伍通讯录
				if(abList.length > 0){
					for (var j=0;j<abList.length;j++){
						var ab = abList[j];
						if(ab.abRole == '2'){//2专业化队伍业务热线
							contacterText = ab.abName + "("+ ab.abMobile + ")" ;	
							contacterTel = ab.abMobile;
						}
					}
				}
				
				htmlProfessionalTr +=
					'<tr class="professionalListTr" index="' + ldId + '">'+
						'<td style="padding-left: 3px;">'+
							'<a href="#" class="NorToolBtn DelBtn" onclick="delTr(this);" data-ldId="' + ldId + '">删除</a>'+
						'</td>'+
	
						'<td class="textTd">'+
							'<span class="FontDarkBlue" id="ldId_p_' + ldId + '" data-ldId="' + ldId + '" data-ldName="' + ldName + '">' + ldName + '</span>'+
						'</td>'+
						'<td class="textTd" >';
						
							if(contacterTel != ""  ){
		    					htmlProfessionalTr += '<span class="FontDarkBlue" id="tel_p_' + ldId + '" data-tel="' + contacterTel + '" >' + contacterText + '&nbsp;<i class="tel_icon" onclick="callOut(this)"></i></span>';
							}else {
								htmlProfessionalTr += '<span class="FontDarkBlue" id="tel_p_' + ldId + '" data-tel="' + contacterTel + '" >' + contacterText + '</span>';
							}
							
						htmlProfessionalTr += 	
						'</td>'+
						
						'<td class="textTd" >'+
	    					'<span class="FontDarkBlue" id="ldItem_' + ldId + '" >' + ldItem + '</span>'+
						'</td>'+
					'</tr>';
				
			}
		
		}
		$("#linkageTeam").append(htmlLinkageTr);
		$("#professionalTeam").append(htmlProfessionalTr);
		
	}
	
	//删除重复数据
	function removalData(data){
		//ldIdArr数组格式化成用于比较使用的Str
		var tempArr = [];
		for (var i=0;i<ldIdArr.length;i++){
			tempArr.push("#"+ldIdArr[i]+"#");
		}
		var ldIdArrStr = tempArr.join(",");
	
		for (var i=0;i<data.length;i++){
			var ldIdStr="#"+data[i].ldId+"#";
	        //在原来的数据里
	        if(ldIdArrStr.indexOf(ldIdStr) >= 0){
	            data.splice(i, 1);  
	            i--;     	        
	        } 
		}
		
		
	}
	
	//联动单位，专业化队伍删除行
	function delTr(objTr){
		 $.messager.confirm('提示', '您确定删除选中的信息吗?', function(r) {
		 	if(r){
		 		$(objTr).parent().parent().remove();
		 		var ldId = $(objTr).attr("data-ldId");
		 		//removeArrVal(ldIdArr,ldId);
		 		ldIdArr.remove(ldId)
		 	}
		 });
	}
	
	//点击电话图标调用话务接口拨打电话
	function callOut(currObj){
		//var curThis = $(currObj).children("i").eq(0);
		var telNum = $(currObj).parent().attr("data-tel");
		alert(telNum);
	}
	
	//勾选短信发送人员时，触发的事件，给自己这一行的联动单位名称列中的data-msg赋值，方便后面拼装json数据使用
	function setDataMsgTab(currObj){
		var val = $(currObj).children("input").val();
		var name = $(currObj).children("input").attr("name");
		var checkStatus = $(currObj).children("input").attr("checked");
		var data_ldId = $(currObj).children("input").attr("data-ldId");
		//console.log(val+","+name+","+checkStatus+","+data_ldId);
		
		var name = "publishPoin_" +data_ldId;//当前checkbox的name
		
		var chk_value =""; //被选中的value
		$('input[name='+name+']:checked').each(function(){ 
			chk_value += $(this).val(); 
		}); 
		
		//给data-msg赋值
		if(chk_value == "01"){
			$("#ldId_"+data_ldId).attr("data-msg","01");
		}else if (chk_value == "02"){
			$("#ldId_"+data_ldId).attr("data-msg","02");
		}else if (chk_value == "0102"){
			$("#ldId_"+data_ldId).attr("data-msg","12");
		}else if (chk_value == ""){
			$("#ldId_"+data_ldId).attr("data-msg","");
		}
		
	}
	
	
	//查看知识库
	function showKnowledge(){
		
	}
	
	//查看驳回信息
	function showRejectInfo(){
		var	caseId = $("#caseId").val();
		var listType = "reject";
		var url = '${rc.getContextPath()}/zhsq/ypms/mscase/showCaseHandlerInfo.jhtml?caseId=' + caseId + '&listType=' + listType;
		showMaxJqueryWindow('驳回信息', url, 800, 400);
	}
	
	
	
	//保存
	function save(updateType) {
		$("#updateType").val(updateType);
	
		var ldIdArrStr = ldIdArr.join(",");
		$("#ldIdArrStr").val(ldIdArrStr);
	
		var handleDeptName = "";
		$(".linkageTeamTr td span").each(function(){
			if(typeof($(this).attr("data-ldId"))!="undefined"){
    			handleDeptName += $(this).attr("data-ldname") + ",";
    		}
    	});
		$("#handleDeptName").val(handleDeptName.substring(0,handleDeptName.length-1));
		
		//获取派发单位json数据
		getRecevieDeptJson();

		modleopen(); //打开遮罩层
		$.ajax({
			type: 'POST',
			url: '${rc.getContextPath()}/zhsq/ypms/mscase/save.json',
			data: $('#submitForm').serializeArray(),
			dataType: 'json',
			success: function(data) {
				if (data.result == 'fail') {
					$.messager.alert('错误', '保存失败！', 'error');
				} else {
					$.messager.alert('提示', '保存成功！', 'info', function() {
						parent.closeMaxJqueryWindow();
					});
					parent.searchData();
				}
			},
			error: function(data) {
				$.messager.alert('错误', '连接超时！', 'error');
			},
			complete : function() {
				modleclose(); //关闭遮罩层
			}
		});
		
	}
	
	//拼装派发单位的json数据
	function getRecevieDeptJson(){
		var recevieDeptInfo = [];
		$(".linkageTeamTr td span[data-ldName]").each(function(){
    		var j = {};
			if(typeof($(this).attr("data-ldId"))!="undefined"){
    			j.ldorgId = $(this).attr("data-ldId");
    		}
    		if(typeof($(this).attr("data-msg"))!="undefined"){
    			j.msgSendPepo = $(this).attr("data-msg");
    		}
    		j.handleStatus = "";
    		recevieDeptInfo.push(j);
    	});
		
		$(".professionalListTr td span[data-ldName]").each(function(){
    		var j = {};
			if(typeof($(this).attr("data-ldId"))!="undefined"){
    			j.ldorgId = $(this).attr("data-ldId");
    		}else{
    			j.ldorgId = "";
    		}
    		if(typeof($(this).attr("data-msg"))!="undefined"){
    			j.msgSendPepo = $(this).attr("data-msg");
    		}else{
    			j.msgSendPepo = "";
    		}
    		j.handleStatus = "";
    		recevieDeptInfo.push(j);
    	});
		
		
		var recevieDeptInfoStr = JSON.stringify(recevieDeptInfo);
		$("#recevieDeptInfoStr").val(recevieDeptInfoStr);
	}
	
	
	
	//取消
	function cancel() {
		parent.closeMaxJqueryWindow();
	}
	
	
	//01办理单位驳回，02中心回退，
	function reject(chain) {//不同环节的回退，对应的是操作员的不同操作内容
		var	caseId = $("#caseId").val();
		var url = '${rc.getContextPath()}/zhsq/ypms/mscase/toRejectContent.jhtml?caseId=' + caseId + '&chain=' + chain;
		showMaxJqueryWindow('原因描述', url, 400, 265);
	}
	
	//点击回访按钮，打开回访情况，满意度，备注，归档按钮
	function showEle(){
		$('#returnRusult').attr("disabled",false); 
		$('#remark').attr("disabled",false); 
		$("input[name='satisfaction']").attr("disabled",false);
		
		$("#archiveBtn").css("display","block");
		$("#backVisitBtn").css("display","none");
	}
	
</script>
</html>

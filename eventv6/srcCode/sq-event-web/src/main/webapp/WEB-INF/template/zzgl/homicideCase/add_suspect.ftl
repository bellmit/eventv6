<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>命案防控-嫌疑人-新增/编辑</title>
	<#include "/component/standard_common_files-1.1.ftl" />
	<#--<人员选择控件-->
	<script type="text/javascript" src="${COMPONENTS_URL}/js/rs/jquery.baseCombo.js"></script>
	<script type="text/javascript" src="${COMPONENTS_URL}/js/rs/residentSelector.js"></script>
    <script type="text/javascript" src="${GEO_DOMAIN}/js/components/geoAddressPlugin/jquery.anole.address.js"></script>
	<style>
		.Asterik{color:#f00;}
		.LabName span{padding-right:0px;}
		.inp140{width:140px !important;}
		.inp132{width:110px !important;}
		.secondComWidth{width:132px;}
		.thirdComWidth{width:135px;}<!--第三列宽度-->
		
	</style>
</head>
<body>
	<#include "/component/ComboBox.ftl" />
	<#include "/component/FieldCfg.ftl" />
	
	<form id="tableForm" name="tableForm" action="" method="post">
		<input type="hidden" id="hashId" name="hashId" value="<#if people.hashId??>${people.hashId}</#if>"/>
		<input type="hidden" name="bizType" value="${people.bizType!}" />
		<input type="hidden" name="hashBizId" value="<#if people.hashBizId??>${people.hashBizId}</#if>" />
		<input type="hidden" id="ciRsId" name="ciRsId" value="<#if people.ciRsId??>${people.ciRsId}</#if>" />
		
		<div id="content-d" class="MC_con content light">
			<div id="norFormDiv" class="NorForm">
				<div>
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td class="LeftTd" style="width: 1px;">
								<label class="LabName"><span><label class="Asterik">*</label>姓名：</span></label>
								<input type="text" class="inp1 easyui-validatebox" style="height:28px;" data-options="{required:true,validType:'maxLength[20]', tipPosition:'bottom'}" name="name" id="name" value="${people.name!''}" />
							</td>
							<td>
					    		<label class="LabName"><span><label class="Asterik">*</label>证件类型：</span></label>
								<input type="hidden" id="cardType" name="cardType" value="${people.cardType!}" />
								<input type="text" class="inp1 secondComWidth easyui-validatebox" data-options="required:true,tipPosition:'bottom'" id="cardTypeName" value="${people.cardTypeName!}"/>
							</td>
							<td class="LeftTd">
								<label class="LabName"><span><label class="Asterik">*</label>证件号码：</span></label>
								<input type="text" class="inp1 secondComWidth  easyui-validatebox" data-options="required:true,tipPosition:'bottom',validType:['maxLength[24]','characterCheck']" name="idCard" id="idCard" value="${people.idCard!''}" onblur="checkCardType();" />
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>曾用名：</span></label>
								<input type="text" class="inp1 secondComWidth easyui-validatebox" data-options="tipPosition:'bottom',validType:['maxLength[80]','characterCheck']" name="usedName" id="usedName" value="${people.usedName!''}" />
							</td>
							<td>
					    		<label class="LabName"><span><label class="Asterik">*</label>性别：</span></label>
								<input type="hidden" id="sex" name="sex" value="${people.sex!}" />
								<input type="text" class="inp1 secondComWidth easyui-validatebox" data-options="required:true,tipPosition:'bottom'" id="sexName" value="${people.sexName!}" />
							</td>
							<td class="LeftTd">
								<label class="LabName"><span><label class="Asterik">*</label>出生日期：</span></label>
								<input type="text" class="secondComWidth  easyui-datebox" style="height: 28px;width:140px;" data-options="required:true,tipPosition:'bottom', onChange:_checkBirthay" name="birthdayStr" id="birthdayStr" value="${people.birthdayStr!''}" editable="false" />
							</td>
						</tr>
						<tr>
							<td>
					    		<label class="LabName"><span>国籍：</span></label>
								<input type="hidden" id="nationality" name="nationality" value="${people.nationality!}" />
								<input type="text" class="inp1 secondComWidth inp132" id="nationalityName" value="${people.nationalityName!}" />
							</td>
							<td>
					    		<label class="LabName"><span>民族：</span></label>
								<input type="hidden" id="nation" name="nation" value="${people.nation!}" />
								<input type="text" class="inp1 secondComWidth" id="nationName" value="${people.nationName!}" />
							</td>
							<td>
					    		<label class="LabName"><span>学历：</span></label>
								<input type="hidden" id="edu" name="edu" value="${people.edu!}" />
								<input type="text" class="inp1 secondComWidth " id="eduName" value="${people.eduName!}" />
							</td>
						</tr>
						<tr>
							<td>
					    		<label class="LabName"><span>婚姻状况：</span></label>
								<input type="hidden" id="marriage" name="marriage" value="${people.marriage!}" />
								<input type="text" class="inp1 secondComWidth inp132" id="marriageName" value="${people.marriageName!}" />
							</td>
							<td>
					    		<label class="LabName"><span>政治面貌：</span></label>
								<input type="hidden" id="politics" name="politics" value="${people.politics!}" />
								<input type="text" class="inp1 secondComWidth secondComWidth" id="politicsName" value="${people.politicsName!}" />
							</td>
							<td>
					    		<label class="LabName"><span>籍贯：</span></label>
					    		<input type="hidden" id="birthPlace" name="birthPlace" value="${people.birthPlace!}" />
					    		<input type="text" id="birthPlaceName" class="inp1 secondComWidth " style="width:130px;" readonly="readonly"/>
							</td>
						</tr>
						<tr>
							<td>
					    		<label class="LabName"><span>宗教信仰：</span></label>
								<input type="hidden" id="religion" name="religion" value="${people.religion!}" />
								<input type="text" class="inp1 secondComWidth inp132" id="religionName" value="${people.religionName!}" />
							</td>
							<td>
					    		<label class="LabName"><span>职业类别：</span></label>
								<input type="hidden" id="professionType" name="professionType" value="${people.professionType!}" />
								<input type="text" class="inp1 secondComWidth " id="professionTypeName" value="${people.professionTypeName!}" />
							</td>
							<td class="LeftTd">
								<label class="LabName"><span>职业：</span></label>
								<input type="text" class="inp1 secondComWidth  easyui-validatebox" data-options="tipPosition:'bottom',validType:['maxLength[30]','characterCheck']" name="profession" id="profession" value="${people.profession!''}" />
							</td>
						</tr>
						<tr>
							<td class="LeftTd" colspan="3">
								<label class="LabName"><span>服务处所：</span></label>
								<input type="text" class="inp1 easyui-validatebox" style="width:804px;" data-options="tipPosition:'bottom',validType:['maxLength[100]','characterCheck']" name="workUnit" id="workUnit" value="${people.workUnit!''}" />
							</td>
						</tr>
						<tr>
							<td class="LeftTd" style="width: 360px;">
								<label class="LabName"><span><#if people.bizType?? && people.bizType=='03'><label class="Asterik">*</label></#if>户籍地：</span></label>
								<input type="hidden" id="reOrgCode" name="reOrgCode" value="${people.reOrgCode!}" />
					    		<input type="text" id="reOrgCodeName" style="width:220px;" class="inp1 easyui-validatebox" data-options="<#if people.bizType?? && people.bizType=='03'>required:true,</#if> tipPosition:'bottom'" readonly="readonly"/>
							</td>
							<td class="LeftTd" colspan="2">
								<label class="LabName"><span>户籍地详址：</span></label>
								<input type="text" class="inp1 easyui-validatebox" style="width:435px;" data-options="tipPosition:'bottom',validType:['maxLength[80]','characterCheck']" name="registAddr" id="registAddr" value="${people.registAddr!''}" />
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>现住地：</span></label>
								<input type="hidden" id="liOrgCode" name="liOrgCode" value="${people.liOrgCode!}" />
					    		<input type="text" id="liOrgCodeName" style="width:220px;" class="inp1" readonly="readonly"/>
							</td>
							<td class="LeftTd" colspan="2">
								<label class="LabName"><span>现住详址：</span></label>
								<input type="text" class="inp1 easyui-validatebox" style="width:435px;" data-options="tipPosition:'bottom',validType:['maxLength[80]','characterCheck']" name="residenceAddr" id="residenceAddr" value="${people.residenceAddr!''}" />
							</td>
						</tr>
						<tr <#if people.bizType?? && people.bizType!='03'>class="hide"</#if>>
							<td>
								<label class="LabName"><span></span></label>
								<input type="hidden" id="isMentalDisease" name="isMentalDisease" value="${people.isMentalDisease!'0'}" />
								<div class="Check_Radio">
									<input type="checkbox" id="mentalDisease" hiddenid="isMentalDisease" onclick="isValidate(this);" <#if people.isMentalDisease?? && people.isMentalDisease=='1'>checked</#if> /><label for="mentalDisease" style="cursor:pointer;">是严重精神障碍患者</label>
								</div>
							</td>
							<td>
								<label class="LabName"><span></span></label>
								<input type="hidden" id="isMinors" name="isMinors" value="${people.isMinors!'0'}" />
								<div class="Check_Radio">
									<input type="checkbox" id="minors" hiddenid="isMinors" onclick="isValidate(this);" <#if people.isMinors?? && people.isMinors=='1'>checked</#if> /><label for="minors" style="cursor:pointer;">是未成年人</label>
								</div>
							</td>
							<td>
								<label class="LabName"><span></span></label>
								<input type="hidden" id="isTeenager" name="isTeenager" value="${people.isTeenager!'0'}" />
								<div class="Check_Radio">
									<input type="checkbox" id="teenager" hiddenid="isTeenager" onclick="isValidate(this);" <#if people.isTeenager?? && people.isTeenager=='1'>checked</#if> /><label for="teenager" style="cursor:pointer;">是青少年</label>
								</div>
							</td>
						</tr>
					</table>
				</div>
			</div>
		</div>
		
		<div class="BigTool">
        	<div class="BtnList">
        		<a href="###" onclick="tableSubmit();" class="BigNorToolBtn SaveBtn">保存</a>
				<a href="###" onclick="cancel();" class="BigNorToolBtn CancelBtn">取消</a>
            </div>
        </div>
	</form>
	
	<script type="text/javascript">
		var genderComboBox = null,
			cardTypeComboBox = null,
		    nationalityComboBox = null,
		    nationComboBox = null,
		    marriageComboBox = null,
		    politicsComboBox = null,
		    eduComboBox = null,
		    religionComboBox = null,
			singleChoose = null;
		var ID_CARD_TYPE = "1";
		
		$(function(){
	        layer.load(0);// 加载遮罩层
			$.excuteFieldCfg({
				moduleCode: "${fieldCfgModuleCode!}",// 必传，模块编码
				infoOrgCode: ""// 可选，不传取默认登录信息域编码
			}, function(isSuccess, msg) {// 回调函数，isSuccess：true成功/false失败
				if(isSuccess != true) {
					$.messager.alert('错误', msg, 'error');
				}
				
				initElement();
				layer.closeAll('loading'); // 关闭加载遮罩层
			}); 
	    });
	    
	    function initElement() {
	    	var options = { 
	            axis : "yx", 
	            theme : "minimal-dark" 
	        }; 
	        enableScrollBar('content-d',options);
			
			genderComboBox = AnoleApi.initListComboBox("sexName", "sex", "B153", null, ["${people.sex!}"]);
			cardTypeComboBox = AnoleApi.initListComboBox("cardTypeName", "cardType", "B010", checkCardType, ["${people.cardType!}"]);
			AnoleApi.initListComboBox("professionTypeName", "professionType", "B265", null, ["${people.professionType!}"], {
				ShowOptions : {
					EnableToolbar : true
				}
			});
			nationalityComboBox = AnoleApi.initListComboBox("nationalityName", "nationality", "B113", null, ["${people.nationality!}"], {
				ShowOptions : {
					EnableToolbar : true
				}
			});
			nationComboBox = AnoleApi.initListComboBox("nationName", "nation", "D177003", null, ["${people.nation!}"], {
				ShowOptions : {
					EnableToolbar : true
				}
			});
			marriageComboBox = AnoleApi.initListComboBox("marriageName", "marriage", "B151", null, ["${people.marriage!}"], {
				ShowOptions : {
					EnableToolbar : true
				}
			});
			politicsComboBox = AnoleApi.initListComboBox("politicsName", "politics", "B118", null, ["${people.politics!}"], {
				ShowOptions : {
					EnableToolbar : true
				}
			});
			eduComboBox = AnoleApi.initListComboBox("eduName", "edu", "B064", null, ["${people.edu!}"], {
				ShowOptions : {
					EnableToolbar : true
				}
			});
			religionComboBox = AnoleApi.initListComboBox("religionName", "religion", "B168", null, ["${people.religion!}"], {
				ShowOptions : {
					EnableToolbar : true
				}
			});

			//人员选择控件初始化
			singleChoose = $('#name').residentSelector({
				height : 30,
				width : 144,
				panelHeight:300,
				panelWidth:520,
				dataDomain : '${RS_DOMAIN}',
				type:'v6resident',
				srchType:'002',
				onClickRow:function(index,row){
					console.log(row);
					fillCiRsInfo(row);
				},
				onLoadSuccess:function(data){
					console.log(data);
				}
			});
			
			$("#birthPlaceName").anoleAddressRender({
				_startDivisionCode : '${people.birthPlace!}',  //起始行政区划code；
				_show_level : "3",            //配置页签显示层级，1,显示到省份;2,显示到市级;3,显示到区县;4,显示到街道;5,显示到社区; 如果参数不配置则默认显示到“社区”,如果参数配置的值超过"5",则显示到"社区"页签
				BackEvents : {
					OnSelected : function(api) {
						//地址名称
						$("#birthPlaceName").val(api.getAddressWithMark());
						//地址编码 
						$("#birthPlace").val(api.getAddressCode());	
					},
					OnCleared : function(api) {
						//清空按钮触发的事件											
						$("#birthPlace").val("");
					}
				}
			});
			
			$("#reOrgCodeName").anoleAddressRender({
				_startDivisionCode : '${people.reOrgCode!}',  //起始行政区划code；
				BackEvents : {
					OnSelected : function(api) {
						//地址名称
						$("#reOrgCodeName").val(api.getAddressWithMark());//搜索选择时需要使用
						//地址编码 
						$("#reOrgCode").val(api.getAddressCode());	
					},
					OnCleared : function(api) {
						//清空按钮触发的事件
						$("#reOrgCode").val("");
						$("#reOrgCodeName").val("");
					}
				}
			});
	
			$("#liOrgCodeName").anoleAddressRender({
				_startDivisionCode : '${people.liOrgCode!}',  //起始行政区划code；
				BackEvents : {
					OnSelected : function(api) {
						//地址名称
						$("#liOrgCodeName").val(api.getAddressWithMark());//搜索选择时需要使用
						//地址编码 
						$("#liOrgCode").val(api.getAddressCode());	
					},
					OnCleared : function(api) {
						//清空按钮触发的事件											
						$("#liOrgCode").val("");
						$("#liOrgCodeName").val("");
					}
				}
			});
			
			$("#norFormDiv").width($(document).width());
	    }
	    
		function tableSubmit(){
			var peopleName = $('#name').combobox('getText');
			if(isNotBlankString(peopleName)) {
				$("#name").val(peopleName);
				$("input[name='name']").val(peopleName);//将人员姓名设置为输入的值
			}
			
		    var isValid = $("#tableForm").form('validate');
		    
		    if(isValid) {
		    	isValid = checkCardType();
		    }
		    
			if(isValid){
				modleopen();
				
				$("#tableForm").attr("action","${rc.getContextPath()}/zhsq/relatedEvents/homicideCase/saveSuspect.jhtml");
	      	
			  	$("#tableForm").ajaxSubmit(function(data) {
			  		var msg = "信息保存成功！";
			  		
			  		if(data.result) {
	  					parent.reloadDataForSubPage(msg);
	  				} else {
	  					modleclose();
	  					
	  					if(data.msg) {
	  						$.messager.alert('错误', data.msg, 'error');
	  					} else {
	  						$.messager.alert('错误', '操作失败！', 'error');
	  					}
	  				}
				});
			}else{
				var birthday = $("#birthdayStr").datebox("getValue");
				var date = getNowDate();
				if(!$("#name").validatebox('isValid')){
					$.messager.alert('错误', '姓名校验不通过', 'error');
				}else if(!checkCardType() || !$("#idCard").validatebox('isValid')){
					if($("#cardType").val() != '1'){
					$.messager.alert('错误', '证件号码校验不通过', 'error');}
				}else if(birthday.length == 0 || birthday>=date ){
					$.messager.alert('错误', '出生日期校验不通过', 'error');
				}else if(!$("#reOrgCodeName").validatebox('isValid')){
					$.messager.alert('错误', '出户籍地校验不通过', 'error');
				}
			}
		}
		function getNowDate() {
			var date =new Date();
			var y = date.getFullYear();
			var m = date.getMonth() + 1;
			var d = date.getDate();
			return y + '-' + (m < 10 ? ('0' + m) : m) + '-' + (d < 10 ? ('0' + d) : d);
		}
		
		//人口信息回填
		function fillCiRsInfo(data){
			$("#name").val(data["name"]);//为了使必填验证通过
			$("input[name='name']").val(data["name"]);//姓名 人员选择器返回值会将ciRId填回到name
			$('#idCard').val(data["identityCard"]);//身份证
			cardTypeComboBox.setSelectedNodes([ID_CARD_TYPE]);//设置选择身份证
		}

		function checkCardType(cardType) {//检验证件类型
			var isValid = true;
			
			if(isBlankStringTrim(cardType)) {
				cardType = $("#cardType").val();
			}
			
			if(cardType && cardType == ID_CARD_TYPE) {//选择了身份证
				isValid = checkCardId();
			}
			
			return isValid;
		}
		
		function checkCardId() {//检验身份号码
			var idCard = $("#idCard").val();
			var isValid = true;
			
			if(isNotBlankStringTrim(idCard)) {
				if(checkIdCard(idCard)) {
					alterInfo(idCard);
				} else {
					isValid = false;
					$.messager.alert('警告','身份证号码不合法！','warning');
				}
			}
			
			return isValid;
		}
		
		function alterInfo(idCard) {//变更信息
			var birthDay = fetchBirthday(idCard);
			var gender = fetchGender(idCard);
			var genderCode = "M";
			var genderCodeBefore = $("#sex").val();
			
			if(isNotBlankStringTrim(birthDay)) {
				$("#birthdayStr").datebox("setValue", birthDay);//会触发datebox的onChange事件
			}
			
			if(gender == 0) {
				genderCode = "F";
			}
			
			if(genderCodeBefore != genderCode) {//防止性别初始化和证件类型变更导致的性别变更的重复修复
				genderComboBox.setSelectedNodes([genderCode]);
			}
			
			fetchRsInfo();
		}
		
		function _checkBirthay() {//依据出生日期获取周岁
			var birthday = $("#birthdayStr").datebox("getValue");
			var age = fetchAgeByBirthday(birthday);
			
			$("#minors").attr("checked", age >= 0 && age < 18);
			$("#teenager").attr("checked", age >= 6 && age < 25);
			isValidate($("#minors"));
			isValidate($("#teenager"));
		}
		
		function fetchRsInfo() {//判断是否是精神病患者
			var idCard = $("#idCard").val();
			
			if(isNotBlankStringTrim(idCard)) {
				$.ajax({
					type: "POST",
		    		url : '${rc.getContextPath()}/zhsq/relatedEvents/homicideCase/fetchRsInfo.jhtml',
					data: 'identityCard='+idCard,
					dataType:"json",
					success: function(data){
						if(data.ciRsTop) {
							var ciRsTop = data.ciRsTop,
								ciRsId = -1;
							
							if(ciRsTop.ciRsId) {
								ciRsId = ciRsTop.ciRsId;
							}
							
							if(ciRsId != $("#ciRsId").val()) {
								$("#ciRsId").val(ciRsId);
								
								if(ciRsTop.name && isBlankStringTrim($('#name').combobox('getText'))) {//姓名
									$("#name").val(ciRsTop.name);
									$('#name').combobox('setText', ciRsTop.name);
									$("input[name='name']").val(ciRsTop.name);
								}
								if(ciRsTop.residentNationality && isBlankStringTrim($("#nationality").val())) {//国籍
									nationalityComboBox.setSelectedNodes([ciRsTop.residentNationality]);
								}
								if(ciRsTop.ethnic && isBlankStringTrim($("#nation").val())) {//民族
									nationComboBox.setSelectedNodes([ciRsTop.ethnic]);
								}
								if(ciRsTop.marriage && isBlankStringTrim($("#marriage").val())) {//婚姻状况
									marriageComboBox.setSelectedNodes([ciRsTop.marriage]);
								}
								if(ciRsTop.residentPolitics && isBlankStringTrim($("#politics").val())) {//政治面貌
									politicsComboBox.setSelectedNodes([ciRsTop.residentPolitics]);
								}
								if(ciRsTop.education && isBlankStringTrim($("#edu").val())) {//学历
									eduComboBox.setSelectedNodes([ciRsTop.education]);
								}
								if(ciRsTop.religion && isBlankStringTrim($("#religion").val())) {//宗教信仰
									religionComboBox.setSelectedNodes([ciRsTop.religion]);
								}
								if(ciRsTop.career && isBlankStringTrim($("#profession").val())) {//职业
									$("#profession").val(ciRsTop.career);
								}
								if(ciRsTop.organization && isBlankStringTrim($("#workUnit").val())) {//服务处所
									$("#workUnit").val(ciRsTop.organization);
								}
								if(ciRsTop.residence && isBlankStringTrim($("#registAddr").val())) {//户籍地详址
									$("#registAddr").val(ciRsTop.residence);
								}
								if(ciRsTop.residenceAddr && isBlankStringTrim($("#residenceAddr").val())) {//现住详址
									$("#residenceAddr").val(ciRsTop.residenceAddr);
								}
							}
						}
						if(data.isMentalDisease) {
							$("#mentalDisease").attr("checked", data.isMentalDisease);
							isValidate($("#mentalDisease"));
						}
					},
					error:function(data){
						$.messager.alert('错误','连接错误！','error');
					}
		    	});
	    	}
		}
		
		function isValidate(obj) {//设置checkBox对应的属性值
			if(obj) {
				var isChecked = $(obj).is(":checked");
				var status = "0";
				
				if(isChecked) {
					status = "1";
				}
				
				$("#"+$(obj).attr("hiddenid")).val(status);
			}
		}
		
		function cancel(){
			parent.closeMaxJqueryWindow();
		}
	</script>
	
</body>
</html>

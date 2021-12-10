<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>人员配置信息-新增/编辑</title>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
		<link rel="stylesheet" type="text/css" href="${COMPONENTS_URL}/css/zzForm/component.css" />
		<#include "/component/standard_common_files-1.1.ftl" />
		<#include "/component/ComboBox.ftl" />
		<script type="text/javascript" src="${GEO_URL}/js/components/geoAddressPlugin/jquery.anole.address.js"></script>
		<script type="text/javascript" src="${COMPONENTS_URL}/js/zzForm/zz-form.js"></script>
		
		<style type="text/css">
			.singleCellInpClass{width: 285px;}
		</style>
	</head>
	<body>
		<form id="msgCCCfgForm" name="msgCCCfgForm" action="" method="post">
			<input type="hidden" name="cfgUUID" value="${cfgInfo.cfgUUID!}" />
			
			<div id="content-d" class="MC_con content light">
				<div id="msgCCCfgNorFormDiv" class="NorForm">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						
						<tr>
							<td class="LeftTd" width="420">
								<label class="LabName"><span><div class="Check_Radio" style="float: none; cursor: pointer;"><input type="radio" name="orgRadio" value="1" onclick="checkOrgRadio(this, 'orgChiefLevelName', 'orgCode')" <#if cfgInfo.orgCode??><#else>checked</#if>/>组织层级：</div></span></label>
								<input type="hidden" id="orgChiefLevel" name="orgChiefLevel" value="${cfgInfo.orgChiefLevel!}" />
								<input type="text" class="inp1 easyui-validatebox singleCellInpClass" data-options="required:true,tipPosition:'bottom'" id="orgChiefLevelName" value="" <#if cfgInfo.orgCode??>disabled</#if>/>
							</td>
							<td rowspan="13" class="RightTd">
								<p><b>使用说明：</b></p>
								<p><b>支持两种形式的配置：</b></p>
								<p><b>1、组织层级。</b>如：当前操作人员为街道层级，配送对象为社区人员，则使用组织层级配置。<br/>
								（1）以流程节点操作人员所属组织为基准，组织层级在其之下的使用组织层级配置；<br/>
								（2）如果需要调整基准组织，可设置“基准组织”属性进行变更；<br/>
								（3）如果需要获取职能部门，可通过专业编码设置，目前只支持配置一个专业编码；<br/>
								（4）向下查找时，建议使用组织层级配置。</p>
								<p><b>2、组织编码。</b>如：当前操作人员为街道层级，配送对象为县区人员，则使用组织编码配置，配置的组织编码为该街道对应的县区组织编码。<br/>
								（1）以流程节点操作人员所属组织为基准，组织层级在其之上的使用组织编码配置；<br/>
								（2）向上查找时，建议使用组织编码配置。</p>
								<p><b>3、起始节点。</b>对于没有指定流程起始节点名称的，将起始节点设置为<b class="FontRed">task</b>。<br/></p>
								<p><b>4、配置值。</b><br/>
								（1）配置类型为“用户”，则配置值为用户id；<br/>
								（2）配置类型为“组织”，则配置值为组织id；<br/>
								（3）配置类型为“角色”，则配置值为角色id；<br/>
								（4）配置类型为“职位”，则配置值为职位id。<br/>
								</p>
								<p><b>5、判重条件。</b><br/>
								（1）组织层级/组织编码 + 流程图名称 + 起始节点 + 目标节点 + 配送类型 + 配置类型 + 配置值<br/>
								</p>
				    		</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span><div class="Check_Radio" style="float: none; cursor: pointer;"><input type="radio" name="orgRadio" value="1" onclick="checkOrgRadio(this, 'orgCode', 'orgChiefLevelName', 'orgChiefLevel')" <#if cfgInfo.orgCode??>checked</#if>/>组织编码：</div></span></label>
								<input  id="orgCode" name="orgCode" type="text" class="inp1 easyui-validatebox singleCellInpClass" data-options="tipPosition:'bottom',validType:['maxLength[24]','characterCheck']" value="${cfgInfo.orgCode!}" <#if cfgInfo.orgChiefLevel??>disabled</#if> />
							</td>
						</tr>
						<tr class="orgChiefLevelTr">
							<td class="LeftTd">
								<label class="LabName"><span>基准组织：</span></label>
								<input  id="benchmarkOrgCode" name="benchmarkOrgCode" type="text" class="inp1 easyui-validatebox singleCellInpClass" data-options="tipPosition:'bottom',validType:['maxLength[30]','characterCheck']" value="${cfgInfo.benchmarkOrgCode!}" />
							</td>
						</tr>
						<tr class="orgChiefLevelTr">
							<td class="LeftTd">
								<label class="LabName"><span>专业编码：</span></label>
								<input  id="professionCode" name="professionCode" type="hidden" value="${cfgInfo.professionCode!}" />
								<input  id="professionName" type="text" class="inp1 easyui-validatebox singleCellInpClass" />
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span><label class="Asterik">*</label>流程图名称：</span></label>
								<input  id="workflowName" name="workflowName" type="text" class="inp1 easyui-validatebox singleCellInpClass" data-options="required:true,tipPosition:'bottom',validType:['maxLength[50]','characterCheck']" value="${cfgInfo.workflowName!}" />
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span><label class="Asterik">*</label>起始节点：</span></label>
								<input id="wfStartNodeName" name="wfStartNodeName" type="text" class="inp1 easyui-validatebox singleCellInpClass" data-options="required:true,tipPosition:'bottom',validType:['maxLength[30]','characterCheck']" value="${cfgInfo.wfStartNodeName!}" />
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span><label class="Asterik">*</label>目标节点：</span></label>
								<input  id="wfEndNodeName" name="wfEndNodeName" type="text" class="inp1 easyui-validatebox singleCellInpClass" data-options="required:true,tipPosition:'bottom',validType:['maxLength[30]','characterCheck']" value="${cfgInfo.wfEndNodeName!}" />
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span><label class="Asterik">*</label>配置类型：</span></label>
								<input type="hidden" id="cfgType" name="cfgType" value="${cfgInfo.cfgType!}" />
								<input type="text" class="inp1 easyui-validatebox singleCellInpClass" data-options="required:true,tipPosition:'bottom'" id="cfgTypeName" value="" />
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span><label class="Asterik">*</label>配置值：</span></label>
								<input id="cfgValue" name="cfgValue" type="text" class="inp1 easyui-validatebox singleCellInpClass" data-options="required:true,tipPosition:'bottom',validType:['maxLength[50]','characterCheck']" value="${cfgInfo.cfgValue!}" />
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span><label class="Asterik">*</label>配送类型：</span></label>
								<input type="hidden" id="ccType" name="ccType" value="${cfgInfo.ccType!}" />
								<input type="text" class="inp1 easyui-validatebox singleCellInpClass" data-options="required:true,tipPosition:'bottom'" id="ccTypeName" value="" />
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>启用：</span></label>
								<div class="zz-form" zz-form-filter="cfgStatusDiv">
									<input type="hidden" id="cfgStatus" name="cfgStatus" value="${cfgInfo.cfgStatus!'1'}" />
									<input type="checkbox" zz-form-filter="cfgStatusCheckBox" zz-text="是|否" <#if cfgInfo.cfgStatus?? && cfgInfo.cfgStatus=='0'><#else>checked</#if>/>
								</div>
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>备注：</span></label><textarea name="remark" id="remark" cols="" rows="" class="area1 easyui-validatebox singleCellInpClass" style="height:64px;resize: none;" data-options="tipPosition:'bottom',validType:['maxLength[300]','characterCheck']">${cfgInfo.remark!}</textarea>
							</td>
						</tr>
					</table>
				</div>
			</div>
			
			<div class="BigTool">
				<div class="BtnList">
					<a href="###" onclick="saveCfgInfo();" class="BigNorToolBtn SaveBtn">保存</a>
					<a href="###" onclick="closeWin();" class="BigNorToolBtn CancelBtn">取消</a>
				</div>
			</div>
		</form>
	</body>
	
	<script type="text/javascript">
		var _winHeight = 0, _winWidth = 0;
		
		$(function() {
			_winHeight = $(window).height();
			_winWidth = $(window).width();
			
			AnoleApi.initListComboBox("orgChiefLevelName", "orgChiefLevel", "E008", null, ["${cfgInfo.orgChiefLevel!}"]);
			AnoleApi.initListComboBox("cfgTypeName", "cfgType", null, null, ["${cfgInfo.cfgType!}"], {
				DataSrc : [{"name":"用户", "value":"1"},{"name":"组织", "value":"2"},{"name":"角色", "value":"3"},{"name":"职位", "value":"4"}]
			});
			AnoleApi.initListComboBox("ccTypeName", "ccType", null, null, ["${cfgInfo.ccType!}"], {
				DataSrc : [{"name":"分送", "value":"1"},{"name":"选送", "value":"2"},{"name":"主送", "value":"3"}]
			});
			AnoleApi.initListComboBox("professionName", "professionCode", null, null, ["${cfgInfo.professionCode!}"], {
				DataSrc: eval("(${professionDictJsonStr!})"),
				EnabledSearch : true,
				ShowOptions: {
					EnableToolbar : true
				}
			});
			
			zzForm.render('checkbox','cfgStatusDiv');
			zzForm.on('checkbox(cfgStatusCheckBox)', function (data) {
				if(data.checked) {
					 $('#cfgStatus').val('1');
				} else {
					$('#cfgStatus').val('0');
				}
			});
			
			$('#msgCCCfgNorFormDiv input[type=radio][name=orgRadio]:checked').eq(0).click();
			
			var options = {
				axis : "yx",
				theme : "minimal-dark" 
			};
			
			enableScrollBar('content-d',options);
		});
		
		function checkOrgRadio(obj, selectId, unSelectId, extraId) {
			$('#' + selectId).removeAttr('disabled');
			$('#' + selectId).validatebox({
				required: true
			});
			if(extraId) {
				$('#' + extraId).val('');
			}
			
			$('#' + unSelectId).val('');
			$('#' + unSelectId).attr('disabled', true);
			$('#' + unSelectId).validatebox({
				required: false
			});
			
			if(selectId == 'orgChiefLevelName') {
				$('#msgCCCfgNorFormDiv tr.orgChiefLevelTr').show();
			} else {
				$('#msgCCCfgNorFormDiv tr.orgChiefLevelTr').hide();
				$('#msgCCCfgNorFormDiv tr.orgChiefLevelTr input').val('');
			}
		}
		
		function saveCfgInfo() {
			var isValid =  $("#msgCCCfgForm").form('validate');
			
			if(isValid) {
				modleopen();
				
				$("#msgCCCfgForm").attr("action", "${rc.getContextPath()}/zhsq/reportMsgCCCfg/saveCfgInfo.jhtml");
				
				$("#msgCCCfgForm").ajaxSubmit(function(data) {
					modleclose();
					
					if(data.success && data.success == true) {
		  				parent.reloadDataForSubPage(data.tipMsg);
					} else {
						$.messager.alert('错误', data.tipMsg, 'error');
					}
				});
			}
		}
		
		function closeWin() {
			parent.closeMaxJqueryWindow();
		}
		
		$(window).resize(function() {
			var winHeight = $(window).height();
			var winWidth = $(window).width();
			
			if(winHeight != _winHeight || winWidth != _winWidth) {
				location.reload();
			}
		});
	</script>
</html>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" > 
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<title>企业信息-查看</title>
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/main_new.css" />
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-last/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-last/themes/icon.css">
<script src="${rc.getContextPath()}/js/jqueryeasyui-last/jquery.min.js" type="text/javascript"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-last/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-last/locale/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/ztree/zTreeStyle/zTreeStyle.css" />
<script type="text/javascript" src="${rc.getContextPath()}/js/ztree/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/zzgl_core.js"></script>
</head>
<body>
	<div id="infoTabs" class="easyui-tabs" fit="true" border="false" style="margin: 0; height:530px;">
		<div title="企业信息" id="editInfo" style="margin:0">
			<form id="tableForm" name="tableForm" method="post" enctype="multipart/form-data">
			 <input type="hidden" value="${record.cbiId}" id="cbiId" name="cbiId"/>
				<table width="100%" border="0" cellspacing="0" cellpadding="0" class="border-t">
				    <colgroup>
									<col width="10%"></col>
									<col width="16%"></col>
									<col width="10%"></col>
									<col width="22%"></col>
									<col width="10%"></col>
									<col width="22%"></col>
								</colgroup>
					<tr class="item">
						<td class="itemtit" >&nbsp;企业名称</td>
						<td class="border_b" colspan="3">&nbsp;
						     <#if record.corName??>${record.corName}</#if>
						</td>
						<td class="itemtit">&nbsp;企业类型</td>
						<td class="border_b" >&nbsp;
						      <#if corTypeDC??>
									<#list corTypeDC as l>
									     <#if record.corType??>
											<#if (l.COLUMN_VALUE==record.corType)>${l.COLUMN_VALUE_REMARK}</#if>
									    </#if>
									</#list>
							 </#if>
						   </td>
					</tr>
				<tr class="item">
						<td class="itemtit">&nbsp;法人代表</td>
						<td class="border_b">&nbsp;
						     <#if record.representativeName??>${record.representativeName}</#if>
						</td>
						<td class="itemtit">&nbsp;法人地址</td>
						<td class="border_b"  colspan="3">&nbsp;
						<input type="hidden"   name="addressCode"  />
							   <#if record.corAddr??>${record.corAddr}</#if>
							   <#if record.corAddr1??>${record.corAddr1}</#if>
						         
						</td>
					</tr>
					
					<tr class="item">
						<td class="itemtit">&nbsp;联系电话</td>
						<td class="border_b">&nbsp;
						    <#if record.telephone??>${record.telephone}</#if>
						</td>
						<td class="itemtit">&nbsp;经营或业务范围</td>
						<td class="border_b">&nbsp;
							   <#if record.busScope??>${record.busScope}</#if>
						</td>
						<td class="itemtit">&nbsp是否入盘</td>
						<td class="border_b" >&nbsp;
						         <#if record.intoTheTraySituation??>
							       <#if record.intoTheTraySituation=='1'>
							         是
							      <#else> 否
						         </#if>	  
						       <#else>
						            否
						       </#if>
						</td>
					</tr>
					
					<tr class="item">
						<td class="itemtit">&nbsp;成立日期</td>
						<td class="border_b">&nbsp;
						<#if record.establishDateStr??>${record.establishDateStr}</#if>
						</td>
						<td class="itemtit">&nbsp;注册日期</td>
						<td class="border_b">&nbsp;
							<#if record.registrationTimeStr??>${record.registrationTimeStr}</#if>
						</td>
						<td class="itemtit">&nbsp;注册资金<span style="color:red;"></span></td>						
						<td class="border_b">&nbsp;<#if record.registeredCapital??>${record.registeredCapital}</#if>万&nbsp;
						    		<#if registeredCurrencyDC??>
										<#list registeredCurrencyDC as l>
										<#if record.registeredCurrency??>
											<#if (l.COLUMN_VALUE==record.registeredCurrency)>${l.COLUMN_VALUE_REMARK}</#if>
										</#if>
										</#list>
								   </#if>
						</td>
					</tr>
					
					
					<tr class="item">
						<td class="itemtit">&nbsp;行业分类</td>
						<td class="border_b">&nbsp;
						      <#if categoryDC??>
									<#list categoryDC as l>
									<#if record.category??>
										<#if (l.COLUMN_VALUE==record.category)>${l.COLUMN_VALUE_REMARK}</#if>
									</#if>
									</#list>
							 </#if>
						</td>
						<td class="itemtit">&nbsp;经济类型</td>
						<td class="border_b" >&nbsp;
						      <#if economicTypeDC??>
									<#list economicTypeDC as l>
									<#if record.economicType??>
											 <#if (l.COLUMN_VALUE==record.economicType)>${l.COLUMN_VALUE_REMARK}</#if>
									</#if>
									</#list>
							 </#if>
						</td>
						<td class="itemtit">&nbsp;注册机构名称</td>
						<td class="border_b" >&nbsp;
						    <#if record.registrationOrgName??>${record.registrationOrgName}</#if>
						</td>
					</tr>
					
					
					<tr class="item">
						<td class="itemtit">&nbsp;注册号</td>
						 <td class="border_b">&nbsp;
						     <#if record.registrationNum??>${record.registrationNum}</#if>
						</td>
						<td class="itemtit">&nbsp;组织机构代码</td>
						<td class="border_b">&nbsp;
						   <#if record.orgCode??>${record.orgCode}</#if>
						</td>
						<td class="itemtit">&nbsp;行政区划<span style="color:red;"></span></td>						
						<td class="border_b">&nbsp;
						     <#if record.administrativeDivision??>${record.administrativeDivision}</#if>
						</td>
					</tr>
					
					
					<tr class="item">
						<td class="itemtit">&nbsp;邮政编码</td>
						 <td class="border_b">&nbsp;
						     <#if record.zipCode??>${record.zipCode}</#if>
						</td>
						<td class="itemtit">&nbsp;营业期限起</td>
						<td class="border_b">&nbsp;
						  <#if record.startDoBusiDateStr??>${record.startDoBusiDateStr}</#if>
						</td>
						<td class="itemtit">&nbsp;营业期限止<span style="color:red;"></span></td>						
						<td class="border_b">&nbsp;
						    <#if record.endDoBusiDateStr??>${record.endDoBusiDateStr}</#if>
						</td>
					</tr>
					
					
					<tr class="item">
						<td class="itemtit">&nbsp;年检时间</td>
						 <td class="border_b">&nbsp;
						   <#if record.yearCheckDateStr??>${record.yearCheckDateStr}</#if>
						</td>
						<td class="itemtit">&nbsp;网址</td>
						<td class="border_b" colspan="3">&nbsp;
						    <#if record.webSite??>${record.webSite}</#if>
						</td>
					</tr>
					
					<tr class="item">
						<td class="itemtit">&nbsp;备注</td>
						 <td class="border_b" colspan="5">&nbsp;
						    <#if record.corRemark??>${record.corRemark}</#if>
						</td>
					</tr>
				</table>
				
				 <!-- 法人扩展信息开始 -->
				<table width="100%" border="0" cellspacing="0" cellpadding="0" class="border-t">
				   <tr class="item">
					<td class="itemtit" colspan="6"  style="text-align:left"> &nbsp; &nbsp;&nbsp;<font style="color: red;">扩展信息</font></td>
					</tr>
					</table>
					<table width="100%" border="0" cellspacing="0" cellpadding="0" class="border-t">
					<colgroup>
									<col width="10%"></col>
									<col width="16%"></col>
									<col width="10%"></col>
									<col width="22%"></col>
									<col width="10%"></col>
									<col width="22%"></col>
								</colgroup>
					<tr class="item">
						<td class="itemtit">&nbsp;世界500强</td>
						 <td class="border_b">&nbsp;
					             <#if record.corpExtInfo.isFortuneGlobal500??>
							      <#if record.corpExtInfo.isFortuneGlobal500=='1'>
							           是
							          <#else>
						    		   否
						    	  </#if>
						        </#if>
						</td>
						<td class="itemtit">&nbsp;中国100强</td>
						<td class="border_b">&nbsp;
					             <#if record.corpExtInfo.isfortuneChina100??>
							      <#if record.corpExtInfo.isfortuneChina100=='1'>
							          是
							          <#else>
						    		   否
						    	  </#if>
						        </#if>
						</td>
						<td class="itemtit">&nbsp;是否统计大户<span style="color:red;"></span></td>						
						<td class="border_b">&nbsp;
					             <#if record.corpExtInfo.isStatisticsMajor??>
							      <#if record.corpExtInfo.isStatisticsMajor=='1'>
							         是
							          <#else>
						    		   否
						    	  </#if>
						        </#if>
						</td>
					</tr>
					
					<tr class="item">
						<td class="itemtit">&nbsp;入驻时间</td>
						 <td class="border_b">&nbsp;
						   <#if record.corpExtInfo.settledDateStr??>${record.corpExtInfo.settledDateStr}</#if>
						</td>
						<td class="itemtit">&nbsp;办公面积(m²)</td>
						<td class="border_b">&nbsp;
						    <#if record.corpExtInfo.spaceArea??>${record.corpExtInfo.spaceArea}</#if>
						</td>
						<td class="itemtit">&nbsp;租金(元/m²*月)<span style="color:red;"></span></td>						
						<td class="border_b">&nbsp;
						     <#if record.corpExtInfo.rentMoney??>${record.corpExtInfo.rentMoney}</#if>
						</td>
					</tr>
					
					<tr class="item">
						<td class="itemtit">&nbsp;员工数</td>
						 <td class="border_b">&nbsp;
						     <#if record.corpExtInfo.employeeNum??>${record.corpExtInfo.employeeNum}</#if>
						</td>
						<td class="itemtit">上年营业金额(万/年)</td>
						<td class="border_b"  colspan="3">&nbsp;
						   <#if record.corpExtInfo.lastYearTurnover??>${record.corpExtInfo.lastYearTurnover}</#if>
						</td>
					</tr>
					
					<tr class="item">
						<td class="itemtit">&nbsp;联系人</td>
						 <td class="border_b">&nbsp;
						     <#if record.corpExtInfo.contactPeople??>${record.corpExtInfo.contactPeople}</#if>
						</td>
						<td class="itemtit">电话号码</td>
						<td class="border_b"  colspan="3">&nbsp;
						    <#if record.corpExtInfo.contacePhone??>${record.corpExtInfo.contacePhone}</#if>
						</td>
					</tr>
				</table>
				<!-- 法人扩展信息结束 -->
				
				<!-- 企业国税信息开始 -->
				<table width="100%" border="0" cellspacing="0" cellpadding="0" class="border-t">
				    <tr class="item">
					    <td class="itemtit" colspan="6"  style="text-align:left;">
					      &nbsp; &nbsp;&nbsp;<font style="color: red;">国税信息</font>
					    </td>
					</tr>
					</table>
					
					<table  id="centralTax"   style="display:block;"  width="100%" border="0" cellspacing="0" cellpadding="0" class="border-t">
					 <colgroup>
									<col width="10%"></col>
									<col width="16%"></col>
									<col width="10%"></col>
									<col width="22%"></col>
									<col width="10%"></col>
									<col width="22%"></col>
								</colgroup>
					<tr class="item">
						<td class="itemtit">&nbsp;税务登记号</td>
						 <td class="border_b">&nbsp;
						     <#if record.stateCorpTaxInfo.taxRegNo??>${record.stateCorpTaxInfo.taxRegNo}</#if>
						</td>
						<td class="itemtit">&nbsp;纳税登记机关</td>
						<td class="border_b">&nbsp;
						   <#if record.stateCorpTaxInfo.adminDivision??>${record.stateCorpTaxInfo.adminDivision}</#if>
						</td>
						<td class="itemtit">&nbsp;纳税管征机关编码</span></td>						
						<td class="border_b">&nbsp;
						     <#if stateAdminDivisionType??>
							          <#if stateAdminDivisionType=='01'>
									   市直属
									  </#if>
									   <#if stateAdminDivisionType=='02'>
									   区国税
									  </#if>
									   <#if stateAdminDivisionType=='03'>
									   其它
									  </#if>
					        </#if>
						</td>
					</tr>
					
					<tr class="item">
						<td class="itemtit">&nbsp;上年纳税总额(万/年)</td>
						 <td class="border_b">&nbsp;
						    <#if record.stateCorpTaxInfo.lastYearTaxMoney??>${record.stateCorpTaxInfo.lastYearTaxMoney}</#if>
						</td>
						<td class="itemtit">&nbsp;入盘情况地址</td>
						<td class="border_b">&nbsp;
						    <#if record.stateCorpTaxInfo.adminDivisionInfo??>${record.stateCorpTaxInfo.adminDivisionInfo}</#if>
						</td>
						<td class="itemtit">&nbsp;是否可变更</span></td>						
						<td class="border_b">&nbsp;
					          <#if record.stateCorpTaxInfo.isCentral??>
							     <#if record.stateCorpTaxInfo.isCentral=='1'>
							        是
							          <#else>
							          否
						    	</#if>
						        </#if>
						</td>
					</tr>
					
					<tr class="item">
						<td class="itemtit">&nbsp;是否纳税大户</td>
						 <td class="border_b">&nbsp;
									 <#if record.stateCorpTaxInfo.isMajor??>
							     <#if record.stateCorpTaxInfo.isMajor=='1'>
							        是
							          <#else>
							          否
						    	</#if>
						        </#if>
						</td>
						<td class="itemtit">&nbsp;纳税登记地址</td>
						<td class="border_b"  colspan="3">&nbsp;
						    <#if record.stateCorpTaxInfo.taxPlace??>${record.stateCorpTaxInfo.taxPlace}</#if>
						</td>
					</tr>
				</table>
				<!-- 企业国税信息结束 -->
				
				<!-- 企业地税信息开始 -->
				<table width="100%" border="0" cellspacing="0" cellpadding="0" class="border-t">
				    <tr class="item">
					    <td class="itemtit" colspan="6"  style="text-align:left;">
					          &nbsp; &nbsp;&nbsp; <font style="color: red;">地税信息</font>
					    </td>
					</tr>
				</table>
				
				<table id="localTax"  style="display:block;"  width="100%" border="0" cellspacing="0" cellpadding="0" class="border-t">
				  <colgroup>
									<col width="10%"></col>
									<col width="16%"></col>
									<col width="10%"></col>
									<col width="22%"></col>
									<col width="10%"></col>
									<col width="22%"></col>
								</colgroup>
					<tr class="item">
						<td class="itemtit">&nbsp;税务登记号</td>
						 <td class="border_b">&nbsp;
						     <#if record.areaCorpTaxInfo.taxRegNo??>${record.areaCorpTaxInfo.taxRegNo}</#if>
						</td>
						<td class="itemtit">&nbsp;纳税登记机关</td>
						<td class="border_b">&nbsp;
						   <#if record.areaCorpTaxInfo.adminDivision??>${record.areaCorpTaxInfo.adminDivision}</#if>
						</td>
						<td class="itemtit">&nbsp;纳税管征机关编码</span></td>						
						<td class="border_b">&nbsp;
						      <#if areaAdminDivisionType??>
							          <#if areaAdminDivisionType=='11'>
									        省直征
									  </#if>
									   <#if areaAdminDivisionType=='12'>
											市直征
									  </#if>
									   <#if areaAdminDivisionType=='13'>
											市外税
									  </#if>
									   <#if areaAdminDivisionType=='14'>
											区地税
									  </#if>
									  <#if areaAdminDivisionType=='19'>
										     其它
									  </#if>
							  </#if>
							
						</td>
					</tr>
					
					
					<tr class="item">
						<td class="itemtit">&nbsp;上年纳税总额(万/年)</td>
						 <td class="border_b">&nbsp;
						   <#if record.areaCorpTaxInfo.lastYearTaxMoney??>${record.areaCorpTaxInfo.lastYearTaxMoney}</#if>
						</td>
						<td class="itemtit">&nbsp;入盘情况地址</td>
						<td class="border_b">&nbsp;
						    <#if record.areaCorpTaxInfo.adminDivisionInfo??>${record.areaCorpTaxInfo.adminDivisionInfo}</#if>
						</td>
						<td class="itemtit">&nbsp;是否可变更</span></td>						
						<td class="border_b">&nbsp;
					        <#if record.areaCorpTaxInfo.isCentral??>
							     <#if record.areaCorpTaxInfo.isCentral=='1'>
							           是
							          <#else>否
						    	</#if>
						  
						        </#if>
						</td>
					</tr>
					
					<tr class="item">
						<td class="itemtit">&nbsp;是否纳税大户</td>
						 <td class="border_b">&nbsp;
					        <#if record.areaCorpTaxInfo.isMajor??>
							     <#if record.areaCorpTaxInfo.isMajor=='1'>
							             是
							          <#else>否
						    	</#if>
						        </#if>
						</td>
						<td class="itemtit">&nbsp;纳税登记地址</td>
						<td class="border_b"  colspan="3">&nbsp;
						 <#if record.areaCorpTaxInfo.taxPlace??>${record.areaCorpTaxInfo.taxPlace}</#if>
						</td>
					</tr>
				</table>
				<!-- 企业地税信息结束 -->
			</form>
		</div>			
		<div title="人员组织信息" style="margin:0">
		   <iframe scrolling='auto' frameborder='0' src='${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/corBase_ciRsTopOrgInfo.jhtml?cbiId=${record.cbiId?c}' style='width:99.9%;height:99%;'></iframe>
	    </div>
		<div title="综治信息" style="margin:0">
		   <iframe scrolling='no' frameborder='0' src='${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/corBase_zzGroupInfo.jhtml?cbiId=${record.cbiId?c}' style='width:99.9%;height:99%;'></iframe>
	    </div>
		<div title="楼栋信息" style="margin:0">
		   <iframe scrolling='no' frameborder='0' src='${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/corBase_areaBuilding.jhtml?cbiId=${record.cbiId?c}' style='width:99.9%;height:99%;'></iframe>
	   </div>
</body>
</html>

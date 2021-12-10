<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" > 
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<title>安监检查</title>

<#include "/component/commonFiles-1.1.ftl" />
<script type="text/javascript" src="${rc.getContextPath()}/base/getNewDictionaryListByConfig.jhtml?var=newDictionaryData&bid=safetyStation"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/global.js?_=8"></script>

<style>
.w160{
	width:160px;
}
.title{
	height: 28px;
	background: #eff2f4;
	padding: 0 10px;
	font-size: 16px;
	line-height: 28px;
	font-family: Microsoft YaHei;
	border-left: 2px solid #36c;
	margin-top: 10px;
}
</style>

<script type="text/javascript">
	$(function(){
		$("#content-d").mCustomScrollbar({theme:"minimal-dark"});
		<#if corpId??>
			corDetail(${corpId});
		</#if>
		
	});
	
	$("#controlSafetyRanks").click(function() {
		$("#controlSafetyRanksInfo").attr("src", "${rc.getContextPath()}/zzgl/grid/safetyStation/controlSafetyRanksInfo.jhtml?corpId=${corpId?c}");
	});
	
	
 function corDetail(cbiId){
 	loadCorp(cbiId);
 }
  
 function corToggle(){
 	$("#corDetail").toggle();
 }
 
 function fillData(result){
 	$('#corName').text(result.corBaseInfo.corName);
	$('#category').text(result.corBaseInfo.categoryName==null?"":result.corBaseInfo.categoryName);
	$('#unitScale').text(result.corBaseInfo.corpExtInfo.unitScale==null?"":result.corBaseInfo.corpExtInfo.unitScale);
	$('#corAddr').text(result.corBaseInfo.corAddr==null?"":result.corBaseInfo.corAddr);//法人地址
	$('#safetyDegree').text(result.corBaseInfo.corpExtInfo.safetyDegree==null?"":result.corBaseInfo.corpExtInfo.safetyDegree);
	$('#spaceArea').text(result.corBaseInfo.corpExtInfo.spaceArea==null?"":result.corBaseInfo.corpExtInfo.spaceArea+"m²");//办公面积
	$('#employeeNum').text(result.corBaseInfo.corpExtInfo.employeeNum==null?"":result.corBaseInfo.corpExtInfo.employeeNum+"人");//员工数
	$('#economicType').text(result.corBaseInfo.economicTypeName==null?"":result.corBaseInfo.economicTypeName);
	$('#registrationNum').text(result.corBaseInfo.registrationNum==null?"":result.corBaseInfo.registrationNum);//注册号
	$('#busScope').text(result.corBaseInfo.busScope==null?"":result.corBaseInfo.busScope);//经营或业务范围
	$('#representativeName').text(result.corBaseInfo.representativeName==null?"":result.corBaseInfo.representativeName);//法人代表
	$('#telephone').text(result.corBaseInfo.telephone==null?"":result.corBaseInfo.telephone);//联系电话
	$('#checkPlace').val(result.corBaseInfo.corName);
	var unit = "";
	if(result.corBaseInfo.registeredCurrency=='01'){
		unit = "人民币";
	}else if (result.corBaseInfo.registeredCurrency=='02'){
		unit = "美元";
	}else if (result.corBaseInfo.registeredCurrency=='03'){
		unit = "欧元";
	}else if (result.corBaseInfo.registeredCurrency=='04'){
		unit = "港元";
	}
	$('#registeredCapital').text(result.corBaseInfo.registeredCapital==null?"":result.corBaseInfo.registeredCapital+"万"+unit);//注册资金
	$('#registrationTime').text(result.corBaseInfo.registrationTimeStr==null?"":result.corBaseInfo.registrationTimeStr);//注册时间
 }
 //加载企业信息
 function loadCorp(cbiId){
 	$.ajax({
		url: '${rc.getContextPath()}/zzgl/environment/environmentCheck/ajaxCorpDetail.json',
		type: 'POST',
		async:false,
		data: {cbiId: cbiId},
		error: function () {
			$.messager.alert('友情提示', '数据读取失败,详情请查看后台日志!', 'warning');
		},
		success: function (result) {
			 fillData(result);
			 //判断 CorpInfoCallBack 企业回调方法是否存在：存在则传递企业信息
			 if(typeof CorpInfoCallBack === 'function' ){
			    CorpInfoCallBack(result.corBaseInfo);
			 }
		}
	});
 }
 

</script>
</head>
<body>
<div >
	<input type="hidden" id="corpId" name="corpId" />
	<input type="hidden" id="gridId" name="gridId" /><!--gridId为选择企业-->
	<input type="hidden" id="corplaceName" name="corplaceName" />
      	<div  class="NorForm ConList" >
      	<div class="nav" id="tab" style="margin-top:10px;">
            <ul>
                <li class="current">企业信息</li>
                <li id="controlSafetyRanks">点位信息</li>
            </ul>
        </div>
        <div class="tabss2" style="height:100%">
        	<div id="content-d" class="content light" style="overflow-x:hidden; height:293px;position:relative;">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
			      <tr>
					 <td class="LeftTd" colspan="2">
					 	<label class="LabName"><span>企业名称：</span></label><div class="Check_Radio FontDarkBlue"><label id="corName"></label></div>
					 </td>
				  </tr>
				  <tr>
					 <td class="LeftTd" colspan="2"><label class="LabName"><span>法人地址：</span></label>
						 <div class="Check_Radio FontDarkBlue"><label id="corAddr"><#if corBaseInfo??><#if corBaseInfo.corAddr??>${corBaseInfo.corAddr}</#if></#if></label></div>
					 </td>
				  </tr>
				  <tr>
				  	 <td class="LeftTd" colspan="2"><label class="LabName"><span>经营范围：</span></label> 
					      <div class="Check_Radio FontDarkBlue" ><label id="busScope"><#if corBaseInfo??><#if corBaseInfo.busScope??>${corBaseInfo.busScope}</#if></#if></label></div>						  	               
					 </td>	
				  </tr>
				  <tr>
				  	 <td class="LeftTd"><label class="LabName"><span>经济类型：</span></label>
						 <div class="Check_Radio FontDarkBlue"><label id="economicType"><#if corBaseInfo??><#if corBaseInfo.economicTypeName??>${corBaseInfo.economicTypeName}</#if></#if></label></div>
					 </td>	
				     <td class="LeftTd"><label class="LabName"><span>行业分类：</span></label>
				 		 <div class="Check_Radio FontDarkBlue"><label id="category"><#if corBaseInfo??><#if corBaseInfo.categoryName??>${corBaseInfo.categoryName}</#if></#if></label></div>	             
					 </td>
				  </tr>
			      <tr>
					 <td class="LeftTd"><label class="LabName"><span>单位规模：</span></label>
				 		  <div class="Check_Radio FontDarkBlue"><label id="unitScale"><#if corBaseInfo??><#if corBaseInfo.corpExtInfo.unitScale??>${corBaseInfo.corpExtInfo.unitScale}</#if></#if></label></div>             
					 </td>	
					 
					 <td class="LeftTd"><label class="LabName"><span>员工人数：</span></label>
						 <div class="Check_Radio FontDarkBlue"><label id="employeeNum"><#if corBaseInfo??><#if corBaseInfo.corpExtInfo.employeeNum??>${corBaseInfo.corpExtInfo.employeeNum}</#if></#if></label></div>
					 </td>
				  </tr>
			      <tr>
					 <td class="LeftTd"><label class="LabName"><span>安全级别：</span></label>
						 <div class="Check_Radio FontDarkBlue"><label id="safetyDegree"><#if corBaseInfo??><#if corBaseInfo.corpExtInfo.safetyDegree??>${corBaseInfo.corpExtInfo.safetyDegree}</#if></#if></label></div>
					 </td>						 						
			
					 <td class="LeftTd"><label class="LabName"><span>经营场所面积：</span></label>
						 <div class="Check_Radio FontDarkBlue"><label id="spaceArea"><#if corBaseInfo??><#if corBaseInfo.corpExtInfo.spaceArea??>${corBaseInfo.corpExtInfo.spaceArea}m²</#if></#if></label></div>
					 </td>
				  </tr>
				  <tr>	
					 <td class="LeftTd"><label class="LabName"><span>注册号：</span></label>
						 <div class="Check_Radio FontDarkBlue"><label id="registrationNum"><#if corBaseInfo??><#if corBaseInfo.registrationNum??>${corBaseInfo.registrationNum}</#if></#if></label></div>
					 </td>						
							
					 <td class="LeftTd"><label class="LabName"><span>注册资金：</span></label>
						 <div class="Check_Radio FontDarkBlue"><label id="registeredCapital"><#if corBaseInfo??><#if corBaseInfo.registeredCapital??>${corBaseInfo.registeredCapital}万<#if corBaseInfo.registeredCurrency=='01'>人民币<#elseif corBaseInfo.registeredCurrency=='02'>美元<#elseif corBaseInfo.registeredCurrency=='03'>欧元<#elseif corBaseInfo.registeredCurrency=='04'>港元</#if></#if></#if></label></div>
					 </td>
				  </tr>
				  <tr>	
					 <td class="LeftTd"><label class="LabName"><span>注册时间：</span></label>
						 <div class="Check_Radio FontDarkBlue"><label id="registrationTime"><#if corBaseInfo??><#if corBaseInfo.registrationTimeStr??>${corBaseInfo.registrationTimeStr}</#if></#if></label></div>
					 </td>						
							
					 <td class="LeftTd"><label class="LabName"><span>法人代表：</span></label>
						 <div class="Check_Radio FontDarkBlue"><label id="representativeName"><#if corBaseInfo??><#if corBaseInfo.representativeName??>${corBaseInfo.representativeName}</#if></#if></label></div>
					 </td>
				  </tr>
				  <tr>	
					 <td class="LeftTd" colspan="2"><label class="LabName"><span>联系电话：</span></label>
						 <div class="Check_Radio FontDarkBlue"><label id="telephone"><#if corBaseInfo??><#if corBaseInfo.telephone??>${corBaseInfo.telephone}</#if></#if></label></div>
					 </td>						
				  </tr>
				</table>
			</div>
			<div title="点位信息" style="margin:0; height:293px;" class="content light hide">
			   <iframe id="controlSafetyRanksInfo" src="${rc.getContextPath()}/zzgl/grid/safetyStation/safetyStationInfo.jhtml?corpId=${corpId?c}" scrolling='no' frameborder='0' style='width:100%;height:100%;'></iframe>
		   	</div>
				
    	</div>
    </div>
</div>
</body>
<#include "/component/maxJqueryEasyUIWin.ftl" />
</html>

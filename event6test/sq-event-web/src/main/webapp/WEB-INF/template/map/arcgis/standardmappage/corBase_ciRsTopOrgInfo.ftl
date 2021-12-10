<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" > 
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<title>人员组织信息</title>
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/ligerUI/skins/Aqua/css/ligerui-common.css" />
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/ligerUI/skins/Aqua/css/ligerui-dialog.css" />
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-last/themes/icon.css">
<script src="${rc.getContextPath()}/js/jqueryeasyui-last/jquery.min.js" type="text/javascript"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-last/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-last/locale/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/ztree/zTreeStyle/zTreeStyle.css" />
<script type="text/javascript" src="${rc.getContextPath()}/js/ztree/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/ligerUI/js/core/base.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/ligerUI/js/plugins/ligerDialog.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/ligerUI/js/plugins/ligerResizable.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/ligerUI/js/plugins/ligerDrag.js"></script>
<style>
   body{
     font: 12px/ 160% 'Microsoft YaHei', simsun, Tahoma, Arial;          
   }  
 
</style>
</head>
	<body>
		<div id="editInfo">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">			    
			<#if corpDeptList??>
			<#if (corpDeptList?size>0)>       
					<#list corpDeptList as corpDept>
					    <#if (corpDept_index>0)>
					        <tr height='20'></tr>
					    </#if>				   
						<tr height='30' style="background:url(${rc.getContextPath()}/images/baitiele.png) repeat-x;">		    	
					    	<th  style="text-align:left; font-weight: bold;">
					    	  &nbsp;&nbsp;${corpDept.corpDepartmentName}			
					    	</th>		    	
						</tr>							
						<#if (corpCiRsTopList?size>0)>						    
								<tr height='25'>
								    <td style="text-align:center;">	    	
								    	<table width="100%" border="0" cellspacing="1" cellpadding="1">							    	        
								    	    <tr  style="height: 25px; text-align: center; color: #000; line-height: 28px;  background-color: rgb(238, 238, 238);">
												<td style="text-align:center;border-bottom:1px dotted #BED3DF;border-left:1px dotted #BED3DF; width:100px;">姓名</td>
											    <td style="text-align:center;border-bottom:1px dotted #BED3DF;border-left:1px dotted #BED3DF; width:100px;">人口类型</td>
											    <td style="text-align:center;border-bottom:1px dotted #BED3DF;border-left:1px dotted #BED3DF; width:50px;">性别</td>    
											    <td style="text-align:center;border-bottom:1px dotted #BED3DF;border-left:1px dotted #BED3DF; width:150px;">身份证号</td>
											    <td style="text-align:center;border-bottom:1px dotted #BED3DF;border-left:1px dotted #BED3DF; width:100px;">出生日期</td>
											    <td style="text-align:center;border-bottom:1px dotted #BED3DF;border-left:1px dotted #BED3DF; width:100px;">职务</td>
											    <td style="text-align:center;border-bottom:1px dotted #BED3DF;border-left:1px dotted #BED3DF; width:200px;">户籍所在地</td>
											    <td style="text-align:center;border-bottom:1px dotted #BED3DF;border-left:1px dotted #BED3DF; width:100px;">文化程度</td>
											    <td style="text-align:center;border-bottom:1px dotted #BED3DF;border-left:1px dotted #BED3DF; width:100px;">联系电话</td>
											</tr>
											<#assign i=0>
											<#list corpCiRsTopList as corpCiRsTop>											
							                <#if corpDept.corpDepartmentId==corpCiRsTop.corpDepartmentId>
							                    <#assign i=1>	 
									    	    <tr style="height: 25px; text-align: center;">	    	
											    	<td style="text-align:center;border-bottom:1px dotted #BED3DF;border-left:1px dotted #BED3DF;"">
											    	 <a href="#" onclick="openCiRsResidentDetail(${corpCiRsTop.ciRsId?c})" style="font-family:Arial, sans-serif;color:#008080;font-weight:normal;text-decoration:none; ">${corpCiRsTop.name!}&nbsp;</a> 			
											    	</td>
											    	<td style="text-align:center;border-bottom:1px dotted #BED3DF;border-left:1px dotted #BED3DF;">
											    	  ${corpCiRsTop.typeLabel!}&nbsp;			
											    	</td>
											    	<td style="text-align:center;border-bottom:1px dotted #BED3DF;border-left:1px dotted #BED3DF;">
											    	  ${corpCiRsTop.gender!}&nbsp;			
											    	</td>
											    	<td style="text-align:center;border-bottom:1px dotted #BED3DF;border-left:1px dotted #BED3DF;">
											    	  ${corpCiRsTop.identityCard!}&nbsp;			
											    	</td>
											    	<td style="text-align:center;border-bottom:1px dotted #BED3DF;border-left:1px dotted #BED3DF;">
											    	  ${corpCiRsTop.corpCiRsBirthdayStr!}&nbsp;			
											    	</td>
											    	<td style="text-align:center;border-bottom:1px dotted #BED3DF;border-left:1px dotted #BED3DF;">
											    	  ${corpCiRsTop.curDuty}&nbsp;			
											    	</td>
											    	<td style="text-align:center;border-bottom:1px dotted #BED3DF;border-left:1px dotted #BED3DF;">
											    	  ${corpCiRsTop.residence!}&nbsp;			
											    	</td>
											    	<td style="text-align:center;border-bottom:1px dotted #BED3DF;border-left:1px dotted #BED3DF;">
											    	  ${corpCiRsTop.educationLabel!}&nbsp;			
											    	</td>
											    	<td style="text-align:center;border-bottom:1px dotted #BED3DF;border-left:1px dotted #BED3DF;">
											    	  ${corpCiRsTop.residentMobile!}&nbsp;			
											    	</td>							    	
												</tr>																				
											</#if>																						
											</#list>
											<#if i==0>
											   <tr height='25' style="background-color: rgb(238, 238, 225);"><td  style="height: 30px; text-align: center;" colspan="9"><div>无数据</div></td></tr>
											</#if>																								
								    	</table>
							    	</td>	  	
								</tr>																						
						</#if>						 				
		           </#list>
		   <#else>
	          <tr height='25' style="background-color: rgb(238, 238, 225);"><td  style="height: 30px; text-align: center;"><div>无数据</div></td></tr> 	        	
           </#if>
           </#if>
           </table>
		</div>
	 
<script type="text/javascript">
   function openCiRsResidentDetail(ciRsId){			
			var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/detail/"+ciRsId+".jhtml";				
			var win = $.ligerDialog.open({ 
				title:"人员信息",
				url:url,
				height:420,
				width:800,
				showMax:true,
				showToggle:false,
				showMin:false,
				isResize:true,
				slide:false,
				isDrag:true,
				isunmask:true,
				isMax:false,
				isClosed:false,
				buttons:null
			});
			//return win;
}

</script>
</body>
</html>
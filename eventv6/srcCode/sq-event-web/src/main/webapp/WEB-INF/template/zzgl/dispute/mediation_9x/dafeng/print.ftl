<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>打印事件详情</title>
<#include "/component/commonFiles-1.1.ftl" />
<style type="text/css">
*{margin:0; padding:0; list-style:none; font-family:"微软雅黑"; font-size:12px;}
table{margin:0 auto;}
.con{border-bottom:1px solid #333; border-right:1px solid #333;}
.con td{padding:5px 10px; border-top:1px solid #333; border-left:1px solid #333; font-size:14px;}
h1{text-align:center; font-size:24px; padding-top:10px; font-weight:normal;}
.tr_style {height:30px;}
@media print {   
 .noprint{display:none;}
}
.td_{width: 14%;}
.td1_{width: 12%;}
</style>
</head>
<body>
<div class="noprint tool" style="position: fixed; top: 5px; right: 5px;">  
	<a href="###" class="NorToolBtn PrintBtn" onclick="window.print();">打印</a>
</div>
<h1>网格化信息平台矛盾纠纷详情单</h1>
 <table width="1000" height="44" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <!--<td width="420" style="font-size:14px;"><strong style="font-size:13px;">单号：</strong><#if event.code??>${event.code}</#if></td>
    <td width="330" style="font-size:14px;">&nbsp;</td>
    <td align="right" style="font-size:14px;">${year}<strong style="font-size:13px;">年</strong>${month}<strong style="font-size:13px;">月</strong>${day}<strong style="font-size:13px;">日印</strong></td>
  --></tr>
</table> 
<div style="word-break: break-all;">
	<table width="1000" border="0" cellspacing="0" cellpadding="0" class="con">
	   <tr class="tr_style">
	    <td class="td_">事件名称：</td>
	    <td width="910" colspan="3"><#if disputeMediation.disputeEventName??>${disputeMediation.disputeEventName}</#if></td>
	  </tr>
	  <tr class="tr_style">
	    <td width="90">事件简述：</td>
	    <td width="910" colspan="3"><#if disputeMediation.disputeCondition??>${disputeMediation.disputeCondition}</#if></td>
	  </tr>
	  <tr class="tr_style">
	    <td width="90">发生日期：</td>
	    <td width="410"><#if disputeMediation.happenTimeStr??>${disputeMediation.happenTimeStr}</#if></td>
	    <td class="td1_">所属区域：</td>
	    <td width="410"><#if disputeMediation.gridPath??>${disputeMediation.gridPath}</#if></td>
	  </tr>
	  <tr class="tr_style">
	    <td width="90">受理日期：</td>
	    <td width="410"><#if disputeMediation.acceptedDateStr??>${disputeMediation.acceptedDateStr}</#if></td>
	    <td width="90">化解时限：</td>
	    <td width="410"><#if disputeMediation.mediationDeadlineStr??>${disputeMediation.mediationDeadlineStr}</#if></td>
	  </tr>
	  <tr class="tr_style">
	    <td width="90">事发地址：</td>
	    <td width="410"><#if disputeMediation.happenAddr??>${disputeMediation.happenAddr}</#if></td>
	    <td width="90">事件类别：</td>
	    <td width="410">
	    	<#if disputeType_9x??>
             	 <#list disputeType_9x as l>
					<#if disputeMediation.disputeType2??>
						<#if (l.dictGeneralCode?string==disputeMediation.disputeType2?string)>${l.dictName}</#if>
					</#if>
				</#list>
            </#if>
	    </td>
	  </tr>
	  <tr class="tr_style">
	    <td width="90">事件规模：</td>
	    <td width="410">
			<#if disputeScaleDC??>
             	 <#list disputeScaleDC as l>
					<#if disputeMediation.disputeScale??>
						<#if (l.dictGeneralCode?string==disputeMediation.disputeScale?string)>${l.dictName}</#if>
					</#if>
				</#list>
          	</#if>
		</td>
	    <td width="90">影响范围：</td>
	    <td width="410"><#if scopeInfluence??>${scopeInfluence}</#if></td>
	  </tr>
	  <tr class="tr_style">
	  	<td width="90">事件性质：</td>
	    <td width="410"><#if eventNature??>${eventNature}</#if></td>
	    <td width="90">涉及人数：</td>
	    <td width="410"><#if disputeMediation.involveNum??>${disputeMediation.involveNum}(人)</#if></td>
	  </tr>
	  <tr class="tr_style" id="involvedPeople">
	    <td width="90">风险类型：</td>
	    <td width="410">
	    	<#if FXLXDM??>
		         <#list FXLXDM as l>
					<#if disputeMediation.riskCode??>
						<#if (l.dictGeneralCode?string==disputeMediation.riskCode?string)>${l.dictName}</#if>
					</#if>
				</#list>
            </#if>
        </td>
	    <td width="90">风险等级：</td>
	    <td width="410">
	    	<#if FXDJDM??>
		        <#list FXDJDM as l>
					<#if disputeMediation.riskGrade??>
						<#if (l.dictGeneralCode?string==disputeMediation.riskGrade?string)>${l.dictName}</#if>
					</#if>
				</#list>
            </#if>
		</td>
	  </tr>
	  <tr class="tr_style">
		  	<td width="90">化解方式：</td>
		    <td width="410">
		    	<#if mediationTypeDC_9x??>
			        <#list mediationTypeDC_9x as l>
						<#if disputeMediation.mediationType??>
							<#if (l.dictGeneralCode?string==disputeMediation.mediationType?string)>${l.dictName}</#if>
						</#if>
					</#list>
		       </#if>
		    </td>
		    <td width="90">化解日期：</td>
		    <td width="410"><#if disputeMediation.mediationDateStr??>${disputeMediation.mediationDateStr}</#if></td>
	  </tr>
	  <tr class="tr_style">
		  	<td width="90">化解责任人姓名：</td>
		    <td width="410"><#if disputeMediation.mediator??>${disputeMediation.mediator}</#if></td>
		    <td width="90">化解是否成功：</td>
		    <td width="410"><#if disputeMediation.isSuccess??><#if disputeMediation.isSuccess=="1">成功<#else>失败</#if></#if></td>
	  </tr>
	  <tr class="tr_style">
	    <td width="90">化解情况：</td>
	    <td width="910" colspan="3"><#if disputeMediation.mediationResult??>${disputeMediation.mediationResult}</#if></td>
	  </tr>
	  <tr class="tr_style">
    	<#if attList?size &gt; 0>
    	<#assign x = 0> <#-- 创建变量 x -->
    	 <td width="999" colspan="4" >
			<#list attList as att>
				<#if x lt 3>
	   				<#if att.title == 'image'>
	   				<#assign x = x + 1> <#-- 替换变量 x -->
	   					<img title="${att.fileName}" style="width:320px;height:auto;" src="${imgDomain}${att.filePath}" />
					</#if>
				</#if>	
			</#list>
		</td>
			<#else>
				<td width="999" colspan="4">暂无附件 </td>
		</#if>
	  </tr>
	</table>
</div>
</body>
		<script>
            $(function () {
            	//获取人员信息
            	<#if involvedPeoples??>
	        		<#list involvedPeoples as l>
	                    detailMainPeople("${l.ipId!''}");
	        		</#list>
        		</#if>
            });
            
            function detailMainPeople(ipId){
                if(ipId!=null&&ipId!=""){
                	$.ajax({
        			 	type: "POST",
        			 	url: '${rc.getContextPath()}/zhsq/involvedPeople/detail.jhtml?ipId='+ipId+'',
        			 	dataType:'json',
        			 	success: function(data){
		        			 		var main_people_html = '<tr class="tr_style">'+
		        				  	'<td width="90">姓名：</td>'+
		        				    '<td width="410">'+data.involvedPeople.name+'</td>'+
		        				    '<td width="90">证件号码：</td>'+
		        				    '<td width="410">'+data.involvedPeople.idCard+'</td>'+
		        				    '</tr>'+
		        				    '<tr class="tr_style">'+
		        				  	'<td width="90">人员类别：</td>'+
		        				    '<td width="410">'+data.involvedPeople.peopleTypeName+'</td>'+
		        				    '<td width="90">居住详址：</td>'+
		        				    '<td width="410">'+data.involvedPeople.homeAddr+'</td>'+
		        			  		'</tr>';
									$('#involvedPeople').after(main_people_html);
        			 		}
        			 	});
                }
            }
            
        </script>
</html>

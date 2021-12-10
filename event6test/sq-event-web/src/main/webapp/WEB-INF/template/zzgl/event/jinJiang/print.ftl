<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>打印事件详情-晋江</title>
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
</style>
</head>
<body>
<div class="noprint tool" style="position: fixed; top: 5px; right: 5px;">  
	<a href="###" class="NorToolBtn PrintBtn" onclick="window.print();">打印</a>
</div>
<h1>综治网格化信息平台事件详情单</h1>
<table width="1000" height="44" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="420" style="font-size:14px;"><strong style="font-size:13px;">单号：</strong><#if event.code??>${event.code}</#if></td>
    <td width="330" style="font-size:14px;">&nbsp;</td>
    <td align="right" style="font-size:14px;">${year}<strong style="font-size:13px;">年</strong>${month}<strong style="font-size:13px;">月</strong>${day}<strong style="font-size:13px;">日印</strong></td>
  </tr>
</table>
<div style="word-break: break-all;">
	<table width="1000" border="0" cellspacing="0" cellpadding="0" class="con">
	  <tr class="tr_style">
	    <td width="90">事件类型：</td>
	    <td width="410"><#if event.eventClass??>${event.eventClass}</#if></td>
	    <td width="90">事件标题：</td>
	    <td width="410"><#if event.eventName??>${event.eventName}</#if></td>
	  </tr>
	  <tr class="tr_style">
	    <td width="90">所属网格：</td>
	    <td width="410"><#if event.gridName??>${event.gridName}</#if></td>
	    <td width="90">采集渠道：</td>
	    <td width="410"><#if event.collectWayName??>${event.collectWayName}</#if></td>
	  </tr>
	  <tr class="tr_style">
	    <td width="90">信息来源：</td>
	    <td width="410"><#if event.sourceName??>${event.sourceName}</#if></td>
	    <td width="90">影响范围：</td>
	    <td width="410"><#if event.influenceDegreeName??>${event.influenceDegreeName}</#if></td>
	  </tr>
	  <tr class="tr_style">
	    <td width="90">紧急程度：</td>
	    <td width="410">
	    	<#if event.urgencyDegree?? &&event.urgencyDegree=='02'>
	    		<span style="color:red"><#if event.urgencyDegreeName??>${event.urgencyDegreeName}</#if></span>
	    	<#else>
	    		<#if event.urgencyDegreeName??>${event.urgencyDegreeName}</#if>
	    	</#if>
	    </td>
	    <td width="90">涉及人员：</td>
	    <td width="410"><#if event.involvedNumInt??>${event.involvedNumInt}<#else>0</#if>（人）<#if event.involvedPersion??>${event.involvedPersion}</#if></td>
	  </tr>
	  <tr class="tr_style">
	    <td width="90">采集时间：</td>
	    <td width="410"><#if event.createTimeStr??>${event.createTimeStr}</#if></td>
	    <td width="90">事发时间：</td>
	    <td width="410"><#if event.happenTimeStr??>${event.happenTimeStr}</#if></td>
	  </tr>
	  <tr class="tr_style">
	  	<td width="90">联系人员：</td>
	    <td width="410"><#if event.contactUser??>${event.contactUser}</#if></td>
	    <td width="90">联系电话：</td>
	    <td width="410"><#if event.tel??>${event.tel}</#if></td>
	  </tr>
	  <tr class="tr_style">
	    <td width="90">事发详址：</td>
	    <td width="910" colspan="3"><#if event.occurred??>${event.occurred}</#if></td>
	  </tr>
	  <tr height="70">
	    <td width="90">事件描述：</td>
	    <td width="910" colspan="3"><#if event.content??>${event.content}</#if></td>
	  </tr>
	  <tr class="tr_style">
	    <td width="90">处理记录：</td>
	    <td width="910" colspan="3" style="padding:0;">
	    	<#if taskList??>
	    	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="con2">
	    		<tr>
	    			<td width="15%" style="border-left:none; border-top:none;"><strong>办理环节</strong></td>
	    			<td width="30%" style="border-top:none;"><strong>办理人/办理时间</strong></td>
	    			<td width="55%" style="border-top:none;"><strong>办理意见</strong></td>
	    		</tr>
	    		<#list taskList as l>
					<tr>
		    			<td style="border-left:none; border-top:1px solid #333;">${l.TASK_NAME!'&nbsp;'}</td>
		    			<td>
		    				<#if l.HANDLE_PERSON??>
		    					<p>${l.HANDLE_PERSON!'&nbsp;'}</p>
		    				<#else>
			    				<p>
			    					[${l.ORG_NAME}]-${l.TRANSACTOR_NAME} 耗时 
			    					<#if (l.INTER_TIME == '0分钟')>
			    						小于1分钟
			    					<#else>
			    						${l.INTER_TIME}
			    					</#if>
			    					<#if (l.ISTIMEOUT?? && l.ISTIMEOUT=='1')>超时</#if>
			    				</p>
		                        <p>办理时间：${l.END_TIME}</p>
	                        </#if>
		    			</td>
		    			<td>${l.REMARKS!'&nbsp;'}</td>
		    		</tr>
	            </#list>
	    	</table>
	    	<#else>
	    		&nbsp;
		    </#if>
	    </td>
	  </tr>
	</table>
</div>
</body>
</html>

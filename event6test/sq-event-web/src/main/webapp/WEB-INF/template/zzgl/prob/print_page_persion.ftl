<!DOCTYPE html>
<html>
<head>
	<title>列表</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<#include "/component/maxJqueryEasyUIWin.ftl" />
	<#include "/component/listSet.ftl" />
	<#include "/component/AnoleDate.ftl">
	<style type="text/css">
		.inp1 {width:100px;}
		
		#ff td{
		 text-align: center
		}

 .beforeCss{
      margin-left:10%;margin-top:3%;
    }
    .afterCss{
      margin-left:30px;margin-top:3%;
    }

	</style>
</head>
  
<body >
<div id="content-d" class="MC_con content light">
<div class="tool fr" >
	<a id="print" href="javascript:void(0);" class="NorToolBtn PrintBtn" onclick="printIt();">打印</a>
</div>

<div id="content">


<table width="1000" border="1" cellspacing="0" cellpadding="0" class="border-t beforeCss" style="" id="tabTT">  
　 
	
	<tr>
		
		<td class="LeftTd" colspan="24" style="text-align: center;">
		<h1>个人问题台账</h1>
			
        </td>
	</tr>
	<tr>
	<td>序号</td>
	<td>问题标题</td>
	<td>姓名</td>
	<td>性别</td>
	<td>政治面貌</td>
	<td>单位</td>
	<td>职务</td>
	<td style="text-align: center">简要案情</td>
	<td>违纪人员职级</td>
	<td>违纪违规时间</td>
	<td>党纪处分</td>
	<td>政纪处分</td>
	<td>组织处理类型</td>
	<td>组织处理内容</td>
	<td>是否问责</td>
	<td>是否移送司法</td>
	<td>涉案金额</td>
	<td>违纪金额</td>	
	<td>违纪违法资金类别</td>
	<td>违规违纪类别统计</td>
	<td>作风建设类别统计</td>
	<td>扫黑除恶类别统计</td>
	<td>四种形态</td>
	<td>追缴资金（万元）</td>
	</tr>
  	
<#if pagination?? && pagination.rows??>
<#list pagination.rows as vo>
 <tr id="ff">
   <td>${vo_index+1}</td>
   <td>${vo.probTitle!}</td>
   <td>${vo.name!}</td>
   <td>${vo.sex!}</td>
   <td>${vo.politics!}</td>
   <td>${vo.workUnit!}</td>
   <td>${vo.profession!}</td>
    <td>${vo.caseBrief!}</td>
   <td>${vo.professionType!}</td>
   <td>${vo.violationDate!}</td>
   <td>${vo.partyFlag!}</td>
   <td>${vo.disciplineyFlag!}</td>
   <td>${vo.orgProcType!}</td>
   <td>${vo.procResult!}</td>
   <td>${vo.blameFlag!}</td>  
   <td>${vo.transferJusticeFlag!}</td>
   <td>${vo.amountInvolved!}</td>
   <td>${vo.violationMoney!}</td>
   <td>${vo.violationMoneyType!}</td>
   <td>${vo.violationType1!}</td>
   <td>${vo.violationType2!}</td>
   <td>${vo.violationType0!}</td>
   <td>${vo.shape!}</td>
   <td>${vo.amountOfRecovery!}</td>  

 </tr>

</#list>

</#if>
　</table> 
	</div>

</div>
<script type="text/javascript">

function printIt(){
		var oldstr = document.body.innerHTML; 
		//修改样式
	
		$("#tabTT").removeClass("beforeCss");
		$("#tabTT").addClass("afterCss");
		var newstr = $("#content").html();
		window.document.body.innerHTML=newstr;
		
		window.print();
		document.body.innerHTML = oldstr; 
		$("#tabTT").removeClass("afterCss");
		$("#tabTT").addClass("beforeCss");
		
	}
	
</script>

</body>

<script type="text/javascript">
	
</script>
</html>

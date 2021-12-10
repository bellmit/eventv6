<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>框选统计</title>
<#include "/component/commonFiles-1.1.ftl" />
<script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>
</head>

<script type="text/javascript">


</script>
<body>
	<div id="content-d" class="MC_con content light" style="width:376px; height:290px;">
		<div class="NorForm" style="width:376px;">
    		<table width="100%" border="0" cellspacing="0" cellpadding="0">
		    	<#assign n=0>
		    	<#if result?exists>
		    		<#list result?keys as key>
			    		<#if ((n%2)=0)>
							<tr>
						</#if>
							<td><label class="LabName"><span>${result[key][0]}：</span></label><div class="Check_Radio FontDarkBlue"><a href='javascript:void(0);' onclick='zhoubianNumClick("${result[key][0]}","${key}")'>${result[key][1]}</a></div></td>
						<#if ((n%2)=1)>
							</tr>
						</#if>
						<#assign n=(n+1)>
					</#list>
				</#if>
				
				<#if ((n%2)=1)>
					<td>
					</td>
					</tr>
				</#if>
				<#if ((n%2)=0)>
					</tr>
				</#if>
			</table>
        </div>
	</div>
</body>
<script>
	(function($){
		$(window).load(function(){
			var options = {
				axis : "yx",
				theme : "minimal-dark"
			};
			enableScrollBar('content-d',options);
		});
	})(jQuery);
	
	function zhoubianNumClick(name,zhoubianName) {
		window.parent.parent.showZhouBianObjectList(name,zhoubianName,${x},${y},${distance},${mapType});
	}
</script>
</html>

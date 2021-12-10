<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>命案防控-编辑</title>
	<#include "/component/standard_common_files-1.1.ftl" />
</head>
<body onload="loadIframeSrc();">
	<div class="ConList">
		<div class="nav" id="tab" style="margin-top:10px;" onclick="changeTab();">
            <ul style="padding-left:10px;">
                <li id="homicideCaseTab" class="current">命案防控</li>
                <li id="suspectTab">命案嫌疑人(<label id="suspect_03">0</label>)</li>
                <li id="victimTab">命案受害人(<label id="suspect_04">0</label>)</li>
            </ul>
        </div>
    </div>
	<div id="content-d">
		<div id="homicideCaseTabDiv" class="homicideTabDiv">
			<iframe id="homicideCaseTabIframe" scrolling="no" frameborder="0" style="width:100%;height:400px;"></iframe>
		</div>
		
		<div id="suspectTabDiv" class="homicideTabDiv hide">
			<iframe id="suspectTabIframe" scrolling="no" frameborder="0" style="width:100%;height:100%;"></iframe>
		</div>
		
		<div id="victimTabDiv" class="homicideTabDiv hide">
			<iframe id="victimTabIframe" scrolling="no" frameborder="0" style="width:100%;height:100%;"></iframe>
		</div>
	</div>
	
	<script type="text/javascript">
		$(function(){
			
	        var options = { 
	            axis : "yx", 
	            theme : "minimal-dark" 
	        }; 
	       // enableScrollBar('content-d',options); 
	        
	        $("#content-d iframe").height($(window).height() - $("#tab").height())//为了去除tab高度影响
	        					  .width($(window).width());//为了修复外框架页签切换时，页面宽度受影响的问题
	        
	        var tabIndex = parseInt("${tabIndex!'0'}");
	        
	        if(isNaN(tabIndex)) {
	        	tabIndex = 0;
	        }
	        
	        $('#tab > ul > li').removeClass("current")
	        				   .eq(tabIndex)
	        				   .addClass("current");
	        
	        changeTab();
	    });
		
		function loadIframeSrc() {//为了修复chrome浏览器下iframe会加载两次的问题
			$("#homicideCaseTabIframe").attr("src", "${rc.getContextPath()}/zhsq/relatedEvents/homicideCase/toAddHomicideCase.jhtml?reId=<#if reId??>${reId}</#if>");
			
			var suspectSrc = "${rc.getContextPath()}/zhsq/relatedEvents/homicideCase/toSuspectList.jhtml?bizId=<#if reId??>${reId}</#if>&bizType=";
			
			$("#suspectTabIframe").attr("src", suspectSrc+ "03");
			
			$("#victimTabIframe").attr("src", suspectSrc+ "04");
		}
		
		function changeTab() {//页签切换
			var currentTab = $('#tab > ul > li[class="current"]').eq(0);
			var iframeObj = $("#"+currentTab.attr("id")+"Iframe");
			var iframeLoaded = iframeObj.attr("_loadflag");
			
			$("div .homicideTabDiv").hide();
			$("#"+currentTab.attr("id")+"Div").show();
			
			if(isBlankStringTrim(iframeLoaded)) {
				iframeObj.attr("_loadflag", true);
				iframeObj.attr("src", iframeObj.attr("src"));//重新加载iframe，为了修复IE下隐藏iframe宽度、高度不能自适应的问题
			}
		}
		
		function countSuspect(bizType, total) {
			$("#suspect_"+bizType).html(total);
		}
	</script>
	
</body>
</html>

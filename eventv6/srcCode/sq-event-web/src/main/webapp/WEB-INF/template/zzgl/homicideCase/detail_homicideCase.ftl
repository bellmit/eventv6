<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>命案防控-详情(包含嫌疑人和受害人)</title>
	<#include "/component/standard_common_files-1.1.ftl" />
	<style>
		.LabName span{padding-right: 0px;}
	</style>
</head>
<body>
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
			<iframe id="homicideCaseTabIframe" scrolling="no" frameborder="0" source="iframeSrc" iframeSrc="${rc.getContextPath()}/zhsq/relatedEvents/homicideCase/toDetailHomicide.jhtml?reId=<#if reId??>${reId}</#if>" style="width:100%;height:100%;"></iframe>
		</div>
		
		<div id="suspectTabDiv" class="homicideTabDiv hide">
			<iframe id="suspectTabIframe" scrolling="no" frameborder="0" source="src" src="${rc.getContextPath()}/zhsq/relatedEvents/homicideCase/toSuspectList.jhtml?isNotDetail=false&bizType=03&bizId=<#if reId??>${reId}</#if>" style="width:100%;height:100%;"></iframe>
		</div>
		
		<div id="victimTabDiv" class="homicideTabDiv hide">
			<iframe id="victimTabIframe" scrolling="no" frameborder="0" source="src" src="${rc.getContextPath()}/zhsq/relatedEvents/homicideCase/toSuspectList.jhtml?isNotDetail=false&bizType=04&bizId=<#if reId??>${reId}</#if>" style="width:100%;height:100%;"></iframe>
		</div>
	</div>
	
	<script type="text/javascript">
		$(function(){
	        var options = { 
	            axis : "yx", 
	            theme : "minimal-dark" 
	        }; 
	        //enableScrollBar('content-d',options); 
	        
	        $("#content-d iframe").height($(window).height() - $("#tab").height())//为了去除tab高度影响
	        					  .width($(window).width());//为了修复外框架页签切换时，页面宽度受影响的问题
	        
	        changeTab();
	    });
		
		function changeTab() {
			var currentTab = $('#tab > ul > li[class="current"]').eq(0);
			var iframeObj = $("#"+currentTab.attr("id")+"Iframe");
			var iframeLoaded = iframeObj.attr("_loadflag");
			
			$("div .homicideTabDiv").hide();
			$("#"+currentTab.attr("id")+"Div").show();
			
			if(isBlankStringTrim(iframeLoaded)) {
				iframeObj.attr("_loadflag", true);
				iframeObj.attr("src", iframeObj.attr(iframeObj.attr("source")));
			}
		}
		
		function countSuspect(bizType, total) {
			$("#suspect_"+bizType).html(total);
		}
	</script>
	
</body>
</html>

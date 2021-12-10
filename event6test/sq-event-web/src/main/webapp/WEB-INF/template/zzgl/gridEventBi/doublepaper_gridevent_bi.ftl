<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>办结/采集列表</title>
		<#include "/component/commonFiles-1.1.ftl" />
	</head>
	
	<body>
		<div class="MetterList">
	    	<div class="box">
	        	<div class="MetterContent">
	                <div class="ConList">
	                    <div class="nav" id="tab">
	                        <ul>
	                        	<li id="uncomplete" class="current">未办结信息</li>
	                            <li id="complete">办结信息</li>    
	                        </ul>
	                    </div>
	                    <div class="ListShow">
	                    	<iframe class="tabs2" id="list1" src="${rc.getContextPath()}/zhsq/event/eventOverviewController/listEventA.jhtml?infoOrgCode=${infoOrgCode!''}&startTime=${startTime!''}&endTime=${endTime!''}&eventStatus=00,01,02" width="100%" height="100%" scrolling="no" style="border:0;"></iframe>
							<iframe class="tabs2 hide" id="list2" width="100%" height="100%" scrolling="no" style="border:0;"></iframe>
	                    </div>
	                </div>
	            </div>
	        </div>
	    </div>
		
		<script>window.jQuery || document.write('<script src="${rc.getContextPath()}/theme/standardfordetail/js/minified/jquery-1.11.0.min.js"><\/script>')</script>
		<script type="text/javascript">
			$(".ListShow").height($(document).height() - 38);
			
			var urls = [
				"${rc.getContextPath()}/zhsq/event/eventOverviewController/listEventA.jhtml?infoOrgCode=${infoOrgCode!''}&startTime=${startTime!''}&endTime=${endTime!''}&eventStatus=00,01,02",
				"${rc.getContextPath()}/zhsq/event/eventOverviewController/listEventA.jhtml?infoOrgCode=${infoOrgCode!''}&startTime=${startTime!''}&endTime=${endTime!''}&eventStatus=03,04",
			];
			
			//选项卡切换
			var $NavDiv2 = $(".ConList ul li");
			$NavDiv2.click(function() {
				$(this).addClass("current").siblings().removeClass("current");
				var NavIndex2 = $NavDiv2.index(this);
				var tab = $(".ListShow .tabs2").eq(NavIndex2);
				setUrl(tab, urls[NavIndex2]);
		   	});
		   	
		   	function setUrl(tab, url) {
		   		if (tab.attr("src") == undefined || tab.attr("src") == "") {
		   			tab.attr("src", url);
		   		}
		   		tab.show().siblings().hide();
		   	}

		</script>
		<#include "/component/maxJqueryEasyUIWin.ftl" />
	</body>
</html>
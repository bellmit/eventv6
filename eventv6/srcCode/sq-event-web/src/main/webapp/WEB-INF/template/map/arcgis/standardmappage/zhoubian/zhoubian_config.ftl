<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>楼宇-详情</title>
<#include "/component/commonFiles-1.1.ftl" />
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
</head>
<style>
.range .current1 .line3 {background:#1bbc9b;}
.range .selected1 .line3{background:#1bbc9b;}
.line3{
    width: 108px;
    height: 8px;
    background: #eee;
    float: left;
    margin-top: 8px;
}
</style>
<body>
<div class="con zhoubiantongji">
			<input type="hidden" name="socialOrgCode" id="socialOrgCode" value="${socialOrgCode}" />
  			<input type="hidden" name="homePageType" id="homePageType" value="${homePageType}" />
				<div class="Navgation">
	            	<ul>
	                	<li id="configTab" class="selected">资源选项</li>
	                    <li id="statTab">统计信息</li>
	                </ul>
	            </div>
		        
	            <div class="NavTabs">
	            	<div>
	            		<div class="range" id="resourceRange">
	            		
			            	</div>
						<div id="content-d" class="content light" style="height:250px;">
							
					        <div id="selectedDiv" class="SelectedTags"></div>
				        	<div class="clear"></div>
					        	<div id="cont">
					        	<!--
					        	<div  class="nav">
									<ul id="ulFirstall">
										<li class="GreenBg current">人</li>
										<li class="YellowBg">地</li>
										<li class="CyanBg">事</li>
										<li class="PrinkBg">物</li>
										<li class="PurpleBg">情</li>
										<li class="BlueBg">组织</li>
									</ul>
									<div class="line"></div>
								</div>
					            <div class="con tabss">
					            	<div>
					                    <ul id="ulPeople"  class="type"></ul>
					                </div>
					            	<div class="hide">
					                    <ul id="ulWorld" class="type"></ul>
					                </div>
					            	<div class="hide">
					                    <ul id="ulMetter" class="type"></ul>
					                </div>
					            	<div class="hide">
					                    <ul id="ulThing" class="type"></ul>
					                </div>
					            	<div class="hide">
					                    <ul id="ulSituation" class="type"></ul>
					                </div>
					            	<div class="hide">
					                    <ul id="ulOrganization" class="type">
					                        <li class="current">6</li>
					                        <li>计生</li>
					                        <li>计生</li>
					                        <li>计生</li>
					                        <li>计生</li>
					                        <div class="clear"></div>
					                    </ul>
					                </div>
					            </div>
					            -->
				            </div>
			            </div>
						<div class="btn">
							<input id="btnStat" onclick="statzhoubian()" name="" type="button" value="资源统计" class="NorBtn" />
						</div>
					</div>
					<div class="hide" style="height:352px;">
						<iframe id="zhoubianStatInfo" name="zhoubianStatInfo" width="100%" height="100%" src="" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>
					</div>
				</div>
</div>
	
</body>
<script type="text/javascript">
var selectedDistance=500;
var selectCurrent;
var x = ${x};
var y = ${y};
var selectedZhouBianStr="";
var menuNameStr="";
var typeStr="";
var selectedZhouBianNum=0;
var mapt=${mapt};

 $(function(){
 	getZhouBianResource(); 
 	getGisStatConfigs();
	//getLayerMenuInfo();
	
	var $NavDiv = $(".zhoubiantongji .Navgation ul li");
	$NavDiv.click(function(){
		$(this).addClass("selected").siblings().removeClass("selected");
		var NavIndex = $NavDiv.index(this);
		$(".NavTabs").children().eq(NavIndex).show().siblings().hide();
	});
	
	var $NavDiv2 = $(".zhoubiantongji .nav ul li");
	$NavDiv2.click(function(){
		$(this).removeClass("current").siblings().addClass("current");
		var NavIndex2 = $NavDiv2.index(this);
		$(".tabss").children().eq(NavIndex2).show().siblings().hide();
	});
	
	var $NavDiv3 = $(".zhoubiantongji .range .dot");
	$NavDiv3.click(function(){
		var thisDistanceStr = $(this).children('em').text();
		selectedDistance = parseInt(thisDistanceStr);
		
		var obj = $('.dot').each(function(i){
			if(selectedDistance > $("#em"+(i+1)).val()){
				$("#R"+(i+1)).removeClass();
				$("#R"+(i+1)).addClass("selected1");
			}else if(selectedDistance == $("#em"+(i+1)).val()){
				$("#R"+(i+1)).removeClass();
				$("#R"+(i+1)).addClass("current1");
			}else if(selectedDistance < $("#em"+(i+1)).val()){
				$("#R"+(i+1)).removeClass();
			}
		});
		
		
	});
	
	var options = {
				axis : "yx",
				theme : "minimal-dark"
			};
	enableScrollBar('content-d',options);
 })

// 获取地图周边资源可配置
function getGisStatConfigs() {
	var categories,smallCategories,smallCategory,gisDataCfg;
	var tableBody = "";
	var navsCount = 0;
	var tabsCount = 0;
	var category_;
	
	$.ajax({   
		 url: '${rc.getContextPath()}/zhsq/map/gisStatConfig/getGisStatConfigs.json?statType=1&bizType=${homePageType}&regionCode=${socialOrgCode}&t='+Math.random(),
		 type: 'POST',
		 dataType:"json",
		 async: false,
		 error: function(data){
		 	$.messager.alert('友情提示','地图周边资源配置信息获取出现异常!','warning');
		 },
		 success: function(data){
		    categories = data;
		    
		    tableBody += '<div id="nav" class="nav">';
		    tableBody += '<ul>';
		    
		    // 大类
		    for (var category in categories) {
		    	if (category == '') {
		    		category_ = category;
		    	} else {
			    	if (navsCount == 0) {
			    		tableBody += '<li class="GreenBg">' + category + '</li>';
			    		navsCount = 1;
			    	} else {
				    	tableBody += '<li class="GreenBg current">' + category + '</li>';
			    	}
		    	}
		    
		    }
		    
		    tableBody += '</ul>';
		    tableBody += '</div>';
		    
	    	tableBody += '<div class="con tabss">';
	    	
	    	// 小类
		    for (var key in categories) {
		    	smallCategories = categories[key];
		    
				if (tabsCount == 0) {
					tableBody += '<div>';
					tabsCount = 1;
				} else {
					tableBody += '<div class="hide">';
				}		    
		    	
		    	tableBody += '<ul class="type">';
		    	
		    	for (var i = 0, j = smallCategories.length; i < j; i++) {
		    		smallCategory = smallCategories[i];
		    		gisDataCfg = smallCategory.gisDataCfg;
		    		tableBody += '<li id="li'+gisDataCfg.menuName+'" onclick="clickChange(this,\''+gisDataCfg.zhoubianName+'\',\''+smallCategory.statObjName+'\')">' + smallCategory.statObjName + '</li>';
		    	}
		    	
		    	tableBody += '</ul>';
		    	tableBody += '</div>';
		    }
		    
		    tableBody += '</div>';
		    $("#cont").html(tableBody);
		    
		    if (category_ == '') {
		    	$("#nav").css("display", "none");
		    }
		 }
	 });
}


function getLayerMenuInfo(){

	$.ajax({   
		 url: '${rc.getContextPath()}/zhsq/map/menuconfigure/menuConfig/getGisDataCfgRelationTreeVersionNoe.json?homePageType=${homePageType}&orgCode=${socialOrgCode}&t='+Math.random(),
		 type: 'POST',
		 timeout: 3000,
		 dataType:"json",
		 async: false,
		 error: function(data){
		 	$.messager.alert('友情提示','地图配置信息获取出现异常!','warning');
		 },
		 success: function(data){
		    gisDataCfg=eval(data.gisDataCfg);
		    if(gisDataCfg != null){
		    	var htmlStr = "";
			    var ulFirstallList = gisDataCfg.childrenList;
			    for(var i=0; i<ulFirstallList.length; i++){
			    	if(htmlStr == ""){
			    		htmlStr +="<li class=\""+ulFirstallList[i].className+" current\">"+ulFirstallList[i].menuName+"</li>";
			    	}else {
			    		htmlStr +="<li class=\""+ulFirstallList[i].className+" \">"+ulFirstallList[i].menuName+"</li>";
			    	}
			    	var ulFirstall = document.getElementById("ulFirstall");
			    	ulFirstall.innerHTML = htmlStr;
			    	if(ulFirstallList[i].childrenGdcIds != ","){
			    		var secondHtmlStr = "";
			    		var ulSecondList = ulFirstallList[i].childrenList;
			    		for(var j=0;j<ulSecondList.length;j++){
			    			if(ulSecondList[j].zhoubianName != null){
			    				secondHtmlStr +="<li id=\"li"+ulSecondList[j].menuName+"\" onclick=\"clickChange(this,'"+ulSecondList[j].zhoubianName+"','"+ulSecondList[j].menuName+"')\">"+ulSecondList[j].menuName+"</li>"
			    			}
			    		}
			    		
			    		if(ulFirstallList[i].menuCode == "PEOPLE"){
			    			var ulSecond = document.getElementById("ulPeople");
			    			secondHtmlStr+= "<li id=\"li老年人\" onclick=\"clickChange(this,'zhouBianStatOfPeopleService','老年人','OldPeople')\">老年人</li>";// 大于等于60
			    			secondHtmlStr+= "<li id=\"li儿童\" onclick=\"clickChange(this,'zhouBianStatOfPeopleService','儿童','Chileren')\">儿童</li>";// 小于14
			    			secondHtmlStr+= "<li id=\"li总人口\" onclick=\"clickChange(this,'zhouBianStatOfPeopleService','总人口','Total')\">总人口</li>";//不传
			    			secondHtmlStr+= "<li id=\"li常口\" onclick=\"clickChange(this,'zhouBianStatOfPeopleService','常口','Permanent')\">常口</li>";//001
			    			secondHtmlStr+= "<li id=\"li流口\" onclick=\"clickChange(this,'zhouBianStatOfPeopleService','流口','Floating')\">流口</li>";//002
			    			secondHtmlStr += "<div class=\"clear\"></div>";
			    			ulSecond.innerHTML = secondHtmlStr;
			    		}else if(ulFirstallList[i].menuCode.toUpperCase() == "WORLD"){
			    			var ulSecond = document.getElementById("ulWorld");
			    			secondHtmlStr += "<div class=\"clear\"></div>";
			    			ulSecond.innerHTML = secondHtmlStr;
			    		}else if(ulFirstallList[i].menuCode.toUpperCase() == "METTER") {
			    			var ulSecond = document.getElementById("ulMetter");
			    			secondHtmlStr += "<div class=\"clear\"></div>";
			    			ulSecond.innerHTML = secondHtmlStr;
			    		}else if(ulFirstallList[i].menuCode.toUpperCase() == "THING") {
			    			var ulSecond = document.getElementById("ulThing");
			    			secondHtmlStr += "<div class=\"clear\"></div>";
			    			ulSecond.innerHTML = secondHtmlStr;
			    		}else if(ulFirstallList[i].menuCode.toUpperCase() == "SITUATION") {
			    			var ulSecond = document.getElementById("ulSituation");
			    			secondHtmlStr += "<div class=\"clear\"></div>";
			    			ulSecond.innerHTML = secondHtmlStr;
			    		}else if(ulFirstallList[i].menuCode.toUpperCase() == "ORGANIZATION") {
			    			var ulSecond = document.getElementById("ulOrganization");
			    			secondHtmlStr += "<div class=\"clear\"></div>";
			    			ulSecond.innerHTML = secondHtmlStr;
			    		}
			    	}
			    }
			    
		    }
		    
		 }
	 });
}
function clickChange(obj,zhoubianName,menuName,type){
	var selectedDiv = document.getElementById("selectedDiv");
	var selectedDivHtml = selectedDiv.innerHTML;
	//$(this).addClass("current").siblings().removeClass("current");
	if($(obj).hasClass("current")) {
		selectedDiv.removeChild(document.getElementById(menuName));
		$(obj).removeClass("current");
		
		if (type != '' && type != null && type != 'undefind') {
			typeStr = typeStr.replace(type+",","");
			
			if (typeStr == '') {
				selectedZhouBianStr = selectedZhouBianStr.replace(zhoubianName+",","");
				menuNameStr = menuNameStr.replace(menuName+",","");
			}
		} else {
			selectedZhouBianStr = selectedZhouBianStr.replace(zhoubianName+",","");
			menuNameStr = menuNameStr.replace(menuName+",","");
		}
		
		selectedZhouBianNum=selectedZhouBianNum-1;
	}else {
		if(selectedZhouBianNum<6){
			selectedDivHtml+="<span  id=\""+menuName+"\">"+menuName+"<em onclick=\"closeMenuName('"+menuName+"','"+zhoubianName+"','"+type+"')\"></em></span>";
			selectedDiv.innerHTML= selectedDivHtml;
			
			if (selectedZhouBianStr.indexOf(zhoubianName) == -1) {
				selectedZhouBianStr+=zhoubianName+",";
				menuNameStr+=menuName+",";
			}
			
			if (type != '' && type != null && type != 'undefind') {
				if (typeStr.indexOf(type) == -1) {
					typeStr += type + ",";
				}
			}
			
			$(obj).addClass("current");
			selectedZhouBianNum=selectedZhouBianNum+1;
		}else {
			$.messager.alert('友情提示','统计项不得超过6个！','warning');
		}
		
	}
}
function closeMenuName(menuName,zhoubianName,type){
	var selectedObj = document.getElementById("li"+menuName);
	$(selectedObj).removeClass("current");
	var selectedDiv = document.getElementById("selectedDiv");
	selectedDiv.removeChild(document.getElementById(menuName));
	selectedZhouBianNum=selectedZhouBianNum-1;
	
	if (zhoubianName != 'zhouBianStatOfPeopleService') {
		selectedZhouBianStr = selectedZhouBianStr.replace(zhoubianName+",","");
		menuNameStr = menuNameStr.replace(menuName+",","");
	}
	
	if (type != '' && type != null && type != 'undefind') {
		typeStr = typeStr.replace(type+",","");
	}
}

function statzhoubian(){
	if(x ==0 || mapt !=parent.currentArcgisConfigInfo.mapType ){
		
	}else if(selectedZhouBianStr == ""){
		$.messager.alert('友情提示','请选择要统计的周边资源！','warning');
	}else{
		toStatZhouBianData()
	}
	
}

function toStatZhouBianData(){
	window.parent.toShowZhouBianSketch(x,y,selectedDistance);
	var url = '${rc.getContextPath()}/zhsq/map/zhoubian/zhouBianStat/getZhouBianStatData.jhtml?x='+x+'&y='+y+'&distance='+selectedDistance+'&mapt='+mapt+'&zhoubianTypeStr='+selectedZhouBianStr+'&menuNameStr='+encodeURIComponent(encodeURIComponent(menuNameStr))+'&typeStr='+typeStr+'&orgCode='+parent.document.getElementById("orgCode").value+'&t='+Math.random();
	document.getElementById("zhoubianStatInfo").src = url
	$("#statTab").click();
}

// 获取地图周边资源范围
function getZhouBianResource() {
	var dict ="B219";
	$.ajax({   
		 url: '${rc.getContextPath()}/zhsq/map/zhoubian/zhouBianStat/getZhouBianResourceTypeList.json?t=' + Math.random(),
		 type: 'POST',
		 dataType:"json",
		 async: false,
		 error: function(data){
		 	$.messager.alert('友情提示','地图周边资源范围获取出现异常!','warning');
		 },
		 success: function(datas){
		 		if(datas.length>5){
		 			var data =datas.slice(0, 6);
		 		}else{
		 			var data =datas;
		 		}
		 		for(var i=0;i<data.length;i++){
			 		if(i==0){
				 		var html = '<div id="R'+(i+1)+'" class="current1"> <div class="dot"><span></span><em id="em'+(i+1)+'">'+data[i].dictName+'</em></div></div>';
			 		}else{
			 			var html = '<div id="R'+(i+1)+'" style="float: left;position: relative;height: 24px;" > <div class="line3"></div><div class="dot"><span></span><em id="em'+(i+1)+'">'+data[i].dictName+'</em></div></div>';
			 		}
				 		$("#resourceRange").append(html);
			 			$("#em"+(i+1)).val(parseInt(data[i].dictName));
			 		
			 		if(i==data.length-1){
		 				var len=data.length-1;
		 				var wid = 326/len +"px";
		 				var obj = $('.line3').each(function(j){
		 					$('.line3').css('width',wid);
			 				$("#R"+(j+2)).css('width',wid);
		 				})
		 			}
			 		
			 	}
		 	
		 }
	});
}
</script>
</html>
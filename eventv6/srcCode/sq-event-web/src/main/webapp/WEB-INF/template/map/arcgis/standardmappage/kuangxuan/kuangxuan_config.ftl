<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>楼宇-详情</title>
<#include "/component/commonFiles-1.1.ftl" />
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<style type="text/css">
	.li-white-space{
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap; 
	}
</style>
</head>
<body>
<div class="con kuangxuantongji">
			<input type="hidden" name="socialOrgCode" id="socialOrgCode" value="${socialOrgCode}" />
  			<input type="hidden" name="homePageType" id="homePageType" value="${homePageType}" />
  			<input type="hidden" name="gridId" id="gridId" value="${gridId?c}" />
				<div class="Navgation">
	            	<ul>
	                	<li id="configTab" class="selected">统计选项</li>
	                    <li id="statTab">统计信息</li>
	                </ul>
	            </div>
	            <div class="NavTabs">
	            	<div>
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
				                    <ul id="ulSituation" class="type">/ul>
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
							<input id="btnKuangxuan" onclick="kuangxuan()" name="" type="button" value="框选" class="NorBtn" />
							<input id="renewKuangxuan" onclick="renewKuangxuan()" style="display:none;" type="button" value="重新框选" class="NorBtn" />
							&nbsp;
							<input id="btnStat" onclick="statkuangxuan()" name="" type="button" value="统计" class="NorBtn" />
						</div>
					</div>
					<div class="hide" style="height:291px;">
						<iframe id="kuangxuanConfigInfo" name="kuangxuanConfigInfo" width="100%" height="100%" src="" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>
					</div>
				</div>
</div>
	
</body>
<script type="text/javascript">
var selectedKuangXuanStr="";
var menuNameStr="";
var selectedKuangXuanNum=0;
var geoString="";
var mapt=0;
 $(function(){
 	getGisStatConfigs();
	//getLayerMenuInfo();
	
	var $NavDiv = $(".kuangxuantongji .Navgation ul li");
	$NavDiv.click(function(){
		$(this).addClass("selected").siblings().removeClass("selected");
		var NavIndex = $NavDiv.index(this);
		$(".NavTabs").children().eq(NavIndex).show().siblings().hide();
	});
	
	var $NavDiv2 = $(".kuangxuantongji .nav ul li");
	$NavDiv2.click(function(){
		$(this).removeClass("current").siblings().addClass("current");
		var NavIndex2 = $NavDiv2.index(this);
		$(".tabss").children().eq(NavIndex2).show().siblings().hide();
	});
	
	var options = {
				axis : "yx",
				theme : "minimal-dark"
			};
			enableScrollBar('content-d',options);
			setNeedKuangxuan();
 })
//调整为需要进行框选的状态
function setNeedKuangxuan(){
	$("#btnStat").css("background-color","#5d5d5d");
	$("#btnStat").attr("title","当前无范围数据，请进行框选操作！");
	$("#btnKuangxuan").attr("value","框选");
}

function moveNeedKuangxuan(){
	$("#btnStat").css("background-color","#448aca");
	$("#btnStat").attr("title","");
	$("#btnKuangxuan").attr("value","重新框选");
}

function renewKuangxuan() {
	if (typeof parent.MMApi != "undefined") {
		parent.MMApi.renewKuangxuan(kuangXuanBackFunc);
		$("#btnKuangxuan").hide();
		$("#renewKuangxuan").show();
	}
}

// 获取地图框选可配置
function getGisStatConfigs() {
	var categories,smallCategories,smallCategory,gisDataCfg;
	var tableBody = "";
	var navsCount = 0;
	var tabsCount = 0;
	var category_;
	
	$.ajax({   
		 url: '${rc.getContextPath()}/zhsq/map/gisStatConfig/getGisStatConfigs.json?statType=0&bizType=${homePageType}&regionCode=${socialOrgCode}&t='+Math.random(),
		 type: 'POST',
		 dataType:"json",
		 async: false,
		 error: function(data){
		 	$.messager.alert('友情提示','地图框选配置信息获取出现异常!','warning');
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
		    		tableBody += '<li class="li-white-space" title="'+smallCategory.statObjName+'" id="li'+gisDataCfg.menuName+'" onclick="clickChange(this,\''+gisDataCfg.kuangxuanName+'\',\''+smallCategory.statObjName+'\')">' + smallCategory.statObjName + '</li>';
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
			    			if(ulSecondList[j].kuangxuanName != null){
			    				secondHtmlStr +="<li id=\"li"+ulSecondList[j].menuName+"\" onclick=\"clickChange(this,'"+ulSecondList[j].kuangxuanName+"','"+ulSecondList[j].menuName+"')\">"+ulSecondList[j].menuName+"</li>"
			    			}
			    		}
			    		secondHtmlStr += "<div class=\"clear\"></div>";
			    		if(ulFirstallList[i].menuCode.toUpperCase() == "PEOPLE"){
			    			var ulSecond = document.getElementById("ulPeople");
			    			ulSecond.innerHTML = secondHtmlStr;
			    		}else if(ulFirstallList[i].menuCode.toUpperCase() == "WORLD"){
			    			var ulSecond = document.getElementById("ulWorld");
			    			ulSecond.innerHTML = secondHtmlStr;
			    		}else if(ulFirstallList[i].menuCode.toUpperCase() == "METTER") {
			    		var ulSecond = document.getElementById("ulMetter");
			    			ulSecond.innerHTML = secondHtmlStr;
			    		}else if(ulFirstallList[i].menuCode.toUpperCase() == "THING") {
			    			var ulSecond = document.getElementById("ulThing");
			    			ulSecond.innerHTML = secondHtmlStr;
			    		}else if(ulFirstallList[i].menuCode.toUpperCase() == "SITUATION") {
			    			var ulSecond = document.getElementById("ulSituation");
			    			ulSecond.innerHTML = secondHtmlStr;
			    		}else if(ulFirstallList[i].menuCode.toUpperCase() == "ORGANIZATION") {
			    			var ulSecond = document.getElementById("ulOrganization");
			    			ulSecond.innerHTML = secondHtmlStr;
			    		}
			    	}
			    }
			    
		    }
		    
		 }
	 });
}

// 图层元素选中状态
function clickChange(obj,kuagnxuanName,menuName){
	var selectedDiv = document.getElementById("selectedDiv");
	var selectedDivHtml = selectedDiv.innerHTML;
	//$(this).addClass("current").siblings().removeClass("current");
	if($(obj).hasClass("current")) {
		selectedDiv.removeChild(document.getElementById(menuName));
		$(obj).removeClass("current");
		selectedKuangXuanStr = selectedKuangXuanStr.replace(kuagnxuanName+",","");
		menuNameStr = menuNameStr.replace(menuName+",","");
		selectedKuangXuanNum=selectedKuangXuanNum-1;
	}else {
		if(selectedKuangXuanNum<6){
			selectedDivHtml+="<span  id=\""+menuName+"\">"+menuName+"<em onclick=\"closeMenuName('"+menuName+"','"+kuagnxuanName+"')\"></em></span>";
			selectedDiv.innerHTML= selectedDivHtml;
			selectedKuangXuanStr+=kuagnxuanName+",";
			menuNameStr+=menuName+",";
			$(obj).addClass("current");
			selectedKuangXuanNum=selectedKuangXuanNum+1;
		}else {
			alert("统计项不得超过6个！")
		}
		
	}
}
function closeMenuName(menuName,kuagnxuanName){
	var selectedObj = document.getElementById("li"+menuName);
	$(selectedObj).removeClass("current");
	var selectedDiv = document.getElementById("selectedDiv");
	selectedDiv.removeChild(document.getElementById(menuName));
	selectedKuangXuanNum=selectedKuangXuanNum-1;
	selectedKuangXuanStr = selectedKuangXuanStr.replace(kuagnxuanName+",","");
	menuNameStr = menuNameStr.replace(menuName+",","");
	
}

function kuangXuanBackFunc(geoStr) {
	geoString = geoStr;
	toStatKuangxuanData(parent.MMGlobal.MapType, geoStr);
}

function kuangxuan(){
	if(selectedKuangXuanStr == ""){
		alert("请选择要统计的项！");return false;
	}
	//parent.closeKuangxuan();
	//toStatKuangxuanData();
	if (typeof parent.MMApi != "undefined") {
		parent.MMApi.startKuangXuan(kuangXuanBackFunc);
		$("#btnKuangxuan").hide();
		$("#renewKuangxuan").show();
	} else {
		parent.setKuangXuanState(selectedKuangXuanStr);
	}
}

function statkuangxuan(){
	if (typeof parent.MMApi != "undefined") {
		if (geoString == "") {
			alert("无框选数据，请进行框选操作！");
		} else {
			toStatKuangxuanData(parent.MMGlobal.MapType, geoString);
		}
	} else {
		if(geoString == "" || mapt !=parent.currentArcgisConfigInfo.mapType){
			//alert("无框选数据，请进行框选操作！");
		}else{
			if (selectedKuangXuanStr == "") {
				alert("请选择要统计的项！");
			} else {
				//parent.closeKuangxuan();
				//parent.arcgisKuangXuanGetData(mapt,geoString,selectedKuangXuanStr);
				toStatKuangxuanData(mapt, geoString);
			}
		}
	}
}

function toStatKuangxuanData(mapt, geoString) {
	var url = '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getKuangXuanStatData.jhtml?mapt='+mapt+'&geoString='+geoString+'&kuangxuanTypeStr='+selectedKuangXuanStr+'&menuNameStr='+encodeURIComponent(encodeURIComponent(menuNameStr))+'&orgCode='+parent.document.getElementById("orgCode").value+'&gridId='+parent.document.getElementById("gridId").value+'&t='+Math.random();
	document.getElementById("kuangxuanConfigInfo").src = url
	$("#statTab").click();
}
</script>
</html>
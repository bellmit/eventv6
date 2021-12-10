<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>重点人群</title>
<#include "/component/commonFiles-map-1.1.ftl" />
<style type="text/css">
/*---------------------------------楼宇简介-----------------------------------------*/
.LyIntroduce{padding:10px;}
.LyPhoto{width:300px; position:relative;}
.LyPhoto .img_list{overflow: hidden; position: relative; height: 300px; width:220px; background:url(${rc.getContextPath()}/theme/standardfordetail/images/ly_01.png); margin:0 auto;}
/* 根据图片的张数来设定ul的宽度 */
.LyPhoto .img_list ul{ width: 1100px; position: absolute; height: 300px; left: 0px;}
.LyPhoto .img_list li{ float: left; width: 220px;}
.LyPhoto .img_list img{ width: 220px; height: 300px;}
/* 左右点击的按钮样式 */
.LyPhoto .toLeft,.LyPhoto .toRight{display: block;position: absolute;width: 20px;height: 100%;top: 0;}
.LyPhoto .toLeft{left:0;background:url(${rc.getContextPath()}/theme/standardfordetail/images/leftbtn.png) no-repeat left center;}
.LyPhoto .toLeft:hover{background:url(${rc.getContextPath()}/theme/standardfordetail/images/leftbtnhover.png) no-repeat left center;}
.LyPhoto .toRight{right:0;background:url(${rc.getContextPath()}/theme/standardfordetail/images/rightbtn.png) no-repeat left center;}
.LyPhoto .toRight:hover{background:url(${rc.getContextPath()}/theme/standardfordetail/images/rightbtnhover.png) no-repeat left center;}
.LyDetail{width: 450px;}
.address{width: 450px; height:40px;}
.address span{font-size:22px; color:#175fa1; font-family:"微软雅黑"; padding-right:10px;}
.LyDetail .fl{width:220px;}
.LyDetail .fl ul li{line-height:25px;}
.LyDetail .fl span{color:#175fa1;}
.LyDetail .fr{width: 200px;}
.LyDetail .fr dl{height:48px; width:160px; float:left; margin-top:20px;}
.LyDetail .fr dl:first-child{margin-top:0;}
.LyDetail .fr dl dt{float:left; width:58px;}
.LyDetail .fr dl dd{float:left; line-height:24px; width:90px;}
.LyPeople .search{padding:10px; border-bottom:1px solid #c7c7c7;}
.LyPeople .search ul li{float:left; margin-right:10px; line-height:22px;}
.LyPeople .type{padding:10px; border-bottom:1px solid #c7c7c7;}
.LyPeople .type ul li{width:80px; margin-left:10px; float:left; border:1px solid #c1c1c1; background:#fff; height:24px; line-height:24px; text-align:center; color:#333; cursor:pointer;}
.LyPeople .type ul li:hover{width:78px; height:22px; border:2px solid #27AE60; line-height:22px;}
.LyPeople .type ul li.current{width:78px; height:22px; border:2px solid #27AE60; line-height:22px; background:url(${rc.getContextPath()}/theme/standardfordetail/images/gou.png) no-repeat #fff right bottom;}
.TypeName{height:22px; line-height:22px;}

.remarks{width:140px; border-left:1px dotted #c7c7c7;}
.remarks ul.CutLine{border-bottom:1px dotted #c7c7c7;}
.remarks img{width:18px; height:18px; vertical-align:middle; margin-right:15px; margin-top:-2px;}
.remarks ul{padding-left:10px;}
.remarks ul li{padding:6px 0;}
</style>

</head>
<body>
    <div class="MetterList">
    	<div class="box">
        	<div class="MetterContent">
                <div class="ConList">
                    <div class="nav" id="tab">
                        <ul>
                            <li id="drugsLi" class="current">吸毒人员</li>
                            <li id="neuropathyLi">精神病人员</li>  
                            <li id="campsLi">刑满释放人员</li>
                            <li id="aidsPatientLi">艾滋病人员</li>
                            <li id="rectifyLi">社区矫正人员</li>
                        </ul>
                    </div>
                    <div class="ListShow">
                    	<iframe class="tabs2" id="drugsList" src="${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofpoplocal/toPopuPage.jhtml?type=drugs&gridId=${gridId?c}" width="100%" height="100%" scrolling="no" style="border:0;"></iframe>
						<iframe class="tabs2 hide" id="neuropathyList" width="100%" height="100%" scrolling="no" style="border:0;"></iframe>
						<iframe class="tabs2 hide" id="campsList" width="100%" height="100%" scrolling="no" style="border:0;"></iframe>
						<iframe class="tabs2 hide" id="aidsPatientList" width="100%" height="100%" scrolling="no" style="border:0;"></iframe>
						<iframe class="tabs2 hide" id="rectifyList" width="100%" height="100%" scrolling="no" style="border:0;"></iframe>
                    </div>
                </div>
            </div>
        </div>
    </div>

	<script>window.jQuery || document.write('<script src="${rc.getContextPath()}/theme/standardfordetail/js/minified/jquery-1.11.0.min.js"><\/script>')</script>
	<script>
	var urls = [
		"${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofpoplocal/toPopuPage.jhtml?type=drugs&gridId=${gridId?c}",
		"${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofpoplocal/toPopuPage.jhtml?type=neuropathy&gridId=${gridId?c}",
		"${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofpoplocal/toPopuPage.jhtml?type=camps&gridId=${gridId?c}",
		"${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofpoplocal/toPopuPage.jhtml?type=aidsPatient&gridId=${gridId?c}",
		"${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofpoplocal/toPopuPage.jhtml?type=rectify&gridId=${gridId?c}"
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
   	$(".ListShow").height($(document).height() - 38);
	</script>
	<#include "/component/maxJqueryEasyUIWin.ftl" />
</body>
</html>

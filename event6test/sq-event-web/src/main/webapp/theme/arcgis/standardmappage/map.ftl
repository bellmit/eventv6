<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<link href="css/public.css" rel="stylesheet" type="text/css" />
<link href="css/map.css" rel="stylesheet" type="text/css" />
<link href="css/jquery.mCustomScrollbar.css" rel="stylesheet" type="text/css">
<script src="js/function.js"></script>
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script type="text/javascript">
function firstall(){
	$(".firstall").show();
	$(".people").hide();
	$(".world").hide();
	$(".metter").hide();
	$(".thing").hide();
	$(".situation").hide();
}
function people(){
	$(".people").show();
	$(".firstall").hide();
	$(".NorList").hide();
}
function world(){
	$(".world").show();
	$(".firstall").hide();
	$(".NorList").hide();
}
function metter(){
	$(".metter").show();
	$(".firstall").hide();
	$(".NorList").hide();
}
function thing(){
	$(".thing").show();
	$(".firstall").hide();
	$(".NorList").hide();
}
function situation(){
	$(".situation").show();
	$(".firstall").hide();
	$(".NorList").hide();
}
function CloseBtn(){
	$(".ListSearch").hide();
}
function CloseX(){
	$(".NorList").hide();
	$(".ztIcon").hide();
}
function SearchBtn(){
	$(".ListSearch").show();
}
</script>
</head>

<body>
<div><img src="images/map.png" width="100%" /></div>
<div class="MapBar">
    <div class="con AlphaBack">
    	<div class="local fl">
        	<ul>
            	<li><img src="images/local.png" /></li>
                <li>台江区</li>
            	<li><img src="images/xiala.png" /></li>
            </ul>
        </div>
        <div class="zhuanti fr"><a href="#"><img src="images/zhuanti.png" />专题图层</a></div>
    </div>
    <div class="SelectTree AlphaBack dest"></div>
    <!-----------------------------------人地事物情------------------------------------->
	<div class="ztIcon AlphaBack dest firstall">
    	<div class="h_20"></div>
		<ul>
			<li class="GreenBg" onclick="people()">
				<p class="pic"><img src="images/icon01.png" /></p>
				<p class="word AlphaBack">人</p>
			</li>
			<li class="YellowBg" onclick="world()">
				<p class="pic"><img src="images/icon02.png" /></p>
				<p class="word AlphaBack">地</p>
			</li>
			<li class="CyanBg" onclick="metter()">
				<p class="pic"><img src="images/icon03.png" /></p>
				<p class="word AlphaBack">事</p>
			</li>
			<li class="PrinkBg" onclick="thing()">
				<p class="pic"><img src="images/icon04.png" /></p>
				<p class="word AlphaBack">物</p>
			</li>
			<li class="PurpleBg" onclick="situation()">
				<p class="pic"><img src="images/icon05.png" /></p>
				<p class="word AlphaBack">情</p>
			</li>
		</ul>
        <div class="clear"></div>
	</div>
    <!-----------------------------------人------------------------------------->
	<div class="ztIcon AlphaBack people">
    	<div class="title"><span class="fr" onclick="CloseX()"><img src="images/closex.png" /></span><a href="#" onclick="firstall()">专题图层</a> > 人</div>
    	<div class="h_20"></div>
		<ul>
			<li class="GreenBg">
				<p class="pic"><img src="images/icon01.png" /></p>
				<p class="word AlphaBack">党员</p>
			</li>
			<li class="YellowBg">
				<p class="pic"><img src="images/icon02.png" /></p>
				<p class="word AlphaBack">退休</p>
			</li>
			<li class="CyanBg">
				<p class="pic"><img src="images/icon03.png" /></p>
				<p class="word AlphaBack">居家养老</p>
			</li>
			<li class="PrinkBg">
				<p class="pic"><img src="images/icon04.png" /></p>
				<p class="word AlphaBack">服兵役</p>
			</li>
			<li class="PurpleBg">
				<p class="pic"><img src="images/icon05.png" /></p>
				<p class="word AlphaBack">失业</p>
			</li>
		</ul>
        <div class="clear"></div>
	</div>
    <div class="NorList AlphaBack" style="display:block;">
    	<div class="title"><span class="fr" onclick="CloseX()"><img src="images/closex.png" /></span><span class="fr" onclick="SearchBtn()"><img src="images/search.png" /></span><a href="#" onclick="firstall()">专题图层</a> > <a href="#" onclick="people()">人</a> > 党员</div>
        <div class="ListSearch">
        	<div class="condition">
            	<ul>
                	<li class="LC1">排序方式：</li>
                	<li class="LC2"><select name="" class="sel1"></select></li>
                </ul>
            	<ul>
                	<li class="LC1">输入姓名：</li>
                	<li class="LC2"><input name="" type="text" class="inp1" /></li>
                </ul>
            	<ul>
                	<li class="LC1">&nbsp;</li>
                	<li class="LC2"><input name="" type="button" value="查询" class="NorBtn" /></li>
                </ul>
                <div class="clear"></div>
            </div>
        	<div class="CloseBtn" onclick="CloseBtn()"></div>
        </div>
        <div class="ListShow content mCustomScrollbar">
        	<div class="liebiao">
                <ul>
                    <li>
                        <p>姓名：某某某</p>
                        <p>身份证：1234567890</p>
                    </li>
                    <li class="bg">
                        <p>姓名：某某某</p>
                        <p>身份证：1234567890</p>
                    </li>
                    <li>
                        <p>姓名：某某某</p>
                        <p>身份证：1234567890</p>
                    </li>
                    <li class="bg">
                        <p>姓名：某某某</p>
                        <p>身份证：1234567890</p>
                    </li>
                    <li>
                        <p>姓名：某某某</p>
                        <p>身份证：1234567890</p>
                    </li>
                    <li class="bg">
                        <p>姓名：某某某</p>
                        <p>身份证：1234567890</p>
                    </li>
                    <li>
                        <p>姓名：某某某</p>
                        <p>身份证：1234567890</p>
                    </li>
                    <li class="bg">
                        <p>姓名：某某某</p>
                        <p>身份证：1234567890</p>
                    </li>
                    <li>
                        <p>姓名：某某某</p>
                        <p>身份证：1234567890</p>
                    </li>
                    <li class="bg">
                        <p>姓名：某某某</p>
                        <p>身份证：1234567890</p>
                    </li>
                    <li>
                        <p>姓名：某某某</p>
                        <p>身份证：1234567890</p>
                    </li>
                    <li class="bg">
                        <p>姓名：某某某</p>
                        <p>身份证：1234567890</p>
                    </li>
                    <li>
                        <p>姓名：某某某</p>
                        <p>身份证：1234567890</p>
                    </li>
                    <li class="bg">
                        <p>姓名：某某某</p>
                        <p>身份证：1234567890</p>
                    </li>
                    <li>
                        <p>姓名：某某某</p>
                        <p>身份证：1234567890</p>
                    </li>
                    <li class="bg">
                        <p>姓名：某某某</p>
                        <p>身份证：1234567890</p>
                    </li>
                    <li>
                        <p>姓名：某某某</p>
                        <p>身份证：1234567890</p>
                    </li>
                    <li class="bg">
                        <p>姓名：某某某</p>
                        <p>身份证：1234567890</p>
                    </li>
                    <li>
                        <p>姓名：某某某</p>
                        <p>身份证：1234567890</p>
                    </li>
                    <li class="bg">
                        <p>姓名：某某某</p>
                        <p>身份证：1234567890</p>
                    </li>
                    <li>
                        <p>姓名：某某某</p>
                        <p>身份证：1234567890</p>
                    </li>
                    <li class="bg">
                        <p>姓名：某某某</p>
                        <p>身份证：1234567890</p>
                    </li>
                    <li>
                        <p>姓名：某某某</p>
                        <p>身份证：1234567890</p>
                    </li>
                    <li class="bg">
                        <p>姓名：某某某</p>
                        <p>身份证：1234567890</p>
                    </li>
                    <li>
                        <p>姓名：某某某</p>
                        <p>身份证：1234567890</p>
                    </li>
                    <li class="bg">
                        <p>姓名：某某某</p>
                        <p>身份证：1234567890</p>
                    </li>
                    <li>
                        <p>姓名：某某某</p>
                        <p>身份证：1234567890</p>
                    </li>
                    <li class="bg">
                        <p>姓名：某某某</p>
                        <p>身份证：1234567890</p>
                    </li>
                </ul>
            </div>
        </div>
        <div class="NorPage">
        	<ul>
            	<li class="PreBtn"><img src="images/pre.png" /></li>
            	<li class="yema">共 1/3 页 转至 </li>
                <li class="PageInp"><input name="" type="text" /></li>
                <li class="PageBtn"><input name="" type="button" value="确定" /></li>
            	<li class="NextBtn"><img src="images/next.png" /></li>
            </ul>
        </div>
    </div>
    <!-----------------------------------地------------------------------------->
	<div class="ztIcon AlphaBack world">
    	<div class="title"><span class="fr" onclick="CloseX()"><img src="images/closex.png" /></span><a href="#" onclick="firstall()">专题图层</a> > 地</div>
    	<div class="h_20"></div>
		<ul>
			<li class="GreenBg">
				<p class="pic"><img src="images/icon01.png" /></p>
				<p class="word AlphaBack">党员</p>
			</li>
			<li class="YellowBg">
				<p class="pic"><img src="images/icon02.png" /></p>
				<p class="word AlphaBack">退休</p>
			</li>
			<li class="CyanBg">
				<p class="pic"><img src="images/icon03.png" /></p>
				<p class="word AlphaBack">居家养老</p>
			</li>
			<li class="PrinkBg">
				<p class="pic"><img src="images/icon04.png" /></p>
				<p class="word AlphaBack">服兵役</p>
			</li>
			<li class="PurpleBg">
				<p class="pic"><img src="images/icon05.png" /></p>
				<p class="word AlphaBack">失业</p>
			</li>
		</ul>
        <div class="clear"></div>
	</div>
    <!-----------------------------------事------------------------------------->
	<div class="ztIcon AlphaBack metter">
    	<div class="title"><span class="fr" onclick="CloseX()"><img src="images/closex.png" /></span><a href="#" onclick="firstall()">专题图层</a> > 事</div>
    	<div class="h_20"></div>
		<ul>
			<li class="GreenBg">
				<p class="pic"><img src="images/icon01.png" /></p>
				<p class="word AlphaBack">党员</p>
			</li>
			<li class="YellowBg">
				<p class="pic"><img src="images/icon02.png" /></p>
				<p class="word AlphaBack">退休</p>
			</li>
			<li class="CyanBg">
				<p class="pic"><img src="images/icon03.png" /></p>
				<p class="word AlphaBack">居家养老</p>
			</li>
			<li class="PrinkBg">
				<p class="pic"><img src="images/icon04.png" /></p>
				<p class="word AlphaBack">服兵役</p>
			</li>
			<li class="PurpleBg">
				<p class="pic"><img src="images/icon05.png" /></p>
				<p class="word AlphaBack">失业</p>
			</li>
		</ul>
        <div class="clear"></div>
	</div>
    <!-----------------------------------物------------------------------------->
	<div class="ztIcon AlphaBack thing">
    	<div class="title"><span class="fr" onclick="CloseX()"><img src="images/closex.png" /></span><a href="#" onclick="firstall()">专题图层</a> > 物</div>
    	<div class="h_20"></div>
		<ul>
			<li class="GreenBg">
				<p class="pic"><img src="images/icon01.png" /></p>
				<p class="word AlphaBack">党员</p>
			</li>
			<li class="YellowBg">
				<p class="pic"><img src="images/icon02.png" /></p>
				<p class="word AlphaBack">退休</p>
			</li>
			<li class="CyanBg">
				<p class="pic"><img src="images/icon03.png" /></p>
				<p class="word AlphaBack">居家养老</p>
			</li>
			<li class="PrinkBg">
				<p class="pic"><img src="images/icon04.png" /></p>
				<p class="word AlphaBack">服兵役</p>
			</li>
			<li class="PurpleBg">
				<p class="pic"><img src="images/icon05.png" /></p>
				<p class="word AlphaBack">失业</p>
			</li>
		</ul>
        <div class="clear"></div>
	</div>
    <!-----------------------------------情------------------------------------->
    <div class="ztIcon AlphaBack situation">
    	<div class="title"><span class="fr" onclick="CloseX()"><img src="images/closex.png" /></span><a href="#" onclick="firstall()">专题图层</a> > 情</div>
    	<div class="h_20"></div>
		<ul>
			<li class="GreenBg">
				<p class="pic"><img src="images/icon01.png" /></p>
				<p class="word AlphaBack">党员</p>
			</li>
			<li class="YellowBg">
				<p class="pic"><img src="images/icon02.png" /></p>
				<p class="word AlphaBack">退休</p>
			</li>
			<li class="CyanBg">
				<p class="pic"><img src="images/icon03.png" /></p>
				<p class="word AlphaBack">居家养老</p>
			</li>
			<li class="PrinkBg">
				<p class="pic"><img src="images/icon04.png" /></p>
				<p class="word AlphaBack">服兵役</p>
			</li>
			<li class="PurpleBg">
				<p class="pic"><img src="images/icon05.png" /></p>
				<p class="word AlphaBack">失业</p>
			</li>
		</ul>
        <div class="clear"></div>
	</div>
</div>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script src="js/jquery.mCustomScrollbar.concat.min.js"></script>
</body>
</html>

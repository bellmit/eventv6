<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>综治信息管理</title>
<LINK rel=stylesheet type=text/css href="${rc.getContextPath()}/theme/frame/css/public.css">
<LINK rel=stylesheet type=text/css href="${rc.getContextPath()}/theme/frame/css/frame.css">
<SCRIPT type=text/javascript>
    var preClassName = "man_nav_1";
    function list_sub_nav(Id, sortname) {
        if (preClassName != "") {
            getObject(preClassName).className = "bg_image";
        }
        if (getObject(Id).className == "bg_image") {
            getObject(Id).className = "bg_image_onclick";
            preClassName = Id;
            showPosition(sortname);
            window.top.frames['leftFrame'].outlookbar.getbytitle(sortname);
            window.top.frames['leftFrame'].outlookbar.getdefaultnav(sortname);
        }
        window.top.frames['myFrame'].cols = "210,11,*";
        //window.top.frames['midFrame'].showOnChange(true);
    }
    
    function showPosition() {
        var tmp = "<li class=\"title\">当前位置：</li>";;
        for (var i = 0; i < arguments.length; i++) {
            if (i == 0) {
                tmp += "<li class=\"first\">" + arguments[0] + "</li>";
            } else if (i > 0) {
                tmp += "<li class=\"normal\">" + arguments[i] + "</li>";
            }

        }
        getObject('showtext').innerHTML = tmp;
    }

    //获取对象属性兼容方法
    function getObject(objectId) {
        if (document.getElementById && document.getElementById(objectId)) {
            // W3C DOM
            return document.getElementById(objectId);
        } else if (document.all && document.all(objectId)) {
            // MSIE 4 DOM
            return document.all(objectId);
        } else if (document.layers && document.layers[objectId]) {
            // NN 4 DOM.. note: this won't find nested layers
            return document.layers[objectId];
        } else {
            return false;
        }
    }
    function funrefresh() {
        //window.top.frames['MainFrame'].location.reload();
        //document.getElementById("tip").innerHTML = "页面重新加载中...";
        //document.getElementById("tip").style.display = "";
        window.top.frames['topFrame'].getObject("CFun").disabled = true;
        window.top.frames['mainFrame'].location.reload();
    }
    window.onload = function() {
        //document.getElementById("CFun").style.left=(document.body.clientWidth-213)+"px";
    }
</SCRIPT>
<SCRIPT type=text/javascript>
    function logout() {
        if (confirm("确定要退出平台吗？")) window.top.location = 'logout'
    }
</SCRIPT>
</head>

<body>
	<div id="tip" style="display:none;">
		加载中...
    </div>
    <div style="DISPLAY: none" id="loading">
        <IMG id="loadimg" src="${rc.getContextPath()}/theme/frame/images/loading.gif">
    </div>
    <div id="top">
    	
    	<div id="logo">
            <img src="${rc.getContextPath()}/theme/frame/images/logo.png">
        </div>
        <div id="nav">
            <ul>
                <li id="man_nav_1" class="bg_image_onclick" onclick="list_sub_nav(id, '地理信息')">地理信息</li>
                <li id="man_nav_2" class="bg_image" onclick="list_sub_nav(id, '事件处理')">事件处理</li>
                <li id="man_nav_3" class="bg_image" onclick="list_sub_nav(id, '日常工作')">日常工作</li>
                <li id="man_nav_4" class="bg_image" onclick="list_sub_nav(id, '系统设置')">系统设置</li>
                <li id="man_nav_5" class="bg_image" onclick="list_sub_nav(id, '城市综管')">城市综管</li>
            </ul>
        </div>
       
        <div id="info">
            <div class="person">
                <P class="photo">
                    <IMG src="${rc.getContextPath()}/theme/frame/images/photo.jpg">
                </P>
                <P class="admin">
                </P>
            </div>
            <div class="tip">
                <STRONG>${userInfo.partyName}</STRONG><BR>登录成功！<br/>${version}
                <!--
                <A onclick="logout()" href="javascript:void(0)">退出平台</A>
                -->
            </div>
        </div>
    </div>
    <div id="subline">
    	 <div id="CFun">
        	<BUTTON title="重新载入当前页面" onclick="funrefresh();">刷新页面</BUTTON>
   		 </div>
        <div class="download">
            <ul>
                <li>
                	欢迎使用社会管理网格化信息系统！
                </li>
            </ul>
        </div>
        <div class="position">
            <ul id="showtext">
                <li class="title">当前位置：</li>
                <li class="first">首页</li>
            </ul>
        </div>
    </div>
</body>
</html>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/js/jquery-easyui-1.4/themes/default/easyui.css">


<#include "/szzg/common.ftl" />
    <style>
        .titleDiv{
            background: url(/css/map_jxhgt/images/lywin_36.png) repeat-x;
            line-height: 60px;
            text-align: left;
            padding-right: 10px;
			max-height:64px; 
			border-bottom:solid 1px rgba(255, 255, 255, 0.65);
        }
        .titleDiv ul {
            margin:0 auto;

            padding: 0;
            list-style: none;
        }
        .titleDiv ul li{
            padding: 0 15px;
            width: 18%;
            height: 30px;
            line-height: 30px;
            background: url(/css/map_jxhgt/images/lywin_36.png) repeat-x;
            list-style: none;
            text-align: center;
            font-family: "微软雅黑";
            font-size: 13px;
            cursor: pointer;
            overflow: hidden; white-space: nowrap; text-overflow: ellipsis;
            padding-right: 20px;
            display:inline-block !important; display:inline;
            border-radius: 12px;
 			border: solid 1px #258CF3;
    		color: rgba(255, 255, 255, 0.65);
    		//box-shadow: inset 0px 0px 12px #00c3ff;
        }
		.titleDiv >li:hover{
			border: solid 1px #00c3ff;
			color: #fff;
			box-shadow: inset 0px 0px 12px #00c3ff;
		}
        .titleDiv ul li.current {
            color: #fff;
            border: solid 1px #00c3ff;
			box-shadow: inset 0px 0px 12px #00c3ff;
        }

        .con{
          	margin-top: 10px;
            height:345px;
            width: 545px;
            padding: 10px;
            font-size: 14px;
            line-height: 34px;
            overflow: auto;
            white-space:normal;
            word-break:break-all;
            text-align:left;
            border-top:1px;
        }

        .text{

        }
        .bgs{
        	border-radius: 0px;
        	padding:0px;
        }
        .bgc{
        	background-color: rgba(0, 0, 0, 0);
        	padding:0px;
        }
    </style>
</head>
<body  class="npmap" style="text-align:center">

<div  class="npAlarminfo citybgbox bpsmain bgs" style="width:570px;margin-left:280px;height:420px;background-color: rgba(3, 52, 134, 0)">

    <div id="right"  class="key-con bgc"  style=" width: 568px; margin-left: 0px;height: 440px;border: none;">
        <div id="content-d"  style="height: 400px;"  >
            <div class='titleDiv' id="titleDiv">
                <ul id="title" ></ul>
            </div>
            <div id="content" class='con'>
            </div>
        </div>

    </div><!--end .bpscon-->


</div><!--end .bpsmain-->


</body>

<script type="text/javascript">

    $(function () {
        $("#titleDiv").mCustomScrollbar({theme: "minimal-dark"});
        getContent(${treeId})
    })
    function getContent(treeid) {
        $.ajax({
            type: 'POST',
            dataType: "json",
            url: '${rc.getContextPath()}/zhsq/szzg/emeryencyplan/findByTreeId.json?t=' + Math.random(),
            data: {treeid: treeid},
            success: function (data) {
                if(data['content'].length!=0)
                {
                    var title = "", content = "";
                    for (var i = 0, l = data['content'].length; i < l; i++) {
                        title += "<li id='title_" + i + "' title='"+data['content'][i].title+"' onclick='setTab(" + i + "," + l + ")' class=''>" + data['content'][i].title + "</li>";
                        content += "<div  id='con_" + i + "' style='display: none;'>" + data['content'][i].txtStr + "</div>";

                    }

                    $("#title").html(title);//, $("#content").html(eval("+content+"));
                    document.getElementById('content').innerHTML = content;
                    $("#content").mCustomScrollbar({theme: "minimal-dark"});
                    setTab(0, data.length);
                }
                else
                {
                    noData('right');
                    return;
                }




            }
        });
    }
    function setTab(cursel, n) {
        for (var i = 0; i < n; i++) {
            var menu = document.getElementById("title_" + i);
            var con = document.getElementById("con_" + i);
            menu.className = "";
            con.style.display = "none";

        }

        document.getElementById("title_" + cursel).className = "current";
        document.getElementById("con_" + cursel).style.display = "block";

    }



</script>

</html >

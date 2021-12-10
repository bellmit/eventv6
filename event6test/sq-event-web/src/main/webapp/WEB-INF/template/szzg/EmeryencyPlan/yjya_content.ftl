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

        }
        .titleDiv ul {
            margin:0 auto;

            padding: 0;
            list-style: none;
        }
        .titleDiv ul li{
            border:1px solid #81a1b9;

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
        }

        .titleDiv ul li.current {
            background: #258CF3;
            color: #fff;
        }

        .con{
          margin-top: 20px;
            height:300px;
            width: 530px;
            padding: 10px;
            font-size: 14px;
            line-height: 34px;
            overflow: auto;
            white-space:normal;
            word-break:break-all;
            text-align:left
        }

        .text{

        }
    </style>
</head>
<body  class="npmap" style="text-align:center">

<div  class="npAlarminfo citybgbox bpsmain" style="width:580px;margin-left:280px;height:420px">

    <div id="right"  class="key-con"  style=" width: 550px; margin-left: 5px;height: 400px;">
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
        $("#content-d").mCustomScrollbar({theme: "minimal-dark"});
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

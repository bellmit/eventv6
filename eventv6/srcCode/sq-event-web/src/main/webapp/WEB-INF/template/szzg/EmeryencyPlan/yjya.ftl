<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>应急预案</title>

    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/js/jquery-easyui-1.4/themes/default/easyui.css">


<#include "/szzg/common.ftl" />
    <style>
        .abs-right_name{ width: 200px;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;}
        .npcityBottom ul li{
            float: none;
            text-align: left;
            line-height: 1;
        }
    </style>
</head>
<body onload="loadGrid()" class="npmap" style="text-align:center;background: rgba(30,64,89,0.75);">
<div class="npcityMainer">
    <div class="npcityBottom" style="bottom:0;position: relative;left: 0px;">

        <div class="npAlarminfo citybgbox bpsmain" style="width:240px;height:420px;margin-left: 10px" >
            <div style="height: 400px;left: 0px;border: #fffbff;border: 1">
                <div style="overflow:auto;height: 400px;width: 240px;margin-top: 20px;border: 1" id="ui_div">
                    <ul id="planTree" style="width: width: 200px" class="easyui-tree"
                        data-options="animate:'true',onClick :treeClick">
                    </ul>
                </div>
            </div>
            <div class="clearfloat"></div>

        </div><!--end .bpsmain-->


        <iframe id="content" name="content" style="width:100%;height:460px;"  marginwidth=0 marginheight=0  frameborder=0></iframe>


    </div>
</div>
</body>

<script type="text/javascript">
    //添加滚动条样式

    $("#ui_div").mCustomScrollbar({theme: "minimal-dark"});

    var tree;

    function loadGrid() {
        $.ajax({
            type: 'POST',
            dataType: "json",
            url: '${rc.getContextPath()}/zhsq/szzg/emeryencyplan/fingdTree.json?t=' + Math.random(),
            success: function (data) {
                //data.push({id:0,parentId:'-1',name:'genmulu'});
                tree = $("#planTree");
                tree.tree({data: arry2TreeFormat(data),formatter:function(node){
                    return "<div class='abs-right_name' title='"+node.text+"'>"+node.text+"</div>";
                }});
                //var node = tree.tree('find', 0);

                //tree.tree('expand', node.target);
            }
        });
    }

    function arry2TreeFormat(sNodes) {
        var r = [], tmpMap = [], id = "id", pid = "parentId", children = "children";
        for (var i = 0, l = sNodes.length; i < l; i++) {
            sNodes[i].text = sNodes[i].name;
            sNodes[i].title = sNodes[i].name;
            tmpMap[sNodes[i][id]] = sNodes[i];
        }
        for (var i = 0, l = sNodes.length; i < l; i++) {
            if (tmpMap[sNodes[i][pid]] && sNodes[i][id] != sNodes[i][pid]) {
                if (!tmpMap[sNodes[i][pid]][children]) {
                    tmpMap[sNodes[i][pid]].state = "closed";
                    tmpMap[sNodes[i][pid]][children] = [];
                }
                tmpMap[sNodes[i][pid]][children].push(sNodes[i]);
            } else {
//			sNodes[i].state =  "closed";
                r.push(sNodes[i]);
            }
        }
        return r;
    }

    function treeClick(node) {

        var l = tree.tree("getChildren", node.target).length
        if (l > 0) {//有子列表
            tree.tree('expand', node.target);
        } else {
            $('#content').attr("src","${rc.getContextPath()}/zhsq/szzg/emeryencyplan/framePage.jhtml?treeId="+node.id)
        }
    }

</script>

</html >

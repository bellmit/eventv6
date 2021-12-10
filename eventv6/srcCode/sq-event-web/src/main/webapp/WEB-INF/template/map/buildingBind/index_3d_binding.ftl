<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="Access-Control-Allow-Origin" content="*">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>楼宇轮廓编辑</title>
    <#include "/component/commonFiles-1.1.ftl" />
    <#global ffcs=JspTaglibs["/WEB-INF/tld/RightTag.tld"] >
</head>
<style type="text/css">
    .detailCol{color:blue; text-decoration:underline;}
    .aaa{background:#c1392b; border-radius:3px; padding:2px 3px; line-height:12px; color:#fff; margin-left:7px; cursor:default; display:inline-block;}
    ‍.aaa‍:hover{background:#e84c3d;}
    .panel-body{overflow:hidden;}
    .LeftTree{width:360px;}
    .LeftTree .search {width:336px;}
    .LeftTree .con li.current {background:#fff;}
    .LeftTree .SearchBox {width:26px;height:26px}
    .NorPage {width:345px;}
    .yema{width:295px;}
</style>
<body class="easyui-layout">

<div id="map_" region="center" border="false" style="width:100%; overflow:hidden;">
    <iframe data-iframe="true" name="MapIframe" id="MapIframe" src="" style="width:100%;height:100%;" frameborder="0" allowtransparency="true"></iframe>
</div>

<div region="west" split="false" border="false" title="<span class='easui-layout-title'>楼宇</span>	" style="width:360px; background:#f4f4f4; border-right:5px solid #d8d8d8;">
    <input type="hidden" id="pageSize" value="20" />
    <input name="buildingId" id="buildingId" type="hidden"/>
    <div class="LeftTree">
        <div class="search" style="height:60px;">
            <ul>
                <li>
                    <input type="hidden" id="gridId" name="gridId" value="">
                    <input type="hidden" id="infoOrgCode" name="infoOrgCode" value="">
                    <input name="gridName" id="gridName" type="text" class="inp1 InpDisable" value="<#if startGridName??>${startGridName}</#if>" style="width:155px;"/>

                    <input id="buildingName" name="buildingName" style="width:160px;" class="inp1 InpDisable" type="text" onkeydown="if (event.keyCode==13) {}" onblur="if(this.value=='')value='查询楼宇名称';" onfocus="if(this.value=='查询楼宇名称')value='';" value="查询楼宇名称">
                </li>
                <li style="margin-top:5px;">
                    <label class="LabName" style="width:100px;"><span>楼宇绑定情况：</span></label>
                    <select name="isbind" id="isbind" class="sel1" style="width:95px;float:left;">
                        <option value="-1">不限</option>
                        <option value="0">未绑定</option>
                        <option value="1">已绑定</option>
                    </select>

                    <span class="SearchBox" style="float:right;">
	        			<span onclick="leftSearch()" class="SearchBtn"></span>
	        		</span>
                </li>
            </ul>
        </div>
        <div id="content-d" class="con content light">
        </div>
        <div class="NorPage">
            <ul>
                <li class="PreBtn"><a href="javascript:change('1');"><img src="${rc.getContextPath()}/ui/images/pre3.png" /></a></li>
                <li class="yema">
                    共 <span id="pagination-num">0</span>/<span id="pageCount">0</span> 页
                    共<span id="records">0</span>条
                </li>
                <li class="NextBtn"><a href="javascript:change('2');"><img src="${rc.getContextPath()}/ui/images/next3.png" /></a></li>
            </ul>
        </div>
    </div>
</div>

<#include "/component/ComboBox.ftl">
<script type="text/javascript">
    var gridId1 = "${startGridId?c}";
    var orgCode = "${defaultInfoOrgCode}";
    var gridName1 = "${startGridName!''}";
    var defaultInfoOrgCode = "${defaultInfoOrgCode}";

    $(document).ready(function(){
        $("#content-d").css("height",$(document).height() - 135);//减菜单高度
    });
    $(function(){
        AnoleApi.initGridZtreeComboBox("gridName", "gridId", function (gridId, items){
            if(items!=undefined && items!=null && items.length>0){
                var grid = items[0];
                $("#infoOrgCode").val(grid.orgCode);
                $("#buildingName").val("查询楼宇名称");
                loadMessage(1,$('#pageSize').val());
            }
        });


        loadMessage(1,$('#pageSize').val());

        loadMap($('#buildingId').val(),gridId1);
        var options = {
            axis : "yx",
            theme : "minimal-dark"
        };
        enableScrollBar('content-d',options);
    })

    function loadMessage(pageNo,pageSize){
        var list = null;
        idStr = "";
        var postData = {};
        var buildingName = $("#buildingName").val();
        if(buildingName!=null && buildingName!="" && buildingName !="查询楼宇名称") {
            postData["buildingName"]=buildingName;
        }
        var gridId = $("#gridId").val();
        if(gridId!=null && gridId!="") {
            postData["gridId"]=gridId;
        }else{
            postData["gridId"]=gridId1;
        }
        var infoOrgCode = $("#infoOrgCode").val();
        if(infoOrgCode!=null && infoOrgCode!="") {
            postData["infoOrgCode"]=infoOrgCode;
        }else{
            postData["infoOrgCode"]=defaultInfoOrgCode;
        }
        var isBind;
        isBind = $("#isbind").val();
        postData["isBind"]=isBind;

        postData["page"] = pageNo;
        postData["rows"] = pageSize;
        $.ajax({
            type: "POST",
            url:'${rc.getContextPath()}/zhsq/map/buildBindController/buildingListData3d.json',
            data: postData,
            dataType:"json",
            success: function(data){
                $('#pagination-num').text(pageNo);
                $('#records').text(data.total);
                var totalPage = Math.floor(data.total/pageSize);
                if(data.total%pageSize>=0) totalPage+=1;
                $('#pageCount').text(totalPage);
                list=data.rows;
                var tableBody="";
                tableBody+='';
                if(list && list.length>0) {
                    tableBody+='<ul style="width:355px;">';
                    for(var i=0;i<list.length;i++){
                        var val=list[i];
                        var buildingAddress = val.buildingAddress;
                        var buildingAddressFull = val.buildingAddress;
                        if(buildingAddress!=null){
                            if(buildingAddress.length>42){
                                buildingAddress = buildingAddress.substring(0,41)+"...";
                            }
                        }else{
                            buildingAddress = '暂无地址';
                        }
                        var buildingName = val.buildingName;
                        if(buildingName!=null && buildingName!="" && buildingName.length>12){
                            buildingName = "["+buildingName.substring(0,12)+"...]";
                        }else if(buildingName==null){
                            buildingName = "";
                        }else{
                            buildingName = "["+buildingName+"]";
                        }

                        tableBody+='<li ';
                        if(i==0){

                        }
                        if(buildingAddress == "暂无地址"){
                            buildingAddressFull = "暂无地址";
                        }

                        tableBody+='onclick="selectRow('+val.buildingId+','+val.oid+',\''+val.buildingName+'\',this)"><div  title="['+val.buildingName+'] : '+buildingAddressFull+'"><span class="FontDarkBlue">'+buildingName+'</span> '+buildingAddress+'</div></li>';
                    }
                    tableBody+='</ul>';
                } else {
                    tableBody+='<div class="nodata" style="width: 360px;text-align: center;"></div>';
                }
                $(".LeftTree .mCSB_container").html(tableBody);
                if(list.length > 0){
                    $('#buildingId').val(list[0].buildingId);
                }else{
                    $('#buildingId').val(0);
                }
            },
            error:function(data){
                var tableBody='<div class="liebiao"><ul>数据读取错误！！！</ul></div>';
                $("#content-md").html(tableBody);
            }
        });
    }

    //分页
    function change(_index){
        var flag;
        var pagenum = $("#pagination-num").text();
        var lastnum = $("#pageCount").text();
        var pageSize = $("#pageSize").val();
        var firstnum = 1;
        switch (_index) {
            case '1':		//上页
                if(pagenum==1){
                    flag=1;
                    break;
                }
                pagenum = parseInt(pagenum) - 1;
                pagenum = pagenum < firstnum ? firstnum : pagenum;
                break;
            case '2':		//下页
                if(pagenum==lastnum){
                    flag=2;
                    break;
                }
                pagenum = parseInt(pagenum) + 1;
                pagenum = pagenum > lastnum ? lastnum : pagenum;
                break;
            case '3':
                flag=3;
                pagenum=1;
                break;
            case '4':
                pagenum = inputNum;
                if(pagenum==lastnum){
                    flag=4;
                    break;
                }
                pagenum = parseInt(pagenum);
                pagenum = pagenum > lastnum ? lastnum : pagenum;
                break;
            default:
                break;
        }

        if(flag==1){
            alert("当前已经是首页");
            return;
        }else if(flag==2){
            alert("当前已经是尾页");
            return;
        }
        loadMessage(pagenum,pageSize);
    }

    function searchData(flag) {
        var a = new Array();
        var buildingName = $("#buildingName").val();
        if(buildingName!=null && buildingName!="") a["buildingName"]=buildingName;
        var buildingId = $("#buildingId").val();
        if(buildingId!=null && buildingId!="") a["buildingId"]=buildingId;
        var infoOrgCode = $("#infoOrgCode").val();
        if(infoOrgCode!=null && infoOrgCode!="") {
            a["infoOrgCode"]=infoOrgCode;
        } else {
            a["infoOrgCode"]="${defaultInfoOrgCode}";
        }
    }

    //左侧列表选择
    function selectRow(buildId, oid, buildName,obj){
        $('#buildingId').val(buildId);
        $(".LeftTree .mCSB_container ul li").removeClass("current");
        $(obj).addClass("current");
        window.MapIframe.document.getElementById("buildingId").value = buildId;
        window.MapIframe.document.getElementById("currentOid").value = oid;
        window.MapIframe.document.getElementById("buildingName").value = buildName;
        //楼宇高亮并定位
        window.MapIframe.showBuilding(buildId);
    }
    //左侧查询
    function leftSearch(){
        loadMessage(1,$('#pageSize').val());
    }
    function loadMap(){
        var url = '${rc.getContextPath()}/zhsq/map/buildBindController/toBindMap.jhtml';
        $("#MapIframe").attr("src",url);
    }

    function isChooseMaptype(value){
        if(value != -1){
            $("#showMaptype").css("display","block");
        }else{
            $("#showMaptype").css("display","none");
        }
    }

</script>

<#include "/component/maxJqueryEasyUIWin.ftl" />

</body>
</html>
<!DOCTYPE html>
<html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>驰名商标列表</title>

    <script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>

    <script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
    <style type="text/css">
        .showRecords ul li{text-align:left; float:left; padding:5px 10px; line-height:18px;}
    </style>
</head>
<body style="border:none;scolling:yes">
<input type="hidden" id="orgCode" value="${orgCode!''}" />
<input type="hidden" id="gridId" value="${gridId!''}" />
<input type="hidden" id="elementsCollectionStr" value="<#if elementsCollectionStr??>${elementsCollectionStr}</#if>" />
<input type="hidden" id="pageSize" value="20" />

<div class="" style="display:block;">
    <!--<div class="title"><span class="fr" onclick="CloseX()"><img src="${rc.getContextPath()}/theme/standardmappage/images/closex.png" /></span><span class="fr" onclick="SearchBtn()"><img src="${rc.getContextPath()}/theme/standardmappage/images/search.png" /></span><a href="#" onclick="firstall()">专题图层</a> > <a href="#" onclick="people()">人</a> > 党员</div>-->
    <div class="ListSearch">
        <div class="condition">
            <ul>
                <li class="LC1">企业名称：</li>
                <li class="LC2"><input id="unitName" name="unitName" type="text" class="inp1" /></li>
            </ul>
            <ul>
                <li class="LC1">企业地址：</li>
                <li class="LC2"><input id="unitAddress" name="unitAddress" type="text" class="inp1" /></li>
            </ul>
            <ul>
                <li class="LC1">&nbsp;</li>
                <li class="LC2"><input name="" type="button" value="查询" class="NorBtn" onclick="loadMessage(1,$('#pageSize').val());"/></li>
            </ul>
            <div class="clear"></div>
        </div>
        <div class="CloseBtn" onclick="CloseSearchBtn()"></div>
    </div>
    <div class="showRecords">
        <ul>
            <li>共查询到<span id="records">0</span>条记录</li>
        </ul>
    </div>
    <div class="ListShow content" style="" id="content">

    </div>
    <div class="NorPage">
    <#include "/map/arcgis/standardmappage/common/standard_page.ftl" />
    </div>
</div>
<script type="text/javascript">
    var inputNum;
    function pageSubmit(){
        inputNum = $("#inputNum").val();
        var pageCount = $("#pageCount").text();
        if(isNaN(inputNum)){
            inputNum=1;
        }
        if(parseInt(inputNum)>parseInt(pageCount)){
            inputNum=pageCount;
        }
        if(inputNum<=0||inputNum==""){
            inputNum=1;
        }
        change('4');
    }
    $('#unitName,#gridName').keydown(function(e){
        if(e.keyCode==13){
            loadMessage(1,$("#pageSize").val());
        }
    });
    function ShowOrCloseSearchBtn(){
        var temp= $(".ListSearch").is(":hidden");//是否隐藏
        if(temp == false) {
            $(".ListSearch").hide();
        }else {
            $(".ListSearch").show();
        }

    }
    function CloseSearchBtn(){
        $(".ListSearch").hide();
    }
    $(document).ready(function(){
        var winHeight=window.parent.document.getElementById('map'+window.parent.currentN).offsetHeight-62;
        $("#content").height(winHeight-56);
        loadMessage(1,$("#pageSize").val());

    });
    var results="";//获取定位对象集合
    function loadMessage(pageNo,pageSize){
        results="";
        var orgCode = $('#orgCode').val();
        var gridId = $('#gridId').val();
        var unitName = $('#unitName').val();
        var unitAddress = $('#unitAddress').val();
        if(unitName=="==输入查询内容==") unitName="";
        var pageSize = $("#pageSize").val();
        $.blockUI({message: "加载中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'20%',
            background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
        var postData = 'page='+pageNo+'&rows='+pageSize+'&orgCode='+orgCode+'&unitName='+unitName+'&unitAddress='+unitAddress+'&gridId='+gridId;
        $.ajax({
            type: "POST",
            url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofgoods/trademarkListData.json?t='+Math.random(),
            data: postData,
            dataType:"json",
            success: function(data){
                $.unblockUI();
                //设置页面页数
                $('#pagination-num').text(pageNo);
                $('#records').text(data.total);
                var totalPage = Math.floor(data.total/pageSize);
                if(data.total%pageSize>0) totalPage+=1;
                $('#pageCount').text(totalPage);
                var list=data.rows;
                var tableBody="";
                tableBody+='<div class="liebiao">';
                if(list && list.length>0) {
                    for(var i=0;i<list.length;i++){
                        var val=list[i];

                        tableBody+='<dl onclick="selected(\''+val.trademarkId+'\',\''+(val.unitName==null?'':val.unitName)+'\')">';
                        tableBody+='<dt>';
                        tableBody+='<b class="FontDarkBlue">'+(val.unitName==null?'':val.unitName)+'</b>';
                        tableBody+='</dt>';
                        tableBody+='<dd>地址：'+(val.unitAddress==null?'':val.unitAddress)+'</dd>';
                        tableBody+='</dl>';

                        results=results+","+val.trademarkId;
                    }
                    results=results.substring(1, results.length);
                } else {
                    tableBody+='<div style="text-align: center;"><img style="width: 174px;" src="${uiDomain!''}/images/map/gisv0/special_config/images/nodata.png" title="暂无数据"/></div>';
                }
                tableBody+='</div>';
                $("#content").html(tableBody);
                $(".AdvanceSearch").css("display","none");
                gisPosition(results);
            },
            error:function(data){
                $.unblockUI();
                var tableBody='<div class="liebiao"><ul>数据读取错误！！！</ul></div>';
                $("#content").html(tableBody);
            }
        });
        CloseSearchBtn();
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

    $("#moreSearch").toggle(function(){
        $(".AdvanceSearch").css("display","block");
    },function(){
        $(".AdvanceSearch").css("display","none");
    });

    function selected(id, name){
        //gisPosition(id);

        setTimeout(function() {
            window.parent.getDetailOnMapOfListClick($('#elementsCollectionStr').val(),320,210,id);
        },1000);

    }
    //地图定位
    function gisPosition(res){

        if (res==""){
            return ;
        }
        var gisDataUrl="${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofgoods/getArcgisTrademarkLocateDataList.jhtml?markType=${markType!''}&ids="+res;
        window.parent.currentLayerLocateFunctionStr="getArcgisDataOfZhuanTi('"+gisDataUrl+"','"+$('#elementsCollectionStr').val()+"')";
        window.parent.getArcgisDataOfZhuanTi(gisDataUrl,$('#elementsCollectionStr').val(),320,210);
    }
</script>
</body>
</html>
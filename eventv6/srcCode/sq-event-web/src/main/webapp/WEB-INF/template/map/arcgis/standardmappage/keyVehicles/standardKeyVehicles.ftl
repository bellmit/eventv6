<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>特色产业列表</title>

    <script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>

    <script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
    <script src="${ZZGRID_DOMAIN!''}/js/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
	<#include "/component/ComboBox.ftl" />
    <style type="text/css">
        .recYearDiv{
            float: left;
            padding:0px 0px 0px 5px;
            line-height : 26px;
        }
    </style>
</head>
<body style="border:none">
<input type="hidden" id="gridId" value="${gridId?c}" />
<input type="hidden" id="orgCode" value="${orgCode!''}" />
<input type="hidden" id="elementsCollectionStr" value="<#if elementsCollectionStr??>${elementsCollectionStr}</#if>" />
<input type="hidden" id="pageSize" value="50" />
<div class="" style="display:block;">
    <!--<div class="title"><span class="fr" onclick="CloseX()"><img src="${rc.getContextPath()}/theme/standardmappage/images/closex.png" /></span><span class="fr" onclick="SearchBtn()"><img src="${rc.getContextPath()}/theme/standardmappage/images/search.png" /></span><a href="#" onclick="firstall()">专题图层</a> > <a href="#" onclick="people()">人</a> > 党员</div>-->
    <div class="ListSearch">
        <div class="condition">
            <ul>
                <li class="LC1">类型：</li>
                <li>
                    <select class="sel1" name="bizType" id="bizType" style="width:165px;">
                        <option value="">-- 全部 --</option>
                        <option value="vehicle">机动车</option>
                        <option value="unlicensedMotorVehicle">无牌机动车</option>
                        <option value="nonMotorVehicle">非机动车</option>
                    </select>
                </li>
            </ul>
            <div class="SearchBtn"><input name="" type="button" value="查询" class="NorBtn" onclick="loadMessage(1,$('#pageSize').val(),'searchBtn');" /></div>
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
    var isFirstTime = 1;
    $(function() {
        var winHeight=window.parent.document.getElementById('map'+window.parent.currentN).offsetHeight-62;
        $("#content").height(winHeight-56);
        loadMessage(1,$("#pageSize").val());
    });

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

    function ShowOrCloseSearchBtn(){
        var temp= $(".ListSearch").is(":hidden");//是否隐藏
        if(temp == false) {
            $(".ListSearch").hide();
        }else {
            $(".ListSearch").show();
        }

    }
    $('#gridName').keydown(function(e){
        if(e.keyCode==13){
            loadMessage(1,$("#pageSize").val());
        }
    });
    function CloseSearchBtn(){
        $(".ListSearch").hide();
    }
    var results="";//获取定位对象集合
    var layerName="";
    function loadMessage(pageNo,pageSize,searchType){
        layerName = window.parent.analysisOfElementsCollection($('#elementsCollectionStr').val(),"menuLayerName");
        window.parent.currentLayerName = layerName;
        if('searchBtn'==searchType) {
            window.parent.clearSpecialLayer(layerName);
            window.parent.currentListNumStr = "";
        }
        results="";
        var gridId = $('#gridId').val();
        var orgCode = $('#orgCode').val();
        var bizType = $("#bizType option:selected").val();
        var pageSize = $("#pageSize").val();
        $.blockUI({message: "加载中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'20%',
            background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
        var postData = 'page='+pageNo+'&rows='+pageSize+'&gridId='+gridId+'&orgCode='+orgCode+'&bizType='+bizType;
        $.ajax({
            type: "POST",
            url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofthing/keyVehiclesListData.json?t='+Math.random(),
            data: postData,
            dataType:"json",
            success: function(data){
                var elementsCollectionStr = $('#elementsCollectionStr').val();
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
                    tableBody+='<ul>';
                    for(var i=0;i<list.length;i++){
                        var val=list[i];

                        tableBody+='<dl onclick="selected(\''+val.kvId+'\',\''+val.bizType+'\')">';
                        tableBody+='<dt>';
                        if(val.kvCarNum != null){
                            tableBody+='<b class="FontDarkBlue">车牌号码：'+val.kvCarNum+'</b>';
                        }else{
                            if(val.kvCarFeatures != null){
                                var kvCarFeatures = val.kvCarFeatures;
                                if(kvCarFeatures.length>10){
                                    kvCarFeatures =kvCarFeatures.substring(1, 9);
                                }
                                tableBody+='<b class="FontDarkBlue">车辆特征：'+val.kvCarFeatures+'</b>';
                            }
                        }
                        tableBody+='</dt>';
                        tableBody+='<dd>'+(val.regionName==null?'':val.regionName)+'</dd>';
                        tableBody+='</dl>';

                        results=results+","+val.kvId;

                    }
                    tableBody+='</ul>';
                    results=results.substring(1, results.length);
                } else {
                    tableBody+='<div style="text-align: center;"><img style="width: 174px;" src="${uiDomain!''}/images/map/gisv0/special_config/images/nodata.png" title="暂无数据"/></div>';
                }
                tableBody+='</div>';
                $("#content").html(tableBody);
                $(".AdvanceSearch").css("display","none");
                gisPosition(results,$('#elementsCollectionStr').val(), 'yes');
            },
            error:function(data){
                $.unblockUI();
                var tableBody='<div class="liebiao"><ul>数据读取错误！！！</ul></div>';
                $("#content").html(tableBody);
            }
        });
        CloseSearchBtn();
    }
    var currentPageNum=1;
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
        currentPageNum = pagenum;
        loadMessage(pagenum,pageSize);
    }

    $("#moreSearch").toggle(function(){
        $(".AdvanceSearch").css("display","block");
    },function(){
        $(".AdvanceSearch").css("display","none");
    });
    function selected(id, bizType){
        window.parent.clearGridLayer();
        var params = {
            "bizType" : bizType
        };

        setTimeout(function() {
            if(elementsCollectionStr != "") {
                window.parent.getDetailOnMapOfListClick($('#elementsCollectionStr').val(), null, null, id, undefined, undefined, params);
            }else {
                window.parent.localtionGridsPoints(id,'');
            }
        },1000);
    }

    //地图定位
    function gisPosition(res, elementsCollectionStr, isClearLayer){
        if(typeof(isClearLayer) != 'undefined' && isClearLayer != null && isClearLayer == 'yes'){
            if("1" != window.parent.IS_ACCUMULATION_LAYER) {
                window.parent.clearSpecialLayer(layerName);
            }else {
                if(window.parent.currentListNumStr.indexOf(currentPageNum+"")>=0) {
                    //return;
                }else {
                    window.parent.currentListNumStr = window.parent.currentListNumStr+","+currentPageNum;
                }
            }
        }
        if (res==""){
            return ;
        }
        if(elementsCollectionStr) {
            var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofthing/getArcgisDataOfKeyVehiclesListByIds.jhtml?ids="+res+"&showType=2";
            window.parent.currentLayerLocateFunctionStr="getArcgisDataOfZhuanTi('"+url+"','"+elementsCollectionStr+"')";
            window.parent.getArcgisDataOfZhuanTi(url,elementsCollectionStr, 360, 248, isClearLayer);
        }
    }

</script>
</body>
</html>
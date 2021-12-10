<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>全球眼列表</title>


    <script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>

    <script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
    <#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
    <#include "/component/ComboBox.ftl">
    <style type="text/css">
        .ztree li span{
            font-size: 12px;
        }
        .backgroundColorDiv{
            background-color: #172c58 !important;
        }
        .FontDarkBlue{
            color:#deefff;
            width:130px;
            overflow:hidden;
        }
        .liebiao dl{
            cursor:pointer;
            border-bottom: 1px solid #71a0ff;
            padding: 5px 0;
            width:100%;
            color:#041944;
            opacity:90%;
        }
        .liebiao dl:hover{
            background:#000004;
        }
    </style>
</head>
<body style="border:none;">
<input type="hidden" id="orgCode" value="${orgCode}" />
<input type="hidden" id="elementsCollectionStr" value="<#if elementsCollectionStr??>${elementsCollectionStr}</#if>" />
<input type="hidden" id="pageSize" value="20" />
<div class="" style="display:block;opacity:92%;">
    <div class="ListSearch">
        <div class="condition">
            <ul>
                <li class="LC1">名称：</li>
                <li class="LC2"><input id="platformName" name="platformName" type="text" class="inp1" /></li>
            </ul>
            <ul>
                <li class="LC1">类型管控：</li>
                <li class="LC2">
                    <input id="eyesType" name="eyesType" type="hidden" class="inp1" />
                    <input id="eyesTypeStr" name="eyesTypeStr" type="text" class="inp1" style="width:185px"/>
                </li>
            </ul>
            <ul>
                <li class="LC1">&nbsp;</li>
                <li class="LC2"><input name="" type="button" value="查询" class="NorBtn" onclick="loadMessage(1,$('#pageSize').val(),'searchBtn');"/></li>
            </ul>
            <div class="clear"></div>
        </div>
        <div class="CloseBtn" onclick="CloseSearchBtn()"></div>
    </div>
    <div class="showRecords backgroundColorDiv" style="border-bottom:#172c58">
        <ul>
            <li style="color: #ecff6e;">共查询到<span id="records" style="color:#17f676">0</span>条记录</li>
        </ul>
    </div>
    <div class="ListShow content backgroundColorDiv" style="border-left:none" id="content">

    </div>
    <div class="NorPage">
        <#include "/map/arcgis/standardmappage/common/standard_page.ftl" />
    </div>
</div>
<div class="videoview">
    <iframe width=0 height=0 id="url"></iframe>
</div>
<script type="text/javascript">
    var inputNum;
    $(function(){
        AnoleApi.initTreeComboBox("eyesTypeStr", "eyesType", "A002001", null, null, {
            ShowOptions : {
                EnableToolbar : true
            }
        });

    });


    function pageSubmit() {
        inputNum = $("#inputNum").val();
        var pageCount = $("#pageCount").text();
        if (isNaN(inputNum)) {
            inputNum = 1;
        }
        if (parseInt(inputNum)>parseInt(pageCount)) {
            inputNum = pageCount;
        }
        if (inputNum <= 0 || inputNum == "") {
            inputNum = 1;
        }
        change('4');
    }
    $('#platformName').keydown(function(e){
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
//var temp1= $(".ListSearch").is(":visible");//是否可见

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
    var results2="";//获取ffcs全球眼定位对象集合
    var layerName="";
    function loadMessage(pageNo,pageSize,searchType){
        layerName = window.parent.analysisOfElementsCollection($('#elementsCollectionStr').val(),"menuLayerName");
        window.parent.currentLayerName = layerName;
        if('searchBtn'==searchType) {
            window.parent.clearSpecialLayer(layerName);
            window.parent.currentListNumStr = "";
        }
        results="";
        results2="";
        var orgCode = $('#orgCode').val();
        var platformName = $('#platformName').val();
        if(platformName=="==输入查询内容==") platformName="";
        var pageSize = $("#pageSize").val();
        var eyesType = $('#eyesType').val();
        $.blockUI({message: "加载中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'20%',
                background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
        var postData = 'page='+pageNo+'&rows='+pageSize+'&orgCode='+orgCode+'&platformName='+platformName+"&eyesType="+eyesType;
        $.ajax({
            type: "POST",
            url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/globalEyesListData.json?t='+Math.random(),
            data: postData,
            dataType:"json",
            success: function(data){
                $.unblockUI();
                //设置页面页数
                $('#pagination-num').text(pageNo);
                var total = data==null?0:data.totalCount;
                $('#records').text(total);
                var totalPage = Math.floor(total/pageSize);
                if(total%pageSize>0) totalPage+=1;
                $('#pageCount').text(totalPage);
                var list=data==null?null:data.list;
                var tableBody="";
                tableBody+='<div class="liebiao">';
                if(list && list.length>0) {
                    for(var i=0;i<list.length;i++){
                        var val=list[i];

                        var userTypeLabel = '';
                        if(val.useType!=null) {
                            if(val.useType=="1") userTypeLabel="社区";
                            else if(val.useType=="2") userTypeLabel="旅游";
                            else if(val.useType=="3") userTypeLabel="交通";
                        }
                        if (val.companyType == "4") {
                            var dataJson = JSON.stringify(val).replace(/\"/g,"'");
                            tableBody+='<dl onclick="showHkRow('+dataJson+')">';
                        } else {
                            tableBody+='<dl onclick="selected(\''+val.monitorId+'\',\''+(val.platformName==null?'':val.platformName)+'\',\''+(val.companyType==null?'':val.companyType)+'\',\''+(val.channelName==null?'':val.channelName)+'\',\''+(val.rtsp==null?'':val.rtsp)+'\',\''+(val.channelId==null?'':val.channelId)+'\')">';
                        }
                        tableBody+='<dt>';
                        tableBody+='<span class="fr">'+'</span>';
                        tableBody+='<b class="FontDarkBlue">'+(val.platformName==null?'':val.platformName)+'</b>';
                        tableBody+='</dt>';
                        tableBody+='<dd style="color:#95b8ff">'+(val.orgName==null?'':val.orgName)+'</dd>';
                        tableBody+='</dl>';
                        if(val.companyType != 20){
                            results=results+","+val.monitorId;
                        }else{//ffcs全球眼类型的是获取设备号
                            results2=results2+",'"+val.channelName+"'";
                        }
                    }
                    results=results.substring(1, results.length);
                    results2=results2.substring(1, results2.length);
                } else {
                    tableBody+='<div style="text-align: center;"><img style="width: 174px;" src="${uiDomain!''}/images/map/gisv0/special_config/images/nodata.png" title="暂无数据"/></div>';
                }
                tableBody+='</div>';
                $("#content").html(tableBody);
                $(".AdvanceSearch").css("display","none");
                gisPosition(results, results2,orgCode);
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

    function selected(id, name, type, channelName,rtsp,channelId){
        console.log(type);
        if (typeof parent.MMApi != "undefined") {	// 判断是否是高德地图
            var opt = {};
            opt.w = 475;
            opt.h = 406;
            opt.ecs = $('#elementsCollectionStr').val();
            return parent.MMApi.clickOverlayById(id, opt);
        }

//		setTimeout(function() {
//			window.parent.locationGlobalEyesPoint(id);
//		},1000);
        setTimeout(function() {
            if (type == 23 && $('#orgCode').val().indexOf("3507")>-1) {
                window.parent.gloybalPlay(channelId);
            } else if (rtsp=='1') {
                window.parent.showGlobalEyes(name, $('#elementsCollectionStr').val(), id);
            } else if(type != 14) {
                if ($('#elementsCollectionStr').val() != "") {
                    //window.parent.getDetailOnMapOfListClick($('#elementsCollectionStr').val(), 475, 406, id);
                    window.parent.showGlobalEyes(name, $('#elementsCollectionStr').val(), id);
                } else {
                    window.parent.locationGlobalEyesPoint(id);
                }
            }else{
                window.parent.showDHGlobalPlay(id);
            }
        },1000);
    }

    //地图定位
    function gisPosition(res, res20,orgCode){
        if (typeof parent.MMApi != "undefined") {	// 判断是否是高德地图
            var opt = {};
            opt.w = 475;
            opt.h = 406;
            opt.ecs = $('#elementsCollectionStr').val();
            opt.url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/getArcgisLocateDataListOfGlobalEyes.jhtml?ids="+res+"&showType=2";
            return parent.MMApi.markerIcons(opt, "globalEyes");
        }

        if("1" != window.parent.IS_ACCUMULATION_LAYER) {
            window.parent.clearSpecialLayer(layerName);
        }else {
            if(window.parent.currentListNumStr.indexOf(currentPageNum+"")>=0) {
                //return;
            }else {
                window.parent.currentListNumStr = window.parent.currentListNumStr+","+currentPageNum;
            }
        }
        if (res==""){
            return ;
        }
        <#--if(typeof(type) != 'undefined' && type != null && type == 20){-->
        <#--var qqyurl= "${rc.getContextPath()}/zhsq/alarm/videoSurveillanceController/getArcgisLocateDataListOfGlobalEyes.jhtml?ids="+res+"&showType=2";-->
        <#--window.parent.currentLayerLocateFunctionStr="getArcgisDataOfGlobalEyes('"+qqyurl+"')";-->
        <#--window.parent.getArcgisDataOfGlobalEyes(qqyurl, false);-->
        <#--}else{-->
        if($('#elementsCollectionStr').val() != "") {
            var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/getArcgisLocateDataListOfGlobalEyes.jhtml?ids="+res+"&showType=2";
            if(res20 != null && res20 != ""){
                url = url +"&ids20="+res20
            }
            if(orgCode.indexOf('3507')>-1 && orgCode.length >=9){//镇级撒点
                url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/getArcgisLocateDataListOfGlobalEyes.jhtml?ids=&showType=2&orgCode="+orgCode;
                window.parent.clearSpecialLayer("globalEyesLayer");
            }
            window.parent.currentLayerLocateFunctionStr="getArcgisDataOfZhuanTi('"+url+"','"+$('#elementsCollectionStr').val()+"')";
            window.parent.getArcgisDataOfZhuanTi(url,$('#elementsCollectionStr').val());
        }else {
            var qqyurl= "${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/getArcgisLocateDataListOfGlobalEyes.jhtml?ids="+res+"&showType=2";
            window.parent.currentLayerLocateFunctionStr="getArcgisDataOfGlobalEyes('"+qqyurl+"')";
            window.parent.getArcgisDataOfGlobalEyes(qqyurl);
        }
//		}
    }

    function showHkRow(dataJson) {
        var PalyType = "PlayReal";
        var SvrIp = dataJson.platformIp;
        var SvrPort = dataJson.port;
        var appkey = dataJson.platformUsername;
        var appSecret = dataJson.channelId;
        var time = dataJson.channelIp;
        var timeSecret = dataJson.platformPassword;
        var httpsflag = "1";
        var CamList = dataJson.platformId;

        var param = 'hikvideoclient://ReqType:' + PalyType + ';' + 'VersionTag:artemis' + ';' + 'SvrIp:' + SvrIp + ';' + 'SvrPort:' + SvrPort + ';' + 'Appkey:' + appkey + ';' + 'AppSecret:' + appSecret + ';' + 'time:' + time + ';' + 'timesecret:' + timeSecret + ';' + 'httpsflag:' + httpsflag + ';' + 'CamList:' + CamList + ';';
        document.getElementById("url").src = param;
    }
</script>
</body>
</html>
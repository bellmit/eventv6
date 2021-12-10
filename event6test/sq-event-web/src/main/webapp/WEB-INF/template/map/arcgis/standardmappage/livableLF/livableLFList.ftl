<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>宜居罗坊-列表</title>


    <script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
    <script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
</head>
<body style="border:none;scolling:yes">

<input type="hidden" id="type" value="${livableType}"/>
<input type="hidden" id="orgCode" value="${orgCode}"/>
<input type="hidden" id="pageSize" value="100" />
<input type="hidden" id="elementsCollectionStr" value="<#if elementsCollectionStr??>${elementsCollectionStr}</#if>"/>


<div class="" style="display:block;">
    <!--<div class="title"><span class="fr" onclick="CloseX()"><img src="${rc.getContextPath()}/theme/standardmappage/images/closex.png" /></span><span class="fr" onclick="SearchBtn()"><img src="${rc.getContextPath()}/theme/standardmappage/images/search.png" /></span><a href="#" onclick="firstall()">专题图层</a> > <a href="#" onclick="people()">人</a> > 党员</div>-->
    <div class="ListSearch">
        <div class="condition">
            <ul>
                <li class="LC1">名称：</li>
                <li class="LC2">
                    <input id="name" name="name" value="" type="text" class="inp1"/>
                </li>
                <li class="LC1">信息概况：</li>
                <li class="LC2">
                    <input id="survey" name="survey" value="" type="text" class="inp1"/>
                </li>
            </ul>
            <ul>
                <li class="LC1">&nbsp;</li>
                <li class="LC2"><input name="" type="button" value="查询" class="NorBtn"
                                       onclick="loadMessage(1,$('#pageSize').val(),'searchBtn');"/></li>
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
    <div class="ListShow content"  id="content">

    </div>

</div>
<script type="text/javascript">
var isExpire = "false";
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

    function ShowOrCloseSearchBtn() {
        var temp = $(".ListSearch").is(":hidden");//是否隐藏
        if (temp == false) {
            $(".ListSearch").hide();
        } else {
            $(".ListSearch").show();
        }
//var temp1= $(".ListSearch").is(":visible");//是否可见

    }

    function CloseSearchBtn() {
        $(".ListSearch").hide();
    }

    $(document).ready(function () {
        var winHeight = window.parent.document.getElementById('map' + window.parent.currentN).offsetHeight - 62;
        $("#content").height(winHeight - 56);
        loadMessage(1, $("#pageSize").val());

    });
    var results = "";//获取定位对象集合
    var layerName = "";

    function loadMessage(pageNo, pageSize, searchType) {
        layerName = window.parent.analysisOfElementsCollection($('#elementsCollectionStr').val(), "menuLayerName");
        window.parent.currentLayerName = layerName;
        if ('searchBtn' == searchType) {
            window.parent.clearSpecialLayer(layerName);
            window.parent.currentListNumStr = "";
        }
        results = "";
        var gridId = $('#gridId').val();
        var type = '${type}'
        var orgCode=$('#orgCode').val();
        var name = $('#name').val();
        var survey = $('#survey').val();
        $.blockUI({
            message: "加载中...", css: {
                width: '150px', height: '50px', lineHeight: '50px', top: '40%', left: '20%',
                background: 'url(${rc.getContextPath()}/css/loading.gif) no-repeat', textIndent: '20px'
            }, overlayCSS: {backgroundColor: '#fff'}
        });


        $.ajax({
            type: "POST",
            url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofeventlocal/livableLFListData.json?t=' + Math.random(),
            data: {livableType: $('#type').val(), page: 1, rows: 100,orgCode: orgCode,name: name, survey: survey,page:pageNo},
            dataType: "json",
            success: function (data) {
                $.unblockUI();
                //设置页面页数
                $('#pagination-num').text(pageNo);
                $('#records').text(data.total);
                var totalPage = Math.floor(data.total / pageSize);
                if (data.total % pageSize > 0) totalPage += 1;
                $('#pageCount').text(totalPage);
                var list = data.rows;
                var tableBody = "";
                tableBody += '<div class="liebiao">';
                if (list && list.length > 0) {
                    for (var i = 0; i < list.length; i++) {
                        var val = list[i];

                        tableBody += '<dl onClick="selected(\'' + val.id + '\',\'' + (val.name == null ? '' : val.name) + '\')">';
                        tableBody += '<dt>';
                        tableBody += '<span class="fr">' + '</span>';
                        tableBody += '<b class="FontDarkBlue" >' + (val.name == null ? '' : val.name) + '</b>';
                        tableBody += '</dt>';
                        tableBody += '<dd style="height: 30px">' +
                                (val.survey == null ? '' :
                                        ('<span class="fr" title=' + val.survey + ' style="display: block;width: 200px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">' + (val.survey == null ? '' : val.survey)) + '</span>') + '</dd>';
                        tableBody += '</dl>';
                        results = results + "," + val.id;
                    }
                    results = results.substring(1, results.length);
                } else {
                    tableBody += '<ul>未查到相关数据！！</ul>';
                }
                tableBody += '</div>';
                $("#content").html(tableBody);
                $(".AdvanceSearch").css("display", "none");
                gisPosition(results);
            },
            error: function (data) {
                $.unblockUI();
                var tableBody = '<div class="liebiao"><ul>数据读取错误！！！</ul></div>';
                $("#content").html(tableBody);
            }
        });
        CloseSearchBtn();
    }

    function selected(id, name) {
        if (typeof parent.MMApi != "undefined") {	// 判断是否是高德地图
            var opt = {};
            opt.w = 300;
            opt.h = 150;
            opt.ecs = $('#elementsCollectionStr').val();
            opt.gridId = $('#gridId').val();
            return parent.MMApi.clickOverlayById(id, opt);
        }

        setTimeout(function () {
            if ($('#elementsCollectionStr').val() != "") {
                window.parent.getDetailOnMapOfListClick($('#elementsCollectionStr').val(), 300, 140, id)
            }
        }, 1000);

    }

    //--定位
    function gisPosition(res) {

        if (res == "") {
            return;
        }
        if ($('#elementsCollectionStr').val() != "") {
            var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofeventlocal/getArcgisLocateDataListOfLivableLF.jhtml?ids=" + res;
            window.parent.currentLayerLocateFunctionStr = "getArcgisDataOfZhuanTi('" + url + "','" + $('#elementsCollectionStr').val() + "')";
            window.parent.getArcgisDataOfZhuanTi(url, $('#elementsCollectionStr').val(), 650, 320);
        } else {
            var gisDataUrl = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofeventlocal/getArcgisLocateDataListOfLivableLF.jhtml?type=${livableType!''}&ids=" + res;
            var type = '${livableType}';
            window.parent.currentLayerLocateFunctionStr = "getArcgisDataOfSociety('" + gisDataUrl + "','" + type + "')";
            window.parent.getArcgisDataOfSociety(gisDataUrl, type);
        }
    }
</script>
</body>
</html>
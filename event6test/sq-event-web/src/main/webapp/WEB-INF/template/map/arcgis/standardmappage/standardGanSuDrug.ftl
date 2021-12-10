<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>甘肃禁毒人员列表</title>
    <#include "/component/commonFiles-1.1.ftl" />
    <script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
    <script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
    <#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
    <#include "/component/ComboBox.ftl">
    <script src="${rc.getContextPath()}/js/layer/3.1.1/layer.js"></script>
</head>
<body style="border:none;">
<input type="hidden" id="gridId" value="${gridId?c}"/>
<input type="hidden" id="infoOrgCode" value="${infoOrgCode!''}"/>
<input type="hidden" id="SQ_ZZGRID_URL" value="${SQ_ZZGRID_URL}"/>
<input type="hidden" id="elementsCollectionStr" value="<#if elementsCollectionStr??>${elementsCollectionStr}</#if>"/>
<input type="hidden" id="pageSize" value="20"/>

<div class="" style="display:block;">
    <div class="ListSearch">
        <div class="condition">
            <ul>
                <li class="LC1">人员现状：</li>
                <li class="LC2">
                    <input id="dType" name="dType" type="hidden" class="inp1"/>
                    <input id="dTypeStr" name="dTypeStr" type="text" class="inp1" style="width:130px"/>
                </li>
            </ul>
            <ul>
                <li class="LC1">输入姓名：</li>
                <li class="LC2"><input id="name" name="name" type="text" class="inp1"/></li>
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
    <div class="ListShow content" style="" id="content">
    </div>
    <div class="NorPage">
    <#include "/map/arcgis/standardmappage/common/standard_page.ftl" />
    </div>
</div>
<script type="text/javascript">
    $(function(){
        AnoleApi.initListComboBox("dTypeStr", "dType", "D121002", null, [""], {
            //RenderType : "01",
            ShowOptions : {
                EnableToolbar : true
            }
        });

    });
    var inputNum;

    function pageSubmit() {
        inputNum = $("#inputNum").val();
        var pageCount = $("#pageCount").text();
        if (isNaN(inputNum)) {
            inputNum = 1;
        }
        if (parseInt(inputNum) > parseInt(pageCount)) {
            inputNum = pageCount;
        }
        if (inputNum <= 0 || inputNum == "") {
            inputNum = 1;
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
    }
    function CloseSearchBtn() {
        $(".ListSearch").hide();
    }
    $(document).ready(function () {
        var winHeight = window.parent.document.getElementById('map' + window.parent.currentN).offsetHeight - 62;
        $("#content").height(winHeight-56);
        loadMessage(1, $("#pageSize").val());
    });
    var results = "";//获取定位对象集合
    var layerName = "";
    function loadMessage(pageNo, pageSize, searchType) {
        if ($('#elementsCollectionStr').val() != "") {
            layerName = window.parent.analysisOfElementsCollection($('#elementsCollectionStr').val(), "menuLayerName");
            window.parent.currentLayerName = layerName;
        }

        if ('searchBtn' == searchType) {
            window.parent.clearSpecialLayer(layerName);
            window.parent.currentListNumStr = "";
        }
        results = "";
        var gridId = $('#gridId').val();
        var infoOrgCode = $('#infoOrgCode').val();
        var name = $("#name").val();
        if (name == "") name = "";
        var dType = $("#dType").val();
        if (dType == "") dType = "";
        var order = $("#order option:selected").val();
        $.blockUI({
            message: "加载中...", css: {
                width: '150px', height: '50px', lineHeight: '50px', top: '40%', left: '20%',
                background: 'url(${rc.getContextPath()}/css/loading.gif) no-repeat', textIndent: '20px'
            }, overlayCSS: {backgroundColor: '#fff'}
        });
        var postData = '';
        if ($('#elementsCollectionStr').val() != "") {
            postData = 'page=' + pageNo + '&rows=' + pageSize + '&gridId=' + gridId + '&infoOrgCode=' + infoOrgCode + '&name=' + name + '&order=' + order + '&dType=' + dType + '&elementsCollectionStr=' + $('#elementsCollectionStr').val();
        } else {
            postData = 'page=' + pageNo + '&rows=' + pageSize + '&gridId=' + gridId + '&name=' + name + '&order=' + order + '&dType=' + dType;
        }
        $.ajax({
            type: "POST",
            url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofpoplocal/getArcgisDataListOfOfGanSuDrug.json',
            data: postData,
            dataType: "json",
            success: function (data) {
                $.unblockUI();
                //设置页面页数
                $('#pagination-num').text(pageNo);
                if (pageNo > 1) {
                    data.total = parseInt($('#records').text());
                }
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
                        var gender = val.gender;
                        var imageGenderSpan = "";
                        if (gender == null) {
                            gender = "";
                        } else if (gender == 'M' || gender == '男') {
                            imageGenderSpan += '<img src="${uiDomain!''}/images/map/gisv0/special_config/images/man.png">';
                        } else {
                            imageGenderSpan += '<img src="${uiDomain!''}/images/map/gisv0/special_config/images/woman.png">';
                        }
                        tableBody += '<dl onclick="selected(\'' + val.partyId + '\',\'' + val.controlDesc + '\')">';
                        tableBody += '<dt>';
                        tableBody += '<span class="fr">' + (val.birthday == null ? '' : val.birthday) + '</span>';
                        tableBody += '<b class="FontDarkBlue" id="pepList_'+val.partyId+'">' + (val.name == null ? '' : val.name) + '&nbsp;' + imageGenderSpan + '</b>';
                        tableBody += '</dt>';
                        tableBody += '<dd>'
                        + (val.identityCard == null ? '' : '<img src="${uiDomain!''}/images/map/gisv0/special_config/images/people_03.png">' + val.identityCard) + '</dd>';
                        tableBody += '</dl>';
                        results = results + "," + val.partyId;

                    }
                    results = results.substring(1, results.length);
                } else {
                    tableBody += '<ul>未查到相关数据！！</ul>';
                }
                tableBody += '</div>';
                $("#content").html(tableBody);
                $(".AdvanceSearch").css("display", "none");
                gisPosition(results, "");
            },
            error: function (data) {
                $.unblockUI();
                var tableBody = '<div class="liebiao"><ul>数据读取错误！！！</ul></div>';
                $("#content").html(tableBody);
            }
        });
        CloseSearchBtn();
    }
    var currentPageNum = 1;
    //分页
    function change(_index) {
        var flag;
        var pagenum = $("#pagination-num").text();
        var lastnum = $("#pageCount").text();
        var pageSize = $("#pageSize").val();
        var firstnum = 1;
        switch (_index) {
            case '1':		//上页
                if (pagenum == 1) {
                    flag = 1;
                    break;
                }
                pagenum = parseInt(pagenum) - 1;
                pagenum = pagenum < firstnum ? firstnum : pagenum;
                break;
            case '2':		//下页
                if (pagenum == lastnum) {
                    flag = 2;
                    break;
                }
                pagenum = parseInt(pagenum) + 1;
                pagenum = pagenum > lastnum ? lastnum : pagenum;
                break;
            case '3':
                flag = 3;
                pagenum = 1;
                break;
            case '4':
                pagenum = inputNum
                if (pagenum == lastnum) {
                    flag = 4;
                    break;
                }
                pagenum = parseInt(pagenum);
                pagenum = pagenum > lastnum ? lastnum : pagenum;
                break;
            default:
                break;
        }

        if (flag == 1) {
            alert("当前已经是首页");
            return;
        } else if (flag == 2) {
            alert("当前已经是尾页");
            return;
        }
        currentPageNum = pagenum;
        loadMessage(pagenum, pageSize);
    }

    $("#moreSearch").toggle(function () {
        $(".AdvanceSearch").css("display", "block");
    }, function () {
        $(".AdvanceSearch").css("display", "none");
    });
    
    /**
	 * 加载人员定位后的回调函数
	 * @param data
	 */
	var pepLocalMap = [];
	function loadPeopleLocateCallBack(data) {
		if(data != null && data.length >0){
			for (var i=0;i<data.length;i++){
	            pepLocalMap.push({
					"ciRsId" : data[i].ciRsId,
					"partyId" : data[i].partyId,
	                "name" : data[i].name,
	                "address" : data[i].address,
	                "wid" : data[i].wid
	            });
			}
		}
	}

    var ss;
    var tipObj;
    function selected(id, controlDesc) {
        var wid = id;
        if(pepLocalMap != null){
            var pepAddressArr = [];
            var pepAddrHTML = "<dd>请选择地址（现居住地址）：</dd>";
            for(var i=0; i< pepLocalMap.length;i++){
                if(id == pepLocalMap[i].partyId){
                    pepAddressArr.push(pepLocalMap[i]);
                    pepAddrHTML += "<dd><a href='#' onclick='showPepDetail(\""+pepLocalMap[i].wid+"\")'>"+ pepLocalMap[i].address+"</a></dd>"
                }
            }
            pepAddrHTML = "<div id='pepAddrList' style='max-height: 300px;overflow-y:auto'><dl>"+pepAddrHTML+"</dl></div>";
            if(pepAddressArr != null && pepAddressArr.length >1){
                tipObj = layer.tips(pepAddrHTML, "#pepList_"+id+"", {
                    tips: [1, 'rgba(120, 186, 50, 1)'], //还可配置颜色
                    area: '240px',
                    time: 0,
                    shade: 0.1,
                    shadeClose: true
                });
            }else{
                if(pepAddressArr != null && pepAddressArr.length == 1){
                    wid = pepAddressArr[0].wid;
                }
                showPepDetail(wid, controlDesc);
            }
        }
    }


    function showPepDetail(wid, controlDesc){
        if (typeof parent.MMApi != "undefined") {	// 判断是否是高德地图
            var opt = {};
            opt.w = 400;
            opt.h = 236;
            opt.ecs = $('#elementsCollectionStr').val();
            opt.gridId = $('#gridId').val();
            return parent.MMApi.clickOverlayById(wid, opt);
        }
        var elementsCollectionStr = "";
        elementsCollectionStr = $('#elementsCollectionStr').val();

        var gridId = $('#gridId').val();
        setTimeout(function () {
            if ($('#elementsCollectionStr').val() != "") {
                window.parent.getDetailOnMapOfListClick(elementsCollectionStr, 400, 236, wid, gridId)
            } else {
                window.parent.getPopDetailOnMapOfListClick('', wid, gridId);
            }
            if(typeof tipObj != 'undefined'){
                layer.close(tipObj);
            }
        }, 1000);
    }
    //--定位
    function gisPosition(res) {
        if (typeof parent.MMApi != "undefined") {	// 判断是否是高德地图
            var opt = {};
            opt.w = 400;
            opt.h = 236;
            opt.ecs = $('#elementsCollectionStr').val();
            opt.gridId = $('#gridId').val();
            opt.url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofpoplocal/getArcgisLocateDataListOfPeople.jhtml?userIds=" + res + "&showType=2";
            return parent.MMApi.markerIcons(opt);
        }

        if ("1" != window.parent.IS_ACCUMULATION_LAYER) {
            window.parent.clearSpecialLayer(layerName);
        } else {
            if (window.parent.currentListNumStr.indexOf(currentPageNum + "") >= 0) {
                //return;
            } else {
                window.parent.currentListNumStr = window.parent.currentListNumStr + "," + currentPageNum;
            }
        }

        if (res == "") {
            return;
        }
        var elementsCollectionStr = "";
        elementsCollectionStr = $('#elementsCollectionStr').val();
        if (elementsCollectionStr != "") {
            var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofpoplocal/getArcgisLocateDataListOfGanSuDrug.jhtml?userIds=" + res;
            window.parent.currentLayerLocateFunctionStr = "getArcgisDataOfZhuanTi('" + url + "','" + elementsCollectionStr + "')";
            window.parent.getArcgisDataOfZhuanTi(url, elementsCollectionStr, 400, 236, null, null, null, null, null, undefined, loadPeopleLocateCallBack);
        }
    }
</script>
</body>
</html>
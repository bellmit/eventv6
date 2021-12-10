<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>协警地址列表</title>
    <#include "/component/commonFiles-1.1.ftl" />

</head>
<style type="text/css">
    .detailCol {
        color: blue;
        text-decoration: underline;
    }

    .aaa {
        background: #c1392b;
        border-radius: 3px;
        padding: 2px 3px;
        line-height: 12px;
        color: #fff;
        margin-left: 7px;
        cursor: default;
        display: inline-block;
    }

    ‍.aaa‍:hover {
        background: #e84c3d;
    }

    .panel-body {
        overflow: hidden;
    }

    .LeftTree {
        width: 300px;
    }

    .LeftTree .search {
        height: 25px;
        width: 280px;
    }

    .LeftTree .SearchBox {
        width: 26px;
        height: 26px
    }

    .NorPage {
        width: 300px;
    }

    .yema {
        width: 235px;
    }

    .selectRow {
        background-color: #79a8ff;
    }

    .LeftTree .con li:hover {
        background-color: #79a8ff;
    }

</style>
<body class="easyui-layout">
<input type="hidden" name="x" id="x" value="${(x?c)!}"/>
<input type="hidden" name="y" id="y" value="${(y?c)!}"/>
<input type="hidden" name="gridId" id="gridId" value="${(gridId)!}"/>
<input type="hidden" name="mapt" id="mapt" value="${(mapt)!}"/>
<input type="hidden" name="callBackUrl" id="callBackUrl" value="${(callBackUrl)!}"/>
<input type="hidden" name="mapType" id="mapType" value="${(mapType)!}"/>
<input type="hidden" name="isEdit" id="isEdit" value="${(isEdit)!}"/>
<input type="hidden" name="infoOrgCode" id="infoOrgCode" value="${(infoOrgCode)!}"/>
<input type="hidden" name="showMap" id="showMap" value="${(showMap)!}" />
<input type="hidden" name="targetDownDivId" id="targetDownDivId" value="${(targetDownDivId)!}"/>


<div id="map_" region="center" border="false" style="width:100%; overflow:hidden;">
    <iframe data-iframe="true" name="MapIframe" id="MapIframe" src="" style="width:100%;height:100%;" frameborder="0"
            allowtransparency="true"></iframe>
</div>
<#if isEdit?? && isEdit=="false">
<#elseif isEdit?? && isEdit=="true" && nearbyAddressShow?? && nearbyAddressShow == "true">
    <div region="west" split="false" border="false"
         style="width:300px; background:#f4f4f4; border-right:5px solid #d8d8d8;">
        <input type="hidden" id="pageSize" value="20"/>
        <div class="LeftTree">
            <div class="search">
                <ul>
                    <li>
                        <input name="addressName" id="addressName" type="text" class="inp1 InpDisable"
                               placeholder="请输入地址名称"
                               value="${(address)!}"
                               style="width:209px;"/>
                        <span class="SearchBox" style="float:right;border-radius: 5px;">
                        <span onclick="listEndPointLocate()" class="checkBtn" title="确定" style="border-radius: 5px;"></span>
                        </span>
                        <span class="SearchBox" style="float:right;margin-right: 5px;border-radius: 5px;">
                        <span onclick="leftSearch()" class="SearchBtn" title="查询" style="border-radius: 5px;"></span>
                        </span>
                    </li>
                </ul>
            </div>
            <div id="content-d" class="con content light">
            </div>
            <div class="NorPage">
                <ul>
                    <li class="PreBtn"><a href="javascript:change('1');"><img
                                    src="${rc.getContextPath()}/ui/images/pre3.png"/></a></li>
                    <li class="yema">
                        共 <span id="pagination-num">0</span>/<span id="pageCount">0</span> 页
                        共<span id="records">0</span>条
                    </li>
                    <li class="NextBtn"><a href="javascript:change('2');"><img
                                    src="${rc.getContextPath()}/ui/images/next3.png"/></a></li>
                </ul>
            </div>
        </div>
    </div>
</#if>

<div style="height:0px">
    <iframe id="cross_domain_frame" name="cross_domain_frame" src="" marginwidth=0 marginheight=0 scrolling="no"
            frameborder=0></iframe>
</div>
<script type="text/javascript">

    var timer;

    $(document).ready(function () {
        $("#content-d").css("height", $(document).height() - 75);//减菜单高度
    });

    window.onresize = function () {
        setTimeout(function () {
            $("#content-d").css("height", $(document).height() - 75);//减菜单高度
            window.MapIframe.resizeMap($(document).width(), $(document).height())
        }, 1000);
    }
    

    $(function () {
		mapIframeModleOpen();
        setTimeout(loadArcgisMap(), 1000);

        enableScrollBar('content-d', {axis: "yx", theme: "minimal-dark"});//列表渲染

        initLeftListDiv();
    });

    function initLeftListDiv() {//初始化左侧列表数据 目前南安要求初始化时候不显示数据，通过查询来展示数据
        $('.LeftTree .mCSB_container').html('<div class="nodata" style="width: 174px;text-align: center;margin-top: 25%"></div>');
    }

    function getSelectParams(pageNo, pageSize) {
        var params = {};
        params['pageNo'] = pageNo ? pageNo : 1;
        params['pageSize'] = pageSize ? pageSize : 20;
        var addressName = $('#addressName').val();
        if (addressName) {
            params['searchStr'] = addressName;
        }
        var infoOrgCode = $('#infoOrgCode').val();
        if (infoOrgCode) {
            params['regionCode'] = infoOrgCode;
        }
        return params;
    }

    function loadMessage(pageNo, pageSize) {
        var params = getSelectParams(pageNo, pageSize);
        modleopen();
        $.ajax({
            type: 'POST',
            url: '${rc.getContextPath()}/zhsq/map/xiejingController/getOnlineStandardAddr.json',
            data: params,
            dataType: 'json',
            success: function (data) {
                modleclose();
                $('.LeftTree .mCSB_container').html('');
                $('#records').text(data.total);
                var totalPage = (data.total % pageSize >= 0) ? Math.floor(data.total / pageSize) + 1 : Math.floor(data.total / pageSize);
                $('#pagination-num').text(pageNo);
                $('#pageCount').text(totalPage);

                var tableBody = "";
                var list = data.rows;
                if (list && list.length > 0) {
                    tableBody += '<ul style="width:100%;">';
                    for (var i = 0; i < list.length; i++) {
                        var addressAllName = list[i].addressName;
                        var addressName = list[i].addressName;
                        var showX = list[i].x;
                        var showY = list[i].y;
                        tableBody += '<li ';
                        if (addressName != null && addressName.length > 22) {
                            addressName = addressName.substring(0, 22) + "...";
                        }
                        tableBody += 'onclick="selectRow(\'' + showX + '\',\'' + showY + '\',\'' + addressAllName + '\',this)" ondblclick="dbSelectRow(\'' + showX + '\',\'' + showY + '\',\'' + addressAllName + '\',this)" title="' + addressAllName + '"> ' + addressName + '</li>';
                    }
                    tableBody += '</ul>';
                } else {
                    tableBody += '<div class="nodata" style="width: 174px;text-align: center;margin-top: 25%"></div>';
                }
                $('.LeftTree .mCSB_container').html(tableBody);
            },
            error: function (data) {
                var tableBody = '<div class="nodata" style="width: 174px;text-align: center;margin-top: 25%"></div>';
                $('.LeftTree .mCSB_container').html(tableBody);
            }
        });
    }

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
                pagenum = inputNum;
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
            $.messager.alert('提示', '当前已经是首页！', 'info');
            return;
        } else if (flag == 2) {
            $.messager.alert('提示', '当前已经是尾页！', 'info');
            return;
        }
        loadMessage(pagenum, pageSize);
    }

    //左侧列表选择
    function selectRow(showX, showY, addressName, obj) {
        clearTimeout(timer);
        timer = setTimeout(function () {
            $(obj).addClass('selectRow').siblings().removeClass('selectRow');
            $('#addressName').val(addressName);
            window.MapIframe.changeCenterPoint(showX, showY);
        }, 300);
    }

    //左侧列表双击选择
    function dbSelectRow(showX, showY, addressName, obj) {
        clearTimeout(timer);
        var url = '${SQ_ZZGRID_URL}/zzgl/important/toArcgisCrossDomain.jhtml?' + data;
        var mapt = $('#mapt').val();
        var data = 'x=' + showX + '&y=' + showY + '&mapt=' + mapt;

        if (addressName) {
            data = data + '&address=' + addressName;
        }
        var targetDownDivId = '${(targetDownDivId)!}';
        if (targetDownDivId) {
            data = data + '&targetDownDivId=' + targetDownDivId;
        }

        var callBackUrl = $('#callBackUrl').val();
        if (callBackUrl) {
            url = callBackUrl + '?' + data;
        }

        window.document.getElementById('cross_domain_frame').src = url;
    }

    //左侧查询
    function leftSearch() {
        var pageSize = $('#pageSize').val()
        loadMessage(1, pageSize);
    }

    function listEndPointLocate() {
        window.MapIframe.endPointLocate();
    }

    function loadArcgisMap() {
        var url = '${rc.getContextPath()}/zhsq/map/xiejingController/xiejingMap.jhtml';
        var data = '';
        var targetDownDivId = '${(targetDownDivId)!}';
        if (targetDownDivId) {
            data += 'targetDownDivId=' + targetDownDivId;
        }
        var wgGisType= '${(wgGisType)!}';
        if (wgGisType) {
            data += '&wgGisType=' + wgGisType;
        }
        var wgGisId= '${(wgGisId)!}';
        if (wgGisId) {
            data += '&wgGisId=' + wgGisId;
        }
        var x = $('#x').val();
        if (x != '') {
            data += '&x=' + x;
        }
        var y = $('#y').val();
        if (y != '') {
            data += '&y=' + y;
        }
        var mapType = $('#mapType').val();
        if (mapType) {
            data += '&mapType=' + mapType;
        }
        var gridId = $('#gridId').val();
        if (gridId) {
            data += '&gridId=' + gridId;
        }
        var isEdit = $('#isEdit').val();
        if (isEdit) {
            data += '&isEdit=' + isEdit;
        }
        var infoOrgCode = $('#infoOrgCode').val();
        if (infoOrgCode) {
            data += '&infoOrgCode=' + infoOrgCode;
        }
        var callBackUrl = $('#callBackUrl').val();
        if (callBackUrl) {
            data += '&callBackUrl=' + callBackUrl;
        }
        var showMap = $('#showMap').val();
        if(showMap!=''){
            data += "&showMap=" + showMap;
        }
        if (data) {
            url += '?' + data;
        }
        $('#MapIframe').attr('src', url);
    }
    
    var mapIframe = document.getElementById('MapIframe');
	mapIframe.onload = mapIframe.onreadystatechange = function() {
	     if (this.readyState && this.readyState != 'complete') return;
	     else {
	         mapIframeModleClose();
	     }
	}

    
	function mapIframeModleOpen(){
		$("<div class='datagrid-mask' id='datagrid-mask'></div>").css( {
			display : "block",
			width : "100%",
			height : $(window).height()
		}).appendTo("#map_");
		$("<div class='datagrid-mask-msg' id='datagrid-mask-msg'></div>").html("正在处理，请稍候。。。").appendTo(
				"#map_").css( {
			display : "block",
			left : ($("#map_").outerWidth(true) - 190) / 2,
			top : ($(window).height() - 45) / 2
		});
	}
	
	//关闭遮罩
	function mapIframeModleClose() {
		$("#datagrid-mask").css( {
			display : "none"
		});
		$("#datagrid-mask-msg").css( {
			display : "none"
		});
		$("#datagrid-mask").remove();
		$("#datagrid-mask-msg").remove();
	}
</script>

<#include "/component/maxJqueryEasyUIWin.ftl" />

</body>
</html>
<!DOCTYPE html>
<html>
<head>
</head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>地图首页</title>
<#include "/component/commonFiles-1.1.ftl" />
<link rel="stylesheet" type="text/css" href="${uiDomain!''}/js/bootstrap/v3.1.1/css/bootstrap.min.css"" />
<link rel="stylesheet" type="text/css" href="${uiDomain!''}/js/bootstrap/v3.1.1/css/style.min.css"" />
<script type="text/javascript" src="${uiDomain}/js/bootstrap/v3.1.1/js/bootstrap.min.js"></script>
<style type="text/css">
    .nav-tabs>li>a {
        color: #A7B1C2;
        font-weight: 600;
        padding: 10px 0px 10px 5px;
        background-color: #f4f4f4;
    }
    .tabs-container .tabs-left>.nav-tabs>li>a, .tabs-container .tabs-right>.nav-tabs>li>a {
        min-width: 84px;
        margin-right: 0;
        margin-bottom: 0px;
        border-color:#d8d8d8;
    }
    .detailCol{color:blue; text-decoration:underline;}
    .aaa{background:#c1392b; border-radius:3px; padding:2px 3px; line-height:12px; color:#fff; margin-left:7px; cursor:default; display:inline-block;}
    ‍.aaa‍:hover{background:#e84c3d;}
    .panel-body{overflow:hidden;}
    .LeftTree{
        width:101%;
        border-right: 5px solid rgb(216, 216, 216);
    }
    .LeftTree .search {width:100%;}
    .LeftTree .con li {background:#f4f4f4;padding-right: 10px}
    .LeftTree .con li.current {background:#fff;}
    .LeftTree .SearchBox {width:28px;height:28px}
    .NorPage {width:330px;}
    .yema{width:265px;}
    .tabs-container .tabs-left .panel-body{
        width: 100%;
        margin-left: 0px;
    }



</style>
<body>
<input type="hidden" id="pageSize" value="20" />
    <input type="hidden" id="elementsCollectionStr" name="elementsCollectionStr">
    <div class="col-sm-6" style="padding-left: 0px;padding-right: 0px">
        <div class="tabs-container">
            <div class="tabs-left">
                <ul class="nav nav-tabs" style="margin-right: 0px;width:80px">
                    <li class="active" onclick="tabChange('building')">
                        <a data-toggle="tab" href="#tab-1">楼栋</a>
                    </li>
                    <li class="" onclick="tabChange('urbanParts')">
                        <a data-toggle="tab" href="#tab-2">城市部件</a>
                    </li>
                    <li class="" onclick="tabChange('grid')">
                        <a data-toggle="tab" href="#tab-3">网格</a>
                    </li>
                </ul>
                <div class="tab-content" style="margin-left: 85px;width:325px">
                    <div id="tab-1" class="tab-pane active">
                        <div class="panel-body" style="padding: 0px">
                            <div class="LeftTree" id="building">
                                <div class="search" style="height:50px;">
                                    <ul>
                                        <li>
                                            <input id="queryStr" name="queryStr" placeholder="查询楼宇名称/地址" style="width:235px;float:left;" class="inp1 InpDisable" type="text">
                                            <span class="SearchBox" style="float:left;margin-left:5px" title="查询">
                                                <span onclick="leftSearch('building')" class="SearchBtn"></span>
                                            </span>
                                        </li>
                                    </ul>
                                </div>
                                <div id="buildingContent" class="con content light">
                                </div>
                                <div class="NorPage">
                                    <ul>
                                        <li class="PreBtn"><a href="javascript:change('1','building');"><img src="${rc.getContextPath()}/ui/images/pre3.png" /></a></li>
                                        <li class="yema">
                                            共 <span id="building-pagination-num">0</span>/<span id="building-pageCount">0</span> 页
                                            共<span id="building-records">0</span>条
                                        </li>
                                        <li class="NextBtn"><a href="javascript:change('2','building');"><img src="${rc.getContextPath()}/ui/images/next3.png" /></a></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div id="tab-2" class="tab-pane">
                        <div class="panel-body" style="padding: 0px">
                            <div class="LeftTree" id="urbanParts">
                                <div class="search" style="height:80px;">
                                    <ul>
                                        <li>
                                            <input id="urbanPartsTypeId" name="urbanPartsTypeId" type="hidden">
                                            <input id="urbanPartsTypeCode" name="urbanPartsTypeCode" type="hidden">
                                            <input id="urbanPartsTypeName" name="urbanPartsTypeName" style="width:260px;float:left;" class="inp1 InpDisable" type="text">
                                        </li>
                                        <li>
                                            <input id="urbanPartsName" name="urbanPartsName" placeholder="查询资源名称" style="width:235px;float:left;margin-top: 5px" class="inp1 InpDisable" type="text">
                                            <span class="SearchBox" style="float:left;margin-left:5px;margin-top: 5px" title="查询">
                                                <span onclick="leftSearch('urbanParts')" class="SearchBtn"></span>
                                            </span>
                                        </li>
                                    </ul>
                                </div>
                                <div id="urbanPartsContent" class="con content light">
                                </div>
                                <div class="NorPage">
                                    <ul>
                                        <li class="PreBtn"><a href="javascript:change('1','urbanParts');"><img src="${rc.getContextPath()}/ui/images/pre3.png" /></a></li>
                                        <li class="yema">
                                            共 <span id="urbanParts-pagination-num">0</span>/<span id="urbanParts-pageCount">0</span> 页
                                            共<span id="urbanParts-records">0</span>条
                                        </li>
                                        <li class="NextBtn"><a href="javascript:change('2','urbanParts');"><img src="${rc.getContextPath()}/ui/images/next3.png" /></a></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div id="tab-3" class="tab-pane">
                        <div class="panel-body" style="padding: 0px">
                            <div class="LeftTree" id="grid">
                                <div class="search" style="height:50px;">
                                    <ul>
                                        <li>
                                            <label class="LabName" style="width:65px;"><span>轮廓绘制：</span></label>
                                            <select name="drawedType" id="drawedType" class="sel1" style="width:70px;float:left;" onchange="leftSearch('grid')">
                                                <option value="-1" selected>全部</option>
                                                <option value="0" >无</option>
                                                <option value="1" >有</option>
                                            </select>
                                        </li>
                                        <li>
                                            <label class="LabName" style="width:80px;"><span>中心点标注：</span></label>
                                            <select name="isMarker" id="isMarker" class="sel1" style="width:70px;float:left;" onchange="leftSearch('grid')">
                                                <option value="-1" selected>全部</option>
                                                <option value="0" >无</option>
                                                <option value="1" >有</option>
                                            </select>
                                        </li>
                                    </ul>
                                </div>
                                <div id="gridContent" class="con content light">
                                </div>
                                <div class="NorPage">
                                    <ul>
                                        <li class="PreBtn"><a href="javascript:change('1','grid');"><img src="${rc.getContextPath()}/ui/images/pre3.png" /></a></li>
                                        <li class="yema">
                                            共 <span id="grid-pagination-num">0</span>/<span id="grid-pageCount">0</span> 页
                                            共<span id="grid-records">0</span>条
                                        </li>
                                        <li class="NextBtn"><a href="javascript:change('2','grid');"><img src="${rc.getContextPath()}/ui/images/next3.png" /></a></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>
</body>
<#include "/component/ComboBox.ftl">
<script type="text/javascript">
    var isUseTwoTypesMap="<#if isUseTwoTypesMap??>${isUseTwoTypesMap}</#if>";
	var isMapLoaded = false;
    $(function(){
        $("#buildingContent").css("height",$(document).height() - 76);//减菜单高度
        $("#urbanPartsContent").css("height",$(document).height() - 109);//减菜单高度
        $("#gridContent").css("height",$(document).height() - 78);//减菜单高度


        $(".panel-body").css("height",$(document).height()-4);

        var options = {
            axis : "yx",
            theme : "minimal-dark"
        };

        AnoleApi.initTreeComboBox("urbanPartsTypeName", "urbanPartsTypeId", null, function(value, item){
            if(item != null){
                $("#urbanPartsTypeId").val(item[0].id);
                $("#urbanPartsTypeCode").val(item[0].typeCode);

            }
        }, ["02010301"], {
            RenderType : "10",
            ChooseType : "1",
            DataName : "name",		// 字典名称
            DataValue : "typeCode",	// 字典值
            DataCode : "id",		// 字典编码
            DataPCode  : "PId",		// 字典编码
            DataSrc: "${ZZGRID_URL!''}/zzgl/res/typeZTreeJsonp.jhtml?jsoncallback=?",
            OnSelected: function(value, treeNode){
                if(treeNode[0].targetUrl=='undefined' || treeNode[0].targetUrl==null || treeNode[0].targetUrl==""){
                    $("#urbanPartsTypeName").val("");
                    $("#urbanPartsTypeId").val("");
                    $("#urbanPartsTypeCode").val("");
                }
                else {
                    $("#urbanPartsTypeName").val(treeNode[0].name);
                    $("#urbanPartsTypeId").val(treeNode[0].id);
                    $("#urbanPartsTypeCode").val(treeNode[0].typeCode);
                }
                tabChange('urbanParts');
                if(typeof window.parent.MapIframe.hasUrbanPartsArcgisData == "function"){
                    window.parent.MapIframe.hasUrbanPartsArcgisData();
                }
                var currentModuleType = "";
                if(typeof window.parent.MapIframe.currentModuleType != 'undefined'){
                    currentModuleType = window.parent.MapIframe.currentModuleType.value;
                }

                if(currentModuleType == "urbanParts"){
                    loadUrbanPartsMessage(1,$('#pageSize').val());
                }

            },
            FilterData: function(data){
                var resuleData = [];
                var filterData = {
                    "02010301" : true,//公共厕所
                    "02010501" : true,//路灯
                    "02010601" : true,//消防栓
                    "02020401" : true,//公交车站
                    "020116" : true//全球眼
                }
                if(data != null && data.length >0){
                    for(var i=0;i<data.length;i++){
                        if(filterData[data[i].typeCode]){
                            resuleData.push(data[i]);
                        }
                    }
                    $("#urbanPartsTypeCode").val(data[0].id);
                    $("#urbanPartsTypeId").val(data[0].id);
                }
                return resuleData;
            }
        });



        enableScrollBar('buildingContent',options);
        enableScrollBar('urbanPartsContent',options);
        enableScrollBar('gridContent',options);
    });
    
    function mapLoadedCallBack() {
    	isMapLoaded = true;
    	window.parent.MapIframe.changeModule("building");
        loadBuildingMessage(1,$('#pageSize').val());//只加载楼宇列表
    }


    /**
     * ===========================building start====================
     */
    var buildingResults="";//获取定位对象集合

    function loadBuildingMessage(pageNo,pageSize){
        var list = null;
        var postData = {};
        var queryStr = $("#queryStr").val();
        if(queryStr!=null && queryStr!="" && queryStr !="查询楼宇名称/地址") {
            postData["queryStr"]=queryStr;
        }
        var gridId = parent.document.getElementById("gridId").value;
        if(gridId!=null && gridId!="") {
            postData["gridId"]=gridId;
        }
        var infoOrgCode = parent.document.getElementById("infoOrgCode").value;
        if(infoOrgCode!=null && infoOrgCode!="") {
            postData["infoOrgCode"]=infoOrgCode;
        }

        postData["page"] = pageNo;
        postData["rows"] = pageSize;
        buildingResults="";
        divModleOpen('buildingContent');
        $.ajax({
            type: "POST",
            url:'${rc.getContextPath()}/zhsq/map/mapDataMaintain/builgingListData.json',
            data: postData,
            dataType:"json",
            success: function(data){
                divModleClose();
                $('#building-pagination-num').text(pageNo);
                $('#building-records').text(data.total);
                var totalPage = Math.floor(data.total/pageSize);
                if(data.total%pageSize>=0) totalPage+=1;
                $('#building-pageCount').text(totalPage);
                list=data.rows;
                var tableBody="";
                tableBody+='';
                if(list && list.length>0) {
                    tableBody+='<ul style="width:320px;">';
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

                        tableBody+='onclick="selectRow(\'building\','+val.buildingId+',this)"><div  title="['+val.buildingName+'] : '+buildingAddressFull+'"><span class="FontDarkBlue">'+buildingName+'</span> '+buildingAddress+'</div></li>';
                        buildingResults = buildingResults+","+val.buildingId;
                    }
                    tableBody+='</ul>';
                    buildingResults=buildingResults.substring(1, buildingResults.length);

                } else {
                    tableBody+='<div class="nodata" style="width: 320px;text-align: center;"></div>';
                }
                $("#building .mCSB_container").html(tableBody);
                gisPositionByType("building",buildingResults);
            },
            error:function(data){
                var tableBody='<div class="liebiao"><ul>数据读取错误！！！</ul></div>';
                $("#building-content-md").html(tableBody);
            }
        });
    }
    /**
     * ===========================building start====================
     */

    /**
     * ===========================UrbanParts start====================
     */
    function loadUrbanPartsMessage(pageNo,pageSize){
        var list = null;
        var postData = {};
        var typeId = $("#urbanPartsTypeId").val();
        if(typeId!=null && typeId!="") {
            postData["resTypeId"]=typeId;
        }
        var typeCode = $("#urbanPartsTypeCode").val();
        if(typeCode!=null && typeCode!="") {
            postData["resTypeCode"]=typeCode;
        }
        var gridId = parent.document.getElementById("gridId").value;
        if(gridId!=null && gridId!="") {
            postData["gridId"]=gridId;
        }
        var infoOrgCode = parent.document.getElementById("infoOrgCode").value;
        if(infoOrgCode!=null && infoOrgCode!="") {
            postData["infoOrgCode"]=infoOrgCode;
        }
        var urbanPartsName = $("#urbanPartsName").val();
        if(urbanPartsName!=null && urbanPartsName!="") {
            postData["resName"]=urbanPartsName;
        }

        postData["page"] = pageNo;
        postData["rows"] = pageSize;
        divModleOpen('urbanPartsContent');
        var resIds="";
        $.ajax({
            type: "POST",
            url:'${rc.getContextPath()}/zhsq/map/mapDataMaintain/resourceListData.json',
            data: postData,
            dataType:"json",
            success: function(data){
                divModleClose();

                $('#urbanParts-pagination-num').text(pageNo);
                $('#urbanParts-records').text(data==null?0:data.total);
                var totalPage = Math.floor(data==null?0:(data.total/pageSize));
                if(data != null && data.total%pageSize>=0){
                    totalPage+=1;
                } else{
                    totalPage = 0;
                }
                $('#urbanParts-pageCount').text(totalPage);
                list=data==null?0:data.rows;
                var tableBody="";
                tableBody+='';
                if(data != null && list && list.length>0) {
                    tableBody+='<ul style="width:320px;">';
                    for(var i=0;i<list.length;i++){
                        var val=list[i];
                        var resName = val.resName;
                        if(resName!=null){
                            if(resName.length>42){
                                resName = resName.substring(0,41)+"...";
                            }
                        }else{
                            resName = '暂无名称';
                        }


                        tableBody+='<li ';
                        if(i==0){

                        }
                        tableBody+='onclick="selectRow(\'urbanParts\','+val.resId+',this)"><div  title="'+val.resName+'"><span class="FontDarkBlue">'+resName+'</span></div></li>';
                        resIds = resIds+","+val.resId;
                    }
                    tableBody+='</ul>';
                    resIds=resIds.substring(1, resIds.length);
                } else {
                    tableBody+='<div class="nodata" style="width: 320px;text-align: center;"></div>';
                }
                $("#urbanParts .mCSB_container").html(tableBody);
                gisPositionByType("urbanParts",resIds);
            },
            error:function(data){
                var tableBody='<div class="liebiao"><ul>数据读取错误！！！</ul></div>';
                $("#urbanParts-content-md").html(tableBody);
            }
        });
    }
    /**
     * ===========================UrbanParts start====================
     */

    /**
     * ===========================grid start====================
     */
    function loadGridMessage(pageNo,pageSize){
        var list = null;
        var postData = {};
        var gridId = parent.document.getElementById("gridId").value;
        if(gridId!=null && gridId!="") {
            postData["gridId"]=gridId;
        }
        var infoOrgCode = parent.document.getElementById("infoOrgCode").value;
        if(infoOrgCode!=null && infoOrgCode!="") {
            postData["infoOrgCode"]=infoOrgCode;
        }

        var drawedType = $("#drawedType").val();
        postData["drawedType"] = drawedType;

        var isMarker = $("#isMarker").val();
        postData["isMarker"] = isMarker;

        postData["page"] = pageNo;
        postData["rows"] = pageSize;
        divModleOpen('gridContent');
        $.ajax({
            type: "POST",
            url:'${rc.getContextPath()}/zhsq/map/mapDataMaintain/gridListData.json',
            data: postData,
            dataType:"json",
            success: function(data){
                divModleClose();
                $('#grid-pagination-num').text(pageNo);
                $('#grid-records').text(data.total);
                var totalPage = Math.floor(data.total/pageSize);
                if(data.total%pageSize>=0) totalPage+=1;
                $('#grid-pageCount').text(totalPage);
                list=data.rows;
                var tableBody="";
                tableBody+='';
                if(list && list.length>0) {
                    tableBody+='<ul style="width:320px;">';
                    for(var i=0;i<list.length;i++){
                        var val=list[i];
                        var gridName = val.gridName;
                        if(gridName!=null && gridName!="" && gridName.length>12){
                            gridName = gridName.substring(0,12);
                        }else if(gridName==null){
                            gridName = "";
                        }

                        tableBody+='<li ';
                        if(i==0){

                        }

                        tableBody+='onclick="selectRow(\'grid\','+val.gridId+',this)"><div  title="'+val.gridName+'"><span class="FontDarkBlue">'+gridName+'</span></div></li>';
                    }
                    tableBody+='</ul>';
                } else {
                    tableBody+='<div class="nodata" style="width: 210px;text-align: center;"></div>';
                }

                $("#grid .mCSB_container").html(tableBody);

            },
            error:function(data){
                var tableBody='<div class="liebiao"><ul>数据读取错误！！！</ul></div>';
                $("#grid-content-md").html(tableBody);
            }
        });
    }
    /**
     * ===========================grid start====================
     */

    //分页
    function change(_index, searchType){
        var flag;
        var pagenum = $("#"+searchType+"-pagination-num").text();
        var lastnum = $("#"+searchType+"-pageCount").text();
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
        if(searchType == "building"){
            loadBuildingMessage(pagenum,pageSize);
        }else if(searchType == "urbanParts"){
            loadUrbanPartsMessage(pagenum,pageSize);
        }else if(searchType == "grid"){
            loadGridMessage(pagenum,pageSize);
        }

    }

    //左侧查询
    function leftSearch(searchType){
        window.parent.MapIframe.clearSpecialLayer("buildingLayer");
        window.parent.MapIframe.clearSpecialLayer("buildLayer");
        window.parent.MapIframe.clearSpecialLayer("gridDataLayer");
        if(searchType == "building"){
            loadBuildingMessage(1,$('#pageSize').val());
        }else if(searchType == "urbanParts"){
            loadUrbanPartsMessage(1,$('#pageSize').val());
        }else if(searchType == "grid"){
            loadGridMessage(1,$('#pageSize').val());
        }
    }



    //左侧列表选择
    function selectRow(type, bizId, obj){
        $("#"+type+" .mCSB_container ul li").removeClass("current");
        $(obj).addClass("current");
        var elementsCollectionStr = $("#elementsCollectionStr").val();
        window.parent.MapIframe.document.getElementById("wid").value = bizId;
        var subType;
        if(type == "urbanParts"){
            subType = $("#urbanPartsTypeCode").val();
        }
        window.parent.MapIframe.mapToolShow(type, bizId, subType);

        setTimeout(function() {
            if($('#elementsCollectionStr').val() != "") {
                if(type == "building"){
                    window.parent.MapIframe.getDetailOnMapOfListClick($('#elementsCollectionStr').val(),370,281,bizId)
                }else if(type == "grid"){
                    window.parent.MapIframe.selectRowGridInfo(bizId);
                    window.parent.MapIframe.showGridDetail(bizId);//显示详情
                }else if(type == "urbanParts"){
                    //window.parent.MapIframe.selectRowUrbanParts(bizId, subType);
                    var currentUrbanPartTypeCode = $("#urbanPartsTypeCode").val();
                    var currentUrbanPartTypeName = $("#urbanPartsTypeName").val();
                    window.parent.MapIframe.getDetailOnMapOfListClick($('#elementsCollectionStr').val(),320,170,bizId);
                    //window.parent.MapIframe.showUrbanPartsDetail(bizId, currentUrbanPartTypeCode, currentUrbanPartTypeName);
                }

            }
        },1000);

    }

    /**
     * 点击tab页更新列表数据
     * @param moduleType
     */
    function tabChange(moduleType){
    	if (isMapLoaded) {
    		window.parent.MapIframe.mapToolHide();
	    	window.parent.MapIframe.changeModule(moduleType);
	        var gridId = parent.document.getElementById("gridId").value;
	        //判断是否显示网格轮廓
	        window.parent.showGridHs(gridId);
	        if(moduleType == "building"){
	            //parent.MapIframe.currentModuleType = "building";
	            loadBuildingMessage(1,$('#pageSize').val());
	
	        }else if(moduleType == "urbanParts"){
	            var currentUrbanPartTypeCode = $("#urbanPartsTypeCode").val();
	            var currentUrbanPartTypeId = $("#urbanPartsTypeId").val();
	            window.parent.MapIframe.mapAddMenu("urbanParts", currentUrbanPartTypeCode, currentUrbanPartTypeId);//地图上不能新增网格
	            //parent.MapIframe.currentModuleType = "urbanParts";
	            loadUrbanPartsMessage(1,$('#pageSize').val());
	        }else if(moduleType == "grid"){
	            window.parent.MapIframe.mapAddMenu("grid");//地图上不能新增网格
	            //parent.MapIframe.currentModuleType = "grid";
	            loadGridMessage(1,$('#pageSize').val());
	        }
    	}
    }


    //打开遮罩
    function divModleOpen(divId) {
        $("<div class='datagrid-mask'></div>").css( {
            display : "block",
            width : "100%",
            height : $(window).height()
        }).appendTo("#"+divId);
        $("<div class='datagrid-mask-msg'></div>").html("正在处理，请稍候。。。").appendTo(
                "#"+divId).css( {
                    display : "block",
                    left : 70,
                    top : (($("#"+divId).height() - 45) / 2),
                    height : 40
                });
        document.body.scroll="no";//除去滚动条
    }
    //关闭遮罩
    function divModleClose() {
        $(".datagrid-mask").css( {
            display : "none"
        });
        $(".datagrid-mask-msg").css( {
            display : "none"
        });
        $(".datagrid-mask").remove();
        $(".datagrid-mask-msg").remove();
        document.body.scroll="auto";//开启滚动条
    }

    //地图定位
    function gisPositionByType(type,bizIds){
        var url = "";
        window.parent.MapIframe.clearSpecialLayer("buildingLayer");
        window.parent.MapIframe.clearSpecialLayer("buildLayer");
        window.parent.MapIframe.clearSpecialLayer("gridDataLayer");
        window.parent.MapIframe.clearSpecialLayer("urbanPartsLayer");
        window.parent.MapIframe.clearSpecialLayer("globalEyesLayer");
        var subTypeCode, subTypeId;
        var menuCode = type;
        if(type == "building"){
            url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofbuilding/getArcgisLocateDataListOfBuilding.jhtml?buildingIds="+bizIds+"&showType=2";

        }else if(type == "urbanParts"){

            subTypeCode = $("#urbanPartsTypeCode").val();
            subTypeId = $("#urbanPartsTypeId").val();
            menuCode = subTypeCode;
            //公共厕所、路灯、消防栓、公交车站
            if(subTypeCode == "02010301" || subTypeCode == "02010501" || subTypeCode == "02010601" || subTypeCode == "02020401" || subTypeCode == "020116"){
                url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofgoods/getArcgisLocateDataListOfResource.jhtml?resTypeCode="+subTypeCode+"&ids="+bizIds;
                if(subTypeCode == "02010601"){//消防栓
                    url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofgoods/getArcgisLocateDataListOfNewResource.jhtml?resTypeCode=0601&ids="+bizIds;
                }
            }else if(subTypeCode == "02020501"){//全球眼
                url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/getArcgisLocateDataListOfGlobalEyes.jhtml?ids="+bizIds+"&showType=2";
            }
        }


        if (bizIds==""){
            return ;
        }else{
            window.parent.MapIframe.document.getElementById("bizIds").value = bizIds;
        }

        $.ajax({
            type: "POST",
            url:'${rc.getContextPath()}/zhsq/map/mapDataMaintain/getGisDataCfgByCode.json',
            data: {
                menuCode : menuCode,
                infoOrgCode : parent.document.getElementById("infoOrgCode").value
            },
            dataType:"json",
            success: function(data){
                if(data != null && data.elementsCollectionStr != 'undefined' && data.elementsCollectionStr != null){
                    $("#elementsCollectionStr").val(data.elementsCollectionStr);
                    window.parent.MapIframe.currentLayerLocateFunctionStr="getArcgisDataOfZhuanTi('"+url+"','"+$('#elementsCollectionStr').val()+"')";

                    window.parent.MapIframe.getArcgisDataOfZhuanTi(url,$('#elementsCollectionStr').val());

                    //加载右键菜单
                    setTimeout(function() {
                        if(type == "building") {
                            parent.MapIframe.mapAddMenu("building");
                            window.parent.MapIframe.layerAddBuildingMenu("buildingLayer");
                        }else if(type == "urbanParts"){
                            parent.MapIframe.mapAddMenu("urbanParts", subTypeCode, subTypeId);
                            if(subTypeCode == "02020501") {//全球眼
                                window.parent.MapIframe.layerAddGlobalEyesMenu("globalEyesLayer");
                            }else{
                                window.parent.MapIframe.layerAddUrbanPartsMenu("urbanPartsLayer");
                            }
                        }
                    }, 500);
                }
            },
            error:function(data){
                var tableBody='<div class="liebiao"><ul>数据读取错误！！！</ul></div>';
                $("#grid-content-md").html(tableBody);
            }
        });


    }


</script>

<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    
    
    <!-- External CSS -->
    <title>gis</title>
    <!--引入 重置默认样式 statics/zhxc -->
    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/common/css/reset.css"/>
    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/common/css/basic/gis-gridcliak.css">
    <!-- JavaScript -->
    <script src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
    <!-- JavaScript -->
   	<#include "/component/maxJqueryEasyUIWin.ftl" />
</head>
<style>
.grid-cs-list{
	display: none;
}
.show{
	display:inline;
}
span{
	font-size: 13px;
    color: #333;
    margin: 0px 2px;
    cursor: pointer;
    font-weight: bold;
} 
</style>
<body>
    <div class="gisgrid-box">
        <div class="grid-popbox wd-500px">
            <!--上部分-->
            <div class="grid-way clearfix">
                <!--图片区域盒子-->
                <div class="grid-icon">
                    <!--下面图片可以更换-->
                    <img class="iconimg" src="${uiDomain!''}/web-assets/common/images/iconclass/iconbox/grid/icon-grid-gs1.png"/>
                </div>
                <div class="grid-text">
                    <p class="grid-name-t"><a>${gridInfo.gridName}</a></p>
                    <p class="grid-data-t"><span><#if statOfGridStr??>${statOfGridStr}</#if></span></p>
                </div>
            </div>
            <!--下部分-->
            <div class="grid-cs-tab">
                <div class="grid-tab-vs clearfix">
                    <a class="bgc-l-green co-on" name="ren">人</a>
                    <a class="bgc-l-yellow" name="di">地</a>
                    <a class="bgc-l-cyan" name="shi">事</a>
                    <a class="bgc-l-blue" name="wu">物</a>
                    <a class="bgc-l-purple" name="qing">情</a>
                    <a class="bgc-l-prink" name="zu-zhi">组织</a>
                </div>
            </div>
            <!-- 人 -->
            <div class="grid-cs-list show" id="ren">
                <ul class="grid-list grid-cont-on clearfix">
                    <li class="xw-com50">
                        <img class="sm-iconimg"src="${uiDomain!''}/web-assets/common/images/iconclass/iconbox/grid/icon-grid-gs1.png"/>
                        <div class="grid-s-dat">户籍人口：<span onclick="myClick('${SQ_BI_URL}/yandu/realBi/houseHold_view.jhtml?infoOrgCode=${gridInfo.infoOrgCode}','户籍人口')"><#if hjCount??>${hjCount}<#else>0</#if></span>人</div>
                    </li>
                    <li class="xw-com50">
                        <img class="sm-iconimg"src="${uiDomain!''}/web-assets/common/images/iconclass/iconbox/grid/icon-grid-gs1.png"/>
                        <div class="grid-s-dat">流动人口：<span onclick="myClick('${SQ_BI_URL}/yandu/realBi/flow_view.jhtml?infoOrgCode=${gridInfo.infoOrgCode}','流动人口')"><#if floatCount??>${floatCount}<#else>0</#if></span>人</div>
                    </li>
                    <li class="xw-com50">
                        <img class="sm-iconimg"src="${uiDomain!''}/web-assets/common/images/iconclass/iconbox/grid/icon-grid-gs1.png"/>
                        <div class="grid-s-dat">待助类：<span onclick="myClick('${SQ_BI_URL}/yandu/rsYutuController/threeClassPeople_single.jhtml?type=SL2&infoOrgCode=${gridInfo.infoOrgCode}','待助类')"><#if dzlCount??>${dzlCount}<#else>0</#if></span>人</div>
                    </li>
                    <li class="xw-com50">
                        <img class="sm-iconimg"src="${uiDomain!''}/web-assets/common/images/iconclass/iconbox/grid/icon-grid-gs1.png"/>
                        <div class="grid-s-dat">回归类：<span onclick="myClick('${SQ_BI_URL}/yandu/rsYutuController/threeClassPeople_single.jhtml?type=SL1&infoOrgCode=${gridInfo.infoOrgCode}','回归类')"><#if hglCount??>${hglCount}<#else>0</#if></span>人</div>
                    </li>
                    <li class="xw-com50">
                        <img class="sm-iconimg"src="${uiDomain!''}/web-assets/common/images/iconclass/iconbox/grid/icon-grid-gs1.png"/>
                        <div class="grid-s-dat">弱势类：<span onclick="myClick('${SQ_BI_URL}/yandu/rsYutuController/threeClassPeople_single.jhtml?type=SL3&infoOrgCode=${gridInfo.infoOrgCode}','弱势类')"><#if rslCount??>${rslCount}<#else>0</#if></span>人</div>
                    </li>
                </ul>
            </div>
            <!-- 地 -->
            <div class="grid-cs-list" id="di">
                <ul class="grid-list grid-cont-on clearfix">
                    <li class="xw-com50">
                        <img class="sm-iconimg"src="${uiDomain!''}/web-assets/common/images/iconclass/iconbox/grid/icon-grid-gs1.png"/>
                        <div class="grid-s-dat">楼宇：<span onclick="myClick('${SQ_BI_URL}/yandu/legalBi/baseYanDuGisBar.jhtml?infoOrgCode=${gridInfo.infoOrgCode}&type=WE','楼宇')"><#if lyCount??>${lyCount}<#else>0</#if></span>栋</div>
                    </li>
                    <li class="xw-com50">
                        <img class="sm-iconimg"src="${uiDomain!''}/web-assets/common/images/iconclass/iconbox/grid/icon-grid-gs1.png"/>
                        <div class="grid-s-dat">出租屋：<span onclick="myClick('${SQ_BI_URL}/yandu/legalBi/baseYanDuGisBar.jhtml?infoOrgCode=${gridInfo.infoOrgCode}&type=WF','出租屋')"><#if czwCount??>${czwCount}<#else>0</#if></span>间</div>
                    </li>
                    <li class="xw-com50">
                        <img class="sm-iconimg"src="${uiDomain!''}/web-assets/common/images/iconclass/iconbox/grid/icon-grid-gs1.png"/>
                        <div class="grid-s-dat">重点场所：<span onclick="myClick('${SQ_BI_URL}/yandu/legalBi/gisYanDuToPlaceBar.jhtml?infoOrgCode=${gridInfo.infoOrgCode}','重点场所')"><#if zzcsCount??>${zzcsCount}<#else>0</#if></span>处</div>
                    </li>
                    <li class="xw-com50">
                        <img class="sm-iconimg"src="${uiDomain!''}/web-assets/common/images/iconclass/iconbox/grid/icon-grid-gs1.png"/>
                        <div class="grid-s-dat">学校：<span onclick="myClick('${SQ_BI_URL}/yandu/legalBi/schoolYanDuGisBar.jhtml?infoOrgCode=${gridInfo.infoOrgCode}','学校')"><#if syxxCount??>${syxxCount}<#else>0</#if></span>间</div>
                    </li>
                </ul>
            </div>
            <!--事  -->
            <div class="grid-cs-list" id="shi">
                <ul class="grid-list grid-cont-on clearfix">
                    <li class="xw-com50">
                        <img class="sm-iconimg"src="${uiDomain!''}/web-assets/common/images/iconclass/iconbox/grid/icon-grid-gs1.png"/>
                        <div class="grid-s-dat">本月辖区事件：<span onclick="eventClick('${SQ_BI_URL}/report/jianhu/eventAllReoport.jhtml?infoOrgCode=${gridInfo.infoOrgCode}','本月辖区事件')"><#if byxqsjCount??>${byxqsjCount}<#else>0</#if></span>件</div>
                    </li>
                </ul>
            </div>
            <!--物  -->
            <div class="grid-cs-list" id="wu">
                <ul class="grid-list grid-cont-on clearfix">
                    <li class="xw-com50">
                        <img class="sm-iconimg"src="${uiDomain!''}/web-assets/common/images/iconclass/iconbox/grid/icon-grid-gs1.png"/>
                        <div class="grid-s-dat">全球眼：<span onclick="myClick('${SQ_BI_URL}/report/yuntuBi/globalEyesForGis.jhtml?infoOrgCode=${gridInfo.infoOrgCode}','全球眼')"><#if qqyCount??>${qqyCount}<#else>0</#if></span>个</div>
                    </li>
                </ul>
            </div>
            <!-- 情 -->
             <div class="grid-cs-list" id="qing">
                <ul class="grid-list grid-cont-on clearfix">
                    <li class="xw-com50">
                        <img class="sm-iconimg"src="${uiDomain!''}/web-assets/common/images/iconclass/iconbox/grid/icon-grid-gs1.png"/>
                        <div class="grid-s-dat">矛盾纠纷：<span onclick="myClick('${EVENT_DOMAIN}/zhsq_event/zhsq/map/gisstat/gisStat/getDisputeBar.jhtml?infoOrgCode=${gridInfo.gridId}','矛盾纠纷')"><#if countDispute??>${countDispute}<#else>0</#if></span>件</div>
                    </li>
                </ul>
            </div>           
            <!-- 组织 -->
            <div class="grid-cs-list" id="zu-zhi">
                <ul class="grid-list grid-cont-on clearfix">
                    <li class="xw-com50">
                        <img class="sm-iconimg"src="${uiDomain!''}/web-assets/common/images/iconclass/iconbox/grid/icon-grid-gs1.png"/>
                        <div class="grid-s-dat">网格员：<span onclick="myClick('${SQ_BI_URL}/yandu/legalBi/baseYanDuGisBar.jhtml?infoOrgCode=${gridInfo.infoOrgCode}&type=WK','网格员')"><#if wgyCount??>${wgyCount}<#else>0</#if></span>人</div>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <script src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
        
    <script>
        $(function () {
            //grid-cs-tab
            $('.grid-tab-vs').on('click', 'a', function(){
                $(this).addClass('co-on').siblings().removeClass('co-on');
                var type=$(this).context.name;
                $('#'+type).addClass('show').siblings().removeClass('show');
            });
        })
        function myClick(url,title){
        	
        	if(url=="" || url == null)return;
        	window.parent.showMaxJqueryWindow(title,url,null,null);
        }
		function eventClick(url,title){
			var d=new Date();
        	var yue=d.getMonth()+1;
        	var n=d.getFullYear();
        	url+="&monthDate="+n+"-"+getNow(yue);
        	window.parent.showMaxJqueryWindow(title,url,null,null);
		}
		//判断是否在前面加0
	    function getNow(s) {
	    	return s < 10 ? '0' + s: s;
	    }
    </script>
</body>
</html>
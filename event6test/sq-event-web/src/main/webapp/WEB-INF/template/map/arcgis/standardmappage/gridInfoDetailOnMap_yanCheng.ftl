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
    <#include "/component/maxJqueryEasyUIWin.ftl" />
    <script src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
    
    <!-- JavaScript -->
    <#--上传附件-->
    <script>
    var base = '${rc.getContextPath()}';//项目的上下文，（工程名）
    var imgDomain = '${IMG_URL}';//文件服务器域名
    var uiDomain = '${uiDomain}';//公共样式域名
    var skyDomain = '${SQ_SKY_URL!''}';//网盘挂载IP【文档在线预览服务器IP：询问赛男团队，获取ip值】
    var componentsDomain = '${COMPONENTS_URL}';//公共组件工程域名
    var fileDomain = '${SQ_FILE_URL}';//文件服务工程域名

    </script>

    <#--<script src="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/js/jqry-9-1-12-4.min.js" type="text/javascript" charset="utf-8"></script>-->
    <script src="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/js/layui-v2.4.5/layui/layui.js" type="text/javascript" charset="utf-8"></script>
    <link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/css/layui.css">
    <link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/css/big-file-upload.css">
    <script src="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/js/webuploader/webuploader.js" type="text/javascript" charset="utf-8"></script>
    <script type="text/javascript" src="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/js/upload-common.js"></script>
    <script src="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/js/big-file-upload.js" type="text/javascript" charset="utf-8"></script>
    <#--上传附件- END -->
   
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
.file-div{
	 float:left;
	 margin-left: 5px;
}
.meida {
    width: 100%;

}
.grid-s-dat>span:hover {
    cursor: pointer;
    color: #5294e8;
}
.grid-s-dat>span {
    font-size: 15px;
    margin-right: 5px;
    font-weight: bold;
    color: #333;
}
.grid-s-dat>a{
font-size: 15px;
    margin-right: 5px;
    font-weight: bold;
    color: #333;
    margin: 0px 2px;

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
                    <a class="bgc-l-green " name="ren" style="cursor: pointer">人</a>
                    <a class="bgc-l-yellow" name="di" style="cursor: pointer">地</a>
                    <a class="bgc-l-cyan" name="shi" style="cursor: pointer">事</a>
                    <a class="bgc-l-blue" name="wu" style="cursor: pointer">物</a>
                    <a class="bgc-l-purple" name="qing" style="cursor: pointer">情</a>
                    <a class="bgc-l-prink" name="zu-zhi" style="cursor: pointer">组织</a>
                    <a class="bgc-l-navy co-on" name="shi-ping" style="cursor: pointer">视频</a>
                </div>
            </div>
            <!-- 人 -->
            <div class="grid-cs-list show" id="ren"><!-- show -->
                <ul class="grid-list grid-cont-on clearfix">
                    <li class="xw-com50">
                        <img class="sm-iconimg"src="${uiDomain!''}/web-assets/common/images/iconclass/iconbox/grid/icon-grid-gs1.png"/>
                        <div class="grid-s-dat" style="cursor: pointer">户籍人口：<span onclick="myClick('${SQ_BI_URL}/yandu/realBi/houseHold_view.jhtml?infoOrgCode=${gridInfo.infoOrgCode}','户籍人口')"><#if hjCount??>${hjCount}<#else>0</#if></span>人</div>
                    </li>
                    <li class="xw-com50">
                        <img class="sm-iconimg"src="${uiDomain!''}/web-assets/common/images/iconclass/iconbox/grid/icon-grid-gs1.png"/>
                        <div class="grid-s-dat" style="cursor: pointer">流动人口：<span onclick="myClick('${SQ_BI_URL}/yandu/realBi/flow_view.jhtml?infoOrgCode=${gridInfo.infoOrgCode}','流动人口')"><#if floatCount??>${floatCount}<#else>0</#if></span>人</div>
                    </li>
                    <li class="xw-com50">
                        <img class="sm-iconimg"src="${uiDomain!''}/web-assets/common/images/iconclass/iconbox/grid/icon-grid-gs1.png"/>
                        <div class="grid-s-dat">上访人员：<a><#if sfry??>${sfry}<#else>0</#if></a>人</div>
                    </li>
                    <li class="xw-com50">
                        <img class="sm-iconimg"src="${uiDomain!''}/web-assets/common/images/iconclass/iconbox/grid/icon-grid-gs1.png"/>
                        <div class="grid-s-dat">吸毒人员：<a><#if xdry??>${xdry}<#else>0</#if></a>人</div>
                    </li>
                    <li class="xw-com50">
                        <img class="sm-iconimg"src="${uiDomain!''}/web-assets/common/images/iconclass/iconbox/grid/icon-grid-gs1.png"/>
                        <div class="grid-s-dat">刑释人员：<a><#if xmsfry??>${xmsfry}<#else>0</#if></a>人</div>
                    </li>
                    <li class="xw-com50">
                        <img class="sm-iconimg"src="${uiDomain!''}/web-assets/common/images/iconclass/iconbox/grid/icon-grid-gs1.png"/>
                        <div class="grid-s-dat">矫正人员：<a><#if sqjzxx??>${sqjzxx}<#else>0</#if></a>人</div>
                    </li>
                    <li class="xw-com50">
                        <img class="sm-iconimg"src="${uiDomain!''}/web-assets/common/images/iconclass/iconbox/grid/icon-grid-gs1.png"/>
                        <div class="grid-s-dat">艾滋病人员：<a><#if azb??>${azb}<#else>0</#if></a>人</div>
                    </li>
                    <li class="xw-com50">
                        <img class="sm-iconimg"src="${uiDomain!''}/web-assets/common/images/iconclass/iconbox/grid/icon-grid-gs1.png"/>
                        <div class="grid-s-dat">邪教人员：<a><#if xjry??>${xjry}<#else>0</#if></a>人</div>
                    </li>
                    <li class="xw-com50">
                        <img class="sm-iconimg"src="${uiDomain!''}/web-assets/common/images/iconclass/iconbox/grid/icon-grid-gs1.png"/>
                        <div class="grid-s-dat">精神障碍患者：<a><#if zzjsbjl??>${zzjsbjl}<#else>0</#if></a>人</div>
                    </li>
                    <li class="xw-com50">
                        <img class="sm-iconimg"src="${uiDomain!''}/web-assets/common/images/iconclass/iconbox/grid/icon-grid-gs1.png"/>
                        <div class="grid-s-dat">重点青少年：<a><#if zdqsn??>${zdqsn}<#else>0</#if></a>人</div>
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
                        <div class="grid-s-dat" style="cursor: pointer">出租屋：<span onclick="myClick('${SQ_BI_URL}/yandu/legalBi/baseYanDuGisBar.jhtml?infoOrgCode=${gridInfo.infoOrgCode}&type=WF','出租屋')"><#if czwCount??>${czwCount}<#else>0</#if></span>间</div>
                    </li>
                    <li class="xw-com50">
                        <img class="sm-iconimg"src="${uiDomain!''}/web-assets/common/images/iconclass/iconbox/grid/icon-grid-gs1.png"/>
                        <div class="grid-s-dat" style="cursor: pointer">重点场所：<span onclick="myClick('${SQ_BI_URL}/yandu/legalBi/gisYanDuToPlaceBar.jhtml?infoOrgCode=${gridInfo.infoOrgCode}','重点场所')"><#if zzcsCount??>${zzcsCount}<#else>0</#if></span>处</div>
                    </li>
                    <li class="xw-com50">
                        <img class="sm-iconimg"src="${uiDomain!''}/web-assets/common/images/iconclass/iconbox/grid/icon-grid-gs1.png"/>
                        <div class="grid-s-dat" style="cursor: pointer">学校：<span onclick="myClick('${SQ_BI_URL}/yandu/legalBi/schoolYanDuGisBar.jhtml?infoOrgCode=${gridInfo.infoOrgCode}','学校')"><#if syxxCount??>${syxxCount}<#else>0</#if></span>间</div>
                    </li>
                </ul>
            </div>
            <!--事  -->
            <div class="grid-cs-list" id="shi">
                <ul class="grid-list grid-cont-on clearfix">
                    <li class="xw-com50">
                        <img class="sm-iconimg"src="${uiDomain!''}/web-assets/common/images/iconclass/iconbox/grid/icon-grid-gs1.png"/>
                        <div class="grid-s-dat" style="cursor: pointer">本月辖区事件：<span onclick="eventClick('${SQ_BI_URL}/report/jianhu/eventAllReoport.jhtml?infoOrgCode=${gridInfo.infoOrgCode}','本月辖区事件')"><#if byxqsjCount??>${byxqsjCount}<#else>0</#if></span>件</div>
                    </li>
                </ul>
            </div>
            <!--物  -->
            <div class="grid-cs-list" id="wu">
                <ul class="grid-list grid-cont-on clearfix">
                    <li class="xw-com50">
                        <img class="sm-iconimg"src="${uiDomain!''}/web-assets/common/images/iconclass/iconbox/grid/icon-grid-gs1.png"/>
                        <div class="grid-s-dat" style="cursor: pointer">全球眼：<span onclick="myClick('${SQ_BI_URL}/report/yuntuBi/globalEyesForGis.jhtml?infoOrgCode=${gridInfo.infoOrgCode}','全球眼')"><#if qqyCount??>${qqyCount}<#else>0</#if></span>个</div>
                    </li>
                </ul>
            </div>
            <!-- 情 -->
             <div class="grid-cs-list" id="qing">
                <ul class="grid-list grid-cont-on clearfix">
                    <li class="xw-com50">
                        <img class="sm-iconimg"src="${uiDomain!''}/web-assets/common/images/iconclass/iconbox/grid/icon-grid-gs1.png"/>
                        <div class="grid-s-dat" style="cursor: pointer">矛盾纠纷：<span onclick="myClick('${EVENT_DOMAIN}/zhsq_event/zhsq/map/gisstat/gisStat/getDisputeBar.jhtml?infoOrgCode=${gridInfo.gridId}','矛盾纠纷')"><#if countDispute??>${countDispute}<#else>0</#if></span>件</div>
                    </li>
                </ul>
            </div>           
            <!-- 组织 -->
            <div class="grid-cs-list" id="zu-zhi">
                <ul class="grid-list grid-cont-on clearfix">
                    <li class="xw-com50">
                        <img class="sm-iconimg"src="${uiDomain!''}/web-assets/common/images/iconclass/iconbox/grid/icon-grid-gs1.png"/>
                        <div class="grid-s-dat" style="cursor: pointer">网格员：<span onclick="myClick('${SQ_BI_URL}/yandu/legalBi/baseYanDuGisBar.jhtml?infoOrgCode=${gridInfo.infoOrgCode}&type=WK','网格员')"><#if wgyCount??>${wgyCount}<#else>0</#if></span>人</div>
                    </li>
                </ul>
            </div>
            <!--视频-->
            <div class="grid-content" id="shi-ping">
                 <p class="article">${gridInfo.gridDesc!''}</p>
                <div class="meida">
                 <div id="bigupload"></div>
                   <!--  <img src="${uiDomain!''}/web-assets/common/images/icon-media-play.jpg" /> -->
                </div> 
            </div>
        </div>
    </div>
        
    <script>
        $(function () {
            //grid-cs-tab
            $('.grid-tab-vs').on('click', 'a', function(){
                $(this).addClass('co-on').siblings().removeClass('co-on');
                var type=$(this).context.name;
                $('#'+type).addClass('show').siblings().removeClass('show');
            });
            var name=$(".co-on").attr("name");
            $('#'+name).addClass('show').siblings().removeClass('show');
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
	   var bigupload = $("#bigupload").bigfileUpload({
	        useType: 'view',//附件上传的使用类型，add,edit,view，（默认edit）;
	        fileExt: '.jpg,.png,.gif,.bmp,.wav,.MIDI,.m4a,.WMA,.wma,.mp4',//支持上传的文件后缀名(默认值请查看参数说明）
	        <#if attList??>initFileArr:${attList},</#if>
	        appcode: "zzgrid",//文件所属的应用代码（默认值components）
	        module: "AttachFile",//文件所属的模块代码（默认值bigfile）
	        imgDomain: imgDomain,//图片服务器域名
	        uiDomain: uiDomain,//公共样式域名
	        skyDomain : skyDomain,//网盘挂载IP
	        fileDomain : fileDomain,//文件服务域名
	        duplicate:true,// 重复上传
	        fileNumLimit:6,// 最多上传6个
	        videoSize:['400px','400px'],
	        componentsDomain:componentsDomain,//公共组件域名
	        selfBeanName:"cn.ffcs.shequ.mybatis.domain.zzgl.work.WorkAttachment",
	        selfServiceName:"cn.ffcs.shequ.zzgl.service.work.IWorkAttachmentService",
	    });
    </script>
</body>
</html>
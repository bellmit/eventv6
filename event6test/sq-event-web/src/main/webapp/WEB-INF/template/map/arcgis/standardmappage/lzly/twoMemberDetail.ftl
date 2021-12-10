<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>两站两员信息</title>

    <script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
    <#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
    <script type="text/javascript" src="${rc.getContextPath()}/js/My97DatePicker/WdatePicker.js"></script>
    <!--呼叫中心-->
    <script type="text/javascript" src="${rc.getContextPath()}/js/call/callCenter.js"></script>
    <!--插件如语音盒 使用js-->
    <script type="text/javascript" src="${rc.getContextPath()}/js/plugIn/plug_in.js"></script>

    <style type="text/css">
        .ManagerContact ul li {
            margin-left: 6px;
        }
    </style>
</head>
<body style="background-color: #fff;">
<div style="margin:2px 3px 0px 3px;">
<#if teamMember?exists>
    <div class="con ManagerWatch">
        <div class="ManagerInfo">
            <ul>
                <li>
                    <#if teamMember.photoPath??>
                        <img id="userImg" alt="" src="${RESOURSE_SERVER_PATH}${teamMember.photoPath}" width="85"
                             height="115"/>
                    <#else>
                        <img id="userImg" alt=""
                             src="${uiDomain!''}/images/map/gisv0/map_config/unselected/situation/untitled.png"
                             width="85" height="115"/>
                    </#if>
                </li>
                <li style="width:245px;">
                    <p>
                        <span>${teamMember.memberName!''}</span>
                    </p>

                    <#--<p title="<#if gridNames??>${gridNames}</#if>"><#if gridNames??>${gridNames}</#if></p>-->
                    <p><code>所属区域：</code>${teamMember.twoStation.stationName!''}</p>
                    <p><code>联系电话：</code>${teamMember.memberPhone!''}</p>
                </li>
            </ul>
            <div class="clear"></div>
        </div>
        <div class="ManagerContact">
            <ul id="operationBtn">
                <li class="GreenBg" onclick="dynamicTrajectoryQuery()"><img  src="${uiDomain!''}/images/map/gisv0/special_config/images/mc_1.png" />实时轨迹定位</li>
                <li id="callPhone" class="YellowBg" onclick="showCall('${teamMember.memberPhone!''}','${teamMember.memberName!''}','<#if teamMember.photoPath??>${RESOURSE_SERVER_PATH}${teamMember.photoPath}</#if>');"><img  src="${uiDomain!''}/images/map/gisv0/special_config/images/mc_2.png" />语音盒呼叫</li>
                <li id="sendMsg" class="CyanBg" onclick="sendMessage('${teamMember.userId!''}','${teamMember.memberPhone!''}');"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/mc_3.png" />发送短信</li>

                <#if isUserMmp?? && isUserMmp=='true'>
                    <li id="mmp" class="BlueBg" onclick="mmp();"><img     src="${uiDomain!''}/images/map/gisv0/special_config/images/mc_5.png" />视频呼叫</li>
                </#if>
            </ul>
            <div class="clear"></div>
        </div>
        <div class="h_10"></div>
        <div class="ManagerSearch">
            <div class="nav">
                <ul>
                    <li class="current">轨迹查询</li>
                </ul>
                <div class="line"></div>
            </div>
            <div class="con">
                <input type="hidden" id="imsi" name="imsi" value="${imsi!''}"/>
                <input type="hidden" id="memberId" name="memberId" value="${memberId!''}"/>
                <input type="hidden" id="userId" name="userId" value="${teamMember.userId!''}"/>
                <input type="hidden" id="mobileTelephone" name="mobileTelephone" value="${teamMember.memberPhone!''}"/>
                <ul>
                    <li>
                        <input type="text" id="start"
                               onclick="WdatePicker({startDate:'${startTime}', dateFmt:'yyyy-MM-dd HH:mm', readOnly:true, alwaysUseStartDate:true})"
                               class="Wdate"
                               style="width:125px; height:26px; *width:100px; *height:24px;font-size:11px;"
                               value="${startTime}"/>
                    </li>
                    <li>至</li>
                    <li>
                        <input type="text" id="end"
                               onclick="WdatePicker({startDate:'${endTime}', dateFmt:'yyyy-MM-dd HH:mm', readOnly:true, alwaysUseStartDate:true})"
                               class="Wdate"
                               style="width:125px; height:26px; *width:100px; *height:24px;font-size:11px;"
                               value="${endTime}"/>
                    </li>
                    <li><a href="javascript:void(0);" onclick="trajectoryQuery()" class="btn"><img
                            src="${uiDomain!''}/images/map/gisv0/special_config/images/mc_4.png"/>查询</a></li>
                </ul>
                <div class="clear"></div>
            </div>
        </div>
    </div>
<#else>
    <table cellpadding="0" cellspacing="0" border="0" class="searchList-2">
        <tr style="height: 185px">
            <td align="center" style="color:red;font:14;width:350px" class="sj_cot2_sty">未查到相关数据！</td>
        </tr>
    </table>
</#if>


</div>
<!--呼叫中心使用-->
<OBJECT id="oOCX" codeBase="" height="0" width="0" align="center"
        classid="clsid:69FA966F-0F98-4CB5-B73A-78D21F7CFD67"></OBJECT>
</body>
<script type="text/javascript">

    //没有手机号码的时候，无法语音呼叫或发送短信，按钮变灰色
    var telephone = $("#mobileTelephone").val();
    if(telephone==null || telephone==""){
        $("#callPhone").css("background-color","#5d5d5d");
        $("#sendMsg").css("background-color","#5d5d5d");
        $("#callPhone").attr("title","无电话号码");
        $("#sendMsg").attr("title","无电话号码");
    }

    function sendMessage(userId,fixedTelephone){
        if(fixedTelephone==null || fixedTelephone==""){
            //alert("没有电话号码可以发送短信");
            return;
        }

        var url = "${SQ_ZZGRID_URL}/zzgl/map/data/situation/sendSMSPage.jhtml?userId="+userId+"&phone="+fixedTelephone;
        var title = "发送短信";

        window.parent.showMaxJqueryWindow(title,url,475,200);
    }

    function showCall(bCall, userName, userImg){
        if(userImg!=null && userImg!=""){
            userImg = "${RESOURSE_SERVER_PATH}" + userImg;
        }

        showVoiceCall("${rc.getContextPath()}", window.parent.showCustomEasyWindow, bCall, userName, userImg);
    }

    var zhsq_url = "${SQ_ZHSQ_EVENT_URL}";

    // 旧地图使用
    function showCall_(bCall, userName, userImg){
        if(bCall==null || bCall==""){
            return;
        }

        if(userName==undefined || userName==null || userName=="null"){
            userName = "";
        }
        if (userImg==undefined || userImg==null || userImg=='') {
            userImg = "${uiDomain!''}/images/map/gisv0/map_config/unselected/situation/untitled.png";
        }

        if (zhsq_url.indexOf("http") == -1) {
            zhsq_url = "http://" + zhsq_url;
        }

        var url = zhsq_url + "/zhsq/map/arcgis/voiceInterface/go.jhtml?bCall=" + bCall + "&userName="
                + encodeURIComponent(encodeURIComponent(encodeURIComponent(encodeURIComponent(userName)))) + "&userImg=" + encodeURI(userImg) + "&"
                + new Date().getTime();

        url = url.replace(/\&/g,"%26");

        var title = "语音盒呼叫";
        title = encodeURIComponent(encodeURIComponent(title));

        //var urlDomain = "${SQ_ZZGRID_URL}/zzgl/map/gis/commonCrossDomain.jhtml?callBack="+"showMaxJqueryWindow('"+title+"','"+url+"',"+320+","+410+", 'no')";
        var urlDomain = "${SQ_ZZGRID_URL}/zzgl/map/gis/commonCrossDomain.jhtml?callBack="+"showCustomEasyWindow('"+title+"','"+url+"',"+314+","+410+", false, 'no')";

        $("#cross_domain_iframe").attr("src",urlDomain);
    }

    var current = $("#end").val();
    current = Date.parse(current.replace(/-/g,"/"));
    var currentDate = new Date(current); // 当前时间

    var currentYear = currentDate.getFullYear();//当前年份
    var currentMonth = currentDate.getMonth() + 1;// 当前月份

    var currentDate2 = new Date(currentYear, currentMonth, 0);
    var lastDate = currentDate2.getDate(); // 当前月最后一天

    var dateLine = new Date();
    dateLine.setFullYear(currentYear);
    dateLine.setMonth(currentMonth - 1);
    dateLine.setDate(lastDate);

    // 获取三个月前的时间
    function get3MonthBefor(currentDate){
        var resultDate,year,month,date,hms;
        year = currentDate.getFullYear();
        month = currentDate.getMonth()+1;
        date = 1;
        switch(month)
        {
            case 1:
            case 2:
                month += 10;
                year--;
                break;
            default:
                month -= 2;
                break;
        }

        month = (month < 10) ? ('0' + month) : month;
        resultDate = year + '/' + month +'/' +date;
        return new Date(resultDate);
    }

    function trajectoryQuery(){
        var start = $("#start").val();
        var end = $("#end").val();
        var imsi = $("#imsi").val();
        var mobileTelephone = $("#mobileTelephone").val();
        var userId = $("#userId").val();
        var memberId = $("#memberId").val();
        if(imsi==""){
            alert("未绑定IMSI号");
            return;
        }

        var tempDate = get3MonthBefor(currentDate);

        if (start > end) {
            alert("开始时间需要小于结束时间");
            return;
        }

        var start2 = Date.parse(start.replace(/-/g,"/"));
        var startDate = new Date(start2); // 开始时间

        var end2 = Date.parse(end.replace(/-/g,"/"));
        var endDate = new Date(end2); // 结束时间

        if (startDate.getTime() < tempDate.getTime() || startDate.getTime() > dateLine.getTime() || endDate.getTime() > dateLine.getTime()) {
            alert("只能查询三个月内（从" + (tempDate.getMonth() + 1) + "月1号开始）的轨迹数据");
            return;
        }

        var params = "?locateTimeBegin="+start+"&locateTimeEnd="+end+"&imsi="+imsi+"&mobileTelephone="+mobileTelephone+"&userId="+userId+"&memberId="+memberId+"&t="+Math.random();

        var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDataTrajectoryOfTeamMember.jhtml"+params;
        window.parent.getTrajectoryOfGridAdmin(url,start,end, '信访责任人轨迹');
    }
    function dynamicTrajectoryQuery(){
        var start = '${startTime}';
        var end = '${endTime}';
        var imsi = $("#imsi").val();
        var userId = $("#userId").val();
        if(imsi==""){
            alert("未绑定IMSI号");
            return false;
        }
        var memberId = $("#memberId").val();
        var params = "?locateTimeBegin="+start+"&imsi="+imsi+"&userId="+userId+"&memberId="+memberId+"&t="+Math.random();

        var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDataTrajectoryOfTeamMember.jhtml"+params;
        window.parent.getDynamicTrajectoryOfGridAdmin(url,start,end, '信访责任人轨迹');
    }

    //视频呼叫网格员
    function mmp(){
        var userName='${teamMember.name!}';
        //if(userName!='huan')
        //	userName="test3";

        var talkid=userName+"@mmp";//视频对应的网格员Id
        window.parent.showMmpSelector();

        if(window.parent.checkActiveX())
            window.parent.StartVideoTalk(talkid);
    }


</script>
</html>

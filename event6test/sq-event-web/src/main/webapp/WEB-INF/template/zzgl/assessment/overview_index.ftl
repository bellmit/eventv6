<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新事件绩效首页</title>
<#include "/component/commonFiles.ftl" />
<link href="${uiDomain!''}/css/normal.css" rel="stylesheet" type="text/css" />
<#include "/component/ComboBox.ftl">
<#include "/component/AnoleDate.ftl">
<script src="${rc.getContextPath()}/js/zzgl_core.js" type="text/javascript"></script>
<style type="text/css">
	/*-------统计概况--------*/
	.tjdata{background:#f9f9f9; width:100%;}
	.tjdata .list{padding-top:10px;}
	.tjdata .list dl{float:left; width:140px; height:48px; margin:0 1.8% 1%;}
	.tjdata .list dl dd{float:left; width:75px; padding-left:10px; line-height:24px;}
	.tjdata .title{font-size:16px; font-family:Microsoft YaHei;}
	.tjdata .data{font-family:Arial, Helvetica, sans-serif; font-size:20px;}
	.tjdata .chart{padding:10px;}
	.xmtitle{ background:url(../images/khpg/xm_kh_topbg.png) repeat-x; height:33px; border-top:1px solid #bed5f3; line-height:33px; padding:0 5px;}
	.xmtitle .fj{ background:#fff; border:1px solid #b8d0d6; padding:2px 4px;}
	.red{ color:#ff0000;}
	.xm_btnsearch{ background:url(../images/khpg/xm_kh_searchbg.png) no-repeat; width:55px; height:24px; border:none; text-align:center; padding-left:23px; cursor:pointer; color:#183152;}
	.xmmain{padding:10px;}
	.xmmain table{ border-left:1px solid #d4d9e7;border-top:1px solid #d4d9e7;}
	.xmmain tr th{ border-right:1px solid #d4d9e7;border-bottom:1px solid #d4d9e7; padding:8px 5px; background:#f5f9ff; color:#014196; text-align:left;font-weight:normal;}
	.xmmain tr td{ border-right:1px solid #d4d9e7;border-bottom:1px solid #d4d9e7; padding:8px 5px; color:#ff0000; font-weight:bold; font-size:16px;}
	.xmmain .xmbtn{ border:none;}
	.xmbtn tr td{ border:none; padding:0;}
	.xm_btnstyle{ background:url(../images/khpg/xm_kh_btn1_a.png) repeat-x; height:74px; line-height:74px; border:none;border:1px solid #d4d9e7; margin-right:10px;color:#014196; font-size:14px; display:inline-block; padding:0 10px; text-decoration:none; font-weight:normal;}
	.xm_btnstyle:hover{ background:url(../images/khpg/xm_kh_btn1_h.png) repeat-x; color:#fff;}
	.xmmain .xm_tit_h3{ color:#014196;}
	.light{cursor:pointer;}
</style>
<script type="text/javascript">
	var PageApi = {};
	$(function() {
		var dateApi = $("#date1").anoleDateRender({
			BackfillType : "1",
			ShowOptions : {
				TabItems : [ "常用", "月", "季", "年" ]
			},
			BackEvents : {
				OnSelected : function(api, type) {
					$("#startTime").val(api.getStartDate());
					$("#endTime").val(api.getEndDate());
					if (type == "自定义") {
						PageApi.DoSearch();
					}
				}
			}
		}).anoleDateApi();
		dateApi.setRangeDate("${startTime!''}", "${endTime!''}");
		
		AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(value, items) {
			if (items && items.length > 0) {
				$("#infoOrgCode").val(items[0].orgCode);
			}
		});
		
		$("label[id^='cj_']").bind("click", function() {
			if ($(this).html() != "0") {
				var type = $(this).attr("id");
				var gridId = $("#gridId").val();
				var infoOrgCode = $("#infoOrgCode").val();
				var startTime = $("#startTime").val();
				var endTime = $("#endTime").val();
				var url = "${rc.getContextPath()}/zhsq/event/eventOverviewController/listEventA.jhtml?type="+type+"&gridId="+gridId+"&infoOrgCode="+infoOrgCode+"&startTime="+startTime+"&endTime=" + endTime;
				showMaxJqueryWindow("[" + $(this).parent().prev().html() + "]事件采集列表", url);
			}
		});
		$("label[id^='bj_']").bind("click", function() {
			if ($(this).html() != "0") {
				var type = $(this).attr("id");
				var gridId = $("#gridId").val();
				var infoOrgCode = $("#infoOrgCode").val();
				var startTime = $("#startTime").val();
				var endTime = $("#endTime").val();
				var url = "${rc.getContextPath()}/zhsq/event/eventOverviewController/listEventA.jhtml?type="+type+"&gridId="+gridId+"&infoOrgCode="+infoOrgCode+"&startTime="+startTime+"&endTime="+endTime+"&eventStatus=03,04";
				showMaxJqueryWindow("[" + $(this).parent().prev().prev().html() + "]事件办结列表", url);
			}
		});
		$(".tjdata").find(".light").each(function(i, ele) {
			if (i == 0) {
				$(this).bind("click", function() {
					PageApi.OverviewList(this, "00,01,02", "", true);
				});
			} else if (i == 1) {
				$(this).bind("click", function() {
					PageApi.OverviewList(this, "00,01,02", "", false);
				});
			} else if (i == 2) {
				$(this).bind("click", function() {
					PageApi.OverviewList(this, "00,01,02,03,04", "", false);
				});
			} else if (i == 3) {
				$(this).bind("click", function() {
					PageApi.OverviewList(this, "03,04", "", false);
				});
			} else if (i == 4) {
				$(this).bind("click", function() {
					PageApi.OverviewList(this, "", "2", false);
				});
			}
		});
	});
	
	PageApi.OverviewList = function(obj, eventStatus, remindStatus, isAll) {
		if ($(obj).html() != "0") {
			var gridId = $("#gridId").val();
			var gridName = $("#gridName").val();
			var infoOrgCode = $("#infoOrgCode").val();
			var startTime = "";
			var endTime = "";
			var status="";
			var superviseMark="";
			if (!isAll) {
				startTime = $("#startTime").val();
				endTime = $("#endTime").val();
			}
			
			if($(obj).attr('status')){
				status=$(obj).attr('status');
			}
			
			if($(obj).attr('superviseMark')){
				superviseMark=$(obj).attr('superviseMark');
			}
			
			//var url = "${rc.getContextPath()}/zhsq/event/eventOverviewController/listEventA.jhtml?gridId="+gridId+"&infoOrgCode="+infoOrgCode+"&startTime="+startTime+"&endTime="+endTime+"&eventStatus="+eventStatus+"&remindStatus="+remindStatus;
			var url='${rc.getContextPath()}/zhsq/event/eventDisposalController/listEvent.jhtml?t=all&model=l&extraParams=%7B%22remindStatus%22:%22'+remindStatus+'%22,%22createTimeStart%22:%22'+startTime+'%22,%22createTimeEnd%22:%22'+endTime+'%22,%22status%22:%22'+status+'%22,%22superviseMark%22:%22'+superviseMark+'%22%7D';
			
			showMaxJqueryWindow("[" + gridName + "]" + $(obj).prev().html(), url);
		}
	};
	
	PageApi.LoadData = function() {
		modleopen();
		var startTime = $("#startTime").val();
		var endTime = $("#endTime").val();
		$.ajax({
			type: "POST",
			url: '${rc.getContextPath()}/zhsq/event/eventOverviewController/listDataA.jhtml',
			data: {gridId:$("#gridId").val(),infoOrgCode:$("#infoOrgCode").val(),startTime:startTime,endTime:endTime,bigTypes:"${bigTypes!''}"},
			dataType: "json",
			success: function(info) {
				var data = info["overview"];
				data["SYWBJ"] = data["SYWBJ"] ? data["SYWBJ"] : 0;
				data["WBJ"] = data["WBJ"] ? data["WBJ"] : 0;
				data["CJ"] = data["CJ"] ? data["CJ"] : 0;
				data["BJ"] = data["BJ"] ? data["BJ"] : 0;
				data["DUBAN"] = data["DUBAN"] ? data["DUBAN"] : 0;
				$("#SYWBJ").html(data["SYWBJ"]);
				$("#WBJ").html(data["WBJ"]);
				$("#CJ").html(data["CJ"]);
				$("#BJ").html(data["BJ"]);
				var wcl = data["BJ"] / data["CJ"] * 100;
				$("#WCL").html(isNaN(wcl) ? "0.00%" : (wcl.toFixed(2) + "%"));
				$("#DUBAN").html(data["DUBAN"]);
				// 清空数据
				$("label[id^='cj_']").html("0");
				$("label[id^='bj_']").html("0");
				$("label[id^='wcl_']").html("0.00%");
				var details = info["details"];
				$.each(details, function(i, detail) {
					$("label[id^='cj_" + detail["TYPE_"] + "']").html(detail["CJ"]);
					$("label[id^='bj_" + detail["TYPE_"] + "']").html(detail["BJ"]);
					var wcl = detail["BJ"] / detail["CJ"] * 100;
					$("label[id^='wcl_" + detail["TYPE_"] + "']").html(isNaN(wcl) ? "0.00%" : (wcl.toFixed(2) + "%"));
				});
				<#if isShowSatisfyRate == '1'>
				// 满意率
				data["VERY_SATISFY"] = data["VERY_SATISFY"] ? data["VERY_SATISFY"] : 0;
				data["SATISFY"] = data["SATISFY"] ? data["SATISFY"] : 0;
				data["TOTAL_SATISFY"] = data["TOTAL_SATISFY"] ? data["TOTAL_SATISFY"] : 0;
				var myl = (data["SATISFY"] + data["VERY_SATISFY"]) / data["TOTAL_SATISFY"] * 100;
				$("#MYL").html(isNaN(myl) ? "0.00%" : (myl.toFixed(2) + "%"));
				
				$("label[id^='myl_']").html("0.00%");
				
				var mylDetails = info["mylDetails"];
				$.each(mylDetails, function(i, detail) {
					detail["VERY_SATISFY"] = detail["VERY_SATISFY"] ? detail["VERY_SATISFY"] : 0;
					detail["TOTAL_SATISFY"] = detail["TOTAL_SATISFY"] ? detail["TOTAL_SATISFY"] : 0;
					detail["SATISFY"] = detail["SATISFY"] ? detail["SATISFY"] : 0;
					var myl = (detail["SATISFY"] + detail["VERY_SATISFY"]) / detail["TOTAL_SATISFY"] * 100;
					$("label[id^='myl_" + detail["TYPE_SATISFY"] + "']").html(isNaN(myl) ? "0.00%" : (myl.toFixed(2) + "%"));
				});
	            </#if>
				modleclose();
			},
			error:function(data) {
				alert('连接超时！');
				modleclose();
			}
		});
	};
	
	PageApi.DoSearch = function() {
		PageApi.LoadData();
	};
</script>
</head>

<body>

<div class="MainContent">
	<div class="ConSearch">
        <div class="fl">
        	<ul>
            	<li style="margin-right:0;">所属网格：</li>
                <li>
                	<input type="hidden" id="infoOrgCode" name="infoOrgCode" value="${infoOrgCode!''}"/>
                	<input type="hidden" id="gridId" name="gridId" value="${gridId?c}"/>
                	<input id="gridName" name="gridName" type="text" class="inp1" style="width:165px;" value="${gridName!''}"/>
                </li>
            	<li style="margin-right:0;">日期：</li>
                <li>
				    <input id="date1" type="text" class="inp1" style="width: 180px;" value=""/>
					<input type="text" id="startTime" name="startTime" value="${yesterday!''}" style="display:none;"/>
					<input type="text" id="endTime" name="endTime" value="${yesterday!''}" style="display:none;"/>
                </li>
            </ul>
        </div>
        <div class="btns">
        	<ul>
            	<li>
            		<a href="#" class="chaxun" title="点击查询" onclick="PageApi.DoSearch();">查询</a>
            	</li>
            </ul>
        </div>
        <div class="clear"></div>
    </div>
    <div class="h_10"></div>
	<div class="tjdata">
    	<div class="list">
        	<dl>
            	<dt class="fl"><img src="${rc.getContextPath()}/images/icon2_03.png" /></dt>
                <dd class="title">总待办量</dd>
                <dd id="SYWBJ" status="00,01,02" class="data FontRed light">0</dd>
            </dl>
        	<dl>
            	<dt class="fl"><img src="${rc.getContextPath()}/images/icon2_09.png" /></dt>
                <dd class="title">待办量</dd>
                <dd id="WBJ" status="00,01,02" class="data FontPurple light">0</dd>
            </dl>
        	<dl>
            	<dt class="fl"><img src="${rc.getContextPath()}/images/icon2_05.png" /></dt>
                <dd class="title">采集量</dd>
                <dd id="CJ" class="data FontCyan light">0</dd>
            </dl>
        	<dl>
            	<dt class="fl"><img src="${rc.getContextPath()}/images/icon2_07.png" /></dt>
                <dd class="title">办结量</dd>
                <dd id="BJ" status="04" class="data FontOrange light">0</dd>
            </dl>
        	<dl>
            	<dt class="fl"><img src="${rc.getContextPath()}/images/icon2_11.png" /></dt>
                <dd class="title">完成率(%)</dd>
                <dd id="WCL" class="data FontBlue">0.00%</dd>
            </dl>
            <#if isShowSatisfyRate == '1'>
            <dl>
            	<dt class="fl"><img src="${rc.getContextPath()}/images/icon2_11.png" /></dt>
                <dd class="title">满意率(%)</dd>
                <dd id="MYL" class="data FontBlue">0.00%</dd>
            </dl>
            </#if>
        	<dl>
            	<dt class="fl"><img src="${rc.getContextPath()}/images/icon2_13.png" /></dt>
                <dd class="title">督办件</dd>
                <dd id="DUBAN" superviseMark="1" class="data FontGreen light">0</dd>
            </dl>
            <div class="clear"></div>
        </div>
    </div>
    <div class="ConList">
        <div class="xmmain">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td colspan="<#if isShowSatisfyRate == '1'>10<#else>8</#if>" class="xm_tit_h3">各类事件受理情况</td>
				</tr>
				${bigTypeTBody!''}
			</table>
		</div>
	</div>
</div>
<#include "/component/maxJqueryEasyUIWin.ftl" />
</body>
</html>

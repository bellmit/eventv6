<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>出租屋概要信息</title>
<#include "/component/commonFiles-1.1.ftl" />
<script src="${rc.getContextPath()}/js/echarts/echarts.min.js"></script>
<style>
body,p,h2,h3,h4,h5,ul,li{ margin:0; padding:0;}
ul,li{ list-style:none;}
.letMainer{ font-family:Verdana, Geneva, sans-serif, "微软雅黑", "冬青黑体简体中文 W3"; color:#666; font-size:12px;}
.letMainer h3,.letMainer h4{ font-size:14px; text-align:center; padding:5px 0;}
.letMainer h4{ padding-bottom:10px; padding-top:0;}
.letMainer h4{ color:#ff4108; font-weight:normal;}
.let-con{ float:left;width:340px; margin:0 5px}
.let-con h5{ background:#fafafa; height:35px; line-height:35px; padding:0 10px; font-size:14px;}
.orglist{ margin:0 10px;}
.orglist li{ padding:0 5px; border-bottom:1px solid #f2f2f2; height:35px; line-height:35px;}
.let-no{ color:#ff4e18; font-size:16px;}
.implist li{ float:left; border-right:1px solid #ececec; text-align:center; width:33%; padding:5px 0; margin:10px 0;}

a {color:red;text-decoration:underline;}
</style>

</head>

<body>
<div class="letMainer">
  <h3>${gridInfo.gridName!''}</h3>
  <h4>出租房：<a href="javascript:void(0);" onclick="openRentRoomList();"><#if info.RENT_ROOM_NUM??>${info.RENT_ROOM_NUM}<#else>0</#if></a>个
  		<#if info.IS_SHOW_RENT_ROOM?? && info.IS_SHOW_RENT_ROOM == "1"><img id="icon_help1" style="cursor:pointer;vertical-align:middle;" src="${uiDomain}/images/ly_46.png" height="18"/></#if>
  		，
           流动人口：<a href="javascript:void(0);" onclick="openPopuDistributionMap();"><#if info.LD_POPU_NUM??>${info.LD_POPU_NUM}<#else>0</#if></a>人
        <#if info.IS_SHOW_LD_POPU?? && info.IS_SHOW_LD_POPU == "1">
        <img id="icon_help2" style="cursor:pointer;vertical-align:middle;" src="${uiDomain}/images/ly_46.png" height="18"/>
        </#if>
  </h4>
  <div class="let-con">
    <h5>重点人群</h5>
    <div id="main1" style="width:340px; height: 200px;"></div>
  </div>
  <div class="let-con">
    <h5>组织队伍</h5>
    <ul class="orglist">
      <li>综治机构：<span class="let-no"><a href="javascript:void(0);" onclick="OpenUrlByType('A');"><#if info.TEAM_SUM??>${info.TEAM_SUM}<#else>0</#if></a>个</span>，
		机构成员：<span class="let-no"><a href="javascript:void(0);" onclick="OpenUrlByType('B');"><#if info.TEAM_MEMBER_SUM??>${info.TEAM_MEMBER_SUM}<#else>0</#if></a>人</span></li>
      <li>群防群治组织：<span class="let-no"><a href="javascript:void(0);" onclick="OpenUrlByType('C');"><#if info.ORG_TEAM_SUM??>${info.ORG_TEAM_SUM}<#else>0</#if></a>个</span>，
		组织成员：<span class="let-no"><a href="javascript:void(0);" onclick="OpenUrlByType('D');"><#if info.ORG_TEAM_MEMBER_SUM??>${info.ORG_TEAM_MEMBER_SUM}<#else>0</#if></a>人</span></li>
      <li>党员：<span class="let-no"><a href="javascript:void(0);" onclick="OpenUrlByType('E');"><#if info.PARTY_NUM??>${info.PARTY_NUM}<#else>0</#if></a>个</span></li>
      <li>志愿者：<span class="let-no"><a href="javascript:void(0);" onclick="OpenUrlByType('F');"><#if info.VOLUNTEER_NUM??>${info.VOLUNTEER_NUM}<#else>0</#if></a>人</span></li>
      <li>网格力量：<span class="let-no"><a href="javascript:void(0);" onclick="OpenUrlByType('G');"><#if info.GRIDADMIN_NUM??>${info.GRIDADMIN_NUM}<#else>0</#if></a>人</span></li>
    </ul>
  </div>
</div>
</body>
<script>

$(document).ready(function() {
	readInBuildingFloor();
	<#if info.IS_SHOW_RENT_ROOM?? && info.IS_SHOW_RENT_ROOM == "1">
	$("#icon_help1").tooltip({
	    position: 'right',
	    content: '<span id="_log_tip_id1"></span>',
	    onShow: function() {
	    	$("#_log_tip_id1").html("此出租屋的数据大于均值(${info.RENT_ROOM_AVG_NUM?c})，请注意做好出租屋的管理。");
	    	$(this).tooltip('tip').css({
	    		width: '200px'
	    	});
	    }
	});
	</#if>
	<#if info.IS_SHOW_LD_POPU?? && info.IS_SHOW_LD_POPU == "1">
	$("#icon_help2").tooltip({
	    position: 'right',
	    content: '<span id="_log_tip_id2"></span>',
	    onShow: function() {
	    	$("#_log_tip_id2").html("此流动人口的数据大于均值(${info.LD_POPU_AVG_NUM?c})，请注意做好流动人口的管理。");
	    	$(this).tooltip('tip').css({
	    		width: '200px'
	    	});
	    }
	});
	</#if>
});

function OpenUrlByType(type) {
	var title = "", url = "";
	if (type == "A") {
		title = "综治机构";
		url = "${GMIS_URL!''}/gmis/prvetionTeam/orgTeam/index.jhtml?bizType=0&isOuter=true&regionCode=${gridInfo.infoOrgCode!''}";
	} else if (type == "B") {
		title = "机构成员";
		url = "${GMIS_URL!''}/gmis/teamMembers/index.jhtml?bizType=0&isOuter=true&regionCode=${gridInfo.infoOrgCode!''}";
	} else if (type == "C") {
		title = "群防群治组织";
		url = "${GMIS_URL!''}/gmis/prvetionTeam/index.jhtml?bizType=1&isOuter=true&regionCode=${gridInfo.infoOrgCode!''}";
	} else if (type == "D") {
		title = "群防群治组织成员";
		url = "${GMIS_URL!''}/gmis/teamMembers/index.jhtml?bizType=1&isOuter=true&regionCode=${gridInfo.infoOrgCode!''}";
	} else if (type == "E") {
		title = "党员";
		url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofpoplocal/toPopuPage.jhtml?type=partyMerber&gridId=${gridInfo.gridId?c}";
	} else if (type == "F") {
		title = "志愿者";
		url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofpoplocal/toPopuPage.jhtml?type=volunteer&gridId=${gridInfo.gridId?c}";
	} else if (type == "G") {
		title = "网格力量";
		url = "${SQ_ZZGRID_URL!''}/zzgl/grid/gridAdmin/index.jhtml?isOuter=true&regionCode=${gridInfo.infoOrgCode!''}";
	}
	window.parent.showMaxJqueryWindow(title, url);
}

function openPopuDistributionMap() {
	window.parent.showMaxJqueryWindow("流动人口分布图", "${BI_REPORT!''}/report/PopulationFlowNewController/view?orgcode=${gridInfo.infoOrgCode!''}");
}

function openRentRoomList() {
	window.parent.showMaxJqueryWindow("出租屋列表", "${SQ_ZZGRID_URL!''}/zzgl/grid/areaRoomRent/index.jhtml?isOuter=true&regionCode=${gridInfo.infoOrgCode!''}");
}

function readInBuildingFloor() {
	var nums = [
		<#if info.DRUG_NUM??>${info.DRUG_NUM}<#else>0</#if>,
		<#if info.CORRECT_NUM??>${info.CORRECT_NUM}<#else>0</#if>,
		<#if info.AIDS_NUM??>${info.AIDS_NUM}<#else>0</#if>,
		<#if info.RELEASED_NUM??>${info.RELEASED_NUM}<#else>0</#if>,
		<#if info.PSYCHIATRIC_NUM??>${info.PSYCHIATRIC_NUM}<#else>0</#if>
	];
	var index = 0, max=0;
	for (var i=0;i<nums.length;i++) {
		if (nums[i] > max) {
			max = nums[i];
		}
	}
	
	var myChart = echarts.init(document.getElementById('main1'));
	var option = {
		
		radar : {
			// shape: 'circle',
			indicator : [ {
				name : '吸毒',
				max : max
			}, {
				name : '社区矫正',
				max : max
			}, {
				name : '艾滋病',
				max : max
			}, {
				name : '刑满释放',
				max : max
			}, {
				name : '精神病',
				max : max
			} ],
			name: {
                textStyle: {
                    color:'#000000',
                    fontSize: 12
                },
                formatter :function (value, indicator) {
				    return value + '('+nums[index++]+'人)';
				}
            },
            radius: "60%",
            center: ["50%","49%"]
		},
		series : [ {
			name : '',
			type : 'radar',
			areaStyle : {
				normal : {
					opacity : 0.5,
					color : new echarts.graphic.RadialGradient(0.5, 0.5, 1, [ {
						color : '#B8D3E4',
						offset : 0
					}, {
						color : '#72ACD1',
						offset : 1
					} ])
				}
			},
			data : [ {
				value : [ 0, 0, 0, 0, 0 ],
				name : '重点人群：',
                symbol: 'rect',
                symbolSize: 5,
                lineStyle: {
					normal: {
						type: 'dashed'
					}
				}
			} ]
		} ]
	};

	option.series[0].data[0].value = nums;
	myChart.setOption(option);
	myChart.on('click', function(param) {
		var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofpoplocal/toKeyPopuPage.jhtml?gridId=${gridInfo.gridId?c}";
		window.parent.showMaxJqueryWindow("重点人群", url);
	});
}
</script>
</html>

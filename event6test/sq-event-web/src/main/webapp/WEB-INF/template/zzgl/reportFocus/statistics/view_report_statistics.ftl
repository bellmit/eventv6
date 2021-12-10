<!DOCTYPE html>
<html>

<head>
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>事项统计</title>
    <#include "/component/standard_common_files-1.1.ftl" />
    <link rel="stylesheet" href="${ANOLE_COMPONENT_URL}/js/components/date/css/date.css" type="text/css"/>
    <script type="text/javascript" src="${ANOLE_COMPONENT_URL}/js/components/date/jquery.anole.date.js"></script>
	<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/plugins/layui-v2.5.5/layui/css/layui.css" />
	<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/common/css/reset.css" />
	<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/_zhcs/nanan/css/global.css" />
	<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/_zhcs/nanan/css/layuiExtend.css" />
	<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/_zhcs/nanan/css/main.css" />
    <link rel="stylesheet" href="${ANOLE_COMPONENT_URL}/js/components/date/css/date.css" type="text/css"/>
    <script type="text/javascript" src="${ANOLE_COMPONENT_URL}/js/components/date/jquery.anole.date.js"></script>
    <#include "/component/ComboBox.ftl" />
    <script type="text/javascript" src="${uiDomain!''}/js/openJqueryEasyUIWin.js"></script>
	<style>
	.col-per-23 {width: 300px;}
	.stat-hei1 {
    height: -webkit-calc(100% - 225px);
    height: calc(100% - 225px);
}.stat-hei2 {
    height: -webkit-calc(100% - 76px);
    height: calc(100% - 76px);
}
.stat-table1>tbody td {
    line-height: 50px;
}
	</style>
</head>

<body class="bg-f5f5f5">

    <form id="delEventQueryForm">
        <input type="hidden" id="reportType" name="reportType" class="queryParam" value="<#if reportType??>${reportType?c}</#if>" />
	<!-- 内容区域 -->
	<div class="layui-row layui-calc-h1">
		<div class="layui-fluid">
			<div class="layui-col-xs12 mt15">
				<div class="layui-row layui-form mt20 layui-sch-g" style="margin-top: 5px;">
					<div class="layui-col-xs12">
						<p class="text-nor font-size-15 cor-333 font-bold fl ml15">数据筛选</p>
					</div>
					<div class="layui-col-xs12 mt10">
						<div class="col-per-23" >
							<div class="layui-form-item">
								<label class="layui-form-label">所属区域</label>
								<div class="layui-input-block">
									<input id="infoOrgCode" name="infoOrgCode" type="text" class="hide queryParam" value="${infoOrgCode!}"/>
									<input id="eOrgCode" name="eOrgCode" type="text" class="hide queryParam"/>
									<input id="gridId" type="text" class="hide" value="${gridId}"/>
									<input id="gridName" type="text" class="layui-input" style="width:90%;" />
								</div>
							</div>
						</div>
						<div class="col-per-23">
							<div class="layui-form-item">
								<label class="layui-form-label" style="width:60px">报告时间</label>
								<div class="layui-input-block">
									<input class="inp1 hide queryParam" type="text" id="beginTime" name="beginTime" value="<#if beginTime??>${beginTime}</#if>"></input>
	                	<input class="inp1 hide queryParam" type="text" id="endTime" name="endTime" value="<#if endTime??>${endTime}</#if>"></input>
	                	<input type="text" id="_createTimeDateRender" class="layui-input" style="width:220px;" value="<#if beginTime??>${beginTime}</#if> ~ <#if endTime??>${endTime}</#if>"/>
								</div>
							</div>
						</div>
						<div class="col-per-28 ml20">
							<button type="button" class="layui-btn layui-btn-normal" onclick="searchData()">查询</button>
							<button type="button" class="layui-btn layui-btn-normal" onclick="resetCondition()">重置</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</form>
	<!-- 统计栏 -->
	<div class="layui-row mt15 layui-calc-h2" style="margin-top: 5px;">
		<div class="layui-fluid">
			<div class="layui-stat-bar1 layui-stat-bg1">
				<i class="layui-stat-icon1"></i>
				<div class="layui-stat-item">
					<h5>在办超期量</h5>
					<p><em id="ZBCQ">0</em>件</p>
				</div>
			</div>
			<div class="layui-stat-bar1 layui-stat-bg2">
				<i class="layui-stat-icon2"></i>
				<div class="layui-stat-item">
					<h5>已办结超期量</h5>
					<p><em id="BJCQ">0</em>件</p>
				</div>
			</div>
			<div class="layui-stat-bar1 layui-stat-bg3">
				<i class="layui-stat-icon3"></i>
				<div class="layui-stat-item">
					<h5>督办量</h5>
					<p><em id="DUBAN">0</em>件</p>
				</div>
			</div>
			<div class="layui-stat-bar1 layui-stat-bg4">
				<i class="layui-stat-icon4"></i>
				<div class="layui-stat-item">
					<h5>采集量</h5>
					<p><em id="TOTAL">0</em>件</p>
				</div>
			</div>
			<div class="layui-stat-bar1 layui-stat-bg5">
				<i class="layui-stat-icon5"></i>
				<div class="layui-stat-item">
					<h5>办结量</h5>
					<p><em id="BANJIE">0</em>件</p>
				</div>
			</div>
			<div class="layui-stat-bar1 layui-stat-bg6">
				<i class="layui-stat-icon6"></i>
				<div class="layui-stat-item">
					<h5>办结率</h5>
					<p><em id="BANJIELV">0</em>%</p>
				</div>
			</div>
		</div>
	</div>

	<!-- 数据列表 -->
	<div class="layui-row stat-hei1 mt15" style="margin-top: 5px;">
		<div class="layui-fluid height-p100">
			<div class="layui-col-xs12 height-p100">
				<div class="ml20 mt20 mr20">
					<table class="stat-table1 width-p100">
						<colgroup>
							<col>
							<col width="12%">
							<col width="12%">
							<col width="12%">
							<col width="12%">
							<col width="12%">
							<col width="12%">
						</colgroup>
						<thead>
							<tr>
								<th>场景应用名称</th>
								<th>在办超期量</th>
								<th>已办结超期量</th>
								<th>督办量</th>
								<th>采集量</th>
								<th>办结量</th>
								<th>办结率</th>
							</tr>
						</thead>
					</table>
				</div>
				<div class="stat-hei2 ml20 mr20 h-x">
					<table class="stat-table1 width-p100">
						<colgroup>
							<col>
							<col width="12%">
							<col width="12%">
							<col width="12%">
							<col width="12%">
							<col width="12%">
							<col width="12%">
						</colgroup>
						<tbody id="tableList">
							
							
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</body>
<script>
	$(function () {
		
		$('.h-x').niceScroll({
			cursorcolor: "#ccc",
			cursoropacitymax: 0.8,
			cursorwidth: "4px",
			cursorborderradius: "2px",
			cursorborder: 'none',
			autohidemode: false,
		});
		 AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId, items){
            if(items!=undefined && items!=null && items.length>0){
                var grid = items[0];
                $("#infoOrgCode").val(grid.orgCode);
                $("#eOrgCode").val(grid.eOrgCode);
            }
        });
createTimeDateRender  = $('#_createTimeDateRender').anoleDateRender({
                BackfillType : "1",
                ChoiceType : "1",		// 选择方式（0-起始和结束时间必须都有，1-起始和结束时间有一个即可，2-起始和结束时间随意）
                ShowOptions : {
                    TabItems : ["常用", "年", "季", "月", "清空"]
                },
                BackEvents : {
                    OnSelected : function(api) {
                        $("#beginTime" ).val(api.getStartDate());
                        $("#endTime").val(api.getEndDate());
                    },
                    OnCleared : function() {
                        $("#beginTime" ).val('');
                         $("#endTime").val('');
                    }
                }
            }).anoleDateApi();
		loadDataList();
	});
	function isNotBlankString(str) {
	return !isBlankString(str);
}
function isBlankString(str) {
	return str==undefined || str==null || str=="";
}
	function loadDataList() {
		var dbIndex = layer.load();
		 $.ajax({
			type: "POST",
			url:'${rc.getContextPath()}/zhsq/statistics/listData.json' ,
			data: queryData(),
			dataType:"json",
			success: function(data){
				layer.close(dbIndex);
				var column = {BANJIE: 0,BJCQ: 0,DUBAN: 0,TOTAL: 0,ZBCQ: 0};
				var html ="";
				for(var i=0,l=data.length;i<l;i++){
					for(var j in column){
						column[j]+= parseInt(data[i][j]);
					}
					html +='<tr><td><p class="cor-4d text-align-c font-size-16">'+data[i].moduleName+'</p></td>'+
								'<td><p class="stat-cor1 text-align-c font-size-16"><span onclick="dataDetail(\'' + data[i].REPORT_TYPE + '\', \'' + data[i].moduleName + '\', \'&overDue=1&jurisdiction=1\')" style="cursor:pointer;">'+data[i].ZBCQ+'</span></p></td>'+
								'<td><p class="stat-cor1 text-align-c font-size-16"><span onclick="dataDetail(\'' + data[i].REPORT_TYPE + '\', \'' + data[i].moduleName + '\', \'&overDue=1&jurisdiction=2\')" style="cursor:pointer;">'+data[i].BJCQ+'</span></p></td>'+
								'<td><p class="stat-cor1 text-align-c font-size-16"><span onclick="dataDetail(\'' + data[i].REPORT_TYPE + '\', \'' + data[i].moduleName + '\', \'&capDB=1&jurisdiction=3\')" style="cursor:pointer;">'+data[i].DUBAN+'</span></p></td>'+
								'<td><p class="stat-cor1 text-align-c font-size-16"><span onclick="dataDetail(\'' + data[i].REPORT_TYPE + '\', \'' + data[i].moduleName + '\', \'&jurisdiction=3\')" style="cursor:pointer;">'+data[i].TOTAL+'</span></p></td>'+
								'<td><p class="stat-cor1 text-align-c font-size-16"><span onclick="dataDetail(\'' + data[i].REPORT_TYPE + '\', \'' + data[i].moduleName + '\', \'&jurisdiction=2\')" style="cursor:pointer;">'+data[i].BANJIE+'</span></p></td>'+
								'<td><p class="cor-4d text-align-c font-size-16" >'+data[i].BANJIELV+'</p></td>'+
								
							'</tr>';
				}
				$("#tableList").html(html);
				for(var j in column){
					$("#"+j).html(column[j]);
				}
				var bjl = column.TOTAL==0?0:(column.BANJIE / column.TOTAL*100);
				$("#BANJIELV").html(bjl.toFixed(2));
			},
			error:function(data){
				layer.close(dbIndex);
				$.messager.alert('错误','添加关注失败！','error');
			}
		});
	}
	
    function gridTreeClickCallback(gridId,gridName,orgId,infoOrgCode,gridInitPhoto) {
        $("#infoOrgCode").val(infoOrgCode);
        $("#gridId").val(gridId);
        $("#beginTime").val("${beginTime!}");
        $("#endTime").val("${endTime!}");
        loadDataList();
    }

    //查询
    function searchData() {
        loadDataList();
    }

    function queryData() {
        var searchArray ={};

        $("#delEventQueryForm .queryParam").each(function() {
            var val = $(this).val(), key = $(this).attr("name");

            if(isNotBlankString(val) && isBlankString(searchArray[key])){
                searchArray[key] = val;
            }
        });

        return searchArray;
    }

    function exportData() {
        var infoOrgCode = $("#infoOrgCode").val();
        var gridId = $("#gridId").val();
        var beginTime = $("#beginTime").val();
        var endTime = $("#endTime").val();
        var url = '${rc.getContextPath()}/zhsq/timeApplicationReportFordelEvent/exportData.jhtml?infoOrgCode='+ infoOrgCode +'&gridId='+ gridId +'&beginTime=' + beginTime +'&endTime=' + endTime + '&reportType=' + $('#reportType').val();

        $.messager.confirm('提示','确定导出数据吗？',function (r) {
            if(r){
                location.href = url;
            }
        });
    }
    function resetCondition() {
        $('#eOrgCode').val("");
        $('#gridName').val("");
        $('#beginTime').val("${beginTime}");
        $('#endTime').val("${endTime}");
		$('#_createTimeDateRender').val("<#if beginTime??>${beginTime}</#if> ~ <#if endTime??>${endTime}</#if>");
        $('#infoOrgCode').val("${infoOrgCode}");
        searchData();
    }
    function dataDetail(REPORT_TYPE,moduleName,extraParams) {
        //在办超期
        var doingOverDue = true;
        var eOrgCode = $('#eOrgCode').val()||'';
        var gridId = $('#gridId').val()||'';
        var reportDayStart = $('#beginTime').val()||'';
        var reportDayEnd = $('#endTime').val()||'';
        var params = 'listType=52&isCapRegionPath=1&eRegionCode='+eOrgCode+'&gridId='+gridId + '&reportType=' + REPORT_TYPE+'&reportDayStart='+reportDayStart+'&reportDayEnd='+reportDayEnd;

        var url = '${rc.getContextPath()}/zhsq/eventAndReportJsonp/reportFocusDoingList.jhtml?' + params + extraParams,
            opt = {
                'title': "查看"+ moduleName +"信息",
                'targetUrl': url,
				top:10
            };

        openJqueryWindowByParams($.extend({},null, opt));
    }
</script>



</html>
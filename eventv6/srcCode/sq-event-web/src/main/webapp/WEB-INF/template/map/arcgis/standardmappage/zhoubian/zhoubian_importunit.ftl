<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>周边重点单位</title>


<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>

<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />

</head>
<body style="border:none">
	<input type="hidden" id="pageSize" value="20" />
	<input type="hidden" id="elementsCollectionStr" value="<#if elementsCollectionStr??>${elementsCollectionStr}</#if>" />
    <div class="" style="display:block;">
    	<div class="ListSearch">
        	<div class="MetterCondition">
            	<ul class="content" style="height:26px;">
                	<li class="LC1" style="float:left; line-height:26px; width:60px;">单位名称：</li>
                	<li class="LC2" style="float:left; line-height:26px;"><input id="name" name="name" type="text" class="inp1" /></li>
                </ul>
                <ul class="content" style="height:26px;">
                	<li class="LC1" style="float:left; line-height:26px; width:60px;">单位地址：</li>
                	<li class="LC2" style="float:left; line-height:26px;"><input id="address" name="address" type="text" class="inp1" /></li>
                </ul>
                <ul id="content-md2" class="type content light" style="height: 153px;">
                	<p>单位类型：</p>
            		<#if importUnitTypeDC??>
						<#list importUnitTypeDC as l>
							<li onclick="selectImportUnitType(this)" id="${l.COLUMN_VALUE}" value="${l.COLUMN_VALUE}">${l.COLUMN_VALUE_REMARK}</li>
						</#list>
					</#if>
                    <div class="clear"></div>
                </ul>
				<div class="SearchBtn"><input name="" type="button" value="查询" class="NorBtn" onclick="loadMessage(1,$('#pageSize').val(),'searchBtn');" /></div>
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
		var temp = $(".ListSearch").is(":hidden");// 是否隐藏
		if (temp == false) {
			$(".ListSearch").hide();
		} else {
			$(".ListSearch").show();
		}
		// var temp1= $(".ListSearch").is(":visible");//是否可见
	}
	
	$('#name').keydown(function(e) {
		if (e.keyCode == 13) {
			loadMessage(1, $("#pageSize").val());
		}
	});
	
	$('#address').keydown(function(e) {
		if (e.keyCode == 13) {
			loadMessage(1, $("#pageSize").val());
		}
	});
	
	function CloseSearchBtn() {
		$(".ListSearch").hide();
	}
	
	$(document).ready(function() {
		try {
			var winHeight = window.parent.document.getElementById('map' + window.parent.currentN).offsetHeight - 62;
			$("#content").height(winHeight - 56);
		} catch (e) {}
		loadMessage(1, $("#pageSize").val());
	});
	
	var results = "";// 获取定位对象集合
	var layerName="";
	function loadMessage(pageNo, pageSize,searchType) {
		layerName = window.parent.analysisOfElementsCollection($('#elementsCollectionStr').val(),"menuLayerName");
		window.parent.currentLayerName = layerName;
		if('searchBtn'==searchType) {
			window.parent.clearSpecialLayer(layerName);
			window.parent.currentListNumStr = "";
		}
		results = "";
		$.blockUI({
			message : "加载中...",
			css : {
				width : '150px',
				height : '50px',
				lineHeight : '50px',
				top : '40%',
				left : '20%',
				background : 'url(${rc.getContextPath()}/css/loading.gif) no-repeat',
				textIndent : '20px'
			},
			overlayCSS : {
				backgroundColor : '#fff'
			}
		});
		
		var importUnitTypes = "";
		$('.type li').each(function(index) {
			if($(this).attr('class')=='current'){
				importUnitTypes = $(this).attr('id') + "," + importUnitTypes; 
			}
		});
		
		if (importUnitTypes!=null && importUnitTypes!="" && importUnitTypes.length > 0) {
			importUnitTypes = importUnitTypes.substring(0, importUnitTypes.length - 1);
		}
		
		$.ajax({
			type : "POST",
			url : '${rc.getContextPath()}/zhsq/map/zhoubian/zhouBianStat/queryZhouBianList.json?t=' + Math.random(),
			data : {
				pageNo : pageNo,
				pageSize : $("#pageSize").val(),
				name : $('#name').val(),
				address : $('#address').val(),
				type : importUnitTypes,
				mapType : "<#if mapType??>${mapType}</#if>",
				distance : "<#if distance??>${distance}</#if>",
				x : "<#if x??>${x}</#if>",
				y : "<#if y??>${y}</#if>",
				infoOrgCode : "${infoOrgCode}",
				zhoubianType : "${zhoubianType}"
			},
			dataType : "json",
			success : function(data) {
				$.unblockUI();
				// 设置页面页数
				$('#pagination-num').text(pageNo);
				if (pageNo == 1) {
					$('#records').text(data.total);
				} else {
					data.total = parseInt($('#records').text());
				}
				var totalPage = Math.floor(data.total / pageSize);
				if (data.total % pageSize > 0)
					totalPage += 1;
				$('#pageCount').text(totalPage);
				var list = data.rows;
				var tableBody = "";
				tableBody += '<div class="liebiao">';
				if (list && list.length > 0) {
					for ( var i = 0; i < list.length; i++) {
						var val = list[i];

						var name = val.ENTERPRISE_NAME;
						var nameTitle = "";
						if (name != null && name != "" && name.length >= 12) {
							nameTitle = "单位名称："+name;
							name = name.substring(0, 12) + "...";
						}

						var address = val.ADDRESS;
						var addressTitle = "";
						if (address != null && address != "" && address.length >= 12) {
							addressTitle = "单位地址："+address;
							address = address.substring(0, 12) + "...";
						}
						
						var distance = val.DISTANCE;
						if(distance==undefined || distance==null || distance==""){
							distance = 0;
						}

						tableBody += '<dl onclick="selected('+val.IMPORT_UNIT_ID+')">';
						tableBody += '<dt>';
						tableBody += '<span class="fr">'+val.TYPE_LABEL+'</span>';
						tableBody += '<b class="FontDarkBlue" title=\'' + nameTitle + '\'>' + name + '</b>';
						tableBody += '</dt>';
						tableBody += '<dd title=\'' + addressTitle + '\'>';
						if(distance != 0){
							tableBody += '<span class="fr">距离：'+distance+'米</span>';
						}
						tableBody += address + '</dd>';
						tableBody += '</dl>';

						results = results + "," + val.IMPORT_UNIT_ID;
					}
					results = results.substring(1, results.length);
				} else {
					tableBody+='<div style="text-align: center;"><img style="width: 174px;" src="${uiDomain!''}/images/map/gisv0/special_config/images/nodata.png" title="暂无数据"/></div>';
				}
				tableBody += '</div>';
				$("#content").html(tableBody);
				$(".AdvanceSearch").css("display", "none");
				gisPosition(results);
			},
			error : function(data) {
				$.unblockUI();
				var tableBody = '<div class="liebiao"><ul>数据读取错误！！！</ul></div>';
				$("#content").html(tableBody);
			}
		});
		CloseSearchBtn();
	}
	var currentPageNum=1;
	// 分页
	function change(_index) {
		var flag;
		var pagenum = $("#pagination-num").text();
		var lastnum = $("#pageCount").text();
		var pageSize = $("#pageSize").val();
		var firstnum = 1;
		switch (_index) {
		case '1': // 上页
			if (pagenum == 1) {
				flag = 1;
				break;
			}
			pagenum = parseInt(pagenum) - 1;
			pagenum = pagenum < firstnum ? firstnum : pagenum;
			break;
		case '2': // 下页
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
			alert("当前已经是首页");
			return;
		} else if (flag == 2) {
			alert("当前已经是尾页");
			return;
		}
		currentPageNum = pagenum;
		loadMessage(pagenum, pageSize);
	}
	
	$("#moreSearch").toggle(function() {
		$(".AdvanceSearch").css("display", "block");
	}, function() {
		$(".AdvanceSearch").css("display", "none");
	});
	
	function selected(id) {
		setTimeout(function() {
			window.parent.localImportUnitPoint(id, '');
		}, 1000);
		setTimeout(function() {
			if($('#elementsCollectionStr').val() != "") {
				window.parent.getDetailOnMapOfListClick($('#elementsCollectionStr').val(),360,260,id)
			}else {
				window.parent.localImportUnitPoint(id, '');
			}
		},1000);
	}
	
	// 地图定位
	function gisPosition(res) {
		if("1" != window.parent.IS_ACCUMULATION_LAYER) {
			window.parent.clearSpecialLayer(layerName);
		}else {
			if(window.parent.currentListNumStr.indexOf(currentPageNum+"")>=0) {
				//return;
			}else {
				window.parent.currentListNumStr = window.parent.currentListNumStr+","+currentPageNum;
			}
		}
		if (res == "") {
			//return;
		}
		
		
		if($('#elementsCollectionStr').val() != "") {
			var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofthing/getArcgisImportUnitLocateDataList.jhtml?ids=" + res + "&showType=2";
			window.parent.currentLayerLocateFunctionStr="getArcgisDataOfZhuanTi('"+url+"','"+$('#elementsCollectionStr').val()+"')";
			window.parent.getArcgisDataOfZhuanTi(url,$('#elementsCollectionStr').val(),360,260);
		}else {
			var importUnitUrl = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofthing/getArcgisImportUnitLocateDataList.jhtml?ids=" + res + "&showType=2";
			window.parent.currentLayerLocateFunctionStr = "getArcgisDataOfImportUnit('" + importUnitUrl + "','')";
			window.parent.getArcgisDataOfImportUnit(importUnitUrl, '');
		}
	}
	
	//--网格职务选择
	function selectImportUnitType(obj){
		if($(obj).attr('class')!='current'){
			$(obj).addClass("current");
		}else{
			$(obj).removeClass("current");
		}
	}
	</script>
</body>
</html>
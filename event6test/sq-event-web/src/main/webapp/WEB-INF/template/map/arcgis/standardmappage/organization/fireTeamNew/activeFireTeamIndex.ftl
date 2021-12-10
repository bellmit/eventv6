<!DOCTYPE html>
<html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>现役消防队列表</title>
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
</head>
<body style="border:none;scolling:yes">
	<input type="hidden" id="infoOrgCode" value="${infoOrgCode!''}" />
	<input type="hidden" id="elementsCollectionStr" value="<#if elementsCollectionStr??>${elementsCollectionStr}</#if>" />
	<input type="hidden" id="teamType" value="${teamType!''}" />
	<input type="hidden" id="markType" value="${markType!''}" />
	<input type="hidden" id="pageSize" value="20" />
	
    <div class="" style="display:block;">
        <div class="ListSearch">
        	<div class="condition">
            	<ul>
                	<li class="LC1">名称：</li>
                	<li class="LC2"><input id="name" name="name" type="text" class="inp1" /></li>
                </ul>
            	<ul>
                	<li class="LC1">&nbsp;</li>
                	<li class="LC2"><input name="" type="button" value="查询" class="NorBtn" onclick="loadMessage(1,$('#pageSize').val(),'searchBtn');"/></li>
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
	$('#name').keydown(function(e) {
		if (e.keyCode == 13) {
			loadMessage(1, $("#pageSize").val());
		}
	});
	function ShowOrCloseSearchBtn() {
		var temp = $(".ListSearch").is(":hidden");// 是否隐藏
		if (temp == false) {
			$(".ListSearch").hide();
		} else {
			$(".ListSearch").show();
		}
	}
	function CloseSearchBtn() {
		$(".ListSearch").hide();
	}
	$(document).ready(function() {
		try {
			var winHeight = window.parent.document.getElementById('map' + window.parent.currentN).offsetHeight - 62;
			$("#content").height(winHeight - 56);
		} catch (e) {
		}
		loadMessage(1, $("#pageSize").val());
	});
	var results = "";// 获取定位对象集合
	var layerName="";
	function loadMessage(pageNo,pageSize,searchType){
		layerName = window.parent.analysisOfElementsCollection($('#elementsCollectionStr').val(),"menuLayerName");
		window.parent.currentLayerName = layerName;
		if('searchBtn'==searchType) {
			window.parent.clearSpecialLayer(layerName);
			window.parent.currentListNumStr = "";
		}
		
		results = "";
		
		var infoOrgCode = $('#infoOrgCode').val();
		var name = $('#name').val();
		var addr = $('#addr').val();
		var teamType = $('#teamType').val();
		var pageSize = $("#pageSize").val();
		
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
		var postData = 'page=' + pageNo + '&rows=' + pageSize + '&infoOrgCode=' + infoOrgCode + '&name=' + name+ '&addr=' + addr + '&teamType=' + teamType;
		$.ajax({
			type : "POST",
			url : '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataoforganization/fireTeamListData.json?t=' + Math.random(),
			data : postData,
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
				if (data.total % pageSize > 0) {
					totalPage += 1;
				}
					
				$('#pageCount').text(totalPage);
				var list = data.rows;
				var tableBody = "";
				tableBody += '<div class="liebiao">';
				if (list && list.length > 0) {
					for ( var i = 0; i < list.length; i++) {
						var val = list[i];
						
						var name = val.name;
						
						if (name != null && name != "" && name.length > 11) {
							name = name.substring(0, 11);
						}
						
						var addr = val.addr;
						if (addr != null && addr != "" && addr.length > 17) {
							addr = addr.substring(0, 17);	
						}
						
						tableBody += '<dl onclick="selected(\'' + val.fireTeamId + '\',\'' + (val.name == null ? '' : val.name) + '\')">';
						tableBody += '<dt>';
						tableBody += '<span class="fr">' + (val.ph==null?'':('<img src="${uiDomain!''}/images/map/gisv0/special_config/images/people_06.png">'+val.ph))+'</span>';
						tableBody += '<b class="FontDarkBlue" title=\''+(val.name == null ? '暂无' : val.name)+'\'>' + (name == null ? '暂无' : name) + '</b>';
						tableBody += '</dt>';
						tableBody += '<dd title=\'' + (val.addr == null ? '暂无' : val.addr) + '\'>地址：' + (addr == null ? '暂无' : addr) + '</dd>';
						tableBody += '<dd>队伍人数：'+(val.personalNum==null?'0':val.personalNum)+ '人&nbsp;&nbsp;车辆：' + (val.carNum==null?'0':val.carNum) + '辆</dd>';
						tableBody += '</dl>';
						results = results + "," + val.fireTeamId;
					}
					results = results.substring(1, results.length);
				} else {
					tableBody += '<div style="text-align: center;"><img style="width: 174px;" src="${uiDomain!''}/images/map/gisv0/special_config/images/nodata.png" title="暂无数据"/></div>';
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
	
	// 概要
	function selected(id, name) {
		setTimeout(function() {
			if($('#elementsCollectionStr').val() != "") {
				window.parent.getDetailOnMapOfListClick($('#elementsCollectionStr').val(),400, 250,id)
			}
		},1000);
	}
	
	// 地图定位
	function gisPosition(res) {
		var markType = $("#markType").val();
		
		if (res == "") {
			return;
		}
		if("1" != window.parent.IS_ACCUMULATION_LAYER) {
			window.parent.clearSpecialLayer(layerName);
		}else {
			if(window.parent.currentListNumStr.indexOf(currentPageNum+"")>=0) {
				return;
			}else {
				window.parent.currentListNumStr = window.parent.currentListNumStr+","+currentPageNum;
			}
		}
		
		if($('#elementsCollectionStr').val() != "") {
			var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataoforganization/fireTeamLocateData.jhtml?ids=" + res + "&markType=" + markType;
			window.parent.currentLayerLocateFunctionStr="getArcgisDataOfZhuanTi('"+url+"','"+$('#elementsCollectionStr').val()+"')";
			window.parent.getArcgisDataOfZhuanTi(url,$('#elementsCollectionStr').val(),400, 250);
		}
	}
	
</script>
</body>
</html>
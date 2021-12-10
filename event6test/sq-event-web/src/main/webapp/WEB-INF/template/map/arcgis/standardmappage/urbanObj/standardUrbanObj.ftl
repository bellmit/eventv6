<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>重点场所列表</title>
<#include "/component/commonFiles-1.1.ftl" />
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<link rel="stylesheet" href="${rc.getContextPath()}/js/ztree/zTreeStyle/zTreeStyle.css" />
<script src="${rc.getContextPath()}/js/ztree/jquery.ztree.core-3.5.min.js"></script>
<script src="${rc.getContextPath()}/js/ztree/jquery.ztree.excheck-3.5.min.js"></script>
<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<style>
	body, html{overflow:auto;}
	.ztree li{overflow:hidden;}
	.ztree li span{font-size:14px;}
</style>
</head>
<body>
	<input type="hidden" id="gridId" value="${gridId?c}" />
    <input type="hidden" id="infoOrgCode" value="<#if infoOrgCode??>${infoOrgCode!''}</#if>" />
	<input type="hidden" id="elementsCollectionStr" value="<#if elementsCollectionStr??>${elementsCollectionStr}</#if>" />
	<input type="hidden" id="pageSize" value="20" />
	<div id="divTreeMenu">
		<ul id="ulFirstall" class="ztree"></ul>
	</div>
	
	<div class="ListSearch">
    	<div class="condition">
        	<ul style="margin-bottom:0px;">
            	<li class="LC2" style="width:100%;">
            		<input id="name" name="name" type="text" class="inp1" style="width:193px;" onkeydown="_onkeydown();"/>
            		<input name="" type="button" value="查询" class="NorBtn" style="width:41px;height:25px;line-height:25px;" onclick="loadMessage(1, 'searchBtn');"/>
            	</li>
            </ul>
            <div class="clear"></div>
        </div>
        <div class="showRecords">
        	<ul>
        		<li>共查询到<span id="records">0</span>条记录</li>
        	</ul>
        </div>
        <div class="ListShow content" style="border-left:0px;" id="content">
        	
        </div>
        <div class="NorPage">
        	<#include "/map/arcgis/standardmappage/common/standard_page.ftl" />
        </div>
    </div>
</body>
<script type="text/javascript">
$(function() {
	$("#divTreeMenu").addClass("TreeMenu").css("height", $(window).height());
	$(".ListSearch").height($(window).height());
	$("#content").height($(window).height() - 105);
	initTree();
});

function ShowOrCloseSearchBtn() {
	var temp = $(".ListSearch").is(":hidden");//是否隐藏 
	if (temp == false) {
		$(".ListSearch").hide();
	} else {
		$(".ListSearch").show();
	}
}

function CloseSearchBtn() {
	$(".ListSearch").hide();
}

function initTree() {
	var setting = {
		check: {
			enable: true,
			chkStyle: "checkbox",
			chkboxType: { "Y": "ps", "N": "ps" }
		},
		data: {
			key : {
				name : "name"
			},
			simpleData: {
				enable: true,
				idKey: "dictCode",
				pIdKey: "dictPcode",
				rootPId: "",
				valKey: "value"
			}
		},
		view: {
			fontCss: function(treeId, treeNode) {
				return {color:"#FFFFFF", "font-size":"14px"};
			}
		},
		callback: {
			onCheck: zTreeOnCheck,
			onClick: treeNodeClick
		}
   	};
	layer.load(0);
	$.ajax({
		type : 'POST',
		dataType : "jsonp",
		url : "${ANOLE_COMPONENT_URL}/system/uam/baseDictionary/baseDictionaryController/getDataDictTreeForJsonp.json?isShowIcon=1&dictPcode=D005011&jsoncallback=?&t="+Math.random(),
		error : function() {
		},
		success: function(data) {
			layer.closeAll('loading');
			if (data != null && data.length > 1) {
				$.fn.zTree.init($("#ulFirstall"), setting, data);
				var zTree = $.fn.zTree.getZTreeObj("ulFirstall");
				var nodes = zTree.transformToArray(zTree.getNodes());
				for (var i = 0; i < nodes.length; i++) {
					if (nodes[i].children && nodes[i].children.length > 0) {
						nodes[i].nocheck = true;
						zTree.updateNode(nodes[i]);
					}
				}
				clearMyLayerB();
                initNodesCount();
			} else {
				$.messager.alert('友情提示','地图首页内容未配置，请联系管理员!','info');
			}
		}
	});
}

function initNodesCount() {
	var infoOrgCode = "<#if infoOrgCode??>${infoOrgCode!''}</#if>";
    $.ajax({
        type : 'POST',
        dataType : "json",
        url : "${rc.getContextPath()}/zhsq/map/gisstat/gisStat/getUrbanCountByCodes.json?infoOrgCode="+infoOrgCode+"&t="+Math.random(),
        error : function() {
        },
        success: function(datas) {
            if (datas != null && datas.length > 1) {
                var zTree = $.fn.zTree.getZTreeObj("ulFirstall");
                var nodes = zTree.transformToArray(zTree.getNodes());
                for (var i = 0; i < nodes.length; i++) {
					var isUpdate = false;
					for(var j=0;j < datas.length; j++){
                        if (nodes[i].value && nodes[i].value == datas[j].CODE) {
                            isUpdate = true;
                            nodes[i].name = nodes[i].name + "("+ datas[j].TOTAL_ +")";
                        }
					}
					if(!isUpdate && (!nodes[i].children || nodes[i].children.length <= 0)){
                        nodes[i].name = nodes[i].name + "(0)";
					}
                    zTree.updateNode(nodes[i]);
                }

            } else {
                $.messager.alert('友情提示','统计部件数据出错!','info');
            }
        }
    });
}

function treeNodeClick(event, treeId, treeNode) {
	var zTree = $.fn.zTree.getZTreeObj(treeId);
	zTree.cancelSelectedNode(treeNode);
}

function zTreeOnCheck(event, treeId, treeNode) {
	if (treeNode.children != null && treeNode.children.length > 0) {
		for (var n = 0; n < treeNode.children.length; n++) {
			var node = treeNode.children[n];
			zTreeOnCheck(event, treeId, node);
		}
	} else if (treeNode.value != null) {
		/*if ('SMART_CITY' == $("#homePageType", parent.document).val()) {
			var treeIcon = treeNode.icon.replace('.png', '_Marker.png');
			parent.showMarkersForUrbanObj($('#elementsCollectionStr').val(), treeNode.name, treeNode.value, treeIcon, treeNode.checked);
		} else {
			parent.renderUrbanObj($('#elementsCollectionStr').val(), treeNode.checked);
		}*/
        var treeObj = $.fn.zTree.getZTreeObj("ulFirstall");
        var nodes = treeObj.getCheckedNodes(true);
		parent.renderUrbanObj($('#elementsCollectionStr').val(), treeNode.checked,'','',nodes.length);
		clearMyLayerC();
	}
}

function clearMyLayerB() {
	var zTree = $.fn.zTree.getZTreeObj("ulFirstall");
	zTree.checkAllNodes(false);
	clearMyLayerC();
}

function getZtreeObj() {
	var zTree = $.fn.zTree.getZTreeObj("ulFirstall");
	return zTree;
}

function getSelectedUrbanCode() {
	var codesAry = new Array();
	var treeObj = getZtreeObj();
	var nodes = treeObj.getCheckedNodes(true);
	for (var i = 0; i < nodes.length; i++) {
		codesAry.push(nodes[i].value);
	}
	return codesAry.join(",");
}

function getCheckedNodes() {
	var treeObj = getZtreeObj();
	return treeObj.getCheckedNodes(true);
}

function clearMyLayerC() {
	var layerName = parent.analysisOfElementsCollection($('#elementsCollectionStr').val(), "menuLayerName");
	window.parent.clearSpecialLayer(layerName);
	window.parent.currentListNumStr = "";
	$("#content").html("");
	setPageParams(1, $("#pageSize").val(), 0);
}

function _onkeydown() {
	var keyCode = event.keyCode;
	if(keyCode == 13) {
		loadMessage(1, 'searchBtn');
	}
}

function loadMessage(pageNo, searchType) {
	layerName = window.parent.analysisOfElementsCollection($('#elementsCollectionStr').val(), "menuLayerName");
	window.parent.currentLayerName = layerName;
	if ('searchBtn' == searchType) {
		clearMyLayerB();
		window.parent.clearSpecialLayer(layerName);
		window.parent.currentListNumStr = "";
	}
	var gridId = $('#gridId').val();
	var name = $('#name').val();
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
	var postData = 'page=' + pageNo + '&rows=' + pageSize + '&gridId=' + gridId + '&name=' + name;
	$.ajax({
		type : "POST",
		url : '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofthing/urbanObjListData.json?t=' + Math.random(),
		data : postData,
		dataType : "json",
		success : function(data) {
			$.unblockUI();
			setPageParams(pageNo, pageSize, data.total);
			var list = data.rows;
			var tableBody = "";
			tableBody += '<div class="liebiao">';
			if (list && list.length > 0) {
				for ( var i = 0; i < list.length; i++) {
					var val = list[i];
					var name = val.name;
					if (name != null && name != "" && name.length > 12) {
						name = name.substring(0, 12) + "...";
					} else {
						name = name;
					}
					var addr = val.position;
					if (addr != null && addr != "" && addr.length > 15) {
						addr = addr.substring(0, 15) + "...";
					} else {
						addr = (addr == null ? '&nbsp;' : addr);
					}
					var info = getUrbanDictInfo(val.classCode);
					tableBody += '<dl onclick="selected(\'' + val.id + '\',\'' + (val.name == null ? '' : val.name) + '\')">';
					tableBody += '<dt>';
					tableBody += '<span class="fr">' + '</span>';
					tableBody += '<b class="FontDarkBlue">' + (val.name == null ? '' : val.name) + '&nbsp;</b>';
					tableBody += '</dt>';
					tableBody += '<dd title=\'' + (val.position == null ? '' : val.position) + '\'>['+info.dictName+']' + addr + '</dd>';
					tableBody += '</dl>';
				}
				gisPosition(list);
			} else {
				tableBody += '<div style="text-align: center;"><img style="width: 174px;" src="${uiDomain!''}/images/map/gisv0/special_config/images/nodata.png" title="暂无数据"/></div>';
			}
			tableBody += '</div>';
			$("#content").html(tableBody);
		},
		error : function(data) {
			$.unblockUI();
			var tableBody = '<div class="liebiao"><ul>数据读取错误！！！</ul></div>';
			$("#content").html(tableBody);
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
	loadMessage(pagenum);
}
var currentPageNum = 1;
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

function setPageParams(pageNo, pageSize, totalNum) {
	// 设置页面页数
	$('#pagination-num').text(pageNo);
	$('#records').text(totalNum);
	var totalPage = Math.floor(totalNum / pageSize);
	if (totalNum % pageSize > 0) totalPage += 1;
	$('#pageCount').text(totalPage);
}
//--定位
function gisPosition(rowsObj) {
	if ($('#elementsCollectionStr').val() != "") {
		window.parent.markerArcgisDataOfZhuanTi($('#elementsCollectionStr').val(), rowsObj, "020130", {
			fieldId : "id",
			fieldName : "name",
			preMakeFunc : function(rowObj) {
				var obj = getUrbanDictInfo(rowObj.classCode);
				var opt = {
					url : obj.iconUrl.replace('.png', '_Marker.png'),
					width : 22,
					height : 29
				};
				return opt;
			}
		});
	}
}
function selected(id, name) {
	if ($('#elementsCollectionStr').val() != "") {
		window.parent.clickMarkerById($('#elementsCollectionStr').val(), id);
	}
}
function getUrbanDictInfo(type) {
	var opt = {
		iconUrl : '',
		dictName : ''
	};
	<#if urbanTypes??>
	<#list urbanTypes as urbanType>
	<#if urbanType.dictGeneralCode?? && urbanType.dictGeneralCode?length gt 2>
	if (type == "${urbanType.dictGeneralCode!''}") {
		opt.iconUrl = "${uiDomain!''}${urbanType.dictRemark!''}";
		opt.dictName = "${urbanType.dictName!''}";
		return opt;
	}
	</#if>
	</#list>
	</#if>
}
</script>
</html>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>党组织（新）</title>
<#include "/component/commonFiles-1.1.ftl" />
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<link rel="stylesheet" href="${rc.getContextPath()}/js/ztree/zTreeStyle/zTreeStyle.css" />
<script src="${rc.getContextPath()}/js/ztree/jquery.ztree.core-3.5.min.js"></script>
<script src="${rc.getContextPath()}/js/ztree/jquery.ztree.excheck-3.5.min.js"></script>
<script src="${rc.getContextPath()}/js/layer/layer.js"></script>
<style>
	body, html{overflow:auto;}
	.ztree li{overflow:hidden;}
	.ztree li span{font-size:14px;}
	.ztree li a.curSelectedNode {
		background-color:transparent;
	}
</style>
</head>
<body>
	<input type="hidden" id="gridId" value="${gridId!''}" />
	<input type="hidden" id="orgCode" value="${orgCode!''}" />
	<input type="hidden" id="elementsCollectionStr" value="<#if elementsCollectionStr??>${elementsCollectionStr}</#if>" />
	<input type="hidden" id="statPartyOrgEcs" value="<#if statPartyOrgEcs??>${statPartyOrgEcs}</#if>" />
	<input type="hidden" id="pageSize" value="20" />
		<div class="ListSearch">
        	<div class="condition">
            	<ul>
                	<li class="LC1">名称：</li>
                	<li class="LC2"><input id="partyGroupName" name="partyGroupName" type="text" class="inp1" /></li>
                </ul>
            	<ul>
                	<li class="LC1">&nbsp;</li>
                	<li class="LC2"><input name="" type="button" value="查询" class="NorBtn" onclick="initTree();"/></li>
                </ul>
                <div class="clear"></div>
            </div>
        	<div class="CloseBtn" onclick="CloseSearchBtn()"></div>
        </div>
	<div id="divTreeMenu">
		<ul id="ulFirstall" class="ztree"></ul>
	</div>
</body>
<script type="text/javascript">
$(function() {
	$("#divTreeMenu").addClass("TreeMenu").css("height", $(window).height());
	initTree();
	// gisPosition();
	parent.renderPartyMember($("#statPartyOrgEcs").val());
});

function initTree() {
	layer.load(0);
	destoryById("ulFirstall");
	var setting = {
		async: {
			enable: true,
			url: "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofthing/partyOrgInfoTreeJson.jhtml?t=" + Math.random(),
			autoParam:["id=partyGroupId"],
			otherParam: {
				orgCode: $("#orgCode").val(),
				partyGroupName: $("#partyGroupName").val()
			}
		},
		view: {
			fontCss: function(treeId, treeNode) {
				return {color:"#FFFFFF", "font-size":"14px"};
			}
		},
		callback: {
			onClick: treeNodeClick,
			onAsyncSuccess: function(event, treeId, treeNode, msg) {
				layer.closeAll('loading');
			}
		}
   	};
   	$.fn.zTree.init($("#ulFirstall"), setting);
}

function destoryById(treeId) {
	try {
		var zTree = $.fn.zTree.getZTreeObj(treeId);
		zTree.destroy();
		CloseSearchBtn();
	} catch(e) {}
}

function treeNodeClick(event, treeId, treeNode) {
	var zTree = $.fn.zTree.getZTreeObj(treeId);
	selected(treeNode.id);
}

function selected(id) {
	var gisDataUrl = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofthing/getNewPartyGroupXYById.jhtml?ids=" + id;
	parent.markArcgisData(gisDataUrl, $('#elementsCollectionStr').val(), 360, 190);
	setTimeout(function() {
		if ($('#elementsCollectionStr').val() != "") {
			window.parent.getDetailOnMapOfListClick($('#elementsCollectionStr').val(),360,190,id);
		}
	}, 1000);
}

//地图定位
function gisPosition() {
	if ("1" != window.parent.IS_ACCUMULATION_LAYER) {
		var layerName = parent.analysisOfElementsCollection($('#elementsCollectionStr').val(), "menuLayerName");
		window.parent.clearSpecialLayer(layerName);
	}
	if ($('#elementsCollectionStr').val() != "") {
		var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofthing/getNewPartyGroupXYList.jhtml?infoOrgCode="+$("#orgCode").val();
		window.parent.currentLayerLocateFunctionStr="getArcgisDataOfZhuanTi('"+url+"','"+$('#elementsCollectionStr').val()+"')";
		window.parent.getArcgisDataOfZhuanTi(url, $('#elementsCollectionStr').val(), 360, 190);
	}
}

function ShowOrCloseSearchBtn() {
	var temp= $(".ListSearch").is(":hidden");//是否隐藏 
	if (temp == false) {
		$(".ListSearch").hide();
	} else {
		$(".ListSearch").show();
	}
}
function CloseSearchBtn() {
	$(".ListSearch").hide();
}

</script>
</html>
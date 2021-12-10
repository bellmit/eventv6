<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>周边资源列表</title>


<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>

<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<style type="text/css">
.showRecords{height:32px;}
.showRecords ul li{text-align:left; float:left; padding:5px 10px; line-height:18px;}
</style>
</head>
<body style="border:none">
	<input type="hidden" id="pageSize" value="20" />
	<input type="hidden" id="orgCode" value="${orgCode!''}" />
	<input type="hidden" id="operateStatus" value="${operateStatus!''}" />
	<input type="hidden" id="elementsCollectionStr" value="<#if elementsCollectionStr??>${elementsCollectionStr}</#if>" />
    <div class="" style="display:block;">
        <div class="ListSearch">
        	<div class="condition">
				<ul>
                	<li class="LC1">名称：</li>
                	<li class="LC2"><input id="name" name="name" type="text" class="inp1" /></li>
                </ul>
            	<ul>
                	<li class="LC1">地址：</li>
                	<li class="LC2"><input id="address" name="address" type="text" class="inp1" /></li>
                </ul>
            	<ul>
                	<li class="LC1">&nbsp;</li>
                	<li class="LC2"><input name="" type="button" value="查询" class="NorBtn" onclick="loadMessage(1,$('#pageSize').val());"/></li>
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
	
	function ShowOrCloseSearchBtn() {
		var temp = $(".ListSearch").is(":hidden");// 是否隐藏
		if (temp == false) {
			$(".ListSearch").hide();
		} else {
			$(".ListSearch").show();
		}
	}
	
	$('#name').keydown(function(e) {
		if (e.keyCode == 13) {
			loadMessage(1, $("#pageSize").val());
		}
	});
	
	function CloseSearchBtn() {
		$(".ListSearch").hide();
	}
	
	$(document).ready(function() {
		var winHeight = window.parent.document.getElementById('map' + window.parent.currentN).offsetHeight - 62;
		$("#content").height(winHeight - 56);
		loadMessage(1, $("#pageSize").val());
	});
	
	var results = "";// 获取定位对象集合
	function loadMessage(pageNo, pageSize) {
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
		$.ajax({
			type : "POST",
			url : '${rc.getContextPath()}/zhsq/map/kuangxuan/kuangxuanStat/queryKuangXuanList.json?t=' + Math.random(),
			data : {
				pageNo : pageNo,
				pageSize : $("#pageSize").val(),
				name : $('#name').val(),
				address : $('#address').val(),
				mapType : "${mapType}",
				geoString : "${geoString}",
				infoOrgCode : "${infoOrgCode}",
				kuangxuanType : "${kuangxuanType}"
			},
			dataType : "json",
			success : function(data) {
				$.unblockUI();
				//设置页面页数
				$('#pagination-num').text(pageNo);
				$('#records').text(data.total);
				var totalPage = Math.floor(data.total/pageSize);
				if(data.total%pageSize>0) totalPage+=1;
				$('#pageCount').text(totalPage);
				var list=data.rows;
				var tableBody="";
				tableBody+='<div class="liebiao">';
				if(list && list.length>0) {
					for(var i=0;i<list.length;i++){
					  var val=list[i];
					  
					  tableBody+='<dl onclick="selected(\''+val.ID+'\',\''+(val.NAME==null?'':val.NAME)+'\')">';
					  tableBody+='<dt>';
					  if(val.OPERATE_STATUS!='1'){//判断是否异常
					  	tableBody+='<img src="${rc.getContextPath()}/ui/images/yichang.png"/>';
					  }
					  tableBody+='<span class="fr">';
					  if(val.CATALOG=='2'){//判断是室内还是室外
					  	tableBody+='室外';
					  }else if(val.CATALOG=='1'){
					  	tableBody+='室内';
					  }
					  tableBody+='</span>';
					  tableBody+='<b class="FontDarkBlue">'+(val.NAME==null?'':val.NAME)+'</b>';
					  tableBody+='</dt>';
					  //tableBody+='<dt><span style="width: 120px;">距离：'+val.DISTANCE+'米</span></dt>';
					  tableBody+='<dd>地址：'+(val.ADDRESS==null?'':val.ADDRESS)+'</dd>';
					  tableBody+='</dl>';
					  
					  results=results+","+val.ID;
					  
					}
					results=results.substring(1, results.length);
				} else {
					tableBody+='<div style="text-align: center;"><img style="width: 174px;" src="${uiDomain!''}/images/map/gisv0/special_config/images/nodata.png" title="暂无数据"/></div>';
					//tableBody+='<ul>未查到相关数据！！</ul>';
				}
		        tableBody+='</div>';
				$("#content").html(tableBody);
				$(".AdvanceSearch").css("display","none");
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
		loadMessage(pagenum, pageSize);
	}
	
	$("#moreSearch").toggle(function() {
		$(".AdvanceSearch").css("display", "block");
	}, function() {
		$(".AdvanceSearch").css("display", "none");
	});
	
	/*function selected(id) {
		setTimeout(function() {
			window.parent.localtionPointForCommon({
				id : id,
				title : "周边出租屋",
				imgUrl : "/images/map/gisv0/map_config/selected/region_let.png",
				url : '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/rentRoomDetail.jhtml?rentId='+id,
				layerName : "rentRoomLayer",
				func1 : function (id, title) {
					var sq_zzgrid_url =  document.getElementById("SQ_ZZGRID_URL").value;
					var url =  sq_zzgrid_url +'/zzgl/map/data/region/rentRoomDetail.jhtml?rentId='+id;
					showMaxJqueryWindow(title,url,850,410);
				}
			});
		}, 1000);
	}
	// 地图定位
	function gisPosition(res) {
		if (res == "") {
			return;
		}
		window.parent.clearMyLayer();
		var corpurl="${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/getArcgisLocateDataListOfRentRoom.jhtml?ids="+res;
		window.parent.currentLayerLocateFunctionStr = "drawArcgisDataForCommon({flgUrl:'/zhsq/map/arcgis/arcgisdataofregion/rentRoomDetail.jhtml?rentId=',dlgUrl:'/zzgl/map/data/region/rentRoomDetail.jhtml?rentId=',dlgW:850,dlgH:410,imgUrl:'/images/map/gisv0/map_config/unselected/region_let.png',title:'周边出租屋',layerName:'rentRoomLayer',url:'"+corpurl+"'})";
		window.parent.drawArcgisDataForCommon({
			title : "周边出租屋",
			layerName : "rentRoomLayer",
			imgUrl : "/images/map/gisv0/map_config/unselected/region_let.png",
			url : corpurl,
			flgUrl : "/zhsq/map/arcgis/arcgisdataofregion/rentRoomDetail.jhtml?rentId=",
			dlgUrl : "/zzgl/map/data/region/rentRoomDetail.jhtml?rentId=",
			dlgW : 850,
			dlgH : 410
		});
	}*/

	function selected(id) {
		//gisPosition(id);
	
		setTimeout(function() {
			window.parent.getDetailOnMapOfListClick($('#elementsCollectionStr').val(),300,253,id);
		}, 1000);
	
	}
	// 地图定位
	function gisPosition1(res) {
		if("1" != window.parent.IS_ACCUMULATION_LAYER) {
			window.parent.clearSpecialLayer(layerName);
		}else {
			if(window.parent.currentListNumStr.indexOf(currentPageNum+"")>=0) {
				return;
			}else {
				window.parent.currentListNumStr = window.parent.currentListNumStr+","+currentPageNum;
			}
		}
		//window.parent.clearMyLayer();
		if (res == "") {
			return;
		}
		var gisDataUrl="${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofgoods/getArcgisLocateDataListOfNewResource.jhtml?resTypeCode=${markType!''}&ids="+res;
	  	window.parent.currentLayerLocateFunctionStr="getArcgisDataOfNewResource('${markType!''}','"+gisDataUrl+"')";
	  	window.parent.getArcgisDataOfNewResource('${markType!''}',gisDataUrl);
	}
	// 地图定位
	function gisPosition(res) {
		if (res == "") {
			return;
		}
		window.parent.clearMyLayer();
		
		if ($('#elementsCollectionStr').val() != "") {
			var gisDataUrl="${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofgoods/getArcgisLocateDataListOfNewResource.jhtml?resTypeCode=${markType!''}&ids="+res;
			window.parent.currentLayerLocateFunctionStr="getArcgisDataOfZhuanTi('"+gisDataUrl+"','"+$('#elementsCollectionStr').val()+"')";
		  	window.parent.getArcgisDataOfZhuanTi(gisDataUrl,$('#elementsCollectionStr').val(),300,253);
		}
	}
	
	function showErrorResource(){
		$('#operateStatus').val('2');
		loadMessage(1,$('#pageSize').val());
	}
	</script>
</body>
</html>
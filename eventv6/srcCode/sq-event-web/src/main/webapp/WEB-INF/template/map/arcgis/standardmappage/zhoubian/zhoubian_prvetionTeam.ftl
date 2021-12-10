<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>周边资源列表</title>


<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>

<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<script type="text/javascript" src="${rc.getContextPath()}/js/plugIn/plug_in.js"></script>

<style type="text/css">
.showRecords{height:32px;}
.showRecords ul li{text-align:left; float:left; padding:5px 10px; line-height:18px;}
</style>
</head>
<body style="border:none">
	<input type="hidden" id="pageSize" value="20" />
	<input type="hidden" id="orgCode" value="${orgCode!''}" />
    <input type="hidden" id="x" value="${x!''}" />
    <input type="hidden" id="y" value="${y!''}" />
    <input type="hidden" id="distance" value="${distance!''}" />
    <input type="hidden" id="mapType" value="${mapType!''}" />
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
		$.ajax({
			type : "POST",
			url : '${rc.getContextPath()}/zhsq/map/zhoubian/zhouBianStat/queryZhouBianList.json?t=' + Math.random(),
			data : {
				pageNo : pageNo,
				pageSize : $("#pageSize").val(),
				name : $('#name').val(),
				address : $('#address').val(),
				mapType : "${mapType}",
				distance : "${distance}",
				x : "${x}",
				y : "${y}",
				infoOrgCode : "${infoOrgCode}",
				zhoubianType : "${zhoubianType}"
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
					  var val=list[i],
					  	  teamName = val.NAME || '';
					  
					  tableBody+='<dl onclick="selected(\''+val.TEAM_ID+'\',\''+ teamName +'\')">';
					  tableBody+='<dt>';
					  if(val.MANAGER_TEL) {
					  	tableBody += '<span class="fr"><img id="_sendMsgImg" src="${uiDomain!}/images/text_msg.png" title="发送信息" _gridAdminId="'+ val.TEAM_ID +'" _mobileTelephone="'+ val.MANAGER_TEL +'" _partyName="'+ val.MANAGER +'" />&nbsp;<img id="_yuyinCallImg" src="${uiDomain!''}/images/cloundcall.png" title="语音呼叫" _mobileTelephone="'+ val.MANAGER_TEL +'" _partyName="'+ val.MANAGER +'" />&nbsp;'+val.MANAGER_TEL+'</span>';
					  }
					  
					  tableBody+='<b class="FontDarkBlue" ';
					  if(teamName && teamName.length > 8) {
					  	tableBody += ' title="'+ teamName +'" ';
					  	teamName = teamName.substring(0, 8) + "...";
					  }
					  tableBody += ' >' + teamName + '</b>';
					  
					  tableBody+='</dt>';
					  tableBody+='<dt><span style="width: 120px;">距离：'+val.DISTANCE+'米</span></dt>';
					  tableBody+='<dd>地址：'+(val.CONTENT_==null?'':val.CONTENT_)+'</dd>';
					  tableBody+='</dl>';
					  
					  results=results+","+val.TEAM_ID;
					  
					}
					results=results.substring(1, results.length);
				} else {
					tableBody+='<div style="text-align: center;"><img style="width: 174px;" src="${uiDomain!''}/images/map/gisv0/special_config/images/nodata.png" title="暂无数据"/></div>';
					//tableBody+='<ul>未查到相关数据！！</ul>';
				}
		        tableBody+='</div>';
				$("#content").html(tableBody);
				
				$("#content img[id='_sendMsgImg']").click(function(event) {
					var url = '${rc.getContextPath()}/zhsq/map/zhoubian/zhouBianStat/toSendMsg.jhtml?gridAdminId='+ $(this).attr('_gridAdminId')+"&partyName="+$(this).attr("_partyName")+"&mobileTelephone="+$(this).attr("_mobileTelephone")+'&bizType=02';
					url = url + '&x='+$('#x').val()+ '&mapType='+$('#mapType').val()+ '&y='+$('#y').val()+ '&distance='+$('#distance').val();
					var params = {
						title: "发送信息",
						targetUrl: url,
						width: 850,
						height: 400,
						resizable: true
					}
					
					parent.showMaxJqueryWindowByParams(params);
					
					event.stopPropagation();//阻止将点击事件向上传递
				});
				
				$("#content img[id='_yuyinCallImg']").click(function(event) {
					showVoiceCall('${rc.getContextPath()}', window.parent.showMaxJqueryWindow, $(this).attr("_mobileTelephone"), $(this).attr("_partyName"));
					
					event.stopPropagation();//阻止将点击事件向上传递
				});
				
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

	function selected(id, name){
		setTimeout(function() {
			if($('#elementsCollectionStr').val() != "") {
				window.parent.getDetailOnMapOfListClick($('#elementsCollectionStr').val(),300,150,id)
			}else {
				window.parent.localtionControlsafetyRanksPoint('${markType!''}',id,'${bizType!''}');
			}
			
		},1000);
	
	}
	//地图定位
	function gisPosition1(res){
		if("1" != window.parent.IS_ACCUMULATION_LAYER) {
			window.parent.clearSpecialLayer(layerName);
		}else {
			if(window.parent.currentListNumStr.indexOf(currentPageNum+"")>=0) {
				//return;
			}else {
				window.parent.currentListNumStr = window.parent.currentListNumStr+","+currentPageNum;
			}
		}
		if (res==""){
			return ;
		}
	  	if($('#elementsCollectionStr').val() != "") {
			var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofgoods/getArcgisLocateDataListOfControlsafetyRanks.jhtml?ids="+res+"&showType=2";
			window.parent.currentLayerLocateFunctionStr="getArcgisDataOfZhuanTi('"+url+"','"+$('#elementsCollectionStr').val()+"')";
			window.parent.getArcgisDataOfZhuanTi(url,$('#elementsCollectionStr').val(),300,150);
		}else {
			var gisDataUrl="${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofgoods/getArcgisLocateDataListOfControlsafetyRanks.jhtml?resTypeCode=${markType!''}&ids="+res+"&bizType=${bizType!''}";
		  	window.parent.currentLayerLocateFunctionStr="getArcgisDataOfControlsafetyRanks('${markType!''}','"+gisDataUrl+"')";
		  	window.parent.getArcgisDataOfControlsafetyRanks('${markType!''}',gisDataUrl,'${bizType!''}');
		}
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
		//window.parent.clearMyLayer();
		
		if ($('#elementsCollectionStr').val() != "") {
			var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofgoods/getArcgisLocateDataListOfControlsafetyRanks.jhtml?ids="+res+"&showType=2";
			window.parent.currentLayerLocateFunctionStr="getArcgisDataOfZhuanTi('"+url+"','"+$('#elementsCollectionStr').val()+"')";
			window.parent.getArcgisDataOfZhuanTi(url,$('#elementsCollectionStr').val(),300,150);
		}
	}
	</script>
</body>
</html>
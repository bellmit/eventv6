<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>周边资源列表（消防栓、自来水公司、天然水源）</title>


<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>

<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<style type="text/css">
.showRecords{height:46px;}
.showRecords ul li{text-align:left; float:left; padding:5px 10px; line-height:18px;}
.icon_select{ background:#ccc;box-shadow:inset 1px 1px 0px 0px #999; border-radius:3px; height:19px; line-height:19px; display:inline-block; text-align:center;}
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
        		<li>
        			<p><span>&nbsp;</span></p>
        			<p>图例说明：<label id="errorIcon" style="cursor:hand; text-decoration:underline;color:red" onclick="showErrorResource();"><img src="${rc.getContextPath()}/ui/images/yichang.png"/>运行异常</label></p>
        		</li>
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
				kuangxuanType : "${kuangxuanType}",
				operateStatus : $('#operateStatus').val()
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
					  
					  tableBody+='<dl onclick="selected(\''+val.FIRE_RES_ID+'\',\''+(val.NAME_==null?'':val.NAME_)+'\')">';
					  tableBody+='<dt>';
					  if(val.OPERATE_STATUS!='1'){//判断是否异常
					  	tableBody+='<img src="${rc.getContextPath()}/ui/images/yichang.png"/>';
					  }
					  tableBody+='<span class="fr">';
					  if(val.catalog=='2'){//判断是室内还是室外
					  	tableBody+='室外';
					  }else if(val.catalog=='1'){
					  	tableBody+='室内';
					  }
					  
					  tableBody+='</span>';
					  
					  if (val.CATALOG_ == 5) {
					  	tableBody+='<span class="fr">'+ (val.CATALOG_==null?'0':val.CATALOG_) + '吨</span>';
					  }
					  
					  var NAME_ = val.NAME_;
					  if (NAME_ != null && NAME_ != "" && NAME_.length > 15) {
						NAME_ = NAME_.substring(0, 15);
					  }
					  
					  var ADDR_ = val.ADDR_;
					  if (ADDR_ != null && ADDR_ != "" && ADDR_.length > 17) {
						ADDR_ = ADDR_.substring(0, 17);	
					  }
					  tableBody+='</span>';
					  tableBody+='<b title=\''+(val.NAME_)+'\' class="FontDarkBlue">'+(NAME_==null?'':NAME_)+'</b>';
					  tableBody+='<dd title=\''+(val.ADDR_==null?'暂无':val.ADDR_)+'\'>地址：'+(ADDR_==null?'暂无 ':ADDR_)+'</dd>';
					  tableBody+='</dl>';
					  
					  results=results+","+val.FIRE_RES_ID;
					  
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
	
	function selected(id, name){
		setTimeout(function() {
			if($('#elementsCollectionStr').val() != "") {
				window.parent.getDetailOnMapOfListClick($('#elementsCollectionStr').val(),370,240,id)
			}else {
				window.parent.localtionNewResourcePoint('${markType!''}',id);
			}
		},1000);
	
	}
	// 地图定位
	function gisPosition(res) {
		//window.parent.clearMyLayer();
		if (res == "") {
			return;
		}
		if($('#elementsCollectionStr').val() != "") {
			var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofgoods/getArcgisLocateDataListOfNewResource.jhtml?resTypeCode=${markType!''}&ids="+res+"&showType=2";
			window.parent.currentLayerLocateFunctionStr="getArcgisDataOfZhuanTi('"+url+"','"+$('#elementsCollectionStr').val()+"')";
			window.parent.getArcgisDataOfZhuanTi(url,$('#elementsCollectionStr').val(),370,240);
		}else {
			var gisDataUrl="${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofgoods/getArcgisLocateDataListOfNewResource.jhtml?resTypeCode=${markType!''}&ids="+res;
		  	window.parent.currentLayerLocateFunctionStr="getArcgisDataOfNewResource('${markType!''}','"+gisDataUrl+"')";
		  	window.parent.getArcgisDataOfNewResource('${markType!''}',gisDataUrl);
		}
	}
	
	function showErrorResource(){
		var obj = $("#errorIcon");
	
		if (obj.hasClass('icon_select')) {
			$(obj).removeClass('icon_select');
			$('#operateStatus').val('');
		} else {
			$(obj).addClass('icon_select');
			$('#operateStatus').val('2');
		}
		
		loadMessage(1,$('#pageSize').val());
	}
	</script>
</body>
</html>
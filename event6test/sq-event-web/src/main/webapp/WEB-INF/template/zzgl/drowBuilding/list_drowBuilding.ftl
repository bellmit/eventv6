<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>楼宇轮廓编辑</title>
<#include "/component/commonFiles-1.1.ftl" />
<#global ffcs=JspTaglibs["/WEB-INF/tld/RightTag.tld"] >
</head>
<style type="text/css">
.detailCol{color:blue; text-decoration:underline;}
.aaa{background:#c1392b; border-radius:3px; padding:2px 3px; line-height:12px; color:#fff; margin-left:7px; cursor:default; display:inline-block;}
‍.aaa‍:hover{background:#e84c3d;}
.panel-body{overflow:hidden;}
.LeftTree{width:360px;}
.LeftTree .search {width:336px;}
.LeftTree .con li.current {background:#fff;}
.LeftTree .SearchBox {width:26px;height:26px}
.NorPage {width:345px;}
.yema{width:295px;}
</style>
<body class="easyui-layout">

<div id="map_" region="center" border="false" style="width:100%; overflow:hidden;">
	<iframe data-iframe="true" name="MapIframe" id="MapIframe" src="" style="width:100%;height:100%;" frameborder="0" allowtransparency="true"></iframe>
</div>

<div region="west" split="false" border="false" title="<span class='easui-layout-title'>楼宇</span>" style="width:340px; background:#f4f4f4; border-right:5px solid #d8d8d8;">
	<input type="hidden" id="pageSize" value="20" />
	<input name="buildingId" id="buildingId" type="hidden"/>
	<div class="LeftTree">
   		<div class="search" style="height:60px;">
	    	<ul>
	        	<li>
	        		<input type="hidden" id="gridId" name="gridId" value="">
	        		<input type="hidden" id="infoOrgCode" name="infoOrgCode" value="">
	        		<input name="gridName" id="gridName" type="text" class="inp1 InpDisable" value="<#if startGridName??>${startGridName}</#if>" style="width:155px;"/>
	        		
	        		<input id="buildingName" name="buildingName" style="width:160px;" class="inp1 InpDisable" type="text" onkeydown="if (event.keyCode==13) {}" onblur="if(this.value=='')value='查询楼宇名称';" onfocus="if(this.value=='查询楼宇名称')value='';" value="查询楼宇名称">
	        	</li>
	        	<li style="margin-top:5px;">


					<#if USE_DRAW_BUILDING_OUTLINE?? && USE_DRAW_BUILDING_OUTLINE=='true'>
                    <label class="LabName" style="width:60px;;float:left;"><span>
						轮廓标记：
					</span></label>
					<#else>
                        <label class="LabName" style="width:40px;;float:left;"><span>
						标记：
						</span></label>
					</#if>
					</span></label>
	        		<select name="isDraw" id="isDraw" class="sel1" style="width:95px;float:left;" <#if isUseTwoTypesMap?? && isUseTwoTypesMap=='true'>onchange="isChooseMaptype(this.value)"</#if>>
	        			<option value="-1" <#if isDraw?? && isDraw == '-1'>selected</#if>>不限</option>
	        			<option value="0" <#if isDraw?? && isDraw == '0'>selected</#if>>无</option>
	        			<option value="1" <#if isDraw?? && isDraw == '1'>selected</#if>>有</option>
	        		</select>
	        		<#if isUseTwoTypesMap?? && isUseTwoTypesMap=='true'>
		        		<div id="showMaptype" style="display:none;float:left;width:145px;">
			        		<label class="LabName" style="width:60px;padding-left:10px"><span>地图类型：</span></label>
			        		<select name="editMapType" id="editMapType" class="sel1" style="width:70px;float:left;" >
			        			<option value="1" <#if editMapType?? && editMapType == '1'>selected</#if>>不限</option>
			        			<option value="2" <#if editMapType?? && editMapType == '2'>selected</#if>>二维</option>
			        			<option value="3" <#if editMapType?? && editMapType == '3'>selected</#if>>三维</option>
			        		</select>
		        		</div>
        			</#if>
	        		
	        		<span class="SearchBox" style="float:left;margin-left:2px">
	        			<span onclick="leftSearch()" class="SearchBtn"></span>
	        		</span>
	        	</li>
	        </ul>
	    </div>
	    <div id="content-d" class="con content light">
	    </div>
	    <div class="NorPage">
	        <ul>
	            <li class="PreBtn"><a href="javascript:change('1');"><img src="${rc.getContextPath()}/ui/images/pre3.png" /></a></li>
	            <li class="yema">
	            	共 <span id="pagination-num">0</span>/<span id="pageCount">0</span> 页
	            	共<span id="records">0</span>条
	            </li>
	            <li class="NextBtn"><a href="javascript:change('2');"><img src="${rc.getContextPath()}/ui/images/next3.png" /></a></li>
	        </ul>
		</div>
    </div>
</div>

<#include "/component/ComboBox.ftl">
<script type="text/javascript">
 	var gridId1 = "${startGridId?c}";
	var orgCode = "${defaultInfoOrgCode}";
	var gridName1 = "${startGridName!''}";
	var defaultInfoOrgCode = "${defaultInfoOrgCode}";
	var isUseTwoTypesMap="${isUseTwoTypesMap!''}";
	
	$(document).ready(function(){
		$("#content-d").css("height",$(document).height() - 135);//减菜单高度
	});
	$(function(){
		AnoleApi.initGridZtreeComboBox("gridName", "gridId", function (gridId, items){
			if(items!=undefined && items!=null && items.length>0){
				var grid = items[0];
				$("#infoOrgCode").val(grid.orgCode);
				$("#buildingName").val("查询楼宇名称");
				loadMessage(1,$('#pageSize').val());
			} 
		});
		
		<#if isUseTwoTypesMap?? && isUseTwoTypesMap=='true'>
			var isDraw = $("#isDraw").val();
			if(isDraw != -1){
				$("#showMaptype").css("display","block");
			}else{
				$("#showMaptype").css("display","none");
			}
		</#if>
		
		loadMessage(1,$('#pageSize').val());
		
	    var options = {
	   		axis : "yx",
	   		theme : "minimal-dark"
	   	};
	   	enableScrollBar('content-d',options);
	})
	
	function loadMessage(pageNo,pageSize){
		var list = null;
		idStr = "";
		var postData = {};
		var buildingName = $("#buildingName").val();
		if(buildingName!=null && buildingName!="" && buildingName !="查询楼宇名称") {
			postData["buildingName"]=buildingName;
		}
		var gridId = $("#gridId").val();
		if(gridId!=null && gridId!="") {
			postData["gridId"]=gridId;
		}else{
			postData["gridId"]=gridId1;
		}
		var infoOrgCode = $("#infoOrgCode").val();
		if(infoOrgCode!=null && infoOrgCode!="") {
			postData["infoOrgCode"]=infoOrgCode;
		}else{
			postData["infoOrgCode"]=defaultInfoOrgCode;
		}
		var isDraw,editMapType,drawedMaptype;
		isDraw = $("#isDraw").val();
		drawedMaptype = isDraw;
		if(isUseTwoTypesMap != "" && isUseTwoTypesMap != null && typeof(isUseTwoTypesMap) != 'undefined' && isUseTwoTypesMap == "true"){
			editMapType = $("#editMapType").val();
			if(isDraw == "-1"){
				drawedMaptype = isDraw;
			}else {
				if(editMapType == "1"){
					drawedMaptype = isDraw;
				}else{
					drawedMaptype = isDraw + '' + editMapType;
				}
			}
		}else{
			if(isDraw == "0"){
				drawedMaptype = "04";
			}
		}
		postData["drawedMaptype"]=drawedMaptype;
		
		postData["page"] = pageNo;
		postData["rows"] = pageSize;
		divModleOpen('content-d');
		$.ajax({
			type: "POST",
			url:'${rc.getContextPath()}/zhsq/drowBuildingController/listData.json',
			data: postData,
			dataType:"json",
			success: function(data){
				divModleClose();
				$('#pagination-num').text(pageNo);
				$('#records').text(data.total);
				var totalPage = Math.floor(data.total/pageSize);
				if(data.total%pageSize>=0) totalPage+=1;
				$('#pageCount').text(totalPage);
				list=data.rows;
				var tableBody="";
				tableBody+='';
				if(list && list.length>0) {
					tableBody+='<ul style="width:355px;">';
					for(var i=0;i<list.length;i++){
					    var val=list[i];
						var buildingAddress = val.buildingAddress;
						var buildingAddressFull = val.buildingAddress;
						if(buildingAddress!=null){
							if(buildingAddress.length>42){
								buildingAddress = buildingAddress.substring(0,41)+"...";
							}
						}else{
							buildingAddress = '暂无地址';
						}
					    var buildingName = val.buildingName;
						if(buildingName!=null && buildingName!="" && buildingName.length>12){
							buildingName = "["+buildingName.substring(0,12)+"...]";
						}else if(buildingName==null){
							buildingName = "";
						}else{
							buildingName = "["+buildingName+"]";
						}
					    
				        tableBody+='<li ';
				        if(i==0){
				        	
				        }
						if(buildingAddress == "暂无地址"){
							buildingAddressFull = "暂无地址";
						}
						
				        tableBody+='onclick="selectRow('+val.buildingId+','+val.gridId+',this)"><div  title="['+val.buildingName+'] : '+buildingAddressFull+'"><span class="FontDarkBlue">'+buildingName+'</span> '+buildingAddress+'</div></li>';
					}
					tableBody+='</ul>';
				} else {
					tableBody+='<div class="nodata" style="width: 360px;text-align: center;"></div>';
				}
				$(".LeftTree .mCSB_container").html(tableBody);
				if(list.length > 0){
					$('#buildingId').val(list[0].buildingId);
				}else{
					$('#buildingId').val(0);
				}
				loadArcgisMap($('#buildingId').val(),gridId1);
			},
			error:function(data){
				var tableBody='<div class="liebiao"><ul>数据读取错误！！！</ul></div>';
				$("#content-md").html(tableBody);
				loadArcgisMap($('#buildingId').val(),gridId1);
			}
		});
	}
	
	//分页
     function change(_index){
        var flag;
        var pagenum = $("#pagination-num").text();
        var lastnum = $("#pageCount").text();
        var pageSize = $("#pageSize").val();
		var firstnum = 1;
		switch (_index) {
			case '1':		//上页
			    if(pagenum==1){
			      flag=1;
			      break;
			    }
				pagenum = parseInt(pagenum) - 1;
				pagenum = pagenum < firstnum ? firstnum : pagenum;
				break;
			case '2':		//下页
			    if(pagenum==lastnum){
			      flag=2;
			      break;
			    }
				pagenum = parseInt(pagenum) + 1;
				pagenum = pagenum > lastnum ? lastnum : pagenum;
				break;
		    case '3':
		        flag=3;
		        pagenum=1;
		        break;
		    case '4':
		        pagenum = inputNum;
		        if(pagenum==lastnum){
			      flag=4;
			      break;
			    }
				pagenum = parseInt(pagenum);
				pagenum = pagenum > lastnum ? lastnum : pagenum;
				break;
			default:
				break;
		}
		
		if(flag==1){
		  alert("当前已经是首页");
		  return;
		}else if(flag==2){
		  alert("当前已经是尾页");
		  return;
		}
	    loadMessage(pagenum,pageSize);
	}

	function searchData(flag) {
		var a = new Array();
		var buildingName = $("#buildingName").val();
		if(buildingName!=null && buildingName!="") a["buildingName"]=buildingName;
		var buildingId = $("#buildingId").val();
		if(buildingId!=null && buildingId!="") a["buildingId"]=buildingId;
		var infoOrgCode = $("#infoOrgCode").val();
		if(infoOrgCode!=null && infoOrgCode!="") {
			a["infoOrgCode"]=infoOrgCode;
		} else {
			a["infoOrgCode"]="${defaultInfoOrgCode}";
		}
	}
	
	//左侧列表选择
	function selectRow(buildId,gridId,obj){
// 		loadArcgisMap(buildId,gridId1);
		$('#buildingId').val(buildId);
		$(".LeftTree .mCSB_container ul li").removeClass("current");
		$(obj).addClass("current");
		window.MapIframe.document.getElementById("tipMessage").innerHTML = "";
		window.MapIframe.document.getElementById("wid").value = buildId;
		window.MapIframe.document.getElementById("gridId").value = gridId;
		window.MapIframe.document.getElementById("hs").value = "";
		window.MapIframe.frames["iframeOfMapLoad"].clearBuildingLayer();
		//中心点定位
		window.MapIframe.frames["iframeOfMapLoad"].locateCenterAndLevel(buildId,window.MapIframe.frames["iframeOfMapLoad"].currentArcgisConfigInfo.mapType);
		//获取楼宇轮廓
		window.MapIframe.loadArcgisData();
		window.MapIframe.frames["iframeOfMapLoad"].getMapArcgisDatas();
		
		//判断是否有楼宇轮廓
		window.MapIframe.hasArcgisData();
	}
	//左侧查询
	function leftSearch(){
		loadMessage(1,$('#pageSize').val());
	}
	function loadArcgisMap(buildingId,gridId){
		var url = '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/toArcgisDrawBuildingPanel.jhtml?buildingId='+buildingId+'&gridId='+gridId;
		$("#MapIframe").attr("src",url);
	}
	
	function isChooseMaptype(value){
		if(value != -1){
			$("#showMaptype").css("display","block");
		}else{
			$("#showMaptype").css("display","none");
		}
	}
	
	//打开遮罩
	function divModleOpen(divId) {
		$("<div class='datagrid-mask'></div>").css( {
			display : "block",
			width : "100%",
			height : $(window).height()
		}).appendTo("#"+divId);
		$("<div class='datagrid-mask-msg'></div>").html("正在处理，请稍候。。。").appendTo(
				"#"+divId).css( {
			display : "block",
			left : ($("#"+divId).width() - 190) / 2,
			top : ($("#"+divId).height() - 45) / 2
		});
		document.body.scroll="no";//除去滚动条
	}
	//关闭遮罩
	function divModleClose() {
		$(".datagrid-mask").css( {
			display : "none"
		});
		$(".datagrid-mask-msg").css( {
			display : "none"
		});
		$(".datagrid-mask").remove();
		$(".datagrid-mask-msg").remove();
		document.body.scroll="auto";//开启滚动条
	}

</script>

<#include "/component/maxJqueryEasyUIWin.ftl" />

</body>
</html>
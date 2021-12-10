<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>楼宇轮廓绑定</title>
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

<div region="west" split="false" border="false" title="<span class='easui-layout-title'>楼宇</span>" style="width:360px; background:#f4f4f4; border-right:5px solid #d8d8d8;">
	<input type="hidden" id="pageSize" value="20" />
	<input name="buildingId" id="buildingId" type="hidden"/>
	<input name="selectBuildingHS" id="selectBuildingHS" type="hidden"/>
	<input name="targetType" id="targetType" type="hidden"/>
	
	<div class="LeftTree">
   		<div class="search" style="height:60px;">
	    	<ul>
	        	<li>
	        		<input type="hidden" id="gridId" name="gridId" value="">
	        		<input type="hidden" id="parentGridId" name="parentGridId" value="">
	        		<input type="hidden" id="infoOrgCode" name="infoOrgCode" value="">
	        		<input name="gridName" id="gridName" type="text" class="inp1 InpDisable" value="<#if startGridName??>${startGridName}</#if>" style="width:155px;"/>
	        		
	        		<input id="buildingName" name="buildingName" style="width:160px;" class="inp1 InpDisable" type="text" onkeydown="if (event.keyCode==13) {}" onblur="if(this.value=='')value='查询楼宇名称';" onfocus="if(this.value=='查询楼宇名称')value='';" value="查询楼宇名称">
	        	</li>
	        	<li style="margin-top:5px;">
	        		<label class="LabName" style="width:85px;;float:left;"><span>是否已经绑定：</span></label>
	        		<select name="isDraw" id="isDraw" class="sel1" style="width:70px;float:left;">
	        			<option value="-1" <#if isDraw?? && isDraw == '-1'>selected</#if>>不限</option>
	        			<option value="0" <#if isDraw?? && isDraw == '0'>selected</#if>>否</option>
	        			<option value="1" <#if isDraw?? && isDraw == '1'>selected</#if>>是</option>
	        		</select>
	        		
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
	var gridName1 = "${startGridName!''}";
	var defaultInfoOrgCode = "${defaultInfoOrgCode}";
	
	$(document).ready(function(){
		$("#content-d").css("height",$(document).height() - 135);//减菜单高度
	});
	$(function(){
		loadArcgisMap(gridId1);
		
		AnoleApi.initGridZtreeComboBox("gridName", "gridId", function (gridId, items){
			if(items!=undefined && items!=null && items.length>0){
				var grid = items[0];
				$("#infoOrgCode").val(grid.orgCode);
				$("#buildingName").val("查询楼宇名称");
				window.MapIframe.document.getElementById("showGridId").value = gridId;
				loadMessage(1,$('#pageSize').val());
				//地图定位
				$("#parentGridId").val(grid.PId);
				$("#targetType").val('grid');
				//地图定位
				//changeMapCenterXY(gridId, grid.PId);
				window.MapIframe.initButton();
				window.MapIframe.removeAllTools();
				window.MapIframe.setMapCenterXY();
				window.MapIframe.removeBuildingPolygonLayer();
			} 
		});
		
		
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
		if(isDraw == "0"){
			drawedMaptype = "04";
		}
		postData["drawedMaptype"]=drawedMaptype;
		postData["mapt"]=5;
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
				var totalPage = 0;
				if(data != null){
					$('#records').text(data.total);
					totalPage = Math.floor(data.total/pageSize);
					if(data.total%pageSize>=0) totalPage+=1;
					list=data.rows;
				}
				
				$('#pageCount').text(totalPage);
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
					    
				        tableBody+='<li style="float:left;width:100%"';
				        if(i==0){
				        	
				        }
						if(buildingAddress == "暂无地址"){
							buildingAddressFull = "暂无地址";
						}
						var td_hs;
						if(val.td_hs != null && val.td_hs != ''){
							td_hs = val.td_hs.replace(/,/g,'_');
						}else{
							td_hs = ""
						}
				        tableBody+='onclick="selectRow(\''+ td_hs +'\','+ val.gridId +','+ val.buildingId +','+ i +',this)">';
				        if(val.td_hs != null && val.td_hs != ''){
							tableBody+='<span id="markerImg_'+ i +'"><img src="${rc.getContextPath()}/images/tj_wg_80.png" class="FontDarkBlue" style="float:left" title="已绑定"></span>';
						}else{
							tableBody+='<span id="markerImg_'+ i +'"></span>';
						}
				        tableBody+='<div style="float:left;width: 90%;" title="['+val.buildingName+'] : '+buildingAddressFull+'"><span class="FontDarkBlue">'+buildingName+'</span> '+buildingAddress+'</div></li>';
					}
					tableBody+='</ul>';
				} else {
					tableBody+='<div class="nodata" style="width: 360px;text-align: center;"></div>';
				}
				$(".LeftTree .mCSB_container").html(tableBody);
				if(list && list.length > 0){
					$('#buildingId').val(list[0].buildingId);
				}else{
					$('#buildingId').val(0);
				}
			},
			error:function(data){
				var tableBody='<div class="liebiao"><ul>数据读取错误！！！</ul></div>';
				$("#content-md").html(tableBody);
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
	function selectRow(hs, gridId, buildId, selectRowNum, obj){
		window.MapIframe.selectRowNum = selectRowNum;
		window.MapIframe.drawPointFlag = true;
		$("#targetType").val('build');
		window.MapIframe.blindBuildingFlag = true;
		window.MapIframe.drawBuildingHSFlag = true;
		$('#selectBuildingHS').val(hs);
		window.MapIframe.document.getElementById("wid").value = buildId;
		window.MapIframe.document.getElementById("showGridId").value = gridId;
		$('#selectBuildingHS').val('');
		$('#buildingId').val(buildId);
		$(".LeftTree .mCSB_container ul li").removeClass("current");
		$(obj).addClass("current");
		window.MapIframe.initButton();
		window.MapIframe.removeAllTools();
		window.MapIframe.getBuildingHSById();
		window.MapIframe.setMapCenterXY();
		window.MapIframe.addSelectClickTool();
		//绑定操作
		/*********************************************
		var x,y;
		var MapHS = window.MapIframe.document.getElementById("hs").value;
		if(hs == null ||  hs == '' || typeof(hs) == 'undefined'){
			if(MapHS != null && MapHS !='' && typeof(MapHS) != 'undefined' ){
				var data = 'wid='+buildingId+'&x='+x+'&y='+y+'&hs='+MapHS+'&mapt=5'
				var url = '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofbuilding/updateMapDataOfBuildOfBinding.json?'+data;
				if(confirm("确定要进行此绑定操作吗？")){
			         $.ajax({   
						 url: url,
						 type: 'POST',
						 timeout: 3000, 
						 dataType:"json",
						 async: false,
						 error: function(data){
						 	$.messager.alert('提示','绑定报错了!','warning');
						 },
						 success: function(data){
						 	if(data.flag == true) {
						 		$.messager.alert('提示','绑定成功!','info');
						 	}else {
						 		$.messager.alert('提示','绑定失败!','info');
						 	}
						 }
					 });
			    } else {
			       
			    }
			}else{
				$.messager.alert('提示','请选择要绑定的楼宇数据!','info');
			}
		}else{
			if(MapHS != null && MapHS !='' && typeof(MapHS) != 'undefined' ){
				var data = 'wid='+buildingId+'&x='+x+'&y='+y+'&hs='+MapHS+'&mapt=5'
				var url = '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofbuilding/updateMapDataOfBuildOfBinding.json?'+data;
				if(confirm("确定要进行此绑定操作吗？")){
			         $.ajax({   
						 url: url,
						 type: 'POST',
						 timeout: 3000, 
						 dataType:"json",
						 async: false,
						 error: function(data){
						 	$.messager.alert('提示','绑定报错了!','warning');
						 },
						 success: function(data){
						 	if(data.flag == true) {
						 		$.messager.alert('提示','绑定成功!','info');
						 	}else {
						 		$.messager.alert('提示','绑定失败!','info');
						 	}
						 }
					 });
			    } else {
			       
			    }
			}
		}
		*********************************/
		window.MapIframe.document.getElementById("hs").value = "";
	}
	//左侧查询
	function leftSearch(){
		window.MapIframe.initButton();
		window.MapIframe.removeAllTools();
		loadMessage(1,$('#pageSize').val());
	}
	function loadArcgisMap(gridId){
		var url = '${rc.getContextPath()}/zhsq/map/buildaddress/buildAddress/olwfs.jhtml?gridId='+gridId;
		$("#MapIframe").attr("src",url);
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
	
	function changeMapCenterXY(gridId, parentGridId){
		window.MapIframe.setMapCenterXY();
	}
	

</script>

<#include "/component/maxJqueryEasyUIWin.ftl" />

</body>
</html>
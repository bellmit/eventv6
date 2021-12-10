<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>平潭批量网格轮廓绑定</title>
<#include "/component/commonFiles-1.1.ftl" />
<#global ffcs=JspTaglibs["/WEB-INF/tld/RightTag.tld"] >
</head>
<style type="text/css">
.detailCol{color:blue; text-decoration:underline;}
.aaa{background:#c1392b; border-radius:3px; padding:2px 3px; line-height:12px; color:#fff; margin-left:7px; cursor:default; display:inline-block;}
‍.aaa‍:hover{background:#e84c3d;}
.panel-body{overflow:hidden;}
.LeftTree{width:210px;}
.LeftTree .search {width:190px;}
.LeftTree .con li.current {background:#fff;}
.LeftTree .SearchBox {width:26px;height:26px}
.NorPage {width:210px;}
.yema{width:145px;}
</style>
<body class="easyui-layout">

<div id="map_" region="center" border="false" style="width:100%; overflow:hidden;">
	<iframe data-iframe="true" name="MapIframe" id="MapIframe" src="" style="width:100%;height:100%;" frameborder="0" allowtransparency="true"></iframe>
</div>

<div region="west" split="false" border="false" title="<span class='easui-layout-title'>网格</span>" style="width:210px; background:#f4f4f4; border-right:5px solid #d8d8d8;">
	<input type="hidden" id="pageSize" value="20" />
	<div class="LeftTree">
   		<div class="search" style="height:95px;">
	    	<ul>
	        	<li>
	        		<input type="hidden" id="gridId" name="gridId" value="<#if startGridId??>${startGridId}</#if>">
	        		<input type="hidden" id="infoOrgCode" name="infoOrgCode" value="<#if defaultInfoOrgCode??>${defaultInfoOrgCode}</#if>">
	        		<input name="gridName" id="gridName" type="text" class="inp1 InpDisable" value="<#if startGridName??>${startGridName}</#if>" style="width:175px;"/>
	        	</li>
                <li style="margin-top:5px;">
                    <input name="searchGridName" id="searchGridName" type="text" class="inp1 InpDisable" value="请输入网格名称"
						   style="font-size:12px;color:gray;width:175px;"
						   onfocus="if(this.value=='请输入网格名称'){this.value='';}$(this).attr('style','width:175px;');"
						   onblur="if(this.value==''){$(this).attr('style','font-size:12px;color:gray;width:175px;');this.value='请输入网格名称';}"
                           onkeydown="_onkeydown();"/>
                </li>
	        	<li style="margin-top:5px;">
	        		<label class="LabName" style="width:60px;"><span>轮廓标记：</span></label>
	        		<select name="isDraw" id="isDraw" class="sel1" style="width:115px;float:left;" onchange="leftSearch()">
	        			<option value="-1" <#if isDraw?? && isDraw == '-1'>selected</#if>>不限</option>
	        			<option value="0" <#if isDraw?? && isDraw == '0'>selected</#if>>无</option>
	        			<option value="1" <#if isDraw?? && isDraw == '1'>selected</#if>>有</option>
	        		</select>
	        	</li>
        		<#if isUseTwoTypesMap?? && isUseTwoTypesMap=='true'>
	        	<li id="showMaptype" style="display:none;margin-top:37px;">
	        		<label class="LabName" style="width:60px;"><span>地图类型：</span></label>
	        		<select name="editMapType" id="editMapType" class="sel1" style="width:115px;float:left;" onchange="leftSearch()">
	        			<option value="1" <#if editMapType?? && editMapType == '1'>selected</#if>>不限</option>
	        			<option value="2" <#if editMapType?? && editMapType == '2'>selected</#if>>二维</option>
	        			<option value="3" <#if editMapType?? && editMapType == '3'>selected</#if>>三维</option>
	        		</select>
	        	</li>
	        	</#if>
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
	var isUseTwoTypesMap="${isUseTwoTypesMap!''}";
	
	$(document).ready(function(){
		$("#content-d").css("height",$(document).height() - 170);//减菜单高度
	});
	$(function(){
		
		AnoleApi.initGridZtreeComboBox("gridName", "gridId", function (gridId, items){
			if(items!=undefined && items!=null && items.length>0){
				var grid = items[0];
				$("#infoOrgCode").val(grid.orgCode);
				loadMessage(1,$('#pageSize').val());
				//地图定位
				window.MapIframe.setMapcenter(false, grid.gridId);
			}
		});
		
		<#if isUseTwoTypesMap?? && isUseTwoTypesMap=='true'>
			if(isDraw != -1){
				$("#showMaptype").css("display","block");
				$(".LeftTree .search").css("height","125px");
				$("#content-d").css("height",$(document).height() - 200);//减菜单高度
			}else{
				$("#showMaptype").css("display","none");
				$(".LeftTree .search").css("height","95px");
				$("#content-d").css("height",$(document).height() - 140);//减菜单高度
			}	
		</#if>
		loadArcgisMap();
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
        var gridName = $("#searchGridName").val();
        if(gridName!=null && gridName!="" && gridName!="请输入网格名称") {
            postData["gridName"]=gridName;
        }
		var isDraw,editMapType,drawedMaptype;
		isDraw = $("#isDraw").val();
		drawedMaptype = isDraw;
		if(isUseTwoTypesMap != "" && isUseTwoTypesMap != null && typeof(isUseTwoTypesMap) != 'undefined' && isUseTwoTypesMap == "true"){
			editMapType = $("#editMapType").val();
			if(isDraw == "-1"){
				drawedMaptype = isDraw;
			}else{
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
		postData["mapt"] = 5;
		divModleOpen('content-d');
		$.ajax({
			type: "POST",
			url:'${rc.getContextPath()}/zhsq/drowGridController/listData.json',
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
					tableBody+='<ul style="width:210px;">';
					for(var i=0;i<list.length;i++){
					    var val=list[i];
						var gridName = val.gridName;
						if(gridName!=null && gridName!="" && gridName.length>12){
							gridName = gridName.substring(0,12);
						}else if(gridName==null){
							gridName = "";
						}
					    
				        tableBody+='<li ';
				        if(i==0){
				        	
				        }
				        var td_hs;
						if(val.td_hs != null && val.td_hs != ''){
							td_hs = val.td_hs.replace(/,/g,'_');
						}else{
							td_hs = "";
						}
						tableBody+='onclick="selectRow(\''+ td_hs +'\','+val.gridId+',\''+val.infoOrgCode+'\',\''+val.parentGridId+'\',\''+val.gridLevel+'\',\''+val.gridName+'\','+ i +',this)">';
						
						if(val.td_hs != null && val.td_hs != ''){
							tableBody+='<span id="markerImg_'+ i +'"><img src="${rc.getContextPath()}/js/map/spgis/lib/img/marker-gold.png"" class="FontDarkBlue" style="float:left" title="已绘制"></span>';
						}else{
							tableBody+='<span id="markerImg_'+ i +'"></span>';
						}
				        tableBody+='<div  title="'+val.gridName+'"><span class="FontDarkBlue">'+gridName+'</span></div></li>';
				        
					}
					tableBody+='</ul>';
				} else {
					tableBody+='<div class="nodata" style="width: 210px;text-align: center;"></div>';
				}
				$(".LeftTree .mCSB_container").html(tableBody);
				if(list.length > 0){
					$('#gridId').val(list[0].gridId);
				}else{
					
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
	
	//左侧列表选择
	function selectRow(hs, gridId, infoOrgCode, parentGridId, gridLevel, gridName, selectRowNum, obj){
		window.MapIframe.document.getElementById("ssqy").value = infoOrgCode;//所属区域
		window.MapIframe.document.getElementById("qyfj").value = parentGridId;//区域父级
		window.MapIframe.document.getElementById("qycj").value = gridLevel;//区域层级
		window.MapIframe.document.getElementById("qymc").value = gridName;//区域名称
		window.MapIframe.document.getElementById("selectRowNum").value = selectRowNum;
		
		window.MapIframe.document.getElementById("wid").value = gridId;
		window.MapIframe.clearAllLayer();
		window.MapIframe.initGridMapData();
		$(".LeftTree .mCSB_container ul li").removeClass("current");
		$(obj).addClass("current");
		
		//window.MapIframe.querySPGISDate(gridId);
		//window.MapIframe.setMapcenter(false, gridId);
	}
	//左侧查询
	function leftSearch(){
		loadMessage(1,$('#pageSize').val());
	}
	function loadArcgisMap(){
		var url = '';
		url = '${rc.getContextPath()}/zhsq/map/gridOLPingTan/gridOLPingTanController/spgisMap.jhtml';
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

    function _onkeydown(){
        var keyCode = event.keyCode;
        if(keyCode == 13){
            leftSearch();
        }
    }


</script>

<#include "/component/maxJqueryEasyUIWin.ftl" />

</body>
</html>
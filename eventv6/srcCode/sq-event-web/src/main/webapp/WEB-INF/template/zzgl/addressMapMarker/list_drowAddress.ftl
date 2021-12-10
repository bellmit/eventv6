<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>标准地址定位编辑</title>
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

<div region="west" split="false" border="false" title="<span class='easui-layout-title'>标准地址</span>" style="width:340px; background:#f4f4f4; border-right:5px solid #d8d8d8;">
	<input type="hidden" id="pageSize" value="20" />
	<input name="addressId" id="addressId" type="hidden" value="<#if addressId??>${addressId}</#if>"/>
    <input name="addressIdStr" id="addressIdStr" type="hidden"/>

	<div class="LeftTree">
   		<div class="search" style="height:60px;">
	    	<ul>
	        	<li>
	        		<input type="hidden" id="gridId" name="gridId" value="">
	        		<input type="hidden" id="regionCode" name="regionCode" value="">
	        		<input name="gridName" id="gridName" type="text" class="inp1 InpDisable" value="<#if startGridName??>${startGridName}</#if>" style="width:155px;"/>

                    <input id="addressName" name="addressName" style="width:150px;" class="inp1 InpDisable" type="text" onkeydown="if (event.keyCode==13) {}" onblur="if(this.value=='')value='查询地址名称';" onfocus="if(this.value=='查询地址名称')value='';" value="查询地址名称">
	        	</li>
	        	<li style="margin-top:5px;">
	        		<label class="LabName" style="width:60px;;float:left;"><span>有无标记：</span></label>
	        		<select name="isMarker" id="isMarker" class="sel1" style="width:95px;float:left;">
	        			<option value="-1" <#if isMarker?? && isMarker == '-1'>selected</#if>>不限</option>
	        			<option value="0" <#if isMarker?? && isMarker == '0'>selected</#if>>无</option>
	        			<option value="1" <#if isMarker?? && isMarker == '1'>selected</#if>>有</option>
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
	var orgCode = "${defaultInfoOrgCode}";
	var gridName1 = "${startGridName!''}";
	var defaultInfoOrgCode = "${defaultInfoOrgCode}";

	$(document).ready(function(){
		$("#content-d").css("height",$(document).height() - 135);//减菜单高度
	});
	$(function(){
		AnoleApi.initGridZtreeComboBox("gridName", "gridId", function (gridId, items){
			if(items!=undefined && items!=null && items.length>0){
				var grid = items[0];
				$("#regionCode").val(grid.orgCode);
				$("#addressName").val("查询地址名称");
				loadMessage(1,$('#pageSize').val());
                window.MapIframe.frames["iframeOfMapLoad"].locateCenterAndLevelOfGrid(grid.gridId);
			} 
		});

		
		loadMessage(1,$('#pageSize').val(), $('#addressId').val());
        var regionCode = "${defaultInfoOrgCode}";

		loadArcgisMap($('#addressId').val(),regionCode);
	    var options = {
	   		axis : "yx",
	   		theme : "minimal-dark"
	   	};
	   	enableScrollBar('content-d',options);
	})
	
	function loadMessage(pageNo,pageSize, addressId){
		var list = null;
		idStr = "";
		var postData = {};
		var addressName = $("#addressName").val();
		if(addressName!=null && addressName!="" && addressName !="查询地址名称") {
			postData["addressName"]=addressName;
		}
		var regionCode = $("#regionCode").val();
		if(regionCode!=null && regionCode!="") {
			postData["regionCode"]=regionCode;
		}else{
			postData["regionCode"]=defaultInfoOrgCode;
		}

		if(typeof addressId != 'undefined' && addressId != null){
            postData["addressId"]=addressId;
		}
		var isMarker;
		isMarker = $("#isMarker").val();
		postData["isMarker"]=isMarker;
		
		postData["page"] = pageNo;
		postData["rows"] = pageSize;
		divModleOpen('content-d');
		$.ajax({
			type: "POST",
			url:'${rc.getContextPath()}/zhsq/addressController/listData.json',
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
						var addressName = val.addressName;
						if(addressName!=null){
							if(addressName.length>42){
                                addressName = addressName.substring(0,41)+"...";
							}
						}else{
                            addressName = '暂无地址';
						}
					    var regionName = val.regionName;
						if(regionName!=null && regionName!="" && regionName.length>12){
                            regionName = "["+regionName.substring(0,12)+"...]";
						}else if(regionName==null){
                            regionName = "";
						}else{
                            regionName = "["+regionName+"]";
						}
					    
				        tableBody+='<li ';
				        tableBody+='onclick="selectRow('+val.addressId+','+val.x+','+val.y+','+val.regionCode+',this)"><div  title="['+val.regionName+'] : '+val.addressName+'"><span class="FontDarkBlue">'+regionName+'</span> '+addressName+'</div></li>';

					}
					tableBody+='</ul>';
				} else {
					tableBody+='<div class="nodata" style="width: 360px;text-align: center;"></div>';
				}
				$(".LeftTree .mCSB_container").html(tableBody);
//				if(list.length > 0){
//					$('#addressId').val(list[0].addressId);
//				}else{
//					$('#addressId').val(0);
//				}
//                if(list.length == 1){
//                    selectRow(val.addressId,val.x,val.y,val.regionCode,this);
//                }
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
		var addressName = $("#addressName").val();
		if(addressName!=null && addressName!="") a["addressName"]=addressName;
		var addressId = $("#addressId").val();
		if(addressId!=null && addressId!="") a["addressId"]=addressId;
		var regionCode = $("#regionCode").val();
		if(regionCode!=null && regionCode!="") {
			a["regionCode"]=regionCode;
		} else {
			a["regionCode"]="${defaultInfoOrgCode}";
		}
	}
	
	//左侧列表选择
	function selectRow(addressId, addressX, addressY, regionCode, obj){
		var addressObj = {
			"addressId" : addressId,
			"regionCode" : regionCode,
			"x" : addressX,
			"y" : addressY
		}
		if(typeof addressObj != 'undefined' && addressObj != null){
            $('#addressId').val(addressObj.addressId);
            $(".LeftTree .mCSB_container ul li").removeClass("current");
            $(obj).addClass("current");
            window.MapIframe.document.getElementById("tipMessage").innerHTML = "";
            window.MapIframe.document.getElementById("addressId").value = addressObj.addressId;
            window.MapIframe.document.getElementById("gridId").value = addressObj.gridId;
            window.MapIframe.frames["iframeOfMapLoad"].clearAddressLayer();//清除上一个标注信息
            //获取地址定位数据并定位到地址的标注点位中心点
            window.MapIframe.loadArcgisData(addressId);

		}

	}
	//左侧查询
	function leftSearch(){
		loadMessage(1,$('#pageSize').val());
	}
	function loadArcgisMap(addressId,regionCode){
		var url = '${rc.getContextPath()}/zhsq/addressController/toDrawAddressPanel.jhtml?addressId='+addressId+'&regionCode='+regionCode;
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

</script>


</body>
</html>
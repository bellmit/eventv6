<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>协警地址列表</title>
<#include "/component/commonFiles-1.1.ftl" />

</head>
<style type="text/css">
.detailCol{color:blue; text-decoration:underline;}
.aaa{background:#c1392b; border-radius:3px; padding:2px 3px; line-height:12px; color:#fff; margin-left:7px; cursor:default; display:inline-block;}
‍.aaa‍:hover{background:#e84c3d;}
.panel-body{overflow:hidden;}
.LeftTree{width:300px;}
.LeftTree .search {height:25px;width:280px;}
.LeftTree .con li.current {background:#fff;}
.LeftTree .SearchBox {width:26px;height:26px}
.NorPage {width:300px;}
.yema{width:235px;}
</style>
<body class="easyui-layout">
<input type="hidden" name="x" id="x" value="<#if x??>${x?c}</#if>" />
<input type="hidden" name="y" id="y" value="<#if y??>${y?c}</#if>" />
<input type="hidden" name="gridId" id="gridId" value="<#if gridId??>${gridId}</#if>" />
<input type="hidden" name="mapt" id="mapt" value="<#if mapt??>${mapt}</#if>" />
<input type="hidden" name="callBackUrl" id="callBackUrl" value="<#if callBackUrl??>${callBackUrl}</#if>" />
<input type="hidden" name="mapType" id="mapType" value="<#if mapType??>${mapType}</#if>" />
<input type="hidden" name="isEdit" id="isEdit" value="<#if isEdit??>${isEdit}</#if>" />
<input type="hidden" name="infoOrgCode" id="infoOrgCode" value="<#if infoOrgCode??>${infoOrgCode}</#if>" />
<input type="hidden" name="showMap" id="showMap" value="${(showMap)!}" />
<input type="hidden" name="targetDownDivId" id="targetDownDivId" value="<#if targetDownDivId??>${targetDownDivId}</#if>" />


<div id="map_" region="center" border="false" style="width:100%; overflow:hidden;">
	<iframe data-iframe="true" name="MapIframe" id="MapIframe" src="" style="width:100%;height:100%;" frameborder="0" allowtransparency="true"></iframe>
</div>
<#if isEdit?? && isEdit=="false">
<#elseif isEdit?? && isEdit=="true" && nearbyAddressShow?? && nearbyAddressShow == "true">
<div region="west" split="false" border="false" style="width:300px; background:#f4f4f4; border-right:5px solid #d8d8d8;">
	<input type="hidden" id="pageSize" value="20" />
	<div class="LeftTree">
   		<div class="search">
	    	<ul>
	        	<li>
	        		<input name="addressName" id="addressName" type="text" class="inp1 InpDisable" value="" style="width:235px;" placeholder="请输入地址名称"/>
                    <span class="SearchBox" style="float:right">
                        <span onclick="leftSearch()" class="SearchBtn" title="查询"></span>
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
</#if>

<div style="height:0px">
    <iframe id="cross_domain_frame" name="cross_domain_frame" src="" marginwidth=0 marginheight=0 scrolling="no" frameborder=0></iframe>
</div>
<script type="text/javascript">

	
	$(document).ready(function(){
        $("#content-d").css("height",$(document).height() - 75);//减菜单高度
	});
	$(function(){
        setTimeout(loadArcgisMap(), 1000);

		loadMessage(1,$('#pageSize').val());

	    var options = {
	   		axis : "yx",
	   		theme : "minimal-dark"
	   	};
	   	enableScrollBar('content-d',options);

        window.onresize=function(){
            setTimeout(function(){
                $("#content-d").css("height",$(document).height() - 75);//减菜单高度
                window.MapIframe.resizeMap($(document).width(), $(document).height())
            }, 1000);

        }

	});
	
	function loadMessage(pageNo,pageSize){
		var list = null;
		var postData = {};
		var addressName = $("#addressName").val();
		if(addressName!=null && addressName!="" && addressName!="请输入地址名称") {
            postData["addressName"]=addressName;
		}
        var infoOrgCode = $("#infoOrgCode").val();
        if(infoOrgCode!=null && infoOrgCode!="") {
            postData["infoOrgCode"]=infoOrgCode;
        }
        var x = $("#x").val();
        if(x!=null && x!="") {
            postData["x"]=x;
        }
        var y = $("#y").val();
        if(y!=null && y!="") {
            postData["y"]=y;
        }

		$.ajax({
			type: "POST",
			url:'${rc.getContextPath()}/zhsq/map/xiejingController/getAuxiliaryPoliceAddrsByXY.json',
			data: postData,
			dataType:"json",
			success: function(data){
                $(".LeftTree .mCSB_container").html("");
				$('#pagination-num').text(pageNo);
				$('#records').text(data.total);
				var totalPage = Math.floor(data.total/pageSize);
				if(data.total%pageSize>=0) totalPage+=1;
				$('#pageCount').text(1);
				list=data.rows;
                var tableBody="";
                if(list && list.length>0) {
                    tableBody+='<ul style="width:100%;">';
                    for(var i=0;i<list.length;i++){
                        var val=list[i].addressPathName;
                        var regionCode=list[i].regionCode;
                        var addressName = val;
                        tableBody+='<li ';
                        if(addressName!=null && addressName.length>22){
                            addressName = addressName.substring(0,22)+"...";
                        }
                        tableBody+='onclick="selectRow(\''+val+'\',\''+regionCode+'\',this)" title="'+val+'"> '+ addressName +'</li>';
                    }
                    tableBody+='</ul>';
                } else {
                    $("#tipMessage").html("未获取到附近地址！");
                    tableBody+='<div class="nodata" style="width: 174px;text-align: center;margin-top: 25%"></div>';
                }
				$(".LeftTree .mCSB_container").html(tableBody);

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
	function selectRow(addressName, regionCode, obj){
        $(".LeftTree .mCSB_container ul li").removeClass("selectRow");
        $(obj).addClass("selectRow");
        var title,value;
        title = "您选择的地址是：" + addressName;
        if(addressName.length>20){
            value = "您选择的地址是：" + addressName.substring(0,20)+"...";;
        }else{
            value = "您选择的地址是：" + addressName;
        }

		window.MapIframe.document.getElementById("tipMessage").innerHTML = value;
        window.MapIframe.document.getElementById("tipMessage").title = title;
		window.MapIframe.document.getElementById("XIEJINGAddress").value = addressName;
		window.MapIframe.document.getElementById("XIEJINGregionCode").value = regionCode;
	}
	//左侧查询
	function leftSearch(){
		loadMessage(1,$('#pageSize').val());
	}
	function loadArcgisMap(){
		var url = '${rc.getContextPath()}/zhsq/map/xiejingController/xiejingMap.jhtml';
		var data = "";
		var targetDownDivId = "<#if targetDownDivId??>${targetDownDivId}</#if>";
        if(targetDownDivId != ""){
            data = data+"targetDownDivId="+targetDownDivId;
        }
		var x = $("#x").val();
        if(x!=''){
            data = data+"&x="+x;
        }
        var y = $("#y").val();
        if(y!=''){
            data = data+"&y="+y;
        }
        var mapType = $("#mapType").val();
        if(mapType!=''){
            data = data+"&mapType="+mapType;
        }
        var gridId = $("#gridId").val();
        if(gridId!=''){
            data = data+"&gridId="+gridId;
        }
        var isEdit = $("#isEdit").val();
        if(isEdit!=''){
            data = data+"&isEdit="+isEdit;
        }
        var infoOrgCode = $("#infoOrgCode").val();
        if(infoOrgCode!=''){
            data = data+"&infoOrgCode="+infoOrgCode;
        }
        var callBackUrl = $("#callBackUrl").val();
        if(callBackUrl!=''){
            data = data+"&callBackUrl="+callBackUrl;
        }
        var showMap = $('#showMap').val();
		if(showMap!=''){
			data += "&showMap=" + showMap;
		}
		url = url + "?" +data;
		$("#MapIframe").attr("src",url);
	}

    function loadXIEJINGAddrss(x, y){
        var list = null;
        var postData = {};
        var addressName = $("#addressName").val();
        if(addressName!=null && addressName!="") {
            postData["addressName"]=addressName;
        }
        //var x = $("#x").val();
        if(x!=null && x!="") {
            postData["x"]=x;
        }
        //var y = $("#y").val();
        if(y!=null && y!="") {
            postData["y"]=y;
        }

        modleopen();
        $.ajax({
            url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/getAuxiliaryPoliceAddrsByXY.json?',
            type: 'POST',
            data: postData,
            dataType: "json",
            async: false,
            error: function (result) {
                $.messager.alert('友情提示', '获取协警地址异常!', 'warning');
            },
            success: function (result) {
                //$("#tipMessage").html("您未选择地址！默认第一条！");
                modleclose();//关闭遮罩层
                $('#pagination-num').text(1);
                if(result != null){
                    $('#records').text(result.total);
                    var totalPage = 1;
                    $('#pageCount').text(totalPage);
                    list=result.rows;
                }else{
                    $('#records').text(0);
                    var totalPage = 1;
                    $('#pageCount').text(1);
                }
                var tableBody="";
                if(list && list.length>0) {
                    tableBody+='<ul style="width:100%;">';
                    for(var i=0;i<list.length;i++){
                        var val=list[i].addressPathName;
                        var regionCode=list[i].regionCode;
//					if(i==0){
//                        $("#XIEJINGAddress").val(val);
//					}
                        var addressName = val;
                        tableBody+='<li ';
                        if(addressName!=null && addressName.length>22){
                            addressName = addressName.substring(0,22)+"...";
                        }
                        tableBody+='onclick="selectRow(\''+val+'\',\''+regionCode+'\',this)" title="'+val+'"> '+ addressName +'</li>';
                    }
                    tableBody+='</ul>';
                } else {
                    $("#tipMessage").html("未获取到附近地址！");
                    tableBody+='<div class="nodata" style="width: 174px;text-align: center;"></div>';
                }
                $(".LeftTree .mCSB_container").html(tableBody);


            },
            error:function(data){
                var tableBody='<div class="liebiao"><ul>数据读取错误！！！</ul></div>';
                $("#content-md").html(tableBody);
            }

        });
    }

</script>

<#include "/component/maxJqueryEasyUIWin.ftl" />

</body>
</html>
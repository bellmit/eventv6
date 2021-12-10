<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>法人列表</title>

<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<#include "/component/ComboBox.ftl" />
<script src="${rc.getContextPath()}/js/layer/3.1.1/layer.js"></script>
<style type="text/css">
    .ztree li span{
		font-size: 12px;
	}
</style>
</head>
<body style="border:none;scolling:yes">
	<input type="hidden" id="gridId" value="<#if gridId??>${gridId?c}</#if>" />
	<input type="hidden" id="elementsCollectionStr" value="<#if elementsCollectionStr??>${elementsCollectionStr}</#if>" />
    <input type="hidden" id="pageSize" value="20" />
	<input type="hidden" id="infoOrgCode" value="<#if infoOrgCode??>${infoOrgCode}</#if>" />
    <div class="" style="display:block;">
        <div class="ListSearch">
        	<div class="condition">
            	<ul>
                	<li class="LC1">法人名称：</li>
                	<li class="LC2"><input id="legalName" name="legalName" type="text" class="inp1" /></li>
                </ul>
                <#if legalType??>
	            	
                <#else>
                	<ul>
	                	<li class="LC1">法人类型：</li>
	                	<li class="LC2">
                            <input type="hidden" id="legalType" name="legalType"/>
                            <input id="legalTypeName" name="legalTypeName" type="text" class="inp1" style="width:130px"/>
	                	</li>
	                </ul>
                </#if>
            	<ul>
                	<li class="LC1">行业分类：</li>
                	<li class="LC2">
                        <input type="hidden" id="industryClassification" name="industryClassification"/>
                        <input id="industryClassificationStr" name="industryClassificationStr" type="text" class="inp1" style="width:130px"/>
					</li>
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
$(function(){
    var legalTypeDc = new Array();
	<#if legalTypeDC??>
		<#list legalTypeDC as l>
		legalTypeDc.push({name:"${l.tagName}",value:"${l.code}"});
		</#list>
	</#if>

    AnoleApi.initListComboBox("legalTypeName", "legalType", "D121002", null, [""], {
        DataSrc : legalTypeDc,
        ShowOptions : {
            EnableToolbar : true
        }
    });

    AnoleApi.initTreeComboBox("industryClassificationStr", "industryClassification", "A001007128", null, [], {
        RenderType: "00",
        ChooseType: "1",
        ShowOptions: {
            EnableToolbar: true
        }
    });


});


function pageSubmit(){
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
$('#legalName').keydown(function(e){
	if(e.keyCode==13){ 
		loadMessage(1,$("#pageSize").val());
	} 
}); 
function ShowOrCloseSearchBtn(){
    var temp = $(".ListSearch").is(":hidden");//是否隐藏
    if (temp == false) {
        $(".ListSearch").hide();
    } else {
        $(".ListSearch").show();
    }

}
function CloseSearchBtn(){
	$(".ListSearch").hide();
}
	$(function(){
	    var winHeight=window.parent.document.getElementById('map'+window.parent.currentN).offsetHeight-62;
       	$("#content").height(winHeight-56); 
	    loadMessage(1,$("#pageSize").val());
	});
	var results="";//获取定位对象集合
	var layerName="";
	function loadMessage(pageNo,pageSize,searchType){
		layerName = window.parent.analysisOfElementsCollection($('#elementsCollectionStr').val(),"menuLayerName");
		window.parent.currentLayerName = layerName;
		if('searchBtn'==searchType) {
			window.parent.clearSpecialLayer(layerName);
			window.parent.currentListNumStr = "";
		}
		results="";
		var gridId = $('#gridId').val();
		var legalName = $('#legalName').val();
		var legalType = $("#legalType").val();
        var infoOrgCode = $("#infoOrgCode").val();
		var industryClassification = $("#industryClassification").val();
		var pageSize = $("#pageSize").val();
		$.blockUI({message: "加载中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'20%',
    	background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
		var postData = 'page='+pageNo+'&rows='+pageSize+'&gridId='+gridId+'&infoOrgCode='+infoOrgCode+'&legalName='+legalName+'&legalType='+legalType+'&industryClassification='+industryClassification;
		$.ajax({
			type: "POST",
			url:'${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataoforganization/legalPersonListData.json?t='+Math.random(),
			data: postData,
			dataType:"json",
			success: function(data){
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
					  
					  var legalName = val.legalName;
					  if(legalName!=null && legalName!="" && legalName.length>12){
                          legalName = legalName.substring(0,12)+"...";
					  }
					  
					  var regAddr = val.regAddr;
					  if(regAddr!=null){
						  if(regAddr.length>15){
                              regAddr = regAddr.substring(0,15)+"...";
						  }
					  }else{
                          regAddr= "";
					  }
					  
					  var involveType = val.corType;

					  tableBody+='<dl onclick="selected(\''+val.legalId+'\',\''+(val.legalName==null?'':val.legalName)+'\')">';
					  tableBody+='<dt>';
					  tableBody+='<span class="fr">'
					  +'</span>';
					  tableBody+='<b class="FontDarkBlue" id="bizList_'+val.legalId+'">'+(val.legalName==null?'':val.legalName)+'</b>';
					  
					  tableBody+='</dt>';
					  tableBody+='<dd title=\''+(val.regAddr==null?'':val.regAddr)+'\'>'+regAddr+'</dd>';
					  tableBody+='</dl>';
					  
					  results=results+","+val.legalId;
					}
					results=results.substring(1, results.length);
				} else {
					tableBody+='<div style="text-align: center;"><img style="width: 174px;" src="${uiDomain!''}/images/map/gisv0/special_config/images/nodata.png" title="暂无数据"/></div>';
				}
		        tableBody+='</div>';
				$("#content").html(tableBody);
				$(".AdvanceSearch").css("display","none");
				gisPosition(results);
			},
			error:function(data){
				$.unblockUI();
				var tableBody='<div class="liebiao"><ul>数据读取错误！！！</ul></div>';
				$("#content").html(tableBody);
			}
		});
		CloseSearchBtn();
	}
	var currentPageNum=1;
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
		currentPageNum = pagenum;
	    loadMessage(pagenum,pageSize);
	}
	
	$("#moreSearch").toggle(function(){
	    $(".AdvanceSearch").css("display","block");
	},function(){
	   $(".AdvanceSearch").css("display","none");
	});
	
	/**
	 * 加载业务定位后的回调函数（通过多个地址定位）
	 * @param data
	 */
	var bizLocalMap = [];
	function loadBizLocateCallBack(data) {
	    bizLocalMap = [];
	    if(data != null && data.length >0){
	        for (var i=0;i<data.length;i++){
	            bizLocalMap.push({
	                "id" : data[i].id,
	                "bizId" : data[i].bizId,
	                "name" : data[i].name,
	                "address" : data[i].address,
	                "wid" : data[i].wid
	            });
	        }
	    }
	}

	function selected(id, name){
        var wid = id;
        if(bizLocalMap != null){
            var bizAddressArr = [];
            var bizAddrHTML = "<dd>请选择地址：</dd>";
            for(var i=0; i< bizLocalMap.length;i++){
                if(id == bizLocalMap[i].bizId){
                    bizAddressArr.push(bizLocalMap[i]);
                    bizAddrHTML += "<dd><a href='#' onclick='showBizDetail(\""+bizLocalMap[i].wid+"\")'>"+ bizLocalMap[i].address+"</a></dd>"
                }
            }
            bizAddrHTML = "<div id='bizAddrList' style='max-height: 300px;overflow-y:auto'><dl>"+bizAddrHTML+"</dl></div>";
            if(bizAddressArr != null && bizAddressArr.length >1){
                tipObj = layer.tips(bizAddrHTML, "#bizList_"+id+"", {
                    tips: [1, 'rgba(120, 186, 50, 1)'], //还可配置颜色
                    area: '240px',
                    time: 0,
                    shade: 0.1,
                    shadeClose: true
                });
            }else{
                if(bizAddressArr != null && bizAddressArr.length == 1){
                    wid = bizAddressArr[0].wid;
                }
                showBizDetail(wid);
            }
        }
    }

	function showBizDetail(wid){
		var gridId = $('#gridId').val();
		setTimeout(function(){
			window.parent.getDetailOnMapOfListClick($('#elementsCollectionStr').val(),400,236,wid,gridId);
			if(typeof tipObj != 'undefined'){
				layer.close(tipObj);
			}
		},1000);
	}

//--定位
	function gisPosition(res){
		if (typeof parent.MMApi != "undefined") {	// 判断是否是高德地图
			var opt = {};
			opt.w = 400;
			opt.h = 236;
			opt.ecs = $('#elementsCollectionStr').val();
			opt.gridId = $('#gridId').val();
			opt.url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/legalPersonLocateData.jhtml?ids="+res+"&showType=2";
			return parent.MMApi.markerIcons(opt);
		}
		
		window.parent.clearSpecialLayer(layerName);
		if (res==""){
			return ;
		}
		
		if($('#elementsCollectionStr').val() != "") {
			var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataoforganization/legalPersonLocateData.jhtml?ids="+res+"&showType=2";
			window.parent.currentLayerLocateFunctionStr="getArcgisDataOfZhuanTi('"+url+"','"+$('#elementsCollectionStr').val()+"')";
			window.parent.getArcgisDataOfZhuanTi(url,$('#elementsCollectionStr').val(),400,235, null, null, null, null, null, undefined, loadBizLocateCallBack);

        }else {
			var corurl="${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataoforganization/legalPersonLocateData.jhtml?ids="+res+"&showType=2";
			window.parent.currentLayerLocateFunctionStr="getArcgisDataOfCor('"+corurl+"')";
			window.parent.getArcgisDataOfCor(corurl);
		}
	}
</script>
</body>
</html>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>违法建设</title>


<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>

<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
</head>
<body style="border:none;scolling:yes">
	<input type="hidden" id="gridId" value="${gridId?c}" />
	<input type="hidden" id="elementsCollectionStr" value="<#if elementsCollectionStr??>${elementsCollectionStr}</#if>" />
	<input type="hidden" id="pageSize" value="20" />
    <div class="" style="display:block;">
        <div class="ListSearch">
        	<div class="condition">
            	<ul>
                	<li class="LC1">业主姓名：</li>
                	<li class="LC2">
                		<input id="owner"  type="text" class="inp1"/>
                	</li>
                </ul>
            	<ul>
                	<li class="LC1">性质类型：</li>
                	<li class="LC2">
                		<select id="nature" class="sel1">
         				   <option value="">==请选择==</option>
      						  <#if TYPE_??>
        						    <#list TYPE_ as l>
          						      <option value="${l.dictGeneralCode}">${l.dictName}</option>
          						  </#list>
       						 </#if>
      				   </select>
                	</li>
                </ul>
            	<ul>
                	<li class="LC1">&nbsp;</li>
                	<li class="LC2"><input name="" type="button" value="查询" class="NorBtn" onclick="loadMessage(1,$('#pageSize').val());" /></li>
                </ul>
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
	var isExpire = "false";
	var inputNum;
	function pageSubmit(){
		inputNum = $("#inputNum").val();
		var pageCount = $("#pageCount").text();
		if(isNaN(inputNum)){
			inputNum=1;
		}
		if(parseInt(inputNum)>parseInt(pageCount)){
			inputNum=pageCount;
		}
		if(inputNum<=0||inputNum==""){
			inputNum=1;
		}
		change('4');
	}
	
	function ShowOrCloseSearchBtn(){
		var temp= $(".ListSearch").is(":hidden");//是否隐藏 
		if(temp == false) {
			$(".ListSearch").hide();
		}else {
			$(".ListSearch").show();
		}
	}
	function CloseSearchBtn(){
		$(".ListSearch").hide();
	}
	$(document).ready(function(){
	    var winHeight=window.parent.document.getElementById('map'+window.parent.currentN).offsetHeight-62;
       	$("#content").height(winHeight-56); 
	    loadMessage(1,$("#pageSize").val());
	});
	var results="";//获取定位对象集合
	function loadMessage(pageNo,pageSize){
		results="";
		var gridId = $('#gridId').val();
		var owner = $('#owner').val();
		var type = $('#type').val();
		if(type==""){
		  type=null;
		}
		$.blockUI({message: "加载中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'20%',
    	background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
		var postData = 'page='+pageNo+'&rows='+pageSize+'&gridId='+gridId;
		if(type!=null && type!=""){
			postData+=('&type='+type);
		}
		if(owner!=null && owner!=""){
			postData+=('&owner='+owner);
		}
		$.ajax({
			type: "POST",
			url:'${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSocietyController/ctIllegalListData.json?t='+Math.random(),
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
					  
					  var owner = val.owner;
					  if(owner!=null && owner!="" && owner.length>15){
						owner = owner.substring(0,14)+"...";
					  }
					  
					  var address = val.address;
					  
					  var timeAndAddr = (val.crimeTime==null?'':val.crimeTime)+'&nbsp;'+(val.crimeAddr==null?'':val.crimeAddr);
					  
					  tableBody+='<dl onClick="selected(\''+val.illegalConstructionId+'\')">';
					  tableBody+='<dt>';
					  tableBody+='<b class="FontDarkBlue" >业主：'+(val.owner==null?'':val.owner)+'</b>';
					  tableBody+='</dt>';
					  tableBody+='<dd title=\''+(address)+'\'>地址：'+(address.length>15?address.substring(0, 14):address)+'</dd>';
					  tableBody+='</dl>';
					  results=results+","+val.illegalConstructionId;
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
	
	$("#moreSearch").toggle(function(){
	    $(".AdvanceSearch").css("display","block");
	},function(){
	   $(".AdvanceSearch").css("display","none");
	});
	function selected(id) {
		setTimeout(function() {
			window.parent.getDetailOnMapOfListClick($('#elementsCollectionStr').val(),565,340,id)
		},1000);
	}
	//--定位
	function gisPosition(res){
		
		if (res==""){
			return ;
		}
		var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSocietyController/getArcgisLocateDataListOfCtIllegal.jhtml?ids="+res;
		window.parent.currentLayerLocateFunctionStr="getArcgisDataOfZhuanTi('"+url+"','"+$('#elementsCollectionStr').val()+"')";
		window.parent.getArcgisDataOfZhuanTi(url,$('#elementsCollectionStr').val(),565,340);
	}
</script>
</body>
</html>
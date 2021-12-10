<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>莆田中间首页页面已办</title>
<link rel=stylesheet type=text/css href="${rc.getContextPath()}/theme/arcgis/standardmappage_putian/css/public.css"/>
<link rel=stylesheet type=text/css href="${rc.getContextPath()}/theme/arcgis/standardmappage_putian/css/frame.css"/>
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/theme/arcgis/standardmappage_putian/css/gridListStyle.css" />
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/theme/arcgis/standardmappage_putian/css/mapData.css" />
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-last/themes/default/easyui.css"/>
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-last/themes/icon.css"/>
<script src="${rc.getContextPath()}/theme/arcgis/standardmappage_putian/js/tablestyle.js"></script>
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>

<style type="text/css">
.backosp {
    background: #f3f6fd;
    height: 24px;
    padding: 6px 10px 0; font-size:14px;
}
.backosp a {
    background: url(${rc.getContextPath()}/theme/arcgis/standardmappage_putian/images/icon2_02.png) no-repeat;
    display: block;
    height: 16px;
    line-height: 16px;
    padding: 0 0 0 23px;
    text-decoration: none !important;
    width: 90px; float:left
}
.backosp span{float:right;}
.pageContent1{position: relative; display: block;width:100%; height:100%;}
	
#listtable{table-layout: fixed;}

#listtable td{
　　white-space: nowrap;
　　overflow: hidden;
　　text-overflow: ellipsis;
}
.kwgray{color:#878787;}
</style>
</head>
<body style="border:none;" scroll="no" >
	
	<table style="background-color: #f7f7f7;height:35px;display:none;" class="pageContent">
		<tr style="">
			<td></td>
		</tr>
	</table>
	<DIV id="mainpanel" class="pageContent">
		<DIV class="grid" >				
			<DIV id="gridTbodyId" class="gridTbody"  >
				 <div id="box_1" style="display:block;">
                    <div class="NavBox" id="listtable">                        
                    </div>                   
                </div>
			</DIV>
		</DIV>
	</DIV>
				
	<#include "/map/arcgis/standardmappage/pagination.ftl"/>
	
<script type="text/javascript">
	var batchType=""; //处理类型 1待办事项、2关注事项、3督办事项
	var gridId = "${gridId}";
	var orgCode = "${startOrgCode}";

	$(document).ready(function(){
		eventTypeClick("1");//默认选中第一个页签并加载数据
		var winHeight=window.parent.document.getElementById('map0').offsetHeight-32-38-28;
       	$("#gridTbodyId").height(winHeight);
	});
	
	//点击查询时显示查询选项
	function changeOfQueryCondition() {
		var queryConditionDivDisplay = $("#queryConditionDiv").css("display");
		if(queryConditionDivDisplay == "none") {
			$("#queryConditionDiv").css("display","block");
		}else if(queryConditionDivDisplay == 'block') {
			$("#queryConditionDiv").css("display","none");
		}
	}
	//点击分类时需要清空查询类容
	function eventTypeClick(bigType,smallType) {
		if(bigType != undefined) {
			batchType = bigType;
		}else {
			batchType = "";
		}
		
		if(smallType != undefined) {
			type = smallType;
		}else {
			type = "";
		}
		if($('#content').val() != '请输入关键字')  {
			$('#content').attr("value","请输入关键字");
		}
		changeIconAfterClick(bigType,smallType);
		page(0);//加载数据
	}
	//切换图片
	function changeIconAfterClick(bigType,smallType) {
		if(bigType != undefined) {
			batchType = bigType;
			if(bigType == '1') {//1待办事项、2关注事项、3督办事项
				$("#eventTypeLi1").addClass("current");
				$("#eventTypeLi2").removeClass("current");
				$("#eventTypeLi3").removeClass("current");
			}else if(bigType == '2') {
				$("#eventTypeLi1").removeClass("current");
				$("#eventTypeLi2").addClass("current");
				$("#eventTypeLi3").removeClass("current");
			}else if(bigType == '3') {
				$("#eventTypeLi1").removeClass("current");
				$("#eventTypeLi2").removeClass("current");
				$("#eventTypeLi3").addClass("current");
			}
		}
	}
	
	var results="";//获取定位对象集合
	function page(i){
     	results="";
		var orgCode = $('#orgCode').val();
		var pageSize = $("#pageSize").val();
		
		var pageNo;
		var totalPage=$('#totalPage').text();
		if(i!=-2){
			if(i==0){
				pageNo = 1;
			} else if(i==-3) {
				if(totalPage=="") return;
				if(parseInt(totalPage) == 0)
					return;
				pageNo = totalPage;
			} else {
				pageNo = $('#curPage').text();
				if(parseInt(totalPage) == 0)
					return;
				if(pageNo==totalPage && i>0)
					return;
				if(pageNo==1 && i<0)
					return;
				pageNo = parseInt(pageNo)+i;
			}
		} else {
			pageNo = $('#toPage').val();
			var reg = /^[1-9]+[0-9]*]*$/;
			if(!reg.test(pageNo))
				return;
			pageNo = parseInt(pageNo);
			if(pageNo>totalPage)
				return;
			if(pageNo<1)
				return;
		}
		
		$.blockUI({message: "加载中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'20%',
    		background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
    	//var gridId = window.parent.frames['iframeOfMapLoad'].document.getElementById("gridId").value;
    	var gridId  = "${gridId}";
		var postData = 'page='+pageNo+'&rows='+pageSize+'&gridId='+gridId+'&eventType=had';
		$.ajax({
			type: "POST",
			url: '${rc.getContextPath()}/zhsq/event/eventDisposalController/listData.json?t='+Math.random(),
			data: postData,
			dataType:"json",
			success: function(data){
				$.unblockUI();
				$('#curPage').text(pageNo);
				var totalPage = Math.floor(data.total/pageSize);
				if(data.total%pageSize>0) totalPage+=1;
				$('#totalPage').text(totalPage);
				var list=data.rows;
				var tableBody="";
				if(list && list.length>0) {
					for(var i=0;i<list.length;i++){
						var val=list[i];
						//var imgurl=window.parent.frames['iframeOfMapLoad'].eventimgurl;
						//var imgurlon=imgurl.replace("gisunselected","gisselected");
						//var imgid = "event"+val.eventId;
						tableBody+="<ul>";
						tableBody+="<div>";
                        tableBody+=" <li>";
                         var timeStr = (val.happenTimeStr==null)?"无发生日期":val.happenTimeStr.substr(0,10);
                        tableBody+="   <p><span class='fr'>"+timeStr+"</span><em><a href='javascript:void(0)'  onClick='selected(\""+(val.taskInfoId==null?'':val.taskInfoId)+"\",\""+val.eventId+"\",\"1201\")'>"+val.typeName+"</a></em></p>";
                        tableBody+="   <p>"+val.content+"</p>";
                        tableBody+=" </li>";
                        tableBody+="</div>";
                        tableBody+="</ul>";
						results=results+","+val.eventId+"_"+(val.taskInfoId==null?'':val.taskInfoId);
					}
					results=results.substring(1, results.length);
				} else {
					tableBody+='<span align="center" style="color:red;">未查到相关数据！</span>';
				}
				$("#listtable").html(tableBody);
				//gisPosition(results);
			},
			error:function(data){
				$.unblockUI();
				var tableBody='<span style="color:red;align-text:center;">数据读取错误！</span>';
				$("#listtable").html(tableBody);
			}
		});
	}
	
	function selected(id, name){
		var arrangeId = id;
		var name =name;
		
		gisPosition(id);
		
		setTimeout(function() {
			window.parent.locationGlobalEyesPoint(id);
		},1000);
		
		window.parent.locationGlobalEyesPoint(id);
	}
	//地图定位
	function gisPosition(res){
		if (res==""){
			return ;
		}
		
		var qqyurl= "${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/getArcgisLocateDataListOfGlobalEyes.jhtml?ids="+res+"&showType=2";
		window.parent.getArcgisDataOfGlobalEyes(qqyurl);
	}
	function styleonmouseoper(id,imgurl) {
		styleonmouseoper(id,imgurl);
	}
	
	var flagimgid = "";
	
	function styleonmouseoper(id,imgurl) {
		var idObj = document.getElementById(id);
		if(idObj != null && flagimgid != id) {
			idObj.src = imgurl;
		}
	}
</script>
</body>
</html>
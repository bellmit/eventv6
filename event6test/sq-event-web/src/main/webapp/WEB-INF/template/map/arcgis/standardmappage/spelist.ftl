<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>政策法规</title>
<#include "/component/commonFiles-1.1.ftl" />
<style>
body{ font-size:14px; font-family:"微软雅黑"; color:#666; }
h2,h3,h4,p,ul,li,body{padding:0; margin:0;}
ul,li{ list-style:none;}
a{ text-decoration:none; color:#666;}
a:hover{ color:#ff3333;}
.speleft{ float:left; width:18%; background:#f9f9f9; min-height:500px; padding:0 1%;}
.speleft h2{ font-size:18px; font-weight:normal;border-bottom:1px solid #e5e5e5; padding:10px 5px; text-align:center; margin:0 3% 3% 3%;}
.speleft ul li{ display:inline-block;color:#666; background:#fff;border:1px solid #e5e5e5;padding:10px 0px;text-align:center; width:90%; float:left; margin:3% 3%; cursor:pointer;}
.speleft ul li.spedq{ background:#03A9F4; color:#fff;border:1px solid #03A9F4;}
.speleft ul li:hover{ background:#03A9F4; color:#fff;border:1px solid #03A9F4; }
.sperigt{margin-left:22%;}
.sperigt h2{ font-size:18px; font-weight:normal; height:42px;padding:0px 5px;border-bottom:1px solid #e5e5e5; position:relative;}
.spetit{ display:inline-block; border-bottom:2px solid #0066cc; position:absolute; left:0; top:0; height:41px; line-height:41px;}
.sperigt ul li{border-bottom:1px solid #e5e5e5;padding:10px 5px;}
.spedate{ float:right; color:#999;}
.w330{width: 330px;}
</style>
</head>

<body>
<div class="speMainer">
	<div class="speleft">
	   <h2>特殊人群</h2>
	   <div style="height:300px;overflow:auto;">
		   <ul id="leftList">
		   </ul>
	   </div>
	</div>
	<div class="sperigt">
	    <h2><span id="title" class="spetit">政策法规</span></h2>
		<div style="height:300px;overflow:auto;">
	        <ul id="rightList">
	   		</ul>
   		</div>
	</div>
</div>
</body>

<script type="text/javascript">

var orgCode = "${orgCode!''}";
var modetype = "${modetype!''}";
var subClassName = "${subClassName!''}";

var tagNameStr = "";
var tagNameArray = new Array();
var tagCodeStr = "";
var tagCodeArray = new Array();

$(function(){
	<#if tagNameList??>
		<#if (tagNameList?size>0)>
			<#list tagNameList as val>
				tagNameStr += "${val}"+",";
			</#list>
		</#if>
	</#if> 
	tagNameArray = tagNameStr.split(",");
	
	<#if tagCodeList??>
		<#if (tagCodeList?size>0)>
			<#list tagCodeList as val>
				tagCodeStr += "${val}"+",";
			</#list>
		</#if>
	</#if> 
	tagCodeArray = tagCodeStr.split(",");
	
	for(var i=0; i<tagNameArray.length-1; i++){
		$("#leftList").append('<li id="'+tagCodeArray[i]+'" onclick="getContent(\''+tagCodeArray[i]+'\');">'+tagNameArray[i]+'</li>');
	}
	
	$(".speleft li:first").addClass("spedq");

	if(tagCodeArray.length<=2){
		getContent(tagCodeArray[0]);
		$(".speleft").hide();
		$(".sperigt").css("marginLeft","2%");
		$(".sperigt").css("marginRight","2%");
	}else if(tagCodeArray.length>2){
		getContent(tagCodeArray[0]);
	}

});

function getContent(code){
	$(".speleft li").removeClass("spedq");
	$("#"+code).addClass("spedq");
	getList(code);
}

function getList(catalogCode){
	if(catalogCode!=""){
		var catalogId = 40000009;
		var subClassPcode = "B913";//政策法规字典
		var status = "2";
		layer.load(0);// 加载遮罩层
		$.ajax({
	        type: "POST",
	        url: "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofpoplocal/releaseList.json",
	        data: {catalogId:catalogId,catalogCode:catalogCode,orgCode:orgCode,subClassPcode:subClassPcode,status:status,page:1,rows:9999},
	        dataType: "json",
	        success: function(data){
	        	layer.closeAll('loading'); // 关闭加载遮罩层
	        	if(data != null){
	        		$("#rightList").empty();
	        		var num = data.total;
	        		if(num>0){
	        			$("#title").text("政策法规-"+data.rows[0].catalogName);
		        		for(var i=0; i<num; i++){
		        			var obj = data.rows[i];
		        			var title = obj.title;
		        			var time = obj.pubDate;
		        			if(title && title.length > 32) {
					            title = title.substr(0, 32); 
					        }
		        			$("#rightList").append('<li><a href="#" onclick="showRowDetail('+obj.infoOpenId+')"><span class="w330">'+title+'</span><span class="spedate">'+time+'</span></a></li>');
		        		}
	        		}else{
						$("#rightList").append('<div id="_nodatapic" style="margin: 30px auto 0; width: 288px;"><img src="${rc.getContextPath()}/images/nodata.png" title="暂无数据"/></div>');
	        		}
	        	}
	        },
			error:function(xhr,status,error) {
				layer.closeAll('loading'); // 关闭加载遮罩层
				//alert("数据请求失败.");
			}
		});
	}else{
		$("#rightList").append('<div id="_nodatapic" style="margin: 30px auto 0; width: 288px;"><img src="${rc.getContextPath()}/images/nodata.png" title="暂无数据"/></div>');
	}
}

function showRowDetail(infoOpenId) {
	var height = 400;
	var width = 850;
	var title = "政策法规详情";
	var url = "${SQ_ZZGRID_URL}/zzgl/grid/knowledgeLibrary/homeShow.jhtml?infoOpenId="+infoOpenId+"&subClassPcode=B913&subClassName="+subClassName+"&modetype=policyLaw";
	window.parent.showMaxJqueryWindow(title,url,width,height); 
}

</script>
</html>

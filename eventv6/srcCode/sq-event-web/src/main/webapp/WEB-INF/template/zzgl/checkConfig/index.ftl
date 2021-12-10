<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>功能配置检测首页</title>
<#include "/component/commonFiles-1.1.ftl" />

<style>
*{margin:0; padding:0; list-style:none;}
.TestList{font-size:18px; font-family:microsoft yahei;}
.FontRed {color:#e84c3d;}/*红色*/
.FontRed a {color:#e84c3d;}
.FontOrange {color:#e77e23;}/*橙色*/
.FontOrange a {color:#e77e23;}
.FontGreen {color:#2dcc70;}/*绿色*/
.FontGreen a {color:#2dcc70;}
.TestList dl{padding:0 20px; border-bottom:1px solid #ccc; line-height:42px;}
.NorSpan{float:right;}
.NorSpan i{display:inline-block; width:16px; height:16px; background-repeat:no-repeat; margin-left:10px; vertical-align:middle; *margin-top:3px;}
.pass i{background-image:url(${rc.getContextPath()}/images/check/success.png);}
.error i{background-image:url(${rc.getContextPath()}/images/check/warning.png);}
.fail i{background-image:url(${rc.getContextPath()}/images/check/fail.png);}
.GreyBack{background:#f5f5f5;}
.arrow{width:14px; height:8px; margin-left:20px;}
.box{border-radius:3px; font-size:16px; line-height:30px; color:#fff; padding:5px 10px; margin-bottom:15px;}
.error .arrow{background:url(${rc.getContextPath()}/images/check/arrow_1.png) no-repeat;}
.error .box{background:#f49645;}
.fail .arrow{background:url(${rc.getContextPath()}/images/check/arrow_2.png) no-repeat;}
.fail .box{background:#ec5749;}
.box b{color:#333;}
</style>
</head>
<body>
<div class="TestList" id = "TestList">
	
</div>

</body>
<script type="text/javascript">
	
    
    $(function(){
    	//$("#TestList").mCustomScrollbar({theme:"minimal-dark"});
    	modleopen();
    	startCheck();
    });
    
    //检测入口
    function startCheck(){
    	$("#TestList").html('');
    	//要检测的服务实现类
    	<#if checkConfigServicesList??>
			<#list checkConfigServicesList as checkConfigServiceName>
				var checkConfigServiceName='${checkConfigServiceName}';
    			doCheck(checkConfigServiceName);
			</#list>
		</#if>
    }
    
    //执行某个检测
    function doCheck(checkConfigServiceName){
    	$.ajax({
    		type : "POST",
    		url : "${rc.getContextPath()}/zhsq/checkConfigController/startCheck.jhtml",
    		data : {checkConfigServiceName : checkConfigServiceName},
			dataType:"json",
    		success: function(data){
    			modleclose();
    			var tableBody="";
			  	if(data.resultCode == "0"){
			  		tableBody += '<dl><dt class="FontGreen"><span class="NorSpan pass">检测通过<i></i></span>'+ data.funcName +'(功能编码：'+ data.funcCode +')检测</dt>';
			  	}else if(data.resultCode == "1" || data.resultCode == "2"){
			  		tableBody += '<dl class="GreyBack">';
			  		if(data.resultCode == "1"){
			  			tableBody += '<dt class="FontRed"><span class="NorSpan fail">检测不通过<i></i></span>'+ data.funcName +'(功能编码：'+ data.funcCode +')检测</dt><dd class="fail"><div class="arrow"></div>';
			  		}
			  		if(data.resultCode == "2"){
			  			tableBody += '<dt class="FontOrange"><span class="NorSpan error">警告<i></i></span>'+ data.funcName +'(功能编码：'+ data.funcCode +')检测</dt><dd class="error"><div class="arrow"></div>';
			  		}
			  		tableBody += '<div class="box"><ul>';
			  		for(var j=0; j < data.reasonDesc.length; j++){
			  			tableBody += '<li><b>Q：'+ data.reasonDesc[j] +'</b></li><li>A：'+ data.solutionDesc[j] +'</li>';
			  		}
			  		tableBody+='</ul></div>';
			  	}	
			  	tableBody+='</dl>';
				$("#TestList").append(tableBody);
    		},
			error:function(data){
				$.messager.alert('错误','连接超时！','error');
			}
    	});
    }
    
    
</script>
</html>
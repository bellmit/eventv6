<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>立体防控——卡口设备——运行数据——图片列表</title>
	<#include "/component/commonFiles-1.1.ftl" />
	
	<style type="text/css">
		.MainContent{padding: 0;}
	</style>

</head>
<body>
	<div id="picBayonetToolbar" class="MainContent">
		<#include "toolbar_bayonetRunData.ftl" />
	</div>
	
    <div class="MetterList" style="overflow-x: hidden;"><!--为了去除IE下的外框滚动条-->
    	<div>
            <div id="content-d" class="MC_con content light">
        		<div id="dayonetRunDataPicDiv"></div>
	        </div>
	        
	        <div id="picBayonetPadding" class="h_10"></div>
	        
	        <div id="picBayonetPagination" style="width: 100%; background: #f4f4f4">
	        	<div class="NorPage" style="width: 100%;">
			        <input type="hidden" id="pageSize" value="20" />
			        <input type="hidden" id="pageNo" value="1" />
			        
		        	<ul>
		            	<li><a href="javascript:change('1');"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/pre.png" /></a></li>
		            	<li style="margin-right: 4px;">共 <span id="pagination-total">0</span>条&nbsp;<span id="pagination-num">0</span>/<span id="pageCount">0</span> 页 转至 </li>
		                <li class="PageInp" style="margin-right: 4px;"><input type="text" id="inputNum" name="inputNum" onkeydown="$onkeydown();" /></li>
		                <li class="PageBtn"><input type="button" value="确定" onclick="pageSubmit()"/></li>
		            	<li><a href="javascript:change('2');"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/next.png" /></a></li>
		            </ul>
		        </div>
	        </div>
	        
        </div>
    </div>

<script type="text/javascript">
	$(function(){
        $(window).load(function(){ 
	        var options = { 
	            axis : "yx", 
	            theme : "minimal-dark" 
	        }; 
	        enableScrollBar('content-d',options); 
	    });
	    
	    searchData();
	    
	    //计算图片列表高度，使得分页信息能够置底
	    $("#content-d").height($(window).height() - $("#picBayonetPagination").outerHeight(true) - $("#picBayonetToolbar").outerHeight(true) - $("#picBayonetPadding").outerHeight());
	});
	
	$(document).ready(function() {//为了去除IE兼容模式下的外框滚动条
		$("#content-d").height($("#content-d").height() - 1);
	});
	
	//图片列表查询列表
	function doSearch(queryParams){
		modleopen();
		
		var pageNo = $("#pageNo").val();
		var pageSize = $("#pageSize").val();
		var picDivHtml = "";
		
		if(pageNo > "1") {
			$("#inputNum").val(pageNo);
		} else {
			$("#inputNum").val("");
		}
		
		if(queryParams) {
			postData = queryParams;
		} else {
			postData = {};
		}
		
		postData["page"] = pageNo;
		postData["rows"] = pageSize;
		
		$.ajax({
			type: "POST",
			url:'${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofgoods/listBayonetRunData.json',
			data: postData,
			dataType:"json",
			success: function(data){
				var list = data.rows;
				
				if(list && list.length > 0) {
					if(pageNo == 0) {
						pageNo = 1;
					}
					
					picDivHtml += '<ul>';
					var picData = null, crossingName = null;
					
					for(var index in list) {
						picData = list[index];
						crossingName = picData.crossingName;
						
						picDivHtml += '<li style="border: solid 1px #ddd; width: 186px; float:left; padding: 2px; margin: 4px; text-align: center;">';
						picDivHtml += 	'<p onclick="showImg(\''+ picData.picVehicle +'\');">';
						picDivHtml += 		'<img style="width: 184px; height: 120px; cursor: pointer;" src="'+ picData.picAbbreviate +'" />';
						picDivHtml +=	"</p>";
						
						picDivHtml +=	'<p style="text-align: center;"';
						if(crossingName && crossingName.length > 15) {
							picDivHtml += ' title="'+ crossingName +'"';
							crossingName = crossingName.substring(0, 15) + '...';
						}
						picDivHtml += '>'+ crossingName +'</p>';
						
						picDivHtml +=	'<p style="text-align: center;">'+ picData.plateInfo +'</p>';
						picDivHtml +=	'<p style="text-align: center;">'+ picData.passTime +'</p>';
						picDivHtml += "</li>";
					}
					
					picDivHtml += "</ul>";
				} else {
					pageNo = 0;
					picDivHtml += '<dl><div class="nodata"></div></dl>';
				}
				
				//设置页面页数
				$('#pagination-num').text(pageNo);
				$("#pageNo").val(pageNo);
				$('#pagination-total').text(data.total);
				var totalPage = Math.ceil(data.total/pageSize);
				$('#pageCount').text(totalPage);
				
				$("#dayonetRunDataPicDiv").html(picDivHtml);
				
				modleclose();
			},
			error:function(data){
				picDivHtml = '<dl><div class="errordata"></div></dl>';
				$("#dayonetRunDataPicDiv").html(picDivHtml);
				modleclose();
			}
		});
	}
	 //分页
     function change(_index){
        var flag;
        var pagenum = $("#pagination-num").text();
        var lastnum = $("#pageCount").text();
		var firstnum = 1;
		
		if(pagenum > 0) {
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
			        pagenum = $("#inputNum").val();
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
			  $.messager.alert('提示', "当前已经是首页", 'info');
			  return;
			}else if(flag==2){
			  $.messager.alert('提示', "当前已经是尾页", 'info');
			  return;
			}
			
			$("#pageNo").val(pagenum);
			
		    searchData();
	    }
	}
	
	function pageSubmit(){
		var inputNum = $("#inputNum").val();
		var pageCount = $("#pageCount").text();
		if(isNaN(inputNum)){
			inputNum=1;
		}
		//防止文本比较，出现3>28的情况，需要转换为数字
		if(inputNum){
			inputNum = parseInt(inputNum, 10);
		}
		if(pageCount){
			pageCount = parseInt(pageCount, 10);
		}
		if(inputNum>pageCount){
			inputNum=pageCount;
		}
		if(inputNum<=0||inputNum==""){
			inputNum=1;
		}
		
		$("#inputNum").val(inputNum);
		
		change('4');
	}
	
	function $onkeydown(){
		var keyCode = event.keyCode;
		if(keyCode == 13){
			pageSubmit();
		}
	}
	
	function resetCondition() {
		$("#pageNo").val("1");
		
		$resetCondition();
	}
	
	function showImg(picUrl) {
		var url = "${rc.getContextPath()}/zhsq/showImage/indexOfPath.jhtml?fieldId=bayonetRunDataImg";
		var picArray = new Array();
		picArray.push(picUrl);
		
		openPostWindow(url, picArray, "图片查看");
		
	}

	function openPostWindow(url, data, name){
		var tempForm = document.createElement("form");
		tempForm.id="tempForm1";
		tempForm.method="post";
		tempForm.action=url;
		tempForm.target=name;
		
		var hideInput = document.createElement("input");
		hideInput.type="hidden";
		hideInput.name= "paths";
		hideInput.value= data;
		
		tempForm.appendChild(hideInput);
		tempForm.submit(function(){
			openWindow(name);
		});
		
		document.body.appendChild(tempForm);
		tempForm.fireEvent("onsubmit");
		tempForm.submit();
		document.body.removeChild(tempForm);
	}

	function openWindow(name){
		window.open('about:blank',name,'height=400, width=400, top=0, left=0, toolbar=yes, menubar=yes, scrollbars=yes, resizable=yes,location=yes, status=yes');
	}
</script>
</body>
</html>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>两站两员隐患路口、隐患路段信息详情</title>
<#include "/component/commonFiles-1.1.ftl" />
<link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css" />
<link href="${rc.getContextPath()}/js/nbspslider-1.0/css/css.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${rc.getContextPath()}/js/nbspslider-1.0/js/jquery.nbspSlider.1.0.min.js" ></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/event/event.js"></script>
<#include "/component/ImageView.ftl" />
</head>

<body onload="checkMetterHeight();">
	<input type="hidden" id="drId" value="<#if dangRoad.drId??>${dangRoad.drId?c}</#if>" />
		
    <div class="MetterList" style="margin:0 auto;">
    	<div id="content-d" class="MC_con content light" style="position:relative;left:0;top:0;overflow-x:hidden;overflow-y:auto"><!--使得事件简介能随着滚动条移动，隐藏横向滚动条-->
        	<div class="MetterContent" style="margin:0 auto;">
	            <div class="title ListShow" style="background:none; padding-right: 0;">
	            	<div id="contentDiv" class="fl" style="width:610px; height: 310px; position: relative;">
	                    
                    	<div id="MetterMore" class="ListShow ListShow2" style="word-break: break-all; border: none;">
                    		<div class="NorForm">
                    			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="line-height: 30px;">
                    				<tr>
		                    			<td align="right" style="width: 85px;">
		                    				<#if dangRoad.drType?? && dangRoad.drType=='1'>
		                    					路口名称：
		                    				<#elseif dangRoad.drType?? && dangRoad.drType=='2'>
		                    					路段名称：
		                    				</#if>
		                    			</td>
		                    			<td><code>${dangRoad.drName!''}</code></td>
		                    		</tr>
		                    		<tr>
		                    			<td align="right">行政区划：</td>
		                    			<td>
		                    				<code>
		                    					<#if dangRoad.infoOrgFullName??>
		                    						${dangRoad.infoOrgFullName}
		                    					<#else>
		                    						${dangRoad.infoOrgName!}
		                    					</#if>
					                    	</code>
		                    			</td>
		                    		</tr>
		                    		<tr>
		                    			<td align="right">
		                    				<#if dangRoad.drType?? && dangRoad.drType=='1'>
		                    					连接道路：
		                    				<#elseif dangRoad.drType?? && dangRoad.drType=='2'>
		                    					所在道路：
		                    				</#if>
		                    			</td>
		                    			<td><code>${dangRoad.roadName!''}</code></td>
		                    		</tr>
		                    		<tr>
		                    			<td align="right">采集人员：</td>
		                    			<td><code>${dangRoad.collPerson!''}<#if dangRoad.collTel??><span title="联系电话" style="padding-left: 10px;"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/dianhua.png" />${dangRoad.collTel}</span></#if></code></td>
		                    		</tr>
		                    		<tr>
		                    			<td align="right">隐患情况描述：</td>
		                    			<td><code>${dangRoad.drDesc!''}</code></td>
		                    		</tr>
		                    		<tr>
		                    			<td align="right">备注：</td>
		                    			<td><code>${dangRoad.remark!''}</code></td>
		                    		</tr>
                    			</table>
                    		</div>
                    	</div>
		                
		                <table width="100%" border="0" cellspacing="0" cellpadding="0" style="line-height: 30px;">
                    		<tr>
		                		<td align="right" style="width: 85px;">
		                			当前状态：
		                		</td>
		                		<td><code><#if dangRoad.status??><span style="padding:5px; border-bottom:1px solid #e2e2e2; color:#fff; background-color:#a2e075; ">${dangRoad.status!}</span></#if></code></td>
		                	</tr>
		                </table>
	                </div>
	                
	                <div id="slider" class="fr" style="width:300px; height:180px; border-left:1px solid #cecece;">
                		<ul></ul>
	                </div>
	            	<div class="clear"></div>
	            </div>
	               	
                <div class="h_20"></div>
                <div class="ConList">
                    <div class="nav" id="tab">
                        <ul>
                        	<li id="01_li" class="current">处理环节</li>
                        </ul>
                    </div>
                    <div class="ListShow ListShow2">
                    	<div id="01_li_div" class="t_a_b_s">
                        	<div id="workflowDetail" border="false"></div>
                        </div>
                    </div>
                </div>
                <div class="h_20"></div>
            </div>
        </div>
    </div>

<script type="text/javascript">
	var downPath = "${downPath!}";
	var contextPath = "${rc.getContextPath()}";
	var _winHeight = 0;
	var _winWidth = 0;
	
	$(function(){
		var drId = $("#drId").val();
		_winHeight = $(window).height();
		_winWidth = $(window).width();
		
		getImages(drId, 'DangRoad');
        
		
		//390为：为图片展示预留的宽度；
		var width = _winWidth - 390;
		var options = { 
            axis : "yx", 
            theme : "minimal-dark" 
        };
        
        $("#contentDiv").width(width);
        
        enableScrollBar('MetterMore',options);
        
		$("#workflowDetail").panel({
			height:'auto',
			width:'auto',
			overflow:'no',
			href: "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/fetchDangerRoadflowDetail.jhtml?drId=" + $("#drId").val(),
			onLoad:function(){//配合detail_workflow.ftl使用
				var workflowDetailWidth = $("#workflowDetail").width() - 10 - 10;//10px分别为左右侧距离
				var maxHandlePersonAndTimeWidth = workflowDetailWidth * 0.4;//人员办理意见的最大宽度，为了使人员信息过长时，办理意见不分行
				var taskListSize = $("#taskListSize").val();	//任务记录数
				var handleTaskNameWidth = 115;		//处理环节总宽度
				var handleLinkWidth = 21;			//办理环节宽度
				var handlePersonAndTimeWidth = 0;	//办理人/办理时间宽度
				var handleRemarkWidth = 0;			//办理意见宽度
				
				for(var index = 0; index < taskListSize; index++){
					handlePersonAndTimeWidth = $("#handlePersonAndTime_"+index).outerWidth();
					
					if(handlePersonAndTimeWidth > maxHandlePersonAndTimeWidth) {
						$("#handlePersonAndTime_"+index).width(maxHandlePersonAndTimeWidth);
						handlePersonAndTimeWidth = $("#handlePersonAndTime_"+index).outerWidth();
					}
					
					handleRemarkWidth = workflowDetailWidth - handleTaskNameWidth - handleLinkWidth - handlePersonAndTimeWidth;
					
					$("#handleRemark_"+index).width(handleRemarkWidth);//办理意见宽度
					
				}
			}
		});
	});
	
	function checkMetterHeight() {//需要在页面渲染完成后，执行，因为"MetterMore > table"的高度会因为折行效果发生变化
		var moreTableDefault = 275;
		var moreTableHeight = $("#MetterMore table").height();//由于添加的滚动条，因此table不再是MetterMore的直接下级
		
		if(moreTableHeight > moreTableDefault) {
			moreTableHeight = moreTableDefault;
		}
		
		$("#MetterMore").height(moreTableHeight);
		
		//显示设置width样式，为了防止菜单页签切换时，宽度被修改
		$("#MetterMore table").width($("#MetterMore").width());
	}
	
	function showMix(fieldId, index){
		<#if !(isOpenInNewWindows?? && isOpenInNewWindows)>
			ffcs_viewImg_win(fieldId, index);
		<#else>
			ffcs_viewImg(fieldId, index);
		</#if>
	}
	
	function openPostWindow(url, data, titles){
		var tempForm = document.createElement("form");
		tempForm.id="tempForm1";
		tempForm.method="post";
		tempForm.action=url;
		tempForm.target="图片查看";
		var hideInput = document.createElement("input");
		hideInput.type="hidden";
		hideInput.name= "paths";
		hideInput.value= data;
		tempForm.appendChild(hideInput);
		var hideInput = document.createElement("input");
		hideInput.type="hidden";
		hideInput.name= "titles";
		hideInput.value= titles;
		tempForm.appendChild(hideInput);
		tempForm.submit(function(){
			openWindow("图片查看");
		});
// 		tempForm.attachEvent("onsubmit",function(){
// 			openWindow(name);
// 		});
		document.body.appendChild(tempForm);
//		tempForm.fireEvent("onsubmit");
		tempForm.submit();
		document.body.removeChild(tempForm);
	}

	function openWindow(name){
		window.open('about:blank',name,'height=400, width=400, top=0, left=0, toolbar=yes, menubar=yes, scrollbars=yes, resizable=yes,location=yes, status=yes');
	}
</script>
</body>
</html>

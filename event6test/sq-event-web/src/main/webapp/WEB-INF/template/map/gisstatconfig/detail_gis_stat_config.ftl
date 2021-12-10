<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>周边框选配置详情</title>
<#include "/component/commonFiles-1.1.ftl" />
<link href="${uiDomain!''}/css/style.css" rel="stylesheet" type="text/css" />
</head>

<body>
	<form>
		<input type="hidden" id="statCfgId" name="statCfgId" value="<#if gisStatConfig??>${gisStatConfig.statCfgId!''}</#if>">
		<div class="NorForm">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
		    		<td class="LeftTd" style="width:340px;">
				        <label class="LabName"><span>所属网格：</span></label>
				        <div class="Check_Radio FontDarkBlue">
				        	<#if gisStatConfig??>${gisStatConfig.gridName!''}</#if>
				        </div>
		    		</td>
					<td class="LeftTd">
						<label class="LabName"><span>地图首页：</span></label>
						<div class="Check_Radio FontDarkBlue">
							<#if gisStatConfig??>${gisStatConfig.bizType!''}</#if>
						</div>
					</td>
				</tr>
			</table>
		</div>
			
		<div class="catebox">	
			<div class="catetit">
		    	已经选择资源统计的对象：
		  	</div>
		  	<div class="clearfloat"></div>
		  	<div id="content-d" class="renTit MC_con content light" style="height:225px;">
		    	<div id="categories" class="ren-item">
		    	</div><!---end .ren-item---->
				<div class="clearfloat"></div>
			</div>
		  	<div class="clearfloat"></div>
		</div>
  		<div class="BigTool">
        	<div class="BtnList">
				<a href="#" onclick="cancel();" class="BigNorToolBtn CancelBtn">关闭</a>
            </div>
        </div>
	</form>
	
	<script type="text/javascript">
		$(function(){
		    $(window).load(function(){ 
		        var options = { 
		            axis : "yx", 
		            theme : "minimal-dark" 
		        }; 
		        enableScrollBar('content-d',options); 
		        
		        $("#content-d").height(225);
		    }); 
		    
		    getGisStatConfigs();
	    });
	    
	    
	    // 获取地图框选可配置
		function getGisStatConfigs() {
			var categories,smallCategories,smallCategory,gisDataCfg;
			var tableBody = "";
			var navsCount = 0;
			var tabsCount = 0;
			var category_;
			
			$.ajax({   
				 url: '${rc.getContextPath()}/zhsq/map/gisStatConfig/getGisStatConfigsById.json?statCfgId=' + $('#statCfgId').val() + '&t='+Math.random(),
				 type: 'POST',
				 timeout: 3000,
				 dataType:"json",
				 async: false,
				 error: function(data){
				 	$.messager.alert('友情提示','地图框选配置信息获取出现异常!','warning');
				 },
				 success: function(data){
				    categories = data;
				    
			    	// 小类
				    for (var key in categories) {
				    	smallCategories = categories[key];
				    	
				    	tableBody += '<div id=\''+key+'\' class="ren-name">';
			    		tableBody += '<a href="#">';
			    		tableBody += '<span title=\''+key+'\'>' + key.substring(0,4) + '</span>';
			    		tableBody += '</a>';
			    		tableBody += '</div>';
				    	tableBody += '<div class="ren-con">';
				    	
				    	for (var i = 0, j = smallCategories.length; i < j; i++) {
				    		smallCategory = smallCategories[i];
				    		gisDataCfg = smallCategory.gisDataCfg;
				    		
				    		if (gisDataCfg != null) {
				    			objClass = smallCategory.objClass;
				    		
					    		if (objClass == null) {
					    			objClass = '';
					    		}
					    		
					    		tableBody += '<div name=\''+objClass+'\' id=\''+gisDataCfg.gdcId+'\' class="r-nav-item">';
					    		tableBody += '<a href="#">';
					    		tableBody += '<span title=\''+smallCategory.statObjName+'\'>' + smallCategory.statObjName.substring(0,6) + '</span>';
					    		tableBody += '</a>';
					    		tableBody += '</div>';
				    		}
				    	}
				    	
				    	tableBody += '</div>';
				    	tableBody += '<div class="clearfloat"></div>';
				    }
				    
				    $("#categories").html(tableBody);
				 }
			 });
		}
	    
		function cancel(){
			parent.closeMaxJqueryWindow();
		}
	</script>
</body>
</html>

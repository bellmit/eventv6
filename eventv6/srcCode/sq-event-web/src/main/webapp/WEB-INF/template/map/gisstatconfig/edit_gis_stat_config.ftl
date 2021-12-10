<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>周边框选配置新增</title>
<link href="${uiDomain!''}/css/style.css" rel="stylesheet" type="text/css" />
<#include "/component/commonFiles-1.1.ftl" />
<#include "/component/ComboBox.ftl" />
<script src="${uiDomain!''}/js/jquery.dragsort-0.5.2.min.js" type="text/javascript"></script>
<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<style type="text/css">
	.popupcontent{z-index:9; position:absolute; color:#5e5e5e;display:none;}
	.popupcontent .box{width:732px;height:354px;background:#fff; border:1px solid #7d7d7d; z-index:3; position:relative; overflow:hidden;}
	.popupcontent .title{background:#eee; height:26px; line-height:26px; font-weight:bold; color:#5e5e5e; padding-left:10px;}
	.popupcontent .title span{cursor:pointer; display:block; width:28px; height:26px;}
	.popupcontent .close{background:url(${uiDomain}/images/map/gisv0/special_config/images/earth_04.png);}
	.popupcontent .close:hover{background:url(${uiDomain}/images/map/gisv0/special_config/images/earth_05.png);}
	.popupcontent .watch{background:url(${uiDomain}/images/map/gisv0/special_config/images/earth_03.png);}
	.popupcontent .watch:hover{background:url(${uiDomain}/images/map/gisv0/special_config/images/earth_02.png);}
	.popupcontent .arrow{background:url(../images/NorMapOpenDivArrow.png); position:absolute; z-index:4; bottom:-50px; width:64px; height:51px;}
	.popupcontent .shadow{background:url(../images/NorMapOpenDivShadow.png); width:406px; height:156px; position:absolute; z-index:2; bottom: -52px; left: 25px;}
	
	.popupcontent .box .ren-item span{width: 107px; margin:0 5px 10px 5px; float:left; border:1px solid #c1c1c1; background:#fff; height:24px; line-height:24px; text-align:center; color:#333; cursor:pointer;}
	.popupcontent .box .ren-item span:hover{width:105px; height:22px; border:2px solid #80c269; line-height:22px;}
	.popupcontent .box .ren-item span.current{width: 105px; height:22px; border:2px solid #80c269; line-height:22px; background:url(${uiDomain}/images/map/gisv0/special_config/images/gou.png) no-repeat #fff right bottom;}
	
	.del {float:left;margin-top:3px;}
	.edit {float:right:margin-top:1px;}
	
	.placeHolder { border: 0; margin-right: 10px; }
	.placeHolder div { width:100%; height:27px; border:dashed 1px gray; }
</style>

</head>

<body>
    <div id="popupcontent" class="popupcontent">
		<div class="box">
	    	<div class="title"><span class="fr close" onclick="closePopup();"></span>选择您想要添加的内容</div>
	    	<div class="catebox">
			  	<div id="" class="renTit" style="height:267px;overflow:auto;">
			    	<div id="menus" class="ren-item">
			    	</div><!---end .ren-item---->
					<div class="clearfloat"></div>
				</div>
				<div class="clearfloat"></div>
		    	<div id="bottom" style="padding:9px 0 0 276px;height:40px;background:#f4f4f4;">
			    	<a href="#" onclick="menuAdd();" class="BigNorToolBtn BigJieAnBtn" style="display:inline-block;">确定</a>
			    	<a href="#" onclick="menuCancel();" class="BigNorToolBtn CancelBtn" style="display:inline-block;">关闭</a>
		    	</div>
			  	<div class="clearfloat"></div>
			</div>
	    </div>
	    <!--<div class="shadow"></div>-->
	</div>
	
	<form id="tableForm" name="tableForm" action=""  method="post" enctype="multipart/form-data">	
		<input type="hidden" id="status" name="status" value="<#if gisStatConfig??>${gisStatConfig.status!''}</#if>">
		<input type="hidden" id="statType" name="statType" value="<#if statType??>${statType!''}</#if>">
		<input type="hidden" id="statCfgId" name="statCfgId" value="<#if gisStatConfig??>${gisStatConfig.statCfgId!''}</#if>">
		<input type="hidden" id="categoriesValue" name="categories" value="<#if gisStatConfig??>${gisStatConfig.categories!''}</#if>">
		<input type="hidden" name="gisStatContConfigs" id="gisStatContConfigs" value="" />
		
		<div class="NorForm">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
		    		<td class="LeftTd">
				        <label class="LabName"><span>所属网格：</span></label>
						<input type="hidden" id="regionCode" name="regionCode" value="<#if gisStatConfig.regionCode??>${gisStatConfig.regionCode}<#else>-1</#if>"/>
						<input type="text" class="inp1 easyui-validatebox" data-options="tipPosition:'bottom',validType:['maxLength[32]']" name="gridName" id="gridName" value="${gisStatConfig.gridName!''}" />
		    		</td>
					<td class="LeftTd">
						<label class="LabName"><span>地图首页：</span></label>
						<input id="bizType" name="bizType" type="hidden" value="<#if gisStatConfig??>${gisStatConfig.bizType!''}</#if>"/>
						<input id="bizName" name="bizName" type="text" class="inp1 InpDisable" style="width:150px;" value="<#if gisStatConfig??>${gisStatConfig.bizName!''}</#if>"/>
					</td>
				</tr>
			</table>
		</div>
		
		<div class="catebox">	
			<div class="catetit">
		    	请选择希望进行<#if statType == '0'>框选<#else>周边</#if>资源统计的对象：
		    	<div class="edit-tool">
		      		<div id="addCategory" class="tool-item" style="display:none;">
		        		<input id="categroy" name="categroy" type="text" placeholder="快速创建分类" maxlength="19" class="tool-add-dir" />
		        		<a onclick="addCategory();" href="#" class="dir-enter">添加分类</a>
		      		</div>
		      		<div class="tool-item">
		      			<input id="classification" type="hidden" name="classification" />
		      			<input id="classificationCbx" onclick="classifiy();" style="vertical-align:middle;" type="checkbox" name="classificationCbx" <#if gisStatConfig.classification??><#if gisStatConfig.classification == '1'>checked</#if></#if> />
		      			<label>分类显示 </label>
		      		</div>
		    	</div>
		  	</div>
		  	<div class="clearfloat"></div>
		  	<div id="content-d" class="renTit MC_con content light" style="height:225px;">
		    	<ul id="categories" class="ren-item">
		    	</ul><!---end .ren-item---->
				<div class="clearfloat"></div>
			</div>
		  	<div class="clearfloat"></div>
		</div>
		
		<div class="BigTool">
	    	<div class="BtnList">
				<a href="#" onclick="tableSubmit();" class="BigNorToolBtn SubmitBtn">保存</a>
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
		    
			AnoleApi.initGridZtreeComboBox("gridName", null, function(regionCode, items) {
				if(items!=undefined && items!=null && items.length>0){
					var grid = items[0];
					$("#regionCode").val(grid.gridCode);
				} 
			},{
				OnCleared : function() {
					$("#regionCode").val("-1");
				},
				ShowOptions : {
					EnableToolbar : true
				}
			});
			
			AnoleApi.initListComboBox("bizName", "bizType", "B559", function(){
				
			}, [<#if gisStatConfig.bizType??>"${gisStatConfig.bizType}"<#else>"ARCGIS_STANDARD_HOME"</#if>]);
			
			var statCfgId = $('#statCfgId').val();
			
			// 编辑 
			if (statCfgId > 0) {
				getGisStatConfigs();
				$("#gridName").attr("disabled", "disabled");
				$("#bizName").attr("disabled", "disabled");
			} else { // 新增
				init();
			}
			
			// 分类
			if (isClassification()) {
				$("#addCategory").show();
			} else {
				$("#addCategory").hide();
			}
			
			$("ul").dragsort();
			
			$(".ren-con").dragsort({
				dragSelector: "div", 
				dragBetween: true,
				placeHolderTemplate: "<div class='placeHolder' style='float:left;'><div></div></div>"
			});
	    });
	    
	    // 按住标签页禁止li拖动事件
	    $('.ren-con').live('mouseenter',function(){
			$("ul").dragsort("destroy");
		}).live('mouseleave',function(){
			$("ul").dragsort();
		});
	    
		// 获取地图框选可配置
		function getGisStatConfigs(param) {
			var categories,smallCategories,smallCategory,gisDataCfg,objClass;
			var tableBody = "";
			
			$.ajax({   
				 url: '${rc.getContextPath()}/zhsq/map/gisStatConfig/getGisStatConfigsById.json?statCfgId=' + $('#statCfgId').val() + '&t='+Math.random(),
				 type: 'POST',
				 timeout: 3000,
				 dataType:"json",
				 data:param,
				 async:false,
				 error:function(data){
				 	$.messager.alert('友情提示','地图框选配置信息获取出现异常!','warning');
				 },
				 success:function(data){
				    categories = data;
				    
			    	// 小类
				    for (var key in categories) {
				    	smallCategories = categories[key];
				    	
			    		tableBody += '<li>';
			    		tableBody += '<div class="ren">';
			    		tableBody += '<div id=\''+key+'\' class="ren-name">';
			    		tableBody += '<a href="#">';
			    		tableBody += '<span onclick="delObjClass(this);" class="nav-del del" style="display:none;"></span>';
			    		tableBody += '<span title=\''+key+'\'>' + key.substring(0,4) + '</span>';
			    		tableBody += '<span onclick="editObjClass(this);" class="nav-item-edit edit" style="display:none;"></span>';
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
					    		tableBody += '<span onclick="del(this);" class="nav-del"></span>';
					    		tableBody += '<span title=\''+smallCategory.statObjName+'\'>' + smallCategory.statObjName.substring(0,6) + '</span>';
					    		tableBody += '<span onclick="edit(this);" class="nav-item-edit" style="display:none;"></span>';
					    		tableBody += '</a>';
					    		tableBody += '</div>';
				    		}
				    	}
				    	
				    	tableBody += '<div></div>';
				    	tableBody += '</div>';
				    	tableBody += '<div class="r-nav-add">';
				    	tableBody += '<a onclick="addItem(this);">+添加</a>';
				    	tableBody += '</div>';
				    	tableBody += '<div class="clearfloat"></div>';
				    	tableBody += '</div>';
				    	tableBody += '</li>';
				    }
				    
				    $("#categories").html(tableBody);
				 }
			 });
		}
		
		function init(category) {
			var tableBody = "";
			
			if (category != undefined) {
				tableBody += '<li>';
				tableBody += '<div class="ren">';
				tableBody += '<div id=\''+category+'\' class="ren-name">';
	    		tableBody += '<a href="#">';
	    		tableBody += '<span onclick="delObjClass(this);" class="nav-del del" style="display:none"></span>';
	    		tableBody += '<span title=\''+category+'\'>' + category + '</span>';
	    		tableBody += '<span onclick="editObjClass(this);" class="nav-item-edit edit" style="display:none;"></span>';
	    		tableBody += '</a>';
	    		tableBody += '</div>';
			} else {
				tableBody += '<li>';
				tableBody += '<div>';
				tableBody += '<div class="ren-name"></div>';
			}
			
	    	tableBody += '<div class="ren-con">';
	    	tableBody += '<div></div>';
	    	tableBody += '</div>';
	    	tableBody += '<div class="r-nav-add add">';
	    	tableBody += '<a onclick="addItem(this);">+添加</a>';
	    	tableBody += '</div>';
	    	tableBody += '<div class="clearfloat"></div>';
	    	tableBody += '</div>';
	    	tableBody += '</li>';
	    	
	    	if (category != undefined) {
	    		$("#categories").append(tableBody);
	    	} else {
	    		$("#categories").html(tableBody);
	    	}
		}
		
		// 是否分类
		function classifiy() {
			var tableBody = "";
			var categoriesValue = $("#categoriesValue").val();
			getGisStatContConfigs();
			var gisStatContConfigs = $("#gisStatContConfigs").val();
			var gisStatContConfigsJSON = eval("("+gisStatContConfigs+")"); 
			
			if (isClassification()) {
				for (var i = 0; i < gisStatContConfigsJSON.length; i++) {
					var	objClass = gisStatContConfigsJSON[i].objClass;
					
					if (objClass == "未分类") {
						categoriesValue = "未分类," + categoriesValue;
						break;
					}
				}
			}
			
			if (isClassification()) {
				data = 'classification=1';
				$("#addCategory").show();
				
				// 分类
				var catesArray = categoriesValue.split(",");
				
				for (var i = 0; i < catesArray.length; i++) {
					var key = catesArray[i];
					if (key != "") {
						tableBody += '<li>';
						tableBody += '<div class="ren">';
						tableBody += '<div id=\''+key+'\' class="ren-name">';
						tableBody += '<a href="#">';
						tableBody += '<span onclick="delObjClass(this);" class="nav-del del" style="display:none;"></span>';
						tableBody += '<span title=\''+key+'\'>' + key.substring(0,4) + '</span>';
						tableBody += '<span onclick="editObjClass(this);" class="nav-item-edit edit" style="display:none;"></span>';
						tableBody += '</a>';
						tableBody += '</div>';
						tableBody += '<div class="ren-con">';
						tableBody += '<div></div>';
						tableBody += '</div>';
						tableBody += '<div class="r-nav-add">';
						tableBody += '<a onclick="addItem(this);">+添加</a>';
						tableBody += '</div>';
						tableBody += '<div class="clearfloat"></div>';
						tableBody += '</div>';
						tableBody += '</li>';
					}
				}
				
				$("#categories").html(tableBody);
				
				// 添加子元素
				for (var j = 0; j < gisStatContConfigsJSON.length; j++) {
					tableBody = "";
					var gisStatContConfig = gisStatContConfigsJSON[j];
					var statObjName = gisStatContConfig.statObjName;
					var layerCfgId = gisStatContConfig.layerCfgId;
					var objClass = gisStatContConfig.objClass;
					
					tableBody += '<div name=\''+objClass+'\' id=\''+layerCfgId+'\' class="r-nav-item">';
		    		tableBody += '<a href="#">';
		    		tableBody += '<span onclick="del(this);" class="nav-del"></span>';
		    		tableBody += '<span title=\''+statObjName+'\'>' + statObjName.substring(0,6) + '</span>';
		    		tableBody += '<span onclick="edit(this);" class="nav-item-edit" style="display:none;"></span>';
		    		tableBody += '</a>';
		    		tableBody += '</div>';
		    		tableBody += '';
		    		
		    		//var $obj = $("div[id='"+objClass+"']");
		    		$("div[id='"+objClass+"']").next().find("div:last").before(tableBody);
				}
			} else {
				var statCfgId = $('#statCfgId').val();
				$("#addCategory").hide();
				
				var objClass = "";
				
				tableBody += '<li>';
	    		tableBody += '<div class="ren">';
	    		tableBody += '<div id=\''+objClass+'\' class="ren-name">';
	    		tableBody += '<a href="#">';
	    		tableBody += '<span onclick="delObjClass(this);" class="nav-del del" style="display:none;"></span>';
	    		tableBody += '<span title=\''+objClass+'\'>' + objClass + '</span>';
	    		tableBody += '<span onclick="editObjClass(this);" class="nav-item-edit edit" style="display:none;"></span>';
	    		tableBody += '</a>';
	    		tableBody += '</div>';
	    		tableBody += '<div class="ren-con">';
			
				for (var i = 0; i < gisStatContConfigsJSON.length; i++) {
					var gisStatContConfig = gisStatContConfigsJSON[i];
					var statObjName = gisStatContConfig.statObjName;
					var layerCfgId = gisStatContConfig.layerCfgId;
					
					objClass = gisStatContConfig.objClass;
					if (objClass == null) {
						objClass = '';
					}
					
		    		tableBody += '<div name=\''+objClass+'\' id=\''+layerCfgId+'\' class="r-nav-item">';
		    		tableBody += '<a href="#">';
		    		tableBody += '<span onclick="del(this);" class="nav-del"></span>';
		    		tableBody += '<span title=\''+statObjName+'\'>' + statObjName.substring(0,6) + '</span>';
		    		tableBody += '<span onclick="edit(this);" class="nav-item-edit" style="display:none;"></span>';
		    		tableBody += '</a>';
		    		tableBody += '</div>';
	    		
		    	}
		    	
		    	tableBody += '<div></div>';
		    	tableBody += '</div>';
		    	tableBody += '<div class="r-nav-add">';
		    	tableBody += '<a onclick="addItem(this);">+添加</a>';
		    	tableBody += '</div>';
		    	tableBody += '<div class="clearfloat"></div>';
		    	tableBody += '</div>';
		    	tableBody += '</li>';
				$("#categories").html(tableBody);
			}
			
			$("ul").dragsort();
			
			$(".ren-con").dragsort("destroy");
			
			$(".ren-con").dragsort({
				dragSelector: "div", 
				dragBetween: true,
				placeHolderTemplate: "<div class='placeHolder' style='float:left;'><div></div></div>"
			});
			
			return;
		
			var data;
			
			// 选中分类
			if (isClassification()) {
				data = 'classification=1';
				$("#addCategory").show();
			} else {
				var statCfgId = $('#statCfgId').val();
				$("#addCategory").hide();
				
				if (statCfgId > 0) {
					data = 'classification=0';	
				} else {
					init();
					return;
				}
			}
			
			getGisStatConfigs(data);
			
			$("ul").dragsort();
			
			$(".ren-con").dragsort("destroy");
			
			$(".ren-con").dragsort({
				dragSelector: "div", 
				dragBetween: true,
				placeHolderTemplate: "<div class='placeHolder' style='float:left;'><div></div></div>"
			});
		}
		
		// 添加分类
		function addCategory() {
			var categroy = $("#categroy").val().trim();
			
			if (categroy == "") {
				$.messager.alert('提示信息','分类不能为空!','info');
				return;
			}
			
			// 是否已经存在该分类
			var categories = $("#categories .ren .ren-name");
			var $category;
			var objName;
			var allowedAdd = true;
			
			for (var i = 0; i < categories.length; i++) {
				$category = $(categories[i]);
				objName = $category.attr("id");
				
				if (categroy == objName) {
					allowedAdd = false;
					$.messager.alert('提示信息','该分类已存在!','info');
					break;
				}
			}
			
			if (allowedAdd) {
				init(categroy);			
			}
			
			$(".ren-con").dragsort("destroy");
			
			$(".ren-con").dragsort({
				dragSelector: "div", 
				dragBetween: true,
				placeHolderTemplate: "<div class='placeHolder' style='float:left;'><div></div></div>"
			});
			
			//$("ul").dragsort("destroy");
			//$("ul").dragsort();
		}
		
		// 移动到标签页上时显示编辑图标(小类)
		$('.r-nav-item').live('mouseenter',function(){
			$(this).find('.nav-item-edit').css('display', 'block');
		}).live('mouseleave',function(){
			$(this).find('.nav-item-edit').css('display', 'none');
		});
		
		// 移动到标签页上时显示编辑图标(大类)
		$('.ren-name').live('mouseenter',function(){
			$(this).find('.nav-del').css('display', 'block');
			$(this).find('.nav-item-edit').css('display', 'block');
		}).live('mouseleave',function(){
			$(this).find('.nav-del').css('display', 'none');
			$(this).find('.nav-item-edit').css('display', 'none');
		});
		
		// 点击添加按钮加载框选周边配置信息
		var $this;// 添加按钮元素
		
		function addItem(obj) {
			$.blockUI({message: "加载中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'40%',
    		background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});	
			
			$("#popupcontent").show();
		
			$this = $(obj).parent();
			var tableBody = "";
			
			$.ajax({   					
				 url: '${rc.getContextPath()}/zhsq/map/menuconfigure/menuConfig/getGisDataCfgs.json?homePageType='+$("#bizType").val()+'&t='+Math.random(),
				 type: 'POST',
				 dataType:"json",
				 async: true,
				 error: function(data){
				 	$.messager.alert('友情提示','地图配置信息获取出现异常!','warning');
				 },
				 success: function(data){
				 	$.unblockUI();
				    var gisDataCfgs = eval(data.gisDataCfgs);
				    
				    // 查找分类下面所有gdcId
					var catagoriesByObjClass = $("#categories .r-nav-item");
					var catagoriesByObjClassGdcIds = new Array();
					
					if (catagoriesByObjClass != null) {
						for (var k = 0; k<catagoriesByObjClass.length; k++) {
							catagoriesByObjClassGdcIds.push(parseInt(catagoriesByObjClass[k].id));
						}
					}
				    catagoriesByObjClassGdcIds = catagoriesByObjClassGdcIds.join(",");
				    if(gisDataCfgs != null){
					    var gisDataCfg = gisDataCfgs;
					    
					    for(var i=0; i<gisDataCfg.length; i++){
					    	tableBody += '<div class="ren-name">' + gisDataCfg[i].menuName + '</div>';
				    		tableBody += '<div style="float:left;width:600px;">';
					    	
				    		var descGisDataCfgs = gisDataCfg[i].childrenList;
				    		
				    		for(var j=0;j<descGisDataCfgs.length;j++){
				    			if ($("#statType").val() == '0') {
				    				if(descGisDataCfgs[j].kuangxuanName != null){
					    				if (catagoriesByObjClassGdcIds.indexOf(descGisDataCfgs[j].gdcId) == -1) { // 是否已选择
						    				tableBody += '<div class="r-nav-item">';
								    		tableBody += '<span id=\''+descGisDataCfgs[j].gdcId+'\' title=\''+descGisDataCfgs[j].menuName+'\' onclick="choose(this);">' + descGisDataCfgs[j].menuName + '</span>';
								    		tableBody += '</div>';
					    				}
				    				}
				    			} else {
				    				if(descGisDataCfgs[j].zhoubianName != null){
					    				if (catagoriesByObjClassGdcIds.indexOf(descGisDataCfgs[j].gdcId) == -1) { // 是否已选择
						    				tableBody += '<div class="r-nav-item">';
								    		tableBody += '<span id=\''+descGisDataCfgs[j].gdcId+'\' title=\''+descGisDataCfgs[j].menuName+'\' onclick="choose(this);">' + descGisDataCfgs[j].menuName + '</span>';
								    		tableBody += '</div>';
					    				}
				    				}
				    			}
				    		}
					    	
					    	tableBody += '<div class="r-nav-add">';
					    	tableBody += '<span href="#" onclick="chooseAll(this);" style="color:#39a1e8;">全选</span>';
					    	tableBody += '</div>';
					    	tableBody += '</div>';
					    	tableBody += '<div class="clearfloat"></div>';
					    }
					    
					    $("#menus").html(tableBody);
				    }
				 }
			 });
		}
		
		// 删除
		function del(obj) {
			$(obj).parent().parent().remove();
		}
		
		// 删除大类
		function delObjClass(obj) {
			var small_categories = $(obj).parent().parent().next().children();
			
			if (small_categories.length > 0) {
				$.messager.confirm('提示', '删除大类将删除该类下所有图层配置？', function(r){
					if (r){
						for (var j = 0; j < small_categories.length; j++) {
							var cate = small_categories[j];
							var $cate = $(cate);
							$cate.remove();
						}
					
						$(obj).parent().parent().parent().parent().remove();
					}
				});
			}
		}
		
		// 编辑		
		function edit(obj) {
			var parent = $(obj).parent().parent();
			var $parent = $(parent);
			var id = $parent.attr("id");
			var value = $(obj).prev().attr("title");
			$parent.html('<input id=\''+id+'\' type="text" class="inp1" style="width:107px;" value=\''+value+'\' onblur="confirm(this);" />');
			$("input[id=" + id + "]").focus();
		}
		
		// 编辑大类
		function editObjClass(obj) {
			var parent = $(obj).parent().parent();
			var $parent = $(parent);
			var id = $parent.attr("id");
			var value = $(obj).prev().attr("title");
			$parent.html('<input id=\''+id+'\' type="text" class="inp1" style="width:66px;" value=\''+value+'\' onblur="confirmObjClass(this);" />');
			$("input[id=" + id + "]").focus();
		}
		
		// 编辑确认
		function confirm(obj) {
			var defaultValue = obj.defaultValue; // 原始值
			var $obj = $(obj);
			
			if ($obj.attr("value") == "") {
				$obj.val(defaultValue);
				$obj.focus();
				$.messager.alert('提示信息','不能为空!','info');
				return;
			}
			
    		var tableBody = '';
    		tableBody += '<a href="#">';
    		tableBody += '<span onclick="del(this);" class="nav-del"></span>';
    		tableBody += '<span title=\''+$obj.attr("value")+'\'>' + $obj.attr("value") + '</span>';
    		tableBody += '<span onclick="edit(this);" class="nav-item-edit" style="display:none;"></span>';
    		tableBody += '</a>';
    		tableBody += '';
    		$obj.before(tableBody);
    		$obj.remove();
		}
		
		// 大类
		function confirmObjClass(obj) {
			var value = obj.value;
			var defaultValue = obj.defaultValue; // 原始值
			var $obj = $(obj);
			
			if (value == "") {
				$obj.val(defaultValue);
				$obj.focus();
				$.messager.alert('提示信息','不能为空!','info');
				return;
			}
			
			// 是否已经存在该分类
			var categories = $("#categories .ren .ren-name");
			var $category;
			var objName;
			var allowedAdd = true;
			
			if (value != defaultValue) {
				for (var i = 0; i < categories.length; i++) {
					$category = $(categories[i]);
					objName = $category.attr("id");
					
					if (value == objName) {
						allowedAdd = false;
						$obj.val(defaultValue);
						$obj.focus();
						$.messager.alert('提示信息','该分类已存在!','info');
						break;
					}
				}
			}
			
			/*
			// 是否已经存在该分类
			var categories = $(".ren-name");
			var objName;
			var allowedAdd = true;
			
			for (var i = 0; i < categories.length; i++) {
				objName = categories[i].innerText;
				
				if (value == objName) {
					allowedAdd = false;
					$obj.val(defaultValue);
					$obj.focus();
					$.messager.alert('提示信息','该分类已存在!','info');
					return;
				}
			}
			*/
			
			if (allowedAdd) {
				$obj.parent().attr("id", value);
	    		var tableBody = '';
	    		tableBody += '<a href="#">';
	    		tableBody += '<span onclick="delObjClass(this);" class="nav-del del" style="display:none;"></span>';
	    		tableBody += '<span title=\''+$obj.attr("value")+'\'>' + $obj.attr("value") + '</span>';
	    		tableBody += '<span onclick="editObjClass(this);" class="nav-item-edit edit" style="display:none;"></span>';
	    		tableBody += '</a>';
	    		tableBody += '';
	    		$obj.before(tableBody);
	    		$obj.remove();
			}
		}
		
		// 选中添加到对应大类
		function choose(obj) {
			var $obj = $(obj);
			var isAllSelected = true;
			
			// 添加按钮
			var add = $(obj).parent().parent().find(".r-nav-add span");
			var $add = $(add);
			
			if($obj.hasClass("current")) {
				isAllSelected = false;
				$obj.removeClass("current");
			}else {
				$obj.addClass("current");
			}
			
			var items = $(obj).parent().parent().find(".r-nav-item span");
			var item;
			
			if (items != null && items.length > 0) {
				for (var i = 0; i < items.length; i++) {
					item = items[i];
					$item = $(item);
					
					if (!($item.hasClass("current"))) {
						isAllSelected = false;
						break;
					}
				}
			}
			
			if (isAllSelected) {
				$add.addClass("current");
			} else {
				$add.removeClass("current");
			}
			
			/*
			var $obj = $(obj);
			
			var tableBody = "";
			
			if($obj.hasClass("current")) {
				$this.parent().find("#" + obj.id);
				$this.parent().find("#" + obj.id).remove();
				$obj.removeClass("current");
			}else {
				$obj.addClass("current");
				tableBody += '<div id=\''+obj.id+'\' class="r-nav-item">';
	    		tableBody += '<a href="#">';
	    		tableBody += '<span onclick="del(this);" class="nav-del"></span>';
	    		tableBody += '<span title=\''+obj.innerHTML+'\'>' + obj.innerHTML + '</span>';
	    		tableBody += '<span onclick="edit(this);" class="nav-item-edit" style="display:none;"></span>';
	    		tableBody += '</a>';
	    		tableBody += '</div>';
	    		$this.prev().find("div:last").after(tableBody);
			}
			*/
			
			$(".ren-con").dragsort("destroy");
			
			$(".ren-con").dragsort({
				dragSelector: "div", 
				dragBetween: true,
				placeHolderTemplate: "<div class='placeHolder' style='float:left;'><div></div></div>"
			});
		}
		
		// 确认添加
		function menuAdd() {
			var catagoriesByObjClass = $this.parent().parent().siblings().find(".r-nav-item");
			var catagoriesByObjClassGdcIds = [];
			
			if (catagoriesByObjClass != null && catagoriesByObjClass.length > 0) {
				for (var k = 0; k<catagoriesByObjClass.length; k++) {
					catagoriesByObjClassGdcIds.push(parseInt(catagoriesByObjClass[k].id));
				}
			}
			
			//$this.prev().children().remove();
		
			var items = $("#menus .r-nav-item span");
			var item, $item, name;
			var tableBody = "";
			
			if (items != null && items.length > 0) {
				for (var i = 0; i < items.length; i++) {
					item = items[i];
					$item = $(item);
					name = $item.attr("name");
					
					if (name == undefined) {
						name = "未分类";
					}
					
					if ($item.hasClass("current")) {
						tableBody += '<div name=\''+name+'\' id=\''+item.id+'\' class="r-nav-item">';
			    		tableBody += '<a href="#">';
			    		tableBody += '<span onclick="del(this);" class="nav-del"></span>';
			    		tableBody += '<span title=\''+item.innerHTML+'\'>' + item.innerHTML + '</span>';
			    		tableBody += '<span onclick="edit(this);" class="nav-item-edit" style="display:none;"></span>';
			    		tableBody += '</a>';
			    		tableBody += '</div>';
					}
				}
				
				//$this.prev().html(tableBody);
				$this.prev().find("div:last").after(tableBody);
			}
			
			closePopup();
			
			$(".ren-con").dragsort("destroy");
			
			$(".ren-con").dragsort({
				dragSelector: "div", 
				dragBetween: true,
				placeHolderTemplate: "<div class='placeHolder' style='float:left;'><div></div></div>"
			});
		}
		
		// 全选
		function chooseAll(obj) {
			var $obj = $(obj);
			var choose = $obj.hasClass("current");
			
			if(choose) {
				$obj.removeClass("current");
			}else {
				$obj.addClass("current");
			}
			
			var items = $(obj).parent().parent().find(".r-nav-item span");
			var item;
			
			if (items != null && items.length > 0) {
				for (var i = 0; i < items.length; i++) {
					item = items[i];
					$item = $(item);
					
					if (choose) {
						$item.removeClass("current");
					} else {
						$item.addClass("current");
					}
				}
			}
		}
		
		// 取消
		function menuCancel() {
			closePopup();
		}
		
		// 获取所有配置内容
		function getGisStatContConfigs() {
			var categoriesValue = "";
			$("#categoriesValue").val("");
		
			var gisStatContConfigs = new Array();
		
			var categories = $('#categories').find('.ren-name');
			
			var small_categories;
			
			for(var i = 0; i < categories.length; i++) {
				var objClass = categories[i].id;
				
				small_categories = $(categories[i]).next().children();
				
				if (objClass != "未分类" && categoriesValue.split(",").indexOf(objClass) == -1) { // categoriesValue需要转数组再判断
					categoriesValue += objClass + ",";
				}
				
				for (var j = 0; j < small_categories.length; j++) {
					var cate = small_categories[j];
					var $cate = $(cate);
					var name = $cate.attr("name");
					
					if ($cate.attr('id') > 0) {
						var gisStatContConfig = {
							"statObjName" : cate.innerText,
							"objClass" : (objClass == "" ? (name == undefined ? "未分类" : name ) : objClass),
							"layerCfgId" : $cate.attr('id'),
							"displayOrder" : j + 1
						};
						
						gisStatContConfigs.push(gisStatContConfig);
					}
				}
			}
			
			$("#categoriesValue").val(categoriesValue);
			
			var gisStatContConfigss = JSON.stringify(gisStatContConfigs);
			$("#gisStatContConfigs").val(gisStatContConfigss);
		}
		
		// 关闭弹出框
		function closePopup() {
			$("#popupcontent").hide();
		}
		
		var oldBizType = "<#if gisStatConfig.bizType??>${gisStatConfig.bizType!''}</#if>";
		var oldRegionCode = "<#if gisStatConfig.regionCode??>${gisStatConfig.regionCode!''}</#if>";
	
		function checkBizType(){
			var isOK = true;
			var val = $("#bizType").val();
			var val_statType = $("#statType").val();
			var val_regionCode = $("#regionCode").val();
			if(oldBizType != val){
				$.ajax(
					  {
					    type : "POST",
					    async : false,
						url: '${rc.getContextPath()}/zhsq/map/gisStatConfig/checkBizType.json',
						data:{regionCode:val_regionCode, bizType:val, statType:val_statType},
						dataType:"json"
					  }).done(function(result)
					  {
						  if(result["check"]==false){
								isOK = false;
						  }else{
								oldBizType = val;
						  }
					  }).fail(function()
					  {
						  $.messager.alert('错误','连接超时！','error');
					  }).always(function()
					  {
					  });
			}else{
				isOK = true;
			}
			
			return isOK;
		}
		
		// 保存
		function tableSubmit() {
			if(!checkBizType()) {
				var cont = "框选";
				
				if ($('#statType').val() == '1') {
					cont = "周边";
				}
				
				alert("同一地域下已经存在相同地图首页的" + cont + "配置，请修改后再保存！");
				$("#bizName").focus();
				return;			
			}
			
			var classification = $("#classificationCbx").is(":checked");
			
			// 选中分类
			if (classification) {
				$("#classification").val('1');
			} else {
				$("#classification").val('0');
			}
		
			var isValid =  $("#tableForm").form('validate');
		
			if(isValid){
				modleopen();
				
				// 获取配置内容
				getGisStatContConfigs();
				
				$("#tableForm").attr("action","${rc.getContextPath()}/zhsq/map/gisStatConfig/saveOrUpdate.json");
		      	
			  	$("#tableForm").ajaxSubmit(function(data) {
			  		var msg = "新增";
			  		
			  		if ($('#statCfgId').val() > 0) {
			  			msg = "编辑";
			  		}
			  		
			  		if(data.result) {
	  					msg += "成功！";
	  				} else{
	  					msg += "失败！";
	  				}
	  				
  					parent.reloadDataForSubPage(msg);
				});
			}
		}
		
		// 是否分类
		function isClassification() {
			var classification = $("#classificationCbx").is(":checked");
			
			return classification;
		}
		
		// 关闭窗口
		function cancel() {
			parent.closeMaxJqueryWindow();
		}
	</script>
</body>
</html>

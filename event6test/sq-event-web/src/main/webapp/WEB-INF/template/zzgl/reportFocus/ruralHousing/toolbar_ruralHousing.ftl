<#include "/component/ComboBox.ftl" />
<link rel="stylesheet" href="${ANOLE_COMPONENT_URL}/js/components/date/css/date.css" type="text/css"/>
<script type="text/javascript" src="${ANOLE_COMPONENT_URL}/js/components/date/jquery.anole.date.js"></script>
<style type="text/css">
	.width65px{width:125px;}
	.w150{width:150px;}
	.keyBlank{color:gray;}
	.icon_select{ background:#ccc;box-shadow:inset 1px 1px 0px 0px #999; border-radius:3px; height:23px; line-height:23px; display:inline-block; padding:0 15px 0 0;text-align:center; margin-left: 10px;}
	.dateRenderWidth{width: 248px;}
</style>
<div id="jqueryToolbar">
	<form id="rhQueryForm">
		<input type="hidden" id="listType" name="listType" class="queryParam" value="${listType!'1'}" />
		<input type="hidden" id="reportType" name="reportType" value="${reportType!'4'}" class="queryParam iconParam" />
		<input type="hidden" name="routineInterval"  class="queryParam" value="${routineInterval!}" />
		<input type="hidden" id="exportQueryCode" name="exportQueryCode"  class="queryParam" value="${userOrgCode!}" />
		<input type="hidden" id="superviseMarkHidden" name="superviseMarkHidden"  class="" value="" />
		<div class="ConSearch">
	        <div class="fl">
	        	<ul>
	        		<li>所属区域：</li>
	                <li>
	                	<input id="infoOrgCode" name="eRegionCode" type="text" class="hide queryParam"/>
	                	<input id="gridId" type="text" class="hide"/>
	                	<input id="gridName" type="text" class="inp1 InpDisable w150" />
	                </li>
	                <#if listType?? && (listType == '2' || listType == '3' || listType == '4' || listType == '5')>
	            	<li>报告状态：</li>
	                <li>
	                	<input id="reportStatus" name="reportStatus" type="text" value="" class="queryParam hide"/>
	                	<input id="reportStatusName" type="text" class="inp1 InpDisable w150" />
	                </li>
	                </#if>
					<#if listType?? && listType == '2'>
						<li>超时状态：</li>
						<li>
							<input id="isOvertime" name="isOvertime" type="text" value="" class="queryParam hide"/>
							<input id="isOvertimeName" type="text" class="inp1 InpDisable w150" />
						</li>
					</#if>
	            	<li>关键字：</li>
	                <li><input name="keyWord" type="text" class="inp1 keyBlank w150 queryParam" id="keyWord" value="报告人姓名/发生地址" defaultValue="报告人姓名/发生地址" onfocus="_onfocus(this);" onblur="_onblur(this);" onkeydown="_onkeydown();" /></li>
	            	<li style="position:relative;">
	            		<a href="javascript:void(0)" class="AdvanceSearchBtn FontDarkBlue">高级查询</a>
	            		<div class="AdvanceSearch DropDownList hide" style="width:410px; top: 42px; left: -130px;">
	                        <div class="LeftShadow">
	                            <div class="RightShadow">
	                                <div class="list NorForm" style="position:relative;">
	                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	                                    	<tr>
	                                    		<td><label class="LabName width65px"><span>报告编号：</span></label><input class="inp1 easyui-validatebox queryParam" type="text" id="reportCode" name="reportCode" style="width:248px;" data-options="validType:['autoTrim[\'reportCode\']']"></input></td>
	                                    	</tr>
	                                    	<tr>
	                                    		<td><label class="LabName width65px"><span>规划许可证编号：</span></label><input class="inp1 easyui-validatebox queryParam" type="text" id="rcpCode" name="rcpCode" style="width:248px;" data-options="validType:['autoTrim[\'rcpCode\']']"></input></td>
	                                    	</tr>
	                                    	<tr>
	                                    		<td><label class="LabName width65px"><span>宅基地批准书编号：</span></label><input class="inp1 easyui-validatebox queryParam" type="text" id="rhaCode" name="rhaCode" style="width:248px;" data-options="validType:['autoTrim[\'rhaCode\']']"></input></td>
	                                    	</tr>
	                                    	<tr>
	                                    		<td><label class="LabName width65px"><span>建房户姓名：</span></label><input class="inp1 queryParam" type="text" id="householder" name="householder" style="width:248px;"></input></td>
	                                    	</tr>
	                                    	<tr>
	                                    		<td>
	                                    			<label class="LabName width65px"><span>证件类型：</span></label>
	                                    			<input type="text" class="hide queryParam" id="rhCardType" name="rhCardType" />
	                                    			<input class="inp1" type="text" id="rhCardTypeName" style="width:248px;"></input>
	                                    		</td>
	                                    	</tr>
	                                    	<tr>
	                                    		<td><label class="LabName width65px"><span>证件号码：</span></label><input class="inp1 queryParam" type="text" id="rhIdCard" name="rhIdCard" style="width:248px;"></input></td>
	                                    	</tr>
	                                    	<tr>
	                                    		<td>
	                                    			<label class="LabName width65px"><span>报告时间：</span></label>
	                                    			<input type="text" id="_reportDayDateRender" startId="reportDayStart" endId="reportDayEnd" class="inp1 InpDisable dateRenderWidth" value=""/>
	                                    		</td>
	                                    	</tr>
	                                    	<#if listType?? && (listType == '6')>
	                                    	<tr>
	                                    		<td>
	                                    			<label class="LabName width65px"><span>办结时间：</span></label>
	                                    			<input type="text" id="_instanceEndDayDateRender" startId="instanceEndDayStart" endId="instanceEndDayEnd" class="inp1 InpDisable dateRenderWidth" value=""/>
	                                    		</td>
	                                    	</tr>
	                                    	</#if>
	                                    </table>
	                                </div>
	                                <div class="BottomShadow"></div>
	                            </div>
	                        </div>
	                    </div>
	            	</li>
	            </ul>
	        </div>
	        <div class="btns">
	        	<ul>            	
	            	<li><a href="#" class="chaxun" title="查询按钮" onclick="conditionSearch()">查询</a></li>
	            	<li><a href="#" class="chongzhi" title="重置查询条件" onclick="resetCondition('rhQueryForm')">重置</a></li>
	            </ul>
	        </div>
	        <div class="clear"></div>‍
	        
		</div>
		<div class="h_10 clear"></div>
		<div style="position: relative;">
	    	<div class="blind"></div><!-- 文字提示 -->
	    	<script type="text/javascript">
	    		function DivHide() {
	    			$(".blind").slideUp();//窗帘效果展开
	    		}
	    		function DivShow(msg) {
	    			$(".blind").html(msg);
	    			$(".blind").slideDown();//窗帘效果展开
	    			setTimeout("this.DivHide()",800);
	    		}
	    	</script>
	    </div>
	    
	    <div class="ToolBar" id="toolbarDiv">
	    	<div id="actionDiv" class="tool fr hide"><@actionCheck></@actionCheck></div>
<#--			<a href="javascript:void(0)" class="NorToolBtn ExportBtn" onclick="exportData();">导出</a>-->
	    </div>
	</form>
</div>

<script type="text/javascript">
	$(function() {
		var listType = $('#listType').val();
		
		AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId, items){
			if(items!=undefined && items!=null && items.length>0){
				var grid = items[0];
				$("#infoOrgCode").val(grid.eOrgCode);
				$("#exportQueryCode").val(grid.orgCode);
			} 
		}, {
			OnCleared: function() {
				$("#infoOrgCode").val('');
				$("#exportQueryCode").val('');
			},
			ShowOptions: {
				EnableToolbar : true
			}
		});
		
		if($('#reportStatus').length > 0) {
			AnoleApi.initTreeComboBox("reportStatusName", "reportStatus", "B210008001", null, null, {
				RenderType : "01",
				ShowOptions: {
					EnableToolbar : true
				}
			});
		}
		
		AnoleApi.initTreeComboBox("rhCardTypeName", "rhCardType", "D030001", null, null, {
			ShowOptions: {
				EnableToolbar : true
			}
		});

		if($('#isOvertime').length > 0) {
			AnoleApi.initListComboBox("isOvertimeName", "isOvertime", null, null, [""], {
				DataSrc: [
					{name:'超时', value:'1'}
				],
				ShowOptions : {
					EnableToolbar : true
				}
			});
		}
		
		init4DateRender('rhQueryForm');
	});
	
    function queryData() {
    	var searchArray = new Array();
    	
		$("#rhQueryForm .queryParam").each(function() {
			var val = $(this).val(), key = $(this).attr("name");
			
			if($(this).hasClass("keyBlank")) {
				val = "";
			}
			
			if(isNotBlankString(val) && isBlankString(searchArray[key])){
				searchArray[key] = val;
			}
		});
		
		return searchArray;
	}

	//导出EDI方式
	function exportData(){
		var modelName = "农村住房";
		var url = 'http://bdio.v6.aishequ.org/export/form/T_EVENT_RURAL_HOUSING_EXPORT.jhtml?';
		var userOrgCode = '${userOrgCode}';
		var regionCode = $('#exportQueryCode').val();
		var searchArray = queryData();
		//上报状态
		var reportStatus= $('#reportStatus').val();


		searchArray["superviseMark"] = $("#superviseMarkHidden").val();//督办隐藏域

		//发现来源
		// var dataSourceArray = $('#dataSourceArray').val();
		if(isNotBlankString(regionCode)){
			searchArray["regionCode"] = regionCode;
		}else{
			searchArray["regionCode"] = userOrgCode;
		}
		if(isNotBlankString(reportStatus) && reportStatus.split(",").length > 1){
			delete searchArray["reportStatus"];
			searchArray["reportStatusArray"] = reportStatus.split(",");
		}
		// if(isNotBlankString(hiddenDangerType) && hiddenDangerType.split(",").length > 1){
		// 	delete searchArray["hiddenDangerType"];
		// 	searchArray["hiddenDangerTypeArray"] = hiddenDangerType.split(",");
		// }

		// if(isNotBlankString(dataSourceArray) && dataSourceArray.split(",").length > 1){
		// 	delete searchArray["dataSourceArray"];
		// 	delete searchArray["dataSource"];
		// 	searchArray["dataSourceArray"] = dataSourceArray.split(",");
		// }else if(isNotBlankString(dataSourceArray) && dataSourceArray.split(",").length == 1){
		// 	delete searchArray["dataSourceArray"];
		// 	delete searchArray["dataSource"];
		// 	searchArray["dataSource"] = dataSourceArray;
		// }
		showExportWindows(modelName + "场景运用事件导出模板", url ,searchArray,1000,420);
	}

	function showExportWindows(title, targetUrl, params, width, height) {
		var $MaxJqueryWindow;
		if (width == undefined || width == null) {
			width = $(window).width();
		}
		if (height == undefined || height == null) {
			height = $(window).height();
		}
		var left = parseInt(($(window).width() - width) * 0.5 + $(document).scrollLeft());
		var top = parseInt(($(window).height() - height) * 0.5 + $(document).scrollTop());
		var form = $('<form style="display:none;" action="' + targetUrl + '" target="_exportPage" method="post"></form>');
		var iframe = $('<iframe  name="_exportPage" src="" scrolling="0" frameborder="0" src="" style="width:100%;height:100%;" ></iframe>');
		var windowDiv = $("<div></div>");

		windowDiv.appendTo("body").append(form).append(iframe);
		if (params) {
			for (var key in params) {
				if(params[key] instanceof Array){
					var arrays = params[key];
					for(var i = 0;i<arrays.length;i++){
						form.append("<input type='hidden' name='" + key + "' value='" + arrays[i] + "' >");
					}
					continue;
				}
				if (key) {
					form.append("<input type='hidden' name='" + key + "' value='" + params[key] + "' >");
				}

			}
		}
		$MaxJqueryWindow = windowDiv.window({
			title: (title == null || title == "") ? "导出页面" : title,
			width: width,
			height: height,
			top: top,
			left: left,
			shadow: false,
			modal: true,
			closed: true,
			closable: true,//是否有关闭按钮
			minimizable: false,
			maximizable: false,
			collapsible: false,
			onBeforeClose: function () {
				windowDiv.remove();
			}
		});
		$MaxJqueryWindow.window('open');
		form.submit();
	}

</script>

<@block name="operateFunction">
	<#include "/zzgl/reportFocus/ruralHousing/operate_ruralHousing.ftl" />
</@block>

<#include "/zzgl/reportFocus/base/list_base.ftl" />
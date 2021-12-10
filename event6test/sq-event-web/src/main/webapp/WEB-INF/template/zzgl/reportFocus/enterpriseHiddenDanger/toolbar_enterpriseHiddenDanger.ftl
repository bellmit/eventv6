<#include "/component/ComboBox.ftl" />
<link rel="stylesheet" href="${ANOLE_COMPONENT_URL}/js/components/date/css/date.css" type="text/css"/>
<script type="text/javascript" src="${ANOLE_COMPONENT_URL}/js/components/date/jquery.anole.date.js"></script>
<style type="text/css">
	.width65px{width:75px;}
	.w150{width:150px;}
	.keyBlank{color:gray;}
	.icon_select{ background:#ccc;box-shadow:inset 1px 1px 0px 0px #999; border-radius:3px; height:23px; line-height:23px; display:inline-block; padding:0 15px 0 0;text-align:center; margin-left: 10px;}
	.dateRenderWidth{width: 248px;}
</style>
<div id="jqueryToolbar">
	<form id="ehdQueryForm">
		<input type="hidden" id="listType" name="listType" class="queryParam" value="${listType!'1'}" />
		<input type="hidden" id="reportType" name="reportType" value="${reportType!'3'}" class="queryParam iconParam" />
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
	            		<div class="AdvanceSearch DropDownList hide" style="width:375px; top: 42px; left: -130px;">
	                        <div class="LeftShadow">
	                            <div class="RightShadow">
	                                <div class="list NorForm" style="position:relative;">
	                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	                                    	<tr>
	                                    		<td><label class="LabName width65px"><span>报告编号：</span></label><input class="inp1 queryParam" type="text" id="reportCode" name="reportCode" style="width:248px;"></input></td>
	                                    	</tr>
	                                    	<tr>
	                                    		<td><label class="LabName width65px"><span>企业名称：</span></label><input class="inp1 queryParam" type="text" id="enterpriseName" name="enterpriseName" style="width:248px;"></input></td>
	                                    	</tr>
	                                    	<tr>
	                                    		<td>
	                                    			<label class="LabName width65px"><span>存在隐患：</span></label>
	                                    			<input type="text" class="hide queryParam" id="isHiddenDanger" name="isHiddenDanger" />
	                                    			<input class="inp1" type="text" id="isHiddenDangerName" style="width:248px;"></input>
	                                    		</td>
	                                    	</tr>
	                                    	<tr id="hiddenDangerTypeTr">
	                                    		<td>
	                                    			<label class="LabName width65px"><span>隐患类型：</span></label>
	                                    			<input type="text" class="hide queryParam" id="hiddenDangerType" name="hiddenDangerType" />
	                                    			<input class="inp1" type="text" id="hiddenDangerTypeName" style="width:248px;"></input>
	                                    		</td>
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
											<#if listType?? && (listType == '2' || listType == '5' || listType == '6')>
												<tr>
													<td>
														<label class="LabName width65px"><span>发现来源：</span></label>
														<input type="text" class="hide queryParam" id="dataSourceArray" name="dataSourceArray" />
														<input class="inp1" type="text" id="dataSourceArrayName" style="width:248px;"></input>
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
	            	<li><a href="#" class="chongzhi" title="重置查询条件" onclick="resetCondition('ehdQueryForm')">重置</a></li>
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
			AnoleApi.initTreeComboBox("reportStatusName", "reportStatus", "B210002001", null, null, {
				RenderType : "01",
				ShowOptions: {
					EnableToolbar : true
				}
			});
		}
		
		AnoleApi.initTreeComboBox("isHiddenDangerName", "isHiddenDanger", null, null, null, {
			DataSrc : [{"name":"是", "value":"1"}, {"name":"否", "value":"0"}],
			ShowOptions: {
				EnableToolbar : true
			},
			OnChanged: function(value) {
				if(value == '1') {
					$('#hiddenDangerTypeTr').show();
				} else {
					$('#hiddenDangerTypeTr').hide();
					$('#hiddenDangerType').val('');
					$('#hiddenDangerTypeName').val('');
				}
			},
			OnCleared: function() {
				$('#hiddenDangerTypeTr').show();
			}
		});
		
		AnoleApi.initTreeComboBox("hiddenDangerTypeName", "hiddenDangerType", "B210002002", null, null, {
			RenderType : "01",
			ShowOptions: {
				EnableToolbar : true
			}
		});

		if($('#dataSourceArrayName').length > 0) {
			AnoleApi.initTreeComboBox("dataSourceArrayName", "dataSourceArray", "B210002005", null, null, {
				RenderType : "01",
				ShowOptions: {
					EnableToolbar : true
				}
			});
		}

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
			
		init4DateRender('ehdQueryForm');
	});
    
    function queryData() {
    	var searchArray = new Array();
    	
		$("#ehdQueryForm .queryParam").each(function() {
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

	//导出企业安全数据
	// function exportData() {
	// 	var searchArray = queryData();
	// 	//excel 报表第一行固定 占7列
	// 	searchArray["mergeCells"] = 19;
	// 	exportReportFocus(searchArray);
	// }

	//导出EDI方式
	function exportData(){
		var modelName = "工业企业安全生产";
		var url = 'http://bdio.v6.aishequ.org/export/form/T_EVENT_ENTER_HIDDEN_DANGER_EXPORT.jhtml?';
		var userOrgCode = '${userOrgCode}';
		var regionCode = $('#exportQueryCode').val();
		var searchArray = queryData();
		//上报状态
		var reportStatus= $('#reportStatus').val();
		//隐患
		var hiddenDangerType = $('#hiddenDangerType').val();
		//发现来源
		var dataSourceArray = $('#dataSourceArray').val();
		searchArray["superviseMark"] = $("#superviseMarkHidden").val();
		if(isNotBlankString(regionCode)){
			searchArray["regionCode"] = regionCode;
		}else{
			searchArray["regionCode"] = userOrgCode;
		}
		if(isNotBlankString(reportStatus) && reportStatus.split(",").length > 1){
			delete searchArray["reportStatus"];
			searchArray["reportStatusArray"] = reportStatus.split(",");
		}
		if(isNotBlankString(hiddenDangerType) && hiddenDangerType.split(",").length > 1){
			delete searchArray["hiddenDangerType"];
			searchArray["hiddenDangerTypeArray"] = hiddenDangerType.split(",");
		}
		if(isNotBlankString(dataSourceArray) && dataSourceArray.split(",").length > 1){
			delete searchArray["dataSourceArray"];
			delete searchArray["dataSource"];
			searchArray["dataSourceArray"] = dataSourceArray.split(",");
		}else if(isNotBlankString(dataSourceArray) && dataSourceArray.split(",").length == 1){
			delete searchArray["dataSourceArray"];
			delete searchArray["dataSource"];
			searchArray["dataSource"] = dataSourceArray;
		}

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
	<#include "/zzgl/reportFocus/enterpriseHiddenDanger/operate_enterpriseHiddenDanger.ftl" />
</@block>

<#include "/zzgl/reportFocus/base/list_base.ftl" />
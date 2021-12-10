<#include "/component/ComboBox.ftl" />
<style type="text/css">
	.width65px{width:75px;}
	.w150{width:150px;}
	.keyBlank{color:gray;}
	/*图标选中凹陷效果只有在ie9及其以上才有效果*/
	.icon_select{ background:#ccc;box-shadow:inset 1px 1px 0px 0px #999; border-radius:3px; height:23px; line-height:23px; display:inline-block; padding:0 15px 0 0;text-align:center; margin-left: 10px;}
</style>
<div id="jqueryToolbar">
	<form id="twoVioPreQueryForm">
		<input type="hidden" id="listType" name="listType" class="queryParam" value="${listType!'5'}" />
		<input type="hidden" id="reportType" name="reportType" value="${reportType!'1'}" class="queryParam iconParam" />
		<input type="hidden" name="routineInterval"  class="queryParam" value="${routineInterval!}" />
		<input type="hidden" id="timeOut" name="timeOut"  class="queryParam" value="${timeOut!''}" />
		<input type="hidden" id="jurisdiction" name="jurisdiction"  class="queryParam" value="${jurisdiction!''}" />
		<input type="hidden" id="reportWay" name="reportWay"  class="queryParam" value="${reportWay!''}" />
		<input type="hidden" id="publicReport" name="publicReport"  class="queryParam" value="${publicReport!''}" />
		<input type="hidden" id="isCapRegionPath" name="isCapRegionPath"  class="queryParam" value="${isCapRegionPath!''}" />
		<input type="hidden" id="overDue" name="overDue"  class="queryParam" value="${overDue!''}" />
		<#--<input type="hidden" id="reportDayStart" name="reportDayStart"  class="queryParam" value="${reportDayStart!''}" />
		<input type="hidden" id="reportDayEnd" name="reportDayEnd"  class="queryParam" value="${reportDayEnd!''}" />-->
		<input type="hidden" id="capDB" name="capDB"  class="queryParam" value="${capDB!''}" />

		<div class="ConSearch">
	        <div class="fl">
	        	<ul>
	        		<li>所属区域：</li>
	                <li>
	                	<input id="infoOrgCode" name="eRegionCode" type="text" class="hide queryParam" value="${eRegionCode}"/>
	                	<input id="gridId" type="text" class="hide"  value="${gridId}" />
	                	<input id="gridName" type="text" class="inp1 InpDisable w150" value="${gridName}" />
	                </li>
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
	                                    		<td>
	                                    			<label class="LabName width65px">
														<span>报告时间：</span>
													</label>
													<input class="inp1 Wdate fl queryParam" type="text" id="reportDayStart" name="reportDayStart" value="${reportDayStart!''}" style="width:110px; *width:100px; cursor:pointer;" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'reportDayEnd\')}',readOnly:true})" readonly="readonly"></input>
													<span class="Check_Radio" style="padding:0 5px;">至</span>
													<input class="inp1 Wdate fl queryParam" type="text" id="reportDayEnd" name="reportDayEnd" value="${reportDayEnd!''}" style="width:110px; *width:100px; cursor:pointer;" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'reportDayStart\')}',readOnly:true})" readonly="readonly"></input>
	                                    		</td>
	                                    	</tr>
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
	            	<li><a href="#" class="chongzhi" title="重置查询条件" onclick="resetCondition('twoVioPreQueryForm')">重置</a></li>
	            </ul>
	        </div>
	        <div class="clear"></div>‍
	        
		</div>
		<div class="h_10 clear"></div>
		<div class="ToolBar" id="toolbarDiv">
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
	        <div id="actionDiv" class="tool fr hide"></div>
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
			} 
		
		});
		
	});
    
    function queryData() {
    	var searchArray = {};
    	
		$("#twoVioPreQueryForm .queryParam").each(function() {
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
</script>

<@block name="operateFunction">
	<#include "/zzgl/eventAndReportFocus/operate_reportFocus.ftl" />
</@block>

<#include "/zzgl/reportFocus/base/list_base.ftl" />

<script type="text/javascript">
function clickFormatter(value, rec, rowIndex) {
	var title = "";
	
	if(value) {
		var instanceId = rec.INSTANCE_ID || '',
			showContent = '',
			//superviseMark = rec.superviseMark || '0';
		
		/* if(superviseMark == '1') {
			showContent = '<img src="${rc.getContextPath()}/images/duban2.png" style="margin:0 10px 0 0; width:28px; height:28px;">';
		} */
		
		title = showContent + '<a href="###" title="'+ value +'" onclick="detail(\'' + rec.REPORT_UUID + '\', \'' + instanceId + '\', \'' + $('#listType').val() + '\')">'+ value +'</a>';
	}
	
	return title;
}
function curUserFormatter(value, rowData, rowIndex) {
	var title = '';

	if(value) {
		var curOrgName = rowData.curOrgName || '';

		if(curOrgName) {
			value += '(' + curOrgName + ')';
		}

		title = '<span title="'+ value +'" >'+ value +'</span>';
	}

	return title;
}

function hourFormatter(value, rowData, rowIndex) {
	if(value && value.length >= 13) {
		value = value.substring(0, 13) + '时';
	}

	return value;
}
</script>
	
<#include "/component/ComboBox.ftl" />
<style type="text/css">
	.w100{width:100px;}
	.w75{width:75px;}
	.pattern{ background: #304EC6; color: #FFFFFF; }
	.patternBasic{height: 12px; line-height: 12px; display: inline-block; padding: 5px; text-align: center;}
</style>

<div id="jqueryToolbar" class="MainContent">
	<div class="ConSearch">
		<form id="bayonetRunDataForm">
			<input id="eqpSn" name="eqpSn" type="hidden" class="queryParam" value="${eqpSn!}" />
			
	        <div class="fl">
	        	<ul>
	        		<li>车牌号码：</li>
	                <li><input id="plateInfo" name="plateInfo" type="text" class="inp1 w100 queryParam" onkeydown="_onkeydown();" /></li>
	                <li>经过时间：</li>
	                <li><input class="inp1 Wdate fl queryParam" type="text" id="passTimeStart" name="passTimeStart" style="width:146px; cursor:pointer;" onclick="WdatePicker({readOnly:true, dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly"></input><span class="Check_Radio" style="padding:0 5px;">至</span><input class="inp1 Wdate fl queryParam" type="text" id="passTimeEnd" name="passTimeEnd" style="width:146px; cursor:pointer;" onclick="WdatePicker({readOnly:true, dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly"></input></li>
	                <li>搜索类型：</li>
	                <li>
	                	<input type="text" id="searchType" name="searchType" class="queryParam hide"  value="1" />
                        <select id="searchTypeSel" class="easyui-combobox" style="width:135px; height:26px; *height:28px;" data-options="panelHeight:100, onSelect:function(record){$('#searchType').val(record.value);}">
			    			<option value="1" selected>机动车辆</option>
			    			<option value="2">异常牌照</option>
			    			<option value="3">违法车辆</option>
			    			<option value="4">红名单</option>
			    			<option value="5">布控车辆</option>
			    		</select>
	                </li>
	            </ul>
	        </div>
	        <div class="btns">
	        	<ul>            	
	            	<li><a href="#" class="chaxun" title="查询按钮" onclick="searchData()">查询</a></li>
	            	<li><a href="#" class="chongzhi" title="重置查询条件" onclick="resetCondition()">重置</a></li>
	            </ul>
	        </div>
        </form>
	</div>
	
	<div class="h_10 clear"></div>
	
	<div class="ToolBar" id="ToolBar">
        <div id="patternDiv" class="tool fr" style="margin-right: 5px;">
        	<a href="#" id="patternPic" class="patternBasic" onclick="changePattern(this);" style="text-decoration: none;">图片</a>
        	<a href="#" id="patternList" class="patternBasic" onclick="changePattern(this);" style="text-decoration: none;">列表</a>
        </div>
    </div>
</div>

<script type="text/javascript">
	$(function() {
		var pattern = parseInt("${pattern}", 10);
		
		if(pattern) {
			$("#patternList").addClass("pattern");
		} else {
			$("#patternPic").addClass("pattern");
		}
	});
	
	function searchData() {
		var searchArray = {},
			queryVal = "",
			isValid = true;
		
		$("#bayonetRunDataForm .queryParam").each(function() {
			queryVal = $(this).val();
			
			if(queryVal) {
				searchArray[$(this).attr("name")] = queryVal;
			}
		});
		
		if(searchArray.passTimeStart && !searchArray.passTimeEnd) {
			isValid = false;
			$.messager.alert('警告', "请选择经过时间的结束时间！", 'warning');
		} else if(!searchArray.passTimeStart && searchArray.passTimeEnd) {
			isValid = false;
			$.messager.alert('警告', "请选择经过时间的开始时间！", 'warning');
		}
		
		if(isValid) {
			doSearch(searchArray);
		}
	}
	
	function $resetCondition() {
		$('#bayonetRunDataForm')[0].reset();
		
		$("#searchTypeSel").combobox("setValue", $("#searchType").val());
		
		searchData();
	}
	
	function _onkeydown(){
		var keyCode = event.keyCode;
		
		if(keyCode == 13){
			searchData();
		}
	}
	
	//列表模式切换
	function changePattern(obj) {
		$(obj).siblings().removeClass("pattern");
		$(obj).addClass("pattern");
		
		var pattern = (parseInt("${pattern}", 10) + 1) % 2;
		var url = '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofgoods/toBayonetRunDataList.jhtml?pattern=' + pattern + '&eqpSn=' + $("#eqpSn").val();
	
		$(obj).attr("href", url);
	}
</script>
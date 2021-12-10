<#include "/component/ComboBox.ftl" />

<div id="jqueryToolbar">
	<div class="ConSearch">
        <div class="fl">
        	<ul>
        		<li>事件分类：</li>
                <li>
                	<input id="type" name="type" type="hidden"/>
                	<input id="typeName" name="typeName" type="text" class="inp1 InpDisable" style="width:150px;"/>
                </li>
                <li>关键字：</li>
                <li><input name="keyWord" type="text" class="inp1" id="keyWord" value="事件描述/标题/事发详址" style="color:gray; width:150px;" onfocus="if(this.value=='事件描述/标题/事发详址'){this.value='';}$(this).attr('style','width:150px;');" onblur="if(this.value==''){$(this).attr('style','color:gray;width:150px;');this.value='事件描述/标题/事发详址';}" onkeydown="_onkeydown();" /></li>
            </ul>
        </div>
        <div class="btns">
        	<ul>            	
            	<li><a href="#" class="chaxun" title="查询按钮" onclick="searchData()">查询</a></li>
            	<li><a href="#" class="chongzhi" title="重置查询条件" onclick="resetCondition()">重置</a></li>
            </ul>
        </div>‍
        
	</div>
	<div class="h_10 clear"></div>
	<div class="ToolBar" id="ToolBar">
    	<div class="blind"></div>
    	<script type="text/javascript">
			function DivHide(){
				$(".blind").slideUp();//窗帘效果展开
			}
			function DivShow(msg){
				$(".blind").html(msg);
				$(".blind").slideDown();//窗帘效果展开
				setTimeout("this.DivHide()",800);
			}
		</script>	
        <div class="tool fr">
    		<a href="#" class="NorToolBtn EmergencyBtn" onclick="showRespondWinodw();">启动预案</a>
        </div>
    </div>
	
</div>

<script type="text/javascript">
	$(function(){
		AnoleApi.initTreeComboBox("typeName", "type", null, null, null, {
        	DataSrc : [{"name":"医患聚集", "value":"0216"},{"name":"讨薪聚集", "value":"0217"},{"name":"信访聚集", "value":"0218"}],
        	ShowOptions: {
				EnableToolbar : true
			}
        });
	});
	
	function showRespondWinodw(){
		var rows = $('#list').datagrid('getSelections');
		
		if(rows.length == 1){
			$("#hideRespondWinodw").show();
		}else{
			$.messager.alert('警告','请选中一条要启动预案的事件!','warning');
		}
		
	}
	
	function searchData() {
	    var a = new Array();
	    
	    a["eventStatus"] = "01,02";
	    a["eventType"] = "all"; 
	    a["urgencyDegrees"] = ["04"];
	    a["typesForList"] = typesForList;
	    
	    var gridId = startGridId;
	    if(gridId!=null && gridId!="") {
	    	a["gridId"]=gridId;
	    }
	    
	    var type = $("#type").val();
	    if(type!=null && type!="") {
	    	a["type"]=type;
	    }
	    
	    var keyWord = $("#keyWord").val();
		if(keyWord!=null && keyWord!="" && keyWord!="事件描述/标题/事发详址") {
			a["keyWord"]=keyWord;
		}
	    
	    doSearch(a);
	}
	
	function resetCondition(){
	    $("#type").val("");
	    $("#typeName").val("");
	    $("#keyWord").val("事件描述/标题/事发详址");
		$("#keyWord").attr('style','font-size:12px;color:gray; width:150px;');
		searchData();
	}
	
	function doSearch(queryParams){
	    $('#list').datagrid('clearSelections');
	    $("#list").datagrid('options').queryParams=queryParams;
	    $("#list").datagrid('load');
	}
	
	function _onkeydown(){
		var keyCode = event.keyCode;
		if(keyCode == 13){
			searchData();
		}
	}
	
</script>
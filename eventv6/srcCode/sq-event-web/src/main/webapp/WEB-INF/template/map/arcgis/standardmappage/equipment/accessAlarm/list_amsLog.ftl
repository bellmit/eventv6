<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>报警机报警记录信息列表</title>
<#include "/component/commonFiles-1.1.ftl" />
<#include "/component/ImageView.ftl" />
</head>
<body class="easyui-layout">
<div id="constructionInfoDiv" region="center" border="false" style="width:100%; overflow:hidden;">
	<table id="list"></table>
</div>


<div id="jqueryToolbar">
	<div class="ConSearch">
		<form id="searchForm">
        <div class="fl">
	        <ul>
		    	<li>用户：</li>
            	<li>
            		<input id="userName" name="userName" type="text" class="inp1" style="width:150px;" value=""/>
            	</li>
            	<li>报警时间：</li>
            	<li>
					<input type="text" style="width:100px" class="Wdate inp1" readonly name="startTimeStr" id="startTimeStr"  onclick="WdatePicker({el:'startTimeStr',dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endTimeStr\')}'})"/>
				</li>
		    	<li>
		    	至
		    	</li>
		    	<li>
					<input  type="text" style="width:100px" class="Wdate inp1" readonly name="endTimeStr" id="endTimeStr"   onclick="WdatePicker({el:'endTimeStr',dateFmt:'yyyy-MM-dd' ,minDate:'#F{$dp.$D(\'startTimeStr\')}'})"/>
                </li>

			</ul>
		</div>
		<div class="btns">
        	<ul>
            	<li><a href="#" class="chaxun" onclick="searchData()">查询</a></li>
            	<li><a href="#" class="chongzhi" onclick="resetCondition()">重置</a></li>
            </ul>
        </div>
        <div class="clear"></div>
        </form>
	</div>
	<div class="h_10" id="TenLineHeight1"></div>
	<div class="ConList" >
		<div class="ToolBar" style="height:0px">
			<div class="fr" style="height:0px;padding-top:0px">
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	var bizId = '<#if bizId??>${bizId!''}</#if>'

	function DivHide(){
		$(".blind").slideUp();//窗帘效果展开
	}
	function DivShow(msg){
		$(".blind").html(msg);
		$(".blind").slideDown();//窗帘效果展开
		setTimeout("this.DivHide()",800);
	}

	$(function(){
		loadDataList();
	})
	
	function loadDataList() {
		$('#list').datagrid({
			width : 600,
			height : 600,
			nowrap : false,
			striped : true,
			singleSelect: true,
			fit : true,
			rownumbers : true,
			idField:'infoOpenId',
			url:'${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofgoods/listAmsLogData.json',
			frozenColumns:[[
            	{field:'logId',title:'ID',hidden:true},
			]],
			columns:[[
                {field:'userName', title:'用户', width:$(this).width() * 0.16, align:'left'},
				{field:'acqTime',title:'时间', align:'left',width:$(this).width() * 0.12,formatter:formartTime},
				/*
                {field:'alarmType',title:'报警类型', align:'center',width:$(this).width() * 0.1,
			    	formatter:function(value, rec, index) {
			    		if(value != null && value != '' && value == '1') {
			    			return '火警';
			    		}else{
			    			return '火警';
			    		}
					}},
				*/
                {field:'alarmData',title:'报警内容', align:'center',width:$(this).width() * 0.2,
			    	formatter:function(value, rec, index) {
			    		var val = value;
                		if(value==null)return "";
						if(value!=null && value.length>13){
							val = value.substring(0,12);
						}
						var tab = '';
						tab = '<span title="'+ rec.alarmData +'">'+val+'</span>';
						return tab;
					}},
                {field:'alarmArea',title:'防区', align:'center',width:$(this).width() * 0.1},
                {field:'resultStatus',title:'是否处理', align:'center',width:$(this).width() * 0.1,
			    	formatter:function(value, rec, index) {
			    		if(value != null && value != '' && value == '1') {
			    			return '已处理';
			    		}else{
			    			return '未处理';
			    		}
					}},
                {field:'resultTime',title:'处理时间', align:'center',width:$(this).width() * 0.1,formatter:formartTime},
                {field:'result',title:'处理信息', align:'center',width:$(this).width() * 0.1},	
			    {field:'imgUrl',title:'处理截图', align:'center',width:$(this).width() * 0.075,
			    	formatter:function(value, rec, index) {
			    		if(value != null && value != ''){
			    			return '<a href="###" onclick="showImg(\''+rec.logId+'\')">详情</a>';
			    		}
					}
			    }
			]],
			toolbar:'#jqueryToolbar',
			pagination:true,
			pageSize: 20,
			queryParams: getQueryParams(),
	        onLoadSuccess:function(data){
				if(data.total==0){
				  	var body = $(this).data().datagrid.dc.body2;
					body.append('<div style="text-align: center;"><img src="${rc.getContextPath()}/images/nodata.png" title="暂无数据"/></div>');
				}else{
		          $('.datagrid-body-inner').eq(0).removeClass("l_elist");
		        }
			},
			onLoadError:function(){
				var body = $(this).data().datagrid.dc.body2;
				body.append('<div style="text-align: center;"><img src="${rc.getContextPath()}/images/errordata.png" title="暂无数据"/></div>');
			}
		});
		
		
		//设置分页控件
	    var p = $('#list').datagrid('getPager');
		$(p).pagination({
			pageSize: 20,//每页显示的记录条数，默认为
			pageList: [20,30,40,50],//可以设置每页记录条数的列表
			beforePageText: '第',//页数文本框前显示的汉字
			afterPageText: '页    共 {pages} 页',
			displayMsg: '当前显示第 {from} 到 {to} 条记录   共 {total} 条记录',
			onBeforeRefresh:function(){
				$('#list').datagrid('clearSelections');
				$(this).pagination('loading');
				$(this).pagination('loaded');
			}
		});
	}
	
	function resetCondition(){
		$("#searchForm")[0].reset();
		searchData();
	}
	
	function getQueryParams() {
		var a = new Array();
		a["eqpId"] = bizId;
		a["userName"] = $("#userName").val();
		a["acqTimeBegin"] = $("#startTimeStr").val();
		a["acqTimeEnd"] = $("#endTimeStr").val();
		return a;
	}
	
	function searchData() {
		doSearch(getQueryParams());
	}
	
	function doSearch(queryParams){
		$('#list').datagrid('clearSelections');
		$("#list").datagrid('options').queryParams=queryParams;
		$("#list").datagrid('load');
	}
	
	//-- 供子页调用的重新载入数据方法
	function reloadDataForSubPage(result) {
		closeMaxJqueryWindow();
		reloadData(result);
	}
	
	function reloadData(result) {
		DivShow(result);
		reloadSelectTreeNode();
		$("#list").datagrid('load');
		$('#list').datagrid('clearSelections');
	}
	
	function viewImg(id, path) {
		ImageViewApi.initImageView(id, path, false, false);
		ffcs_viewImg(id, 0);
	}
	
	function showImg(logId){
		var width = 420;
		var height = 420;
		var showImgUrl = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofgoods/showImg.jhtml?logId=" + logId;
		parent.showMaxJqueryWindow("处理截图",showImgUrl,width,height);
	}
	
	Date.prototype.formatString = function(fmt){
		var o = {   
			"M+" : this.getMonth()+1,                 //月份   
			"d+" : this.getDate(),                    //日   
			"h+" : this.getHours(),                   //小时   
			"m+" : this.getMinutes(),                 //分   
			"s+" : this.getSeconds(),                 //秒   
			"q+" : Math.floor((this.getMonth()+3)/3), //季度   
			"S"  : this.getMilliseconds()             //毫秒   
		};   
		if(/(y+)/.test(fmt))   
			fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
		for(var k in o)   
			if(new RegExp("("+ k +")").test(fmt))   
		fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
		return fmt;   
	}
	
	function formartTime(value){
		if(value){
			var date = new Date(value);
			return date.formatString("yyyy-MM-dd");
		}else{
			return value;
		}
	}
	
	
</script>

<#include "/component/maxJqueryEasyUIWin.ftl" />
</body>
</html>
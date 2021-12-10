<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>车闸列表</title>
<#include "/component/commonFiles-1.1.ftl" />

<style>

</style>
</head>
<body class="easyui-layout">
	<div id="jqueryToolbar">
	<div class="ConSearch">
        <div class="fl">
        	<ul>
            	<li>车牌号：</li>
                <li><input name="eqpSn" type="text" class="inp1" id="eqpSn" value="" style="width:210px;"/></li>
            	<li>
            	</li>
            </ul>
        </div>
        <div class="btns">
        	<ul>            	
            	<li><a href="#" class="chaxun BlueBtn" title="查询按钮" onclick="searchData(1)">查询</a></li>
            	<li><a href="#" class="chongzhi GreenBtn" style="margin-right:0;" title="重置查询条件" onclick="resetCondition()">重置</a></li>
            </ul>
        </div>
        <div class="clear"></div>‍
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
    </div>
	

</div>
	<div id="eventRecordContentDiv" region="center" border="false" style="width:100%; overflow:hidden;">
		<table id="list"></table>
	</div>
</body>
<script type="text/javascript">

//-- 供子页调用的重新载入数据方法
function reloadDataForSubPage(result){
	closeMaxJqueryWindow();
	$.messager.alert('提示', result, 'info');
	$("#list").datagrid('load');
}
</script>
<script type="text/javascript">
    var startGridId = "${startGridId?c}";
    $(function(){
    	loadDataList();//init data
    });
    
    function loadDataList(){
    	$('#list').datagrid({
			width:600,
			height:300,
			nowrap: false,
			remoteSort:false,
			striped: true,
			fit: true,
			//singleSelect: true,
			idField:'logId',
			url:'${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofgoods/listEquipmentApsData.json',
			columns:[[
                {field:'plateSn',title:'车牌号', align:'left',width:fixWidth(0.26),sortable:true},
                {field:'acqTime',title:'开闸时间', align:'center',width:fixWidth(0.25),sortable:true,
                	formatter:function(value,rec,rowIndex){
                		var date = new Date(value);
                		return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate();
                	}	
                },
				{field:'acqType',title:'开闸方式', align:'center',width:fixWidth(0.12),sortable:true},
                {field:'inoutMode',title:'出入方式', align:'center',width:fixWidth(0.12),sortable:true,
                	formatter:function(value,rec,rowIndex){
                		var f = '';
                		if(value == '1'){
                			f = '进门';
                		}else if(value == '2'){
                			f = '出门';
                		}
	               		return f;
	               	}	
               },
                {field:'status',title:'刷卡状态', align:'center',width:fixWidth(0.12),sortable:true},
                {field:'bizId',title:'门禁抓拍', align:'center',width:fixWidth(0.12),sortable:true,
                	formatter:function(value,rec,rowIndex){
                		var f = '<a href="javascript:void(0)" onclick="show(\''+rec.imgUrl+'\')">查看</a>';
                		return f;
                	}
                }
			]],
			toolbar:'#jqueryToolbar',
			pagination:true,
			pageSize: 20,
			onLoadSuccess:function(data){
			    $('#list').datagrid('clearSelections');	//清除掉列表选中记录
				if(data.total==0){
				var body = $(this).data().datagrid.dc.body2;
					body.append('<div style="text-align: center;"><img src="${rc.getContextPath()}/theme/frame/images/nodata.gif" title="暂无数据"/></div>');
				}
			},
			queryParams:{gridId:$("#gridId").val()},
			onSelect:function(index,rec){
				idStr=rec.patrolId;
			}  
		});
		
		//设置分页控件
	    var p = $('#list').datagrid('getPager');
		$(p).pagination({
			pageSize: 20,//每页显示的记录条数，默认为
			pageList: [20,30,40,50],//可以设置每页记录条数的列表
			beforePageText: '第',//页数文本框前显示的汉字
			afterPageText: '页    共 {pages} 页',
			displayMsg: '当前显示第 {from} 到 {to} 条记录   共 {total} 条记录'
		});
    }
    
	function show(imgUrl){
		if(!imgUrl){
		    $.messager.alert('提示','请选择一条记录！','info');
		}else{
			var iUrl = 'http://img.fjsq.org/zzgrid/attachment/2015/07/11/zzgrid-attachment-9c4a39c107444d5d8b32e89c736c1368.jpg';
		    var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofgoods/toEquipmentApsImgs.jhtml?imgUrl="+iUrl;
		    showMaxJqueryWindow("门禁抓拍", url, 300, 300);
		}
	}
	
	function searchData(b) {
		var a = new Array();
		var eqpSn = $("#eqpSn").val();
		if(eqpSn!=null && eqpSn!="" && eqpSn!="") a["eqpSn"]=eqpSn;
		doSearch(a);
	}
	
	function doSearch(queryParams){
		$('#list').datagrid('clearSelections');
		$("#list").datagrid('options').queryParams=queryParams;
		$("#list").datagrid('load');
	}
	
	function resetCondition() {
		$("#eqpSn").val("");
		searchData();
	}
	
	
</script>
<#include "/component/customEasyWin.ftl" />
<#include "/component/ComboBox.ftl" />
<#include "/component/maxJqueryEasyUIWin.ftl" />
</html>
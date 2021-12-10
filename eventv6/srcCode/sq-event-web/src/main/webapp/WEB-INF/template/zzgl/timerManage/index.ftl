<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>定时器管理列表</title>
<#include "/component/commonFiles-1.1.ftl" />
<style>

</style>
</head>
<body class="easyui-layout">

	<div id="jqueryToolbar">
	<div class="ConSearch">
        <div class="fl">
        	<ul>
            	<li>关键字：</li>
                <li><input name="keyWord" type="text" class="inp1" id="keyWord" value="" style="font-size:12px;font-style:italic;color:gray; width:210px;" onfocus="if(this.value==''){this.value='';}$(this).attr('style','width:210px;');" onblur="if(this.value==''){$(this).attr('style','font-size:12px;font-style:italic;color:gray;width:210px;');this.value='';}"/></li>
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
        <div class="tool fr">
       			<#--<a href="#" class="NorToolBtn DelBtn" onclick="del();">删除</a>-->
				<a href="#" class="NorToolBtn EditBtn" onclick="edit();">编辑</a>
				<a href="#" class="NorToolBtn AddBtn" onclick="add();">新增</a>
                    <a href="#" class="NorToolBtn DelBtn" onclick="removeJob();">禁用</a>
                <a href="#" class="NorToolBtn AddBtn" onclick="addJob();">启动</a>
        </div>
    </div>
	

</div>
	<div id="eventRecordContentDiv" region="center" border="false" style="width:100%; overflow:hidden;">
		<table id="list"></table>
	</div>
<#include "/component/customEasyWin.ftl" />
<#include "/component/ComboBox.ftl" />
<#include "/component/maxJqueryEasyUIWin.ftl" />

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
			singleSelect: true,
			idField:'timerId',
			url:'${rc.getContextPath()}/zhsq/timerManage/getData.json',
			frozenColumns:[[
//                {field:'ck',checkbox:true},
				{field:'timerId',title:'ID', align:'center',hidden:true}
			]],
			columns:[[
                {field:'timerName',title:'名称', align:'left',width:fixWidth(0.2)},
                {field:'taskClass',title:'taskClass', align:'center',width:fixWidth(0.35)},
                {field:'expression',title:'expression', align:'center',width:fixWidth(0.1)},
				{field:'timerRemark',title:'timerRemark', align:'center',width:fixWidth(0.1)},
				{field:'appCode',title:'appCode', align:'center',width:fixWidth(0.15)},
				{field:'status',title:'状态', align:'center',width:fixWidth(0.1),
                    formatter:function(value,rec,rowIndex){
                        var f;
                        if(value == '001'){
                            f = '禁用';
                        }else if(value == '002'){
                            f = '启用';
                        }else if(value == '003'){
                            f = '删除';
                        }
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
			queryParams:{},
			onSelect:function(index,rec){
				idStr=rec.timerId;
			}  
		});
		
		//设置分页控件
	    var p = $('#list').datagrid('getPager');
		$(p).pagination({
			pageSize: 20,//每页显示的记录条数，默认为
			pageList: [20,30,40,50],//可以设置每页记录条数的列表
			beforePageText: '第',//页数文本框前显示的汉字
			afterPageText: '页    共 {pages} 页',
			displayMsg: '当前显示第 {from} 到 {to} 条记录   共 {total} 条记录'/*,
			onBeforeRefresh:function(){
				$(this).pagination('loading');
				alert('before refresh');
				$(this).pagination('loaded');
			}*/
		});
    }

    function removeJob() {
        var rows = $('#list').datagrid('getSelections');
        var num=rows.length;
        if(num!=1){
            $.messager.alert('提示','请选中要编辑的数据再执行此操作!','warning');
            return;
        }else{
            var status=rows[0].status;
            if(status == '002'){
                var id=rows[0].timerId;
                var taskClass=rows[0].taskClass;
                var timerName=rows[0].timerName;
                var expression=rows[0].expression;

                $.messager.confirm('提示', '您确定禁用定时器吗？', function(r){
					if(r){
                        modleopen();
                        $.ajax({
                            type: "POST",
                            url: '${rc.getContextPath()}/zhsq/timerManage/removeJob.jhtml',
                            data: 'timerId='+id+'&expression='+expression+'&timerName='+timerName+'&taskClass='+taskClass+'&status=001',
                            dataType:"json",
                            success: function(data){
                                modleclose();
                                if(data.result){
                                    $.messager.alert('提示', '操作成功！', 'info');
                                    $("#list").datagrid('load');
                                }
                            },
                            error:function(data){
                                $.messager.alert('错误','连接超时！','error');
                            }
                        });
					}
                });
            }else{
                $.messager.alert('提示','操作失败!','warning');
                return;
            }
        }
    }

    function addJob() {
        var rows = $('#list').datagrid('getSelections');
        var num=rows.length;
        if(num!=1){
            $.messager.alert('提示','请选中要编辑的数据再执行此操作!','warning');
            return;
        }else{
            var status=rows[0].status;
			if(status == '001'){
                var id=rows[0].timerId;
                var taskClass=rows[0].taskClass;
                var timerName=rows[0].timerName;
                var expression=rows[0].expression;

                $.messager.confirm('提示', '您确定启动定时器吗？', function(r){
					if(r){
                        modleopen();
                        $.ajax({
                            type: "POST",
                            url: '${rc.getContextPath()}/zhsq/timerManage/addJob.jhtml',
                            data: 'timerId='+id+'&expression='+expression+'&timerName='+timerName+'&taskClass='+taskClass+'&status=002',
                            dataType:"json",
                            success: function(data){
                                modleclose();
                                if(data.result){
                                    $.messager.alert('提示', '操作成功！', 'info');
                                    $("#list").datagrid('load');
                                }
                            },
                            error:function(data){
                                $.messager.alert('错误','连接超时！','error');
                            }
                        });
					}
                });
			}else{
                $.messager.alert('提示','操作失败!','warning');
                return;
			}
        }
    }
    
	function edit() {
		var rows = $('#list').datagrid('getSelections');
		var num=rows.length;
		if(num!=1){
		   $.messager.alert('提示','请选中要编辑的数据再执行此操作!','warning');
		   return;
		}else{
            var id=rows[0].timerId;
            var url = '${rc.getContextPath()}/zhsq/timerManage/toEdit.jhtml?id='+id;
            showMaxJqueryWindow("编辑", url, 600, fixHeight(0.7));
		}
	}
	
	function add(){
		var url = '${rc.getContextPath()}/zhsq/timerManage/toEdit.jhtml';
		showMaxJqueryWindow("新增", url, 600, fixHeight(0.7));
	}
	
	function del() {
		var ids = [];
		var statusArry = [];
		var rows = $('#list').datagrid('getSelections');
		for(var i = 0; i<rows.length; i++){
			ids.push(rows[i].patrolId);
			statusArry.push(rows[i].status);
		}
		var idStr = ids.join(',');
		if(idStr == null || idStr == "") {
			$.messager.alert('提示','请选中要删除的数据再执行此操作!','warning');
			return false;
		}
		var guidang = 0;
		var shangbao = 0;
		for(var j = 0; j<statusArry.length; j++){
			if(statusArry[j]=='2'){
				guidang = guidang + 1;
			}else if(statusArry[j]=='1'){
				shangbao = shangbao + 1;
			}
		}
		if(guidang==0 && shangbao ==0 ){
			$.messager.confirm('提示', '您确定删除选中的信息吗？', function(r){
				if (r){
					modleopen();
					$.ajax({
						type: "POST",
						url: '${rc.getContextPath()}/zhsq/patrolController/del.jhtml',
						data: 'idStr='+idStr,
						dataType:"json",
						success: function(data){
							modleclose();
							$.messager.alert('提示', '您成功删除'+data.result+'条信息！', 'info');
							$("#list").datagrid('load');
						},
						error:function(data){
							$.messager.alert('错误','连接超时！','error');
						}
					});
				}
			});
		}else{
			$.messager.alert('提示','已上报或结案记录不能删除！','info');
			return;
		}
	}

	function show(recordId){
		if(!recordId){
		    $.messager.alert('提示','请选择一条记录！','info');
		}else{
		    var url = "${rc.getContextPath()}/zhsq/patrolController/show.jhtml?patrolId="+recordId+"&json="+Math.random();
		    showMaxJqueryWindow("查看巡防信息", url, 600, fixHeight(0.85));
		}
	}

	function searchData(b) {
		var a = new Array();
		var keyWord = $("#keyWord").val();
		if(keyWord!=null && keyWord!="" && keyWord!="") a["keyWord"]=keyWord;
		doSearch(a);
	}
	
	function doSearch(queryParams){
		$('#list').datagrid('clearSelections');
		$("#list").datagrid('options').queryParams=queryParams;
		$("#list").datagrid('load');
	}
	
	function resetCondition() {
		$("#keyWord").val("");
		$("#keyWord").attr('style','font-size:12px;font-style:italic;color:gray; width:210px;');
		searchData();
	}
	
	
</script>
</html>
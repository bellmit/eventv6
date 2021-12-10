<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>综治信息</title>
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/main_new.css" />
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/ligerUI/skins/Aqua/css/ligerui-common.css" />
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/ligerUI/skins/Aqua/css/ligerui-dialog.css" />
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-last/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-last/themes/icon.css">
<script src="${rc.getContextPath()}/js/jqueryeasyui-last/jquery.min.js" type="text/javascript"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-last/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-last/locale/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/ztree/zTreeStyle/zTreeStyle.css" />
<script type="text/javascript" src="${rc.getContextPath()}/js/ztree/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/ligerUI/js/core/base.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/ligerUI/js/plugins/ligerDialog.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/ligerUI/js/plugins/ligerResizable.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/ligerUI/js/plugins/ligerDrag.js"></script>
</head>
	<body class="easyui-layout">
		 <div id="zzGroupInfoDiv" region="center"  border="false" style="overflow:hidden;">
			<table id="zzGroupInfoList"></table>
		</div>
	 

    </body>
<script type="text/javascript">
var cbiId="${cbiId?c}";
//综治信息
$(function(){
	$('#zzGroupInfoList').datagrid({
		width:600,
		height:400,
		nowrap: false,
		striped: true,
		fit: true,
		idField:'corpCiRsId',
		url:'${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/corBaseZzGroupInfolistData.json',		
		columns:[[
		        {field:'name',title:'姓名', align:'center',width:100, formatter:function(value, rec, index){	
		           var e = '<a href="###" onclick="openCiRsResidentDetail('+rec.ciRsId+')">'+rec.name+'</a>&nbsp;';		         	        
		           return e;
		        }},				  		
				{field:'gender',title:'性别', align:'center',width:70},
				{field:'identityCard',title:'身份证号', align:'center',width:150},
				{field:'corpCiRsBirthdayStr',title:'出生日期', align:'center',width:100},               
                {field:'zzGroupDutyLabel',title:'综治职务', align:'center',width:100},
                {field:'corpDepartmentName',title:'工作部门', align:'center',width:200},
                {field:'curDuty',title:'工作职务', align:'center',width:100},              
                {field:'residentMobile',title:'联系电话', align:'center',width:100}
		]],
		toolbar:'#jqueryToolbar',
		pagination:true,
		queryParams:{cbiId:cbiId},
		onLoadSuccess:function(data){
		    $('#zzGroupInfoList').datagrid('clearSelections');	//清除掉列表选中记录
			if(data.total==0){					
				$('.datagrid-body-inner').eq(0).addClass("l_elist");					
				$('.datagrid-body').eq(1).append('<div class="r_elist">无数据</div>');					
			}else{
			    $('.datagrid-body-inner').eq(0).removeClass("l_elist");
			}
		}
	});

	//设置分页控件
    var p = $('#zzGroupInfoList').datagrid('getPager');
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
})


 function openCiRsResidentDetail(ciRsId){			
			var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/detail/"+ciRsId+".jhtml";				
			var win = $.ligerDialog.open({ 
				title:"人员信息",
				url:url,
				height:420,
				width:800,
				showMax:true,
				showToggle:false,
				showMin:false,
				isResize:true,
				slide:false,
				isDrag:true,
				isunmask:true,
				isMax:false,
				isClosed:false,
				buttons:null
			});
			//return win;
}
	</script>    
</html>
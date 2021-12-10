<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>城市经济运行</title>
<#include "/component/commonFiles-1.1.ftl" />
<script type="text/javascript" src="${uiDomain!''}/js/openJqueryEasyUIWin.js"></script>
</head>
<body class="easyui-layout">

<div class="MainContent">
	<#include "/szzg/statistics/toolbar_statistics.ftl" />
</div>

<div id="listDiv" region="center" border="false" style="width:100%; overflow:hidden;">
	<table id="list"></table>
</div>

<script type="text/javascript">
    var sData=[];
    $(function(){
    	var title = [{field:'dictName',title:'指标', align:'left',width:200}];
    	<#list title as k>
		    title.push({field:'s${k.DICT_ORDERBY}',title:'${k.DICT_NAME}', align:'center',width:100});
		    sData.push({name:'s${k.DICT_ORDERBY}',titleValid:"${k.DICT_REMARK!''}"});
		</#list>
	     loadDataList(title);
	});
	var list = null;
    function loadDataList(title){
    	list=$('#list').treegrid({
			width:600,
			height:300,
			//nowrap: false,
			rownumbers :true,
			//remoteSort :false,
			autoRowHeight:false,
			fit: true,
			fitColumns: true,
			singleSelect: true,
			treeField:'dictName',
			idField:'id',
			url:'${rc.getContextPath()}/zhsq/szzg/zgStatisticsController/listData.json',
			columns:[title],
			toolbar:'#jqueryToolbar',
			//pagination:true,
			//pageSize: 20,
			queryParams: {smonth:${currentMonth},syear :${currentYear},stype:'${stype}'},
			loadFilter : function(data,parentId){
				if(data.rows.length == 0){
					setTimeout(function(){
						$('.datagrid-body').eq(1).append('<div class="nodata"></div>');
					},1000);
					return data;
				}
				var colValid = {};
				for(var j=0,lj=sData.length;j<lj;j++){//列统一指定单位	'9,4|(万元)'
					var col = sData[j].titleValid?sData[j].titleValid.split("|")[1]:"['']";
					colValid[sData[j].name]=col;
				}
				for(var i=0,l=data.rows.length;i<l;i++){
					var d = data.rows[i];
					if(d.dictCode.length >7){//treegrid 默认树结构要有 _parentId 且根节点不能有 _parentId
	                   d['_parentId'] = d.dictPid;  
					}
                   d['id'] = d.dictId;
				   var rowValid={};
                   if(d.dictRemark && d.dictRemark!="none"){//行 指定单位's1:9,4;s2:9,4;s3:9,4|s1:(万㎡);s2:(万㎡);s3:(万㎡)'
						var row = d.dictRemark.split("|")[1].split(";");
						for(var k=0;k<row.length;k++){
							var r = row[k].split(":");
							d[r[0]] = d[r[0]]+r[1];
						}
				   }else{
						d.s1 =d.s1?(d.s1+isNull(colValid['s1'])):'',
						d.s2 =d.s2?(d.s2+isNull(colValid['s2'])):'',
						d.s3 =d.s3?(d.s3+isNull(colValid['s3'])):'',
						d.s4 =d.s4?(d.s4+isNull(colValid['s4'])):'';
				   }
				}
				return data;
			},
			onLoadError:function(){
				$('.datagrid-body-inner').eq(0).addClass("l_elist");
			}
		});
		
		//设置分页控件
	   /* var p = $('#list').datagrid('getPager');
		$(p).pagination({
			pageSize: 20,//每页显示的记录条数，默认为
			pageList: [20,30,40,50],//可以设置每页记录条数的列表
			beforePageText: '第',//页数文本框前显示的汉字
			afterPageText: '页    共 {pages} 页',
			displayMsg: '当前显示第 {from} 到 {to} 条记录   共 {total} 条记录'
		});*/
    }
    //-- 供子页调用的重新载入数据方法
	function reloadDataForSubPage(result){
		closeMaxJqueryWindow();
		DivShow(result.tipMsg);
		searchData();
	}
	function isNull(v){
		return v?v:'';
	}
	function arry2TreeFormat(sNodes){
		var r = [];
		var tmpMap = [];
		var id="dictId",pid="dictPid",children="children";
		for (i=0, l=sNodes.length; i<l; i++) {
			tmpMap[sNodes[i][id]] = sNodes[i];
		}
		for (i=0, l=sNodes.length; i<l; i++) {
			if (tmpMap[sNodes[i][pid]] && sNodes[i][id] != sNodes[i][pid]) {
				if (!tmpMap[sNodes[i][pid]][children])
					tmpMap[sNodes[i][pid]][children] = [];
				tmpMap[sNodes[i][pid]][children].push(sNodes[i]);
			} else {
				r.push(sNodes[i]);
			}
		}
		return r;
	}
	
</script>

</body>
</html>
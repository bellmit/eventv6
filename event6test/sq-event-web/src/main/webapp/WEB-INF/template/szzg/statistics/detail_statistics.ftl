<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>城市经济运行</title>
<#include "/component/commonFiles-1.1.ftl" />
<script type="text/javascript" src="${uiDomain!''}/js/TreeGrid.js"></script>
<style type="text/css">
.NorForm td{vertical-align:middle;}
.font{color:#7c7c7c;}
.unit7{line-height:28px;width:36px;text-align:left;color:#7c7c7c;margin:0 2px;}
</style>
</head>
<body  class="easyui-layout">
	<form id="tableForm" name="tableForm" action="${rc.getContextPath()}/zhsq/szzg/zgStatisticsController/saveOrUpdateByList.jhtml" method="post" Content-Type="application/json;charset=utf-8" >
		<div id="content-d" class="MC_con content light">
			<div class="NorForm">
				<div id="year_div"><label class="LabName" style="width:120px;font-size:14px;text-align:center;"><span>日期：${syear}年${smonth}月</span></label>
				</div>
				<div id="statitics">
					
				</div>	
			</div>
		</div>
		<div class="BigTool">
        	<div class="BtnList">
				<a href="#" onclick="cancel();" class="BigNorToolBtn CancelBtn">关闭</a>
            </div>
        </div>	
	</form>
	
</body>

<script type="text/javascript">
	var operation = true;
	$(function(){
		var title = [{dataField:'dictName',headerText:"<label class='font'>指标</label>",dataAlign: "left",width:'150'}],sData=[];
    	<#list title as k>
		    title.push({dataField:'s${k.DICT_ORDERBY}',headerText:"<label class='font'>${k.DICT_NAME}</label>", dataAlign:'center',width:100});
			sData.push({name:'s${k.DICT_ORDERBY}',titleValid:"${k.DICT_REMARK!''}"});
		</#list>
	   var config = {id: "xx",width: "100%",renderTo: "statitics",headerAlign: "center",headerHeight: "20",folderOpenIcon: "${rc.getContextPath()}/images/tree_open.png",
		folderCloseIcon: "${rc.getContextPath()}/images/tree_close.png",dataAlign: "center",indentation: "20",	hoverRowBackground: "false",folderColumnIndex: "0",	expandLayer:10,
		columns:title,data:[]};
			$.ajax({
				dataType : "json",
				url : '${rc.getContextPath()}/zhsq/szzg/zgStatisticsController/listData.json',
				data:{smonth:${smonth},syear :${syear},stype:'${stype}','join':"right",'t':Math.random()},
				success : function(data) {
					var arr = [];
					
					for(var i=0, l=data.rows.length; i<l; i++){
						var d = data.rows[i];
						var row ={id:d.dictId,pid:d.dictPid,dictName:"<label class='font'>"+d.dictName+"</label>"};
						//s1:9,4;s2:9,4;s3:3,2;
						var valids ={},units={},isRemarkNone = d.dictRemark=='none';
						if(d.dictRemark && !isRemarkNone){// 行 有特别指定验证规则
							var rowRemark = d.dictRemark.split('|')
							var rowValid = rowRemark[0].split(';'),rowUnit=rowRemark[1]?rowRemark[1].split(';'):[];
							for(var j1=0,m1=rowValid.length;j1<m1;j1++){
								var s = rowValid[j1].split(":");
								valids[s[0]] = s[1];
							}
							for(var j2=0,m2=rowUnit.length;j2<m2;j2++){
								var s2 = rowUnit[j2].split(":");
								units[s2[0]] = s2[1];
							}
						}
						for(var j=0, n=sData.length;j<n; j++){//[{name:'s1',titleValid:'9,4|(万元)'}]
							var v = isNull(d[sData[j].name]),
							rowRemark=sData[j].titleValid?sData[j].titleValid.split("|"):"['','']",//列统一验证
							rowValid=rowRemark[0],//数字验证
							rowUnit=rowRemark[1];//单位显示
							if(valids[sData[j].name]) {//行指定数字验证
								rowValid = valids[sData[j].name];
							}
							if(units[sData[j].name]) {//行指定单位显示
								rowUnit = units[sData[j].name];
							}
							rowUnit = rowUnit?rowUnit:'';
							row[sData[j].name]="<span>"+v+"</span><span style='display:"+(v.length==0?'none':'inline')+";' class='unit7'>"+rowUnit+"</span>";
						}
						arr.push(row);
					}
					config.data=arry2TreeFormat(arr);
					var treeGrid = new TreeGrid(config);
					treeGrid.show();
				}		
			});
	   
	});
	 
	function isNull(v){
		return operation?(v?v:''):'';
	}
	function arry2TreeFormat(sNodes){
		var r = [];
		var tmpMap = [];
		var id="id",pid="pid",children="children";
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
	
	function cancel(){
		parent.closeMaxJqueryWindow();
	}
	
</script>
</html>
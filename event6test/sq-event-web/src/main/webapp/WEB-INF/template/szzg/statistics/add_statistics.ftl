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
.unit7{line-height:28px;width:36px;float:left;text-align:left;color:#7c7c7c;margin:0 2px;}
</style>
</head>
<body  class="easyui-layout">
	<form id="tableForm" name="tableForm" action="${rc.getContextPath()}/zhsq/szzg/zgStatisticsController/saveOrUpdateByList.jhtml" method="post" Content-Type="application/json;charset=utf-8" >
		<div id="content-d" class="MC_con content light">
			<div class="NorForm">
				<div id="year_div"><label class="LabName"><span>日期：</span></label><input type="text" class="inp1 Wdate timeClass" id="syear"  value="${syear}-${smonth}"
					onClick="WdatePicker({readOnly:true,isShowOK:false,isShowClear:false, isShowToday:false,dateFmt:'yyyy-M'});"  style='width:100px'>
				</div>
				<label id="year_label" class="LabName" style="width:120px;font-size:14px;text-align:center;display:none;"><span>日期：${syear}年${smonth}月</span></label>
				<div id="statitics">
					
				</div>	
			</div>
		</div>
		<div class="BigTool">
        	<div class="BtnList">
        		<a href="#" onclick="tableSubmit();" class="BigNorToolBtn BigJieAnBtn">保存</a>
				<a href="#" onclick="cancel();" class="BigNorToolBtn CancelBtn">关闭</a>
            </div>
        </div>	
	</form>
	
</body>

<script type="text/javascript">
	var operation = true;
	$(function(){
		$("body").click();
		if('${operation}' == 'edit'){
			 document.getElementById('year_div').style.display='none';
			 document.getElementById('year_label').style.display='block';
   		}
		var title = [{dataField:'dictName',headerText:"<label class='font'>指标</label>",dataAlign: "left",width:'150'}],sData=[];
    	<#list title as k>
		    title.push({dataField:'s${k.DICT_ORDERBY}',headerText:"<label class='font'  style='margin-left:-50px;'>${k.DICT_NAME}</label>", dataAlign:'center',width:100});
			sData.push({name:'s${k.DICT_ORDERBY}',titleValid:"${k.DICT_REMARK!''}"});
		</#list>
	   var config = {id: "xx",width: "100%",renderTo: "statitics",headerAlign: "center",headerHeight: "20",folderOpenIcon: "${rc.getContextPath()}/images/tree_open.png",
		folderCloseIcon: "${rc.getContextPath()}/images/tree_close.png",dataAlign: "center",indentation: "20",	hoverRowBackground: "false",folderColumnIndex: "0",	expandLayer:10,
		columns:title,data:[]};
		    var syear = document.getElementById('syear').value.split('-') ;     
			$.ajax({
				dataType : "json",
				url : '${rc.getContextPath()}/zhsq/szzg/zgStatisticsController/listData.json',
				data:{smonth:syear[1],syear :syear[0],stype:'${stype}','join':"right",'t':Math.random()},
				success : function(data) {
					var arr = [];
					if(data.rows.length >0 && data.rows[0].seqId && '${operation}'=='add' ){
						operation = false;
						//$.messager.alert('提示','该年月数据已经存在不能再新增!','warning');
					}
					for(var i=0, l=data.rows.length; i<l; i++){
						var d = data.rows[i];
						var row ={id:d.dictId,pid:d.dictPid,dictName:"<label class='font'>"+d.dictName+"</label>",
						htmlHidden:"<input type='hidden' name='seqId' value='"+isNull(d.seqId)+"' /><input type='hidden' name='dictCode' value='"+d.dictCode+"' />"};
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
							var s=rowValid?rowValid.split(','):[0,0],z=s[0],f=s[1];
							
							row[sData[j].name]="<div style='display:"+(isRemarkNone?'none':'block')+";float: left;'><input class='inp1 easyui-validatebox validatebox-text' type='text' id='"+i+"_"+j+"' style='width:120px' title='"+v+"' value='"+v+"' name='"+sData[j].name
							+"' onblur='change(this,"+z+","+f+")'/></div><div style='display:"+(isRemarkNone?'none':'block')+";' class='unit7'>"+rowUnit+"</div>";
						}
						arr.push(row);
					}
					config.data=arry2TreeFormat(arr);
					var treeGrid = new TreeGrid(config);
					treeGrid.show();
				}		
			});
	   
	});
	 
	function change(o,z,f){//o 输入框对象 ,z 整数位,f 浮点位
		if(o.value.length == 0){
			o.data = "";
			return;
		}
		
		if(!isNum(z) || z==0){z = 9;}//整数位数
		if(!isNum(f)){f = 0;}//小数位数
		var reg = null;
		if(z == 9 && f == 4){
			reg = new RegExp(/^(([1-9][0-9]{0,8}(\.[0-9]{1,4})?)|(0(\.[0-9]{1,4})?))$/g);
		}else if (z == 9 && f == 0){
			reg = new RegExp(/^(([1-9][0-9]{0,8})|(0))$/g);
		}else if (z == 4 && f == 2){
			reg = new RegExp(/^-?(([1-9][0-9]{0,3}(\.[0-9]{1,2})?)|(0(\.[0-9]{1,2})?))$/g);
		}else{
			$.messager.alert('错误','参数有误!');
			return;
		}
		if(!reg.test(o.value)){
			layer.tips("<span style='color:#000'>"+(f==0?"最多填写"+z+"位整数！":"最多填写"+z+"位整数,保留"+f+"位小数!")+"</span>", "#"+o.id, {
			  tips: [1, '#c9e3ff'],
			  time: 2000
			});
			o.value = o.title!=undefined?o.title:"";
			return;
		}
		o.title = o.value;
	}
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
	function formSubmit(){
		var params = [];
		var syear = document.getElementById('syear').value.split('-') ;
		$("#statitics tr").each(function(row){
		   var inputs = $(this).find("input");
		   if(inputs.length >0){
		   		var obj = {'syear':syear[0],'smonth':syear[1],stype:'${stype}',createId:'${userId}',updateId:'${userId}'};
			   for(var i=0;i<inputs.length;i++){
			   		obj[inputs[i].name] = inputs[i].value;
			   		if('${operation}' == 'add' && inputs[i].name == 'seqId'){
			   			obj['seqId'] = '';
			   		}
			   }
			   params.push(obj);
		   }
		 })
		$.ajax({
			type: "POST",
			url: "${rc.getContextPath()}/zhsq/szzg/zgStatisticsController/saveOrUpdateByList.jhtml",
			data : JSON.stringify(params),
			contentType : 'application/json;charset=utf-8',
			dataType: "json",
			success: function(data) {
				if(data !=null && data.tipMsg == undefined && data.length>100){
						$.messager.alert('错误','提交保存出现异常，请联系管理员！','error');
						return;
					}
					window.parent.reloadDataForSubPage(data);
			},
			error:function(data){
				$.messager.alert('错误','连接超时！','error');
			}
		});
	}
	function tableSubmit(m){
		if('${operation}' == 'edit' && operation){
			var isFalse = false;
			$("#statitics tr").each(function(row){
				   var inputs = $(this).find("input");
				   if(inputs.length >0 && !isFalse){
					   for(var i=0;i<inputs.length;i++){
					   		if(inputs[i].name == 'seqId' && inputs[i].value == ""){
					   			isFalse = true;
					   			return;
					   		}
					   }
				   }
				 });
			if(isFalse){
				$.messager.alert('提示',"数据seqid为空", 'error');
				return;
			}
			formSubmit();
			return;
		}
		var syear = document.getElementById('syear').value.split('-') ;
		params={'syear':syear[0],'smonth':syear[1],stype:'${stype}',};
		 $.ajax({
			type: "POST",
			url: "${rc.getContextPath()}/zhsq/szzg/zgStatisticsController/findCount.jhtml",
			data : params,
			dataType: "json",
			success: function(data) {
				if(data>0){
					$.messager.alert('提示',syear[0]+"年"+syear[1]+"月数据已经存在", 'error');
					return;
				}
				formSubmit();
				
			},
			error:function(data){
				$.messager.alert('错误','连接超时！','error');
			}
		});
	}
	
	function cancel(){
		parent.closeMaxJqueryWindow();
	}
	
</script>
</html>
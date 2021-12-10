<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<#include "/component/commonFiles-1.1.ftl" />
<#include "/component/ComboBox.ftl" />
<#include "/component/AnoleDate.ftl">
<link href="${rc.getContextPath()}/css/fillreport.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${rc.getContextPath()}/js/fillreport.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/json/json2.js"></script>
<style>
	tr{cursor:default;}
	td.back1{background:#e4e4e4; font-weight:bold; text-align:center;}
	td.back2{background:#f5f5f5;}
	td{background:#fff; padding:0 5px; height:40px; text-align:left;}
	.GreyBack{font-weight:bold;}
</style>
</head>

<body>
<!-- 
  桥							A	T1 T2 T3
  涵洞							B	T1 T2 T3
  隧道							C	T1 T2 T3
  公跨立交						D	T1 T2 T3
  车站数						E	T1
  行政村数						F	T1
  总户数（铁路两侧2.5公里内）			G	T1
  总人口数（铁路两侧2.5公里内）		H	T1
  学校（铁路两侧2.5公里内）			J	T1 T2
  工厂（铁路两侧2.5公里内）			K	T1 T2
  水库（铁路两侧2.5公里内）			L	T1 T2
  耕牛总数						M	T1
  五残人员数						N	T1
  非行人通道数					O	T1
  废旧金属收购站点数				P	T1
  铁路周边天网工程摄像头数			Q	T1
  护路队伍						R	T1 T2 T3 T4 T5
  备注							S	T1
  总里程						T	T
  -->

<input type="hidden" id="gridId" name="gridId" value="<#if fillReport.gridId??>${fillReport.gridId}</#if>"/>
<input type="hidden" id="kvJson" name="kvJson" value='<#if fillReport.kvJson??>${fillReport.kvJson}</#if>'/>
<input type="hidden" id="rptId" name="rptId" value="<#if fillReport.rptId??>${fillReport.rptId}</#if>"/>
<input type="hidden" id="rptOrg" name="rptOrg" value="<#if fillReport.rptOrg??>${fillReport.rptOrg}</#if>"/>
<input type="text" id="status" name="status" value="<#if fillReport.status??>${fillReport.status}</#if>" style="display:none;"/>
<div class="Mc_con">
<div class="con">
<h1>重大决策社会稳定风险评估信息（季度）</h1>
<table border=0 cellpadding=0 cellspacing=1>
	<tr>
		<td colspan="15" width="120" align="center" class="back2">
			<table border=0 cellpadding=0 cellspacing=0>
				<tr>
					<td width="120" align="center" class="back2">所属网格</td>
					<td width="240"><#if fillReport.gridName??>${fillReport.gridName}</#if></td>
					<td width="120" align="center" class="back2">报送时间</td>
					<td width="240"><#if fillReport.dateDesc??>${fillReport.dateDesc}</#if></td>
					<td width="120" align="center" class="back2">签发领导</td>
					<td width="240"><#if fillReport.checker??>${fillReport.checker}</#if></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td rowspan="2" colspan="2" width="80" align="center" class="back1">&nbsp;</td>
		<td rowspan="2" width="40" align="center" class="back1">报备</td>
		<td rowspan="2" width="40" align="center" class="back1">正在评估</td>
		<td rowspan="2" width="40" align="center" class="back1">完成评估</td>
		<td colspan="3" width="120" align="center" class="back1">准予实施</td>
		<td colspan="3" width="120" align="center" class="back1">暂缓实施</td>
		<td colspan="3" width="120" align="center" class="back1">不予实施</td>
		<td rowspan="2" width="120" align="center" class="back1">备注</td>
	</tr>
	<tr>
		<td width="40" align="center" class="back1">完成实施</td>
		<td width="40" align="center" class="back1">进行中</td>
		<td width="40" align="center" class="back1">尚未实施</td>
		<td width="40" align="center" class="back1">完成实施</td>
		<td width="40" align="center" class="back1">进行中</td>
		<td width="40" align="center" class="back1">尚未实施</td>
		<td width="40" align="center" class="back1">完成实施</td>
		<td width="40" align="center" class="back1">进行中</td>
		<td width="40" align="center" class="back1">尚未实施</td>
	</tr>
	<tr>
		<td rowspan="3" width="10" align="center" class="back1">项目分类</td>
		<td width="70" align="center" class="back1">重大工程项目
		<input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700100A_T0" type="hidden" value=""/>
		</td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700100A_T1" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700100A_T2" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700100A_T3" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700100A_T4" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700100A_T5" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700100A_T6" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700100A_T7" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700100A_T8" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700100A_T9" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700100A_T10" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700100A_T11" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700100A_T12" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700100A_T13" data-options="required:true,validType:['maxLength[400]','characterCheck']" type="text" class="inp1 easyui-validatebox" style="width:95%;" value="0"/></td>
	</tr>
	
	<tr>
		<td width="70" align="center" class="back1">重大决策制定
		<input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700100B_T0" type="hidden" value=""/>
		</td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700100B_T1" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700100B_T2" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700100B_T3" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700100B_T4" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700100B_T5" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700100B_T6" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700100B_T7" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700100B_T8" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700100B_T9" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700100B_T10" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700100B_T11" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700100B_T12" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700100B_T13" data-options="required:true,validType:['maxLength[400]','characterCheck']" type="text" class="inp1 easyui-validatebox" style="width:95%;" value="0"/></td>
	</tr>
	<tr>
		<td width="70" align="center" class="back1">其它重大决策
		<input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700100C_T0" type="hidden" value=""/>
		</td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700100C_T1" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700100C_T2" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700100C_T3" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700100C_T4" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700100C_T5" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700100C_T6" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700100C_T7" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700100C_T8" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700100C_T9" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700100C_T10" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700100C_T11" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700100C_T12" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700100C_T13" data-options="required:true,validType:['maxLength[400]','characterCheck']" type="text" class="inp1 easyui-validatebox" style="width:95%;" value="0"/></td>
	</tr>
	<tr>
		<td rowspan="8" width="10" align="center" class="back1">项目类别</td>
		<td width="70" align="center" class="back1">农村征地
		<input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200D_T0" type="hidden" value=""/>
		</td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200D_T1" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200D_T2" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200D_T3" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200D_T4" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200D_T5" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200D_T6" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200D_T7" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200D_T8" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200D_T9" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200D_T10" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200D_T11" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200D_T12" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200D_T13" data-options="required:true,validType:['maxLength[400]','characterCheck']" type="text" class="inp1 easyui-validatebox" style="width:95%;" value="0"/></td>
	</tr>
	<tr>
		<td width="70" align="center" class="back1">城镇拆迁
		<input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200E_T0" type="hidden"  value=""/>
		</td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200E_T1" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200E_T2" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200E_T3" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200E_T4" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200E_T5" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200E_T6" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200E_T7" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200E_T8" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200E_T9" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200E_T10" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200E_T11" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200E_T12" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200E_T13" data-options="required:true,validType:['maxLength[400]','characterCheck']" type="text" class="inp1 easyui-validatebox" style="width:95%;" value="0"/></td>
	</tr>
	<tr>
		<td width="70" align="center" class="back1">农民负担
		<input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200F_T0" type="hidden" value=""/>
		</td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200F_T1" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200F_T2" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200F_T3" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200F_T4" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200F_T5" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200F_T6" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200F_T7" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200F_T8" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200F_T9" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200F_T10" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200F_T11" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200F_T12" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200F_T13" data-options="required:true,validType:['maxLength[400]','characterCheck']" type="text" class="inp1 easyui-validatebox" style="width:95%;" value="0"/></td>
	</tr>
	<tr>
		<td width="70" align="center" class="back1">国企改制
		<input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200G_T0" type="hidden" value=""/>
		</td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200G_T1" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200G_T2" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200G_T3" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200G_T4" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200G_T5" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200G_T6" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200G_T7" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200G_T8" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200G_T9" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200G_T10" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200G_T11" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200G_T12" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200G_T13" data-options="required:true,validType:['maxLength[400]','characterCheck']" type="text" class="inp1 easyui-validatebox" style="width:95%;" value="0"/></td>
	</tr>
	<tr>
		<td width="70" align="center" class="back1">环境影响
		<input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200H_T0" type="hidden" value=""/>
		</td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200H_T1" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200H_T2" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200H_T3" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200H_T4" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200H_T5" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200H_T6" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200H_T7" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200H_T8" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200H_T9" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200H_T10" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200H_T11" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200H_T12" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200H_T13" data-options="required:true,validType:['maxLength[400]','characterCheck']" type="text" class="inp1 easyui-validatebox" style="width:95%;" value="0"/></td>
	</tr>
	<tr>
		<td width="70" align="center" class="back1">社会保障
		<input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200I_T0" type="hidden" value=""/>
		</td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200I_T1" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200I_T2" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200I_T3" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200I_T4" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200I_T5" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200I_T6" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200I_T7" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200I_T8" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200I_T9" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200I_T10" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200I_T11" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200I_T12" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200I_T13" data-options="required:true,validType:['maxLength[400]','characterCheck']" type="text" class="inp1 easyui-validatebox" style="width:95%;" value="0"/></td>
	</tr>
	<tr>
		<td width="70" align="center" class="back1">公益事业
		<input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200J_T0" type="hidden"  value=""/>
		</td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200J_T1" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200J_T2" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200J_T3" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200J_T4" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200J_T5" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200J_T6" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200J_T7" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200J_T8" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200J_T9" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200J_T10" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200J_T11" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200J_T12" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200J_T13" data-options="required:true,validType:['maxLength[400]','characterCheck']" type="text" class="inp1 easyui-validatebox" style="width:95%;" value="0"/></td>
	</tr>
	<tr>
		<td width="70" align="center" class="back1">其它类型
		<input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200K_T0" type="hidden" value=""/>
		</td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200K_T1" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200K_T2" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200K_T3" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200K_T4" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200K_T5" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200K_T6" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200K_T7" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200K_T8" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200K_T9" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200K_T10" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200K_T11" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 easyui-numberbox" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200K_T12" data-options="required:true,validType:['maxLength[12]','characterCheck']" type="text" class="inp1 c" precision="0" style="width:95%;" value="0"/></td>
		<td><input data-fillreport id="${fillReport.rptType}_${fillReport.rptCatalog}_B89700200K_T13" data-options="required:true,validType:['maxLength[400]','characterCheck']" type="text" class="inp1 easyui-validatebox" style="width:95%;" value="0"/></td>
	</tr>
</table>
</div>
</div>


<script>
	$(function() {
		$(".Mc_con").height($(window).height());
		$(".easyui-validatebox").attr("readonly",true);
		$(".easyui-numberbox").attr("readonly",true);
		FillReport.setJson($("#kvJson").val());
	});
	
	function tableSubmit(saveType) {
		var isValid =  $("#tableForm").form('validate');
		if(isValid) {
			if($("#rptId").val() == "") {
				var flag = checkIsHaveSameData();
				if(flag == "true") {
					$.messager.alert('友情提示',$("#gridName").val()+$("#dateDesc").val()+"报表已经存在了！",'warning'); 
					return;
				}
			}
			var sJson = FillReport.getJson();
			$("#kvJson").val(sJson);
			$("#status").val(saveType == 'pub' ? '1' : '0');
			modleopen();
			$("#tableForm").submit();
			
		}
	}
function checkIsHaveSameData() {
	var gridId = $("#gridId").val();
	var dateDesc = $("#dateDesc").val();
	var flag=false;
	$.ajax({   
		 url: '${rc.getContextPath()}/gmis/fillreport/riskEvalRptItemController/isHaveSameData.json?t='+Math.random(),
		 type: 'POST',
		 timeout: 3000,
		 dataType:"json",
		 data: { gridId:gridId,dateDesc:dateDesc},
		 async: false,
		 error: function(data){
		 	$.messager.alert('友情提示','检验报表数据出现异常!','warning'); 
		 },
		 success: function(data){
		    flag = data.flag;
		 }
	 });
	 return flag;
}
FillReport.setJson = function(sJson) {
	if (sJson && sJson.length > 0) {
		var rows = eval('(' + sJson + ')');
		if (rows && rows.length > 0) {
			for (var i in rows) {
				var key = rows[i]["key"];
				var val = rows[i]["val"];
				var e = $("#" + key);
				if (e.is("input")) {
					if(!isNaN(val)) {
						try {
							e.numberbox('setValue', val);
						} catch(err) {
							e.val(val);
						}
					}else {
						e.val(val);
					}
				} else {
					e.html(val);
				}
			}
		}
	}
};
	
</script>
</body>
</html>

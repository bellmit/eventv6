<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>框选统计</title>
</head>

<script type="text/javascript">
</script>

<body  style="border:none;background-color: rgb(255,255,255);">
	<DIV id="mainpanel" class="pageContent" style="overflow: hidden;">
		 <table width="100%" class="jcsj-searchList-2 " cellpadding="1" cellspacing="1" border="0" >
		 	<tr>
				<td colspan="5" bgcolor="#f4f4f4" style="height: 30px;font-size: 14px;font-weight: bold;"><img src="${rc.getContextPath()}/jsp/scim/gis/img/executive.png" width="13px" height="13px"/>&nbsp;楼宇统计数据</td>
			</tr>
			<tr>
				<th >楼栋总数</th>
				<td colspan="4"  ><#if results??>${results['buildNum']}</#if></td>
			</tr>
			<tr>
				<th >在建楼栋数</th>
				<td colspan="6"  ><#if results??>${results['buildingNum']}</#if></td>
			</tr>
			<tr>
				<th >已建楼栋数</th>
				<td colspan="4"  ><#if results??>${results['buildedNum']}</#if></td>
			</tr>
			<tr>
				<th >商务楼栋数</th>
				<td colspan="4"  ><#if results??>${results['buildsw']}</#if></td>
			</tr>
			<tr>
				<th >商住楼栋数</th>
				<td colspan="4"  ><#if results??>${results['buildsz']}</#if></td>
			</tr>
		 	<tr>
				<td colspan="8" bgcolor="#f4f4f4" style="height: 30px;font-size: 14px;font-weight: bold;"><img src="${rc.getContextPath()}/jsp/scim/gis/img/executive.png" width="13px" height="13px"/>&nbsp;企业统计数据</td>
			</tr>
			<tr>
				<th >企业总数</th>
				<td colspan="4"  ><#if results??>${results['corpNum']}</#if></td>
			</tr>
			<tr>
				<th >中国100强</th>
				<td colspan="4"  ><#if results??>${results['corpCountryTopNum']}</#if></td>
			</tr>
			<tr>
				<th >世界500强</th>
				<td colspan="4"  ><#if results??>${results['corpWorkTopNum']}</#if></td>
			</tr>
			<tr>
				<th >入盘企业数</th>
				<td colspan="4"  ><#if results??>${results['corpDivisionNum']}</#if></td>
			</tr>
			<tr>
				<th >出口企业数</th>
				<td colspan="4"  ><#if results??>${results['corpExportNum']}</#if></td>
			</tr>
			<tr>
				<th >上市企业数</th>
				<td colspan="4"  ><#if results??>${results['corpMarketNum']}</#if></td>
			</tr>
		</table>
	</DIV>
</body>
</html>

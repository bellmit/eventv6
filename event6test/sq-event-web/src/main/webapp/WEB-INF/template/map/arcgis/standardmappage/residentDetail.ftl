<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script src="${rc.getContextPath()}/js/global.js"></script>
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/main.css" />
<title>房屋居民信息</title>
</head>
<body>
<style>
	body {
	    font-size: 12px;
	    margin: 0;
	    padding: 0;
	}
	.nav{
		height:30px;
		line-height:30px;
		background:url(searchFormBg.png) repeat-x;
		width:100%;
		position:fixed;
		top:0;
	
	}
	.tmenu{
		float:right;
		margin-right:10px;
		width:200px;
		text-align:right;
	}
	.tmenu a{
		margin:0 10px;
		color:#000;
		font-size:12px;
		text-decoration:none;
	}
	.tit_top{
		float:left;
		margin-left:10px;
		width:200px;
	}
	.main_c{
		margin-top:5px;
	}
	.headbox{
		background: none repeat scroll 0 0 #7BAFDE;
	    border-bottom: 3px solid #184AA1;
	    font-weight: normal;
	    padding: 10px;
		text-align:center;

		margin:0 auto;

	}
	.headbox span {
	    display: inline-block;
	    padding: 3px;
	    text-align: left;
	    width: 200px;
	
	}	
	a{color:#000; text-decoration:none;}
	fieldset {
	    margin: 5px auto;
	    width: 750px;
		background:#fff;
	}
	.list {
	    background: none repeat scroll 0 0 #CCCCCC;
	    color: #444444;
	    line-height: 23px;
	    margin: 5px;
	    width: 98.5%;
		font-size:12px;
	}
	.list th {
	    background: none repeat scroll 0 0 #EEEEEE;
	    border: 0 none;
	    padding: 0;
	    text-align: right;
	    white-space: nowrap;
	}
	.list td {
	    background: none repeat scroll 0 0 #FFFFFF;
	    text-align: left;
	    width: 20%;
	}
	legend {
	    color: #0000FF;
	    font-weight: bold;
	    padding: 0 5px;
		font-size:14px;
	}
	.btn_back {
	    background-image: url("${rc.getContextPath()}/images/btn_back.gif");
	    background-repeat: no-repeat;
	    border: 0 solid #888888;
	    height: 22px;
	    width: 57px;
		cursor:pointer;
	}
</style>
<div class="main_c">
	<div style="width:750px; margin:20px auto">
		<#if residentDetail??>
			<table cellspacing="1" cellpadding="5" border="0" class="list">
				<tbody>
		        	<tr>
						<th rowspan="9">个人照片</th>
						<td rowspan="9">
							<img src="${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/photo/${residentDetail.CI_RS_ID?c}.jhtml" border=0 style="width: 150px; height: 200px" />
						</td>
						<th>姓名</th>
						<td><#if residentDetail.I_NAME??>${residentDetail.I_NAME}</#if></td>
						<th>性别</th>
						<td>
							<#if residentDetail.I_GENDER??>
								<#if residentDetail.I_GENDER=="M">
									男
								<#else>
									女
								</#if>
							</#if>
						</td>
						<th>出生日期</th>
						<td>
							<#if residentDetail.BIRTHDAY_STR??>
								${residentDetail.BIRTHDAY_STR}
							</#if>
						</td>
					</tr>
					<tr>
		                <th>身份证号</th>
		                <td><#if residentDetail.I_IDENTITY_CARD??>${residentDetail.I_IDENTITY_CARD}</#if></td>
		                <th>籍贯</th>
						<td><#if residentDetail.RESIDENT_BIRTHPLACE??>${residentDetail.RESIDENT_BIRTHPLACE}</#if></td>
						<th>户籍地</th>
						<td><#if residentDetail.I_RESIDENCE??>${residentDetail.I_RESIDENCE}</#if></td>
					</tr>
		            <tr>
						<th>政治面貌</th>
						<td><#if residentDetail.RESIDENT_POLITICS??>${residentDetail.RESIDENT_POLITICS}</#if></td>
						<th>民族</th>
						<td><#if residentDetail.I_ETHNIC??>${residentDetail.I_ETHNIC}</#if></td>
						<th>婚姻状况</th>
						<td stringMap="I_MARRIAGE"><#if residentDetail.I_MARRIAGE??>${residentDetail.I_MARRIAGE}</#if></td>
					</tr>
					<tr>
						<th>文化程度</th>
						<td><#if residentDetail.I_EDUCATION??>${residentDetail.I_EDUCATION}</#if></td>
						<th>职业</th>
						<td><#if residentDetail.I_CAREER??>${residentDetail.I_CAREER}</#if></td>
						<th>工作单位</th>
						<td><#if residentDetail.I_ORGANIZATION??>${residentDetail.I_ORGANIZATION}</#if></td>
					</tr>
					<tr>
						<th>家庭编号</th>
						<td><#if residentDetail.I_FAMILY_SN??>${residentDetail.I_FAMILY_SN}</#if></td>
						<th>家庭地址</th>
						<td><#if residentDetail.I_FAMILY_ADDRESS??>${residentDetail.I_FAMILY_ADDRESS}</#if></td>
						<th>住房性质</th>
						<td><#if residentDetail.I_HOUSE_SOURCE??>${residentDetail.I_HOUSE_SOURCE}</#if></td>
					</tr>
		            <tr>
						<th>与户主关系</th>
						<td><#if residentDetail.I_HOLDER_RELATION??>${residentDetail.I_HOLDER_RELATION}</#if></td>
						<th>人户状态</th>
						<td><#if residentDetail.I_HOUSE_RESIDE??>${residentDetail.I_HOUSE_RESIDE}</#if></td>
						<th>人口类型</th>
						<td stringMap="I_TYPE"><#if residentDetail.I_TYPE??>${residentDetail.I_TYPE}</#if></td>
					</tr>
					<tr>
						<th>家庭电话</th>
						<td><#if residentDetail.I_PHONE??>${residentDetail.I_PHONE}</#if></td>
						<th>办公电话</th>
						<td><#if residentDetail.OPHONE_NUM??>${residentDetail.OPHONE_NUM}</#if></td>
						<th>移动电话</th>
						<td><#if residentDetail.RESIDENT_MOBILE??>${residentDetail.RESIDENT_MOBILE}</#if></td>
					</tr>
					<tr>
						<th>国籍</th>
						<td><#if residentDetail.RESIDENT_NATIONALITY??>${residentDetail.RESIDENT_NATIONALITY}</#if></td>
						<th>现居住地址</th>
						<td colspan="3"><#if residentDetail.I_RESIDENCE_ADDR??>${residentDetail.I_RESIDENCE_ADDR}</#if></td>
					</tr>
					<tr>
						<th>临时居住人员</th>
						<td><#if residentDetail.IS_TEMPORARY_RESIDENCE??>${residentDetail.IS_TEMPORARY_RESIDENCE}</#if></td>
						<th>备注</th>
						<td colspan="3"><#if residentDetail.REMARK??>${residentDetail.REMARK}</#if></td>
					</tr>
				</tbody>
			</table>
		</#if>
	</div>				
</div>
<script src="${rc.getContextPath()}/js/global.js"></script>
<script type="text/javascript">
	if(window["globalDictionary"]){
		with(window["globalDictionary"]){
			//婚姻字典
			addColumn("I_MARRIAGE", "001", "未婚");
			addColumn("I_MARRIAGE", "002", "初婚");
			addColumn("I_MARRIAGE", "003", "再婚");
			addColumn("I_MARRIAGE", "004", "复婚");
			addColumn("I_MARRIAGE", "005", "丧偶");
			addColumn("I_MARRIAGE", "006", "离婚");
			addColumn("I_MARRIAGE", "007", "其他");
			//人口类型字典
			addColumn("I_TYPE", "001", "常住人口");
			addColumn("I_TYPE", "002", "流动人口");
			addColumn("I_TYPE", "003", "临时常住人口");
			addColumn("I_TYPE", "004", "临时居住人口");
			addColumn("I_TYPE", "005", "台胞");
			addColumn("I_TYPE", "006", "户籍人口");
			addColumn("I_TYPE", "007", "挂户人口");
		}
		
		$(function (){
			window["globalDictionary"]._initForInnerHtml(); 
		});
    }
</script>
</body>
</html>
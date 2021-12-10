<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>楼房-详情</title>
<#include "/component/commonFiles-map-1.1.ftl" />
</head>
<body>
    <div class="MetterList">
    	<div class="box">
        	<div class="MetterContent">
                <div class="ConList">
                    <div class="nav" id="tab">
                        <ul>
                            <li class="current">基本信息</li>
                            <li id="cirInfoLi">人员信息</li>
                        </ul>
                    </div>
                    <div class="ListShow">
                    	<!-----------------------------------基本信息-------------------------------------->
                        <div class="tabs2">
                        	<div class="NorForm ThreeColumn" style="overflow-y:auto;height:331px;">
	                            <table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td class="LeftTd"><label class="LabName"><span>所属网格：</span></label><div class="Check_Radio FontDarkBlue"><#if homeInfo.gridName??>${homeInfo.gridName}</#if></div></td>
										<td><label class="LabName"><span>家庭编号：</span></label><div class="Check_Radio FontDarkBlue"><#if homeInfo.familySn??>${homeInfo.familySn}</#if></div></td>
									</tr>
									<tr>
										<td class="LeftTd"><label class="LabName"><span>户口本编号：</span></label><div class="Check_Radio FontDarkBlue"><#if homeInfo.householderNo??>${homeInfo.householderNo}</#if></div></td>
										<td><label class="LabName"><span>户口性质：</span></label><div class="Check_Radio FontDarkBlue"><#if homeInfo.householdCharacter??>${homeInfo.householdCharacter}</#if></div></td>
									</tr>
									<tr>
										<td class="LeftTd"><label class="LabName"><span>户主姓名：</span></label><div class="Check_Radio FontDarkBlue"><#if homeInfo.houseHoldName??>${homeInfo.houseHoldName}</#if></div></td>
										<td><label class="LabName"><span>联系方式：</span></label><div class="Check_Radio FontDarkBlue"><#if homeInfo.telephone??>${homeInfo.telephone}</#if></div></td>
									</tr>
									<tr>
										<td class="LeftTd"><label class="LabName"><span>档案编号：</span></label><div class="Check_Radio FontDarkBlue"><#if homeInfo.archivesNo??>${homeInfo.archivesNo}</#if></div></td>
										<td><label class="LabName"><span>户类型：</span></label><div class="Check_Radio FontDarkBlue"><#if homeInfo.lowIncomeCN??>${homeInfo.lowIncomeCN}</#if></div></td>
									</tr>
									<tr>
										<td class="LeftTd"><label class="LabName"><span>零就业：</span></label><div class="Check_Radio FontDarkBlue"><#if homeInfo.zeroEmployedCN??>${homeInfo.zeroEmployedCN}</#if></div></td>
										<td><label class="LabName"><span>单亲家庭：</span></label><div class="Check_Radio FontDarkBlue"><#if homeInfo.spFamilyCN??>${homeInfo.spFamilyCN}</#if></div></td>
									</tr>
									<tr>
										<td colspan="2" class="LeftTd"><label class="LabName"><span>家庭地址：</span></label><div class="Check_Radio FontDarkBlue" style="width:600px;"><#if homeInfo.familyAddress??>${homeInfo.familyAddress}</#if></div></td>
									</tr>
									<tr>
										<td colspan="2" class="LeftTd"><label class="LabName"><span>户籍地址：</span></label><div class="Check_Radio FontDarkBlue" style="width:600px;"><#if homeInfo.householderAddress??>${homeInfo.householderAddress}</#if></div></td>
									</tr>
								</table>
							</div>
                        </div>
                    	<!-----------------------------------人员信息-------------------------------------->
                        <div class="tabs2 hide" style="margin:0;height:328px;width:100%;overflow:auto">
                        	<table id="cirsInfoList" border="false"></table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

	<script type="text/javascript">
		//选项卡切换
		var $NavDiv2 = $(".ConList ul li");
			$NavDiv2.click(function(){
				  $(this).addClass("current").siblings().removeClass("current");
				  var NavIndex2 = $NavDiv2.index(this);
				  $(".ListShow .tabs2").eq(NavIndex2).show().siblings().hide();
	   	});
	</script>
	
	<script type="text/javascript">
		//家庭人员信息列表
		$('#cirInfoLi').click(function(){
			$('#cirsInfoList').datagrid({
				nowrap: true,
				striped: true,
				fit: true,
				fitColumns:true,
				singleSelect:true,
				scrollbarSize:0,
				url:'${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfPrecisionPoverty/cirListData.json',
                pageSize: 20,
				columns:[[
                    {field:'name',title:'姓名', align:'center',width:100, formatter:function(value, rec, index){
				        var f = '<a href="###" onclick="showPeople('+ rec.ciRsId+ ')">'+value+'</a>&nbsp;';
				        return f;
				     }},
                    {field:'holderRelation',title:'与户主关系', align:'center',width:100},
                    {field:'genderCN',title:'性别', align:'center',width:50},
				    {field:'identityCard',title:'公民身份证号码', align:'center',width:100},
                    {field:'residenceAddr',title:'现住地址', align:'center',width:200}
				]],
				toolbar:'#jqueryToolbar',
				pagination:true,  
				queryParams:{familyId:'${homeInfo.familyId}'},
				onLoadSuccess:function(data){
				   $('#cirsInfoList').datagrid('clearSelections'); //清除掉列表选中记录 
				   if(data.total==0){ 
				      var body = $(this).data().datagrid.dc.body2; 
				      body.append('<div style="text-align: center;"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/nodata.png" title="暂无数据"/></div>'); 
				   }
                },
                onLoadError:function(){
				   var body = $(this).data().datagrid.dc.body2; 
				   body.append('<div style="text-align: center;">数据加载出错！</div>'); 
            	}
			});
			
			//设置分页控件
            var p = $('#cirsInfoList').datagrid('getPager');
			$(p).pagination({
				pageSize: 20,//每页显示的记录条数，默认为20
				pageList: [20,30,40,50],//可以设置每页记录条数的列表
				beforePageText: '第',//页数文本框前显示的汉字
				afterPageText: '页    共 {pages} 页',
				displayMsg: '当前显示第 {from} 到 {to} 条记录   共 {total} 条记录'
			});
		});
		
        function showPeople(cirId){
		    var url = '${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfPrecisionPoverty/rsPoorHoldDetail.jhtml?cirId='+cirId;
		    showMaxJqueryWindow('人员基础信息',url,656,338,'no');
		}
	</script>
	<#include "/component/maxJqueryEasyUIWin.ftl" />
</body>
</html>

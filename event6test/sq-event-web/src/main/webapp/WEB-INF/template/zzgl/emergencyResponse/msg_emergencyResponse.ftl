<!DOCTYPE html">
<html">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<#include "/component/commonFiles-1.1.ftl" />
<link href="${rc.getContextPath()}/css/emergency_response.css" rel="stylesheet" type="text/css" />
<!--插件如语音盒 使用js-->
<script type="text/javascript" src="${rc.getContextPath()}/js/plugIn/plug_in.js"></script>
</head>
<body>
<form id="emergencyResponseForm" name="emergencyResponseForm" action="" method="post">
	<div id="content-d" class="MC_con content light boxe">
	    <div class="MetterCon">
	        <h1>${title}</h1>
	        <textarea id="details" class="area2 easyui-validatebox" data-options="required:true,tipPosition:'bottom',validType:['maxLength[1000]','characterCheck']">${event.happenTimeStr}在${event.gridName}发生了<#if event.involvedNumName?? && event.involvedNum?? && event.involvedNum!='00'>${event.involvedNumName}的</#if>${event.eventClass}事件</textarea>
	    </div>
	    <#if important?? &&  (important=='1' || important=='2')>
		    <div class="h_20"></div>
			<div class="MetterList">
				<div class="title">
		        	<div class="fl">选择相关领导</div>
		        </div>
		        <div class="h_10"></div>
		        <div class="list" id="relatedLeaderDiv">
		            <table id="relatedLeaderDiv_table" width="100%" border="0" cellspacing="1" cellpadding="0">
		              <tr class="TabHeader">
		                <td width="30"><input type="checkbox" class="che1" checked value="relatedLeaderCheck" id="relatedLeaderCheck" onclick="selectAllStreet(this,'relatedLeaderCheck','relatedLeaderDiv')"/></td>
		                <td width="8%">部门名称</td>
		                <td width="8%">职务</td>
		                <td width="8%">姓名</td>
		                <td width="10%">联系方式</td>
		                <td>职责描述</td>
		              </tr>
		              <tbody id="relatedLeaderDiv_tbody"></tbody>
		            </table>
				</div>
		    </div>
	    </#if>
	    <div class="h_20"></div>
		<div class="MetterList">
			<div class="title">
	        	<div class="fl">选择相关部门</div>
	        </div>
	        <div class="h_10"></div>
	        <div class="list" id="relatedDepartmentDiv">
	            <table id="relatedDepartmentDiv_table" width="100%" border="0" cellspacing="1" cellpadding="0">
	              <tr class="TabHeader">
	                <td width="30"><input type="checkbox" class="che1" checked value="relatedDepartmentCheck" id="relatedDepartmentCheck" onclick="selectAllStreet(this,'relatedDepartmentCheck','relatedDepartmentDiv')"/></td>
	                <td width="8%">部门名称</td>
	                <td width="8%">职务</td>
	                <td width="8%">姓名</td>
	                <td width="10%">联系方式</td>
	                <td>职责描述</td>
	              </tr>
	              <tbody id="relatedDepartmentDiv_tbody"></tbody>
	            </table>
			</div>
	    </div>
	    <div class="h_20"></div>
		<div class="MetterList">
			<div class="title">
	        	<div class="fl">选择相关街道</div>
	        </div>
	        <div class="h_10"></div>
	        <div class="list" id="relatedStreetDiv">
	            <table id="relatedStreetDiv_table" width="100%" border="0" cellspacing="1" cellpadding="0">
	              <tr class="TabHeader">
	                <td width="30"><input type="checkbox" class="che1" value="relatedStreetCheck" id="relatedStreetCheck" onclick="selectAllStreet(this,'relatedStreetCheck','relatedStreetDiv')"/></td>
	                <td width="8%">街道名称</td>
	                <td width="8%">部门名称</td>
	                <td width="8%">职务</td>
	                <td width="8%">姓名</td>
	                <td width="10%">联系方式</td>
	                <td>职责描述</td>
	              </tr>
	              <tbody id="relatedStreetDiv_tbody"></tbody>
	            </table>
			</div>
	    </div>
	</div>
	<div class="BigTool">
		<div class="BtnList">
			<a href="#" class="BigNorToolBtn BigShangBaoBtn" onclick="sendMessage();">发送短信</a>
			<a href="#" class="BigNorToolBtn CancelBtn" onclick="closeWin();">取消</a>
	    </div>
	</div>
</form>
	
<script>
	(function($){ 
	    $(window).load(function(){ 
	        var options = { 
	            axis : "yx", 
	            theme : "minimal-dark" 
	        }; 
	        enableScrollBar('content-d',options); 
	        enableScrollBar('content-md',options);
	    }); 
	    
	    <#if important?? &&  (important=='1' || important=='2')>
	    	relatedLeaders();//构建相关领导
	    </#if>
	    relatedDeparments();//构建相关部门
	    relatedStreets();//构建相关街道
	    
	    selectDefaultStreet();//勾选默认街道
		 
	})(jQuery);
	
	function sendMessage(){
		var telephoneAndDuty = "";//需要使用的电话号码和职责描述组合
		var validateFlag = true;//判读勾选的职责描述是否有空值
		var isValid =  $("#emergencyResponseForm").form('validate');//判读各输入值是否均有效
		
		if(isValid){
			$('input[type=checkbox]:checked').each(function(i){//获取勾选的chexkbox
				var checkboxId = $(this).attr('id');
				
				if(checkboxId!=undefined && checkboxId!=null){
					$('a[name="'+checkboxId+'_telephone"]').each(function(index){
						var telephone = $("#"+checkboxId+"_telephone_"+index).text();
						var duty = $("#"+checkboxId+"_duty_"+index).val();
						
						if(telephone!=null && telephone!=""){
					        if(duty!=null && duty!=""){
					        	telephoneAndDuty += telephone + ',' + duty + ";";
					        }else{
					        	validateFlag = false;
					        	$.messager.alert('警告','职责描述不能为空！','warning',function(){
					        		$("#"+checkboxId+"_duty_"+index).focus();//定位到首个为空的文本域
					        	});
					        	return false;//结束循环
					        }
					    }
					});
		        }
		    });
		    
		    if(validateFlag){
			    if(telephoneAndDuty == ""){
			    	$.messager.alert('警告','请先选择要发送短信人员！','warning');
			    }else{
			    	var actionUrl = encodeURI("${rc.getContextPath()}/zhsq/emergencyResponse/sendMessage.jhtml?importantType=${importantType}&telephoneAndDuty="+telephoneAndDuty+"&details="+$("#details").val());
			    	$("#emergencyResponseForm").attr("action", actionUrl);
			    	
			    	modleopen();
				  	$("#emergencyResponseForm").ajaxSubmit(function(data) {
				  		closeWin();
					});
			    }
		    }
	    }
	}

	function selectAllStreet(obj,checkboxValue,divId){//全选、全不选
		var value=$(obj).val();
		var select;
	    if(value == checkboxValue){
	   		select=divId;
	    }
		if($("#"+value)[0].checked){ 
		    //从div1中获取所有属性type=checkbox的input元素
		    $('#'+select+ ' input[type=checkbox]').each(function(i){
		        this.checked=true;
		    });
		}else{//取消反选的选中状态
		    $('#'+select+ ' input[type=checkbox]').each(function(i){
		        this.checked=false;
		    });
		}
	}
	
	function selectDefaultStreet(){//默认勾选所属街道
		var gridCode = "<#if gridCode??>${gridCode}</#if>";
	    $('input[type=checkbox][name='+gridCode+']').each(function(i){//默认勾选所属街道
	    	this.checked = true;
	    });
	}
	
	function closeWin(){//关闭当前窗口
		parent.closeMaxJqueryWindow();
	}
	
	function relatedLeaders(){//构建相关领导
		var relatedLeaderDiv_tbody = document.getElementById("relatedLeaderDiv_tbody");
		var imgPath = "${uiDomain!''}/images/cloundcall.png";
		var contextPath = "${rc.getContextPath()}";
		var checkName = "leaderCheck";
		
		var leadersNameAndTel = [<#if important?? &&  important=='1'>
								{"name":"陈曾勇", "telephone":"13509308686"},{"name":"李凡", "telephone":"13599063555"},
								</#if>
								{"name":"何长嘉", "telephone":"13906938720"},{"name":"吴勤", "telephone":"13705992333"},
								{"name":"黄建雄", "telephone":"13960700033"},{"name":"郑则传", "telephone":"13509369330"}];
		
		for(var index = 0, len = leadersNameAndTel.length; index < len; index++){
			var leadersTr = document.createElement("tr");
			var name = leadersNameAndTel[index].name;
			var telephone = leadersNameAndTel[index].telephone;
			var checkId = checkName + '_' + index;
			var html = 
		        '<td><input class="che1" type="checkbox" checked id="'+checkId+'" /></td>'+
		        '<td>区指挥中心</td>'+
		        '<td>领导</td>'+
		        '<td>'+name+'</td>'+
		        '<td><a name="'+checkId+'_telephone" id="'+checkId+'_telephone_0" href="javascript:showVoiceCall(\''+contextPath+'\', window.parent.showCustomEasyWindow, \''+telephone+'\',\''+name+'\');">'+telephone+'<img title="语音呼叫" src="'+imgPath+'"></a></td>'+
		        '<td><textarea name="'+checkId+'_duty" id="'+checkId+'_duty_0" cols="" rows="" class="area1 easyui-validatebox" data-options="tipPosition:\'bottom\',validType:[\'maxLength[1000]\',\'characterCheck\']">请赶赴现场指挥工作。</textarea></td>';
		    relatedLeaderDiv_tbody.appendChild(leadersTr);
		    $(leadersTr).html(html);
		}
	}
	
	function relatedDeparments(){//构建相关部门
		var relatedDepartmentDiv_tbody = document.getElementById("relatedDepartmentDiv_tbody");
		var imgPath = "${uiDomain!''}/images/cloundcall.png";
		var contextPath = "${rc.getContextPath()}";
		var checkName = "departmentCheck";
		
		var departmentNameAndTel = [
								   <#if event.type?? &&  event.type=='0216'><!--医患聚集-->
									   {"departmentName":"政法委", "positionName":"副书记", "name":"翁翔", "telephone":"13609558199", "duty":"做好参与街道、部门在现场工作的组织协调为领导决策提出意见"},
									   <#if important?? &&  important=='3'>
										   {"departmentName":"司法局", "positionName":"副局长", "name":"潘榕", "telephone":"13763829611", "duty":"带领医调中心人员第一时间赶赴现场，主要负责：向医患双方宣传有关法律法规引导其正确途径解决，并引导到区医调中心调处。市属医院由其通知到市相关部门到场。"},
										   {"departmentName":"医调中心", "positionName":"中心主任", "name":"徐建闽", "telephone":"13067216739", "duty":"带领医调中心人员第一时间赶赴现场，主要负责：向医患双方宣传有关法律法规引导其正确途径解决，并引导到区医调中心调处。市属医院由其通知市相关部门到场"},
										   {"departmentName":"卫生局", "positionName":"副局长", "name":"林秀玲", "telephone":"13593987177", "duty":"带领工作人员第一时间赶赴现场，主要负责：现场调解疏导等协调工作"},
									   <#else>
										   {"departmentName":"司法局", "positionName":"局长", "name":"吴燕娇", "telephone":"13515028632", "duty":"带领医调中心人员第一时间赶赴现场，主要负责：向医患双方宣传有关法律法规引导其正确途径解决，并引导到区医调中心调处。市属医院由其通知市相关部门到场。"},
										   {"departmentName":"医调中心", "positionName":"中心主任", "name":"徐建闽", "telephone":"13067216739", "duty":"带领医调中心人员第一时间赶赴现场，主要负责：向医患双方宣传有关法律法规引导其正确途径解决，并引导到区医调中心调处。市属医院由其通知市相关部门到场"},
										   {"departmentName":"卫生局", "positionName":"局长", "name":"林茂", "telephone":"13705075684", "duty":"带领工作人员第一时间赶赴现场，主要负责：现场调解疏导等工作"},
										   <#if important?? &&  important=='1'>
											   {"departmentName":"公安分局", "positionName":"局长", "name":"黄绍兴", "telephone":"13905008566", "duty":"按公安预案要求负责做好现场事态控制治安秩序维护、交通管制疏导、取证、处置等工作协助做好调处。"},
										   </#if>
										   {"departmentName":"公安分局", "positionName":"副局长", "name":"陈远政", "telephone":"13805099161", "duty":"按公安预案要求负责做好现场事态控制治安秩序维护、交通管制疏导、取证、处置等工作协助做好调处。"},
										   {"departmentName":"治安大队", "positionName":"队长", "name":"董影", "telephone":"13705070939", "duty":"按公安预案要求负责做好现场事态控制治安秩序维护、交通管制疏导、取证、处置等工作协助做好调处。"},
										   {"departmentName":"交巡大队", "positionName":"大队长", "name":"张勉", "telephone":"13805051717", "duty":"按公安预案要求负责做好现场事态控制治安秩序维护、交通管制疏导、取证、处置等工作协助做好调处。"},
									   </#if>
								   </#if>
								   
								   <#if event.type?? &&  event.type=='0217'><!--讨薪聚集-->
									   {"departmentName":"政法委", "positionName":"副书记", "name":"翁翔", "telephone":"13609558199", "duty":"做好参与街道、部门在现场工作的组织协调为领导决策提出意见"},
									   <#if important?? &&  important=='1'>
									   	   {"departmentName":"人社局", "positionName":"局长", "name":"林清", "telephone":"13600828311", "duty":"带领人社局监察大队第一时间赶赴现场，主要负责：召集相关负责人了解情况，开展调解、取证、核实依法做好处置工作"},
									   </#if>
									   {"departmentName":"人社局", "positionName":"分管领导", "name":"许宝堤", "telephone":"13509332270", "duty":"带领人社局监察大队第一时间赶赴现场，主要负责主要负责：召集相关负责人了解情况，开展调解、取证、核实依法做好处置工作"},
									   {"departmentName":"人社局", "positionName":"监察大队队长", "name":"黄成贵", "telephone":"13905015151", "duty":"召集相关负责人了解情况，开展调解、取证、核实依法做好处置工作"},
									   <#if important?? &&  (important=='1' || important=='2')>
										   {"departmentName":"建设局", "positionName":"局长", "name":"耿雨", "telephone":"13850172909", "duty":"负责区属工地，负责联系工地负责人、施工方进行协调并协助人社局做好处置工作"},
									   </#if>
									   <#if important?? &&  important=='1'>
									   	   {"departmentName":"公安分局", "positionName":"局长", "name":"黄绍兴", "telephone":"13905008566", "duty":"按公安预案要求负责做好现场事态控制治安秩序维护、交通管制疏导、取证、处置等工作协助做好调处。"},
									   </#if>
									   <#if important?? &&  (important=='1' || important=='2')>
										   {"departmentName":"公安分局", "positionName":"副局长", "name":"陈远政", "telephone":"13805099161", "duty":"按公安预案要求负责做好现场事态控制治安秩序维护、交通管制疏导、取证、处置等工作协助做好调处。"},
										   {"departmentName":"治安大队", "positionName":"队长", "name":"董影", "telephone":"13705070939", "duty":"按公安预案要求负责做好现场事态控制治安秩序维护、交通管制疏导、取证、处置等工作协助做好调处。"},
										   {"departmentName":"交巡大队", "positionName":"大队长", "name":"张勉", "telephone":"13805051717", "duty":"按公安预案要求负责做好现场事态控制治安秩序维护、交通管制疏导、取证、处置等工作协助做好调处。"},
									   </#if>
									   <#if important?? &&  important=='3'>
									   	   {"departmentName":"建设局", "positionName":"负责工地", "name":"杨峰", "telephone":"13178110916", "duty":"负责区属工地，负责联系工地负责人、施工方进行协调并协助人社局做好处置工作"},
									   </#if>
								   </#if>
								   
								   <#if event.type?? &&  event.type=='0218'><!--信访聚集-->
									   {"departmentName":"政法委", "positionName":"副书记", "name":"翁翔", "telephone":"13609558199", "duty":"做好参与街道、部门在现场工作的组织协调为领导决策提出意见"},
									   <#if important?? &&  (important=='1' || important=='2')>
									   	   {"departmentName":"信访局", "positionName":"局长", "name":"郑文青", "telephone":"13459415598", "duty":"通知相关部门领导到位，并带领信访干部第一时间到现场进行疏导，稳定群众情绪，同时向上级反馈情况"},
									   </#if>
									   {"departmentName":"信访局", "positionName":"副局长", "name":"郑健康", "telephone":"13805063608", "duty":"通知相关部门领导到位，并带领信访干部第一时间到现场进行疏导，稳定群众情绪，同时向上级反馈情况"},
									   <#if important?? &&  (important=='1' || important=='2')>
									   	   {"departmentName":"广电局", "positionName":"局长", "name":"戴天惠", "telephone":"13609559685", "duty":"带领2摄像人员赴现场开展处置现场录像取证工作。"},
									   </#if>
									   <#if important?? &&  important=='1'>
									   	   {"departmentName":"公安分局", "positionName":"局长", "name":"黄绍兴", "telephone":"13905008566", "duty":"按公安预案要求负责做好现场事态控制治安秩序维护、交通管制疏导、取证、处置等工作协助做好调处。"},
									   </#if>
									   <#if important?? &&  (important=='1' || important=='2')>
										   {"departmentName":"公安分局", "positionName":"副局长", "name":"陈远政", "telephone":"13805099161", "duty":"按公安预案要求负责做好现场事态控制治安秩序维护、交通管制疏导、取证、处置等工作协助做好调处。"},
										   {"departmentName":"治安大队", "positionName":"队长", "name":"董影", "telephone":"13705070939", "duty":"按公安预案要求负责做好现场事态控制治安秩序维护、交通管制疏导、取证、处置等工作协助做好调处。"},
										   {"departmentName":"交巡大队", "positionName":"大队长", "name":"张勉", "telephone":"13805051717", "duty":"按公安预案要求负责做好现场事态控制治安秩序维护、交通管制疏导、取证、处置等工作协助做好调处。"},
									   </#if>
								   </#if>
								   {}
								   ];
								   
		for(var index = 0, len = departmentNameAndTel.length-1; index < len; index++){
			var tr = document.createElement("tr");
			var departmentName = departmentNameAndTel[index].departmentName;
			var positionName = departmentNameAndTel[index].positionName;
			var name = departmentNameAndTel[index].name;
			var telephone = departmentNameAndTel[index].telephone;
			var duty = departmentNameAndTel[index].duty;
			var checkId = checkName + '_' + index;
			var html = 
		        '<td><input type="checkbox" class="che1" checked id="'+checkId+'" /></td>'+
		        '<td>'+departmentName+'</td>'+
		        '<td>'+positionName+'</td>'+
		        '<td>'+name+'</td>'+
		        '<td><a name="'+checkId+'_telephone" id="'+checkId+'_telephone_0" href="javascript:showVoiceCall(\''+contextPath+'\', window.parent.showCustomEasyWindow, \''+telephone+'\',\''+name+'\');">'+telephone+'<img title="语音呼叫" src="'+imgPath+'"></a></td>'+
		        '<td><textarea name="'+checkId+'_duty" id="'+checkId+'_duty_0" cols="" rows="" class="area1 easyui-validatebox" data-options="tipPosition:\'bottom\',validType:[\'maxLength[1000]\',\'characterCheck\']">'+duty+'</textarea></td>';
		    relatedDepartmentDiv_tbody.appendChild(tr);
		    $(tr).html(html);
		}
	}
	
	function relatedStreets(){//构建相关街道
		var relatedStreetDiv_tbody = document.getElementById("relatedStreetDiv_tbody");
		var imgPath = "${uiDomain!''}/images/cloundcall.png";
		var contextPath = "${rc.getContextPath()}";
		var checkName = "streetCheck";
		
		var gridCodeAndName = [{"gridCode":"350103010","gridName":"鳌峰街道"},{"gridCode":"350103001","gridName":"苍霞街道"},
							  {"gridCode":"350103002","gridName":"洋中街道"},{"gridCode":"350103003","gridName":"新港街道"},
							  {"gridCode":"350103008","gridName":"宁化街道"},{"gridCode":"350103004","gridName":"义洲街道"},
							  {"gridCode":"350103007","gridName":"瀛洲街道"},{"gridCode":"350103006","gridName":"上海街道"},
							  {"gridCode":"350103005","gridName":"茶亭街道"},{"gridCode":"350103009","gridName":"后洲街道"}];
		var positionNameAndDuty = [
								 <#if important?? && (important=='1' || important=='2')>
								 {"departmentName":"街道办", "positionName":"书记", "duty":"带领街道、社区干部至现场，主要负责：现场秩序维护和聚集人员安抚工作"},
								 </#if>
								 {"departmentName":"街道办", "positionName":"综治副书记", "duty":"带领街道、社区干部至现场，主要负责：现场秩序维护和聚集人员安抚、疏导、疏散工作"},
								 {"departmentName":"派出所", "positionName":"派出所所长", "duty":"带领所民警至现场，主要负责：聚集现场治安维护和交通疏导等工作，协助控制事态和做好调处工作"}
								 ];
		var streetsNameAndTel = [
								{"name":"林立清", "telephone":"13720833881"},{"name":"黄建华", "telephone":"13805057616"},{"name":"林锋", "telephone":"13906928717"},
								{"name":"徐超", "telephone":"13906909919"},{"name":"游兆敏", "telephone":"13600813717"},{"name":"王军", "telephone":"13950399077"},
								{"name":"辛承翠", "telephone":"13809512125"},{"name":"赵劲", "telephone":"13805048615"},{"name":"曾诚", "telephone":"13600812199"},
								{"name":"何贞铨", "telephone":"13599417858"},{"name":"李岩忠", "telephone":"13696834744"},{"name":"郭忠勇", "telephone":"13905005563"},
								{"name":"陈子湘", "telephone":"13705088593"},{"name":"许志清", "telephone":"13615001571"},{"name":"余洪海", "telephone":"13959111717"},
								{"name":"杨强", "telephone":"13705933135"},{"name":"陈峰", "telephone":"13905020327"},{"name":"林忠", "telephone":"13905007707"},
								{"name":"刘友书", "telephone":"13905906179"},{"name":"魏岱合", "telephone":"13906938697"},{"name":"陈翀", "telephone":"13905908908"},
								{"name":"张统廉", "telephone":"13809550199"},{"name":"周大明", "telephone":"15060666833"},{"name":"王榕生", "telephone":"13809506568"},
								{"name":"林朝樑", "telephone":"13906915237"},{"name":"林政春", "telephone":"13003823635"},{"name":"林岩", "telephone":"13906900183"},
								{"name":"林志勇", "telephone":"13696869933"},{"name":"陈永忠", "telephone":"13960929897"},{"name":"陈建民", "telephone":"13905909295"}
								];
		
		
		var checkId = "";//checkbox编号
		var indexInterval = 3;//人员信息每组长度
		
		<#if important?? && important=='3'>
			var streetsNameAndTelTmp = [];
			indexInterval = 2;
			
			for(var index = 0, len = streetsNameAndTel.length; index < len; index++){//去除街道办 书记一列
				if(index % 3 != 0){
					streetsNameAndTelTmp.push(streetsNameAndTel[index]);
				}
			}
			streetsNameAndTel = streetsNameAndTelTmp;
		</#if>
		
		for(var index = 0, len = streetsNameAndTel.length; index < len; index++){
			var tr = document.createElement("tr");
			var positionIndex = index % indexInterval;//获取职责的下标
			var name = streetsNameAndTel[index].name;
			var telephone = streetsNameAndTel[index].telephone;
			var departmentName = positionNameAndDuty[positionIndex].departmentName;
			var positionName = positionNameAndDuty[positionIndex].positionName;
			var duty = positionNameAndDuty[positionIndex].duty;
			var html = "";
			
			if(positionIndex == 0){//创建相关街道的checkbox
				var gridIndex = index / indexInterval;//获取网格的下标
				var gridCode = gridCodeAndName[gridIndex].gridCode;
				var gridName = gridCodeAndName[gridIndex].gridName;
				checkId = checkName + '_' + index;
				html +=
					'<td rowspan="'+indexInterval+'"><input type="checkbox" class="che1" name="'+gridCode+'" id="'+checkId+'"/></td>'+
			        '<td rowspan="'+indexInterval+'">'+gridName+'</td>';
		    }
			html += 
		        '<td>'+departmentName+'</td>'+
		        '<td>'+positionName+'</td>'+
		        '<td>'+name+'</td>'+
		        '<td><a name="'+checkId+'_telephone" id="'+checkId+'_telephone_'+positionIndex+'" href="javascript:showVoiceCall(\''+contextPath+'\', window.parent.showCustomEasyWindow, \''+telephone+'\',\''+name+'\');">'+telephone+'<img title="语音呼叫" src="'+imgPath+'"></a></td>'+
		        '<td><textarea name="'+checkId+'_duty" id="'+checkId+'_duty_'+positionIndex+'" cols="" rows="" class="area1 easyui-validatebox" data-options="tipPosition:\'bottom\',validType:[\'maxLength[1000]\',\'characterCheck\']">'+duty+'</textarea></td>';
		    relatedStreetDiv_tbody.appendChild(tr);
		    $(tr).html(html);
		}
	}
</script>
</body>
</html>

<!-- 
#选择居民
说明：
#1、入口函数showResidentSelector，参数：无
#2、引用页面需要有回调函数residentCallback，参数：users
 -->
<style>
.involed{
	text-align:center;
	line-height:30px;
	padding-right:5px;
	font-weight:bold;
	background:#eff0f1;
	color:#000;
	font-size:12px;
	border-bottom:1px solid #bed3df;
}
.border_b table{
	line-height:30px;
}
</style>
<div id="involedPeopleSelector" class="easyui-window" title="选择居民" minimizable="false" maximizable="false" collapsible="false" closed="true" modal="false" style="width:550px;height:342px;padding:1px;overflow:hidden;">
	<div id="content-d" class="MC_con content light">
	<div class="easyui-layout" style="width:514px;height:320px;">
		<div region="north" style="height:35px;border-bottom:1px solid #bed3df;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="50" align="right">姓名：</td>
			    <td width="145">
			    	<input type="text" class="inp1" id="name" name="name" value="" style="width:140px;" />
			    </td>
			    <td width="65" align="right">公民身份号码：</td>
			    <td width="185">
			    	<input type="text" class="inp1" id="idCard" name="idCard" value="" style="width:175px;"/>
			    </td>
				<td>
					<a href="#" class="NorToolBtn BlueBtn" onclick="javascript:addPeople();"><img src="${rc.getContextPath()}/images/add.png" />添加</a>
				</td>
			</tr>
			</table>
		</div>
		<div region="center">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="involed" width="120px;">姓名</td>
					<td class="involed" width="230px;">公民身份号码</td>
					<td class="involed" width="100px;">操作</td>
				</tr>
				<tbody id="involvedPeopleList" align="center">
					
				</tbody>
			</table>
		</div>
		<div class="BigTool" region="south" style="height:67px; background:#FAFAFA; padding-top:10px;">
				<div class="BtnList" style="">
					<a href="#" onclick="javascript:closeInvoledPeopleSelector();" class="BigNorToolBtn GreenBtn"><img src="${rc.getContextPath()}/ui/images/cancel.png" />取消</a>
					<a href="#" onclick="javascript:addInfo();" class="BigNorToolBtn BlueBtn"><img src="${rc.getContextPath()}/ui/images/finish2.png" />确定</a>
				</div>
		</div>	
	</div>
	</div>
</div>

<script type="text/javascript">
	var involedPeopleSelectorWin;
	function showInvoledPeopleSelector() {
	    //根据eventInvolvePeople值 初始化表格 数据
	    $("#involvedPeopleList").html("");//清空
		var eventInvolvedPeople = $("#eventInvolvedPeople").val();
		if (eventInvolvedPeople != null && eventInvolvedPeople != "") {
			var eventInvolvedPeopleArray = eventInvolvedPeople.split("；");
			$.each(eventInvolvedPeopleArray, function(i, n){
				var items = n.split("，");
				if(items.length > 1){//去除不完整的数据，或者因尾部“；”导致多余的条目。  20140208
					var name = items[1];
					var idCard = items[2];
					addPeopleHtml(name, idCard);
				}
			})
		}
	    $involedPeopleSelectorWin = $('#involedPeopleSelector').window({
	    	title:"添加人员",
	    	width: 530,
	    	height: 358,
	    	top:($(window).height()-400)*0.5+$(document).scrollTop(),
		    left:($(window).width()-600)*0.5+$(document).scrollLeft(),
	    	shadow: true,
	    	modal:true,
	    	closed:true,
	    	minimizable:false,
	    	maximizable:false,
	    	collapsible:false
	    });
		$involedPeopleSelectorWin.window('open');
	}
	function closeInvoledPeopleSelector() {
		$involedPeopleSelectorWin.window('close');
	}
	function addPeople() {
		if ($("#name").val() == "") {
			$.messager.alert('提示','请输入姓名!','warning');
			$("#name").focus();
			return;
		}
		if ($("#idCard").val() == "") {
			$.messager.alert('提示','请输入身份证号','warning');
			$("#idCard").focus();
			return;
		}
		
		var name=$.trim($("#name").val());
		var idCard=$.trim($("#idCard").val());
		
		if (isIdCardNo()) {
			var isPeopleAdded = false;
			$("#involvedPeopleList tr").each(function(){//判断人员信息是否重复添加
				var name_ = $("td", this).eq(0).text();
				var idCard_ = $("td", this).eq(1).text();
				if(name==name_ && idCard==idCard_){
					isPeopleAdded = true;
					$.messager.alert('提示','该人员信息已添加','warning');
					return false;//break; 
					//return true;//continue;
				}
			});
			
			if(!isPeopleAdded){//没有增添的人员信息，才新增
				addPeopleHtml(name,idCard);
				$("#name").val("");
				$("#idCard").val("");
			}
		}
		
		
		
	}
	
	function addPeopleHtml(name, idCard) {
		var html = "<tr><td class=\"border_b\">" + name + "</td><td class=\"border_b\">" + idCard + 
		"</td><td class=\"border_b\"><a style=\"cursor:pointer\" onclick='$(this).parent().parent().remove();'>删除</a>&nbsp;" + 
		"|&nbsp;<a style=\"cursor:pointer\" onclick='viewResidentInfo(\"" + name + "\",\"" + idCard + "\");'>查看</a></td></tr>";
		$("#involvedPeopleList").append(html); 
	}
	function addInfo() {
		//if ($("#involvedPeopleList tr").size() == 0) {
			//判断是否已添加过人员，没有，则要求添加
			//$.messager.alert('请添加人员','您还未添加人员，请添加！', 'info');
			//return false;
		//}
		var users = "";
		$("#involvedPeopleList tr").each(function(){
			if (users != "") {
				users += "；";
			}
			users +="001" + "，" + $("td", this).eq(0).text() + "，" + $("td", this).eq(1).text();
		});
		closeInvoledPeopleSelector();
		involedPeopleCallback(users);
	}
	function viewResidentInfo(name,idCard){
		$.ajax({
			type: "POST",
			url: "${rc.getContextPath()}/resident/getCiRsIdByNameAndIdentityCard.jhtml?name=" + encodeURIComponent(encodeURIComponent(name)) + "&identityCard=" + idCard,
			async : false,
			success: function(data){
				if (data.ciRsId>0) {
					viewResidentInfoById(data.ciRsId);
				} else {
					$.messager.alert('错误','不存在该居民用户信息！', 'error');
				}
			},
			error:function(data){
				$.messager.alert('错误','连接超时，请重试！', 'error');
			}
		});
	}
	function viewResidentInfoById(ciRsId) {
		var url = "${rc.getContextPath()}/resident/detail/"+ciRsId+".jhtml";
		if(typeof(parent.showCustomEasyWindow) != 'undefined'){
			parent.showCustomEasyWindow("人员基本信息",url,770,445);
		}else if(typeof(parent.showMaxJqueryWindow) != 'undefined'){
			parent.showMaxJqueryWindow("人员基本信息",url,766,445);
		}else{
			$.messager.alert('错误','人员基本信息查看失败！', 'error');
		}
	}


	//这个可以验证15位和18位的身份证，并且包含生日和校验位的验证。  
	function isIdCardNo()
	{
		var num = $.trim($("#idCard").val());
		num = num.toUpperCase();
		if(num==""||num==null){
			return false;
		}
        //公民身份号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X。  
        if (!(/(^\d{15}$)|(^\d{17}([0-9]|X)$)/.test(num))){
        	$.messager.alert('错误','输入的身份证号长度不对，或者号码不符合规定！\n15位号码应全为数字，18位号码末位可以为数字或X。','error');
            return false;
    	}
		//校验位按照ISO 7064:1983.MOD 11-2的规定生成，X可以认为是数字10。
		//下面分别分析出生日期和校验位
		var len, re;
		len = num.length;
		if (len == 15){
			re = new RegExp(/^(\d{6})(\d{2})(\d{2})(\d{2})(\d{3})$/);
			var arrSplit = num.match(re);
			//检查生日日期是否正确
			var dtmBirth = new Date('19' + arrSplit[2] + '/' + arrSplit[3] + '/' + arrSplit[4]);
			var bGoodDay;
			bGoodDay = (dtmBirth.getYear() == Number(arrSplit[2])) && ((dtmBirth.getMonth() + 1) == Number(arrSplit[3])) && (dtmBirth.getDate() == Number(arrSplit[4]));
			if (!bGoodDay){
	        	$.messager.alert('错误','输入的身份证号里出生日期不对！','error');  
	            return false;
			}else{
				//将15位身份证转成18位
				//校验位按照ISO 7064:1983.MOD 11-2的规定生成，X可以认为是数字10。
	          	var arrInt = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2);
	            var arrCh = new Array('1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2');
	            var nTemp = 0, i;  
	            num = num.substr(0, 6) + '19' + num.substr(6, num.length - 6);
	            for(i = 0; i < 17; i ++){
	                nTemp += num.substr(i, 1) * arrInt[i];
	            }
	            num += arrCh[nTemp % 11];  
	            return num;  
			}  
		}

		if (len == 18){
			re = new RegExp(/^(\d{6})(\d{4})(\d{2})(\d{2})(\d{3})([0-9]|X)$/);
			var arrSplit = num.match(re);
			//检查生日日期是否正确
			var dtmBirth = new Date(arrSplit[2] + "/" + arrSplit[3] + "/" + arrSplit[4]);
			var bGoodDay;
			bGoodDay = (dtmBirth.getFullYear() == Number(arrSplit[2])) && ((dtmBirth.getMonth() + 1) == Number(arrSplit[3])) && (dtmBirth.getDate() == Number(arrSplit[4]));
			if (!bGoodDay){
				$.messager.alert('错误','输入的身份证号里出生日期不对！','error');
				return false;
			}else{
				//检验18位身份证的校验码是否正确。
				//校验位按照ISO 7064:1983.MOD 11-2的规定生成，X可以认为是数字10。
				var valnum;
				var arrInt = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2);
				var arrCh = new Array('1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2');
				var nTemp = 0, i;
				for(i = 0; i < 17; i ++){
					nTemp += num.substr(i, 1) * arrInt[i];
				}
				valnum = arrCh[nTemp % 11];
				if (valnum != num.substr(17, 1)){
					$.messager.alert('错误','身份证的校验码不正确！','error');
					return false;
				}
				return num;
			}
		}
		return false;
	} 
	
	//移除涉及人员
	function _removeInvolvedPeople(name, idCard){
		var eventInvolvedPeople = $("#eventInvolvedPeople").val();
		var userNames = $("#involvedPersion").val();
		var removePeople  ="001" + "，" + name + "，" + idCard;
		
		if(eventInvolvedPeople.indexOf(removePeople+"；") != -1){
			eventInvolvedPeople = replaceStr(eventInvolvedPeople, removePeople+"；" , "");
		}else if(eventInvolvedPeople.indexOf(removePeople) != -1){
			eventInvolvedPeople = replaceStr(eventInvolvedPeople, removePeople , "");
		}
		
		if(userNames.indexOf(name) != -1){
			userNames = replaceStr(userNames, name, "");
		}
		$("#eventInvolvedPeople").val(eventInvolvedPeople);
		$("#involvedPersion").val(userNames);
	}
	
	//增添信息提示的涉及人员移除方法
	function removeInvolvedPeople(name, idCard, object){
		$.messager.confirm('提示', '确定移除该人员信息吗？', function(r){
			if(r){
				_removeInvolvedPeople(name, idCard);
				if(object!=undefined && object!=null){
					try{
						object.remove();//页面展示移除
					}catch(e){}
				}
			}
		});
	}
</script>
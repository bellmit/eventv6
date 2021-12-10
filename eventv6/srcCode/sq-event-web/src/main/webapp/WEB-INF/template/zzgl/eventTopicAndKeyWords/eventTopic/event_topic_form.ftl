<!DOCTYPE html>
<html>
<head>
	<title>新增/编辑</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/standard_common_files-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<style type="text/css">
		.inp1 {width:350px;}
	</style>
</head>
<body>
	<form id="submitForm">
		<input type="hidden" id="id_" name="id_" value="${(bo.id_)!}" />
		<div id="content-d" class="MC_con content light">
			<div name="tab" class="NorForm">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td>
							<label class="LabName"><span><font color="red">*</font>热点主题:</span></label>
							<input type="text" id="topicName" name="topicName" value="${(bo.topicName)!}" class="inp1 easyui-validatebox" <#if (bo.isRelease == '1')>readonly</#if> data-options="required:true"/>
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span><font color="red">*</font>分析规则:</span></label>
							<textarea id="rule" name="rule" class="inp1 easyui-validatebox" style="height:120px;" data-options="required:true" >${(bo.rule)!}</textarea>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<label class="LabName"><span><font color="red"><b>注意事项：</b></font></span></label>
							<span class="Check_Radio" style="width:80%;">
								1、分析规则中只能输入英文状态下的'('或')'或'&'或'/'四种特殊字符<br/>
								2、分析规则中的 "&"代表"与","/"代表"或"  (如(A/B)&(C/D))<br/>
								3、输入时要注意语法问题 (如(A&B/C&D),与或之间要有括号
							</span>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<div class="BigTool">
	    	<div class="BtnList">
	    		<a href="javascript:;" class="BigNorToolBtn SaveBtn" onClick="testData();">保存</a>
	    		<a href="javascript:;" class="BigNorToolBtn CancelBtn" onClick="cancel();">取消</a>
	        </div>
	    </div>
	</form>
</body>
<script type="text/javascript">

	function testData(){
		var topicName = $.trim($("#topicName").val()); //热点主题
		var rule = $.trim($("#rule").val());   //分析规则
		if(topicName == null || topicName == ""){
			$.messager.alert('提示', '热点主题不能为空！', 'info');
			return;
		}
		
		if(topicName.length > 25){
			$.messager.alert('提示', '热点主题长度不能超过25！', 'info');
			return;
		}
		
		if(rule == null || rule == ""){
			$.messager.alert('提示', '分析规则不能为空！', 'info');
			return;
		}
		
		if(rule.length > 100){
			$.messager.alert('提示', '分析规则长度不能大于100！', 'info');
			return;
		}
		
		//去空格		
		rule = rule.replaceAll(" ","").trim();
		
		//只能包含中文,英文,数字 或者 "&" ,"/" ,"(" ,")" 四种特殊字符
		if(!dataCheck(rule)){
			$.messager.alert('提示', "只能包含中文,英文,数字或者英文状态下的'&' ,'/' ,'(' ,')'四种特殊字符!", 'info');
			return;
		}
		
		//不能以 ')' , '/' ,'&' ,特殊字符 开头
		var firstStr = rule[0];
		if(firstStr == ')' || firstStr == '/' || firstStr == '&'){
			$.messager.alert('提示', "不能以')'或'&'或'/'开头！", 'info');
			return;
		}
			
		//不能以 '(' , '/' ,'&' ,特殊字符 结尾
		var lastStr = rule[rule.length-1];
		if(lastStr == '(' || lastStr == '/' || lastStr == '&'){
			$.messager.alert('提示', "不能以'('或'&'或'/'结尾！", 'info');
			return;
		}
		
		//判断括号是否匹配
		if(!isBracketBalance(rule)){
			$.messager.alert('提示', '括号不匹配,请核对！', 'info');
			return;
		}
		
		//与或关系同时存在时需要括号修饰优先级别
		if(rule.search("&") != -1 && rule.search("/") != -1 ){
			var pattern = new RegExp("[()]");
			if(!pattern.test(rule)){
				$.messager.alert('提示', '同时存在"与","或"关系时,需要括号修饰优先级别!', 'info');	
				return;
			}
		}
		
		/*
			  验证以下三种情况
		    1、( 后面不能跟 '/','&',')' 
			2、 / 后面不能直接跟 & ) /
			3、& 不能跟 / 
		   */
		var result = verifyData(rule);
		if(!result.flag){
			$.messager.alert('提示', result.msg, 'info');	
			return;
		}
		
		save(topicName,rule);
	}
	
	//只能包含中文,英文,数字 或者 "&" ,"/" ,"(" ,")" 四种特殊字符
	function dataCheck(rule){
		var pattern = new RegExp("[()/&]");
		for(var i = 0,l=rule.length;i<l;i++){
			var fg1 = isStrOrNumber(rule[i]);
			var fg2 = pattern.test(rule[i]);
			if(!fg1 && !fg2){
				return false;
			}
		}
		return true;
	}
	
	
	/*
	  验证以下三种情况
    1、( 后面不能跟特殊字符 
	2、 / 后面不能直接跟 & ) /
	3、& 不能跟 / 
   */
	function verifyData(str){
		// flag 是否通过规则
		// msg 提示信息
		var map = {'flag':'','msg':''};
		var flag = true;
		var msg = "";
		var pattern = new RegExp("[)/&]");
		
		for(var i=0,l=str.length;i<l;i++){
			if('(' == str[i]){
				if(pattern.test(str[i+1])){	
					msg = "'('后面不能直接跟'/'或'&'或')'等特殊字符!";
					flag = false;
				}
			}else if("/" == str[i]){
				if("/" == str[i+1] || ")" == str[i+1] || "&" == str[i+1]){
					msg = "'/'后面不能直接跟')'或'&'或'/'等特殊字符!";
					flag = false;
				}
			}else if("&" == str[i]){
				if("/" == str[i+1] || "&" == str[i+1] || ")" == str[i+1]){
					msg = "'&'后面不能直接跟'/'或'&'或')'等特殊字符!";
					flag = false;
				}
			}
		}			
		map.flag = flag;
		map.msg = msg;
		return map;
	}
	
	//判断字符串中是否包含 '/' 或者 '&' 特殊字符 
	function  strCheck(str){
		for(var i=0,l=str.length;i<l;i++){
			if(str[i] == "/" || str[i] == "&"){
				return true;
			}
		}
		return false;
	}
	
	//查找字符串的位置
	function findStrOrNumberIndex(str){
		for(var i=0,l=str.length;i<l;i++){
			if(isStrOrNumber(str[i])){
				return i;
			}
		}
	}
	
	//判断括号是否匹配
	function isBracketBalance(str)
    {
        var leftBracketNum = 0,  // 用于保存左括号个数的变量
        strLength = str.length; // 把字符串的长度付给一个变量增加程序的性能

        // 通过for循环来读取字符串中的一个一个的字符
        for(var i = 0; i < strLength; i++)
        {
            var temp = str.charAt(i); // 付给临时变量增加程序的性能
            if(temp === '(') // 如果是左括号，则leftBracketNum++
            {
                leftBracketNum++;
            }
            if(temp === ')') // 如果是右括号，则leftBracketNum--
            {
                leftBracketNum--;
            }
        }

        // 最后判断leftBracketNum，如果为0表示平衡否则不平衡
        if(leftBracketNum === 0)
        {
            return true;
        }else{
            return false;
        }
    }
    
    //是否是数字, 中文, 英文 
     function isStrOrNumber(charAt){
    	var isStrOrNumberFlag = false;
    	var pattern = new RegExp("[\u4E00-\u9FA5]+");
    	if(pattern.test(charAt)){
    		isStrOrNumberFlag = true;
    	}
    	pattern = new RegExp("[A-Za-z]+");
    	if(pattern.test(charAt)){
    		isStrOrNumberFlag = true;
    	}
    	pattern = new RegExp("[0-9]+");
    	if(pattern.test(charAt)){
    		isStrOrNumberFlag = true;
    	}
    	return isStrOrNumberFlag;
    }
	
	//保存
	function save(topicName,rule) {
		modleopen(); //打开遮罩层
		$.ajax({
			type: 'POST',
			url: '${rc.getContextPath()}/zhsq/eventTopic/save.json',
			data: {"topicName":topicName,"rule":rule,"bizType":parent.$("#bizType").val(),"remark":ruleChangeToSql(rule),"id_":$("#id_").val()},
			dataType: 'json',
			success: function(data) {
				if (data.result == 'fail') {
					$.messager.alert('错误', '保存失败！', 'error');
				} else {
					$.messager.alert('提示', '保存成功！', 'info', function() {
						parent.closeMaxJqueryWindow();
					});
					parent.searchData();
				}
			},
			error: function(data) {
				$.messager.alert('错误', '连接超时！', 'error');
			},
			complete : function() {
				modleclose(); //关闭遮罩层
			}
		});
		
	}

	
	//将分析规则转成sql
	function ruleChangeToSql(rule){
        var pattern = new RegExp("[()&/]");
        var sql = "";
        for(var i=0,l=rule.length;i<l;i++){
            if(pattern.test(rule[i])){
                switch(rule[i]){
                    case "(":
                    	if(i == 0){
                    		sql += rule[i] +" table.col like '%";
                    	}else if(i !=0 && isStrOrNumber(rule[i-1])){
                    		sql += "%' " + rule[i];
                    	}else if(i != 0 && rule[i+1] == '('){
                    		sql += rule[i] + ' ';
                    	}else if(i !=0 && isStrOrNumber(rule[i+1])){
                    		sql += rule[i] +" table.col like '%";
                    	}else{
                    		sql += rule[i];
                    	}
                        break;
                    case ")":
                    	if(isStrOrNumber(rule[i-1])){
                    		sql += "%' "+ rule[i] + " ";
                    	}else{
                    		sql += rule[i] + " ";
                    	}
                        break;
                    case "&":
                        if(!pattern.test(rule[i-1])){
                            sql += "%' and ";
                        }else{
                            sql += " and ";
                        }
                        if(!pattern.test(rule[i+1])){
                            sql += " table.col like '%"
                        }
                        break;
                    case "/":
                        if(isStrOrNumber(rule[i-1]) && isStrOrNumber(rule[i+1])){
                            sql += "%' or table.col like '%";
                        }else if(isStrOrNumber(rule[i+1])){
                        	sql += "or table.col like '%";
                        }else{
                            sql += " or ";
                        }
                        break;
                }
            }else{
            	if(i == 0){
            		sql += "table.col like '%"+rule[i];
            	}else{
            		sql += rule[i];
            	}
            }

        }
        if(!pattern.test(rule[rule.length - 1])){
            sql += "%' ";
        }
        return sql;
    }

	//取消
	function cancel() {
		parent.closeMaxJqueryWindow();
	}
</script>
</html>

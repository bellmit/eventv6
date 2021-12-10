var softPhone = {isLogin:true,isCall:false,callFrame:{}};
function loginSoftPhone(){
	var agentid = document.getElementById("agentid").value;
	var extension = document.getElementById("extension").value;
	if(agentid.length ==0 || extension.length ==0){
		softPhone.isLogin = false;
		$.messager.alert('友情提示','当前账号未配置工号或分机号'); 
		return;
	}
	try {
		document.Phone.AgentID = agentid;
		document.Phone.Extension = extension;
		var ret = document.Phone.Login();
		if(!ret){
			if(logoutSoftPhone()){
				ret = document.Phone.Login();
			}else{
				softPhone.isLogin = false;
			}
		}
	} catch (err) {
		$.messager.alert('友情提示',err);
	}
}

function logoutSoftPhone(){
	try {
		document.Phone.Logout();
		document.Phone.QuitSoftphone();
		return true;
	} catch (err) {
		$.messager.alert('友情提示','软电话登出异常'); 
		$.messager.alert('友情提示',err);
		return false;
	}
}

function makeCallSoftPhone(num){
	if(!softPhone.isLogin){
		softPhone.isCall = false;
		$.messager.alert('友情提示','请先登录'); 
		return false;
	}
	if(isNaN(num)){
		softPhone.isCall = false;
		$.messager.alert('友情提示','当前电话号码无效'); 
		return false;
	}
	try {
		document.Phone.MakeCall(num,true);
		softPhone.isCall = true;
		softPhone.callFrame = document.getElementById('CustomEasyWinIframe').contentWindow;
		return true;
	} catch (err) {
		softPhone.isCall = false;
		$.messager.alert('友情提示','软电话拨打异常'); 
		$.messager.alert('友情提示',err);
		return false;
	}
}
function releaseCallSoftPhone(){
	if(!softPhone.isLogin){
		softPhone.isCall = false;
		$.messager.alert('友情提示','请先登录'); 
		return false;
	}
	try {
		document.Phone.ReleaseCall();
		softPhone.isCall = false;
		return true;
	} catch (err) {
		softPhone.isCall = true;
		$.messager.alert('友情提示','软电话拨打异常'); 
		$.messager.alert('友情提示',err);
		return false;
	}
}
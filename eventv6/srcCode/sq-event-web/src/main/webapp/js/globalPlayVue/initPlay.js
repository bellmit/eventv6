const DHWsInstance = new DHWs(); // 获取对象实例，使用单例模式，唯一
const encrypt = new JSEncrypt();
const publick_key = 'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDbEpPpxpLJft4W9YZj8bRh2bYYZshBEsKOlxgyn11rlEyTasjBSZRV9aj33tvQ2T55izH0fWl+dL/dLZChawFrlGDcH8JuWge2xYMgII9mggcYa0UiQ7pLXJ9ivXZ/cOY3HzrRQdR7dGTSNn3Z0Ctbns6mLgvlA2r3qMNs/8wHBwIDAQAB';
encrypt.setPublicKey(publick_key);
var isLogin = false;
var loginIp = '59.204.145.130';

function isLoginSuccess() {
    if (!DHWsInstance.isConnectSuccessQt) {
        return false;
    }
    if (!DHWsInstance.isLoginSuccess) {
        return false;
    }
    return true;
}

function globaleyesLogin(chinnaId,resourceDomain) { // 调用登录接口
	this.channelId = chinnaId;
	if (isLoginSuccess()) {
		$.unblockUI();
		realTimeVideoDialog();
	} else {
        DHWsInstance.on('loginState', (res) => {
            console.log("res==",res);
            isLogin = res;
            $.unblockUI();
            if(res) {
                realTimeVideoDialog();
            } else {
                $.messager.show({
                    title:'友情提示',
                    msg:'视频监控播放失败！请检查网络或者点击<a href="'+resourceDomain+'/cms/r/monitorInstallSoft.rar" style="color:rgb(255, 0, 0) !important;">下载相应版本<a/>插件！注意：请使用360浏览器极速模式下查看监控视频。',
                    showType:'show',
                    timeout:5000,
                    style:{
                        baileft:200, // 与左边du界的距离zhidao
                        top:200 // 与顶zhuan部的shu距离
                    }
                });
            }
        });
	}
}

function logout() { // 调用登出接口
    DHWsInstance.logout({
        loginIp:loginIp
    });
}

function destroy() { // 调用销毁控件接口
    if (!isLogin) {
        return false;
    }
    const ctrls = DHWsInstance.ctrls.map(i => {
        if (i.ctrlCode === 'ctrl1') {
            return i.ctrlCode;
        }
    });
    DHWsInstance.destroyCtrl(ctrls);
}

function realTimeVideo() { // 调用控件实时播放接口
    if (!isLoginSuccess()) return false;
    const params = {
        ctrlCode: 'ctrl1',
        channelIds: [channelId]
    };
    DHWsInstance.openCtrlPreview(params);
}

function realTimeVideoDialog() { // 调用弹窗实时播放接口
    if (!isLoginSuccess()) return false;
    const params = [channelId];
    DHWsInstance.openVideo(params);
}

function login(){
    try{
        DHWsInstance.login({
            loginIp: loginIp,
            loginPort: '8282',
            userName: 'NP_ST',
            userPwd: 'dahua123!',
            userCode: 'S4NbecfYB19QUJHT4T8M7G',
            token: ''
        });
    }catch(err){
        $.messager.show({
            title:'友情提示',
            msg:'登录失败!' +
            '点击<a href="'+resourceDomain+'/monitorInstallSoft.rar" style="color:rgb(255, 0, 0) !important;">下载相应版本<a/>插件！注意：请使用360浏览器极速模式下查看监控视频。',
            showType:'show',
            timeout:5000,
            style:{
                baileft:200, // 与左边du界的距离zhidao
                top:200 // 与顶zhuan部的shu距离
            }
        });
    }
}

login();
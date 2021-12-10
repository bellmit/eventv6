/**
 * 定义获取token规范接口
 * 
 * @param url
 * @param backFn
 * @returns
 */
function fetchMapTokenForUrl(url, backFn) {
	if (!url) {
		backFn(null);
		return;
	}
	// token服务的地址
	const tokenServerUrl = "http://10.18.24.14:8080/onemap/admin/rest/generateToken"
	const username = "ncdsjj"; // 用户名:jydsj
	const password = "admin123"; // 密码:12345678
	const clientid = 'ref.zhsq_event'; // localhost
										// 有2种认证方式，referrer和ip，web页面适合使用referrer方式。生产环境中，需要把localhost替换为生产环境的域名
	const expiration = 24 * 60; // token的有效时间，单位是分钟
	// 使用jquery发送请求，获取token，存在跨域的问题，所以以jsonp方式发送请求
	$.ajax({
		url : tokenServerUrl,
		jsonp : "callback",
		type : "POST",
		dataType : "jsonp",
		data : {
			username : username,
			password : password
		},
		success : function(data) {
			// 返回的数据中，data.token即为认证的token
			if (data) {
				if (data.code == 200 || data.code == "200") {
					url += "?token=" + data.content.token;
					backFn(url);
				} else {
					alert("获取地图token错误：" + data.message);
					backFn(null);
				}
			} else {
				alert("获取地图token错误！");
				backFn(null);
			}
		},
		error : function() {
			alert("获取地图token错误！");
			backFn(null);
		}
	});
}
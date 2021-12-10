<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>系统异常</title>
		<style>
img,body {
	padding: 0;
	margin: 0;
}

img {
	border: 0;
}

p {
	text-align: center
}

a {
	color: red;
}
.error_btn {
	color: red;
	font-size: 16px;
	background: none;
	border: 0;
	padding: 0;
	cursor: hand
}
	</style>
		<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
		<script type="text/javascript">
			$(function(){
					$("#error").hide();
					$("#errorBtn").toggle(
						function(){
					     $("#error").show('slow');
					   	},
					   	function(){
					     $("#error").hide('fast');
					  	 }
				   );
			});
		</script>
	</head>

	<body>
		<div style="width: 484px; height: 348px; margin: 50px auto auto auto">
			<img src="${rc.getContextPath()}/image/error.jpg" />
		</div>
		<p>
			网站出现异常，<input type="button" value="点这" id="errorBtn" class="error_btn"/>查看错误信息
		</p>
		<div id="error">${errorMsg}</div>
	</body>
</html>

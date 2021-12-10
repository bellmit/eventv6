<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>跨域中间页面</title>
	</head>
	
	<body>
		<script type="text/javascript">
			<#if callBackFunction??>
				parent.parent.eval('${callBackFunction}');
			</#if>
		</script>
	</body>
</html>

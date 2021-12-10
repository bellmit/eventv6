<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>跨域中间页面</title>
	</head>
	
	<body>
		<script type="text/javascript">
			<#if url??>
				parent.parent.parent.showMaxJqueryWindow("楼房详情信息", '${url}',948,405);
			</#if>
		</script>
	</body>
</html>

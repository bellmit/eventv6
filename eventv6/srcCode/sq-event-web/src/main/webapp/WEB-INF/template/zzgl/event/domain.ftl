<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<script type="text/javascript">
	<#if source?? && source = 'workPlatform'>
		document.domain = "${domain}";
	<#elseif source?? && source = 'oldWorkPlatform'>
		document.domain = "${domain}";
	<#else>
	</#if>
</script>
</head>
<body>
	<script type="text/javascript">
		
		<#if callBack??>
			<#if source?? && source = 'oldWorkPlatform'>
				//parent.parent.closeLhgdialog();
				//parent.closeMaxJqueryWindow();
				${callBack}
			<#else>
				${callBack}
			</#if>
		<#else>
			<#if source?? && source = 'oldWorkPlatform'>
				parent.closeMaxJqueryWindow();
			</#if>
		</#if>
	</script>
</body>
</html>

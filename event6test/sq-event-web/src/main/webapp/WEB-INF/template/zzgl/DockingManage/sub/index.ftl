<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>列表</title>
<#include "/component/commonFiles-1.1.ftl" />
</head>
<body>

<table>
<tr>
	<td>TASK_ID</td>
	<td>TASK_NAME</td>
	<td>TASK_NAME</td>
</tr>
<#list taskList as l>
	<tr>
		<td>${l.TASK_ID}</td>
		<td>${l.TASK_NAME}</td>
	</tr>
</#list>
</table>
</body>
<script type="text/javascript">
</script>
</html>
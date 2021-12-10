<!DOCTYPE html>
<html">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<#include "/component/commonFiles-1.1.ftl">
<title>跨域</title>

<script type="text/javascript">
	if (parent.parent.document.getElementById('x')) {
		parent.parent.document.getElementById('x').value = ${x!''};
	}

	if (parent.parent.document.getElementById('y')) {
		parent.parent.document.getElementById('y').value = ${y!''};
	}

    if (parent.parent.document.getElementById('mapt')) {
        parent.parent.document.getElementById('mapt').value = ${mapt!''};
    }

    parent.parent.document.getElementById('_labelLocationType').value = "map";

    $("#${targetDownDivId!''}",window.parent.parent.document).hide();

	<#--parent.parent.mapMarkerSelectorCallback(${x}, ${y}, ${mapt});-->
</script>

</head>

<body>
</body>

</html>
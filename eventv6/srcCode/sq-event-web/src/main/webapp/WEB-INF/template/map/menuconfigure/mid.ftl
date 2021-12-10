<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title> 
<style>
.middleframebg {
   cursor:pointer; 
}
</style>
<script>	var flag = false;
	function shift_status()
	{
		var mainFrame = parent.document.getElementById("index_frameset");
		if(flag)
		{
			if(screen.width>1024) {
				mainFrame.cols = "230,6,*";
			} else if(screen.width>800) {
				mainFrame.cols = "230,6,*";
			} else {
				mainFrame.cols = "230,6,*";
			}
			document.getElementById("menuSwitch1").src='${rc.getContextPath()}/images/control_1.gif';
		}
		else
		{
			mainFrame.cols = "0,6,*";
			document.getElementById("menuSwitch1").src='${rc.getContextPath()}/images/control_2.gif';
		}
		flag = !flag;
	} 
	
</script>
</head>

<body  bgcolor="#bed3de" onclick="shift_status()">

<div class="middleframebg">
<img src="${rc.getContextPath()}/images/control_1.gif" id="menuSwitch1" style="margin:200px 0;">
</div>

</body>
</html>

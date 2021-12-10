<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>组织配置</title>  
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script type='text/javascript'>
$(function(){
		var w = document.body.scrollWidth-230-6;
		var h = document.documentElement.clientHeight;
		var f1,f3,f2=$("<iframe scrolling='no' width='6' height='"+h+"' resize='yes' frameborder='0' marginheight='0' marginwidth='0' src='${rc.getContextPath()}/zhsq/map/menuconfigure/menuConfig/mid.jhtml'  ></iframe>");
		[#if id??]
		f1=$("<iframe scrolling='yes' width='230' height='"+h+"' resize='yes' frameborder='0' marginheight='0' marginwidth='0' id='left' src='${rc.getContextPath()}/zhsq/szzg/zgResourceController/page.jhtml?page=left&id=info' ></iframe>");
	     f3=$("<iframe scrolling='yes' width='"+w+"' height='"+h+"' resize='yes' frameborder='0' marginheight='0' marginwidth='0' id='main' name='' src='${rc.getContextPath()}/zhsq/szzg/zgResourceController/page.jhtml?page=list_info&id=0' ></iframe>");
	     [#else]
	     f1=$("<iframe scrolling='yes' width='230' height='"+h+"' resize='yes' frameborder='0' marginheight='0' marginwidth='0' id='left' src='${rc.getContextPath()}/zhsq/szzg/zgResourceController/page.jhtml?page=left' ></iframe>");
	     f3=$("<iframe scrolling='yes' width='"+w+"' height='"+h+"' resize='yes' frameborder='0' marginheight='0' marginwidth='0' id='main' name='' src='' ></iframe>");    
	     [/#if]
	    $("body").append(f1,f2,f3);
})

</script>
</head>
<body style="padding:0;margin:0;">
</body>
</html>

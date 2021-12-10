

<!-- 语音视频插件-->
<script language="javascript" for="MMPLogicOcx" event="NotifyMMPMsg(p1,p2)">  
	MMPNotifyMsg(p1,p2);
</script>
<OBJECT id="MMPLogicOcx" name="MMPLogicOcx" classid="clsid:E1DBA827-5CC3-431C-97F9-306937F06EE3" width="1" height="1"></OBJECT>

<div id="mmpSelector" class="easyui-window" title="视频" minimizable="false" maximizable="false" collapsible="false" closed="true" modal="false" style="width:500px;height:550px;padding:1px;overflow:hidden;zIndex:1">
	<div style="padding-left:10px; line-height:25px; text-align:left;">若无法使用该功能，请点击下载：<a href="${RESOURCE_DOMAIN!}/public_download/MMPOCX.zip" title="点击下载" style="text-decoration:none;color:blue">视频通话控件</a></div>
	<OBJECT id="MMPPreviewWndOcx" name=MMPPreviewWndOcx classid=clsid:6D17667D-9D73-411D-AC6C-F80A49BF3000 width=500 height=500></OBJECT>
</div>
<script>
    var i=0;
	var $mmpSelectorWin=null;
	function initMmpSelector() {
	    $mmpSelectorWin = $('#mmpSelector').window({ 
	    	title:"视频",
	    	width: 510,
	    	height: 565,
	    	top: 32,
	    	left:430,
	    	shadow: true,
	   	    modal: false,
	   	    minimizable:true,
			collapsible: true,
			resizable: false ,
			maximizable:false,
			closed:true,
			onMinimize: function () {
			 if(i==0){
	               //最下化移动到右下角并折叠
	               $mmpSelectorWin.window('move', {
	                   left: "0",
	                   top: "94%"
	               }).window('collapse').window('open');
	               i=1;
              }else{
               	   $mmpSelectorWin.window('move', {
	                   left:430,
	                   top: 32
	               }).window('expand').window('open');  
	               i=0;
              }
           },
           onBeforeClose: function () {
           		if(checkActiveX()){
    				StopPreview(); 					
					StopVideoTalk(); 
				}
           } 
	    });
		
	}
	function showMmpSelector() {
		$mmpSelectorWin.window('open');
		/*if(!checkActiveX()){
			 $.messager.show({
				title:'提示',
				msg:'您未安装控件或者浏览器不支持！',
				//msg:'若无法使用该功能，请点击下载：<a href="javaScript:void(0)" style="color:blue"  title="点击下载">视频控件地址</a>',
				showType:'slide',
				timeout:5000,
				style:{
				    top:215,
					right:'',
					bottom:''
				}
			});
		}
		else{
			 $mmpSelectorWin.window('open');
		}
	   */
	}
	
	function closeMmpSelector() {
		 $mmpSelectorWin.window('close');
	}

</script>
<script type="text/javascript" src="${rc.getContextPath()}/js/mmp/mmp.js" ></script>

// JavaScript Document

var ffcs_animation = true;
var ffcs_DONE = false;
var ffcs_HEIGHT = 400;
var ffcs_WIDTH = 400;
var ffcs_LOADFLAG = false;
//生成DIV
 function ffcs_show(caption, url, height, width,top, loadFlag){
	 ffcs_LOADFLAG = loadFlag || ffcs_LOADFLAG;
	 var de = document.documentElement;
     var w = self.innerWidth || (de&&de.clientWidth) || document.body.clientWidth;
	 var h = self.innerHeight || (de&&de.clientHeight) || document.body.clientHeight;
	  ffcs_HEIGHT = height || h-2;
      ffcs_WIDTH = width || w; 
	  ffcs_TOP = top || 0; 
	if(!ffcs_DONE) {
		$(document.body).append("<div id='tab_contoner' style='z-index:9999'><div class='contleft'><span id='contspan' class='contspan'></span><div class='cla_close'><a href='javascript:void(0);' ><img src='/images/close1.jpg' /></a></div></div></div>");
	    $("#tab_contoner").css("top",top)
	    if(height==undefined){
	    	$("#tab_contoner").css("border","0")	  
	    }
	    $("#tab_contoner a").click(ffcs_hide);//关闭按钮
	 }
	 $("#ffcs_frame").remove();
     $("#tab_contoner").append("<iframe id='ffcs_frame' frameBorder='0'   style='overflow: auto'  src='"+url+"'></iframe>"); //生成IFRAME
	 
	 $("#contspan").html(caption);
     $("#tab_contoner").show();
      ffcs_position();
	 if(ffcs_animation){ $("#tab_contoner").slideDown("slow")} else{  $("#tab_contoner").show()}
 }
 
 function  ffcs_hide(){
	 $("#tab_contoner").remove();
	 if(window.qnviccub == undefined && $("#qnviccubDiv",window.document).size() > 0) {
 	 	$("#qnviccubDiv",window.document).append('<object style="display:none;" classid="clsid:F44CFA19-6B43-45EE-90A3-29AA08000510" id="qnviccub" data="DATA:application/x-oleobject;BASE64,GfpM9ENr7kWQoymqCAAFEAADAAD7FAAADhEAAA==" width="33" height="33"></object>');
 	 	window.TV_Initialize();
 	 }
 	 
	 if(ffcs_LOADFLAG){
	 	 document.forms[0].submit();
	 	 /*var method = document.getElementById("method");
	 	 if(method!=null && method!=undefined) {
		 	document.forms[0].submit();
		 } else {
		 	window.parent.location.reload();//未设置method跳转时，使用该法刷新父页面
		 }*/
	 }
 }
 function ffcs_position(){
	var de = document.documentElement;
  var w = self.innerWidth || (de&&de.clientWidth) || document.body.clientWidth;
   var h = self.innerHeight || (de&&de.clientHeight) || document.body.clientHeight;
   ffcs_HEIGHT = ffcs_HEIGHT;
  $("#tab_contoner").css({width:ffcs_WIDTH +"px",height:ffcs_HEIGHT +"px",
    left: ((w - ffcs_WIDTH)/2)+"px" });
   $("#ffcs_frame").css("height",(ffcs_HEIGHT - $("#contspan").height() - 3) +"px");
   $("#ffcs_frame").css("width",(ffcs_WIDTH - 5)+"px");
//	$("#tab_contoner").css("height",h+"px");
 }
//获得域名
function getBasePath() {  
    var fileName = "js/dhGlobalEye/videoBox.js";  
    var scripts = document.getElementsByTagName('script'), i, ln, path, scriptSrc;
    for (i = 0, ln = scripts.length; i < ln; i++) {
        scriptSrc = scripts[i].src;
        var n = scriptSrc.indexOf(fileName);
        if (n > 0) {
            path = scriptSrc.substring(0, n);
            break;
        }
    }
    if (path)
        return path;
    return "";
}

var video_box,video_title,video_close,video_b,video_content,video_msg,video_type,iframeUrl,eventFrameUrl,videoMinBox;
$(function(){
    init();
    createMsg();
});
var initialization = 0;
var tempdir='';
var picNames='';
var monitorIds={};
function showGlobalPlayBox(id){
    if(initialization == 0){
        var host = getBasePath();
        var path = host.substring(0, host.lastIndexOf("/")) + '/zhsq/map/nanpingVideoDataController/playPage.jhtml';  
        $(iframeUrl).attr("src",path);
        initialization = 1;   
    } 
   
    
    showVideoBox();

    receiveVideo({type:1,note:"(请求播放...)"});
    getVideoParamAndPlay(id);
    

}

function createMsg(){gmMsgClient
    var iframeObj = document.getElementById("video_frame").contentWindow;
    gmMsgClient.addObserver(iframeObj, receiveVideo, "videoMsg");
}

function receiveVideo(msg){
    try{
        if(msg.type==1){ //登录
            var title="视频播放";
            $(video_title).text(title+msg.note);
        }else if(msg.type==3){ //返回抓拍路径
        	
        	
            alert("path==="+msg.path);
        }
    }catch (e){
    }
}

function sendVideo(msg){
    var iframeObj = document.getElementById("video_frame").contentWindow;
    gmMsgClient.sent(iframeObj, "videoMsg", msg);
}

function init(){
    tempdir=document.getElementById("tempdir").value;
    $(document.body).append(video_box = $('<div class="video_con"/>'));
    $(video_box).append([
        $('<div class="video_con_h flex flex_ac flex_jb"/>').append([
            $('<h5></h5>').append('<span></span>').append(video_title=$('<label>视频播放</label>'))[0],
            $("<label></label>").click(videoMined).append([
                $(videoMined=$('<a href="#" class="video_min" title="缩小"><span></span></a>').click(videoMined))[0],
                $(video_close=$('<a href="#" class="video_close" title="关闭 "><span></span></a>').click(videoClose))[0]
            ])[0]
        ])[0],
        video_content = $('<div class="video_con_b flex flex_ac flex_jc"/>')[0],
        $('<div class="video_con_f flex flex_ac">').append([
            $('<h5>分屏<br />选择</h5>')[0],
           
            $('<ul class="video_nav flex"></ul>').append([
                $('<li class="active"></li>').bind('click',{num:1},videoTypeChange).append('<span class="one"></span><p>单屏显示</p>')[0],
                $('<li></li>').bind('click',{num:4},videoTypeChange).append('<span class="four"></span><p>四屏显示</p>')[0],
                $('<li></li>').bind('click',{num:9},videoTypeChange).append('<span class="nine"></span><p>九屏显示</p>')[0] ,
                $('<li  style="padding-left: 50px;" onclick="doReport()"></li>').append('<span class="cur" ></span>')[0] 
            ])[0],
           
        ])[0]
    ]).tinyDrag(); 
    
    //事件上报
    $(video_box).append(event_box = $('<div class="event_con" style="left: 657px; top: 1px;background-color:#ffff; width:400px"/>'));
   
    var str='<div style="text-align: center;"><img id="picName"  width="220px" height="100px" src="" /></div>'
    
    eventFrameUrl= $('<iframe id="event_frame" width="100%" height="100%" frameborder="no" scrolling="no" overflow="hidden" ></iframe>')[0];
    
    $(event_box).append(eventFrameUrl);
   // $(event_box).append(str).tinyDrag();
    $(event_box).tinyDrag();
    
    
   
    $(document.body).append(videoMinBox = $('<div class="video_con_min flex flex_ac flex_jb"><h5><span></span><label>视频播放</label></h5><a href="#" class="video_max" title="还原"><span></span></a></div>').click(videoBoxRestore).hide());
    var left = ($(window).width()-$(video_box).width())/2;
    var top =  ($(window).height()-$(video_box).height())/2;
    $(video_box).css({left:left,top:top});
    iframeUrl = $('<iframe id="video_frame" width="100%" height="100%" frameborder="no" scrolling="no" overflow="hidden" ></iframe>')[0];
    $(video_content).append(iframeUrl);
    
    
    $(event_box).css({left:$(".video_con_h").css('width'),top:0,height:$(".video_con").css('height')});  //$(".video_con").css('height')  
    $(video_box).css({display:"none"});
    
    $(".event_con").hide();
        
}
function doReport(){
	var id;
	var iframeObj;
	try {
		iframeObj = document.getElementById("video_frame").contentWindow;  
		id=monitorIds['key'+iframeObj.getCurWndNo()];
	}
	catch(err) {
	    alert(err.message);
	    return ;
	}
	var picName=iframeObj.capturePicture(); //截图
	if($(".event_con").css("display")=='block'){
		//$(".event_con").hide();
		// picName="20190402203801.jpg"; 
		if(picName!=null){
			picNames+=","+picName; 
		}
	}else{
		//picName="20190402203801.jpg"; 
	    if(picName==null){
	    	 alert('图片截取失败！');
	    	 return ;
	    }
	    //事件上报  
		var host=getBasePath();
		var event = {
			    "isReport":false
		}  
		var event = JSON.stringify(event);  
		var path = host.substring(0, host.lastIndexOf("/")) + '/zhsq/event/eventDisposalController/toAddEventByVideo.jhtml?picName=&monitorById='+id+'&eventJson='+encodeURIComponent(event);
		if(typeof(id) != undefined && id!=undefined){
	    	$(".event_con").show();
	    	$(eventFrameUrl).attr("src",path); 
	    }else{
	    	alert("获取视频信息失败！");
	    }
		
	}   

}
function hideEvent_con(){
	$(".event_con").hide();
}


function showVideoBox(){
    $(video_box).show();
    $(videoMinBox).hide();
}

function setTitle(title){
    $(video_title).text(title);
}

function videoBoxRestore(){
    $(video_box).show();
    $(videoMinBox).hide();
}

function videoTypeChange(e){
    $(this).addClass("active").siblings().removeClass("active");
    sendVideo({type:1,num:e.data.num});
}

function videoMined(){
    $(video_box).hide();
    $(videoMinBox).css({left:10,top:$(window).height()-50,display:"inline-flex"});
}

function videoClose(){
    event.stopPropagation();
    $(video_box).hide();
    sendVideo({type:2});
}

function capturePicture(){
    sendVideo({type:3});
}

function getVideoParamAndPlay(id){
    var host = getBasePath();
    var path = host.substring(0, host.lastIndexOf("/")) + '/zhsq/map/nanpingVideoDataController/findGlabalEyeInfo.json';
    $.ajax({
        type: "POST",
        url: path,
        data: {id:id},
        dataType:"jsonp",
        success: function(data){
            if(data){
                var msg ={type:0,data:data};
                sendVideo(msg);
                try {
                	 var iframeObj = document.getElementById("video_frame").contentWindow;  
        			 monitorIds['key'+iframeObj.getCurWndNo()]=id;
        		}
        		catch(err) {
        		    //
        		}
            	   $(".event_con").hide();
            }
        },
        error:function(data){
            if(data.responseText){
                var d = $.parseJSON(data.responseText);
                var msg ={type:0,data:d};
                sendVideo(msg);
            }
            //$.messager.alert('错误','连接超时！','error');
        }
    });
}
(function ($) {
    /*! 移动图片 兼容各浏览器 开始
     * tinyDrag v0.9.2
     * (c) 2010 Ben Kay <http://bunnyfire.co.uk>
     */
    $.fn.tinyDrag = function(callback) {
        return $.tinyDrag(this, callback);
    }

    $.tinyDrag = function(el, callback) {
        var mouseStart, elStart, moved, doc = $(document), abs = Math.abs;
        el.mousedown(function(e) {
            moved = false;
            mouseStart = {x: e.pageX, y: e.pageY};
            elStart = {x: parseInt(el.css("left")), y: parseInt(el.css("top"))}
            doc.mousemove(drag).mouseup(stop);
            return false;
        });

        function drag(e) {
            var x = e.pageX, y = e.pageY;
            if (moved) {
                el.css({left: elStart.x + (x - mouseStart.x), top: elStart.y + (y - mouseStart.y)});
            } else {
                if (abs(x - mouseStart.x) > 1 || abs(y - mouseStart.y) > 1)
                    moved = true;
            }
            return false;
        }

        function stop() {
            doc.unbind("mousemove", drag).unbind("mouseup");
            moved&&callback&&callback()
        }

        return el;
    }
    /**移动图片 兼容各浏览器 结束 ***/
})(jQuery);

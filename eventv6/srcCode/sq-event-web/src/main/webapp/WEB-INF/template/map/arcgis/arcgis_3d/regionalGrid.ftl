<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>地域选择组件弹窗</title>
    <link rel="stylesheet" href="${uiDomain!''}/web-assets/common/css/reset.css">
    <link rel="stylesheet" href="${uiDomain!''}/web-assets/_big-screen/nanchang-cc/css/index-mask.css">
    <link rel="stylesheet" href="${uiDomain!''}/web-assets/_big-screen/nanchang-cc/css/index-diyu.css">
	<style>
		 .handle{cursor:pointer;}
	</style>	
    <script src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js" type="text/javascript" charset="utf-8">
    </script>
    <script type="text/javascript">
        var winW, scale;
        $(window).on('load resize', function () {
            fullPage();
            setTimeout(fullPage, 10); //二次执行页面缩放，解决全屏浏览时滚动条问题
            function fullPage() { //将页面等比缩放
              winW = $(window.parent).width();
               // winW = 1366;
                // if (winW < 1000) {
                //     winW = 1000;
                // }
                var whdef = 100 / 1920;
                var rem = winW * whdef; // 以默认比例值乘以当前窗口宽度,得到该宽度下的相应FONT-SIZE值
                $('html').css('font-size', rem + "px");
                if(winW<=1200){
                    $("head").append("<link>");
                        css = $("head").children(":last");
                        css.attr({
                        rel: "stylesheet",
                        type: "text/css",
                        href: "${uiDomain!''}/web-assets/_big-screen/nanchang-cc/css/small-screen.css"
                    }); 
                }

            }
        });
    </script>
</head>

<body style="background: transparent;">
    <div class="mae-container mae-event-list mae-container-on">
        <Div class="dianwei-xuanze2">
            <div class="dianwei-xianze-a1 diyu-xuanze-a1 xqi-bs clearfix">
                 <div class="dianwei-xianze-top dianwei-xianze-top-a1 diyu-xuanze">
                    <div class="diyu-top-left"><p id="gridName"></p></div>
                    <div class="diyu-top-right" id="diyu-top-right">
                    <i></i>
                    <span id="diyuPath"></span>
                    <span id="connect" class="span-henggang"></span>
                    <span id="jiedaoPath"></span>
                    <span class="span-clear handle" id="span-clear">清除全部</span></div> 
                 </div>
                 <div class="dianwei-xianze-btn dianwei-xianze-btn-a1 diyu-xuanze-btn" id="diyu-xuanze-btn">
                  <ul id="diyu-quxian">
                 
                  </ul> 
                  <div class="clearfloat"></div>
                  <div class="diyu-jiedao" id="diyu-jiedao-div">
                    <dl>
                    	<Dt id="quxianName"></Dt>
                    </dl>
                    <dl id="diyu-jiedao">
                        
                    </dl>
                  </div>
                 </div>
             </div>
             <div class="dianwei-center dianwei-center-a1 diyu-center-a1 ">
                 <div class="diyu-anniu" style="width: 3.2rem;">
                    <div class="xqi-bs diyu-aniu"><a id="submit">确定</a></div>
                    <div class="xqi-bs diyu-aniu diyu-quxiao" id="diyu-quxiao"><a id="cancel">取消</a></div>
                 </div>
            </Div>
    </div>
</div>
</body>

</html>
	<script src="${uiDomain!''}/web-assets/common/js/basic/jquery.nicescroll.js" type="text/javascript"
	    charset="utf-8"></script>
<script>

	var infoOrgCode = "${infoOrgCode}";
	var zNodes = [];
	var children = [];
	var str = "";
	
	var param = {
		centerX : '',
		centerY : '',
	    centerLevel : '',
	    infoOrgCode : '',
		gridName : '',
		gridId : '',
		gridLevel : ''
	}
	
    $(function () {
    
		//初始化网格树
		findGridData(infoOrgCode);

		$("#submit").click(function(){
			if(window.parent.pointPosition || window.parent.videoPosition){
				window.parent.$("#camSearch").val("");
				window.parent.$("#myiframe1")[0].contentWindow.$("#camSearch").val("");
				window.parent.$("#pointSearch").val("");
				window.parent.$("#myiframe1")[0].contentWindow.$("#spointName").val("");
			}
			window.parent.document.getElementById("diyu").setAttribute("gridId",param.gridId);
			window.parent.document.getElementById("diyu").innerText = param.gridName;
			window.parent.document.getElementById("popupWindowDiv").style.display = "none";
			window.parent.diyuNodeClick(param);
		});
		
		$("#cancel").click(function(){
			window.parent.document.getElementById("diyu").setAttribute("gridId",selectGridId);
			window.parent.document.getElementById("popupWindowDiv").style.display = "none";
		});
    
    
        //左边点位选择弹窗切换
        var ind;
        var inh;
        //右边边点位选择弹窗切换
        $('#diyu-xuanze-btn>ul').on('click', 'li', function () {
        	$("#diyu-jiedao-div").show().addClass('diyu-jiedao-on');
            $("#diyuPath").html("");
            $(this).text();
            $('#diyu-top-right>span').eq(0).text($(this).text());
            $('#diyu-top-right>span').slice(1,3).text('');
            $('#diyu-jiedao-div>dl').eq(0).find('dt').text($(this).text());
            $(this).addClass('active').siblings().removeClass('active');
            $(this).parent().siblings().find('li').removeClass('active');
            $(this).parent().nextAll().find('dd').removeClass('active');
			showData(zNodes,$(this).attr('infoorgcode'));
			param = {
				centerX : $(this).attr('x'),
				centerY : $(this).attr('y'),
			    centerLevel : $(this).attr('zoom'),
			    infoOrgCode : $(this).attr('infoOrgCode'),
				gridName : $(this).attr('gridName'),
				gridId : $(this).attr('gridId'),
				gridLevel : $(this).attr('gridLevel')
			}
        });  
        $('#diyu-jiedao-div>dl').on('click', 'dd', function () {
            //$(this).removeClass('active');
            $("#diyuPath").html("");
			$("#diyuPath").html($("#quxianName").text());            
            $('#connect').text('---');
            $('#jiedaoPath').text($(this).text());
            $(this).addClass('active').siblings().removeClass('active');
            $(this).parent().parent().siblings().find('dd').removeClass('active');
            param = {
				centerX : $(this).attr('x'),
				centerY : $(this).attr('y'),
			    centerLevel : $(this).attr('zoom'),
			    infoOrgCode : $(this).attr('infoOrgCode'),
				gridName : $(this).attr('gridName'),
				gridId : $(this).attr('gridId'),
				gridLevel : $(this).attr('gridLevel')
			}
        });
        $('#span-clear, #diyu-quxiao').click(function(){
            $('#diyu-top-right>span').slice(0,3).text('');
            $('#diyu-xuanze-btn>ul>li').removeClass('active');
            $('#diyu-xuanze-btn>ul>li>i').removeClass('sanjiaoxing-icon-on');
            $('#diyu-xuanze-btn>div>dl>dd').removeClass('active');
            $('#diyu-xuanze-btn>div').removeClass('diyu-jiedao-on');
            param = {
				centerX : zNodes[0].x,
				centerY : zNodes[0].y,
			    centerLevel : zNodes[0].mapCenterLevel,
			    infoOrgCode : zNodes[0].infoOrgCode,
				gridName : zNodes[0].name,
				gridId : zNodes[0].gridId,
				gridLevel : zNodes[0].gridLevel
			}
        });
    });
    
    
    var initGridData = {};
    //获取网格数据
	function findGridData(infoOrgCode){
		$.ajax({
    		url :"${rc.getContextPath()}/zhsq/nanChang3D/findGridData.json",
			type : 'POST',
			dataType : "json",
			data: {'infoOrgCode':infoOrgCode},
			success: function(data) {	
				if(data != null && data.length > 0){
					for(var i=0;i<data.length;i++){
						initGridData[data[i].gridId] = data[i];
					}
					zNodes = arry2TreeFormat(data);
					$("#gridName").text(zNodes[0].name);
					showData(zNodes,"");
					
				}	
			}, 
			error : function() {
			//	$.messager.alert('错误', '信息获取失败!', 'error');
		    }
	   });
    }
    
    function arry2TreeFormat(sNodes){
		var r = [];
		var tmpMap = [];
		var id="gridId",pid="pId",children="children";
		for (i=0, l=sNodes.length; i<l; i++) {
			tmpMap[sNodes[i][id]] = sNodes[i];
		}
		for (i=0, l=sNodes.length; i<l; i++) {
			if (tmpMap[sNodes[i][pid]] && sNodes[i][id] != sNodes[i][pid]) {
				if (!tmpMap[sNodes[i][pid]][children])
					tmpMap[sNodes[i][pid]][children] = [];
				tmpMap[sNodes[i][pid]][children].push(sNodes[i]);
			} else {
				r.push(sNodes[i]);
			}
		}
		
		window.parent.initX = r[0].x;
		window.parent.initY = r[0].y;
		window.parent.initZoom = r[0].mapCenterLevel;
		return r;
	}
	
	var rsNodes = [];	
    
    function showData(zNodes,infoOrgCode){
    	var html = "";
    	if(infoOrgCode == "" || infoOrgCode == null){
    		$("#diyu-jiedao-div").css("display","none");
    		if(zNodes[0].children == null){
    			return;
    		}
    		var data = zNodes[0].children;
    		for(var i = 0,l = data.length;i<l;i++){
    			html += "<li class='handle' infoOrgCode = '"+ data[i].infoOrgCode +"' x = '"+ data[i].x +"' y = '"+ data[i].y +"' zoom = '"+ data[i].mapCenterLevel +"' gridName = '"+ data[i].name +"' gridId = '"+ data[i].gridId +"' gridLevel = '"+ data[i].gridLevel +"' >"+ data[i].name + "</li>";
    		}
    		$("#diyu-quxian").html(html);
    	}else{
    		findDataByInfoOrgCode(zNodes,infoOrgCode);
    		if(children ==null || children.length ==0){ $("#diyu-jiedao-div").css("display","none"); return;}
    		$("#diyu-jiedao-div").css("display","block");
    		for(var i = 0,l = children.length;i<l;i++){
    			html += "<dd class='handle' infoOrgCode = '"+ children[i].infoOrgCode +"' x = '"+ children[i].x +"' y = '"+ children[i].y +"' zoom = '"+ children[i].mapCenterLevel +"' gridName = '"+ children[i].name +"' gridId = '"+ children[i].gridId +"' gridLevel = '"+ children[i].gridLevel +"' >"+ children[i].name + "</dd>";
    		}
    		$("#diyu-jiedao").html(html);
    	}
	}
	
	function findDataByInfoOrgCode(data,infoOrgCode){
		if(data != null && data.length > 0){
			for(var i=0,l=data.length;i<l;i++){
				if(data[i].infoOrgCode == infoOrgCode){
					children = data[i].children;
					return;
				}else{
					if(data[i].children != null && data[i].children.length != 0){
						findDataByInfoOrgCode(data[i].children,infoOrgCode)
					}
				}
			}
		}
	}    
	
	
	var selectGridId = "";
	
	function showPathByGridName(gridId){
		selectGridId = gridId;
		showData(zNodes,"");
		if(gridId == window.parent.defaultGridId){
			return;
		}
		var path = [];
		var code = [];
		var gridName= [];
		param = {
			centerX : initGridData[gridId].x,
			centerY : initGridData[gridId].y,
		    centerLevel : initGridData[gridId].mapCenterLevel,
		    infoOrgCode : initGridData[gridId].infoOrgCode,
			gridName : initGridData[gridId].name,
			gridId : initGridData[gridId].gridId,
			gridLevel : initGridData[gridId].gridLevel
		}
		for(var i=0;i<3;i++){
			if(initGridData[gridId]){
				path[i] = initGridData[gridId].name ;
				code[i] = initGridData[gridId].infoOrgCode;
				gridName[i] = initGridData[gridId].name;
				gridId = initGridData[gridId].pId;
			}
		}
		showData(zNodes,code[parseInt(code.length)-2]);
		xuanzhong(path,parseInt(path.length)-1);
		$("#quxianName").html(gridName[parseInt(code.length)-2]);
		
	}  
	
	function findPath(data){
		if(data != null && data.length > 0){
			for(var i = 0,l = data.length;i < l;i++){
				if(data[i].gridId == targetPid){
					if(data[i].pId == "" || data[i].pId == 'NaN' || data[i].gridLevel == 2){
						gridLevel = data[i].gridLevel;
						break ;
					}			
					targetId = data[i].gridId;
					targetPid = data[i].pId;
					targetName = data[i].name;
					gridLevel = data[i].gridLevel;
					parentCode = data[i].infoOrgCode;
					break;
				}else{
					findPath(data[i].children);
				}
			}
		}
		return targetName;
	}
	
	
	function xuanzhong(strData,strDatalength){
		//diyu-quxian diyu-jiedao
		var diyuQuxianChildren = $("#diyu-quxian").children();
		$("#diyuPath").html("");
		if(strDatalength == 1){
			$("#diyuPath").html(strData[0]);
			for(var i = 0,l=diyuQuxianChildren.length;i<l;i++){
				if(diyuQuxianChildren[i].innerText == strData[0]){
					$(diyuQuxianChildren[i]).addClass('active');
					break;
				}
			}
			
		}else{
			
			$("#diyuPath").html(strData[1]);            
        	$('#connect').text('---');
        	$('#jiedaoPath').text(strData[0]);
        	
			for(var i = 0,l=diyuQuxianChildren.length;i<l;i++){
				if(diyuQuxianChildren[i].innerText == strData[1]){
					$(diyuQuxianChildren[i]).addClass('active');
					break;
				}
			}
			var diyuJiedaoChildren = $("#diyu-jiedao").children();
			for(var i = 0,l=diyuJiedaoChildren.length;i<l;i++){
				if(diyuJiedaoChildren[i].innerText == strData[0]){
					$(diyuJiedaoChildren[i]).addClass('active');
					break;
				}
			}
		}

		$("#diyuPath").html(str);		

	}
	
</script>
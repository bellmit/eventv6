<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>语音调度</title>

<script type="text/javascript" src="${rc.getContextPath()}/js/call/callCenter.js"></script>
<!--插件如语音盒 使用js-->
<script type="text/javascript" src="${rc.getContextPath()}/js/plugIn/plug_in.js"></script>
<#include "/component/commonFiles-map-1.1.ftl" />
<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
    <script type="text/javascript" src="${uiDomain!''}/js/zzgl_core.js"></script>
<style type="text/css">
/*---------------------------------语音呼叫-----------------------------------------*/

.voice{width:706px;}
.AddressList{width:370px; border-right:1px solid #d8d8d8;}
.AddressList .search{background:#f4f4f4; height:35px; padding-top:6px; overflow:hidden;}
.AddressList .search ul li{float:left; margin-left:5px; *margin-left:3px; display:inline;}
.AddressList .list{height:355px; overflow:auto;}
.AddressList .list dl{padding:10px; border-bottom:1px solid #d8d8d8; height:103px;}
.AddressList .list dl dt{width:90px; height:100px; float:left; margin-right:10px;}
.AddressList .list dl dt img{width:90px; height:100px;}
.AddressList .list dl dd{width:190px; float:left;}
.AddressList .list dl dd p{margin-bottom:15px;}
.AddressList .list dl dd p a{padding:0 10px; color:#fff; margin-right:10px; display:block; height:28px; line-height:28px; float:left;}
.AddressList .list dl dd p a img{vertical-align:middle; margin-right:5px; margin-top:-3px;}
.AddressList .page{background:#f4f4f4; height:34px; line-height:34px; text-align:center; color:#5e5e5e;}
.keyboard{width:335px;}
.YYphone{width:256px; margin:0 auto; font-family:Arial, Helvetica, sans-serif;}
.NumberArea input{width:256px; height:55px; text-align:center; font-size:36px; line-height:55px; color:#5fb9e3; border:none 0; font-family:Arial, Helvetica, sans-serif;}
.numbers ul li{width:55px; height:55px; background:url(${rc.getContextPath()}/images/yy_03.png); cursor:pointer; text-align:center; line-height:55px; font-size:32px; color:#5fb9e3; float:left; margin-right:45px; margin-bottom:13px;}
.numbers ul li:hover{background:url(${rc.getContextPath()}/images/yy_05.png); color:#fff;}
.numbers ul li span{display:block; width:32px; height:32px; margin:11px auto 0;}
.qingkong{background:url(${rc.getContextPath()}/images/yy_10.png);}
.numbers ul li:hover .qingkong{background:url(${rc.getContextPath()}/images/yy_11.png);}
.shanchu{background:url(${rc.getContextPath()}/images/yy_13.png);}
.numbers ul li:hover .shanchu{background:url(${rc.getContextPath()}/images/yy_14.png);}
.call{width:256px; height:48px; background:url(${rc.getContextPath()}/images/yy_17.png); cursor:pointer;}
.call:hover{background:url(${rc.getContextPath()}/images/yy_19.png);}
.SendMessage{padding:10px 20px;}
.SendMessage .title{font-size:16px; font-family:Microsoft YaHei; margin-bottom:7px;}
.SendMessage .con{margin-bottom:20px;}
.send{width:295px; height:48px; background:url(${rc.getContextPath()}/images/yy_21.png); cursor:pointer;}
.send:hover{background:url(${rc.getContextPath()}/images/yy_23.png);}
.CyanBg{background:#16A085;}
.CyanBg:hover{background:#1ABC9C; text-decoration:none;}
.GreenBg{background:#27AE60;}
.GreenBg:hover{background:#2ECC71; text-decoration:none;}
.Small_chaxun, .Small_chongzhi{display:block; width: 35px; height: 28px; border-radius:3px; transition:all 0.2s; background-repeat:no-repeat; background-position:9px 6px;}
.Small_chaxun{background-image:url(${rc.getContextPath()}/images/sys_13.png); background-color:#2980B9;}
.Small_chongzhi{background-image:url(${rc.getContextPath()}/images/reset.png); background-color:#27AE60;}
.Small_chaxun:hover{background-color:#3498DB;}
.Small_chongzhi:hover{background-color:#2ECC71;}

</style>
<script type="text/javascript">
function hujiaoCenterCall(telNo){
	if(telNo==""){
		alert("呼叫号码为空");
		return false;
	}
	if (!fCheckAgent()) {
	    alert("您没有安装Agent,请安装Agent！");
		// 没有安装AGENT
		return;
	}
	oOCX.IMRISetValue(0, "SERVICE_SPACE_6", " DIALOUT;" + telNo);
}
</script>
</head>
<iframe id="cross_domain_iframe" scrolling='no' frameborder='0' style='display:none;'></iframe>
<body class="easyui-layout">
  <input type="hidden" id="userGridId" value="${gridId?c}" />
  <input type="hidden" id="gridId" value="" />
  <input type="hidden" id="pageSize" value="20" />
	<div class="OpenWindow">
    <div class="voice">
        <div class="AddressList fl">
            <div class="search">
                <ul>
                    <li><input id="name" name="" type="text" onkeydown="if (event.keyCode==13) {}" onblur="if(this.value=='')value='输入姓名';" onfocus="if(this.value=='输入姓名')value='';" value="输入姓名" class="inp1" style="width:90px; *width:68px;" /></li>
                    <li>
                    	<select id="duty" name="" class="sel1" style="width:90px;">
                    		<option value="">全部职务</option>
				    		<#if dutyDC??>
								<#list dutyDC as l>
									<option value="${l.COLUMN_VALUE}">${l.COLUMN_VALUE_REMARK}</option>
								</#list>
					        </#if>
                    	</select>
                    </li>
                 	<li><input name="gridName" id="gridName" type="text" onkeydown="if (event.keyCode==13) {}" onblur="if(this.value=='')value='所属网格';" onfocus="if(this.value=='所属网格')value='';" value="所属网格" class="inp1" style="width:90px;" /></li>
                    <li><a href="#" class="Small_chaxun" title="点击查询" onclick="loadMessage(1,$('#pageSize').val());"></a></li>
                    <li><a href="#" class="Small_chongzhi" title="点击重置" onclick="resetCondition()"></a></li>
                </ul>
            </div>
            <div id="content-d" class="list content light" style="width:370px;">
            	<div id="listData" style="width:370px;"></div>
            </div>
         <div class="NorPage" style="width:360px;">
        	<ul>
            	<li class="PreBtn"><a href="javascript:change('1');"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/pre.png" /></a></li>
            	<li style="width:120px;">共查询到 <span id="records">0</span> 条记录</li>
            	<li class="yema">共 <span id="pagination-num">0</span>/<span id="pageCount">0</span> 页 转至 </li>
                <li class="PageInp"><input id="inputNum" name="inputNum" type="text" /></li>
                <li class="PageBtn"><input type="button" value="确定" onclick="pageSubmit()"/></li>
            	<li class="NextBtn"><a href="javascript:change('2');"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/next.png" /></a></li>
            </ul>
        </div>
        </div>
        <div class="keyboard fr">
        	<div class="ConList" style="padding-top:5px;">
                    <div class="nav" id="tab" style="padding-left:15px;">
                        <ul>
                            <li class="current">语音盒呼叫</li>
                            <li id="sendMessageLi">发送短信</li>
                            <li id="callCenterLi">呼叫中心</li>
                        </ul>
                    </div>
                    <div class="ListShow tabss" style="border:none;">
                    	<div id="YYphone">
                        	<div class="YYphone">
                            	<div class="NumberArea"><input id="callerNbr" name="callerNbr" onblur="value=value.replace(/[^\d#*]/g,'')" onkeyup="value=value.replace(/[^\d#*]/g,'') " type="text" value="" /></div>
                                <div class="numbers">
                                	<ul>
                                    	<li onclick="enterNumber(1);">1</li>
                                    	<li onclick="enterNumber(2);">2</li>
                                    	<li onclick="enterNumber(3);" style="margin-right:0;">3</li>
                                    	<li onclick="enterNumber(4);">4</li>
                                    	<li onclick="enterNumber(5);">5</li>
                                    	<li onclick="enterNumber(6);" style="margin-right:0;">6</li>
                                    	<li onclick="enterNumber(7);">7</li>
                                    	<li onclick="enterNumber(8);">8</li>
                                    	<li onclick="enterNumber(9);" style="margin-right:0;">9</li>
                                    	<li><span class="qingkong" onmouseup="mouon('chongzhi')"  onmousedown="moudown('chongzhi')" onclick="zhongzhi();"></span></li>
                                    	<li onclick="enterNumber(0);">0</li>
                                    	<li style="margin-right:0;"><span class="shanchu" onclick="shanchu()" onmouseup="mouondelete('delete')"  onmousedown="moudowndelete('delete')"></span></li>
                                    </ul>
                                </div>
                                <div class="clear call" onclick="callOutFunYY('2');"></div>
                            </div>
                        </div>
                    	<div id="sendMessage" class="hide">
                        	<div class="SendMessage">
                            	<div class="title">收信人</div>
                                <div class="con">
                                	<textarea id="otherDialNum" name="otherDialNum" name="" cols="" rows="" class="area1" style="width:290px;"></textarea>
                                </div>
                            	<div class="title">短信内容</div>
                                <div class="con">
                                	<textarea id="content" name="" cols="" rows="" class="area1" style="width:290px; height:140px;"></textarea>
                                </div>
                                <div class="send" id="sendSMSBtn"  name="but" onclick="afterSendSMS('')"></div>
                            </div>
                        </div>
                    	<div class="hide">
	                    		<div id="YYphone">
	                        	<div class="YYphone">
	                            	<div class="NumberArea"><input id="callerNbr2" name="callerNbr2" onblur="value=value.replace(/[^\d#*]/g,'')" onkeyup="value=value.replace(/[^\d#*]/g,'') " type="text" value="" /></div>
	                                <div class="numbers">
	                                	<ul>
	                                    	<li onclick="enterNumber2(1);">1</li>
	                                    	<li onclick="enterNumber2(2);">2</li>
	                                    	<li onclick="enterNumber2(3);" style="margin-right:0;">3</li>
	                                    	<li onclick="enterNumber2(4);">4</li>
	                                    	<li onclick="enterNumber2(5);">5</li>
	                                    	<li onclick="enterNumber2(6);" style="margin-right:0;">6</li>
	                                    	<li onclick="enterNumber2(7);">7</li>
	                                    	<li onclick="enterNumber2(8);">8</li>
	                                    	<li onclick="enterNumber2(9);" style="margin-right:0;">9</li>
	                                    	<li><span class="qingkong" onclick="zhongzhi2();"></span></li>
	                                    	<li onclick="enterNumber2(0);">0</li>
	                                    	<li style="margin-right:0;"><span class="shanchu" onclick="shanchu2()"></span></li>
	                                    </ul>
	                                </div>
	                                <div class="clear call" onclick="callOutFunHjzx('1');"></div>
	                            </div>
	                        </div>
                    	</div>
                    </div>
        	</div>
        </div>
        <div class="clear"></div>
    </div>
</div>
<OBJECT id="oOCX" codeBase="" height="0" width="0" align="center" classid="clsid:69FA966F-0F98-4CB5-B73A-78D21F7CFD67"></OBJECT>
<#include "/component/ComboBox.ftl">

<script type="text/javascript">
	$(function(){
		AnoleApi.initGridZtreeComboBox("gridName", "gridId", function (gridId, items){
			if(items!=undefined && items!=null && items.length>0){
				var grid = items[0];
				//$("#regionCode").val(grid.orgCode);
			} 
		});
	})
	
	//分页
     function change(_index){
        var flag;
        var pagenum = $("#pagination-num").text();
        var lastnum = $("#pageCount").text();
        var pageSize = $("#pageSize").val();
		var firstnum = 1;
		switch (_index) {
			case '1':		//上页
			    if(pagenum==1){
			      flag=1;
			      break;
			    }
				pagenum = parseInt(pagenum) - 1;
				pagenum = pagenum < firstnum ? firstnum : pagenum;
				break;
			case '2':		//下页
			    if(pagenum==lastnum){
			      flag=2;
			      break;
			    }
				pagenum = parseInt(pagenum) + 1;
				pagenum = pagenum > lastnum ? lastnum : pagenum;
				break;
		    case '3':
		        flag=3;
		        pagenum=1;
		        break;
		    case '4':
		        pagenum = inputNum;
		        if(pagenum==lastnum){
			      flag=4;
			      break;
			    }
				pagenum = parseInt(pagenum);
				pagenum = pagenum > lastnum ? lastnum : pagenum;
				break;
			default:
				break;
		}
		
		if(flag==1){
		  alert("当前已经是首页");
		  return;
		}else if(flag==2){
		  alert("当前已经是尾页");
		  return;
		}
	    loadMessage(pagenum,pageSize);
	}
	
	function pageSubmit(){
		inputNum = $("#inputNum").val();
		var pageCount = $("#pageCount").text();
		if(isNaN(inputNum)){
			inputNum=1;
		}
		if(parseInt(inputNum)>parseInt(pageCount)){
			inputNum=pageCount;
		}
		if(inputNum<=0||inputNum==""){
			inputNum=1;
		}
		change('4');
		}

    ocxInitFun();//初始化
    var userGridId = $('#userGridId').val();
	var results="";//获取定位对象集合
	function loadMessage(pageNo,pageSize){
     	results="";
		var gridId = $('#gridId').val() == '' ? userGridId:$('#gridId').val();
		var name = $('#name').val();
		
		if (name == "输入姓名") {
			name = "";
		}
		
		//var duty="";
		//if(index!=0){
			 //duty = $("#duty option:selected").val();
		//}
		
		var duty = $("#duty option:selected").val();
		var pageSize = $("#pageSize").val();
		/*var pageNo;
		var totalPage=$('#pageCount').text();
		if(i!=-2){
			if(i==0){
				pageNo = 1;
			} else if(i==-3) {
				if(totalPage=="") return;
				if(parseInt(totalPage) == 0)
					return;
				pageNo = totalPage;
			} else {
				pageNo = $('#curPage').text();
				if(parseInt(totalPage) == 0)
					return;
				if(pageNo==totalPage && i>0)
					return;
				if(pageNo==1 && i<0)
					return;
				pageNo = parseInt(pageNo)+i;
			}
		} else {
			pageNo = $('#toPage').val();
			var reg = /^[1-9]+[0-9]*]*$/;
			if(!reg.test(pageNo))
				return;
			pageNo = parseInt(pageNo);
			if(pageNo>totalPage)
				return;
			if(pageNo<1)
				return;
		}*/
		$.blockUI({message: "加载中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'20%',
    	background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
		var postData = 'page='+pageNo+'&rows='+pageSize+'&gridId='+gridId+'&name='+name+'&duty='+duty;
		$.ajax({
			type: "POST",
			url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgisCenterControl/gridAdminListData.json?t='+Math.random(),
			data: postData,
			dataType:"json",
			success: function(data){
				$.unblockUI();
				//设置页面页数
				$('#pagination-num').text(pageNo);
				//$('#curPage').text(pageNo);
				$('#records').text(data.total);
				var totalPage = Math.floor(data.total/pageSize);
				if(data.total%pageSize>0) totalPage+=1;
				$('#pageCount').text(totalPage);
				var list=data.rows;
				var tableBody="";
				if(list && list.length>0) {
					for(var i=0;i<list.length;i++){
						var val=list[i];
						var name=val.partyName==null?'':val.partyName;
						var imgSrc=val.photo==null?'':val.photo;
						tableBody+="<dl>";
						tableBody+="<dt>";
						if(val.photo==null){
							tableBody+="<img src=\"${uiDomain!''}/images/map/gisv0/map_config/unselected/situation/untitled.png\" />";
						}else{
							tableBody+="<img src=\"${IMG_URL}"+val.photo+"\" />";
						}
						tableBody+="</dt>";
						tableBody+="<dd>";
						tableBody+="<p>";
						tableBody+="<span class=\"FontDarkBlue\" style=\"font-size:16px; font-weight:bold; padding-right:10px;\">"+(val.partyName==null?'':val.partyName)+"</span>"+(val.dutyLabel==null?'':val.dutyLabel);
						tableBody+="</p>";
						tableBody+="<p>";
						tableBody+="联系电话：<span class=\"FontDarkBlue\">"+(val.mobileTelephone==null?'':val.mobileTelephone)+"</span>";
						tableBody+="</p>";
						tableBody+="<p>";
						tableBody+="<a href='javascript:showCall(\""+(val.mobileTelephone==null?'':val.mobileTelephone)+"\",\""+name+"\",\""+imgSrc+"\")' class=\"CyanBg\"><img src=\"${rc.getContextPath()}/images/yy_01.png\" />语音盒呼叫</a>";
						tableBody+="<a href='javascript:sendMessage(\""+(val.mobileTelephone==null?'':val.mobileTelephone)+"\")' class=\"GreenBg\"><img src=\"${rc.getContextPath()}/images/yy_02.png\" />发短信</a>";
						tableBody+="</p>";
					    tableBody+="</dd>";
						tableBody+="</dl>";
						results=results+","+val.gridAdminId;
					}
					results=results.substring(1, results.length);
				} else {
					$('#pagination-num').text("0");
					tableBody+='<div class="search_card" ><table width="96%" border="0" cellspacing="0" cellpadding="0" align="center" class="search_info"><tr style="height: 85px"><td align="center" style="color:red;">未查到相关数据！</td></tr><table></div>';
				}
				$("#listData").html(tableBody);
			},
			error:function(data){
				$.unblockUI();
				var tableBody='<div class="search_card" ><table width="96%" border="0" cellspacing="0" cellpadding="0" align="center" class="search_info"><tr style="height: 85px"><td align="center" style="color:red;">数据读取错误！</td></tr><table></div>';
				$("#listData").html(tableBody);
			}
		});
	}
	var name="";
	var imgSrc="";
	//初始化
	  //page(1,0);
	  loadMessage(1,$("#pageSize").val());
	//呼叫电话号码
	  function callPhone(phones,name1,imgSrc1){
	  		$('#otherDialNum').val("");
	  		$('#content').val("");
		    document.getElementById('sendMessages').style.display = "none"; 
			document.getElementById('bohao').style.display = "";
		   	document.getElementById("callerNbr").value=phones;
		   	name=name1;
		   	imgSrc=imgSrc1;
		  }
	 //发送短信
	  function sendMessage(phone){
		//document.getElementById('bohao').style.display = "none"; 
		//document.getElementById('sendMessages').style.display = "";
		 if(phone==null || phone==""){
			 $.messager.alert("提示", "没有电话号码可以发送！", "info");
			return;
		 }
		
		$(".current").removeClass("current");
		$("#YYphone").addClass("hide");
		
		$("#sendMessageLi").addClass("current");
		$("#sendMessage").removeClass("hide");
		
	  	var phones="";
	  	var val =document.getElementById("otherDialNum");
	  	if(val!=null&&val.value!=''){
	  		if(val.value.indexOf(phone) < 0 ){ 
	  		   phones = val.value + "," + phone;
	  		}else{
	  			 phones = val.value
	  		}
	  	}else{
	  		phones = phone;
	  	}
	  	document.getElementById("otherDialNum").value=phones;
	  }
	  function zhongzhi(){
		  	//$("#call_center_status").html("&nbsp;");
		  	document.getElementById("callerNbr").value="";
		  }
		  
	  function zhongzhi2(){
		  	//$("#call_center_status").html("&nbsp;");
		  	document.getElementById("callerNbr2").value="";
		  }
		  
		
		
	  function enterNumberover(aa){
		  	var url="${rc.getContextPath()}/images/callCenter/blueback.png";
		    var cl="."+aa;
		    $(cl).css("color","#ffffff");
		  	$(cl).css("background","url("+url+") no-repeat");
		  	
		  }
	  function enterNumberout(aa){
		  	var url="${rc.getContextPath()}/images/callCenter/jianpan.png";
		  	var cl="."+aa;
		  	 $(cl).css("color","#000000");
		  	$(cl).css("background","url("+url+") no-repeat");
		  	
		  }
	  function enterNumber(num){
		  	//$("#call_center_status").html("&nbsp;");
		  	var caller = document.getElementById("callerNbr").value;
		  	if(caller.length>19){
		  		return false;
		  	}
		  	 document.getElementById("callerNbr").value=caller+num;
		  	//alert(num);
		 	name='';
			imgSrc='';
		  }
	  function enterNumber2(num){
		  	//$("#call_center_status").html("&nbsp;");
		  	var caller = document.getElementById("callerNbr2").value;
		  	if(caller.length>19){
		  		return false;
		  	}
		  	 document.getElementById("callerNbr2").value=caller+num;
		  	//alert(num);
		 	name='';
			imgSrc='';
		  }
	  //删除号码
	  function shanchu(){
		  	//$("#call_center_status").html("&nbsp;");
		  	var nums=document.getElementById("callerNbr").value;
		  	if(nums==""){
		  		return false;
		  	}
		  	document.getElementById("callerNbr").value=nums.substring(0,nums.length-1);
		  }
	  //
	  //删除号码
	  function shanchu2(){
		  	//$("#call_center_status").html("&nbsp;");
		  	var nums=document.getElementById("callerNbr2").value;
		  	if(nums==""){
		  		return false;
		  	}
		  	document.getElementById("callerNbr2").value=nums.substring(0,nums.length-1);
		  }
	  //
	  function mouon(aa){
	    var url="${rc.getContextPath()}/images/callCenter/"+aa+".png";
	  	var cl="."+aa;
	  	$(cl).css("color","#000000");
	  	$(cl).css("background","url("+url+")");
	  }
	  function moudown(aa){
		 var url="${rc.getContextPath()}/images/callCenter/"+aa+"2.png";
		 var cl="."+aa;
		 $(cl).css("color","#ffffff");
		 $(cl).css("background","url("+url+")");
	    }
	  function mouondelete(aa){
	  	 var url="${rc.getContextPath()}/images/callCenter/"+aa+".png";
	  	 var cl="."+aa;
	   	 $(cl).attr("src",url)
	  }
	  function moudowndelete(aa){
	  	var url="${rc.getContextPath()}/images/callCenter/"+aa+"2.png";
	  	var cl="."+aa;
	  	$(cl).attr("src",url)
	  	
	  }
	  //语音呼叫
	  function callOutFunYY(type) {
	  	var caller = document.getElementById("callerNbr").value;
	  	if (caller == undefined) {
	  		caller = '';
	  	}
	  	if(caller==""){
	  		alert("呼叫号码不能为空！");
	  		//var _html_sta = "<strong><font color=red>&nbsp;&nbsp;呼叫号码不能为空 </font></strong>";
	  	   //$("#call_center_status").html(_html_sta);
	  	   return false;	
	  	}
	    	  callPhone1(caller);
	  }
	  // 呼叫中心
	  function callOutFunHjzx(type) {
	  	var caller = document.getElementById("callerNbr2").value;
	  	if (caller == undefined) {
	  		caller = '';
	  	}
	  	if(caller==""){
	  		alert("呼叫号码不能为空！");
	  		//var _html_sta = "<strong><font color=red>&nbsp;&nbsp;呼叫号码不能为空 </font></strong>";
	  	   //$("#call_center_status").html(_html_sta);
	  	   return false;	
	  	}
	  	hujiaoCenterCall(caller);
	  }
	 
	  <!--语音盒-->
		function callPhone1(phoneNum){
			//var reporterName="";
			if (phoneNum==null || phoneNum==""){
				alert("反馈人员电话不存在！");
				return;
			}else {
				phoneNum = phoneNum.replace(/[ \-]/g,"");//去除电话号码中包含的"-"和空格
				var pattern = /^\d*$/;
				if(phoneNum!="" && pattern.test(phoneNum)){//排除只包含空格和"-"的电话号码
					//callSpeech(phoneNum, reporterName, "${rc.getContextPath()}/images/map/arcgis/unselected/situation/untitled.png");
				
					showCall(phoneNum,name,imgSrc);
					name='';
					imgSrc='';
					//window.makeCall(phoneNum, reporterName, null);
				}else{
					alert("反馈人员电话："+phoneNum+"\n\n反馈人员电话格式有误！");
					return;
				}
			}
		}	  
	  
	  var isCross;
	<#if isCross??>
		isCross = '${isCross}';
	</#if>
	  
	  function showCall(bCall, userName, userImg){
	  	 if(userImg!=null && userImg!=""){
	  	 	userImg = "${RESOURSE_SERVER_PATH}" + userImg;
	  	 }
	  	 
	  	 if (isCross != undefined) { // 跨域
	  	 	var url = '${SQ_ZHSQ_EVENT_URL}';
	  	 	//url = url.replace("http://", "");
	  	 	userName = encodeURIComponent(encodeURIComponent(userName));
			var urlDomain = "${SQ_ZZGRID_URL}/zzgl/map/gis/commonCrossDomain.jhtml?callBack="+"showVoiceCall('"+url+"','','"+bCall+"','"+userName+"','"+userImg+"')";
			$("#cross_domain_iframe").attr("src",urlDomain);
		} else {
			showVoiceCall("${rc.getContextPath()}", window.parent.showCustomEasyWindow, bCall, userName, userImg);
		}
	  }
		
//		function isphone(a){
//			var pattern=/^((0?1[358]\d{9})|((0(10|2[1-3]|[3-9]\d{2}))?[1-9]\d{6,7}))$/;
//			return pattern.test(a);
//		}
	  //发送短信
	  function afterSendSMS(defaultTip){
			  //号码
				var otherDialNum = $('#otherDialNum').val() == defaultTip ? '' : $('#otherDialNum').val();
				if(otherDialNum==null ||otherDialNum==""){
					alert("请先输入手机号码！");
					return false;
				}else{
					var phones = otherDialNum.split(",");
					for(var i=0;i<phones.length;i++){
						if(!isMobile(phones[i])){
							alert("号码"+phones[i]+"不正确!");
							return false;
						}
					}
				}
				if($("#content").val()==null ||$("#content").val()==""){
					alert("请先输入短信内容！");
					return false;
				}
			//var bases = document.getElementsByName("base");
			$.ajax({
			    	type: "POST",
					url: "${rc.getContextPath()}/zhsq/map/arcgis/arcgisCenterControl/sendSMS.jhtml",
					data: {
						otherDialNum : otherDialNum,
						content : $("#content").val()
					},
					success: function(data){
						if(data.result==true) {
							$.messager.alert('成功','操作成功！','success');
						
						}else{
							$.messager.alert('错误','操作失败！','error');
						}
					}
			});
		}
	  
	  //网格树
	 function singleMixedGridSelectCallback(gridId,gridName,orgId,orgCode,gridPhoto){
		$("#gridId").attr("value",gridId);
		$("#gridName").attr("value",gridName);
	  }
	 //重置
	 function resetCondition(){
			$("#name").val("输入姓名");
			$("#gridName").val("所属网格");
			$("#duty option:first").attr("selected",true);
			//$("#duty").combobox("setValue","");
			$("#gridId").val("");
		}
	 //短信推送
	 function smsPush(){
		 document.getElementById('bohao').style.display = "none"; 
		 $("#otherDialNum").val("");
		 $("#content").val("");
		 document.getElementById('sendMessages').style.display = "";
	 }
</script>


<!--  -->
<!-- 
#网格选择
说明：
#1、入口函数showSingleMixedGridSelector，参数：无
#2、引用页面需要有回调函数singleMixedGridSelectZtreeCallback，参数：gridId,gridName,orgId,orgCode,gridPhoto
 -->
 
<div id="SingleMixedGridSelector" class="easyui-window" title="选择网格" minimizable="false" maximizable="false" collapsible="false" closed="true" modal="false" style="width:600px;height:300px;padding:1px;overflow:hidden;">
	<table width="100%" border="0" cellspacing="1" cellpadding="0">
		<tr>
			<td style="background-color: #F3F8FE;">
				<div id="singleMixedGridSelectorZTree" class="ztree" style="height:230px; width:230px; overflow:auto;"></div>
            </td>
		</tr>
		<tr>
			<td align="center">
				<input type="button" value="确定" onclick="singleMixedGridSelectComplete()" />
				<input type="button" value="关闭" onclick="closeSingleMixedGridSelector()" />
			</td>
		</tr>
	</table>
</div>
<script>
	var $singleMixedGridSelectorWin;
	var treeInitFlag = false;

	function showSingleMixedGridSelector() {
		if(!treeInitFlag) {
			loadMixedGridTree();
			treeInitFlag = true;
		}
	    $singleMixedGridSelectorWin = $('#SingleMixedGridSelector').window({
	    	title:"选择网格（双击或者选中节点）",
	    	width: 260,
	    	height: 306,
	    	shadow: true,
	    	modal:true,
	    	closed:true,
	    	minimizable:false,
	    	maximizable:false,
	    	collapsible:false
	    });
		$singleMixedGridSelectorWin.window('open');
	}
	
	function closeSingleMixedGridSelector() {
		$singleMixedGridSelectorWin.window('close');
	}
	
	function loadMixedGridTree() {
		var setting = {
			async: {
				enable: true,
				url:"${rc.getContextPath()}/zhsq/grid/mixedGrid/gridZTree.json",
				autoParam:["id=gridId"],
				dataFilter: filter
			},
			check:{
				enable: true,
				chkStyle: "checkbox",
				chkboxType: { "Y": "s", "N": "s" }
			},
			callback:{
				onDblClick:zTreeOnDblClick
			}
		};
		$.fn.zTree.init($("#singleMixedGridSelectorZTree"), setting);
	}
	
	function zTreeOnDblClick(event, treeId, treeNode) {
    	singleMixedGridSelectCallback(treeNode.id, treeNode.name, treeNode.orgId, treeNode.orgCode, treeNode.gridPhoto);
    	closeSingleMixedGridSelector();
	};
	
	function filter(treeId, parentNode, childNodes) {
		if (!childNodes) return null;
		for (var i=0, l=childNodes.length; i<l; i++) {
			if(childNodes[i].name) {
				childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
			}
		}
		return childNodes;
	}
	
	function singleMixedGridSelectComplete() {
		var treeObj = $.fn.zTree.getZTreeObj("singleMixedGridSelectorZTree");
		var nodes = treeObj.getSelectedNodes();
		if(nodes==null || nodes.length==0) return;
		singleMixedGridSelectCallback(nodes[0].id, nodes[0].name, nodes[0].orgId, nodes[0].orgCode, nodes[0].gridPhoto);
		closeSingleMixedGridSelector();
	}
</script>
<!-- custom scrollbar plugin -->
<script>
	(function($){
		$(window).load(function(){
			var options = {
				axis : "yx",
				theme : "minimal-dark"
			};
			enableScrollBar('content-d',options);
		});
	})(jQuery);
</script>
</body>
</html>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>语音调度</title>
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/ligerUI/skins/Aqua/css/ligerui-dialog.css" />
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/main_new.css" />
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-last/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-last/themes/icon.css">
	<script type="text/javascript" src="${uiDomain!''}/js/zzgl_core.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/call/callCenter.js"> 
</script>
<script src="${rc.getContextPath()}/js/jqueryeasyui-last/jquery.min.js" type="text/javascript"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-last/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-last/locale/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/ztree/zTreeStyle/zTreeStyle.css" />
<script type="text/javascript" src="${rc.getContextPath()}/js/ztree/jquery.ztree.core-3.5.js"></script>
<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/ligerUI/js/core/base.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/ligerUI/js/plugins/ligerForm.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/ligerUI/js/plugins/ligerDateEditor.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/ligerUI/js/plugins/ligerCheckBox.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/ligerUI/js/plugins/ligerComboBox.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/ligerUI/js/plugins/ligerButton.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/ligerUI/js/plugins/ligerRadio.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/ligerUI/js/plugins/ligerSpinner.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/ligerUI/js/plugins/ligerTextBox.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/ligerUI/js/plugins/ligerDialog.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/ligerUI/js/plugins/ligerResizable.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/ligerUI/js/plugins/ligerDrag.js"></script>




<style type="text/css">
img {
	border-top-style: none;
	border-right-style: none;
	border-bottom-style: none;
	border-left-style: none;
}

.bohao {
	height: 312px;
	width: 294px;
	margin: 0 auto;
}
.search_info {
	margin-left: 15px;
	margin-right: 15px;
	margin-top: 10px;
	position:relative;
}

.search_info td {
	height: 24px;
}

.search_info th {
	text-align: left;
	height: 30px;
	font-size: 18px;
}
.myphoto {
	padding-right: 15px;
}

.ffcs_myphone {
	background:
		url(${rc.getContextPath()}/images/callCenter/yuyin_icon1.png)
		repeat-x;
	height: 28px;
	width: 28px;
	border: none;
	display: inline-block;
	position:absolute;
	right:85px;
	top:50px;
}
.ffcs_choose {
	background:
		
		repeat-x;
	height: 28px;
	width: 28px;
	border: none;
	display: inline-block;
	position:absolute;
	right:10px;
	top:50px;
}

.ffcs_mymsg {
	background:
		url(${rc.getContextPath()}/images/callCenter/yuyin_icon2.png)
		repeat-x;
	height: 28px;
	width: 28px;
	border: none;
	display: inline-block;
    position:absolute;
	right:50px;
	top:50px;
}

.search_card {
	background:
		url(${rc.getContextPath()}/images/callCenter/man_cardbg.png) repeat-x;
	width: 350px;
	height: 120px;
	float: left;
	cursor: pointer;
}
.shuru {
	height:46px;
	width:294px;
	margin:0;
}
.wenben {
	margin: 0px;
	height: 40px;
	width: 236px;
	float:left;
	font-size:20px;
	font:"微软雅黑";
}
.call_center_status {
    margin-top:3px;
	height:5px;
	width: 236px;
	float:left;
	font:"微软雅黑";
}


.shanchu {
	margin: 0px;
	width:52px;
	height:36px;
	float:right;
	cursor: pointer;
}


.shuzi table tr td a {
	color: #000;
	text-decoration: none;
	width: 95px;
	height: 30px;
	display: inline-block;
	text-align: center;
	cursor: pointer;
}

.shuzi table tr td a:hover {
	color: #06F;
}
.shuzi table tr td {
	text-align:center;
	background:url(${rc.getContextPath()}/images/callCenter/jianpan.png) no-repeat;
	height:48px;
	width:94px;
	font-family:"微软雅黑";
	font-size:20px;
	cursor: pointer;
}
.shuzi {
	height:208px;
	width:294px;
	text-align:center;
	margin-top: 2px;
	margin-right: 0px;
	margin-bottom: 0px;
	margin-left: 0px;
}
a {
	color:#000;
	text-decoration: none;
}
a:hover {
	color:#06F;
}
.footer {
	width: 294px;
}

.hujiao{ width:94px; height:48px; margin:2px 2px;; background:url(${rc.getContextPath()}/images/callCenter/hujiao.png) no-repeat; display:inline-block;}
.yuyin{ width:94px; height:48px; margin:2px 2px;; background:url(${rc.getContextPath()}/images/callCenter/yuyin.png) no-repeat; display:inline-block;}
.chongzhi{ width:94px; height:48px; margin:2px 2px;; background:url(${rc.getContextPath()}/images/callCenter/chongzhi.png) no-repeat; display:inline-block;}
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
<body class="easyui-layout">
<div id="releasedRecordContentDiv" region="center"  border="false" style="overflow:hidden;width: 100%;height: 100%; ">
<OBJECT id="oOCX" codeBase="" height="0" width="0" align="center" classid="clsid:69FA966F-0F98-4CB5-B73A-78D21F7CFD67"></OBJECT>
  <input type="hidden" id="gridId" value="${gridId?c}" />
	<div style="margin-top:5px; padding-bottom:8px; border-bottom:1px solid #ccc;" >
		姓名：<input type="text" id="name" style="width:70px" />
		所属网格：<input type="text" readonly="readonly" style="width:90px;" onclick="showSingleMixedGridSelector();"  name="gridName" id="gridName" value="<#if gridName??>${gridName}</#if>" />
		职务：<select id="duty" class="easyui-combobox" data-options="panelHeight:null,width:100">
		    		<option value="">--全部--</option>
		    		 <#if dutyDC??>
						<#list dutyDC as l>
							<option value="${l.COLUMN_VALUE}">${l.COLUMN_VALUE_REMARK}</option>
						</#list>
			        </#if>
    	      </select>			
		<a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="page(0,1);">查询</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-reload" onclick="resetCondition()">重置</a>
		<a href="#"  id="clearAll" class="easyui-linkbutton" iconCls="icon-redo" onclick="delAll();" style="display:none">清除</a>
		<a href="#"  id="addAll" class="easyui-linkbutton" iconCls="icon-redo" onclick="selectAll();">全选</a>
	</div>

<div style="width: 100%;height: 100%;" >
	<div style="float: left; width:383px;height:310px;">
	<div id="mainpanel" class="pageContent" >			
		<div class="grid" >
			<div style="float: left;width:383px;height:320px;overflow:auto;" id="listData"></div>
		</div>
	</div>
	  <#include "/map/arcgis/standardmappage/pagination.ftl"/>
	</div>
	<div id="navtab">
			
			<div class="bohao" style="float: left;margin-left: 20px;display:none;" id="sendMessages" >
				 <div style="background: #f1f1f1;margin: 3px ;padding : 0px 0;">
				   		 对以下人员发送短消息
				 </div>
				<div style="margin: 10px 0 10px 15px;">
					<textarea id="otherDialNum" name="otherDialNum" rows="3" cols="33"></textarea>
					<input type="hidden" name="phoneNums" id="phoneNums" />
				</div>
				<div style="margin: 10px 0 10px 15px;">
					<b>消息内容：</b>
				</div>
				<div style="margin: 0 0 10px 15px;">
					<textarea id="content" name="content" rows="5" cols="33"></textarea>
				</div>
				<div  align="center" style="margin-top: 5px">
					<a id="sendSMSBtn"  name="but" onclick="afterSendSMS('')" style="background: url('${rc.getContextPath()}/images/callCenter/send_btn_03.png');width: 210px;height: 43px;display: inline-block;"/></a>
					<!--调用 GisDemo.jsp中事件弹出层的关闭按钮-->
					<a id="cancelSendSMSBtn" name="but" onclick="smsPush()" style="background: url('${rc.getContextPath()}/images/callCenter/send_btn_04.png');width: 210px;height: 43px;display: inline-block;"/></a>
				</div>
	
	      </div>
	</div>
	</div>
</div>

<script type="text/javascript">
    ocxInitFun();//初始化
    smsPush();
    var userGridId = $('#gridId').val();
	var results="";//获取定位对象集合
	function page(i,index){
     	results="";
		var gridId = $('#gridId').val() == '' ? userGridId:$('#gridId').val();
		var name = $('#name').val();
		var duty="";
		if(index!=0){
			 duty = $('#duty').combobox("getValue");
		}
		var pageSize = $("#pageSize").val();
		var pageNo;
		var totalPage=$('#totalPage').text();
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
		}
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
				$('#curPage').text(pageNo);
				var totalPage = Math.floor(data.total/pageSize);
				if(data.total%pageSize>0) totalPage+=1;
				$('#totalPage').text(totalPage);
				var list=data.rows;
				var tableBody="";
				if(list && list.length>0) {
					for(var i=0;i<list.length;i++){
						var val=list[i];
						tableBody+="<div class=\"search_card\" >";
						tableBody+="<table width=\"96%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" class=\"search_info\" id=\"choosetable\">";
						tableBody+="<tr>";
						tableBody+="<td width=\"80\" rowspan=\"4\" class=\"myphoto\">";
						if(val.photo==null){
							tableBody+="<img src=\"${uiDomain!''}/images/map/gisv0/map_config/unselected/situation/untitled.png\" width=\"75\" height=\"100\" />";
						}else{
							tableBody+="<img src=\"${RESOURSE_SERVER_PATH}"+val.photo+"\" width=\"75\" height=\"100\" />";
						}
						tableBody+="</td>";
						tableBody+="<th>姓名："+(val.partyName==null?'':val.partyName)+"</th>";
						tableBody+="</tr>";
						tableBody+="<tr>";
						tableBody+="<td>职务："+(val.dutyLabel==null?'':val.dutyLabel)+"</td>";
						tableBody+="</tr>";
						tableBody+="<tr>";
						var name=val.partyName==null?'':val.partyName;
						var imgSrc=val.photo==null?'':val.photo;
						var mobileTelephone = (val.mobileTelephone==null?'':val.mobileTelephone);
						tableBody+="<td>电话："+mobileTelephone+
								"<a href='javascript:showCall(\""+mobileTelephone+"\",\""+name+"\",\""+imgSrc+"\");' class=\"ffcs_myphone\" style=\"margin-left:30px;\"></a>"+
					            "<a href='javascript:showPhone1(\"checkBox"+i+"\")' class=\"ffcs_mymsg\" style=\"margin-left:30px;\"></a>"+
					           " <input type=\"checkbox\"  id=\"checkBox"+i+"\"  onclick='javascript:showPhone(\"checkBox"+i+"\")'  name=\"checkBox\" class=\"ffcs_choose\" style=\"margin-left:5px; \" value=\" "+(val.partyName==null?'':val.partyName)+","+(val.mobileTelephone==null?'':val.mobileTelephone)  +"\" />" ;
					    tableBody+="</td>";
					    tableBody+="</tr>";
					    tableBody+="<tr>";
					    tableBody+="<td>网格：<div style='display:inline-block;' title='"+val.gridPath+"'>"+(val.gridName==null?'':val.gridName)+"</div></td>";
					    tableBody+="</tr>";
						tableBody+="</table>";
							tableBody+="</div>";
						results=results+","+val.gridAdminId;
					}
					results=results.substring(1, results.length);
				} else {
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
	  page(0,0);
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
	//添加选中要发送的人
	function addAll(){
	    var rows= document.getElementsByName('checkBox');
		for(var i=0;i<rows.length;i++){
			if(rows[i].checked){
			nameAndPhone=rows[i].value;
			var phones = nameAndPhone.split(",");
				if(phones[1]!=null&&phones[1]!=''){
			       sendMessage(phones[1],phones[0]);
				}
			}
		}
	}
	

	//全选
	function selectAll(){
	     $("#addAll").hide();
	      $("#clearAll").show();
		 $("[name='checkBox']").attr("checked",'true');//全选  ;
		 addAll();
	}
	 //发送短信
	  function sendMessage(phone,name){
		//document.getElementById('bohao').style.display = "none"; 
		document.getElementById('sendMessages').style.display = "";
	  	var phones="";
	  	var ps ="";
	  	var val =document.getElementById("otherDialNum");
	  	var phoneNums =document.getElementById("phoneNums");
	  	if(phone!=null&&phone!=''){
		  	if(val!=null&&val.value!=''){
		  		if(val.value.indexOf(phone) < 0 ){ 
		  			ps = phoneNums.value + phone +",";
		  		   phones = val.value  + name+"("+ phone +"),";
		  		}else{
		  			 ps = phoneNums.value;
		  			 phones = val.value;
		  		}
		  	}else{
		  		ps = phone + ",";
		  	    phones =name+"("+phone+"),";
		  	}
	  	}else{
	  	 ps = phoneNums.value;
	  	 phones = val.value;
	  	}
	  	document.getElementById("phoneNums").value = ps;
	  	document.getElementById("otherDialNum").value=phones;
	  }
	  function zhongzhi(){
		  	$("#call_center_status").html("&nbsp;");
		  	document.getElementById("callerNbr").value="";
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
		  	$("#call_center_status").html("&nbsp;");
		  	var caller = document.getElementById("callerNbr").value;
		  	if(caller.length>19){
		  		return false;
		  	}
		  	 document.getElementById("callerNbr").value=caller+num;
		  	//alert(num);
		 	name='';
			imgSrc='';
		  }
	  //删除号码
	  function shanchu(){
		  	$("#call_center_status").html("&nbsp;");
		  	var nums=document.getElementById("callerNbr").value;
		  	if(nums==""){
		  		return false;
		  	}
		  	document.getElementById("callerNbr").value=nums.substring(0,nums.length-1);
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
	  //呼出
	  function callOutFun(type) {
	  	var caller = document.getElementById("callerNbr").value;
	  	if (caller == undefined) {
	  		caller = '';
	  	}
	  	if(caller==""){
	  		var _html_sta = "<strong><font color=red>&nbsp;&nbsp;呼叫号码不能为空 </font></strong>";
	  	   $("#call_center_status").html(_html_sta);
	  	   return false;	
	  	}
	  	
	  	
	      if(type=="1"){
	    	  	hujiaoCenterCall(caller);
	      	
	      }else{
	    	  callPhone1(caller);
	      	
	       
	      }
	  	
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
					//callSpeech(phoneNum, reporterName, "${rc.getContextPath()}/theme/scim/gis/query/img/untitled.png");
				if(imgSrc!=''){
					imgSrc="${RESOURSE_SERVER_PATH}"+imgSrc;
				}else{
					 imgSrc="${uiDomain!''}/images/map/gisv0/map_config/unselected/situation/untitled.png";
				}
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
		
		
		function showCall(bCall, userName, userImg){
			 if(bCall==null || bCall==""){
				alert("没有电话号码可以呼叫");
				return;
			 }
			 
			 userName = bCall == null || userName == "null" ? "" : userName;
			 if (userImg == null || userImg == '') { 
			  	 userImg = "${uiDomain!''}/images/map/gisv0/map_config/unselected/situation/untitled.png";
			 }else{
				userImg = '${RESOURSE_SERVER_PATH}'+userImg;
			 }
			 var url = "${rc.getContextPath()}/zhsq/map/arcgis/voiceInterface/go.jhtml?bCall=" + bCall + "&userName="
			 + encodeURIComponent(encodeURIComponent(userName)) + "&userImg=" + encodeURI(userImg) + "&"
			 + new Date().getTime();
			
			 var title = "语音盒呼叫";
			 window.parent.showMaxJqueryWindow(title,url,590,200);
		 }
		  
//		function isphone(a){
//			var pattern=/^((0?1[358]\d{9})|((0(10|2[1-3]|[3-9]\d{2}))?[1-9]\d{6,7}))$/;
//			return pattern.test(a);
//		}
	  //发送短信
	  function afterSendSMS(defaultTip){
			  //号码
				var otherDialNum = $('#otherDialNum').val() == defaultTip ? '' : $('#otherDialNum').val();
				var phoneNums = $('#phoneNums').val() ==defaultTip ?'': $('#phoneNums').val();
				if(otherDialNum==null ||otherDialNum==""){
					alert("请先输入手机号码！");
					return false;
				}else{
					otherDialNum = otherDialNum.substring(0,otherDialNum.length-1);
					phoneNums = phoneNums.substring(0, phoneNums.length-1);
					var phones = otherDialNum.split(",");
					
					for(var i=0;i<phones.length;i++){
					var begin=phones[i].lastIndexOf("(");
					
					var end=phones[i].length;
					
					  var phone=phones[i].substring(begin+1,end-1);
					 
						if(!isMobile(phone)){
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
						otherDialNum : phoneNums,//otherDialNum,
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
			$("#name").val("");
			$("#duty").combobox("setValue","");
			$("#gridName").val("");
			$("#gridId").val("");
		}
	 //短信推送
	 function smsPush(){
		// document.getElementById('bohao').style.display = "none"; 
		 $("#otherDialNum").val("");
		 $("#content").val("");
		 $("[name='checkBox']").attr("checked", false);
		 document.getElementById('sendMessages').style.display = "";
		// document.getElementByName("checkBox").checked=false;
	 }
	 
	//语音盒
	function callPhone(phoneNum,reporterName){
		if (phoneNum ==null || phoneNum==""  || reporterName==""){
			alert("反馈人员电话不存在！");
			return;
		}else {
			phoneNum = phoneNum.replace(/[ \-]/g,"");//去除电话号码中包含的"-"和空格
			var pattern = /^\d*$/;
			if(phoneNum!="" && pattern.test(phoneNum)){//排除只包含空格和"-"的电话号码
				//parent.window.callSpeech(phoneNum, reporterName, "${rc.getContextPath()}/theme/scim/gis/query/img/untitled.png");
				callSpeech(phoneNum, reporterName, "${uiDomain!''}/images/map/gisv0/map_config/unselected/situation/untitled.png");
				//window.makeCall(phoneNum, reporterName, null);
			}else{
				alert("反馈人员电话："+phoneNum+"\n\n反馈人员电话格式有误！");
				return;
			}
		}
	}
	var wingrid="";
	function callSpeech(phoneNum, reporterName, pictureUrl){
			//var url = "${rc.getContextPath()}/zzgl/event/requiredEvent/emp.jhtml?bCall=" + phoneNum + "&userName="
			var url = "${rc.getContextPath()}/zhsq/map/arcgis/voiceInterface/go.jhtml?bCall=" + phoneNum + "&userName="
					+ encodeURIComponent(encodeURIComponent(reporterName)) + "&userImg=" + encodeURI(pictureUrl);
			
			var title = "语音盒呼叫";
			window.parent.showMaxJqueryWindow(title,url,590,200);
			
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
	
	function showPhone1(id){
		var aa=document.getElementById(id);
		if($(aa).attr("checked")){
			$(aa).attr("checked",'false');
		}else{
			$(aa).attr("checked",'true');
		}
		showPhone(id);
	}
	
	function showPhone(id){
	   var aa=document.getElementById(id);
	   var nameAndPhone=$(aa).val();
	   var phones = nameAndPhone.split(",");
	   if($(aa).attr("checked")){
				if(phones[1]!=null&&phones[1]!=''){
			       sendMessage(phones[1],phones[0]);
				}
	   }else{
	      var str=phones[0]+"("+phones[1]+")";
	      var changgeStr=str+",";
	      
	      var totalStr=$("#otherDialNum").val();
	      var changeTotalStr=totalStr;
	      
	      if(changeTotalStr.indexOf(changgeStr)!=-1){
	         changeTotalStr=changeTotalStr.replace(changgeStr,"");
	         $("#otherDialNum").val(changeTotalStr);
	      }
	      if(changeTotalStr.indexOf(str)!=-1){
	         changeTotalStr=changeTotalStr.replace(str,"");
	         $("#otherDialNum").val(changeTotalStr);
	      }
	   }
	}
	
function delAll(){
    $("#addAll").show();
     $("#clearAll").hide();
   $("[name='checkBox']").attr("checked", false);
 // document.getElementByName("checkBox").value=false;
    $("#otherDialNum").val("");
}	
</script>
</body>
</html>
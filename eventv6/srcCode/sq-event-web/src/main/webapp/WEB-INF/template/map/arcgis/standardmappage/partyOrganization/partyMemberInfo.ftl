<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>党员列表</title>
<#if Session.uiDomain?exists>
	<#assign uiDomain = Session.uiDomain>
</#if>
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-last/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-last/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/main_new.css" />
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-last/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-last/locale/easyui-lang-zh_CN.js"></script>
<link href="${uiDomain!''}/css/normal.css" rel="stylesheet" type="text/css" />
<style type="text/css">
/*---------------------------------人员组织信息-----------------------------------------*/
.list {
    background: none repeat scroll 0 0 #c5d0dc;
    font-size: 12px;
}

.list td {
    background: none repeat scroll 0 0 #fff;
    border: 0 none;
    padding: 10px 0;
    text-align: center;
    vertical-align: middle;
    width: auto;
}

.list td.header {
    background: none repeat scroll 0 0 #f9f9f9;
    color: #5e5e5e;
    font-weight: bold;
}
</style>
</head>
<body>
    <input type="hidden" id="partyGroupId" value="${partyGroupId?c}" />	
    <input type="hidden" id="pageSize" value="10" />
	<div style="height:294px;overflow-y:auto;">
		<table id="listtable" width="100%" border="0" cellspacing="1" cellpadding="0" class="list" ></table>
	</div>
	
 	<div class="NorPage" style="width:868px;">
    	<ul>
        	<li class="PreBtn"><a href="javascript:change('1');"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/pre.png" /></a></li>
        	<li style="width:120px;">共查询到 <span id="records">0</span> 条记录</li>
        	<li class="yema">共 <span id="pagination-num">0</span>/<span id="pageCount">0</span> 页 转至 </li>
            <li class="PageInp"><input id="inputNum" name="inputNum" type="text" /></li>
            <li class="PageBtn"><input type="button" value="确定" onclick="pageSubmit()"/></li>
        	<li class="NextBtn"><a href="javascript:change('2');"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/next.png" /></a></li>
        </ul>
    </div>
</body>

<script type="text/javascript">

    $(document).ready(function(){
		page(1);
	//	$("#gridTbodyId").height(document.body.clientHeight-56);
	});
	
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
	    page(pagenum);
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
	
	function page(pageNo){
		var partyGroupId = "${partyGroupId?c}";
		var pageSize = $("#pageSize").val();
		/*
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
		}*/
		
		$.blockUI({message: "加载中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'42%',
    		background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
		var postData = 'page='+pageNo+'&rows='+pageSize+'&partyGroupId='+partyGroupId;
		$.ajax({
			type: "POST",
			url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofthing/listPartyMemberData.json?t='+Math.random(),
			data: postData,
			dataType:"json",
			success: function(data){
				/*$.unblockUI();
				$('#curPage').text(pageNo);
				var totalPage = Math.floor(data.totalCount/pageSize);
				if(data.totalCount%pageSize>0) totalPage+=1;
				$('#totalPage').text(totalPage);
				var list=data.list;
				var tableBody="";*/
				console.log(data);
				$.unblockUI();
				//设置页面页数
				//$('#curPage').text(pageNo);
				$('#records').text(data.totalCount);
				var totalPage = Math.floor(data.totalCount/pageSize);
				if(data.totalCount%pageSize>0) totalPage+=1;
				$('#pageCount').text(totalPage);
				var list=data.list;
				var tableBody="";
				
				if(list && list.length>0) {
					$('#pagination-num').text(pageNo);				
				    tableBody+='<tr>';
				    tableBody+='<td class="header">姓名</td><td class="header">性别</td><td class="header">所属党支部</td><td class="header">入党时间</td>';
					tableBody+='<td class="header">隶属关系</td><td class="header">现居住地址</td><td class="header">移动电话</td>';
					tableBody+='</tr>';
					for(var i=0;i<list.length;i++){
						var val=list[i];						
						tableBody+='<tr>';						
						tableBody+='<td  align="center"><a href="###" onclick="selected('+val.ciRsId+')">'+(val.name==null?'':val.name)+'</a></td>';
						tableBody+='<td  align="center">'+(val.genderCN==null?'':val.genderCN)+'</td>';					
						tableBody+='<td  align="center">'+(val.partyGroupName==null?'':val.partyGroupName)+'</td>';							
						tableBody+='<td  align="center">'+(val.partyArmDate==null?'':val.partyArmDate)+'</td>';
						tableBody+='<td  align="center">'+(val.suborRela==null?'':val.suborRela)+'</td>';
						tableBody+='<td  align="center">'+(val.residenceAddr==null?'':val.residenceAddr)+'</td>';
						//tableBody+='<td  align="center">'+(val.organization==null?'':val.organization)+'</td>';
						tableBody+='<td  align="center">'+(val.phone==null?'':val.phone)+'</td>';
						tableBody+='</tr>';
					}
				} else {
					//tableBody+='<tr style="height: 85px"><td align="center" style="color:red;">未查到相关数据！</td></tr>';
					tableBody+='<img width="882" height="330" src="${rc.getContextPath()}/css/images/nodata.png" title="暂无数据"/>';
				}
				$("#listtable").html(tableBody);
			},
			error:function(data){
				$.unblockUI();
				var tableBody='<tr style="height: 85px"><td align="center" style="color:red;">数据读取错误！</td></tr>';
				$("#listtable").html(tableBody);
			}
		});
	}
	

	//查看所属的党员信息
	function selected(ciRsId){
		<#if isCross??>
			var url = '${ZHSQ_EVENT_URL}/zhsq/map/arcgis/arcgisdataofthing/partyMemberInfoDetail.jhtml?ciRsId='+ciRsId;
		 	var title = "党员信息";
		 	window.parent.showMaxJqueryWindow(title,url);
		<#else>
			var url = '${ZHSQ_EVENT_URL}/zhsq/map/arcgis/arcgisdataofthing/partyMemberInfoDetail.jhtml?ciRsId='+ciRsId;
		 	var title = "党员信息";
		 	window.parent.showMaxJqueryWindow(title,url);
		</#if>
	 	 
	}
	
	 
</script>
</html>
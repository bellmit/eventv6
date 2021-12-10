
<div id="jqueryToolbar" class="MainContent">
	<div class="ConSearch">
        <div class="fl">
        	<ul>
               <li>日期:</li>
                <li>
                <input type="text" class="inp1 Wdate timeClass" id="syear"  value="${currentYear}-${currentMonth}"
					onClick="WdatePicker({readOnly:true,isShowOK:false,isShowClear:false,
					 isShowToday:false,dateFmt:'yyyy-M'});"  style='width:100px'>
				</li>
            </ul>
        </div>
        <div class="btns">
        	<ul>            	
            	<li><a href="#" class="chaxun" title="查询按钮" onclick="searchData(1)">查询</a></li>
            	<li><a href="#" class="chongzhi" title="重置查询条件" onclick="resetCondition()">重置</a></li>
            </ul>
        </div>‍
        
	</div>
	<div class="h_10 clear"></div>
	<div class="ToolBar" id="ToolBar">
    	<div class="blind"></div>
    	<script type="text/javascript">
			function DivHide(){
				$(".blind").slideUp();//窗帘效果展开
			}
			function DivShow(msg){
				$(".blind").html(msg);
				$(".blind").slideDown();//窗帘效果展开
				setTimeout("this.DivHide()",800);
			}
		</script>	
        <div class="tool fr">
    			<a href="#" class="NorToolBtn DelBtn" onclick="del();">删除</a>
    			<a href="#" class="NorToolBtn linkBtn" onclick="detail();">详情</a>
				<a href="#" class="NorToolBtn EditBtn" onclick="edit();">编辑</a>
				<a href="#" class="NorToolBtn AddBtn" onclick="add(this);">新增</a>
        </div>
    </div>
	
</div>

<script type="text/javascript">
	var enter={add:0,edit:0,detail:0,del:0};
	
	function add() {
		if(enter.add){return;}
		enter.add = 1;
		var opt = {
	    	title: "新增${dictName}",
	    	targetUrl: '${rc.getContextPath()}/zhsq/szzg/zgStatisticsController/toAdd.jhtml?stype=${stype}&operation=add',
	    	width: 900,
			onBeforeClose:function(){
				enter.add = 0;
			}
	    };
		openJqueryWindowByParams(opt);
	}
	var current_date ="${currentYear}-${currentMonth}";
	function detail() {
		
		var rows = $('#list').treegrid('getData');
		if(rows.length == 0) {
			$.messager.alert('提示','该年月无数据展示!','warning');
			return;
		}
		if(enter.detail){return;}
		enter.detail = 1;
		var syear = current_date.split('-') ;
		var opt = {
	    	title: "${dictName}详情",
	    	targetUrl: '${rc.getContextPath()}/zhsq/szzg/zgStatisticsController/detail.jhtml?stype=${stype}&syear='+syear[0]+'&smonth='+syear[1],
	    	width: 800,
			onBeforeClose:function(){
				enter.detail = 0;
			}
	    };
		openJqueryWindowByParams(opt);
	}
	function edit() {
		
		var rows = $('#list').treegrid('getData');
		if(rows.length == 0) {
			$.messager.alert('提示','该年月无数据编辑!','warning');
			return;
		}
		if(enter.edit){return;}
		enter.edit = 1;
		var syear = current_date.split('-') ;
		var opt = {
	    	title: "编辑${dictName}",
	    	targetUrl: '${rc.getContextPath()}/zhsq/szzg/zgStatisticsController/toAdd.jhtml?stype=${stype}&operation=edit&syear='+syear[0]+'&smonth='+syear[1],
	    	width: 900,
			onBeforeClose:function(){
				enter.edit = 0;
			}
	    };
		openJqueryWindowByParams(opt);
	}
	
	function del() {
		var rows = $('#list').treegrid('getData');
		if(rows.length == 0) {
			$.messager.alert('提示','该年月无数据删除','warning');
			return;
		}
		var syear = current_date.split('-') ;
		$.messager.confirm('提示', '您确定删除'+syear[0]+'年'+syear[1]+'月记录吗？', function(r){
			if (r){
				modleopen();
				var params ={stype:'${stype}'};
				var syear = current_date.split('-') ;
				params['syear']= syear[0];
				params['smonth']= syear[1];
				$.ajax({
					type: "POST",
					url: '${rc.getContextPath()}/zhsq/szzg/zgStatisticsController/delete.jhtml',
					data: params,
					dataType:"json",
					success: function(data){
						modleclose();
						DivShow(data.tipMsg);
						searchData();
					},
					error:function(data){
						$.messager.alert('错误','连接超时！','error');
					}
				});
			}
		});
	}
	function searchData() {
		var params ={stype:'${stype}'};
		var syear = document.getElementById('syear').value.split('-') ;
		current_date = document.getElementById('syear').value;
		params['syear']= syear[0];
		params['smonth']= syear[1];
		list.treegrid('clearSelections');
		list.treegrid('options').queryParams=params;
		list.treegrid('reload');
	}
	function resetCondition() {
		document.getElementById('syear').value = "${currentYear}-${currentMonth}";
	}
</script>
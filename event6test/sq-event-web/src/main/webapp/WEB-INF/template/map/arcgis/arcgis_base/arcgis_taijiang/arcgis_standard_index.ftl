<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>地图首页</title>
	<link href="${rc.getContextPath()}/js/ligerUI/skins/Aqua/css/ligerui-dialog.css" rel="stylesheet" type="text/css" />
    <link href="${rc.getContextPath()}/js/ligerUI/skins/ligerui-icons.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
	<script type="text/javascript" src="${uiDomain!''}/images/map/gisv0/map_taijiang/js/GisDemo_taijiang.js"></script>
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-last/themes/default/easyui.css">
	<link href="${uiDomain!''}/images/map/gisv0/map_taijiang/css/frame.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/fbsource/jquery.fancybox.css?v=2.1.5" media="screen" />
	<#include "/map/arcgis/arcgis_base/arcgis_common.ftl" />
	<script src="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/arcgis_base/arcgis_demo.js"></script>
    <#include "/component/ImageView.ftl" />
	<script type="text/javascript">
	//图片查看器回调
	function ffcs_viewImg(fieldId){
		var sourceId = fieldId + "_Div";
		var imgDiv = $("#"+sourceId+"");
		imgDiv.find('.fancybox-button').eq(0).click();
	}
	</script>

</head>
<body style="width:100%;height:100%;border:none;" >
 	<div id="firstImgs" style="display: none;"></div>
	<div>
		<div class="page-container" id="map0" style="position: absolute; width:100%; height:100%; z-index: 1;">
		</div>
		<div class="page-container" id="map1" style="position: absolute; width:100%; height:100%; z-index: 2;display:none;">
		</div>
		<div class="page-container" id="map2" style="position: absolute; width:100%; height:100%; z-index: 3;display:none;">
		</div>
	</div>
	<div id="jsSlider"></div>

<div class="MapSize">
    <div class="MS MS_1" onclick="setLevel(13);"><a href="javascript:void(0);">片区</a></div>
	<div class="MS MS_2" onclick="setLevel(12);"><a href="javascript:void(0);">网格</a></div>
	<div class="MS MS_3" onclick="setLevel(11);"><a href="javascript:void(0);">社区</a></div>
	<div class="MS MS_4" onclick="setLevel(10);"><a href="javascript:void(0);">街道</a></div>
</div>
	
<div class="MapTools" style="top:470px;">
	<div class="TwoThree"><a href="#"></a>
       	<div id="mapStyleDiv2" class="MapStyle2" style="display:none;">
           <span class="current">矢量图</span>
           <span>影像图</span>
       </div>
    </div>
	<div class="MapClear" onclick="clearMyLayer();"><a href="javascript:void(0);"></a></div>
	<div class="IconList" onclick=""><a href="javascript:void(0);"></a>
        <div class="SecMenu" style="display:none;"><img src="${uiDomain!''}/images/map/gisv0/map_taijiang/images/tj_wg_78.png" /></div>
	</div>
	<div class="MapPointEvent" onclick="showNoticewin();"><a href="javascript:void(0);"></a></div>
</div>

<!--日常管理-->
<div class="DayWork">
	<div class="pic dayWorkPic"><a href="javascript:void(0);"></a></div>
    <div class="SecMenu" style="display:none;">
       <table class="NorTable" id="menuListTab"> 
       </table>
	</div>
</div>

<!--分类管理-->
<div class="zttc">
	<div class="pic zttcPic"><a href="javascript:void(0);"></a></div>
    <div class="SecMenu" style="display:none;">
    	<table class="NorTable">
          <tr>
            <td onclick="showSegmentGrid(this,'01');"><a href="javascript:void(0);" >城市综合体</a></td>
            <td onclick="showMessage(this,2);"><a href="javascript:void(0);" >场所</a></td>
            <td onclick="showMessage(this,3);"><a href="javascript:void(0);" >企业</a></td>
          </tr>
          <tr>
            <td onclick="selectBuildingType(this);"><a href="javascript:void(0);" >楼宇</a></td>
            <td onclick="selectKeyEventType(this);"><a href="javascript:void(0);" >重点事件</a></td>
            <td onclick="selectPersonType(this);"><a href="javascript:void(0);" >重点人员</a></td>
          </tr>
          <tr>
            <td onclick="showMessage(this,7);"><a href="javascript:void(0);" >全球眼</a></td>
            <td onclick="showSegmentGrid(this,'02');"><a href="javascript:void(0);" >木屋片区</a></td>
            <td onclick="showMessage(this,8);"><a href="javascript:void(0);" >消防栓</a></td>
          </tr>
          <tr>
            <td onclick="showMessage(this,9);"><a href="javascript:void(0);" >网格员</a></td>
            <td onclick="showHiddenTrouble(this);"><a href="javascript:void(0);" >隐患</a></td>
            <td ><a href="javascript:void(0);"></a></td>
          </tr>
        </table>
		<div class="con">
		   <div  id="message" style="display:none">
		     <iframe id="get_grid_name_frme" name="get_grid_name_frme" width="300px"  src="" marginwidth=0 marginheight=0 scrolling="no" frameborder=0></iframe>
           </div>
           <div class="list1" id="personType"  style="display:none;height:180px;overflow:auto;">
            	<ul>
               	   <li><a href="javascript:showPerson('${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofpoplocal/gisPop.jhtml?gridId=${gridId}&type=drugs');">吸毒人员</a></li>
                   <li class="back"><a href="javascript:showPerson('${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofpoplocal/gisPop.jhtml?gridId=${gridId}&type=rectify');">矫正人员</a></li>
                   <li><a href="javascript:showPerson('${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofpoplocal/gisPop.jhtml?gridId=${gridId}&type=petition');">上访人员</a></li>
                   <li class="back"><a href="javascript:showPerson('${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofpoplocal/gisPop.jhtml?gridId=${gridId}&type=camps');">刑事解教人员</a></li>
                   <li><a href="javascript:showPerson('${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofpoplocal/gisPop.jhtml?gridId=${gridId}&type=dangerous');">危险品从业人员</a></li>
                   <li class="back"><a href="javascript:showPerson('${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofpoplocal/gisPop.jhtml?gridId=${gridId}&type=neuropathy');">重精神病人员</a></li>
                   <li><a href="javascript:showPerson('${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofpoplocal/gisPop.jhtml?gridId=${gridId}&type=heresy');">邪教人员</a></li>
                   <li class="back"><a href="javascript:showPerson('${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofpoplocal/gisPop.jhtml?gridId=${gridId}&type=disability');">残障人员</a></li>
                   <li><a href="javascript:showPerson('${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofpoplocal/gisPop.jhtml?gridId=${gridId}&type=welfare');">低保人员</a></li>
                   <li class="back"><a href="javascript:showPerson('${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofpoplocal/gisPop.jhtml?gridId=${gridId}&type=homeAge');">居家养老人员</a></li>
                   <li><a href="javascript:showPerson('${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofpoplocal/gisPop.jhtml?gridId=${gridId}&type=retire');">退休人员</a></li>
                   <li class="back"><a href="javascript:showPerson('${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofpoplocal/gisPop.jhtml?gridId=${gridId}&type=unemployment');">失业人员</a></li>
                   <li><a href="javascript:showPerson('${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofpoplocal/gisPop.jhtml?gridId=${gridId}&type=military');">服兵役人员</a></li>
                </ul>
			</div>
			
			<div class="list1" id="buildingType"  style="display:none;height:180px;overflow:auto;">
            	<ul>
            	    <li><a href="javascript:showBuilding('001');">物业管理住宅</a></li>
                	<li class="back"><a href="javascript:showBuilding('002');">社区管理住宅</a></li>
                	<li><a href="javascript:showBuilding('003');">单位自管住宅</a></li>
                	<li class="back"><a href="javascript:showBuilding('004');">宿舍</a></li>
                	<li><a href="javascript:showBuilding('005');">民房</a></li>
                	<li class="back"><a href="javascript:showBuilding('006');">写字楼</a></li>
                	<li><a href="javascript:showBuilding('007');">单位楼</a></li>
                	<li class="back"><a href="javascript:showBuilding('008');">城市综合体</a></li>
                	
                	
                	<li><a href="javascript:showBuilding('009');">公园广场</a></li>
                	<li class="back"><a href="javascript:showBuilding('010');">旅馆</a></li>
                	<li><a href="javascript:showBuilding('011');">影剧院</a></li>
                	<li class="back"><a href="javascript:showBuilding('012');">体育馆</a></li>
                	<li><a href="javascript:showBuilding('013');">医院</a></li>
                	<li class="back"><a href="javascript:showBuilding('014');">银行</a></li>
                	<li><a href="javascript:showBuilding('015');">寺庙</a></li>
                	<li class="back"><a href="javascript:showBuilding('016');">工地</a></li>
                	<li><a href="javascript:showBuilding('017');">仓库</a></li>
                	<li class="back"><a href="javascript:showBuilding('018');">商住两用</a></li>
                	<li><a href="javascript:showBuilding('999');">其它</a></li>
                </ul>
			</div>
			
			<div class="list1" id="keyEvent"  style="display:none">
            	<ul>
            	    <li><a href="javascript:showKeyEvent(1);">个人待办事件</a></li>
                	<li class="back"><a href="javascript:showKeyEvent(2);">辖区待办事件</a></li>
                </ul>
			</div>
        </div>
    </div>
</div>


<!--应急指挥-->
<div class="yjzh">
	<div class="pic yjzhPic"><a href="javascript:void(0);"></a></div>
    <div class="SecMenu" style="display:none;">
    	<table class="NorTable">
          <tr>
          <td onclick="showyjzh('综治维稳','0214');"><a href="javascript:void(0);">群体事件</a></td>
          <td onclick="showyjzh('农林水','0802');"><a href="javascript:void(0);">火灾</a></td>
          <td onclick="showyjzh('安全工作','0601');"><a href="javascript:void(0);">安全生产</a></td>
          </tr>
          <tr>
          <td onclick="showyjzh('安全工作','0605');"><a href="javascript:void(0);">交通事故</a></td>
            <td onclick="showyjzh('农林水','0804');"><a href="javascript:void(0);">自然灾害</a></td>
            <td><a href="javascript:sendNote();">短信群发</a></td>
          </tr>
          <tr>
          <!--<td><a href="javascript:showyjya('${rc.getContextPath()}/zzgl/emergencyResponse/index.jhtml')">应急预案</a></td>-->
          <td><a href="javascript:showMenu(267851)">应急预案</a></td>
          </tr>
        </table>
      <div class="con">
		   <div  id="trafficmessage" style="display:none">
		     <iframe id="get_name_frme" width="300px"  src="" marginwidth=0 marginheight=0 scrolling="no" frameborder=0></iframe>
           </div>
           
			<div class="list3" id="trafficEvent"  style="display:none">
            	<ul>
            	    <li><a href="javascript:showKeyEvent(1);">交通事故1</a></li>
                	<li class="back"><a href="javascript:showKeyEvent(2);">交通事故2</a></li>
                </ul>
			</div>
     </div>
	</div>
</div>
<!--报表
<div class="bb" onclick="showMenu('250972')"></div>-->
<!--消防管理-->
<a href="http://172.17.0.177:8080/xfsystem" target="_blank"><div class="bb" onclick="">
</div></a>

<!--各图层图标说明-->
<div class="IconNote" style="display:none">
	<div class="con">
    	<div class="icon fl">图标说明</div>
    	<div id="showIcon" class="fl">
    	    <!-- 重点场所 -->
        	<div class="list" name="2" style="display:none">
            	<ul>
                	<li><img src="${uiDomain!''}/images/map/gisv0/map_config/unselected/region_keyPlace.png" />重点场所</li>
                	<li><img src="${uiDomain!''}/images/map/gisv0/map_config/unselected/region_keyPlace.png" />非重点场所</li>
                	<div class="clear"></div>
                </ul>
            </div>
            <!-- 企业 -->
            <div class="list" name="3" style="display:none">
            	<ul>
                	<li><img src="${uiDomain!''}/images/map/gisv0/map_config/unselected/building_dormitory.png"/>企业</li>
                	<div class="clear"></div>
                </ul>
            </div>
            <!--重点事件 -->
            <div class="list" name="4" style="display:none">
            	<ul>
                	<li><img src="${uiDomain!''}/images/map/gisv0/map_config/unselected/event/event_green.gif"/>一般事件</li>
                	<li><img src="${uiDomain!''}/images/map/gisv0/map_config/unselected/event/event_red.gif"/>紧急事件</li>
                	<li><img src="${uiDomain!''}/images/map/gisv0/map_config/unselected/event/event_green.gif"/>处理时限正常</li>
                	<li><img src="${uiDomain!''}/images/map/gisv0/map_config/unselected/event/event_green.gif"/>处理时限将到期</li>
                	<li><img src="${uiDomain!''}/images/map/gisv0/map_config/unselected/event/event_red.gif"/>处理时限已到期</li>
                	<div class="clear"></div>
                </ul>
            </div>
            <!--重点人员 -->
            <div class="list" name="5" style="display:none">
            	<ul style="display:block; height:60px; width:840px;">
            	
                	<li>服务类：</li>
                	<li><img   src="${uiDomain!''}/images/map/gisv0/map_config/unselected/people_physicalDisabilities.png"/>残障人员</li>
                	<li><img   src="${uiDomain!''}/images/map/gisv0/map_config/unselected/people_lowObject.png"/>低保人员</li>
                	<li><img   src="${uiDomain!''}/images/map/gisv0/map_config/unselected/people_yanglao.png"/>居家养老人员</li>
                	<li><img    src="${uiDomain!''}/images/map/gisv0/map_config/unselected/people_retire.png"/>退休人员</li>
                	<li><img    src="${uiDomain!''}/images/map/gisv0/map_config/unselected/people_partyMember.png"/>党员人员</li>
                	<li>&nbsp;</li>
                	<li><img    src="${uiDomain!''}/images/map/gisv0/map_config/unselected/people_unemployed.png"/>失业人员</li>
                	<li><img    src="${uiDomain!''}/images/map/gisv0/map_config/unselected/people_serveArmy.png"/>服兵役人员</li>
                	<div class="clear"></div>
            	</ul>
            	<ul style="display:block; height:60px; width:840px;">
                	<li>监管类：</li>
                	<li><img  src="${uiDomain!''}/images/map/gisv0/map_config/unselected/people_drugAddict.png"/>吸毒人员</li>
                	<li><img    src="${uiDomain!''}/images/map/gisv0/map_config/unselected/people_rectify.png"/>矫正人员</li>
                	<li><img    src="${uiDomain!''}/images/map/gisv0/map_config/unselected/people_petitioner.png"/>上访人员</li>
                	<li><img    src="${uiDomain!''}/images/map/gisv0/map_config/unselected/people_RehabilitationXieJiao.png"/>刑释解教人员</li>
                	<li><img    src="${uiDomain!''}/images/map/gisv0/map_config/unselected/people_dangerousGoods.png"/>危险品从业人</li>
                	<li>&nbsp;</li>
                	<li><img    src="${uiDomain!''}/images/map/gisv0/map_config/unselected/people_psychosis.png"/>重精神病人员</li>
                	<li><img    src="${uiDomain!''}/images/map/gisv0/map_config/unselected/people_heresy.png"/>邪教人员</li>
                	<div class="clear"></div>
                </ul>
            </div>
            <!--全球眼 -->
            <div class="list" name="7" style="display:none">
            	<ul>
                	<li><img src="${uiDomain!''}/images/map/gisv0/map_config/unselected/situation_globalEyes.png"/>全球眼</li>
                	<div class="clear"></div>
                </ul>
            </div>
             <!--消防栓 -->
            <div class="list" name="8" style="display:none">
            	<ul>
                	<li><img src="${uiDomain!''}/images/map/gisv0/map_config/unselected/things_fireHydrant.png"/>消防栓 </li>
                	<div class="clear"></div>
                </ul>
            </div>
              <!--网格员-->
            <div class="list" name="9" style="display:none">
            	<ul>
                	<li><img src="${uiDomain!''}/images/map/gisv0/map_config/unselected/situation_gridAdmin.png"/>网格员</li>
                	<div class="clear"></div>
                </ul>
            </div>
            <div class="clear"></div>
        </div><!-- end showIcon -->
    	<div class="fr"><a href="javascript:closeIcon();"><img src="${uiDomain!''}/images/map/gisv0/map_taijiang/images/tj_wg_71.png" /></a></div>
    	<div class="clear"></div>
    </div>
</div>

<!--
	<div id="switchMap">
		<span onclick="switchMap('img')" id="imgMap" title="显示卫星地图">
			<span id="imgInner">
				<span id="inner"></span>
				<span id="text">卫星</span>
			</span>
		</span>
		<span onclick="switchMap('vec')" id="vecMap" title="显示普通地图">
			<span id="vecInner">
				<span id="inner"></span>
				<span id="text">地图</span>
			</span>
		</span>
	</div> 
 -->
	
<!-- baseDataTabs end -->

		<!-- 专题地图 -->
  		<form name="gridForm" method="post" action="${rc.getContextPath()}/admin/grid.shtml" target="_self">
  			<input type="hidden" name="gridId" id="gridId" value="${gridId?c}" />
  			<input type="hidden" name="gridCode" id="gridCode" value="${gridCode}" />
  			<input type="hidden" name="orgCode" id="orgCode" value="${orgCode}" />
  			<input type="hidden" name="gridName" id="gridName" value="${gridName}" />
  			<input type="hidden" name="gridLevel" id="gridLevel" value="${gridLevel}" />
  			<input type="hidden" name="SQ_ZZGRID_URL" id="SQ_ZZGRID_URL" value="${SQ_ZZGRID_URL}" />
  			<input type="hidden" name="POPULATION_URL" id="POPULATION_URL" value="${POPULATION_URL}" />
  		</form> 
  		<!-----------------------------------设置------------------------------------->
    <div class="NorMapOpenDiv2 zhoubianWindow hide dest" style="bottom:30px; left:60px;">
		<div class="box" style="width:376px;height:404px;">
	    	<div class="title"><span class="fr close" onclick="javascirpt:closeZhoubian()"></span>选择您想要查看的内容</div>
	        <iframe id="zhoubianConfig" name="zhoubianConfig" width="100%" height="100%" src="" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>
	    </div>
	    <!--<div class="shadow"></div>-->
	</div>
<div class="MapBar">
    <div class="location">
    	<ul>
    		<li class="pic fl"></li>
    		<li class="info fl" id="selectGridName"><a href="javascript:void(0);" id="changeGridName">${gridName}</a></li>
    		
    		<!--
        	<li><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/local.png" /></li>
            <li><a href="javascript:void(0);" id="changeGridName">${gridName}</a></li>
        	<li><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/xiala.png" /></li>-->
        </ul>
        <!--
        <div class="MapLevel fl">
        	<ul>
            	<li><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/cengji.png" /></li>
                <li><a href="javascript:void(0);">层&nbsp;级</a></li>
            	<li><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/xiala.png" /></li>
            </ul>
        </div>-->
    </div>
    <div class="SelectTree AlphaTree dest" style="overflow:auto;">
    	<iframe id="gridTree" name="gridTree" width="100%" height="99%" src="${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapTree.jhtml" marginwidth=0 marginheight=0 scrolling="yes" frameborder=0></iframe>
    </div>
    <div class="ztIconZhouBian AlphaBackZhouBian titlezhoubian" style="display:none;">
		<div class="title"><span class="fr" onclick="zhoubianListHide()"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/closex.png" /></span><span id="searchBtnIdZhouBian" class="fr" style="" onclick="ShowSearchBtnZhouBian()"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/search.png" /></span><div id="titlePathZhouBian" name="titlePathZhouBian">周边资源</div></div>
	</div>	
    <div class="SelectTree2 AlphaBack dest">
    	<ul>
        	<li id = "li2"><span><input name="gridLevelName" type="checkbox" value="2" onclick="getArcgisDataOfGridsByLevel(2);" id="gridLevelName2" /></span>市</li>
        	<li id = "li3"><span><input name="gridLevelName" type="checkbox" value="3" onclick="getArcgisDataOfGridsByLevel(3);" id="gridLevelName3" /></span>县（区）</li>
        	<li id = "li4"><span><input name="gridLevelName" type="checkbox" value="4" onclick="getArcgisDataOfGridsByLevel(4);" id="gridLevelName4" /></span>乡镇（街道）</li>
        	<li id = "li5"><span><input name="gridLevelName" type="checkbox" value="5" onclick="getArcgisDataOfGridsByLevel(5);" id="gridLevelName5" /></span>村（社区）</li>
        	<li id = "li6"><span><input name="gridLevelName" type="checkbox" value="6" onclick="getArcgisDataOfGridsByLevel(6);" id="gridLevelName6" /></span>网格</li>
        	<li id = "liBuild0"><span><input name="buildName" type="checkbox" value="0" onclick="getArcgisDataOfBuildsByCheck();" id="buildName0" /></span>楼宇（民房）</li>
        </ul>
    </div>
</div>


<!-- 重大事件 -->
<!--
<div id="importantEvent" class="easyui-dialog"  title="重大紧急事件"   closed=""  style="width:600px;height:260px;padding:0px">
	<iframe id="importantEventFrame" width="100%" height="100%" src="" marginwidth=0 marginheight=0 scrolling="no" frameborder=0></iframe>
</div>-->

<div class="NorNoticeWindow AboutMetter" id="noticewin" style="width:450px; height:250px;display:none;z-index:10;" >
	<div class="title"><span class="fr"><a href="#"><img id="eventClose" src="${uiDomain!''}/images/map/gisv0/map_taijiang/images/mnw_03.png"></a></span>重大紧急事件</div>
    <div class="con" style="padding:1px">
    	<iframe id="importantEventFrame" width="100%" height="224" src="" marginwidth=0 marginheight=0 scrolling="yes" frameborder=0 overflow="auto"></iframe>
    </div>
</div>
<div class="NorListZhouBian AlphaBackZhouBian zhoubianList">
		<iframe id="zhoubian_list_frme" name="zhoubian_list_frme" width="100%" height="450px" src="" marginwidth=0 marginheight=0 scrolling="no" frameborder=0></iframe>
	</div>
</body>
<script src="${rc.getContextPath()}/js/map/arcgis/arcgis_base/function.js"></script> 
<#include "/component/maxJqueryEasyUIWin.ftl" />
<#include "/component/customEasyWin.ftl" />
	<script  src="${rc.getContextPath()}/js/ligerUI/js/core/base.js" type="text/javascript"></script>
    <script  src="${rc.getContextPath()}/js/ligerUI/js/plugins/ligerDrag.js" type="text/javascript"></script>
    <script  src="${rc.getContextPath()}/js/ligerUI/js/plugins/ligerDialog.js" type="text/javascript"></script>
    <script  src="${rc.getContextPath()}/js/ligerUI/js/plugins/ligerResizable.js" type="text/javascript"></script>
    <script type="text/javascript" src="${rc.getContextPath()}/js/map_gridforliger.js"></script>
    <script type="text/javascript" src="${rc.getContextPath()}/js/zzgl_core.js"></script>
	<script type="text/javascript" src="${rc.getContextPath()}/js/event/event_4_parent.js"></script>
<script type="text/javascript">
var test1="tets";
var openflag = false;
var currentGridLevel;
 $(function(){
 	modleopen();
 	zhoubianListHide();
 	setTimeout(function(){
	 	$("#map0").css("height",$(document).height());
		$("#map0").css("width",$(document).width());
		$("#map1").css("height",$(document).height());
		$("#map1").css("width",$(document).width());
		$("#map2").css("height",$(document).height());
		$("#map2").css("width",$(document).width());
	 	showGridLevel('${gridLevel}');
	 	eventInit("${rc.getContextPath()}", "${SQ_ZZGRID_URL}");
	 	var level = (parseInt($("#gridLevel").val()) < 6) ? parseInt($("#gridLevel").val())+1 : parseInt($("#gridLevel").val());
	 	document.getElementById("gridLevelName"+level).checked = true;
	 	currentGridLevel=level;
	 	getArcgisInfo();
	 	
	 	//changeCheckedAndStatus($("#gridLevel").val(),level);
	 	//getArcgisDataOfGrids($("#gridId").val(),$("#gridCode").val(),currentArcgisConfigInfo.mapType ,level);
	 	
	 	$("#jsSlider").css("top", "10px") // 缩放条位置
	 	getMenuList();
	 	menu();  // 右侧菜单隐藏显示
	 	
	 	showPointEvent();
	   	
	   	$("#eventClose").click(function() {
	   		$("#noticewin").hide();
	   	});
   	},100);
   	window.onresize=function(){
	  	$("#map0").css("height",$(document).height());
		$("#map0").css("width",$(document).width());
		$("#map1").css("height",$(document).height());
		$("#map1").css("width",$(document).width());
		$("#map2").css("height",$(document).height());
		$("#map2").css("width",$(document).width());
	 }
 });

function showPointEvent(){
	document.getElementById("importantEventFrame").src = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/toImportantEvent.jhtml?statusName=innerPlatform&gridId=${gridId}";
}

function showNoticewin() {
	openflag = true;
	$("#noticewin").show();
}

 function showGridLevel(level){
	if (level >= 5) {
		document.getElementById("buildName0").checked=true;
		//getArcgisDataOfBuildsPoints($("#gridId").val(),$("#gridCode").val(),currentArcgisConfigInfo.mapType);
	}else {
		document.getElementById("buildName0").checked=false;
	}	
      if(level>=6){//网格以下
        $(".MS_1").css("display","block");
        $(".MS_2").css("display","block");
        $(".MS_3").css("display","none");
        $(".MS_4").css("display","none");
     }else if(level==5){//社区
        $(".MS_1").css("display","block");
        $(".MS_2").css("display","block");
        $(".MS_3").css("display","block");
        $(".MS_4").css("display","none");
     }else if(level<=4){//街道以上
        $(".MS_1").css("display","block");
        $(".MS_2").css("display","block");
        $(".MS_3").css("display","block");
        $(".MS_4").css("display","block");
     }
 }

function getMenuList(){
    $.ajax({   
		 url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getDisplayMemu.json',
		 type: 'POST',
		 timeout: 3000000, 
		 dataType:"json",
		 error: function(data){  
		   $.messager.alert('友情提示','日常管理列表读取失败,详情请查看后台日志!','warning'); 
		 },   
		 success: function(data){
		    var result=data.result;
	        var val=eval(data.menuList);
	        var num = 0;
	        var tableBody = "<tr>";
             for(var i=0;i<val.length;i++){
             	num++;
             	if(num%3 == 0){
             		tableBody = tableBody+"<td><a href=\"javascript:showMenu('"+val[i].menuId+"');\">"+val[i].menuName+"</a></td>";
             		tableBody = tableBody+"</tr><tr>";
             	}else {
             		tableBody = tableBody+"<td><a href=\"javascript:showMenu('"+val[i].menuId+"');\">"+val[i].menuName+"</a></td>";
             	}
       		 }
	         tableBody+='</tr>';
	         $("#menuListTab").html(tableBody);
		 }
   	  });
}

// 右侧菜单隐藏显示
function menu() {
	$(".IconList").click(function() {
		$(".IconNote").toggle(200);
	});
	
	$(".dayWorkPic").click(function() {
		$(".DayWork").find(".SecMenu").toggle(200);
		$(".zttc").find(".SecMenu").hide(200);
		$(".yjzh").find(".SecMenu").hide(200);
	});
	
	$(".zttcPic").click(function() {
		$(".DayWork").find(".SecMenu").hide(200);
		$(".zttc").find(".SecMenu").toggle(200);
		$(".yjzh").find(".SecMenu").hide(200);
	});
	
	$(".yjzhPic").click(function() {
		$(".DayWork").find(".SecMenu").hide(200);
		$(".zttc").find(".SecMenu").hide(200);
		$(".yjzh").find(".SecMenu").toggle(200);
	});
	
	// 二三维切换
	$(".TwoThree").click(function(){
		$(this).find(".MapStyle2").show(200);
	});
	
	$(".TwoThree").hover(function(){
		$(this).find(".MapStyle2").hide(200);
	});
}

<!---------------分类管理开始----------------->
//设置分类管理列表高度
function setFlglListHeight(){
    var winHeight=$("#map0").height()-$("#message").offset().top;
    $("#get_grid_name_frme").height(winHeight-10); 
}

//设置应急指挥列表高度
function setYjzhListHeight(){
    var winHeight=$("#map0").height()-$("#trafficmessage").offset().top;
    $("#get_name_frme").height(winHeight-10); 
}

//设置人员类型列表高度
function setPersonTypeHeight(){
	var winHeight=$("#map0").height()-$("#personType").offset().top;
    $("#personType").height(winHeight-10);
}

//设置楼宇类型列表高度
function setBuildingTypeHeight(){
	var winHeight=$("#map0").height()-$("#buildingType").offset().top;
    $("#buildingType").height(winHeight-10);
}

//设置重点事件类型列表高度
function setKeyEventHeight(){
	var winHeight=$("#map0").height()-$("#keyEvent").offset().top;
    $("#keyEvent").height(winHeight-10);
}

//片区网格，城市综合体
function showSegmentGrid(o,type){

    $(".IconNote").css("display","block");
    $("#showIcon").find("div").css("display","none");
    showBackGround(o)
    $("#message").css("display","block");
	var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/segmentGrid.jhtml?gridId="+document.getElementById("gridId").value+"&segmentGridType="+type;
	$("#get_grid_name_frme").attr("src",url); 
	
	$("#keyEvent").css("display","none");
    $("#personType").css("display","none");
    $("#buildingType").css("display","none");
    
    setFlglListHeight();
}

//清除图层选中的背景颜色
function showBackGround(o){
    jqFocusRow = o.parentNode;//tr
    if (o != null) {
		$("a", o).addClass("FontRed");
	}
	$(o).siblings("td").find("a").removeClass("FontRed");
	$(jqFocusRow).siblings("tr").find("a").removeClass("FontRed");
}

//显示专题图层上模块信息
function showMessage(o,type){
    showBackGround(o)
    showIcon(type);
    
    $("#message").css("display","block");
    
    if(type==2){//场所
	    var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/place.jhtml?gridId="+document.getElementById("gridId").value;
	    $("#get_grid_name_frme").attr("src",url);
    }else if(type==3){//企业
    	var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/tjCorBase.jhtml?gridId="+document.getElementById("gridId").value;
		$("#get_grid_name_frme").attr("src",url); 
    }else if(type==7){//全球眼
    	var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/globalEyesTaijiang.jhtml?orgCode="+document.getElementById("gridCode").value;
		$("#get_grid_name_frme").attr("src",url);
    }else if(type==8){//消防栓
    	var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofgoods/resource.jhtml?resTypeId=22&gridId="+document.getElementById("gridId").value;
		$("#get_grid_name_frme").attr("src",url); 
    }else if(type==9){//网格员
    	var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/gridAdminTaijiang.jhtml?gridId="+document.getElementById("gridId").value+"&gridCode="+document.getElementById("gridCode").value;
		$("#get_grid_name_frme").attr("src",url); 
    }
    
    $("#keyEvent").css("display","none");
    $("#personType").css("display","none");
    $("#buildingType").css("display","none");
    
    setFlglListHeight();
}

//显示图标说明
function showIcon(type){
    $(".IconNote").css("display","block");
    var aa=$("#showIcon").find(".list[name='"+type+"']").css("display","block");
    $("#showIcon").find(".list[name='"+type+"']").siblings("div").css("display","none");
}

//显示楼宇类型表
function selectBuildingType(o){
    $(".IconNote").css("display","block");
    $("#showIcon").find("div").css("display","none");
    showBackGround(o)
	
    $("#message").css("display","none");
    $("#keyEvent").css("display","none");
    $("#personType").css("display","none");
    $("#buildingType").css("display","block");
    
    setBuildingTypeHeight();
    setFlglListHeight();
}

//显示楼宇信息
function showBuilding(type){
	$("#message").css("display","block");
	$("#buildingType").css("display","none");
	var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/building.jhtml?gridId="+document.getElementById("gridId").value+"&useNature="+type;
	$("#get_grid_name_frme").attr("src",url); 
	
	setFlglListHeight();
}

//显示重点事件类型
function selectKeyEventType(o){
    showBackGround(o)
	
    showIcon('4');
    $("#message").css("display","none");
    $("#keyEvent").css("display","block");
    $("#personType").css("display","none");
    $("#buildingType").css("display","none");
    
    setKeyEventHeight();
    setFlglListHeight();
}

//显示重点事件信息
function showKeyEvent(type){
    $("#message").css("display","block");
    $("#keyEvent").css("display","none");
	if(type==1){//个人待办事件
	    var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofeventlocal/dbEvent.jhtml?gridId="+document.getElementById("gridId").value+"&gridFlag=0";
	}else if(type==2){//辖区待办事件
	    var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofeventlocal/dbEvent.jhtml?gridId="+document.getElementById("gridId").value+"&gridFlag=1";
	}	
	$("#get_grid_name_frme").attr("src",url);
	
	setFlglListHeight();
}

//显示重点人口类型
function selectPersonType(o){
    showBackGround(o)
    showIcon('5');
    $("#message").css("display","none");
    $("#keyEvent").css("display","none");
    $("#personType").css("display","block");
    $("#buildingType").css("display","none");
    
    setPersonTypeHeight();
    setFlglListHeight();
}
//显示重点人口信息
function showPerson(url){
    $("#message").css("display","block");
    $("#personType").css("display","none");
    $("#get_grid_name_frme").attr("src",url); 
    
    setFlglListHeight();
}

//隐患
function showHiddenTrouble(o){
    $(".IconNote").css("display","block");
    $("#showIcon").find("div").css("display","none");
    showBackGround(o)
    $("#message").css("display","block");
	var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/hiddenTrouble.jhtml?gridId="+document.getElementById("gridId").value;
	$("#get_grid_name_frme").attr("src",url); 
	
	$("#keyEvent").css("display","none");
    $("#personType").css("display","none");
    $("#buildingType").css("display","none");
    
    setFlglListHeight(); 
}
<!---------------分类管理结束----------------->

<!---------------应急指挥开始----------------->
//应急指挥五小类调用方法
function showyjzh(bigType,type){
    $("#trafficmessage").css("display","block");
    
    //var url = "${SQ_ZZGRID_URL}/zzgl/map/data/region/yjzhEvent.jhtml?gridId="+document.getElementById("gridId").value+"&bigType="+encodeURIComponent(encodeURIComponent(bigType))+"&type="+encodeURIComponent(encodeURIComponent(type))+"&statusName=ybEvent";
 	var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/standardYjzhEvent.jhtml?gridId="+document.getElementById("gridId").value+"&bigType="+encodeURIComponent(encodeURIComponent(bigType))+"&type="+encodeURIComponent(encodeURIComponent(type))+"&statusName=ybEvent";
 
    $("#get_name_frme").attr("src",url); 
    
    setYjzhListHeight();
}

//群发短信
function sendNote() {
	//var url = "${SQ_ZZGRID_URL}/zzgl/map/zhddData/centerControl/sendNote.jhtml?gridId="+document.getElementById("gridId").value;;
	var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisCenterControl/voiceCall.jhtml?gridId="+document.getElementById("gridId").value;;
	showMaxJqueryWindow("短信群发",url,720,463);
}

//日常工作跳转
function showMenu(menuId){
   window.parent.location.href="${OAUTH_URL}/admin/admin_taijiang_menu?FirstMenuId="+menuId;
}

<!---------------应急指挥结束----------------->

//关闭图标显示
function closeIcon(){
    $(".IconNote").css("display","none");
}

function setLevel(level) {
	currentGridLevel = level-6;
	getArcgisDataOfGrids($("#gridId").val(),$("#gridCode").val(),currentArcgisConfigInfo.mapType ,level-6);
	
	var map = $("#map").ffcsMap.getMap();
	map.setLevel(level);
}

//楼宇
function getArcgisDataOfBuildsByCheck() {
	if(document.getElementById("buildName0").checked == true) {
		if (currentArcgisConfigInfo.mapType == 5) { // 二维地图显示点
			getArcgisDataOfBuildsPoints($("#gridId").val(),$("#gridCode").val(),currentArcgisConfigInfo.mapType);
		} else if (currentArcgisConfigInfo.mapType == 30) { // 三维地图显示轮廓移动上去显示
			getArcgisDataOfBuilds($("#gridId").val(),$("#gridCode").val(),currentArcgisConfigInfo.mapType);
		}
	}else {
		if (currentArcgisConfigInfo.mapType == 5) { // 二维地图显示点
			$("#map").ffcsMap.clear({layerName : "buildLayerPoint"});
		} else if (currentArcgisConfigInfo.mapType == 30) { // 三维地图显示轮廓移动上去显示
			$("#map").ffcsMap.clear({layerName : "buildLayer"});
		}
	}
}

function getArcgisDataOfGridsByLevel(level) {
	if(document.getElementById("gridLevelName"+level).checked == true) {
		for(i=0; i<document.all("gridLevelName").length; i++) {
			if(document.all("gridLevelName")[i].value!=level){
				document.all("gridLevelName")[i].checked = false;
			}
		}
		getArcgisDataOfGrids($("#gridId").val(),$("#gridCode").val(),currentArcgisConfigInfo.mapType ,level)
	}else {
		$("#map").ffcsMap.clear({layerName : "gridLayer"});
	}
}

function getArcgisDataByCurrentSet(){
	
    getArcgisDataOfGrids($("#gridId").val(),$("#gridCode").val(),currentArcgisConfigInfo.mapType ,currentGridLevel);
    if(document.getElementById("buildName0").checked == true) { 
		getArcgisDataOfBuildsByCheck();
	}
	
}
var currentMapStyleObj;
//获取arcgis地图路径的配置信息
function getArcgisInfo(){
	$.ajax({   
		 url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/getArcgisInfo.json?t='+Math.random(),
		 type: 'POST',
		 timeout: 3000,
		 dataType:"json",
		 async: false,
		 error: function(data){
		 	$.messager.alert('友情提示','地图配置信息获取出现异常!','warning'); 
		 },
		 success: function(data){
		    arcgisConfigInfos=eval(data.arcgisConfigInfos);
		    var htmlStr = "";
		    for(var i=0; i<arcgisConfigInfos.length; i++){
		    	if(i==0){
		    		htmlStr += "<span class=\"current\" onclick=\"switchArcgisByNumber(this,"+i+");$(\'#jsSlider\').css(\'top\', \'10px\')\">"+arcgisConfigInfos[i].mapTypeName+"</span>"
		    	}else (
		    		htmlStr += "<span onclick=\"switchArcgisByNumber(this,"+i+");$(\'#jsSlider\').css(\'top\', \'10px\')\">"+arcgisConfigInfos[i].mapTypeName+"</span>"
		    	)
		    }
		    var mapStyleDiv = document.getElementById("mapStyleDiv2");
		    mapStyleDiv.innerHTML = htmlStr;
		    $("#mapStyleDiv2").width(60*arcgisConfigInfos.length+8)
		    
		    if(htmlStr!=""){
		    	currentMapStyleObj = mapStyleDiv.getElementsByTagName('span')[0]
		    }
		    
		    if(arcgisConfigInfos.length > 0) {
		    	switchArcgisTaijiang(arcgisConfigInfos[0],0);
		    }
		 }
	 });
}

var winType = "";//用于判断是否关闭详细窗口
var mapObjectName = "";//用于确定刷新的列表
function flashData(){
	if(winType!="" && winType=='0'){//关闭详细窗口
		closeMaxJqueryWindow();
		winType = "";
	}
	
	if(mapObjectName == "待办事件"){
		metter();
		showObjectList(mapObjectName);
	}else if(mapObjectName == "将到期"){
		metter();
		showObjectList(mapObjectName);
	}
}
</script>
</html>

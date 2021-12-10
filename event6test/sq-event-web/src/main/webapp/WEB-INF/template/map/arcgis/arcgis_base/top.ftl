<div class="local fl">
	<ul>
    	<li><img src="${uiDomain!''}/images/map/gisv0/special_config/images/local.png" /></li>
        <li><a href="javascript:void(0);" id="changeGridName">${gridName}</a></li>
    	<li><img src="${uiDomain!''}/images/map/gisv0/special_config/images/xiala.png" /></li>
    </ul>
    <div class="SelectTree AlphaTree dest" style="overflow:hidden;">
    	<iframe id="gridTree" name="gridTree" width="100%" height="99%" src="${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapTree.jhtml" marginwidth=0 marginheight=0 scrolling="yes" frameborder=0></iframe>
    </div>
</div>
<div class="MapLevel fl">
	<ul>
    	<li><img src="${uiDomain!''}/images/map/gisv0/special_config/images/cengji.png" /></li>
        <li><a id="level" href="javascript:void(0);">层&nbsp;级</a></li>
    	<li><img src="${uiDomain!''}/images/map/gisv0/special_config/images/xiala.png" /></li>
    </ul>
    <div class="SelectTree2 AlphaBack dest" style="overflow:hidden;">
    	<ul>
    		<#if DIVISIONS_LEVEL_NAME_CFG??>
    		<#list DIVISIONS_LEVEL_NAME_CFG as name>
    			<li id = "li${name_index+2}"  index="${name_index+2}"><span><input name="gridLevelName" type="checkbox" value="${name_index+2}" onclick="getArcgisDataOfGridsByLevel(${name_index+2});" id="gridLevelName${name_index+2}" /></span>显示${name}轮廓</li>
    		</#list>
    		<#else>
    		<li id = "li2"><span><input name="gridLevelName" type="checkbox" value="2" onclick="getArcgisDataOfGridsByLevel(2);" id="gridLevelName2" /></span>显示市轮廓</li>
        	<li id = "li3"><span><input name="gridLevelName" type="checkbox" value="3" onclick="getArcgisDataOfGridsByLevel(3);" id="gridLevelName3" /></span>显示县（区）轮廓</li>
        	<li id = "li4"><span><input name="gridLevelName" type="checkbox" value="4" onclick="getArcgisDataOfGridsByLevel(4);" id="gridLevelName4" /></span>显示乡镇（街道）轮廓</li>
        	<li id = "li5"><span><input name="gridLevelName" type="checkbox" value="5" onclick="getArcgisDataOfGridsByLevel(5);" id="gridLevelName5" /></span>显示村（社区）轮廓</li>
        	<li id = "li6"><span><input name="gridLevelName" type="checkbox" value="6" onclick="getArcgisDataOfGridsByLevel(6);" id="gridLevelName6" /></span>显示网格轮廓</li>
    		</#if>
        </ul>
    </div>
</div>
<div id="BuildOutlineDiv" class="Build fl hide">
	<ul>
		<li id = "liBuild0"><span><input name="buildName" type="checkbox" value="0" onclick="getArcgisDataOfBuildsByCheck();" id="buildName0" /></span>显示楼宇（民房）轮廓</li>
	</ul>
</div>
<div class="Build fl hide">
	<ul>
		<li><span><input name="buildName" type="checkbox" value="1" id="autoShowGridLevel" <#if AUTO_SHOW_GRID_LEVEL == 'true'>checked="checked"</#if>/></span>自动显示层级轮廓</li>
	</ul>
</div>
<div id="HeatMapDiv" class="Build fl hide">
	<ul>
		<li><span><input type="checkbox" value="1" onclick="optHeatMap(this.checked == true);" /></span>热力图</li>
	</ul>
</div>
<div id="_map_tip" class="Build fl hide">
	<ul>
		<li style="color:yellow;font-size:13px;"></li>
	</ul>
</div>

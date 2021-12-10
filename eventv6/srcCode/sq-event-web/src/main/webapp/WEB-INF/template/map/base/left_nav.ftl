
<div class="left-nav" style="z-index: 5">
    <div class="left-nav-item fl clearfix">
        <a href="###" class="address-sel fl mt10 ml20">
            <img src="${rc.getContextPath()}/map/base/images/icon_wl_add.png" class="fl"/>
            <p class="fl" id="changeGridName">${gridName}</p>
            <i class="fl"></i>
        </a>
        <div class="left-ztree bs SelectTree AlphaTree dest">
            <iframe id="gridTree" name="gridTree" width="100%" height="99%" src="${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapTree.jhtml" marginwidth=0 marginheight=0 scrolling="yes" frameborder=0></iframe>
        </div>
    </div>
    <div class="left-nav-item fl clearfix">
        <a href="javascript:void(0);" class="ordinary outline fl mt10 ml20">
            <img src="${rc.getContextPath()}/map/base/images/icon-outline.png" class="fl"/>
            <p class="fl" id="level">层&nbsp;级</p>
            <i class="fl"></i>
        </a>
        <ul class="outline-list bs">
           <#if DIVISIONS_LEVEL_NAME_CFG??>
               <#list DIVISIONS_LEVEL_NAME_CFG as name>
    			<li id = "li${name_index+2}"><span><input name="gridLevelName" type="checkbox" value="${name_index+2}" onclick="getArcgisDataOfGridsByLevel(${name_index+2});" id="gridLevelName${name_index+2}" /></span>显示${name}轮廓</li>
               </#list>
           <#else>
    		<li id = "li2"><span><input name="gridLevelName" type="checkbox" value="2" onclick="getArcgisDataOfGridsByLevel(2);" id="gridLevelName2" /></span>显示市轮廓</li>
        	<li id = "li3"><span><input name="gridLevelName" type="checkbox" value="3" onclick="getArcgisDataOfGridsByLevel(3);" id="gridLevelName3" /></span>显示县（区）轮廓</li>
        	<li id = "li4"><span><input name="gridLevelName" type="checkbox" value="4" onclick="getArcgisDataOfGridsByLevel(4);" id="gridLevelName4" /></span>显示乡镇（街道）轮廓</li>
        	<li id = "li5"><span><input name="gridLevelName" type="checkbox" value="5" onclick="getArcgisDataOfGridsByLevel(5);" id="gridLevelName5" /></span>显示村（社区）轮廓</li>
        	<li id = "li6"><span><input name="gridLevelName" type="checkbox" value="6" onclick="getArcgisDataOfGridsByLevel(6);" id="gridLevelName6" /></span>显示网格轮廓</li>
           </#if>
            <#--<li><a href="javascript:void(0);">显示市的轮廓</a></li>-->
            <#--<li><a href="javascript:void(0);">显示县(区)的轮廓</a></li>-->
        </ul>
    </div>
    <#if kuangxuanFlag?? && kuangxuanFlag == "yes">
    <div class="left-nav-item fl clearfix">
        <a href="javascript:void(0);" class="kuangxuan ordinary statistical fl mt10 ml20">
            <img src="${rc.getContextPath()}/map/base/images/icon-statistical.png" class="fl"/>
            <p class="fl">框选统计</p>
        </a>
    </div>
    <#--<div class="kuangxuan fr"><a href="#"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/kuangxuan.png" />框选统计</a></div>-->
    </#if>
    <div class="left-nav-item fl clearfix" id="BuildOutlineDiv">
        <div id="liBuild0">
            <span style=" float: left;margin-left: 15px;margin-top: 13px;">
                <input name="buildName" type="checkbox" value="0" onclick="getArcgisDataOfBuildsByCheck();" id="buildName0" />
            </span>
            <a href="javascript:void(0);" class="ordinary statistical fl mt10 mr10">
                <p class="fl" style="margin-left: 0px">显示楼宇（民房）轮廓</p>
            </a>
        </div>

        <#--<ul>-->
            <#--<li id = "liBuild0"><span><input name="buildName" type="checkbox" value="0" onclick="getArcgisDataOfBuildsByCheck();" id="buildName0" /></span>显示楼宇（民房）轮廓</li>-->
        <#--</ul>-->
    </div>
    <div class="left-nav-item fl clearfix">
        <a href="javascript:void(0);" class="full-screen fl mt10 ml20">
            <img src="${rc.getContextPath()}/map/base/images/icon-full-screen.png" class="fl"/>
            <p class="fl">全屏</p>
        </a>
    </div>
</div>

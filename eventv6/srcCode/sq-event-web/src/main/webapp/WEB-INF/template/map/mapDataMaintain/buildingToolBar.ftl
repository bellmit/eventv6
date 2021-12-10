
<div id="buildingMapBar" class="MapBar" style="display: none">
    <div class="con AlphaBack1" style="height:32px">


        <table width="100%" border="0" cellspacing="0" cellpadding="0" >
            <tr style="float:left;">
                <td>
                    <a href="#" onclick="saveBuildingData()" id="building_saveButton" class="NorToolBtn SmallSaveBtn" style="display:none;">保存</a>
                </td>
                <td>
                    <a href="#" onclick="chooseBuildingEditTool('drawPoint')" id="building_drawPoint" class="NorToolBtn SetCenterBtn" style="display:none;">编辑中心点</a>
                </td>
                <td>
                    <a href="#" onclick="chooseBuildingEditTool('cancleDrawPoint')" id="building_cancleDrawPoint" class="NorToolBtn BackBtn" style="display:none;">取消中心点标注</a>
                </td>

                <td>
                    <input type="hidden" id="building_mapt" value="" disabled="true" />
                    <input type="hidden" id="building_x" value="" readonly="true"/>
                    <input type="hidden" id="building_y" value="" readonly="true"/>
                    <input type="hidden" id="building_hs" value="" readonly="true"/>
                </td>
                <#--<td>-->
                    <#--<div class="MapLevel fl" style="width:120px">-->
                        <#--<ul>-->
                            <#--<li><img src="${uiDomain!''}/images/map/gisv0/special_config/images/cengji.png" onclick="buildingDisplayChange();" /></li>-->
                            <#--<li><a class="styleChangeA" onclick="buildingDisplayChange();">辅助显示</a></li>-->
                            <#--<li><img src="${uiDomain!''}/images/map/gisv0/special_config/images/xiala.png" onclick="buildingDisplayChange();" /></li>-->
                        <#--</ul>-->
                    <#--</div>-->
                <#--</td>-->
                <td>
                    <div id="building_tipMessage" style="color:yellow;font-size:13px"></div>
                    <div id="building_blind" class="blind" style="color:yellow;font-size:13px;float:right;"></div>
                </td>
            </tr>
        </table>
    </div>

    <div id="buildingDisplayDiv" class="AlphaBack" style="display:none;">
        <ul>
            <li>
                <span><input name="displayLevel0" type="checkbox" value="0" onclick="getArcgisDataOfGridsByLevel(0);" id="displayLevel0" /></span>所在网格轮廓
            </li>
        </ul>
    </div>
</div>
<script>

</script>


<div id="urbanPartsMapBar" class="MapBar" style="display: none">
    <div class="con AlphaBack1" style="height:32px">
        <input type="hidden" id="urbanParts_wid" value="" />

        <table width="100%" border="0" cellspacing="0" cellpadding="0" >
            <tr style="float:left;padding:1.5px">
                <td>
                    <a href="#" onclick="saveUrbanPartsData()" id="saveUrbanPartsButton" class="NorToolBtn SmallSaveBtn" style="display:none;">保存</a>
                </td>
                <td>
                    <a href="#" onclick="chooseUrbanPartsEditTool('drawUrbanPartsPoint')" id="drawUrbanPartsPoint" class="NorToolBtn SetCenterBtn">编辑定位点</a>
                </td>
                <td>
                    <a href="#" onclick="chooseUrbanPartsEditTool('cancleDrawPoint')" id="cancleDrawUrbanPartsPoint" class="NorToolBtn BackBtn" style="display:none;">取消定位点标注</a>
                </td>
                <td>
                    <a href="#" onclick="chooseUrbanPartsEditTool('edit')" id="editUrbanParts" class="NorToolBtn BackBtn" style="display:none;">编辑</a>
                </td>
                <td>
                    <input type="hidden" id="urbanParts_mapt" value="" disabled="true" />
                    <input type="hidden" id="urbanParts_x" value="" readonly="true"/>
                    <input type="hidden" id="urbanParts_y" value="" readonly="true"/>
                    <input type="hidden" id="urbanParts_typeCode" value="" readonly="true"/>
                    <input type="hidden" id="urbanParts_typeId" value="" readonly="true"/>
                </td>
                <td>
                    <div id="urbanParts_tipMessage" style="color:yellow;font-size:13px"></div>
                    <div id="urbanParts_blind" class="blind" style="color:yellow;font-size:13px;float:right;"></div>
                </td>
            </tr>
        </table>
    </div>


</div>

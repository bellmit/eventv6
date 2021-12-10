
<div id="gridMapBar" class="MapBar" style="display: none">
    <div class="con AlphaBack1" style="height:32px">
        <input type="hidden" id="grid_wid" value="" />

        <table width="100%" border="0" cellspacing="0" cellpadding="0" >
            <tr style="float:left;padding:1.5px">
                <td>
                    <a href="#" onclick="saveGridData()" id="saveGridButton" class="NorToolBtn SmallSaveBtn" style="display:none;">保存</a>
                </td>
                <td>
                    <a href="#" onclick="chooseGridEditTool('drawGridPoint')" id="drawGridPoint" class="NorToolBtn SetCenterBtn">编辑中心点</a>
                </td>
                <td>
                    <a href="#" onclick="chooseGridEditTool('cancleDrawPoint')" id="cancleDrawGridPoint" class="NorToolBtn BackBtn" style="display:none;">取消中心点标注</a>
                </td>
                <td>
                    <a href="#" onclick="chooseGridEditTool('drawGrid')" id="drawGrid" class="NorToolBtn DrawBorderBtn">轮廓编辑</a>
                </td>
                <td>
                    <a href="#" onclick="chooseGridEditTool('cancleDrawOrEditGrid')" id="cancleDrawOrEditGrid" class="NorToolBtn BackBtn" style="display:none;">取消轮廓编辑</a>
                </td>
                <td>
                    <a href="#" onclick="chooseGridEditTool('delDrawGridData')" id="delDrawGridData" class="NorToolBtn DrawBorderBtn" style="display:none;">删除轮廓</a>
                </td>
                <td>
                    <input type="hidden" id="grid_mapt" value="" disabled="true" />
                    <input type="hidden" id="grid_x" value="" readonly="true"/>
                    <input type="hidden" id="grid_y" value="" readonly="true"/>
                    <input type="hidden" id="grid_hs" value="" readonly="true"/>
                </td>
                <td>
                    <div id="grid_tipMessage" style="color:yellow;font-size:13px"></div>
                    <div id="grid_blind" class="blind" style="color:yellow;font-size:13px;float:right;"></div>
                </td>
            </tr>
        </table>
    </div>


</div>

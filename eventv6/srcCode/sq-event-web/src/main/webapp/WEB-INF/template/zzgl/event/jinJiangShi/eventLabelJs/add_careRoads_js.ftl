<script type="text/javascript">
    var ID_CARD_TYPE = "1";//身份证证件类型

    function initItem4CareRoads() {

        AnoleApi.initListComboBox("careRoads_natureName", "careRoads_nature", "B040", null, [<#if careRoads?? && careRoads.nature??>"${careRoads.nature}"<#else>"1"</#if>]);

        <#if careRoads?? && careRoads.suspectList?? && (careRoads.suspectList?size > 0)>
            showMajorSuspectPeople();//若被嫌疑人存在，展示嫌疑人信息
        </#if>

        zzForm.render('checkbox','careRoadsIsDetection');
        zzForm.on('checkbox(_careRoadsIsDetection)', function (data) {
            if(data.checked){
                $(this).val('1');
            }else {
                $(this).val('0');
            }
        });
    }
    

    function fetchCareRoadsPersonInfo() {//构建人员信息
    	var result=true;
        var reportedObjArray = [],
            reportedObj = {},
            inpVal = "",
            inpId = "";

        //构建命案防控嫌疑人员/受害人人员信息
        $('#majorSuspectInfoDiv table[id=majorSuspectInfo_list] > tbody tr').each(function() {
            reportedObj = {};

            $(this).find('input').each(function() {
                inpVal = $(this).val();
                inpId = $(this).attr('id');

                if(inpVal != undefined) {
                    reportedObj[inpId.substr(0, inpId.indexOf('_'))] = inpVal;
                }
            });

            reportedObjArray.push(reportedObj);
        });

        if(reportedObjArray.length > 0) {
            $("#careRoads_person").val(JSON.stringify(reportedObjArray));
        }else{
        	result = false;
        }
        
        return result;
    }

    /*var resumeRec = 0;
    var flag8 = 1;
    //peopleType:人员类型 03 嫌疑人 04 受害人
    var peopleType = '';*/
    //若嫌疑人存在，展示嫌疑人信息
    function showMajorSuspectPeople() {
        <#if careRoads?? && careRoads.suspectList?? && (careRoads.suspectList?size > 0)>
        <#list careRoads.suspectList as list>
        var str = "";
        str = "<tr>" +
            "<td style=\"display:none;\">" +
            '<input id=\"bizType_' + resumeRec + '\" name=\"bizType_' + resumeRec + '\" value=\"${list.bizType}\" class=\"inp1 InpDisable\"/>' +
            "</td>" +
            "<td style=\"display:none;\">" +
            '<input id=\"partyId_\" name=\"partyId_\" value=\"${list.partyId}\" class=\"inp1 InpDisable\"/>'+
            "</td>" +
            "<td style='width:144px'>" +
            '<input type=\"hidden\" id=\"name_suspect\" name=\"name_suspect\"  readonly=\"readonly\"  data-options=\"tipPosition:\'bottom\',validType:[\'maxLength[80]\',\'characterCheck\']\" class=\"inp1 easyui-validatebox requestParam\" style=\"width:120px;\" value=\"${list.name}\" />' +
            '<div class="Check_Radio FontDarkBlue" style="width: 86%;"><a onclick="detailPartyIndividual(<#if list.partyId??>${list.partyId!}</#if>)" style="cursor: pointer"><#if list.name??>${list.name!}</#if></a></div>' +
            "</td>" +
            "<td style='width:145px'>" +
            '<input type=\"hidden\" id=\"cardType_suspect' + resumeRec + '\" value=\"${list.cardType?string}\"/>' +
            '<input type=\"hidden\" id=\"cardTypeName_suspect' + resumeRec + '\" name=\"cardTypeName_suspect' + resumeRec + '\" data-options=\"tipPosition:\'bottom\'\" class=\"inp1 easyui-validatebox requestParam\" style=\"width:90px;\"  />' +
            '<div class="Check_Radio FontDarkBlue" style="width: 86%;"><#if list.cardTypeName??>${list.cardTypeName!}</#if></div>' +
            "</td>" +
            "<td style='width:239px'>" +
            '<input type=\"hidden\" id=\"idCard_suspect\" name=\"idCard_suspect\" readonly=\"readonly\" class=\"inp1 easyui-validatebox requestParam\" style=\"width: 130px;\" data-options=\"tipPosition:\'bottom\',validType:[\'maxLength[24]\',\'characterCheck\']\" value=\"${list.idCard}" />' +
            '<div class="Check_Radio FontDarkBlue" style="width: 86%;"><#if list.idCard??>${list.idCard!}</#if></div>' +
            "</td>" +
            "<td style='width:144px'>" +
            '<input type=\"hidden\" id=\"tel_suspect\" name=\"tel_suspect\"  readonly=\"readonly\"  data-options=\"tipPosition:\'bottom\',validType:[\'maxLength[32]\',\'characterCheck\']\" class=\"inp1 easyui-validatebox\" style=\"width:120px;\" value=\"${list.tel} \" />' +
            '<div class="Check_Radio FontDarkBlue" style="width: 86%;"><#if list.tel??>${list.tel!}</#if></div>' +
            "</td>" +
            "<td style='width:100px'>" +
            '<input type=\"hidden\" id=\"sex_suspect' + resumeRec + '\" value=\"${list.sex}\"/>' +
            '<input type=\"hidden\" id=\"sexName_suspect' + resumeRec + '\" name=\"sexName_suspect' + resumeRec + '\" class=\"inp1 easyui-validatebox requestParam\" style=\"width: 80px;height:30px;\" data-options=\"tipPosition:\'bottom\'\" />' +
            '<div class="Check_Radio FontDarkBlue" style="width: 86%;"><#if list.sexName??>${list.sexName!}</#if></div>' +
            "</td>" +
            "<td style='width:92px;'>" +
            "<a href='###' class='del-tr bg-gray' style='width:50px;' onclick='delRec(this);'><i class='icon-del'></i>删 除</a>" +
            "</td>" +
            "</tr>";

        $("#majorSuspectInfo_list").append(str);

        /*AnoleApi.initListComboBox('cardTypeName_suspect' + resumeRec, 'cardType_suspect' + resumeRec, "D030001", function() {checkCardType($('#cardType_' + resumeRec));}, [obj.certType]);*/
        var cardTypeObj = AnoleApi.initListComboBox('cardTypeName_suspect' + resumeRec, 'cardType_suspect' + resumeRec, "D030001", null, ['${list.cardType}']);
        var genderObj = AnoleApi.initListComboBox('sexName_suspect' + resumeRec, 'sex_suspect' + resumeRec, "D060002", null, ['${list.sex}']);
        cardTypeObj.setDisabled(true);
        genderObj.setDisabled(true);

        resumeRec++;
        </#list>
        </#if>
    }
</script>